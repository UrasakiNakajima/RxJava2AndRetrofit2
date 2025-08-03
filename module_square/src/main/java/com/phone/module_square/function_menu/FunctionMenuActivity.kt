package com.phone.module_square.function_menu

import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSONObject
import com.phone.library_base.callback.OnCommonSingleParamCallback
import com.phone.library_base.common.ConstantData
import com.phone.library_base.manager.StatusBarManager
import com.phone.library_base.manager.DialogManager
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.ScreenManager
import com.phone.library_base.manager.ToastManager
import com.phone.library_common.bean.BiographyData
import com.phone.library_mvvm.BaseMvvmAppRxActivity
import com.phone.library_network.bean.DownloadState
import com.phone.library_network.bean.State
import com.phone.module_square.BuildConfig
import com.phone.module_square.R
import com.phone.module_square.databinding.SquareActivityFunctionMenuBinding
import com.phone.module_square.function_menu.view_model.FunctionMenuViewModelImpl

@Route(path = ConstantData.Route.ROUTE_FUNCTION_MENU)
class FunctionMenuActivity :
    BaseMvvmAppRxActivity<FunctionMenuViewModelImpl, SquareActivityFunctionMenuBinding>() {

    companion object {
        private const val TAG = "FunctionMenuActivity"
    }
    val dialogManager = DialogManager()

    override fun initLayoutId(): Int = R.layout.square_activity_function_menu

    override fun initViewModel(): FunctionMenuViewModelImpl =
        ViewModelProvider(this).get(FunctionMenuViewModelImpl::class.java)

    override fun initData() {
        mDatabind.viewModel = mViewModel
        mDatabind.executePendingBindings()
    }

    override fun initObservers() {
        mViewModel.downloadData.observe(this) {
            LogManager.i(TAG, "onChanged*****downloadData")
            when (it) {
                is DownloadState.ProgressState -> {
                    onProgress(it.progress, it.total, it.speed)
                }

                is DownloadState.CompletedState -> {
                    showToast("下载文件成功", true)
                    hideLoading()
                    mIsLoadView = false
                }

                is DownloadState.ErrorState -> {
                    showToast(it.errorMsg, true)
                    hideLoading()
                    mIsLoadView = false
                }

                else -> {}
            }
        }
        mViewModel.insertData.observe(this) {
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
        }
        mViewModel.queryData.observe(this) {
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
        }
    }

    override fun initViews() {
        setToolbar(false, R.color.library_black)
        mDatabind?.apply {
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
                            mViewModel.insertBook(this@FunctionMenuActivity, success)
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

                            mIsLoadView = true
                            showLoading()
                            mViewModel.downloadFile(this@FunctionMenuActivity)
                        }

                        override fun onError(error: String) {
                            mDialogManager.dismissCommonDialog()
                        }
                    })
            }
            tevCustomBanner.setOnClickListener {
                //Jump with parameters
                ARouter.getInstance().build(ConstantData.Route.ROUTE_SQUARE_CUSTOM_BANNER)
                    .navigation()
            }
            tevActivityStart.setOnClickListener {
                //Jump with parameters
                ARouter.getInstance().build(ConstantData.Route.ROUTE_SQUARE_ACTIVITY_START)
                    .navigation()
            }


            val statusBarHeight = StatusBarManager.getStatusBarHeight()
            LogManager.i(TAG, "statusBarHeight*****${StatusBarManager.getStatusBarHeight()}")
            viewStatusBar.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, statusBarHeight)
            LogManager.i(TAG, "statusBarHeight dp*****${ScreenManager.pxToDp(statusBarHeight.toFloat())}")
        }
    }

    override fun initLoadData() {

    }

    fun onProgress(progress: Int, total: Long, speed: Long) {
        if (!mRxAppCompatActivity.isFinishing) {
            mDialogManager.onProgress(progress)
        }
    }


}