package com.mobile.rxjava2andretrofit2.java.mine.view;

import com.mobile.rxjava2andretrofit2.java.base.IBaseView;
import com.mobile.rxjava2andretrofit2.java.mine.bean.MineResponse;

import java.util.List;

public interface IMineView extends IBaseView {

    void mineDataSuccess(List<MineResponse.AnsListBean> success);

    void mineDataError(String error);

}
