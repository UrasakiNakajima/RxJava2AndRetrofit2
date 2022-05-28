package com.phone.square_module

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.base64_and_file.Base64AndFileActivity
import com.phone.common_library.BaseApplication
import com.phone.common_library.base.BaseMvvmRxFragment
import com.phone.common_library.bean.User
import com.phone.common_library.bean.User2
import com.phone.common_library.bean.User3
import com.phone.common_library.callback.OnCommonRxPermissionsCallback
import com.phone.common_library.manager.*
import com.phone.square_module.bean.DataX
import com.phone.square_module.databinding.FragmentSquareBinding
import com.phone.square_module.view_model.SquareViewModelImpl
import java.util.concurrent.atomic.AtomicBoolean

@Route(path = "/square_module/square")
class SquareFragment() : BaseMvvmRxFragment<SquareViewModelImpl, FragmentSquareBinding>() {

    companion object {
        private val TAG: String = SquareFragment::class.java.simpleName
    }

    //    private var mainActivity: MainActivity? = null
    //    private var dataList: MutableList<DataX> = mutableListOf()
    private var currentPage: Int = 1
    private var dataxSuccessObserver: Observer<List<DataX>>? = null;
    private var dataxErrorObserver: Observer<String>? = null;
    private var datax: DataX = DataX()
    private var atomicBoolean: AtomicBoolean = AtomicBoolean(false);

    private var mPermissionsDialog: AlertDialog? = null
    private var number: Int? = null

    override fun initLayoutId(): Int {
        return R.layout.fragment_square
    }

    override fun initViewModel(): SquareViewModelImpl {
        return ViewModelProvider(this).get(SquareViewModelImpl::class.java)
    }

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
//                    LogManager.i(TAG, "onChanged*****${t.toString()}")
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

//        dataxSuccessObserver = null
        viewModel!!.getDataxSuccess().observe(viewLifecycleOwner, dataxSuccessObserver!!)
        viewModel!!.getDataxError().observe(viewLifecycleOwner, dataxErrorObserver!!)
    }

    override fun initViews() {
        mDatabind.imvPic.setOnClickListener {
//            startActivity(SquareDetailsActivity::class.java)
//            startActivity(PickerViewActivity::class.java)
            startActivity(Base64AndFileActivity::class.java)
        }
        mDatabind.tevCreateAnException.setOnClickListener {
            number = 1;
            initRxPermissionsRxFragment(number!!)
        }
        mDatabind.tevCreateAnException2.setOnClickListener {
            number = 2;
            initRxPermissionsRxFragment(number!!)
        }
    }

    override fun initLoadData() {
        initSquare("$currentPage")

//        startAsyncTask()

//        //製造一個类强制转换异常（java.lang.ClassCastException）
//        val user: User = User2()
//        val user3 = user as User3
//        LogManager.i(TAG, user3.toString())
    }

//    private fun startAsyncTask() {
//
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
        if (mDatabind.loadView != null && !mDatabind.loadView.isShown()) {
            mDatabind.loadView.setVisibility(View.VISIBLE)
            mDatabind.loadView.start()
        }
    }

    fun hideLoading() {
        if (mDatabind.loadView != null && mDatabind.loadView.isShown()) {
            mDatabind.loadView.stop()
            mDatabind.loadView.setVisibility(View.GONE)
        }
    }

    fun squareDataSuccess(success: List<DataX>) {
        if (!rxAppCompatActivity!!.isFinishing()) {
            if (success.size > 0) {
                datax.title = success.get(1).title
                datax.chapterName = success.get(1).chapterName
                datax.link = success.get(1).link
                datax.envelopePic = success.get(1).envelopePic
            }
            hideLoading()
        }
    }

    fun squareDataError(error: String) {
        if (!rxAppCompatActivity!!.isFinishing()) {
            showCustomToast(
                ScreenManager.dpToPx(rxAppCompatActivity, 20f),
                ScreenManager.dpToPx(rxAppCompatActivity, 20f),
                18,
                ContextCompat.getColor(rxAppCompatActivity!!, R.color.white),
                ContextCompat.getColor(rxAppCompatActivity!!, R.color.color_FFE066FF),
                ScreenManager.dpToPx(rxAppCompatActivity, 40f),
                ScreenManager.dpToPx(rxAppCompatActivity, 20f),
                error
            )

            hideLoading()
        }
    }

    /**
     * RxFragment里需要的时候直接调用就行了
     */
    private fun initRxPermissionsRxFragment(number: Int) {
        val rxPermissionsManager = RxPermissionsManager.getInstance()
        rxPermissionsManager.initRxPermissionsRxFragment2(
            this,
            object : OnCommonRxPermissionsCallback {
                override fun onRxPermissionsAllPass() {
                    if (number == 1) {
                        //製造一個造成App崩潰的異常（类强制转换异常java.lang.ClassCastException）
                        val user: User = User2()
                        val user3 = user as User3
                        LogManager.i(TAG, user3.toString())
                    } else if (number == 2) {
                        try {
                            //製造一個不會造成App崩潰的異常（类强制转换异常java.lang.ClassCastException）
                            val user: User = User2()
                            val user3 = user as User3
                            LogManager.i(TAG, user3.toString())
                        } catch (e: Exception) {
                            ExceptionManager.getInstance().throwException(rxAppCompatActivity, e)
                        }
                    }
                }

                override fun onNotCheckNoMorePromptError() {
                    showSystemSetupDialog()
                }

                override fun onCheckNoMorePromptError() {
                    showSystemSetupDialog()
                }
            })
    }

    private fun showSystemSetupDialog() {
        cancelPermissionsDialog()
        if (mPermissionsDialog == null) {
            mPermissionsDialog = AlertDialog.Builder(rxAppCompatActivity!!)
                .setTitle("权限设置")
                .setMessage("获取相关权限失败，将导致部分功能无法正常使用，请到设置页面手动授权")
                .setPositiveButton("去授权") { dialog, which ->
                    cancelPermissionsDialog()
                    intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts(
                        "package",
                        rxAppCompatActivity!!.applicationContext.packageName,
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

    private fun initSquare(currentPage: String) {
        showLoading()
        if (RetrofitManager.isNetworkAvailable(rxAppCompatActivity)) {
            viewModel!!.squareData(this, currentPage)
        } else {
            squareDataError(BaseApplication.getInstance().resources.getString(R.string.please_check_the_network_connection));
        }

        LogManager.i(TAG, "atomicBoolean.get()1*****" + atomicBoolean.get());
        atomicBoolean.compareAndSet(atomicBoolean.get(), true);
        LogManager.i(TAG, "atomicBoolean.get()2*****" + atomicBoolean.get());
    }

}