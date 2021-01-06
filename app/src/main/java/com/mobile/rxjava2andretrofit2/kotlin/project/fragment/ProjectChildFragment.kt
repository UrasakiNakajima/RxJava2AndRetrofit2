package com.mobile.rxjava2andretrofit2.kotlin.project.fragment

import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.base.BaseMvvmFragment
import com.mobile.rxjava2andretrofit2.databinding.FragmentProjectChildBinding
import com.mobile.rxjava2andretrofit2.kotlin.project.adapter.ProjectAdapter
import com.mobile.rxjava2andretrofit2.kotlin.project.bean.DataX
import com.mobile.rxjava2andretrofit2.kotlin.project.view_model.ProjectViewModelImpl
import com.mobile.rxjava2andretrofit2.main.MainActivity
import com.mobile.rxjava2andretrofit2.manager.LogManager
import com.mobile.rxjava2andretrofit2.manager.ScreenManager
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_project_child.*


class ProjectChildFragment : BaseMvvmFragment<ProjectViewModelImpl, FragmentProjectChildBinding>() {

    private val TAG: String = "ProjectChildFragment"
    private var projectViewModel: ProjectViewModelImpl? = null
    private var projectAdapter: ProjectAdapter? = null
    private var mainActivity: MainActivity? = null
    private var dataList: MutableList<DataX> = mutableListOf()
    private var isRefresh: Boolean = true
    private var currentPage: Int = 1

    override fun initLayoutId(): Int {
        return R.layout.fragment_project_child
    }

    override fun initData() {
        mainActivity = activity as MainActivity;
        projectViewModel = ProjectViewModelImpl()
//        mDatabind.setVariable()

        projectViewModel!!.dataxSuccess.observe(this, object : Observer<List<DataX>> {
            override fun onChanged(t: List<DataX>?) {
                if (t != null && t.size > 0) {
                    projectDataSuccess(t);
                }
            }
        })

        projectViewModel!!.dataxError.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
                if (!TextUtils.isEmpty(t)) {
                    projectDataError(t!!)
                } else {
//                        projectDataError()
                }
            }
        })
    }

    override fun initViews() {
        projectAdapter = ProjectAdapter(mainActivity!!);
        rcv_data.itemAnimator = DefaultItemAnimator()
        rcv_data.adapter = projectAdapter

        refresh_layout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refresh_layout: RefreshLayout) {
                LogManager.i(TAG, "onLoadMore")
                isRefresh = false
                inintProject("$currentPage")
            }

            override fun onRefresh(refresh_layout: RefreshLayout) {
                LogManager.i(TAG, "onRefresh")
                isRefresh = true
                currentPage = 1;
                inintProject("$currentPage")
            }
        })
    }

    override fun initLoadData() {
        refresh_layout.autoRefresh()
    }

    private fun inintProject(currentPage: String) {
        showLoading()
        projectViewModel!!.projectData(currentPage)
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
        }
        hideLoading()
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
        }
        hideLoading()
    }


}