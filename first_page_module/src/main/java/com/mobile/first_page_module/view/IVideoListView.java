package com.mobile.first_page_module.view;


import com.mobile.common_library.base.IBaseView;

public interface IVideoListView extends IBaseView {

    void videoListSuccess(String success);

    void videoListError(String error);

}
