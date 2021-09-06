package com.fachidiot.nursehro.Class

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.ClusterManager.OnClusterClickListener
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator


@SuppressLint("InflateParams")
class MarkerClusterRenderer(context: Context, private val googleMap: GoogleMap, clusterManager: ClusterManager<LatLngUser?>) :
    DefaultClusterRenderer<LatLngUser>(context, googleMap, clusterManager), OnClusterClickListener<LatLngUser?>,
    OnInfoWindowClickListener {
    private val layoutInflater: LayoutInflater
    private val clusterIconGenerator: IconGenerator
    private val clusterItemView: View
    override fun onBeforeClusterItemRendered(item: LatLngUser, markerOptions: MarkerOptions) {
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        markerOptions.title(item.getTitle())
    }

    override fun onBeforeClusterRendered(cluster: Cluster<LatLngUser>, markerOptions: MarkerOptions) {
        val singleClusterMarkerSizeTextView =
            clusterItemView.findViewById<TextView>(R.id.singleClusterMarkerSizeTextView)
        singleClusterMarkerSizeTextView.text = cluster.getSize().toString()
        val icon = clusterIconGenerator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    override fun onClusterItemRendered(clusterItem: LatLngUser, marker: Marker) {
        marker.tag = clusterItem
    }

    override fun onClusterClick(cluster: Cluster<LatLngUser?>): Boolean {
        val builder = LatLngBounds.Builder()
        for (user in cluster.items) builder.include(user.getPosition())
        val bounds = builder.build()
        try {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    override fun onInfoWindowClick(marker: Marker) {
        val context = clusterItemView.context
        val user: LatLngUser = marker.tag as LatLngUser //  handle the clicked marker object
        if (context != null) Toast.makeText(context, user.getTitle(), Toast.LENGTH_SHORT).show()
    }

    private inner class MyCustomClusterItemInfoView internal constructor() : InfoWindowAdapter {
        private val clusterItemView: View
        override fun getInfoWindow(marker: Marker): View {
            val user: LatLngUser = marker.tag as LatLngUser ?: return clusterItemView
            val itemNameTextView = clusterItemView.findViewById<TextView>(R.id.itemNameTextView)
            val itemAddressTextView = clusterItemView.findViewById<TextView>(R.id.itemAddressTextView)
            itemNameTextView.text = marker.title
            itemAddressTextView.setText(user.getAddress())
            return clusterItemView
        }

        override fun getInfoContents(marker: Marker): View {
            return null
        }

        init {
            clusterItemView = layoutInflater.inflate(R.layout.marker_info_window, null)
        }
    }

    init {
        layoutInflater = LayoutInflater.from(context)
        clusterItemView = layoutInflater.inflate(R.layout.custom_marker, null)
        clusterIconGenerator = IconGenerator(context)
        val drawable = ContextCompat.getDrawable(context, R.color.transparent)
        clusterIconGenerator.setBackground(drawable)
        clusterIconGenerator.setContentView(clusterItemView)
        clusterManager.setOnClusterClickListener(this)
        googleMap.setInfoWindowAdapter(clusterManager.markerManager)
        googleMap.setOnInfoWindowClickListener(this)
        clusterManager.markerCollection.setOnInfoWindowAdapter(MyCustomClusterItemInfoView())
        googleMap.setOnCameraIdleListener(clusterManager)
        googleMap.setOnMarkerClickListener(clusterManager)
    }
}
