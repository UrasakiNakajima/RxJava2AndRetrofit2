package com.phone.module_square.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.phone.module_square.R
import com.phone.module_square.bean.PictureBean

class NewBannerAdapter(val context: Context?, val list: MutableList<PictureBean>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TAG = "NewBannerAdapter"
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.square_item_image, parent, false)
        return BodyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder is BodyViewHolder) {
            if (list.isNotEmpty() && context != null) {
                Glide.with(context).load(list[position % list.size].picture).into(holder.imageView)
            }
        }
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    private class BodyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView

        init {
            imageView = itemView.findViewById<View>(R.id.item_image) as ImageView
        }
    }

}