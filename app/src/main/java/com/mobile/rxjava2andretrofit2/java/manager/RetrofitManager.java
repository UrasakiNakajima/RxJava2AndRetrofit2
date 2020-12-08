package com.mobile.rxjava2andretrofit2.java.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mobile.rxjava2andretrofit2.java.MineApplication;
import com.mobile.rxjava2andretrofit2.R;
import com.mobile.rxjava2andretrofit2.java.callback.OnCommonSingleParamCallback;
import com.mobile.rxjava2andretrofit2.java.common.Url;
import com.mobile.rxjava2andretrofit2.java.interceptor.AddCookiesInterceptor;
import com.mobile.rxjava2andretrofit2.java.interceptor.LogInterceptor;
import com.mobile.rxjava2andretrofit2.java.interceptor.ReceivedCookiesInterceptor;

import java.io.File;
import java.net.Proxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static final String TAG = "RetrofitManager";
    private static RetrofitManager manager;
    private Retrofit retrofit;

    private Cache cache;

    /**
     * 初始化必要对象和参数
     */
    private RetrofitManager() {
        cache = new Cache(new File(MineApplication.getInstance().getCacheDir(), "HttpCache"), 1024 * 1024 * 100);
        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(5 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(5 * 1000, TimeUnit.MILLISECONDS)
                .cache(cache)
//                .addInterceptor(new CacheControlInterceptor(MineApplication.getInstance()))
                .addInterceptor(new AddCookiesInterceptor(MineApplication.getInstance()))
                .addInterceptor(new ReceivedCookiesInterceptor(MineApplication.getInstance()))
                .addInterceptor(new LogInterceptor())
                .proxy(Proxy.NO_PROXY)
                .build();

        // 初始化Retrofit
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Url.BASE_URL)
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
        RequestBody requestBody = RequestBody.create(mediaType, requestData);
        return requestBody;
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
                if (bodyParams.get(key) != null && !"".equals(bodyParams.get(key))) {//如果参数不是null，才把参数传给后台
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
                if (bodyParams.get(key) != null && !"".equals(bodyParams.get(key))) {//如果参数不是null，才把参数传给后台
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
                if (files != null) {//如果参数不是null，才把参数传给后台
                    if (files != null && files.size() > 0) {
                        for (int i = 0; i < files.size(); i++) {
                            if (files.get(i) != null && files.get(i).exists()) {
                                multipartBodyBuilder.addFormDataPart(key, files.get(i).getName(), RequestBody.create(MEDIA_TYPE, files.get(i)));

                                LogManager.i(TAG, "files.get(i).getName()*****" + files.get(i).getName());
                            }
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
     * 返回Disposable，接口回调返回字符串
     *
     * @param observable
     * @param onCommonSingleParamCallback
     * @return
     */
    public Disposable responseString(Observable<ResponseBody> observable, OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        Disposable disposable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                               @Override
                               public void accept(ResponseBody responseBody) throws Exception {
                                   String responseString = responseBody.string();
                                   LogManager.i(TAG, "responseString*****" + responseString);
                                   onCommonSingleParamCallback.onSuccess(responseString);

                                   ReadAndWriteManager manager = ReadAndWriteManager.getInstance();
                                   manager.writeExternal("mineLog.txt",
                                           responseString,
                                           new OnCommonSingleParamCallback<Boolean>() {
                                               @Override
                                               public void onSuccess(Boolean success) {
                                                   LogManager.i(TAG, "success*****" + success);
                                                   manager.unSubscribe();
                                               }

                                               @Override
                                               public void onError(String error) {
                                                   LogManager.i(TAG, "error*****" + error);
                                                   manager.unSubscribe();
                                               }
                                           });
                               }
                           }
//                        , new Consumer<Throwable>() {
//                            @Override
//                            public void accept(Throwable throwable) throws Exception {
//                                LogManager.i(TAG, "throwable*****" + throwable.toString());
//                                LogManager.i(TAG, "throwable message*****" + throwable.getMessage());
//                                // 异常处理
//                                onCommonSingleParamCallback.onError(MineApplication.getInstance().getResources().getString(R.string.request_was_aborted));
//                            }
//                        }
                );
        return disposable;
    }

//    /**
//     * 返回Disposable，接口回调返回字符串
//     *
//     * @param observable
//     * @param onCommonSingleParamCallback
//     * @return
//     */
//    public Disposable responseData(Observable<FirstPageResponse.QuestionBean> observable, OnCommonSingleParamCallback<FirstPageResponse.QuestionBean> onCommonSingleParamCallback) {
//        Disposable disposable = observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<FirstPageResponse.QuestionBean>() {
//                    @Override
//                    public void accept(FirstPageResponse.QuestionBean success) throws Exception {
////                        String responseString = responseBody.string();
////                        LogManager.i(TAG, "responseString*****" + responseString);
//                        onCommonSingleParamCallback.onSuccess(success);
//
////                        ReadAndWriteManager manager = ReadAndWriteManager.getInstance();
////                        manager.writeExternal("mineLog.txt",
////                                responseString,
////                                new OnCommonSingleParamCallback<Boolean>() {
////                                    @Override
////                                    public void onSuccess(Boolean success) {
////                                        LogManager.i(TAG, "success*****" + success);
////                                        manager.unSubscribe();
////                                    }
////
////                                    @Override
////                                    public void onError(String error) {
////                                        LogManager.i(TAG, "error*****" + error);
////                                        manager.unSubscribe();
////                                    }
////                                });
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        LogManager.i(TAG, "throwable*****" + throwable.toString());
//                        LogManager.i(TAG, "throwable message*****" + throwable.getMessage());
//                        // 异常处理
//                        onCommonSingleParamCallback.onError(MineApplication.getInstance().getResources().getString(R.string.request_was_aborted));
//                    }
//                });
//        return disposable;
//    }

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
