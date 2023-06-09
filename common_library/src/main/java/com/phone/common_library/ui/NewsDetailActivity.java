package com.phone.common_library.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.phone.common_library.R;
import com.phone.common_library.base.BaseRxAppActivity;
import com.phone.common_library.manager.LogManager;

public class NewsDetailActivity extends BaseRxAppActivity {

    private static final String TAG = "NewsDetailActivity";
    private Toolbar toolbar;
    private FrameLayout layoutBack;
    private ImageView imvBack;
    private TextView tevTitle;
    private ProgressBar activityWebProgressbar;
    private WebView llWebContent;

    private WebSettings webSettings;

    private String detailUrl;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (null == intent) {
            return;
        }
        detailUrl = intent.getStringExtra("detailUrl");
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        layoutBack = (FrameLayout) findViewById(R.id.layout_back);
        imvBack = (ImageView) findViewById(R.id.imv_back);
        tevTitle = (TextView) findViewById(R.id.tev_title);
        activityWebProgressbar = (ProgressBar) findViewById(R.id.activity_web_progressbar);
        llWebContent = (WebView) findViewById(R.id.ll_web_content);

        setToolbar(true, R.color.color_FFE066FF);
        imvBack.setColorFilter(ContextCompat.getColor(rxAppCompatActivity, R.color.white));
        layoutBack.setOnClickListener(view -> {
            finish();
        });

        webSettings = llWebContent.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        llWebContent.clearFormData();
        //设置缓存模式（只加载无缓存）
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//有五种缓存模式
//        //设置数据库缓存路径
//        webSettings.setDatabasePath(cacheDirPath);
//        //设置应用缓存目录
//        webSettings.setAppCachePath(cacheDirPath);
//        //DOM存储功能
//        webSettings.setDomStorageEnabled(true);
//        //数据库存储功能
//        webSettings.setDatabaseEnabled(true);
//        //应用缓存
//        webSettings.setAppCacheEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(
                    WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        setWebSetting();
        llWebContent.loadUrl(detailUrl);
    }

    @Override
    protected void initLoadData() {
//		startAsyncTask();
    }

    private void startAsyncTask() {

        // This async task is an anonymous class and therefore has a hidden reference to the outer
        // class MainActivity. If the activity gets destroyed before the task finishes (e.g. rotation),
        // the activity instance will leak.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // Do some slow work in background
                SystemClock.sleep(10000);
                return null;
            }
        }.execute();

        Toast.makeText(this, "请关闭这个A完成泄露", Toast.LENGTH_SHORT).show();
    }

    private void setWebSetting() {
        llWebContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.e("wangfeng", "onReceivedError=errorCode:" + errorCode + "," + description);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        llWebContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 50) {
                    activityWebProgressbar.setVisibility(View.GONE);
                } else {
                    if (View.GONE == activityWebProgressbar.getVisibility()) {
                        activityWebProgressbar.setVisibility(View.VISIBLE);
                    }
                    activityWebProgressbar.setProgress(newProgress);
                }
                LogManager.i(TAG, "网页进度=" + newProgress);
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                LogManager.i(TAG, "网页TITLE=" + title);
                //                titleBar.setTitle(title);
            }

        });
    }

    private void destroyWebView(WebView mWebView) {
        if (mWebView != null) {
            //1.清除数据库缓存
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
            //2.清除历史
            mWebView.clearHistory();
            //3.清空Cookie
            CookieManager cookieManager = CookieManager.getInstance();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.removeSessionCookies(null);
                cookieManager.removeAllCookie();
                cookieManager.flush();
            } else {
                cookieManager.removeSessionCookies(null);
                cookieManager.removeAllCookie();
                CookieSyncManager.getInstance().sync();
            }
            //4.清空Localstorage
            WebStorage.getInstance().deleteAllData(); //清空WebView的localStorage
            mWebView = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing
        }
    }

    @Override
    protected void onDestroy() {
        destroyWebView(llWebContent);
        super.onDestroy();
    }
}