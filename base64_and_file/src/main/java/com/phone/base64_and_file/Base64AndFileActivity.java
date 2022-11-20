package com.phone.base64_and_file;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.phone.base64_and_file.adapter.Base64StrAdapter;
import com.phone.base64_and_file.bean.Base64AndFileBean;
import com.phone.base64_and_file.manager.Base64AndFileManager;
import com.phone.base64_and_file.manager.BitmapManager;
import com.phone.base64_and_file.manager.FileManager;
import com.phone.base64_and_file.presenter.Base64AndFilePresenterImpl;
import com.phone.base64_and_file.view.IBase64AndFileView;
import com.phone.common_library.base.BaseMvpRxAppActivity;
import com.phone.common_library.base.IBaseView;
import com.phone.common_library.callback.OnCommonRxPermissionsCallback;
import com.phone.common_library.manager.LogManager;
import com.phone.common_library.manager.MediaFileManager;
import com.phone.common_library.manager.RxPermissionsManager;
import com.phone.common_library.manager.SystemManager;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce :
 */
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
    private Base64StrAdapter base64StrAdapter;

    private Timer timer;
    private TimerTask timerTask;
    private Handler handler;

    private AlertDialog mPermissionsDialog;
    private Base64AndFileBean base64AndFileBean;
    private final String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

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
        dirsPath = baseApplication.getExternalCacheDir()
                + File.separator + "Pictures";
        String dirsPathCompressed = baseApplication.getExternalCacheDir()
                + File.separator + "PicturesCompressed";
        String dirsPathCompressedRecover = baseApplication.getExternalCacheDir()
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
        setToolbar(false, R.color.color_FFE066FF);

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvBase64Str.setLayoutManager(linearLayoutManager);
        rcvBase64Str.setItemAnimator(new DefaultItemAnimator());

        base64StrAdapter = new Base64StrAdapter(rxAppCompatActivity);
        rcvBase64Str.setAdapter(base64StrAdapter);
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

    /**
     * 請求權限，RxAppCompatActivity里需要的时候直接调用就行了
     */
    private void initRxPermissionsRxAppCompatActivity() {
        RxPermissionsManager rxPermissionsManager = RxPermissionsManager.getInstance();
        rxPermissionsManager.initRxPermissionsRxAppCompatActivity(this, permissions, new OnCommonRxPermissionsCallback() {
            @Override
            public void onRxPermissionsAllPass() {
                //所有的权限都授予
                if (TextUtils.isEmpty(baseApplication.getSystemId())) {
                    String systemId = SystemManager.getSystemId(baseApplication);
                    baseApplication.setSystemId(systemId);
                    LogManager.i(TAG, "isEmpty systemId*****" + baseApplication.getSystemId());
                } else {
                    LogManager.i(TAG, "systemId*****" + baseApplication.getSystemId());
                }

//                //第一种方法
//                if (presenter != null) {
//                    showLoading();
//
//                    presenter.showCompressedPicture(baseApplication,
//                            base64AndFileBean);
//                }

                //第二种方法
                initBase64AndFileTask();
            }

            @Override
            public void onNotCheckNoMorePromptError() {
                //至少一个权限未授予且未勾选不再提示
                showSystemSetupDialog();
            }

            @Override
            public void onCheckNoMorePromptError() {
                //至少一个权限未授予且勾选了不再提示
                showSystemSetupDialog();
            }
        });
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
     * 开启获取App中的图片，然后图片转bitmap，然后bitmap压缩，然后bitmap转base64，之后base64转bitmap的任务
     */
    private void initBase64AndFileTask() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                LogManager.i(TAG, "threadName*****" + Thread.currentThread().getName());
                showLoading();
                emitter.onNext(0);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(AndroidSchedulers.mainThread()) //给上面分配了UI线程
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
                        LogManager.i(TAG, "bitmap mWidth*****" + bitmap.getWidth());
                        LogManager.i(TAG, "bitmap mHeight*****" + bitmap.getHeight());
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
                        LogManager.i(TAG, "bitmapCompressed mWidth*****" + bitmapCompressed.getWidth());
                        LogManager.i(TAG, "bitmapCompressed mHeight*****" + bitmapCompressed.getHeight());
                        base64AndFileBean.setBitmapCompressed(bitmapCompressed);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) //给下面分配了UI线程
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
                        MediaFileManager.MediaFileType mediaFileType = MediaFileManager.getFileType(base64AndFileBean.getFile().getAbsolutePath());
                        String mimeType = mediaFileType.mimeType;
                        String[] typeArr = mimeType.split("/");
                        String fileType = typeArr[1];
                        //再把压缩后的bitmap保存到本地
                        File fileCompressed = BitmapManager.saveFile(base64AndFileBean.getBitmapCompressed(), base64AndFileBean.getDirsPathCompressed(), "picture_large_compressed" + "." + fileType);
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
                .observeOn(AndroidSchedulers.mainThread()) //给下面分配了UI线程
                .doOnNext(new Consumer<Base64AndFileBean>() {
                    @Override
                    public void accept(Base64AndFileBean base64AndFileBean) throws Exception {
                        LogManager.i(TAG, "threadName8*****" + Thread.currentThread().getName());
                        tevPictureToBase64.setVisibility(View.GONE);
                        rcvBase64Str.setVisibility(View.VISIBLE);

                        base64StrAdapter.clearData();
                        base64StrAdapter.addData(base64AndFileBean.getBase64StrList());

                        Observable.timer(1000, TimeUnit.MILLISECONDS)
                                .subscribeOn(Schedulers.io()) //给上面分配了异步线程
                                .observeOn(AndroidSchedulers.mainThread()) //给下面分配了UI线程
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
                        MediaFileManager.MediaFileType mediaFileType = MediaFileManager.getFileType(base64AndFileBean.getFileCompressed().getAbsolutePath());
                        String mimeType = mediaFileType.mimeType;
                        String[] typeArr = mimeType.split("/");
                        String fileType = typeArr[1];
                        //再把压缩后的bitmap保存到本地
                        File fileCompressedRecover = Base64AndFileManager.base64ToFile(base64AndFileBean.getBase64Str(),
                                base64AndFileBean.getDirsPathCompressedRecover(),
                                "picture_large_compressed_recover" + "." + fileType);
                        base64AndFileBean.setFileCompressedRecover(fileCompressedRecover);
                    }
                })
                .observeOn(Schedulers.io()) //给下面分配了异步线程
                .doOnNext(new Consumer<Base64AndFileBean>() {
                    @Override
                    public void accept(Base64AndFileBean base64AndFileBean) throws Exception {
                        LogManager.i(TAG, "threadName10*****" + Thread.currentThread().getName());
                        Bitmap bitmapCompressedRecover = BitmapFactory.decodeFile(base64AndFileBean.getFileCompressedRecover().getAbsolutePath());
                        base64AndFileBean.setBitmapCompressedRecover(bitmapCompressedRecover);
                    }
                })
