package com.phone.module_resource.fragment

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
import com.phone.module_resource.R
import com.phone.module_resource.databinding.ResourceFragmentResourceSubBinding
import com.phone.module_resource.view.ISubResourceView
import com.phone.module_square.view_model.SubResourceViewModelImpl
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class SubResourceFragment :
    BaseMvvmRxFragment<SubResourceViewModelImpl, ResourceFragmentResourceSubBinding>(),
    ISubResourceView {

    private val TAG = SubResourceFragment::class.java.simpleName

    private var subResourceAdapter: ProjectAndResourceAdapter? = null
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

    override fun initLayoutId() = R.layout.resource_fragment_resource_sub

    /**
     * 这里ViewModelProvider的参数要使用this，不要使用rxAppCompatActivity
     */
    override fun initViewModel() = ViewModelProvider(this).get(SubResourceViewModelImpl::class.java)

    override fun initData() {
        type = arguments?.getInt("type") ?: 0
        tabId = arguments?.getInt("tabId") ?: 0
    }

    override fun initObservers() {
        mViewModel.dataxRxFragment.observe(this, {
//            LogManager.i(TAG, "onChanged*****dataxRxFragment")
            when (it) {
                is State.SuccessState -> {
                    if (it.success.size > 0) {
//                    LogManager.i(TAG, "onChanged*****${t.toString()}")
                        subResourceDataSuccess(it.success)
                    } else {
                        subResourceDataError(BaseApplication.instance().resources.getString(R.string.library_no_data_available))
                    }
                }

                is State.ErrorState -> {
                    subResourceDataError(it.errorMsg)
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
                initSubResource(tabId, pageNum)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                LogManager.i(TAG, "onRefresh")
                isRefresh = true
                pageNum = 1
                initSubResource(tabId, pageNum)
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

            subResourceAdapter = ProjectAndResourceAdapter(mRxAppCompatActivity, list)
            subResourceAdapter?.apply {
                //kotlin 使用这个方法需要初始化ProjectAndResourceAdapter 的时候把list 传进去，
                //不然就会报VirtualLayout：Cannot change whether this adapter has stable IDs while the adapter has registered observers.
                setHasStableIds(true)
                setOnItemViewClickListener { i, view ->
                    when (view.id) {
                        //子项
                        R.id.root -> {
                            subResourceAdapter?.list?.get(i)?.let {
                                ARouter.getInstance().build(ConstantData.Route.ROUTE_WEB_VIEW)
                                    .withString("loadUrl", it.link).withString("title", it.title)
                                    .withString("author", it.author).withInt("id", it.id)
                                    .navigation()
                            }
                        }
                        //收藏
                        R.id.ivCollect -> {
//                        this@ResourceChildFragment.subResourceAdapter.list[i].apply {
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
            setAdapter(subResourceAdapter)
        }
    }

    override fun initLoadData() {
        if (isFirstLoad) {
            initSubResource(tabId, pageNum)
            LogManager.i(TAG, "initLoadData*****$TAG")
            isFirstLoad = false
        }
    }

    override fun subResourceDataSuccess(success: MutableList<ArticleListBean>) {
        if (!mRxAppCompatActivity.isFinishing()) {
            if (isRefresh) {
                if (subResourceAdapter == null) {
                    initAdapter(success)
                } else {
                    subResourceAdapter?.let {
                        it.clearData()
                        it.addData(success)
                    }
                }
                mDatabind.refreshLayout.finishRefresh()
            } else {
                subResourceAdapter?.addData(success)
                mDatabind.refreshLayout.finishLoadMore()
            }
            hideLoading()
        }
    }

    override fun subResourceDataError(error: String) {
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
                mDatabind.refreshLayout.finishRefresh(false)
            } else {
                mDatabind.refreshLayout.finishLoadMore(false)
            }
            hideLoading()
        }
    }

    private fun initSubResource(tabId: Int, pageNum: Int) {
        showLoading()
        ThreadPoolManager.instance().createScheduledThreadPoolToUIThread2(1000, {
            if (RetrofitManager.isNetworkAvailable()) {
                mViewModel.subResourceData(tabId, pageNum)
            } else {
                subResourceDataError(resources.getString(R.string.library_please_check_the_network_connection))
            }
        })
    }


}