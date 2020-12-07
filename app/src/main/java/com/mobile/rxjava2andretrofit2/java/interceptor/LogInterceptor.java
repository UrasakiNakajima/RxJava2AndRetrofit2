package com.mobile.rxjava2andretrofit2.java.interceptor;

import com.mobile.rxjava2andretrofit2.java.manager.LogManager;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LogInterceptor implements Interceptor {

    public static String TAG = "LogInterceptor";

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        Response response = chain.proceed(chain.request());
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        LogManager.i(TAG, "\n");
        LogManager.i(TAG, "----------Start----------------");
        LogManager.i(TAG, "| " + request.toString());
        String method = request.method();
        if ("POST".equals(method)) {
            StringBuilder stringBuilder = new StringBuilder();
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                for (int i = 0; i < body.size(); i++) {
                    stringBuilder.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                }
                stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
                LogManager.i(TAG, "| RequestParams:{" + stringBuilder.toString() + "}");
            }
        }
        LogManager.i(TAG, "| BaseResponse:" + content);
        LogManager.i(TAG, "----------End:" + duration + "毫秒----------");
        return response.newBuilder()
                .body(ResponseBody.create(mediaType, content))
                .build();
    }
}