//                .map(new Function<Base64AndFileBean, Bitmap>() {
//                    @Override
//                    public Bitmap apply(Base64AndFileBean base64AndFileBean) throws Exception {
//                        LogManager.i(TAG, "threadName10*****" + Thread.currentThread().getName());
//                        Bitmap bitmapCompressedRecover = BitmapFactory.decodeFile(base64AndFileBean.getFileCompressedRecover().getAbsolutePath());
//                        return bitmapCompressedRecover;
//                    }
//                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Base64AndFileBean>() {
                    @Override
                    public void accept(Base64AndFileBean base64AndFileBean) throws Exception {
                        LogManager.i(TAG, "threadName11*****" + Thread.currentThread().getName());
                        tevBase64ToPicture.setVisibility(View.GONE);
                        imvBase64ToPicture.setVisibility(View.VISIBLE);
                        imvBase64ToPicture.setImageBitmap(base64AndFileBean.getBitmapCompressedRecover());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) //给下面分配了UI线程
                //解决RxJava2导致的内存泄漏问题
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Base64AndFileBean>() {
                    @Override
                    public void accept(Base64AndFileBean base64AndFileBean) throws Exception {

                        ossPutSaveFile(base64AndFileBean.getFileCompressedRecover());
                    }
                });

    }

    private void ossPutSaveFile(File file) {
        String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";

        //你的阿里云对象存储上的accessKeyId
        String accessKeyId = "xxxxxxx";
        //你的阿里云对象存储上的accessKeySecret
        String accessKeySecret = "xxxxxxx";
        //token
        String token = "xxxxxxx";
        //expiration
        String expiration = "xxxxxxx";
        //这里要使用阿里云的STS，不要自己配置（这个是模拟代码，所以这样使用的）
        OSSCredentialProvider credentialProvider = new OSSCredentialProvider() {
            @Override
            public OSSFederationToken getFederationToken() throws ClientException {
                return new OSSFederationToken(accessKeyId, accessKeySecret, token, expiration);
            }
        };


//        // 填写STS应用服务器地址。（使用这个，服务端要部署STS）
//        String stsServer = "https://example.com";
//        // 推荐使用OSSAuthCredentialsProvider。token过期可以及时更新。
//        OSSCredentialProvider credentialProvider = new OSSAuthCredentialsProvider(stsServer);


        // 配置类如果不设置，会有默认配置。
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒。
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒。
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个。
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次。
        conf.setHttpDnsEnable(true);//默认为true，表示开启DNS配置。如需关闭请将其设置为false。

        OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider);
        // 调用此方法开启日志。
        OSSLog.enableLog();

        MediaFileManager.MediaFileType mediaFileType = MediaFileManager.getFileType(file.getAbsolutePath());
        String mimeType = mediaFileType.mimeType;
        String[] typeArr = mimeType.split("/");
        String fileType = typeArr[1];
        String objectKey = "base64_and_file" + "/" + "picture_" + System.currentTimeMillis() + "." + fileType;

        // 构造上传请求。
        PutObjectRequest put = new PutObjectRequest("rx-java2-and-retrofit2-bucket", objectKey, file.getAbsolutePath());

        // 异步上传时可以设置进度回调。
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//                LogManager.i(TAG, "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                LogManager.i(TAG, "onSuccess ETag*****" + result.getETag());
                LogManager.i(TAG, "onSuccess RequestId*****" + result.getRequestId());
                LogManager.i(TAG, "onSuccess threadName*****" + Thread.currentThread().getName());


                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(0);
                        LogManager.i(TAG, "onSuccess threadName2*****" + Thread.currentThread().getName());
                    }
                })
