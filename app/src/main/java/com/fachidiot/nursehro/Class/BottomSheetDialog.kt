package com.fachidiot.nursehro.Class

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fachidiot.nursehro.MapList_Adapter
import com.fachidiot.nursehro.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.modal_bottom_sheet_layout.*
import java.util.ArrayList

class BottomSheetDialog(var adapter: MapList_Adapter): BottomSheetDialogFragment() {

    private var profileList : ArrayList<UserList> = ArrayList()

    private lateinit var mFirebaseStoreDatabase: FirebaseFirestore
    lateinit var bottomSheetListClickListener : BottomSheetListClickListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.modal_bottom_sheet_layout, container, false)
    }

    interface BottomSheetListClickListener {
        fun onButtonClick()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        button_back.setOnClickListener {
            if(::bottomSheetListClickListener.isInitialized)
            {
                bottomSheetListClickListener.onButtonClick()
                dismiss()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            val mapListAdapter = view.findViewById<RecyclerView>(R.id.MapRecyclerView)
            mapListAdapter.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) //레이아웃매니저를 이용해 어뎁터의 방향을 결정
            mapListAdapter.setHasFixedSize(true)//어뎁터에 성능을 위한것
            mapListAdapter.adapter = adapter
        } catch (e : Exception) {
            Log.e("BottomSheetDialog", e.toString())
        }

        // 팝업 생성 시 전체화면으로 띄우기
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from<View>(bottomSheet!!)

        // 드래그해도 팝업이 종료되지 않도록
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setWhiteNavigationBar(dialog)
        }

        return dialog
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun setWhiteNavigationBar(dialog: Dialog) {
        val window: Window? = dialog.window
        if (window != null) {
            val metrics = DisplayMetrics()
            window.windowManager.defaultDisplay.getMetrics(metrics)

            val dimDrawable = ColorDrawable(Color.TRANSPARENT)
            val navigationBarDrawable = ColorDrawable(Color.WHITE)

            val layers = arrayOf(dimDrawable, navigationBarDrawable)
            val windowBackground = LayerDrawable(layers)
            windowBackground.setLayerInsetTop(1, metrics.heightPixels)

            window.setBackgroundDrawable(windowBackground)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        //try{
        //    bottomSheetListClickListener = context as BottomSheetListClickListener
        //} catch (e : ClassCastException) {
        //    Log.e("BottomSheetDialog", e.toString())
        //}
    }

    companion object {
    }
}