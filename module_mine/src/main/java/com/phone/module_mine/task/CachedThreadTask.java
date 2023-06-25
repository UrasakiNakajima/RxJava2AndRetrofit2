package com.phone.module_mine.task;

import com.phone.library_common.manager.LogManager;

import java.util.concurrent.atomic.AtomicInteger;

public class CachedThreadTask implements Runnable {

    private static final String TAG = CachedThreadTask.class.getSimpleName();
    public static final int SLEEP_GAP = 1000;

    static AtomicInteger taskNo = new AtomicInteger(1);
    private final String taskName;

    public CachedThreadTask() {
        taskName = "cachedThreadTask-" + taskNo;
        taskNo.incrementAndGet();
    }

    @Override
    public void run() {
        LogManager.i(TAG, "cachedThreadTask:" + taskName + " is doing...");
        try {
            Thread.sleep(SLEEP_GAP);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LogManager.i(TAG, "cachedThreadTask:" + taskName + " end...");
    }

}
