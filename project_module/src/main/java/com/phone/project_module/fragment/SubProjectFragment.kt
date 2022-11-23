package com.phone.project_module.fragment

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
import com.phone.project_module.R
import com.phone.project_module.adapter.SubProjectAdapter
import com.phone.project_module.databinding.FragmentProjectSubBinding
import com.phone.project_module.view.ISubProjectView
import com.phone.project_module.view_model.SubProjectViewModelImpl
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class SubProjectFragment : BaseMvvmRxFragment<SubProjectViewModelImpl, FragmentProjectSubBinding>(),
    ISubProjectView {

    companion object {
        private val TAG: String = SubProjectFragment::class.java.simpleName
    }

    private val subProjectAdapter by lazy { SubProjectAdapter(rxAppCompatActivity) }
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

    override fun initLayoutId() = R.layout.fragment_project_sub

    override fun initViewModel() =
        ViewModelProvider(rxAppCompatActivity).get(SubProjectViewModelImpl::class.java)

    override fun initData() {
        type = arguments?.getInt("type") ?: 0
        tabId = arguments?.getInt("tabId") ?: 0
    }

    override fun initObservers() {
        viewModel.dataxRxFragmentSuccess.observe(this, {
            if (it != null && it.size > 0) {
                LogManager.i(TAG, "onChanged*****dataxSuccessObserver")
//                    LogManager.i(TAG, "onChanged*****${t.toString()}")
                subProjectDataSuccess(it)
                hideLoading()
            } else {
                subProjectDataError(BaseApplication.getInstance().resources.getString(R.string.no_data_available))
                hideLoading()
            }
        })
        viewModel.dataxRxFragmentError.observe(this, {
            if (!TextUtils.isEmpty(it)) {
                LogManager.i(TAG, "onChanged*****dataxErrorObserver")
                subProjectDataError(it)
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
            subProjectAdapter.apply {
                setRcvOnItemViewClickListener { i, view ->
                    when (view.id) {
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

        mDatabind.refreshLayout.setOnRefreshLoadMoreListener(
            object : OnRefreshLoadMoreListener {
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

    override fun initLoadData() {
        LogManager.i(TAG, "initLoadData")
        mDatabind.refreshLayout.autoRefresh()

        LogManager.i(TAG, "SubProjectFragment initLoadData")
    }

    override fun showLoading() {
        if (!rxAppCompatActivity.isFinishing()) {
            if (!mDatabind.loadView.isShown()) {
                mDatabind.loadView.setVisibility(View.VISIBLE)
                mDatabind.loadView.start()
            }
        }
    }

    override fun hideLoading() {
        if (!rxAppCompatActivity.isFinishing()) {
            if (mDatabind.loadView.isShown()) {
                mDatabind.loadView.stop()
                mDatabind.loadView.setVisibility(View.GONE)
            }
        }
    }

    override fun subProjectDataSuccess(success: MutableList<ArticleListBean>) {
        if (!rxAppCompatActivity.isFinishing()) {
            if (isRefresh) {
                subProjectAdapter.clearData();
                subProjectAdapter.addData(success)
                mDatabind.refreshLayout.finishRefresh()
            } else {
                subProjectAdapter.addData(success)
                mDatabind.refreshLayout.finishLoadMore()
            }
        }
    }

    override fun subProjectDataError(error: String) {
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
    }

    private fun initSubProject(pageNum: Int, tabId: Int) {
        showLoading()
        if (RetrofitManager.isNetworkAvailable(rxAppCompatActivity)) {
            viewModel.subProjectData(pageNum, tabId)
        } else {
            subProjectDataError(BaseApplication.getInstance().resources.getString(R.string.please_check_the_network_connection));
        }
    }

}