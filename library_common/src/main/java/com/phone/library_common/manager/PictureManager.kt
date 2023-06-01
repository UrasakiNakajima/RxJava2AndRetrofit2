package com.phone.library_common.manager

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import com.phone.library_common.BaseApplication
import com.phone.library_common.R
import com.phone.library_common.callback.OnCommonSingleParamCallback
import okhttp3.ResponseBody
import java.io.*

object PictureManager {

    @JvmStatic
    private val TAG = PictureManager::class.java.simpleName

    /**
     * 保存文件到指定路径
     *
     * @param bitmap
     * @param fileName
     * @return
     */
    @JvmStatic
    fun saveImageToPath(
        bitmap: Bitmap,
        fileName: String
    ): Boolean {
        var newFileName = fileName
        val path = BaseApplication.instance().externalCacheDir
            .toString() + File.separator + "Pictures"
        val appDir = File(path)
        if (!appDir.exists()) {
            appDir.mkdirs()
        }

        //这个才是文件
        newFileName = "$newFileName.png"
        val file = File(appDir, newFileName)
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            val fos = FileOutputStream(file)
            val bos = BufferedOutputStream(fos)
            //通过io流的方式来压缩保存图片
            val isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
            bos.flush()
            bos.close()

            //保存图片后发送广播通知更新数据库
            val uri = Uri.fromFile(file)
            BaseApplication.instance()
                .sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
            return isSuccess
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

//        //一定要是外部目录
//		String path = context.getExternalCacheDir()
//						  + File.separator + "Pictures"
//        String path = File.separator + "storage"
//                + File.separator + "emulated"
//                + File.separator + "0"
//                + File.separator + "Pictures"
//        String filesDir = context.getFilesDir().getAbsolutePath()
//        //先判断是否是沙盒内部目录
//        StringBuilder stringBuilder = new StringBuilder()
//        String[] filesDirArr = filesDir.split("/")
//        for (int i = 0 i < filesDirArr.length - 1 i++) {
//            if (i == 0) {
//                stringBuilder.append(filesDirArr[i])
//            } else {
//                stringBuilder.append("/").append(filesDirArr[i])
//            }
//        }
//        String pathInternal = stringBuilder.toString()
//        boolean isPathInternal = path.contains(pathInternal)
//        if (isPathInternal) {//是沙盒内部目录
//            LogManager.i(TAG, "saveImageToPath*****isPathInternal" + isPathInternal)
//            //首先创建路径（有则不创建，没有则创建）
//            File appDir = new File(path)
//            if (!appDir.exists()) {
//                appDir.mkdirs()
//            }
//
//            //这个才是文件
//            fileName = fileName + ".png"
//            File file = new File(appDir, fileName)
//            try {
//                if (!file.exists()) {
//                    file.createNewFile()
//                }
//                FileOutputStream fos = new FileOutputStream(file)
//                BufferedOutputStream bos = new BufferedOutputStream(fos)
//                //通过io流的方式来压缩保存图片
//                boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
//                bos.flush()
//                bos.close()
//
//                //保存图片后发送广播通知更新数据库
//                Uri uri = Uri.fromFile(file)
//                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
//                return isSuccess
//            } catch (IOException e) {
//                e.printStackTrace()
//            }
//        } else {//不是沙盒内部目录
//            LogManager.i(TAG, "saveImageToPath*****isPathInternal" + isPathInternal)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                ContentValues values = new ContentValues()
//                String mimeType = "image/png"
//                values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName)
//                values.put(MediaStore.Files.FileColumns.TITLE, fileName)
//                values.put(MediaStore.Files.FileColumns.MIME_TYPE, mimeType)
//                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
//
//                ContentResolver contentResolver = context.getContentResolver()
//                Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
//                if (uri != null) {
//                    try {
//                        OutputStream os = contentResolver.openOutputStream(uri)
//                        BufferedOutputStream bos = new BufferedOutputStream(os)
//                        //通过io流的方式来压缩保存图片
//                        boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
//                        bos.flush()
//                        bos.close()
//                        return isSuccess
//                    } catch (IOException e) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//        }
//        return false
//    }

    //        //一定要是外部目录
    //		String path = context.getExternalCacheDir()
    //						  + File.separator + "Pictures"
    //        String path = File.separator + "storage"
    //                + File.separator + "emulated"
    //                + File.separator + "0"
    //                + File.separator + "Pictures"
    //        String filesDir = context.getFilesDir().getAbsolutePath()
    //        //先判断是否是沙盒内部目录
    //        StringBuilder stringBuilder = new StringBuilder()
    //        String[] filesDirArr = filesDir.split("/")
    //        for (int i = 0 i < filesDirArr.length - 1 i++) {
    //            if (i == 0) {
    //                stringBuilder.append(filesDirArr[i])
    //            } else {
    //                stringBuilder.append("/").append(filesDirArr[i])
    //            }
    //        }
    //        String pathInternal = stringBuilder.toString()
    //        boolean isPathInternal = path.contains(pathInternal)
    //        if (isPathInternal) {//是沙盒内部目录
    //            LogManager.i(TAG, "saveImageToPath*****isPathInternal" + isPathInternal)
    //            //首先创建路径（有则不创建，没有则创建）
    //            File appDir = new File(path)
    //            if (!appDir.exists()) {
    //                appDir.mkdirs()
    //            }
    //
    //            //这个才是文件
    //            fileName = fileName + ".png"
    //            File file = new File(appDir, fileName)
    //            try {
    //                if (!file.exists()) {
    //                    file.createNewFile()
    //                }
    //                FileOutputStream fos = new FileOutputStream(file)
    //                BufferedOutputStream bos = new BufferedOutputStream(fos)
    //                //通过io流的方式来压缩保存图片
    //                boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
    //                bos.flush()
    //                bos.close()
    //
    //                //保存图片后发送广播通知更新数据库
    //                Uri uri = Uri.fromFile(file)
    //                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
    //                return isSuccess
    //            } catch (IOException e) {
    //                e.printStackTrace()
    //            }
    //        } else {//不是沙盒内部目录
    //            LogManager.i(TAG, "saveImageToPath*****isPathInternal" + isPathInternal)
    //            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    //                ContentValues values = new ContentValues()
    //                String mimeType = "image/png"
    //                values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName)
    //                values.put(MediaStore.Files.FileColumns.TITLE, fileName)
    //                values.put(MediaStore.Files.FileColumns.MIME_TYPE, mimeType)
    //                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    //
    //                ContentResolver contentResolver = context.getContentResolver()
    //                Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    //                if (uri != null) {
    //                    try {
    //                        OutputStream os = contentResolver.openOutputStream(uri)
    //                        BufferedOutputStream bos = new BufferedOutputStream(os)
    //                        //通过io流的方式来压缩保存图片
    //                        boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
    //                        bos.flush()
    //                        bos.close()
    //                        return isSuccess
    //                    } catch (IOException e) {
    //                        e.printStackTrace()
    //                    }
    //                }
    //            }
    //        }
    //        return false
    //    }

    /**
     * 保存文件到指定路径
     *
     * @param responseBody
     * @param fileName
     * @param onCommonSingleParamCallback
     * @return
     */
    @JvmStatic
    fun saveImageToPath(
        responseBody: ResponseBody,
        fileName: String,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<Int?>?
    ): Boolean {
        var newFileName = fileName
        val path = BaseApplication.instance().externalCacheDir
            .toString() + File.separator + "Pictures"
        //首先创建路径（有则不创建，没有则创建）
        val appDir = File(path)
        if (!appDir.exists()) {
            appDir.mkdirs()
        }

        //这个才是文件
        newFileName = "$fileName.png"
        val file = File(appDir, newFileName)
        if (!file.exists()) {
            try {
                file.createNewFile()
                val inputStream = responseBody.byteStream()
                val bufferedInputStream = BufferedInputStream(inputStream)
                val bitmap = BitmapFactory.decodeStream(bufferedInputStream)
                val fileOutputStream = FileOutputStream(file)
                val bufferedOutputStream = BufferedOutputStream(fileOutputStream)

                //通过io流的方式来压缩保存图片
                val isSuccess =
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream)
                bufferedOutputStream.flush()
                bufferedOutputStream.close()
                //保存图片后发送广播通知更新数据库
                val uri = Uri.fromFile(file)
                BaseApplication.instance()
                    .sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
                return isSuccess
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
        }
        return false
    }


//        //一定要是外部目录
//        String path = context.getExternalCacheDir()
//                + File.separator + "Pictures"
//        String filesDir = context.getFilesDir().getAbsolutePath()
//        //先判断是否是沙盒内部目录
//        StringBuilder stringBuilder = new StringBuilder()
//        String[] filesDirArr = filesDir.split("/")
//        for (int i = 0 i < filesDirArr.length - 1 i++) {
//            if (i == 0) {
//                stringBuilder.append(filesDirArr[i])
//            } else {
//                stringBuilder.append("/").append(filesDirArr[i])
//            }
//        }
//        String pathInternal = stringBuilder.toString()
//        boolean isPathInternal = path.contains(pathInternal)
//        Bitmap bitmap = null
//        if (isPathInternal) {//是沙盒内部目录
//            try {
//                //首先创建路径（有则不创建，没有则创建）
//                File appDir = new File(path)
//                if (!appDir.exists()) {
//                    appDir.mkdirs()
//                }
//
//                //这个才是文件
//                fileName = fileName + ".png"
//                File file = new File(appDir, fileName)
//                if (!file.exists()) {
//                    file.createNewFile()
//                }
//                InputStream inputStream = responseBody.byteStream()
//                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)
//                bitmap = BitmapFactory.decodeStream(bufferedInputStream)
//                FileOutputStream fileOutputStream = new FileOutputStream(file)
//                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)
//
//                //通过io流的方式来压缩保存图片
//                boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream)
//                bufferedOutputStream.flush()
//                bufferedOutputStream.close()
//                //保存图片后发送广播通知更新数据库
//                Uri uri = Uri.fromFile(file)
//                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
//                return isSuccess
//            } catch (IOException e) {
//                e.printStackTrace()
//            }
//        } else {//不是沙盒内部目录
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                //Android 外部目录
//                String mimeType = "image/png"
//                ContentValues values = new ContentValues()
//                values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName)
//                values.put(MediaStore.Files.FileColumns.TITLE, fileName)
//                values.put(MediaStore.Files.FileColumns.MIME_TYPE, mimeType)
//                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
//
//                Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//                ContentResolver resolver = context.getContentResolver()
//                Uri insertUri = resolver.insert(external, values)
//                try {
//                    InputStream is = responseBody.byteStream()
//                    BufferedInputStream bufferedInputStream = null
//                    OutputStream outputStream = null
//                    BufferedOutputStream bufferedOutputStream = null
//                    bufferedInputStream = new BufferedInputStream(is)
//                    bitmap = BitmapFactory.decodeStream(bufferedInputStream)
//                    if (insertUri != null) {
//                        outputStream = resolver.openOutputStream(insertUri)
//                        if (outputStream != null) {
//                            bufferedOutputStream = new BufferedOutputStream(outputStream)
//                        }
//                    }
//                    if (bufferedOutputStream != null) {
//                        //通过io流的方式来压缩保存图片
//                        boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream)
//                        bufferedOutputStream.flush()
//                        bufferedInputStream.close()
//                        bufferedOutputStream.close()
//                        return isSuccess
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace()
//                }
//            }
//        }
//        return false

    //        //一定要是外部目录
    //        String path = context.getExternalCacheDir()
    //                + File.separator + "Pictures"
    //        String filesDir = context.getFilesDir().getAbsolutePath()
    //        //先判断是否是沙盒内部目录
    //        StringBuilder stringBuilder = new StringBuilder()
    //        String[] filesDirArr = filesDir.split("/")
    //        for (int i = 0 i < filesDirArr.length - 1 i++) {
    //            if (i == 0) {
    //                stringBuilder.append(filesDirArr[i])
    //            } else {
    //                stringBuilder.append("/").append(filesDirArr[i])
    //            }
    //        }
    //        String pathInternal = stringBuilder.toString()
    //        boolean isPathInternal = path.contains(pathInternal)
    //        Bitmap bitmap = null
    //        if (isPathInternal) {//是沙盒内部目录
    //            try {
    //                //首先创建路径（有则不创建，没有则创建）
    //                File appDir = new File(path)
    //                if (!appDir.exists()) {
    //                    appDir.mkdirs()
    //                }
    //
    //                //这个才是文件
    //                fileName = fileName + ".png"
    //                File file = new File(appDir, fileName)
    //                if (!file.exists()) {
    //                    file.createNewFile()
    //                }
    //                InputStream inputStream = responseBody.byteStream()
    //                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)
    //                bitmap = BitmapFactory.decodeStream(bufferedInputStream)
    //                FileOutputStream fileOutputStream = new FileOutputStream(file)
    //                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)
    //
    //                //通过io流的方式来压缩保存图片
    //                boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream)
    //                bufferedOutputStream.flush()
    //                bufferedOutputStream.close()
    //                //保存图片后发送广播通知更新数据库
    //                Uri uri = Uri.fromFile(file)
    //                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
    //                return isSuccess
    //            } catch (IOException e) {
    //                e.printStackTrace()
    //            }
    //        } else {//不是沙盒内部目录
    //            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    //                //Android 外部目录
    //                String mimeType = "image/png"
    //                ContentValues values = new ContentValues()
    //                values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName)
    //                values.put(MediaStore.Files.FileColumns.TITLE, fileName)
    //                values.put(MediaStore.Files.FileColumns.MIME_TYPE, mimeType)
    //                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    //
    //                Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    //                ContentResolver resolver = context.getContentResolver()
    //                Uri insertUri = resolver.insert(external, values)
    //                try {
    //                    InputStream is = responseBody.byteStream()
    //                    BufferedInputStream bufferedInputStream = null
    //                    OutputStream outputStream = null
    //                    BufferedOutputStream bufferedOutputStream = null
    //                    bufferedInputStream = new BufferedInputStream(is)
    //                    bitmap = BitmapFactory.decodeStream(bufferedInputStream)
    //                    if (insertUri != null) {
    //                        outputStream = resolver.openOutputStream(insertUri)
    //                        if (outputStream != null) {
    //                            bufferedOutputStream = new BufferedOutputStream(outputStream)
    //                        }
    //                    }
    //                    if (bufferedOutputStream != null) {
    //                        //通过io流的方式来压缩保存图片
    //                        boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream)
    //                        bufferedOutputStream.flush()
    //                        bufferedInputStream.close()
    //                        bufferedOutputStream.close()
    //                        return isSuccess
    //                    }
    //                } catch (IOException e) {
    //                    e.printStackTrace()
    //                }
    //            }
    //        }
    //        return false
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
        cursor?.let {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val fileStr = cursor.getString(columnIndex)
            cursor.close()
            return fileStr
        }
        return null
    }

