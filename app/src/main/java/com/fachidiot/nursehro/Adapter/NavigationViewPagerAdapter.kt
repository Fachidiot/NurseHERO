@file:Suppress("DEPRECATION")

package com.fachidiot.nursehro.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fachidiot.nursehro.MainFragment.MainAccountFragment
import com.fachidiot.nursehro.MainFragment.MainFindFragment
import com.fachidiot.nursehro.MainFragment.MainHomeFragment
import com.fachidiot.nursehro.MainFragment.MainChatFragment

class NavigationViewPagerAdapter (supportFragmentManager: FragmentManager): FragmentPagerAdapter(supportFragmentManager) {
    var list = ArrayList<String>()
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> MainHomeFragment()
            1 -> MainChatFragment()
            2 -> MainFindFragment()
            3 -> MainAccountFragment()
            else -> MainHomeFragment()
        }
    }

    override fun getCount(): Int {
        return list.size
    }

}
