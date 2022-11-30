package com.phone.base64_and_file.thread_pool

import com.phone.base64_and_file.bean.Base64AndFileBean
import com.phone.base64_and_file.manager.Base64AndFileManager
import com.phone.base64_and_file.manager.FileManager
import com.phone.library_common.callback.OnCommonSingleParamCallback
import com.phone.library_common.manager.LogManager
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PictureToBase64ThreadPool(var base64AndFileBean: Base64AndFileBean) {

    private val TAG = PictureToBase64ThreadPool::class.java.simpleName
    private var pictureToBase64TaskExcutor: ExecutorService

    init {
        pictureToBase64TaskExcutor = Executors.newSingleThreadExecutor()
    }

    fun submit() {
        pictureToBase64TaskExcutor.submit {
            LogManager.i(
                TAG,
                "PictureToBase64TaskThread*******" + Thread.currentThread().name
            )
            var base64Str: String? = null
            val fileCompressed = File(base64AndFileBean.fileCompressed.absolutePath)
            LogManager.i(
                TAG,
                "PictureToBase64TaskThread fileCompressedPath*******" + fileCompressed.absolutePath
            )
            if (fileCompressed.exists()) {
                base64Str = Base64AndFileManager.fileToBase64(fileCompressed)
                //                base64Str = Base64AndFileManager.fileToBase64Test(fileCompressed);
//                    base64Str = Base64AndFileManager.fileToBase64Second(fileCompressed);
                base64AndFileBean.setBase64Str(base64Str)
            }
            base64Str?.let {
                val fileName = "base64Str.txt"
                val txtFilePath = FileManager.writeStrToTextFile(
                    it,
                    base64AndFileBean.dirsPathCompressed,
                    fileName
                )
                val base64StrList = Base64AndFileManager.getBase64StrList(txtFilePath)
                base64AndFileBean.txtFilePath = txtFilePath
                base64AndFileBean.base64StrList = base64StrList
                //                base64Str = "";
//                StringBuilder stringBuilder = new StringBuilder();
//                for (int i = 0; i < base64StrList.size(); i++) {
//                    stringBuilder.append(base64StrList.get(i));
//                }
//                it = stringBuilder.toString();

                //把字符串的最後一個打印出來，然後看看和RecyclerView顯示的最後一個字符串是否一致
                LogManager.i(TAG, "base64StrList******" + base64StrList[base64StrList.size - 1])
            }
            if (base64AndFileBean.base64StrList != null && base64AndFileBean.base64StrList.size > 0) {
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