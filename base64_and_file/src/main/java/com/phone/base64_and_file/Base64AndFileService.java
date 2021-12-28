package com.phone.base64_and_file;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.phone.common_library.callback.OnCommonBothParamCallback;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.manager.LogManager;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class Base64AndFileService extends Service {

    private static final String TAG = "Base64AndFileService";

    private String base64Str;
    private TaskBinder mBinder = new TaskBinder();
    private Thread thread;
    private Thread thread2;
    private Thread thread3;

    private Handler handler;


    class TaskBinder extends Binder {

        /**
         * 在服务中自定义startCompressedPictureTask()方法
         * 开启压缩图片任务
         */
        public void startCompressedPictureTask(AppCompatActivity appCompatActivity,
                                               String dirsPath,
                                               String dirsPath2) {
            Log.d(TAG, "startTask executed");
            thread = new CompressedPictureThread(
                    appCompatActivity,
                    dirsPath,
                    dirsPath2, new OnCommonBothParamCallback<Bitmap>() {
                @Override
                public void onSuccess(Bitmap success, String data) {
                    if (appCompatActivity instanceof Base64AndFileActivity) {
                        Base64AndFileActivity base64AndFileActivity = (Base64AndFileActivity) appCompatActivity;
                        base64AndFileActivity.showCompressedPictureSuccess(success, data);
                    }
                }

                @Override
                public void onError(String error) {
                    if (appCompatActivity instanceof Base64AndFileActivity) {
                        Base64AndFileActivity base64AndFileActivity = (Base64AndFileActivity) appCompatActivity;
                        base64AndFileActivity.showCompressedPictureError(error);
                    }
                }
            });
            thread.start();
        }

        /**
         * 在服务中自定义startPictureToBase64Task()方法
         * 开启图片转Base64任务
         */
        public void startPictureToBase64Task(AppCompatActivity appCompatActivity,
                                             String filePath) {
            Log.d(TAG, "startTask2 executed");
            thread2 = new PictureToBase64TaskThread(
                    filePath, new OnCommonSingleParamCallback<String>() {
                @Override
                public void onSuccess(String success) {
                    if (appCompatActivity instanceof Base64AndFileActivity) {
                        Base64AndFileActivity base64AndFileActivity = (Base64AndFileActivity) appCompatActivity;
                        base64AndFileActivity.showPictureToBase64Success(success);
                    }
                }

                @Override
                public void onError(String error) {
                    if (appCompatActivity instanceof Base64AndFileActivity) {
                        Base64AndFileActivity base64AndFileActivity = (Base64AndFileActivity) appCompatActivity;
                        base64AndFileActivity.showPictureToBase64Error(error);
                    }
                }
            });
            thread2.start();
        }

        /**
         * 在服务中自定义startBase64ToPictureTask()方法
         * 开启Base64转图片Base64任务
         */
        public void startBase64ToPictureTask(AppCompatActivity appCompatActivity,
                                             String filePath,
                                             String base64Str) {
            Log.d(TAG, "startTask2 executed");
            thread3 = new Base64ToPictureThread(
                    filePath,
                    base64Str, new OnCommonSingleParamCallback<Bitmap>() {
                @Override
                public void onSuccess(Bitmap success) {
                    if (appCompatActivity instanceof Base64AndFileActivity) {
                        Base64AndFileActivity base64AndFileActivity = (Base64AndFileActivity) appCompatActivity;
                        base64AndFileActivity.showBase64ToPictureSuccess(success);
                    }
                }

                @Override
                public void onError(String error) {
                    if (appCompatActivity instanceof Base64AndFileActivity) {
                        Base64AndFileActivity base64AndFileActivity = (Base64AndFileActivity) appCompatActivity;
                        base64AndFileActivity.showBase64ToPictureError(error);
                    }
                }
            });
            thread3.start();
        }

        /**
         * 在服务中自定义getProgress()方法，待会活动中调用此方法
         *
         * @return
         */
        public int getProgress() {
            Log.d(TAG, "getProgress executed");

            return 0;
        }

        public Base64AndFileService getService() {
            return Base64AndFileService.this;
        }
    }

    /**
     * 普通服务的不同之处，onBind()方法不在打酱油，而是会返回一个实例
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        handler = new Handler(getMainLooper());
        return mBinder;
    }

    private class CompressedPictureThread extends Thread {

        private String dirsPath;
        private String dirsPath2;
        private OnCommonBothParamCallback<Bitmap> onCommonBothParamCallback;

        protected CompressedPictureThread(AppCompatActivity appCompatActivity,
                                          String dirsPath,
                                          String dirsPath2,
                                          OnCommonBothParamCallback<Bitmap> onCommonBothParamCallback) {
            this.dirsPath = dirsPath;
            this.dirsPath2 = dirsPath2;
            this.onCommonBothParamCallback = onCommonBothParamCallback;
        }

        @Override
        public void run() {
            super.run();
            LogManager.i(TAG, "CompressedPictureThread*******" + Thread.currentThread().getName());

            //先取出资源文件保存在本地
            File file = BitmapManager.getAssetFile(getApplicationContext(), "picture8.png", dirsPath);
            LogManager.i(TAG, "file size*****" + BitmapManager.getDataSize(BitmapManager.getFileSize(file)));
//            LogManager.i(TAG, "file size*****" + BitmapManager.getFileSize(file));

            //再压缩本地图片
            File result = BitmapManager.initCompressorIO(getApplication(), file.getAbsolutePath(), dirsPath2);
            LogManager.i(TAG, "result size*****" + BitmapManager.getDataSize(BitmapManager.getFileSize(result)));
//            LogManager.i(TAG, "result size*****" + BitmapManager.getFileSize(result));

            //然后把压缩后的图片转化成bitmap
            Bitmap bitmap = BitmapManager.getBitmap(result.getAbsolutePath());
            LogManager.i(TAG, "bitmap mWidth*****" + bitmap.getWidth());
            LogManager.i(TAG, "bitmap mHeight*****" + bitmap.getHeight());
            //再压缩bitmap
            Bitmap bitmapNew = BitmapManager.scaleImage(bitmap, 1280, 960);
            LogManager.i(TAG, "bitmapNew mWidth*****" + bitmapNew.getWidth());
            LogManager.i(TAG, "bitmapNew mHeight*****" + bitmapNew.getHeight());
            //再把压缩后的bitmap保存到本地
            File result2 = BitmapManager.saveFile(bitmapNew, dirsPath2, file.getName());
            LogManager.i(TAG, "result2 size*****" + BitmapManager.getDataSize(BitmapManager.getFileSize(result2)));
//            LogManager.i(TAG, "result2 size*****" + BitmapManager.getFileSize(result2));

            //            base64Str = Base64AndFileManager.fileToBase64(file);
//            base64Str = Base64AndFileManager.fileToBase64Second(result);
//            base64Str = Base64AndFileManager.fileToBase64Second(result2);
//            base64Str = Base64AndFileManager.fileToBase64Third(file);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    onCommonBothParamCallback.onSuccess(bitmapNew, result2.getAbsolutePath());
                }
            });
        }
    }

    private class PictureToBase64TaskThread extends Thread {

        private String filePath;
        private OnCommonSingleParamCallback<String> onCommonSingleParamCallback;

        protected PictureToBase64TaskThread(String filePath,
                                            OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
            this.filePath = filePath;
            this.onCommonSingleParamCallback = onCommonSingleParamCallback;
        }

        @Override
        public void run() {
            super.run();
            LogManager.i(TAG, "MineThread2*******" + Thread.currentThread().getName());

            base64Str = null;
            File file = new File(filePath);
            if (file.exists()) {
                base64Str = Base64AndFileManager.fileToBase64Second(file);
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(base64Str)) {
                        onCommonSingleParamCallback.onSuccess(base64Str);
                    } else {
                        onCommonSingleParamCallback.onError("图片不存在");
                    }
                }
            });


//            String fileName = "pictureNew2.png";
//            String fileName = "pictureNew5.jpeg";
//            File fileNew = Base64AndFileManager.base64ToFile(base64Str, dirsPath5, fileName);
//            File fileNew = Base64AndFileManager.base64ToFileSecond(base64Str, dirsPath5, fileName);
//            File fileNew = Base64AndFileManager.base64ToFileThird(base64Str, dirsPath5, fileName);

//            Bitmap bitmap = BitmapFactory.decodeFile(fileNew.getAbsolutePath());
//            onCommonSingleParamCallback2.onSuccess(bitmap);
        }
    }

    private class Base64ToPictureThread extends Thread {

        private String filePath;
        private String base64Str;
        private OnCommonSingleParamCallback<Bitmap> onCommonSingleParamCallback;

        protected Base64ToPictureThread(String filePath,
                                        String base64Str,
                                        OnCommonSingleParamCallback<Bitmap> onCommonSingleParamCallback) {
            this.filePath = filePath;
            this.base64Str = base64Str;
            this.onCommonSingleParamCallback = onCommonSingleParamCallback;
        }

        @Override
        public void run() {
            super.run();
            LogManager.i(TAG, "MineThread2*******" + Thread.currentThread().getName());

//            File fileNew = Base64AndFileManager.base64ToFile(base64Str, dirsPath5, fileName);
            File fileNew = Base64AndFileManager.base64ToFileSecond(base64Str, filePath);
//            File fileNew = Base64AndFileManager.base64ToFileThird(base64Str, dirsPath5, fileName);

            Bitmap bitmap = BitmapFactory.decodeFile(fileNew.getAbsolutePath());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (bitmap != null) {
                        onCommonSingleParamCallback.onSuccess(bitmap);
                    } else {
                        onCommonSingleParamCallback.onError("图片不存在");
                    }
                }
            });
        }
    }

}
