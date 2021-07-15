package com.fachidiot.nursehro.MainFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.facebook.login.LoginManager
import com.fachidiot.nursehro.LoginActivity
import com.fachidiot.nursehro.Class.MySharedPreferences
import com.fachidiot.nursehro.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_main_profile.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentMainProfile : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var mFirebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFirebaseAuth = FirebaseAuth.getInstance()

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        RelativeLayout_LogOut.setOnClickListener {
            logOut()
        }

    }

    private fun logOut() {
        mFirebaseAuth.signOut()
        val user = mFirebaseAuth.currentUser
        LoginManager.getInstance().logOut()
        context?.let { MySharedPreferences.setAuto(it, false) }

        activity?.let {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

        Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show()
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