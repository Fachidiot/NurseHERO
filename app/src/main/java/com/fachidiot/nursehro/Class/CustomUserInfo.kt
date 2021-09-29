package com.fachidiot.nursehro.Class

import com.google.firebase.firestore.GeoPoint

data class CustomUserInfo(
    // 사용자 기본정보
    var nurse: Boolean = false,
    var userNickname: String = "nickname", // 사용자 이름(닉네임)
    var userFirstname: String = "firstname", // 사용자 이름(성)
    var userLastname: String = "lastname", // 사용자 이름(이)
    var profileImage: String = "", // 사용자 프로필사진
    var location: String = "",
    var latLng: GeoPoint = GeoPoint(0.0, 0.0),
    var sex: Boolean = false, // 성별
    var age: Int = 0, // 나이
    val uid: String = "uid"
)