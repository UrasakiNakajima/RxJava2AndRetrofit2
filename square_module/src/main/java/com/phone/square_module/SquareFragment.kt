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
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.phone.base64_and_file.Base64AndFileActivity
import com.phone.common_library.BaseApplication
import com.phone.common_library.base.BaseMvpRxAppActivity
import com.phone.common_library.base.BaseMvvmRxFragment
import com.phone.common_library.bean.DataX
import com.phone.common_library.bean.UserBean
import com.phone.common_library.bean.UserBean2
import com.phone.common_library.bean.UserBean3
import com.phone.common_library.callback.OnCommonRxPermissionsCallback
import com.phone.common_library.common.DecimalInputFilter
import com.phone.common_library.common.DecimalTextWatcher
import com.phone.common_library.manager.*
import com.phone.common_library.service.IFirstPageService
import com.phone.common_library.service.ISquareService
import com.phone.square_module.databinding.FragmentSquareBinding
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
@Route(path = "/square_module/square")
class SquareFragment() : BaseMvvmRxFragment<SquareViewModelImpl, FragmentSquareBinding>() {

    companion object {
        private val TAG: String = SquareFragment::class.java.simpleName
    }

    private var currentPage: Int = 1
    private lateinit var dataxSuccessObserver: Observer<List<DataX>>
    private lateinit var dataxErrorObserver: Observer<String>
    private var datax: DataX = DataX()
    private var atomicBoolean: AtomicBoolean = AtomicBoolean(false)

    private var mPermissionsDialog: AlertDialog? = null
    private var number: Int = 1

