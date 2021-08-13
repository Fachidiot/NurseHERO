package com.fachidiot.nursehro.MainFragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fachidiot.nursehro.Class.UserList
import com.fachidiot.nursehro.MapList_Adapter
import com.fachidiot.nursehro.R
import com.fachidiot.nursehro.databinding.ActivityMainBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_main_find.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentMainFind : Fragment(), OnMapReadyCallback {
    private val permission_list = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var mMap : GoogleMap
    private var mapView : MapView? = null

    private var param1: String? = null
    private var param2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val profileList = arrayListOf(
            UserList(R.drawable.icon_nurse, "서울/강남구/봉천동", 38, false, "김연자", "adsf02302jif2.png"),
            UserList(R.drawable.icon_nurse, "서울/성동구/응봉동", 41, false, "김복덩", "adsf02302jif2.png"),
            UserList(R.drawable.icon_nurse, "서울/강남구/응암동", 52, false, "김닥터", "adsf02302jif2.png"),
            UserList(R.drawable.icon_nurse, "경기/시골구/시골동", 32, true, "김간호사", "adsf02302jif2.png"),
            UserList(R.drawable.icon_nurse, "부산/해운구/해운동", 26, false, "김연자", "adsf02302jif2.png"),
            UserList(R.drawable.icon_nurse, "대전/대전구/대전동", 39, false, "김연자", "adsf02302jif2.png"),
            UserList(R.drawable.icon_nurse, "서울/해남구/해남동", 23, true, "김남자", "adsf02302jif2.png"),
            UserList(R.drawable.icon_nurse, "군산/군산구/군산동", 43, false, "김연자", "adsf02302jif2.png"),
            UserList(R.drawable.icon_nurse, "미국/캘리포니아/양키동", 46, false, "김연자", "adsf02302jif2.png"),
            UserList(R.drawable.icon_nurse, "중국/우한/코로나", 45, false, "김연자", "adsf02302jif2.png"),
            UserList(R.drawable.icon_nurse, "일본/도쿄/나가사키", 41, false, "김연자", "adsf02302jif2.png"),
        )
        MapRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) //레이아웃매니저를 이용해 어뎁터의 방향을 결정
        MapRecyclerView.setHasFixedSize(true)//어뎁터에 성능을 위한것
        MapRecyclerView.adapter = MapList_Adapter(profileList) //어뎁터에 리스트 자료를 넣는다.

        ListButton.setOnClickListener {
            Map_ListLayout.visibility = View.VISIBLE
        }

        button_back.setOnClickListener {
            Map_ListLayout.visibility = View.INVISIBLE
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
        var rootView = inflater.inflate(R.layout.fragment_main_find, container, false)
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
        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true
    }

    // 구글맵 주소 검색 메서드
    private fun search(addresses: List<Address>) {
        val address: Address = addresses[0]
        val latLng = LatLng(address.latitude, address.longitude)
        val addressText = java.lang.String.format(
            "%s, %s",
            if (address.maxAddressLineIndex > 0) address
                .getAddressLine(0) else " ", address.featureName
        )
        //locationText.visibility = View.VISIBLE
        //locationText.text = """
        //        Latitude${address.latitude.toString()}Longitude${address.longitude.toString()}
        //        $addressText
        //        """.trimIndent()
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title(addressText)
        mMap.clear()
        mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f))
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

    //private fun getDeviceLocation() {
    //    /*
    //     * Get the best and most recent location of the device, which may be null in rare
    //     * cases when a location is not available.
    //     */
    //    try {
    //        if (locationPermissionGranted) {
    //            val locationResult = fusedLocationProviderClient.lastLocation
    //            locationResult.addOnCompleteListener(this) { task ->
    //                if (task.isSuccessful) {
    //                    // Set the map's camera position to the current location of the device.
    //                    lastKnownLocation = task.result
    //                    if (lastKnownLocation != null) {
    //                        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(
    //                            LatLng(lastKnownLocation!!.latitude,
    //                                lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
    //                    }
    //                } else {
    //                    Log.d(this.toString(), "Current location is null. Using defaults.")
    //                    Log.e(this.toString(), "Exception: %s", task.exception)
    //                    mMap?.moveCamera(CameraUpdateFactory
    //                        .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
    //                    mMap?.uiSettings?.isMyLocationButtonEnabled = false
    //                }
    //            }
    //        }
    //    } catch (e: SecurityException) {
    //        Log.e("Exception: %s", e.message, e)
    //    }
    //}

    // Permission이 없을시에 확인 해야 한다.
    private fun onPermissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission_list, 0)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }



    companion object {
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