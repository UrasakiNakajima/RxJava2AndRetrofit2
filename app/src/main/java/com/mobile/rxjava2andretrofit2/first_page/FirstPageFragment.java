package com.mobile.rxjava2andretrofit2.first_page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.mobile.common_library.base.BaseMvpFragment;
import com.mobile.common_library.base.IBaseView;
import com.mobile.common_library.callback.RcvOnItemViewClickListener;
import com.mobile.common_library.manager.LogManager;
import com.mobile.common_library.manager.RetrofitManager;
import com.mobile.common_library.manager.ScreenManager;
import com.mobile.rxjava2andretrofit2.R;
import com.mobile.rxjava2andretrofit2.first_page.adapter.FirstPageAdapter;
import com.mobile.rxjava2andretrofit2.first_page.bean.FirstPageResponse;
import com.mobile.rxjava2andretrofit2.first_page.presenter.FirstPagePresenterImpl;
import com.mobile.rxjava2andretrofit2.first_page.view.IFirstPageView;
import com.mobile.rxjava2andretrofit2.main.MainActivity;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FirstPageFragment extends BaseMvpFragment<IBaseView, FirstPagePresenterImpl>
        implements IFirstPageView {

    private static final String TAG = "FirstPageFragment";
    @BindView(R.id.tev_title)
    TextView tevTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rcv_data)
    RecyclerView rcvData;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.loadView)
    QMUILoadingView loadView;

    private MainActivity mainActivity;

    private List<FirstPageResponse.AnsListBean> ansListBeanList;
    private FirstPageAdapter firstPageAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean isRefresh;

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
        mainActivity = (MainActivity) activity;
        isRefresh = true;
    }

    @Override
    protected void initViews() {

        initAdapter();
    }

    private void initAdapter() {
        linearLayoutManager = new LinearLayoutManager(mainActivity);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvData.setLayoutManager(linearLayoutManager);
        rcvData.setItemAnimator(new DefaultItemAnimator());

        firstPageAdapter = new FirstPageAdapter(mainActivity);
        firstPageAdapter.setRcvOnItemViewClickListener(new RcvOnItemViewClickListener() {
            @Override
            public void onItemClickListener(int position, View view) {
                switch (view.getId()) {
                    case R.id.tev_data:
//                        bodyParams.clear();
//                        bodyParams.put("max_behot_time", System.currentTimeMillis() / 1000 + "");
//                        startActivityCarryParams(FirstPageDetailsActivity.class, bodyParams);

                        //Jump with parameters
                        ARouter.getInstance().build("/first_page_module/ui/first_page_details")
                                .withString("max_behot_time", (System.currentTimeMillis() / 1000) + "")
                                .navigation();
                        break;
                    default:
                        break;
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
        if (!mainActivity.isFinishing()) {
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
        if (!mainActivity.isFinishing()) {
//            showToast(error, true);
            showCustomToast(ScreenManager.dipTopx(activity, 20f), ScreenManager.dipTopx(activity, 20f),
                    18, getResources().getColor(R.color.white),
                    getResources().getColor(R.color.color_FFE066FF), ScreenManager.dipTopx(activity, 40f),
                    ScreenManager.dipTopx(activity, 20f), error);
            if (isRefresh) {
                refreshLayout.finishRefresh(false);
            } else {
                refreshLayout.finishLoadMore(false);
            }
        }
    }

    private void initFirstPage() {
        showLoading();
        if (RetrofitManager.isNetworkAvailable(mainActivity)) {
            bodyParams.clear();

            bodyParams.put("qid", "6855150375201390856");
            presenter.firstPage(bodyParams);
        } else {
            firstPageDataError(getResources().getString(R.string.please_check_the_network_connection));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.tev_title)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tev_title:
                LogManager.i(TAG, "tev_title");
                initFirstPage();
                break;
        }
    }
}
