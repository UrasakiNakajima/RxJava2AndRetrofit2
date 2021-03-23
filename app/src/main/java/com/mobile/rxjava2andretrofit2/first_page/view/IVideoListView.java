package com.mobile.rxjava2andretrofit2.first_page.view;


import com.mobile.common_library.base.IBaseView;

public interface IVideoListView extends IBaseView {

    void videoListSuccess(String success);

    void videoListError(String error);

}
