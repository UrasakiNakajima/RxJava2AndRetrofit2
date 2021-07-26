package com.phone.square_module.ui

import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.phone.common_library.BaseApplication
import com.phone.common_library.base.BaseMvvmAppActivity
import com.phone.common_library.manager.LogManager
import com.phone.common_library.manager.RetrofitManager
import com.phone.common_library.manager.ScreenManager
import com.phone.square_module.R
import com.phone.square_module.bean.DataX
import com.phone.square_module.databinding.ActivitySquareDetailsBinding
import com.phone.square_module.view_model.SquareViewModelImpl

class SquareDetailsActivity : BaseMvvmAppActivity<SquareViewModelImpl, ActivitySquareDetailsBinding>() {

    companion object {
        private val TAG: String = "SquareDetailsActivity"
    }

    private var currentPage: Int = 1
    private var dataxDetailsSuccessObserver: Observer<List<DataX>>? = null;
    private var dataxDetailsErrorObserver: Observer<String>? = null;
    private var dataxDetails: DataX = DataX()

    override fun initLayoutId(): Int {
        return R.layout.activity_square_details
    }

    override fun initViewModel(): SquareViewModelImpl {
        return ViewModelProvider(this).get(SquareViewModelImpl::class.java)
    }

    override fun initData() {
        mDatabind.viewModel = viewModel
        mDatabind.dataxDetails = dataxDetails

        mDatabind.executePendingBindings()


//        //测试专用（只支持到Android 10，在Android 11手机上无效）
//        val resourcesPath: String = "/storage/emulated/0/Pictures/1622037257522.png";
//        val fileName: String = System.currentTimeMillis().toString();
//        val isSuccess: Boolean = PictureManager.copyPublicDirectoryFile(this, resourcesPath, fileName);
//        if (isSuccess) {
//            //            showToast(error, true);
//            showCustomToast("复制成功", true)
//        }
    }

    override fun initObservers() {
        dataxDetailsSuccessObserver = object : Observer<List<DataX>> {
            override fun onChanged(t: List<DataX>?) {
                if (t != null && t.size > 0) {
                    LogManager.i(TAG, "onChanged*****dataxDetailsSuccessObserver")
//                    LogManager.i(TAG, "onChanged*****${t.toString()}")
                    squareDetailsSuccess(t)
                } else {
                    squareDetailsError(BaseApplication.getInstance().resources.getString(R.string.no_data_available))
                }
            }

        }

        dataxDetailsErrorObserver = object : Observer<String> {
            override fun onChanged(t: String?) {
                if (!TextUtils.isEmpty(t)) {
                    LogManager.i(TAG, "onChanged*****dataxDetailsErrorObserver")
                    squareDetailsError(t!!)
                }
            }

        }

        viewModel!!.getDataxDetailsSuccess().observe(this, dataxDetailsSuccessObserver!!)
        viewModel!!.getDataxDetailsError().observe(this, dataxDetailsErrorObserver!!)
    }

    override fun initViews() {
        setToolbar(false, R.color.color_FF198CFF)
        mDatabind.imvBack.setColorFilter(resources!!.getColor(R.color.color_FFFFFFFF))
        mDatabind.layoutBack.setOnClickListener {
            finish()
        }
    }

    override fun initLoadData() {
        initSquareDetails("$currentPage")
    }

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

    fun squareDetailsSuccess(success: List<DataX>) {
        if (!this.isFinishing()) {
            if (success.size > 0) {
                dataxDetails.title = success.get(5).title
                dataxDetails.chapterName = success.get(5).chapterName
                dataxDetails.link = success.get(5).link
                dataxDetails.desc = success.get(5).desc
                dataxDetails.envelopePic = success.get(5).envelopePic
            }
            hideLoading()
        }
    }

    fun squareDetailsError(error: String) {
        if (!this.isFinishing()) {
            showCustomToast(ScreenManager.dpToPx(this, 20f), ScreenManager.dpToPx(this, 20f),
                    18, resources!!.getColor(R.color.white),
                    resources!!.getColor(R.color.color_FFE066FF), ScreenManager.dpToPx(this, 40f),
                    ScreenManager.dpToPx(this, 20f), error,
                    true)

            hideLoading()
        }
    }

    private fun initSquareDetails(currentPage: String) {
        showLoading()
        if (RetrofitManager.isNetworkAvailable(this)) {
            viewModel!!.squareDetails(currentPage)
        } else {
            squareDetailsError(BaseApplication.getInstance().resources.getString(R.string.please_check_the_network_connection));
        }
    }

    override fun onDestroy() {
        viewModel!!.getDataxSuccess().removeObservers(this)
        super.onDestroy()
    }

}
