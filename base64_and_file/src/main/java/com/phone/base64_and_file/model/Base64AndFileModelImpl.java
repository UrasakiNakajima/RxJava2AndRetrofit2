package com.phone.base64_and_file.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.phone.base64_and_file.thread.Base64ToPictureThreadPool;
import com.phone.base64_and_file.thread.CompressedPictureThreadPool;
import com.phone.base64_and_file.thread.PictureToBase64TaskThreadPool;
import com.phone.common_library.callback.OnCommonBothParamCallback;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.manager.LogManager;

import java.util.List;

public class Base64AndFileModelImpl implements IBase64AndFileModel {

    private static final String TAG = Base64AndFileModelImpl.class.getSimpleName();
    private CompressedPictureThreadPool compressedPictureThreadPool;
    private PictureToBase64TaskThreadPool pictureToBase64TaskThreadPool;
    private Base64ToPictureThreadPool base64ToPictureThreadPool;

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

        compressedPictureThreadPool = new CompressedPictureThreadPool(
                context,
                dirsPath,
                dirsPath2);
        compressedPictureThreadPool.setOnCommonBothParamCallback(new OnCommonBothParamCallback<Bitmap>() {
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
        compressedPictureThreadPool.submit();
    }

    @Override
    public void showPictureToBase64(String filePath,
                                    OnCommonBothParamCallback<List<String>> onCommonBothParamCallback) {
        LogManager.i(TAG, "showPictureToBase64");

        pictureToBase64TaskThreadPool = new PictureToBase64TaskThreadPool(filePath);
        pictureToBase64TaskThreadPool.setOnCommonBothParamCallback(new OnCommonBothParamCallback<List<String>>() {
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
        pictureToBase64TaskThreadPool.submit();
    }

    @Override
    public void showBase64ToPicture(String filePath,
                                    String base64Str,
                                    OnCommonSingleParamCallback<Bitmap> onCommonSingleParamCallback) {
        LogManager.i(TAG, "showBase64ToPicture");

        base64ToPictureThreadPool = new Base64ToPictureThreadPool(filePath, base64Str);
        base64ToPictureThreadPool.setOnCommonSingleParamCallback(new OnCommonSingleParamCallback<Bitmap>() {
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
        base64ToPictureThreadPool.submit();
    }
}
