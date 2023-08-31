package com.phone.module_square.fragment

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSONObject
import com.phone.library_base.BaseApplication
import com.phone.library_base.base.BaseRxAppActivity
import com.phone.library_base.callback.OnCommonSingleParamCallback
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_base.manager.ScreenManager
import com.phone.library_mvvm.BaseMvvmRxFragment
import com.phone.library_network.bean.State
import com.phone.library_common.bean.*
import com.phone.library_common.callback.OnCommonRxPermissionsCallback
import com.phone.library_base.common.ConstantData
import com.phone.library_base.manager.DialogManager
import com.phone.library_common.manager.*
import com.phone.library_common.iprovider.ISquareProvider
import com.phone.library_greendao.bean.UserBean
import com.phone.library_greendao.bean.UserBean2
import com.phone.library_greendao.bean.UserBean3
import com.phone.library_network.manager.RetrofitManager
import com.phone.library_base.manager.ThreadPoolManager
import com.phone.library_base.manager.ToastManager
import com.phone.library_network.bean.DownloadState
import com.phone.library_network.manager.GsonManager
import com.phone.module_square.BuildConfig
import com.phone.module_square.R
import com.phone.module_square.databinding.SquareFragmentSquareBinding
import com.phone.module_square.view_model.SquareViewModelImpl
import java.util.concurrent.atomic.AtomicBoolean

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce :
 */
@Route(path = ConstantData.Route.ROUTE_SQUARE_FRAGMENT)
class SquareFragment : BaseMvvmRxFragment<SquareViewModelImpl, SquareFragmentSquareBinding>() {

    companion object {
        private val TAG: String = SquareFragment::class.java.simpleName
    }

    private var currentPage = 1
    private var subDataSquare = SubDataSquare()
    private var atomicBoolean = AtomicBoolean(false)

    private var mPermissionsDialog: AlertDialog? = null
    private var number = 1
    val dialogManager = DialogManager()

