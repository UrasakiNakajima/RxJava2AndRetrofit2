package com.phone.library_common.manager

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.*

/**
 * author : Urasaki
 * e-mail : 1164688204@qq.com
 * date   : 2021/4/22 9:06
 * desc   :
 * version: 1.0
 */

object UpdateFileNameManager {

    @JvmStatic
    private val TAG = UpdateFileNameManager::class.java.simpleName

    /**
     * 修改多个文件名字
     *
     * @param context
     * @param filesList 文件列表
     * @throws IOException
     */
    fun updateFileListName(context: Context, filesList: List<File>): List<File>? {
        val files: MutableList<File> = ArrayList()
        try {
            val FILEPATH = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
                .absolutePath + File.separator + "MinePictures"
            var fileInputStream: FileInputStream
            var bufferedInputStream: BufferedInputStream
            var fileOutputStream: FileOutputStream?
            var bufferedOutputStream: BufferedOutputStream
            for (i in filesList.indices) {
                var fileName = System.currentTimeMillis().toString() + ""

                //注意：无论什么时候都不要用中文命名
                //            String fileName = System.currentTimeMillis() + "这是什么啊";
                //这个才是文件
                val mediaFileType = MediaFileManager.getFileType(filesList[i].absolutePath)
                val type = mediaFileType.mimeType
                Log.i(TAG, "file name******" + filesList[i].name)
                Log.i(TAG, "type******$type")
                val typeArr = type.split("/").toTypedArray()

                //            fileName = fileName + ".png";
                fileName = fileName + "." + typeArr[1]
                val dirs = File(FILEPATH)
                if (!dirs.exists()) {
                    dirs.mkdirs()
                }
                val file = File(dirs, fileName)
                if (!file.exists()) {
                    file.createNewFile()
                }
                fileInputStream = FileInputStream(filesList[i].absolutePath) //读入原文件
                bufferedInputStream = BufferedInputStream(fileInputStream)
                fileOutputStream = FileOutputStream(file.absolutePath)
                bufferedOutputStream = BufferedOutputStream(fileOutputStream)
                val buffer = ByteArray(1024 * 2)
                var len: Int
                while (bufferedInputStream.read(buffer).also { len = it } != -1) {
                    bufferedOutputStream.write(buffer, 0, len)
                }
                bufferedOutputStream.flush()
                bufferedInputStream.close()
                bufferedOutputStream.close()
                files.add(file)

                //				//其次把文件插入到系统图库
                //				String insertImage = MediaStore.Images.Media.insertImage(context.getContentResolver(), fileUpdate.getAbsolutePath(), fileUpdate.getName(), null);
                //				File fileNew = new File(getRealPathFromURI(context, Uri.parse(insertImage)));
                //				updatePhotoMedia(context, fileNew);
            }
            for (i in files.indices) {
                Log.i(TAG, "updateFileListName***" + i + "***" + files[i].name)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return files
    }

    /**
     * 修改单个文件名字
     *
     * @param context
     * @param file    文件
     * @throws IOException
     */
    fun updateFileName(context: Context, file: File): File? {
        var fileUpdate: File? = null
        try {
            val FILEPATH = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
                .absolutePath + File.separator + "MinePictures"
            val fileInputStream: FileInputStream
            val bufferedInputStream: BufferedInputStream
            val fileOutputStream: FileOutputStream
            val bufferedOutputStream: BufferedOutputStream
            var fileName = System.currentTimeMillis().toString() + ""

            //这个才是文件
            val mediaFileType = MediaFileManager.getFileType(file.absolutePath)
            val type = mediaFileType.mimeType
            Log.i(TAG, "file name******" + file.name)
            Log.i(TAG, "type******$type")
            val typeArr = type.split("/").toTypedArray()
            //注意：无论什么时候都不要用中文命名
            //            String fileName = System.currentTimeMillis() + "这是什么啊";
            //            fileName = fileName + ".png";
            fileName = fileName + "." + typeArr[1]
            val dirs = File(FILEPATH)
            if (!dirs.exists()) {
                dirs.mkdirs()
            }
            fileUpdate = File(dirs, fileName)
            if (!fileUpdate.exists()) {
                try {
                    fileUpdate.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            fileInputStream = FileInputStream(file.absolutePath) //读入原文件
            bufferedInputStream = BufferedInputStream(fileInputStream)
            fileOutputStream = FileOutputStream(fileUpdate.absolutePath)
            bufferedOutputStream = BufferedOutputStream(fileOutputStream)
            val buffer = ByteArray(1024 * 2)
            var len: Int
            while (bufferedInputStream.read(buffer).also { len = it } != -1) {
                bufferedOutputStream.write(buffer, 0, len)
            }
            bufferedOutputStream.flush()
            bufferedInputStream.close()
            bufferedOutputStream.close()

            //				//其次把文件插入到系统图库
            //				String insertImage = MediaStore.Images.Media.insertImage(context.getContentResolver(), fileUpdate.getAbsolutePath(), fileUpdate.getName(), null);
            //				File fileNew = new File(getRealPathFromURI(context, Uri.parse(insertImage)));
            //				updatePhotoMedia(context, fileNew);
            Log.i(TAG, "updateFileListName******" + fileUpdate.name)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return fileUpdate
    }

    /**
     * 得到绝对地址
     *
     * @param context
     * @param contentUri
     * @return
     */
    private fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(contentUri, proj, null, null, null)
        val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val fileStr = cursor.getString(columnIndex)
        cursor.close()
        return fileStr
    }

    /**
     * 更新图库
     *
     * @param file
     * @param context
     */
    private fun updatePhotoMedia(context: Context, file: File) {
        val intent = Intent()
        intent.action = Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
        intent.data = Uri.fromFile(file)
        context.sendBroadcast(intent)
    }
}