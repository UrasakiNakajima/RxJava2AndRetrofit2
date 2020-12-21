package com.mobile.rxjava2andretrofit2.kotlin.mine.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.callback.RcvOnItemViewClickListener
import com.mobile.rxjava2andretrofit2.kotlin.mine.bean.Ans

class MineAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "MineAdapter"
    private var list: MutableList<Ans> = mutableListOf()

    fun clearData() {
        this.list.clear()
        notifyDataSetChanged()
    }

    fun addAllData(list: MutableList<Ans>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_mine, parent, false)
        return ContentHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContentHolder) {
            holder.tevAnsid.text = list[position].ansid
            holder.tevData.text = list[position].content_abstract.text

            holder.tevData.setOnClickListener { v -> rcvOnItemViewClickListener!!.onItemClickListener(position, v) }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    protected class ContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tevAnsid: TextView = itemView.findViewById(R.id.tev_ansid)
        var tevData: TextView = itemView.findViewById(R.id.tev_data)

    }

    private var rcvOnItemViewClickListener: RcvOnItemViewClickListener? = null

    fun setRcvOnItemViewClickListener(rcvOnItemViewClickListener: RcvOnItemViewClickListener) {
        this.rcvOnItemViewClickListener = rcvOnItemViewClickListener
    }
}