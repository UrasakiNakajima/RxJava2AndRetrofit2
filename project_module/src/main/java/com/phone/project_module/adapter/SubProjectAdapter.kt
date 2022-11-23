package com.phone.project_module.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.DataBindingUtil
import com.phone.common_library.bean.ArticleListBean
import com.phone.common_library.callback.OnItemViewClickListener
import com.phone.common_library.common.Constants
import com.phone.project_module.R
import com.phone.project_module.databinding.ItemProjectBinding


class SubProjectAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val TAG = SubProjectAdapter::class.java.simpleName
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
        val binding: ItemProjectBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.item_project,
            parent,
            false
        )
        return ArticlePicViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.clickNoRepeat {
            onItemViewClickListener?.onItemClickListener(position, it)
        }
//        //收藏
//        holder.itemView.findViewById<View>(R.id.ivCollect)?.clickNoRepeat {
//            onItemChildClickListener?.invoke(position,it)
//        }

        //获取ViewDataBinding
        val binding = DataBindingUtil.getBinding<ItemProjectBinding>(holder.itemView)?.apply {
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

    /**
     * 防止重复点击
     * @param interval 重复间隔
     * @param onClick  事件响应
     */
    var lastTime = 0L
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
     * 带图片viewHolder
     */
    class ArticlePicViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var root: ConstraintLayout
        var ivTitle: ImageView
        var tvTitle: TextView
        var tvDes: TextView
        var tvNameData: TextView
        var ivCollect: ImageView

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