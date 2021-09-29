package com.fachidiot.nursehro.Class

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fachidiot.nursehro.MapList_Adapter
import com.fachidiot.nursehro.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.modal_bottom_sheet_layout.*
import java.util.ArrayList

class BottomSheetDialog(var adapter: MapList_Adapter): BottomSheetDialogFragment() {
    private var profileList : ArrayList<UserList> = ArrayList()

    private lateinit var mFirebaseStoreDatabase: FirebaseFirestore
    private lateinit var bottomSheetListClickListener : BottomSheetListClickListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.modal_bottom_sheet_layout, container, false)
    }

    interface BottomSheetListClickListener {
        fun onButtonClick()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        button_back.setOnClickListener {
            dismiss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            val mapListAdapter = view.findViewById<RecyclerView>(R.id.MapRecyclerView)
            mapListAdapter.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false) //레이아웃매니저를 이용해 어뎁터의 방향을 결정
            mapListAdapter.setHasFixedSize(true)//어뎁터에 성능을 위한것
            mapListAdapter.adapter = adapter

            Log.d("BottomSheetDialog", adapter.itemCount.toString())
        } catch (e : Exception) {
            Log.e("BottomSheetDialog", e.toString())
        }

        // 팝업 생성 시 전체화면으로 띄우기
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from<View>(bottomSheet!!)
    }

    companion object {
    }
}