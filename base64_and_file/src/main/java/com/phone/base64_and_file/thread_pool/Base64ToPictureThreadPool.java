package com.phone.base64_and_file.thread_pool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.phone.base64_and_file.manager.Base64AndFileManager;
import com.phone.base64_and_file.bean.Base64AndFileBean;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.manager.LogManager;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Base64ToPictureThreadPool {

    private static final String TAG = Base64ToPictureThreadPool.class.getSimpleName();
    private Base64AndFileBean base64AndFileBean;
    private ExecutorService base64ToPictureExcutor;

    public Base64ToPictureThreadPool(Base64AndFileBean base64AndFileBean) {
        this.base64AndFileBean = base64AndFileBean;
        base64ToPictureExcutor = Executors.newSingleThreadExecutor();
    }

    public void submit() {
        base64ToPictureExcutor.submit(new Runnable() {
            @Override
            public void run() {

                LogManager.i(TAG, "Base64ToPictureThreadPool*******" + Thread.currentThread().getName());

                File fileCompressedRecover = Base64AndFileManager.base64ToFile(base64AndFileBean.getBase64Str(),
                        base64AndFileBean.getDirsPathCompressedRecover(),
                        "picture_large_compressed_recover");
//                File fileCompressedRecover = Base64AndFileManager.base64ToFileSecond(
//                        base64AndFileBean.getBase64Str(),
//                        base64AndFileBean.getDirsPathCompressedRecover(),
//                        base64AndFileBean.getFile().getName());
//            File fileCompressedRecover = Base64AndFileManager.base64ToFileThird(base64Str, dirsPath5, fileName);

                Bitmap bitmapCompressedRecover = BitmapFactory.decodeFile(fileCompressedRecover.getAbsolutePath());
                if (bitmapCompressedRecover != null) {
                    base64AndFileBean.setBitmapCompressedRecover(bitmapCompressedRecover);
                    onCommonSingleParamCallback.onSuccess(base64AndFileBean);
                } else {
                    onCommonSingleParamCallback.onError("圖片不存在");
                }
            }
        });
    }

    private OnCommonSingleParamCallback<Base64AndFileBean> onCommonSingleParamCallback;

    public void setOnCommonSingleParamCallback(OnCommonSingleParamCallback<Base64AndFileBean> onCommonSingleParamCallback) {
        this.onCommonSingleParamCallback = onCommonSingleParamCallback;
    }
}
