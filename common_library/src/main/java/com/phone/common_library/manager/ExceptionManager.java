package com.phone.common_library.manager;

import android.content.Context;

/**
 * 不會造成App崩潰的異常管理類（被try(){}catch拋出來的異常日誌管理類）
 */
public class ExceptionManager {

    private static final String TAG = ExceptionManager.class.getSimpleName();
    private static ExceptionManager exceptionManager;

    private ExceptionManager() {
    }

    public static ExceptionManager getInstance() {
        if (exceptionManager == null) {
            synchronized (ExceptionManager.class) {
                if (exceptionManager == null) {
                    exceptionManager = new ExceptionManager();
                }
            }
        }

        return exceptionManager;
    }

    /**
     * 打印異常的堆棧日誌
     *
     * @param throwable
     */
    public void throwException(Context context, Throwable throwable) {
//        //1.调试打印堆栈而不退出（推薦使用，開發人員用Log就可以把異常日誌打印出來）
//        LogManager.i(TAG, Log.getStackTraceString(throwable));

        //2.打印异常堆栈（推薦使用，讓系統把異常日誌打印出來）
        throwable.printStackTrace();

//        //3.获取当前线程的堆栈（打印的不是很詳細，報錯具體哪行沒打印出來，故不要使用這種方法）
//        for (StackTraceElement i : Thread.currentThread().getStackTrace()) {
//            LogManager.i(TAG, i.toString());
//        }

//        //4.打印异常堆栈（打印的不是很詳細，報錯具體哪行沒打印出來，故不要使用這種方法）
//        throwable.fillInStackTrace();
//        LogManager.i(TAG, "stackTrace", throwable);

        CrashHandlerManager crashHandlerManager = CrashHandlerManager.getInstance(context.getApplicationContext());
        //收集設備信息和保存異常日誌到文件
        crashHandlerManager.handleException(throwable);
    }


}
