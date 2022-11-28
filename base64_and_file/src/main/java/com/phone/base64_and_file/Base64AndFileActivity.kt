package com.phone.base64_and_file

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.sdk.android.oss.*
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.OSSLog
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.phone.base64_and_file.adapter.Base64StrAdapter
import com.phone.base64_and_file.bean.Base64AndFileBean
import com.phone.base64_and_file.manager.Base64AndFileManager
import com.phone.base64_and_file.manager.BitmapManager
import com.phone.base64_and_file.manager.FileManager
import com.phone.base64_and_file.presenter.Base64AndFilePresenterImpl
import com.phone.base64_and_file.view.IBase64AndFileView
import com.phone.common_library.BaseApplication
import com.phone.common_library.base.BaseMvpRxAppActivity
import com.phone.common_library.base.IBaseView
import com.phone.common_library.callback.OnCommonRxPermissionsCallback
import com.phone.common_library.manager.LogManager
import com.phone.common_library.manager.MediaFileManager
import com.phone.common_library.manager.ResourcesManager
import com.phone.common_library.manager.RxPermissionsManager
import com.phone.common_library.manager.SystemManager.getSystemId
import com.trello.rxlifecycle3.android.ActivityEvent
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

class Base64AndFileActivity :
    BaseMvpRxAppActivity<IBaseView, Base64AndFilePresenterImpl>(), IBase64AndFileView {

    private val TAG = Base64AndFileActivity::class.java.simpleName
    private var toolbar: Toolbar? = null
    private var layoutBack: FrameLayout? = null
    private var imvBack: ImageView? = null
    private var tevTitle: TextView? = null
    private var tevRequestPermissions: TextView? = null
    private var tevCompressedPicture: TextView? = null
    private var imvCompressedPicture: ImageView? = null
    private var tevPictureToBase64: TextView? = null
    private var rcvBase64Str: RecyclerView? = null
    private var tevBase64ToPicture: TextView? = null
    private var imvBase64ToPicture: ImageView? = null
    private var tevResetData: TextView? = null

    // where this is an Activity or Fragment instance
    //    private Binder binder;
    private lateinit var dirsPath: String
    private var dirsPathCompressed: String? = null
    private var dirsPathCompressedRecover: String? = null
    private var base64StrAdapter: Base64StrAdapter? = null

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private var handler: Handler? = null

    private var mPermissionsDialog: AlertDialog? = null
    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE
    )

    override fun initLayoutId() = R.layout.activity_base64_and_file

    override fun initData() {
        handler = Handler(Looper.getMainLooper())
        dirsPath = mBaseApplication?.externalCacheDir
            .toString() + File.separator + "Pictures"
        dirsPathCompressed = mBaseApplication?.externalCacheDir
            .toString() + File.separator + "PicturesCompressed"
        dirsPathCompressedRecover = mBaseApplication?.externalCacheDir
            .toString() + File.separator + "PicturesCompressedRecover"
    }

    override fun initViews() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        layoutBack = findViewById<View>(R.id.layout_back) as FrameLayout
        imvBack = findViewById<View>(R.id.imv_back) as ImageView
        tevTitle = findViewById<View>(R.id.tev_title) as TextView
        tevRequestPermissions = findViewById<View>(R.id.tev_request_permissions) as TextView
        tevCompressedPicture = findViewById<View>(R.id.tev_compressed_picture) as TextView
        imvCompressedPicture = findViewById<View>(R.id.imv_compressed_picture) as ImageView
        tevPictureToBase64 = findViewById<View>(R.id.tev_picture_to_base64) as TextView
        rcvBase64Str = findViewById<View>(R.id.rcv_base64_str) as RecyclerView
        tevBase64ToPicture = findViewById<View>(R.id.tev_base64_to_picture) as TextView
        imvBase64ToPicture = findViewById<View>(R.id.imv_base64_to_picture) as ImageView
        tevResetData = findViewById<View>(R.id.tev_reset_data) as TextView

        setToolbar(false, R.color.color_FF198CFF)
        imvBack?.setColorFilter(ResourcesManager.getColor(R.color.white))
        layoutBack?.setOnClickListener { v: View? -> finish() }
        tevRequestPermissions?.setOnClickListener {
            LogManager.i(TAG, "tevRequestPermissions")
            initRxPermissions()
        }
        //        tevCompressedPicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initRxPermissions();
