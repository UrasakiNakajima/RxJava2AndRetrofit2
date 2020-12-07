package com.mobile.rxjava2andretrofit2.mine;

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

import com.mobile.rxjava2andretrofit2.R;
import com.mobile.rxjava2andretrofit2.base.BaseMvpFragment;
import com.mobile.rxjava2andretrofit2.base.IBaseView;
import com.mobile.rxjava2andretrofit2.callback.RcvOnItemViewClickListener;
import com.mobile.rxjava2andretrofit2.main.MainActivity;
import com.mobile.rxjava2andretrofit2.manager.LogManager;
import com.mobile.rxjava2andretrofit2.mine.adapter.MineAdapter;
import com.mobile.rxjava2andretrofit2.mine.bean.MineResponse;
import com.mobile.rxjava2andretrofit2.mine.presenter.MinePresenterImpl;
import com.mobile.rxjava2andretrofit2.mine.ui.MineDetailsActivity;
import com.mobile.rxjava2andretrofit2.mine.view.IMineView;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.http.Field;

public class MineFragment extends BaseMvpFragment<IBaseView, MinePresenterImpl>
        implements IMineView {

    private static final String TAG = "MineFragment";
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
    Unbinder unbinder;

    private MainActivity mainActivity;

    private List<MineResponse.AnsListBean> ansListBeanList;
    private MineAdapter mineAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean isRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_mine;
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
        linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvData.setLayoutManager(linearLayoutManager);
        rcvData.setItemAnimator(new DefaultItemAnimator());

        mineAdapter = new MineAdapter(activity);
//        mineAdapter.setRcvOnItemViewClickListener(new RcvOnItemViewClickListener() {
//            @Override
//            public void onItemClickListener(int position, View view) {
//                startActivity(MineDetailsActivity.class);
//            }
//        });
        mineAdapter.setRcvOnItemViewClickListener((position, view) -> {
            bodyParams.clear();
            bodyParams.put("max_behot_time", "1000");
            startActivityCarryParams(MineDetailsActivity.class, bodyParams);
        });
        rcvData.setAdapter(mineAdapter);
        mineAdapter.clearData();
        mineAdapter.addAllData(ansListBeanList);

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogManager.i(TAG, "onLoadMore");
                isRefresh = false;
                initMine();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                initMine();
            }
        });
    }

    @Override
    protected void initLoadData() {
        refreshLayout.autoRefresh();
    }

    @Override
    protected MinePresenterImpl attachPresenter() {
        return new MinePresenterImpl(this);
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
    public void mineDataSuccess(List<MineResponse.AnsListBean> success) {
//        showToast(success, true);

        if (!mainActivity.isFinishing()) {
            if (isRefresh) {
                ansListBeanList.clear();
                ansListBeanList.addAll(success);
                mineAdapter.addAllData(ansListBeanList);
                refreshLayout.finishRefresh();
            } else {
                ansListBeanList.addAll(success);
                mineAdapter.addAllData(ansListBeanList);
                refreshLayout.finishLoadMore();
            }
        }
    }

    @Override
    public void mineDataError(String error) {
        if (!mainActivity.isFinishing()) {
            showToast(error, true);
            if (isRefresh) {
                refreshLayout.finishRefresh();
            } else {
                refreshLayout.finishLoadMore();
            }
        }
    }

    private void initMine() {
        bodyParams.clear();

        bodyParams.put("qid", "6463093341545300238");
//        bodyParams.put("max_behot_time", System.currentTimeMillis() / 1000 + "");
        presenter.mineData(bodyParams);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tev_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tev_title:
                LogManager.i(TAG, "tev_title");
                initMine();
                break;
        }
    }
}
