package com.phone.base64_and_file;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.PathUtils;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.phone.common_library.base.BaseAppActivity;
import com.phone.common_library.base.IBaseView;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.manager.LogManager;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalantis.ucrop.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Base64AndFileActivity extends BaseAppActivity implements IBaseView {

    private static final String TAG = "Base64AndFileActivity";
    private Toolbar toolbar;
    private TextView tevTitle;
    private TextView tevBase64Str;
    private ImageView imvFile;
    private QMUILoadingView loadView;


    private Handler handler;
    private Thread thread;
    private Thread thread2;
    private String base64Str;

    private Timer timer;
    private TimerTask timerTask;

    // where this is an Activity or Fragment instance
    private RxPermissions rxPermissions;
    private Disposable disposable;
    private Disposable disposable2;
    private Bitmap bitmap;
    private String filePath;
    private String dirsPath2;
    private String dirsPath3;
    private String dirsPath5;

    private Base64AndFileService.TaskBinder taskBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_base64_and_file;
    }

    @Override
    protected void initData() {
        handler = new Handler(getMainLooper());

        filePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "Pictures" + File.separator + "picture2.jpeg";
        dirsPath2 = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "Pictures2";
        dirsPath3 = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "Pictures3";
        dirsPath5 = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "base64";

        File dirs2 = new File(dirsPath2);
        if (!dirs2.exists()) {
            dirs2.mkdirs();
        }
        File dirs3 = new File(dirsPath3);
        if (!dirs3.exists()) {
            dirs3.mkdirs();
        }
        File dirs5 = new File(dirsPath5);
        if (!dirs5.exists()) {
            dirs5.mkdirs();
        }
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tevTitle = (TextView) findViewById(R.id.tev_title);
        tevBase64Str = (TextView) findViewById(R.id.tev_base64_str);
        imvFile = (ImageView) findViewById(R.id.imv_file);
        loadView = (QMUILoadingView) findViewById(R.id.load_view);
        loadView.setColor(R.color.color_80000000);

        setToolbar(false, R.color.color_54E066FF);
    }

    @Override
    protected void initLoadData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initRxPermissions();
        } else {

//                        thread = new MineThread();
//                        thread.start();
//                        initTask();

//                        if (connection != null) {
//                            // 解绑服务，服务要记得解绑，不要造成内存泄漏
//                            unbindService(connection);
//                        }
            showLoading();
            Intent bindIntent = new Intent(this, Base64AndFileService.class);
            // 绑定服务和活动，之后活动就可以去调服务的方法了
            bindService(bindIntent, connection, BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

        }
    }

    private void initRxPermissions() {
        rxPermissions = new RxPermissions(this);
        disposable = rxPermissions
                .requestEach(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                        , Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
                .subscribe(permission -> { // will emit 2 Permission objects
                    if (permission.granted) {
                        // `permission.name` is granted !

                        // 用户已经同意该权限
                        LogManager.i(TAG, "用户已经同意该权限");

//                        thread = new MineThread();
//                        thread.start();
//                        initTask();

//                        if (connection != null) {
//                            // 解绑服务，服务要记得解绑，不要造成内存泄漏
//                            unbindService(connection);
//                        }
                        showLoading();
                        Intent bindIntent = new Intent(this, Base64AndFileService.class);
                        // 绑定服务和活动，之后活动就可以去调服务的方法了
                        bindService(bindIntent, connection, BIND_AUTO_CREATE);

                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // Denied permission without ask never again

                        // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                        LogManager.i(TAG, "用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框");
                    } else {
                        // Denied permission with ask never again
                        // Need to go to the settings

                        // 用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限
                        LogManager.i(TAG, "用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限");
                    }
                });
    }

//    private void initTask() {
//        showLoading();
//        disposable2 = Observable.timer(1000, TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .doOnNext(new Consumer<Long>() {
////                    @Override
////                    public void accept(Long aLong) throws Exception {
////                        LogManager.i(TAG, "MineThread*******" + Thread.currentThread().getName());
////
////                    }
////                })
////                .observeOn(Schedulers.io())
//                .doOnNext(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        LogManager.i(TAG, "MineThread2*******" + Thread.currentThread().getName());
//
//                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
//                                + "Pictures" + File.separator
//                                + "picture2.jpeg";
////            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
////                    + "Pictures" + File.separator
////                    + "picture5.jpeg";
//                        File file = new File(path);
////            base64Str = Base64AndFileManager.fileToBase64(file);
//                        base64Str = Base64AndFileManager.fileToBase64Second(file);
////            base64Str = Base64AndFileManager.fileToBase64Third(file);
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        LogManager.i(TAG, "MineThread3*******" + Thread.currentThread().getName());
//
//                        tevBase64Str.setText(base64Str);
//                    }
//                })
//                .observeOn(Schedulers.io())
//                .doOnNext(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        LogManager.i(TAG, "MineThread5*******" + Thread.currentThread().getName());
//
//                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "base64" + File.separator;
//                        String fileName = "pictureNew2.jpeg";
////            String fileName = "pictureNew5.jpeg";
////            File fileNew = Base64AndFileManager.base64ToFile(base64Str, path, fileName);
//                        File fileNew = Base64AndFileManager.base64ToFileSecond(base64Str, path, fileName);
////            File fileNew = Base64AndFileManager.base64ToFileThird(base64Str, path, fileName);
//
//                        bitmap = BitmapFactory.decodeFile(fileNew.getAbsolutePath());
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        LogManager.i(TAG, "MineThread6*******" + Thread.currentThread().getName());
//
//                        imvFile.setImageBitmap(bitmap);
//                        hideLoading();
//                    }
//                });
//    }

    private ServiceConnection connection = new ServiceConnection() {

        /**
         * 可交互的后台服务与普通服务的不同之处，就在于这个connection建立起了两者的联系
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        /**
         * onServiceConnected()方法关键，在这里实现对服务的方法的调用
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            taskBinder = (Base64AndFileService.TaskBinder) service;
            taskBinder.getService().setOnCommonSingleParamCallback(
                    new OnCommonSingleParamCallback<String>() {
                        @Override
                        public void onSuccess(String success) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tevBase64Str.setText(success);

                                    taskBinder.startTask2(dirsPath5);
                                }
                            });
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
            taskBinder.getService().setOnCommonSingleParamCallback2(
                    new OnCommonSingleParamCallback<Bitmap>() {
                        @Override
                        public void onSuccess(Bitmap success) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    bitmap = success;
                                    imvFile.setImageBitmap(success);

//                                    Glide.with(Base64AndFileActivity.this)
//                                            .load(success).into(imvFile);
                                    hideLoading();
                                }
                            });
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
//            downloadBinder.getProgress();

            taskBinder.startTask(filePath, dirsPath2, dirsPath3);
        }
    };

    @Override
    public void showLoading() {
        if (loadView != null && !loadView.isShown()) {
            loadView.setVisibility(View.VISIBLE);
            loadView.start();
        }
    }

    @Override
    public void hideLoading() {
        if (loadView != null && loadView.isShown()) {
            loadView.stop();
            loadView.setVisibility(View.GONE);
        }
    }

    private void initTimer() {
        showLoading();
        stopTimer();
        if (timer == null) {
            timer = new Timer();
        }

        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    thread = new MineThread();
                    thread.start();
                }
            };
        }

        if (timer != null && timerTask != null) {
            timer.schedule(timerTask, 1000);
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    private class MineThread extends Thread {

        @Override
        public void run() {
            super.run();
            LogManager.i(TAG, "MineThread*******" + Thread.currentThread().getName());

//            InputStream is = null;
//            try {
            //得到资源中的asset数据流
//                is = getResources().getAssets().open("picture2.jpeg");
//                base64Str = Base64AndFile.fileToBase64(is);
//                base64Str = Base64AndFile.fileToBase64Second(is);

            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                    + "Pictures" + File.separator
                    + "picture2.jpeg";
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
//                    + "Pictures" + File.separator
//                    + "picture5.jpeg";
            File file = new File(path);
            File result = initCompressorIO(file.getAbsolutePath());

            //            base64Str = Base64AndFileManager.fileToBase64(file);
            base64Str = Base64AndFileManager.fileToBase64Second(result);
//            base64Str = Base64AndFileManager.fileToBase64Third(file);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    tevBase64Str.setText(base64Str);

                    thread2 = new MineThread2();
                    thread2.start();
                }
            });


//            // 使用文件 File 获取 Compress 实例
//            Compress.with(Base64AndFileActivity.this, file)
//                    // 指定要求的图片的质量
//                    .setQuality(20)
//                    // 指定文件的输出目录（如果返回结果不是 File 的会，无效）
//                    .setTargetDir(dirsPath)
//                    // 指定压缩结果回调（如哦返回结果不是 File 则不会被回调到）
//                    .setCompressListener(new CompressListener() {
//                        @Override
//                        public void onStart() {
//                            LogManager.i(TAG, "onStart*****");
//                        }
//
//                        @Override
//                        public void onSuccess(File result) {
//                            LogManager.i(TAG, "result*****" + result.getAbsolutePath());
//
//
//                        }
//
//                        @Override
//                        public void onError(Throwable throwable) {
//
//                            LogManager.i(TAG, "throwable*****" + throwable.getMessage());
//                        }
//                    });

//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    /**
     * 使用Compressor IO模式自定义压缩
     *
     * @param path .setMaxWidth(640).setMaxHeight(480)这两个数值越高，压缩力度越小，图片也不清晰
     *             .setQuality(75)这个方法只是设置图片质量，并不影响压缩图片的大小KB
     *             .setCompressFormat(Bitmap.CompressFormat.WEBP) WEBP图片格式是Google推出的 压缩强，质量 高，但是IOS不识别，需要把图片转为字节流然后转PNG格式
     *             .setCompressFormat(Bitmap.CompressFormat.PNG)PNG格式的压缩，会导致图片变大，并耗过大的内 存，手机反应缓慢
     *             .setCompressFormat(Bitmap.CompressFormat.JPEG)JPEG压缩；压缩速度比PNG快，质量一般，基本上属于1/10的压缩比例
     */
    private File initCompressorIO(String path) {
        try {
            String dirsPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                    + "Pictures2";
            File dirs = new File(dirsPath);
            if (!dirs.exists()) {
                dirs.mkdirs();
            }

            File file = new Compressor(Base64AndFileActivity.this)
//                    .setMaxWidth(640)
//                    .setMaxHeight(480)
                    .setQuality(25)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .setDestinationDirectoryPath(dirs.getAbsolutePath())
                    .compressToFile(new File(path));

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private class MineThread2 extends Thread {

        @Override
        public void run() {
            super.run();
            LogManager.i(TAG, "MineThread2*******" + Thread.currentThread().getName());

            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "base64" + File.separator;
            String fileName = "pictureNew2.jpeg";
//            String fileName = "pictureNew5.jpeg";
//            File fileNew = Base64AndFileManager.base64ToFile(base64Str, path, fileName);
            File fileNew = Base64AndFileManager.base64ToFileSecond(base64Str, path, fileName);
//            File fileNew = Base64AndFileManager.base64ToFileThird(base64Str, path, fileName);


            Bitmap bitmap = BitmapFactory.decodeFile(fileNew.getAbsolutePath());

            handler.post(new Runnable() {
                @Override
                public void run() {
                    imvFile.setImageBitmap(bitmap);
                    stopTimer();
                    hideLoading();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        stopTimer();
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
        if (thread2 != null && thread2.isAlive()) {
            thread2.interrupt();
        }
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        if (disposable2 != null && !disposable2.isDisposed()) {
            disposable2.dispose();
        }
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        if (connection != null) {
            // 解绑服务，服务要记得解绑，不要造成内存泄漏
            unbindService(connection);
        }
        super.onDestroy();
    }
}