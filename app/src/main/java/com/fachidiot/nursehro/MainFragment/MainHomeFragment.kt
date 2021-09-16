package com.fachidiot.nursehro.MainFragment

import android.os.Bundle
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
import com.fachidiot.nursehro.Adapter.VP2ADSAdapter
import com.fachidiot.nursehro.Class.CustomUserInfo
import com.fachidiot.nursehro.Class.RecyclerViewDecoration
import com.fachidiot.nursehro.Class.UserList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.main_home_fragment.*

class MainHomeFragment : Fragment() {
    companion object {
        private const val MIN_SCALE = 0.85f // 뷰가 몇퍼센트로 줄어들 것인지
        private const val MIN_ALPHA = 0.5f // 어두워지는 정도를 나타낸 듯 하다.
    }

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

        vp2_TutorialAds.offscreenPageLimit = 1
        vp2_TutorialAds.adapter = VP2ADSAdapter(getAdsList()) // 어댑터 생성
        vp2_TutorialAds.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로
        //vp2_TutorialAds.setPageTransformer(ZoomOutPageTransformer())
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

        ((MainActivity)getActivity()).mFirebaseStoreDatabase
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

                    Toast.makeText(context, "${userListB.size} / ${userListB.last()}", Toast.LENGTH_SHORT).show()
                    UserRecommendRecyclerGrid.layoutManager = gridLayoutManager
                    UserRecommendRecyclerGrid.setHasFixedSize(true)//어뎁터에 성능을 위한것
                    UserRecommendRecyclerGrid.adapter = HomeUser_Adapter(userListB) //어뎁터에 리스트 자료를 넣는다.

                    context?.setTheme(R.style.Theme_NurseHRO)
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to load recommend userlist", Toast.LENGTH_SHORT).show()
            }
    }

    private fun onGetPlace() {

    }

    private fun setUserList(userList: ArrayList<UserList>) {

    }

    // 뷰 페이저에 들어갈 아이템
    private fun getAdsList(): ArrayList<Int> {
        return arrayListOf<Int>(R.drawable.my_post1, R.drawable.my_post2, R.drawable.my_post3)
    }

    // page.translationX = position * -offsetPx
    // Android Developer Api
    inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }
                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }
}