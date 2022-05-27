package com.phone.common_library.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.phone.common_library.BaseApplication;
import com.phone.common_library.R;
import com.phone.common_library.manager.ActivityPageManager;
import com.phone.common_library.manager.ToolbarManager;

import java.util.Map;

import butterknife.ButterKnife;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/7 10:11
 * introduce :
 */

public abstract class BaseAppActivity extends AppCompatActivity {

	protected BaseApplication          baseApplication;
//	protected QMUILoadingView          loadView;
//	protected FrameLayout.LayoutParams layoutParams;
	protected Intent                   intent;
	protected Bundle                   bundle;
	protected AppCompatActivity        appCompatActivity;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		appCompatActivity = this;
		baseApplication = (BaseApplication) getApplication();
		ActivityPageManager.getInstance().addActivity(appCompatActivity);

		setContentView(initLayoutId());
		ButterKnife.bind(appCompatActivity);

//		loadView = new QMUILoadingView(appCompatActivity);
//		loadView.setVisibility(View.GONE);
//		loadView.setSize(100);
//		loadView.setColor(getResources().getColor(R.color.color_80000000));
//		layoutParams = new FrameLayout.LayoutParams(
//			FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//		layoutParams.gravity = Gravity.CENTER;

		//        setToolbar();
		initData();
		initViews();
		initLoadData();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		//非默认值
		if (newConfig.fontScale != 1) {
			getResources();
		}
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public Resources getResources() {//还原字体大小
		Resources res = super.getResources();
		//非默认值
		if (res.getConfiguration().fontScale != 1) {
			Configuration newConfig = new Configuration();
			newConfig.setToDefaults();//设置默认
			res.updateConfiguration(newConfig, res.getDisplayMetrics());
		}
		return res;
	}

	protected abstract int initLayoutId();

	protected void setToolbar(boolean isDarkFont) {
		if (isDarkFont) {
			ImmersionBar.with(appCompatActivity) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
				.statusBarDarkFont(isDarkFont)
				.statusBarColor(R.color.color_FFFFFFFF)     //状态栏颜色，不写默认透明色
				//                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
				.keyboardEnable(true)
				.init();
		} else {
			ImmersionBar.with(appCompatActivity)
				.statusBarDarkFont(isDarkFont)
				.statusBarColor(R.color.color_FF198CFF)     //状态栏颜色，不写默认透明色
				//                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
				.keyboardEnable(true)
				.init();
		}

		ToolbarManager.assistActivity(findViewById(android.R.id.content));
	}

	protected void setToolbar(boolean isDarkFont, boolean isResizeChildOfContent) {
		if (isDarkFont) {
			ImmersionBar.with(appCompatActivity) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
				.statusBarDarkFont(isDarkFont)
				.statusBarColor(R.color.color_FFFFFFFF)     //状态栏颜色，不写默认透明色
				//                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
				.keyboardEnable(true)
				.init();
		} else {
			ImmersionBar.with(appCompatActivity)
				.statusBarDarkFont(isDarkFont)
				.statusBarColor(R.color.color_FF198CFF)     //状态栏颜色，不写默认透明色
				//                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
				.keyboardEnable(true)
				.init();
		}

		if (isResizeChildOfContent) {
			ToolbarManager.assistActivity(findViewById(android.R.id.content));
		}
	}

	protected void setToolbar(boolean isDarkFont, int color) {
		if (isDarkFont) {
			ImmersionBar.with(appCompatActivity) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
				.statusBarDarkFont(isDarkFont)
				.statusBarColor(color)     //状态栏颜色，不写默认透明色
				//                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
				.init();
		} else {
			ImmersionBar.with(appCompatActivity)
				.statusBarDarkFont(isDarkFont)
				.statusBarColor(color)     //状态栏颜色，不写默认透明色
				//                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
				.init();
		}

		ToolbarManager.assistActivity(findViewById(android.R.id.content));
	}

