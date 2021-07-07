package com.fachidiot.nursehro.RegisterFragment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fachidiot.nursehro.LoginActivity
import com.fachidiot.nursehro.R
import kotlinx.android.synthetic.main.activity_login.*

class RegisterSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_success)

        RelativeLayout_Login.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}