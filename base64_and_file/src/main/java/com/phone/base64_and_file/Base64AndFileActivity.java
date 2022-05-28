package com.phone.base64_and_file;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phone.base64_and_file.adapter.Base64StrAdapter;
import com.phone.base64_and_file.presenter.Base64AndFilePresenterImpl;
import com.phone.common_library.base.BaseMvpRxAppActivity;
import com.phone.common_library.base.IBaseView;
import com.phone.common_library.callback.OnCommonRxPermissionsCallback;
import com.phone.common_library.manager.LogManager;
import com.phone.common_library.manager.RxPermissionsManager;
import com.qmuiteam.qmui.widget.QMUILoadingView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Base64AndFileActivity extends BaseMvpRxAppActivity<IBaseView, Base64AndFilePresenterImpl> implements IBase64AndFileView {

    private static final String TAG = Base64AndFileActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView tevTitle;
    private TextView tevCompressedPicture;
    private ImageView imvCompressedPicture;
    private TextView tevPictureToBase64;
    private RecyclerView rcvBase64Str;
    private TextView tevBase64ToPicture;
    private ImageView imvBase64ToPicture;
    private TextView tevResetData;
    private QMUILoadingView loadView;


    // where this is an Activity or Fragment instance
    //    private Binder binder;
//    private Disposable disposable;
//    private Disposable disposable2;
    private String dirsPath;
    private String dirsPath2;

    private String compressedPicturePath;
    public String base64Str;
    //    private Bitmap bitmapNew;
    public Bitmap bitmap;

    private List<String> base64StrList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private Base64StrAdapter base64StrAdapter;

    private Timer timer;
    private TimerTask timerTask;
    private Handler handler;

    private AlertDialog mPermissionsDialog;

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

        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tevTitle = (TextView) findViewById(R.id.tev_title);
        tevCompressedPicture = (TextView) findViewById(R.id.tev_compressed_picture);
        imvCompressedPicture = (ImageView) findViewById(R.id.imv_compressed_picture);
        tevPictureToBase64 = (TextView) findViewById(R.id.tev_picture_to_base64);
        rcvBase64Str = (RecyclerView) findViewById(R.id.rcv_base64_str);
        tevBase64ToPicture = (TextView) findViewById(R.id.tev_base64_to_picture);
        imvBase64ToPicture = (ImageView) findViewById(R.id.imv_base64_to_picture);
        tevResetData = (TextView) findViewById(R.id.tev_reset_data);
        loadView = (QMUILoadingView) findViewById(R.id.load_view);
        setToolbar(false, R.color.color_54E066FF);


        tevCompressedPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRxPermissionsRxAppCompatActivity();
            }
        });
        tevPictureToBase64.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    showLoading();
                    presenter.showPictureToBase64(compressedPicturePath);
                }
            }
        });
        tevBase64ToPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    showLoading();
                    presenter.showBase64ToPicture(compressedPicturePath, base64Str);
                }
            }
        });
        tevResetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetData();
            }
        });

        initAdapter();
    }

    private void initAdapter() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvBase64Str.setLayoutManager(linearLayoutManager);
        rcvBase64Str.setItemAnimator(new DefaultItemAnimator());

        base64StrAdapter = new Base64StrAdapter(this);
        rcvBase64Str.setAdapter(base64StrAdapter);
        base64StrAdapter.clearData();
        base64StrAdapter.addAllData(base64StrList);
    }

    @Override
    protected void initLoadData() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            initRxPermissions();
