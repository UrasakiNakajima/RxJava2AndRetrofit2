package com.mobile.rxjava2andretrofit2.first_page.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.mobile.common_library.base.BaseMvpAppActivity;
import com.mobile.common_library.base.IBaseView;
import com.mobile.common_library.manager.LogManager;
import com.mobile.rxjava2andretrofit2.R;
import com.mobile.rxjava2andretrofit2.first_page.adapter.VideoListAdapter;
import com.mobile.rxjava2andretrofit2.first_page.bean.VideoListBean;
import com.mobile.rxjava2andretrofit2.first_page.presenter.FirstPagePresenterImpl;
import com.mobile.rxjava2andretrofit2.first_page.view.IVideoListView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/11/19 16:04
 * introduce :
 */
public class VideoListActivity extends BaseMvpAppActivity<IBaseView, FirstPagePresenterImpl>
        implements IVideoListView {

    private static final String TAG = "VideoListActivity";
    @BindView(R.id.imv_back)
    ImageView imvBack;
    @BindView(R.id.layout_back)
    FrameLayout layoutBack;
    @BindView(R.id.tev_title)
    TextView tevTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rcv_data)
    RecyclerView rcvData;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    private String data;
    private List<VideoListBean.LargeImageListBean> videoListBeanList;
    private VideoListAdapter videoListAdapter;
    private LinearLayoutManager linearLayoutManager;
//    private boolean isRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_video_list;
    }

    @Override
    protected void initData() {
//        isRefresh = true;
        intent = getIntent();
        bundle = intent.getExtras();
        data = bundle.getString("data");
        LogManager.i(TAG, "data*****" + data);
        VideoListBean videoListBean = JSONObject.parseObject(data, VideoListBean.class);
        videoListBeanList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            videoListBeanList.add(videoListBean.getLarge_image_list().get(0));
        }
    }

    @Override
    protected void initViews() {
        setToolbar(false, R.color.color_FFE066FF);
        addContentView(loadView, layoutParams);
        imvBack.setColorFilter(getResources().getColor(R.color.color_FFFFFFFF));

        initAdapter();
    }

    private void initAdapter() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvData.setLayoutManager(linearLayoutManager);
        rcvData.setItemAnimator(new DefaultItemAnimator());

        videoListAdapter = new VideoListAdapter(this);
//        videoListAdapter.setRcvOnItemViewClickListener(new RcvOnItemViewClickListener() {
//            @Override
//            public void onItemClickListener(int position, View view) {
//
//            }
//        });
        rcvData.setAdapter(videoListAdapter);
        videoListAdapter.clearData();
        videoListAdapter.addAllData(videoListBeanList);

        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(false);
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

    @Override
    protected void initLoadData() {

    }

    @Override
    protected FirstPagePresenterImpl attachPresenter() {
        return new FirstPagePresenterImpl(this);
    }

    @Override
    public void videoListSuccess(String success) {

    }

    @Override
    public void videoListError(String error) {

    }

    @Override
    public void showLoading() {
        if (loadView != null && !loadView.isShown()) {
            loadView.setVisibility(View.VISIBLE);
            loadView.start();
        }
    }

    @Override
    public void hideLoading() {
        if (loadView != null && loadView.isShown()) {
            loadView.stop();
            loadView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.layout_back)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
        }
    }
}
