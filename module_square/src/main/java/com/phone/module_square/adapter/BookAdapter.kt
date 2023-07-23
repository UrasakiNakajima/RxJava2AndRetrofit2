package com.phone.module_square.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.phone.library_room.Book
import com.phone.module_square.R
import com.phone.module_square.databinding.SquareItemBookBinding


class BookAdapter(val context: Context, val bookList: MutableList<Book>) :
    RecyclerView.Adapter<ViewHolder>() {

    fun clearData() {
        notifyItemRangeRemoved(0, this.bookList.size)
        notifyItemRangeChanged(0, this.bookList.size)
        this.bookList.clear()
    }

    fun addData(bookList: MutableList<Book>) {
        notifyItemRangeInserted(this.bookList.size, bookList.size)
        this.bookList.addAll(bookList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: SquareItemBookBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.square_item_book, parent, false
        )
        return ContentHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //获取ViewDataBinding
        val binding = DataBindingUtil.getBinding<SquareItemBookBinding>(holder.itemView)?.apply {
            dataBean = bookList.get(position)
        }
        binding?.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    private class ContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


}