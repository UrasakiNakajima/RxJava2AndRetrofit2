package com.phone.common_library.manager;

import com.phone.common_library.callback.OnCommonSuccessCallback;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {

    private static final String TAG = ThreadPoolManager.class.getSimpleName();
    private ExecutorService multiplexSingleThreadPool;
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
     * 复用单一线程池，同步执行任务的线程池
     * @param keepAliveTime
     * @param onCommonSuccessCallback
     */
    public void multiplexSyncThreadPool(long keepAliveTime,
                                    OnCommonSuccessCallback onCommonSuccessCallback) {
        if (multiplexSingleThreadPool == null) {
            multiplexSingleThreadPool = new ThreadPoolExecutor(1,
                    1,
                    keepAliveTime,
                    TimeUnit.SECONDS,
                    new LinkedBlockingDeque<>(2),
                    new ThreadPoolExecutor.CallerRunsPolicy());
        }
        //        for (int i = 0; i < 1; i++) {
        //创建任务
        Runnable runnable = onCommonSuccessCallback::onSuccess;
        // 将任务交给线程池管理
        multiplexSingleThreadPool.execute(runnable);
//        }
    }

    public void shutdown() {
        if (multiplexSingleThreadPool != null) {
            multiplexSingleThreadPool.shutdown();
            multiplexSingleThreadPool = null;
        }
    }

    public void shutdownNow() {
        if (multiplexSingleThreadPool != null) {
            multiplexSingleThreadPool.shutdownNow();
            multiplexSingleThreadPool = null;
        }
    }


}
