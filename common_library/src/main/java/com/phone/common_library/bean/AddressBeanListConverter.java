package com.phone.common_library.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

public class AddressBeanListConverter implements PropertyConverter<List<AddressBean>, String> {

    @Override
    public List<AddressBean> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        // 先得获得这个，然后再typeToken.getType()，否则会异常
        TypeToken<List<AddressBean>> typeToken = new TypeToken<List<AddressBean>>() {
        };
        return new Gson().fromJson(databaseValue, typeToken.getType());
    }

    @Override
    public String convertToDatabaseValue(List<AddressBean> arrays) {
        if (arrays == null || arrays.size() == 0) {
            return null;
        } else {
            String str = new Gson().toJson(arrays);
            return str;
        }
    }

}
