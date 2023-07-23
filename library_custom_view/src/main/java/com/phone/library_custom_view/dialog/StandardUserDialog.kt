package com.phone.library_custom_view.dialog

import android.content.Context
import android.text.InputType
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.phone.library_greendao.bean.AddressBean
import com.phone.library_greendao.bean.UserBean
import com.phone.library_base.callback.OnCommonSuccessCallback
import com.phone.library_common.callback.OnItemViewClick2Listener
import com.phone.library_custom_view.R

class StandardUserDialog(val context: Context) {

    private var alertDialog: AlertDialog? = null
    private var tevTitle: TextView? = null
    private var edtUserId: EditText? = null
    private var edtPassword: EditText? = null
    private var edtBirthday: EditText? = null
    private var edtAge: EditText? = null
    private var edtSalary: EditText? = null
    private var viewHorizontalLine: View? = null
    private var tevCancel: TextView? = null
    private var viewVerticalLine: View? = null
    private var tevOk: TextView? = null

    private var userId: String? = null
    private var password: String? = null
    private var birthday: String? = null
    private var age: String? = null
    private var salary: String? = null

    init {
        val view: View =
            LayoutInflater.from(context)
                .inflate(R.layout.custom_view_dialog_standard_create_user, null, false)
        tevTitle = view.findViewById<View>(R.id.tev_title) as TextView
        edtUserId = view.findViewById<View>(R.id.edt_user_id) as EditText
        edtPassword = view.findViewById<View>(R.id.edt_password) as EditText
        edtBirthday = view.findViewById<View>(R.id.edt_birthday) as EditText
        edtAge = view.findViewById<View>(R.id.edt_age) as EditText
        edtSalary = view.findViewById<View>(R.id.edt_salary) as EditText
        viewHorizontalLine = view.findViewById(R.id.view_horizontal_line) as View
        tevCancel = view.findViewById<View>(R.id.tev_cancel) as TextView
        viewVerticalLine = view.findViewById(R.id.view_vertical_line) as View
        tevOk = view.findViewById<View>(R.id.tev_ok) as TextView
        edtPassword?.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        //设置R.style.standard_dialog_style2就可以去掉
        //AlertDialog的默认边框，此时AlertDialog的layout的宽高就是AlertDialog的宽高
        alertDialog = AlertDialog.Builder(context, R.style.library_standard_dialog_style)
            .setView(view)
            .create()
        tevCancel?.setOnClickListener { v: View ->
            onCommonSuccessCallback?.onSuccess()
        }
        tevOk?.setOnClickListener { v: View ->
            userId = edtUserId?.text.toString()
            password = edtPassword?.text.toString()
            birthday = edtBirthday?.text.toString()
            age = edtAge?.text.toString()
            salary = edtSalary?.text.toString()
            if (!TextUtils.isEmpty(userId)
                && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(birthday)
                && !TextUtils.isEmpty(age)
                && !TextUtils.isEmpty(salary)
            ) {
                val userBean = UserBean()
                userBean.userId = userId
                userBean.password = password
                userBean.age = password?.toInt()
                userBean.birthday = birthday
                userBean.salary = salary?.toDouble()
//                val userBean = UserBean()
//                userBean.userId = "13513311001"
//                userBean.password = "12345678"
//                userBean.age = "23".toInt()
//                userBean.birthday = "2000-10-10"
//                userBean.salary = "7000".toDouble()
                val addressBeanList =
                    mutableListOf<AddressBean>()
                addressBeanList.add(
                    AddressBean(
                        "北莱茵-威斯特法伦州",
                        "波恩"
                    )
                )
                addressBeanList.add(AddressBean("汉堡州", "汉堡"))
                userBean.addressBeanList = addressBeanList
                onItemViewClick2Listener?.onItemClickListener(1, v, userBean)
            } else {
                Toast.makeText(
                    context,
                    context.getResources()
                        .getString(R.string.library_please_fill_in_the_information_completely),
                    Toast.LENGTH_SHORT
                ).show();
            }
        }
        alertDialog?.setOnCancelListener { onCommonSuccessCallback?.onSuccess() }
        alertDialog?.show()
        val window = alertDialog?.window
        if (window != null) {
            window.setBackgroundDrawable(null)
            window.setGravity(Gravity.CENTER)
            //            window.setWindowAnimations(R.style.PictureThemeDialogWindowStyle);
            val params = window.attributes
            window.attributes = params
            //把 DecorView 的默认 padding 取消，同时 DecorView 的默认大小也会取消
            window.decorView.setPadding(0, 0, 0, 0)
        }
    }

    fun setTevTitle(content: String?) {
        tevTitle?.text = content
    }

    fun setTevCancelHide() {
        viewVerticalLine?.visibility = View.GONE
        tevCancel?.visibility = View.GONE
    }

    fun setUserData(userBean: UserBean) {
        userBean.userId?.let {
            edtUserId?.setText(it)
        }
        userBean.password?.let {
            edtPassword?.setText(it)
        }
        userBean.birthday?.let {
            edtBirthday?.setText(it)
        }
        userBean.age?.let {
            edtAge?.setText(it.toString())
        }
        userBean.salary?.let {
            edtSalary?.setText(it.toString())
        }
    }

    fun setCannotHide() {
        alertDialog?.setCancelable(false)
        alertDialog?.setCanceledOnTouchOutside(false)
    }

    fun hideStandardDialog() {
        alertDialog?.dismiss()
        alertDialog = null
    }

    private var onItemViewClick2Listener: OnItemViewClick2Listener<UserBean>? = null

    fun setOnItemViewClick2Listener(onItemViewClick2Listener: OnItemViewClick2Listener<UserBean>) {
        this.onItemViewClick2Listener = onItemViewClick2Listener
    }

    private var onCommonSuccessCallback: OnCommonSuccessCallback? = null

    fun setOnCommonSuccessCallback(onCommonSuccessCallback: OnCommonSuccessCallback?) {
        this.onCommonSuccessCallback = onCommonSuccessCallback
    }
}