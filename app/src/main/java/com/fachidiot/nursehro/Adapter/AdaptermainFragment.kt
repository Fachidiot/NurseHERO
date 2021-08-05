package com.fachidiot.nursehro.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.fachidiot.nursehro.MainFragment.*

class AdaptermainFragment (fm : FragmentManager, private val fragmentCount : Int) :
FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> FragmentMainChat()
            1 -> FragmentMainFind()
            2 -> FragmentMainProfile()
            else -> FragmentMainProfile()
        }
    }

    override fun getCount(): Int = fragmentCount

}
