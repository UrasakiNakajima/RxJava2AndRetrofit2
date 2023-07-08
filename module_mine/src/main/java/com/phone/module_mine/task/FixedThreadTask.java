package com.phone.module_mine.task;

import com.phone.library_base.manager.LogManager;

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
//            //这个是真正的模拟计算圆周率需要的时长（需要时间比较长的，CPU密集型操作）
//            Thread.sleep(5 * 60 * SLEEP_GAP);

            //这里只是想早点看到效果，所以减少了时长
            Thread.sleep(10 * SLEEP_GAP);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LogManager.i(TAG, "fixedThreadTask:" + taskName + " end...");
    }

}
