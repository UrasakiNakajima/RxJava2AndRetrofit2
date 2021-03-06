package com.phone.first_page_module.ui

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.phone.common_library.callback.OnDialogCallback
import com.phone.first_page_module.R
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
        // ????????????????????????????????????(onStart())????????????
        val window = dialog!!.window
        // ???????????????????????????, ????????????????????????????????????????????????
        //        window.setBackgroundDrawableResource(android.R.color.transparent);
        window!!.setBackgroundDrawableResource(R.drawable.corners_14_color_white)
        // ????????????
        //		window.setWindowAnimations(R.style.BottomDialogAnimation);
        val params = window.attributes
        params.gravity = Gravity.BOTTOM
        // ?????????????????????,?????????????????????????????????????????? match_parent ??????????????????
        params.width = resources.displayMetrics.widthPixels
        window.attributes = params
    }

    private var onDialogCallback: OnDialogCallback<Int>? = null

    fun setOnDialogCallback(onDialogCallback: OnDialogCallback<Int>?) {
        this.onDialogCallback = onDialogCallback
    }

}