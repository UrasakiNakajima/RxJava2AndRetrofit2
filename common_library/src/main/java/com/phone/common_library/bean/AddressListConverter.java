package com.phone.common_library.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

public class AddressListConverter implements PropertyConverter<List<Address>, String> {

    @Override
    public List<Address> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        // 先得获得这个，然后再typeToken.getType()，否则会异常
        TypeToken<List<Address>> typeToken = new TypeToken<List<Address>>() {
        };
        return new Gson().fromJson(databaseValue, typeToken.getType());
    }

    @Override
    public String convertToDatabaseValue(List<Address> arrays) {
        if (arrays == null || arrays.size() == 0) {
            return null;
        } else {
            String str = new Gson().toJson(arrays);
            return str;
        }
    }

}
