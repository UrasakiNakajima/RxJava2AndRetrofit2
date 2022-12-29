package com.phone.base64_and_file.presenter

import android.content.Context
import com.phone.base64_and_file.bean.Base64AndFileBean
import com.phone.base64_and_file.model.Base64AndFileModelImpl
import com.phone.base64_and_file.view.IBase64AndFileView
import com.phone.library_common.base.BasePresenter
import com.phone.library_common.base.IBaseView
import com.phone.library_common.callback.OnCommonSingleParamCallback

class Base64AndFilePresenterImpl(baseView: IBaseView) : BasePresenter<IBaseView>(),
    IBase64AndFilePresenter {

    private val TAG = Base64AndFilePresenterImpl::class.java.simpleName
    private var model: Base64AndFileModelImpl

    init {
        attachView(baseView)
        model = Base64AndFileModelImpl()
    }

    override fun showCompressedPicture(
        context: Context,
        base64AndFileBean: Base64AndFileBean
    ) {
        val baseView = obtainView()
        baseView?.let {
            if (baseView is IBase64AndFileView) {
                val base64AndFileView = baseView
                model.showCompressedPicture(
                    context,
                    base64AndFileBean,
                    object : OnCommonSingleParamCallback<Base64AndFileBean> {
                        override fun onSuccess(success: Base64AndFileBean) {
                            base64AndFileView.showCompressedPictureSuccess(success)
                        }

                        override fun onError(error: String) {
                            base64AndFileView.showCompressedPictureError(error)
                        }
                    })
            }
        }
    }

    override fun showPictureToBase64(base64AndFileBean: Base64AndFileBean) {
        val baseView = obtainView()
        baseView?.let {
            if (baseView is IBase64AndFileView) {
                val base64AndFileView = baseView
                model.showPictureToBase64(
                    base64AndFileBean,
                    object : OnCommonSingleParamCallback<Base64AndFileBean> {
                        override fun onSuccess(success: Base64AndFileBean) {
                            base64AndFileView.showPictureToBase64Success(success)
                        }

                        override fun onError(error: String) {
                            base64AndFileView.showPictureToBase64Error(error)
                        }
                    })
            }
        }
    }

    override fun showBase64ToPicture(base64AndFileBean: Base64AndFileBean) {
        val baseView = obtainView()
        baseView?.let {
            if (baseView is IBase64AndFileView) {
                val base64AndFileView = baseView
                model.showBase64ToPicture(
                    base64AndFileBean,
                    object : OnCommonSingleParamCallback<Base64AndFileBean> {
                        override fun onSuccess(success: Base64AndFileBean) {
                            base64AndFileView.showBase64ToPictureSuccess(success)
                        }

                        override fun onError(error: String) {
                            base64AndFileView.showBase64ToPictureError(error)
                        }
                    })
            }
        }
    }

    override fun detachView() {
        super.detachView()
        model.detachViewModel()
    }
}