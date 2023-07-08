package com.phone.library_glide.okhttp3_app_glide_module

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.phone.library_network.manager.Okhttp3Manager
import java.io.InputStream

/**
 * 若要使用自定义module，外部必须使用glideapp进行调用
 */
class Okhttp3AppGlideModule : AppGlideModule() {

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

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        Okhttp3Manager.instance().mClient.let {
            registry.replace(
                GlideUrl::class.java,
                InputStream::class.java, OkHttpUrlLoader.Factory(it)
            )
        }
    }

}