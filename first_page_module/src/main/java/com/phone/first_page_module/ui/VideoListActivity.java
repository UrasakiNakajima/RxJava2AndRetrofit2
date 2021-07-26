package com.phone.first_page_module.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.phone.common_library.base.BaseMvpAppActivity;
import com.phone.common_library.base.IBaseView;
import com.phone.common_library.manager.LogManager;
import com.phone.first_page_module.R;
import com.phone.first_page_module.R2;
import com.phone.first_page_module.adapter.VideoListAdapter;
import com.phone.first_page_module.bean.VideoListBean;
import com.phone.first_page_module.presenter.FirstPagePresenterImpl;
import com.phone.first_page_module.view.IVideoListView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/11/19 16:04
 * introduce :
 */

public class VideoListActivity extends BaseMvpAppActivity<IBaseView, FirstPagePresenterImpl>
	implements IVideoListView {
	
	private static final String TAG = "VideoListActivity";
	@BindView(R2.id.imv_back)
	ImageView          imvBack;
	@BindView(R2.id.layout_back)
	FrameLayout        layoutBack;
	@BindView(R2.id.tev_title)
	TextView           tevTitle;
	@BindView(R2.id.toolbar)
	Toolbar            toolbar;
	@BindView(R2.id.rcv_data)
	RecyclerView       rcvData;
	@BindView(R2.id.refresh_layout)
	SmartRefreshLayout refreshLayout;
	
	private String                                 data;
	private List<VideoListBean.LargeImageListBean> videoListBeanList;
	private VideoListAdapter                       videoListAdapter;
	private LinearLayoutManager                    linearLayoutManager;
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
	
}
