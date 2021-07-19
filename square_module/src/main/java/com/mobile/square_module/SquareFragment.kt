package com.mobile.square_module

import android.os.AsyncTask
import android.os.SystemClock
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.mobile.common_library.BaseApplication
import com.mobile.common_library.base.BaseMvvmFragment
import com.mobile.common_library.manager.LogManager
import com.mobile.common_library.manager.RetrofitManager
import com.mobile.common_library.manager.ScreenManager
import com.mobile.square_module.bean.DataX
import com.mobile.square_module.databinding.FragmentSquareBinding
import com.mobile.square_module.ui.SquareDetailsActivity
import com.mobile.square_module.view_model.SquareViewModelImpl

@Route(path = "/square_module/square")
class SquareFragment() : BaseMvvmFragment<SquareViewModelImpl, FragmentSquareBinding>() {

    companion object {
        private val TAG: String = "SquareFragment"
    }

    //    private var mainActivity: MainActivity? = null
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
//        mainActivity = activity as MainActivity
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

//        startAsyncTask()
    }

    private fun startAsyncTask() {

        // This async task is an anonymous class and therefore has a hidden reference to the outer
        // class MainActivity. If the activity gets destroyed before the task finishes (e.g. rotation),
        // the activity instance will leak.
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                // Do some slow work in background
                SystemClock.sleep(10000)
                return null
            }
        }.execute()
        Toast.makeText(appCompatActivity, "请关闭这个A完成泄露", Toast.LENGTH_SHORT).show()
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
        if (!activity!!.isFinishing()) {
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
        if (!activity!!.isFinishing()) {
            showCustomToast(ScreenManager.dpToPx(activity, 20f), ScreenManager.dpToPx(activity, 20f),
                    18, resources.getColor(R.color.white),
                    resources.getColor(R.color.color_FFE066FF), ScreenManager.dpToPx(activity, 40f),
                    ScreenManager.dpToPx(activity, 20f), error)

            hideLoading()
        }
    }

    private fun initSquare(currentPage: String) {
        showLoading()
        if (RetrofitManager.isNetworkAvailable(activity)) {
            viewModel!!.squareData(this, currentPage)
        } else {
            squareDataError(BaseApplication.getInstance().resources.getString(R.string.please_check_the_network_connection));
        }
    }

    override fun onDestroyView() {
        viewModel!!.getDataxSuccess().removeObservers(this)
        super.onDestroyView()
    }

}