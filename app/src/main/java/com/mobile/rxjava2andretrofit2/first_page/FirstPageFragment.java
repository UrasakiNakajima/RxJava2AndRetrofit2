package com.mobile.rxjava2andretrofit2.first_page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.mobile.rxjava2andretrofit2.R;
import com.mobile.rxjava2andretrofit2.base.BaseMvpFragment;
import com.mobile.rxjava2andretrofit2.base.IBaseView;
import com.mobile.rxjava2andretrofit2.first_page.presenter.FirstPagePresenterImpl;
import com.mobile.rxjava2andretrofit2.first_page.view.IFirstPageView;
import com.mobile.rxjava2andretrofit2.main.MainActivity;
import com.mobile.rxjava2andretrofit2.manager.LogManager;
import com.qmuiteam.qmui.widget.QMUILoadingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FirstPageFragment extends BaseMvpFragment<IBaseView, FirstPagePresenterImpl>
        implements IFirstPageView {

    private static final String TAG = "FirstPageFragment";
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tev_first_page)
    TextView tevFirstPage;
    @BindView(R.id.loadView)
    QMUILoadingView loadView;
    Unbinder unbinder;

    private MainActivity mainActivity;

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

        mainActivity = (MainActivity) activity;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_first_page;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {

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
    public void firstPageDataSuccess(String success) {
        showToast(success, true);
    }

    @Override
    public void firstPageDataError(String error) {
        showToast(error, true);
    }

    private void initFirstPage() {
        bodyParams.clear();
//        bodyParams.put("shopId", mineApplication.getShopId());
//        bodyParams.put("userId", mineApplication.getUserId());


        bodyParams.put("qid", "6761573827886448907");
        presenter.firstPageData(bodyParams);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tev_first_page)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tev_first_page:
                LogManager.i(TAG, "tev_first_page");
                initFirstPage();
                break;
        }
    }
}
