package com.phone.common_library.okhttp3_app_glide_module;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.phone.common_library.manager.Okhttp3Manager;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * 若要使用自定义module，外部必须使用glideapp进行调用
 */
@com.bumptech.glide.annotation.GlideModule
public class Okhttp3AppGlideModule extends AppGlideModule {

//    private static final String TAG = Okhttp3AppGlideModule.class.getSimpleName();
//    private String appRootPath = null;

//    @Override
//    public void applyOptions(Context context, GlideBuilder builder) {
//        //手机app路径--目前该路径是不用权限即可保存的路径
//        appRootPath = context.getFilesDir().getPath();
//        LogManager.i(TAG, "glide 图片缓存路径：" + appRootPath);
//        // 100 MB
//        int diskCacheSizeBytes = 1024 * 1024 * 100;
//        builder.setDiskCache(new DiskLruCacheFactory(appRootPath + "/GlideDisk", diskCacheSizeBytes));
//    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        OkHttpClient client = Okhttp3Manager.getInstance().getClient();
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
    }

}
