package com.phone.base64_and_file.model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.phone.base64_and_file.bean.Base64AndFileBean;
import com.phone.base64_and_file.thread_pool.Base64ToPictureThreadPool;
import com.phone.base64_and_file.thread_pool.CompressedPictureThreadPool;
import com.phone.base64_and_file.thread_pool.PictureToBase64ThreadPool;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.manager.LogManager;

public class Base64AndFileModelImpl implements IBase64AndFileModel {

    private static final String TAG = Base64AndFileModelImpl.class.getSimpleName();
    private CompressedPictureThreadPool compressedPictureThreadPool;
    private PictureToBase64ThreadPool pictureToBase64ThreadPool;
    private Base64ToPictureThreadPool base64ToPictureThreadPool;

    private Handler handler;

    public Base64AndFileModelImpl() {
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void showCompressedPicture(Context context,
                                      Base64AndFileBean base64AndFileBean,
                                      OnCommonSingleParamCallback<Base64AndFileBean> onCommonSingleParamCallback) {
        LogManager.i(TAG, "showCompressedPicture");

        compressedPictureThreadPool = new CompressedPictureThreadPool(
                context,
                base64AndFileBean);
        compressedPictureThreadPool.setOnCommonSingleParamCallback(new OnCommonSingleParamCallback<Base64AndFileBean>() {
            @Override
            public void onSuccess(Base64AndFileBean success) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onCommonSingleParamCallback.onSuccess(success);
                    }
                });
            }

            @Override
            public void onError(String error) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onCommonSingleParamCallback.onError(error);
                    }
                });
            }
        });
        compressedPictureThreadPool.submit();
    }

    @Override
    public void showPictureToBase64(Base64AndFileBean base64AndFileBean,
                                    OnCommonSingleParamCallback<Base64AndFileBean> onCommonSingleParamCallback) {
        LogManager.i(TAG, "showPictureToBase64");

        pictureToBase64ThreadPool = new PictureToBase64ThreadPool(base64AndFileBean);
        pictureToBase64ThreadPool.setOnCommonSingleParamCallback(new OnCommonSingleParamCallback<Base64AndFileBean>() {
            @Override
            public void onSuccess(Base64AndFileBean success) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onCommonSingleParamCallback.onSuccess(success);
                    }
                });
            }

            @Override
            public void onError(String error) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onCommonSingleParamCallback.onError(error);
                    }
                });
            }
        });
        pictureToBase64ThreadPool.submit();
    }

    @Override
    public void showBase64ToPicture(Base64AndFileBean base64AndFileBean,
                                    OnCommonSingleParamCallback<Base64AndFileBean> onCommonSingleParamCallback) {
        LogManager.i(TAG, "showBase64ToPicture");

        base64ToPictureThreadPool = new Base64ToPictureThreadPool(base64AndFileBean);
        base64ToPictureThreadPool.setOnCommonSingleParamCallback(new OnCommonSingleParamCallback<Base64AndFileBean>() {
            @Override
            public void onSuccess(Base64AndFileBean success) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onCommonSingleParamCallback.onSuccess(success);
                    }
                });
            }

            @Override
            public void onError(String error) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onCommonSingleParamCallback.onError(error);
                    }
                });
            }
        });
        base64ToPictureThreadPool.submit();
    }
}
