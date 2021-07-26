package com.phone.first_page_module.view;

import com.phone.common_library.base.IBaseView;
import com.phone.first_page_module.bean.FirstPageResponse;

import java.util.List;

/**
 * author    : Urasaki
 * e-mail    : Urasaki@qq.com
 * date      : 2020/3/7 15:44
 * introduce :
 */


public interface IFirstPageView extends IBaseView {

    void firstPageDataSuccess(List<FirstPageResponse.ResultData.JuheNewsBean> success);

    void firstPageDataError(String error);
}
