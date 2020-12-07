package com.mobile.rxjava2andretrofit2.mine.view;

import com.mobile.rxjava2andretrofit2.base.IBaseView;
import com.mobile.rxjava2andretrofit2.first_page.bean.FirstPageDetailsResponse;

import java.util.List;

public interface IMineDetailsView extends IBaseView {

    void mineDetailsSuccess(List<FirstPageDetailsResponse.DataBean> success);

    void mineDetailsError(String error);
}
