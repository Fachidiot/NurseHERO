package com.fachidiot.nursehro

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fachidiot.nursehro.MainFragment.MainAccountFragment
import com.fachidiot.nursehro.MainFragment.MainChatFragment
import com.fachidiot.nursehro.MainFragment.MainFindFragment
import com.fachidiot.nursehro.MainFragment.MainHomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    // 마지막으로 뒤로가기  버튼을 눌렀던 시간 저장
    private var backKeyPressedTime: Long = 0
    // 첫 번째 뒤로가기 버튼을 누를때 표시
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFirebaseStoreDatabase = Firebase.firestore
        mFirebaseStorage = FirebaseStorage.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()

        chipNavigationBar_Bottom.setItemSelected(R.id.home, true)
        val homeFragment = MainHomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout_MainFragment_Container, homeFragment).commit()

        //Badges Test
        chipNavigationBar_Bottom.showBadge(R.id.chat, 249)

        configureBottomChipNavigation()
    }

    override fun onBackPressed() {
        //super.onBackPressed()  기존 뒤로가기 버튼의 기능을 막기위해 주석처리

        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT)
            toast?.show()
            return
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish()
            toast?.cancel()
            moveTaskToBack(true)				// 태스크를 백그라운드로 이동
            finishAndRemoveTask()						// 액티비티 종료 + 태스크 리스트에서 지우기
            android.os.Process.killProcess(android.os.Process.myPid())	// 앱 프로세스 종료
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun configureBottomChipNavigation() {
        chipNavigationBar_Bottom.setOnItemSelectedListener { id ->
            var fragment : Fragment? = null
            when(id) {
                R.id.home -> fragment = MainHomeFragment()
                R.id.chat -> fragment = MainChatFragment()
                R.id.search -> fragment = MainFindFragment()
                R.id.profile -> fragment = MainAccountFragment()
            }

            if (fragment != null)
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout_MainFragment_Container, fragment).commit()
            else
                Log.e("MainActivity", "Error in creating fragment")
        }
    }

    companion object {
        private lateinit var mFirebaseAuth : FirebaseAuth
        private lateinit var mFirebaseStorage: FirebaseStorage
        private lateinit var mFirebaseStoreDatabase: FirebaseFirestore
    }
}