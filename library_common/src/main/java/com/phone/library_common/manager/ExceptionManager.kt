package com.phone.library_common.manager

/**
 * 不會造成App崩潰的異常（被try(){}catch拋出來的異常）管理類
 */
class ExceptionManager private constructor() {

    /**
     * 保证只有一个实例
     *
     * @return
     */
    companion object {
        private val TAG = ExceptionManager::class.java.simpleName
        private var instance: ExceptionManager? = null
            get() {
                if (field == null) {
                    field = ExceptionManager()
                }
                return field
            }

        //Synchronized添加后就是线程安全的的懒汉模式
        @Synchronized
        @JvmStatic
        fun instance(): ExceptionManager {
            return instance!!
        }
    }

    /**
     * 打印異常的堆棧日誌
     *
     * @param throwable
     */
    fun throwException(throwable: Throwable) {
//        //1.调试打印堆栈而不退出（推薦使用，開發人員用Log就可以把異常日誌打印出來）
//        LogManager.i(TAG, Log.getStackTraceString(throwable))

//        //2.打印异常堆栈（推薦使用，讓系統把異常日誌打印出來）
//        throwable.printStackTrace()

//        //3.获取当前线程的堆栈（不推薦使用，打印的不是很詳細，報錯具體哪行沒打印出來，故不要使用這種方法）
//        for (StackTraceElement i : Thread.currentThread().getStackTrace()) {
//            LogManager.i(TAG, i.toString())
//        }

//        //4.打印异常堆栈（不推薦使用，打印的不是很詳細，報錯具體哪行沒打印出來，故不要使用這種方法）
//        throwable.fillInStackTrace()
//        LogManager.i(TAG, "stackTrace", throwable)
        val crashHandlerManager = CrashHandlerManager.instance()
        //收集設備信息和保存異常日誌到文件
        crashHandlerManager?.handleException(throwable)
    }


}