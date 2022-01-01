package com.phone.base64_and_file;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.PrecomputedTextCompat;
import androidx.core.widget.NestedScrollView;

import com.phone.common_library.base.BaseAppActivity;
import com.phone.common_library.base.IBaseView;
import com.phone.common_library.manager.LogManager;
import com.phone.common_library.manager.ScreenManager;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import io.reactivex.disposables.Disposable;

public class Base64AndFileActivity extends BaseAppActivity implements IBaseView {

    private static final String TAG = "Base64AndFileActivity";
    private Toolbar toolbar;
    private TextView tevTitle;
    private TextView tevCompressedPicture;
    private ImageView imvCompressedPicture;
    private TextView tevPictureToBase64;
    private NestedScrollView scrollViewBase64Str;
    private FrameLayout layoutBase64Str;
    private AppCompatTextView tevBase64Str;
    private StaticLayoutView staticLayoutView;
    private TextView tevBase64ToPicture;
    private ImageView imvBase64ToPicture;
    private TextView tevResetData;
    private QMUILoadingView loadView;


    // where this is an Activity or Fragment instance
    private RxPermissions rxPermissions;
    private Base64AndFileService.TaskBinder taskBinder;
    private Disposable disposable;
    private Disposable disposable2;
    private String dirsPath;
    private String dirsPath2;

    private String compressedPicturePath;
    public String base64Str;
    public Bitmap bitmap;

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
        dirsPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "Pictures";
        dirsPath2 = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "Pictures2";
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tevTitle = (TextView) findViewById(R.id.tev_title);
        tevCompressedPicture = (TextView) findViewById(R.id.tev_compressed_picture);
        imvCompressedPicture = (ImageView) findViewById(R.id.imv_compressed_picture);
        tevPictureToBase64 = (TextView) findViewById(R.id.tev_picture_to_base64);
        scrollViewBase64Str = (NestedScrollView) findViewById(R.id.scroll_view_base64_str);
        layoutBase64Str = (FrameLayout) findViewById(R.id.layout_base64_str);
        tevBase64Str = (AppCompatTextView) findViewById(R.id.tev_base64_str);
        staticLayoutView = (StaticLayoutView) findViewById(R.id.static_layout_view);
        tevBase64ToPicture = (TextView) findViewById(R.id.tev_base64_to_picture);
        imvBase64ToPicture = (ImageView) findViewById(R.id.imv_base64_to_picture);
        tevResetData = (TextView) findViewById(R.id.tev_reset_data);
        loadView = (QMUILoadingView) findViewById(R.id.load_view);


        setToolbar(false, R.color.color_54E066FF);

        tevCompressedPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskBinder != null) {
                    showLoading();
                    taskBinder.startCompressedPictureTask(appCompatActivity, dirsPath, dirsPath2);
                }
            }
        });
        tevPictureToBase64.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskBinder != null) {
                    showLoading();
                    taskBinder.startPictureToBase64Task(appCompatActivity, compressedPicturePath, staticLayoutView);
                }
            }
        });
        tevBase64ToPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskBinder != null) {
                    showLoading();
                    taskBinder.startBase64ToPictureTask(appCompatActivity, compressedPicturePath, base64Str);
                }
            }
        });
        tevResetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetData();
            }
        });
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

    private ServiceConnection connection = new ServiceConnection() {

        /**
         * 可交互的后台服务与普通服务的不同之处，就在于这个connection建立起了两者的联系
         *
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        /**
         * onServiceConnected()方法关键，在这里实现对服务的方法的调用
         *
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            taskBinder = (Base64AndFileService.TaskBinder) service;
        }
    };

    public void showCompressedPictureSuccess(Bitmap bitmap, String compressedPicturePath) {
        this.bitmap = bitmap;
        this.compressedPicturePath = compressedPicturePath;
        tevCompressedPicture.setVisibility(View.GONE);
        imvCompressedPicture.setVisibility(View.VISIBLE);
        imvCompressedPicture.setImageBitmap(bitmap);
        hideLoading();
    }

    public void showCompressedPictureError(String error) {
        hideLoading();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    public void showPictureToBase64Success(StaticLayout staticLayout, String base64Str) {
        this.base64Str = base64Str;
        tevPictureToBase64.setVisibility(View.GONE);
        scrollViewBase64Str.setVisibility(View.VISIBLE);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            tevBase64Str.setBreakStrategy(Layout.BREAK_STRATEGY_SIMPLE);
//        }
//        tevBase64Str.setVisibility(View.VISIBLE);
//        tevBase64Str.setText(base64Str);

        staticLayoutView.setVisibility(View.VISIBLE);
//        staticLayoutView.setLayout(staticLayout);
        hideLoading();
    }

    public void showPictureToBase64Error(String error) {
        hideLoading();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    public void showBase64ToPictureSuccess(Bitmap bitmap) {
        this.bitmap = bitmap;
        tevBase64ToPicture.setVisibility(View.GONE);
        imvBase64ToPicture.setVisibility(View.VISIBLE);
        imvBase64ToPicture.setImageBitmap(bitmap);
        hideLoading();
    }

    public void showBase64ToPictureError(String error) {
        hideLoading();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        resetData();
    }

    private void resetData() {
        this.compressedPicturePath = null;
        this.base64Str = null;
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }

        tevCompressedPicture.setVisibility(View.VISIBLE);
        imvCompressedPicture.setVisibility(View.GONE);
        tevPictureToBase64.setVisibility(View.VISIBLE);
        scrollViewBase64Str.setVisibility(View.GONE);
        tevBase64ToPicture.setVisibility(View.VISIBLE);
        imvBase64ToPicture.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
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