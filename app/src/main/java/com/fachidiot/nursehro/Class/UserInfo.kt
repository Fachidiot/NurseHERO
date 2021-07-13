package com.fachidiot.nursehro.Class

data class UserInfo (
    // 사용자 기본정보
    var nurse : Boolean? = false,
    var userName : String? = null, // 사용자 이름(닉네임)
    var profileImageUrl : String? = null, // 사용자 프로필사진
    var useruid : String? = null // 사용자 프로필사진
)