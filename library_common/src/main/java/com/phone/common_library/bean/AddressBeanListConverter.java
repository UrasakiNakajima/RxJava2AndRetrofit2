package com.phone.common_library.bean;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.List;

public class AddressBeanListConverter implements PropertyConverter<List<AddressBean>, String> {

    @Override
    public List<AddressBean> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return new ArrayList<>();
        }

//        List<AddressBean> addressBeanList = JSONObject.parseArray(databaseValue, AddressBean.class);
//        return addressBeanList;

        // 先得获得这个，然后再typeToken.getType()，否则会异常
        TypeToken<List<AddressBean>> typeToken = new TypeToken<List<AddressBean>>() {
        };
        List<AddressBean> addressBeanList = new Gson().fromJson(databaseValue, typeToken.getType());
        return addressBeanList;
    }

    @Override
    public String convertToDatabaseValue(List<AddressBean> arrays) {
        if (arrays == null || arrays.size() == 0) {
            return "";
        } else {
//            String str = JSONObject.toJSONString(arrays);
//            return str;

            String str = new Gson().toJson(arrays);
            return str;
        }
    }

}
