package com.phone.library_common.manager

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.text.TextUtils
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/10 14:05
 * introduce :
 */

class ActivityPageManager private constructor() {

    private val TAG = ActivityPageManager::class.java.simpleName

    /**
     * Activity栈 Stack:线程安全
     */
    private val mActivityStack = Stack<Activity>()

    /**
     * 還存活的Activity栈
     */
    private val mActivityAliveStack = Stack<Activity>()

    /**
     * 是否是棧中最後一個存活的Activity
     */
    val mIsLastAliveActivity = AtomicBoolean(false)

    companion object {
        private var instance: ActivityPageManager? = null
            get() {
                if (field == null) {
                    field = ActivityPageManager()
                }
                return field
            }

        //Synchronized添加后就是线程安全的的懒汉模式
        @Synchronized
        @JvmStatic
        fun instance(): ActivityPageManager {
            return instance!!
        }
    }

    /**
     * 添加Activity到栈
     */
    fun addActivity(activity: Activity) {
        mActivityStack.add(activity)
    }

    /**
     * 移除栈中的Activity
     *
     * @param activity Activity
     */
    fun removeActivity(activity: Activity) {
        if (mActivityStack.contains(activity)) {
            mActivityStack.remove(activity)
        }
    }

    /**
     * 获取当前Activity (堆栈中最后一个添加的)
     *
     * @return Activity
     */
    fun getCurrentActivity(): Activity? {
        return mActivityStack.lastElement()
    }

    /**
     * 获取指定类名的Activity
     */
    fun getActivity(cls: Class<*>): Activity? {
        for (activity in mActivityStack) {
            if (activity?.javaClass == cls) {
                return activity
            }
        }
        return null
    }

    /**
     * 结束当前Activity (堆栈中最后一个添加的)
     */
    fun finishCurrentActivity() {
        val activity = mActivityStack.lastElement()
        activity?.let {
            finishActivity(activity)
        }
    }

    /**
     * 结束指定的Activity
     *
     * @param activity Activity
     */
    fun finishActivity(activity: Activity) {
        if (mActivityStack.contains(activity)) {
            mActivityStack.remove(activity)
            LogManager.i(TAG, "finishActivity")
            activity.finish()
        }
    }

    /**
     * 结束指定的Activity
     *
     * @param activity Activity
     */
    fun finishAliveActivity(activity: Activity) {
        if (mActivityAliveStack.contains(activity)) {
            mActivityAliveStack.remove(activity)
            LogManager.i(TAG, "finishAliveActivity")
            activity.finish()
        }
    }

    /**
     * 结束指定类名的Activity
     *
     * @param clazz Activity.class
     */
    fun finishActivity(clazz: Class<*>) {
        for (activity in mActivityStack) {
            if (activity?.javaClass == clazz) {
                finishActivity(activity)
                break
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        mActivityAliveStack.clear()
        for (i in mActivityStack.indices.reversed()) {
            if (mActivityStack[i] != null) {
                mActivityAliveStack.add(mActivityStack[i])
            }
        }
        for (i in mActivityAliveStack.indices.reversed()) {
            if (i == 0) {
                mIsLastAliveActivity.set(true)
                LogManager.i(TAG, "mIsLastAliveActivity.set(true)")
            }
            finishAliveActivity(mActivityAliveStack[i])
        }
        mActivityAliveStack.clear()
        mActivityStack.clear()
    }

    /**
     * 结束某个Activity之外的所有Activity
     */
    fun finishAllActivityExcept(clazz: Class<*>) {
        for (i in mActivityStack.indices.reversed()) {
            if (mActivityStack[i] != null && mActivityStack[i]?.javaClass != clazz) {
                finishActivity(mActivityStack[i])
            }
        }
    }

//    /**
//     * 退出应用程序
//     */
//    public void exitApp() {
//        try {
//            finishAllActivity()
//        } catch (Exception e) {
//            e.printStackTrace()
//        } finally {
//            System.exit(0)
//        }
//    }

    //    /**
    //     * 退出应用程序
    //     */
    //    public void exitApp() {
    //        try {
    //            finishAllActivity()
    //        } catch (Exception e) {
    //            e.printStackTrace()
    //        } finally {
    //            System.exit(0)
    //        }
    //    }

    /**
     * 退出应用程序（最後一個存活的Activity退出的时候（onDestroy）做了退出應用程序處理）
     */
    fun exitApp() {
        try {
            LogManager.i(TAG, "exitApp")
            finishAllActivity()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 判断某个Activity 界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     * @return
     */
    fun isForeground(context: Context?, className: String): Boolean {
        if (context == null || TextUtils.isEmpty(className)) {
            return false
        }
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val list = am.getRunningTasks(1)
        if (list != null && list.size > 0) {
            val cpn = list[0].topActivity
            return className == cpn?.className
        }
        return false
    }

}