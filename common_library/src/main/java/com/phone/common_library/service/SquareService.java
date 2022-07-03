package com.phone.common_library.service;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.phone.common_library.bean.DataX;

import java.util.ArrayList;
import java.util.List;

public interface SquareService extends IProvider {

    List<DataX> squareDataList = new ArrayList<>();

    void setSquareDataList(List<DataX> squareDataList);

    List<DataX> getSquareDataList();
}
