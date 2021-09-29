package com.fachidiot.nursehro.MainFragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.fachidiot.nursehro.*
import com.fachidiot.nursehro.Adapter.BannerAdapter
import com.fachidiot.nursehro.Adapter.VP2ADSAdapter
import com.fachidiot.nursehro.Class.CustomUserInfo
import com.fachidiot.nursehro.Class.ProgressDialog
import com.fachidiot.nursehro.Class.RecyclerViewDecoration
import com.fachidiot.nursehro.Class.UserList
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.main_home_fragment.*

class MainHomeFragment : Fragment() {
    companion object {
        private const val MIN_SCALE = 0.85f // 뷰가 몇퍼센트로 줄어들 것인지
        private const val MIN_ALPHA = 0.5f // 어두워지는 정도를 나타낸 듯 하다.

        private const val intervalTime = 4500.toLong() // 몇초 간격으로 페이지를 넘길것인지 (1500 = 1.5초)
    }

    private var customProgressDialog: ProgressDialog? = null
    private var currentPosition = Int.MAX_VALUE / 2
    private var myHandler = MyHandler()

    private var userListA : ArrayList<UserList> = ArrayList()
    private var userListB : ArrayList<UserList> = ArrayList()

    private lateinit var mFirebaseAuth : FirebaseAuth
    private lateinit var mFirebaseStoreDatabase: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFirebaseStoreDatabase = Firebase.firestore
        mFirebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //로딩창 객체 생성
        customProgressDialog = ProgressDialog(context)
        //로딩창을 투명하게
        customProgressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        customProgressDialog!!.setCancelable(false)
        customProgressDialog!!.show()

        onGetRate()
        onGetRecommend()

