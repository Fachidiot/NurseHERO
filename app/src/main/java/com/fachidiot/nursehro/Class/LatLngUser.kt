package com.fachidiot.nursehro.Class

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class LatLngUser (
    val uid : String,
    val latLng: LatLng,
    val location: String
) : ClusterItem {
    override fun getPosition(): LatLng {
        return latLng
    }

    override fun getTitle(): String? {
        return null
    }

    override fun getSnippet(): String? {
        return null
    }
}
