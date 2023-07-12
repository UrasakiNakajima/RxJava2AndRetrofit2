package com.phone.library_network

import java.io.File


interface OnDownloadCallBack {

    /**
     * 进度，运行在主线程
     *
     * @param progress 下载进度
     * @param total 总大小
     * @param speed 下载速率
     */
    fun onProgress(progress: Int, total: Long, speed: Long)

    /**
     * 运行在主线程
     *
     * @param file
     */
    fun onCompleted(file: File?)

    /**
     * 运行在主线程
     *
     * @param e
     */
    fun onError(e: Throwable?)
}