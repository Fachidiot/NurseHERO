package com.fachidiot.nursehro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.fachidiot.nursehro.Adapter.AdaptermainFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureBottomNavigation()

    }

    private fun configureBottomNavigation() {
        ViewPager_main_xml.adapter = AdaptermainFragment(supportFragmentManager, 5)
        TabLayout_main_xml.setupWithViewPager(ViewPager_main_xml)

        val viewBtmNaviMain : View = this.layoutInflater.inflate(R.layout.btm_navigation_main, null, false)

        TabLayout_main_xml.getTabAt(0)!!.customView = viewBtmNaviMain.findViewById(R.id.xml_btmnv_btn_profile) as RelativeLayout
        TabLayout_main_xml.getTabAt(1)!!.customView = viewBtmNaviMain.findViewById(R.id.xml_btmnv_btn_friends) as RelativeLayout
        TabLayout_main_xml.getTabAt(2)!!.customView = viewBtmNaviMain.findViewById(R.id.xml_btmnv_btn_chat) as RelativeLayout
        TabLayout_main_xml.getTabAt(3)!!.customView = viewBtmNaviMain.findViewById(R.id.xml_btmnv_btn_search) as RelativeLayout
        TabLayout_main_xml.getTabAt(4)!!.customView = viewBtmNaviMain.findViewById(R.id.xml_btmnv_btn_setting) as RelativeLayout

    }

}