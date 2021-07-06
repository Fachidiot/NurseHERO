package com.fachidiot.nursehro

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import javax.security.auth.login.LoginException

class LoginActivity : AppCompatActivity() {

    var mFirebaseAuth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mFirebaseAuth = FirebaseAuth.getInstance()

        // 1. 값을 가져온다 - 검사 ( Test@gmail.com / admin )
        // 2. 클릭을 감지한다
        // 3. 1번의 값을 다음 화면으로 넘긴다

        Button_Register.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        RelativeLayout_Login.setOnClickListener(View.OnClickListener {
            loginEmail()
        })


    }

    private fun loginEmail() {
        if(TextInputEditText_LoginEmail.text.toString().isEmpty()) {
            Toast.makeText(this, "Check the Email", Toast.LENGTH_SHORT).show()
            TextInputEditText_LoginEmail.setError("you must set your email")
            return
        } else if (!TextInputEditText_LoginEmail.text.toString().contains("@")) {
            Toast.makeText(this, "Wrong Type Email", Toast.LENGTH_SHORT).show()
            TextInputEditText_LoginEmail.setError("you must set right email")
            return
        }
        else if(TextInputEditText_LoginPassword.text.toString().isEmpty())
        {
            Toast.makeText(this, "Check the Password", Toast.LENGTH_SHORT).show()
            TextInputEditText_LoginPassword.setError("you must set your password")
            return
        }
        mFirebaseAuth!!.signInWithEmailAndPassword(TextInputEditText_LoginEmail.text.toString(), TextInputEditText_LoginPassword.text.toString())
            .addOnCompleteListener(this) {
                Log.d(this.toString(), TextInputEditText_LoginEmail.text.toString())
                Log.d(this.toString(), TextInputEditText_LoginPassword.text.toString())
            if (it.isSuccessful) {
                Toast.makeText(this, "signInWithEmail Success", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("email", TextInputEditText_LoginEmail.text.toString())
                intent.putExtra("password", TextInputEditText_LoginPassword.text.toString())
                startActivity(intent)

                val user = mFirebaseAuth?.currentUser


            } else {
                Toast.makeText(this, "signInWithEmail Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }


    //fun validation(): Boolean {
    //    return eok == ine && pok == inp // java에서는 (ㅈ같은) 문자열 비교시 ==로 하면 안된다!!
    //}
}