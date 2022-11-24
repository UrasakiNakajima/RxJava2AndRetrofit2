package com.phone.common_library.manager;

import com.phone.common_library.callback.OnCommonSuccessCallback;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {

    private static final String TAG = ThreadPoolManager.class.getSimpleName();
    private ExecutorService syncThreadPool;
    private ScheduledExecutorService scheduledThreadPool;
    private static volatile ThreadPoolManager threadPoolManager;

    private ThreadPoolManager() {
    }

    public static ThreadPoolManager getInstance() {
        if (threadPoolManager == null) {
            synchronized (ThreadPoolManager.class) {
                if (threadPoolManager == null) {
                    threadPoolManager = new ThreadPoolManager();
                }
            }
        }

        return threadPoolManager;
    }

    /**
     * 复用单一线程的线程池，同步执行任务的线程池
     *
     * @param onCommonSuccessCallback
     */
    public void createSyncThreadPool(OnCommonSuccessCallback onCommonSuccessCallback) {
        if (syncThreadPool == null) {
//            syncThreadPool = Executors.newSingleThreadExecutor();
            syncThreadPool = new ThreadPoolExecutor(1,
                    1,
                    0L,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingDeque<>(100),
                    new ThreadPoolExecutor.CallerRunsPolicy());
        }
        //        for (int i = 0; i < 1; i++) {
        //创建任务
        Runnable runnable = onCommonSuccessCallback::onSuccess;
        // 将任务交给线程池管理
        syncThreadPool.execute(runnable);
//        }
    }

    /**
     * 延迟执行的线程池
     *
     * @param delay
     * @param onCommonSuccessCallback
     */
    public void createScheduledThreadPool(long delay,
                                          OnCommonSuccessCallback onCommonSuccessCallback) {
        if (scheduledThreadPool == null) {
            scheduledThreadPool = Executors.newScheduledThreadPool(1);
        }
        //创建任务
        Runnable runnable = onCommonSuccessCallback::onSuccess;
        scheduledThreadPool.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }

    public void shutdownScheduledThreadPool() {
        if (scheduledThreadPool != null) {
            scheduledThreadPool.shutdown();
            scheduledThreadPool = null;
        }
    }

    public void shutdownNowScheduledThreadPool() {
        if (scheduledThreadPool != null) {
            scheduledThreadPool.shutdownNow();
            scheduledThreadPool = null;
        }
    }

    public void shutdownSyncThreadPool() {
        if (syncThreadPool != null) {
            syncThreadPool.shutdown();
            syncThreadPool = null;
        }
    }

    public void shutdownNowSyncThreadPool() {
        if (syncThreadPool != null) {
            syncThreadPool.shutdownNow();
            syncThreadPool = null;
        }
    }


}
