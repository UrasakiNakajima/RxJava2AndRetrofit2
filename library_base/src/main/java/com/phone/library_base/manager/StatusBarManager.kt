package com.phone.library_base.manager

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import com.phone.library_base.BaseApplication
import com.phone.library_base.R

object StatusBarManager {

    @JvmStatic
    private val TAG = StatusBarManager::class.java.simpleName

    @JvmStatic
    fun getStatusBarHeight(): Int {
        return getInternalDimensionSize("status_bar_height")
    }

    @JvmStatic
    fun getNavigationBarHeight(context: Context): Int {
        val mInPortrait =
            context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val result = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (hasNavBar(context as Activity)) {
                val key: String
                key = if (mInPortrait) {
                    "navigation_bar_height"
                } else {
                    "navigation_bar_height_landscape"
                }
                return getInternalDimensionSize(key)
            }
        }
        return result
    }

    @JvmStatic
    private fun getInternalDimensionSize(key: String): Int {
        var result = 0
        try {
            val resourceId = BaseApplication.instance().resources.getIdentifier(key, "dimen", "android")
            if (resourceId > 0) {
                result = Math.round(
                    BaseApplication.instance().resources.getDimensionPixelSize(resourceId) *
                            Resources.getSystem().displayMetrics.density /
                            BaseApplication.instance().resources.displayMetrics.density
                )
            } else {
                setResult(key)
            }
        } catch (ignored: Resources.NotFoundException) {
            setResult(key)
        }
        return result
    }

    @JvmStatic
    private fun hasNavBar(activity: Activity): Boolean {
        //判断小米手机是否开启了全面屏,开启了，直接返回false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Settings.Global.getInt(activity.contentResolver, "force_fsg_nav_bar", 0) != 0) {
                return false
            }
        }
        //其他手机根据屏幕真实高度与显示高度是否相同来判断
        val windowManager = activity.windowManager
        val d = windowManager.defaultDisplay
        val realDisplayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            d.getRealMetrics(realDisplayMetrics)
        }
        val realHeight = realDisplayMetrics.heightPixels
        val realWidth = realDisplayMetrics.widthPixels
        val displayMetrics = DisplayMetrics()
        d.getMetrics(displayMetrics)
        val displayHeight = displayMetrics.heightPixels
        val displayWidth = displayMetrics.widthPixels
        return realWidth - displayWidth > 0 || realHeight - displayHeight > 0
    }

    private fun setResult(key: String) : Int {
        var result = 0
        if("status_bar_height" == key) {
            result = ScreenManager.dpToPx(ScreenManager.getDimenDp(R.dimen.base_dp_25).toFloat())
        } else {
            result = ScreenManager.dpToPx(ScreenManager.getDimenDp(R.dimen.base_dp_48).toFloat())
        }
        return result
    }

}