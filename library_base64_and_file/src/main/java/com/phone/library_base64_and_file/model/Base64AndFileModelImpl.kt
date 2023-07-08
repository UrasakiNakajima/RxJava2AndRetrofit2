package com.phone.library_base64_and_file.model

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.phone.library_base64_and_file.bean.Base64AndFileBean
import com.phone.library_base64_and_file.thread_pool.Base64ToPictureThreadPool
import com.phone.library_base64_and_file.thread_pool.CompressedPictureThreadPool
import com.phone.library_base64_and_file.thread_pool.PictureToBase64ThreadPool
import com.phone.library_base.callback.OnCommonSingleParamCallback
import com.phone.library_base.manager.LogManager.i

class Base64AndFileModelImpl : IBase64AndFileModel {

    private val TAG = Base64AndFileModelImpl::class.java.simpleName
    private var handler: Handler

    init {
        handler = Handler(Looper.getMainLooper())
    }

    override fun showCompressedPicture(
        context: Context,
        base64AndFileBean: Base64AndFileBean,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<Base64AndFileBean>
    ) {
        i(TAG, "showCompressedPicture")
        val compressedPictureThreadPool = CompressedPictureThreadPool(
            context,
            base64AndFileBean
        )
        compressedPictureThreadPool.setOnCommonSingleParamCallback(object :
            OnCommonSingleParamCallback<Base64AndFileBean?> {
            override fun onSuccess(success: Base64AndFileBean?) {
                handler.post { onCommonSingleParamCallback.onSuccess(success) }
            }

            override fun onError(error: String) {
                handler.post { onCommonSingleParamCallback.onError(error) }
            }
        })
        compressedPictureThreadPool.submit()
    }

    override fun showPictureToBase64(
        base64AndFileBean: Base64AndFileBean,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<Base64AndFileBean>
    ) {
        i(TAG, "showPictureToBase64")
        val pictureToBase64ThreadPool = PictureToBase64ThreadPool(base64AndFileBean)
        pictureToBase64ThreadPool.setOnCommonSingleParamCallback(object :
            OnCommonSingleParamCallback<Base64AndFileBean?> {
            override fun onSuccess(success: Base64AndFileBean?) {
                handler.post { onCommonSingleParamCallback.onSuccess(success) }
            }

            override fun onError(error: String) {
                handler.post { onCommonSingleParamCallback.onError(error) }
            }
        })
        pictureToBase64ThreadPool.submit()
    }

    override fun showBase64ToPicture(
        base64AndFileBean: Base64AndFileBean,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<Base64AndFileBean>
    ) {
        i(TAG, "showBase64ToPicture")
        val base64ToPictureThreadPool = Base64ToPictureThreadPool(base64AndFileBean)
        base64ToPictureThreadPool.setOnCommonSingleParamCallback(object :
            OnCommonSingleParamCallback<Base64AndFileBean?> {
            override fun onSuccess(success: Base64AndFileBean?) {
                handler.post { onCommonSingleParamCallback.onSuccess(success) }
            }

            override fun onError(error: String) {
                handler.post { onCommonSingleParamCallback.onError(error) }
            }
        })
        base64ToPictureThreadPool.submit()
    }

    override fun detachViewModel() {
        handler.removeCallbacksAndMessages(null)
    }
}