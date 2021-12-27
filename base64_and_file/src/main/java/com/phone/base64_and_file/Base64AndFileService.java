package com.phone.base64_and_file;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

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

    private OnCommonSingleParamCallback<String> onCommonSingleParamCallback;

    public void setOnCommonSingleParamCallback(OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        this.onCommonSingleParamCallback = onCommonSingleParamCallback;
    }

    private OnCommonSingleParamCallback<Bitmap> onCommonSingleParamCallback2;

    public void setOnCommonSingleParamCallback2(OnCommonSingleParamCallback<Bitmap> onCommonSingleParamCallback2) {
        this.onCommonSingleParamCallback2 = onCommonSingleParamCallback2;
    }

    class TaskBinder extends Binder {

        /**
         * 在服务中自定义startDownload()方法，待会活动中调用此方法
         */
        public void startTask(String path, String dirsPath, String dirsPath2) {
            Log.d(TAG, "startTask executed");
            thread = new MineThread(path, dirsPath, dirsPath2);
            thread.start();
        }

        /**
         * 在服务中自定义startDownload()方法，待会活动中调用此方法
         */
        public void startTask2(String path2) {
            Log.d(TAG, "startTask2 executed");
            thread2 = new MineThread2(path2);
            thread2.start();
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
        return mBinder;
    }

    private class MineThread extends Thread {

        private String path;
        private String dirsPath;
        private String dirsPath2;

        protected MineThread(String path, String dirsPath, String dirsPath2) {
            this.path = path;
            this.dirsPath = dirsPath;
            this.dirsPath2 = dirsPath2;
        }

        @Override
        public void run() {
            super.run();
            LogManager.i(TAG, "MineThread*******" + Thread.currentThread().getName());

            File file = new File(path);
            LogManager.i(TAG, "file size*****" + BitmapManager.getDataSize(BitmapManager.getFileSize(file)));
            LogManager.i(TAG, "file size*****" + BitmapManager.getFileSize(file));
            //先压缩图片
            File result = BitmapManager.initCompressorIO(Base64AndFileService.this, file.getAbsolutePath(), dirsPath);
            LogManager.i(TAG, "result size*****" + BitmapManager.getDataSize(BitmapManager.getFileSize(result)));
            LogManager.i(TAG, "result size*****" + BitmapManager.getFileSize(result));


//            Bitmap bitmap = BitmapManager.getBitmap(result.getAbsolutePath());
//            LogManager.i(TAG, "bitmap mWidth*****" + bitmap.getWidth());
//            LogManager.i(TAG, "bitmap mHeight*****" + bitmap.getHeight());
//            //再压缩bitmap
//            Bitmap bitmapNew = BitmapManager.calculateInSampleSize(bitmap, 2016, 1512);
//            LogManager.i(TAG, "bitmapNew mWidth*****" + bitmapNew.getWidth());
//            LogManager.i(TAG, "bitmapNew mHeight*****" + bitmapNew.getHeight());
//            File result2 = BitmapManager.saveFile(bitmapNew, dirsPath2, result.getName());

            //            base64Str = Base64AndFileManager.fileToBase64(file);
            base64Str = Base64AndFileManager.fileToBase64Second(result);
//            base64Str = Base64AndFileManager.fileToBase64Second(result2);
//            base64Str = Base64AndFileManager.fileToBase64Third(file);

            onCommonSingleParamCallback.onSuccess(base64Str);
        }
    }

    private class MineThread2 extends Thread {

        private String path2;

        protected MineThread2(String path2) {
            this.path2 = path2;
        }

        @Override
        public void run() {
            super.run();
            LogManager.i(TAG, "MineThread2*******" + Thread.currentThread().getName());

            String fileName = "pictureNew5.jpeg";
//            String fileName = "pictureNew5.jpeg";
//            File fileNew = Base64AndFileManager.base64ToFile(base64Str, path, fileName);
            File fileNew = Base64AndFileManager.base64ToFileSecond(base64Str, path2, fileName);
//            File fileNew = Base64AndFileManager.base64ToFileThird(base64Str, path, fileName);

            Bitmap bitmap = BitmapFactory.decodeFile(fileNew.getAbsolutePath());
            onCommonSingleParamCallback2.onSuccess(bitmap);
        }
    }

}
