package com.phone.common_library.manager

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.phone.common_library.BaseApplication
import com.phone.common_library.manager.LogManager.i

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/09 11:35
 * introduce : 屏幕管理类
 */

object ScreenManager {

    private val TAG = ScreenManager::class.java.simpleName

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dpToPx(dpValue: Float): Int {
        val scale = BaseApplication.get().resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun pxToDp(pxValue: Float): Int {
        val scale = BaseApplication.get().resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    fun pxToSp(pxValue: Float): Int {
        val fontScale = BaseApplication.get().resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    fun spToPx(spValue: Float): Int {
        val fontScale = BaseApplication.get().resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 拿到资源文件的dp值
     */
    fun getDimenDp(dpValue: Int): Int {
        return pxToDp(BaseApplication.get().resources.getDimension(dpValue))
    }

    /**
     * 拿到资源文件的sp值
     */
    fun getDimenSp(spValue: Int): Int {
        return pxToSp(BaseApplication.get().resources.getDimension(spValue))
    }

    /**
     * 获取屏幕高度(px)
     */
    fun getScreenHeight(context: Context?): Int {
        return BaseApplication.get().resources.displayMetrics.heightPixels
    }

    /**
     * 获取屏幕宽度(px)
     */
    fun getScreenWidth(context: Context?): Int {
        return BaseApplication.get().resources.displayMetrics.widthPixels
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView  呼出window的view
     * @param contentView window的内容布局
     * @return window显示的左上角的xOff, yOff坐标
     */
    fun calculatePopWindowPos(anchorView: View, contentView: View): IntArray? {
        val windowPos = IntArray(2)
        val anchorLoc = IntArray(2)
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc)
        val anchorHeight = anchorView.height
        // 获取屏幕的高宽
        val screenHeight = getScreenHeight(anchorView.context)
        val screenWidth = getScreenWidth(anchorView.context)
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        // 计算contentView的高宽
        val windowHeight = contentView.measuredHeight
        val windowWidth = contentView.measuredWidth
        // 判断需要向上弹出还是向下弹出显示
        val isNeedShowUp = screenHeight - anchorLoc[1] - anchorHeight < windowHeight
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth
            windowPos[1] = anchorLoc[1] - windowHeight
        } else {
            windowPos[0] = screenWidth - windowWidth
            windowPos[1] = anchorLoc[1] + anchorHeight
        }
        return windowPos
    }

    /**
     * 获取屏幕宽度(dp)
     */
    fun getScreenWidthDp(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels // 屏幕宽度（像素）
        val height = dm.heightPixels // 屏幕高度（像素）
        val density = dm.density // 屏幕密度（0.75 / 1.0 / 1.5）
        val densityDpi = dm.densityDpi // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        val screenWidth = (width / density).toInt() // 屏幕宽度(dp)
        val screenHeight = (height / density).toInt() // 屏幕高度(dp)
        val widthStr = "屏幕宽度（像素）：$width"
        val heightStr = "屏幕宽度（像素）：$height"
        val dpi = "屏幕密度dpi（120 / 160 / 240）：$densityDpi"
        val widthDpStr = "屏幕宽度（dp）：$screenWidth"
        val heightDpStr = "屏幕高度（dp）：$screenHeight"
        i(TAG, "屏幕宽度（像素）：$width")
        i(TAG, "屏幕高度（像素）：$height")
        i(TAG, "屏幕密度（0.75 / 1.0 / 1.5）：$density")
        i(TAG, "屏幕密度dpi（120 / 160 / 240）：$densityDpi")
        i(TAG, "屏幕宽度（dp）：$screenWidth")
        i(TAG, "屏幕高度（dp）：$screenHeight")
        val allStr =
            "$widthStr*****$heightStr*****$dpi*****$widthDpStr*****$heightDpStr"
        //        try {
        //            ReadAndWriteManager.writeExternal(context, "mineData.txt", allStr);
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
        return screenWidth
    }

    private fun getInternalDimensionSize(key: String): Int {
        var result = 0
        try {
            val resourceId = BaseApplication.get().resources.getIdentifier(key, "dimen", "android")
            if (resourceId > 0) {
                result = Math.round(
                    BaseApplication.get().resources.getDimensionPixelSize(resourceId) *
                            Resources.getSystem().displayMetrics.density /
                            BaseApplication.get().resources.displayMetrics.density
                )
            }
        } catch (ignored: NotFoundException) {
            return 0
        }
        return result
    }

    fun getStatusBarHeight(): Int {
        return getInternalDimensionSize("status_bar_height")
    }

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

    fun getWidthAndHeight(window: Window?): Array<Int?>? {
        if (window == null) {
            return null
        }
        val integer = arrayOfNulls<Int>(2)
        val dm = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.windowManager.defaultDisplay.getRealMetrics(dm)
        } else {
            window.windowManager.defaultDisplay.getMetrics(dm)
        }
        integer[0] = dm.widthPixels
        integer[1] = dm.heightPixels
        return integer
    }

}