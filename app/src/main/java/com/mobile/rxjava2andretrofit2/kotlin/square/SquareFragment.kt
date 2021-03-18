package com.mobile.rxjava2andretrofit2.kotlin.square

import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mobile.rxjava2andretrofit2.MineApplication
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.base.BaseMvvmFragment
import com.mobile.rxjava2andretrofit2.databinding.FragmentSquareBinding
import com.mobile.rxjava2andretrofit2.kotlin.square.bean.DataX
import com.mobile.rxjava2andretrofit2.kotlin.square.ui.SquareDetailsActivity
import com.mobile.rxjava2andretrofit2.kotlin.square.view_model.SquareViewModelImpl
import com.mobile.rxjava2andretrofit2.main.MainActivity
import com.mobile.rxjava2andretrofit2.manager.LogManager
import com.mobile.rxjava2andretrofit2.manager.RetrofitManager
import com.mobile.rxjava2andretrofit2.manager.ScreenManager

class SquareFragment() : BaseMvvmFragment<SquareViewModelImpl, FragmentSquareBinding>() {

    companion object {
        private val TAG: String = "SquareFragment"
    }

    private var mainActivity: MainActivity? = null
    //    private var dataList: MutableList<DataX> = mutableListOf()
    private var currentPage: Int = 1
    private var dataxSuccessObserver: Observer<List<DataX>>? = null;
    private var dataxErrorObserver: Observer<String>? = null;
    private var datax: DataX = DataX()

    override fun initLayoutId(): Int {
        return R.layout.fragment_square
    }

    override fun initViewModel(): SquareViewModelImpl {
        return ViewModelProvider(this).get(SquareViewModelImpl::class.java)
    }

    override fun initData() {
        mainActivity = activity as MainActivity
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
                    squareDataError(MineApplication.getInstance().resources.getString(R.string.no_data_available))
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

        viewModel!!.getDataxSuccess().observe(this, dataxSuccessObserver!!)
        viewModel!!.getDataxError().observe(this, dataxErrorObserver!!)
    }

    override fun initViews() {
        mDatabind.imvPic.setOnClickListener {
            startActivity(SquareDetailsActivity::class.java)
        }
    }

    override fun initLoadData() {
        initSquare("$currentPage")
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

    fun squareDataSuccess(success: List<DataX>) {
        if (!mainActivity!!.isFinishing()) {
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
        if (!mainActivity!!.isFinishing()) {
            showCustomToast(ScreenManager.dipTopx(activity, 20f), ScreenManager.dipTopx(activity, 20f),
                    18, resources.getColor(com.mobile.rxjava2andretrofit2.R.color.white),
                    resources.getColor(com.mobile.rxjava2andretrofit2.R.color.color_FFE066FF), ScreenManager.dipTopx(activity, 40f),
                    ScreenManager.dipTopx(activity, 20f), error)

            hideLoading()
        }
    }

    override fun onDestroyView() {
        datax.title = null
        datax.chapterName = null
        datax.link = null
        viewModel!!.getDataxSuccess().removeObservers(this)
        super.onDestroyView()
    }

    private fun initSquare(currentPage: String) {
        showLoading()
        if (RetrofitManager.isNetworkAvailable(mainActivity)) {
            viewModel!!.squareData(currentPage)
        } else {
            squareDataError(MineApplication.getInstance().resources.getString(R.string.please_check_the_network_connection));
        }
    }

}