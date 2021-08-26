package com.fachidiot.nursehro.Class

class UserList(
    // 사용자 리사이클러뷰 정보
    var userNickname: String = "nickname", // 사용자 이름(닉네임)
    var profileImage: String? = null, // 사용자 프로필사진
    var location: String? = null, // Tagging
    var sex: Boolean? = null, // 성별
    var age: Int? = null, // 나이
)