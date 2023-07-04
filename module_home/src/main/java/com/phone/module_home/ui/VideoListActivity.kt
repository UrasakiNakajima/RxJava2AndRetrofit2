package com.phone.module_home.ui

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSONObject
import com.phone.library_common.base.BaseMvpRxAppActivity
import com.phone.library_common.base.IBaseView
import com.phone.library_common.manager.LogManager.i
import com.phone.library_common.manager.ResourcesManager
import com.phone.module_home.R
import com.phone.module_home.adapter.LargeImageListBean
import com.phone.module_home.adapter.VideoListAdapter
import com.phone.module_home.adapter.VideoListBean
import com.phone.module_home.presenter.HomePresenterImpl
import com.phone.module_home.view.IVideoListView
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class VideoListActivity : BaseMvpRxAppActivity<IBaseView, HomePresenterImpl>(),
    IVideoListView {

    companion object {
        @JvmStatic
        private val TAG = VideoListActivity::class.java.simpleName
    }

    private var toolbar: Toolbar? = null
    private var layoutBack: FrameLayout? = null
    private var imvBack: ImageView? = null
    private var tevTitle: TextView? = null
    private var refreshLayout: SmartRefreshLayout? = null
    private var rcvData: RecyclerView? = null

    private val videoListBeanList: MutableList<LargeImageListBean> = ArrayList()
    //    private boolean isRefresh;

    override fun initLayoutId(): Int {
        return R.layout.home_activity_video_list
    }

    override fun initData() {
        //        isRefresh = true;
        val intent = intent
        val bundle = intent.extras
        val data = bundle?.getString("data")
        i(TAG, "data*****$data")
        val videoListBean = JSONObject.parseObject(
            data,
            VideoListBean::class.java
        )
        for (i in 0..14) {
            videoListBeanList.add(videoListBean.large_image_list[0])
        }
    }

    override fun initViews() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        layoutBack = findViewById<View>(R.id.layout_back) as FrameLayout
        imvBack = findViewById<View>(R.id.imv_back) as ImageView
        tevTitle = findViewById<View>(R.id.tev_title) as TextView
        refreshLayout = findViewById<View>(R.id.refresh_layout) as SmartRefreshLayout
        rcvData = findViewById<View>(R.id.rcv_data) as RecyclerView
        setToolbar(false, R.color.library_color_FFE066FF)
        imvBack?.setColorFilter(ResourcesManager.getColor(R.color.library_color_FFFFFFFF))
        layoutBack?.setOnClickListener { finish() }
        initAdapter()

        //        Glide.with(this).load("")
        //            .placeholder(com.phone.common_library.R.mipmap.ic_launcher)
        //            .listener(new RequestListener<Drawable>() {
        //
        //                @Override
        //                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        //                    return false;
        //                }
        //
        //                @Override
        //                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
        //                    return false;
        //                }
        //            })
        //            .into(imvBack);
    }

    private fun initAdapter() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        rcvData?.layoutManager = linearLayoutManager
        rcvData?.itemAnimator = DefaultItemAnimator()
        val videoListAdapter = VideoListAdapter(this)
        //        videoListAdapter.setOnItemViewClickListener(new RcvOnItemViewClickListener() {
        //            @Override
        //            public void onItemClickListener(int position, View view) {
        //
        //            }
        //        });
        rcvData?.adapter = videoListAdapter
        videoListAdapter.clearData()
        videoListAdapter.addData(videoListBeanList)
        refreshLayout?.setEnableRefresh(false)
        refreshLayout?.setEnableLoadMore(false)
        //        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
        //            @Override
        //            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        //                LogManager.i(TAG, "onLoadMore");
        //                isRefresh = false;
        //
        //            }
        //
        //            @Override
        //            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        //                isRefresh = true;
        //
        //            }
        //        });
    }

    override fun initLoadData() {}

    override fun attachPresenter(): HomePresenterImpl {
        return HomePresenterImpl(this)
    }

    override fun videoListSuccess(success: String) {}

    override fun videoListError(error: String) {}

    override fun showLoading() {
        if (!mLoadView.isShown) {
            mLoadView.visibility = View.VISIBLE
            mLoadView.start()
        }
    }

    override fun hideLoading() {
        if (mLoadView.isShown) {
            mLoadView.stop()
            mLoadView.visibility = View.GONE
        }
    }

}