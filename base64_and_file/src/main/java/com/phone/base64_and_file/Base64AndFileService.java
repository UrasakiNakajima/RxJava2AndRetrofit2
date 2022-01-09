package com.phone.base64_and_file;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.phone.common_library.callback.OnCommonBothParamCallback;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.manager.LogManager;
import com.phone.common_library.manager.ScreenManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;

public class Base64AndFileService extends Service {

    private static final String TAG = "Base64AndFileService";

    private String base64Str;
    private TaskBinder mBinder = new TaskBinder();
    private CompressedPictureThread compressedPictureThread;
    private PictureToBase64TaskThread pictureToBase64TaskThread;
    private Base64ToPictureThread base64ToPictureThread;

    private Handler handler;


    class TaskBinder extends Binder {

        /**
         * 在服务中自定义startCompressedPictureTask()方法
         * 开启压缩图片任务
         */
        public void startCompressedPictureTask(AppCompatActivity appCompatActivity,
                                               String dirsPath,
                                               String dirsPath2) {
            LogManager.i(TAG, "startTask executed");
            compressedPictureThread = new CompressedPictureThread(
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
            compressedPictureThread.start();
        }

        /**
         * 在服务中自定义startPictureToBase64Task()方法
         * 开启图片转Base64任务
         */
        public void startPictureToBase64Task(AppCompatActivity appCompatActivity,
                                             String filePath) {
            LogManager.i(TAG, "startTask2 executed");
            pictureToBase64TaskThread = new PictureToBase64TaskThread(
                    filePath,
                    new OnCommonBothParamCallback<List<String>>() {
                        @Override
                        public void onSuccess(List<String> success, String base64Str) {
                            if (appCompatActivity instanceof Base64AndFileActivity) {
                                Base64AndFileActivity base64AndFileActivity = (Base64AndFileActivity) appCompatActivity;
                                base64AndFileActivity.showPictureToBase64Success(success, base64Str);
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
            pictureToBase64TaskThread.start();
        }

        /**
         * 在服务中自定义startBase64ToPictureTask()方法
         * 开启Base64转图片Base64任务
         */
        public void startBase64ToPictureTask(AppCompatActivity appCompatActivity,
                                             String filePath,
                                             String base64Str) {
            LogManager.i(TAG, "startTask2 executed");
            base64ToPictureThread = new Base64ToPictureThread(
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
            base64ToPictureThread.start();
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

        protected CompressedPictureThread(String dirsPath,
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
        private OnCommonBothParamCallback<List<String>> onCommonBothParamCallback;
        private String txtFilePath = null;
        private List<String> base64StrList;

        protected PictureToBase64TaskThread(String filePath,
                                            OnCommonBothParamCallback<List<String>> onCommonBothParamCallback) {
            this.filePath = filePath;
            this.onCommonBothParamCallback = onCommonBothParamCallback;
        }

        @Override
        public void run() {
            super.run();
            LogManager.i(TAG, "PictureToBase64TaskThread*******" + Thread.currentThread().getName());

            base64Str = null;
            File file = new File(filePath);
            LogManager.i(TAG, "PictureToBase64TaskThread filePath*******" + filePath);
            if (file.exists()) {
//                base64Str = Base64AndFileManager.fileToBase64(file);
//                base64Str = Base64AndFileManager.fileToBase64Test(file);
                base64Str = Base64AndFileManager.fileToBase64Second(file);
            }

            if (!TextUtils.isEmpty(base64Str)) {
                String fileName = "base64Str.txt";
                txtFilePath = FileManager.writeStrToTextFile(base64Str, filePath, fileName);
                base64StrList = Base64AndFileManager.getBase64StrList(txtFilePath);
//                base64Str = "";
//                StringBuilder stringBuilder = new StringBuilder();
//                for (int i = 0; i < base64StrList.size(); i++) {
//                    stringBuilder.append(base64StrList.get(i));
//                }
//                base64Str = stringBuilder.toString();

                //把字符串的最後一個打印出來，然後看看和RecyclerView顯示的最後一個字符串是否一致
                LogManager.i(TAG, "base64StrList******" + base64StrList.get(base64StrList.size() - 1));
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (base64StrList.size() > 0) {
                        onCommonBothParamCallback.onSuccess(base64StrList, base64Str);
                    } else {
                        onCommonBothParamCallback.onError("圖片不存在");
                    }
                }
            });
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
            LogManager.i(TAG, "Base64ToPictureThread*******" + Thread.currentThread().getName());

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
                        onCommonSingleParamCallback.onError("圖片不存在");
                    }
                }
            });
        }
    }

}