//        } else {
//
////            Intent bindIntent = new Intent(this, Base64AndFileService.class);
////            // 绑定服务和活动，之后活动就可以去调服务的方法了
////            bindService(bindIntent, connection, BIND_AUTO_CREATE);
//        }


    }

    @Override
    protected Base64AndFilePresenterImpl attachPresenter() {
        return new Base64AndFilePresenterImpl(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 207) {
//            initRxPermissions();
        }
    }

    private void showSystemSetupDialog() {
        cancelPermissionsDialog();
        if (mPermissionsDialog == null) {
            mPermissionsDialog = new AlertDialog.Builder(this)
                    .setTitle("权限设置")
                    .setMessage("获取相关权限失败，将导致部分功能无法正常使用，请到设置页面手动授权")
                    .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionsDialog();
                            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, 207);
                        }
                    })
                    .create();
        }

        mPermissionsDialog.setCancelable(false);
        mPermissionsDialog.setCanceledOnTouchOutside(false);
        mPermissionsDialog.show();
    }

    /**
     * 关闭对话框
     */
    private void cancelPermissionsDialog() {
        if (mPermissionsDialog != null) {
            mPermissionsDialog.cancel();
            mPermissionsDialog = null;
        }
    }

    /**
     * RxAppCompatActivity里需要的时候直接调用就行了
     */
    private void initRxPermissionsRxAppCompatActivity() {
        RxPermissionsManager rxPermissionsManager = RxPermissionsManager.getInstance();
        rxPermissionsManager.initRxPermissionsRxAppCompatActivity(this, new OnCommonRxPermissionsCallback() {
            @Override
            public void onRxPermissionsAllPass() {
                if (presenter != null) {
                    showLoading();

                    presenter.showCompressedPicture(rxAppCompatActivity.getApplicationContext(),
                            dirsPath, dirsPath2);
                }
            }

            @Override
            public void onNotCheckNoMorePromptError() {
//                showSystemSetupDialog();
            }

            @Override
            public void onCheckNoMorePromptError() {
                showSystemSetupDialog();
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
////                    + "picture5.webp";
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

//    private ServiceConnection connection = new ServiceConnection() {
//
//        /**
//         * 可交互的后台服务与普通服务的不同之处，就在于这个connection建立起了两者的联系
//         *
//         * @param name
//         */
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//        }
//
//        /**
//         * onServiceConnected()方法关键，在这里实现对服务的方法的调用
//         *
//         * @param name
//         * @param service
//         */
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            taskBinder = (TaskBinder) service;
//        }
//    };

//    private Base64AndFileServiceConnection connection = new Base64AndFileServiceConnection(new OnCommonSingleParamCallback<Binder>() {
//        @Override
//        public void onSuccess(Binder success) {
//            binder = success;
//        }
//
//        @Override
//        public void onError(String error) {
//
//        }
//    });

    private void startTimer() {
        stopTimer();
        if (timer == null) {
            timer = new Timer();
        }

        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (base64StrList.size() > 0) {
                                //RecyclerView自動滑動到底部，看看最後一個字符串和打印出來的字符串是否一致
                                rcvBase64Str.scrollToPosition(base64StrList.size() - 1);
                            }
                        }
                    });
                }
            };
        }

        if (timer != null && timerTask != null) {
            timer.schedule(timerTask, 500);
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

    @Override
    public void showCompressedPictureSuccess(Bitmap bitmap, String compressedPicturePath) {
        this.bitmap = bitmap;
        this.compressedPicturePath = compressedPicturePath;
        tevCompressedPicture.setVisibility(View.GONE);
        imvCompressedPicture.setVisibility(View.VISIBLE);
        imvCompressedPicture.setImageBitmap(bitmap);
        hideLoading();
        LogManager.i(TAG, "showCompressedPictureSuccess");

        if (presenter != null) {
            showLoading();
            presenter.showPictureToBase64(compressedPicturePath);
        }
    }

    @Override
    public void showCompressedPictureError(String error) {
        hideLoading();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPictureToBase64Success(List<String> base64StrList, String base64Str) {
        this.base64Str = base64Str;
        tevPictureToBase64.setVisibility(View.GONE);

        rcvBase64Str.setVisibility(View.VISIBLE);
        this.base64StrList.clear();
        this.base64StrList.addAll(base64StrList);
        base64StrAdapter.clearData();
        base64StrAdapter.addAllData(this.base64StrList);
        hideLoading();
        startTimer();

        if (presenter != null) {
            showLoading();
            presenter.showBase64ToPicture(compressedPicturePath, base64Str);
        }
    }

    @Override
    public void showPictureToBase64Error(String error) {
        hideLoading();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showBase64ToPictureSuccess(Bitmap bitmap) {
        this.bitmap = bitmap;
        tevBase64ToPicture.setVisibility(View.GONE);
        imvBase64ToPicture.setVisibility(View.VISIBLE);
        imvBase64ToPicture.setImageBitmap(bitmap);
        hideLoading();
    }

    @Override
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
        if (base64StrList.size() > 0) {
            rcvBase64Str.scrollToPosition(0);
            base64StrList.clear();
            base64StrAdapter.clearData();
        }
        rcvBase64Str.setVisibility(View.GONE);
        tevBase64ToPicture.setVisibility(View.VISIBLE);
        imvBase64ToPicture.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
//        if (disposable != null && !disposable.isDisposed()) {
//            disposable.dispose();
//        }

//        if (disposable2 != null && !disposable2.isDisposed()) {
//            disposable2.dispose();
//        }
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        stopTimer();
//        if (connection != null) {
//            // 解绑服务，服务要记得解绑，不要造成内存泄漏
//            unbindService(connection);
//            binder = null;
//        }
        super.onDestroy();
    }
}