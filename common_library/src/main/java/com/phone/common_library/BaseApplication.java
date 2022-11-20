package com.phone.common_library;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.phone.common_library.manager.ActivityPageManager;
import com.phone.common_library.manager.CrashHandlerManager;
import com.phone.common_library.manager.LogManager;
import com.phone.common_library.manager.RetrofitManager;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce :
 */
public class BaseApplication extends MultiDexApplication {

    private static final String TAG = BaseApplication.class.getSimpleName();
    //声明 初始化
    protected SharedPreferences sp;
    protected SharedPreferences.Editor editor;
    protected static final int MODE = Context.MODE_PRIVATE;
    private boolean isLogin;
    private String accessToken;
    private String systemId;

    protected static BaseApplication baseApplication;
    private ActivityPageManager activityPageManager;

    public WebView webView;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;

        //文件为mySp  存放在/data/data/<packagename>/shared_prefs/目录下的
        sp = getSharedPreferences("app", MODE);
        editor = sp.edit();

        //初始化retrofit
        RetrofitManager.getInstance();
        activityPageManager = ActivityPageManager.getInstance();

        CrashHandlerManager crashHandlerManager = CrashHandlerManager.getInstance(this);
        crashHandlerManager.sendPreviousReportsToServer();

        if (true) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);

        //		RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
        //			@Override
        //			public void accept(Throwable throwable) {
        //				//异常处理
        //				LogManager.i(TAG, "throwable*****" + throwable.toString());
        //				LogManager.i(TAG, "throwable message*****" + throwable.getMessage());
        //			}
        //		});


        webView = new WebView(this);
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        // android 5.0以上默认不支持Mixed Content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(
                    WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
//        //其他细节操作
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
//        webSettings.setAllowFileAccess(true); //设置可以访问文件
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式


//        //优先使用缓存:
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        //不使用缓存:
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        //步骤2. 选择加载方式
        //方式1. 加载一个网页：
        webView.loadUrl("http://www.google.com/");

//        //方式2：加载apk包中的html页面
//        webView.loadUrl("file:///android_asset/test.html");
//        //方式3：加载手机本地的html页面
//        webView.loadUrl("content://com.android.htmlfileprovider/sdcard/test.html");

        //步骤3. 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                view.loadUrl("file:///android_assets/error_handle.html");
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress < 100) {
//                    String progress = newProgress + "%";
//                    progress.setText(progress);
                } else {

                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
//                titleview.setText(title);
            }
        });

//        if (webView != null) {
//            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
//            webView.clearHistory();
//
//            ((ViewGroup) webView.getParent()).removeView(webView);
//            webView.destroy();
//            webView = null;
//        }

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static BaseApplication getInstance() {
        return baseApplication;
    }

    public boolean isLogin() {
        isLogin = sp.getBoolean("isLogin", false);
        LogManager.i(TAG, "isLogin***" + isLogin);
        return isLogin;
    }

    public void setLogin(boolean isLogin) {
        LogManager.i(TAG, "setLogin***" + isLogin);
        editor.putBoolean("isLogin", isLogin);
        editor.commit();
        if (!isLogin) {
            setLogout();
        }
    }

    public String getAccessToken() {
        accessToken = sp.getString("accessToken", "");
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        LogManager.i(TAG, "setAccessToken***" + accessToken);
        editor.putString("accessToken", accessToken);
        editor.commit();
    }

    public String getSystemId() {
        systemId = sp.getString("systemId", "");
        return systemId;
    }

    public void setSystemId(String systemId) {
        LogManager.i(TAG, "setSystemId***" + systemId);
        editor.putString("systemId", systemId);
        editor.commit();
    }

    public void setLogout() {
        LogManager.i(TAG, "setLogout***");
        //        editor.clear();
        editor.remove("accessToken");
        editor.remove("isLogin");
        editor.commit();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_MODERATE) {
            //App开始自杀，清理掉所有的activity（最後一個存活的Activity退出的时候（onDestroy）做了退出應用程序處理）
            activityPageManager.exitApp2();
        }
    }
}
