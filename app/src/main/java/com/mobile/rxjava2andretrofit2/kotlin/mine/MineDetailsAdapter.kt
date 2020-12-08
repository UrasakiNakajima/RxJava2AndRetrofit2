package com.mobile.rxjava2andretrofit2.kotlin.mine

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.java.callback.RcvOnItemViewClickListener
import com.mobile.rxjava2andretrofit2.java.manager.LogManager
import com.mobile.rxjava2andretrofit2.kotlin.mine.bean.Data

class MineDetailsAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG: String = "MineDetailsAdapter";
    //    private var context: Context? = null
    //    private var dataBeanList = MutableList<MineDetailsResponse.List<Data>>?=null
    private var dataBeanList: MutableList<Data> = mutableListOf()

//    fun MineDetailsAdapter(context: Context) {
//        this.context = context
//    }

//    fun MineDetailsAdapter(context: Context, dataBeanList: MutableList<MineDetailsResponse.List<Data>>) {
//        this.context = context
//        this.dataBeanList = dataBeanList
//    }

    fun clearData() {
        this.dataBeanList.clear()
        notifyDataSetChanged()
    }

    fun addAllData(dataBeanList: List<Data>) {
        this.dataBeanList.addAll(dataBeanList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_first_page_details, parent, false)
        return ContentHolder(view)
    }

    override fun getItemCount(): Int {
        return dataBeanList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContentHolder) {
            holder.tevCode!!.text = dataBeanList.get(position).code
            val data = dataBeanList.get(position).content
            LogManager.i(TAG, "onBindViewHolder data*****$data")
            holder.tevData!!.text = data
            holder.tevData!!.setOnClickListener(View.OnClickListener { v -> rcvOnItemViewClickListener!!.onItemClickListener(position, v) })
        }
    }


    internal class ContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tevCode: TextView? = null
        var tevData: TextView? = null

        init {
            tevCode = itemView.findViewById(R.id.tev_code)
            tevData = itemView.findViewById(R.id.tev_data)
        }
    }

    private var rcvOnItemViewClickListener: RcvOnItemViewClickListener? = null

    fun setRcvOnItemViewClickListener(rcvOnItemViewClickListener: RcvOnItemViewClickListener) {
        this.rcvOnItemViewClickListener = rcvOnItemViewClickListener
    }
}