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

public class RetrofitManager {

    private static final String TAG = "RetrofitManager";
    private static RetrofitManager manager;
    private Retrofit retrofit;

    /**
     * ???????????????????????????
     */
    public static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    /**
     * ???????????????Cache-Control????????????if-only-cache?????????????????????????????????????????????max-stale????????????????????????????????????
     * max-stale ???????????????????????????????????????????????????????????????????????????max-stale???????????????????????????????????????????????????????????????????????????????????????
     */
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    /**
     * ???????????????Cache-Control???????????????Cache-Control??????max-age=0
     * (??????????????????????????????a?????????????????????????????????max-age??????????????????????????????????????????????????????????????????????????????????????????????????????)??????????????????????????????????????????
     */
    private static final String CACHE_CONTROL_AGE = "max-age=0";

    /**
     * ??????????????? ??????????????????
     * ??????????????????????????????
     */
    private RetrofitManager() {
        //??????
        File cacheFile = new File(BaseApplication.getInstance().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb

        RewriteCacheControlInterceptor rewriteCacheControlInterceptor = new RewriteCacheControlInterceptor();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        // ??????header???body??????
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        HeaderInterceptor headerInterceptor = new HeaderInterceptor();
        // ?????????okhttp
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
                .sslSocketFactory(SSLSocketManager.getSSLSocketFactory())//??????
                .hostnameVerifier(SSLSocketManager.getHostnameVerifier())//??????
                //								  .proxy(Proxy.NO_PROXY)
                .build();

        // ?????????Retrofit
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
     * ????????????jsonString???RequestBody
     *
     * @param requestData
     * @return
     */
    public RequestBody jsonStringBody(String requestData) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");//"??????,?????????"
        //2.??????RequestBody.create ??????requestBody??????
        RequestBody requestBody = RequestBody.create(mediaType, requestData);
        return requestBody;
    }

    /**
     * ??????????????????????????????????????????????????????RequestBody
     *
     * @param bodyParams
     * @param fileMap
     * @return
     */
    public RequestBody multipartBody(Map<String, String> bodyParams, Map<String, File> fileMap) {
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        // form ??????????????????
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //1.??????????????????
        //??????map??????????????????builder
        if (bodyParams != null && bodyParams.size() > 0) {
            for (String key : bodyParams.keySet()) {
                if (bodyParams.get(key) != null && !"".equals(bodyParams.get(key))) {//??????????????????null???????????????????????????
                    multipartBodyBuilder.addFormDataPart(key, bodyParams.get(key));
                }
            }
        }

        //??????fileMap??????????????????????????????builder????????????key???"upload[]"??????php??????????????????????????????key
        if (fileMap != null && fileMap.size() > 0) {
            for (String key : fileMap.keySet()) {
                File file = fileMap.get(key);
                if (file != null && file.exists()) {//??????????????????null???????????????????????????
                    multipartBodyBuilder.addFormDataPart(key, file.getName(), RequestBody.create(MEDIA_TYPE, file));

                    LogManager.i(TAG, "file.getName()*****" + file.getName());
                }
            }
        }

        //???????????????
        RequestBody requestBody = multipartBodyBuilder.build();
        return requestBody;
    }

    /**
     * ???????????????????????????????????????????????????????????????RequestBody
     *
     * @param bodyParams
     * @param fileMap
     * @param filesMap
     * @return
     */
    public RequestBody multipartBody2(Map<String, String> bodyParams, Map<String, File> fileMap, Map<String, List<File>> filesMap) {
        MediaType MEDIA_TYPE = MediaType.parse("image/*");
        // form ??????????????????
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //1.??????????????????
        //??????map??????????????????builder
        if (bodyParams != null && bodyParams.size() > 0) {
            for (String key : bodyParams.keySet()) {
                if (bodyParams.get(key) != null && !"".equals(bodyParams.get(key))) {//??????????????????null???????????????????????????
                    multipartBodyBuilder.addFormDataPart(key, bodyParams.get(key));
                }
            }
        }

        //??????fileMap??????????????????????????????builder????????????key???"upload[]"??????php??????????????????????????????key
        if (fileMap != null && fileMap.size() > 0) {
            for (String key : fileMap.keySet()) {
                File file = fileMap.get(key);
                if (file != null && file.exists()) {//??????????????????null???????????????????????????
                    multipartBodyBuilder.addFormDataPart(key, file.getName(), RequestBody.create(MEDIA_TYPE, file));

                    LogManager.i(TAG, "file.getName()*****" + file.getName());
                }
            }
        }

        //??????filesMap??????????????????????????????builder????????????key???"upload[]"??????php??????????????????????????????key
        if (filesMap != null && filesMap.size() > 0) {
            for (String key : filesMap.keySet()) {
                List<File> files = filesMap.get(key);
                if (files != null) {//??????????????????null???????????????????????????
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

        //???????????????
        RequestBody requestBody = multipartBodyBuilder.build();
        return requestBody;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param activity
     * @param observable
     * @param onCommonSingleParamCallback
     * @return
     */
    public void responseStringAutoDispose(AppCompatActivity activity, Observable<ResponseBody> observable, OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        observable.onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //AutoDispose???????????????
                .as(AutoDispose.<ResponseBody>autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(new Consumer<ResponseBody>() {
                               @Override
                               public void accept(ResponseBody responseBody) throws
                                       Exception {
                                   String responseString = responseBody.string();
                                   LogManager.i(TAG, "responseString*****" + responseString);
                                   onCommonSingleParamCallback.onSuccess(responseString);

//                                   if (!isEmpty(responseString)) {
//                                       BaseResponse baseResponse;
//                                       try {
//                                           baseResponse = JSON.parseObject(responseString, BaseResponse.class);
//                                       } catch (Exception e) {
//                                           //??????????????????json?????????????????????????????????
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
                                // ????????????
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.request_was_aborted));
                            }
                        }
                );
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param fragment
     * @param observable
     * @param onCommonSingleParamCallback
     * @return
     */
    public void responseStringAutoDispose(Fragment fragment, Observable<ResponseBody> observable, OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        observable.onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //AutoDispose????????????????????????RxJava2???????????????????????????
                .as(AutoDispose.<ResponseBody>autoDisposable(AndroidLifecycleScopeProvider.from(fragment)))
                .subscribe(new Consumer<ResponseBody>() {
                               @Override
                               public void accept(ResponseBody responseBody) throws
                                       Exception {
                                   String responseString = responseBody.string();
                                   LogManager.i(TAG, "responseString*****" + responseString);
                                   onCommonSingleParamCallback.onSuccess(responseString);
//                                   if (!isEmpty(responseString)) {
//                                       BaseResponse baseResponse;
//                                       try {
//                                           baseResponse = JSON.parseObject(responseString, BaseResponse.class);
//                                       } catch (Exception e) {
//                                           //??????????????????json?????????????????????????????????
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
                                // ????????????
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.request_was_aborted));
                            }
                        }
                );
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param rxAppCompatActivity
     * @param observable
     * @param onCommonSingleParamCallback
     * @return
     */
    public void responseStringRxAppActivityBindToLifecycle(RxAppCompatActivity rxAppCompatActivity, Observable<ResponseBody> observable, OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        observable.onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //??????RxJava2???????????????????????????
                .compose(rxAppCompatActivity.<ResponseBody>bindToLifecycle())
                .subscribe(new Consumer<ResponseBody>() {
                               @Override
                               public void accept(ResponseBody responseBody) throws
                                       Exception {
                                   String responseString = responseBody.string();
                                   LogManager.i(TAG, "responseString*****" + responseString);
                                   onCommonSingleParamCallback.onSuccess(responseString);
//                                   if (!isEmpty(responseString)) {
//                                       BaseResponse baseResponse;
//                                       try {
//                                           baseResponse = JSON.parseObject(responseString, BaseResponse.class);
//                                       } catch (Exception e) {
//                                           //??????????????????json?????????????????????????????????
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
                                // ????????????
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.request_was_aborted));
                            }
                        }
                );
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param rxAppCompatActivity
     * @param observable
     * @param onCommonSingleParamCallback
     * @return
     */
    public void responseStringRxAppActivityBindUntilEvent(RxAppCompatActivity rxAppCompatActivity, Observable<ResponseBody> observable, OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        observable.onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //??????RxJava2???????????????????????????
                .compose(rxAppCompatActivity.<ResponseBody>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<ResponseBody>() {
                               @Override
                               public void accept(ResponseBody responseBody) throws
                                       Exception {
                                   String responseString = responseBody.string();
                                   LogManager.i(TAG, "responseString*****" + responseString);
                                   onCommonSingleParamCallback.onSuccess(responseString);
//                                   if (!isEmpty(responseString)) {
//                                       BaseResponse baseResponse;
//                                       try {
//                                           baseResponse = JSON.parseObject(responseString, BaseResponse.class);
//                                       } catch (Exception e) {
//                                           //??????????????????json?????????????????????????????????
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
                                // ????????????
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.request_was_aborted));
                            }
                        }
                );
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param rxFragment
     * @param observable
     * @param onCommonSingleParamCallback
     * @return
     */
    public void responseStringRxFragmentBindToLifecycle(RxFragment rxFragment, Observable<ResponseBody> observable, OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        observable.onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //??????RxJava2???????????????????????????
                .compose(rxFragment.<ResponseBody>bindToLifecycle())
                .subscribe(new Consumer<ResponseBody>() {
                               @Override
                               public void accept(ResponseBody responseBody) throws
                                       Exception {
                                   String responseString = responseBody.string();
                                   LogManager.i(TAG, "responseString*****" + responseString);
                                   onCommonSingleParamCallback.onSuccess(responseString);
//                                   if (!isEmpty(responseString)) {
//                                       BaseResponse baseResponse;
//                                       try {
//                                           baseResponse = JSON.parseObject(responseString, BaseResponse.class);
//                                       } catch (Exception e) {
//                                           //??????????????????json?????????????????????????????????
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
                                // ????????????
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.request_was_aborted));
                            }
                        }
                );
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param rxFragment
     * @param observable
     * @param onCommonSingleParamCallback
     * @return
     */
    public void responseStringRxFragmentBindUntilEvent(RxFragment rxFragment, Observable<ResponseBody> observable, OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        observable.onTerminateDetach()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //??????RxJava2???????????????????????????
                .compose(rxFragment.<ResponseBody>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<ResponseBody>() {
                               @Override
                               public void accept(ResponseBody responseBody) throws
                                       Exception {
                                   String responseString = responseBody.string();
                                   LogManager.i(TAG, "responseString*****" + responseString);
                                   onCommonSingleParamCallback.onSuccess(responseString);
//                                   if (!isEmpty(responseString)) {
//                                       BaseResponse baseResponse;
//                                       try {
//                                           baseResponse = JSON.parseObject(responseString, BaseResponse.class);
//                                       } catch (Exception e) {
//                                           //??????????????????json?????????????????????????????????
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
                                // ????????????
                                onCommonSingleParamCallback.onError(BaseApplication.getInstance().getResources().getString(R.string.request_was_aborted));
                            }
                        }
                );
    }

    public static String buildSign(String secret, long time) {
        //        Map treeMap = new TreeMap(params);// treeMap????????????key???????????????
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
            bytes = md5.digest(sb.toString().getBytes("UTF-8"));// md5??????
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // ???MD5??????????????????????????????????????????????????????
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
     * ????????????????????????
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            //???????????????????????????????????????
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

    protected boolean isEmpty(String dataStr) {
        if (!TextUtils.isEmpty(dataStr)) {
            return false;
        } else {
            return true;
        }
    }

}
