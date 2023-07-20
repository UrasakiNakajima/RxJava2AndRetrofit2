package com.phone.library_custom_view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.DataBindingUtil
import com.phone.library_common.callback.OnItemViewClickListener
import com.phone.library_base.common.Constants
import com.phone.library_custom_view.bean.ArticleListBean
import com.phone.library_custom_view.R
import com.phone.library_custom_view.databinding.CustomViewItemProjectBinding
import com.phone.library_custom_view.databinding.CustomViewItemResourceBinding

class ProjectAndResourceAdapter(val context: Context, val list: MutableList<ArticleListBean>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val TAG = ProjectAndResourceAdapter::class.java.simpleName
    }
    var lastTime = 0L

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
        return if (viewType == Constants.ITEM_ARTICLE) {
            val binding: CustomViewItemResourceBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.custom_view_item_resource,
                parent,
                false
            )
            ArticleViewHolder(binding.root)
        } else {
            val binding: CustomViewItemProjectBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.custom_view_item_project,
                parent,
                false
            )
            ArticlePicViewHolder(binding.root)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.findViewById<View>(R.id.root)?.clickNoRepeat {
            onItemViewClickListener?.onItemClickListener(position, it)
        }
        //收藏
        holder.itemView.findViewById<View>(R.id.imv_collect)?.clickNoRepeat {
            onItemViewClickListener?.onItemClickListener(position, it)
        }

        val binding = if (holder is ArticleViewHolder) {
            //获取ViewDataBinding
            DataBindingUtil.getBinding<CustomViewItemResourceBinding>(holder.itemView)?.apply {
                dataBean = list[position]
            }
        } else {
            DataBindingUtil.getBinding<CustomViewItemProjectBinding>(holder.itemView)?.apply {
                dataBean = list[position]
            }
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
     * 防止重复点击
     * @param interval 重复间隔
     * @param onClick  事件响应
     */
    fun View.clickNoRepeat(interval: Long = 400, onClick: (View) -> Unit) {
        setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (lastTime != 0L && (currentTime - lastTime < interval)) {
                return@setOnClickListener
            }
            lastTime = currentTime
            onClick(it)
        }
    }

    /**
     * 默认viewHolder
     */
    class ArticleViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    /**
     * 带图片viewHolder
     */
    class ArticlePicViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    private var onItemViewClickListener: OnItemViewClickListener? = null

    fun setOnItemViewClickListener(onItemViewClickListener: OnItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener
    }

}