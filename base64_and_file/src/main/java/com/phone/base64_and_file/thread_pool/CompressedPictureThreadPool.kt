package com.phone.base64_and_file.thread_pool

import android.content.Context
import com.phone.base64_and_file.bean.Base64AndFileBean
import com.phone.base64_and_file.manager.BitmapManager
import com.phone.library_common.BaseApplication
import com.phone.library_common.callback.OnCommonSingleParamCallback
import com.phone.library_common.manager.LogManager
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CompressedPictureThreadPool(
    var context: Context,
    var base64AndFileBean: Base64AndFileBean
) {

    private val TAG = CompressedPictureThreadPool::class.java.simpleName
    private var compressedPictureExcutor: ExecutorService

    init {
        compressedPictureExcutor = Executors.newSingleThreadExecutor()
    }

    fun submit() {
        compressedPictureExcutor.submit {
            LogManager.i(
                TAG,
                "CompressedPictureThreadPool*******" + Thread.currentThread().name
            )

            //先取出资源文件保存在本地
            val file =
                BitmapManager.getAssetFile(
                    context,
                    base64AndFileBean.dirsPath ?: "",
                    "picture_large.webp"
                )
            LogManager.i(
                TAG,
                "file size*****" + BitmapManager.getDataSize(BitmapManager.getFileSize(file))
            )
            base64AndFileBean.file = file

            //再压缩本地图片
            BitmapManager.initCompressorIO(BaseApplication.instance(), file, {
                LogManager.i(
                    TAG,
                    "initCompressorIO it*****${it.name}"
                )
                LogManager.i(
                    TAG,
                    "initCompressorIO result size*****${BitmapManager.getDataSize(BitmapManager.getFileSize(it))}"
                )
            })

            //把图片转化成bitmap
            val bitmap = BitmapManager.getBitmap(file.absolutePath)
            LogManager.i(TAG, "bitmap mWidth*****" + bitmap.width)
            LogManager.i(TAG, "bitmap mHeight*****" + bitmap.height)
            base64AndFileBean.bitmap = bitmap
            //再压缩bitmap
            val bitmapCompressed = BitmapManager.scaleImage(bitmap, 1280, 960)
            LogManager.i(TAG, "bitmapCompressed mWidth*****" + bitmapCompressed.width)
            LogManager.i(TAG, "bitmapCompressed mHeight*****" + bitmapCompressed.height)
            base64AndFileBean.bitmapCompressed = bitmapCompressed
            //再把压缩后的bitmap保存到本地
            val fileCompressed = BitmapManager.saveFile(
                bitmapCompressed,
                base64AndFileBean.dirsPathCompressed ?: "",
                "picture_large_compressed.png"
            )
            LogManager.i(
                TAG,
                "CompressedPictureThreadPool fileCompressedPath*******" + fileCompressed.absolutePath
            )
            LogManager.i(
                TAG,
                "fileCompressed size*****" + BitmapManager.getDataSize(
                    BitmapManager.getFileSize(fileCompressed)
                )
            )
            base64AndFileBean.fileCompressed = fileCompressed
            //                if (bitmap != null) {
            //                    bitmap.recycle();
            //                    bitmap = null;
            //                }
            onCommonSingleParamCallback?.onSuccess(base64AndFileBean)
        }
    }

    private var onCommonSingleParamCallback: OnCommonSingleParamCallback<Base64AndFileBean?>? = null

    fun setOnCommonSingleParamCallback(onCommonSingleParamCallback: OnCommonSingleParamCallback<Base64AndFileBean?>?) {
        this.onCommonSingleParamCallback = onCommonSingleParamCallback
    }
}