package com.phone.module_square.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.phone.library_common.bean.UserBean
import com.phone.library_common.callback.OnItemViewClickListener
import com.phone.library_common.manager.ResourcesManager.getString
import com.phone.module_square.R

class UserBeanAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = UserBeanAdapter::class.java.simpleName
    private val TYPE_HEADER = 0
    private val TYPE_BODY = 1
    private val TYPE_FOOTER = 2
    val userBeanList = mutableListOf<UserBean>()

    fun clearData() {
        notifyItemRangeRemoved(0, userBeanList.size)
        notifyItemRangeChanged(0, userBeanList.size)
        userBeanList.clear()
    }

    fun addData(userBeanList: List<UserBean>) {
        notifyItemRangeInserted(this.userBeanList.size, userBeanList.size)
        this.userBeanList.addAll(userBeanList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(context).inflate(R.layout.item_user_header, parent, false)
            HeaderHolder(view)
        } else if (viewType == TYPE_FOOTER) {
            view = LayoutInflater.from(context).inflate(R.layout.item_user_footer, parent, false)
            FooterHolder(view)
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_user_body, parent, false)
            BodyHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderHolder) {
            val headerHolder = holder
            if (!TextUtils.isEmpty(userBeanList[position].userId)) {
                headerHolder.tevUserId.text =
                    getString(R.string.user_id_b) + userBeanList[position].userId
            } else {
                headerHolder.tevUserId.text = getString(R.string.user_id_b)
            }
            if (!TextUtils.isEmpty(userBeanList[position].birthday)) {
                headerHolder.tevBirthday.text =
                    getString(R.string.birthday_b) + userBeanList[position].birthday
            } else {
                headerHolder.tevBirthday.text = getString(R.string.birthday_b)
            }
            if (userBeanList[position].salary != null) {
                headerHolder.tevSalary.text =
                    getString(R.string.salary_b) + userBeanList[position].salary
            } else {
                headerHolder.tevSalary.text = getString(R.string.salary_b)
            }
            if (userBeanList[position].addressBeanList != null && userBeanList[position].addressBeanList.size > 0) {
                if (userBeanList[position].addressBeanList[0] != null) {
                    headerHolder.tevAddress.text = (getString(R.string.address_b)
                            + userBeanList[position].addressBeanList[0].city)
                }
            } else {
                headerHolder.tevAddress.text = getString(R.string.address_b)
            }
            headerHolder.tevUpdate.setOnClickListener { v: View? ->
                onItemViewClickListener?.onItemClickListener(
                    position,
                    v
                )
            }
            headerHolder.tevDelete.setOnClickListener { v: View? ->
                onItemViewClickListener?.onItemClickListener(
                    position,
                    v
                )
            }
        } else if (holder is FooterHolder) {
            val footerHolder = holder
            if (!TextUtils.isEmpty(userBeanList[position].userId)) {
                footerHolder.tevUserId.text =
                    getString(R.string.user_id_b) + userBeanList[position].userId
            } else {
                footerHolder.tevUserId.text = getString(R.string.user_id_b)
            }
            if (!TextUtils.isEmpty(userBeanList[position].birthday)) {
                footerHolder.tevBirthday.text =
                    getString(R.string.birthday_b) + userBeanList[position].birthday
            } else {
                footerHolder.tevBirthday.text = getString(R.string.birthday_b)
            }
            if (userBeanList[position].salary != null) {
                footerHolder.tevSalary.text =
                    getString(R.string.salary_b) + userBeanList[position].salary
            } else {
                footerHolder.tevSalary.text = getString(R.string.salary_b)
            }
            if (userBeanList[position].addressBeanList != null && userBeanList[position].addressBeanList.size > 0) {
                footerHolder.tevAddress.text = (getString(R.string.address_b)
                        + userBeanList[position].addressBeanList[0].city)
            } else {
                footerHolder.tevAddress.text = getString(R.string.address_b)
            }
            footerHolder.tevUpdate.setOnClickListener { v: View? ->
                onItemViewClickListener?.onItemClickListener(
                    position,
                    v
                )
            }
            footerHolder.tevDelete.setOnClickListener { v: View? ->
                onItemViewClickListener?.onItemClickListener(
                    position,
                    v
                )
            }
        } else {
            val bodyHolder = holder as BodyHolder
            if (!TextUtils.isEmpty(userBeanList[position].userId)) {
                bodyHolder.tevUserId.text =
                    getString(R.string.user_id_b) + userBeanList[position].userId
            } else {
                bodyHolder.tevUserId.text = getString(R.string.user_id_b)
            }
            if (!TextUtils.isEmpty(userBeanList[position].birthday)) {
                bodyHolder.tevBirthday.text =
                    getString(R.string.birthday_b) + userBeanList[position].birthday
            } else {
                bodyHolder.tevBirthday.text = getString(R.string.birthday_b)
            }
            if (userBeanList[position].salary != null) {
                bodyHolder.tevSalary.text =
                    getString(R.string.salary_b) + userBeanList[position].salary
            } else {
                bodyHolder.tevSalary.text = getString(R.string.salary_b)
            }
            if (userBeanList[position].addressBeanList != null && userBeanList[position].addressBeanList.size > 0) {
                bodyHolder.tevAddress.text = (getString(R.string.address_b)
                        + userBeanList[position].addressBeanList[0].city)
            } else {
                bodyHolder.tevAddress.text = getString(R.string.address_b)
            }
            bodyHolder.tevUpdate.setOnClickListener { v: View? ->
                onItemViewClickListener?.onItemClickListener(
                    position,
                    v
                )
            }
            bodyHolder.tevDelete.setOnClickListener { v: View? ->
                onItemViewClickListener?.onItemClickListener(
                    position,
                    v
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_HEADER
        } else if (position == userBeanList.size - 1) {
            TYPE_FOOTER
        } else {
            TYPE_BODY
        }
    }

    override fun getItemCount(): Int {
        return userBeanList.size
    }

    private class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tevUserId: TextView
        val tevBirthday: TextView
        val tevUpdate: TextView
        val tevSalary: TextView
        val tevAddress: TextView
        val tevDelete: TextView

        init {
            tevUserId = itemView.findViewById<View>(R.id.tev_user_id) as TextView
            tevBirthday = itemView.findViewById<View>(R.id.tev_birthday) as TextView
            tevUpdate = itemView.findViewById<View>(R.id.tev_update) as TextView
            tevSalary = itemView.findViewById<View>(R.id.tev_salary) as TextView
            tevAddress = itemView.findViewById<View>(R.id.tev_address) as TextView
            tevDelete = itemView.findViewById<View>(R.id.tev_delete) as TextView
        }
    }

    private class BodyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tevUserId: TextView
        val tevBirthday: TextView
        val tevUpdate: TextView
        val tevSalary: TextView
        val tevAddress: TextView
        val tevDelete: TextView

        init {
            tevUserId = itemView.findViewById<View>(R.id.tev_user_id) as TextView
            tevBirthday = itemView.findViewById<View>(R.id.tev_birthday) as TextView
            tevUpdate = itemView.findViewById<View>(R.id.tev_update) as TextView
            tevSalary = itemView.findViewById<View>(R.id.tev_salary) as TextView
            tevAddress = itemView.findViewById<View>(R.id.tev_address) as TextView
            tevDelete = itemView.findViewById<View>(R.id.tev_delete) as TextView
        }
    }

    private class FooterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tevUserId: TextView
        val tevBirthday: TextView
        val tevUpdate: TextView
        val tevSalary: TextView
        val tevAddress: TextView
        val tevDelete: TextView

        init {
            tevUserId = itemView.findViewById<View>(R.id.tev_user_id) as TextView
            tevBirthday = itemView.findViewById<View>(R.id.tev_birthday) as TextView
            tevUpdate = itemView.findViewById<View>(R.id.tev_update) as TextView
            tevSalary = itemView.findViewById<View>(R.id.tev_salary) as TextView
            tevAddress = itemView.findViewById<View>(R.id.tev_address) as TextView
            tevDelete = itemView.findViewById<View>(R.id.tev_delete) as TextView
        }
    }

    private var onItemViewClickListener: OnItemViewClickListener? = null

    fun setOnItemViewClickListener(onItemViewClickListener: OnItemViewClickListener?) {
        this.onItemViewClickListener = onItemViewClickListener
    }
}