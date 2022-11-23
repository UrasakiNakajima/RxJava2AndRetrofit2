package com.phone.base64_and_file.thread_pool;

import android.content.Context;
import android.graphics.Bitmap;

import com.phone.base64_and_file.manager.BitmapManager;
import com.phone.base64_and_file.bean.Base64AndFileBean;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.manager.LogManager;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompressedPictureThreadPool {

    private static final String TAG = CompressedPictureThreadPool.class.getSimpleName();
    private Context context;
    private Base64AndFileBean base64AndFileBean;
    private ExecutorService compressedPictureExcutor;

    public CompressedPictureThreadPool(Context context,
                                       Base64AndFileBean base64AndFileBean) {
        this.context = context;
        this.base64AndFileBean = base64AndFileBean;
        compressedPictureExcutor = Executors.newSingleThreadExecutor();
    }

    public void submit() {
        compressedPictureExcutor.submit(new Runnable() {
            @Override
            public void run() {
                LogManager.i(TAG, "CompressedPictureThreadPool*******" + Thread.currentThread().getName());

                //先取出资源文件保存在本地
                File file = BitmapManager.getAssetFile(context, base64AndFileBean.getDirsPath(), "picture_large.webp");
                LogManager.i(TAG, "file size*****" + BitmapManager.getDataSize(BitmapManager.getFileSize(file)));
                base64AndFileBean.setFile(file);

//            //再压缩本地图片
//            File result = BitmapManager.initCompressorIO(getApplication(), file.getAbsolutePath(), dirsPath2);
//            LogManager.i(TAG, "result size*****" + BitmapManager.getDataSize(BitmapManager.getFileSize(result)));

                //把图片转化成bitmap
                Bitmap bitmap = BitmapManager.getBitmap(file.getAbsolutePath());
                LogManager.i(TAG, "bitmap mWidth*****" + bitmap.getWidth());
                LogManager.i(TAG, "bitmap mHeight*****" + bitmap.getHeight());
                base64AndFileBean.setBitmap(bitmap);
                //再压缩bitmap
                Bitmap bitmapCompressed = BitmapManager.scaleImage(bitmap, 1280, 960);
                LogManager.i(TAG, "bitmapCompressed mWidth*****" + bitmapCompressed.getWidth());
                LogManager.i(TAG, "bitmapCompressed mHeight*****" + bitmapCompressed.getHeight());
                base64AndFileBean.setBitmapCompressed(bitmapCompressed);
                //再把压缩后的bitmap保存到本地
                File fileCompressed = BitmapManager.saveFile(bitmapCompressed, base64AndFileBean.getDirsPathCompressed(), "picture_large_compressed.png");
                LogManager.i(TAG, "CompressedPictureThreadPool fileCompressedPath*******" + fileCompressed.getAbsolutePath());
                LogManager.i(TAG, "fileCompressed size*****" + BitmapManager.getDataSize(BitmapManager.getFileSize(fileCompressed)));
                base64AndFileBean.setFileCompressed(fileCompressed);
//                if (bitmap != null) {
//                    bitmap.recycle();
//                    bitmap = null;
//                }

                onCommonSingleParamCallback.onSuccess(base64AndFileBean);
            }
        });
    }

    private OnCommonSingleParamCallback<Base64AndFileBean> onCommonSingleParamCallback;

    public void setOnCommonSingleParamCallback(OnCommonSingleParamCallback<Base64AndFileBean> onCommonSingleParamCallback) {
        this.onCommonSingleParamCallback = onCommonSingleParamCallback;
    }
}

