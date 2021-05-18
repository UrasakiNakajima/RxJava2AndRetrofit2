package com.mobile.first_page_module.ui

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mobile.common_library.callback.OnDialogCallback
import com.mobile.first_page_module.R
import kotlinx.android.synthetic.main.dialog_fragment_show_download.*

/**
 *    author : Urasaki
 *    e-mail : 1164688204@qq.com
 *    date   : 2021/5/17 10:28
 *    desc   :
 *    version: 1.0
 */
class ShowDownloadDialogFragment : DialogFragment() {

    companion object {
        private const val TAG = "ShowDownloadDialogFragment"

        //        fun newInstance(dateStr: String?): ShowDownloadDialogFragment? {
//            val args = Bundle()
//            args.putString("dateStr", dateStr)
//            val fragment = ShowDownloadDialogFragment()
//            fragment.arguments = args
//            return fragment
//        }
        fun newInstance(): ShowDownloadDialogFragment? {
            val fragment = ShowDownloadDialogFragment()
            return fragment
        }
    }

    private var viewRoot: View? = null
//    private var bundle: Bundle? = null
//    private var url: String? = null
//    private var suffix: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewRoot = inflater.inflate(R.layout.dialog_fragment_show_download, container, false)
        return viewRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        bundle = arguments;
//        if (bundle != null) {
//            url = bundle!!.getString("url");
//            suffix = bundle!!.getString("suffix");
//        }
        tev_save_to_phone.setOnClickListener(View.OnClickListener {
            onDialogCallback!!.onDialogClick(tev_save_to_phone, 1)
            dismiss()
        })
        tev_cancel.setOnClickListener {
            onDialogCallback!!.onDialogClick(tev_cancel, 0)
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        // 下面这些设置必须在此方法(onStart())中才有效
        val window = dialog!!.window
        // 如果不设置这句代码, 那么弹框就会与四边都有一定的距离
        //        window.setBackgroundDrawableResource(android.R.color.transparent);
        window!!.setBackgroundDrawableResource(R.drawable.corners_14_color_white)
        // 设置动画
        //		window.setWindowAnimations(R.style.BottomDialogAnimation);
        val params = window.attributes
        params.gravity = Gravity.BOTTOM
        // 如果不设置宽度,那么即使你在布局中设置宽度为 match_parent 也不会起作用
        params.width = resources.displayMetrics.widthPixels
        window.attributes = params
    }

    private var onDialogCallback: OnDialogCallback<Int>? = null

    fun setOnDialogCallback(onDialogCallback: OnDialogCallback<Int>?) {
        this.onDialogCallback = onDialogCallback
    }

}