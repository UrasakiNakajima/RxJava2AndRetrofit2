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
import com.phone.common_library.bean.ArticleListBean
import com.phone.common_library.manager.LogManager
import com.phone.common_library.manager.RetrofitManager
import com.phone.common_library.manager.ScreenManager
import com.phone.resource_module.R
import com.phone.resource_module.adapter.SubResourceAdapter
import com.phone.resource_module.databinding.FragmentResourceSubBinding
import com.phone.resource_module.view.ISubResourceView
import com.phone.square_module.view_model.SubResourceViewModelImpl
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class SubResourceFragment :
    BaseMvvmRxFragment<SubResourceViewModelImpl, FragmentResourceSubBinding>(),
    ISubResourceView {

    private val TAG = SubResourceFragment::class.java.simpleName

    private val subResourceAdapter by lazy { SubResourceAdapter(rxAppCompatActivity) }
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

    override fun initLayoutId() = R.layout.fragment_resource_sub

    override fun initViewModel() =
        ViewModelProvider(rxAppCompatActivity).get(SubResourceViewModelImpl::class.java)

    override fun initData() {
        type = arguments?.getInt("type") ?: 0
        tabId = arguments?.getInt("tabId") ?: 0
    }

    override fun initObservers() {
        viewModel.dataxRxFragmentSuccess.observe(this, {
            if (it != null && it.size > 0) {
                LogManager.i(TAG, "onChanged*****dataxSuccessObserver")
//                    LogManager.i(TAG, "onChanged*****${t.toString()}")
                subResourceDataSuccess(it)
            } else {
                subResourceDataError(BaseApplication.getInstance().resources.getString(R.string.no_data_available))
            }
        })
        viewModel.dataxRxFragmentError.observe(this, {
            if (!TextUtils.isEmpty(it)) {
                LogManager.i(TAG, "onChanged*****dataxErrorObserver")
                subResourceDataError(it)
            }
        })
    }

    override fun initViews() {
        initAdapter()
    }

    private fun initAdapter() {
        linearLayoutManager = LinearLayoutManager(rxAppCompatActivity)
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL)
        mDatabind.rcvData.apply {
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()
            (itemAnimator as DefaultItemAnimator).changeDuration = 0
            subResourceAdapter.apply {
                setRcvOnItemViewClickListener { i, view ->
                    when (view.id) {
                        //收藏
                        R.id.ivCollect -> {
//                        this@ResourceChildFragment.subResourceAdapter.list[i].apply {
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
            setAdapter(subResourceAdapter)
        }

        mDatabind.refreshLayout.setOnRefreshLoadMoreListener(
            object : OnRefreshLoadMoreListener {
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

    override fun initLoadData() {
        mDatabind.refreshLayout.autoRefresh()
    }

    override fun showLoading() {
        if (!mDatabind.loadView.isShown()) {
            mDatabind.loadView.setVisibility(View.VISIBLE)
            mDatabind.loadView.start()
        }
    }

    override fun hideLoading() {
        if (mDatabind.loadView.isShown()) {
            mDatabind.loadView.stop()
            mDatabind.loadView.setVisibility(View.GONE)
        }
    }

    override fun subResourceDataSuccess(success: MutableList<ArticleListBean>) {
        if (!rxAppCompatActivity.isFinishing()) {
            if (isRefresh) {
                subResourceAdapter.clearData()
                subResourceAdapter.addData(success)
                mDatabind.refreshLayout.finishRefresh()
            } else {
                subResourceAdapter.addData(success)
                mDatabind.refreshLayout.finishLoadMore()
            }

        }
        hideLoading()
    }

    override fun subResourceDataError(error: String) {
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

    private fun initSubResource(tabId: Int, pageNum: Int) {
        if (RetrofitManager.isNetworkAvailable(rxAppCompatActivity)) {
            showLoading()
            viewModel.subResourceData(tabId, pageNum)
        } else {
            subResourceDataError(resources.getString(R.string.please_check_the_network_connection))
            if (isRefresh) {
                mDatabind.refreshLayout.finishRefresh()
            } else {
                mDatabind.refreshLayout.finishLoadMore()
            }
        }
    }


}