package com.fachidiot.nursehro.Class

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.fachidiot.nursehro.R
import com.fachidiot.nursehro.UserProfileActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class CustomCluserRenderer(context: Context, map: GoogleMap?, clusterManager: ClusterManager<LatLngUser>?,
                           marker_view: View) : DefaultClusterRenderer<LatLngUser>(context, map, clusterManager) {
    private val context = context
    private var marker_view = marker_view

    override fun onBeforeClusterItemRendered(item: LatLngUser, markerOptions: MarkerOptions) {
        markerOptions.position(item.latLng)
        val markerbody = marker_view.findViewById(R.id.tv_marker_body) as TextView
        val markernum = marker_view.findViewById(R.id.tv_marker_num) as TextView
        markerbody.text = item.userNickname
        markernum.text = "1"
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context, marker_view)))
    }

    override fun onBeforeClusterRendered(
        cluster: Cluster<LatLngUser?>,
        markerOptions: MarkerOptions
    ) {
        // Draw multiple people.
        // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
        val markerbody = marker_view.findViewById(R.id.tv_marker_body) as TextView
        val markernum = marker_view.findViewById(R.id.tv_marker_num) as TextView

        var location = ""
        for (p in cluster.items) {
            location = p!!.location

        }
        markerbody.text = location
        markernum.text = cluster.size.toString()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context, marker_view)))
    }

    override fun onClusterUpdated(cluster: Cluster<LatLngUser?>, marker: Marker) {
        val markerbody = marker_view.findViewById(R.id.tv_marker_body) as TextView
        val markernum = marker_view.findViewById(R.id.tv_marker_num) as TextView

        var location = ""
        for (p in cluster.items) {
            //if (location == )
            //location = p!!.location
            location = p!!.location
        }
        markerbody.text = location
        markernum.text = cluster.size.toString()
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context, marker_view)))
    }

    override fun shouldRenderAsCluster(cluster: Cluster<LatLngUser?>): Boolean {
        // Always render clusters.
        return cluster.size > 1
    }

    companion object {

        private fun createDrawableFromView(context: Context, view: View): Bitmap {
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
    }
}