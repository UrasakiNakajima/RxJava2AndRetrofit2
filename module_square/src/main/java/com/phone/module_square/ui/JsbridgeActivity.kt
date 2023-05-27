package com.phone.module_square.ui

import android.net.Uri
import android.os.Build
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import com.alibaba.android.arouter.facade.annotation.Route
import com.github.lzyzsd.jsbridge.CallBackFunction
import com.github.lzyzsd.jsbridge.DefaultHandler
import com.google.gson.Gson
import com.phone.library_common.base.BaseBindingRxAppActivity
import com.phone.library_common.bean.UserBean
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.ResourcesManager
import com.phone.module_square.R
import com.phone.module_square.databinding.SquareActivityJsbridgeBinding


@Route(path = "/module_square/jsbridge")
class JsbridgeActivity : BaseBindingRxAppActivity<SquareActivityJsbridgeBinding>() {

    companion object {
        private val TAG = JsbridgeActivity::class.java.simpleName
    }

//    private var mUploadMessage: ValueCallback<Uri>? = null
//    private var mUploadMessageArray: ValueCallback<Array<Uri>>? = null
//    private var RESULT_CODE = 0

//    private var permissions = arrayOf(
//        Manifest.permission.READ_EXTERNAL_STORAGE,
//        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//        Manifest.permission.READ_PHONE_STATE
//    )
//    private var mPermissionsDialog: AlertDialog? = null

    override fun initLayoutId(): Int {
        return R.layout.square_activity_jsbridge
    }

    override fun initData() {

    }

