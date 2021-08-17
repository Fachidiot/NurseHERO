package com.fachidiot.nursehro.Class

data class UserInfo(
    // 사용자 기본정보
    var nurse: Boolean,
    var userNickname: String, // 사용자 이름(닉네임)
    var userFirstname: String, // 사용자 이름(성)
    var userLastname: String, // 사용자 이름(이)
    var profileImage: String? = null, // 사용자 프로필사진
    var location: String, // Tagging
    var sex: Boolean? = null, // 성별
    //var age: Int? = null, // 나이
    val uid: String
)