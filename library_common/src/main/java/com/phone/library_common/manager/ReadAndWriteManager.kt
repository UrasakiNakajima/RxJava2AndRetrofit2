package com.phone.library_common.manager

import android.os.Environment
import com.phone.library_common.BaseApplication
import com.phone.library_common.callback.OnCommonSingleParamCallback
import com.phone.library_common.manager.LogManager.i
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.*

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/17 10:35
 * introduce :
 */

class ReadAndWriteManager private constructor() {

    private val TAG = ReadAndWriteManager::class.java.simpleName
    private var manager: ReadAndWriteManager? = null

    /**
     * 保证只有一个实例
     *
     * @return
     */
    companion object {
        @Volatile
        private var instance: ReadAndWriteManager? = null
            get() {
                if (field == null) {
                    field = ReadAndWriteManager()
                }
                return field
            }

        //Synchronized添加后就是线程安全的的懒汉模式
        @Synchronized
        @JvmStatic
        fun instance(): ReadAndWriteManager? {
            return instance
        }
    }

    /**
     * 将内容写入sd卡中
     *
     * @param rxAppCompatActivity
     * @param fileName            要写入的文件名
     * @param content             待写入的内容
     * @throws IOException
     */
    fun writeExternal(
        rxAppCompatActivity: RxAppCompatActivity,
        fileName: String?,
        content: String,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<Boolean?>
    ) {
        i(TAG, "writeExternal")
        Observable.create<Boolean> { e ->
            i(
                TAG,
                "Observable thread is*****" + Thread.currentThread().name
            )
            val FILEPATH = (BaseApplication.instance().externalCacheDir
                .toString() + File.separator
                    + "Mine")
            val dirs = File(FILEPATH)
            if (!dirs.exists()) {
                i(TAG, "writeExternal*****!dirs.exists()")
                dirs.mkdirs()
            }
            val file = File(dirs, fileName)
            if (!file.exists()) {
                i(TAG, "writeExternal*****!file.exists()")
                file.createNewFile()
            } else {
                i(
                    TAG,
                    "writeExternal file.getAbsolutePath()*****" + file.absolutePath
                )
            }
            val outputStream = FileOutputStream(file, false)
            val bufferedOutputStream = BufferedOutputStream(outputStream)
            bufferedOutputStream.write(content.toByteArray())
            bufferedOutputStream.close()
            e.onNext(true)
            e.onComplete()
        }.subscribeOn(Schedulers.io())
            .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ isSuccess ->
                i(TAG, "isSuccess*****$isSuccess")
                onCommonSingleParamCallback.onSuccess(isSuccess)
            }) { throwable ->
                i(TAG, "throwable*****" + throwable.message)
                // 异常处理
                onCommonSingleParamCallback.onError("写入失败")
            }
    }

    /**
     * 从sd card文件中读取数据
     *
     * @param filename 待读取的sd card
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    fun readExternal(filename: String): String {
        val stringBuilder = StringBuilder("")
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            BaseApplication.instance().externalCacheDir?.let {
                //打开文件输入流
                val inputStream = FileInputStream(it.absolutePath + File.separator + filename)
                val buffer = ByteArray(1024)
                var len = inputStream.read(buffer)
                //读取文件内容
                while (len > 0) {
                    stringBuilder.append(String(buffer, 0, len))

                    //继续将数据放到buffer中
                    len = inputStream.read(buffer)
                }
                //关闭输入流
                inputStream.close()
            }
        }
        return stringBuilder.toString()
    }

}