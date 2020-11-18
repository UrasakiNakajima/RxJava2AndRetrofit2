package com.mobile.rxjava2andretrofit2.mine.view;

import com.mobile.rxjava2andretrofit2.base.IBaseView;
import com.mobile.rxjava2andretrofit2.mine.bean.MineResponse;

import java.util.List;

public interface IMineView extends IBaseView {

    void mineDataSuccess(List<MineResponse.AnsListBean> success);

    void mineDataError(String error);

}
