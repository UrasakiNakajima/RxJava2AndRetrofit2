package com.mobile.rxjava2andretrofit2.kotlin.resources

import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.base.BaseMvpFragment
import com.mobile.rxjava2andretrofit2.base.IBaseView
import com.mobile.rxjava2andretrofit2.callback.RcvOnItemViewClickListener
import com.mobile.rxjava2andretrofit2.kotlin.mine.adapter.MineAdapter
import com.mobile.rxjava2andretrofit2.kotlin.mine.bean.Ans
import com.mobile.rxjava2andretrofit2.kotlin.mine.ui.MineDetailsActivity
import com.mobile.rxjava2andretrofit2.kotlin.resources.bean.Result
import com.mobile.rxjava2andretrofit2.kotlin.resources.presenter.ResourcesPresenterImpl
import com.mobile.rxjava2andretrofit2.kotlin.resources.view.IResourcesView
import com.mobile.rxjava2andretrofit2.main.MainActivity
import com.mobile.rxjava2andretrofit2.manager.LogManager
import com.mobile.rxjava2andretrofit2.manager.RetrofitManager
import com.mobile.rxjava2andretrofit2.manager.ScreenManager
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_mine.*

class ResourcesFragment : BaseMvpFragment<IBaseView, ResourcesPresenterImpl>(), IResourcesView {

    private val TAG: String = "ResourcesFragment"
    private var mainActivity: MainActivity? = null

    private var resultList: MutableList<Result>? = null
//    private var mineAdapter: MineAdapter? = null
//    private var linearLayoutManager: LinearLayoutManager? = null
    private var isRefresh: Boolean? = null

    override fun initLayoutId(): Int {
        return R.layout.fragment_resources
    }

    override fun initData() {
        resultList = mutableListOf()
        mainActivity = activity as MainActivity
        isRefresh = true
    }

    override fun initViews() {
        tev_title.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
                initResources()
            }
        })

        initAdapter()
    }

    private fun initAdapter() {
//        linearLayoutManager = LinearLayoutManager(mainActivity)
//        linearLayoutManager!!.setOrientation(RecyclerView.VERTICAL)
//        rcv_data.layoutManager = (linearLayoutManager)
//        rcv_data.itemAnimator = DefaultItemAnimator()
//
//        mineAdapter = MineAdapter(mainActivity!!)
//        mineAdapter!!.setRcvOnItemViewClickListener(object : RcvOnItemViewClickListener {
//
//            override fun onItemClickListener(position: Int, view: View?) {
//                bodyParams.clear()
//                bodyParams["max_behot_time"] = "1000"
//                startActivityCarryParams(MineDetailsActivity::class.java, bodyParams)
//            }
//        })
//        rcv_data.setAdapter(mineAdapter)
//        mineAdapter!!.clearData()
//        mineAdapter!!.addAllData(resultList!!)
//
        refresh_layout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refresh_layout: RefreshLayout) {
                LogManager.i(TAG, "onLoadMore")
                isRefresh = false
                initResources()
            }

            override fun onRefresh(refresh_layout: RefreshLayout) {
                isRefresh = true
                initResources()
            }
        })
    }

    override fun initLoadData() {
        refresh_layout.autoRefresh()
    }

    override fun attachPresenter(): ResourcesPresenterImpl {
        return ResourcesPresenterImpl(this)
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

    override fun resourcesDataSuccess(success: List<Result>) {
        if (!mainActivity!!.isFinishing()) {
            if (isRefresh!!) {
                resultList!!.clear()
                resultList!!.addAll(success)
//                mineAdapter!!.addAllData(resultList!!)
                refresh_layout.finishRefresh()
            } else {
                resultList!!.addAll(success)
//                mineAdapter!!.addAllData(resultList!!)
                refresh_layout.finishLoadMore()
            }
        }
    }

    override fun resourcesDataError(error: String?) {
        if (!mainActivity!!.isFinishing()) {
//            showCustomToast(ScreenManager.dipTopx(activity, 51f), ScreenManager.dipTopx(activity, 51f),
//                    ScreenManager.dipTopx(activity, 38f), resources.getColor(R.color.white),
//                    resources.getColor(R.color.color_FFE066FF), ScreenManager.dipTopx(activity, 95f),
//                    ScreenManager.dipTopx(activity, 48f), error!!)
            if (isRefresh!!) {
                refresh_layout.finishRefresh(false)
            } else {
                refresh_layout.finishLoadMore(false)
            }
        }
    }

    private fun initResources() {
        if (RetrofitManager.isNetworkAvailable(mainActivity)) {
            bodyParams.clear()

            bodyParams.put("", "");
            //        bodyParams.put("max_behot_time", System.currentTimeMillis() / 1000 + "");
            presenter.resourcesData()
        } else {
            showToast(resources.getString(R.string.please_check_the_network_connection), true)
            if (isRefresh!!) {
                refresh_layout.finishRefresh()
            } else {
                refresh_layout.finishLoadMore()
            }
        }
    }

}