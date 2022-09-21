package com.phone.common_library.base;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.phone.common_library.BaseApplication;
import com.phone.common_library.R;
import com.phone.common_library.manager.ActivityPageManager;
import com.phone.common_library.manager.CrashHandlerManager;
import com.phone.common_library.manager.LogManager;
import com.phone.common_library.manager.ToolbarManager;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseMvpRxAppActivity<V, T extends BasePresenter<V>> extends RxAppCompatActivity {


    private static final String TAG = BaseMvpRxAppActivity.class.getSimpleName();
    public QMUILoadingView loadView;
    protected FrameLayout.LayoutParams layoutParams;

    protected T presenter;

    protected String url;
    protected Map<String, String> bodyParams = new HashMap<>();
    protected Intent intent;
    protected Bundle bundle;
    protected RxAppCompatActivity rxAppCompatActivity;
    protected BaseApplication baseApplication;
    protected ActivityPageManager activityPageManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxAppCompatActivity = this;
        baseApplication = (BaseApplication) getApplication();
        activityPageManager = ActivityPageManager.getInstance();
        activityPageManager.addActivity(this);

        setContentView(initLayoutId());

        loadView = new QMUILoadingView(this);
        loadView.setVisibility(View.GONE);
        loadView.setSize(100);
        loadView.setColor(getResources().getColor(R.color.color_80000000));
        layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;

        //        setToolbar();
        presenter = attachPresenter();
        initData();
        initViews();
        initLoadData();

//        RxPermissionsManager rxPermissionsManager = RxPermissionsManager.getInstance(this);
//        rxPermissionsManager.initRxPermissionsActivity(new OnCommonRxPermissionsCallback() {
//            @Override
//            public void onRxPermissionsAllPass() {
//                CrashHandlerManager crashHandlerManager = CrashHandlerManager.getInstance(rxAppCompatActivity);
//                crashHandlerManager.sendPreviousReportsToServer();
//                crashHandlerManager.init();
//            }
//
//            @Override
//            public void onNotCheckNoMorePromptError() {
//
//            }
//
//            @Override
//            public void onCheckNoMorePromptError() {
//
//            }
//        });
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
        ImmersionBar.with(this)
                .navigationBarColor(R.color.color_FFE066FF).init();
    }

    protected abstract void initData();

    protected abstract void initViews();

    protected abstract void initLoadData();

    /**
     * 适配为不同的view 装载不同的presenter
     *
     * @return
     */
    protected abstract T attachPresenter();

    protected void showToast(String message, boolean isLongToast) {
        //        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        if (!rxAppCompatActivity.isFinishing()) {
            Toast toast;
            int duration;
            if (isLongToast) {
                duration = Toast.LENGTH_LONG;
            } else {
                duration = Toast.LENGTH_SHORT;
            }
            toast = Toast.makeText(rxAppCompatActivity, message, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    protected void showCustomToast(int left, int right,
                                   int textSize, int textColor,
                                   int bgColor, int height,
                                   int roundRadius, String message,
                                   boolean isLongToast) {
        FrameLayout frameLayout = new FrameLayout(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        frameLayout.setLayoutParams(layoutParams);
        TextView textView = new TextView(this);
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

        Toast toast = new Toast(this);
        toast.setView(frameLayout);
        if (isLongToast) {
            toast.setDuration(Toast.LENGTH_LONG);
        } else {
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
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

    protected void detachPresenter() {
        if (presenter != null) {
            presenter.detachView();
            presenter = null;
        }
    }

    public ActivityPageManager getActivityPageManager() {
        return activityPageManager;
    }

    private void killAppProcess(Context context) {
        LogManager.i(TAG, "killAppProcess");
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = manager.getRunningAppProcesses();
        // 先杀掉相关进程，最后再杀掉主进程
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : processInfos) {
            if (runningAppProcessInfo.pid != android.os.Process.myPid()) {
                android.os.Process.killProcess(runningAppProcessInfo.pid);
            }
        }

        LogManager.i(TAG, "执行killAppProcess，應用開始自殺");
        CrashHandlerManager crashHandlerManager = CrashHandlerManager.getInstance(context);
        crashHandlerManager.saveTrimMemoryInfoToFile("执行killAppProcess，應用開始自殺");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LogManager.i(TAG, "error");
        }

        android.os.Process.killProcess(android.os.Process.myPid());
        // 正常退出程序，也就是结束当前正在运行的 java 虚拟机
        System.exit(0);
    }

    @Override
    protected void onDestroy() {
        detachPresenter();
        if (bodyParams != null) {
            bodyParams.clear();
            bodyParams = null;
        }
        if (activityPageManager != null) {
            if (activityPageManager.isLastAliveActivity().get()) {
                killAppProcess(baseApplication);
            }
            activityPageManager.removeActivity(rxAppCompatActivity);
        }
        super.onDestroy();
    }
}
