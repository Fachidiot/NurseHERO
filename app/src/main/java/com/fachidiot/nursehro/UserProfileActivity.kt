package com.fachidiot.nursehro

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fachidiot.nursehro.Class.UserList
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)


        button_back.setOnClickListener {
            finish()
        }

    }

    private fun setInfo() {
        // TODO : get user Info and set data
        val userInfo = intent.getParcelableExtra<UserList>("userinfo")

        val drawable : Drawable = resources.getDrawable(R.drawable.icon_nurse)
        UserProfile_image.setImageDrawable(drawable)
        UserName.text = userInfo?.userNickname
        Ranked.text = userInfo?.age.toString()
        Whished.text = userInfo?.location
        Likes.text = userInfo?.profileImage
    }
}