package com.fachidiot.nursehro.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.fachidiot.nursehro.MainFragment.*

class AdaptermainFragment (fm : FragmentManager, private val fragmentCount : Int) :
FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> FragmentMainHome()
            1 -> FragmentMainFind()
            2 -> FragmentMainAccount()
            else -> FragmentMainHome()
        }
    }

    override fun getCount(): Int = fragmentCount

}
