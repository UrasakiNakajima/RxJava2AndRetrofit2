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
import com.mobile.rxjava2andretrofit2.kotlin.square.view_model.SquareViewModelImpl
import com.mobile.rxjava2andretrofit2.main.MainActivity
import com.mobile.rxjava2andretrofit2.manager.LogManager
import com.mobile.rxjava2andretrofit2.manager.RetrofitManager
import com.mobile.rxjava2andretrofit2.manager.ScreenManager
import kotlinx.android.synthetic.main.fragment_square.*

class SquareFragment() : BaseMvvmFragment<SquareViewModelImpl, FragmentSquareBinding>() {

    companion object {
        private val TAG: String = "SquareFragment"
    }

    private var viewModel: SquareViewModelImpl? = null
    private var mainActivity: MainActivity? = null
    //    private var dataList: MutableList<DataX> = mutableListOf()
    private var currentPage: Int = 1
    private var dataxSuccessObserver: Observer<List<DataX>>? = null;
    private var dataxErrorObserver: Observer<String>? = null;
    private var datax: DataX = DataX()

    override fun initLayoutId(): Int {
        return R.layout.fragment_square
    }

    override fun initData() {
        mainActivity = activity as MainActivity
        viewModel = ViewModelProvider(this).get(SquareViewModelImpl::class.java)

        mDatabind.datax = datax
    }

    override fun initViews() {

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

    override fun initLoadData() {
        initSquare("$currentPage")
    }

    fun showLoading() {
        if (load_view != null && !load_view.isShown()) {
            load_view.setVisibility(View.VISIBLE)
            load_view.start()
        }
    }

    fun hideLoading() {
        if (load_view != null && load_view.isShown()) {
            load_view.stop()
            load_view.setVisibility(View.GONE)
        }
    }

    fun squareDataSuccess(success: List<DataX>) {
        if (!mainActivity!!.isFinishing()) {
            if (success.size > 0) {
                datax.title = success.get(0).title
                datax.chapterName = success.get(0).chapterName
                datax.link = success.get(0).link
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
        viewModel!!.getDataxSuccess().removeObservers(this)
        viewModelStore.clear()
        super.onDestroyView()
    }

    private fun initSquare(currentPage: String) {
        showLoading()
        if (RetrofitManager.isNetworkAvailable(mainActivity)) {
            viewModel?.squareData(currentPage)
        } else {
            squareDataError(MineApplication.getInstance().resources.getString(R.string.please_check_the_network_connection));
        }
    }

}