//                        .subscribeOn(Schedulers.io()) //给上面分配了异步线程
                        .observeOn(AndroidSchedulers.mainThread()) //给下面分配了UI线程
                        //解决RxJava2导致的内存泄漏问题
                        .compose(bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                LogManager.i(TAG, "onSuccess threadName3*****" + Thread.currentThread().getName());
                                hideLoading();
                                showToast("upload success", true);
                            }
                        });
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常。
                if (clientExcepion != null) {
                    // 本地异常，如网络异常等。
                    clientExcepion.printStackTrace();
//                    ExceptionManager.getInstance().throwException(rxAppCompatActivity, clientExcepion);
                }
                if (serviceException != null) {
                    // 服务异常。
                    LogManager.i(TAG, "onFailure ErrorCode*****" + serviceException.getErrorCode());
                    LogManager.i(TAG, "onFailure RequestId*****" + serviceException.getRequestId());
                    LogManager.i(TAG, "onFailure HostId*****" + serviceException.getHostId());
                    LogManager.i(TAG, "onFailure RawMessage*****" + serviceException.getRawMessage());
                    LogManager.i(TAG, "onFailure threadName*****" + Thread.currentThread().getName());
                    LogManager.i(TAG, "onFailure serviceException*****" + serviceException.toString());
//                    ExceptionManager.getInstance().throwException(rxAppCompatActivity, clientExcepion);
                }

                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(0);
                        LogManager.i(TAG, "onFailure threadName2*****" + Thread.currentThread().getName());
                    }
                })
//                        .subscribeOn(Schedulers.io()) //给上面分配了异步线程
                        .observeOn(AndroidSchedulers.mainThread()) //给下面分配了UI线程
                        //解决RxJava2导致的内存泄漏问题
                        .compose(bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                LogManager.i(TAG, "onFailure threadName3*****" + Thread.currentThread().getName());
                                hideLoading();
                                showToast("upload error", true);
                            }
                        });
            }
        });

        // task.cancel(); // 可以取消任务。
        // task.waitUntilFinished(); // 等待上传完成。
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
                            if (base64StrAdapter.base64StrList.size() > 0) {
                                //RecyclerView自動滑動到底部，看看最後一個字符串和打印出來的字符串是否一致
                                rcvBase64Str.scrollToPosition(base64StrAdapter.base64StrList.size() - 1);
                            }
                        }
                    });
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
        base64StrAdapter.addData(success.getBase64StrList());
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
        if (base64StrAdapter.base64StrList.size() > 0) {
            rcvBase64Str.scrollToPosition(0);
            base64StrAdapter.base64StrList.clear();
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