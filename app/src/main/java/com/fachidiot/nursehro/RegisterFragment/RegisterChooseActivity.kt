package com.fachidiot.nursehro.RegisterFragment

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.fachidiot.nursehro.R
import kotlinx.android.synthetic.main.activity_register_choose.*
import kotlinx.android.synthetic.main.activity_register_choose.button_back

class RegisterChooseActivity : AppCompatActivity() {
    private var nurse : Boolean = false
    private var sex : Boolean = true
    private var birth : Boolean = false
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

        CheckBox_Man.setOnClickListener {
            CheckBox_Woman.isChecked = false
            sex = true
        }

        CheckBox_Woman.setOnClickListener {
            CheckBox_Man.isChecked = false
            sex = false
        }

        vDateEnter.setOnClickListener {
            DatePickLayout.visibility = View.VISIBLE
        }

        Enter.setOnClickListener {
            DatePickLayout.visibility = View.INVISIBLE
            vDateEnter.text = "${vDatePicker.year} / ${vDatePicker.month + 1} / ${vDatePicker.dayOfMonth}"
            birth = true
        }
    }

    private fun checkChoice() {
        if ((CheckBox_Normal.isChecked || CheckBox_Nurse.isChecked) && (CheckBox_Man.isChecked || CheckBox_Woman.isChecked) && birth) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("nurse", nurse)
            intent.putExtra("sex", sex)
            intent.putExtra("birth", vDatePicker.toString())
            startActivity(intent)
        }
        else {
            Toast.makeText(this, "You have to choose position", Toast.LENGTH_SHORT).show()
        }
    }
}