package com.phone.project_module.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.DataBindingUtil
import com.phone.common_library.callback.OnItemViewClickListener
import com.phone.project_module.R
import com.phone.project_module.bean.DataX
import com.phone.project_module.databinding.ItemProjectBinding


class ProjectAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val TAG = ProjectAdapter::class.java.simpleName
    }
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
        return BodyHolder(binding.getRoot())
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
        binding.itemProjectImageview.setOnClickListener { v -> onItemViewClickListener!!.onItemClickListener(position, v) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class BodyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    private var onItemViewClickListener: OnItemViewClickListener? = null

    fun setRcvOnItemViewClickListener(onItemViewClickListener: OnItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener
    }

}