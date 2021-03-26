package com.mobile.project_module

import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import com.alibaba.android.arouter.facade.annotation.Route
import com.mobile.common_library.MineApplication
import com.mobile.common_library.base.BaseMvvmFragment
import com.mobile.common_library.callback.RcvOnItemViewClickListener
import com.mobile.common_library.manager.LogManager
import com.mobile.common_library.manager.RetrofitManager
import com.mobile.common_library.manager.ScreenManager
import com.mobile.project_module.adapter.ProjectAdapter
import com.mobile.project_module.bean.DataX
import com.mobile.project_module.databinding.FragmentProjectBinding
import com.mobile.project_module.ui.SurfaceViewActivity
import com.mobile.project_module.view.IProjectChildView
import com.mobile.project_module.view_model.ProjectViewModelImpl
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

@Route(path = "/project_module/project")
class ProjectFragment : BaseMvvmFragment<ProjectViewModelImpl, FragmentProjectBinding>(), IProjectChildView {

    companion object {
        private val TAG: String = "ProjectChildFragment"
    }

    private var projectAdapter: ProjectAdapter? = null
    private var dataList: MutableList<DataX> = mutableListOf()
    private var isRefresh: Boolean = true
    private var currentPage: Int = 1
    private var dataxSuccessObserver: Observer<List<DataX>>? = null;
    private var dataxErrorObserver: Observer<String>? = null;

    override fun initLayoutId(): Int {
        return R.layout.fragment_project
    }

    override fun initViewModel(): ProjectViewModelImpl {
        return ViewModelProvider(this).get(ProjectViewModelImpl::class.java)
    }

    override fun initData() {
//        mDatabind.setVariable()
    }

    override fun initObservers() {
        dataxSuccessObserver = object : Observer<List<DataX>> {
            override fun onChanged(t: List<DataX>?) {
                if (t != null && t.size > 0) {
                    LogManager.i(TAG, "onChanged*****dataxSuccessObserver")
//                    LogManager.i(TAG, "onChanged*****${t.toString()}")
                    projectDataSuccess(t)
                    hideLoading()
                } else {
                    projectDataError(MineApplication.getInstance().resources.getString(R.string.no_data_available))
                    hideLoading()
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
        projectAdapter = ProjectAdapter(activity!!);
        projectAdapter!!.setRcvOnItemViewClickListener(object : RcvOnItemViewClickListener {

            override fun onItemClickListener(position: Int, view: View?) {
//                startActivity(VideoViewActivity::class.java)
                startActivity(SurfaceViewActivity::class.java)
            }

        })
        mDatabind.rcvData.itemAnimator = DefaultItemAnimator()
        mDatabind.rcvData.adapter = projectAdapter

        mDatabind.refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                LogManager.i(TAG, "onLoadMore")
                isRefresh = false
                initProject("$currentPage")
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                LogManager.i(TAG, "onRefresh")
                isRefresh = true
                currentPage = 1;
                initProject("$currentPage")
            }
        })
    }

    override fun initLoadData() {
        mDatabind.refreshLayout.autoRefresh()
    }

    override fun showLoading() {
        if (!activity!!.isFinishing()) {
            if (mDatabind.loadView != null && !mDatabind.loadView.isShown()) {
                mDatabind.loadView.setVisibility(View.VISIBLE)
                mDatabind.loadView.start()
            }
        }
    }

    override fun hideLoading() {
        if (!activity!!.isFinishing()) {
            if (mDatabind.loadView != null && mDatabind.loadView.isShown()) {
                mDatabind.loadView.stop()
                mDatabind.loadView.setVisibility(View.GONE)
            }
        }
    }

    override fun projectDataSuccess(success: List<DataX>) {
        if (!activity!!.isFinishing()) {
            if (isRefresh) {
                dataList.clear()
                dataList.addAll(success)
                projectAdapter!!.clearData();
                projectAdapter!!.addAllData(dataList)
                mDatabind.refreshLayout.finishRefresh()
            } else {
                dataList.addAll(success)
                projectAdapter!!.clearData();
                projectAdapter!!.addAllData(dataList)
                mDatabind.refreshLayout.finishLoadMore()
            }
            currentPage++;
        }
    }

    override fun projectDataError(error: String) {
        if (!activity!!.isFinishing()) {
            showCustomToast(ScreenManager.dipTopx(activity, 20f), ScreenManager.dipTopx(activity, 20f),
                    18, resources.getColor(R.color.white),
                    resources.getColor(R.color.color_FFE066FF), ScreenManager.dipTopx(activity, 40f),
                    ScreenManager.dipTopx(activity, 20f), error)

            if (isRefresh) {
                mDatabind.refreshLayout.finishRefresh(false)
            } else {
                mDatabind.refreshLayout.finishLoadMore(false)
            }
        }
    }

    private fun initProject(currentPage: String) {
        showLoading()
        if (RetrofitManager.isNetworkAvailable(activity)) {
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