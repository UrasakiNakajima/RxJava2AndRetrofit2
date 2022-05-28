package com.phone.common_library.manager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import com.blankj.utilcode.util.DeviceUtils;

import java.util.Locale;
import java.util.UUID;

public class SystemManager {

    /**
     * 获取当前手机系统语言
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */

    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */

    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */

    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */

    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */

    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            return telephonyManager.getDeviceId();
        }
        return null;
    }

    /**
     * 获取手机CPU_ABI
     *
     * @return 手机CPU_ABI
     */
    public static String getDeviceCpuAbi() {
        return android.os.Build.CPU_ABI;
    }

    /**
     * 获取手機唯一識別码（推薦使用，Android有很多雜牌手機，還有山寨機，这个方法可以統一獲取到）
     *
     * @return
     */
    public static String getSystemId(Context context) {
        return SystemIdManager.getSystemId(context);
    }

    /**
     * 获取手機唯一識別码（不要使用，Android有很多雜牌手機，還有山寨機，根本沒有統一的deviceUuid）
     *
     * @return
     */
    public static String getDeviceUUid() {
        String androidId = DeviceUtils.getAndroidID();
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) androidId.hashCode() << 32));
        return deviceUuid.toString();
    }

}
