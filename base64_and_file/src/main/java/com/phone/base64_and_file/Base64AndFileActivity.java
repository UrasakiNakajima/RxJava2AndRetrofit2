package com.phone.base64_and_file;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.phone.common_library.base.BaseAppActivity;
import com.phone.common_library.manager.LogManager;
import com.tbruyelle.rxpermissions3.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.rxjava3.disposables.Disposable;

public class Base64AndFileActivity extends BaseAppActivity {

    private static final String TAG = "Base64AndFileActivity";
    private TextView tevBase64Str;
    private ImageView imvFile;

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
    protected int initLayoutId() {
        return R.layout.activity_base64_and_file;
    }

    @Override
    protected void initData() {
        handler = new Handler(getMainLooper());
    }

    @Override
    protected void initViews() {
        tevBase64Str = (TextView) findViewById(R.id.tev_base64_str);
        imvFile = (ImageView) findViewById(R.id.imv_file);
    }

    @Override
    protected void initLoadData() {


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
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
    }

    private void initTimer() {
        if (timer == null) {
            timer = new Timer();
        }

        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    thread2 = new MineThread2();
                    thread2.start();
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
            InputStream is = null;
            try {
                //得到资源中的asset数据流
                is = getResources().getAssets().open("picture3.jpeg");
                base64Str = Base64AndFile.fileToBase64(is);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tevBase64Str.setText(base64Str);

                        thread2 = new MineThread2();
                        thread2.start();
//                        initTimer();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class MineThread2 extends Thread {

        @Override
        public void run() {
            super.run();

            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "base64" + File.separator;
            String fileName = "pictureNew3.jpeg";
            File fileNew = Base64AndFile.base64ToFile(base64Str, path, fileName);
            Bitmap bitmap = BitmapFactory.decodeFile(fileNew.getAbsolutePath());

            handler.post(new Runnable() {
                @Override
                public void run() {
                    imvFile.setImageBitmap(bitmap);
//                    stopTimer();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
//        stopTimer();

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