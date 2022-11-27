package com.phone.mine_module

import android.content.Intent
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.phone.common_library.base.BaseMvpRxFragment
import com.phone.common_library.base.IBaseView
import com.phone.common_library.callback.OnItemViewClickListener
import com.phone.common_library.manager.LogManager
import com.phone.common_library.manager.ResourcesManager
import com.phone.common_library.manager.RetrofitManager
import com.phone.common_library.manager.ScreenManager
import com.phone.common_library.ui.WebViewActivity
import com.phone.mine_module.adapter.MineAdapter
import com.phone.mine_module.bean.Data
import com.phone.mine_module.presenter.MinePresenterImpl
import com.phone.mine_module.ui.ParamsTransferChangeProblemActivity
import com.phone.mine_module.ui.ThreadPoolActivity
import com.phone.mine_module.ui.UserDataActivity
import com.phone.mine_module.view.IMineView
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
@Route(path = "/mine_module/mine")
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
    private var loadView: QMUILoadingView? = null

    private val mineAdapter by lazy { MineAdapter(rxAppCompatActivity) }
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var isRefresh: Boolean = true

    override fun initLayoutId() = R.layout.fragment_mine

    override fun initData() {
    }

    override fun initViews() {
        layoutOutLayer = rootView.findViewById(R.id.layout_out_layer)
        toolbar = rootView.findViewById(R.id.toolbar)
        tevTitle = rootView.findViewById(R.id.tev_title)
        tevLogout = rootView.findViewById(R.id.tev_logout)
        tevThreadPool = rootView.findViewById(R.id.tev_thread_pool)
        tevParamsTransferChangeProblem =
            rootView.findViewById(R.id.tev_params_transfer_change_problem)
        refreshLayout = rootView.findViewById(R.id.refresh_layout)
        rcvData = rootView.findViewById(R.id.rcv_data)
        loadView = rootView.findViewById(R.id.load_view)

        tevTitle?.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
//                initMine()
                startActivity(UserDataActivity::class.java)
            }
        })
        tevLogout?.setOnClickListener {
            baseApplication.isLogin = false
            ARouter.getInstance().build("/main_module/login").navigation()
        }
        tevThreadPool?.setOnClickListener {
            startActivity(ThreadPoolActivity::class.java)
        }
        tevParamsTransferChangeProblem?.setOnClickListener {
            startActivity(ParamsTransferChangeProblemActivity::class.java)
        }

        initAdapter()
    }

    private fun initAdapter() {
        linearLayoutManager = LinearLayoutManager(rxAppCompatActivity)
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL)
        rcvData?.layoutManager = (linearLayoutManager)
        rcvData?.itemAnimator = DefaultItemAnimator()

        mineAdapter.apply {
            setRcvOnItemViewClickListener(object : OnItemViewClickListener {

                override fun onItemClickListener(position: Int, view: View?) {
//                bodyParams.clear()
//                bodyParams["max_behot_time"] = "1000"
//                startActivityCarryParams(MineDetailsActivity::class.java, bodyParams)

//                //Jump with parameters
//                ARouter.getInstance().build("/mine_module/ui/mine_details")
//                        .withString("max_behot_time", (System.currentTimeMillis() / 1000).toString())
//                        .navigation()

                    if (view?.id == R.id.ll_root) {
                        val intent = Intent(rxAppCompatActivity, WebViewActivity::class.java)
                        intent.putExtra("loadUrl", mineAdapter.mJuheNewsBeanList.get(position).url)
                        startActivity(intent)
                    }
                }
            })
        }
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

        LogManager.i(TAG, "MineFragment initLoadData")
    }

    override fun attachPresenter() = MinePresenterImpl(this)

    override fun showLoading() {
        loadView?.let {
            if (!it.isShown()) {
                it.setVisibility(View.VISIBLE)
                it.start()
            }
        }
    }

    override fun hideLoading() {
        loadView?.let {
            if (it.isShown()) {
                it.stop()
                it.setVisibility(View.GONE)
            }
        }
    }

    override fun mineDataSuccess(success: MutableList<Data>) {
        if (!rxAppCompatActivity.isFinishing()) {
            if (isRefresh) {
                mineAdapter.clearData()
                mineAdapter.addData(mineAdapter.mJuheNewsBeanList)
                refreshLayout?.finishRefresh()
            } else {
                mineAdapter.clearData()
                mineAdapter.addData(mineAdapter.mJuheNewsBeanList)
                refreshLayout?.finishLoadMore()
            }
        }
    }

    override fun mineDataError(error: String) {
        if (!rxAppCompatActivity.isFinishing()) {
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
        }
    }

    private fun initMine() {
        if (RetrofitManager.isNetworkAvailable()) {
            bodyParams.clear()

            bodyParams["type"] = "keji"
            bodyParams["key"] = "d5cc661633a28f3cf4b1eccff3ee7bae"
            presenter.mineData(rxFragment, bodyParams)
        } else {
            showToast(resources.getString(R.string.please_check_the_network_connection), true)
            if (isRefresh) {
                refreshLayout?.finishRefresh()
            } else {
                refreshLayout?.finishLoadMore()
            }
        }
    }

    override fun onDestroyView() {
        layoutOutLayer?.removeAllViews()
        rootView = null
        super.onDestroyView()
    }

}