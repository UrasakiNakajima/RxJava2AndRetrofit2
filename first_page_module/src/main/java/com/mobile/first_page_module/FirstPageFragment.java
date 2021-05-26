package com.mobile.first_page_module;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.mobile.common_library.base.BaseMvpFragment;
import com.mobile.common_library.base.IBaseView;
import com.mobile.common_library.callback.RcvOnItemViewClickListener;
import com.mobile.common_library.manager.LogManager;
import com.mobile.common_library.manager.RetrofitManager;
import com.mobile.common_library.manager.ScreenManager;
import com.mobile.first_page_module.adapter.FirstPageAdapter;
import com.mobile.first_page_module.bean.FirstPageResponse;
import com.mobile.first_page_module.presenter.FirstPagePresenterImpl;
import com.mobile.first_page_module.ui.ShowVideoActivity;
import com.mobile.first_page_module.view.IFirstPageView;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.mobile.first_page_module.R2.id;

@Route(path = "/first_page_module/first_page")
public class FirstPageFragment extends BaseMvpFragment<IBaseView, FirstPagePresenterImpl>
	implements IFirstPageView {
	
	private static final String TAG = "FirstPageFragment";
	@BindView(id.tev_title)
	TextView           tevTitle;
	@BindView(id.toolbar)
	Toolbar            toolbar;
	@BindView(id.rcv_data)
	RecyclerView       rcvData;
	@BindView(id.refresh_layout)
	SmartRefreshLayout refreshLayout;
	@BindView(id.loadView)
	QMUILoadingView    loadView;
	
	//    private MainActivity mainActivity;
	
	private List<FirstPageResponse.AnsListBean> ansListBeanList;
	private FirstPageAdapter                    firstPageAdapter;
	private LinearLayoutManager                 linearLayoutManager;
	private boolean                             isRefresh;
	private Map<String, String>                 paramMap;
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO: inflate a fragment view
		rootView = super.onCreateView(inflater, container, savedInstanceState);
		return rootView;
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	protected int initLayoutId() {
		return R.layout.fragment_first_page;
	}
	
	@Override
	protected void initData() {
		ansListBeanList = new ArrayList<>();
		paramMap = new HashMap<>();
		//        mainActivity = (MainActivity) activity;
		isRefresh = true;
	}
	
	@Override
	protected void initViews() {
		tevTitle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LogManager.i(TAG, "tev_title");
				initFirstPage();
			}
		});
		
		initAdapter();
	}
	
	private void initAdapter() {
		linearLayoutManager = new LinearLayoutManager(activity);
		linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
		rcvData.setLayoutManager(linearLayoutManager);
		rcvData.setItemAnimator(new DefaultItemAnimator());
		
		firstPageAdapter = new FirstPageAdapter(activity);
		firstPageAdapter.setRcvOnItemViewClickListener(new RcvOnItemViewClickListener() {
			@Override
			public void onItemClickListener(int position, View view) {
				//                if (view.getId() == R.id.tev_data) {
				//                        bodyParams.clear();
				//                        bodyParams.put("max_behot_time", System.currentTimeMillis() / 1000 + "");
				//                        startActivityCarryParams(FirstPageDetailsActivity.class, bodyParams);
				
				//				//Jump with parameters
				//				ARouter.getInstance().build("/first_page_module/ui/first_page_details")
				//					.withString("max_behot_time", (System.currentTimeMillis() / 1000) + "")
				//					.navigation();
				
				//                }
				
				if (view.getId() == R.id.tev_data) {
					//					url = "http://rbv01.ku6.com/omtSn0z_PTREtneb3GRtGg.mp4";
					//					url = "http://rbv01.ku6.com/7lut5JlEO-v6a8K3X9xBNg.mp4";
					url = "https://t-cmcccos.cxzx10086.cn/statics/shopping/detective_conan_japanese.mp4";
					//					fileFullname = mFileDTO.getFName();
					String[] arr = url.split("\\.");
					if (arr != null && arr.length > 0) {
						String fileName = "";
						StringBuilder stringBuilder = new StringBuilder();
						for (int i = 0; i < arr.length - 1; i++) {
							stringBuilder.append(arr[i]);
						}
						fileName = stringBuilder.toString();
						String suffix = arr[arr.length - 1];
						
						paramMap.clear();
						paramMap.put("url", url);
						paramMap.put("suffix", suffix);
						startActivityCarryParams(ShowVideoActivity.class, paramMap);
					}
					
				}
			}
		});
		rcvData.setAdapter(firstPageAdapter);
		firstPageAdapter.clearData();
		firstPageAdapter.addAllData(ansListBeanList);
		
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
	public void firstPageDataSuccess(List<FirstPageResponse.AnsListBean> success) {
		if (!activity.isFinishing()) {
			if (isRefresh) {
				ansListBeanList.clear();
				ansListBeanList.addAll(success);
				firstPageAdapter.addAllData(ansListBeanList);
				refreshLayout.finishRefresh();
			} else {
				ansListBeanList.addAll(success);
				firstPageAdapter.addAllData(ansListBeanList);
				refreshLayout.finishLoadMore();
			}
		}
	}
	
	@Override
	public void firstPageDataError(String error) {
		if (!activity.isFinishing()) {
			//            showToast(error, true);
			showCustomToast(ScreenManager.dipTopx(activity, 20f), ScreenManager.dipTopx(activity, 20f),
							18, getResources().getColor(R.color.white),
							getResources().getColor(R.color.color_FFE066FF), ScreenManager.dipTopx(activity, 40f),
							ScreenManager.dipTopx(activity, 20f), error,
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
		if (RetrofitManager.isNetworkAvailable(activity)) {
			bodyParams.clear();
			
			bodyParams.put("qid", "6855150375201390856");
			presenter.firstPage(this, bodyParams);
		} else {
			firstPageDataError(getResources().getString(R.string.please_check_the_network_connection));
		}
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
}
