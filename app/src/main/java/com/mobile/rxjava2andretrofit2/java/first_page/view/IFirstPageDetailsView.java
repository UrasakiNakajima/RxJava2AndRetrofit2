package com.mobile.rxjava2andretrofit2.java.first_page.view;

import com.mobile.rxjava2andretrofit2.java.base.IBaseView;
import com.mobile.rxjava2andretrofit2.java.first_page.bean.FirstPageDetailsResponse;

import java.util.List;

public interface IFirstPageDetailsView extends IBaseView {

    void firstPageDetailsSuccess(List<FirstPageDetailsResponse.DataBean> success);

    void firstPageDetailsError(String error);
}