    /**
     * 更新图库
     *
     * @param context
     * @param file
     */
    private fun updatePhotoMedia(context: Context, file: File) {
        val intent = Intent()
        intent.action = Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
        intent.data = Uri.fromFile(file)
        context.sendBroadcast(intent)
    }

    /**
     * 对文件进行写操作
     *
     * @param context
     * @param fileName
     */
    @JvmStatic
    fun copyExternalCacheDirFile(context: Context, fileName: String): Boolean {
        var newFileName = fileName
        val resourcesPath = context.externalCacheDir
            .toString() + File.separator + "Cache"
        //一定要是外部目录
        val path = context.externalCacheDir
            .toString() + File.separator + "Pictures"
        var fis: FileInputStream? = null
        var bis: BufferedInputStream? = null
        var fos: FileOutputStream? = null
        var bos: BufferedOutputStream? = null
        try {
            //首先创建路径（有则不创建，没有则创建）
            val appDir = File(path)
            if (!appDir.exists()) {
                appDir.mkdirs()
            }
            //这个才是文件
            newFileName = "$fileName.png"
            val fileUpdate = File(appDir, newFileName)
            if (!fileUpdate.exists()) {
                fileUpdate.createNewFile()
            }
            val resourcesFile = File(resourcesPath)
            fis = FileInputStream(resourcesFile)
            bis = BufferedInputStream(fis)
            val bitmap = BitmapFactory.decodeStream(bis)
            fos = FileOutputStream(fileUpdate)
            bos = BufferedOutputStream(fos)

            //通过io流的方式来压缩保存图片
            val isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
            bos.flush()
            bos.close()
            //保存图片后发送广播通知更新数据库
            updatePhotoMedia(context, fileUpdate)
            return isSuccess
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

//		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
//			String fileId = PictureManager.queryFile(context, resourcesPath)
//			if (!TextUtils.isEmpty(fileId)) {
//				Uri uri = PictureManager.getMediaFileUriFromID(fileId)
//
//				//Android 外部目录
//				String mimeType = "image/png"
//				ContentValues values = new ContentValues()
//				values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName)
//				values.put(MediaStore.Files.FileColumns.TITLE, fileName)
//				values.put(MediaStore.Files.FileColumns.MIME_TYPE, mimeType)
//				values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
//
//				Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//				//				Uri external
//				//				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//				//									external = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
//				//				} else {
//				//									external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//				//				}
//				ContentResolver resolver = context.getContentResolver()
//				Uri insertUri = resolver.insert(external, values)
//				try {
//					InputStream inputStream = null
//					BufferedInputStream bufferedInputStream = null
//					OutputStream outputStream = null
//					BufferedOutputStream bufferedOutputStream = null
//
//					inputStream = resolver.openInputStream(uri)
//					bufferedInputStream = new BufferedInputStream(inputStream)
//					bitmap = BitmapFactory.decodeStream(bufferedInputStream)
//					bufferedInputStream = new BufferedInputStream(inputStream)
//					if (insertUri != null) {
//						outputStream = resolver.openOutputStream(insertUri)
//						if (outputStream != null) {
//							bufferedOutputStream = new BufferedOutputStream(outputStream)
//						}
//					}
//					if (bufferedOutputStream != null) {
//						//通过io流的方式来压缩保存图片
//						boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream)
//						bufferedOutputStream.flush()
//						bufferedInputStream.close()
//						bufferedOutputStream.close()
//						return isSuccess
//					}
//				} catch (IOException e) {
//					e.printStackTrace()
//				}
//			}
//		} else {
//        //一定要是外部目录
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
//                + File.separator + "Pictures"
//
//        FileInputStream fis = null
//        BufferedInputStream bis = null
//        FileOutputStream fos = null
//        BufferedOutputStream bos = null
//        try {
//            //首先创建路径（有则不创建，没有则创建）
//            File appDir = new File(path)
//            if (!appDir.exists()) {
//                appDir.mkdirs()
//            }
//
//            //这个才是文件
//            fileName = fileName + ".png"
//            File fileUpdate = new File(appDir, fileName)
//
//            if (!fileUpdate.exists()) {
//                fileUpdate.createNewFile()
//            }
//
//            File resourcesFile = new File(resourcesPath)
//            fis = new FileInputStream(resourcesFile)
//            bis = new BufferedInputStream(fis)
//            bitmap = BitmapFactory.decodeStream(bis)
//            fos = new FileOutputStream(fileUpdate)
//            bos = new BufferedOutputStream(fos)
//
//            //通过io流的方式来压缩保存图片
//            boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
//            bos.flush()
//            bos.close()
//            //保存图片后发送广播通知更新数据库
//            PictureManager.updatePhotoMedia(context, fileUpdate)
//            return isSuccess
//        } catch (IOException e) {
//            e.printStackTrace()
//        }
//		}
//        return false
//    }

//	/**
//	 * 查询外部存储所有媒体文件
//	 */
//	@RequiresApi(api = Build.VERSION_CODES.Q)
//	public static String queryFile(Context context, String resourcesPath) {
//		Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), null, null, null, null)
//		String fileId = null
//		while (cursor.moveToNext()) {
//			String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID))
//			String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Downloads.DATA))
//			Log.i(TAG, "queryFile id*****" + id)
//			Log.i(TAG, "queryFile data*****" + data)
//
//			if (resourcesPath.equals(data)) {
//				fileId = id
//				break
//			}
//		}
//		cursor.close()
//		return fileId
//	}

    //		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
    //			String fileId = PictureManager.queryFile(context, resourcesPath)
    //			if (!TextUtils.isEmpty(fileId)) {
    //				Uri uri = PictureManager.getMediaFileUriFromID(fileId)
    //
    //				//Android 外部目录
    //				String mimeType = "image/png"
    //				ContentValues values = new ContentValues()
    //				values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName)
    //				values.put(MediaStore.Files.FileColumns.TITLE, fileName)
    //				values.put(MediaStore.Files.FileColumns.MIME_TYPE, mimeType)
    //				values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    //
    //				Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    //				//				Uri external
    //				//				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    //				//									external = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    //				//				} else {
    //				//									external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    //				//				}
    //				ContentResolver resolver = context.getContentResolver()
    //				Uri insertUri = resolver.insert(external, values)
    //				try {
    //					InputStream inputStream = null
    //					BufferedInputStream bufferedInputStream = null
    //					OutputStream outputStream = null
    //					BufferedOutputStream bufferedOutputStream = null
    //
    //					inputStream = resolver.openInputStream(uri)
    //					bufferedInputStream = new BufferedInputStream(inputStream)
    //					bitmap = BitmapFactory.decodeStream(bufferedInputStream)
    //					bufferedInputStream = new BufferedInputStream(inputStream)
    //					if (insertUri != null) {
    //						outputStream = resolver.openOutputStream(insertUri)
    //						if (outputStream != null) {
    //							bufferedOutputStream = new BufferedOutputStream(outputStream)
    //						}
    //					}
    //					if (bufferedOutputStream != null) {
    //						//通过io流的方式来压缩保存图片
    //						boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream)
    //						bufferedOutputStream.flush()
    //						bufferedInputStream.close()
    //						bufferedOutputStream.close()
    //						return isSuccess
    //					}
    //				} catch (IOException e) {
    //					e.printStackTrace()
    //				}
    //			}
    //		} else {
    //        //一定要是外部目录
    //        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
    //                + File.separator + "Pictures"
    //
    //        FileInputStream fis = null
    //        BufferedInputStream bis = null
    //        FileOutputStream fos = null
    //        BufferedOutputStream bos = null
    //        try {
    //            //首先创建路径（有则不创建，没有则创建）
    //            File appDir = new File(path)
    //            if (!appDir.exists()) {
    //                appDir.mkdirs()
    //            }
    //
    //            //这个才是文件
    //            fileName = fileName + ".png"
    //            File fileUpdate = new File(appDir, fileName)
    //
    //            if (!fileUpdate.exists()) {
    //                fileUpdate.createNewFile()
    //            }
    //
    //            File resourcesFile = new File(resourcesPath)
    //            fis = new FileInputStream(resourcesFile)
    //            bis = new BufferedInputStream(fis)
    //            bitmap = BitmapFactory.decodeStream(bis)
    //            fos = new FileOutputStream(fileUpdate)
    //            bos = new BufferedOutputStream(fos)
    //
    //            //通过io流的方式来压缩保存图片
    //            boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
    //            bos.flush()
    //            bos.close()
    //            //保存图片后发送广播通知更新数据库
    //            PictureManager.updatePhotoMedia(context, fileUpdate)
    //            return isSuccess
    //        } catch (IOException e) {
    //            e.printStackTrace()
    //        }
    //		}
    //        return false
    //    }
    //	/**
    //	 * 查询外部存储所有媒体文件
    //	 */
    //	@RequiresApi(api = Build.VERSION_CODES.Q)
    //	public static String queryFile(Context context, String resourcesPath) {
    //		Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), null, null, null, null)
    //		String fileId = null
    //		while (cursor.moveToNext()) {
    //			String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID))
    //			String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Downloads.DATA))
    //			Log.i(TAG, "queryFile id*****" + id)
    //			Log.i(TAG, "queryFile data*****" + data)
    //
    //			if (resourcesPath.equals(data)) {
    //				fileId = id
    //				break
    //			}
    //		}
    //		cursor.close()
    //		return fileId
    //	}

    /**
     * 根据媒体文件的ID来获取文件的Uri
     *
     * @param id
     * @return
     */
    @JvmStatic
    fun getMediaFileUriFromID(id: String): Uri? {
        return MediaStore.Files.getContentUri("external").buildUpon().appendPath(id).build()
    }

    /**
     * 设置位图的背景色
     *
     * @param bitmap 需要设置的位图
     * @param color  背景色
     */
    @JvmStatic
    fun setBitmapBGColor(bitmap: Bitmap, color: Int) {
        for (i in 0 until bitmap.width) {
            for (j in 0 until bitmap.height) {
                bitmap.setPixel(i, j, color) //将bitmap的每个像素点都设置成相应的颜色
            }
        }
    }

    /**
     * 获取一个 View 的缓存视图
     */
    @JvmStatic
    fun getCacheBitmapFromView(view: View): Bitmap? {
        //第一种方案   返回的bitmap不为空
        if (view.measuredWidth <= 0 || view.measuredHeight <= 0) {
            return null
        }
        //不能使用此方法的宽高
//        Bitmap b = Bitmap.createBitmap(view.getLayoutParams().width,
//                view.getLayoutParams().height, Bitmap.Config.ARGB_8888)
        //一定要使用这个方法的宽高
        val bitmap = Bitmap.createBitmap(
            view.measuredWidth,
            view.measuredHeight, Bitmap.Config.ARGB_8888
        )
        setBitmapBGColor(bitmap, ResourcesManager.getColor(R.color.color_FFFFFF))
        val c = Canvas(bitmap)
        //        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())
        view.draw(c)
        return bitmap
    }

}