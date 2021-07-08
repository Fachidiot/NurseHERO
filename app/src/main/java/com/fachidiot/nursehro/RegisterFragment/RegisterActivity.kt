package com.fachidiot.nursehro.RegisterFragment

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fachidiot.nursehro.LoginActivity
import com.fachidiot.nursehro.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private var mFirebaseAuth: FirebaseAuth? = null
    private var mDatabaseRef: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        button_back.setOnClickListener {
            finish()
        }

        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().reference

        RelativeLayout_Register.setOnClickListener {
            checkInfo()
        }
    }

    private fun createEmail(){
        this.mFirebaseAuth!!.createUserWithEmailAndPassword(TextInputEditText_Email.text.toString(), TextInputEditText_Password.text.toString()).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                // 회원가입 성공시
                val user = mFirebaseAuth?.currentUser

                Toast.makeText(this, "Authentication Success.", Toast.LENGTH_SHORT).show()
                val intent = Intent(
                    this,
                    RegisterSuccessActivity::class.java
                )
                intent.putExtra("email", TextInputEditText_Email.text.toString())
                intent.putExtra("password", TextInputEditText_Password.text.toString())
                startActivity(intent)
            } else {
                // 회원가입 실패시
                Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkInfo() {
        if (TextInputLayout_FirstName.text.toString().isEmpty())
            TextInputLayout_FirstName.setError("Enter your first name")
        else if (TextInputLayout_LastName.text.toString().isEmpty())
            TextInputLayout_LastName.setError("Enter your last name")
        else if (TextInputEditText_Email.text.toString().isEmpty())
            TextInputEditText_Email.setError("Enter your email")
        else if (TextInputEditText_Password.text.toString().isEmpty())
            TextInputEditText_Password.setError("Enter your password")
        else if (TextInputEditText_Comfirm_Password.text.toString().isEmpty())
            TextInputEditText_Comfirm_Password.setError("Enter one more password")
        else if (TextInputEditText_Password.text.toString() != TextInputEditText_Comfirm_Password.text.toString()) {
            TextInputEditText_Password.setError("Your password is not correct")
            TextInputEditText_Comfirm_Password.setError("Your password is not correct")
        }
        else if (!CheckBox_Policy.isChecked)
            CheckBox_Policy.setError("Please check the checkbox")
        else
            createEmail()
    }
}