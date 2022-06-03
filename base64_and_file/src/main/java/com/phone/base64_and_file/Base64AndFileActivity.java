package com.phone.base64_and_file;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
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
import com.phone.base64_and_file.bean.Base64AndFileBean;
import com.phone.base64_and_file.presenter.Base64AndFilePresenterImpl;
import com.phone.common_library.base.BaseMvpRxAppActivity;
import com.phone.common_library.base.IBaseView;
import com.phone.common_library.callback.OnCommonRxPermissionsCallback;
import com.phone.common_library.manager.LogManager;
import com.phone.common_library.manager.RxPermissionsManager;
import com.phone.common_library.manager.SystemManager;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class Base64AndFileActivity extends BaseMvpRxAppActivity<IBaseView, Base64AndFilePresenterImpl> implements IBase64AndFileView {

    private static final String TAG = Base64AndFileActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView tevTitle;
    private TextView tevRequestPermissions;
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
    private String dirsPath;
    private String dirsPathCompressed;
    private String dirsPathCompressedRecover;

    private List<String> base64StrList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private Base64StrAdapter base64StrAdapter;

    private Timer timer;
    private TimerTask timerTask;
    private Handler handler;

    private AlertDialog mPermissionsDialog;
    private Base64AndFileBean base64AndFileBean;

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
        dirsPathCompressed = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "PicturesCompressed";
        dirsPathCompressedRecover = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "PicturesCompressedRecover";
        base64AndFileBean = new Base64AndFileBean();
        base64AndFileBean.setDirsPath(dirsPath);
        base64AndFileBean.setDirsPathCompressed(dirsPathCompressed);
        base64AndFileBean.setDirsPathCompressedRecover(dirsPathCompressedRecover);

        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tevTitle = (TextView) findViewById(R.id.tev_title);
        tevRequestPermissions = (TextView) findViewById(R.id.tev_request_permissions);
        tevCompressedPicture = (TextView) findViewById(R.id.tev_compressed_picture);
        imvCompressedPicture = (ImageView) findViewById(R.id.imv_compressed_picture);
        tevPictureToBase64 = (TextView) findViewById(R.id.tev_picture_to_base64);
        rcvBase64Str = (RecyclerView) findViewById(R.id.rcv_base64_str);
        tevBase64ToPicture = (TextView) findViewById(R.id.tev_base64_to_picture);
        imvBase64ToPicture = (ImageView) findViewById(R.id.imv_base64_to_picture);
        tevResetData = (TextView) findViewById(R.id.tev_reset_data);
        loadView = (QMUILoadingView) findViewById(R.id.load_view);

        setToolbar(false, R.color.color_54E066FF);

        tevRequestPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogManager.i(TAG, "tevRequestPermissions");
                initRxPermissionsRxAppCompatActivity();
            }
        });
//        tevCompressedPicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initRxPermissionsRxAppCompatActivity();
//            }
//        });
//        tevPictureToBase64.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (presenter != null) {
//                    showLoading();
//                    presenter.showPictureToBase64(compressedPicturePath);
//                }
//            }
//        });
//        tevBase64ToPicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (presenter != null) {
//                    showLoading();
//                    presenter.showBase64ToPicture(compressedPicturePath, base64Str);
//                }
//            }
//        });
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
     * 請求權限，RxAppCompatActivity里需要的时候直接调用就行了
     */
    private void initRxPermissionsRxAppCompatActivity() {
        RxPermissionsManager rxPermissionsManager = new RxPermissionsManager();
        rxPermissionsManager.initRxPermissionsRxAppCompatActivity(this, new OnCommonRxPermissionsCallback() {
            @Override
            public void onRxPermissionsAllPass() {
                if (TextUtils.isEmpty(baseApplication.getSystemId())) {
                    String systemId = SystemManager.getSystemId(baseApplication);
                    baseApplication.setSystemId(systemId);
                    LogManager.i(TAG, "isEmpty systemId*****" + baseApplication.getSystemId());
                } else {
                    LogManager.i(TAG, "systemId*****" + baseApplication.getSystemId());
                }

                //第一种方法
                if (presenter != null) {
                    showLoading();

                    presenter.showCompressedPicture(baseApplication,
                            base64AndFileBean);
                }


//                //第二种方法
//                initBase64AndFileTask();
            }

            @Override
            public void onNotCheckNoMorePromptError() {
                showSystemSetupDialog();
            }

            @Override
            public void onCheckNoMorePromptError() {
                showSystemSetupDialog();
            }
        });
    }

    /**
     * 开启获取App中的图片，然后图片转bitmap，然后bitmap压缩，然后bitmap转base64，之后base64转bitmap的任务
     */
    private void initBase64AndFileTask() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                LogManager.i(TAG, "threadName*****" + Thread.currentThread().getName());
                showLoading();
                emitter.onNext(0);
            }
        }).subscribeOn(AndroidSchedulers.mainThread()) //给上面分配了UI线程
