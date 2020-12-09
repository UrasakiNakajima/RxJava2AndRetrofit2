package com.mobile.rxjava2andretrofit2.kotlin.mine.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.java.callback.RcvOnItemViewClickListener
import com.mobile.rxjava2andretrofit2.kotlin.mine.bean.Ans

class MineAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "MineAdapter"
    //    private var context: Context? = null
    //    private var list = MutableList<MineResponse.AnsListBean>? = null
    private var list: MutableList<Ans>? = null

    init {
        list = mutableListOf()
    }

//    fun MineAdapter(context: Context) {
//        this.context = context
//    }
//
//    fun MineAdapter(context: Context, list: MutableList<MineResponse.AnsListBean>) {
//        this.context = context
//        this.list = list
//    }

    fun clearData() {
        this.list!!.clear()
        notifyDataSetChanged()
    }

    fun addAllData(list: MutableList<Ans>) {
        this.list!!.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_mine, parent, false)
        return ContentHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContentHolder) {

            holder.tevAnsid!!.text = list!![position].ansid
            holder.tevData!!.text = list!![position].content_abstract.text

            holder.tevData!!.setOnClickListener { v -> rcvOnItemViewClickListener!!.onItemClickListener(position, v) }
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    internal class ContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tevAnsid: TextView? = null
        var tevData: TextView? = null

        init {
            tevAnsid = itemView.findViewById(R.id.tev_ansid)
            tevData = itemView.findViewById(R.id.tev_data)
        }
    }

    private var rcvOnItemViewClickListener: RcvOnItemViewClickListener? = null

    fun setRcvOnItemViewClickListener(rcvOnItemViewClickListener: RcvOnItemViewClickListener) {
        this.rcvOnItemViewClickListener = rcvOnItemViewClickListener
    }
}