package com.fachidiot.nursehro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.fachidiot.nursehro.Class.UserList


class MapList_Adapter(private val profileList : ArrayList<UserList>) : RecyclerView.Adapter<MapList_Adapter.CustomViewHolder>() {
    //뷰홀더가 처음 생성될때
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapList_Adapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maplist_recyccler, parent, false)
        return CustomViewHolder(view).apply { //클릭시 액션
            itemView.setOnClickListener { //여기서 itemview는 뷰홀더의 아이템들을 의미한다.
            val curPos : Int = adapterPosition //누른 뷰의 순서값
            val profile : UserList = profileList[curPos] //객체형태로 번호에 맞게 가져오기
            Toast.makeText(parent.context, "이름 : ${profile.userFullname} 성별 : ${profile.sex} 위치 : ${profile.location} ", Toast.LENGTH_LONG).show()
            }
        }
    //뷰홀더에 뷰를 넘겨주고 이 것을 반환한다.
    }


    //재활용해주는 곳 및 값을 넣어주는 곳
    override fun onBindViewHolder(holder: MapList_Adapter.CustomViewHolder, position: Int) {
        //holder.profile.setImageResource(profileList[position].profileImage)
        holder.name.text = profileList[position].userFullname
        holder.age.text = profileList[position].location
        holder.job.text = profileList[position].sex.toString()
    }

    //리스트의 갯수를 적어준다
    override fun getItemCount(): Int {
        return profileList.size
    }



    //뷰홀더 클래스(음료수처럼 잡아주는 홀더)
    // 이곳에서 파인드뷰아이디로 리스트 아이템에 있는 뷰들을 참조한다.
    inner class CustomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val profile = itemView.findViewById<ImageView>(R.id.imageView) //사진
        val name = itemView.findViewById<TextView>(R.id.textView) //이름
        val age = itemView.findViewById<TextView>(R.id.ageView) //나이
        val job = itemView.findViewById<TextView>(R.id.jobView) //직업
    }
}
