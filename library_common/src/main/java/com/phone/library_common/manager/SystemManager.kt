package com.phone.library_common.manager

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import com.phone.library_common.BaseApplication
import java.util.*

object SystemManager {

    /**
     * 获取当前手机系统语言
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    fun getSystemLanguage(): String? {
        return Locale.getDefault().language
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    fun getSystemLanguageList(): Array<Locale?>? {
        return Locale.getAvailableLocales()
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    fun getSystemVersion(): String? {
        return Build.VERSION.RELEASE
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    fun getSystemModel(): String? {
        return Build.MODEL
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    fun getDeviceBrand(): String? {
        return Build.BRAND
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    fun getIMEI(): String? {
        val telephonyManager =
            BaseApplication.get().getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager
        return if (telephonyManager != null) {
            if (ActivityCompat.checkSelfPermission(
                    BaseApplication.get(),
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                null
            } else telephonyManager.deviceId
        } else null
    }

    /**
     * 获取手机CPU_ABI
     *
     * @return 手机CPU_ABI
     */
    fun getDeviceCpuAbi(): String? {
        return Build.CPU_ABI
    }

    /**
     * 获取手機唯一識別码（推薦使用，Android有很多雜牌手機，還有山寨機，这个方法可以統一獲取到）
     *
     * @return
     */
    fun getSystemId(): String {
        return SystemIdManager.getSystemId()
    }

//    /**
//     * 获取手機唯一識別码（不推薦使用，Android有很多雜牌手機，還有山寨機，根本不能統一獲取到deviceUuid）
//     *
//     * @return
//     */
//    public static String getDeviceUUid() {
//        String androidId = DeviceUtils.getAndroidID();
//        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) androidId.hashCode() << 32));
//        return deviceUuid.toString();
//    }

}