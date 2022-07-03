package com.phone.square_module;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.phone.common_library.bean.DataX;
import com.phone.common_library.service.SquareService;

import java.util.List;

@Route(path = "/square_module/SquareServiceImpl")
public class SquareServiceImpl implements SquareService {

    private static final String TAG = "SquareServiceImpl";

    @Override
    public void setSquareDataList(List<DataX> squareDataList) {
        this.squareDataList.clear();
        this.squareDataList.addAll(squareDataList);
    }

    @Override
    public List<DataX> getSquareDataList() {
        return squareDataList;
    }

    @Override
    public void init(Context context) {

    }
}
