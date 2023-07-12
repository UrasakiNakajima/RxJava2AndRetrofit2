package com.phone.module_mine.fragment

import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.phone.library_mvp.BaseMvpRxFragment
import com.phone.library_base.base.IBaseView
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_base.manager.ScreenManager
import com.phone.library_base.manager.SharedPreferencesManager
import com.phone.library_common.bean.Data
import com.phone.library_common.callback.OnItemViewClickListener
import com.phone.library_base.common.ConstantData
import com.phone.library_custom_view.custom_view.LoadingLayout
import com.phone.library_network.manager.RetrofitManager
import com.phone.library_base.manager.ThreadPoolManager
import com.phone.module_mine.R
import com.phone.module_mine.adapter.MineAdapter
import com.phone.module_mine.presenter.MinePresenterImpl
import com.phone.module_mine.view.IMineView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce :
 */

@Route(path = ConstantData.Route.ROUTE_MINE_FRAGMENT)
class MineFragment : BaseMvpRxFragment<IBaseView, MinePresenterImpl>(), IMineView {

    companion object {
        private val TAG: String = MineFragment::class.java.name
    }

    private var layoutOutLayer: FrameLayout? = null
    private var toolbar: Toolbar? = null
    private var tevTitle: TextView? = null
    private var tevLogout: TextView? = null
    private var tevThreadPool: TextView? = null
    private var tevParamsTransferChangeProblem: TextView? = null
    private var refreshLayout: SmartRefreshLayout? = null
    private var rcvData: RecyclerView? = null
    private lateinit var loadLayout: LoadingLayout

    private val mineAdapter by lazy {
        MineAdapter(mRxAppCompatActivity)
    }
    private var isRefresh: Boolean = true

    override fun initLayoutId() = R.layout.mine_fragment_mine

    override fun initData() {
    }

    override fun initViews() {
        mRootView?.let {
            layoutOutLayer = it.findViewById(R.id.layout_out_layer)
            toolbar = it.findViewById(R.id.toolbar)
            tevTitle = it.findViewById(R.id.tev_title)
            tevLogout = it.findViewById(R.id.tev_logout)
            tevThreadPool = it.findViewById(R.id.tev_thread_pool)
            tevParamsTransferChangeProblem =
                it.findViewById(R.id.tev_params_transfer_change_problem)
            refreshLayout = it.findViewById(R.id.refresh_layout)
            rcvData = it.findViewById(R.id.rcv_data)
            loadLayout = it.findViewById(R.id.load_layout)

            tevTitle?.setOnClickListener(object : View.OnClickListener {

                override fun onClick(v: View?) {
                    ARouter.getInstance().build(ConstantData.Route.ROUTE_USER_DATA).navigation()
                }
            })
            tevLogout?.setOnClickListener {
                SharedPreferencesManager.put("isLogin", false)
                ARouter.getInstance().build(ConstantData.Route.ROUTE_LOGIN).navigation()
            }
            tevThreadPool?.setOnClickListener {
                ARouter.getInstance().build(ConstantData.Route.ROUTE_THREAD_POOL).navigation()
            }
            tevParamsTransferChangeProblem?.setOnClickListener {
                ARouter.getInstance().build(ConstantData.Route.ROUTE_PARAMS_TRANSFER_CHANGE_PROBLEM)
                    .navigation()
            }

            initAdapter()
        }
    }

    private fun initAdapter() {
        val linearLayoutManager = LinearLayoutManager(mRxAppCompatActivity)
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL)
        rcvData?.layoutManager = (linearLayoutManager)
        rcvData?.itemAnimator = DefaultItemAnimator()

        mineAdapter.apply {
            setOnItemViewClickListener(object : OnItemViewClickListener {

                override fun onItemClickListener(position: Int, view: View?) {
                    //                mBodyParams.clear()
                    //                mBodyParams["max_behot_time"] = "1000"
                    //                startActivityCarryParams(MineDetailsActivity::class.java, mBodyParams)

                    //                //Jump with parameters
                    //                ARouter.getInstance().build("/module_mine/ui/mine_details")
                    //                        .withString("max_behot_time", (System.currentTimeMillis() / 1000).toString())
                    //                        .navigation()

                    if (view?.id == R.id.ll_root) {
                        ARouter.getInstance().build(ConstantData.Route.ROUTE_WEB_VIEW).withString(
                            "loadUrl", mineAdapter.mJuheNewsBeanList.get(position).url
                        ).navigation()
                    }
                }
            })
        }
        rcvData?.adapter = mineAdapter

        refreshLayout?.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                LogManager.i(TAG, "onLoadMore")
                isRefresh = false
                initMine()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                isRefresh = true
                initMine()
            }
        })
    }

    override fun initLoadData() {
        initMine()
        LogManager.i(TAG, "MineFragment initLoadData")
    }

    override fun attachPresenter() = MinePresenterImpl(this)

    override fun mineDataSuccess(success: MutableList<Data>) {
        if (!mRxAppCompatActivity.isFinishing) {
            if (isRefresh) {
                mineAdapter.also {
                    it.clearData()
                    it.addData(success)
                }
                refreshLayout?.finishRefresh()
            } else {
                mineAdapter.addData(success)
                refreshLayout?.finishLoadMore()
            }
            hideLoading()
        }
    }

    override fun mineDataError(error: String) {
        if (!mRxAppCompatActivity.isFinishing) {
            showCustomToast(
                ScreenManager.dpToPx(20f),
                ScreenManager.dpToPx(20f),
                18,
                ResourcesManager.getColor(R.color.library_white),
                ResourcesManager.getColor(R.color.library_color_FFE066FF),
                ScreenManager.dpToPx(40f),
                ScreenManager.dpToPx(20f),
                error
            )

            if (isRefresh) {
                refreshLayout?.finishRefresh(false)
            } else {
                refreshLayout?.finishLoadMore(false)
            }
            hideLoading()
        }
    }

    private fun initMine() {
//        showLoading()
//        ThreadPoolManager.instance().createScheduledThreadPoolToUIThread2(1000, {
        if (RetrofitManager.isNetworkAvailable()) {
            mBodyParams.clear()

            mBodyParams["type"] = "keji"
            mBodyParams["key"] = "d5cc661633a28f3cf4b1eccff3ee7bae"
            presenter?.mineData(mRxFragment, mBodyParams)
        } else {
            mineDataError(resources.getString(R.string.library_please_check_the_network_connection))
        }
//        })
    }

    override fun onDestroyView() {
        layoutOutLayer?.removeAllViews()
        mRootView = null
        ThreadPoolManager.instance().shutdownNowScheduledThreadPool()
        super.onDestroyView()
    }

}