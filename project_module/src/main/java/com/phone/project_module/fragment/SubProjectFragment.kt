package com.phone.project_module.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phone.common_library.BaseApplication
import com.phone.common_library.adapter.ProjectAndResourceAdapter
import com.phone.common_library.base.BaseMvvmRxFragment
import com.phone.common_library.bean.ArticleListBean
import com.phone.common_library.manager.LogManager
import com.phone.common_library.manager.ResourcesManager
import com.phone.common_library.manager.RetrofitManager
import com.phone.common_library.manager.ScreenManager
import com.phone.common_library.ui.WebViewActivity
import com.phone.project_module.R
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

    override fun initLayoutId() = R.layout.fragment_project_sub

    /**
     * 这里ViewModelProvider的参数要使用this，不要使用rxAppCompatActivity
     */
    override fun initViewModel() =
        ViewModelProvider(this).get(SubProjectViewModelImpl::class.java)

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
            } else {
                subProjectDataError(ResourcesManager.getString(R.string.no_data_available))
            }
        })
        viewModel.dataxRxFragmentError.observe(this, {
            it?.let {
                LogManager.i(TAG, "onChanged*****dataxErrorObserver")
                subProjectDataError(it)
            }
        })
    }

    override fun initViews() {
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
                setRcvOnItemViewClickListener { i, view ->
                    when (view.id) {
                        //子项
                        R.id.root -> {
                            val intent = Intent(mRxAppCompatActivity, WebViewActivity::class.java)
                            intent.putExtras(Bundle().apply {
                                subProjectAdapter?.list?.get(i)?.let {
                                    putString(
                                        "loadUrl",
                                        it.link
                                    )
                                    putString(
                                        "title",
                                        it.title
                                    )
                                    putString(
                                        "author",
                                        it.author
                                    )
                                    putInt(
                                        "id",
                                        it.id
                                    )
                                }
                            })
                            startActivity(intent)
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
        if (!mRxAppCompatActivity.isFinishing()) {
            if (!mDatabind.loadView.isShown()) {
                mDatabind.loadView.setVisibility(View.VISIBLE)
                mDatabind.loadView.start()
            }
        }
    }

    override fun hideLoading() {
        if (!mRxAppCompatActivity.isFinishing()) {
            if (mDatabind.loadView.isShown()) {
                mDatabind.loadView.stop()
                mDatabind.loadView.setVisibility(View.GONE)
            }
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
                ResourcesManager.getColor(R.color.color_FFE066FF),
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
            subProjectDataError(BaseApplication.get().resources.getString(R.string.please_check_the_network_connection));
        }
    }

}