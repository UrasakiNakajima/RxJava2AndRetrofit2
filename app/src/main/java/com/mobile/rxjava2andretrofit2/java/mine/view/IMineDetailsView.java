package com.mobile.rxjava2andretrofit2.java.mine.view;

import com.mobile.rxjava2andretrofit2.java.base.IBaseView;
import com.mobile.rxjava2andretrofit2.java.first_page.bean.FirstPageDetailsResponse;

import java.util.List;

public interface IMineDetailsView extends IBaseView {

    void mineDetailsSuccess(List<FirstPageDetailsResponse.DataBean> success);

    void mineDetailsError(String error);
}
