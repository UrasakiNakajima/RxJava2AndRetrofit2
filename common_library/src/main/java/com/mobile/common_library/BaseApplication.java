package com.mobile.common_library;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.mobile.common_library.manager.LogManager;
import com.mobile.common_library.manager.RetrofitManager;

public class BaseApplication extends MultiDexApplication {

    private static final String TAG = "BaseApplication";
    //声明 初始化
    protected SharedPreferences sp;
    protected SharedPreferences.Editor editor;
    protected static int MODE = Context.MODE_PRIVATE;
    private String authorization;

    protected static BaseApplication baseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;

        //文件为mySp  存放在/data/data/<packagename>/shared_prefs/目录下的
        sp = getSharedPreferences("app", MODE);
        editor = sp.edit();

        //初始化retrofit
        RetrofitManager.getInstance();

        if (true) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static BaseApplication getInstance() {
        return baseApplication;
    }

    public String getAuthorization() {
        authorization = sp.getString("authorization", "");
        return authorization;
    }

    public void setAuthorization(String authorization) {
        LogManager.i(TAG, "setAuthorization***" + authorization);
        editor.putString("authorization", authorization);
        editor.commit();
    }

}
