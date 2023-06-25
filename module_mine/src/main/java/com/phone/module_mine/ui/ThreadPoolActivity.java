package com.phone.module_mine.ui;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.FaceDetector;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.phone.library_common.base.BaseRxAppActivity;
import com.phone.library_common.common.ConstantData;
import com.phone.library_common.manager.LogManager;
import com.phone.library_common.manager.ResourcesManager;
import com.phone.module_mine.R;
import com.phone.module_mine.task.CachedThreadTask;
import com.phone.module_mine.task.FixedThreadTask;
import com.phone.module_mine.task.ScheduledThreadTask;
import com.phone.module_mine.task.SingleThreadTask;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce : 多种线程池的使用
 */

@Route(path = ConstantData.Route.ROUTE_THREAD_POOL)
public class ThreadPoolActivity extends BaseRxAppActivity {

    private static final String TAG = ThreadPoolActivity.class.getSimpleName();
    private Toolbar toolbar;
    private FrameLayout layoutBack;
    private ImageView imvBack;
    private TextView tevTitle;
    private TextView tevStartSingleThreadExecutor;
    private TextView tevStartFixedThreadPool;
    private TextView tevStartCachedThreadPool;
    private TextView tevStartScheduledThreadPool;
    private TextView tevStartCustomThreadPool;
    private TextView tevStartThreadPool;
    private TextView tevStopThreadPool;
    private TextView tevStartThreadPool2;
    private TextView tevStopThreadPool2;


    private ExecutorService singleThreadExecutor;
    private ExecutorService fixedThreadPool;
    private ExecutorService cachedThreadPool;
    private ScheduledExecutorService scheduledThreadPool;
    private ExecutorService customThreadPool;

    private ExecutorService excutor;
    private ExecutorService excutor2;
    private Handler handler;
    private final Vector<Bitmap> vector = new Vector<>();

    private Future<?> future;
    private Future<?> future2;

    @Override
    protected int initLayoutId() {
        return R.layout.mine_activity_thread_pool;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        layoutBack = (FrameLayout) findViewById(R.id.layout_back);
        imvBack = (ImageView) findViewById(R.id.imv_back);
        tevTitle = (TextView) findViewById(R.id.tev_title);
        tevStartSingleThreadExecutor = (TextView) findViewById(R.id.tev_start_single_thread_executor);
        tevStartFixedThreadPool = (TextView) findViewById(R.id.tev_start_fixed_thread_pool);
        tevStartCachedThreadPool = (TextView) findViewById(R.id.tev_start_cached_thread_pool);
        tevStartScheduledThreadPool = (TextView) findViewById(R.id.tev_start_scheduled_thread_pool);
        tevStartCustomThreadPool = (TextView) findViewById(R.id.tev_start_custom_thread_pool);
        tevStartThreadPool = (TextView) findViewById(R.id.tev_start_thread_pool);
        tevStopThreadPool = (TextView) findViewById(R.id.tev_stop_thread_pool);
        tevStartThreadPool2 = (TextView) findViewById(R.id.tev_start_thread_pool2);
        tevStopThreadPool2 = (TextView) findViewById(R.id.tev_stop_thread_pool2);

        setToolbar(false, R.color.color_FF198CFF);
        imvBack.setColorFilter(ResourcesManager.getColor(R.color.white));
        layoutBack.setOnClickListener(v -> {
            finish();
        });

        tevStartSingleThreadExecutor.setOnClickListener(v -> {
            startSingleThreadExecutor();
        });
        tevStartFixedThreadPool.setOnClickListener(v -> {
            startFixedThreadPool();
        });
        tevStartCachedThreadPool.setOnClickListener(v -> {
            startCachedThreadPool();
        });
        tevStartScheduledThreadPool.setOnClickListener(v -> {
            startScheduledThreadPool();
        });
        tevStartCustomThreadPool.setOnClickListener(v -> {
            startCustomThreadPool();
        });


        tevStartThreadPool.setOnClickListener(v -> {
            startThreadPool();
        });
        tevStopThreadPool.setOnClickListener(v -> {
            stopThreadPool();
        });
        tevStartThreadPool2.setOnClickListener(v -> {
            startThreadPool2();
        });
        tevStopThreadPool2.setOnClickListener(v -> {
            stopThreadPool2();
        });

    }

