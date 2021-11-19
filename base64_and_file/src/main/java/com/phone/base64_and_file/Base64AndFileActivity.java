package com.phone.base64_and_file;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.phone.common_library.base.BaseAppActivity;
import com.phone.common_library.base.IBaseView;
import com.phone.common_library.manager.LogManager;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.disposables.Disposable;

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

                        thread = new MineThread();
                        thread.start();
                        LogManager.i(TAG, "Thread*******" + Thread.currentThread().getName());

//                        initTimer();
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

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

            InputStream is = null;
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
//            base64Str = Base64AndFile.fileToBase64(file);
            base64Str = Base64AndFileManager.fileToBase64Second(file);
//            base64Str = Base64AndFile.fileToBase64Third(file);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    tevBase64Str.setText(base64Str);

                    thread2 = new MineThread2();
                    thread2.start();
                }
            });

//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    private class MineThread2 extends Thread {

        @Override
        public void run() {
            super.run();
            LogManager.i(TAG, "MineThread2*******" + Thread.currentThread().getName());

            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "base64" + File.separator;
            String fileName = "pictureNew2.jpeg";
//            String fileName = "pictureNew5.jpeg";
//            File fileNew = Base64AndFile.base64ToFile(base64Str, path, fileName);
            File fileNew = Base64AndFileManager.base64ToFileSecond(base64Str, path, fileName);
//            File fileNew = Base64AndFile.base64ToFileThird(base64Str, path, fileName);


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
        super.onDestroy();
    }
}