package com.phone.mine_module

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.phone.common_library.base.BaseMvpRxAppActivity
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
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce :
 */
//@Route(path = "/mine_module/ui/mine_details")
class MineActivity : BaseMvpRxAppActivity<IBaseView, MinePresenterImpl>(), IMineView {

    companion object {
        private val TAG: String = MineActivity::class.java.simpleName
    }

    private val mineAdapter by lazy { MineAdapter(rxAppCompatActivity) }
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var isRefresh: Boolean = true

    override fun initLayoutId() = R.layout.activity_mine

    override fun initData() {
    }

    override fun initViews() {
        setToolbar(false, R.color.color_FFE066FF)

        tev_title.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
//                initMine()
                startActivity(UserDataActivity::class.java)
            }
        })
        tev_logout.setOnClickListener {
            showToast(
                ResourcesManager.getString(R.string.this_function_can_only_be_used_under_componentization),
                false
            )
//            baseApplication?.isLogin = false
//            ARouter.getInstance().build("/main_module/login").navigation()
        }
        tev_thread_pool.setOnClickListener {
            startActivity(ThreadPoolActivity::class.java)
        }
        tev_params_transfer_change_problem.setOnClickListener {
            startActivity(ParamsTransferChangeProblemActivity::class.java)
        }

        initAdapter()
    }

    private fun initAdapter() {
        linearLayoutManager = LinearLayoutManager(rxAppCompatActivity)
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL)
        rcv_data.layoutManager = (linearLayoutManager)
        rcv_data.itemAnimator = DefaultItemAnimator()

        mineAdapter.setRcvOnItemViewClickListener(object : OnItemViewClickListener {

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
//                startActivity(UserDataActivity::class.java)
            }
        })
        rcv_data.setAdapter(mineAdapter)

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

    override fun mineDataSuccess(success: MutableList<Data>) {
        if (!rxAppCompatActivity.isFinishing()) {
            if (isRefresh) {
                mineAdapter.apply {
                    clearData();
                    addData(success)
                }
                refresh_layout.finishRefresh()
            } else {
                mineAdapter.addData(success)
                refresh_layout.finishLoadMore()
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
                refresh_layout.finishRefresh(false)
            } else {
                refresh_layout.finishLoadMore(false)
            }
        }
    }

    private fun initMine() {
        if (RetrofitManager.isNetworkAvailable()) {
            bodyParams.clear()

            bodyParams["type"] = "keji"
            bodyParams["key"] = "d5cc661633a28f3cf4b1eccff3ee7bae"
            presenter?.mineData2(rxAppCompatActivity, bodyParams)
        } else {
            showToast(
                ResourcesManager.getString(R.string.please_check_the_network_connection),
                true
            )
            if (isRefresh) {
                refresh_layout.finishRefresh()
            } else {
                refresh_layout.finishLoadMore()
            }
        }
    }

}
