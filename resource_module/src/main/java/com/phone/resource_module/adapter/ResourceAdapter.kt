package com.phone.resource_module.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.phone.common_library.callback.OnItemViewClickListener
import com.phone.common_library.common.Constants
import com.phone.resource_module.R
import com.phone.resource_module.bean.ArticleListBean
import com.phone.resource_module.databinding.ItemHomeArticleBinding
import com.phone.resource_module.databinding.ItemResourceBinding

class ResourceAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val TAG = ResourceAdapter::class.java.simpleName
    }

    var list: MutableList<ArticleListBean> = mutableListOf()

    fun clearData() {
        notifyItemRangeRemoved(0, this.list.size)
        this.list.clear()
    }

    fun addData(list: MutableList<ArticleListBean>) {
        notifyItemRangeInserted(this.list.size, list.size)
        this.list.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constants.ITEM_ARTICLE) {
            val binding: ItemHomeArticleBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.item_home_article,
                parent,
                false
            )
            ArticleViewHolder(binding.root)
        } else {
            val binding: ItemResourceBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.item_resource,
                parent,
                false
            )
            ArticlePicViewHolder(binding.root)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ArticleViewHolder) {
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
        } else {
            //获取ViewDataBinding
            val binding = DataBindingUtil.getBinding<ItemResourceBinding>(holder.itemView)?.apply {
                dataBean = list[position]
            }
            binding?.executePendingBindings()
        }

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

        lateinit var root: RelativeLayout
        lateinit var tvTag: TextView
        lateinit var tvAuthor: TextView
        lateinit var tvDate: TextView
        lateinit var tvTitle: TextView
        lateinit var tvChapterName: TextView
        lateinit var ivCollect: ImageView

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

    /**
     * 带图片viewHolder
     */
    class ArticlePicViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        lateinit var root: ConstraintLayout
        lateinit var ivTitle: ImageView
        lateinit var tvTitle: TextView
        lateinit var tvDes: TextView
        lateinit var tvNameData: TextView
        lateinit var ivCollect: ImageView

        init {
            root = itemView.findViewById(R.id.root)
            ivTitle = itemView.findViewById(R.id.ivTitle)
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvDes = itemView.findViewById(R.id.tvDes)
            tvNameData = itemView.findViewById(R.id.tvNameData)
            ivCollect = itemView.findViewById(R.id.ivCollect)
        }

    }

    private var onItemViewClickListener: OnItemViewClickListener? = null

    fun setRcvOnItemViewClickListener(onItemViewClickListener: OnItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener
    }

}