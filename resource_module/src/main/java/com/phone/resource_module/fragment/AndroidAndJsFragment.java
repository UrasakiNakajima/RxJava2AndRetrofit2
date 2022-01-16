package com.phone.resource_module.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.phone.common_library.base.BaseFragment;
import com.phone.common_library.manager.LogManager;
import com.phone.resource_module.R;

import androidx.appcompat.widget.Toolbar;

/**
 * author : Urasaki
 * e-mail : 1164688204@qq.com
 * date   : 2021/6/1717:04
 * desc   :
 * version: 1.0
 */
public class AndroidAndJsFragment extends BaseFragment {
	
	private static final String      TAG = "AndroidAndJsActivity";
	private              Toolbar     toolbar;
	private              TextView    tevTitle;
	private              WebView     webView;
	private              TextView    tevAndroidToJs;
	private              TextView    tevAndroidToJs2;
	private              TextView    tevAndroidToJs3;
	private              WebSettings webSettings;
	
	public static AndroidAndJsFragment getInstance(String type) {
		AndroidAndJsFragment fragment = new AndroidAndJsFragment();
		Bundle bundle = new Bundle();
		bundle.putString("type", type);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	protected int initLayoutId() {
		return R.layout.fragment_android_and_js;
	}
	
	@Override
	protected void initData() {
	
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void initViews() {
		toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
		tevTitle = (TextView) rootView.findViewById(R.id.tev_title);
		webView = (WebView) rootView.findViewById(R.id.web_view);
		tevAndroidToJs = (TextView) rootView.findViewById(R.id.tev_android_to_js);
		tevAndroidToJs2 = (TextView) rootView.findViewById(R.id.tev_android_to_js2);
		tevAndroidToJs3 = (TextView) rootView.findViewById(R.id.tev_android_to_js3);
		
		tevAndroidToJs.setOnClickListener(view -> {
			
			int num = 1;
			int num2 = 2;
			//android调用js，有参数有返回值
			webView.evaluateJavascript("sum(" + num + "," + num2 + ")", new ValueCallback<String>() {
				@Override
				public void onReceiveValue(String value) {
					LogManager.i(TAG, "js返回的结果是=" + value);
					showToast("js返回的结果是" + value, true);
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
		webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient());
		//1.直接把js弹框弹出来（第一种方法）
		webView.setWebChromeClient(new WebChromeClient());
		
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
		webView.addJavascriptInterface(new JsInteration(), "android");
	}
	
	@Override
	protected void initLoadData() {
	
	}
	
	private static class JsInteration {

		@JavascriptInterface
		public String back(int num) {
			return "Js to android hello world******" + num;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (webSettings != null) {
			// webview 优化 据说可以省电
			webSettings.setJavaScriptEnabled(true);
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if (webSettings != null) {
			// webview 优化 据说可以省电
			webSettings.setJavaScriptEnabled(false);
		}
	}
	
	@Override
	public void onDestroyView() {

		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		if (webView != null) {
			// 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
			// destory()
			ViewParent parent = webView.getParent();
			if (parent != null) {
				((ViewGroup) parent).removeView(webView);
			}

			webView.stopLoading();
			// 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
			webView.getSettings().setJavaScriptEnabled(false);
			webView.clearHistory();
			webView.clearView();
			webView.removeAllViews();
			webView.destroy();
		}
		super.onDestroy();
	}
}
