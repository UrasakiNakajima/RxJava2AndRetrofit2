package com.phone.base64_and_file.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.phone.base64_and_file.thread.Base64ToPictureThread;
import com.phone.base64_and_file.thread.CompressedPictureThread;
import com.phone.base64_and_file.thread.PictureToBase64TaskThread;
import com.phone.common_library.callback.OnCommonBothParamCallback;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.manager.LogManager;

import java.util.List;

public class Base64AndFileModelImpl implements IBase64AndFileModel {

    private static final String TAG = Base64AndFileModelImpl.class.getSimpleName();
    private CompressedPictureThread compressedPictureThread;
    private PictureToBase64TaskThread pictureToBase64TaskThread;
    private Base64ToPictureThread base64ToPictureThread;
    private Handler handler;

    public Base64AndFileModelImpl() {
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void showCompressedPicture(Context context,
                                      String dirsPath,
                                      String dirsPath2,
                                      OnCommonBothParamCallback<Bitmap> onCommonBothParamCallback) {
        LogManager.i(TAG, "showCompressedPicture");

        compressedPictureThread = new CompressedPictureThread(
                context,
                dirsPath,
                dirsPath2);
        compressedPictureThread.setOnCommonBothParamCallback(new OnCommonBothParamCallback<Bitmap>() {
            @Override
            public void onSuccess(Bitmap success, String data) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onCommonBothParamCallback.onSuccess(success, data);
                    }
                });
            }

            @Override
            public void onError(String error) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onCommonBothParamCallback.onError(error);
                    }
                });
            }
        });
        compressedPictureThread.start();
    }

    @Override
    public void showPictureToBase64(String filePath,
                                    OnCommonBothParamCallback<List<String>> onCommonBothParamCallback) {
        LogManager.i(TAG, "showPictureToBase64");

        pictureToBase64TaskThread = new PictureToBase64TaskThread(filePath);
        pictureToBase64TaskThread.setOnCommonBothParamCallback(new OnCommonBothParamCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> success, String data) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onCommonBothParamCallback.onSuccess(success, data);
                    }
                });
            }

            @Override
            public void onError(String error) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onCommonBothParamCallback.onError(error);
                    }
                });
            }
        });
        pictureToBase64TaskThread.start();
    }

    @Override
    public void showBase64ToPicture(String filePath,
                                    String base64Str,
                                    OnCommonSingleParamCallback<Bitmap> onCommonSingleParamCallback) {
        LogManager.i(TAG, "showBase64ToPicture");

        base64ToPictureThread = new Base64ToPictureThread(filePath, base64Str);
        base64ToPictureThread.setOnCommonSingleParamCallback(new OnCommonSingleParamCallback<Bitmap>() {
            @Override
            public void onSuccess(Bitmap success) {
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
        base64ToPictureThread.start();
    }
}
