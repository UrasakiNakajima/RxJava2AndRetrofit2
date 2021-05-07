package com.mobile.resource_module.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.common_library.base.BaseMvpFragment
import com.mobile.common_library.base.IBaseView
import com.mobile.common_library.callback.RcvOnItemViewClickListener
import com.mobile.common_library.manager.LogManager
import com.mobile.common_library.manager.RetrofitManager
import com.mobile.common_library.manager.ScreenManager
import com.mobile.resource_module.R
import com.mobile.resource_module.adapter.ResourceAdapter
import com.mobile.resource_module.bean.Result2
import com.mobile.resource_module.presenter.ResourcePresenterImpl
import com.mobile.resource_module.view.IResourceChildView
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_resource_child.*

class ResourceChildFragment : BaseMvpFragment<IBaseView, ResourcePresenterImpl>(), IResourceChildView {

    private val TAG: String = "ResourceChildFragment";

    private var resultList: MutableList<Result2> = mutableListOf()
    private var resourceAdapter: ResourceAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var isRefresh: Boolean = true
    private var type: String = "all"
    private val pageSize: String = "20";
    private var currentPage: Int = 1;

    companion object {
        fun getInstance(type: String): ResourceChildFragment {
            val fragment = ResourceChildFragment()
            val bundle = Bundle()
            bundle.putString("type", type)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initLayoutId(): Int {
        return R.layout.fragment_resource_child
    }

    override fun initData() {

        bundle = arguments
        type = bundle.getString("type")!!
//        if (resources.getString(R.string.all_resources).equals(type)) {
//
//        } else if (resources.getString(R.string.beautiful_woman).equals(type)) {
//
//        } else if (resources.getString(R.string.android).equals(type)) {
//
//        } else if (resources.getString(R.string.ios).equals(type)) {
//
//        } else if (resources.getString(R.string.video).equals(type)) {
//
//        } else if (resources.getString(R.string.app).equals(type)) {
//
//        }
    }

    override fun initViews() {
        initAdapter()
    }

    private fun initAdapter() {
        linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager!!.setOrientation(RecyclerView.VERTICAL)
        rcv_data.layoutManager = (linearLayoutManager)
        rcv_data.itemAnimator = DefaultItemAnimator()

        resourceAdapter = ResourceAdapter(activity!!)
        resourceAdapter!!.setRcvOnItemViewClickListener(object : RcvOnItemViewClickListener {

            override fun onItemClickListener(position: Int, view: View?) {
//                bodyParams.clear()
//                bodyParams["max_behot_time"] = "1000"
//                startActivityCarryParams(MineDetailsActivity::class.java, bodyParams)
            }
        })
        rcv_data.setAdapter(resourceAdapter)
        resourceAdapter!!.clearData()
        resourceAdapter!!.addAllData(resultList)

        refresh_layout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refresh_layout: RefreshLayout) {
                LogManager.i(TAG, "onLoadMore")
                isRefresh = false
                initResource(type, pageSize, currentPage.toString())
            }

            override fun onRefresh(refresh_layout: RefreshLayout) {
                LogManager.i(TAG, "onRefresh")
                isRefresh = true
                currentPage = 1;
                initResource(type, pageSize, currentPage.toString())
            }
        })
    }

    override fun initLoadData() {
        refresh_layout.autoRefresh()
    }

    override fun attachPresenter(): ResourcePresenterImpl {
        return ResourcePresenterImpl(this)
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

    override fun resourceDataSuccess(success: List<Result2>) {
        if (!activity!!.isFinishing()) {
            if (isRefresh) {
                resultList.clear()
                resultList.addAll(success)
                resourceAdapter!!.clearData();
                resourceAdapter!!.addAllData(resultList)
                refresh_layout.finishRefresh()
            } else {
                resultList.addAll(success)
                resourceAdapter!!.clearData();
                resourceAdapter!!.addAllData(resultList)
                refresh_layout.finishLoadMore()
            }
            currentPage++;
        }
    }

    override fun resourceDataError(error: String) {
        if (!activity!!.isFinishing()) {
            showCustomToast(ScreenManager.dipTopx(activity, 20f), ScreenManager.dipTopx(activity, 20f),
                    18, resources.getColor(R.color.white),
                    resources.getColor(R.color.color_FFE066FF), ScreenManager.dipTopx(activity, 40f),
                    ScreenManager.dipTopx(activity, 20f), error)

            if (isRefresh) {
                refresh_layout.finishRefresh(false)
            } else {
                refresh_layout.finishLoadMore(false)
            }
        }
    }

    private fun initResource(type: String, pageSize: String, currentPage: String) {
        if (RetrofitManager.isNetworkAvailable(activity)) {
//            bodyParams.clear()
//            bodyParams.put("max_behot_time", System.currentTimeMillis() / 1000 + "");
            presenter.resourceData(this,type, pageSize, currentPage)
        } else {
            showToast(resources.getString(R.string.please_check_the_network_connection), true)
            if (isRefresh) {
                refresh_layout.finishRefresh()
            } else {
                refresh_layout.finishLoadMore()
            }
        }
    }

    override fun onDestroyView() {
        resultList.clear()
        super.onDestroyView()
    }
}