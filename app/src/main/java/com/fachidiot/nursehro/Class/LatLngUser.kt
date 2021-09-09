package com.fachidiot.nursehro.Class

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class LatLngUser (
    val uid : String,
    val latlng: LatLng,
    val location: String
) : ClusterItem {
    override fun getPosition(): LatLng {
        return latlng
    }
}
