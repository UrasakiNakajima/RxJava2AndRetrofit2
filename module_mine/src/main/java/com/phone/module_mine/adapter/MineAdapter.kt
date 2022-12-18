package com.phone.module_mine.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.phone.library_common.callback.OnItemViewClickListener
import com.phone.module_mine.R
import com.phone.module_mine.bean.Data

class MineAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val TAG = MineAdapter::class.java.simpleName
    }

    val mJuheNewsBeanList = mutableListOf<Data>()

    fun clearData() {
        notifyItemRangeRemoved(0, this.mJuheNewsBeanList.size)
        notifyItemRangeChanged(0, this.mJuheNewsBeanList.size)
        this.mJuheNewsBeanList.clear()
    }

    fun addData(mJuheNewsBeanList: MutableList<Data>) {
        notifyItemRangeInserted(this.mJuheNewsBeanList.size, mJuheNewsBeanList.size)
        this.mJuheNewsBeanList.addAll(mJuheNewsBeanList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_mine, parent, false)
        return BodyHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BodyHolder) {
            val bodyHolder: BodyHolder = holder as BodyHolder
            val juheNewsBean: Data = mJuheNewsBeanList.get(position)
            val title: String = juheNewsBean.title
            val author: String = juheNewsBean.author_name
            val time: String = juheNewsBean.date
            val imgSrc: String = juheNewsBean.thumbnail_pic_s
            val imgMid: String = juheNewsBean.thumbnail_pic_s02
            val imgRight: String = juheNewsBean.thumbnail_pic_s03

            bodyHolder.newsSummaryTitleTv.setText(title)
            bodyHolder.newsSummaryAuthor.setText(author)
            bodyHolder.newsSummaryTime.setText(time)
            Glide.with(context).load(imgSrc).into(bodyHolder.newsSummaryPhotoIvLeft)
            Glide.with(context).load(imgMid).into(bodyHolder.newsSummaryPhotoIvMiddle)
            Glide.with(context).load(imgRight).into(bodyHolder.newsSummaryPhotoIvRight)

            bodyHolder.llRoot.setOnClickListener(View.OnClickListener { view: View? ->
                onItemViewClickListener.onItemClickListener(
                    position,
                    view
                )
            })
        }
    }

    override fun getItemCount(): Int {
        return mJuheNewsBeanList.size
    }

    private class BodyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val llRoot: LinearLayout
        val newsSummaryTitleTv: TextView
        val newsSummaryPhotoIvGroup: LinearLayout
        val newsSummaryPhotoIvLeft: ImageView
        val newsSummaryPhotoIvMiddle: ImageView
        val newsSummaryPhotoIvRight: ImageView
        val newsSummaryAuthor: TextView
        val newsSummaryTime: TextView

        init {
            llRoot = itemView.findViewById<View>(R.id.ll_root) as LinearLayout
            newsSummaryTitleTv = itemView.findViewById<View>(R.id.news_summary_title_tv) as TextView
            newsSummaryPhotoIvGroup =
                itemView.findViewById<View>(R.id.news_summary_photo_iv_group) as LinearLayout
            newsSummaryPhotoIvLeft =
                itemView.findViewById<View>(R.id.news_summary_photo_iv_left) as ImageView
            newsSummaryPhotoIvMiddle =
                itemView.findViewById<View>(R.id.news_summary_photo_iv_middle) as ImageView
            newsSummaryPhotoIvRight =
                itemView.findViewById<View>(R.id.news_summary_photo_iv_right) as ImageView
            newsSummaryAuthor = itemView.findViewById<View>(R.id.news_summary_author) as TextView
            newsSummaryTime = itemView.findViewById<View>(R.id.news_summary_time) as TextView
        }
    }

    private lateinit var onItemViewClickListener: OnItemViewClickListener

    fun setOnItemViewClickListener(onItemViewClickListener: OnItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener
    }
}