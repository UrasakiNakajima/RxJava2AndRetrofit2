package com.phone.module_square

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.phone.library_common.BaseApplication
import com.phone.library_common.base.BaseMvpRxAppActivity
import com.phone.library_common.base.BaseMvvmRxFragment
import com.phone.library_common.bean.*
import com.phone.library_common.callback.OnCommonRxPermissionsCallback
import com.phone.library_common.manager.*
import com.phone.library_common.service.ISquareService
import com.phone.module_square.databinding.SquareFragmentSquareBinding
import com.phone.module_square.ui.*
import com.phone.module_square.view_model.SquareViewModelImpl
import java.util.concurrent.atomic.AtomicBoolean

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce :
 */
@Route(path = "/module_square/square")
class SquareFragment() : BaseMvvmRxFragment<SquareViewModelImpl, SquareFragmentSquareBinding>() {

    companion object {
        private val TAG: String = SquareFragment::class.java.simpleName
    }

    private var currentPage = 1
    private var subDataSquare = SubDataSquare()
    private var atomicBoolean = AtomicBoolean(false)

    private var mPermissionsDialog: AlertDialog? = null
    private var number = 1

    private var permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE
    )

    override fun initLayoutId() = R.layout.square_fragment_square

    /**
     * 这里ViewModelProvider的参数要使用this，不要使用rxAppCompatActivity
     */
    override fun initViewModel() =
        ViewModelProvider(this).get(SquareViewModelImpl::class.java)

    override fun initData() {
        mDatabind.viewModel = viewModel
        mDatabind.subDataSquare = subDataSquare

        mDatabind.executePendingBindings()
    }

    override fun initObservers() {
        viewModel.dataxRxFragmentSuccess.observe(this, {
            if (it != null && it.size > 0) {
                LogManager.i(TAG, "onChanged*****dataxRxFragmentSuccess")
                squareDataSuccess(it)
            } else {
                squareDataError(BaseApplication.get().resources.getString(R.string.no_data_available))
            }
        })
        viewModel.dataxRxFragmentError.observe(this, {
            LogManager.i(TAG, "onChanged*****dataxRxFragmentError")
            squareDataError(
                it ?: BaseApplication.get().resources.getString(R.string.no_data_available)
            )
        })
    }

    override fun initViews() {
        mDatabind.apply {
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
            tevAndroidAndJs.setOnClickListener {
                //Jump with parameters
                ARouter.getInstance()
                    .build("/android_and_js/ui/android_and_js")
                    .navigation()
            }
            tevEditTextInputLimits.run {
                setOnClickListener {
                    startActivity(EditTextInputLimitsActivity::class.java)
                }
            }
            tevDecimalOperation.setOnClickListener {
                startActivity(DecimalOperationActivity::class.java)
            }
            tevCreateUser.setOnClickListener {
                startActivity(CreateUserActivity::class.java)
            }
            tevKotlinCoroutine.setOnClickListener {
                startActivity(KotlinCoroutineActivity::class.java)
            }
            tevMounting.setOnClickListener {
                //Jump with parameters
                ARouter.getInstance()
                    .build("/module_main/mounting")
                    .navigation()
            }
            tevJsbridge.setOnClickListener {
                //Jump with parameters
                ARouter.getInstance()
                    .build("/module_square/jsbridge")
                    .navigation()
            }
//            tevThreeLevelLinkageList.setOnClickListener {
//                startActivity(PickerViewActivity::class.java)
//            }
//            imvPicture.setOnClickListener {
////                ARouter.getInstance().build("/module_project/ui/event_schedule").navigation()
//                startActivity(Base64AndFileActivity::class.java)
//            }
        }

    }

    override fun initLoadData() {
        initSquareData("$currentPage")
        LogManager.i(TAG, "SquareFragment initLoadData")

//        startAsyncTask()

//        //製造一個类强制转换异常（java.lang.ClassCastException）
//        val user: User = User2()
//        val user3 = user as User3
//        LogManager.i(TAG, user3.toString())
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

    fun showLoading() {
        if (!mDatabind.loadView.isShown()) {
            mDatabind.loadView.setVisibility(View.VISIBLE)
            mDatabind.loadView.start()
        }
    }

    fun hideLoading() {
        if (mDatabind.loadView.isShown()) {
            mDatabind.loadView.stop()
            mDatabind.loadView.setVisibility(View.GONE)
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
                val ISquareService: ISquareService =
                    ARouter.getInstance().build("/module_square/SquareServiceImpl")
                        .navigation() as ISquareService
                ISquareService.mSquareDataList = success
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
                ResourcesManager.getColor(R.color.white),
                ResourcesManager.getColor(R.color.color_FF198CFF),
                ScreenManager.dpToPx(40f),
                ScreenManager.dpToPx(20f),
                error,
                true
            )
            hideLoading()
        }
    }

    /**
     * 請求權限，RxFragment里需要的时候直接调用就行了
     */
    private fun initRxPermissions(number: Int) {
        val rxPermissionsManager = RxPermissionsManager.get()
        rxPermissionsManager.initRxPermissions2(
            this,
            permissions,
            object : OnCommonRxPermissionsCallback {
                override fun onRxPermissionsAllPass() {
                    //所有的权限都授予
                    if (number == 1) {
                        val baseMvpRxAppActivity =
                            mRxAppCompatActivity as BaseMvpRxAppActivity<*, *>
                        baseMvpRxAppActivity.getActivityPageManager()?.exitApp()
                    } else if (number == 2) {
                        //製造一個造成App崩潰的異常（类强制转换异常java.lang.ClassCastException）
                        val userBean: UserBean =
                            UserBean2()
                        val user3 = userBean as UserBean3
                        LogManager.i(TAG, user3.toString())
                    } else if (number == 3) {
                        try {
                            //製造一個不會造成App崩潰的異常（类强制转换异常java.lang.ClassCastException）
                            val userBean: UserBean =
                                UserBean2()
                            val user3 = userBean as UserBean3
                            LogManager.i(TAG, user3.toString())
                        } catch (e: Exception) {
                            ExceptionManager.get().throwException(e)
                        }
                    }
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
            mPermissionsDialog = AlertDialog.Builder(mRxAppCompatActivity)
                .setTitle("权限设置")
                .setMessage("获取相关权限失败，将导致部分功能无法正常使用，请到设置页面手动授权")
                .setPositiveButton("去授权") { dialog, which ->
                    cancelPermissionsDialog()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts(
                        "package",
                        mRxAppCompatActivity.applicationContext.packageName,
                        null
                    )
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

    private fun initSquareData(currentPage: String) {
        showLoading()
        if (RetrofitManager.isNetworkAvailable()) {
            viewModel.squareData(this, currentPage)
        } else {
            squareDataError(BaseApplication.get().resources.getString(R.string.please_check_the_network_connection))
        }

        LogManager.i(TAG, "atomicBoolean.get()1*****" + atomicBoolean.get())
        atomicBoolean.compareAndSet(atomicBoolean.get(), true)
        LogManager.i(TAG, "atomicBoolean.get()2*****" + atomicBoolean.get())
    }

}