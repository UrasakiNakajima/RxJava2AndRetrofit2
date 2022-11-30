package com.phone.module_mine.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.FaceDetector;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.phone.common_library.base.BaseRxAppActivity;
import com.phone.common_library.manager.LogManager;
import com.phone.common_library.manager.ResourcesManager;
import com.phone.module_mine.R;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce : 记一次Java参数传递时（传Vector，就是线程安全的List）值发生变化问题
 */
public class ThreadPoolActivity extends BaseRxAppActivity {

    private static final String TAG = ThreadPoolActivity.class.getSimpleName();
    private Toolbar toolbar;
    private FrameLayout layoutBack;
    private ImageView imvBack;
    private TextView tevTitle;
    private TextView tevStartThreadPool;
    private TextView tevStopThreadPool;
    private TextView tevStartThreadPool2;
    private TextView tevStopThreadPool2;

    private ExecutorService excutor;
    private ExecutorService excutor2;
    private Handler handler;
    private Vector<Bitmap> vector = new Vector<>();

    private Future<?> future;
    private Future<?> future2;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_thread_pool;
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
        tevStartThreadPool = (TextView) findViewById(R.id.tev_start_thread_pool);
        tevStopThreadPool = (TextView) findViewById(R.id.tev_stop_thread_pool);
        tevStartThreadPool2 = (TextView) findViewById(R.id.tev_start_thread_pool2);
        tevStopThreadPool2 = (TextView) findViewById(R.id.tev_stop_thread_pool2);
        setToolbar(false, R.color.color_FF198CFF);
        imvBack.setColorFilter(ResourcesManager.INSTANCE.getColor(R.color.white));
        layoutBack.setOnClickListener(v -> {
            finish();
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

    /**
     * 记一次Java参数传递时值发生变化问题（传Vector，就是线程安全的List）
     */
    private void startThreadPool() {
        LogManager.INSTANCE.i(TAG, "startThreadPool currentThread name*****" + Thread.currentThread().getName());
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
                LogManager.INSTANCE.i(TAG, "startThreadPool currentThread2 name*****" + Thread.currentThread().getName());
                BitmapFactory.Options mOption = new BitmapFactory.Options();
                mOption.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.picture15, mOption);
                vector.add(mBitmap);
                LogManager.INSTANCE.i(TAG, "startThreadPool vector size*****" + vector.size());
                handler.removeCallbacksAndMessages(null);
                handler = null;
            }
        }, 150);

        excutor = Executors.newSingleThreadExecutor();
        future = excutor.submit(new Runnable() {
            @Override
            public void run() {
                LogManager.INSTANCE.i(TAG, "startThreadPool excutor currentThread name*****" + Thread.currentThread().getName());
                //1、如果vector是由原vector.clone()传来的时候，当原vector发生变化，他是不会变化的
                Vector<Bitmap> list = (Vector<Bitmap>) vector.clone();
                initFaceRecognition(list);
            }
        });
        LogManager.INSTANCE.i(TAG, "startThreadPool&&&*****");

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
            LogManager.INSTANCE.i(TAG, "initFaceRecognition i*****" + i);
            int maxFaces = 20;
            FaceDetector mFaceDetector = new FaceDetector(mBitmap.getWidth(), mBitmap.getHeight(), maxFaces);
            FaceDetector.Face[] mFace = new FaceDetector.Face[maxFaces];
            maxFaces = mFaceDetector.findFaces(mBitmap, mFace);
            LogManager.INSTANCE.i(TAG, "initFaceRecognition maxFaces*****" + maxFaces);
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
                LogManager.INSTANCE.i(TAG, "stopThreadPool shutdownNow*****");
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
        future2 = excutor2.submit(new Runnable() {
            @Override
            public void run() {
//                for (int i = 0; i < 1000000; i++) {
//                    LogManager.INSTANCE.i(TAG, "startThreadPool2*****" + i);
//                }
            }
        });
    }

    private void stopThreadPool2() {
        if (excutor2 != null && future2 != null && !excutor2.isShutdown()) {
            future2.cancel(true);
            if (!excutor2.isShutdown()) {
                excutor2.shutdownNow();
                LogManager.INSTANCE.i(TAG, "stopThreadPool2 shutdownNow*****");
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
        stopThreadPool();
        stopThreadPool2();
        super.onDestroy();
    }
}