	protected void setToolbar(boolean isDarkFont, int color, boolean isResizeChildOfContent) {
		if (isDarkFont) {
			ImmersionBar.with(appCompatActivity) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
				.statusBarDarkFont(isDarkFont)
				.statusBarColor(color)     //状态栏颜色，不写默认透明色
				//                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
				.init();
		} else {
			ImmersionBar.with(appCompatActivity)
				.statusBarDarkFont(isDarkFont)
				.statusBarColor(color)     //状态栏颜色，不写默认透明色
				//                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
				.init();
		}

		if (isResizeChildOfContent) {
			ToolbarManager.assistActivity(findViewById(android.R.id.content));
		}
	}

	/**
	 * 初始化沉浸式
	 * Init immersion bar.
	 */
	protected void initImmersionBar() {
		//设置共同沉浸式样式
		ImmersionBar.with(appCompatActivity).navigationBarColor(R.color.color_FFE066FF).init();
	}

	protected abstract void initData();

	protected abstract void initViews();

	protected abstract void initLoadData();

	protected void showToast(String message, boolean isLongToast) {
		//        Toast.makeText(appCompatActivity, message, Toast.LENGTH_LONG).show();
		if (!appCompatActivity.isFinishing()) {
			Toast toast;
			int duration;
			if (isLongToast) {
				duration = Toast.LENGTH_LONG;
			} else {
				duration = Toast.LENGTH_SHORT;
			}
			toast = Toast.makeText(appCompatActivity, message, duration);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	protected void showCustomToast(int left, int right,
		int textSize, int textColor,
		int bgColor, int height,
		int roundRadius, String message) {
		FrameLayout frameLayout = new FrameLayout(appCompatActivity);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		frameLayout.setLayoutParams(layoutParams);
		TextView textView = new TextView(appCompatActivity);
		FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, height);
		textView.setLayoutParams(layoutParams1);
		textView.setPadding(left, 0, right, 0);
		textView.setTextSize(textSize);
		textView.setTextColor(textColor);
		textView.setGravity(Gravity.CENTER);
		textView.setIncludeFontPadding(false);
		GradientDrawable gradientDrawable = new GradientDrawable();//创建drawable
		gradientDrawable.setColor(bgColor);
		gradientDrawable.setCornerRadius(roundRadius);
		textView.setBackground(gradientDrawable);
		textView.setText(message);
		frameLayout.addView(textView);

		Toast toast = new Toast(appCompatActivity);
		toast.setView(frameLayout);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.show();
	}

	public boolean isOnMainThread() {
		return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
	}

	protected void startActivity(Class<?> cls) {
		intent = new Intent(appCompatActivity, cls);
		startActivity(intent);
	}

	protected void startActivityCarryParams(Class<?> cls, Map<String, String> params) {
		intent = new Intent(appCompatActivity, cls);
		bundle = new Bundle();

		if (params != null && params.size() > 0) {
			for (String key : params.keySet()) {
				if (params.get(key) != null) {//如果参数不是null，才把参数传给后台
					bundle.putString(key, params.get(key));
				}
			}
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	protected void startActivityForResult(Class<?> cls, int requestCode) {
		intent = new Intent(appCompatActivity, cls);
		startActivityForResult(intent, requestCode);
	}

	protected void startActivityForResultCarryParams(Class<?> cls, Map<String, String> params, int requestCode) {
		intent = new Intent(appCompatActivity, cls);
		bundle = new Bundle();

		if (params != null && params.size() > 0) {
			for (String key : params.keySet()) {
				if (params.get(key) != null) {//如果参数不是null，才把参数传给后台
					bundle.putString(key, params.get(key));
				}
			}
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, requestCode);
	}

	protected boolean isEmpty(String dataStr) {
		if (dataStr != null && !"".equals(dataStr)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected void onDestroy() {
		ActivityPageManager.getInstance().removeActivity(appCompatActivity);
		super.onDestroy();
	}
}
