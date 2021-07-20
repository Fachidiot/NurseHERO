package com.fachidiot.nursehro.MainFragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.loader.content.CursorLoader
import com.facebook.login.LoginManager
import com.fachidiot.nursehro.LoginActivity
import com.fachidiot.nursehro.Class.MySharedPreferences
import com.fachidiot.nursehro.Class.UserInfo
import com.fachidiot.nursehro.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_main_account.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentMainProfile : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mFirebaseAuth : FirebaseAuth
    private lateinit var mFirebaseStorage: FirebaseStorage
    private lateinit var mFirebaseStoreDatabase: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseStoreDatabase = Firebase.firestore
        mFirebaseStorage = FirebaseStorage.getInstance()

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        if(mFirebaseAuth.currentUser != null)
            setInfo()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_account, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        LogoutButton.setOnClickListener {
            logOut()
        }

        LoginButton.setOnClickListener {
            activity?.let {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun logOut() {
        mFirebaseAuth.signOut()
        val user = mFirebaseAuth.currentUser
        LoginManager.getInstance().logOut()
        context?.let { MySharedPreferences.setAuto(it, false) }

        //activity?.let {
        //    val intent = Intent(context, LoginActivity::class.java)
        //    startActivity(intent)
        //}

        LogoutButton.visibility = View.INVISIBLE

        LoginButton.visibility = View.VISIBLE

        Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()
    }

    private fun setInfo() {
        val userRef = mFirebaseAuth.currentUser?.let {
            mFirebaseStoreDatabase.collection("users").document(
                it.uid)
        }
        userRef?.get()?.addOnSuccessListener { documentSnapshot ->
            val user = documentSnapshot.toObject<UserInfo>()

            TextView_username.text = user?.userNickname
            TextView_Firstname.text = user?.userFirstname
            TextView_Lastname.text = user?.userLastname
            if (user?.nurse == true)
                TextView_Nurse.text = "Nurse"
            else
                TextView_Nurse.text = "Normal"
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentMainProfile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}