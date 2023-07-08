package com.phone.module_mine.task;

import com.phone.library_base.manager.LogManager;

import java.util.concurrent.atomic.AtomicInteger;

public class SingleThreadTask implements Runnable {

    private static final String TAG = SingleThreadTask.class.getSimpleName();
    public static final int SLEEP_GAP = 1000;

    static AtomicInteger taskNo = new AtomicInteger(1);
    private final String taskName;

    public SingleThreadTask() {
        taskName = "singleThreadTask-" + taskNo;
        taskNo.incrementAndGet();
    }

    @Override
    public void run() {
        LogManager.i(TAG, "singleThreadTask:" + taskName + " is doing...");
        try {
            Thread.sleep(SLEEP_GAP);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LogManager.i(TAG, "singleThreadTask:" + taskName + " end...");
    }

}
