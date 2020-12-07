package com.mobile.rxjava2andretrofit2.java.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;
import com.mobile.rxjava2andretrofit2.java.MineApplication;
import com.mobile.rxjava2andretrofit2.R;
import com.mobile.rxjava2andretrofit2.java.manager.ActivityPageManager;
import com.mobile.rxjava2andretrofit2.java.manager.ToolbarManager;
import com.qmuiteam.qmui.widget.QMUILoadingView;

import java.util.Map;

import butterknife.ButterKnife;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/7 10:11
 * introduce :
 */

public abstract class BaseAppActivity extends AppCompatActivity {

    protected MineApplication mineApplication;
    public QMUILoadingView loadView;
    protected FrameLayout.LayoutParams layoutParams;
    protected Intent intent;
    protected Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mineApplication = (MineApplication) getApplication();
        ActivityPageManager.getInstance().addActivity(this);

        loadView = new QMUILoadingView(this);
        loadView.setVisibility(View.GONE);
        loadView.setSize(100);
        loadView.setColor(getResources().getColor(R.color.color_80000000));
        layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;

        setContentView(initLayoutId());
        ButterKnife.bind(this);
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
            ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                    .statusBarDarkFont(isDarkFont)
                    .statusBarColor(R.color.color_FFFFFFFF)     //状态栏颜色，不写默认透明色
//                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                    .keyboardEnable(true)
                    .init();
        } else {
            ImmersionBar.with(this)
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
            ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                    .statusBarDarkFont(isDarkFont)
                    .statusBarColor(R.color.color_FFFFFFFF)     //状态栏颜色，不写默认透明色
//                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                    .keyboardEnable(true)
                    .init();
        } else {
            ImmersionBar.with(this)
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
            ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                    .statusBarDarkFont(isDarkFont)
                    .statusBarColor(color)     //状态栏颜色，不写默认透明色
//                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                    .init();
        } else {
            ImmersionBar.with(this)
                    .statusBarDarkFont(isDarkFont)
                    .statusBarColor(color)     //状态栏颜色，不写默认透明色
//                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                    .init();
        }

        ToolbarManager.assistActivity(findViewById(android.R.id.content));
    }

    protected void setToolbar(boolean isDarkFont, int color, boolean isResizeChildOfContent) {
        if (isDarkFont) {
            ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                    .statusBarDarkFont(isDarkFont)
                    .statusBarColor(color)     //状态栏颜色，不写默认透明色
//                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                    .init();
        } else {
            ImmersionBar.with(this)
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
        ImmersionBar.with(this).navigationBarColor(R.color.color_FFE066FF).init();
    }

    protected abstract void initData();

    protected abstract void initViews();

    protected abstract void initLoadData();


    protected void showToast(String message, boolean isLongToast) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        if (!BaseAppActivity.this.isFinishing()) {
            Toast toast;
            int duration;
            if (isLongToast) {
                duration = Toast.LENGTH_LONG;
            } else {
                duration = Toast.LENGTH_SHORT;
            }
            toast = Toast.makeText(BaseAppActivity.this, message, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public boolean isOnMainThread() {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }

    protected void startActivity(Class<?> cls) {
        intent = new Intent(this, cls);
        startActivity(intent);
    }

    protected void startActivityCarryParams(Class<?> cls, Map<String, String> params) {
        intent = new Intent(this, cls);
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
        intent = new Intent(this, cls);
        startActivityForResult(intent, requestCode);
    }

    protected void startActivityForResultCarryParams(Class<?> cls, Map<String, String> params, int requestCode) {
        intent = new Intent(this, cls);
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
        ActivityPageManager.getInstance().removeActivity(this);
        super.onDestroy();
    }
}
