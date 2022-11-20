package com.phone.resource_module.fragment

import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phone.common_library.BaseApplication
import com.phone.common_library.base.BaseMvvmRxFragment
import com.phone.common_library.manager.LogManager
import com.phone.common_library.manager.RetrofitManager
import com.phone.common_library.manager.ScreenManager
import com.phone.resource_module.R
import com.phone.resource_module.adapter.ResourceAdapter
import com.phone.resource_module.bean.ArticleListBean
import com.phone.resource_module.databinding.FragmentResourceChildBinding
import com.phone.resource_module.view.IResourceChildView
import com.phone.square_module.view_model.ResourceChildViewModelImpl
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_resource_child.*

class ResourceChildFragment :
    BaseMvvmRxFragment<ResourceChildViewModelImpl, FragmentResourceChildBinding>(),
    IResourceChildView {

    private val TAG = ResourceChildFragment::class.java.simpleName

    private lateinit var dataxSuccessObserver: Observer<MutableList<ArticleListBean>>
    private lateinit var dataxErrorObserver: Observer<String>
    private val resourceAdapter by lazy { ResourceAdapter(rxAppCompatActivity) }
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

    override fun initLayoutId() = R.layout.fragment_resource_child

    override fun initViewModel() =
        ViewModelProvider(rxAppCompatActivity).get(ResourceChildViewModelImpl::class.java)

    override fun initData() {
        type = arguments?.getInt("type") ?: 0
        tabId = arguments?.getInt("tabId") ?: 0
    }

    override fun initObservers() {
        dataxSuccessObserver = object : Observer<MutableList<ArticleListBean>> {
            override fun onChanged(t: MutableList<ArticleListBean>?) {
                if (t != null && t.size > 0) {
                    LogManager.i(TAG, "onChanged*****dataxSuccessObserver")
//                    LogManager.i(TAG, "onChanged*****${t.toString()}")
                    resourceDataSuccess(t)
                } else {
                    resourceDataError(BaseApplication.getInstance().resources.getString(R.string.no_data_available))
                }
            }
        }

        dataxErrorObserver = object : Observer<String> {
            override fun onChanged(t: String?) {
                if (!TextUtils.isEmpty(t)) {
                    LogManager.i(TAG, "onChanged*****dataxErrorObserver")
                    resourceDataError(t!!)
                }
            }
        }

        viewModel.dataxRxFragmentSuccess.observe(this, dataxSuccessObserver)
        viewModel.dataxRxFragmentError.observe(this, dataxErrorObserver)
    }

    override fun initViews() {
        initAdapter()
    }

    private fun initAdapter() {
        linearLayoutManager = LinearLayoutManager(rxAppCompatActivity)
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL)
        mDatabind.rcvData.layoutManager = linearLayoutManager
        mDatabind.rcvData.itemAnimator = DefaultItemAnimator()
        (mDatabind.rcvData.itemAnimator as DefaultItemAnimator).changeDuration = 0

        resourceAdapter.apply {
            setRcvOnItemViewClickListener { i, view ->
                when (view.id) {
                    //收藏
                    R.id.ivCollect -> {
//                        this@ResourceChildFragment.resourceAdapter.list[i].apply {
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
        mDatabind.rcvData.setAdapter(resourceAdapter)

        mDatabind.refreshLayout.setOnRefreshLoadMoreListener(
            object : OnRefreshLoadMoreListener {
                override fun onLoadMore(refreshLayout: RefreshLayout) {
                    LogManager.i(TAG, "onLoadMore")
                    isRefresh = false
                    pageNum++
                    initResource(tabId, pageNum)
                }

                override fun onRefresh(refreshLayout: RefreshLayout) {
                    LogManager.i(TAG, "onRefresh")
                    isRefresh = true
                    pageNum = 1
                    initResource(tabId, pageNum)
                }
            })
    }

    override fun initLoadData() {
        mDatabind.refreshLayout.autoRefresh()
    }

    override fun showLoading() {
        if (!mDatabind.loadView.isShown()) {
            load_view.setVisibility(View.VISIBLE)
            load_view.start()
        }
    }

    override fun hideLoading() {
        if (mDatabind.loadView.isShown()) {
            load_view.stop()
            load_view.setVisibility(View.GONE)
        }
    }

    override fun resourceDataSuccess(success: MutableList<ArticleListBean>) {
        if (!rxAppCompatActivity.isFinishing()) {
            if (isRefresh) {
                resourceAdapter.clearData()
                resourceAdapter.addData(success)
                mDatabind.refreshLayout.finishRefresh()
            } else {
                resourceAdapter.addData(success)
                mDatabind.refreshLayout.finishLoadMore()
            }

        }
        hideLoading()
    }

    override fun resourceDataError(error: String) {
        if (!rxAppCompatActivity.isFinishing()) {
            showCustomToast(
                ScreenManager.dpToPx(rxAppCompatActivity, 20f),
                ScreenManager.dpToPx(rxAppCompatActivity, 20f),
                18,
                ContextCompat.getColor(rxAppCompatActivity, R.color.white),
                ContextCompat.getColor(rxAppCompatActivity, R.color.color_FFE066FF),
                ScreenManager.dpToPx(rxAppCompatActivity, 40f),
                ScreenManager.dpToPx(rxAppCompatActivity, 20f),
                error,
                true
            )

            if (isRefresh) {
                mDatabind.refreshLayout.finishRefresh(false)
            } else {
                mDatabind.refreshLayout.finishLoadMore(false)
            }
        }
        hideLoading()
    }

    private fun initResource(tabId: Int, pageNum: Int) {
        if (RetrofitManager.isNetworkAvailable(rxAppCompatActivity)) {
            showLoading()
            viewModel.resourceData(this, tabId, pageNum)
        } else {
            resourceDataError(resources.getString(R.string.please_check_the_network_connection))
            if (isRefresh) {
                mDatabind.refreshLayout.finishRefresh()
            } else {
                mDatabind.refreshLayout.finishLoadMore()
            }
        }
    }


}