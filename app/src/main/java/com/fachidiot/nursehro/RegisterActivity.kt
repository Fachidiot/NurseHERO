package com.fachidiot.nursehro

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
            if (!CheckBox_Policy.isChecked)
                Toast.makeText(this, "Please Check the Checkbox", Toast.LENGTH_SHORT).show()
            else if (TextInputEditText_Password.text.toString() == TextInputEditText_Comfirm_Password.text.toString())
                CreateEmail()
            else
                Toast.makeText(this, "Check Password or Comfirm Password.", Toast.LENGTH_SHORT)
                    .show()
        }

        //RelativeLayout_Register.setOnClickListener {
        //
        //
        //    // 회원가입 처리 시작
        //    val strEmail: String = TextInputEditText_Email.toString()
        //    val strPwd: String = TextInputEditText_Password.toString()
//
        //    // Firebase Auth 진행
        //    mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(this, OnCompleteListener<AuthResult>){
//
        //    }
        //}
//
        //FirebaseAuth.getInstance()
        //val mDatabaseRef : DatabaseReference
        //if(CheckBox_Policy.isChecked)
        //{
        //    if(TextInputEditText_Password == TextInputEditText_Comfirm_Password)
        //    {
        //        if(CheckInfo(TextInputEditText_Email.toString())) {
        //            RelativeLayout_Register.setOnClickListener {
        //                val intent = Intent(this, RegisterSuccessActivity::class.java)
        //                startActivity(intent)
        //            }
        //        }
        //    }
//
        //}


    }

    //fun CheckInfo(stremail: String): Boolean {
    //    var arr = Array<String>(64,{""})
    //    arr = stremail.split("").toTypedArray()
    //    val count = find<String>(arr, "@")
    //    if(count < 0)
    //        return false
    //    var sliceArr = Array<String>(32,{""})
    //    sliceArr = arr.sliceArray(count..arr.size-count)
    //    when(sliceArr.toString()){
    //        "gmail.com"->return true
    //        "naver.com"->return true
    //    }
    //    return false
    //}
//
    //fun <T> find(a: Array<String>, Target: T): Int {
    //    for (i in a.indices) {
    //        if (a[i] == Target) return i
    //    }
    //    return -1
    //}

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