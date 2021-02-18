package com.mobile.rxjava2andretrofit2.kotlin.project.fragment

import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import com.mobile.rxjava2andretrofit2.MineApplication
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.base.BaseMvvmFragment
import com.mobile.rxjava2andretrofit2.callback.RcvOnItemViewClickListener
import com.mobile.rxjava2andretrofit2.databinding.FragmentProjectChildBinding
import com.mobile.rxjava2andretrofit2.kotlin.project.adapter.ProjectAdapter
import com.mobile.rxjava2andretrofit2.kotlin.project.bean.DataX
import com.mobile.rxjava2andretrofit2.kotlin.project.ui.SurfaceViewActivity
import com.mobile.rxjava2andretrofit2.kotlin.project.ui.VideoViewActivity
import com.mobile.rxjava2andretrofit2.kotlin.project.view_model.ProjectViewModelImpl
import com.mobile.rxjava2andretrofit2.main.MainActivity
import com.mobile.rxjava2andretrofit2.manager.LogManager
import com.mobile.rxjava2andretrofit2.manager.RetrofitManager
import com.mobile.rxjava2andretrofit2.manager.ScreenManager
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_project_child.*


class ProjectChildFragment : BaseMvvmFragment<ProjectViewModelImpl, FragmentProjectChildBinding>() {

    companion object {
        private val TAG: String = "ProjectChildFragment"
    }

    private var projectAdapter: ProjectAdapter? = null
    private var mainActivity: MainActivity? = null
    private var dataList: MutableList<DataX> = mutableListOf()
    private var isRefresh: Boolean = true
    private var currentPage: Int = 1
    private var dataxSuccessObserver: Observer<List<DataX>>? = null;
    private var dataxErrorObserver: Observer<String>? = null;

    override fun initLayoutId(): Int {
        return R.layout.fragment_project_child
    }

    override fun initViewModel(): ProjectViewModelImpl {
        return ViewModelProvider(this).get(ProjectViewModelImpl::class.java)
    }

    override fun initData() {
        mainActivity = activity as MainActivity
//        mDatabind.setVariable()
    }

    override fun initObservers() {
        dataxSuccessObserver = object : Observer<List<DataX>> {
            override fun onChanged(t: List<DataX>?) {
                if (t != null && t.size > 0) {
                    LogManager.i(TAG, "onChanged*****dataxSuccessObserver")
//                    LogManager.i(TAG, "onChanged*****${t.toString()}")
                    projectDataSuccess(t)
                } else {
                    projectDataError(MineApplication.getInstance().resources.getString(R.string.no_data_available))
                }
            }

        }

        dataxErrorObserver = object : Observer<String> {
            override fun onChanged(t: String?) {
                if (!TextUtils.isEmpty(t)) {
                    LogManager.i(TAG, "onChanged*****dataxErrorObserver")
                    projectDataError(t!!)
                }
            }

        }

        viewModel!!.getDataxSuccess().observe(this, dataxSuccessObserver!!)

        viewModel!!.getDataxError().observe(this, dataxErrorObserver!!)
    }

    override fun initViews() {
        projectAdapter = ProjectAdapter(mainActivity!!);
        projectAdapter!!.setRcvOnItemViewClickListener(object : RcvOnItemViewClickListener {

            override fun onItemClickListener(position: Int, view: View?) {
//                startActivity(VideoView2Activity::class.java)
                startActivity(VideoViewActivity::class.java)
            }

        })
        rcv_data.itemAnimator = DefaultItemAnimator()
        rcv_data.adapter = projectAdapter

        refresh_layout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refresh_layout: RefreshLayout) {
                LogManager.i(TAG, "onLoadMore")
                isRefresh = false
                initProject("$currentPage")
            }

            override fun onRefresh(refresh_layout: RefreshLayout) {
                LogManager.i(TAG, "onRefresh")
                isRefresh = true
                currentPage = 1;
                initProject("$currentPage")
            }
        })
    }

    override fun initLoadData() {
        refresh_layout.autoRefresh()
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

    fun projectDataSuccess(success: List<DataX>) {
        if (!mainActivity!!.isFinishing()) {
            if (isRefresh) {
                dataList.clear()
                dataList.addAll(success)
                projectAdapter!!.clearData();
                projectAdapter!!.addAllData(dataList)
                refresh_layout.finishRefresh()
            } else {
                dataList.addAll(success)
                projectAdapter!!.clearData();
                projectAdapter!!.addAllData(dataList)
                refresh_layout.finishLoadMore()
            }
            currentPage++;
            hideLoading()
        }
    }

    fun projectDataError(error: String) {
        if (!mainActivity!!.isFinishing()) {
            showCustomToast(ScreenManager.dipTopx(activity, 20f), ScreenManager.dipTopx(activity, 20f),
                    18, resources.getColor(com.mobile.rxjava2andretrofit2.R.color.white),
                    resources.getColor(com.mobile.rxjava2andretrofit2.R.color.color_FFE066FF), ScreenManager.dipTopx(activity, 40f),
                    ScreenManager.dipTopx(activity, 20f), error)

            if (isRefresh) {
                refresh_layout.finishRefresh(false)
            } else {
                refresh_layout.finishLoadMore(false)
            }
            hideLoading()
        }
    }

    private fun initProject(currentPage: String) {
        showLoading()
        if (RetrofitManager.isNetworkAvailable(mainActivity)) {
            viewModel!!.projectData(currentPage)
        } else {
            projectDataError(MineApplication.getInstance().resources.getString(R.string.please_check_the_network_connection));
        }
    }

    override fun onDestroyView() {
//        viewModel!!.getDataxSuccess().removeObserver(dataxSuccessObserver!!)
//        viewModel!!.getDataxError().removeObserver(dataxErrorObserver!!)

        viewModel!!.getDataxSuccess().removeObservers(this)
        super.onDestroyView()
    }

}