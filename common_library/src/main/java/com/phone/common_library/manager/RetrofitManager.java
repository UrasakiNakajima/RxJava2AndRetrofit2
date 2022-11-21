package com.phone.common_library.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.phone.common_library.BaseApplication;
import com.phone.common_library.R;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.common.ConstantUrl;
import com.phone.common_library.interceptor.AddAccessTokenInterceptor;
import com.phone.common_library.interceptor.BaseUrlManagerInterceptor;
import com.phone.common_library.interceptor.CacheControlInterceptor;
import com.phone.common_library.interceptor.ReceivedAccessTokenInterceptor;
import com.phone.common_library.interceptor.RewriteCacheControlInterceptor;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle3.components.support.RxFragment;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce :
 */
public class RetrofitManager {

    private static final String TAG = RetrofitManager.class.getSimpleName();
    private static RetrofitManager manager;
    private final Retrofit retrofit;

    /**
     * 设缓存有效期为两天
     */
    public static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    /**
     * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
     * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
     */
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    /**
     * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
     * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
     */
    private static final String CACHE_CONTROL_AGE = "max-age=0";

    /**
     * 私有构造器 无法外部创建
     * 初始化必要对象和参数
     */
    private RetrofitManager() {
        //缓存
        File cacheFile = new File(BaseApplication.getInstance().getExternalCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 10); //10Mb

        RewriteCacheControlInterceptor rewriteCacheControlInterceptor = new RewriteCacheControlInterceptor();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        // 包含header、body数据
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        HeaderInterceptor headerInterceptor = new HeaderInterceptor();
        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(5 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(5 * 1000, TimeUnit.MILLISECONDS)
                .addInterceptor(new CacheControlInterceptor(BaseApplication.getInstance()))
                .addInterceptor(new AddAccessTokenInterceptor(BaseApplication.getInstance()))
                .addInterceptor(new ReceivedAccessTokenInterceptor(BaseApplication.getInstance()))
                .addInterceptor(new BaseUrlManagerInterceptor(BaseApplication.getInstance()))
                .addInterceptor(rewriteCacheControlInterceptor)
//                .addNetworkInterceptor(rewriteCacheControlInterceptor)
//                .addInterceptor(headerInterceptor)
                .addInterceptor(loggingInterceptor)
                .cache(cache)
                .sslSocketFactory(SSLSocketManager.getSSLSocketFactory())//配置
                .hostnameVerifier(SSLSocketManager.getHostnameVerifier())//配置
                //								  .proxy(Proxy.NO_PROXY)
                .build();

        // 初始化Retrofit
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(ConstantUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static RetrofitManager getInstance() {
        if (manager == null) {
            synchronized (RetrofitManager.class) {
                if (manager == null) {
                    manager = new RetrofitManager();
                }
            }
        }
        return manager;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    /**
     * 返回上传jsonString的RequestBody
     *
     * @param requestData
     * @return
     */
    public RequestBody jsonStringBody(String requestData) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");//"类型,字节码"
        //2.通过RequestBody.create 创建requestBody对象
        return RequestBody.create(mediaType, requestData);
    }

    /**
     * 返回上传文件一对一，并且携带参数的的RequestBody
     *
     * @param bodyParams
     * @param fileMap
     * @return
     */
    public RequestBody multipartBody(Map<String, String> bodyParams, Map<String, File> fileMap) {
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        // form 表单形式上传
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //1.添加请求参数
        //遍历map中所有参数到builder
        if (bodyParams != null && bodyParams.size() > 0) {
            for (String key : bodyParams.keySet()) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams.get(key))) {
                    //如果参数不是null，才把参数传给后台
                    multipartBodyBuilder.addFormDataPart(key, bodyParams.get(key));
                }
            }
        }

        //遍历fileMap中所有图片绝对路径到builder，并约定key如"upload[]"作为php服务器接受多张图片的key
        if (fileMap != null && fileMap.size() > 0) {
            for (String key : fileMap.keySet()) {
                File file = fileMap.get(key);
                if (file != null && file.exists()) {//如果参数不是null，才把参数传给后台
                    multipartBodyBuilder.addFormDataPart(key, file.getName(), RequestBody.create(MEDIA_TYPE, file));
                    LogManager.i(TAG, "file.getName()*****" + file.getName());
                }
            }
        }

        //构建请求体
        RequestBody requestBody = multipartBodyBuilder.build();
        return requestBody;
    }

