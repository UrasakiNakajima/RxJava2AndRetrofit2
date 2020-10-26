package com.mobile.rxjava2andretrofit2.mine.ui;

import android.os.Bundle;
import android.view.View;

import com.mobile.rxjava2andretrofit2.R;
import com.mobile.rxjava2andretrofit2.base.BaseMvpAppActivity;
import com.mobile.rxjava2andretrofit2.base.IBaseView;
import com.mobile.rxjava2andretrofit2.mine.presenter.MinePresenterImpl;
import com.mobile.rxjava2andretrofit2.mine.view.IFeedbackView;

public class FeedbackActivity extends BaseMvpAppActivity<IBaseView, MinePresenterImpl>
        implements IFeedbackView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        addContentView(loadView, layoutParams);
        setToolbar(true, R.color.color_FFFFFFFF);
    }

    @Override
    protected void initLoadData() {
        initSubmitFeedback();
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
    public void submitFeedbackSuccess(String success) {

    }

    @Override
    public void submitFeedbackError(String error) {

    }

    private void initSubmitFeedback() {
        bodyParams.clear();
//        bodyParams.put("shopId", mineApplication.getShopId());
//        bodyParams.put("userId", mineApplication.getUserId());

//        //产品反馈
//        bodyParams.put("userId", mineApplication.getUserId());
//        bodyParams.put("type", 1 + "");
//        bodyParams.put("typeName", "功能异常");
//        bodyParams.put("problem", "感觉还行啊");
//        bodyParams.put("img", "");

        bodyParams.put("qid", "6761573827886448907");
        presenter.submitFeedback(bodyParams);
    }

}
