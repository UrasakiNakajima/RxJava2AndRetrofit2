package com.phone.common_library;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.phone.common_library.manager.ActivityPageManager;
import com.phone.common_library.manager.CrashHandlerManager;
import com.phone.common_library.manager.LogManager;
import com.phone.common_library.manager.RetrofitManager;

public class BaseApplication extends MultiDexApplication {

    private static final String TAG = "BaseApplication";
    //声明 初始化
    protected SharedPreferences sp;
    protected SharedPreferences.Editor editor;
    protected static final int MODE = Context.MODE_PRIVATE;
    private boolean isLogin;
    private String accessToken;
    private String systemId;

    protected static BaseApplication baseApplication;
    private ActivityPageManager activityPageManager;

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
        crashHandlerManager.init();

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
            //App开始自杀，清场掉所有的activity（最後一個存活的Activity退出的时候（onDestroy）做了退出應用程序處理）
            activityPageManager.exitApp2();
        }
    }
}
