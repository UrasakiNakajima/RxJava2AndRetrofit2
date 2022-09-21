package com.phone.common_library.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.phone.common_library.BaseApplication;
import com.phone.common_library.R;
import com.phone.common_library.base.BaseResponse;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.callback.OnSubThreadToMainThreadCallback;
import com.phone.common_library.interceptor.AddAccessTokenInterceptor;
import com.phone.common_library.interceptor.ReceivedAccessTokenInterceptor;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/10 9:35
 * introduce : 联网okhttp3管理类
 */

public class Okhttp3Manager {

    private static final String TAG = Okhttp3Manager.class.getSimpleName();
    private OkHttpClient client;
    private static Okhttp3Manager manager;
    public static final int UPDATA = 666;

    /**
     * 单例模式，所以构造函数私有化
     */
    private Okhttp3Manager() {
        //创建OkHttpClient对象
        client = new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.MILLISECONDS) //连接超时
                .readTimeout(5000, TimeUnit.MILLISECONDS) //读取超时
                .writeTimeout(5000, TimeUnit.MILLISECONDS) //写入超时
                .addInterceptor(new AddAccessTokenInterceptor(BaseApplication.getInstance())) //拦截器用于设置header
                .addInterceptor(new ReceivedAccessTokenInterceptor(BaseApplication.getInstance())) //拦截器用于接收并持久化cookie
                .sslSocketFactory(SSLSocketManager.getSSLSocketFactory())//配置（只有https请求需要配置）
                .hostnameVerifier(SSLSocketManager.getHostnameVerifier())//配置（只有https请求需要配置）
//                .proxy(Proxy.NO_PROXY)
                .build();
    }

    /**
     * 线程安全的单例模式，整个项目中只有一个okhttp3实例
     *
     * @return
     */
    public static Okhttp3Manager getInstance() {
        if (manager == null) {
            synchronized (Okhttp3Manager.class) {
                if (manager == null) {
                    manager = new Okhttp3Manager();
                }
            }
        }
        return manager;
    }

    public OkHttpClient getClient() {
        return client;
    }

    /**
     * get请求，异步方式，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param onCommonSingleParamCallback
     */
    public void getAsync(String url,
                         OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
        Request request = new Request.Builder().url(url).method("GET", null).build();
//        Request request = new Request.Builder()
//                .url(url)
//                .get()//默认就是GET请求，可以不写（最好写上，要清晰表达出来）
//                .build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = client.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogManager.i(TAG, "getAsync onFailure e*******" + e.toString());
                LogManager.i(TAG, "getAsync onFailure e detailMessage*******" + e.getMessage());
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.network_sneak_off));
                    }
                });
                mainThreadManager.subThreadToUIThread();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseString = response.body().string();
                LogManager.i(TAG, "getAsync onResponse responseString*****" + responseString);
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        if (!TextUtils.isEmpty(responseString)) {
                            BaseResponse baseResponse;
                            try {
                                baseResponse = JSON.parseObject(responseString, BaseResponse.class);
                            } catch (Exception e) {
                                //如果不是标准json字符串，就返回错误提示
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                                return;
                            }
                            onCommonSingleParamCallback.onSuccess(responseString);
                        } else {
                            onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                        }
                    }
                });
                mainThreadManager.subThreadToUIThread();
            }
        });
    }

    /**
     * get请求，添加请求参数，异步方式，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param bodyParams
     * @param onCommonSingleParamCallback
     */
    public void getAsync(String url,
                         Map<String, String> bodyParams,
                         OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        String urlNew = url;
        // 设置HTTP请求参数
        urlNew += getBodyParams(bodyParams);
        //2.创建Request对象，设置一个url地址,设置请求方式。
        Request request = new Request.Builder().url(urlNew).get().build();
//        Request request = new Request.Builder()
//                .url(url)
//                .get()//默认就是GET请求，可以不写（最好写上，要清晰表达出来）
//                .build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = client.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogManager.i(TAG, "getAsync onFailure e*******" + e.toString());
                LogManager.i(TAG, "getAsync onFailure e detailMessage*******" + e.getMessage());
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.network_sneak_off));
                    }
                });
                mainThreadManager.subThreadToUIThread();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseString = response.body().string();
                LogManager.i(TAG, "getAsync onResponse responseString*****" + responseString);
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        if (!TextUtils.isEmpty(responseString)) {
                            BaseResponse baseResponse;
                            try {
                                baseResponse = JSON.parseObject(responseString, BaseResponse.class);
                            } catch (Exception e) {
                                //如果不是标准json字符串，就返回错误提示
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                                return;
                            }
                            onCommonSingleParamCallback.onSuccess(responseString);
                        } else {
                            onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                        }
                    }
                });
                mainThreadManager.subThreadToUIThread();
            }
        });
    }

    /**
     * get请求，添加请求参数和header参数，异步方式，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param headerParams
     * @param bodyParams
     * @param onCommonSingleParamCallback
     */
    public void getAsync(String url,
                         Map<String, String> headerParams,
                         Map<String, String> bodyParams,
                         OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        String urlNew = url;
        // 设置HTTP请求参数
        urlNew += getBodyParams(bodyParams);
        Headers headers = setHeaderParams(headerParams);
        //2.创建Request对象，设置一个url地址,设置请求方式。
        Request request = new Request.Builder().url(urlNew).get().headers(headers).build();
//        Request request = new Request.Builder()
//                .url(url)
//                .get()//默认就是GET请求，可以不写（最好写上，要清晰表达出来）
//                .build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = client.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogManager.i(TAG, "getAsync onFailure e*******" + e.toString());
                LogManager.i(TAG, "getAsync onFailure e detailMessage*******" + e.getMessage());
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.network_sneak_off));
                    }
                });
                mainThreadManager.subThreadToUIThread();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseString = response.body().string();
                LogManager.i(TAG, "getAsync onResponse responseString*****" + responseString);
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        if (!TextUtils.isEmpty(responseString)) {
                            BaseResponse baseResponse;
                            try {
                                baseResponse = JSON.parseObject(responseString, BaseResponse.class);
                            } catch (Exception e) {
                                //如果不是标准json字符串，就返回错误提示
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                                return;
                            }
                            onCommonSingleParamCallback.onSuccess(responseString);
                        } else {
                            onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                        }
                    }
                });
                mainThreadManager.subThreadToUIThread();
            }
        });
    }

    /**
     * 添加参数
     *
     * @param bodyParams
     * @return
     */
    private String getBodyParams(Map<String, String> bodyParams) {
        //1.添加请求参数
        //遍历map中所有参数到builder
        if (bodyParams != null && bodyParams.size() > 0) {
            StringBuffer stringBuffer = new StringBuffer("?");
            for (String key : bodyParams.keySet()) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams.get(key))) {
                    //如果参数不是null并且不是""，就拼接起来
                    stringBuffer.append("&");
                    stringBuffer.append(key);
                    stringBuffer.append("=");
                    stringBuffer.append(bodyParams.get(key));
                }
            }

            return stringBuffer.toString();
        } else {
            return "";
        }
    }

    /**
     * 添加headers
     *
     * @param headerParams
     * @return
     */
    private Headers setHeaderParams(Map<String, String> headerParams) {
        Headers headers = null;
        Headers.Builder headersbuilder = new Headers.Builder();
        if (headerParams != null && headerParams.size() > 0) {
            for (String key : headerParams.keySet()) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(headerParams.get(key))) {
                    //如果参数不是null并且不是""，就拼接起来
                    headersbuilder.add(key, headerParams.get(key));
                }
            }
        }

        headers = headersbuilder.build();
        return headers;
    }

    /**
     * post请求提交字符串，异步方式，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param bodyParams
     * @param onCommonSingleParamCallback
     */
    public void postAsyncString(String url,
                                Map<String, String> bodyParams,
                                OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        JSONObject jsonObject = new JSONObject(bodyParams);
        String requestData = jsonObject.toString();
        LogManager.i(TAG, "postAsyncString requestData*****" + requestData);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");//"类型,字节码"
        //2.通过RequestBody.create 创建requestBody对象
        RequestBody requestBody = RequestBody.create(mediaType, requestData);
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder().url(url).post(requestBody).build();
        //4.创建一个call对象,参数就是Request请求对象
        Call call = client.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogManager.i(TAG, "postAsyncString onFailure e*******" + e.toString());
                LogManager.i(TAG, "postAsyncString onFailure e detailMessage*******" + e.getMessage());
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.network_sneak_off));
                    }
                });
                mainThreadManager.subThreadToUIThread();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseString = response.body().string();
                LogManager.i(TAG, "postAsyncString onResponse responseString*****" + responseString);
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        if (!TextUtils.isEmpty(responseString)) {
                            BaseResponse baseResponse;
                            try {
                                baseResponse = JSON.parseObject(responseString, BaseResponse.class);
                            } catch (Exception e) {
                                //如果不是标准json字符串，就返回错误提示
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                                return;
                            }
                            onCommonSingleParamCallback.onSuccess(responseString);
                        } else {
                            onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                        }
                    }
                });
                mainThreadManager.subThreadToUIThread();
            }
        });
    }

    /**
     * post请求提交流，异步方式，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param requestData
     * @param onCommonSingleParamCallback
     */
    public void postAsyncStream(String url,
                                String requestData,
                                OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        LogManager.i(TAG, "requestData*****" + requestData);

        //2.通过new RequestBody 创建requestBody对象
        RequestBody requestBody = new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return MediaType.parse("text/x-markdown; charset=utf-8");
            }

            @Override
            public void writeTo(@NonNull BufferedSink sink) throws IOException {
                sink.writeUtf8(requestData);
            }
        };
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder().url(url).post(requestBody).build();
        //4.创建一个call对象,参数就是Request请求对象
        Call call = client.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogManager.i(TAG, "postAsyncStream onFailure e*******" + e.toString());
                LogManager.i(TAG, "postAsyncStream onFailure e detailMessage*******" + e.getMessage());
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.network_sneak_off));
                    }
                });
                mainThreadManager.subThreadToUIThread();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseString = response.body().string();
                LogManager.i(TAG, "postAsyncStream onResponse responseString*****" + responseString);
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        if (!TextUtils.isEmpty(responseString)) {
                            BaseResponse baseResponse;
                            try {
                                baseResponse = JSON.parseObject(responseString, BaseResponse.class);
                            } catch (Exception e) {
                                //如果不是标准json字符串，就返回错误提示
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                                return;
                            }
                            onCommonSingleParamCallback.onSuccess(responseString);
                        } else {
                            onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                        }
                    }
                });
                mainThreadManager.subThreadToUIThread();
            }
        });
    }

    /**
     * post请求提交键值对，异步方式，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param bodyParams
     * @param onCommonSingleParamCallback
     */
    public void postAsyncKeyValuePairs(String url,
                                       Map<String, String> bodyParams,
                                       OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        if (bodyParams != null && bodyParams.size() > 0) {
            LogManager.i(TAG, "postAsyncKeyValuePairs bodyParams*****" + bodyParams.toString());
            LogManager.i(TAG, "postAsyncKeyValuePairs bodyParams json*****" + MapManager.mapToJsonStr(bodyParams));
        }

        //2.通过new FormBody()调用build方法,创建一个RequestBody,可以用add添加键值对
        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        //1.添加请求参数
        //遍历map中所有参数到builder
        if (bodyParams != null && bodyParams.size() > 0) {
            for (String key : bodyParams.keySet()) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams.get(key))) {
                    //如果参数不是null，才把参数传给后台
                    formEncodingBuilder.add(key, bodyParams.get(key));
                }
            }
        }

        //构建请求体
        RequestBody requestBody = formEncodingBuilder.build();
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder().url(url).post(requestBody).build();
        //4.创建一个call对象,参数就是Request请求对象
        Call call = client.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogManager.i(TAG, "postAsyncKeyValuePairs onFailure e*******" + e.toString());
                LogManager.i(TAG, "postAsyncKeyValuePairs onFailure e detailMessage*******" + e.getMessage());
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.network_sneak_off));
                    }
                });
                mainThreadManager.subThreadToUIThread();

