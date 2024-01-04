package com.phone.module_project.fragment

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.phone.library_base.BaseApplication
import com.phone.library_custom_view.adapter.ProjectAndResourceAdapter
import com.phone.library_mvvm.BaseMvvmRxFragment
import com.phone.library_network.bean.State
import com.phone.library_custom_view.bean.ArticleListBean
import com.phone.library_base.common.ConstantData
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_network.manager.RetrofitManager
import com.phone.library_base.manager.ScreenManager
import com.phone.library_base.manager.ThreadPoolManager
import com.phone.module_project.R
import com.phone.module_project.databinding.ProjectFragmentProjectSubBinding
import com.phone.module_project.view.ISubProjectView
import com.phone.module_project.view_model.SubProjectViewModelImpl
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class SubProjectFragment :
    BaseMvvmRxFragment<SubProjectViewModelImpl, ProjectFragmentProjectSubBinding>(R.layout.project_fragment_project_sub),
    ISubProjectView {

    companion object {
        private val TAG: String = SubProjectFragment::class.java.simpleName
    }

    private val list = mutableListOf<ArticleListBean>()
    private val subProjectAdapter by lazy { ProjectAndResourceAdapter(mRxAppCompatActivity, list) }
    private val linearLayoutManager by lazy { LinearLayoutManager(mRxAppCompatActivity) }

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

//    override fun initLayoutId() = R.layout.project_fragment_project_sub

    /**
     * 这里ViewModelProvider的参数要使用this，不要使用rxAppCompatActivity
     */
    override fun initViewModel() = ViewModelProvider(this).get(SubProjectViewModelImpl::class.java)

    override fun initData() {
        type = arguments?.getInt("type") ?: 0
        tabId = arguments?.getInt("tabId") ?: 0
    }

    override fun initObservers() {
        mViewModel.subProjectData.observe(this, {
//            LogManager.i(TAG, "onChanged*****tabRxFragmentSuccess")
            when (it) {
                is State.SuccessState -> {
                    if (it.success.size > 0) {
                        subProjectDataSuccess(it.success)
                    } else {
                        subProjectDataError(ResourcesManager.getString(R.string.library_no_data_available))
                    }
                }

                is State.ErrorState -> {
                    subProjectDataError(it.errorMsg)
                }

                else -> {}
            }
        })
    }

    override fun initViews() {
        mDatabind?.refreshLayout?.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
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

        initAdapter()
    }

    private fun initAdapter() {
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL)
        mDatabind?.rcvData?.apply {
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()
//            (itemAnimator as DefaultItemAnimator).changeDuration = 0
//            (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

            subProjectAdapter.apply {
                //kotlin 使用这个方法需要初始化ProjectAndResourceAdapter 的时候把list 传进去，
                //不然就会报VirtualLayout：Cannot change whether this adapter has stable IDs while the adapter has registered observers.
                setHasStableIds(true)
                setOnItemViewClickListener { i, view ->
                    when (view.id) {
                        //子项
                        R.id.root -> {
                            subProjectAdapter.list.get(i).let {
                                ARouter.getInstance().build(ConstantData.Route.ROUTE_WEB_VIEW)
                                    .withString("loadUrl", it.link).withString("title", it.title)
                                    .withString("author", it.author).withInt("id", it.id)
                                    .navigation()
                            }
                        }
                        //收藏
                        R.id.imv_collect -> {
//                        this@ResourceChildFragment.subProjectAdapter.list[i].apply {
//                            //已收藏取消收藏
//                            if (collect) {
//                                mViewModel.unCollect(id)
//                            } else {
//                                mViewModel.collect(id)
//                            }
//                        }
                        }
                    }
                }
            }
            adapter = subProjectAdapter
        }
    }

    override fun initLoadData() {
        if (isFirstLoad) {
            initSubProject(pageNum, tabId)
            LogManager.i(TAG, "initLoadData*****$TAG")
            isFirstLoad = false
        }
    }

    override fun subProjectDataSuccess(success: MutableList<ArticleListBean>) {
        if (!mRxAppCompatActivity.isFinishing()) {
            if (isRefresh) {
                subProjectAdapter.let {
                    it.clearData()
                    it.addData(success)
                }
                mDatabind?.refreshLayout?.finishRefresh()
            } else {
                subProjectAdapter.addData(success)
                mDatabind?.refreshLayout?.finishLoadMore()
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
                ResourcesManager.getColor(R.color.library_white),
                ResourcesManager.getColor(R.color.library_color_FF198CFF),
                ScreenManager.dpToPx(40f),
                ScreenManager.dpToPx(20f),
                error
            )

            if (isRefresh) {
                mDatabind?.refreshLayout?.finishRefresh(false)
            } else {
                mDatabind?.refreshLayout?.finishLoadMore(false)
            }
            hideLoading()
        }
    }

    private fun initSubProject(pageNum: Int, tabId: Int) {
        showLoading()
        ThreadPoolManager.instance().createScheduledThreadPoolToUIThread2(1000, {
            if (RetrofitManager.isNetworkAvailable()) {
                mViewModel.subProjectData(pageNum, tabId)
            } else {
                subProjectDataError(BaseApplication.instance().resources.getString(R.string.library_please_check_the_network_connection));
            }
        })
    }

}