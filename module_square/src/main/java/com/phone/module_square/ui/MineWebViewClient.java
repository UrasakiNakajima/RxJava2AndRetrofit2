package com.phone.module_square.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.net.http.SslError;
import android.os.Build;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;

public class MineWebViewClient extends BridgeWebViewClient {

    public MineWebViewClient(BridgeWebView webView) {
        super(webView);
    }

    @SuppressLint("WebViewClientOnReceivedSslError")
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        //super.onReceivedSslError(view, handler, error);
        // 如何处理应用中的 WebView SSL 错误处理程序提醒
        handler.proceed();
    }

    /**
     * 同名 API 兼容
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (request.isForMainFrame()) {
            onReceivedError(view,
                    error.getErrorCode(), error.getDescription().toString(),
                    request.getUrl().toString());
        }
    }

    /**
     * 加载错误
     */
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    /**
     * 同名 API 兼容
     */
    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return shouldOverrideUrlLoading(view, request.getUrl().toString());
    }

    /**
     * 跳转到其他链接
     */
//    @Override
//    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        Log.i("WebView shouldOverrideUrlLoading：%s", url);
//        String scheme = Uri.parse(url).getScheme();
//        if (scheme == null) {
//            return false;
//        }
//        switch (scheme) {
//            // 如果这是跳链接操作
//            case "http":
//            case "https":
//                view.loadUrl(url);
//                break;
//            // 如果这是打电话操作
//            case "tel":
//    dialing(view, url);
//                break;
//            default:
//                break;
//        }
//        return true;
//    }


}
