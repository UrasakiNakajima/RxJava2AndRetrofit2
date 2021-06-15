package com.phone.android_and_js;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class MainActivity extends BaseAppActivity {
	
	private static final String      TAG = "MainActivity";
	private              Toolbar     toolbar;
	private              FrameLayout layoutBack;
	private              ImageView   imvBack;
	private              TextView    tevTitle;
	private              WebView     webView;
	private              TextView    tevAndroidToJs;
	private              TextView    tevAndroidToJs2;
	
	@Override
	protected int initLayoutId() {
		return R.layout.activity_main;
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
		
		imvBack.setColorFilter(ContextCompat.getColor(appCompatActivity, R.color.white));
		setToolbar(false, R.color.color_FFE066FF);
		tevAndroidToJs.setOnClickListener(view -> {
			
			//Android调用Js,有返回值
			webView.evaluateJavascript("sum(1,2)", new ValueCallback<String>() {
				@Override
				public void onReceiveValue(String value) {
					LogManager.i(TAG, "onReceiveValue value=" + value);
					showToast(value, true);
				}
			});
		});
		tevAndroidToJs2.setOnClickListener(view -> {
			//Android调用Js,无返回值
			webView.loadUrl("JavaScript:show()");
		});
		
		//加载assets文件夹下的test.html页面
		webView.loadUrl("file:///android_asset/test.html");
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new JsInteration(), "android");
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.equals("file:///android_asset/test2.html")) {
					LogManager.i(TAG, "shouldOverrideUrlLoading: " + url);
					//					startActivity(new Intent(appCompatActivity, Main2Activity.class));
					return true;
				} else {
					webView.loadUrl(url);
					return false;
				}
			}
		});
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