package com.phone.module_mine

import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.phone.library_common.base.BaseMvpRxAppActivity
import com.phone.library_common.base.IBaseView
import com.phone.library_common.bean.Data
import com.phone.library_common.callback.OnItemViewClickListener
import com.phone.library_common.common.ConstantData
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.ResourcesManager
import com.phone.library_common.manager.RetrofitManager
import com.phone.library_common.manager.ScreenManager
import com.phone.library_common.manager.SharedPreferencesManager
import com.phone.module_mine.adapter.MineAdapter
import com.phone.module_mine.presenter.MinePresenterImpl
import com.phone.module_mine.view.IMineView
import com.qmuiteam.qmui.widget.QMUILoadingView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce :
 */

class MineActivity : BaseMvpRxAppActivity<IBaseView, MinePresenterImpl>(), IMineView {

    companion object {
        private val TAG: String = MineActivity::class.java.simpleName
    }

    private var layoutOutLayer: FrameLayout? = null
    private var toolbar: Toolbar? = null
    private var tevTitle: TextView? = null
    private var tevLogout: TextView? = null
    private var tevThreadPool: TextView? = null
    private var tevParamsTransferChangeProblem: TextView? = null
    private var refreshLayout: SmartRefreshLayout? = null
    private var rcvData: RecyclerView? = null

    private val mineAdapter by lazy { MineAdapter(mRxAppCompatActivity) }
    private var isRefresh: Boolean = true

    override fun initLayoutId() = R.layout.mine_activity_mine

    override fun initData() {
    }

    override fun initViews() {
        layoutOutLayer = findViewById(R.id.layout_out_layer)
        toolbar = findViewById(R.id.toolbar)
        tevTitle = findViewById(R.id.tev_title)
        tevLogout = findViewById(R.id.tev_logout)
        tevThreadPool = findViewById(R.id.tev_thread_pool)
        tevParamsTransferChangeProblem =
            findViewById(R.id.tev_params_transfer_change_problem)
        refreshLayout = findViewById(R.id.refresh_layout)
        rcvData = findViewById(R.id.rcv_data)

        setToolbar(false, R.color.color_FF198CFF)

        tevTitle?.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
//                initMine()
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

    private fun initAdapter() {
        val linearLayoutManager = LinearLayoutManager(mRxAppCompatActivity)
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL)
        rcvData?.layoutManager = (linearLayoutManager)
        rcvData?.itemAnimator = DefaultItemAnimator()

        mineAdapter.setOnItemViewClickListener(object : OnItemViewClickListener {

            override fun onItemClickListener(position: Int, view: View?) {
//                bodyParams.clear()
//                bodyParams["max_behot_time"] = "1000"
//                startActivityCarryParams(MineDetailsActivity::class.java, bodyParams)

//                //Jump with parameters
//                ARouter.getInstance().build("/module_mine/ui/mine_details")
//                        .withString("max_behot_time", (System.currentTimeMillis() / 1000).toString())
//                        .navigation()

                if (view?.id == R.id.ll_root) {
                    ARouter.getInstance().build(ConstantData.Route.ROUTE_WEB_VIEW)
                        .withString("loadUrl", mineAdapter.mJuheNewsBeanList.get(position).url)
                        .navigation()
                }
//                startActivity(UserDataActivity::class.java)
            }
        })
        rcvData?.setAdapter(mineAdapter)

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
        refreshLayout?.autoRefresh()
    }

    override fun attachPresenter(): MinePresenterImpl {
        return MinePresenterImpl(this)
    }

    override fun showLoading() {
        if (!mRxAppCompatActivity.isFinishing && !mLoadView.isShown()) {
            mLoadView.visibility = View.VISIBLE
            mLoadView.start()
        }
    }

    override fun hideLoading() {
        if (!mRxAppCompatActivity.isFinishing && mLoadView.isShown()) {
            mLoadView.stop()
            mLoadView.setVisibility(View.GONE)
        }
    }

    override fun mineDataSuccess(success: MutableList<Data>) {
        if (!mRxAppCompatActivity.isFinishing()) {
            if (isRefresh) {
                mineAdapter.also {
                    it.clearData();
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
                refreshLayout?.finishRefresh(false)
            } else {
                refreshLayout?.finishLoadMore(false)
            }
            hideLoading()
        }
    }

    private fun initMine() {
        showLoading()
        if (RetrofitManager.isNetworkAvailable()) {
            mBodyParams.clear()

            mBodyParams["type"] = "keji"
            mBodyParams["key"] = "d5cc661633a28f3cf4b1eccff3ee7bae"
            presenter.mineData2(mRxAppCompatActivity, mBodyParams)
        } else {
            mineDataError(ResourcesManager.getString(R.string.please_check_the_network_connection))
        }
    }

}
