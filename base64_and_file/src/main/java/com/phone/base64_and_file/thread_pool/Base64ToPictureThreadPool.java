package com.phone.base64_and_file.thread_pool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.phone.base64_and_file.Base64AndFileManager;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.manager.LogManager;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Base64ToPictureThreadPool {

    private static final String TAG = Base64ToPictureThreadPool.class.getSimpleName();
    private String filePath;
    private String base64Str;
    private ExecutorService base64ToPictureExcutor;

    public Base64ToPictureThreadPool(String filePath,
                                     String base64Str) {
        this.filePath = filePath;
        this.base64Str = base64Str;
        base64ToPictureExcutor = Executors.newSingleThreadExecutor();
    }

    public void submit() {
        base64ToPictureExcutor.submit(new Runnable() {
            @Override
            public void run() {

                LogManager.i(TAG, "Base64ToPictureThreadPool*******" + Thread.currentThread().getName());

//            File fileNew = Base64AndFileManager.base64ToFile(base64Str, dirsPath5, fileName);
                File fileNew = Base64AndFileManager.base64ToFileSecond(base64Str, filePath);
//            File fileNew = Base64AndFileManager.base64ToFileThird(base64Str, dirsPath5, fileName);

                Bitmap bitmap = BitmapFactory.decodeFile(fileNew.getAbsolutePath());
                if (bitmap != null) {
                    onCommonSingleParamCallback.onSuccess(bitmap);
                } else {
                    onCommonSingleParamCallback.onError("圖片不存在");
                }
            }
        });
    }

    private OnCommonSingleParamCallback<Bitmap> onCommonSingleParamCallback;

    public void setOnCommonSingleParamCallback(OnCommonSingleParamCallback<Bitmap> onCommonSingleParamCallback) {
        this.onCommonSingleParamCallback = onCommonSingleParamCallback;
    }
}
