package com.mobile.first_page_module.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.mobile.common_library.base.BaseAppActivity;
import com.mobile.common_library.manager.LogManager;
import com.mobile.first_page_module.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

@Route(path = "/first_page_module/ui/android_and_js")
public class AndroidAndJsActivity extends BaseAppActivity {
	
	private static final String      TAG = "AndroidAndJsActivity";
	private              Toolbar     toolbar;
	private              FrameLayout layoutBack;
	private              ImageView   imvBack;
	private              TextView    tevTitle;
	private              WebView     webView;
	private              TextView    tevAndroidToJs;
	private              TextView    tevAndroidToJs2;
	private              TextView    tevAndroidToJs3;
	
	@Override
	protected int initLayoutId() {
		return R.layout.activity_android_and_js;
	}
	
	@Override
	protected void initData() {
	
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void initViews() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		layoutBack = (FrameLayout) findViewById(R.id.layout_back);
		imvBack = (ImageView) findViewById(R.id.imv_back);
		tevTitle = (TextView) findViewById(R.id.tev_title);
		webView = (WebView) findViewById(R.id.web_view);
		tevAndroidToJs = (TextView) findViewById(R.id.tev_android_to_js);
		tevAndroidToJs2 = (TextView) findViewById(R.id.tev_android_to_js2);
		tevAndroidToJs3 = (TextView) findViewById(R.id.tev_android_to_js3);
		
		imvBack.setColorFilter(ContextCompat.getColor(appCompatActivity, R.color.white));
		setToolbar(false, R.color.color_FFE066FF);
		tevAndroidToJs.setOnClickListener(view -> {
			
			int num = 1;
			int num2 = 2;
			//android调用js，有参数有返回值
			webView.evaluateJavascript("sum(" + num + "," + num2 + ")", new ValueCallback<String>() {
				@Override
				public void onReceiveValue(String value) {
					LogManager.i(TAG, "js返回的结果是=" + value);
					showToast("js返回的结果是=" + value, true);
				}
			});
		});
		tevAndroidToJs2.setOnClickListener(view -> {
			//当出入变量名时，需要用转义符隔开
			String content = "9880";
			//android调用js，有参数无返回值
			webView.loadUrl("javascript:alertMessage(" + content + ")");
		});
		tevAndroidToJs3.setOnClickListener(view -> {
			//android调用js，无参数无返回值
			webView.loadUrl("javascript:show()");
		});
		
		//加载assets文件夹下的js_java_interaction.html页面
		webView.loadUrl("file:///android_asset/js_java_interaction.html");
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient());
		//		//1.直接把js弹框弹出来（第一种方法）
		//		webView.setWebChromeClient(new WebChromeClient());
		
		/**
		 * 2.js弹框自定义之后弹出来（第二种方法）
		 *
		 * 设置响应js 的alert()函数
		 * 设置需要支持js对话框，webview只是载体，内容的渲染需要使用webviewChromClient类去实现
		 * 通过设置WebChromeClient对象处理JavaScript的对话框（也就是把js弹框自定义之后弹出来）
		 */
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity);
				builder.setTitle("Alert");
				builder.setMessage(message);
				builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						result.confirm();
					}
				});
				builder.setCancelable(false);
				builder
					.create()
					.show();
				return true;
			}
			
		});
		//js调用android
		webView.addJavascriptInterface(new JsInteration(), "android");
	}
	
	@Override
	protected void initLoadData() {
	
	}
	
	public static class JsInteration {
		
		@JavascriptInterface
		public String back() {
			return "Js to android hello world";
		}
	}
	
}