package com.phone.library_common.manager

import com.phone.library_common.callback.OnDownloadListener
import okhttp3.*
import java.io.*
import java.net.Proxy
import java.util.concurrent.TimeUnit


/**
 *    author : Urasaki
 *    e-mail : 1164688204@qq.com
 *    date   : 2021/5/17 11:01
 *    desc   :
 *    version: 1.0
 */
class DownloadManger private constructor() {

    private lateinit var client: OkHttpClient

    init {
        client = OkHttpClient.Builder()
            .connectTimeout(5000, TimeUnit.MILLISECONDS) //连接超时
            .readTimeout(5000, TimeUnit.MILLISECONDS) //读取超时
            .writeTimeout(5000, TimeUnit.MILLISECONDS) //写入超时
            .proxy(Proxy.NO_PROXY)
            .build()
    }

    companion object {
        private val TAG = DownloadManger::class.java.simpleName

        private var manager: DownloadManger? = null
            get() {
                if (field == null) {
                    synchronized(DownloadManger::class.java) {
                        if (field == null) {
                            field = DownloadManger()
                        }
                    }
                }

                return field
            }

        @Synchronized
        fun get(): DownloadManger {
            return manager!!
        }
    }

    fun download(url: String, suffix: String, saveDir: String, listener: OnDownloadListener) {
        val request = Request.Builder().url(url).get().build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 下载失败
                listener.onDownloadError()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                var `is`: InputStream? = null
                var bis: BufferedInputStream? = null
                var fos: FileOutputStream? = null
                var bos: BufferedOutputStream? = null
                // 储存下载文件的目录
                val downloadFile = File(saveDir)
                if (!downloadFile.exists()) {
                    downloadFile.mkdirs()
                }

                var name: String = System.currentTimeMillis().toString();
                name = name + "." + suffix;
                LogManager.i(TAG, "download name*****$name")
                val file = File(downloadFile, name)
                if (file.exists()) {
                    file.createNewFile()
                }
                try {
                    val buf = ByteArray(1024 * 1024 * 2)
                    var len = 0
                    val total = response.body()?.contentLength()
                    `is` = response.body()?.byteStream()
                    bis = BufferedInputStream(`is`)
                    fos = FileOutputStream(file, false)
                    bos = BufferedOutputStream(fos)
                    var sum: Long = 0
                    while (`is`?.read(buf).also { len = it!! } != -1) {
                        fos.write(buf, 0, len)
                        sum += len.toLong()
                        val progress = (sum * 1.0f / total!! * 100).toInt()
                        // 下载中
                        val mainThreadManager = MainThreadManager()
                        mainThreadManager.setOnSubThreadToMainThreadCallback {
                            listener.onDownloading(progress)
                        }
                        mainThreadManager.subThreadToUIThread()
                    }
                    bos.flush()
                    bis.close()
                    bos.close()
                    // 下载完成
                    val mainThreadManager = MainThreadManager()
                    mainThreadManager.setOnSubThreadToMainThreadCallback {
                        listener.onDownloadSuccess()
                    }
                    mainThreadManager.subThreadToUIThread()
                } catch (e: Exception) {
                    val mainThreadManager = MainThreadManager()
                    mainThreadManager.setOnSubThreadToMainThreadCallback {
                        listener.onDownloadError()
                    }
                    mainThreadManager.subThreadToUIThread()
                } finally {

                }
            }
        })
    }


}