package com.fachidiot.nursehro.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fachidiot.nursehro.R
import kotlinx.android.synthetic.main.adslist_item.view.*

class VP2ADSAdapter(ADSList: ArrayList<Int>) : RecyclerView.Adapter<VP2ADSAdapter.PagerViewHolder>() {
    var item = ADSList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.idol.setImageResource(item[position])
    }

    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.adslist_item, parent, false)){

        val idol = itemView.imageView_ads!!
    }
}