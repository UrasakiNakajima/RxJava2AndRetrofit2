package com.mobile.common_library;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.mobile.common_library.manager.LogManager;
import com.mobile.common_library.manager.RetrofitManager;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2020/3/7 10:07
 * introduce :
 */

public class MineApplication extends MultiDexApplication {

    private static final String TAG = "MineApplication";
    //声明 初始化
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    public static int MODE = Context.MODE_PRIVATE;
    //用户名
    private String userName;
    //店主真实姓名（负责人）
    private String fullName;
    //用户Id
    private String userId;
    //用户昵称
    private String nickName;
    //店铺Id
    private String shopId;
    private boolean isLogin;
    private String authorization;
    private boolean isCopyDatabase;
    private String date;
    private String alipyQrcode;

    private boolean isCreateMineApplication;

    //经度
    private String longitude;
    //纬度
    private String latitude;
    private static MineApplication mineApplication;

    private RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        mineApplication = this;
        //检测内存泄漏
//        mRefWatcher = setupLeakCanary();

        //文件为mySp  存放在/data/data/<packagename>/shared_prefs/目录下的
        sp = getSharedPreferences("app", MODE);
        editor = sp.edit();

        isCreateMineApplication = false;
        date = getDate();
        if (date == null || "".equals(date)) {
            date = "0";
        }

        //初始化okhttp3
//        Okhttp3Manager.getInstance(this);

        //初始化retrofit
        RetrofitManager.getInstance();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

//    public static RefWatcher getRefWatcher() {
//        return mineApplication.mRefWatcher;
//    }

    public static MineApplication getInstance() {
        return mineApplication;
    }

    protected boolean isEmpty(String dataStr) {
        if (dataStr != null && !"".equals(dataStr)) {
            return false;
        } else {
            return true;
        }
    }

    public String getUserName() {
        userName = sp.getString("userName", "");
        LogManager.i(TAG, "getUserName***" + userName);
        return userName;
    }

    public void setUserName(String userName) {
        LogManager.i(TAG, "setUserName***" + userName);
        editor.putString("userName", userName);
        editor.commit();
    }

    public String getFullName() {
        fullName = sp.getString("fullName", "");
        LogManager.i(TAG, "getFullName***" + fullName);
        return fullName;
    }

    public void setFullName(String fullName) {
        LogManager.i(TAG, "setFullName***" + fullName);
        editor.putString("fullName", fullName);
        editor.commit();
    }

    /**
     * 获取userId
     *
     * @return
     */
    public String getUserId() {
        userId = sp.getString("userId", "");
        LogManager.i(TAG, "getUserId***" + userId);
        return userId;
    }

    /**
     * 存储userId
     *
     * @param userId
     */
    public void setUserId(String userId) {
        LogManager.i(TAG, "setUserId***" + userId);
        editor.putString("userId", userId);
        editor.commit();
    }

    public String getNickName() {
        nickName = sp.getString("nickName", "");
        LogManager.i(TAG, "getNickName***" + nickName);
        return nickName;
    }

    public void setNickName(String nickName) {
        LogManager.i(TAG, "setNickName***" + nickName);
        editor.putString("nickName", nickName);
        editor.commit();
    }

    public String getShopId() {
        shopId = sp.getString("shopId", "");
        LogManager.i(TAG, "getShopId***" + shopId);
        return shopId;
    }

    public void setShopId(String shopId) {
        LogManager.i(TAG, "setShopId***" + shopId);
        editor.putString("shopId", shopId);
        editor.commit();
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

    public String getAuthorization() {
        authorization = sp.getString("authorization", "");
        return authorization;
    }

    public void setAuthorization(String authorization) {
        LogManager.i(TAG, "setAuthorization***" + authorization);
        editor.putString("authorization", authorization);
        editor.commit();
    }

    public boolean isCopyDatabase() {
        isCopyDatabase = sp.getBoolean("isCopyDatabase", false);
        return isCopyDatabase;
    }

    public void setCopyDatabase(boolean isCopyDatabase) {
        editor.putBoolean("isCopyDatabase", isCopyDatabase);
        editor.commit();
    }

    public String getDate() {
        date = sp.getString("date", "");
        return date;
    }

    public void setDate(String date) {
        editor.putString("date", date);
        editor.commit();
    }

    public String getAlipyQrcode() {
        alipyQrcode = sp.getString("alipyQrcode", "");
        return alipyQrcode;
    }

    public void setAlipyQrcode(String alipyQrcode) {
        editor.putString("alipyQrcode", alipyQrcode);
        editor.commit();
    }

    public String getLongitude() {
        longitude = sp.getString("longitude", "");
        return longitude;
    }

    public void setLongitude(String longitude) {
        editor.putString("longitude", longitude);
        editor.commit();
    }

    public String getLatitude() {
        latitude = sp.getString("latitude", "");
        return latitude;
    }

    public void setLatitude(String latitude) {
        editor.putString("latitude", latitude);
        editor.commit();
    }

    public void setLogout() {
        LogManager.i(TAG, "setLogout***");
//        editor.clear();
        editor.remove("userName");
        editor.remove("userId");
        editor.remove("nickName");
        editor.remove("shopId");
        editor.remove("isLogin");
        editor.remove("isBindKingBeans");
        editor.remove("authorization");
        editor.remove("cookie");
        editor.remove("isCopyDatabase");
        editor.remove("province");
        editor.remove("city");
        editor.remove("county");
        editor.remove("address");
        editor.remove("date");
        editor.remove("longitude");
        editor.remove("latitude");
        editor.commit();
    }

}
