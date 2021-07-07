package com.fachidiot.nursehro.RegisterFragment

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fachidiot.nursehro.LoginActivity
import com.fachidiot.nursehro.R
import com.fachidiot.nursehro.RegisterFragment.RegisterSuccessActivity
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
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        mFirebaseAuth = FirebaseAuth.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference()

        RelativeLayout_Register.setOnClickListener {
            if (TextInputLayout_FirstName.text.toString().isEmpty() || TextInputLayout_LastName.text.toString().isEmpty())
                Toast.makeText(this, "Check Name", Toast.LENGTH_SHORT).show()
            else if (TextInputEditText_Email.text.toString().isEmpty())
                Toast.makeText(this, "Check Email", Toast.LENGTH_SHORT).show()
            else if (TextInputEditText_Password.text.toString().isEmpty() || TextInputEditText_Comfirm_Password.text.toString().isEmpty())
                Toast.makeText(this, "Check Password.", Toast.LENGTH_SHORT).show()
            else if (TextInputEditText_Password.text.toString() != TextInputEditText_Comfirm_Password.text.toString())
                Toast.makeText(this, "Password incorrect with Comfirm Password.", Toast.LENGTH_SHORT).show()
            else if (!CheckBox_Policy.isChecked)
                Toast.makeText(this, "Please Check the Checkbox", Toast.LENGTH_SHORT).show()
            else
                CreateEmail()
        }
    }

    private fun CreateEmail(){
        val asdf1 = TextInputEditText_Email.text.toString()
        val asdf2 = TextInputEditText_Password.text.toString()
        mFirebaseAuth!!.createUserWithEmailAndPassword(TextInputEditText_Email.text.toString(), TextInputEditText_Password.text.toString()).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                // 회원가입 성공시
                val user = mFirebaseAuth?.currentUser

                Toast.makeText(this, "Authentication Success.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, RegisterSuccessActivity::class.java)
                startActivity(intent)
            } else {
                // 회원가입 실패시
                Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}