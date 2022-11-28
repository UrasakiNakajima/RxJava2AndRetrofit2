package com.phone.first_page_module.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.phone.common_library.callback.OnItemViewClickListener
import com.phone.common_library.manager.LogManager.i
import com.phone.first_page_module.R
import com.phone.first_page_module.bean.VideoListBean.LargeImageListBean

class VideoListAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = VideoListAdapter::class.java.simpleName
    private var videoListBeanList: MutableList<LargeImageListBean> = ArrayList()

    fun clearData() {
        notifyItemRangeRemoved(0, videoListBeanList.size)
        notifyItemRangeChanged(0, videoListBeanList.size)
        videoListBeanList.clear()
    }

    fun addData(videoListBeanList: List<LargeImageListBean>) {
        notifyItemRangeInserted(this.videoListBeanList.size, videoListBeanList.size)
        this.videoListBeanList.addAll(videoListBeanList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_video_list, parent, false)
        return BodyHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BodyHolder) {
            //            bodyHolder.tevCode.setText(videoListBeanList.get(position).getCode());
            val data = videoListBeanList[position].url
            i(TAG, "onBindViewHolder data*****$data")

//            ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams();
            Glide.with(context).load(data).into(holder.imvData)
        }
    }

    override fun getItemCount(): Int {
        return videoListBeanList.size
    }

    private class BodyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tevId: TextView
        val imvData: ImageView

        init {
            tevId = itemView.findViewById(R.id.tev_id)
            imvData = itemView.findViewById(R.id.imv_data)
        }
    }

    private var onItemViewClickListener: OnItemViewClickListener? = null

    fun setRcvOnItemViewClickListener(onItemViewClickListener: OnItemViewClickListener?) {
        this.onItemViewClickListener = onItemViewClickListener
    }
}