    private var permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE
    )

    override fun initLayoutId() = R.layout.fragment_square

    override fun initViewModel() = ViewModelProvider(rxAppCompatActivity).get(SquareViewModelImpl::class.java)

    override fun initData() {
        mDatabind.viewModel = viewModel
        mDatabind.datax = datax

        mDatabind.executePendingBindings()
    }

    override fun initObservers() {
        dataxSuccessObserver = object : Observer<List<DataX>> {
            override fun onChanged(t: List<DataX>?) {
                if (t != null && t.size > 0) {
                    LogManager.i(TAG, "onChanged*****dataxSuccessObserver")
                    squareDataSuccess(t)
                } else {
                    squareDataError(BaseApplication.getInstance().resources.getString(R.string.no_data_available))
                }
            }
        }

        dataxErrorObserver = object : Observer<String> {
            override fun onChanged(t: String?) {
                if (!TextUtils.isEmpty(t)) {
                    LogManager.i(TAG, "onChanged*****dataxErrorObserver")
                    squareDataError(t!!)
                }
            }
        }

        viewModel.dataxRxFragmentSuccess.observe(this, dataxSuccessObserver)
        viewModel.dataxRxFragmentError.observe(this, dataxErrorObserver)
    }

    override fun initViews() {
        mDatabind.tevKillApp.setOnClickListener {
            LogManager.i(TAG, "tevKillApp");
            number = 1;
            initRxPermissionsRxFragment(number)
        }
        mDatabind.tevCreateAnException.setOnClickListener {
            number = 2;
            initRxPermissionsRxFragment(number)
        }
        mDatabind.tevCreateAnException2.setOnClickListener {
            number = 3;
            initRxPermissionsRxFragment(number)
        }
        mDatabind.tevEditTextInputLimits.setOnClickListener {
//            showEditTextInputLimitsDialog()
            startActivity(EditTextInputLimitsActivity::class.java)
        }
        mDatabind.tevDecimalOperation.setOnClickListener {
            startActivity(DecimalOperationActivity::class.java)
        }
        mDatabind.imvPicture.setOnClickListener {
//            startActivity(SquareDetailsActivity::class.java)
//            startActivity(PickerViewActivity::class.java)
            startActivity(Base64AndFileActivity::class.java)
        }
        mDatabind.tevCreateUser.setOnClickListener {
            startActivity(CreateUserActivity::class.java)
        }
        mDatabind.tevKotlinCoroutine.setOnClickListener {
            startActivity(KotlinCoroutineActivity::class.java)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun showEditTextInputLimitsDialog() {
        val view: View =
            LayoutInflater.from(rxAppCompatActivity)
                .inflate(R.layout.dialog_layout_edit_text_input_limits, null, false)
        val edtInput = view.findViewById<View>(R.id.edt_input) as EditText
        val tevCancel = view.findViewById<View>(R.id.tev_cancel) as TextView
        val tevConfirm = view.findViewById<View>(R.id.tev_confirm) as TextView
        //小数点前边几位（修改这里可以自定义）
        val beforeDecimalNum = 5
        //小数点后边几位（修改这里可以自定义）
        val afterDecimalNum = 5
        //最大长度是多少位（修改这里可以自定义）
        val maxLength = 11
        //输入的类型可以是整数或小数
        edtInput.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        val decimalInputFilter =
            DecimalInputFilter(
                beforeDecimalNum,
                afterDecimalNum
            )
        //输入总长度多少位，小数几位（修改这里可以自定义）
        val inputFilter = arrayOf(InputFilter.LengthFilter(maxLength), decimalInputFilter)
        edtInput.filters = inputFilter
        edtInput.addTextChangedListener(
            DecimalTextWatcher(
                edtInput,
                afterDecimalNum
            )
        )
        val alertDialog =
            AlertDialog.Builder(rxAppCompatActivity, R.style.standard_dialog_style2)
                .setView(view)
                .show()

        val widthPx = ScreenManager.getScreenWidth(rxAppCompatActivity)
        LogManager.i(TAG, "widthPx*****" + widthPx)
        val widthDp = ScreenManager.pxToDp(rxAppCompatActivity, widthPx.toFloat())
        LogManager.i(TAG, "widthDp*****" + widthDp)
        val heightPx = ScreenManager.getScreenHeight(rxAppCompatActivity)
        LogManager.i(TAG, "heightPx*****" + heightPx)
        val heightDp = ScreenManager.pxToDp(rxAppCompatActivity, heightPx.toFloat())
        LogManager.i(TAG, "heightDp*****" + heightDp)
        tevCancel.setOnClickListener { v: View? ->
            SoftKeyboardManager.hideInputMethod(rxAppCompatActivity);
            alertDialog.dismiss()
        }
        tevConfirm.setOnClickListener { v: View? ->
            val afterData = edtInput.text.toString()
            if (!TextUtils.isEmpty(afterData)) {
                if (afterData.contains(".")) {
                    val afterDataArr = afterData.split("\\.".toRegex()).toTypedArray()
                    if ("" == afterDataArr[0]) {
                        Toast.makeText(rxAppCompatActivity, "请输入正常整数或小数", Toast.LENGTH_SHORT)
                            .show()
                    } else if (afterDataArr.size == 1) { //当afterData是这种类型的小数时（0. 100.）
                        Toast.makeText(rxAppCompatActivity, "请输入正常整数或小数", Toast.LENGTH_SHORT)
                            .show()
                    } else {
//                        SquareFragment::tev_edit_text_decimal_or_integer.get(SquareFragment()).setText(edtInput.text.toString())
                        tev_edit_text_input_limits.setText(edtInput.text.toString())
                        SoftKeyboardManager.hideInputMethod(rxAppCompatActivity);
                        alertDialog.dismiss()
                    }
                } else {
                    if (afterData.length <= beforeDecimalNum) {
                        val afterDataArr = afterData.split("".toRegex()).toTypedArray()
                        if (afterDataArr.size > 1 && "0" == afterDataArr[1]) {
                            Toast.makeText(rxAppCompatActivity, "请输入正常整数或小数", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            tev_edit_text_input_limits.setText(edtInput.text.toString())
                            SoftKeyboardManager.hideInputMethod(rxAppCompatActivity);
                            alertDialog.dismiss()
                        }
                    } else {
                        Toast.makeText(
                            rxAppCompatActivity,
                            "整数长度不能大于" + beforeDecimalNum + "位",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(rxAppCompatActivity, "请输入整数或小数", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        alertDialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        edtInput.setFocusable(true)
        edtInput.setFocusableInTouchMode(true)
        edtInput.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
//                val widthPx = alertDialog.window!!.getAttributes().width
//                val widthDp = ScreenManager.pxToDp(rxAppCompatActivity, widthPx.toFloat())
//                val heightPx = alertDialog.window!!.getAttributes().height
//                val heightDp = ScreenManager.pxToDp(rxAppCompatActivity, heightPx.toFloat())
//                LogManager.i(TAG, "alertDialog widthDp*****" + widthDp)
//                LogManager.i(TAG, "alertDialog heightDp*****" + heightDp)
                SoftKeyboardManager.showInputMethod(rxAppCompatActivity, edtInput);
            } else {

            }
        }
        edtInput.requestFocus();
    }

    override fun initLoadData() {
        initSquareData("$currentPage")

        val firstPageService =
            ARouter.getInstance().build("/first_page_module/FirstPageServiceImpl")
                .navigation() as IFirstPageService
        LogManager.i(
            TAG,
            "firstPageService.firstPageDataList******" + firstPageService.firstPageDataList.toString()
        )
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
                datax.title = success.get(1).title
                datax.chapterName = success.get(1).chapterName
                datax.link = success.get(1).link
                datax.envelopePic = success.get(1).envelopePic
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
        rxPermissionsManager.initRxPermissionsRxFragment(
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
        if (mPermissionsDialog != null) {
            mPermissionsDialog?.cancel()
            mPermissionsDialog = null
        }
    }

    private fun initSquareData(currentPage: String) {
        showLoading()
        if (RetrofitManager.isNetworkAvailable(rxAppCompatActivity)) {
            viewModel.squareDataRxFragment(this, currentPage)
        } else {
            squareDataError(BaseApplication.getInstance().resources.getString(R.string.please_check_the_network_connection));
        }

        LogManager.i(TAG, "atomicBoolean.get()1*****" + atomicBoolean.get());
        atomicBoolean.compareAndSet(atomicBoolean.get(), true);
        LogManager.i(TAG, "atomicBoolean.get()2*****" + atomicBoolean.get());
    }

}