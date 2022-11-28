package com.phone.base64_and_file.thread_pool

import android.graphics.BitmapFactory
import com.phone.base64_and_file.bean.Base64AndFileBean
import com.phone.base64_and_file.manager.Base64AndFileManager
import com.phone.common_library.callback.OnCommonSingleParamCallback
import com.phone.common_library.manager.LogManager
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Base64ToPictureThreadPool(var base64AndFileBean: Base64AndFileBean) {

    private val TAG = Base64ToPictureThreadPool::class.java.simpleName
    private var base64ToPictureExcutor: ExecutorService

    init {
        base64ToPictureExcutor = Executors.newSingleThreadExecutor()
    }

    fun submit() {
        base64ToPictureExcutor.submit {
            LogManager.i(
                TAG,
                "Base64ToPictureThreadPool*******" + Thread.currentThread().name
            )
            val fileCompressedRecover = Base64AndFileManager.base64ToFile(
                base64AndFileBean.getBase64Str(),
                base64AndFileBean.dirsPathCompressedRecover,
                "picture_large_compressed_recover"
            )
            //                File fileCompressedRecover = Base64AndFileManager.base64ToFileSecond(
//                        base64AndFileBean.getBase64Str(),
//                        base64AndFileBean.getDirsPathCompressedRecover(),
//                        base64AndFileBean.getFile().getName());
//            File fileCompressedRecover = Base64AndFileManager.base64ToFileThird(base64Str, dirsPath5, fileName);
            val bitmapCompressedRecover =
                BitmapFactory.decodeFile(fileCompressedRecover?.absolutePath)
            if (bitmapCompressedRecover != null) {
                base64AndFileBean.bitmapCompressedRecover = bitmapCompressedRecover
                onCommonSingleParamCallback?.onSuccess(base64AndFileBean)
            } else {
                onCommonSingleParamCallback?.onError("圖片不存在")
            }
        }
    }

    private var onCommonSingleParamCallback: OnCommonSingleParamCallback<Base64AndFileBean?>? = null

    fun setOnCommonSingleParamCallback(onCommonSingleParamCallback: OnCommonSingleParamCallback<Base64AndFileBean?>?) {
        this.onCommonSingleParamCallback = onCommonSingleParamCallback
    }
}