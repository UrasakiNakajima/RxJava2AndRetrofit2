package com.phone.library_common.dialog

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.phone.library_common.R
import com.phone.library_common.callback.OnCommonSuccessCallback
import com.phone.library_common.callback.OnItemViewClickListener

class StandardDialog(val context: Context) {

    private var alertDialog: AlertDialog? = null
    private var tevTitle: TextView? = null
    private var tevContent: TextView? = null
    private var viewHorizontalLine: View? = null
    private var tevCancel: TextView? = null
    private var viewVerticalLine: View? = null
    private var tevOk: TextView? = null

    init {
        val view: View = LayoutInflater.from(context).inflate(R.layout.library_dialog_standard, null, false)
        tevTitle = view.findViewById<View>(R.id.tev_title) as TextView
        tevContent = view.findViewById<View>(R.id.tev_content) as TextView
        viewHorizontalLine = view.findViewById(R.id.view_horizontal_line) as View
        tevCancel = view.findViewById<View>(R.id.tev_cancel) as TextView
        viewVerticalLine = view.findViewById(R.id.view_vertical_line) as View
        tevOk = view.findViewById<View>(R.id.tev_ok) as TextView


        //设置R.style.standard_dialog_style就可以去掉
        //AlertDialog的默认边框，此时AlertDialog的layout的宽高就是AlertDialog的宽高
        alertDialog = AlertDialog.Builder(context, R.style.library_standard_dialog_style)
            .setView(view)
            .create()
        tevCancel?.setOnClickListener { v: View? ->
            onItemViewClickListener?.onItemClickListener(
                0,
                v
            )
        }
        tevOk?.setOnClickListener { v: View? ->
            onItemViewClickListener?.onItemClickListener(
                1,
                v
            )
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

    fun setTevContent(content: String?) {
        tevContent?.text = content
    }

    fun setTevCancelHide() {
        viewVerticalLine?.visibility = View.GONE
        tevCancel?.visibility = View.GONE
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

    private var onItemViewClickListener: OnItemViewClickListener? = null

    fun setOnItemViewClickListener(onItemViewClickListener: OnItemViewClickListener?) {
        this.onItemViewClickListener = onItemViewClickListener
    }

    private var onCommonSuccessCallback: OnCommonSuccessCallback? = null

    fun setOnCommonSuccessCallback(onCommonSuccessCallback: OnCommonSuccessCallback?) {
        this.onCommonSuccessCallback = onCommonSuccessCallback
    }
}