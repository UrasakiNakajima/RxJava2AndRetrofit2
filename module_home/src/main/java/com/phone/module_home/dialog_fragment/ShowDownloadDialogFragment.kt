package com.phone.module_home.dialog_fragment

import com.phone.common_library.base.BaseBindingDialogFragment
import com.phone.module_home.R
import com.phone.module_home.databinding.DialogFragmentShowDownloadBinding

/**
 *    author : Urasaki
 *    e-mail : 1164688204@qq.com
 *    date   : 2021/5/17 10:28
 *    desc   :
 *    version: 1.0
 */
class ShowDownloadDialogFragment : BaseBindingDialogFragment<DialogFragmentShowDownloadBinding>() {

    companion object {
        private const val TAG = "ShowDownloadDialogFragment"

        fun newInstance(): ShowDownloadDialogFragment {
            val fragment = ShowDownloadDialogFragment()
            return fragment
        }
    }

    override fun initLayoutId() = R.layout.dialog_fragment_show_download

    override fun initData() {

    }

    override fun initViews() {
        mDatabind.let {
            it.tevSaveToPhone.setOnClickListener {
                getOnDialogCallback()?.onDialogClick(it, 1)
                dismiss()
            }
            it.tevCancel.setOnClickListener {
                getOnDialogCallback()?.onDialogClick(it, 0)
                dismiss()
            }
        }
    }

    override fun initLoadData() {

    }

}