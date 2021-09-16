@file:Suppress("DEPRECATION")

package com.fachidiot.nursehro.Adapter

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fachidiot.nursehro.MainFragment.MainAccountFragment
import com.fachidiot.nursehro.MainFragment.MainFindFragment
import com.fachidiot.nursehro.MainFragment.MainHomeFragment
import com.fachidiot.nursehro.MainFragment.MainChatFragment

class NavigationViewPagerAdapter (fragmentActivity: FragmentActivity):
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MainHomeFragment()
            1 -> MainChatFragment()
            2 -> MainFindFragment()
            3 -> MainAccountFragment()
            else -> MainHomeFragment()
        }
    }
}
