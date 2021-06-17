package com.mobile.mine_module.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobile.common_library.callback.RcvOnItemViewClickListener
import com.mobile.mine_module.R
import com.mobile.mine_module.bean.Data

class MineAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "MineAdapter"
    private var mJuheNewsBeanList: MutableList<Data>? = null

    init {
        mJuheNewsBeanList = mutableListOf()
    }

//    fun MineAdapter(context: Context) {
//        this.context = context
//    }

    fun clearData() {
        this.mJuheNewsBeanList!!.clear()
        notifyDataSetChanged()
    }

    fun addAllData(mJuheNewsBeanList: MutableList<Data>) {
        this.mJuheNewsBeanList!!.addAll(mJuheNewsBeanList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_mine, parent, false)
        return ContentHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContentHolder) {
            val contentHolder: ContentHolder = holder as ContentHolder
            val juheNewsBean: Data = mJuheNewsBeanList!!.get(position)
            val title: String = juheNewsBean.title
            val author: String = juheNewsBean.author_name
            val time: String = juheNewsBean.date
            val imgSrc: String = juheNewsBean.thumbnail_pic_s
            val imgMid: String = juheNewsBean.thumbnail_pic_s02
            val imgRight: String = juheNewsBean.thumbnail_pic_s03

            contentHolder.newsSummaryTitleTv.setText(title)
            contentHolder.newsSummaryAuthor.setText(author)
            contentHolder.newsSummaryTime.setText(time)
            Glide.with(context).load(imgSrc).into(contentHolder.newsSummaryPhotoIvLeft)
            Glide.with(context).load(imgMid).into(contentHolder.newsSummaryPhotoIvMiddle)
            Glide.with(context).load(imgRight).into(contentHolder.newsSummaryPhotoIvRight)

            contentHolder.llRoot.setOnClickListener(View.OnClickListener { view: View? -> rcvOnItemViewClickListener!!.onItemClickListener(position, view) })
        }
    }

    override fun getItemCount(): Int {
        return mJuheNewsBeanList!!.size
    }

    protected class ContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
            newsSummaryPhotoIvGroup = itemView.findViewById<View>(R.id.news_summary_photo_iv_group) as LinearLayout
            newsSummaryPhotoIvLeft = itemView.findViewById<View>(R.id.news_summary_photo_iv_left) as ImageView
            newsSummaryPhotoIvMiddle = itemView.findViewById<View>(R.id.news_summary_photo_iv_middle) as ImageView
            newsSummaryPhotoIvRight = itemView.findViewById<View>(R.id.news_summary_photo_iv_right) as ImageView
            newsSummaryAuthor = itemView.findViewById<View>(R.id.news_summary_author) as TextView
            newsSummaryTime = itemView.findViewById<View>(R.id.news_summary_time) as TextView
        }
    }

    private var rcvOnItemViewClickListener: RcvOnItemViewClickListener? = null

    fun setRcvOnItemViewClickListener(rcvOnItemViewClickListener: RcvOnItemViewClickListener) {
        this.rcvOnItemViewClickListener = rcvOnItemViewClickListener
    }
}