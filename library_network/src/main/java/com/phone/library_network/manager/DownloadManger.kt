package com.phone.library_network.manager

import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.MainThreadManager
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

    private var client: OkHttpClient

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
        @Volatile
        private var instance: DownloadManger? = null
            get() {
                if (field == null) {
                    field = DownloadManger()
                }
                return field
            }

        @Synchronized
        @JvmStatic
        fun instance(): DownloadManger {
            return instance!!
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
                var `is`: InputStream
                var bis: BufferedInputStream
                var fos: FileOutputStream
                var bos: BufferedOutputStream
                // 储存下载文件的目录
                val downloadFile = File(saveDir)
                if (!downloadFile.exists()) {
                    downloadFile.mkdirs()
                }

                var name: String = System.currentTimeMillis().toString()
                name = name + "." + suffix
                LogManager.i(TAG, "download name*****$name")
                val file = File(downloadFile, name)
                if (file.exists()) {
                    file.createNewFile()
                }
                try {
                    response.body()?.let {
                        val buf = ByteArray(1024 * 1024 * 2)
                        var len = 0
                        val total = it.contentLength()
                        `is` = it.byteStream()
                        bis = BufferedInputStream(`is`)
                        fos = FileOutputStream(file, false)
                        bos = BufferedOutputStream(fos)
                        var sum: Long = 0
                        while (`is`.read(buf).also { len = it } != -1) {
                            fos.write(buf, 0, len)
                            sum += len.toLong()
                            val progress = (sum * 1.0f / total * 100).toInt()
                            // 下载中
                            MainThreadManager {
                                listener.onDownloading(progress)
                            }
                        }
                        bos.flush()
                        bis.close()
                        bos.close()
                        // 下载完成
                        MainThreadManager {
                            listener.onDownloadSuccess()
                        }
                    }
                } catch (e: Exception) {
                    MainThreadManager {
                        listener.onDownloadError()
                    }
                } finally {

                }
            }
        })
    }


}