//                .observeOn(AndroidSchedulers.mainThread()) //给下面分配了UI线程
//                .doOnNext(new Consumer<Integer>() {
//                    @Override
//                    public void accept(Integer integer) throws Exception {
//                        LogManager.i(TAG, "threadName*****" + Thread.currentThread().getName());
//                        showLoading();
//                    }
//                })
                .observeOn(Schedulers.io()) //给下面分配了异步线程
                .map(new Function<Integer, Base64AndFileBean>() {
                    @Override
                    public Base64AndFileBean apply(Integer integer) throws Exception {
                        LogManager.i(TAG, "threadName2*****" + Thread.currentThread().getName());
                        File file = BitmapManager.getAssetFile(baseApplication, dirsPath, "picture_large.png");
                        base64AndFileBean.setFile(file);
                        return base64AndFileBean;
                    }
                })
                .observeOn(Schedulers.io()) //给下面分配了异步线程
                .doOnNext(new Consumer<Base64AndFileBean>() {
                    @Override
                    public void accept(Base64AndFileBean base64AndFileBean) throws Exception {
                        LogManager.i(TAG, "threadName3*****" + Thread.currentThread().getName());
                        //把图片转化成bitmap
                        Bitmap bitmap = BitmapManager.getBitmap(base64AndFileBean.getFile().getAbsolutePath());
                        base64AndFileBean.setBitmap(bitmap);
                    }
                })
                .observeOn(Schedulers.io()) //给下面分配了异步线程
                .doOnNext(new Consumer<Base64AndFileBean>() {
                    @Override
                    public void accept(Base64AndFileBean base64AndFileBean) throws Exception {
                        LogManager.i(TAG, "threadName4*****" + Thread.currentThread().getName());
                        //再压缩bitmap
                        Bitmap bitmapCompressed = BitmapManager.scaleImage(base64AndFileBean.getBitmap(), 1280, 960);
                        base64AndFileBean.setBitmapCompressed(bitmapCompressed);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) //给下面分配了异步线程
                .doOnNext(new Consumer<Base64AndFileBean>() {
                    @Override
                    public void accept(Base64AndFileBean base64AndFileBean) throws Exception {
                        LogManager.i(TAG, "threadName5*****" + Thread.currentThread().getName());
                        //展示压缩过的图片
                        tevCompressedPicture.setVisibility(View.GONE);
                        imvCompressedPicture.setVisibility(View.VISIBLE);
                        imvCompressedPicture.setImageBitmap(base64AndFileBean.getBitmapCompressed());
                    }
                })
                .observeOn(Schedulers.io()) //给下面分配了异步线程
                .doOnNext(new Consumer<Base64AndFileBean>() {
                    @Override
                    public void accept(Base64AndFileBean base64AndFileBean) throws Exception {
                        LogManager.i(TAG, "threadName6*****" + Thread.currentThread().getName());
                        //再把压缩后的bitmap保存到本地
                        File fileCompressed = BitmapManager.saveFile(base64AndFileBean.getBitmapCompressed(), base64AndFileBean.getDirsPathCompressed(), "picture_large_compressed.png");
                        base64AndFileBean.setFileCompressed(fileCompressed);
                        LogManager.i(TAG, "base64AndFileBean.getFileCompressed().getPath()*****" + base64AndFileBean.getFileCompressed().getPath());
                        LogManager.i(TAG, "base64AndFileBean.getFileCompressed().getAbsolutePath()*****" + base64AndFileBean.getFileCompressed().getAbsolutePath());
                    }
                })
                .observeOn(Schedulers.io()) //给下面分配了异步线程
                .doOnNext(new Consumer<Base64AndFileBean>() {
                    @Override
                    public void accept(Base64AndFileBean base64AndFileBean) throws Exception {
                        LogManager.i(TAG, "threadName7*****" + Thread.currentThread().getName());
                        String base64Str = null;
                        if (base64AndFileBean.getFileCompressed().exists()) {
                            base64Str = Base64AndFileManager.fileToBase64(base64AndFileBean.getFileCompressed());
//                base64Str = Base64AndFileManager.fileToBase64Test(file);
//                    base64Str = Base64AndFileManager.fileToBase64Second(file);
                            base64AndFileBean.setBase64Str(base64Str);
                        }
                        if (!TextUtils.isEmpty(base64Str)) {
                            String fileName = "base64Str.txt";
                            String txtFilePath = FileManager.writeStrToTextFile(base64Str, base64AndFileBean.getDirsPathCompressed(), fileName);
                            base64AndFileBean.setTxtFilePath(txtFilePath);
                            base64AndFileBean.setBase64StrList(Base64AndFileManager.getBase64StrList(txtFilePath));
//                base64Str = "";
//                StringBuilder stringBuilder = new StringBuilder();
//                for (int i = 0; i < base64StrList.size(); i++) {
//                    stringBuilder.append(base64StrList.get(i));
//                }
//                base64Str = stringBuilder.toString();

//                    //把字符串的最後一個打印出來，然後看看和RecyclerView顯示的最後一個字符串是否一致
//                    LogManager.i(TAG, "base64StrList******" + base64StrList.get(base64StrList.size() - 1));
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) //给下面分配了异步线程
                .doOnNext(new Consumer<Base64AndFileBean>() {
                    @Override
                    public void accept(Base64AndFileBean base64AndFileBean) throws Exception {
                        LogManager.i(TAG, "threadName8*****" + Thread.currentThread().getName());
                        tevPictureToBase64.setVisibility(View.GONE);
                        rcvBase64Str.setVisibility(View.VISIBLE);

                        base64StrAdapter.clearData();
                        base64StrAdapter.addAllData(base64AndFileBean.getBase64StrList());

                        Observable.timer(2000, TimeUnit.MILLISECONDS)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                //解决RxJava2导致的内存泄漏问题
                                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(Long aLong) throws Exception {
                                        LogManager.i(TAG, "threadNameC*****" + Thread.currentThread().getName());
                                        if (base64AndFileBean.getBase64StrList().size() > 0) {
                                            //RecyclerView自動滑動到底部，看看最後一個字符串和打印出來的字符串是否一致
                                            rcvBase64Str.scrollToPosition(base64AndFileBean.getBase64StrList().size() - 1);
                                        }
                                    }
                                });
                    }
                })
                .observeOn(Schedulers.io()) //给下面分配了异步线程
                .doOnNext(new Consumer<Base64AndFileBean>() {
                    @Override
                    public void accept(Base64AndFileBean base64AndFileBean) throws Exception {
                        LogManager.i(TAG, "threadName9*****" + Thread.currentThread().getName());
                        //再把压缩后的bitmap保存到本地
                        File fileCompressedRecover = Base64AndFileManager.base64ToFile(base64AndFileBean.getBase64Str(),
                                base64AndFileBean.getDirsPathCompressedRecover(),
                                "picture_large_compressed_recover");
                        base64AndFileBean.setFileCompressedRecover(fileCompressedRecover);
                    }
                })
                .observeOn(Schedulers.io()) //给下面分配了异步线程
//                .doOnNext(new Consumer<Base64AndFileBean>() {
//                    @Override
//                    public void accept(Base64AndFileBean base64AndFileBean) throws Exception {
//                        LogManager.i(TAG, "threadName10*****" + Thread.currentThread().getName());
//                        Bitmap bitmapCompressedRecover = BitmapFactory.decodeFile(base64AndFileBean.getFileCompressedRecover().getAbsolutePath());
//                        base64AndFileBean.setBitmapCompressedRecover(bitmapCompressedRecover);
//                    }
//                })
                .map(new Function<Base64AndFileBean, Bitmap>() {
                    @Override
                    public Bitmap apply(Base64AndFileBean base64AndFileBean) throws Exception {
                        LogManager.i(TAG, "threadName10*****" + Thread.currentThread().getName());
                        Bitmap bitmapCompressedRecover = BitmapFactory.decodeFile(base64AndFileBean.getFileCompressedRecover().getAbsolutePath());
                        return bitmapCompressedRecover;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) //给下面分配了UI线程
                //解决RxJava2导致的内存泄漏问题
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmapCompressedRecover) throws Exception {
                        LogManager.i(TAG, "threadName11*****" + Thread.currentThread().getName());
                        tevBase64ToPicture.setVisibility(View.GONE);
                        imvBase64ToPicture.setVisibility(View.VISIBLE);
                        imvBase64ToPicture.setImageBitmap(bitmapCompressedRecover);
                        base64AndFileBean.setBitmapCompressedRecover(bitmapCompressedRecover);
                        hideLoading();
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
    public void showCompressedPictureSuccess(Base64AndFileBean success) {
        base64AndFileBean = success;
        tevCompressedPicture.setVisibility(View.GONE);
        imvCompressedPicture.setVisibility(View.VISIBLE);
        imvCompressedPicture.setImageBitmap(base64AndFileBean.getBitmapCompressed());
        hideLoading();
        LogManager.i(TAG, "showCompressedPictureSuccess");

        if (presenter != null) {
            showLoading();
            presenter.showPictureToBase64(base64AndFileBean);
        }
    }

    @Override
    public void showCompressedPictureError(String error) {
        hideLoading();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPictureToBase64Success(Base64AndFileBean success) {
        base64AndFileBean = success;
        tevPictureToBase64.setVisibility(View.GONE);

        rcvBase64Str.setVisibility(View.VISIBLE);
        this.base64StrList.clear();
        this.base64StrList.addAll(base64AndFileBean.getBase64StrList());
        base64StrAdapter.clearData();
        base64StrAdapter.addAllData(this.base64StrList);
        hideLoading();
        startTimer();

        if (presenter != null) {
            showLoading();
            presenter.showBase64ToPicture(success);
        }
    }

    @Override
    public void showPictureToBase64Error(String error) {
        hideLoading();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showBase64ToPictureSuccess(Base64AndFileBean success) {
        base64AndFileBean = success;
        tevBase64ToPicture.setVisibility(View.GONE);
        imvBase64ToPicture.setVisibility(View.VISIBLE);
        imvBase64ToPicture.setImageBitmap(success.getBitmap());
        hideLoading();
    }

    @Override
    public void showBase64ToPictureError(String error) {
        hideLoading();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        resetData();
    }

    private void resetData() {
        if (base64AndFileBean.getBitmap() != null && !base64AndFileBean.getBitmap().isRecycled()) {
            base64AndFileBean.getBitmap().recycle();
            base64AndFileBean.setBitmap(null);
        }
        if (base64AndFileBean.getBitmapCompressed() != null && !base64AndFileBean.getBitmapCompressed().isRecycled()) {
            base64AndFileBean.getBitmapCompressed().recycle();
            base64AndFileBean.setBitmapCompressed(null);
        }
        if (base64AndFileBean.getBitmapCompressedRecover() != null && !base64AndFileBean.getBitmapCompressedRecover().isRecycled()) {
            base64AndFileBean.getBitmapCompressedRecover().recycle();
            base64AndFileBean.setBitmapCompressedRecover(null);
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
        stopTimer();
        if (base64AndFileBean.getBitmap() != null && !base64AndFileBean.getBitmap().isRecycled()) {
            base64AndFileBean.getBitmap().recycle();
            base64AndFileBean.setBitmap(null);
        }
        if (base64AndFileBean.getBitmapCompressed() != null && !base64AndFileBean.getBitmapCompressed().isRecycled()) {
            base64AndFileBean.getBitmapCompressed().recycle();
            base64AndFileBean.setBitmapCompressed(null);
        }
        if (base64AndFileBean.getBitmapCompressedRecover() != null && !base64AndFileBean.getBitmapCompressedRecover().isRecycled()) {
            base64AndFileBean.getBitmapCompressedRecover().recycle();
            base64AndFileBean.setBitmapCompressedRecover(null);
        }
//        if (connection != null) {
//            // 解绑服务，服务要记得解绑，不要造成内存泄漏
//            unbindService(connection);
//            binder = null;
//        }
        super.onDestroy();
    }
}