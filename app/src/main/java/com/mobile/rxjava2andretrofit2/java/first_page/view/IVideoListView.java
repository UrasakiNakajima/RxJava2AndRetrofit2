package com.mobile.rxjava2andretrofit2.java.first_page.view;

import com.mobile.rxjava2andretrofit2.java.base.IBaseView;

public interface IVideoListView extends IBaseView {

    void videoListSuccess(String success);

    void videoListError(String error);

}