//                MainThreadManager mainThreadManager2 =
//                        new MainThreadManager(new OnSubThreadToMainThreadCallback() {
//                            @Override
//                            public void onSuccess() {
//                                onCommonSingleParamCallback.onError(context.getResources().getString(R.string.network_sneak_off));
//                            }
//                        });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseString = response.body().string();
                LogManager.i(TAG, "postAsyncKeyValuePairs onResponse responseString*****" + responseString);
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        if (!TextUtils.isEmpty(responseString)) {
                            BaseResponse baseResponse;
                            try {
                                baseResponse = JSON.parseObject(responseString, BaseResponse.class);
                            } catch (Exception e) {
                                //如果不是标准json字符串，就返回错误提示
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                                return;
                            }
                            onCommonSingleParamCallback.onSuccess(responseString);
                        } else {
                            onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                        }
                    }
                });
                mainThreadManager.subThreadToUIThread();
            }
        });
    }

    /**
     * post请求不携带参数，异步方式，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param onCommonSingleParamCallback
     */
    public void postAsync(String url,
                          OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        //这句话是重点Request
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder().post(RequestBody.create(null, "")).url(url).build();
        //4.创建一个call对象,参数就是Request请求对象
        Call call = client.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogManager.i(TAG, "postAsyncKeyValuePairs onFailure e*******" + e.toString());
                LogManager.i(TAG, "postAsyncKeyValuePairs onFailure e detailMessage*******" + e.getMessage());
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.network_sneak_off));
                    }
                });
                mainThreadManager.subThreadToUIThread();

