package com.mobile.mine_module

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.mobile.common_library.base.BaseMvpFragment
import com.mobile.common_library.base.IBaseView
import com.mobile.common_library.callback.RcvOnItemViewClickListener
import com.mobile.common_library.manager.LogManager
import com.mobile.common_library.manager.RetrofitManager
import com.mobile.common_library.manager.ScreenManager
import com.mobile.mine_module.adapter.MineAdapter
import com.mobile.mine_module.bean.Ans
import com.mobile.mine_module.presenter.MinePresenterImpl
import com.mobile.mine_module.ui.UserDataActivity
import com.mobile.mine_module.view.IMineView
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_mine.*

@Route(path = "/mine_module/mine")
class MineFragment : BaseMvpFragment<IBaseView, MinePresenterImpl>(), IMineView {


    private val TAG: String = "MineFragment"
//    private var mainActivity: MainActivity? = null

    private var ansListBeanList: MutableList<Ans> = mutableListOf()
    private var mineAdapter: MineAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var isRefresh: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // TODO: inflate a fragment view
        rootView = super.onCreateView(inflater, container, savedInstanceState)
        return rootView
    }

    override fun initLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun initData() {
//        mainActivity = activity as MainActivity
    }

    override fun initViews() {
        tev_title.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
//                initMine()
                startActivity(UserDataActivity::class.java)
            }
        })

        initAdapter()
    }

    private fun initAdapter() {
        linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager!!.setOrientation(RecyclerView.VERTICAL)
        rcv_data.layoutManager = (linearLayoutManager)
        rcv_data.itemAnimator = DefaultItemAnimator()

        mineAdapter = MineAdapter(activity!!)
        mineAdapter!!.setRcvOnItemViewClickListener(object : RcvOnItemViewClickListener {

            override fun onItemClickListener(position: Int, view: View?) {
//                bodyParams.clear()
//                bodyParams["max_behot_time"] = "1000"
//                startActivityCarryParams(MineDetailsActivity::class.java, bodyParams)

//                //Jump with parameters
//                ARouter.getInstance().build("/mine_module/ui/mine_details")
//                        .withString("max_behot_time", (System.currentTimeMillis() / 1000).toString())
//                        .navigation()

                startActivity(UserDataActivity::class.java)
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
        if (!activity!!.isFinishing()) {
            if (isRefresh) {
                ansListBeanList.clear()
                ansListBeanList.addAll(success)
                mineAdapter!!.clearData();
                mineAdapter!!.addAllData(ansListBeanList)
                refresh_layout.finishRefresh()
            } else {
                ansListBeanList.addAll(success)
                mineAdapter!!.clearData();
                mineAdapter!!.addAllData(ansListBeanList)
                refresh_layout.finishLoadMore()
            }
        }
    }

    override fun mineDataError(error: String) {
        if (!activity!!.isFinishing()) {
            showCustomToast(ScreenManager.dpToPx(activity, 20f), ScreenManager.dpToPx(activity, 20f),
                    18, resources.getColor(R.color.white),
                    resources.getColor(R.color.color_FFE066FF), ScreenManager.dpToPx(activity, 40f),
                    ScreenManager.dpToPx(activity, 20f), error,
                    true)

            if (isRefresh) {
                refresh_layout.finishRefresh(false)
            } else {
                refresh_layout.finishLoadMore(false)
            }
        }
    }

    private fun initMine() {
        if (RetrofitManager.isNetworkAvailable(activity)) {
            bodyParams.clear()
            bodyParams["qid"] = "6463093341545300238"
//            bodyParams.put("max_behot_time", System.currentTimeMillis() / 1000 + "");

            presenter.mineData(this, bodyParams)
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