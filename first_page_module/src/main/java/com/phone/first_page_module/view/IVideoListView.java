package com.phone.first_page_module.view;


import com.phone.common_library.base.IBaseView;

public interface IVideoListView extends IBaseView {

    void videoListSuccess(String success);

    void videoListError(String error);

}
