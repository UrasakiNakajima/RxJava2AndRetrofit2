package com.phone.base64_and_file.thread;

import android.content.Context;
import android.graphics.Bitmap;

import com.phone.base64_and_file.BitmapManager;
import com.phone.common_library.callback.OnCommonBothParamCallback;
import com.phone.common_library.manager.LogManager;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompressedPictureThreadPool {

    private static final String TAG = CompressedPictureThreadPool.class.getSimpleName();
    private Context context;
    private String dirsPath;
    private String dirsPath2;
    private ExecutorService compressedPictureExcutor;

    public CompressedPictureThreadPool(Context context,
                                       String dirsPath,
                                       String dirsPath2) {
        this.context = context;
        this.dirsPath = dirsPath;
        this.dirsPath2 = dirsPath2;
        compressedPictureExcutor = Executors.newSingleThreadExecutor();
    }

    public void submit() {
        compressedPictureExcutor.submit(new Runnable() {
            @Override
            public void run() {
                LogManager.i(TAG, "CompressedPictureThread*******" + Thread.currentThread().getName());

                //先取出资源文件保存在本地
                File file = BitmapManager.getAssetFile(context, "picture26.jpeg", dirsPath);
                LogManager.i(TAG, "file size*****" + BitmapManager.getDataSize(BitmapManager.getFileSize(file)));

//            //再压缩本地图片
//            File result = BitmapManager.initCompressorIO(getApplication(), file.getAbsolutePath(), dirsPath2);
//            LogManager.i(TAG, "result size*****" + BitmapManager.getDataSize(BitmapManager.getFileSize(result)));

                //然后把压缩后的图片转化成bitmap
                Bitmap bitmap = BitmapManager.getBitmap(file.getAbsolutePath());
                LogManager.i(TAG, "bitmap mWidth*****" + bitmap.getWidth());
                LogManager.i(TAG, "bitmap mHeight*****" + bitmap.getHeight());
                //再压缩bitmap
                Bitmap bitmapNew = BitmapManager.scaleImage(bitmap, 1280, 960);
                LogManager.i(TAG, "bitmapNew mWidth*****" + bitmapNew.getWidth());
                LogManager.i(TAG, "bitmapNew mHeight*****" + bitmapNew.getHeight());
                //再把压缩后的bitmap保存到本地
                File result2 = BitmapManager.saveFile(bitmapNew, dirsPath2, file.getName());
                LogManager.i(TAG, "CompressedPictureThread filePath*******" + result2.getAbsolutePath());
                LogManager.i(TAG, "result2 size*****" + BitmapManager.getDataSize(BitmapManager.getFileSize(result2)));
//                if (bitmap != null) {
//                    bitmap.recycle();
//                    bitmap = null;
//                }

                onCommonBothParamCallback.onSuccess(bitmapNew, result2.getAbsolutePath());
            }
        });
    }

    private OnCommonBothParamCallback<Bitmap> onCommonBothParamCallback;

    public void setOnCommonBothParamCallback(OnCommonBothParamCallback<Bitmap> onCommonBothParamCallback) {
        this.onCommonBothParamCallback = onCommonBothParamCallback;
    }
}

