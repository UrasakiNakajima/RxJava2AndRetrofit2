package com.phone.module_project.fragment

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.phone.library_common.BaseApplication
import com.phone.library_common.adapter.ProjectAndResourceAdapter
import com.phone.library_common.base.BaseMvvmRxFragment
import com.phone.library_common.base.State
import com.phone.library_common.bean.ArticleListBean
import com.phone.library_common.common.ConstantData
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.ResourcesManager
import com.phone.library_common.manager.RetrofitManager
import com.phone.library_common.manager.ScreenManager
import com.phone.module_project.R
import com.phone.module_project.databinding.ProjectFragmentProjectSubBinding
import com.phone.module_project.view.ISubProjectView
import com.phone.module_project.view_model.SubProjectViewModelImpl
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class SubProjectFragment :
    BaseMvvmRxFragment<SubProjectViewModelImpl, ProjectFragmentProjectSubBinding>(),
    ISubProjectView {

    companion object {
        private val TAG: String = SubProjectFragment::class.java.simpleName
    }

    private var subProjectAdapter: ProjectAndResourceAdapter? = null
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var isRefresh: Boolean = true

    /**
     * fragment类型，项目或公号
     */
    private var type = 0

    /**
     * tab的id
     */
    private var tabId: Int = 0
    private var pageNum: Int = 1

    override fun initLayoutId() = R.layout.project_fragment_project_sub

    /**
     * 这里ViewModelProvider的参数要使用this，不要使用rxAppCompatActivity
     */
    override fun initViewModel() = ViewModelProvider(this).get(SubProjectViewModelImpl::class.java)

    override fun initData() {
        type = arguments?.getInt("type") ?: 0
        tabId = arguments?.getInt("tabId") ?: 0
    }

    override fun initObservers() {
        viewModel.dataxRxFragment.observe(this, {
            when (it) {
                is State.SuccessState -> {
                    if (it.list != null && it.list.size > 0) {
                        LogManager.i(TAG, "onChanged*****tabRxFragmentSuccess")
                        subProjectDataSuccess(it.list)
                    } else {
                        subProjectDataError(ResourcesManager.getString(R.string.no_data_available))
                    }
                }

                is State.ErrorState -> {
                    subProjectDataError(it.errorMsg)
                }
            }
        })
    }

    override fun initViews() {
        mDatabind.refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                LogManager.i(TAG, "onLoadMore")
                isRefresh = false
                pageNum++
                initSubProject(pageNum, tabId)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                LogManager.i(TAG, "onRefresh")
                isRefresh = true
                pageNum = 1
                initSubProject(pageNum, tabId)
            }
        })
    }

    private fun initAdapter(list: MutableList<ArticleListBean>) {
        linearLayoutManager = LinearLayoutManager(mRxAppCompatActivity)
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL)
        mDatabind.rcvData.apply {
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()
//            (itemAnimator as DefaultItemAnimator).changeDuration = 0
//            (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

            subProjectAdapter = ProjectAndResourceAdapter(mRxAppCompatActivity, list)
            subProjectAdapter?.apply {
                //kotlin 使用这个方法需要初始化ProjectAndResourceAdapter 的时候把list 传进去，
                //不然就会报VirtualLayout：Cannot change whether this adapter has stable IDs while the adapter has registered observers.
                setHasStableIds(true)
                setOnItemViewClickListener { i, view ->
                    when (view.id) {
                        //子项
                        R.id.root -> {
                            subProjectAdapter?.list?.get(i)?.let {
                                ARouter.getInstance().build(ConstantData.Route.ROUTE_WEB_VIEW)
                                    .withString("loadUrl", it.link).withString("title", it.title)
                                    .withString("author", it.author).withInt("id", it.id)
                                    .navigation()
                            }
                        }
                        //收藏
                        R.id.ivCollect -> {
//                        this@ResourceChildFragment.subProjectAdapter.list[i].apply {
//                            //已收藏取消收藏
//                            if (collect) {
//                                viewModel.unCollect(id)
//                            } else {
//                                viewModel.collect(id)
//                            }
//                        }
                        }
                    }
                }
            }
            setAdapter(subProjectAdapter)
        }
    }

    override fun initLoadData() {
        LogManager.i(TAG, "initLoadData")
        mDatabind.refreshLayout.autoRefresh()

        LogManager.i(TAG, "SubProjectFragment initLoadData")
    }

    override fun showLoading() {
        if (!mRxAppCompatActivity.isFinishing() && !mDatabind.loadView.isShown()) {
            mDatabind.loadView.setVisibility(View.VISIBLE)
            mDatabind.loadView.start()
        }
    }

    override fun hideLoading() {
        if (!mRxAppCompatActivity.isFinishing() && mDatabind.loadView.isShown()) {
            mDatabind.loadView.stop()
            mDatabind.loadView.setVisibility(View.GONE)
        }
    }

    override fun subProjectDataSuccess(success: MutableList<ArticleListBean>) {
        if (!mRxAppCompatActivity.isFinishing()) {
            if (isRefresh) {
                initAdapter(success)
                mDatabind.refreshLayout.finishRefresh()
            } else {
                subProjectAdapter?.addData(success)
                mDatabind.refreshLayout.finishLoadMore()
            }
            hideLoading()
        }
    }

    override fun subProjectDataError(error: String) {
        if (!mRxAppCompatActivity.isFinishing()) {
            showCustomToast(
                ScreenManager.dpToPx(20f),
                ScreenManager.dpToPx(20f),
                18,
                ResourcesManager.getColor(R.color.white),
                ResourcesManager.getColor(R.color.color_FF198CFF),
                ScreenManager.dpToPx(40f),
                ScreenManager.dpToPx(20f),
                error,
                true
            )

            if (isRefresh) {
                mDatabind.refreshLayout.finishRefresh(false)
            } else {
                mDatabind.refreshLayout.finishLoadMore(false)
            }
            hideLoading()
        }
    }

    private fun initSubProject(pageNum: Int, tabId: Int) {
        showLoading()
        if (RetrofitManager.isNetworkAvailable()) {
            viewModel.subProjectData(pageNum, tabId)
        } else {
            subProjectDataError(BaseApplication.instance().resources.getString(R.string.please_check_the_network_connection));
        }
    }

}