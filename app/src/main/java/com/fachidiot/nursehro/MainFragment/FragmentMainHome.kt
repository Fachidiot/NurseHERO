package com.fachidiot.nursehro.MainFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fachidiot.nursehro.LoginActivity
import com.fachidiot.nursehro.R
import kotlinx.android.synthetic.main.fragment_main_account.*
import kotlinx.android.synthetic.main.fragment_main_home.*
import com.fachidiot.nursehro.MainActivity
import com.fachidiot.nursehro.UserProfileActivity


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentMainHome : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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