package com.phone.library_network.manager

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.phone.library_base.BaseApplication
import com.phone.library_base.callback.OnCommonSingleParamCallback
import com.phone.library_base.common.ConstantUrl
import com.phone.library_base.manager.LogManager
import com.phone.library_common.R
import com.phone.library_network.DownloadProgressHandler
import com.phone.library_network.OnDownloadCallBack
import com.phone.library_network.bean.DownloadInfo
import com.phone.library_network.interceptor.AddAccessTokenInterceptor
import com.phone.library_network.interceptor.BaseUrlManagerInterceptor
import com.phone.library_network.interceptor.CacheControlInterceptor
import com.phone.library_network.interceptor.ReceivedAccessTokenInterceptor
import com.phone.library_network.interceptor.RewriteCacheControlInterceptor
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.FragmentEvent
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.experimental.and


class RetrofitManager private constructor() {

    private val TAG = RetrofitManager::class.java.simpleName

    @JvmField
    val mRetrofit: Retrofit

    /**
     * 私有构造器 无法外部创建
     * 初始化必要对象和参数
     */
    init {
        //缓存
        val cacheFile = File(BaseApplication.instance().externalCacheDir, "cache")
        val cache = Cache(cacheFile, 1024 * 1024 * 10) //10Mb
        val rewriteCacheControlInterceptor = RewriteCacheControlInterceptor()
        val loggingInterceptor = HttpLoggingInterceptor()
        // 包含header、body数据
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        //        HeaderInterceptor headerInterceptor = new HeaderInterceptor()

        // 初始化okhttp
        val client = OkHttpClient.Builder()
            .connectTimeout((15 * 1000).toLong(), TimeUnit.MILLISECONDS) //连接超时
            .readTimeout((15 * 1000).toLong(), TimeUnit.MILLISECONDS) //读取超时
            .writeTimeout((15 * 1000).toLong(), TimeUnit.MILLISECONDS) //写入超时
            .addInterceptor(CacheControlInterceptor())
            .addInterceptor(AddAccessTokenInterceptor()) //拦截器用于设置header
            .addInterceptor(ReceivedAccessTokenInterceptor()) //拦截器用于接收并持久化cookie
            .addInterceptor(BaseUrlManagerInterceptor())
            .addInterceptor(rewriteCacheControlInterceptor) //                .addNetworkInterceptor(rewriteCacheControlInterceptor)
            //                .addInterceptor(headerInterceptor)
//            .addInterceptor(loggingInterceptor)
            //                .addInterceptor(new GzipRequestInterceptor()) //开启Gzip压缩
            .cache(cache).sslSocketFactory(SSLSocketManager.sslSocketFactory()) //配置
            .hostnameVerifier(SSLSocketManager.hostnameVerifier()) //配置
            //                .proxy(Proxy.NO_PROXY)
            .build()

        // 初始化Retrofit
        mRetrofit = Retrofit.Builder().client(client).baseUrl(ConstantUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    }

    /**
     * 保证只有一个实例
     *
     * @return
     */
    companion object {
        @Volatile
        private var instance: RetrofitManager? = null
            get() {
                if (field == null) {
                    field = RetrofitManager()
                }
                return field
            }

        //Synchronized添加后就是线程安全的的懒汉模式
        @Synchronized
        @JvmStatic
        fun instance(): RetrofitManager {
            return instance!!
        }

        /**
         * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
         * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
         */
        private const val CACHE_CONTROL_AGE = "max-age=0"

        /**
         * 设缓存有效期为两天
         */
        const val CACHE_STALE_SEC = (60 * 60 * 24 * 2).toLong()

        /**
         * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
         * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
         */
        private const val CACHE_CONTROL_CACHE = "only-if-cached, max-stale=$CACHE_STALE_SEC"

        fun buildSign(secret: String, time: Long): String {
            //        Map treeMap = new TreeMap(params)// treeMap默认会以key值升序排序
            val stringBuilder = StringBuilder()
            stringBuilder.append(secret)
            stringBuilder.append(time)
            stringBuilder.append("1.1.0")
            stringBuilder.append("861875048330495")
            stringBuilder.append("android")
            Log.d("GlobalConfiguration", "sting:$stringBuilder")
            val md5: MessageDigest
            var bytes: ByteArray? = null
            try {
                md5 = MessageDigest.getInstance("MD5")
                bytes = md5.digest(stringBuilder.toString().toByteArray(charset("utf-8"))) // md5加密
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            // 将MD5输出的二进制结果转换为小写的十六进制
            val sign = StringBuilder()
            bytes?.let {
                for (i in 0 until it.size) {
                    val hex = Integer.toHexString((it[i] and 0xFF.toByte()).toInt())
                    if (hex.length == 1) {
                        sign.append("0")
                    }
                    sign.append(hex)
                }
            }
            Log.d("GlobalConfiguration", "MD5:$sign")
            return sign.toString()
        }

        fun getCacheControl(): String {
            return if (isNetworkAvailable()) CACHE_CONTROL_AGE else CACHE_CONTROL_CACHE
        }

        /**
         * 判断网络是否可用
         *
         * @return
         */
        fun isNetworkAvailable(): Boolean {
            val connectivityManager =
                BaseApplication.instance().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            //如果仅仅是用来判断网络连接
            //connectivityManager.getActiveNetworkInfo().isAvailable()
            val info = connectivityManager.allNetworkInfo
            //            LogManager.i(TAG, "isNetworkAvailable*****" + info.toString())
            for (i in info.indices) {
                if (info[i].state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
            return false
        }
    }

    /**
     * 返回上传jsonString的RequestBody
     *
     * @param requestData
     * @return
     */
    fun jsonStringBody(requestData: String): RequestBody {
        val mediaType = MediaType.parse("application/json charset=utf-8") //"类型,字节码"
        //2.通过RequestBody.create 创建requestBody对象
        return RequestBody.create(mediaType, requestData)
    }

    /**
     * 返回上传文件一对一，并且携带参数的的RequestBody
     *
     * @param bodyParams
     * @param fileMap
     * @return
     */
    fun multipartBody(
        bodyParams: Map<String, String>, fileMap: Map<String, File>
    ): RequestBody {
        val MEDIA_TYPE = MediaType.parse("image/*")
        // form 表单形式上传
        val multipartBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        //1.添加请求参数
        //遍历map中所有参数到builder
        if (bodyParams.size > 0) {
            for (key in bodyParams.keys) {
                bodyParams[key]?.let {
                    //如果参数不是null，才把参数传给后台
                    multipartBodyBuilder.addFormDataPart(
                        key, it
                    )
                }
            }
        }

        //遍历fileMap中所有图片绝对路径到builder，并约定key如"upload[]"作为php服务器接受多张图片的key
        if (fileMap.size > 0) {
            for (key in fileMap.keys) {
                val file = fileMap[key]
                if (file != null && file.exists()) { //如果参数不是null，才把参数传给后台
                    multipartBodyBuilder.addFormDataPart(
                        key, file.name, RequestBody.create(MEDIA_TYPE, file)
                    )
                    LogManager.i(
                        TAG, "file.getName()*****" + file.name
                    )
                }
            }
        }

        //构建请求体
        return multipartBodyBuilder.build()
    }

    /**
     * 返回上传文件一对一和一对多，并且携带参数的RequestBody
     *
     * @param bodyParams
     * @param fileMap
     * @param filesMap
     * @return
     */
    fun multipartBody2(
        bodyParams: Map<String, String>,
        fileMap: Map<String, File>,
        filesMap: Map<String, List<File>>
    ): RequestBody? {
        val MEDIA_TYPE = MediaType.parse("image/*")
        // form 表单形式上传
        val multipartBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        //1.添加请求参数
        //遍历map中所有参数到builder
        if (bodyParams.size > 0) {
            for (key in bodyParams.keys) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(
                        bodyParams[key]
                    )
                ) {
                    //如果参数不是null，才把参数传给后台
                    multipartBodyBuilder.addFormDataPart(
                        key, Objects.requireNonNull(bodyParams[key])
                    )
                }
            }
        }

        //遍历fileMap中所有图片绝对路径到builder，并约定key如"upload[]"作为php服务器接受多张图片的key
        if (fileMap.size > 0) {
            for (key in fileMap.keys) {
                val file = fileMap[key]
                if (file != null && file.exists()) { //如果参数不是null，才把参数传给后台
                    multipartBodyBuilder.addFormDataPart(
                        key, file.name, RequestBody.create(MEDIA_TYPE, file)
                    )
                    LogManager.i(
                        TAG, "file.getName()*****" + file.name
                    )
                }
            }
        }

        //遍历filesMap中所有图片绝对路径到builder，并约定key如"upload[]"作为php服务器接受多张图片的key
        if (filesMap.size > 0) {
            for (key in filesMap.keys) {
                val files = filesMap[key]
                if (files != null && files.size > 0) { //如果参数不是null，才把参数传给后台
                    for (i in files.indices) {
                        if (files[i].exists()) {
                            multipartBodyBuilder.addFormDataPart(
                                key, files[i].name, RequestBody.create(MEDIA_TYPE, files[i])
                            )
                            LogManager.i(
                                TAG, "files.get(i).getName()*****" + files[i]!!.name
                            )
                        }
                    }
                }
            }
        }

        //构建请求体
        return multipartBodyBuilder.build()
    }

    /**
     * 无返回值，接口回调返回字符串
     *
     * @param appCompatActivity
     * @param observable
     * @param onCommonSingleParamCallback
     * @return
     */
    fun responseString(
        appCompatActivity: AppCompatActivity,
        observable: Observable<ResponseBody>,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        observable.onTerminateDetach().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) //AutoDispose的关键语句
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(appCompatActivity)))
            .subscribe({ responseBody ->
                val responseString = responseBody?.string()
                LogManager.i(TAG, "responseString*****$responseString")
                onCommonSingleParamCallback.onSuccess(responseString)
            }) { throwable ->
                LogManager.i(TAG, "throwable*****$throwable")
                LogManager.i(
                    TAG, "throwable message*****" + throwable.message
                )
                // 异常处理
                onCommonSingleParamCallback.onError(BaseApplication.instance().resources.getString(R.string.library_request_was_aborted))
            }
    }

    /**
     * 无返回值，接口回调返回字符串
     *
     * @param fragment
     * @param observable
     * @param onCommonSingleParamCallback
     * @return
     */
    fun responseString2(
        fragment: Fragment,
        observable: Observable<ResponseBody>,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        observable.onTerminateDetach().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) //AutoDispose的关键语句（解决RxJava2导致的内存泄漏的）
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(fragment)))
            .subscribe({ responseBody ->
                val responseString = responseBody?.string()
                LogManager.i(TAG, "responseString*****$responseString")
                onCommonSingleParamCallback.onSuccess(responseString)

                //                                   ReadAndWriteManager manager = ReadAndWriteManager.getInstance()
                //                                   manager.writeExternal("mineLog.txt",
                //                                           responseString,
                //                                           new OnCommonSingleParamCallback<Boolean>() {
                //                                               @Override
                //                                               public void onSuccess(Boolean success) {
                //                                                   LogManager.i(TAG, "success*****" + success)
                //                                                   manager.unSubscribe()
                //                                               }
                //
                //                                               @Override
                //                                               public void onError(String error) {
                //                                                   LogManager.i(TAG, "error*****" + error)
                //                                                   manager.unSubscribe()
                //                                               }
                //                                           })
            }) { throwable ->
                LogManager.i(TAG, "throwable*****$throwable")
                LogManager.i(
                    TAG, "throwable message*****" + throwable.message
                )
                // 异常处理
                onCommonSingleParamCallback.onError(BaseApplication.instance().resources.getString(R.string.library_request_was_aborted))
            }
    }

    /**
     * 无返回值，接口回调返回字符串
     *
     * @param rxAppCompatActivity
     * @param observable
     * @param onCommonSingleParamCallback
     * @return
     */
    fun responseString3(
        rxAppCompatActivity: RxAppCompatActivity,
        observable: Observable<ResponseBody>,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        observable.onTerminateDetach().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) //解决RxJava2导致的内存泄漏问题
            .compose(rxAppCompatActivity.bindToLifecycle()).subscribe({ responseBody ->
                val responseString = responseBody.string()
                LogManager.i(TAG, "responseString*****$responseString")
                onCommonSingleParamCallback.onSuccess(responseString)
                //                                   if (!TextUtils.isEmpty(responseString)) {
//                                       BaseResponse baseResponse
//                                       try {
//                                           baseResponse = JSON.parseObject(responseString, BaseResponse.class)
//                                       } catch (Exception e) {
//                                           //如果不是标准json字符串，就返回错误提示
//                                           onCommonSingleParamCallback.onError(BaseApplication.instance().getResources().getString(R.string.library_server_sneak_off))
//                                           return
//                                       }
//                                       onCommonSingleParamCallback.onSuccess(responseString)
//                                   } else {
//                                       onCommonSingleParamCallback.onError(BaseApplication.instance().getResources().getString(R.string.library_server_sneak_off))
//                                   }


                //                                   ReadAndWriteManager manager = ReadAndWriteManager.getInstance()
                //                                   manager.writeExternal("mineLog.txt",
                //                                           responseString,
                //                                           new OnCommonSingleParamCallback<Boolean>() {
                //                                               @Override
                //                                               public void onSuccess(Boolean success) {
                //                                                   LogManager.i(TAG, "success*****" + success)
                //                                                   manager.unSubscribe()
                //                                               }
                //
                //                                               @Override
                //                                               public void onError(String error) {
                //                                                   LogManager.i(TAG, "error*****" + error)
                //                                                   manager.unSubscribe()
                //                                               }
                //                                           })
            }) { throwable ->
                LogManager.i(TAG, "throwable*****$throwable")
                LogManager.i(
                    TAG, "throwable message*****" + throwable.message
                )
                // 异常处理
                onCommonSingleParamCallback.onError(BaseApplication.instance().resources.getString(R.string.library_request_was_aborted))
            }
    }

    /**
     * 无返回值，接口回调返回字符串
     *
     * @param rxAppCompatActivity
     * @param observable
     * @param onCommonSingleParamCallback
     * @return
     */
    fun responseString5(
        rxAppCompatActivity: RxAppCompatActivity,
        observable: Observable<ResponseBody>,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        observable.onTerminateDetach().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) //解决RxJava2导致的内存泄漏问题
            .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe({ responseBody ->
                val responseString = responseBody.string()
                LogManager.i(TAG, "responseString*****$responseString")
                onCommonSingleParamCallback.onSuccess(responseString)
            }) { throwable ->
                LogManager.i(TAG, "throwable*****$throwable")
                LogManager.i(
                    TAG, "throwable message*****" + throwable.message
                )
                // 异常处理
                onCommonSingleParamCallback.onError(BaseApplication.instance().resources.getString(R.string.library_request_was_aborted))
            }
    }

    /**
     * 无返回值，，接口回调返回字符串
     *
     * @param rxFragment
     * @param observable
     * @param onCommonSingleParamCallback
     * @return
     */
    fun responseString6(
        rxFragment: RxFragment,
        observable: Observable<ResponseBody>,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        observable.onTerminateDetach().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) //解决RxJava2导致的内存泄漏问题
            .compose(rxFragment.bindToLifecycle()).subscribe({ responseBody ->
                val responseString = responseBody.string()
                LogManager.i(TAG, "responseString*****$responseString")
                onCommonSingleParamCallback.onSuccess(responseString)
                //                                   if (!TextUtils.isEmpty(responseString)) {
//                                       BaseResponse baseResponse
//                                       try {
//                                           baseResponse = JSON.parseObject(responseString, BaseResponse.class)
//                                       } catch (Exception e) {
//                                           //如果不是标准json字符串，就返回错误提示
//                                           onCommonSingleParamCallback.onError(BaseApplication.instance().getResources().getString(R.string.library_server_sneak_off))
//                                           return
//                                       }
//                                       onCommonSingleParamCallback.onSuccess(responseString)
//                                   } else {
//                                       onCommonSingleParamCallback.onError(BaseApplication.instance().getResources().getString(R.string.library_server_sneak_off))
//                                   }


                //                                   ReadAndWriteManager manager = ReadAndWriteManager.getInstance()
                //                                   manager.writeExternal("mineLog.txt",
                //                                           responseString,
                //                                           new OnCommonSingleParamCallback<Boolean>() {
                //                                               @Override
                //                                               public void onSuccess(Boolean success) {
                //                                                   LogManager.i(TAG, "success*****" + success)
                //                                                   manager.unSubscribe()
                //                                               }
                //
                //                                               @Override
                //                                               public void onError(String error) {
                //                                                   LogManager.i(TAG, "error*****" + error)
                //                                                   manager.unSubscribe()
                //                                               }
                //                                           })
            }) { throwable ->
                LogManager.i(TAG, "throwable*****$throwable")
                LogManager.i(
                    TAG, "throwable message*****" + throwable.message
                )
                // 异常处理
                onCommonSingleParamCallback.onError(BaseApplication.instance().resources.getString(R.string.library_request_was_aborted))
            }
    }

    /**
     * 无返回值，接口回调返回字符串
     *
     * @param rxFragment
     * @param observable
     * @param onCommonSingleParamCallback
     * @return
     */
    fun responseString7(
        rxFragment: RxFragment,
        observable: Observable<ResponseBody>,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        observable.onTerminateDetach().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) //解决RxJava2导致的内存泄漏问题
            .compose(rxFragment.bindUntilEvent(FragmentEvent.DESTROY)).subscribe({ responseBody ->
                val responseString = responseBody.string()
                LogManager.i(TAG, "responseString*****$responseString")
                onCommonSingleParamCallback.onSuccess(responseString)
                //                                   if (!TextUtils.isEmpty(responseString)) {
//                                       BaseResponse baseResponse
//                                       try {
//                                           baseResponse = JSON.parseObject(responseString, BaseResponse.class)
//                                       } catch (Exception e) {
//                                           //如果不是标准json字符串，就返回错误提示
//                                           onCommonSingleParamCallback.onError(BaseApplication.instance().getResources().getString(R.string.library_server_sneak_off))
//                                           return
//                                       }
//                                       onCommonSingleParamCallback.onSuccess(responseString)
//                                   } else {
//                                       onCommonSingleParamCallback.onError(BaseApplication.instance().getResources().getString(R.string.library_server_sneak_off))
//                                   }


                //                                   ReadAndWriteManager manager = ReadAndWriteManager.getInstance()
                //                                   manager.writeExternal("mineLog.txt",
                //                                           responseString,
                //                                           new OnCommonSingleParamCallback<Boolean>() {
                //                                               @Override
                //                                               public void onSuccess(Boolean success) {
                //                                                   LogManager.i(TAG, "success*****" + success)
                //                                                   manager.unSubscribe()
                //                                               }
                //
                //                                               @Override
                //                                               public void onError(String error) {
                //                                                   LogManager.i(TAG, "error*****" + error)
                //                                                   manager.unSubscribe()
                //                                               }
                //                                           })
            }) { throwable ->
                LogManager.i(TAG, "throwable*****$throwable")
                LogManager.i(
                    TAG, "throwable message*****" + throwable.message
                )
                // 异常处理
                onCommonSingleParamCallback.onError(BaseApplication.instance().resources.getString(R.string.library_request_was_aborted))
            }
    }

    /**
     * 下载文件法1(使用Handler更新UI)
     *
     * @param observable      下载被观察者
     * @param destDir         下载目录
     * @param fileName        文件名
     * @param progressHandler 进度handler
     */
    fun downloadFile(
        rxFragment: RxFragment,
        observable: Observable<ResponseBody>,
        destDir: String,
        fileName: String,
        progressHandler: DownloadProgressHandler
    ) {
        val downloadInfo = DownloadInfo(null, null, null, null, null, null, null)
        observable.subscribeOn(Schedulers.io())
            //解决RxJava2导致的内存泄漏问题
            .compose(rxFragment.bindUntilEvent(FragmentEvent.DESTROY)).observeOn(Schedulers.io())
            .subscribe({
                var inputStream: InputStream? = null
                var bis: BufferedInputStream? = null
                var total: Long = 0
                val responseLength: Long
                var fos: FileOutputStream? = null
                var bos: BufferedOutputStream? = null
                try {
                    val buf = ByteArray(1024 * 8)
                    var len: Int
                    responseLength = it.contentLength()
                    inputStream = it.byteStream()
                    bis = BufferedInputStream(inputStream)

                    val dir = File(destDir)
                    if (!dir.exists()) {
                        dir.mkdirs()
                    }
                    val file = File(dir, fileName)
                    if (!file.exists()) {
                        file.createNewFile()
                    }
                    downloadInfo.file = file
                    downloadInfo.fileSize = responseLength

                    fos = FileOutputStream(file)
                    bos = BufferedOutputStream(fos)
                    var progress = 0
                    var lastProgress = -1
                    val startTime = System.currentTimeMillis() // 开始下载时获取开始时间
                    while (bis.read(buf).also { len = it } != -1) {
                        bos.write(buf, 0, len)
                        total += len.toLong()
                        progress = (total * 100 / responseLength).toInt()
                        val curTime = System.currentTimeMillis()
                        var usedTime = (curTime - startTime) / 1000
                        if (usedTime == 0L) {
                            usedTime = 1
                        }
                        val speed = total / usedTime // 平均每秒下载速度
                        // 如果进度与之前进度相等，则不更新，如果更新太频繁，则会造成界面卡顿
                        if (progress != lastProgress) {
                            downloadInfo.speed = speed
                            downloadInfo.progress = progress
                            downloadInfo.currentSize = total
                            progressHandler.sendMessage(
                                DownloadProgressHandler.DOWNLOAD_PROGRESS, downloadInfo
                            )
                        }
//                            LogManager.i(TAG, "downloadInfo total******${total}")
//                            LogManager.i(TAG, "downloadInfo progress******${progress}")
                        lastProgress = progress
                    }
                    bos.flush()
                    downloadInfo.file = file
                    progressHandler.sendMessage(
                        DownloadProgressHandler.DOWNLOAD_SUCCESS, downloadInfo
                    )
                } catch (e: java.lang.Exception) {
                    downloadInfo.error = e
                    progressHandler.sendMessage(
                        DownloadProgressHandler.DOWNLOAD_FAIL, downloadInfo
                    )
                } finally {
                    try {
                        bis?.close()
                        bos?.close()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }, {
                downloadInfo.error = it
                progressHandler.sendMessage(DownloadProgressHandler.DOWNLOAD_FAIL, downloadInfo)
            })
//            .subscribe(object : Observer<ResponseBody> {
//                override fun onSubscribe(d: Disposable) {
//                }
//
//                override fun onNext(responseBody: ResponseBody) { // Consumer<ResponseBody>
//                    var inputStream: InputStream? = null
//                    var bis: BufferedInputStream? = null
//                    var total: Long = 0
//                    val responseLength: Long
//                    var fos: FileOutputStream? = null
//                    var bos: BufferedOutputStream? = null
//                    try {
//                        val buf = ByteArray(1024 * 8)
//                        var len: Int
//                        responseLength = responseBody.contentLength()
//                        inputStream = responseBody.byteStream()
//                        bis = BufferedInputStream(inputStream)
//
//                        val dir = File(destDir)
//                        if (!dir.exists()) {
//                            dir.mkdirs()
//                        }
//                        val file = File(dir, fileName)
//                        if (!file.exists()) {
//                            file.createNewFile()
//                        }
//                        downloadInfo.file = file
//                        downloadInfo.fileSize = responseLength
//
//                        fos = FileOutputStream(file)
//                        bos = BufferedOutputStream(fos)
//                        var progress = 0
//                        var lastProgress = -1
//                        val startTime = System.currentTimeMillis() // 开始下载时获取开始时间
//                        while (bis.read(buf).also { len = it } != -1) {
//                            bos.write(buf, 0, len)
//                            total += len.toLong()
//                            progress = (total * 100 / responseLength).toInt()
//                            val curTime = System.currentTimeMillis()
//                            var usedTime = (curTime - startTime) / 1000
//                            if (usedTime == 0L) {
//                                usedTime = 1
//                            }
//                            val speed = total / usedTime // 平均每秒下载速度
//                            // 如果进度与之前进度相等，则不更新，如果更新太频繁，则会造成界面卡顿
//                            if (progress != lastProgress) {
//                                downloadInfo.speed = speed
//                                downloadInfo.progress = progress
//                                downloadInfo.currentSize = total
//                                progressHandler.sendMessage(
//                                    DownloadProgressHandler.DOWNLOAD_PROGRESS, downloadInfo
//                                )
//                            }
////                            LogManager.i(TAG, "downloadInfo total******${total}")
////                            LogManager.i(TAG, "downloadInfo progress******${progress}")
//                            lastProgress = progress
//                        }
//                        bos.flush()
//                        downloadInfo.file = file
//                        progressHandler.sendMessage(
//                            DownloadProgressHandler.DOWNLOAD_SUCCESS, downloadInfo
//                        )
//                    } catch (e: java.lang.Exception) {
//                        downloadInfo.error = e
//                        progressHandler.sendMessage(
//                            DownloadProgressHandler.DOWNLOAD_FAIL, downloadInfo
//                        )
//                    } finally {
//                        try {
//                            bis?.close()
//                            bos?.close()
//                        } catch (e: java.lang.Exception) {
//                            e.printStackTrace()
//                        }
//                    }
//                }
//
//                override fun onError(e: Throwable) { // Consumer<Throwable>
//                    downloadInfo.error = e
//                    progressHandler.sendMessage(DownloadProgressHandler.DOWNLOAD_FAIL, downloadInfo)
//                }
//
//                override fun onComplete() { // Action()
//                }
//            })
    }

    /**
     * 下载文件法2(使用RXJava更新UI)
     *
     * @param observable
     * @param destDir
     * @param fileName
     * @param progressHandler
     */
    fun downloadFile2(
        rxFragment: RxFragment,
        observable: Observable<ResponseBody>,
        destDir: String,
        fileName: String,
        onDownloadCallBack: OnDownloadCallBack
    ) {
        val downloadInfo = DownloadInfo(null, null, null, null, null, null, null)
        observable.subscribeOn(Schedulers.io())
            //解决RxJava2导致的内存泄漏问题
            .compose(rxFragment.bindUntilEvent(FragmentEvent.DESTROY))
            .flatMap(object : Function<ResponseBody, ObservableSource<DownloadInfo?>> {
                @Throws(Exception::class)
                override fun apply(responseBody: ResponseBody): ObservableSource<DownloadInfo?> {
                    return Observable.create({ emitter ->
                        var inputStream: InputStream? = null
                        var bis: BufferedInputStream? = null
                        var total: Long = 0
                        var responseLength: Long = 0L
                        var fos: FileOutputStream? = null
                        var bos: BufferedOutputStream? = null
                        try {
                            val buf = ByteArray(1024 * 8)
                            var len = 0
                            responseLength = responseBody.contentLength()
                            inputStream = responseBody.byteStream()
                            bis = BufferedInputStream(inputStream)
                            val dir = File(destDir)
                            if (!dir.exists()) {
                                dir.mkdirs()
                            }
                            val file = File(dir, fileName)
                            if (!dir.exists()) {
                                dir.createNewFile()
                            }
                            downloadInfo.file = file
                            downloadInfo.fileSize = responseLength

                            fos = FileOutputStream(file)
                            bos = BufferedOutputStream(fos)
                            var progress = 0
                            var lastProgress = -1
                            val startTime = System.currentTimeMillis() // 开始下载时获取开始时间
                            while (bis.read(buf).also { len = it } != -1) {
                                bos.write(buf, 0, len)
                                total += len.toLong()

                                progress = (total * 100 / responseLength).toInt()
                                val curTime = System.currentTimeMillis()
                                var usedTime = (curTime - startTime) / 1000
                                if (usedTime == 0L) {
                                    usedTime = 1
                                }
                                val speed = total / usedTime // 平均每秒下载速度
                                // 如果进度与之前进度相等，则不更新，如果更新太频繁，则会造成界面卡顿
                                if (progress != lastProgress) {
                                    downloadInfo.speed = speed
                                    downloadInfo.progress = progress
                                    downloadInfo.currentSize = total
                                    emitter.onNext(downloadInfo)
                                    LogManager.i(
                                        TAG,
                                        "emitter.onNext downloadInfo******${downloadInfo.progress}"
                                    )
                                }
//                                LogManager.i(TAG, "downloadInfo total******${total}")
//                                LogManager.i(TAG, "downloadInfo progress******${progress}")
                                lastProgress = progress
                            }
                            bos.flush()
                            downloadInfo.file = file
                            emitter.onComplete()
                        } catch (e: Exception) {
                            downloadInfo.error = e
                            emitter.onError(e)
                        } finally {
                            try {
                                bis?.close()
                                bos?.close()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    })
                }
            }).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogManager.i(TAG, "onNext downloadInfo******${downloadInfo.progress}")
                onDownloadCallBack.onProgress(
                    downloadInfo.progress!!, downloadInfo.fileSize!!, downloadInfo.speed!!
                )
            }, {
                onDownloadCallBack.onError(it)
            }, {
                LogManager.i(TAG, "下载完成")
                onDownloadCallBack.onCompleted(downloadInfo.file!!)
            })
//            .subscribe(object : Observer<DownloadInfo?> { // Consumer<DownloadInfo?>
//
//                override fun onSubscribe(d: Disposable) {
//                }
//
//                override fun onNext(downloadInfo: DownloadInfo) {
//                    LogManager.i(TAG, "onNext downloadInfo******${downloadInfo.progress}")
//                    onDownloadCallBack.onProgress(
//                        downloadInfo.progress!!, downloadInfo.fileSize!!, downloadInfo.speed!!
//                    )
//                }
//
//                override fun onError(e: Throwable) { // Consumer<Throwable>
//                    onDownloadCallBack.onError(e)
//                }
//
//                override fun onComplete() {// Action<DownloadInfo?>
//                    LogManager.i(TAG, "下载完成")
//                    onDownloadCallBack.onCompleted(downloadInfo.file!!)
//                }
//            })
    }


}