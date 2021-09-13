package com.fachidiot.nursehro.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fachidiot.nursehro.R

class TagAdapter(private val tags : ArrayList<String>) : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    companion object;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.taglist_recycler, parent, false)
        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.tag.text = tags[position]
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    inner class TagViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tag: TextView = itemView.findViewById<TextView>(R.id.textView)
    }
}