package com.phone.library_base.manager

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
import androidx.core.content.ContextCompat.getSystemService
import com.phone.library_base.BaseApplication
import com.phone.library_base.R
import com.phone.library_base.manager.LogManager.i

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/09 11:35
 * introduce : 屏幕管理类
 */

object ScreenManager {

    @JvmStatic
    private val TAG = ScreenManager::class.java.simpleName

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    @JvmStatic
    fun dpToPx(dpValue: Float): Int {
        val scale = BaseApplication.instance().resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    @JvmStatic
    fun pxToDp(pxValue: Float): Int {
        val scale = BaseApplication.instance().resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    @JvmStatic
    fun pxToSp(pxValue: Float): Int {
        val fontScale = BaseApplication.instance().resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    @JvmStatic
    fun spToPx(spValue: Float): Int {
        val fontScale = BaseApplication.instance().resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 拿到资源文件的dp值
     */
    @JvmStatic
    fun getDimenDp(dpValue: Int): Int {
        return pxToDp(BaseApplication.instance().resources.getDimension(dpValue))
    }

    /**
     * 拿到资源文件的dp值
     */
    @JvmStatic
    fun getDimenPx(dpValue: Int): Int {
        return BaseApplication.instance().resources.getDimension(dpValue).toInt()
    }

    /**
     * 拿到资源文件的sp值
     */
    @JvmStatic
    fun getDimenSp(spValue: Int): Int {
        return pxToSp(BaseApplication.instance().resources.getDimension(spValue))
    }

    /**
     * 获取屏幕高度(px)
     */
    @JvmStatic
    fun getScreenHeight(): Int {
        return BaseApplication.instance().resources.displayMetrics.heightPixels
    }

    /**
     * 获取屏幕宽度(px)
     */
    @JvmStatic
    fun getScreenWidth(): Int {
        return BaseApplication.instance().resources.displayMetrics.widthPixels
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView  呼出window的view
     * @param contentView window的内容布局
     * @return window显示的左上角的xOff, yOff坐标
     */
    @JvmStatic
    fun calculatePopWindowPos(anchorView: View, contentView: View): IntArray {
        val windowPos = IntArray(2)
        val anchorLoc = IntArray(2)
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc)
        val anchorHeight = anchorView.height
        // 获取屏幕的高宽
        val screenHeight = getScreenHeight()
        val screenWidth = getScreenWidth()
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
    @JvmStatic
    fun getScreenWidthDp(): Int {
        val wm = BaseApplication.instance().getSystemService(Context.WINDOW_SERVICE) as WindowManager
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

    @JvmStatic
    fun getWidthAndHeight(window: Window): Array<Int?> {
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