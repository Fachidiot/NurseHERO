package com.fachidiot.nursehro.Class

data class CustomUserInfo(
    // 사용자 기본정보
    var nurse: Boolean = false,
    var userNickname: String = "nickname", // 사용자 이름(닉네임)
    var userFirstname: String = "firstname", // 사용자 이름(성)
    var userLastname: String = "lastname", // 사용자 이름(이)
    var profileImage: String? = null, // 사용자 프로필사진
    var location: String? = null, // Tagging
    var sex: Boolean? = null, // 성별
    var age: Int? = null, // 나이
    val uid: String = "uid"
    //val rate : Int = 0
    //val whished : Int = 0
    //val like : Int = 0
    //val Introduce : String = ""
)