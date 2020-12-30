package com.mobile.rxjava2andretrofit2.kotlin.project.fragment

import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.base.BaseMvvmFragment
import com.mobile.rxjava2andretrofit2.databinding.FragmentProjectChildBinding
import com.mobile.rxjava2andretrofit2.kotlin.project.adapter.ProjectAdapter
import com.mobile.rxjava2andretrofit2.kotlin.project.bean.DataX
import com.mobile.rxjava2andretrofit2.kotlin.project.view.IProjectChildView
import com.mobile.rxjava2andretrofit2.kotlin.project.view_model.ProjectViewModelImpl
import com.mobile.rxjava2andretrofit2.main.MainActivity
import com.mobile.rxjava2andretrofit2.manager.ScreenManager
import kotlinx.android.synthetic.main.fragment_project_child.*

class ProjectChildFragment : BaseMvvmFragment<ProjectViewModelImpl, FragmentProjectChildBinding>(), IProjectChildView {

    private var currentPage: Int = 1;
    private var projectViewModel: ProjectViewModelImpl? = null;
    private var projectAdapter: ProjectAdapter? = null;
    private var mainActivity: MainActivity? = null
    private var list: MutableList<DataX> = mutableListOf()

    override fun initLayoutId(): Int {
        return R.layout.fragment_project_child
    }

    override fun initData() {
        mainActivity = activity as MainActivity;
        projectViewModel = ProjectViewModelImpl(this)
    }

    override fun initViews() {
        projectAdapter = ProjectAdapter(mainActivity!!);
        mine_recycler_view.itemAnimator = DefaultItemAnimator()
        mine_recycler_view.adapter = projectAdapter
    }

    override fun initLoadData() {
        projectViewModel!!.getProjectData("$currentPage")
    }

    override fun showLoading() {
        if (load_view != null && !load_view.isShown()) {
            load_view.setVisibility(View.VISIBLE)
            load_view.start()
        }
    }

    override fun hideLoading() {
        if (load_view != null && load_view.isShown()) {
            load_view.stop()
            load_view.setVisibility(View.GONE)
        }
    }

    override fun projectDataSuccess(success: List<DataX>) {
        list.clear()
        list.addAll(success)
        projectAdapter!!.clearData()
        projectAdapter!!.addAllData(list)
    }

    override fun projectDataError(error: String) {
        if (!mainActivity!!.isFinishing()) {
            showCustomToast(ScreenManager.dipTopx(activity, 20f), ScreenManager.dipTopx(activity, 20f),
                    18, resources.getColor(R.color.white),
                    resources.getColor(R.color.color_FFE066FF), ScreenManager.dipTopx(activity, 40f),
                    ScreenManager.dipTopx(activity, 20f), error)

//            if (isRefresh) {
//                refresh_layout.finishRefresh(false)
//            } else {
//                refresh_layout.finishLoadMore(false)
//            }
        }
    }


}