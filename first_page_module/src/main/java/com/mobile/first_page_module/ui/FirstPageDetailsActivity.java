package com.mobile.first_page_module.ui;

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

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.mobile.common_library.base.BaseMvpAppActivity;
import com.mobile.common_library.base.IBaseView;
import com.mobile.common_library.callback.RcvOnItemViewClickListener;
import com.mobile.common_library.manager.LogManager;
import com.mobile.common_library.manager.RetrofitManager;
import com.mobile.common_library.manager.ScreenManager;
import com.mobile.first_page_module.R;
import com.mobile.first_page_module.adapter.FirstPageDetailsAdapter;
import com.mobile.first_page_module.bean.FirstPageDetailsResponse;
import com.mobile.first_page_module.presenter.FirstPagePresenterImpl;
import com.mobile.first_page_module.view.IFirstPageDetailsView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.mobile.first_page_module.R2.*;

@Route(path = "/first_page_module/ui/first_page_details")
public class FirstPageDetailsActivity extends BaseMvpAppActivity<IBaseView, FirstPagePresenterImpl>
        implements IFirstPageDetailsView {

    private static final String TAG = "FirstPageDetailsActivity";
    @BindView(id.imv_back)
    ImageView imvBack;
    @BindView(id.layout_back)
    FrameLayout layoutBack;
    @BindView(id.tev_title)
    TextView tevTitle;
    @BindView(id.toolbar)
    Toolbar toolbar;
    @BindView(id.rcv_data)
    RecyclerView rcvData;
    @BindView(id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    @Autowired
    public String max_behot_time;
    private List<FirstPageDetailsResponse.DataBean> dataBeanList;
    private FirstPageDetailsAdapter firstPageDetailsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean isRefresh;
    private boolean isFirstLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_first_page_details;
    }

    @Override
    protected void initData() {
        ARouter.getInstance().inject(this);
//        intent = getIntent();
//        bundle = intent.getExtras();
//        max_behot_time = bundle.getString("max_behot_time");
//        max_behot_time = "1000";
        LogManager.i(TAG, "max_behot_time*****" + max_behot_time);

        dataBeanList = new ArrayList<>();
        isRefresh = true;
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

        firstPageDetailsAdapter = new FirstPageDetailsAdapter(this);
        firstPageDetailsAdapter.setRcvOnItemViewClickListener(new RcvOnItemViewClickListener() {
            @Override
            public void onItemClickListener(int position, View view) {
                String data = dataBeanList.get(position).getContent();
                bodyParams.clear();
                bodyParams.put("data", data);
                startActivityCarryParams(VideoListActivity.class, bodyParams);
            }
        });
        rcvData.setAdapter(firstPageDetailsAdapter);
        firstPageDetailsAdapter.clearData();
        firstPageDetailsAdapter.addAllData(dataBeanList);

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogManager.i(TAG, "onLoadMore");
                isRefresh = false;
                initFirstPageDetails();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                initFirstPageDetails();
            }
        });
    }

    private void initFirstPageDetails() {
        if (RetrofitManager.isNetworkAvailable(this)) {
            if (isFirstLoad) {
                isFirstLoad = false;
            } else {
                max_behot_time = System.currentTimeMillis() / 1000 + "";
            }

//        max_behot_time = 1605844009 + "";
//        max_behot_time = 1605844868 + "";
            bodyParams.clear();
            bodyParams.put("category", "video");
            bodyParams.put("max_behot_time", max_behot_time);
            presenter.firstPageDetails(bodyParams);
        } else {
            showToast(getResources().getString(R.string.please_check_the_network_connection), true);
            if (isRefresh) {
                refreshLayout.finishRefresh();
            } else {
                refreshLayout.finishLoadMore();
            }
        }
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
    public void firstPageDetailsSuccess(List<FirstPageDetailsResponse.DataBean> success) {
//        showToast(success, true);

        if (!this.isFinishing()) {
            if (isRefresh) {
                dataBeanList.clear();
                dataBeanList.addAll(success);
                firstPageDetailsAdapter.clearData();
                firstPageDetailsAdapter.addAllData(dataBeanList);
                refreshLayout.finishRefresh();
            } else {
                dataBeanList.addAll(success);
                firstPageDetailsAdapter.clearData();
                firstPageDetailsAdapter.addAllData(dataBeanList);
                refreshLayout.finishLoadMore();
            }
        }
    }

    @Override
    public void firstPageDetailsError(String error) {
        if (!this.isFinishing()) {
//            showToast(error, true);
            showCustomToast(ScreenManager.dipTopx(this, 20f), ScreenManager.dipTopx(this, 20f),
                    16, getResources().getColor(R.color.white),
                    getResources().getColor(R.color.color_FFE066FF), ScreenManager.dipTopx(this, 40f),
                    ScreenManager.dipTopx(this, 20f), error);
            if (isRefresh) {
                refreshLayout.finishRefresh(false);
            } else {
                refreshLayout.finishLoadMore(false);
            }
        }
    }

    @OnClick(id.layout_back)
    public void onViewClicked(View view) {
        if (view.getId() == id.layout_back) {
            finish();
        }
    }
}
