package com.phone.common_library.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/10 14:05
 * introduce :
 */

public class ActivityPageManager {

    private static final String TAG = ActivityPageManager.class.getSimpleName();
    /**
     * 单一实例
     */
    private static ActivityPageManager mActivityPageManager;
    /**
     * Activity栈 Stack:线程安全
     */
    private final Stack<Activity> mActivityStack = new Stack<>();

    /**
     * 還存活的Activity栈
     */
    private final Stack<Activity> mActivityAliveStack = new Stack<>();

    /**
     * 是否是棧中最後一個存活的Activity
     */
    private final AtomicBoolean isLastAliveActivity = new AtomicBoolean(false);

    /**
     * 私有构造器 无法外部创建
     */
    private ActivityPageManager() {
    }

    /**
     * 获取单一实例 双重锁定
     *
     * @return this
     */
    public static ActivityPageManager getInstance() {
        if (mActivityPageManager == null) {
            synchronized (ActivityPageManager.class) {
                if (mActivityPageManager == null) {
                    mActivityPageManager = new ActivityPageManager();
                }
            }
        }
        return mActivityPageManager;
    }

    /**
     * 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        mActivityStack.add(activity);
    }

    /**
     * 移除栈中的Activity
     *
     * @param activity Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null && mActivityStack.contains(activity)) {
            mActivityStack.remove(activity);
        }
    }

    /**
     * 获取当前Activity (堆栈中最后一个添加的)
     *
     * @return Activity
     */
    public Activity getCurrentActivity() {
        return mActivityStack.lastElement();
    }

    /**
     * 获取指定类名的Activity
     */
    public Activity getActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 结束当前Activity (堆栈中最后一个添加的)
     */
    public void finishCurrentActivity() {
        Activity activity = mActivityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     *
     * @param activity Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && mActivityStack.contains(activity)) {
            mActivityStack.remove(activity);
            LogManager.i(TAG, "finishActivity");
            activity.finish();
        }
    }

    /**
     * 结束指定的Activity
     *
     * @param activity Activity
     */
    public void finishAliveActivity(Activity activity) {
        if (activity != null && mActivityAliveStack.contains(activity)) {
            mActivityAliveStack.remove(activity);
            LogManager.i(TAG, "finishAliveActivity");
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     *
     * @param clazz Activity.class
     */
    public void finishActivity(Class<?> clazz) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(clazz)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        mActivityAliveStack.clear();
        for (int i = mActivityStack.size() - 1; i >= 0; i--) {
            if (mActivityStack.get(i) != null) {
                mActivityAliveStack.add(mActivityStack.get(i));
            }
        }
        for (int i = mActivityAliveStack.size() - 1; i >= 0; i--) {
            if (i == 0) {
                isLastAliveActivity.set(true);
                LogManager.i(TAG, "isLastAliveActivity.set(true)");
            }
            finishAliveActivity(mActivityAliveStack.get(i));
        }
        mActivityAliveStack.clear();
        mActivityStack.clear();
    }

    public AtomicBoolean isLastAliveActivity() {
        return isLastAliveActivity;
    }

    /**
     * 结束某个Activity之外的所有Activity
     */
    public void finishAllActivityExcept(Class<?> clazz) {
        for (int i = mActivityStack.size() - 1; i >= 0; i--) {

            if (mActivityStack.get(i) != null && !mActivityStack.get(i).getClass().equals(clazz)) {
                finishActivity(mActivityStack.get(i));
            }
        }
    }

    /**
     * 退出应用程序
     */
    public void exitApp() {
        try {
            finishAllActivity();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    /**
     * 退出应用程序（最後一個存活的Activity退出的时候（onDestroy）做了退出應用程序處理）
     */
    public void exitApp2() {
        try {
            LogManager.i(TAG, "exitApp2");
            finishAllActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断某个Activity 界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     * @return
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            return className.equals(cpn.getClassName());
        }
        return false;
    }


}
