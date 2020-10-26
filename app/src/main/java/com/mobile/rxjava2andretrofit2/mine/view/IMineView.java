package com.mobile.rxjava2andretrofit2.mine.view;

import com.mobile.rxjava2andretrofit2.base.IBaseView;

public interface IMineView extends IBaseView {

    void mineDataSuccess(String success);

    void mineDataError(String error);

}
