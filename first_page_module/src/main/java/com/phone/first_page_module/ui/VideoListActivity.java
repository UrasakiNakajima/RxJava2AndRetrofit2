package com.phone.first_page_module.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.phone.common_library.base.BaseMvpRxAppActivity;
import com.phone.common_library.base.IBaseView;
import com.phone.common_library.manager.LogManager;
import com.phone.common_library.manager.ResourcesManager;
import com.phone.first_page_module.R;
import com.phone.first_page_module.adapter.VideoListAdapter;
import com.phone.first_page_module.bean.VideoListBean;
import com.phone.first_page_module.presenter.FirstPagePresenterImpl;
import com.phone.first_page_module.view.IVideoListView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/11/19 16:04
 * introduce :
 */

public class VideoListActivity extends BaseMvpRxAppActivity<IBaseView, FirstPagePresenterImpl>
        implements IVideoListView {

    private static final String TAG = "VideoListActivity";
    private Toolbar toolbar;
    private FrameLayout layoutBack;
    private ImageView imvBack;
    private TextView tevTitle;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView rcvData;

    private final List<VideoListBean.LargeImageListBean> videoListBeanList = new ArrayList<>();
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
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String data = bundle.getString("data");
        LogManager.i(TAG, "data*****" + data);
        VideoListBean videoListBean = JSONObject.parseObject(data, VideoListBean.class);
        for (int i = 0; i < 15; i++) {
            videoListBeanList.add(videoListBean.getLarge_image_list().get(0));
        }
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        layoutBack = (FrameLayout) findViewById(R.id.layout_back);
        imvBack = (ImageView) findViewById(R.id.imv_back);
        tevTitle = (TextView) findViewById(R.id.tev_title);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        rcvData = (RecyclerView) findViewById(R.id.rcv_data);

        setToolbar(false, R.color.color_FFE066FF);
        imvBack.setColorFilter(ResourcesManager.getColor(R.color.color_FFFFFFFF));

        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initAdapter();

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

    private void initAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvData.setLayoutManager(linearLayoutManager);
        rcvData.setItemAnimator(new DefaultItemAnimator());

        VideoListAdapter videoListAdapter = new VideoListAdapter(this);
        //        videoListAdapter.setRcvOnItemViewClickListener(new RcvOnItemViewClickListener() {
        //            @Override
        //            public void onItemClickListener(int position, View view) {
        //
        //            }
        //        });
        rcvData.setAdapter(videoListAdapter);
        videoListAdapter.clearData();
        videoListAdapter.addData(videoListBeanList);

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

    @NonNull
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
        if (getLoadView() != null && !getLoadView().isShown()) {
            getLoadView().setVisibility(View.VISIBLE);
            getLoadView().start();
        }
    }

    @Override
    public void hideLoading() {
        if (getLoadView() != null && getLoadView().isShown()) {
            getLoadView().stop();
            getLoadView().setVisibility(View.GONE);
        }
    }

}
