package com.fachidiot.nursehro.ViewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fachidiot.nursehro.R

class SliderItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    val tvItem: TextView? = itemView?.findViewById(R.id.tv_item)
}