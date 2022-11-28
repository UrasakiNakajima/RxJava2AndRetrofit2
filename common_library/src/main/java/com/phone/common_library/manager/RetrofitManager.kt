package com.phone.common_library.manager

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.phone.common_library.BaseApplication
import com.phone.common_library.R
import com.phone.common_library.callback.OnCommonSingleParamCallback
import com.phone.common_library.common.ConstantUrl
import com.phone.common_library.interceptor.*
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.FragmentEvent
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.experimental.and

class RetrofitManager {

    private val TAG = RetrofitManager::class.java.simpleName
    private var retrofit: Retrofit

    /**
     * 私有构造器 无法外部创建
     * 初始化必要对象和参数
     */
    init {
        //缓存
        val cacheFile = File(BaseApplication.get().externalCacheDir, "cache")
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
            .addInterceptor(loggingInterceptor) //                .addInterceptor(new GzipRequestInterceptor()) //开启Gzip压缩
            .cache(cache)
            .sslSocketFactory(SSLSocketManager.getSSLSocketFactory()) //配置
            .hostnameVerifier(SSLSocketManager.getHostnameVerifier()) //配置
            //                .proxy(Proxy.NO_PROXY)
            .build()

        // 初始化Retrofit
        retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(ConstantUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    /**
     * 保证只有一个实例
     *
     * @return
     */
    companion object {
        private var instance: RetrofitManager? = null

        /**
         * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
         * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
         */
        private val CACHE_CONTROL_AGE = "max-age=0"

        /**
         * 设缓存有效期为两天
         */
        const val CACHE_STALE_SEC = (60 * 60 * 24 * 2).toLong()

        /**
         * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
         * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
         */
        private val CACHE_CONTROL_CACHE = "only-if-cached, max-stale=$CACHE_STALE_SEC"


        //       Synchronized添加后就是线程安全的的懒汉模式
        @Synchronized
        fun get(): RetrofitManager {
            if (instance == null) {
                instance = RetrofitManager()
            }
            return instance!!
        }

        fun buildSign(secret: String?, time: Long): String {
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
            val connectivityManager = BaseApplication.get().applicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

    fun getRetrofit(): Retrofit {
        return retrofit
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
        bodyParams: Map<String, String>,
        fileMap: Map<String, File>
    ): RequestBody? {
        val MEDIA_TYPE = MediaType.parse("image/*")
        // form 表单形式上传
        val multipartBodyBuilder =
            MultipartBody.Builder().setType(MultipartBody.FORM)
        //1.添加请求参数
        //遍历map中所有参数到builder
        if (bodyParams.size > 0) {
            for (key in bodyParams.keys) {
                bodyParams[key]?.let {
                    //如果参数不是null，才把参数传给后台
                    multipartBodyBuilder.addFormDataPart(
                        key,
                        it
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
                        key,
                        file.name,
                        RequestBody.create(MEDIA_TYPE, file)
                    )
                    LogManager.i(
                        TAG,
                        "file.getName()*****" + file.name
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
        filesMap: Map<String, List<File?>>
    ): RequestBody? {
        val MEDIA_TYPE = MediaType.parse("image/*")
        // form 表单形式上传
        val multipartBodyBuilder =
            MultipartBody.Builder().setType(MultipartBody.FORM)
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
                        key,
                        Objects.requireNonNull(bodyParams[key])
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
                        key,
                        file.name,
                        RequestBody.create(MEDIA_TYPE, file)
                    )
                    LogManager.i(
                        TAG,
                        "file.getName()*****" + file.name
                    )
                }
            }
        }

        //遍历filesMap中所有图片绝对路径到builder，并约定key如"upload[]"作为php服务器接受多张图片的key
        if (filesMap != null && filesMap.size > 0) {
            for (key in filesMap.keys) {
                val files = filesMap[key]
                if (files != null && files.size > 0) { //如果参数不是null，才把参数传给后台
                    for (i in files.indices) {
                        if (files[i] != null && files[i]!!.exists()) {
                            multipartBodyBuilder.addFormDataPart(
                                key,
                                files[i]!!.name,
                                RequestBody.create(MEDIA_TYPE, files[i])
                            )
                            LogManager.i(
                                TAG, "files.get(i).getName()*****" + files[i]!!
                                    .name
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
        observable: Observable<ResponseBody?>,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        observable.onTerminateDetach()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) //AutoDispose的关键语句
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(appCompatActivity)))
            .subscribe({ responseBody ->
                val responseString = responseBody?.string()
                LogManager.i(TAG, "responseString*****$responseString")
                onCommonSingleParamCallback.onSuccess(responseString)
            }
            ) { throwable ->
                LogManager.i(TAG, "throwable*****$throwable")
                LogManager.i(
                    TAG,
                    "throwable message*****" + throwable.message
                )
                // 异常处理
                onCommonSingleParamCallback.onError(BaseApplication.get().resources.getString(R.string.request_was_aborted))
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
        fragment: Fragment?,
        observable: Observable<ResponseBody?>,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        observable.onTerminateDetach()
            .subscribeOn(Schedulers.io())
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
            }
            ) { throwable ->
                LogManager.i(TAG, "throwable*****$throwable")
                LogManager.i(
                    TAG,
                    "throwable message*****" + throwable.message
                )
                // 异常处理
                onCommonSingleParamCallback.onError(BaseApplication.get().resources.getString(R.string.request_was_aborted))
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
        observable: Observable<ResponseBody?>,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        observable.onTerminateDetach()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) //解决RxJava2导致的内存泄漏问题
            .compose(rxAppCompatActivity.bindToLifecycle())
            .subscribe({ responseBody ->
                val responseString = responseBody?.string()
                LogManager.i(TAG, "responseString*****$responseString")
                onCommonSingleParamCallback.onSuccess(responseString)
                //                                   if (!TextUtils.isEmpty(responseString)) {
//                                       BaseResponse baseResponse
//                                       try {
//                                           baseResponse = JSON.parseObject(responseString, BaseResponse.class)
//                                       } catch (Exception e) {
//                                           //如果不是标准json字符串，就返回错误提示
//                                           onCommonSingleParamCallback.onError(BaseApplication.get().getResources().getString(R.string.server_sneak_off))
//                                           return
//                                       }
//                                       onCommonSingleParamCallback.onSuccess(responseString)
//                                   } else {
//                                       onCommonSingleParamCallback.onError(BaseApplication.get().getResources().getString(R.string.server_sneak_off))
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
            }
            ) { throwable ->
                LogManager.i(TAG, "throwable*****$throwable")
                LogManager.i(
                    TAG,
                    "throwable message*****" + throwable.message
                )
                // 异常处理
                onCommonSingleParamCallback.onError(BaseApplication.get().resources.getString(R.string.request_was_aborted))
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
        observable.onTerminateDetach()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) //解决RxJava2导致的内存泄漏问题
            .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe({ responseBody ->
                val responseString = responseBody?.string()
                LogManager.i(TAG, "responseString*****$responseString")
                onCommonSingleParamCallback.onSuccess(responseString)
            }
            ) { throwable ->
                LogManager.i(TAG, "throwable*****$throwable")
                LogManager.i(
                    TAG,
                    "throwable message*****" + throwable.message
                )
                // 异常处理
                onCommonSingleParamCallback.onError(BaseApplication.get().resources.getString(R.string.request_was_aborted))
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
        observable: Observable<ResponseBody?>,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<String>
    ) {
        observable.onTerminateDetach()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) //解决RxJava2导致的内存泄漏问题
            .compose(rxFragment.bindToLifecycle())
            .subscribe({ responseBody ->
                val responseString = responseBody?.string()
                LogManager.i(TAG, "responseString*****$responseString")
                onCommonSingleParamCallback.onSuccess(responseString)
                //                                   if (!TextUtils.isEmpty(responseString)) {
//                                       BaseResponse baseResponse
//                                       try {
//                                           baseResponse = JSON.parseObject(responseString, BaseResponse.class)
//                                       } catch (Exception e) {
//                                           //如果不是标准json字符串，就返回错误提示
//                                           onCommonSingleParamCallback.onError(BaseApplication.get().getResources().getString(R.string.server_sneak_off))
//                                           return
//                                       }
//                                       onCommonSingleParamCallback.onSuccess(responseString)
//                                   } else {
//                                       onCommonSingleParamCallback.onError(BaseApplication.get().getResources().getString(R.string.server_sneak_off))
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
            }
            ) { throwable ->
                LogManager.i(TAG, "throwable*****$throwable")
                LogManager.i(
                    TAG,
                    "throwable message*****" + throwable.message
                )
                // 异常处理
                onCommonSingleParamCallback.onError(BaseApplication.get().resources.getString(R.string.request_was_aborted))
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
        observable.onTerminateDetach()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()) //解决RxJava2导致的内存泄漏问题
            .compose(rxFragment.bindUntilEvent(FragmentEvent.DESTROY))
            .subscribe({ responseBody ->
                val responseString = responseBody?.string()
                LogManager.i(TAG, "responseString*****$responseString")
                onCommonSingleParamCallback.onSuccess(responseString)
                //                                   if (!TextUtils.isEmpty(responseString)) {
//                                       BaseResponse baseResponse
//                                       try {
//                                           baseResponse = JSON.parseObject(responseString, BaseResponse.class)
//                                       } catch (Exception e) {
//                                           //如果不是标准json字符串，就返回错误提示
//                                           onCommonSingleParamCallback.onError(BaseApplication.get().getResources().getString(R.string.server_sneak_off))
//                                           return
//                                       }
//                                       onCommonSingleParamCallback.onSuccess(responseString)
//                                   } else {
//                                       onCommonSingleParamCallback.onError(BaseApplication.get().getResources().getString(R.string.server_sneak_off))
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
            }
            ) { throwable ->
                LogManager.i(TAG, "throwable*****$throwable")
                LogManager.i(
                    TAG,
                    "throwable message*****" + throwable.message
                )
                // 异常处理
                onCommonSingleParamCallback.onError(BaseApplication.get().resources.getString(R.string.request_was_aborted))
            }
    }

}