    override fun initViews() {
        setToolbar(true)
        mDatabind.imvBack.setColorFilter(ResourcesManager.getColor(R.color.color_80000000))
        mDatabind.tevFunctionToJs.setOnClickListener {
            val userBean = UserBean()
            userBean.id = 1
            userBean.userId = "100"
            userBean.birthday = "1998.05.10"
            userBean.salary = 7000.0
            mDatabind.webView.callHandler(
                "functionToJs",
                Gson().toJson(userBean),
                CallBackFunction {
                    LogManager.i(TAG, "reponse data from JS $it")
                })
        }
        mDatabind.tevFunctionToJs2.setOnClickListener {
            mDatabind.webView.callHandler(
                "functionToJs2",
                "data from Android",
                CallBackFunction { data ->
                    LogManager.i(TAG, "reponse data from JS $data")
                })
        }
        mDatabind.webView.apply {
            setDefaultHandler(DefaultHandler())
            getSettings().setAllowFileAccess(true)
            getSettings().setAppCacheEnabled(true)
            getSettings().setDatabaseEnabled(true)
            // 允许网页定位
            // 允许网页定位
            getSettings().setGeolocationEnabled(true)
            // 允许网页弹对话框
            // 允许网页弹对话框
            getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
            // 加快网页加载完成的速度，等页面完成再加载图片
            // 加快网页加载完成的速度，等页面完成再加载图片
            getSettings().setLoadsImagesAutomatically(true)
            // 开启 localStorage
            // 开启 localStorage
            getSettings().setDomStorageEnabled(true)
            // 设置支持javascript// 本地 DOM 存储（解决加载某些网页出现白板现象）
            // 设置支持javascript// 本地 DOM 存储（解决加载某些网页出现白板现象）
            getSettings().setJavaScriptEnabled(true)
            // 进行缩放
            // 进行缩放
            getSettings().setBuiltInZoomControls(true)
            // 设置UserAgent
            // 设置UserAgent
            getSettings().setUserAgentString(getSettings().getUserAgentString() + "app")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 解决 Android 5.0 上 WebView 默认不允许加载 Http 与 Https 混合内容
                getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
            }
            webChromeClient = object : WebChromeClient() {
                fun openFileChooser(
                    uploadMsg: ValueCallback<Uri>, AcceptType: String?, capture: String?
                ) {
                    this.openFileChooser(uploadMsg)
                }

                fun openFileChooser(uploadMsg: ValueCallback<Uri>, AcceptType: String?) {
                    this.openFileChooser(uploadMsg)
                }

                fun openFileChooser(uploadMsg: ValueCallback<Uri>) {
//                    mUploadMessage = uploadMsg
//                    pictureSelector()
                }

                override fun onShowFileChooser(
                    webView: WebView,
                    filePathCallback: ValueCallback<Array<Uri>>,
                    fileChooserParams: FileChooserParams
                ): Boolean {
//                    mUploadMessageArray = filePathCallback
//                    pictureSelector()
                    return true
                }
            }
            loadUrl("file:///android_asset/jsbridge_js_java_interaction.html")


            registerHandler(
                "functionToAndroid"
            ) { data, function ->
                LogManager.i(
                    TAG,
                    "functionToAndroid handler = callNativeHandler, data from web = $data"
                )
                function.onCallBack("reponse data from Android 中文 from Java")
            }


            registerHandler(
                "functionToAndroid2"
            ) { data, function ->
                LogManager.i(
                    TAG,
                    "functionToAndroid2 handler = callNativeHandler, data from web = $data"
                )
                function.onCallBack("reponse data from Android 中文 from Java")
            }
        }
    }

    override fun initLoadData() {

    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

//    fun pictureSelector() {
//        val rxPermissionsManager = RxPermissionsManager()
//        rxPermissionsManager.initRxPermissions(
//            this,
//            permissions,
//            object : OnCommonRxPermissionsCallback {
//                override fun onRxPermissionsAllPass() {
//                    //所有的权限都授予
//                    val chooserIntent = Intent(Intent.ACTION_GET_CONTENT)
//                    chooserIntent.type = "image/*"
//                    startActivityForResult(chooserIntent, RESULT_CODE)
//
////                    PictureSelector.create(this@JsbridgeActivity)
////                        .dataSource(SelectMimeType.ofImage())
////                        .obtainMediaData({
////                            if (null == mUploadMessage && null == mUploadMessageArray) {
////                                return@obtainMediaData
////                            }
////                            if (it != null && mUploadMessage != null && mUploadMessageArray == null) {
////                                mUploadMessage!!.onReceiveValue(Uri.parse(it.get(0).path))
////                                mUploadMessage = null
////                            }
////                            if (it != null && null == mUploadMessage && mUploadMessageArray != null) {
////                                val pathList = mutableListOf<String>()
////                                for (i in 0 until it.size) {
////                                    pathList.add(it.get(i).path)
////                                }
////                                val uriList = mutableListOf<Uri>()
////                                for (i in 0 until uriList.size) {
////                                    uriList.add(Uri.parse(uriList.get(i).path))
////                                }
////                                mUploadMessageArray!!.onReceiveValue(uriList.toTypedArray())
////                                mUploadMessageArray = null
////                            }
////                        })
//                }
//
//                override fun onNotCheckNoMorePromptError() {
//                    //至少一个权限未授予且未勾选不再提示
//                    showSystemSetupDialog()
//                }
//
//                override fun onCheckNoMorePromptError() {
//                    //至少一个权限未授予且勾选了不再提示
//                    showSystemSetupDialog()
//                }
//            })
//
//
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode === RESULT_CODE) {
//            if (null == mUploadMessage && null == mUploadMessageArray) {
//                return
//            }
//            if (null == data) {
//                return
//            }
//            val uri = intent.data
//            if (uri != null && null != mUploadMessage && null == mUploadMessageArray) {
//                mUploadMessage!!.onReceiveValue(uri)
//                mUploadMessage = null
//            }
//            if (uri != null && null == mUploadMessage && null != mUploadMessageArray) {
//                mUploadMessageArray!!.onReceiveValue(arrayOf(uri))
//                mUploadMessageArray = null
//            }
//        }
//    }
//
//    private fun showSystemSetupDialog() {
//        cancelPermissionsDialog()
//        if (mPermissionsDialog == null) {
//            mPermissionsDialog = AlertDialog.Builder(mRxAppCompatActivity).setTitle("权限设置")
//                .setMessage("获取相关权限失败，将导致部分功能无法正常使用，请到设置页面手动授权")
//                .setPositiveButton("去授权") { dialog, which ->
//                    cancelPermissionsDialog()
//                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                    val uri = Uri.fromParts(
//                        "package", mRxAppCompatActivity.applicationContext.packageName, null
//                    )
//                    intent.data = uri
//                    startActivityForResult(intent, 207)
//                }.create()
//        }
//        mPermissionsDialog?.setCancelable(false)
//        mPermissionsDialog?.setCanceledOnTouchOutside(false)
//        mPermissionsDialog?.show()
//    }
//
//    /**
//     * 关闭对话框
//     */
//    private fun cancelPermissionsDialog() {
//        mPermissionsDialog?.cancel()
//        mPermissionsDialog = null
//    }

}