package com.phone.common_library.manager;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonManager {

    public static final String TAG = GsonManager.class.getSimpleName();
    private Gson gson;
//    private static Gson gson;
//    private static GsonManager gsonManager;

    public GsonManager() {
        gson = new Gson();
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

    /**
     * 将json转化为对应的实体对象
     * new TypeToken<HashMap<String, Object>>(){}.getType()
     */
    public <T> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }

    /**
     * 将json字符串转化成实体对象
     *
     * @param json json字符串
     * @param <T>  目标对象类型
     * @return 目标对象实例
     */
    public <T> T convert(String json, Class<T> clz) {
        return fromJson(json, clz);
    }

    /**
     * toJson
     *
     * @param src 对象
     * @return String
     */
    public String toJson(Object src) {
        return gson.toJson(src);
    }
}
