package com.phone.call_third_party_so.callback

/**
 *    author : Urasaki
 *    e-mail : 1164688204@qq.com
 *    date   : 2021/5/17 14:26
 *    desc   :
 *    version: 1.0
 */
interface OnDownloadListener {

    /**
     * 下载成功
     */
    fun onDownloadSuccess()

    /**
     * @param progress 下载进度
     */
    fun onDownloading(progress: Int)

    /**
     * 下载失败
     */
    fun onDownloadError()
}