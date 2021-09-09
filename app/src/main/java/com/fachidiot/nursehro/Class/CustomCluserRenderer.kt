package com.fachidiot.nursehro.Class

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fachidiot.nursehro.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer


class CustomCluserRenderer(context: Context?, map: GoogleMap?, clusterManager: ClusterManager<LatLngUser>?,
                           marker_view: View) : DefaultClusterRenderer<LatLngUser>(context, map, clusterManager) {
    private val context = context
    private var marker_view = marker_view
    private lateinit var tag_marker: TextView

    override fun onBeforeClusterItemRendered(item: LatLngUser, markerOptions: MarkerOptions) { // 5
        tag_marker = marker_view.findViewById(R.id.tv_marker_body) as TextView
        //tag_marker.text = MediaStore_Dao.getNameById(context!!, item.id)
        //marker_body.text = user.location
        //marker_num.text = profileList.count().toString()
        val markerOptions = MarkerOptions()
        markerOptions.icon( BitmapDescriptorFactory.fromBitmap(context?.let { createDrawableFromView(it, marker_view) }) )
    }

    override fun onClustersChanged(clusters: MutableSet<out Cluster<LatLngUser>>?) {
        super.onClustersChanged(clusters)

        //if(selectedMarker != null) {
        //    selectedMarker!!.setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context, marker_view)))
        //    selectedMarker = null
        //}
    }

    companion object {

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
    }
}