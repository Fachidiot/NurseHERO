package com.fachidiot.nursehro.MainFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fachidiot.nursehro.*
import com.fachidiot.nursehro.Class.CustomUserInfo
import com.fachidiot.nursehro.Class.UserList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main_home.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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

        onGetRate()
        Log.d("FragmentHome", userListA.count().toString())

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUserList()

        RateUserIcon1.setOnClickListener {
            activity?.let {
                val intent = Intent(context, UserProfileActivity::class.java)
                startActivity(intent)
            }
        }

        RateUserIcon2.setOnClickListener {
            activity?.let {
                val intent = Intent(context, UserProfileActivity::class.java)
                startActivity(intent)
            }
        }

        RateUserIcon3.setOnClickListener {
            activity?.let {
                val intent = Intent(context, UserProfileActivity::class.java)
                startActivity(intent)
            }
        }

        RateUserIcon4.setOnClickListener {
            activity?.let {
                val intent = Intent(context, UserProfileActivity::class.java)
                startActivity(intent)
            }
        }

        RateUserIcon5.setOnClickListener {
            activity?.let {
                val intent = Intent(context, UserProfileActivity::class.java)
                startActivity(intent)
            }
        }

        RateUserIcon6.setOnClickListener {
            activity?.let {
                val intent = Intent(context, UserProfileActivity::class.java)
                startActivity(intent)
            }
        }

        RecommendUserIcon1.setOnClickListener {
            activity?.let {
                val intent = Intent(context, UserProfileActivity::class.java)
                startActivity(intent)
            }
        }

        RecommendUserIcon2.setOnClickListener {
            activity?.let {
                val intent = Intent(context, UserProfileActivity::class.java)
                startActivity(intent)
            }
        }

        RecommendUserIcon3.setOnClickListener {
            activity?.let {
                val intent = Intent(context, UserProfileActivity::class.java)
                startActivity(intent)
            }
        }

        RecommendUserIcon4.setOnClickListener {
            activity?.let {
                val intent = Intent(context, UserProfileActivity::class.java)
                startActivity(intent)
            }
        }

        RecommendUserIcon5.setOnClickListener {
            activity?.let {
                val intent = Intent(context, UserProfileActivity::class.java)
                startActivity(intent)
            }
        }

        RecommendUserIcon6.setOnClickListener {
            activity?.let {
                val intent = Intent(context, UserProfileActivity::class.java)
                startActivity(intent)
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_home, container, false)
    }

    private fun onGetRate() {
        userListA.clear()

        mFirebaseStoreDatabase.collection("users").whereEqualTo("nurse", true).get()
            .addOnCompleteListener{
                if(it.isSuccessful) {
                    for (dc in it.result!!.documents) {
                        val user = dc.toObject(CustomUserInfo::class.java)
                        if (user != null) {
                            userListA.add(UserList(user.userNickname, user.profileImage, user.location?.last(), user.sex, user.age))
                        }
                    }
                }
            }
    }

    private fun onGetRecommend() {
        userListB.clear()

        mFirebaseStoreDatabase.collection("users").whereEqualTo("nurse", true).get()
            .addOnCompleteListener{
                if(it.isSuccessful) {
                    for (dc in it.result!!.documents) {
                        val user = dc.toObject(CustomUserInfo::class.java)
                        if (user != null) {
                            userListB.add(UserList(user.userNickname, user.profileImage, user.location?.last(), user.sex, user.age))
                        }
                    }
                }
            }
    }

    private fun onGetNear() {

    }

    private fun setUserList() {
        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        UserRecyclerGrid.layoutManager = gridLayoutManager
        UserRecyclerGrid.setHasFixedSize(true)//어뎁터에 성능을 위한것
        UserRecyclerGrid.adapter = HomeUser_Adapter(userListA) //어뎁터에 리스트 자료를 넣는다.
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentMainHome().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}