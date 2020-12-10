package com.mobile.rxjava2andretrofit2.first_page.view;


import com.mobile.rxjava2andretrofit2.base.IBaseView;
import com.mobile.rxjava2andretrofit2.first_page.bean.FirstPageResponse;

import java.util.List;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2020/3/7 15:44
 * introduce :
 */


public interface IFirstPageView extends IBaseView {

    void firstPageDataSuccess(List<FirstPageResponse.AnsListBean> success);

    void firstPageDataError(String error);
}
