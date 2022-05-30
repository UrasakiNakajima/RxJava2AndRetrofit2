package com.phone.common_library.manager;


import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

//import org.apache.commons.lang3.StringUtils;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/15 10:19
 * introduce :
 */


public class MapManager {

    private static final String TAG = "MapManager";

    /**
     * map 转成json字符串
     *
     * @param map
     * @return
     */
    public static String mapToJsonStr(Map map) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        return gson.toJson(map);
    }

    /**
     * jsonStr 转成map
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, String> jsonStrToMap(String jsonStr) {
        Map mapTypes = JSON.parseObject(jsonStr);
        Map<String, String> bodyParams = new HashMap<>();
        for (Object obj : mapTypes.keySet()) {
            LogManager.i(TAG,"key为：" + obj + "值为：" + mapTypes.get(obj));
            bodyParams.put(obj.toString(), mapTypes.get(obj).toString());
        }
        return bodyParams;
    }

    /**
     * map 转成 key=value & key2=value2形式
     *
     * @param map
     * @return
     */
    public static String mapToParam(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue());
            stringBuffer.append("&");
        }
        String s = stringBuffer.toString();
        if (s.endsWith("&")) {
            s = StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }


}
