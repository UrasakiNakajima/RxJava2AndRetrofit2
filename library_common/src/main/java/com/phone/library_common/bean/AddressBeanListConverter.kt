package com.phone.library_common.bean

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.greenrobot.greendao.converter.PropertyConverter

class AddressBeanListConverter :
    PropertyConverter<MutableList<AddressBean>, String> {

    override fun convertToEntityProperty(databaseValue: String?): MutableList<AddressBean> {
        if (databaseValue == null || "".equals(databaseValue)) {
            return ArrayList()
        }

//        List<AddressBean> addressBeanList = JSONObject.parseArray(databaseValue, AddressBean.class);
//        return addressBeanList;

        // 先得获得这个，然后再typeToken.getType()，否则会异常
        val typeToken: TypeToken<List<AddressBean>> =
            object :
                TypeToken<List<AddressBean>>() {}
        return Gson().fromJson(databaseValue, typeToken.type)
    }

    override fun convertToDatabaseValue(entityProperty: MutableList<AddressBean>?): String {
        return if (entityProperty == null || entityProperty.size == 0) {
            ""
        } else {
            //            String str = JSONObject.toJSONString(arrays);
            //            return str;
            Gson().toJson(entityProperty)
        }
    }


}