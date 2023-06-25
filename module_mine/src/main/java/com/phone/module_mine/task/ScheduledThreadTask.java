package com.phone.module_mine.task;

import com.phone.library_common.manager.LogManager;

import java.util.concurrent.atomic.AtomicInteger;

public class ScheduledThreadTask implements Runnable {

    private static final String TAG = ScheduledThreadTask.class.getSimpleName();
    public static final int SLEEP_GAP = 1000;

    static AtomicInteger taskNo = new AtomicInteger(1);
    private final String taskName;

    public ScheduledThreadTask() {
        taskName = "scheduledThreadTask-" + taskNo;
        taskNo.incrementAndGet();
    }

    @Override
    public void run() {
        LogManager.i(TAG, "scheduledThreadTask:" + taskName + " is doing...");
        try {
            Thread.sleep(SLEEP_GAP);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LogManager.i(TAG, "scheduledThreadTask:" + taskName + " end...");
    }

}
