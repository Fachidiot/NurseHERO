package com.fachidiot.nursehro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.fachidiot.nursehro.Adapter.TagAdapter
import com.fachidiot.nursehro.Class.UserList
import com.google.android.flexbox.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_main_userprofileview.*

class UserProfileActivity : AppCompatActivity() {
    private lateinit var mFirebaseStorage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_main_userprofileview)
        mFirebaseStorage = FirebaseStorage.getInstance()

        setInfo()

        button_back.setOnClickListener {
            finish()
        }
    }

    private fun setInfo() {
        // TODO : get user Info and set data
        val userInfo = intent.getParcelableExtra<UserList>("userinfo")
        if (userInfo != null) {
            if (userInfo.profileImage != "null")
            {
                val storageRef: StorageReference = mFirebaseStorage.reference
                storageRef.child("userprofileImages/uid/${userInfo.profileImage}").downloadUrl.addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        this.let {
                            Glide.with(baseContext)
                                .load(task.result)
                                .into(UserProfile_image)
                        }
                    }
                }

            }
            else{
                UserProfile_image.setImageResource(R.drawable.icon_nurse)
            }
        }
        UserName.text = userInfo?.userNickname
        Ranked.text = userInfo?.age.toString()
        Whished.text = userInfo?.age.toString()
        Likes.text = userInfo?.age.toString()

        val taglist : ArrayList<String> = ArrayList()
        if (userInfo != null) {
            taglist.add(userInfo.location.toString())
            taglist.add("${userInfo.age.toString()}세")
            if (userInfo.sex == true)
                taglist.add("남자")
            else
                taglist.add("여자")
        }

        FlexboxLayoutManager(this).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }.let {
            tagLayout.layoutManager = it
            tagLayout.adapter = TagAdapter(taglist)
        }
    }
}