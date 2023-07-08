package com.phone.library_network.manager

import com.google.gson.Gson
import java.lang.reflect.Type

class GsonManager {

    val TAG = GsonManager::class.java.simpleName
    private var gson: Gson

    init {
        gson = Gson()
    }

//    /**
//     * 线程安全的单例模式
//     *
//     * @return
//     */
//    companion object {
//        private var instance: GsonManager? = null
//            get() {
//                if (field == null) {
//                    field = GsonManager()
//                }
//                return field
//            }
//
//        //Synchronized添加后就是线程安全的的懒汉模式
//        @Synchronized
//        @JvmStatic
//        fun instance(): GsonManager {
//            return instance!!
//        }
//    }

    /**
     * 将json转化为对应的实体对象
     * new TypeToken<HashMap></HashMap><String></String>, Object>>(){}.getType()
     */
    fun <T> fromJson(json: String, type: Type): T {
        return gson.fromJson(json, type)
    }

    /**
     * 将json字符串转化成实体对象
     *
     * @param json json字符串
     * @param <T>  目标对象类型
     * @return 目标对象实例
    </T> */
    fun <T> convert(json: String, clz: Class<T>): T {
        return fromJson(json, clz)
    }

    /**
     * toJson
     *
     * @param src 对象
     * @return String
     */
    fun toJson(src: Any): String {
        return gson.toJson(src)
    }

}