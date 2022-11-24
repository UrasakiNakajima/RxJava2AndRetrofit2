package com.phone.resource_module.fragment

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.*
import com.phone.common_library.base.BaseBindingRxFragment
import com.phone.common_library.manager.LogManager
import com.phone.resource_module.R
import com.phone.resource_module.databinding.FragmentAndroidAndJsBinding

class AndroidAndJsFragment : BaseBindingRxFragment<FragmentAndroidAndJsBinding>() {

    val TAG = AndroidAndJsFragment::class.java.simpleName
    var webSettings: WebSettings? = null

    override fun initLayoutId(): Int {
        return R.layout.fragment_android_and_js
    }

    override fun initData() {
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initViews() {
        mDatabind.apply {
            tevAndroidToJs.setOnClickListener { view ->
                val num = 1
                val num2 = 2
                //android调用js，有参数有返回值
                webView.evaluateJavascript(
                    "sum($num,$num2)",
                    ValueCallback<String> { value ->
                        LogManager.i(TAG, "js返回的结果是=$value")
                        showToast("js返回的结果是$value", true)
                    })
            }
            tevAndroidToJs2.setOnClickListener { view ->
                //当出入变量名时，需要用转义符隔开
                val content = "9880"
                //android调用js，有参数无返回值
                webView.loadUrl("javascript:alertMessage($content)")
            }
            tevAndroidToJs3.setOnClickListener { view ->
                //android调用js，无参数无返回值
                webView.loadUrl("javascript:show()")
            }

            webView.apply {
                //加载assets文件夹下的js_java_interaction.html页面
                loadUrl("file:///android_asset/js_java_interaction.html")
                webSettings = getSettings()
                webSettings?.javaScriptEnabled = true
                setWebViewClient(WebViewClient())
                //1.直接把js弹框弹出来（第一种方法）
                setWebChromeClient(WebChromeClient())

                //		/**
                //		 * 2.js弹框自定义之后弹出来（第二种方法）
                //		 *
                //		 * 设置响应js 的alert()函数
                //		 * 设置需要支持js对话框，webview只是载体，内容的渲染需要使用webviewChromClient类去实现
                //		 * 通过设置WebChromeClient对象处理JavaScript的对话框（也就是把js弹框自定义之后弹出来）
                //		 */
                //		webView.setWebChromeClient(new WebChromeClient() {
                //			@Override
                //			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //				AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity);
                //				builder.setTitle("Alert");
                //				builder.setMessage(message);
                //				builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                //					@Override
                //					public void onClick(DialogInterface dialog, int which) {
                //						result.confirm();
                //					}
                //				});
                //				builder.setCancelable(false);
                //				builder
                //					.create()
                //					.show();
                //				return true;
                //			}
                //
                //		});
                //js调用android
                addJavascriptInterface(JsInteration(), "android")
            }
        }
    }

    override fun initLoadData() {
    }

    class JsInteration {
        @JavascriptInterface
        fun back(num: Int): String {
            return "Js to android hello world******$num"
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onResume() {
        super.onResume()
        // webview 优化 据说可以省电
        webSettings?.javaScriptEnabled = true
    }

    override fun onStop() {
        super.onStop()
        // webview 优化 据说可以省电
        webSettings?.javaScriptEnabled = false
    }

    override fun onDestroy() {
        mDatabind.webView.apply {
            loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            clearHistory()
            (getParent() as ViewGroup).removeView(this)
            destroy()
        }
        super.onDestroy()
    }

}