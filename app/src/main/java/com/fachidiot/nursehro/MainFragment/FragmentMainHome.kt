package com.fachidiot.nursehro.MainFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fachidiot.nursehro.*
import com.fachidiot.nursehro.Class.CustomUserInfo
import com.fachidiot.nursehro.Class.RecyclerViewDecoration
import com.fachidiot.nursehro.Class.UserList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main_home.*

class FragmentMainHome : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_main_home, container, false)
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

                    Toast.makeText(context, "${userListB.size} / ${userListB.last()}", Toast.LENGTH_SHORT).show()
                    UserRecommendRecyclerGrid.layoutManager = gridLayoutManager
                    UserRecommendRecyclerGrid.setHasFixedSize(true)//어뎁터에 성능을 위한것
                    UserRecommendRecyclerGrid.adapter = HomeUser_Adapter(userListB) //어뎁터에 리스트 자료를 넣는다.
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
}