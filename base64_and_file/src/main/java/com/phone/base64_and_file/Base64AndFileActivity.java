package com.phone.base64_and_file;

import android.Manifest;
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
import java.util.ArrayList;
import java.util.List;
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
////            // ?????????????????????????????????????????????????????????????????????
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
                    .setTitle("????????????")
                    .setMessage("???????????????????????????????????????????????????????????????????????????????????????????????????")
                    .setPositiveButton("?????????", new DialogInterface.OnClickListener() {
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
     * ???????????????
     */
    private void cancelPermissionsDialog() {
        if (mPermissionsDialog != null) {
            mPermissionsDialog.cancel();
            mPermissionsDialog = null;
        }
    }

    /**
     * ???????????????RxAppCompatActivity???????????????????????????????????????
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

//                //???????????????
//                if (presenter != null) {
//                    showLoading();
//
//                    presenter.showCompressedPicture(baseApplication,
//                            base64AndFileBean);
//                }


                        //???????????????
                        initBase64AndFileTask();
                    }

                    @Override
                    public void onNotCheckNoMorePromptError() {
                        showSystemSetupDialog();
                    }

                    @Override
                    public void onCheckNoMorePromptError() {
                        showSystemSetupDialog();
                    }
                }, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE);
    }

    /**
     * ????????????App??????????????????????????????bitmap?????????bitmap???????????????bitmap???base64?????????base64???bitmap?????????
     */
    private void initBase64AndFileTask() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                LogManager.i(TAG, "threadName*****" + Thread.currentThread().getName());
                showLoading();
                emitter.onNext(0);
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(AndroidSchedulers.mainThread()) //??????????????????UI??????
//                .observeOn(AndroidSchedulers.mainThread()) //??????????????????UI??????
//                .doOnNext(new Consumer<Integer>() {
//                    @Override
//                    public void accept(Integer integer) throws Exception {
//                        LogManager.i(TAG, "threadName*****" + Thread.currentThread().getName());
//                        showLoading();
//                    }
//                })
                .observeOn(Schedulers.io()) //??????????????????????????????
                .map(new Function<Integer, Base64AndFileBean>() {
                    @Override
                    public Base64AndFileBean apply(Integer integer) throws Exception {
                        LogManager.i(TAG, "threadName2*****" + Thread.currentThread().getName());
                        File file = BitmapManager.getAssetFile(baseApplication, dirsPath, "picture_large.png");
                        base64AndFileBean.setFile(file);
                        return base64AndFileBean;
                    }
                })
                .observeOn(Schedulers.io()) //??????????????????????????????
                .doOnNext(new Consumer<Base64AndFileBean>() {
                    @Override
                    public void accept(Base64AndFileBean base64AndFileBean) throws Exception {
                        LogManager.i(TAG, "threadName3*****" + Thread.currentThread().getName());
                        //??????????????????bitmap
                        Bitmap bitmap = BitmapManager.getBitmap(base64AndFileBean.getFile().getAbsolutePath());
                        LogManager.i(TAG, "bitmap mWidth*****" + bitmap.getWidth());
                        LogManager.i(TAG, "bitmap mHeight*****" + bitmap.getHeight());
                        base64AndFileBean.setBitmap(bitmap);
                    }
                })
                .observeOn(Schedulers.io()) //??????????????????????????????
                .doOnNext(new Consumer<Base64AndFileBean>() {
                    @Override
                    public void accept(Base64AndFileBean base64AndFileBean) throws Exception {
                        LogManager.i(TAG, "threadName4*****" + Thread.currentThread().getName());
                        //?????????bitmap
                        Bitmap bitmapCompressed = BitmapManager.scaleImage(base64AndFileBean.getBitmap(), 1280, 960);
                        LogManager.i(TAG, "bitmapCompressed mWidth*****" + bitmapCompressed.getWidth());
                        LogManager.i(TAG, "bitmapCompressed mHeight*****" + bitmapCompressed.getHeight());
                        base64AndFileBean.setBitmapCompressed(bitmapCompressed);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) //??????????????????UI??????
                .doOnNext(new Consumer<Base64AndFileBean>() {
                    @Override
                    public void accept(Base64AndFileBean base64AndFileBean) throws Exception {
                        LogManager.i(TAG, "threadName5*****" + Thread.currentThread().getName());
                        //????????????????????????
                        tevCompressedPicture.setVisibility(View.GONE);
                        imvCompressedPicture.setVisibility(View.VISIBLE);
                        imvCompressedPicture.setImageBitmap(base64AndFileBean.getBitmapCompressed());
                    }
                })
                .observeOn(Schedulers.io()) //??????????????????????????????
                .doOnNext(new Consumer<Base64AndFileBean>() {
                    @Override
                    public void accept(Base64AndFileBean base64AndFileBean) throws Exception {
                        LogManager.i(TAG, "threadName6*****" + Thread.currentThread().getName());
                        MediaFileManager.MediaFileType mediaFileType = MediaFileManager.getFileType(base64AndFileBean.getFile().getAbsolutePath());
                        String mimeType = mediaFileType.mimeType;
                        String[] typeArr = mimeType.split("/");
                        String fileType = typeArr[1];
                        //??????????????????bitmap???????????????
                        File fileCompressed = BitmapManager.saveFile(base64AndFileBean.getBitmapCompressed(), base64AndFileBean.getDirsPathCompressed(), "picture_large_compressed" + "." + fileType);
                        base64AndFileBean.setFileCompressed(fileCompressed);
                        LogManager.i(TAG, "base64AndFileBean.getFileCompressed().getPath()*****" + base64AndFileBean.getFileCompressed().getPath());
                        LogManager.i(TAG, "base64AndFileBean.getFileCompressed().getAbsolutePath()*****" + base64AndFileBean.getFileCompressed().getAbsolutePath());
                    }
                })
                .observeOn(Schedulers.io()) //??????????????????????????????
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

//                    //?????????????????????????????????????????????????????????RecyclerView??????????????????????????????????????????
//                    LogManager.i(TAG, "base64StrList******" + base64StrList.get(base64StrList.size() - 1));
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()) //??????????????????UI??????
                .doOnNext(new Consumer<Base64AndFileBean>() {
                    @Override
                    public void accept(Base64AndFileBean base64AndFileBean) throws Exception {
                        LogManager.i(TAG, "threadName8*****" + Thread.currentThread().getName());
                        tevPictureToBase64.setVisibility(View.GONE);
                        rcvBase64Str.setVisibility(View.VISIBLE);

                        base64StrAdapter.clearData();
                        base64StrAdapter.addAllData(base64AndFileBean.getBase64StrList());

                        Observable.timer(1000, TimeUnit.MILLISECONDS)
                                .subscribeOn(Schedulers.io()) //??????????????????????????????
                                .observeOn(AndroidSchedulers.mainThread()) //??????????????????UI??????
                                //??????RxJava2???????????????????????????
                                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(Long aLong) throws Exception {
                                        LogManager.i(TAG, "threadNameC*****" + Thread.currentThread().getName());
                                        if (base64AndFileBean.getBase64StrList().size() > 0) {
                                            //RecyclerView??????????????????????????????????????????????????????????????????????????????????????????
                                            rcvBase64Str.scrollToPosition(base64AndFileBean.getBase64StrList().size() - 1);
                                        }
                                    }
                                });
                    }
                })
                .observeOn(Schedulers.io()) //??????????????????????????????
                .doOnNext(new Consumer<Base64AndFileBean>() {
                    @Override
                    public void accept(Base64AndFileBean base64AndFileBean) throws Exception {
                        LogManager.i(TAG, "threadName9*****" + Thread.currentThread().getName());
                        MediaFileManager.MediaFileType mediaFileType = MediaFileManager.getFileType(base64AndFileBean.getFileCompressed().getAbsolutePath());
                        String mimeType = mediaFileType.mimeType;
                        String[] typeArr = mimeType.split("/");
                        String fileType = typeArr[1];
                        //??????????????????bitmap???????????????
                        File fileCompressedRecover = Base64AndFileManager.base64ToFile(base64AndFileBean.getBase64Str(),
                                base64AndFileBean.getDirsPathCompressedRecover(),
                                "picture_large_compressed_recover" + "." + fileType);
                        base64AndFileBean.setFileCompressedRecover(fileCompressedRecover);
                    }
                })
                .observeOn(Schedulers.io()) //??????????????????????????????
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
                .observeOn(AndroidSchedulers.mainThread()) //??????????????????UI??????
                //??????RxJava2???????????????????????????
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

        //?????????????????????????????????accessKeyId
        String accessKeyId = "xxxxxxx";
        //?????????????????????????????????accessKeySecret
        String accessKeySecret = "xxxxxxx";
        //token
        String token = "xxxxxxx";
        //expiration
        String expiration = "xxxxxxx";
        //???????????????????????????STS????????????????????????????????????????????????????????????????????????
        OSSCredentialProvider credentialProvider = new OSSCredentialProvider() {
            @Override
            public OSSFederationToken getFederationToken() throws ClientException {
                return new OSSFederationToken(accessKeyId, accessKeySecret, token, expiration);
            }
        };


//        // ??????STS????????????????????????????????????????????????????????????STS???
//        String stsServer = "https://example.com";
//        // ????????????OSSAuthCredentialsProvider???token???????????????????????????
//        OSSCredentialProvider credentialProvider = new OSSAuthCredentialsProvider(stsServer);


        // ????????????????????????????????????????????????
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // ?????????????????????15??????
        conf.setSocketTimeout(15 * 1000); // socket???????????????15??????
        conf.setMaxConcurrentRequest(5); // ??????????????????????????????5??????
        conf.setMaxErrorRetry(2); // ????????????????????????????????????2??????
        conf.setHttpDnsEnable(true);//?????????true???????????????DNS???????????????????????????????????????false???

        OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider);
        // ??????????????????????????????
        OSSLog.enableLog();

        MediaFileManager.MediaFileType mediaFileType = MediaFileManager.getFileType(file.getAbsolutePath());
        String mimeType = mediaFileType.mimeType;
        String[] typeArr = mimeType.split("/");
        String fileType = typeArr[1];
        String objectKey = "base64_and_file" + "/" + "picture_" + System.currentTimeMillis() + "." + fileType;

        // ?????????????????????
        PutObjectRequest put = new PutObjectRequest("rx-java2-and-retrofit2-bucket", objectKey, file.getAbsolutePath());

        // ??????????????????????????????????????????
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
//                        .subscribeOn(Schedulers.io()) //??????????????????????????????
                        .observeOn(AndroidSchedulers.mainThread()) //??????????????????UI??????
                        //??????RxJava2???????????????????????????
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
                // ???????????????
                if (clientExcepion != null) {
                    // ????????????????????????????????????
                    clientExcepion.printStackTrace();
//                    ExceptionManager.getInstance().throwException(rxAppCompatActivity, clientExcepion);
                }
                if (serviceException != null) {
                    // ???????????????
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
//                        .subscribeOn(Schedulers.io()) //??????????????????????????????
                        .observeOn(AndroidSchedulers.mainThread()) //??????????????????UI??????
                        //??????RxJava2???????????????????????????
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

        // task.cancel(); // ?????????????????????
        // task.waitUntilFinished(); // ?????????????????????
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
//         * ????????????????????????????????????????????????????????????????????????connection???????????????????????????
//         *
//         * @param name
//         */
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//        }
//
//        /**
//         * onServiceConnected()?????????????????????????????????????????????????????????
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
                                //RecyclerView??????????????????????????????????????????????????????????????????????????????????????????
                                rcvBase64Str.scrollToPosition(base64StrList.size() - 1);
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
//            // ???????????????????????????????????????????????????????????????
//            unbindService(connection);
//            binder = null;
//        }
        super.onDestroy();
    }
}