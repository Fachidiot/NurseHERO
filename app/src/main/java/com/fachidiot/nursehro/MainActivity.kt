package com.fachidiot.nursehro

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fachidiot.nursehro.Adapter.NavigationViewPagerAdapter
import com.fxn.ariana.ArianaBackgroundListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private var backKeyPressedTime: Long = 0

    // 첫 번째 뒤로가기 버튼을 누를때 표시
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        chip_navigation.setOnItemSelectedListener { id ->
            when(id) {
                R.id.home -> ViewPager_main.currentItem = 0
                R.id.like -> ViewPager_main.currentItem = 1
                R.id.search -> ViewPager_main.currentItem = 2
                R.id.profile -> ViewPager_main.currentItem = 3
            }
        }

        ViewPager_main.setOnTouchListener { _, _ -> true }
        ViewPager_main.adapter = NavigationViewPagerAdapter(supportFragmentManager).apply {
            list = ArrayList<String>().apply {
                add("Home")
                add("Like")
                add("Search")
                add("Profile")
            }
        }

        ViewPager_main.addOnPageChangeListener(
            ArianaBackgroundListener(
                getColors(),
                imageview,
                ViewPager_main
            )
        )
    }

    private fun getColors() :IntArray {
        return intArrayOf(
            ContextCompat.getColor(this, R.color.home),
            ContextCompat.getColor(this, R.color.like),
            ContextCompat.getColor(this, R.color.search),
            ContextCompat.getColor(this, R.color.profile)
        )
    }
}