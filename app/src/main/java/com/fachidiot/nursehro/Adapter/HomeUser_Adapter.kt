package com.fachidiot.nursehro

import android.app.Activity
import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fachidiot.nursehro.Class.UserList
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_main_account.*
import kotlin.coroutines.coroutineContext


class HomeUser_Adapter(private val profileList : ArrayList<UserList>) : RecyclerView.Adapter<HomeUser_Adapter.CustomViewHolder>() {
    private lateinit var mFirebaseAuth : FirebaseAuth
    private lateinit var mFirebaseStorage: FirebaseStorage
    private lateinit var mFirebaseStoreDatabase: FirebaseFirestore
    private lateinit var mContext : Context

    //뷰홀더가 처음 생성될때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeUser_Adapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.homelist_user_recycler, parent, false)

        mContext = parent.context
        mFirebaseStoreDatabase = Firebase.firestore
        mFirebaseStorage = FirebaseStorage.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()

        return CustomViewHolder(view).apply { //클릭시 액션
            itemView.setOnClickListener { //여기서 itemview는 뷰홀더의 아이템들을 의미한다.
            val curPos : Int = adapterPosition //누른 뷰의 순서값
            val profile : UserList = profileList[curPos] //객체형태로 번호에 맞게 가져오기
            Toast.makeText(parent.context, "이름 : ${profile.userNickname} 성별 : ${profile.sex} 위치 : ${profile.location} ", Toast.LENGTH_LONG).show()
            }
        }
    //뷰홀더에 뷰를 넘겨주고 이 것을 반환한다.
    }


    //재활용해주는 곳 및 값을 넣어주는 곳
    override fun onBindViewHolder(holder: HomeUser_Adapter.CustomViewHolder, position: Int) {
        if (profileList[position].profileImage != "null")
        {
            val storageRef: StorageReference = mFirebaseStorage.reference
            storageRef.child("userprofileImages/uid/${profileList[position].profileImage}").downloadUrl.addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    this.let {
                        Glide.with(mContext)
                            .load(task.result)
                            .into(holder.profile)
                    }
                }
            }
        }
        else{
            holder.profile.setImageResource(R.drawable.icon_nurse)
        }

        holder.name.text = profileList[position].userNickname
    }

    //리스트의 갯수를 적어준다
    override fun getItemCount(): Int {
        return profileList.size
    }



    //뷰홀더 클래스(음료수처럼 잡아주는 홀더)
    // 이곳에서 파인드뷰아이디로 리스트 아이템에 있는 뷰들을 참조한다.
    inner class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val profile = itemView.findViewById<ImageView>(R.id.homeuserprofile) //사진
        val name = itemView.findViewById<TextView>(R.id.homeusername) //이름
    }
}
