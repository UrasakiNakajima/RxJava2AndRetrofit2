package com.phone.resource_module.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.phone.common_library.bean.ArticleListBean
import com.phone.common_library.callback.OnItemViewClickListener
import com.phone.common_library.common.Constants
import com.phone.common_library.manager.LogManager
import com.phone.resource_module.R
import com.phone.resource_module.databinding.ItemHomeArticleBinding

class SubResourceAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val TAG = SubResourceAdapter::class.java.simpleName
    }

    var list: MutableList<ArticleListBean> = mutableListOf()

    fun clearData() {
        notifyItemRangeRemoved(0, this.list.size)
        notifyItemRangeChanged(0, this.list.size)
        this.list.clear()


//        this.list.clear()
//        notifyDataSetChanged()
    }

    fun addData(list: MutableList<ArticleListBean>) {
        notifyItemRangeInserted(this.list.size, list.size)
        this.list.addAll(list)

//        this.list.addAll(list)
//        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemHomeArticleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.item_home_article,
            parent,
            false
        )
        return ArticleViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //            val articleViewHolder = holder as ArticleViewHolder
//            //收藏
//            articleViewHolder.ivCollect.setOnClickListener {
//
//            }

        //获取ViewDataBinding
        val binding =
            DataBindingUtil.getBinding<ItemHomeArticleBinding>(holder.itemView)?.apply {
                dataBean = list[position]
            }
        binding?.executePendingBindings()
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].getItemType() == Constants.ITEM_ARTICLE) {
            //普通类型
            Constants.ITEM_ARTICLE
        } else {
            //带图片类型
            Constants.ITEM_ARTICLE_PIC
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * 默认viewHolder
     */
    class ArticleViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var root: RelativeLayout
        var tvTag: TextView
        var tvAuthor: TextView
        var tvDate: TextView
        var tvTitle: TextView
        var tvChapterName: TextView
        var ivCollect: ImageView

        init {
            root = itemView.findViewById(R.id.root)
            tvTag = itemView.findViewById(R.id.tvTag)
            tvAuthor = itemView.findViewById(R.id.tvAuthor)
            tvDate = itemView.findViewById(R.id.tvDate)
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvChapterName = itemView.findViewById(R.id.tvChapterName)
            ivCollect = itemView.findViewById(R.id.ivCollect)
        }
    }

    private var onItemViewClickListener: OnItemViewClickListener? = null

    fun setRcvOnItemViewClickListener(onItemViewClickListener: OnItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener
    }

}