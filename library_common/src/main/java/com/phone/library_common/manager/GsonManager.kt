package com.phone.library_common.manager

import com.google.gson.Gson
import java.lang.reflect.Type

class GsonManager {

    val TAG = GsonManager::class.java.simpleName
    private var gson: Gson
//    private static Gson gson;
//    private static GsonManager gsonManager;

    //    private static Gson gson;
    //    private static GsonManager gsonManager;
    init {
        gson = Gson()
    }

//    /**
//     * 线程安全的单例模式
//     *
//     * @return
//     */
//    public static GsonManager getInstance() {
//        if (gsonManager == null) {
//            synchronized (GsonManager.class) {
//                if (gsonManager == null) {
//                    gsonManager = new GsonManager();
//                }
//            }
//        }
//        return gsonManager;
//    }

    //    /**
    //     * 线程安全的单例模式
    //     *
    //     * @return
    //     */
    //    public static GsonManager getInstance() {
    //        if (gsonManager == null) {
    //            synchronized (GsonManager.class) {
    //                if (gsonManager == null) {
    //                    gsonManager = new GsonManager();
    //                }
    //            }
    //        }
    //        return gsonManager;
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