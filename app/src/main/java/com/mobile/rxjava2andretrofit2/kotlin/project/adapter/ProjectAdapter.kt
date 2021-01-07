package com.mobile.rxjava2andretrofit2.kotlin.project.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobile.rxjava2andretrofit2.callback.RcvOnItemViewClickListener
import com.mobile.rxjava2andretrofit2.kotlin.project.bean.DataX
import androidx.databinding.DataBindingUtil
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.databinding.ItemProjectBinding


class ProjectAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "ProjectAdapter"
    private var list: MutableList<DataX> = mutableListOf()

    fun clearData() {
        this.list.clear()
        notifyDataSetChanged()
    }

    fun addAllData(list: MutableList<DataX>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemProjectBinding = DataBindingUtil.inflate<ItemProjectBinding>(
                LayoutInflater.from(this.context),
                R.layout.item_project, parent,
                false)
        return ContentHolder(binding.getRoot())
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding: ItemProjectBinding = DataBindingUtil.getBinding<ItemProjectBinding>(holder.itemView)!!
        binding.datax = list.get(position)
        binding.executePendingBindings()
        if (position == 1) {
            binding.itemProjectAuthor.setVisibility(View.GONE)
        } else if (position == 2) {
            binding.itemProjectAuthor.setText("aaa")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    protected class ContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    private var rcvOnItemViewClickListener: RcvOnItemViewClickListener? = null

    fun setRcvOnItemViewClickListener(rcvOnItemViewClickListener: RcvOnItemViewClickListener) {
        this.rcvOnItemViewClickListener = rcvOnItemViewClickListener
    }

}