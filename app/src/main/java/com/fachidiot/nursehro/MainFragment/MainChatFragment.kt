package com.fachidiot.nursehro.MainFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fachidiot.nursehro.R

class MainChatFragment : Fragment() {

    companion object {
        fun newInstance() = MainChatFragment()
    }

    private lateinit var viewModel: MainChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_chat_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainChatViewModel::class.java)
        // TODO: Use the ViewModel
    }

}