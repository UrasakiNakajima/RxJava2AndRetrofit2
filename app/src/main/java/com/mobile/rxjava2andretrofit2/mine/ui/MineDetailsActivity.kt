package com.mobile.rxjava2andretrofit2.mine.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.base.BaseMvpAppActivity
import com.mobile.rxjava2andretrofit2.base.IBaseView
import com.mobile.rxjava2andretrofit2.first_page.adapter.FirstPageDetailsAdapter
import com.mobile.rxjava2andretrofit2.first_page.bean.FirstPageDetailsResponse
import com.mobile.rxjava2andretrofit2.first_page.ui.VideoListActivity
import com.mobile.rxjava2andretrofit2.manager.LogManager
import com.mobile.rxjava2andretrofit2.mine.presenter.MinePresenterImpl
import com.mobile.rxjava2andretrofit2.mine.view.IMineDetailsView
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_mine_details.*

class MineDetailsActivity : BaseMvpAppActivity<IBaseView, MinePresenterImpl>(), IMineDetailsView {

    private val TAG: String = "MineDetailsActivity"
    private var max_behot_time: String? = null
    private var dataBeanList: MutableList<FirstPageDetailsResponse.DataBean>? = null
    private var mineDetailsAdapter: FirstPageDetailsAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var isRefresh: Boolean? = true;
    private var isFirstLoad: Boolean? = true;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initLayoutId(): Int {
        return R.layout.activity_mine_details;
    }

    override fun initData() {
        intent = getIntent()
        bundle = intent.extras
        max_behot_time = bundle.getString("max_behot_time")
        LogManager.i(TAG, "max_behot_time*****$max_behot_time")

        dataBeanList = mutableListOf()
        isRefresh = true
        isFirstLoad = true
    }

    override fun initViews() {
        addContentView(loadView, layoutParams)
        setToolbar(false, R.color.color_FF198CFF)

        initAdapter()
    }

    private fun initAdapter() {
        linearLayoutManager = LinearLayoutManager(this);
        linearLayoutManager!!.orientation = RecyclerView.VERTICAL;
        rcv_data.layoutManager = linearLayoutManager
        rcv_data.itemAnimator = DefaultItemAnimator()
        mineDetailsAdapter = FirstPageDetailsAdapter(this)
        mineDetailsAdapter!!.setRcvOnItemViewClickListener { position, view ->
            val data: String = dataBeanList!!.get(position).getContent()
            bodyParams.clear()
            bodyParams.put("data", data)
            startActivityCarryParams(VideoListActivity::class.java, bodyParams)
        }
        rcv_data.adapter = mineDetailsAdapter
        mineDetailsAdapter!!.clearData()
        mineDetailsAdapter!!.addAllData(dataBeanList)
        refresh_layout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {

            }

            override fun onRefresh(refreshLayout: RefreshLayout) {

            }
        });

        refresh_layout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                LogManager.i(TAG, "onLoadMore")
                isRefresh = false
                initFirstPageDetails()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                isRefresh = true
                initFirstPageDetails()
            }
        })
//        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
//        rcvData.setLayoutManager(linearLayoutManager);
//        rcvData.setItemAnimator(new DefaultItemAnimator ());
//
//        mineDetailsAdapter = new FirstPageDetailsAdapter (this);
//        mineDetailsAdapter.setRcvOnItemViewClickListener(new RcvOnItemViewClickListener () {
//            @Override
//            public void onItemClickListener(int position, View view) {
//                String data = dataBeanList . get (position).getContent();
//                bodyParams.clear();
//                bodyParams.put("data", data);
//                startActivityCarryParams(VideoListActivity.class, bodyParams);
//            }
//        });
//        rcvData.setAdapter(mineDetailsAdapter);
//        mineDetailsAdapter.clearData();
//        mineDetailsAdapter.addAllData(dataBeanList);
//
//        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener () {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                LogManager.i(TAG, "onLoadMore");
//                isRefresh = false;
//                initFirstPageDetails();
//            }
//
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                isRefresh = true;
//                initFirstPageDetails();
//            }
//        });
    }

    override fun initLoadData() {
        refresh_layout.autoRefresh()
    }

    override fun attachPresenter(): MinePresenterImpl {
        return MinePresenterImpl(this)
    }

    override fun showLoading() {
        if (loadView != null && !loadView.isShown) {
            loadView.setVisibility(View.VISIBLE)
            loadView.start()
        }
    }

    override fun hideLoading() {
        if (loadView != null && loadView.isShown) {
            loadView.stop()
            loadView.visibility = View.GONE
        }
    }

    override fun mineDetailsSuccess(success: MutableList<FirstPageDetailsResponse.DataBean>?) {
        if (!this.isFinishing()) {
            if (isRefresh!!) {
                dataBeanList!!.clear()
                dataBeanList!!.addAll(success!!)
                mineDetailsAdapter!!.addAllData(dataBeanList)
                refresh_layout.finishRefresh()
            } else {
                dataBeanList!!.addAll(success!!)
                mineDetailsAdapter!!.addAllData(dataBeanList)
                refresh_layout.finishLoadMore()
            }
        }
    }

    override fun mineDetailsError(error: String?) {
        if (!this.isFinishing()) {
            showToast(error, true)
            if (isRefresh!!) {
                refresh_layout.finishRefresh()
            } else {
                refresh_layout.finishLoadMore()
            }
        }
    }

    private fun initFirstPageDetails() {
        if (isFirstLoad!!) {
            isFirstLoad = false
        } else {
            max_behot_time = "${System.currentTimeMillis() / 1000}"
        }

//        max_behot_time = 1605844009 + "";
//        max_behot_time = 1605844868 + "";
        bodyParams.clear()
        bodyParams.put("category", "video")
        bodyParams.put("max_behot_time", max_behot_time)
        presenter.mineDetails(bodyParams)
    }

}
