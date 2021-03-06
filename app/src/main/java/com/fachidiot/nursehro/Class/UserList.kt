package com.fachidiot.nursehro.Class

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserList(
    // 사용자 리사이클러뷰 정보
    var userNickname: String? = "nickname", // 사용자 이름(닉네임)
    var profileImage: String? = null, // 사용자 프로필사진
    var location: String? = null, // Tagging
    var sex: Boolean? = null, // 성별
    var age: Int? = null, // 나이
) : Parcelable