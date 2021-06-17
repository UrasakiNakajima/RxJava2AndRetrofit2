package com.mobile.first_page_module.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mobile.common_library.base.BaseMvpAppActivity;
import com.mobile.common_library.base.IBaseView;
import com.mobile.common_library.callback.RcvOnItemViewClickListener;
import com.mobile.common_library.manager.LogManager;
import com.mobile.common_library.manager.RetrofitManager;
import com.mobile.common_library.manager.ScreenManager;
import com.mobile.first_page_module.R;
import com.mobile.first_page_module.adapter.FirstPageAdapter;
import com.mobile.first_page_module.bean.FirstPageResponse;
import com.mobile.first_page_module.presenter.FirstPagePresenterImpl;
import com.mobile.first_page_module.view.IFirstPageView;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FirstPageActivity extends BaseMvpAppActivity<IBaseView, FirstPagePresenterImpl>
	implements IFirstPageView {
	
	private static final String             TAG = "FirstPageDetailsActivity";
	private              Toolbar            toolbar;
	private              TextView           tevTitle;
	private              SmartRefreshLayout refreshLayout;
	private              RecyclerView       rcvData;
	private              QMUILoadingView    loadView;
	
	private List<FirstPageResponse.ResultData.JuheNewsBean> mJuheNewsBeanList = new ArrayList<>();
	private FirstPageAdapter                                firstPageAdapter;
	private LinearLayoutManager                             linearLayoutManager;
	private boolean                                         isRefresh;
	private boolean                                         isFirstLoad;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected int initLayoutId() {
		return R.layout.activity_first_page;
	}
	
	@Override
	protected void initData() {
		isRefresh = true;
	}
	
	@Override
	protected void initViews() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		tevTitle = (TextView) findViewById(R.id.tev_title);
		refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
		rcvData = (RecyclerView) findViewById(R.id.rcv_data);
		loadView = (QMUILoadingView) findViewById(R.id.loadView);
		
		setToolbar(false, R.color.color_FFE066FF);
		initAdapter();
	}
	
	private void initAdapter() {
		linearLayoutManager = new LinearLayoutManager(appCompatActivity);
		linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
		rcvData.setLayoutManager(linearLayoutManager);
		rcvData.setItemAnimator(new DefaultItemAnimator());
		
		firstPageAdapter = new FirstPageAdapter(appCompatActivity);
		//		firstPageAdapter = new FirstPageAdapter2(activity, R.layout.item_first_page);
		firstPageAdapter.setRcvOnItemViewClickListener(new RcvOnItemViewClickListener() {
			@Override
			public void onItemClickListener(int position, View view) {
				//				if (view.getId() == R.id.tev_data) {
				//					//					url = "http://rbv01.ku6.com/omtSn0z_PTREtneb3GRtGg.mp4";
				//					//					url = "http://rbv01.ku6.com/7lut5JlEO-v6a8K3X9xBNg.mp4";
				//					url = "https://t-cmcccos.cxzx10086.cn/statics/shopping/detective_conan_japanese.mp4";
				//					//					fileFullname = mFileDTO.getFName();
				//					String[] arr = url.split("\\.");
				//					if (arr != null && arr.length > 0) {
				//						String fileName = "";
				//						StringBuilder stringBuilder = new StringBuilder();
				//						for (int i = 0; i < arr.length - 1; i++) {
				//							stringBuilder.append(arr[i]);
				//						}
				//						fileName = stringBuilder.toString();
				//						String suffix = arr[arr.length - 1];
				//
				//						paramMap.clear();
				//						paramMap.put("url", url);
				//						paramMap.put("suffix", suffix);
				//						startActivityCarryParams(ShowVideoActivity.class, paramMap);
				//					}
				//
				//				}
				
				if (view.getId() == R.id.ll_root) {
					Intent intent = new Intent(appCompatActivity, NewsDetailActivity.class);
					intent.putExtra("detailUrl", mJuheNewsBeanList.get(position).getUrl());
					startActivity(intent);
				}
			}
		});
		rcvData.setAdapter(firstPageAdapter);
		firstPageAdapter.clearData();
		firstPageAdapter.addAllData(mJuheNewsBeanList);
		
		refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
			@Override
			public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
				LogManager.i(TAG, "onLoadMore");
				isRefresh = false;
				initFirstPage();
			}
			
			@Override
			public void onRefresh(@NonNull RefreshLayout refreshLayout) {
				isRefresh = true;
				initFirstPage();
			}
		});
	}
	
	@Override
	protected void initLoadData() {
		refreshLayout.autoRefresh();
	}
	
	@Override
	protected FirstPagePresenterImpl attachPresenter() {
		return new FirstPagePresenterImpl(this);
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
	
	@Override
	public void firstPageDataSuccess(List<FirstPageResponse.ResultData.JuheNewsBean> success) {
		if (!this.isFinishing()) {
			if (isRefresh) {
				mJuheNewsBeanList.clear();
				mJuheNewsBeanList.addAll(success);
				firstPageAdapter.addAllData(mJuheNewsBeanList);
				refreshLayout.finishRefresh();
			} else {
				mJuheNewsBeanList.addAll(success);
				firstPageAdapter.addAllData(mJuheNewsBeanList);
				refreshLayout.finishLoadMore();
			}
		}
	}
	
	@Override
	public void firstPageDataError(String error) {
		if (!this.isFinishing()) {
			//            showToast(error, true);
			showCustomToast(ScreenManager.dpToPx(this, 20f), ScreenManager.dpToPx(this, 20f),
							16, getResources().getColor(R.color.white),
							getResources().getColor(R.color.color_FFE066FF), ScreenManager.dpToPx(this, 40f),
							ScreenManager.dpToPx(this, 20f), error,
							true);
			if (isRefresh) {
				refreshLayout.finishRefresh(false);
			} else {
				refreshLayout.finishLoadMore(false);
			}
		}
	}
	
	private void initFirstPage() {
		showLoading();
		if (RetrofitManager.isNetworkAvailable(appCompatActivity)) {
			bodyParams.clear();
			
			bodyParams.put("type", "yule");
			bodyParams.put("key", "d5cc661633a28f3cf4b1eccff3ee7bae");
			presenter.firstPage(appCompatActivity, bodyParams);
		} else {
			firstPageDataError(getResources().getString(R.string.please_check_the_network_connection));
		}
	}
	
}
