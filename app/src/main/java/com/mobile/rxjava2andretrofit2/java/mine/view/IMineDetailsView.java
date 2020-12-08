package com.mobile.rxjava2andretrofit2.java.mine.view;

import com.mobile.rxjava2andretrofit2.java.base.IBaseView;
import com.mobile.rxjava2andretrofit2.kotlin.mine.bean.Data;

import java.util.List;

public interface IMineDetailsView extends IBaseView {

    void mineDetailsSuccess(List<Data> success);

    void mineDetailsError(String error);
}
