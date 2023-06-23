package com.phone.module_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.phone.library_common.bean.HomePageResponse.ResultData.JuheNewsBean
import com.phone.library_common.callback.OnItemViewClickListener
import com.phone.module_home.R

class HomeAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        @JvmStatic
        private val TAG = HomeAdapter::class.java.simpleName
    }

    var mJuheNewsBeanList: MutableList<JuheNewsBean> = ArrayList()

    fun clearData() {
        notifyItemRangeRemoved(0, mJuheNewsBeanList.size)
        notifyItemRangeChanged(0, mJuheNewsBeanList.size)
        mJuheNewsBeanList.clear()
    }

    fun addData(dataBeanList: List<JuheNewsBean>) {
        notifyItemRangeInserted(mJuheNewsBeanList.size, dataBeanList.size)
        mJuheNewsBeanList.addAll(dataBeanList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.home_item_first_page, parent, false)
        return BodyHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BodyHolder) {
            val bodyHolder = holder
            val juheNewsBean = mJuheNewsBeanList[position]
            val title = juheNewsBean.title
            val author = juheNewsBean.author_name
            val time = juheNewsBean.date
            val imgSrc = juheNewsBean.thumbnail_pic_s
            val imgMid = juheNewsBean.thumbnail_pic_s02
            val imgRight = juheNewsBean.thumbnail_pic_s03
            bodyHolder.newsSummaryTitleTv.text = title
            bodyHolder.newsSummaryAuthor.text = author
            bodyHolder.newsSummaryTime.text = time
            Glide.with(context).load(imgSrc).into(bodyHolder.newsSummaryPhotoIvLeft)
            Glide.with(context).load(imgMid).into(bodyHolder.newsSummaryPhotoIvMiddle)
            Glide.with(context).load(imgRight).into(bodyHolder.newsSummaryPhotoIvRight)
            bodyHolder.llRoot.setOnClickListener { view: View? ->
                onItemViewClickListener?.onItemClickListener(
                    position,
                    view
                )
            }
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

    private var onItemViewClickListener: OnItemViewClickListener? = null

    fun setOnItemViewClickListener(onItemViewClickListener: OnItemViewClickListener?) {
        this.onItemViewClickListener = onItemViewClickListener
    }

}