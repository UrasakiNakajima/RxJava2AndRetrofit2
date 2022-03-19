package com.phone.base64_and_file.thread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.phone.base64_and_file.Base64AndFileManager;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.manager.LogManager;

import java.io.File;

public class Base64ToPictureThread extends Thread {

    private static final String TAG = Base64ToPictureThread.class.getSimpleName();
    private String filePath;
    private String base64Str;

    public Base64ToPictureThread(String filePath,
                                 String base64Str) {
        this.filePath = filePath;
        this.base64Str = base64Str;
    }

    @Override
    public void run() {
        super.run();
        LogManager.i(TAG, "Base64ToPictureThread*******" + Thread.currentThread().getName());

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

    private OnCommonSingleParamCallback<Bitmap> onCommonSingleParamCallback;

    public void setOnCommonSingleParamCallback(OnCommonSingleParamCallback<Bitmap> onCommonSingleParamCallback) {
        this.onCommonSingleParamCallback = onCommonSingleParamCallback;
    }
}
