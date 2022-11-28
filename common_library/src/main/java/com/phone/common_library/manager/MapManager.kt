package com.phone.common_library.manager

import com.alibaba.fastjson.JSON
import com.google.gson.GsonBuilder
import com.phone.common_library.manager.LogManager.i
import org.apache.commons.lang3.StringUtils

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/15 10:19
 * introduce :
 */
object MapManager {

    private val TAG = MapManager::class.java.simpleName

    /**
     * map 转成json字符串
     *
     * @param map
     * @return
     */
    fun mapToJsonStr(map: Map<*, *>?): String? {
        val gson = GsonBuilder().enableComplexMapKeySerialization().create()
        return gson.toJson(map)
    }

    /**
     * jsonStr 转成map
     *
     * @param jsonStr
     * @return
     */
    fun jsonStrToMap(jsonStr: String?): Map<String, String> {
        val mapTypes: Map<*, *> = JSON.parseObject(jsonStr)
        val bodyParams: MutableMap<String, String> = HashMap()
        for (obj in mapTypes.keys) {
            i(TAG, "key为：" + obj + "值为：" + mapTypes[obj])
            bodyParams[obj.toString()] = mapTypes[obj].toString()
        }
        return bodyParams
    }

    /**
     * map 转成 key=value & key2=value2形式
     *
     * @param map
     * @return
     */
    fun mapToParam(map: Map<String?, Any?>?): String {
        if (map == null) {
            return ""
        }
        val stringBuilder = StringBuilder()
        for ((key, value) in map) {
            stringBuilder.append(key).append("=").append(value)
            stringBuilder.append("&")
        }
        var s = stringBuilder.toString()
        if (s.endsWith("&")) {
            s = StringUtils.substringBeforeLast(s, "&")
        }
        return s
    }

}