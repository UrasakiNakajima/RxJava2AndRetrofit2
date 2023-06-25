package com.phone.module_mine.task;

import com.phone.library_common.manager.LogManager;

import java.util.concurrent.atomic.AtomicInteger;

public class FixedThreadTask implements Runnable {

    private static final String TAG = FixedThreadTask.class.getSimpleName();
    public static final int SLEEP_GAP = 1000;

    static AtomicInteger taskNo = new AtomicInteger(1);
    private final String taskName;

    public FixedThreadTask() {
        taskName = "fixedThreadTask-" + taskNo;
        taskNo.incrementAndGet();
    }

    @Override
    public void run() {
        LogManager.i(TAG, "fixedThreadTask:" + taskName + " is doing...");
        try {
            Thread.sleep(SLEEP_GAP);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LogManager.i(TAG, "fixedThreadTask:" + taskName + " end...");
    }

}
