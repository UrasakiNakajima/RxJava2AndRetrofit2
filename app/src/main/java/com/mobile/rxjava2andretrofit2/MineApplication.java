package com.mobile.rxjava2andretrofit2;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.multidex.MultiDexApplication;

import com.mobile.rxjava2andretrofit2.manager.LogManager;
import com.mobile.rxjava2andretrofit2.manager.RetrofitManager;

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
    //    //店铺负责人
//    private String personInCharge;
    //用户角色
    private String userRole;
    //店铺Id
    private String shopId;
    //密码
    private String password;
    //是否绑定皇豆账号
    private String isBindKingBeans;

    private boolean isLogin;
    private String registrationID;
    private String authorization;
    private String cookie;
    //    private String longLat;
    private boolean isCopyDatabase;
    private String date;
    private String alipyQrcode;

    private boolean isCreateMineApplication;
    private boolean isPrizesActivity;
    private boolean isShop;

    private String province;
    private String city;
    private String county;
    private String address;
    //经度
    private String longitude;
    //纬度
    private String latitude;
    private boolean insertOrderSearchHistory;
    //营业状态
    private String businessStatus;
    private static MineApplication mineApplication;

//    private RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
//        //检测内存泄漏
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        mRefWatcher = LeakCanary.install(this);

        //初始化okhttp3
//        Okhttp3Manager.getInstance(this);

        mineApplication = this;
        //文件为mySp  存放在/data/data/<packagename>/shared_prefs/目录下的
        sp = getSharedPreferences("app", MODE);
        editor = sp.edit();

        isCreateMineApplication = false;
        date = getDate();
        if (date == null || "".equals(date)) {
            date = "0";
        }

        RetrofitManager.getInstance();
    }

//    public static RefWatcher getRefWatcher(Context context) {
//        MineApplication mineApplication = (MineApplication) context.getApplicationContext();
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

//    public String getPersonInCharge() {
//        personInCharge = sp.getString("personInCharge", "");
//        LogManager.i(TAG, "getPersonInCharge***" + personInCharge);
//        return personInCharge;
//    }
//
//    public void setPersonInCharge(String personInCharge) {
//        LogManager.i(TAG, "setPersonInCharge***" + personInCharge);
//        editor.putString("personInCharge", personInCharge);
//        editor.commit();
//    }

    public String getUserRole() {
        userRole = sp.getString("userRole", "");
        LogManager.i(TAG, "getUserRole***" + userRole);
        return userRole;
    }

    public void setUserRole(String userRole) {
        LogManager.i(TAG, "setUserRole***" + userRole);
        if (!isEmpty(userRole)) {
            if ("1".equals(userRole)) {
                setShop(true);
            } else {
                setShop(false);
            }
            editor.putString("userRole", userRole);
            editor.commit();
        }
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

    public String getPassword() {
        password = sp.getString("password", "");
        LogManager.i(TAG, "getPassword***" + password);
        return password;
    }

    public void setPassword(String password) {
        LogManager.i(TAG, "setPassword***" + password);
        editor.putString("password", password);
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

    public String getRegistrationID() {
        registrationID = sp.getString("registrationID", "");
        return registrationID;
    }

    public void setRegistrationID(String registrationID) {
        LogManager.i(TAG, "setRegistrationID***" + registrationID);
        editor.putString("registrationID", registrationID);
        editor.commit();
    }

    public String getIsBindKingBeans() {
        isBindKingBeans = sp.getString("isBindKingBeans", "1");
        return isBindKingBeans;
    }

    public void setIsBindKingBeans(String isBindKingBeans) {
        LogManager.i(TAG, "setIsBindKingBeans***" + isBindKingBeans);
        editor.putString("isBindKingBeans", isBindKingBeans);
        editor.commit();
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

    public String getCookie() {
        cookie = sp.getString("cookie", "");
        return cookie;
    }

    public void setCookie(String cookie) {
        LogManager.i(TAG, "setCookie***" + cookie);
        editor.putString("cookie", cookie);
        editor.commit();
    }

//    public String getLongLat() {
//        longLat = sp.getString("longLat", "");
//        return longLat;
//    }
//
//    public void setLongLat(String longLat) {
//        editor.putString("longLat", longLat);
//        editor.commit();
//    }

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

    public boolean isCreateMineApplication() {
        return isCreateMineApplication;
    }

    public void setCreateMineApplication(boolean isCreateMineApplication) {
        this.isCreateMineApplication = isCreateMineApplication;
    }

    public boolean isPrizesActivity() {
        return isPrizesActivity;
    }

    public void setPrizesActivity(boolean prizesActivity) {
        isPrizesActivity = prizesActivity;
    }

    public String getProvince() {
        province = sp.getString("province", "");
        return province;
    }

    public void setProvince(String province) {
        editor.putString("province", province);
        editor.commit();
    }

    public String getCity() {
        city = sp.getString("city", "");
        return city;
    }

    public void setCity(String city) {
        editor.putString("city", city);
        editor.commit();
    }

    public String getCounty() {
        county = sp.getString("county", "");
        return county;
    }

    public void setCounty(String county) {
        editor.putString("county", county);
        editor.commit();
    }

    public String getAddress() {
        address = sp.getString("address", "");
        return address;
    }

    public void setAddress(String address) {
        editor.putString("address", address);
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

    public boolean isInsertOrderSearchHistory() {
        insertOrderSearchHistory = sp.getBoolean("insertOrderSearchHistory", false);
        return insertOrderSearchHistory;
    }

    public void setInsertOrderSearchHistory(boolean insertOrderSearchHistory) {
        editor.putBoolean("insertOrderSearchHistory", insertOrderSearchHistory);
        editor.commit();
    }

    public String getBusinessStatus() {
        businessStatus = sp.getString("businessStatus", "0");
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        editor.putString("businessStatus", businessStatus);
        editor.commit();
    }

    public boolean isShop() {
        isShop = sp.getBoolean("isShop", false);
        return isShop;
    }

    public void setShop(boolean isShop) {
        editor.putBoolean("isShop", isShop);
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
