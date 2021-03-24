package com.mobile.mine_module.ui

import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.mobile.common_library.base.BaseMvpAppActivity
import com.mobile.common_library.base.IBaseView
import com.mobile.common_library.callback.RcvOnItemViewClickListener
import com.mobile.common_library.manager.LogManager
import com.mobile.common_library.manager.ScreenManager
import com.mobile.mine_module.R
import com.mobile.mine_module.adapter.MineDetailsAdapter
import com.mobile.mine_module.bean.Data
import com.mobile.mine_module.presenter.MinePresenterImpl
import com.mobile.mine_module.view.IMineDetailsView
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_mine_details.*

//@Route(path = "/mine_module/ui/mine_details")
class MineDetailsActivity : BaseMvpAppActivity<IBaseView, MinePresenterImpl>(), IMineDetailsView {

    private val TAG: String = "MineDetailsActivity"
//    @Autowired
//    @JvmField
    var max_behot_time: String? = null
    private var dataBeanList: MutableList<Data> = mutableListOf()
    private var mineDetailsAdapter: MineDetailsAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var isRefresh: Boolean = true;
    private var isFirstLoad: Boolean = true;

    override fun initLayoutId(): Int {
        return R.layout.activity_mine_details;
    }

    override fun initData() {
//        ARouter.getInstance().inject(this)
        intent = getIntent()
        bundle = intent.extras
        max_behot_time = bundle.getString("max_behot_time")

        LogManager.i(TAG, "max_behot_time*****$max_behot_time")
    }

    override fun initViews() {
        addContentView(loadView, layoutParams)
        setToolbar(false, R.color.color_FFE066FF)
        imv_back.setColorFilter(resources.getColor(R.color.color_FFFFFFFF))

        layout_back.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
                finish()
            }
        })
        initAdapter()
    }

    private fun initAdapter() {
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager!!.orientation = RecyclerView.VERTICAL
        rcv_data.layoutManager = linearLayoutManager
        rcv_data.itemAnimator = DefaultItemAnimator()
        mineDetailsAdapter = MineDetailsAdapter(this)
//        mineDetailsAdapter!!.setRcvOnItemViewClickListener { position, view ->
//            val data: String = dataBeanList!!.get(position).content
//            bodyParams.clear()
//            bodyParams.put("data", data)
//            startActivityCarryParams(VideoListActivity::class.java, bodyParams)
//        }
        mineDetailsAdapter!!.setRcvOnItemViewClickListener(object : RcvOnItemViewClickListener {

            override fun onItemClickListener(position: Int, view: View?) {
//                val data: String = dataBeanList[position].content!!
//                bodyParams.clear()
//                bodyParams.put("data", data)
//                startActivityCarryParams(VideoListActivity::class.java, bodyParams)
            }
        })
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
                initMineDetails()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                isRefresh = true
                initMineDetails()
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

    override fun mineDetailsSuccess(success: List<Data>) {
        if (!this.isFinishing()) {
            if (isRefresh) {
                dataBeanList.clear()
                dataBeanList.addAll(success)
                mineDetailsAdapter!!.clearData();
                mineDetailsAdapter!!.addAllData(dataBeanList)
                refresh_layout.finishRefresh()
            } else {
                dataBeanList.addAll(success)
                mineDetailsAdapter!!.clearData();
                mineDetailsAdapter!!.addAllData(dataBeanList)
                refresh_layout.finishLoadMore()
            }
        }
    }

    override fun mineDetailsError(error: String) {
        if (!this.isFinishing()) {
//            showToast(error, true)
            showCustomToast(ScreenManager.dipTopx(this, 20f), ScreenManager.dipTopx(this, 20f),
                    18, resources.getColor(R.color.white),
                    resources.getColor(R.color.color_FFE066FF), ScreenManager.dipTopx(this, 40f),
                    ScreenManager.dipTopx(this, 20f), error)

            if (isRefresh) {
                refresh_layout.finishRefresh(false)
            } else {
                refresh_layout.finishLoadMore(false)
            }
        }
    }

    private fun initMineDetails() {
        if (isFirstLoad) {
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
