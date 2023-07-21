package com.phone.library_base64_and_file.thread_pool

import android.graphics.BitmapFactory
import com.phone.library_base64_and_file.bean.Base64AndFileBean
import com.phone.library_base64_and_file.manager.Base64AndFileManager
import com.phone.library_base.callback.OnCommonSingleParamCallback
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.ThreadPoolManager
import com.phone.library_common.manager.MediaFileManager

class Base64ToPictureThreadPool(var base64AndFileBean: Base64AndFileBean) {

    private val TAG = Base64ToPictureThreadPool::class.java.simpleName

    fun execute() {
        ThreadPoolManager.instance().createSyncThreadPool({
            LogManager.i(
                TAG,
                "Base64ToPictureThreadPool*******" + Thread.currentThread().name
            )

            val mediaFileType =
                MediaFileManager.getFileType(base64AndFileBean.fileCompressed?.absolutePath)
            val mimeType = mediaFileType.mimeType
            val typeArr = mimeType.split("/").toTypedArray()
            val fileType = typeArr[1]
            //再把压缩后的bitmap保存到本地
            val fileCompressedRecover = Base64AndFileManager.base64ToFile(
                base64AndFileBean.base64Str ?: "",
                base64AndFileBean.dirsPathCompressedRecover ?: "",
                "picture_large_compressed_recover.$fileType"
            )
//            val fileCompressedRecover = Base64AndFileManager.base64ToFileSecond(
//                base64AndFileBean.base64Str ?: "",
//                base64AndFileBean.dirsPathCompressedRecover ?: "",
//                "picture_large_compressed_recover.$fileType"
//            )
//            val fileCompressedRecover = Base64AndFileManager.base64ToFileThird(
//                base64AndFileBean.base64Str ?: "",
//                base64AndFileBean.dirsPathCompressedRecover ?: "",
//                "picture_large_compressed_recover.$fileType")
            base64AndFileBean.fileCompressedRecover = fileCompressedRecover


            val bitmapCompressedRecover =
                BitmapFactory.decodeFile(fileCompressedRecover.absolutePath)
            if (bitmapCompressedRecover != null) {
                base64AndFileBean.bitmapCompressedRecover = bitmapCompressedRecover
                onCommonSingleParamCallback?.onSuccess(base64AndFileBean)
            } else {
                onCommonSingleParamCallback?.onError("圖片不存在")
            }
        })
    }

    private var onCommonSingleParamCallback: OnCommonSingleParamCallback<Base64AndFileBean?>? = null

    fun setOnCommonSingleParamCallback(onCommonSingleParamCallback: OnCommonSingleParamCallback<Base64AndFileBean?>?) {
        this.onCommonSingleParamCallback = onCommonSingleParamCallback
    }
}