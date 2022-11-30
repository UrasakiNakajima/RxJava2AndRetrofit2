package com.phone.module_mine

import android.content.Intent
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phone.common_library.base.BaseMvpRxAppActivity
import com.phone.common_library.base.IBaseView
import com.phone.common_library.callback.OnItemViewClickListener
import com.phone.common_library.manager.LogManager
import com.phone.common_library.manager.ResourcesManager
import com.phone.common_library.manager.RetrofitManager
import com.phone.common_library.manager.ScreenManager
import com.phone.common_library.ui.WebViewActivity
import com.phone.module_mine.adapter.MineAdapter
import com.phone.module_mine.bean.Data
import com.phone.module_mine.presenter.MinePresenterImpl
import com.phone.module_mine.ui.ParamsTransferChangeProblemActivity
import com.phone.module_mine.ui.ThreadPoolActivity
import com.phone.module_mine.ui.UserDataActivity
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
//@Route(path = "/module_mine/ui/mine_details")
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
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var isRefresh: Boolean = true

    override fun initLayoutId() = R.layout.activity_mine

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
                startActivity(UserDataActivity::class.java)
            }
        })
        tevLogout?.setOnClickListener {
            showToast(
                ResourcesManager.getString(R.string.this_function_can_only_be_used_under_componentization),
                false
            )
//            baseApplication?.isLogin = false
//            ARouter.getInstance().build("/main_module/login").navigation()
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
        linearLayoutManager = LinearLayoutManager(mRxAppCompatActivity)
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL)
        rcvData?.layoutManager = (linearLayoutManager)
        rcvData?.itemAnimator = DefaultItemAnimator()

        mineAdapter.setRcvOnItemViewClickListener(object : OnItemViewClickListener {

            override fun onItemClickListener(position: Int, view: View?) {
//                bodyParams.clear()
//                bodyParams["max_behot_time"] = "1000"
//                startActivityCarryParams(MineDetailsActivity::class.java, bodyParams)

//                //Jump with parameters
//                ARouter.getInstance().build("/module_mine/ui/mine_details")
//                        .withString("max_behot_time", (System.currentTimeMillis() / 1000).toString())
//                        .navigation()

                if (view?.id == R.id.ll_root) {
                    val intent = Intent(mRxAppCompatActivity, WebViewActivity::class.java)
                    intent.putExtra("loadUrl", mineAdapter.mJuheNewsBeanList.get(position).url)
                    startActivity(intent)
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
        if (!mLoadView.isShown()) {
            mLoadView.setVisibility(View.VISIBLE)
            mLoadView.start()
        }
    }

    override fun hideLoading() {
        if (mLoadView.isShown()) {
            mLoadView.stop()
            mLoadView.setVisibility(View.GONE)
        }
    }

    override fun mineDataSuccess(success: MutableList<Data>) {
        if (!mRxAppCompatActivity.isFinishing()) {
            if (isRefresh) {
                mineAdapter.apply {
                    clearData();
                    addData(success)
                }
                refreshLayout?.finishRefresh()
            } else {
                mineAdapter.addData(success)
                refreshLayout?.finishLoadMore()
            }
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
        }
    }

    private fun initMine() {
        if (RetrofitManager.isNetworkAvailable()) {
            mBodyParams.clear()

            mBodyParams["type"] = "keji"
            mBodyParams["key"] = "d5cc661633a28f3cf4b1eccff3ee7bae"
            presenter.mineData2(mRxAppCompatActivity, mBodyParams)
        } else {
            showToast(
                ResourcesManager.getString(R.string.please_check_the_network_connection),
                true
            )
            if (isRefresh) {
                refreshLayout?.finishRefresh()
            } else {
                refreshLayout?.finishLoadMore()
            }
        }
    }

}