        /* 여백, 너비에 대한 정의 */
        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin) // dimen 파일 안에 크기를 정의해두었다.
        val pagerWidth = resources.getDimensionPixelOffset(R.dimen.pageWidth) // dimen 파일이 없으면 생성해야함
        val screenWidth = resources.displayMetrics.widthPixels // 스마트폰의 너비 길이를 가져옴
        val offsetPx = screenWidth - pageMarginPx - pagerWidth

        vp2_TutorialAds.setPageTransformer { page, position ->
            page.translationX = position * -offsetPx
        }
        vp2_TutorialAds_Banner.setPageTransformer { page, position ->
            page.translationX = position * -offsetPx
        }

        vp2_TutorialAds.offscreenPageLimit = 1
        vp2_TutorialAds.adapter = BannerAdapter(getAdsList()) // 어댑터 생성
        vp2_TutorialAds.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로
        vp2_TutorialAds.setCurrentItem(currentPosition, false)

        vp2_TutorialAds_Banner.offscreenPageLimit = 1
        vp2_TutorialAds_Banner.adapter = BannerAdapter(getAdsList2()) // 어댑터 생성
        vp2_TutorialAds_Banner.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로
        vp2_TutorialAds_Banner.setCurrentItem(currentPosition, false)
        vp2_TutorialAds_Banner.apply {
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    when (state) {
                        // 뷰페이저에서 손 떼었을때 / 뷰페이저 멈춰있을 때
                        ViewPager2.SCROLL_STATE_IDLE -> autoScrollStart(intervalTime)
                        // 뷰페이저 움직이는 중
                        ViewPager2.SCROLL_STATE_DRAGGING -> autoScrollStop()
                    }
                }
            })
        }
    }

    fun MasterCode_SetLatLng() {
        mFirebaseStoreDatabase.collection("users").get()
            .addOnCompleteListener{
                if(it.isSuccessful) {
                    for (dc in it.result!!.documents) {
                        val user = dc.toObject(CustomUserInfo::class.java)
                        if (user != null) {
                            val geocoder = Geocoder(context)
                            val addresses: List<Address> = geocoder.getFromLocationName(user.location, 3)
                            val address : Address = addresses[0]
                            var latLng: GeoPoint? = null
                            latLng = GeoPoint(address.latitude, address.longitude)

                            mFirebaseStoreDatabase.collection("users").document(user.uid).update("latLng", latLng)
                        }
                    }
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        UserRecyclerGrid.addItemDecoration(RecyclerViewDecoration(10))
        UserRecommendRecyclerGrid.addItemDecoration(RecyclerViewDecoration(10))

        swipeRefreshLayout.setOnRefreshListener {
            onGetRate()
            onGetRecommend()

            // 새로고침 중지
            // 없으면 새로고침 애니메이션 끝나지 않음
            swipeRefreshLayout.isRefreshing = false

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_home_fragment, container, false)
    }

    private fun onGetRate() {
        userListA.clear()

        mFirebaseStoreDatabase.collection("users").whereEqualTo("nurse", true).limit(12).get()
            .addOnCompleteListener{
                if(it.isSuccessful) {
                    for (dc in it.result!!.documents) {
                        val user = dc.toObject(CustomUserInfo::class.java)
                        if (user != null) {
                            userListA.add(UserList(user.userNickname, user.profileImage, user.location, user.sex, user.age))
                        }
                    }
                    Log.d("firestore loading", "is Finished")

                    val gridLayoutManager = GridLayoutManager(requireContext(), 2)
                    gridLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

                    UserRecyclerGrid.layoutManager = gridLayoutManager
                    UserRecyclerGrid.setHasFixedSize(true)//어뎁터에 성능을 위한것
                    UserRecyclerGrid.adapter = HomeUser_Adapter(userListA) //어뎁터에 리스트 자료를 넣는다.
                }
            }
    }

    private fun onGetRecommend() {
        userListB.clear()

        mFirebaseStoreDatabase.collection("users").whereEqualTo("nurse", true).orderBy("age").limit(12).get()
            .addOnCompleteListener{
                if(it.isSuccessful) {
                    for (dc in it.result!!.documents) {
                        val user = dc.toObject(CustomUserInfo::class.java)
                        if (user != null) {
                            userListB.add(UserList(user.userNickname, user.profileImage, user.location, user.sex, user.age))
                        }
                    }
                    Log.d("firestore loading", "is Finished")

                    val gridLayoutManager = GridLayoutManager(requireContext(), 2)
                    gridLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

                    //Toast.makeText(context, "${userListB.size} / ${userListB.last()}", Toast.LENGTH_SHORT).show()
                    UserRecommendRecyclerGrid.layoutManager = gridLayoutManager
                    UserRecommendRecyclerGrid.setHasFixedSize(true)//어뎁터에 성능을 위한것
                    UserRecommendRecyclerGrid.adapter = HomeUser_Adapter(userListB) //어뎁터에 리스트 자료를 넣는다.

                    context?.setTheme(R.style.Theme_NurseHRO)
                    customProgressDialog!!.dismiss()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to load recommend userlist", Toast.LENGTH_SHORT).show()
            }
    }

    // 뷰 페이저에 들어갈 아이템
    private fun getAdsList(): ArrayList<Int> {
        return arrayListOf<Int>(R.drawable.my_post1, R.drawable.my_post2, R.drawable.my_post3)
    }

    private fun getAdsList2(): ArrayList<Int> {
        return arrayListOf<Int>(R.drawable.ads_banner, R.drawable.ads_banner, R.drawable.ads_banner)
    }

    private fun autoScrollStart(intervalTime: Long) {
        myHandler.removeMessages(0) // 이거 안하면 핸들러가 1개, 2개, 3개 ... n개 만큼 계속 늘어남
        myHandler.sendEmptyMessageDelayed(0, intervalTime) // intervalTime 만큼 반복해서 핸들러를 실행하게 함
    }

    private fun autoScrollStop(){
        myHandler.removeMessages(0) // 핸들러를 중지시킴
    }

    @SuppressLint("HandlerLeak")
    private inner class MyHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            if(msg.what == 0) {
                vp2_TutorialAds_Banner.setCurrentItem(++currentPosition, true) // 다음 페이지로 이동
                autoScrollStart(intervalTime) // 스크롤을 계속 이어서 한다.
            }
        }
    }

    // 다른 페이지 갔다가 돌아오면 다시 스크롤 시작
    override fun onResume() {
        super.onResume()
        autoScrollStart(intervalTime)
    }

    // 다른 페이지로 떠나있는 동안 스크롤이 동작할 필요는 없음. 정지
    override fun onPause() {
        super.onPause()
        autoScrollStop()

    }
}