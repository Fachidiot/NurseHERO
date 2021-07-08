package com.fachidiot.nursehro.RegisterFragment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fachidiot.nursehro.LoginActivity
import com.fachidiot.nursehro.R
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register_choose.*
import kotlinx.android.synthetic.main.activity_register_choose.button_back

class RegisterChooseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_choose)

        RelativeLayout_Next.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
        }

        button_back.setOnClickListener {
            finish()
        }

        CheckBox_Nurse.setOnClickListener {
            CheckBox_Normal.isChecked = false
        }

        CheckBox_Normal.setOnClickListener {
            CheckBox_Nurse.isChecked = false
        }
    }

    private fun checkChoice() {

    }
}