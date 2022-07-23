package com.phone.first_page_module;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.phone.common_library.service.FirstPageService;
import com.phone.common_library.bean.FirstPageResponse;

import java.util.List;

@Route(path = "/first_page_module/FirstPageServiceImpl")
public class FirstPageServiceImpl implements FirstPageService {

    private static final String TAG = FirstPageServiceImpl.class.getSimpleName();

    @Override
    public void setFirstPageDataList(List<FirstPageResponse.ResultData.JuheNewsBean> firstPageDataList) {
        this.firstPageDataList.clear();
        this.firstPageDataList.addAll(firstPageDataList);
    }

    @Override
    public List<FirstPageResponse.ResultData.JuheNewsBean> getFirstPageDataList() {
        return firstPageDataList;
    }

    @Override
    public void init(Context context) {

    }
}
