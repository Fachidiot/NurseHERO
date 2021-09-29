package com.fachidiot.nursehro.Class

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class LatLngUser (
    var userNickname: String,
    val latLng: LatLng,
    val location: String,
    val uid: String = "uid"
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