//                MainThreadManager mainThreadManager2 =
//                        new MainThreadManager(new OnSubThreadToMainThreadCallback() {
//                            @Override
//                            public void onSuccess() {
//                                onCommonSingleParamCallback.onError(context.getResources().getString(R.string.network_sneak_off));
//                            }
//                        });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseString = response.body().string();
                LogManager.i(TAG, "postAsyncKeyValuePairs onResponse responseString*****" + responseString);
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        if (!TextUtils.isEmpty(responseString)) {
                            BaseResponse baseResponse;
                            try {
                                baseResponse = JSON.parseObject(responseString, BaseResponse.class);
                            } catch (Exception e) {
                                //如果不是标准json字符串，就返回错误提示
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                                return;
                            }
                            onCommonSingleParamCallback.onSuccess(responseString);
                        } else {
                            onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                        }
                    }
                });
                mainThreadManager.subThreadToUIThread();
            }
        });
    }

    /**
     * post请求上传Form表单，异步方式，提交表单，是在子线程中执行的，需要切换到主线程才能更新UI
     * 这个函数可以把服务器返回的数据统一处理
     *
     * @param url
     * @param bodyParams
     * @param onCommonSingleParamCallback
     */
    public void postAsyncForm(String url,
                              Map<String, String> bodyParams,
                              OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        if (bodyParams != null && bodyParams.size() > 0) {
            LogManager.i(TAG, "postAsyncForm bodyParams String*******" + bodyParams.toString());
            LogManager.i(TAG, "postAsyncKeyValuePairs bodyParams json*****" + MapManager.mapToJsonStr(bodyParams));
        }

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

        //构建请求体
        RequestBody requestBody = multipartBodyBuilder.build();
        Request request = new Request.Builder().post(requestBody).url(url).build();
        //3 将Request封装为Call
        Call call = client.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogManager.i(TAG, "postAsyncForm onFailure e*******" + e.toString());
                LogManager.i(TAG, "postAsyncForm onFailure e detailMessage*******" + e.getMessage());
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.network_sneak_off));
                    }
                });
                mainThreadManager.subThreadToUIThread();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                //服务器返回的是加密字符串，要解密
