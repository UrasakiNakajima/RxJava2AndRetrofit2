package com.phone.android_and_js

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.android_and_js.databinding.JsActivityAndroidAndJsBinding
import com.phone.library_common.base.BaseBindingRxAppActivity
import com.phone.library_common.common.ConstantData
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.ResourcesManager

@Route(path = ConstantData.Route.ROUTE_ANDROID_AND_JS)
class AndroidAndJsActivity : BaseBindingRxAppActivity<JsActivityAndroidAndJsBinding>() {

    companion object {
        private val TAG = AndroidAndJsActivity::class.java.simpleName

        class JsInteration {
            @JavascriptInterface
            fun back(num: Int): String {
                return "Js to android hello world******$num"
            }
        }
    }

    private var webSettings: WebSettings? = null

    override fun initLayoutId(): Int = R.layout.js_activity_android_and_js

    override fun initData() {
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initViews() {
        mDatabind.apply {
            imvBack.setColorFilter(ResourcesManager.getColor(R.color.white))
            setToolbar(false, R.color.color_FF198CFF)

            layoutBack.setOnClickListener {
                finish()
            }
            tevAndroidToJs.setOnClickListener {
                val num = 1
                val num2 = 2
                //android调用js，有参数有返回值
                webView.evaluateJavascript(
                    "sum($num,$num2)",
                    ValueCallback<String> { value ->
                        LogManager.i(
                            TAG,
                            "js返回的结果是=$value"
                        )
                        showToast("js返回的结果是$value", true)
                    })
            }
            tevAndroidToJs2.setOnClickListener {
                //当出入变量名时，需要用转义符隔开
                val content = "9880"
                //android调用js，有参数无返回值
                webView.loadUrl("javascript:alertMessage($content)")
            }
            tevAndroidToJs3.setOnClickListener {
                //android调用js，无参数无返回值
                webView.loadUrl("javascript:show()")
            }

            //加载assets文件夹下的js_java_interaction.html页面
            webView.apply {
                loadUrl("file:///android_asset/js_java_interaction.html")
                webSettings = getSettings()
                webSettings?.javaScriptEnabled = true
                setWebViewClient(WebViewClient())
                //1.直接把js弹框弹出来（第一种方法）
                //1.直接把js弹框弹出来（第一种方法）
                setWebChromeClient(WebChromeClient())

                //		/**
                //		 * 2.js弹框自定义之后弹出来（第二种方法）
                //		 *
                //		 * 设置响应js 的alert()函数
                //		 * 设置需要支持js对话框，webview只是载体，内容的渲染需要使用webviewChromClient类去实现
                //		 * 通过设置WebChromeClient对象处理JavaScript的对话框（也就是把js弹框自定义之后弹出来）
                //		 */
                //		setWebChromeClient(new WebChromeClient() {
                //			@Override
                //			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //				AlertDialog.Builder builder = new AlertDialog.Builder(rxAppCompatActivity)
                //				builder.setTitle("Alert")
                //				builder.setMessage(message)
                //				builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                //					@Override
                //					public void onClick(DialogInterface dialog, int which) {
                //						result.confirm()
                //					}
                //				})
                //				builder.setCancelable(false)
                //				builder
                //					.create()
                //					.show()
                //				return true
                //			}
                //
                //		})
                //js调用android

                //		/**
                //		 * 2.js弹框自定义之后弹出来（第二种方法）
                //		 *
                //		 * 设置响应js 的alert()函数
                //		 * 设置需要支持js对话框，webview只是载体，内容的渲染需要使用webviewChromClient类去实现
                //		 * 通过设置WebChromeClient对象处理JavaScript的对话框（也就是把js弹框自定义之后弹出来）
                //		 */
                //		setWebChromeClient(new WebChromeClient() {
                //			@Override
                //			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //				AlertDialog.Builder builder = new AlertDialog.Builder(rxAppCompatActivity)
                //				builder.setTitle("Alert")
                //				builder.setMessage(message)
                //				builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                //					@Override
                //					public void onClick(DialogInterface dialog, int which) {
                //						result.confirm()
                //					}
                //				})
                //				builder.setCancelable(false)
                //				builder
                //					.create()
                //					.show()
                //				return true
                //			}
                //
                //		})
                //js调用android
                addJavascriptInterface(
                    JsInteration(),
                    "android"
                )
            }
        }
    }

    override fun initLoadData() {
    }

    override fun showLoading() {
        if (!loadView.isShown()) {
            loadView.setVisibility(View.VISIBLE)
            loadView.start()
        }
    }

    override fun hideLoading() {
        if (loadView.isShown()) {
            loadView.stop()
            loadView.setVisibility(View.GONE)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onResume() {
        super.onResume()
        // webview 优化 据说可以省电
        webSettings?.setJavaScriptEnabled(true)
    }

    override fun onStop() {
        super.onStop()
        // webview 优化 据说可以省电
        webSettings?.setJavaScriptEnabled(false)
    }

    override fun onDestroy() {
        mDatabind.webView.apply {
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webSettings?.javaScriptEnabled = false
            loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            clearHistory()
            (getParent() as ViewGroup).removeView(this)
            destroy()
        }
        super.onDestroy()
    }
}