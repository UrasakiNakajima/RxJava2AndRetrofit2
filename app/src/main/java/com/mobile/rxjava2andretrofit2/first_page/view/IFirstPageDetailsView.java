package com.mobile.rxjava2andretrofit2.first_page.view;

import com.mobile.common_library.base.IBaseView;
import com.mobile.rxjava2andretrofit2.first_page.bean.FirstPageDetailsResponse;

import java.util.List;

public interface IFirstPageDetailsView extends IBaseView {

    void firstPageDetailsSuccess(List<FirstPageDetailsResponse.DataBean> success);

    void firstPageDetailsError(String error);
}