//                String dataEncrypt = response.body().string();
//                LogManager.i(TAG, "login onResponse dataEncrypt*****" + dataEncrypt);
                String responseString = response.body().string();
//                try {
//                    responseString = AESManager.aesDecrypt(dataEncrypt);
                LogManager.i(TAG, "postAsyncForm onResponse responseString*****" + responseString);
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        if (!TextUtils.isEmpty(responseString)) {
                            BaseResponse baseResponse;
                            try {
                                baseResponse = JSON.parseObject(responseString, BaseResponse.class);
                            } catch (Exception e) {
                                //如果不是标准json字符串，就返回错误提示
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                                return;
                            }
                            onCommonSingleParamCallback.onSuccess(responseString);
                        } else {
                            onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                        }
                    }
                });
                mainThreadManager.subThreadToUIThread();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    /**
     * post请求上传Form表单和图片文件（上传java服务器），异步方式，提交表单，是在子线程中执行的，需要切换到主线程才能更新UI
     * 这个函数可以把服务器返回的数据统一处理
     *
     * @param url
     * @param bodyParams
     * @param fileList
     * @param onCommonSingleParamCallback
     */
    public void postAsyncFormAndFiles(String url,
                                      Map<String, String> bodyParams,
                                      List<File> fileList,
                                      OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        if (bodyParams != null && bodyParams.size() > 0) {
            LogManager.i(TAG, "postAsyncForm bodyParams String*******" + bodyParams.toString());
            LogManager.i(TAG, "postAsyncKeyValuePairs bodyParams json*****" + MapManager.mapToJsonStr(bodyParams));
        }
        //        MediaType MEDIA_TYPE = MediaType.parse("image/png");
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

        //遍历fileList中所有图片绝对路径到builder，并约定key如"upload"作为上传php服务器接受多张图片的key
        if (fileList != null && fileList.size() > 0) {
            for (int i = 0; i < fileList.size(); i++) {
                if (fileList.get(i).exists()) {
                    multipartBodyBuilder.addFormDataPart("upload", fileList.get(i).getName(), RequestBody.create(MEDIA_TYPE, fileList.get(i)));
                }
            }
        } else {
            LogManager.i(TAG, "postAsyncFormAndFile fileList.size() = 0");
        }

        //构建请求体
        RequestBody requestBody = multipartBodyBuilder.build();
        Request request = new Request.Builder().post(requestBody).url(url).build();
        //3 将Request封装为Call
        Call call = client.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogManager.i(TAG, "postAsyncFormAndFiles onFailure e*******" + e.toString());
                LogManager.i(TAG, "postAsyncFormAndFiles onFailure e detailMessage*******" + e.getMessage());
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.network_sneak_off));
                    }
                });
                mainThreadManager.subThreadToUIThread();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseString = response.body().string();
                LogManager.i(TAG, "postAsyncFormAndFiles onResponse responseString*****" + responseString);
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        if (!TextUtils.isEmpty(responseString)) {
                            BaseResponse baseResponse;
                            try {
                                baseResponse = JSON.parseObject(responseString, BaseResponse.class);
                            } catch (Exception e) {
                                //如果不是标准json字符串，就返回错误提示
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                                return;
                            }
                            onCommonSingleParamCallback.onSuccess(responseString);
                        } else {
                            onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                        }
                    }
                });
                mainThreadManager.subThreadToUIThread();
            }
        });
    }

    /**
     * post请求上传Form表单和文件（上传java服务器），异步方式，提交表单，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param bodyParams
     * @param fileMap
     */
    public void postAsyncFormAndFiles(String url,
                                      Map<String, String> bodyParams,
                                      Map<String, File> fileMap,
                                      Map<String, List<File>> filesMap,
                                      OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        if (bodyParams != null && bodyParams.size() > 0) {
            LogManager.i(TAG, "postAsyncForm bodyParams String*******" + bodyParams.toString());
            LogManager.i(TAG, "postAsyncKeyValuePairs bodyParams json*****" + MapManager.mapToJsonStr(bodyParams));
        }
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
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams.get(key))) {
                    //如果参数不是null，才把参数传给后台
                    multipartBodyBuilder.addFormDataPart(key, fileMap.get(key).getName(), RequestBody.create(MEDIA_TYPE, fileMap.get(key)));

                    LogManager.i(TAG, "fileMap.get(key).getName()*****" + fileMap.get(key).getName());
                }
            }
        }

        //遍历filesMap中所有图片绝对路径到builder，并约定key如"upload[]"作为php服务器接受多张图片的key
        if (filesMap != null && filesMap.size() > 0) {
            for (String key : filesMap.keySet()) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams.get(key))) {
                    //如果参数不是null，才把参数传给后台
                    if (filesMap.get(key) != null && filesMap.get(key).size() > 0) {
                        for (int i = 0; i < filesMap.get(key).size(); i++) {
                            multipartBodyBuilder.addFormDataPart(key, filesMap.get(key).get(i).getName(), RequestBody.create(MEDIA_TYPE, filesMap.get(key).get(i)));

                            LogManager.i(TAG, "filesMap.get(key).get(i).getName()*****" + filesMap.get(key).get(i).getName());
                        }
                    }
                }
            }
        }

        //构建请求体
        RequestBody requestBody = multipartBodyBuilder.build();
        Request request = new Request.Builder().post(requestBody).url(url).build();
        //3 将Request封装为Call
        Call call = client.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogManager.i(TAG, "postAsyncFormAndFiles onFailure e*******" + e.toString());
                LogManager.i(TAG, "postAsyncFormAndFiles onFailure e detailMessage*******" + e.getMessage());
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.network_sneak_off));
                    }
                });
                mainThreadManager.subThreadToUIThread();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseString = response.body().string();
                LogManager.i(TAG, "postAsyncFormAndFiles onResponse responseString*****" + responseString);
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        if (!TextUtils.isEmpty(responseString)) {
                            BaseResponse baseResponse;
                            try {
                                baseResponse = JSON.parseObject(responseString, BaseResponse.class);
                            } catch (Exception e) {
                                //如果不是标准json字符串，就返回错误提示
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                                return;
                            }
                            onCommonSingleParamCallback.onSuccess(responseString);
                        } else {
                            onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                        }
                    }
                });
                mainThreadManager.subThreadToUIThread();
            }
        });
    }

    /**
     * post请求上传Form表单和文件（上传php服务器），异步方式，提交表单，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param bodyParams
     * @param fileList
     * @param onCommonSingleParamCallback
     */
    public void postAsyncPhpFormAndFiles(String url,
                                         Map<String, String> bodyParams,
                                         List<File> fileList,
                                         OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        if (bodyParams != null && bodyParams.size() > 0) {
            LogManager.i(TAG, "postAsyncForm bodyParams String*******" + bodyParams.toString());
            LogManager.i(TAG, "postAsyncKeyValuePairs bodyParams json*****" + MapManager.mapToJsonStr(bodyParams));
        }
        MediaType MEDIA_TYPE = MediaType.parse("image/png");
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

        //遍历fileList中所有图片绝对路径到builder，并约定key如"upload[]"作为php服务器接受多张图片的key
        if (fileList != null && fileList.size() > 0) {
            for (int i = 0; i < fileList.size(); i++) {
                if (fileList.get(i).exists()) {
                    multipartBodyBuilder.addFormDataPart("upload[]", fileList.get(i).getName(), RequestBody.create(MEDIA_TYPE, fileList.get(i)));
                }
            }
        }


        //构建请求体
        RequestBody requestBody = multipartBodyBuilder.build();
        Request request = new Request.Builder().post(requestBody).url(url).build();
        //3 将Request封装为Call
        Call call = client.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogManager.i(TAG, "postAsyncFormAndFiles onFailure e*******" + e.toString());
                LogManager.i(TAG, "postAsyncFormAndFiles onFailure e detailMessage*******" + e.getMessage());
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.network_sneak_off));
                    }
                });
                mainThreadManager.subThreadToUIThread();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseString = response.body().string();
                LogManager.i(TAG, "postAsyncFormAndFiles onResponse responseString*****" + responseString);
                MainThreadManager mainThreadManager = new MainThreadManager();
                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
                    @Override
                    public void onSuccess() {
                        if (!TextUtils.isEmpty(responseString)) {
                            BaseResponse baseResponse;
                            try {
                                baseResponse = JSON.parseObject(responseString, BaseResponse.class);
                            } catch (Exception e) {
                                //如果不是标准json字符串，就返回错误提示
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                                return;
                            }
                            onCommonSingleParamCallback.onSuccess(responseString);
                        } else {
                            onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.server_sneak_off));
                        }
                    }
                });
                mainThreadManager.subThreadToUIThread();
            }
        });
    }

