package com.phone.library_common.manager

import com.phone.library_common.callback.OnCommonSuccessCallback
import java.util.concurrent.*

class ThreadPoolManager {

    private var syncThreadPool: ExecutorService? = null
    private var scheduledThreadPool: ScheduledExecutorService? = null

    /**
     * 保证只有一个实例
     *
     * @return
     */
    companion object {
        private val TAG = ThreadPoolManager::class.java.simpleName
        @Volatile
        private var instance: ThreadPoolManager? = null
            get() {
                if (field == null) {
                    field = ThreadPoolManager()
                }
                return field
            }

        //Synchronized添加后就是线程安全的的懒汉模式
        @Synchronized
        @JvmStatic
        fun instance(): ThreadPoolManager {
            return instance!!
        }
    }

    /**
     * 复用单一线程的线程池，同步执行任务的线程池
     *
     * @param onCommonSuccessCallback
     */
    fun createSyncThreadPool(onCommonSuccessCallback: OnCommonSuccessCallback) {
        if (syncThreadPool == null) {
//            syncThreadPool = Executors.newSingleThreadExecutor()
            syncThreadPool = ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                LinkedBlockingDeque(100),
                ThreadPoolExecutor.CallerRunsPolicy()
            )
        }
        //        for (int i = 0 i < 1 i++) {
        //创建任务
        val runnable = Runnable {
            onCommonSuccessCallback.onSuccess()
        }
        // 将任务交给线程池管理
        syncThreadPool?.execute(runnable)
//        }
    }

    /**
     * 延迟执行的线程池
     *
     * @param delay
     * @param onCommonSuccessCallback
     */
    fun createScheduledThreadPool(
        delay: Long,
        onCommonSuccessCallback: OnCommonSuccessCallback
    ) {
        if (scheduledThreadPool == null) {
            scheduledThreadPool = Executors.newScheduledThreadPool(1)
        }
        //创建任务
        val runnable = Runnable {
            onCommonSuccessCallback.onSuccess()
        }
        scheduledThreadPool?.schedule(runnable, delay, TimeUnit.MILLISECONDS)
    }

    /**
     * 延迟执行的线程池
     *
     * @param delay
     * @param onCommonSuccessCallback
     */
    fun createScheduledThreadPoolToUIThread(
        delay: Long,
        onCommonSuccessCallback: OnCommonSuccessCallback
    ) {
        if (scheduledThreadPool == null) {
            scheduledThreadPool = Executors.newScheduledThreadPool(1)
        }
        //创建任务
        val runnable = Runnable {
            MainThreadManager {
                onCommonSuccessCallback.onSuccess()
            }
        }
        scheduledThreadPool?.schedule(runnable, delay, TimeUnit.MILLISECONDS)
    }

    fun shutdownScheduledThreadPool() {
        scheduledThreadPool?.shutdown()
        scheduledThreadPool = null
    }

    fun shutdownNowScheduledThreadPool() {
        scheduledThreadPool?.shutdownNow()
        scheduledThreadPool = null
    }

    fun shutdownSyncThreadPool() {
        syncThreadPool?.shutdown()
        syncThreadPool = null
    }

    fun shutdownNowSyncThreadPool() {
        syncThreadPool?.shutdownNow()
        syncThreadPool = null
    }

}