    private var permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE
    )

    override fun initLayoutId() = R.layout.square_fragment_square

    /**
     * 这里ViewModelProvider的参数要使用this，不要使用rxAppCompatActivity
     */
    override fun initViewModel() = ViewModelProvider(this).get(SquareViewModelImpl::class.java)

    override fun initData() {
        mDatabind.viewModel = mViewModel
        mDatabind.subDataSquare = subDataSquare
        mDatabind.executePendingBindings()

    }

    override fun initObservers() {
        mViewModel.liveData.observe(this, {
            LogManager.i(TAG, "onChanged*****dataxRxFragment")
            when (it) {
                is State.SuccessState -> {
                    if (it.success.size > 0) {
                        squareDataSuccess(it.success)
                    } else {
                        squareDataError(BaseApplication.instance().resources.getString(R.string.library_no_data_available))
                    }
                }

                is State.ErrorState -> {
                    squareDataError(
                        it.errorMsg
                    )
                }

                else -> {}
            }
        })

        mViewModel.downloadData.observe(this, {
            LogManager.i(TAG, "onChanged*****downloadData")
            when (it) {
                is DownloadState.ProgressState -> {
                    onProgress(it.progress, it.total, it.speed)
                }

                is DownloadState.CompletedState -> {
                    showToast("下载文件成功", true)
                    hideLoading()
                    mIsLoadView = true
                }

                is DownloadState.ErrorState -> {
                    showToast(it.errorMsg, true)
                    hideLoading()
                    mIsLoadView = true
                }

                else -> {}
            }
        })
        mViewModel.insertData.observe(this, {
            LogManager.i(TAG, "onChanged*****roomData")
            when (it) {
                is State.SuccessState -> {
                    mDialogManager.dismissLoadingDialog()
                    dialogManager.dismissBookDialog()
                    ToastManager.toast(mRxAppCompatActivity, it.success.toString())
                }

                is State.ErrorState -> {
                    mDialogManager.dismissLoadingDialog()
                    dialogManager.dismissBookDialog()
                    ToastManager.toast(mRxAppCompatActivity, it.errorMsg)
                }
            }
        })
        mViewModel.queryData.observe(this, {
            LogManager.i(TAG, "onChanged*****roomData")
            when (it) {
                is State.SuccessState -> {
                    hideLoading()
                    val bookJsonStr = JSONObject.toJSONString(it.success)
                    ARouter.getInstance().build(ConstantData.Route.ROUTE_SHOW_BOOK)
                        .withString("bookJsonStr", bookJsonStr)
                        .navigation()
                }

                is State.ErrorState -> {
                    hideLoading()
                    ToastManager.toast(mRxAppCompatActivity, it.errorMsg)
                }
            }
        })
    }

    override fun initViews() {
        mDatabind.apply {
            tevAndroidAndJs.setOnClickListener {
                //Jump with parameters
                ARouter.getInstance().build(ConstantData.Route.ROUTE_ANDROID_AND_JS).navigation()
            }
            tevEditTextInputLimits.run {
                setOnClickListener {
                    ARouter.getInstance().build(ConstantData.Route.ROUTE_EDIT_TEXT_INPUT_LIMITS)
                        .navigation()
                }
            }
            tevDecimalOperation.setOnClickListener {
                ARouter.getInstance().build(ConstantData.Route.ROUTE_DECIMAL_OPERATION).navigation()
            }
            tevKillApp.setOnClickListener {
                LogManager.i(TAG, "tevKillApp")
                number = 1
                initRxPermissions(number)
            }
            tevCreateAnException.setOnClickListener {
                number = 2
                initRxPermissions(number)
            }
            tevCreateAnException.apply {
                setOnClickListener {
                    number = 2
                    initRxPermissions(number)
                }
            }
            tevCreateAnException2.setOnClickListener {
                number = 3
                initRxPermissions(number)
            }
            tevCreateUser.setOnClickListener {
                ARouter.getInstance().build(ConstantData.Route.ROUTE_CREATE_USER).navigation()
            }
            tevKotlinCoroutine.setOnClickListener {
                ARouter.getInstance().build(ConstantData.Route.ROUTE_KOTLIN_COROUTINE).navigation()
            }
            tevRoomInsertBook.setOnClickListener {
                dialogManager.showBookDialog(mRxAppCompatActivity,
                    object : OnCommonSingleParamCallback<String> {
                        override fun onSuccess(success: String) {
                            mDialogManager.showLoadingDialog(mRxAppCompatActivity)
                            mViewModel.insertBook(this@SquareFragment, success)
                        }

                        override fun onError(error: String) {
                            dialogManager.dismissBookDialog()
                        }
                    })
            }
            tevRoomQueryBook.setOnClickListener {
                showLoading()
                mViewModel.queryBook()
            }
            tevEventSchedule.setOnClickListener {
                //Jump with parameters
                ARouter.getInstance().build(ConstantData.Route.ROUTE_EVENT_SCHEDULE).navigation()
            }
            tevMounting.setOnClickListener {
                //Jump with parameters
                ARouter.getInstance().build(ConstantData.Route.ROUTE_MOUNTING).navigation()
            }
            tevJsbridge.setOnClickListener {
                //Jump with parameters
                ARouter.getInstance().build(ConstantData.Route.ROUTE_JSBRIDGE).navigation()
            }
            tevThreadPool.setOnClickListener {
                if (!BuildConfig.IS_MODULE) {
                    //Jump with parameters
                    ARouter.getInstance().build(ConstantData.Route.ROUTE_THREAD_POOL)
                        .withString("title", "線程池")
                        .withParcelable(
                            "biographyData", BiographyData("book", "Rommel的传记", "Rommel的简介")
                        ).navigation()
                } else {
                    showToast("单独组件不能进入線程池页面，需使用整个项目才能进入線程池页面", true)
                }
            }
            tevPickerView.setOnClickListener {
                if (!BuildConfig.IS_MODULE) {
                    ARouter.getInstance().build(ConstantData.Route.ROUTE_PICKER_VIEW).navigation()
                } else {
                    showToast(
                        "单独组件不能进入三级联动列表，需使用整个项目才能进入三级联动列表", true
                    )
                }
            }
            tevDownloadFile.setOnClickListener {
                mDialogManager.showCommonDialog(mRxAppCompatActivity,
                    "是否下载视频文件",
                    "这里有一个好可爱的日本女孩",
                    object : OnCommonSingleParamCallback<String> {
                        override fun onSuccess(success: String) {
                            mDialogManager.dismissCommonDialog()

                            mIsLoadView = false
                            showLoading()
                            mViewModel.downloadFile(this@SquareFragment)
                        }

                        override fun onError(error: String) {
                            mDialogManager.dismissCommonDialog()
                        }
                    })
            }
        }
    }


    override fun initLoadData() {
//        startAsyncTask()

//        //製造一個类强制转换异常（java.lang.ClassCastException）
//        val user: User = User2()
//        val user3 = user as User3
//        LogManager.i(TAG, user3.toString())


        if (isFirstLoad) {
            initSquareData("$currentPage")
            LogManager.i(TAG, "initLoadData*****$TAG")
            isFirstLoad = false
        }
    }

