package com.mobile.rxjava2andretrofit2.kotlin.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.base.BaseMvpFragment
import com.mobile.rxjava2andretrofit2.base.IBaseView
import com.mobile.rxjava2andretrofit2.callback.RcvOnItemViewClickListener
import com.mobile.rxjava2andretrofit2.main.MainActivity
import com.mobile.rxjava2andretrofit2.manager.LogManager
import com.mobile.rxjava2andretrofit2.manager.RetrofitManager
import com.mobile.rxjava2andretrofit2.kotlin.mine.adapter.MineAdapter
import com.mobile.rxjava2andretrofit2.kotlin.mine.bean.Ans
import com.mobile.rxjava2andretrofit2.kotlin.mine.presenter.MinePresenterImpl
import com.mobile.rxjava2andretrofit2.kotlin.mine.ui.MineDetailsActivity
import com.mobile.rxjava2andretrofit2.kotlin.mine.view.IMineView
import com.mobile.rxjava2andretrofit2.manager.ScreenManager
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : BaseMvpFragment<IBaseView, MinePresenterImpl>(), IMineView {

    private val TAG: String = "MineFragment"
    private var mainActivity: MainActivity? = null

    private var ansListBeanList: MutableList<Ans> = mutableListOf()
    private var mineAdapter: MineAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var isRefresh: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun initData() {
        mainActivity = activity as MainActivity
    }

    override fun initViews() {
        tev_title.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
                initMine()
            }
        })

        initAdapter()
    }

    private fun initAdapter() {
        linearLayoutManager = LinearLayoutManager(mainActivity)
        linearLayoutManager!!.setOrientation(RecyclerView.VERTICAL)
        rcv_data.layoutManager = (linearLayoutManager)
        rcv_data.itemAnimator = DefaultItemAnimator()

        mineAdapter = MineAdapter(mainActivity!!)
        mineAdapter!!.setRcvOnItemViewClickListener(object : RcvOnItemViewClickListener {

            override fun onItemClickListener(position: Int, view: View?) {
                bodyParams.clear()
                bodyParams["max_behot_time"] = "1000"
                startActivityCarryParams(MineDetailsActivity::class.java, bodyParams)
            }
        })
        rcv_data.setAdapter(mineAdapter)
        mineAdapter!!.clearData()
        mineAdapter!!.addAllData(ansListBeanList)

        refresh_layout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refresh_layout: RefreshLayout) {
                LogManager.i(TAG, "onLoadMore")
                isRefresh = false
                initMine()
            }

            override fun onRefresh(refresh_layout: RefreshLayout) {
                isRefresh = true
                initMine()
            }
        })
    }

    override fun initLoadData() {
        refresh_layout.autoRefresh()
    }

    override fun attachPresenter(): MinePresenterImpl {
        return MinePresenterImpl(this)
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

    override fun mineDataSuccess(success: List<Ans>) {
        if (!mainActivity!!.isFinishing()) {
            if (isRefresh) {
                ansListBeanList.clear()
                ansListBeanList.addAll(success)
                mineAdapter!!.addAllData(ansListBeanList)
                refresh_layout.finishRefresh()
            } else {
                ansListBeanList.addAll(success)
                mineAdapter!!.addAllData(ansListBeanList)
                refresh_layout.finishLoadMore()
            }
        }
    }

    override fun mineDataError(error: String?) {
        if (!mainActivity!!.isFinishing()) {
//            showToast(error!!, true)
            showCustomToast(ScreenManager.dipTopx(activity, 51f), ScreenManager.dipTopx(activity, 51f),
                    ScreenManager.dipTopx(activity, 20f), resources.getColor(R.color.white),
                    resources.getColor(R.color.color_FFE066FF), ScreenManager.dipTopx(activity, 95f),
                    ScreenManager.dipTopx(activity, 48f), error!!)
            if (isRefresh) {
                refresh_layout.finishRefresh(false)
            } else {
                refresh_layout.finishLoadMore(false)
            }
        }
    }

    private fun initMine() {
        if (RetrofitManager.isNetworkAvailable(mainActivity)) {
            bodyParams.clear()

            bodyParams["qid"] = "6463093341545300238"
            //        bodyParams.put("max_behot_time", System.currentTimeMillis() / 1000 + "");
            presenter.mineData(bodyParams)
        } else {
            showToast(resources.getString(R.string.please_check_the_network_connection), true)
            if (isRefresh) {
                refresh_layout.finishRefresh()
            } else {
                refresh_layout.finishLoadMore()
            }
        }
    }

}