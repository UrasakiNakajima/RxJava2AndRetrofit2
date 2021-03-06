package com.phone.mine_module

import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phone.common_library.base.BaseMvpRxAppActivity
import com.phone.common_library.base.IBaseView
import com.phone.common_library.callback.RcvOnItemViewClickListener
import com.phone.common_library.manager.LogManager
import com.phone.common_library.manager.RetrofitManager
import com.phone.common_library.manager.ScreenManager
import com.phone.common_library.ui.NewsDetailActivity
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

//@Route(path = "/mine_module/ui/mine_details")
class MineActivity : BaseMvpRxAppActivity<IBaseView, MinePresenterImpl>(), IMineView {

    companion object {
        private val TAG: String = MineActivity::class.java.simpleName
    }

//    private var mainActivity: MainActivity? = null

    private var juheNewsBeanList: MutableList<Data> = mutableListOf()
    private var mineAdapter: MineAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var isRefresh: Boolean = true

    override fun initLayoutId(): Int {
        return R.layout.activity_mine
    }

    override fun initData() {
//        mainActivity = rxAppCompatActivity as MainActivity
    }

    override fun initViews() {
        setToolbar(false, R.color.color_FFE066FF)

        tev_title.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
//                initMine()
                startActivity(UserDataActivity::class.java)
            }
        })
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
        linearLayoutManager!!.setOrientation(RecyclerView.VERTICAL)
        rcv_data.layoutManager = (linearLayoutManager)
        rcv_data.itemAnimator = DefaultItemAnimator()

        mineAdapter = MineAdapter(rxAppCompatActivity!!)
        mineAdapter!!.setRcvOnItemViewClickListener(object : RcvOnItemViewClickListener {

            override fun onItemClickListener(position: Int, view: View?) {
//                bodyParams.clear()
//                bodyParams["max_behot_time"] = "1000"
//                startActivityCarryParams(MineDetailsActivity::class.java, bodyParams)

//                //Jump with parameters
//                ARouter.getInstance().build("/mine_module/ui/mine_details")
//                        .withString("max_behot_time", (System.currentTimeMillis() / 1000).toString())
//                        .navigation()

                if (view?.id == R.id.ll_root) {
                    val intent = Intent(rxAppCompatActivity, NewsDetailActivity::class.java)
                    intent.putExtra("detailUrl", juheNewsBeanList.get(position).url)
                    startActivity(intent)
                }
//                startActivity(UserDataActivity::class.java)
            }
        })
        rcv_data.setAdapter(mineAdapter)
        mineAdapter!!.clearData()
        mineAdapter!!.addAllData(juheNewsBeanList)

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

    override fun mineDataSuccess(success: List<Data>) {
        if (!rxAppCompatActivity!!.isFinishing()) {
            if (isRefresh) {
                juheNewsBeanList.clear()
                juheNewsBeanList.addAll(success)
                mineAdapter!!.clearData();
                mineAdapter!!.addAllData(juheNewsBeanList)
                refresh_layout.finishRefresh()
            } else {
                juheNewsBeanList.addAll(success)
                mineAdapter!!.clearData();
                mineAdapter!!.addAllData(juheNewsBeanList)
                refresh_layout.finishLoadMore()
            }
        }
    }

    override fun mineDataError(error: String) {
        if (!rxAppCompatActivity!!.isFinishing()) {
            showCustomToast(ScreenManager.dpToPx(rxAppCompatActivity, 20f), ScreenManager.dpToPx(rxAppCompatActivity, 20f),
                    18, ContextCompat.getColor(rxAppCompatActivity!!,R.color.white),
                    ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FFE066FF), ScreenManager.dpToPx(rxAppCompatActivity, 40f),
                    ScreenManager.dpToPx(rxAppCompatActivity, 20f), error,
                    true)

            if (isRefresh) {
                refresh_layout.finishRefresh(false)
            } else {
                refresh_layout.finishLoadMore(false)
            }
        }
    }

    private fun initMine() {
        if (RetrofitManager.isNetworkAvailable(rxAppCompatActivity)) {
            bodyParams.clear()

            bodyParams["type"] = "keji"
            bodyParams["key"] = "d5cc661633a28f3cf4b1eccff3ee7bae"
            presenter.mineDataRxAppCompatActivity(rxAppCompatActivity, bodyParams)
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