//            }
//        });
//        tevPictureToBase64?.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getPresenter() != null) {
//                    showLoading();
//                    getPresenter().showPictureToBase64(compressedPicturePath);
//                }
//            }
//        });
//        tevBase64ToPicture?.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getPresenter() != null) {
//                    showLoading();
//                    getPresenter().showBase64ToPicture(compressedPicturePath, base64Str);
//                }
//            }
//        });
        tevResetData?.setOnClickListener { resetData() }
        initAdapter()
    }

    private fun initAdapter() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        rcvBase64Str?.layoutManager = linearLayoutManager
        rcvBase64Str?.itemAnimator = DefaultItemAnimator()
        base64StrAdapter = Base64StrAdapter(rxAppCompatActivity)
        rcvBase64Str?.adapter = base64StrAdapter
    }

    override fun initLoadData() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            initRxPermissions();
//        } else {
//
////            Intent bindIntent = new Intent(this, Base64AndFileService.class);
////            // 绑定服务和活动，之后活动就可以去调服务的方法了
////            bindService(bindIntent, connection, BIND_AUTO_CREATE);
//        }
    }

    override fun attachPresenter() = Base64AndFilePresenterImpl(this)

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 207) {
//            initRxPermissions();
        }
    }

    /**
     * 請求權限，RxAppCompatActivity里需要的时候直接调用就行了
     */
    private fun initRxPermissions() {
        val rxPermissionsManager = RxPermissionsManager.get()
        rxPermissionsManager.initRxPermissions(
            this,
            permissions,
            object : OnCommonRxPermissionsCallback {
                override fun onRxPermissionsAllPass() {
                    //所有的权限都授予
                    if (TextUtils.isEmpty(mBaseApplication?.getSystemId())) {
                        val systemId = getSystemId()
                        mBaseApplication?.setSystemId(systemId)
                        LogManager.i(
                            TAG,
                            "isEmpty systemId*****" + mBaseApplication?.getSystemId()
                        )
                    } else {
                        LogManager.i(TAG, "systemId*****" + mBaseApplication?.getSystemId())
                    }

//                //第一种方法
//                if (getPresenter() != null) {
//                    showLoading();
//
//                    Base64AndFileBean base64AndFileBean = new Base64AndFileBean();
//                    base64AndFileBean.setDirsPath(dirsPath);
//                    base64AndFileBean.setDirsPathCompressed(dirsPathCompressed);
//                    base64AndFileBean.setDirsPathCompressedRecover(dirsPathCompressedRecover);
//                    getPresenter().showCompressedPicture(getMBaseApplication(),
//                            base64AndFileBean);
//                }

                    //第二种方法
                    initBase64AndFileTask()
                }

                override fun onNotCheckNoMorePromptError() {
                    //至少一个权限未授予且未勾选不再提示
                    showSystemSetupDialog()
                }

                override fun onCheckNoMorePromptError() {
                    //至少一个权限未授予且勾选了不再提示
                    showSystemSetupDialog()
                }
            })
    }

    private fun showSystemSetupDialog() {
        cancelPermissionsDialog()
        if (mPermissionsDialog == null) {
            mPermissionsDialog = AlertDialog.Builder(this)
                .setTitle("权限设置")
                .setMessage("获取相关权限失败，将导致部分功能无法正常使用，请到设置页面手动授权")
                .setPositiveButton("去授权") { dialog, which ->
                    cancelPermissionsDialog()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", BaseApplication.get()?.packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, 207)
                }
                .create()
        }
        mPermissionsDialog?.setCancelable(false)
        mPermissionsDialog?.setCanceledOnTouchOutside(false)
        mPermissionsDialog?.show()
    }

    /**
     * 关闭对话框
     */
    private fun cancelPermissionsDialog() {
        mPermissionsDialog?.cancel()
        mPermissionsDialog = null
    }

    /**
     * 开启获取App中的图片，然后图片转bitmap，然后bitmap压缩，然后bitmap转base64，之后base64转bitmap的任务
     */
    private fun initBase64AndFileTask() {
        Flowable.create<Int>({ emitter ->
            LogManager.i(TAG, "threadName*****" + Thread.currentThread().name)
            showLoading()
            emitter.onNext(0)
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
            .map(object : Function<Int, Base64AndFileBean> {
                @Throws(Exception::class)
                override fun apply(t: Int): Base64AndFileBean {
                    LogManager.i(TAG, "threadName2*****" + Thread.currentThread().name)
                    val file =
                        mBaseApplication?.let {
                            BitmapManager.getAssetFile(
                                it,
                                dirsPath,
                                "picture_large.webp"
                            )
                        }
                    val base64AndFileBean = Base64AndFileBean()
                    base64AndFileBean.dirsPath = dirsPath
                    base64AndFileBean.dirsPathCompressed = dirsPathCompressed
                    base64AndFileBean.dirsPathCompressedRecover = dirsPathCompressedRecover
                    base64AndFileBean.file = file
                    return base64AndFileBean
                }

            })
            .observeOn(Schedulers.io()) //给下面分配了异步线程
            .doOnNext { base64AndFileBean ->
                LogManager.i(TAG, "threadName3*****" + Thread.currentThread().name)
                //把图片转化成bitmap
                val bitmap = BitmapManager.getBitmap(base64AndFileBean.file.absolutePath)
                LogManager.i(TAG, "bitmap mWidth*****" + bitmap?.width)
                LogManager.i(TAG, "bitmap mHeight*****" + bitmap?.height)
                base64AndFileBean.bitmap = bitmap
            }
            .observeOn(Schedulers.io()) //给下面分配了异步线程
            .doOnNext { base64AndFileBean ->
                LogManager.i(TAG, "threadName4*****" + Thread.currentThread().name)
                //再压缩bitmap
                val bitmapCompressed =
                    BitmapManager.scaleImage(base64AndFileBean.bitmap, 1280, 960)
                LogManager.i(TAG, "bitmapCompressed mWidth*****" + bitmapCompressed?.width)
                LogManager.i(TAG, "bitmapCompressed mHeight*****" + bitmapCompressed?.height)
                base64AndFileBean.bitmapCompressed = bitmapCompressed
            }
            .observeOn(AndroidSchedulers.mainThread()) //给下面分配了UI线程
            .doOnNext { base64AndFileBean ->
                LogManager.i(TAG, "threadName5*****" + Thread.currentThread().name)
                //展示压缩过的图片
                tevCompressedPicture?.visibility = View.GONE
                imvCompressedPicture?.visibility = View.VISIBLE
                imvCompressedPicture?.setImageBitmap(base64AndFileBean.bitmapCompressed)
            }
            .observeOn(Schedulers.io()) //给下面分配了异步线程
            .doOnNext { base64AndFileBean ->
                LogManager.i(TAG, "threadName6*****" + Thread.currentThread().name)
                val mediaFileType =
                    MediaFileManager.getFileType(base64AndFileBean.file.absolutePath)
                val mimeType = mediaFileType.mimeType
                val typeArr = mimeType.split("/").toTypedArray()
                val fileType = typeArr[1]
                //再把压缩后的bitmap保存到本地
                val fileCompressed = BitmapManager.saveFile(
                    base64AndFileBean.bitmapCompressed, base64AndFileBean.dirsPathCompressed,
                    "picture_large_compressed.$fileType"
                )
                base64AndFileBean.fileCompressed = fileCompressed
                LogManager.i(
                    TAG,
                    "base64AndFileBean.getFileCompressed().getPath()*****" + base64AndFileBean.fileCompressed.path
                )
                LogManager.i(
                    TAG,
                    "base64AndFileBean.getFileCompressed().getAbsolutePath()*****" + base64AndFileBean.fileCompressed.absolutePath
                )
            }
            .observeOn(Schedulers.io()) //给下面分配了异步线程
            .doOnNext { base64AndFileBean ->
                LogManager.i(TAG, "threadName7*****" + Thread.currentThread().name)
                base64AndFileBean.fileCompressed.let {
                    if (it.exists()) {
                        val base64Str = Base64AndFileManager.fileToBase64(it)
                        //                base64Str = Base64AndFileManager.fileToBase64Test(file);
//                    base64Str = Base64AndFileManager.fileToBase64Second(file);
                        base64AndFileBean.setBase64Str(base64Str)
                        val fileName = "base64Str.txt"
                        val txtFilePath = FileManager.writeStrToTextFile(
                            base64Str,
                            base64AndFileBean.dirsPathCompressed,
                            fileName
                        )
                        base64AndFileBean.txtFilePath = txtFilePath
                        base64AndFileBean.base64StrList =
                            Base64AndFileManager.getBase64StrList(txtFilePath)
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
            }
            .observeOn(AndroidSchedulers.mainThread()) //给下面分配了UI线程
            .doOnNext { base64AndFileBean ->
                LogManager.i(TAG, "threadName8*****" + Thread.currentThread().name)
                tevPictureToBase64?.visibility = View.GONE
                rcvBase64Str?.visibility = View.VISIBLE
                base64StrAdapter?.clearData()
                base64StrAdapter?.addData(base64AndFileBean.base64StrList)
                Observable.timer(1000, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io()) //给上面分配了异步线程
                    .observeOn(AndroidSchedulers.mainThread()) //给下面分配了UI线程
                    //解决RxJava2导致的内存泄漏问题
                    .compose(bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe {
                        LogManager.i(TAG, "threadNameC*****" + Thread.currentThread().name)
                        base64AndFileBean.base64StrList.let {
                            if (it.size > 0) {
                                //RecyclerView自動滑動到底部，看看最後一個字符串和打印出來的字符串是否一致
                                rcvBase64Str?.scrollToPosition(it.size - 1)
                            }
                        }
                    }
            }
            .observeOn(Schedulers.io()) //给下面分配了异步线程
            .doOnNext { base64AndFileBean ->
                LogManager.i(TAG, "threadName9*****" + Thread.currentThread().name)
                val mediaFileType =
                    MediaFileManager.getFileType(base64AndFileBean.fileCompressed?.absolutePath)
                val mimeType = mediaFileType.mimeType
                val typeArr = mimeType.split("/").toTypedArray()
                val fileType = typeArr[1]
                //再把压缩后的bitmap保存到本地
                val fileCompressedRecover = Base64AndFileManager.base64ToFile(
                    base64AndFileBean.getBase64Str(),
                    base64AndFileBean.dirsPathCompressedRecover,
                    "picture_large_compressed_recover.$fileType"
                )
                base64AndFileBean.fileCompressedRecover = fileCompressedRecover
            }
            .observeOn(Schedulers.io()) //给下面分配了异步线程
            .doOnNext { base64AndFileBean ->
                LogManager.i(TAG, "threadName10*****" + Thread.currentThread().name)
                val bitmapCompressedRecover =
                    BitmapFactory.decodeFile(base64AndFileBean.fileCompressedRecover.absolutePath)
                base64AndFileBean.bitmapCompressedRecover = bitmapCompressedRecover
            } //                .map(new Function<Base64AndFileBean, Bitmap>() {
            //                    @Override
            //                    public Bitmap apply(Base64AndFileBean base64AndFileBean?) throws Exception {
            //                        LogManager.i(TAG, "threadName10*****" + Thread.currentThread().getName());
            //                        Bitmap bitmapCompressedRecover = BitmapFactory.decodeFile(base64AndFileBean?.getFileCompressedRecover().getAbsolutePath());
            //                        return bitmapCompressedRecover;
            //                    }
            //                })
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { base64AndFileBean ->
                LogManager.i(TAG, "threadName11*****" + Thread.currentThread().name)
                tevBase64ToPicture?.visibility = View.GONE
                imvBase64ToPicture?.visibility = View.VISIBLE
                imvBase64ToPicture?.setImageBitmap(base64AndFileBean.bitmapCompressedRecover)
            }
            .observeOn(AndroidSchedulers.mainThread()) //给下面分配了UI线程
            //解决RxJava2导致的内存泄漏问题
            .compose(bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe { base64AndFileBean -> ossPutSaveFile(base64AndFileBean.fileCompressedRecover) }
    }

    private fun ossPutSaveFile(file: File?) {
        val endpoint = "http://oss-cn-shenzhen.aliyuncs.com"

        //你的阿里云对象存储上的accessKeyId
        val accessKeyId = "xxxxxxx"
        //你的阿里云对象存储上的accessKeySecret
        val accessKeySecret = "xxxxxxx"
        //token
        val token = "xxxxxxx"
        //expiration
        val expiration = "xxxxxxx"
        //这里要使用阿里云的STS，不要自己配置（这个是模拟代码，所以这样使用的）
        val credentialProvider = OSSCredentialProvider {
            OSSFederationToken(
                accessKeyId,
                accessKeySecret,
                token,
                expiration
            )
        }


//        // 填写STS应用服务器地址。（使用这个，服务端要部署STS）
//        String stsServer = "https://example.com";
//        // 推荐使用OSSAuthCredentialsProvider。token过期可以及时更新。
//        OSSCredentialProvider credentialProvider = new OSSAuthCredentialsProvider(stsServer);


        // 配置类如果不设置，会有默认配置。
        val conf = ClientConfiguration()
        conf.connectionTimeout = 15 * 1000 // 连接超时，默认15秒。
        conf.socketTimeout = 15 * 1000 // socket超时，默认15秒。
        conf.maxConcurrentRequest = 5 // 最大并发请求数，默认5个。
        conf.maxErrorRetry = 2 // 失败后最大重试次数，默认2次。
        conf.isHttpDnsEnable = true //默认为true，表示开启DNS配置。如需关闭请将其设置为false。
        val oss: OSS = OSSClient(applicationContext, endpoint, credentialProvider)
        // 调用此方法开启日志。
        OSSLog.enableLog()
        val mediaFileType = MediaFileManager.getFileType(file?.absolutePath)
        val mimeType = mediaFileType.mimeType
        val typeArr = mimeType.split("/").toTypedArray()
        val fileType = typeArr[1]
        val objectKey =
            "base64_and_file" + "/" + "picture_" + System.currentTimeMillis() + "." + fileType

        // 构造上传请求。
        val put = PutObjectRequest("rx-java2-and-retrofit2-bucket", objectKey, file?.absolutePath)

        // 异步上传时可以设置进度回调。
        put.progressCallback =
            OSSProgressCallback { request, currentSize, totalSize ->
                //                LogManager.i(TAG, "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        val task = oss.asyncPutObject(
            put,
            object : OSSCompletedCallback<PutObjectRequest?, PutObjectResult> {
                override fun onSuccess(request: PutObjectRequest?, result: PutObjectResult) {
                    LogManager.i(TAG, "onSuccess ETag*****" + result.eTag)
                    LogManager.i(TAG, "onSuccess RequestId*****" + result.requestId)
                    LogManager.i(TAG, "onSuccess threadName*****" + Thread.currentThread().name)
                    Observable.create<Int> { emitter ->
                        emitter.onNext(0)
                        LogManager.i(
                            TAG,
                            "onSuccess threadName2*****" + Thread.currentThread().name
                        )
                    } //                        .subscribeOn(Schedulers.io()) //给上面分配了异步线程
                        .observeOn(AndroidSchedulers.mainThread()) //给下面分配了UI线程
                        //解决RxJava2导致的内存泄漏问题
                        .compose(bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe {
                            LogManager.i(
                                TAG,
                                "onSuccess threadName3*****" + Thread.currentThread().name
                            )
                            hideLoading()
                            showToast("upload success", true)
                        }
                }

                override fun onFailure(
                    request: PutObjectRequest?,
                    clientExcepion: ClientException,
                    serviceException: ServiceException
                ) {
                    // 请求异常。
                    // 本地异常，如网络异常等。
                    clientExcepion.printStackTrace()
                    //                    ExceptionManager.get()?.throwException(getRxAppCompatActivity(), clientExcepion);
                    // 服务异常。
                    LogManager.i(TAG, "onFailure ErrorCode*****" + serviceException.errorCode)
                    LogManager.i(TAG, "onFailure RequestId*****" + serviceException.requestId)
                    LogManager.i(TAG, "onFailure HostId*****" + serviceException.hostId)
                    LogManager.i(TAG, "onFailure RawMessage*****" + serviceException.rawMessage)
                    LogManager.i(TAG, "onFailure threadName*****" + Thread.currentThread().name)
                    LogManager.i(TAG, "onFailure serviceException*****$serviceException")
                    //                    ExceptionManager.get()?.throwException(getRxAppCompatActivity(), clientExcepion);
                    Observable.create<Int> { emitter ->
                        emitter.onNext(0)
                        LogManager.i(
                            TAG,
                            "onFailure threadName2*****" + Thread.currentThread().name
                        )
                    } //                        .subscribeOn(Schedulers.io()) //给上面分配了异步线程
                        .observeOn(AndroidSchedulers.mainThread()) //给下面分配了UI线程
                        //解决RxJava2导致的内存泄漏问题
                        .compose(bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe {
                            LogManager.i(
                                TAG,
                                "onFailure threadName3*****" + Thread.currentThread().name
                            )
                            hideLoading()
                            showToast("upload error", true)
                        }
                }
            })

        // task.cancel(); // 可以取消任务。
        // task.waitUntilFinished(); // 等待上传完成。
    }

    override fun showLoading() {
        if (!mLoadView.isShown) {
            mLoadView.visibility = View.VISIBLE
            mLoadView.start()
        }
    }

    override fun hideLoading() {
        if (mLoadView.isShown) {
            mLoadView.stop()
            mLoadView.visibility = View.GONE
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
    private fun startTimer() {
        stopTimer()
        if (timer == null) {
            timer = Timer()
        }
        if (timerTask == null) {
            timerTask = object : TimerTask() {
                override fun run() {
                    handler?.post {
                        base64StrAdapter?.base64StrList?.let {
                            if (it.size > 0) {
                                //RecyclerView自動滑動到底部，看看最後一個字符串和打印出來的字符串是否一致
                                rcvBase64Str?.scrollToPosition(it.size - 1)
                            }
                        }
                    }
                }
            }
        }
        timer?.schedule(timerTask, 1000)
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
        timerTask?.cancel()
        timerTask = null
    }

    override fun showCompressedPictureSuccess(success: Base64AndFileBean) {
        tevCompressedPicture?.visibility = View.GONE
        imvCompressedPicture?.visibility = View.VISIBLE
        imvCompressedPicture?.setImageBitmap(success.bitmapCompressed)
        hideLoading()
        LogManager.i(TAG, "showCompressedPictureSuccess")
        showLoading()
        presenter.showPictureToBase64(success)
    }

    override fun showCompressedPictureError(error: String) {
        hideLoading()
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun showPictureToBase64Success(success: Base64AndFileBean) {
        tevPictureToBase64?.visibility = View.GONE
        rcvBase64Str?.visibility = View.VISIBLE
        base64StrAdapter?.addData(success.base64StrList)
        hideLoading()
        startTimer()
        showLoading()
        presenter.showBase64ToPicture(success)
    }

    override fun showPictureToBase64Error(error: String) {
        hideLoading()
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun showBase64ToPictureSuccess(success: Base64AndFileBean) {
        tevBase64ToPicture?.visibility = View.GONE
        imvBase64ToPicture?.visibility = View.VISIBLE
        imvBase64ToPicture?.setImageBitmap(success.bitmap)
        hideLoading()
    }

    override fun showBase64ToPictureError(error: String) {
        hideLoading()
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        resetData()
    }

    private fun resetData() {
        tevCompressedPicture?.visibility = View.VISIBLE
        imvCompressedPicture?.visibility = View.GONE
        tevPictureToBase64?.visibility = View.VISIBLE
        base64StrAdapter?.base64StrList?.let {
            if (it.size > 0) {
                rcvBase64Str?.scrollToPosition(0)
                it.clear()
                base64StrAdapter?.clearData()
            }
        }
        rcvBase64Str?.visibility = View.GONE
        tevBase64ToPicture?.visibility = View.VISIBLE
        imvBase64ToPicture?.visibility = View.GONE
    }

    override fun onDestroy() {
        stopTimer()
        handler?.removeCallbacksAndMessages(null)
        handler = null
        //        if (connection != null) {
//            // 解绑服务，服务要记得解绑，不要造成内存泄漏
//            unbindService(connection);
//            binder = null;
//        }
        super.onDestroy()
    }
}