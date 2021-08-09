package com.fachidiot.nursehro

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        val drawable : Drawable = getResources().getDrawable(R.drawable.icon_nurse)
        UserProfile_image.setImageDrawable(drawable)
        UserName.text = intent.getStringExtra("userName")
        Ranked.text = intent.getStringExtra("ranked")
        Whished.text = intent.getStringExtra("whished")
        Likes.text = intent.getStringExtra("likes")
    }
}