    private void startSingleThreadExecutor() {
        //只有一个核心线程，没有非核心线程，线程队列是int最大值
        //线程池中的任务是按照提交的次序顺序执行的
        singleThreadExecutor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            singleThreadExecutor.execute(new SingleThreadTask());
        }
        //shutdown函数修改线程池状态为SHUTDOWN
        //不再接收新提交的任务
        //中断线程池中空闲的线程
        //第③步只是中断了空闲的线程，但正在执行的任务以及线程池任务队列中的任务会继续执行完毕
        singleThreadExecutor.shutdown();
    }

    private void startFixedThreadPool() {
        //CPU密集型线程池：只有几个自定义的核心线程，没有非核心线程，线程队列是int最大值
        fixedThreadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            fixedThreadPool.execute(new FixedThreadTask());
        }
        //shutdown函数修改线程池状态为SHUTDOWN
        //不再接收新提交的任务
        //中断线程池中空闲的线程
        //第③步只是中断了空闲的线程，但正在执行的任务以及线程池任务队列中的任务会继续执行完毕
        fixedThreadPool.shutdown();
    }

    private void startCachedThreadPool() {
        //IO密集型线程池：只有非核心线程，非核心线程最多可以达到int的最大值，非核心线程的最大空闲时间是60s，
        //没有核心线程，线程队列特点
        //（1）内部容量是0
        //（2）每次删除操作都要等待插入操作
        //（3）每次插入操作都要等待删除操作
        //（4）一个元素，一旦有了插入线程和移除线程，那么很快由插入线程移交给移除线程，这个容器相当于通道，本身不存储元素
        //（5）在多任务队列，是最快的处理任务方式。
        cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            cachedThreadPool.execute(new CachedThreadTask());
        }
        //shutdown函数修改线程池状态为SHUTDOWN
        //不再接收新提交的任务
        //中断线程池中空闲的线程
        //第③步只是中断了空闲的线程，但正在执行的任务以及线程池任务队列中的任务会继续执行完毕
        cachedThreadPool.shutdown();
    }

    private void startScheduledThreadPool() {
        //核心线程是自定义的，最大线程数可以达到int的最大值，非核心线程的最大空闲时间是0.01s，
        //线程队列是最大是int最大值，使用优先级队列DelayedWorkQueue，保证添加到队列中的任务，
        //会按照任务的延时时间进行排序，延时时间少的任务首先被获取。
        scheduledThreadPool = Executors.newScheduledThreadPool(5);
        for (int i = 0; i < 10; i++) {
            scheduledThreadPool.scheduleAtFixedRate(new ScheduledThreadTask(), 0, 500, MILLISECONDS);
        }
        //shutdown函数修改线程池状态为SHUTDOWN
        //不再接收新提交的任务
        //中断线程池中空闲的线程
        //第③步只是中断了空闲的线程，但正在执行的任务以及线程池任务队列中的任务会继续执行完毕
        scheduledThreadPool.shutdown();
    }

    private void startCustomThreadPool() {
        //自定义线程池
        customThreadPool = new ThreadPoolExecutor(5, 10,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
        for (int i = 0; i < 30; i++) {
            customThreadPool.execute(new FixedThreadTask());
        }
        //shutdown函数修改线程池状态为SHUTDOWN
        //不再接收新提交的任务
        //中断线程池中空闲的线程
        //第③步只是中断了空闲的线程，但正在执行的任务以及线程池任务队列中的任务会继续执行完毕
        customThreadPool.shutdown();
    }


    /**
     * 记一次Java参数传递时值发生变化问题（传Vector，就是线程安全的List）
     */
    private void startThreadPool() {
        LogManager.i(TAG, "startThreadPool currentThread name*****" + Thread.currentThread().getName());
        vector.clear();
        for (int i = 0; i < 6; i++) {
            BitmapFactory.Options mOption = new BitmapFactory.Options();
            mOption.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap mBitmap = null;
            if (i == 0) {
                mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.picture3, mOption);
            } else if (i == 1) {
                mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.picture6, mOption);
            } else if (i == 2) {
                mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.picture8, mOption);
            } else if (i == 3) {
                mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.picture14, mOption);
            } else if (i == 4) {
                mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.picture16, mOption);
            } else {
                mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.picture18, mOption);
            }
            vector.add(mBitmap);
        }

        handler = new Handler();
        //这是为了在检测人脸过程中改变原vector的数据
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LogManager.i(TAG, "startThreadPool currentThread2 name*****" + Thread.currentThread().getName());
                BitmapFactory.Options mOption = new BitmapFactory.Options();
                mOption.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.picture15, mOption);
                vector.add(mBitmap);
                LogManager.i(TAG, "startThreadPool vector size*****" + vector.size());
                handler.removeCallbacksAndMessages(null);
                handler = null;
            }
        }, 150);

        excutor = Executors.newSingleThreadExecutor();
        future = excutor.submit(new Runnable() {
            @Override
            public void run() {
                LogManager.i(TAG, "startThreadPool excutor currentThread name*****" + Thread.currentThread().getName());
                //1、如果vector是由原vector.clone()传来的时候，当原vector发生变化，他是不会变化的
                Vector<Bitmap> list = (Vector<Bitmap>) vector.clone();
                initFaceRecognition(list);
            }
        });
        LogManager.i(TAG, "startThreadPool&&&*****");

    }

    /**
     * 人脸个数识别方法
     *
     * @param vector 1、vector是final的，按说不会变化，可是vector是原vector传来的时候，当原vector发生变化，他还是会变化的
     *               2、如果vector是由原vector.clone()传来的时候，当原vector发生变化，他是不会变化的
     */
    private void initFaceRecognition(final Vector<Bitmap> vector) {
        for (int i = 0; i < vector.size(); i++) {
            Bitmap mBitmap = vector.get(i);
            LogManager.i(TAG, "initFaceRecognition i*****" + i);
            int maxFaces = 20;
            FaceDetector mFaceDetector = new FaceDetector(mBitmap.getWidth(), mBitmap.getHeight(), maxFaces);
            FaceDetector.Face[] mFace = new FaceDetector.Face[maxFaces];
            maxFaces = mFaceDetector.findFaces(mBitmap, mFace);
            LogManager.i(TAG, "initFaceRecognition maxFaces*****" + maxFaces);
        }
    }

    /**
     * （1）修改线程池状态为STOP
     * （2）不再接收任务提交
     * （3）尝试中断线程池中所有的线程（包括正在执行的线程）
     * （4）返回正在等待执行的任务列表 List<Runnable>
     * 此时线程池中等待队列中的任务不会被执行，正在执行的任务也可能被终止（为什么是可能呢？因为如果正常执行的任务
     * 如果不响应中断，那么就不会被终止，直到任务执行完毕）
     * PS：线程池中的正在执行的任务一般是停不下来的
     */
    private void stopThreadPool() {
        if (excutor != null && future != null && !excutor.isShutdown()) {
            future.cancel(true);
            if (!excutor.isShutdown()) {
                excutor.shutdownNow();
                LogManager.i(TAG, "stopThreadPool shutdownNow*****");
            }
        }
    }

    /**
     * （1）修改线程池状态为SHUTDOWN
     * （2）不再接收新提交的任务
     * （3）中断线程池中空闲的线程
     * 第（3）步只是中断了空闲的线程，但正在执行的任务以及线程池任务队列中的任务会继续执行完毕
     */
    private void startThreadPool2() {
        excutor2 = Executors.newSingleThreadExecutor();
        future2 = excutor2.submit(() -> {
//                for (int i = 0; i < 1000000; i++) {
//                    LogManager.i(TAG, "startThreadPool2*****" + i);
//                }
        });
    }

    private void stopThreadPool2() {
        if (excutor2 != null && future2 != null && !excutor2.isShutdown()) {
            future2.cancel(true);
            if (!excutor2.isShutdown()) {
                excutor2.shutdownNow();
                LogManager.i(TAG, "stopThreadPool2 shutdownNow*****");
            }
        }
    }

    @Override
    protected void initLoadData() {

    }

    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (singleThreadExecutor != null && singleThreadExecutor.isShutdown()) {
            //shutdownNow函数修改线程池状态为STOP
            //不再接收任务提交
            //尝试中断线程池中所有的线程（包括正在执行的线程）
            //返回正在等待执行的任务列表 List<Runnable>
            singleThreadExecutor.shutdownNow();
        }
        if (fixedThreadPool != null && fixedThreadPool.isShutdown()) {
            //shutdownNow函数修改线程池状态为STOP
            //不再接收任务提交
            //尝试中断线程池中所有的线程（包括正在执行的线程）
            //返回正在等待执行的任务列表 List<Runnable>
            fixedThreadPool.shutdownNow();
        }
        if (cachedThreadPool != null && cachedThreadPool.isShutdown()) {
            //shutdownNow函数修改线程池状态为STOP
            //不再接收任务提交
            //尝试中断线程池中所有的线程（包括正在执行的线程）
            //返回正在等待执行的任务列表 List<Runnable>
            cachedThreadPool.shutdownNow();
        }
        if (scheduledThreadPool != null && scheduledThreadPool.isShutdown()) {
            //shutdownNow函数修改线程池状态为STOP
            //不再接收任务提交
            //尝试中断线程池中所有的线程（包括正在执行的线程）
            //返回正在等待执行的任务列表 List<Runnable>
            scheduledThreadPool.shutdownNow();
        }
        if (customThreadPool != null && customThreadPool.isShutdown()) {
            //shutdownNow函数修改线程池状态为STOP
            //不再接收任务提交
            //尝试中断线程池中所有的线程（包括正在执行的线程）
            //返回正在等待执行的任务列表 List<Runnable>
            customThreadPool.shutdownNow();
        }
        stopThreadPool();
        stopThreadPool2();
        super.onDestroy();
    }
}