//    /**
//     * get请求下载文件，是在子线程中执行的，需要切换到主线程才能更新UI
//     *
//     * @param url
//     * @param bodyParams
//     * @param onCommonSingleParamCallback
//     */
//    public void getAppContentLength(String url,
//                                           Map<String, String> bodyParams,
//                                           OnCommonSingleParamCallback<Long> onCommonSingleParamCallback) {
//        LogManager.i(TAG, "bodyParams String*******" + bodyParams.toString());
//        Request request = new Request.Builder()
//                .get()
//                .url(url)
//                .build();
//        //3 将Request封装为Call
//        Call call = client.newCall(request);
//        //4 执行Call
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                LogManager.i(TAG, "getAppContentLength onFailure e*******" + e.toString());
//                LogManager.i(TAG, "getAppContentLength onFailure e detailMessage*******" + e.getMessage());
//                MainThreadManager mainThreadManager = new MainThreadManager();
//                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
//                    @Override
//                    public void onSuccess() {
//                        onCommonSingleParamCallback.onError(context.getResources().getString(R.string.network_sneak_off));
//                    }
//                });
//                mainThreadManager.subThreadToUIThread();
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if (response != null && response.isSuccessful()) {
//                    long contentLength = response.body().contentLength();
//                    LogManager.i(TAG, "getAppContentLength onResponse contentLength*******" + contentLength);
//                    MainThreadManager mainThreadManager = new MainThreadManager();
//                    mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
//                        @Override
//                        public void onSuccess() {
//                            onCommonSingleParamCallback.onSuccess(contentLength);
//                        }
//                    });
//                    mainThreadManager.subThreadToUIThread();
//                }
//            }
//        });
//    }
//
//    /**
//     * get请求下载文件，是在子线程中执行的，需要切换到主线程才能更新UI
//     *
//     * @param url
//     * @param bodyParams
//     * @param onDownloadCallback
//     */
//    public void downloadApp(String url, Map<String, String> bodyParams, OnDownloadCallback<String> onDownloadCallback) {
//        LogManager.i(TAG, "downloadApp bodyParams String*******" + bodyParams.toString());
//        String alreadyDownloadLength = null;
//        String appContentLength = null;
//        //遍历map中所有参数到builder
//        if (bodyParams != null && bodyParams.size() > 0) {
//            if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams.get(key))) {
//                    //如果参数不是null，才把参数传给后台
//                if ("alreadyDownloadLength".equals(key)) {
//                    alreadyDownloadLength = bodyParams.get(key);
//                } else if ("appContentLength".equals(key)) {
//                    appContentLength = bodyParams.get(key);
//                }
//            }
//        }
//
//        Request request = new Request.Builder()
//                .get()
//                //确定下载的范围,添加此头,则服务器就可以跳过已经下载好的部分
//                .addHeader("Range", "bytes=" + alreadyDownloadLength + "-" + appContentLength)
//                .url(url)
//                .build();
//        //3 将Request封装为Call
//        Call call = client.newCall(request);
//        //4 执行Call
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                LogManager.i(TAG, "getAppContentLength onFailure e*******" + e.toString());
//                LogManager.i(TAG, "getAppContentLength onFailure e detailMessage*******" + e.getMessage());
//                MainThreadManager mainThreadManager = new MainThreadManager();
//                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
//                    @Override
//                    public void onSuccess() {
//                        onDownloadCallback.onError(context.getResources().getString(R.string.network_sneak_off));
//                    }
//                });
//                mainThreadManager.subThreadToUIThread();
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                String alreadyDownloadLength = null;
//                String appContentLength = null;
//                String fileName = null;
//                String FILEPATH = null;
//                //遍历map中所有参数到builder
//                if (bodyParams != null && bodyParams.size() > 0) {
//                    for (String key : bodyParams.keySet()) {
//                        if ("alreadyDownloadLength".equals(key)) {
//                            alreadyDownloadLength = bodyParams.get(key);
//                            LogManager.i(TAG, "downloadApp alreadyDownloadLength*****" + alreadyDownloadLength);
//                        } else if ("appContentLength".equals(key)) {
//                            appContentLength = bodyParams.get(key);
//                            LogManager.i(TAG, "downloadApp appContentLength*****" + appContentLength);
//                        } else if ("fileName".equals(key)) {
//                            fileName = bodyParams.get(key);
//
//                            //查询数据库，看看有没有数据
//                            AppInfo appInfo = AppInfoDaoManager.getInstance(context).queryAppInfoById(1);
//                            if (appInfo != null) {
//                                LogManager.i(TAG, "versionUpdate appInfoList*****" + appInfo.toString());
//                                //如果存在数据，则更新这条数据
//                                appInfo.set_id(appInfo.get_id());
//                                appInfo.setFileName(fileName);
//                                AppInfoDaoManager.getInstance(context).updateAppInfo(appInfo);
//                            } else {
//                                appInfo = new AppInfo();
//                                appInfo.setFileName(fileName);
//                                //如果不存在数据，则插入这条数据
//                                AppInfoDaoManager.getInstance(context).insertAppInfo(appInfo);
//                            }
//                            LogManager.i(TAG, "downloadApp fileName*****" + fileName);
//                        } else if ("FILEPATH".equals(key)) {
//                            FILEPATH = bodyParams.get(key);
//                            LogManager.i(TAG, "downloadApp FILEPATH*****" + FILEPATH);
//                        }
//                    }
//                }
//                if (appContentLength != null && !"".equals(appContentLength) &&
//                        fileName != null && !"".equals(fileName) &&
//                        FILEPATH != null && !"".equals(FILEPATH)) {
//                    long alreadyDownloadLengthL = Long.valueOf(alreadyDownloadLength);
//                    long appContentLengthL = Long.valueOf(appContentLength);
//                    LogManager.i(TAG, "downloadApp alreadyDownloadLengthL*****" + alreadyDownloadLengthL);
//                    LogManager.i(TAG, "downloadApp appContentLengthL*****" + appContentLengthL);
//                    File dirs = new File(FILEPATH);
//                    if (!dirs.exists()) {
//                        LogManager.i(TAG, "downloadApp*****!dirs.exists()");
//                        dirs.mkdirs();
//                    }
//                    File file = new File(dirs, fileName);
//                    if (!file.exists()) {
//                        file.createNewFile();
//                        LogManager.i(TAG, "downloadApp*****!file.exists()");
//                    } else {
//                        LogManager.i(TAG, "downloadApp file.getAbsolutePath()*****" + file.getAbsolutePath());
//                    }
//
//                    if (file.exists() && appContentLengthL == file.length()) {
//                        //查询数据库，看看有没有数据
//                        AppInfo appInfo = AppInfoDaoManager.getInstance(context).queryAppInfoById(1);
//                        if (appInfo != null) {
//                            LogManager.i(TAG, "versionUpdate appInfoList*****" + appInfo.toString());
//                            //如果存在数据，则更新这条数据
//                            appInfo.set_id(appInfo.get_id());
//                            appInfo.setFileName(fileName);
//                            appInfo.setAppContentLength(appContentLengthL);
//                            AppInfoDaoManager.getInstance(context).updateAppInfo(appInfo);
//                        } else {
//                            appInfo = new AppInfo();
//                            appInfo.setFileName(fileName);
//                            appInfo.setAppContentLength(appContentLengthL);
//                            //如果不存在数据，则插入这条数据
//                            AppInfoDaoManager.getInstance(context).insertAppInfo(appInfo);
//                        }
//                        MainThreadManager mainThreadManager = new MainThreadManager();
//                        mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
//                            @Override
//                            public void onSuccess() {
//                                onDownloadCallback.onDownloading(appContentLengthL);
//                                onDownloadCallback.onSuccess("");
//                            }
//                        });
//                        mainThreadManager.subThreadToUIThread();
//                    } else {
//                        InputStream inputStream = null;
//                        BufferedInputStream bufferedInputStream = null;
//                        FileOutputStream fileOutputStream = null;
//                        BufferedOutputStream bufferedOutputStream = null;
//                        try {
//                            inputStream = response.body().byteStream();
//                            bufferedInputStream = new BufferedInputStream(inputStream);
//                            fileOutputStream = new FileOutputStream(file, false);
//                            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
//                            int bytesPerBuffer = 1024 * 1024;
//                            byte[] buffer = new byte[bytesPerBuffer];//缓冲数组1024kB
//                            int len;
//
//                            long alreadyDownloadLengthDownloading = alreadyDownloadLengthL;
//                            while ((len = bufferedInputStream.read(buffer)) != -1) {
//                                bufferedOutputStream.write(buffer, 0, len);
//                                alreadyDownloadLengthDownloading += len;
//                                if (Okhttp3Manager.isNetworkAvailable(context)) {//连网呢
//                                    long finalAlreadyDownloadLengthDownloading = alreadyDownloadLengthDownloading;
//                                    MainThreadManager mainThreadManager = new MainThreadManager();
//                                    mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
//                                        @Override
//                                        public void onSuccess() {
//                                            onDownloadCallback.onDownloading(finalAlreadyDownloadLengthDownloading);
//                                        }
//                                    });
//                                    mainThreadManager.subThreadToUIThread();
//                                    LogManager.i(TAG, "alreadyDownloadLengthDownloading=" + alreadyDownloadLengthDownloading);
//                                } else {//没连网呢
//                                    onDownloadCallback.onError(context.getResources().getString(R.string.please_check_the_network_connection));
//                                    break;
//                                }
//                            }
//
//                            bufferedOutputStream.flush();
//                            if (file.exists()) {
//                                alreadyDownloadLengthDownloading = file.length();
//                            }
//                            if (alreadyDownloadLengthDownloading == appContentLengthL) {
//                                //查询数据库，看看有没有数据
//                                AppInfo appInfo = AppInfoDaoManager.getInstance(context).queryAppInfoById(1);
//                                if (appInfo != null) {
//                                    LogManager.i(TAG, "versionUpdate appInfoList*****" + appInfo.toString());
//                                    //如果存在数据，则更新这条数据
//                                    appInfo.set_id(appInfo.get_id());
//                                    appInfo.setFileName(fileName);
//                                    appInfo.setAppContentLength(alreadyDownloadLengthDownloading);
//                                    AppInfoDaoManager.getInstance(context).updateAppInfo(appInfo);
//                                } else {
//                                    appInfo = new AppInfo();
//                                    appInfo.setFileName(fileName);
//                                    appInfo.setAppContentLength(alreadyDownloadLengthDownloading);
//                                    //如果不存在数据，则插入这条数据
//                                    AppInfoDaoManager.getInstance(context).insertAppInfo(appInfo);
//                                }
//
//                                MainThreadManager mainThreadManager = new MainThreadManager();
//                                mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
//                                    @Override
//                                    public void onSuccess() {
//                                        onDownloadCallback.onSuccess("");
//                                    }
//                                });
//                                mainThreadManager.subThreadToUIThread();
//                            } else {
//                                onDownloadCallback.onError("下载失败，请检查网络连接");
//                            }
//                        } finally {
//                            //关闭IO流
//                            IOManager.closeAll(bufferedInputStream, bufferedOutputStream);
//                        }
//                    }
//                } else {
//                    LogManager.i(TAG, "downloadApp*****" + "找不到文件名");
//                    onDownloadCallback.onError("找不到文件名");
//                }
//            }
//        });
//    }
//
//    /**
//     * get请求下载文件，是在子线程中执行的，需要切换到主线程才能更新UI
//     *
//     * @param url
//     * @param bodyParams
//     * @param callback
//     */
//    public void getDownloadApp(String url, Map<String, String> bodyParams, Callback callback) {
//        LogManager.i(TAG, "bodyParams String*******" + bodyParams.toString());
//        String alreadyDownloadLength = null;
//        String appContentLength = null;
//        //遍历map中所有参数到builder
//        if (bodyParams != null && bodyParams.size() > 0) {
//            for (String key : bodyParams.keySet()) {
//    if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams.get(key))) {
//        //如果参数不是null，才把参数传给后台
//                if ("alreadyDownloadLength".equals(key)) {
//                    alreadyDownloadLength = bodyParams.get(key);
//                } else if ("appContentLength".equals(key)) {
//                    appContentLength = bodyParams.get(key);
//                }
//}
//            }
//        }
//
//        LogManager.i(TAG, "getDownloadApp alreadyDownloadLength*******" + alreadyDownloadLength);
//        LogManager.i(TAG, "getDownloadApp appContentLength*******" + appContentLength);
//        Request request = null;
//        if (alreadyDownloadLength != null && !"".equals(alreadyDownloadLength)
//                && appContentLength != null && !"".equals(appContentLength)) {
//            request = new Request.Builder()
//                    //确定下载的范围,添加此头,则服务器就可以跳过已经下载好的部分
//                    .addHeader("range", "bytes=" + alreadyDownloadLength + "-" + appContentLength)
//                    .url(url)
//                    .build();
//        } else {
//            request = new Request.Builder()
//                    //确定下载的范围,添加此头,则服务器就可以跳过已经下载好的部分
////                .addHeader("RANGE", "bytes=" + alreadyDownloadLengthL + "-" + appContentLengthL)
//                    .url(url)
//                    .build();
//        }
//
//        //3 将Request封装为Call
//        Call call = client.newCall(request);
//        //4 执行Call
//        call.enqueue(callback);
//    }

    /**
     * post的请求参数，构造RequestBody
     *
     * @param bodyParams
     * @return
     */
    private RequestBody setRequestBody(Map<String, String> bodyParams) {
        RequestBody body = null;
        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        if (bodyParams != null) {
            Iterator<String> iterator = bodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next();
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams.get(key))) {
                    //如果参数不是null，才把参数传给后台
                    formEncodingBuilder.add(key, bodyParams.get(key));
                }
            }
        }
        body = formEncodingBuilder.build();
        return body;
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
