package com.phone.library_base.manager

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.phone.library_base.R
import com.phone.library_base.callback.OnCommonSingleParamCallback
import com.qmuiteam.qmui.widget.QMUILoadingView
import com.qmuiteam.qmui.widget.QMUIProgressBar


class DialogManager {

    var dialog: AlertDialog? = null
    var mProgressBar: QMUIProgressBar? = null
    var mLoadingView: QMUILoadingView? = null

    fun showProgressBarDialog(context: Context) {
        val builder = AlertDialog.Builder(context, R.style.base_standard_dialog_style)
        val view = LayoutInflater.from(context)
            .inflate(R.layout.base_layout_progress_bar_dialog_view, null)
        builder.setView(view)
        mProgressBar = view.findViewById<View>(R.id.progress_bar) as QMUIProgressBar
        //设置文字样式  百分之样式 如 10%
        mProgressBar?.setQMUIProgressBarTextGenerator({ progressBar, value, maxValue -> (100 * value / maxValue).toString() + "%" })
        builder.setCancelable(false)
        dialog = builder.create()
        dialog?.show()

        val window: Window? = dialog?.getWindow()
//        window?.setBackgroundDrawableResource(android.R.color.transparent);
        // 取消这些边框的关键代码
        window?.setBackgroundDrawable(null)
        window?.setGravity(Gravity.CENTER)
        //            window.setWindowAnimations(R.style.PictureThemeDialogWindowStyle);
        val params = window?.attributes
        window?.attributes = params
        //把 DecorView 的默认 padding 取消，同时 DecorView 的默认大小也会取消
        window?.decorView?.setPadding(0, 0, 0, 0)
    }

    fun onProgress(progress: Int) {
        mProgressBar?.progress = progress
    }

    fun showLoadingDialog(context: Context) {
        val builder = AlertDialog.Builder(context, R.style.base_standard_dialog_style)
        val view =
            LayoutInflater.from(context).inflate(R.layout.base_layout_loading_dialog_view, null)
        builder.setView(view)
        mLoadingView = view.findViewById<View>(R.id.loading_view) as QMUILoadingView
        mLoadingView?.start()
        builder.setCancelable(false)
        dialog = builder.create()
        dialog?.show()

        val window: Window? = dialog?.getWindow()
        //            window.setBackgroundDrawableResource(android.R.color.transparent);
        // 取消这些边框的关键代码
        window?.setBackgroundDrawable(null)
        window?.setGravity(Gravity.CENTER)
        //            window.setWindowAnimations(R.style.PictureThemeDialogWindowStyle);
        val params = window?.attributes
        window?.attributes = params
        //把 DecorView 的默认 padding 取消，同时 DecorView 的默认大小也会取消
        window?.decorView?.setPadding(0, 0, 0, 0)
    }

    fun showCommonDialog(
        context: Context,
        title: String,
        content: String,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        val builder = AlertDialog.Builder(context, R.style.base_standard_dialog_style)
        val view =
            LayoutInflater.from(context).inflate(R.layout.base_layout_common_dialog_view, null)
        builder.setView(view)
        val tevTitle = view.findViewById<View>(R.id.tev_title) as TextView
        val tevContent = view.findViewById<View>(R.id.tev_content) as TextView
        val tevCancel = view.findViewById<View>(R.id.tev_cancel) as TextView
        val tevConfirm = view.findViewById<View>(R.id.tev_confirm) as TextView
        tevTitle.text = title
        tevContent.text = content
        tevCancel.setOnClickListener {
            onCommonSingleParamCallback.onError(tevCancel.text.toString())
        }
        tevConfirm.setOnClickListener {
            onCommonSingleParamCallback.onSuccess(tevConfirm.text.toString())
        }
        builder.setCancelable(false)
        dialog = builder.create()
        dialog?.show()

        val window: Window? = dialog?.getWindow()
        //            window.setBackgroundDrawableResource(android.R.color.transparent);
        // 取消这些边框的关键代码
        window?.setBackgroundDrawable(null)
        window?.setGravity(Gravity.CENTER)
        //            window.setWindowAnimations(R.style.PictureThemeDialogWindowStyle);
        val params = window?.attributes
        window?.attributes = params
        //把 DecorView 的默认 padding 取消，同时 DecorView 的默认大小也会取消
        window?.decorView?.setPadding(0, 0, 0, 0)
    }

    fun dismissProgressBarDialog() {
        dialog?.dismiss()
        dialog = null
    }

    fun dismissLoadingDialog() {
        mLoadingView?.stop()
        dialog?.dismiss()
        dialog = null
    }

    fun dismissCommonDialog() {
        dialog?.dismiss()
        dialog = null
    }

}