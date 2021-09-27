package com.fachidiot.nursehro.ViewHolder

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fachidiot.nursehro.R

class SliderAdapter : RecyclerView.Adapter<SliderItemViewHolder>() {

    private val data: ArrayList<String> = ArrayList()
    var callback: Callback? = null
    private val clickListener = View.OnClickListener { v -> v?.let { callback?.onItemClicked(it) } }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderItemViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_slider_item, parent, false)

        itemView.setOnClickListener(clickListener)
        return SliderItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SliderItemViewHolder, position: Int) {
        holder.tvItem?.text = data[position]
    }

    fun setData(data: ArrayList<String>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    interface Callback {
        fun onItemClicked(view: View)
    }
}