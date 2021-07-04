package com.fachidiot.nursehro.Adapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.fachidiot.nursehro.Fragment.*

class AdaptermainFragment (fm : FragmentManager, private val fragmentCount : Int) :
FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> FragmentMainProfile()
            1 -> FragmentMainFriends()
            2 -> FragmentMainChat()
            3 -> FragmentMainSearch()
            4 -> FragmentMainSetting()
            else -> FragmentMainProfile()
        }
    }

    override fun getCount(): Int = fragmentCount

}
