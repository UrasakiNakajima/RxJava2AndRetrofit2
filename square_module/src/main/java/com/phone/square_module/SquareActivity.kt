package com.phone.square_module

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.text.InputFilter
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ImmersionBar
import com.phone.base64_and_file.Base64AndFileActivity
import com.phone.common_library.BaseApplication
import com.phone.common_library.base.BaseMvpRxAppActivity
import com.phone.common_library.base.BaseMvvmAppRxActivity
import com.phone.common_library.bean.DataX
import com.phone.common_library.bean.UserBean
import com.phone.common_library.bean.UserBean2
import com.phone.common_library.bean.UserBean3
import com.phone.common_library.callback.OnCommonRxPermissionsCallback
import com.phone.common_library.common.DecimalInputFilter
import com.phone.common_library.common.DecimalTextWatcher
import com.phone.common_library.manager.*
import com.phone.common_library.service.ISquareService
import com.phone.square_module.databinding.ActivitySquareBinding
import com.phone.square_module.ui.CreateUserActivity
import com.phone.square_module.ui.DecimalOperationActivity
import com.phone.square_module.ui.EditTextInputLimitsActivity
import com.phone.square_module.ui.KotlinCoroutineActivity
import com.phone.square_module.view_model.SquareViewModelImpl
import kotlinx.android.synthetic.main.fragment_square.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce :
 */
class SquareActivity :
    BaseMvvmAppRxActivity<SquareViewModelImpl, ActivitySquareBinding>() {

    companion object {
        private val TAG: String = SquareActivity::class.java.simpleName
    }

    private var currentPage: Int = 1
    private var datax: DataX = DataX()
    private var atomicBoolean: AtomicBoolean = AtomicBoolean(false)

    private var mPermissionsDialog: AlertDialog? = null
    private var number: Int = 1

    private var permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE
    )

    override fun initLayoutId() = R.layout.activity_square

    override fun initViewModel() =
        ViewModelProvider(rxAppCompatActivity).get(SquareViewModelImpl::class.java)

    override fun initData() {
        mDatabind.viewModel = viewModel
        mDatabind.datax = datax

        mDatabind.executePendingBindings()
    }

    override fun initObservers() {
        viewModel.dataxRxFragmentSuccess.observe(this, {
            if (it != null && it.size > 0) {
                LogManager.i(TAG, "onChanged*****dataxRxFragmentSuccess")
                squareDataSuccess(it)
            } else {
                squareDataError(BaseApplication.getInstance().resources.getString(R.string.no_data_available))
            }
        })
        viewModel.dataxRxFragmentError.observe(this, {
            if (!TextUtils.isEmpty(it)) {
                LogManager.i(TAG, "onChanged*****dataxRxFragmentError")
                squareDataError(it)
            }
        })
    }

    override fun initViews() {
        ImmersionBar.with(rxAppCompatActivity)
            .keyboardEnable(false)
            .statusBarDarkFont(false)
            .statusBarColor(R.color.color_FF198CFF)
            .navigationBarColor(R.color.color_FF198CFF).init()

        mDatabind.apply {
            tevKillApp.setOnClickListener {
                LogManager.i(TAG, "tevKillApp");
                number = 1;
                initRxPermissionsRxFragment(number)
            }
            tevCreateAnException.run {
                setOnClickListener {
                    number = 2;
                    initRxPermissionsRxFragment(number)
                }
            }
            tevCreateAnException2.setOnClickListener {
                number = 3;
                initRxPermissionsRxFragment(number)
            }
            tevEditTextInputLimits.setOnClickListener {
//            showEditTextInputLimitsDialog()
                startActivity(EditTextInputLimitsActivity::class.java)
            }
            tevDecimalOperation.setOnClickListener {
                startActivity(DecimalOperationActivity::class.java)
            }
            imvPicture.setOnClickListener {
//            startActivity(SquareDetailsActivity::class.java)
//            startActivity(PickerViewActivity::class.java)
                startActivity(Base64AndFileActivity::class.java)
            }
            tevCreateUser.setOnClickListener {
                startActivity(CreateUserActivity::class.java)
            }
            tevKotlinCoroutine.setOnClickListener {
                startActivity(KotlinCoroutineActivity::class.java)
            }
        }
    }

    override fun initLoadData() {
        initSquareData("$currentPage")

        LogManager.i(TAG, "SquareActivity initLoadData")

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
//        Toast.makeText(rxAppCompatActivity, "请关闭这个A完成泄露", Toast.LENGTH_SHORT).show()
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

    fun squareDataSuccess(success: List<DataX>) {
        if (!rxAppCompatActivity.isFinishing()) {
            if (success.size > 0) {
                datax.apply {
                    title = success.get(1).title
                    chapterName = success.get(1).chapterName
                    link = success.get(1).link
                    envelopePic = success.get(1).envelopePic
                }
                val ISquareService: ISquareService =
                    ARouter.getInstance().build("/square_module/SquareServiceImpl")
                        .navigation() as ISquareService
                ISquareService.squareDataList = success;
            }
            hideLoading()
        }
    }

    fun squareDataError(error: String) {
        if (!rxAppCompatActivity.isFinishing()) {
            showCustomToast(
                ScreenManager.dpToPx(rxAppCompatActivity, 20f),
                ScreenManager.dpToPx(rxAppCompatActivity, 20f),
                18,
                ContextCompat.getColor(rxAppCompatActivity, R.color.white),
                ContextCompat.getColor(rxAppCompatActivity, R.color.color_FFE066FF),
                ScreenManager.dpToPx(rxAppCompatActivity, 40f),
                ScreenManager.dpToPx(rxAppCompatActivity, 20f),
                error,
                true
            )
            hideLoading()
        }
    }

    /**
     * 請求權限，RxFragment里需要的时候直接调用就行了
     */
    private fun initRxPermissionsRxFragment(number: Int) {
        val rxPermissionsManager = RxPermissionsManager.getInstance()
        rxPermissionsManager.initRxPermissions(
            this,
            permissions,
            object : OnCommonRxPermissionsCallback {
                override fun onRxPermissionsAllPass() {
                    //所有的权限都授予
                    if (number == 1) {
                        val baseMvpRxAppActivity =
                            rxAppCompatActivity as BaseMvpRxAppActivity<*, *>;
                        baseMvpRxAppActivity.getActivityPageManager().exitApp2();
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
                            ExceptionManager.getInstance().throwException(rxAppCompatActivity, e)
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
            mPermissionsDialog = AlertDialog.Builder(rxAppCompatActivity)
                .setTitle("权限设置")
                .setMessage("获取相关权限失败，将导致部分功能无法正常使用，请到设置页面手动授权")
                .setPositiveButton("去授权") { dialog, which ->
                    cancelPermissionsDialog()
                    intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts(
                        "package",
                        rxAppCompatActivity.applicationContext.packageName,
                        null
                    )
                    intent!!.data = uri
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
        if (RetrofitManager.isNetworkAvailable(rxAppCompatActivity)) {
            viewModel.squareData2(this, currentPage)
        } else {
            squareDataError(BaseApplication.getInstance().resources.getString(R.string.please_check_the_network_connection));
        }

        LogManager.i(TAG, "atomicBoolean.get()1*****" + atomicBoolean.get());
        atomicBoolean.compareAndSet(atomicBoolean.get(), true);
        LogManager.i(TAG, "atomicBoolean.get()2*****" + atomicBoolean.get());
    }

}
