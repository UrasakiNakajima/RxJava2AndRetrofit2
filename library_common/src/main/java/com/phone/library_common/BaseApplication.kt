package com.phone.library_common

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Build
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.phone.library_common.callback.OnCommonSingleParamCallback
import com.phone.library_common.manager.ActivityPageManager
import com.phone.library_common.manager.CrashHandlerManager
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.RetrofitManager

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce :
 */
open class BaseApplication : MultiDexApplication() {

    companion object {
        private val TAG = BaseApplication::class.java.simpleName
        private var instance: BaseApplication? = null

        @JvmStatic
        fun get(): BaseApplication {
            return instance!!
        }
    }

    //声明 初始化
    protected var sp: SharedPreferences? = null
    protected var editor: SharedPreferences.Editor? = null
    protected val MODE = Context.MODE_PRIVATE
    private var isLogin = false
    private var accessToken: String? = null
    private var systemId: String? = null

    private var activityPageManager: ActivityPageManager? = null

    var webView: WebView? = null

    override fun onCreate() {
        super.onCreate()
        instance = this

        //文件为mySp  存放在/data/data/<packagename>/shared_prefs/目录下的
        sp = getSharedPreferences("app", MODE)
        editor = sp?.edit()

        //初始化retrofit
        RetrofitManager.get()
        activityPageManager = ActivityPageManager.get()
        val crashHandlerManager = CrashHandlerManager.get()
        crashHandlerManager?.sendPreviousReportsToServer()
        if (true) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)

        //		RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
        //			@Override
        //			public void accept(Throwable throwable) {
        //				//异常处理
        //				LogManager.i(TAG, "throwable*****" + throwable.toString())
        //				LogManager.i(TAG, "throwable message*****" + throwable.getMessage())
        //			}
        //		})
        initWebView()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initWebView() {
        webView = WebView(this)
        //声明WebSettings子类
        val webSettings = webView?.settings
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings?.javaScriptEnabled = true
        // android 5.0以上默认不支持Mixed Content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings?.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        }
        //设置自适应屏幕，两者合用
        webSettings?.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings?.loadWithOverviewMode = true // 缩放至屏幕的大小
        //缩放操作
        webSettings?.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        webSettings?.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings?.displayZoomControls = false //隐藏原生的缩放控件
        //        //其他细节操作
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK) //关闭webview中缓存
//        webSettings.setAllowFileAccess(true) //设置可以访问文件
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true) //支持通过JS打开新窗口
        webSettings?.loadsImagesAutomatically = true //支持自动加载图片
        webSettings?.defaultTextEncodingName = "utf-8" //设置编码格式

        //优先使用缓存:
        webSettings?.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。

//        //不使用缓存:
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE)

        //步骤2. 选择加载方式
        //方式1. 加载一个网页：
//        webView.loadUrl("http://www.baidu.com/")

//        //方式2：加载apk包中的html页面
//        webView.loadUrl("file:///android_asset/test.html")
//        //方式3：加载手机本地的html页面
//        webView.loadUrl("content://com.android.htmlfileprovider/sdcard/test.html")

        //步骤3. 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        webView?.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    view?.loadUrl(url)
                }
                return true
            }

            @Deprecated("Deprecated in Java")
            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)

                //                view.loadUrl("file:///android_assets/error_handle.html")
            }
        }
        webView?.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                onCommonSingleParamCallback?.onSuccess(newProgress)
            }

            override fun onReceivedTitle(view: WebView, title: String) {
                //                titleview.setText(title)
            }
        }

//        if (webView != null) {
//            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
//            webView.clearHistory()
//
//            ((ViewGroup) webView.getParent()).removeView(webView)
//            webView.destroy()
//            webView = null
//        }
    }

    private var onCommonSingleParamCallback: OnCommonSingleParamCallback<Int>? = null

    fun setOnCommonSingleParamCallback(onCommonSingleParamCallback: OnCommonSingleParamCallback<Int>?) {
        this.onCommonSingleParamCallback = onCommonSingleParamCallback
    }

    fun isLogin(): Boolean {
        isLogin = sp?.getBoolean("isLogin", false) == true
        LogManager.i(TAG, "isLogin***$isLogin")
        return isLogin
    }

    fun setLogin(isLogin: Boolean) {
        LogManager.i(TAG, "setLogin***$isLogin")
        editor?.putBoolean("isLogin", isLogin)
        editor?.commit()
        if (!isLogin) {
            setLogout()
        }
    }

    fun getAccessToken(): String? {
        accessToken = sp?.getString("accessToken", "")
        return accessToken
    }

    fun setAccessToken(accessToken: String) {
        LogManager.i(TAG, "setAccessToken***$accessToken")
        editor?.putString("accessToken", accessToken)
        editor?.commit()
    }

    fun getSystemId(): String? {
        systemId = sp?.getString("systemId", "")
        return systemId
    }

    fun setSystemId(systemId: String) {
        LogManager.i(TAG, "setSystemId***$systemId")
        editor?.putString("systemId", systemId)
        editor?.commit()
    }

    fun setLogout() {
        LogManager.i(TAG, "setLogout***")
        //        editor.clear()
        editor?.remove("accessToken")
        editor?.remove("isLogin")
        editor?.commit()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == TRIM_MEMORY_MODERATE) {
            //App开始自杀，清理掉所有的activity（最後一個存活的Activity退出的时候（onDestroy）做了退出應用程序處理）
            activityPageManager?.exitApp2()
        }
    }
}