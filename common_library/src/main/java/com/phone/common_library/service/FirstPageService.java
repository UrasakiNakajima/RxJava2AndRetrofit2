package com.phone.common_library.service;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.phone.common_library.bean.FirstPageResponse;

import java.util.ArrayList;
import java.util.List;

public interface FirstPageService extends IProvider {

    List<FirstPageResponse.ResultData.JuheNewsBean> firstPageDataList = new ArrayList<>();

    void setFirstPageDataList(List<FirstPageResponse.ResultData.JuheNewsBean> firstPageDataList);

    List<FirstPageResponse.ResultData.JuheNewsBean> getFirstPageDataList();
}
