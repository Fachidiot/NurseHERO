package com.fachidiot.nursehro.MainFragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fachidiot.nursehro.Class.CustomUserInfo
import com.fachidiot.nursehro.Class.LatLngUser
import com.fachidiot.nursehro.Class.UserList
import com.fachidiot.nursehro.MapList_Adapter
import com.fachidiot.nursehro.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.maps.android.clustering.ClusterManager
import kotlinx.android.synthetic.main.fragment_main_find.*
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentMainFind : Fragment(), OnMapReadyCallback {

    private var profileList : ArrayList<UserList> = ArrayList()

    private lateinit var mFirebaseAuth : FirebaseAuth
    private lateinit var mFirebaseStorage: FirebaseStorage
    private lateinit var mFirebaseStoreDatabase: FirebaseFirestore

    private lateinit var mMap : GoogleMap
    private var mapView : MapView? = null
    private var locationPermissionGranted = false
    private val defaultLocation = LatLng(37.557667, 126.926546)
    private var lastKnownLocation: Location? = null

    private lateinit var clusterManager: ClusterManager<LatLngUser>

    private var likelyPlaceNames: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAddresses: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAttributions: Array<List<*>?> = arrayOfNulls(0)
    private var likelyPlaceLatLngs: Array<LatLng?> = arrayOfNulls(0)

    private lateinit var markerRootView : View
    private lateinit var marker_body : TextView
    private lateinit var marker_num : TextView

    private lateinit var placesClient : PlacesClient
    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient

    private var param1: String? = null
    private var param2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Places.initialize(requireContext(), getString(R.string.maps_api_key))
        placesClient = Places.createClient(requireContext())
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        mFirebaseStoreDatabase = Firebase.firestore
        mFirebaseStorage = FirebaseStorage.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setAddressMarker()

        ListButton.setOnClickListener {
            Map_ListLayout.visibility = View.VISIBLE
            setUserList()
        }

        button_back.setOnClickListener {
            Map_ListLayout.visibility = View.INVISIBLE
            // Delete UserList
        }

        searchButton.setOnClickListener {
            if (searchEditText.text.isEmpty())
                return@setOnClickListener
            // 검색창에서 텍스트를 가져온다
            val searchText: String = searchEditText.text.toString()
            val geocoder = Geocoder(context)
            var addresses: List<Address?>? = null
            try {

                addresses = geocoder.getFromLocationName(searchText, 3)
                if (addresses != null) {
                    search(addresses)
                }
            } catch (e: Exception) {
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_main_find, container, false)
        mapView = rootView.findViewById(R.id.map) as MapView
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync(this)
        return rootView
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val Seoul = LatLng(37.557667, 126.926546)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(Seoul))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))

        setCustomMarkerView()

        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        // Prompt the user for permission.
        getLocationPermission()
        // [END_EXCLUDE]

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()
    }

    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))

                        }
                    } else {
                        Log.d("Map/GetDeviceLocation", "Current location is null. Using defaults.")
                        Log.e("Map/GetDeviceLocation", "Exception: %s", task.exception)
                        mMap.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))

                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    // 구글맵 주소 검색 메서드 근처 위치의 간호사 유저 정보를 가져와야함
    private fun search(addresses: List<Address>) {
        val address: Address = addresses[0]
        val latLng = LatLng(address.latitude, address.longitude)

        Toast.makeText(context, "슝 ~", Toast.LENGTH_SHORT).show()
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f))
    }

    private fun setAddressMarker() {
        mFirebaseStoreDatabase.collection("users").whereEqualTo("nurse",  true).orderBy("location").get().addOnSuccessListener {
            for (dc in it.documents) {
                val user = dc.toObject(CustomUserInfo::class.java)
                if (user != null) {
                    val geocoder: Geocoder = Geocoder(requireContext(), Locale.getDefault())

                    val location = geocoder.getFromLocationName(user.location, 3)
                    val latLng = LatLng(location[0].latitude, location[0].longitude)

                    Log.d("setAddressMarker", user.location.toString())
                    val markerOptions = MarkerOptions()
                    markerOptions.position(latLng)
                    marker_body.text = user.location
                    marker_num.text = profileList.count().toString()
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(requireContext(), markerRootView)))
                    mMap.addMarker(markerOptions)
                }
            }
        }
    }

    // 현위치의 리스트
    private fun onGetUserList(location : String, latLng : LatLng? = null) {
        profileList.clear()
        mFirebaseStoreDatabase.collection("users").whereEqualTo("nurse",  true).whereArrayContains("location", location).get()
            .addOnSuccessListener{
                for (dc in it.documents) {
                    val user = dc.toObject(CustomUserInfo::class.java)
                    if (user != null) {
                        profileList.add(UserList(user.userNickname, user.profileImage, user.location, user.sex, user.age))
                    }
                }
            }
    }

    private fun setCustomMarkerView() {
        markerRootView = LayoutInflater.from(context).inflate(R.layout.custom_marker, null)
        marker_body = markerRootView.findViewById(R.id.tv_marker_body) as TextView
        marker_num = markerRootView.findViewById(R.id.tv_marker_num) as TextView
    }



    private fun setUpClusterer() {
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        clusterManager = ClusterManager(context, mMap)

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(clusterManager)
        mMap.setOnMarkerClickListener(clusterManager)

        // Add cluster items (markers) to the cluster manager.
        addItems()
    }

    private fun addItems() {

        // Set some lat/lng coordinates to start with.
        var lat = 51.5145160
        var lng = -0.1270060

        // Add ten cluster items in close proximity, for purposes of this example.
        for (i in 0..9) {
            val offset = i / 60.0
            lat += offset
            lng += offset
            val offsetItem =
                    LatLngUser(lat, lng, "Title $i", "Snippet $i")
            clusterManager.addItem(offsetItem)
        }
    }


    // View를 Bitmap으로 변환
    private fun createDrawableFromView(context: Context, view: View): Bitmap? {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun setUserList() {
        MapRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) //레이아웃매니저를 이용해 어뎁터의 방향을 결정
        MapRecyclerView.setHasFixedSize(true)//어뎁터에 성능을 위한것
        MapRecyclerView.adapter = MapList_Adapter(profileList) //어뎁터에 리스트 자료를 넣는다.
    }


    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onLowMemory()
    }


    //TODO : PermissionCheck
    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
        }
        updateLocationUI()
    }

    private fun updateLocationUI() {
        try {
            if (locationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isZoomControlsEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isZoomControlsEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }



    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private const val DEFAULT_ZOOM = 15
        private const val M_MAX_ENTRIES = 5

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentMainFind().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}