//    private fun startAsyncTask() {
//        // This async task is an anonymous class and therefore has a hidden reference to the outer
//        // class MainActivity. If the activity gets destroyed before the task finishes (e.g. rotation),
//        // the activity instance will leak.
//        object : AsyncTask<Void?, Void?, Void?>() {
//            override fun doInBackground(vararg p0: Void?): Void? {
//                // Do some slow work in background
//                SystemClock.sleep(10000)
//                return null
//            }
//        }.execute()
//        Toast.makeText(mRxAppCompatActivity, "请关闭这个A完成泄露", Toast.LENGTH_SHORT).show()
//    }

    fun onProgress(progress: Int, total: Long, speed: Long) {
        if (!mRxAppCompatActivity.isFinishing) {
            mDialogManager.onProgress(progress)
        }
    }

    fun squareDataSuccess(success: List<SubDataSquare>) {
        if (!mRxAppCompatActivity.isFinishing()) {
            if (success.size > 0) {
                subDataSquare.apply {
                    title = success.get(1).title
                    chapterName = success.get(1).chapterName
                    link = success.get(1).link
                    envelopePic = success.get(1).envelopePic
                    desc = success.get(1).desc
                    LogManager.i(TAG, "desc@*****${desc}")
                }

                if (!BuildConfig.IS_MODULE) {
                    val ISquareProvider: ISquareProvider =
                        ARouter.getInstance().build(ConstantData.Route.ROUTE_SQUARE_SERVICE)
                            .navigation() as ISquareProvider
                    ISquareProvider.mSquareDataList = success
                }
            }
            hideLoading()
        }
    }

    fun squareDataError(error: String) {
        if (!mRxAppCompatActivity.isFinishing()) {
            showCustomToast(
                ScreenManager.dpToPx(20f),
                ScreenManager.dpToPx(20f),
                18,
                ResourcesManager.getColor(R.color.library_white),
                ResourcesManager.getColor(R.color.library_color_FF198CFF),
                ScreenManager.dpToPx(40f),
                ScreenManager.dpToPx(20f),
                error
            )
            hideLoading()
        }
    }

    /**
     * 請求權限，RxFragment里需要的时候直接调用就行了
     */
    private fun initRxPermissions(number: Int) {
        val rxPermissionsManager = RxPermissionsManager.instance()
        rxPermissionsManager.initRxPermissions2(
            this,
            permissions,
            object : OnCommonRxPermissionsCallback {
                override fun onRxPermissionsAllPass() {
                    ThreadPoolManager.instance().createSyncThreadPool({
                        //所有的权限都授予
                        if (number == 1) {
                            val baseRxAppActivity = mRxAppCompatActivity as BaseRxAppActivity
                            baseRxAppActivity.mActivityPageManager?.exitApp()
                        } else if (number == 2) {
                            LogManager.i(TAG, "thread name*****${Thread.currentThread().name}")
                            //製造一個造成App崩潰的異常（类强制转换异常java.lang.ClassCastException）
                            val userBean: UserBean = UserBean2()
                            val user3 = userBean as UserBean3
                            LogManager.i(TAG, user3.toString())
                        } else if (number == 3) {
                            try {
                                //製造一個不會造成App崩潰的異常（类强制转换异常java.lang.ClassCastException）
                                val userBean: UserBean = UserBean2()
                                val user3 = userBean as UserBean3
                                LogManager.i(TAG, user3.toString())
                            } catch (e: Exception) {
                                ExceptionManager.instance().throwException(e)
                            }
                        }
                    })
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
            mPermissionsDialog = AlertDialog.Builder(mRxAppCompatActivity).setTitle("权限设置")
                .setMessage("获取相关权限失败，将导致部分功能无法正常使用，请到设置页面手动授权")
                .setPositiveButton("去授权") { dialog, which ->
                    cancelPermissionsDialog()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts(
                        "package", mRxAppCompatActivity.applicationContext.packageName, null
                    )
                    intent.data = uri
                    startActivityForResult(intent, 207)
                }.create()
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

    private fun initSquareData(currentPage: String) {
        showLoading()
        ThreadPoolManager.instance().createScheduledThreadPoolToUIThread2(1000, {
            if (RetrofitManager.isNetworkAvailable()) {
                mViewModel.squareData(this, currentPage)
            } else {
                squareDataError(BaseApplication.instance().resources.getString(R.string.library_please_check_the_network_connection))
            }

            LogManager.i(TAG, "atomicBoolean.get()1*****" + atomicBoolean.get())
            atomicBoolean.compareAndSet(atomicBoolean.get(), true)
            LogManager.i(TAG, "atomicBoolean.get()2*****" + atomicBoolean.get())
        })
    }

    override fun onDestroy() {
        ThreadPoolManager.instance().shutdownNowScheduledThreadPool()
        super.onDestroy()
    }
}