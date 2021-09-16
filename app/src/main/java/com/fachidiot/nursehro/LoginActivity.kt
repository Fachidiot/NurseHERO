package com.fachidiot.nursehro

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.fachidiot.nursehro.Class.MySharedPreferences
import com.fachidiot.nursehro.RegisterFragment.RegisterChooseActivity
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


class LoginActivity : AppCompatActivity() {
    private val permission_list = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

    lateinit var mFirebaseAuth : FirebaseAuth
    lateinit var mCallbackManager: CallbackManager
    private var backKeyPressedTime: Long = 0
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        onPermissionCheck()

        mFirebaseAuth = FirebaseAuth.getInstance()
        mCallbackManager = CallbackManager.Factory.create()

        // 1. 값을 가져온다 - 검사 ( Test@gmail.com / admin )
        // 2. 클릭을 감지한다
        // 3. 1번의 값을 다음 화면으로 넘긴다

        Button_Register.setOnClickListener{
            val intent = Intent(this, RegisterChooseActivity::class.java)
            startActivity(intent)
        }

        login_nbtn.setOnClickListener(View.OnClickListener {
            loginEmail()
        })

        RelativeLayout_LoginwithFacebook.setOnClickListener(View.OnClickListener {
            loginFacebook()
        })

        if(MySharedPreferences.getAuto(this) && mFirebaseAuth.currentUser != null) {
            Toast.makeText(this, "Auto Authentication success", Toast.LENGTH_SHORT).show()
            loginSuccess(false)
        }

    }

    private fun onPermissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission_list, 0)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT)
            toast?.show()
            return
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish()
            toast?.cancel()
            moveTaskToBack(true)
            finishAndRemoveTask()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }





    private fun loginEmail() {
        if (!TextInputEditText_LoginEmail.text.toString().contains("@")) {
            Toast.makeText(this, "Wrong Type Email", Toast.LENGTH_SHORT).show()
            TextInputEditText_LoginEmail.error = "you must set right email"
            return
        }
        else if(TextInputEditText_LoginPassword.text.toString().isEmpty())
        {
            Toast.makeText(this, "Check the Password", Toast.LENGTH_SHORT).show()
            TextInputEditText_LoginPassword.error = "you must set your password"
            return
        }
        mFirebaseAuth.signInWithEmailAndPassword(TextInputEditText_LoginEmail.text.toString(), TextInputEditText_LoginPassword.text.toString())
            .addOnCompleteListener(this) {
                Log.d(this.toString(), TextInputEditText_LoginEmail.text.toString())
                Log.d(this.toString(), TextInputEditText_LoginPassword.text.toString())
            if (it.isSuccessful) {
                Toast.makeText(this, "signInWithEmail Success", Toast.LENGTH_SHORT).show()

                loginSuccess(CheckBox_AutoLogin.isChecked)
            } else {
                Toast.makeText(this, "Email or Password Incorrect", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
        LoginManager.getInstance().registerCallback(mCallbackManager, object:FacebookCallback<LoginResult> {

            override fun onSuccess(result: LoginResult?) {
                // Facebook 로그인 성공
                handleFacebookAccessToken(result?.accessToken)
                Toast.makeText(this@LoginActivity, "Facebook Authentication success", Toast.LENGTH_SHORT).show()
                loginSuccess(CheckBox_AutoLogin.isChecked)
            }
            override fun onCancel() {
                // Facebook 로그인 취소
                Toast.makeText(this@LoginActivity, "Facebook Authentication canceled", Toast.LENGTH_SHORT).show()
            }
            override fun onError(error: FacebookException) {
                // Facebook 로그인 실패
                Toast.makeText(this@LoginActivity, "Facebook Authentication failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun logOut() {
        mFirebaseAuth.signOut()
        LoginManager.getInstance().logOut()
        MySharedPreferences.setAuto(this, false)
        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
    }

    private fun loginSuccess(autologin: Boolean) {
        if(autologin) {
            MySharedPreferences.setUserId(this, TextInputEditText_LoginEmail.text.toString())
            MySharedPreferences.setUserPW(this, TextInputEditText_LoginPassword.text.toString())
            MySharedPreferences.setAuto(this, true)

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("email", MySharedPreferences.getUserId(this))

            application.setTheme(R.style.Splash)
            startActivity(intent)
        } else {
            val intent = Intent(this, MainActivity::class.java)
            //intent.putExtra("email", MySharedPreferences.getUserId(this))
            intent.putExtra("email", TextInputEditText_LoginEmail.text.toString())
            startActivity(intent)
        }
    }









    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
        // onActivityResult에서는 callbackManager에 로그인 결과를 넘겨줍니다
        // 여기에 callbackMAnager?.onActivityResult가 있어야 onSuccess를 호출할 수 있습니다
    }

    private fun handleFacebookAccessToken(token: AccessToken?) {
        Log.d("MainActivity", "handleFacebookAccessToken:$token")
        if(token != null) {
            val credential = FacebookAuthProvider.getCredential(token.token)
            mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this) { task->
                if (task.isSuccessful) {
                    Log.d("MainActivity", "SignInWithCredential:Success")
                }else {
                    Log.w("MainActivity", "SignInWithCredential:Failure", task.exception)
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}