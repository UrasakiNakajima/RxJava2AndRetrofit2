package com.mobile.rxjava2andretrofit2.first_page.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.rxjava2andretrofit2.R;
import com.mobile.rxjava2andretrofit2.base.BaseMvpAppActivity;
import com.mobile.rxjava2andretrofit2.base.IBaseView;
import com.mobile.rxjava2andretrofit2.first_page.presenter.FirstPagePresenterImpl;
import com.mobile.rxjava2andretrofit2.first_page.view.IFirstPageDetailsView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import butterknife.BindView;

public class FirstPageDetailsActivity extends BaseMvpAppActivity<IBaseView, FirstPagePresenterImpl>
        implements IFirstPageDetailsView {

    private static final String TAG = "FirstPageDetailsActivity";
    @BindView(R.id.tev_title)
    TextView tevTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rcv_data)
    RecyclerView rcvData;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

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

    }

    @Override
    protected void initViews() {

        addContentView(loadView, layoutParams);
    }

    @Override
    protected void initLoadData() {

    }

    @Override
    protected FirstPagePresenterImpl attachPresenter() {
        return new FirstPagePresenterImpl(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void firstPageDetailsSuccess(String success) {

    }

    @Override
    public void firstPageDetailsError(String error) {

    }
}
