package com.phone.library_network

import android.os.Handler
import android.os.Looper
import android.os.Message

import com.phone.library_network.bean.DownloadInfo


/**
 * 下载进度Handler
 *
 * @author Kelly
 * @version 1.0.0
 * @filename DownloadProgressHandler.java
 * @time 2018/7/25 15:25
 * @copyright(C) 2018 song
 */
abstract class DownloadProgressHandler : OnDownloadCallBack {

    companion object {
        const val DOWNLOAD_SUCCESS = 0
        const val DOWNLOAD_PROGRESS = 1
        const val DOWNLOAD_FAIL = 2


        class ResponseHandler(
            private val mProgressHandler: DownloadProgressHandler,
            looper: Looper
        ) :
            Handler(looper) {
            override fun handleMessage(msg: Message) {
                mProgressHandler.handleMessage(msg)
            }
        }
    }

    protected var mHandler: ResponseHandler = ResponseHandler(this, Looper.getMainLooper())

    /**
     * 发送消息，更新进度
     *
     * @param what
     * @param downloadInfo
     */
    fun sendMessage(what: Int, downloadInfo: DownloadInfo?) {
//        mHandler.obtainMessage(what, downloadInfo).sendToTarget()
        val message = Message.obtain()
        message.what = what
        message.obj = downloadInfo
        mHandler.sendMessage(message)
    }

    /**
     * 处理消息
     * @param message
     */
    protected fun handleMessage(message: Message) {
        val progressBean = message.obj as DownloadInfo
        when (message.what) {
            DOWNLOAD_SUCCESS -> {
                onCompleted(progressBean.file!!)
                removeMessage()
            }

            DOWNLOAD_PROGRESS -> onProgress(
                progressBean.progress!!,
                progressBean.fileSize!!,
                progressBean.speed!!
            )

            DOWNLOAD_FAIL -> onError(progressBean.error)
            else -> removeMessage()
        }
    }

    private fun removeMessage() {
        mHandler.removeCallbacksAndMessages(null)
    }

}