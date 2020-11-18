package com.mobile.rxjava2andretrofit2.first_page.view;

import com.mobile.rxjava2andretrofit2.base.IBaseView;

public interface IFirstPageDetailsView extends IBaseView {

    void firstPageDetailsSuccess(String success);

    void firstPageDetailsError(String error);
}
