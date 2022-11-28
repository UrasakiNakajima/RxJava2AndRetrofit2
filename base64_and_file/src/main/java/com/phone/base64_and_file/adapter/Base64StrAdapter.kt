package com.phone.base64_and_file.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.phone.base64_and_file.R

class Base64StrAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = Base64StrAdapter::class.java.simpleName
    var base64StrList = mutableListOf<String>()

    fun clearData() {
        notifyItemRangeRemoved(0, base64StrList.size)
        notifyItemRangeChanged(0, base64StrList.size)
        base64StrList.clear()
    }

    fun addData(base64StrList: List<String>) {
        notifyItemRangeInserted(this.base64StrList.size, base64StrList.size)
        this.base64StrList.addAll(base64StrList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_base64_str, parent, false)
        return BodyHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BodyHolder) {
            holder.tevBase64Str.text = base64StrList[position]
        }
    }

    override fun getItemCount(): Int {
        return base64StrList.size
    }

    private class BodyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tevBase64Str: TextView

        init {
            tevBase64Str = itemView.findViewById<View>(R.id.tev_base64_str) as TextView
        }
    }
}