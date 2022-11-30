package com.phone.common_library.manager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * author : Urasaki
 * e-mail : 1164688204@qq.com
 * date   : 2021/4/22 9:06
 * desc   :
 * version: 1.0
 */

public class UpdateFileNameManager {

    private static final String TAG = UpdateFileNameManager.class.getSimpleName();

    /**
     * 修改多个文件名字
     *
     * @param context
     * @param filesList 文件列表
     * @throws IOException
     */
    public static List<File> updateFileListName(Context context, List<File> filesList) {
        List<File> files = new ArrayList<>();
        try {
            String FILEPATH = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + "MinePictures";
            FileInputStream fileInputStream;
            BufferedInputStream bufferedInputStream;
            FileOutputStream fileOutputStream;
            BufferedOutputStream bufferedOutputStream;

            for (int i = 0; i < filesList.size(); i++) {
                String fileName = System.currentTimeMillis() + "";

                //注意：无论什么时候都不要用中文命名
                //            String fileName = System.currentTimeMillis() + "这是什么啊";
                //这个才是文件
                MediaFileManager.MediaFileType mediaFileType = MediaFileManager.getFileType(filesList.get(i).getAbsolutePath());
                String type = mediaFileType.mimeType;

                Log.i(TAG, "file name******" + filesList.get(i).getName());
                Log.i(TAG, "type******" + type);

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
                byte[] buffer = new byte[1024 * 2];
                int len;

                while ((len = bufferedInputStream.read(buffer)) != -1) {
                    bufferedOutputStream.write(buffer, 0, len);
                }
                bufferedOutputStream.flush();
                bufferedInputStream.close();
                bufferedOutputStream.close();
                files.add(file);

                //				//其次把文件插入到系统图库
                //				String insertImage = MediaStore.Images.Media.insertImage(context.getContentResolver(), fileUpdate.getAbsolutePath(), fileUpdate.getName(), null);
                //				File fileNew = new File(getRealPathFromURI(context, Uri.parse(insertImage)));
                //				updatePhotoMedia(context, fileNew);
            }

            for (int i = 0; i < files.size(); i++) {
                Log.i(TAG, "updateFileListName***" + i + "***" + files.get(i).getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    /**
     * 修改单个文件名字
     *
     * @param context
     * @param file    文件
     * @throws IOException
     */
    public static File updateFileName(Context context, File file) {
        File fileUpdate = null;
        try {
            String FILEPATH = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + "MinePictures";
            FileInputStream fileInputStream;
            BufferedInputStream bufferedInputStream;
            FileOutputStream fileOutputStream;
            BufferedOutputStream bufferedOutputStream;

            String fileName = System.currentTimeMillis() + "";

            //这个才是文件
            MediaFileManager.MediaFileType mediaFileType = MediaFileManager.getFileType(file.getAbsolutePath());
            String type = mediaFileType.mimeType;

            Log.i(TAG, "file name******" + file.getName());
            Log.i(TAG, "type******" + type);

            String[] typeArr = type.split("/");
            //注意：无论什么时候都不要用中文命名
            //            String fileName = System.currentTimeMillis() + "这是什么啊";
            //            fileName = fileName + ".png";
            fileName = fileName + "." + typeArr[1];

            File dirs = new File(FILEPATH);
            if (!dirs.exists()) {
                dirs.mkdirs();
            }
            fileUpdate = new File(dirs, fileName);
            if (!fileUpdate.exists()) {
                try {
                    fileUpdate.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            fileInputStream = new FileInputStream(file.getAbsolutePath());//读入原文件
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            fileOutputStream = new FileOutputStream(fileUpdate.getAbsolutePath());
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            byte[] buffer = new byte[1024 * 2];
            int len;

            while ((len = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, len);
            }
            bufferedOutputStream.flush();
            bufferedInputStream.close();
            bufferedOutputStream.close();

            //				//其次把文件插入到系统图库
            //				String insertImage = MediaStore.Images.Media.insertImage(context.getContentResolver(), fileUpdate.getAbsolutePath(), fileUpdate.getName(), null);
            //				File fileNew = new File(getRealPathFromURI(context, Uri.parse(insertImage)));
            //				updatePhotoMedia(context, fileNew);

            Log.i(TAG, "updateFileListName******" + fileUpdate.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileUpdate;
    }

    /**
     * 得到绝对地址
     *
     * @param context
     * @param contentUri
     * @return
     */
    private static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String fileStr = cursor.getString(columnIndex);
        cursor.close();
        return fileStr;
    }

    /**
     * 更新图库
     *
     * @param file
     * @param context
     */
    private static void updatePhotoMedia(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }
}
