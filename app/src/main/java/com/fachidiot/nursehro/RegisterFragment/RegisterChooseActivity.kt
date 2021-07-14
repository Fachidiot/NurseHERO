package com.fachidiot.nursehro.RegisterFragment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.fachidiot.nursehro.LoginActivity
import com.fachidiot.nursehro.R
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register_choose.*
import kotlinx.android.synthetic.main.activity_register_choose.button_back

class RegisterChooseActivity : AppCompatActivity() {
    var nurse : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_choose)

        RelativeLayout_Next.setOnClickListener {
            checkChoice()
        }

        button_back.setOnClickListener {
            finish()
        }

        CheckBox_Nurse.setOnClickListener {
            CheckBox_Normal.isChecked = false
            nurse = true
        }

        CheckBox_Normal.setOnClickListener {
            CheckBox_Nurse.isChecked = false
            nurse = false
        }
    }

    private fun checkChoice() {
        if (CheckBox_Normal.isChecked || CheckBox_Nurse.isChecked) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("nurse", nurse)
            startActivity(intent)
        }
        else {
            Toast.makeText(this, "You have to choose position", Toast.LENGTH_SHORT).show()
        }
    }
}