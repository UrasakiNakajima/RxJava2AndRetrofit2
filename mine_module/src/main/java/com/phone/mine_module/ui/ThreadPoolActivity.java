package com.phone.mine_module.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.FaceDetector;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.phone.common_library.base.BaseRxAppActivity;
import com.phone.common_library.manager.LogManager;
import com.phone.mine_module.R;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 记一次Java参数传递时（传Vector，就是线程安全的List）值发生变化问题
 */
public class ThreadPoolActivity extends BaseRxAppActivity {

    private static final String TAG = ThreadPoolActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView tevTitle;
    private TextView tevStartThreadPool;
    private TextView tevStopThreadPool;
    private TextView tevStartThreadPool2;
    private TextView tevStopThreadPool2;

//    private ExecutorService excutor0;
    private ExecutorService excutor;
    private ExecutorService excutor2;
    //    private FutureTask<String> futureTask;
    private Handler handler;
    private Vector<Bitmap> vector = new Vector<>();


//    private Future<?> future0;
    private Future<?> future;
    private Future<?> future2;
//    private MineAsyncTask mineAsyncTask;


    @Override
    protected int initLayoutId() {
        return R.layout.activity_thread_pool;
    }

    @Override
    protected void initData() {
//        futureTask = new FutureTask<String>(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                LogManager.i(TAG, "startThreadPool*****");
//                for (int i = 0; i < 5; i++) {
//                    BitmapFactory.Options mOption = new BitmapFactory.Options();
//                    mOption.inPreferredConfig = Bitmap.Config.RGB_565;
//                    Bitmap mBitmap = null;
//                    if (i == 0){
//                        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.picture, mOption);
//                    } else if(i == 1){
//                        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.picture2, mOption);
//                    } else if(i == 2){
//                        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.picture3, mOption);
//                    } else if(i == 3){
//                        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.picture5, mOption);
//                    } else {
//                        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.picture8, mOption);
//                    }
//                    vector.add(mBitmap);
//                }
//                initFaceRecognition(vector);
//
//                Timer timer = new Timer();
//                TimerTask timerTask = new TimerTask() {
//                    @Override
//                    public void run() {
//                        BitmapFactory.Options mOption = new BitmapFactory.Options();
//                        mOption.inPreferredConfig = Bitmap.Config.RGB_565;
//                        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.picture3, mOption);
//                        vector.add(mBitmap);
//                        LogManager.i(TAG, "startThreadPool vector*****" + vector.size());
//                    }
//                };
//                timer.schedule(timerTask, 300);
//                return "";
//            }
//        });


//        mineAsyncTask = new MineAsyncTask();
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tevTitle = (TextView) findViewById(R.id.tev_title);
        tevStartThreadPool = (TextView) findViewById(R.id.tev_start_thread_pool);
        tevStopThreadPool = (TextView) findViewById(R.id.tev_stop_thread_pool);
        tevStartThreadPool2 = (TextView) findViewById(R.id.tev_start_thread_pool2);
        tevStopThreadPool2 = (TextView) findViewById(R.id.tev_stop_thread_pool2);
        setToolbar(false, R.color.color_FFE066FF);

        tevStartThreadPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                excutor0 = Executors.newSingleThreadExecutor();
//                future0 = excutor0.submit(new Runnable() {
//                    @Override
//                    public void run() {
//                    }
//                });
                startThreadPool();
            }
        });
        tevStopThreadPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopThreadPool();
            }
        });
        tevStartThreadPool2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startThreadPool2();
            }
        });
        tevStopThreadPool2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopThreadPool2();
            }
        });
    }

//    private class MineAsyncTask extends AsyncTask<String, Integer, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//
//            return "";
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//
//        }
//    }

    /**
     * 记一次Java参数传递时值发生变化问题（传Vector，就是线程安全的List）
     */
    private void startThreadPool() {
        LogManager.i(TAG, "startThreadPool*****");
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
                mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.picture7, mOption);
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
//                //1、vector是原vector传来的时候，当原vector发生变化，他还是会变化的
//                initFaceRecognition(vector);

                //2、如果vector是由原vector.clone()传来的时候，当原vector发生变化，他是不会变化的
                Vector<Bitmap> list = (Vector<Bitmap>) vector.clone();
                initFaceRecognition(list);
            }
        });
        LogManager.i(TAG, "startThreadPool&&&*****");

//        mineAsyncTask.execute();

//        futureTask.run();
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
            LogManager.i(TAG, "initFaceRecognition vector size*****" + vector.size());
            int maxFaces = 20;
            FaceDetector mFaceDetector = new FaceDetector(mBitmap.getWidth(), mBitmap.getHeight(), maxFaces);
            FaceDetector.Face[] mFace = new FaceDetector.Face[maxFaces];
            maxFaces = mFaceDetector.findFaces(mBitmap, mFace);
            LogManager.i(TAG, "initFaceRecognition maxFaces*****" + maxFaces);
        }
    }

//    private void stopThreadPool0() {
//        if (excutor0 != null && future0 != null && !excutor0.isShutdown()) {
//            future0.cancel(true);
//            if (!excutor0.isShutdown()) {
//                excutor0.shutdownNow();
//                LogManager.i(TAG, "stopThreadPool0 shutdownNow*****");
//            }
//        }
//    }

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

//        if (mineAsyncTask != null && !mineAsyncTask.isCancelled()) {
//            mineAsyncTask.cancel(true);
//            LogManager.i(TAG, "stopThreadPool shutdownNow*****");
//        }


//        if (futureTask != null && !futureTask.isCancelled()) {
//            futureTask.cancel(true);
//            LogManager.i(TAG, "stopThreadPool cancel*****");
//        }
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
//                    LogManager.i(TAG, "startThreadPool2*****" + i);
//                }
            }
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
//        stopThreadPool0();
        stopThreadPool();
        stopThreadPool2();
        super.onDestroy();
    }
}