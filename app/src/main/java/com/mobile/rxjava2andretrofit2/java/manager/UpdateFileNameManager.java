package com.mobile.rxjava2andretrofit2.java.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2020/3/10 10:39
 * introduce :
 */


public class UpdateFileNameManager {

    private static final String TAG = "UpdateFileNameManager";

    /**
     * 修改多个文件名字
     *
     * @param context
     * @param filesList 文件列表
     * @throws IOException
     */
    public static List<File> updateFilesListNames(Context context, List<File> filesList) throws IOException {
        WeakReference<Context> modelContext = new WeakReference<>(context);

        List<File> files = new ArrayList<>();
        String FILEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Pictures";

        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        context = modelContext.get();
        if (context != null) {
            for (int i = 0; i < filesList.size(); i++) {
                String fileName = System.currentTimeMillis() + "";

                //注意：无论什么时候都不要用中文命名
//            String fileName = System.currentTimeMillis() + "这是什么啊";
                //这个才是文件
                MediaFileManager.MediaFileType mediaFileType = MediaFileManager.getFileType(filesList.get(i).getAbsolutePath());
                String type = mediaFileType.mimeType;

                LogManager.i(TAG, "file name******" + filesList.get(i).getName());
                LogManager.i(TAG, "type******" + type);

                String[] typeArr = type.split("/");

//            fileName = fileName + ".png";
                fileName = fileName + "." + typeArr[1];
                File dirs = new File(FILEPATH);
                if (!dirs.exists()) {
                    dirs.mkdirs();
                }
                File file = new File(dirs, fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                fileInputStream = new FileInputStream(filesList.get(i).getAbsolutePath());//读入原文件
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                fileOutputStream = new FileOutputStream(file.getAbsolutePath());
                bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                byte[] buffer = new byte[2048];
                int len;

                while ((len = bufferedInputStream.read(buffer)) != -1) {
                    bufferedOutputStream.write(buffer, 0, len);
                }

                bufferedOutputStream.flush();
                bufferedInputStream.close();
                bufferedOutputStream.close();
                files.add(file);

                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(file);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            }

            for (int i = 0; i < files.size(); i++) {
                LogManager.i(TAG, "updateFileListName***" + i + "***" + files.get(i).getName());
            }
            return files;
        } else {
            return files;
        }
    }

    /**
     * 修改单个文件名字
     *
     * @param context
     * @param file    文件
     * @throws IOException
     */
    public static File updateFileName(Context context, File file) throws IOException {
        WeakReference<Context> modelContext = new WeakReference<>(context);

        File fileUpdate = null;
        String FILEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Pictures";

        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        context = modelContext.get();
        if (context != null) {
            String fileName = System.currentTimeMillis() + "";

            //注意：无论什么时候都不要用中文命名
//            String fileName = System.currentTimeMillis() + "这是什么啊";
            //这个才是文件
            MediaFileManager.MediaFileType mediaFileType = MediaFileManager.getFileType(file.getAbsolutePath());
            String type = mediaFileType.mimeType;

            LogManager.i(TAG, "file name******" + file.getName());
            LogManager.i(TAG, "type******" + type);

            String[] typeArr = type.split("/");

//            fileName = fileName + ".png";
            fileName = fileName + "." + typeArr[1];
            File dirs = new File(FILEPATH);
            if (!dirs.exists()) {
                dirs.mkdirs();
            }
            fileUpdate = new File(dirs, fileName);
            if (!fileUpdate.exists()) {
                fileUpdate.createNewFile();

                fileInputStream = new FileInputStream(file.getAbsolutePath());//读入原文件
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                fileOutputStream = new FileOutputStream(fileUpdate.getAbsolutePath());
                bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                byte[] buffer = new byte[2048];
                int len;

                while ((len = bufferedInputStream.read(buffer)) != -1) {
                    bufferedOutputStream.write(buffer, 0, len);
                }

                bufferedOutputStream.flush();
                bufferedInputStream.close();
                bufferedOutputStream.close();

                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(fileUpdate);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            } else {
                fileInputStream = new FileInputStream(file.getAbsolutePath());//读入原文件
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                fileOutputStream = new FileOutputStream(fileUpdate.getAbsolutePath());
                bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                byte[] buffer = new byte[2048];
                int len;

                while ((len = bufferedInputStream.read(buffer)) != -1) {
                    bufferedOutputStream.write(buffer, 0, len);
                }

                bufferedOutputStream.flush();
                bufferedInputStream.close();
                bufferedOutputStream.close();

                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(fileUpdate);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            }
        }
        LogManager.i(TAG, "updateFileListName******" + fileUpdate.getName());
        return fileUpdate;
    }


}
