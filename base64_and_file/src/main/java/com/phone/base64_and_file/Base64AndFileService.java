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
            //压缩图片
            File result = initCompressorIO(file.getAbsolutePath(), dirsPath);

            byte[] bytes = BitmapManager.readStream(result.getAbsolutePath());
            int orientation = BitmapManager.decodeImageDegree(result.getAbsolutePath());
            Bitmap bitmap = BitmapManager.createBitmap(bytes, orientation);
            //压缩bitmap
            Bitmap bitmapNew = BitmapManager.calculateInSampleSize(bitmap, 1280, 960);
            File result2 = BitmapManager.saveFile(bitmapNew, dirsPath2, result.getName());

            //            base64Str = Base64AndFileManager.fileToBase64(file);
//            base64Str = Base64AndFileManager.fileToBase64Second(result);
            base64Str = Base64AndFileManager.fileToBase64Second(result2);
//            base64Str = Base64AndFileManager.fileToBase64Third(file);

            onCommonSingleParamCallback.onSuccess(base64Str);
        }
    }

    /**
     * 使用Compressor IO模式自定义压缩
     *
     * @param path .setMaxWidth(640).setMaxHeight(480)这两个数值越高，压缩力度越小，图片也不清晰
     *             .setQuality(75)这个方法只是设置图片质量，并不影响压缩图片的大小KB
     *             .setCompressFormat(Bitmap.CompressFormat.WEBP) WEBP图片格式是Google推出的 压缩强，质量 高，但是IOS不识别，需要把图片转为字节流然后转PNG格式
     *             .setCompressFormat(Bitmap.CompressFormat.PNG)PNG格式的压缩，会导致图片变大，并耗过大的内 存，手机反应缓慢
     *             .setCompressFormat(Bitmap.CompressFormat.JPEG)JPEG压缩；压缩速度比PNG快，质量一般，基本上属于1/10的压缩比例
     */
    private File initCompressorIO(String path, String dirsPath) {
        try {
            File file = new Compressor(this)
                    .setMaxWidth(1280)
                    .setMaxHeight(960)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(dirsPath)
                    .compressToFile(new File(path));

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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