    /**
     * 返回上传文件一对一和一对多，并且携带参数的RequestBody
     *
     * @param bodyParams
     * @param fileMap
     * @param filesMap
     * @return
     */
    public RequestBody multipartBody2(Map<String, String> bodyParams, Map<String, File> fileMap, Map<String, List<File>> filesMap) {
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        // form 表单形式上传
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //1.添加请求参数
        //遍历map中所有参数到builder
        if (bodyParams != null && bodyParams.size() > 0) {
            for (String key : bodyParams.keySet()) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams.get(key))) {
                    //如果参数不是null，才把参数传给后台
                    multipartBodyBuilder.addFormDataPart(key, bodyParams.get(key));
                }
            }
        }

        //遍历fileMap中所有图片绝对路径到builder，并约定key如"upload[]"作为php服务器接受多张图片的key
        if (fileMap != null && fileMap.size() > 0) {
            for (String key : fileMap.keySet()) {
                File file = fileMap.get(key);
                if (file != null && file.exists()) {//如果参数不是null，才把参数传给后台
                    multipartBodyBuilder.addFormDataPart(key, file.getName(), RequestBody.create(MEDIA_TYPE, file));
                    LogManager.i(TAG, "file.getName()*****" + file.getName());
                }
            }
        }

        //遍历filesMap中所有图片绝对路径到builder，并约定key如"upload[]"作为php服务器接受多张图片的key
        if (filesMap != null && filesMap.size() > 0) {
            for (String key : filesMap.keySet()) {
                List<File> files = filesMap.get(key);
                if (files != null && files.size() > 0) {//如果参数不是null，才把参数传给后台
                    for (int i = 0; i < files.size(); i++) {
                        if (files.get(i) != null && files.get(i).exists()) {
                            multipartBodyBuilder.addFormDataPart(key, files.get(i).getName(), RequestBody.create(MEDIA_TYPE, files.get(i)));
                            LogManager.i(TAG, "files.get(i).getName()*****" + files.get(i).getName());
                        }
                    }
                }
            }
        }

        //构建请求体
        RequestBody requestBody = multipartBodyBuilder.build();
        return requestBody;
    }

    /**
     * 无返回值，接口回调返回字符串
     *
     * @param appCompatActivity
     * @param observable
     * @param onCommonSingleParamCallback
     * @return
     */
    public void responseString(AppCompatActivity appCompatActivity, Observable<ResponseBody> observable, OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        observable.onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //AutoDispose的关键语句
                .as(AutoDispose.<ResponseBody>autoDisposable(AndroidLifecycleScopeProvider.from(appCompatActivity)))
                .subscribe(new Consumer<ResponseBody>() {
                               @Override
                               public void accept(ResponseBody responseBody) throws
                                       Exception {
                                   String responseString = responseBody.string();
                                   LogManager.i(TAG, "responseString*****" + responseString);
                                   onCommonSingleParamCallback.onSuccess(responseString);
                               }
                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogManager.i(TAG, "throwable*****" + throwable.toString());
                                LogManager.i(TAG, "throwable message*****" + throwable.getMessage());
                                // 异常处理
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.request_was_aborted));
                            }
                        }
                );
    }

    /**
     * 无返回值，接口回调返回字符串
     *
     * @param fragment
     * @param observable
     * @param onCommonSingleParamCallback
     * @return
     */
    public void responseString2(Fragment fragment, Observable<ResponseBody> observable, OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        observable.onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //AutoDispose的关键语句（解决RxJava2导致的内存泄漏的）
                .as(AutoDispose.<ResponseBody>autoDisposable(AndroidLifecycleScopeProvider.from(fragment)))
                .subscribe(new Consumer<ResponseBody>() {
                               @Override
                               public void accept(ResponseBody responseBody) throws
                                       Exception {
                                   String responseString = responseBody.string();
                                   LogManager.i(TAG, "responseString*****" + responseString);
                                   onCommonSingleParamCallback.onSuccess(responseString);

                                   //                                   ReadAndWriteManager manager = ReadAndWriteManager.getInstance();
                                   //                                   manager.writeExternal("mineLog.txt",
                                   //                                           responseString,
                                   //                                           new OnCommonSingleParamCallback<Boolean>() {
                                   //                                               @Override
                                   //                                               public void onSuccess(Boolean success) {
                                   //                                                   LogManager.i(TAG, "success*****" + success);
                                   //                                                   manager.unSubscribe();
                                   //                                               }
                                   //
                                   //                                               @Override
                                   //                                               public void onError(String error) {
                                   //                                                   LogManager.i(TAG, "error*****" + error);
                                   //                                                   manager.unSubscribe();
                                   //                                               }
                                   //                                           });
                               }
                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogManager.i(TAG, "throwable*****" + throwable.toString());
                                LogManager.i(TAG, "throwable message*****" + throwable.getMessage());
                                // 异常处理
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.request_was_aborted));
                            }
                        }
                );
    }

    /**
     * 无返回值，接口回调返回字符串
     *
     * @param rxAppCompatActivity
     * @param observable
     * @param onCommonSingleParamCallback
     * @return
     */
    public void responseString3(RxAppCompatActivity rxAppCompatActivity, Observable<ResponseBody> observable, OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        observable.onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //解决RxJava2导致的内存泄漏问题
                .compose(rxAppCompatActivity.<ResponseBody>bindToLifecycle())
                .subscribe(new Consumer<ResponseBody>() {
                               @Override
                               public void accept(ResponseBody responseBody) throws
                                       Exception {
                                   String responseString = responseBody.string();
                                   LogManager.i(TAG, "responseString*****" + responseString);
                                   onCommonSingleParamCallback.onSuccess(responseString);
//                                   if (!TextUtils.isEmpty(responseString)) {
//                                       BaseResponse baseResponse;
//                                       try {
//                                           baseResponse = JSON.parseObject(responseString, BaseResponse.class);
//                                       } catch (Exception e) {
//                                           //如果不是标准json字符串，就返回错误提示
//                                           onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
//                                           return;
//                                       }
//                                       onCommonSingleParamCallback.onSuccess(responseString);
//                                   } else {
//                                       onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
//                                   }


                                   //                                   ReadAndWriteManager manager = ReadAndWriteManager.getInstance();
                                   //                                   manager.writeExternal("mineLog.txt",
                                   //                                           responseString,
                                   //                                           new OnCommonSingleParamCallback<Boolean>() {
                                   //                                               @Override
                                   //                                               public void onSuccess(Boolean success) {
                                   //                                                   LogManager.i(TAG, "success*****" + success);
                                   //                                                   manager.unSubscribe();
                                   //                                               }
                                   //
                                   //                                               @Override
                                   //                                               public void onError(String error) {
                                   //                                                   LogManager.i(TAG, "error*****" + error);
                                   //                                                   manager.unSubscribe();
                                   //                                               }
                                   //                                           });
                               }
                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogManager.i(TAG, "throwable*****" + throwable.toString());
                                LogManager.i(TAG, "throwable message*****" + throwable.getMessage());
                                // 异常处理
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.request_was_aborted));
                            }
                        }
                );
    }

    /**
     * 无返回值，接口回调返回字符串
     *
     * @param rxAppCompatActivity
     * @param observable
     * @param onCommonSingleParamCallback
     * @return
     */
    public void responseString5(RxAppCompatActivity rxAppCompatActivity, Observable<ResponseBody> observable, OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        observable.onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //解决RxJava2导致的内存泄漏问题
                .compose(rxAppCompatActivity.<ResponseBody>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<ResponseBody>() {
                               @Override
                               public void accept(ResponseBody responseBody) throws
                                       Exception {
                                   String responseString = responseBody.string();
                                   LogManager.i(TAG, "responseString*****" + responseString);
                                   onCommonSingleParamCallback.onSuccess(responseString);
                               }
                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogManager.i(TAG, "throwable*****" + throwable.toString());
                                LogManager.i(TAG, "throwable message*****" + throwable.getMessage());
                                // 异常处理
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.request_was_aborted));
                            }
                        }
                );
    }

    /**
     * 无返回值，，接口回调返回字符串
     *
     * @param rxFragment
     * @param observable
     * @param onCommonSingleParamCallback
     * @return
     */
    public void responseString6(RxFragment rxFragment, Observable<ResponseBody> observable, OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        observable.onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //解决RxJava2导致的内存泄漏问题
                .compose(rxFragment.<ResponseBody>bindToLifecycle())
                .subscribe(new Consumer<ResponseBody>() {
                               @Override
                               public void accept(ResponseBody responseBody) throws
                                       Exception {
                                   String responseString = responseBody.string();
                                   LogManager.i(TAG, "responseString*****" + responseString);
                                   onCommonSingleParamCallback.onSuccess(responseString);
//                                   if (!TextUtils.isEmpty(responseString)) {
//                                       BaseResponse baseResponse;
//                                       try {
//                                           baseResponse = JSON.parseObject(responseString, BaseResponse.class);
//                                       } catch (Exception e) {
//                                           //如果不是标准json字符串，就返回错误提示
//                                           onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
//                                           return;
//                                       }
//                                       onCommonSingleParamCallback.onSuccess(responseString);
//                                   } else {
//                                       onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
//                                   }


                                   //                                   ReadAndWriteManager manager = ReadAndWriteManager.getInstance();
                                   //                                   manager.writeExternal("mineLog.txt",
                                   //                                           responseString,
                                   //                                           new OnCommonSingleParamCallback<Boolean>() {
                                   //                                               @Override
                                   //                                               public void onSuccess(Boolean success) {
                                   //                                                   LogManager.i(TAG, "success*****" + success);
                                   //                                                   manager.unSubscribe();
                                   //                                               }
                                   //
                                   //                                               @Override
                                   //                                               public void onError(String error) {
                                   //                                                   LogManager.i(TAG, "error*****" + error);
                                   //                                                   manager.unSubscribe();
                                   //                                               }
                                   //                                           });
                               }
                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogManager.i(TAG, "throwable*****" + throwable.toString());
                                LogManager.i(TAG, "throwable message*****" + throwable.getMessage());
                                // 异常处理
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.request_was_aborted));
                            }
                        }
                );
    }

    /**
     * 无返回值，接口回调返回字符串
     *
     * @param rxFragment
     * @param observable
     * @param onCommonSingleParamCallback
     * @return
     */
    public void responseString7(RxFragment rxFragment, Observable<ResponseBody> observable, OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        observable.onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //解决RxJava2导致的内存泄漏问题
                .compose(rxFragment.<ResponseBody>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<ResponseBody>() {
                               @Override
                               public void accept(ResponseBody responseBody) throws
                                       Exception {
                                   String responseString = responseBody.string();
                                   LogManager.i(TAG, "responseString*****" + responseString);
                                   onCommonSingleParamCallback.onSuccess(responseString);
//                                   if (!TextUtils.isEmpty(responseString)) {
//                                       BaseResponse baseResponse;
//                                       try {
//                                           baseResponse = JSON.parseObject(responseString, BaseResponse.class);
//                                       } catch (Exception e) {
//                                           //如果不是标准json字符串，就返回错误提示
//                                           onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
//                                           return;
//                                       }
//                                       onCommonSingleParamCallback.onSuccess(responseString);
//                                   } else {
//                                       onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
//                                   }


                                   //                                   ReadAndWriteManager manager = ReadAndWriteManager.getInstance();
                                   //                                   manager.writeExternal("mineLog.txt",
                                   //                                           responseString,
                                   //                                           new OnCommonSingleParamCallback<Boolean>() {
                                   //                                               @Override
                                   //                                               public void onSuccess(Boolean success) {
                                   //                                                   LogManager.i(TAG, "success*****" + success);
                                   //                                                   manager.unSubscribe();
                                   //                                               }
                                   //
                                   //                                               @Override
                                   //                                               public void onError(String error) {
                                   //                                                   LogManager.i(TAG, "error*****" + error);
                                   //                                                   manager.unSubscribe();
                                   //                                               }
                                   //                                           });
                               }
                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                LogManager.i(TAG, "throwable*****" + throwable.toString());
                                LogManager.i(TAG, "throwable message*****" + throwable.getMessage());
                                // 异常处理
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.request_was_aborted));
                            }
                        }
                );
    }

    public static String buildSign(String secret, long time) {
        //        Map treeMap = new TreeMap(params);// treeMap默认会以key值升序排序
        StringBuilder sb = new StringBuilder();
        sb.append(secret);
        sb.append(time + "");
        sb.append("1.1.0");
        sb.append("861875048330495");
        sb.append("android");
        Log.d("GlobalConfiguration", "sting:" + sb.toString());
        MessageDigest md5;
        byte[] bytes = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            bytes = md5.digest(sb.toString().getBytes("UTF-8"));// md5加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 将MD5输出的二进制结果转换为小写的十六进制
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex);
        }

        Log.d("GlobalConfiguration", "MD5:" + sign.toString());
        return sign.toString();
    }

    public static String getCacheControl() {
        return isNetworkAvailable(BaseApplication.getInstance()) ? CACHE_CONTROL_AGE : CACHE_CONTROL_CACHE;
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            //如果仅仅是用来判断网络连接
            //connectivityManager.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            //            LogManager.i(TAG, "isNetworkAvailable*****" + info.toString());
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
