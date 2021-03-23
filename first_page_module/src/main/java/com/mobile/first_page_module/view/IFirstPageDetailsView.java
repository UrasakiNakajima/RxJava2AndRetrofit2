package com.mobile.first_page_module.view;

import com.mobile.common_library.base.IBaseView;
import com.mobile.first_page_module.bean.FirstPageDetailsResponse;

import java.util.List;

public interface IFirstPageDetailsView extends IBaseView {

    void firstPageDetailsSuccess(List<FirstPageDetailsResponse.DataBean> success);

    void firstPageDetailsError(String error);
}
