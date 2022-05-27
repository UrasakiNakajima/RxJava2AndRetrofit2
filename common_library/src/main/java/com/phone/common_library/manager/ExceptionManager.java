package com.phone.common_library.manager;

public class ExceptionManager {

    private static final String TAG = ExceptionManager.class.getSimpleName();

    public static void throwException() {
//        //1.调试打印堆栈而不退出
//        LogManager.i(TAG, Log.getStackTraceString(new Throwable()));
//
//        //2.创建异常打印堆栈
//        Exception e = new Exception("this is a log");
//        e.printStackTrace();

        //3.获取当前线程的堆栈
        for (StackTraceElement i : Thread.currentThread().getStackTrace()) {
            LogManager.i(TAG, i.toString());
        }


//        RuntimeException re = new RuntimeException();
//        re.fillInStackTrace();
//        LogManager.i(TAG, "stackTrace", re);
//
//        // 主动抛出异常调试
//        try {
//            LogManager.i(TAG,
//                    "--------------------------------NullPointerException-----------1");
//            throw new NullPointerException();
//        } catch (NullPointerException e1) {
//            // TODO: handle exception
//            LogManager.i(TAG, "--------------------------------NullPointerException");
//            Log.e(TAG, Log.getStackTraceString(e1));
//            // e1.printStackTrace();
//        }
//        LogManager.i(TAG,
//                "--------------------------------NullPointerException-----------end");
    }


}
