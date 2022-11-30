package com.phone.library_common.dialog

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
import com.phone.library_common.R
import com.phone.library_common.bean.AddressBean
import com.phone.library_common.bean.UserBean
import com.phone.library_common.callback.OnCommonSuccessCallback
import com.phone.library_common.callback.OnItemViewClick2Listener

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
            LayoutInflater.from(context).inflate(R.layout.dialog_standard_create_user, null, false)
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
        alertDialog = AlertDialog.Builder(context, R.style.standard_dialog_style)
            .setView(view)
            .create()
        tevCancel?.setOnClickListener { v: View? ->
            onItemViewClick2Listener?.onItemClickListener(
                0,
                v,
                null
            )
        }
        tevOk?.setOnClickListener { v: View? ->
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
                val addressBeanList: MutableList<AddressBean> =
                    ArrayList()
                addressBeanList.add(AddressBean("北莱茵-威斯特法伦州", "波恩"))
                addressBeanList.add(AddressBean("汉堡州", "汉堡"))
                userBean.addressBeanList = addressBeanList
                onItemViewClick2Listener?.onItemClickListener(1, v, userBean)
            } else {
                Toast.makeText(
                    context,
                    context.getResources()
                        .getString(R.string.please_fill_in_the_information_completely),
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
        if (!TextUtils.isEmpty(userBean.userId)) {
            edtUserId?.setText(userBean.userId)
        }
        if (!TextUtils.isEmpty(userBean.password)) {
            edtPassword?.setText(userBean.password)
        }
        if (!TextUtils.isEmpty(userBean.birthday)) {
            edtBirthday?.setText(userBean.birthday)
        }
        if (userBean.age != null) {
            edtAge?.setText(userBean.age.toString())
        }
        if (userBean.salary != null) {
            edtSalary?.setText(userBean.salary.toString())
        }
    }

    fun setCannotHide() {
        alertDialog?.setCancelable(false)
        alertDialog?.setCanceledOnTouchOutside(false)
    }

    fun hideStandardDialog() {
        if (alertDialog != null) {
            alertDialog?.dismiss()
            alertDialog = null
        }
    }

    private var onItemViewClick2Listener: OnItemViewClick2Listener<UserBean>? = null

    fun setOnItemViewClick2Listener(onItemViewClick2Listener: OnItemViewClick2Listener<UserBean>?) {
        this.onItemViewClick2Listener = onItemViewClick2Listener
    }

    private var onCommonSuccessCallback: OnCommonSuccessCallback? = null

    fun setOnCommonSuccessCallback(onCommonSuccessCallback: OnCommonSuccessCallback?) {
        this.onCommonSuccessCallback = onCommonSuccessCallback
    }
}