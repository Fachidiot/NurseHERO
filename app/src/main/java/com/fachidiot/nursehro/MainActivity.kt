package com.fachidiot.nursehro

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fachidiot.nursehro.Adapter.AdaptermainFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private var backKeyPressedTime: Long = 0

    // 첫 번째 뒤로가기 버튼을 누를때 표시
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureBottomNavigation()

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
            return;
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

    private fun configureBottomNavigation() {
        ViewPager_main_xml.adapter = AdaptermainFragment(supportFragmentManager, 3)
        TabLayout_main_xml.setupWithViewPager(ViewPager_main_xml)

        val viewBtmNaviMain : View = this.layoutInflater.inflate(R.layout.btm_navigation_main, null, false)

        TabLayout_main_xml.getTabAt(0)!!.customView = viewBtmNaviMain.findViewById(R.id.xml_btmnv_btn_profile) as RelativeLayout
        TabLayout_main_xml.getTabAt(1)!!.customView = viewBtmNaviMain.findViewById(R.id.xml_btmnv_btn_friends) as RelativeLayout
        TabLayout_main_xml.getTabAt(2)!!.customView = viewBtmNaviMain.findViewById(R.id.xml_btmnv_btn_chat) as RelativeLayout

    }

}