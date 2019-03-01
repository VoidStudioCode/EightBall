package com.i605.eightball.adapter

import android.support.v7.widget.RecyclerView
import android.widget.*
import android.view.*
import com.i605.eightball.R

class RvAdapter(private val list: MutableList<String>) :
    RecyclerView.Adapter<RvAdapter.MyViewHolder>() {
    private lateinit var onItemClickListener: OnItemClickListener

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv: TextView by lazy { itemView.findViewById<TextView>(R.id.tv) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.widget_rv_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val name = list[position]
        holder.tv.text = name
        holder.tv.setOnClickListener { v ->
            onItemClickListener.onItemClick((v as TextView).text.toString())
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(name: String)
    }
}