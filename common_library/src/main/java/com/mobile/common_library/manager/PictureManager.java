package com.mobile.common_library.manager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.mobile.common_library.callback.OnCommonSingleParamCallback;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.annotation.RequiresApi;
import okhttp3.ResponseBody;

/**
 * author : Urasaki
 * e-mail : 1164688204@qq.com
 * date   : 2021/5/11 10:31
 * desc   :
 * version: 1.0
 */
public class PictureManager {
	
	private static final String TAG = "PictureManager";
	
	/**
	 * 保存文件到指定路径
	 *
	 * @param context
	 * @param bitmap
	 * @param path
	 * @return
	 */
	public static boolean saveImageToPath(Context context, Bitmap bitmap,
		String path, String fileName) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			ContentValues values = new ContentValues();
			String mimeType = "image/png";
			values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
			values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
			values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);
			
			ContentResolver contentResolver = context.getContentResolver();
			Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			if (uri != null) {
				try {
					OutputStream os = contentResolver.openOutputStream(uri);
					BufferedOutputStream bos = new BufferedOutputStream(os);
					//通过io流的方式来压缩保存图片
					boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
					bos.flush();
					bos.close();
					return isSuccess;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			//首先创建路径（有则不创建，没有则创建）
			File appDir = new File(path);
			if (!appDir.exists()) {
				appDir.mkdirs();
			}
			
			//这个才是文件
			fileName = fileName + ".png";
			File file = new File(appDir, fileName);
			try {
				if (!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				//通过io流的方式来压缩保存图片
				boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
				bos.flush();
				bos.close();
				
				//保存图片后发送广播通知更新数据库
				Uri uri = Uri.fromFile(file);
				context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
				return isSuccess;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 保存文件到指定路径
	 *
	 * @param context
	 * @param responseBody
	 * @param onCommonSingleParamCallback
	 * @return
	 */
	public static boolean saveImageToPath(Context context,
		ResponseBody responseBody, String fileName, OnCommonSingleParamCallback<Integer> onCommonSingleParamCallback) {
		
		Bitmap bitmap = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			//Android 外部目录
			String mimeType = "image/png";
			ContentValues values = new ContentValues();
			values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName);
			values.put(MediaStore.Files.FileColumns.TITLE, fileName);
			values.put(MediaStore.Files.FileColumns.MIME_TYPE, mimeType);
			values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
			
			Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
			ContentResolver resolver = context.getContentResolver();
			Uri insertUri = resolver.insert(external, values);
			
			//			long total = responseBody.contentLength();
			try {
				InputStream is = responseBody.byteStream();
				BufferedInputStream bufferedInputStream = null;
				OutputStream outputStream = null;
				BufferedOutputStream bufferedOutputStream = null;
				bufferedInputStream = new BufferedInputStream(is);
				bitmap = BitmapFactory.decodeStream(bufferedInputStream);
				if (insertUri != null) {
					outputStream = resolver.openOutputStream(insertUri);
					if (outputStream != null) {
						bufferedOutputStream = new BufferedOutputStream(outputStream);
					}
				}
				if (bufferedOutputStream != null) {
					//					byte[] buffer = new byte[1024 * 2];
					//					int len = 0;
					//					int sum = 0;
					//					while ((len = bis.read(buffer)) != -1) {
					//						bos.write(buffer, 0, len);
					//						sum += len;
					//						MainThreadManager mainThreadManager = new MainThreadManager();
					//						int finalSum = sum;
					//						mainThreadManager.setOnSubThreadToMainThreadCallback(new OnSubThreadToMainThreadCallback() {
					//							@Override
					//							public void onSuccess() {
					//								int progress = (int) (finalSum * 1.0f / total * 100);
					//								onCommonSingleParamCallback.onSuccess(progress);
					//							}
					//						});
					//						mainThreadManager.subThreadToUIThread();
					//					}
					
					//通过io流的方式来压缩保存图片
					boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream);
					bufferedOutputStream.flush();
					bufferedInputStream.close();
					bufferedOutputStream.close();
					return isSuccess;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			//一定要是外部目录
			String path = Environment.getExternalStorageDirectory().getAbsolutePath()
							  + File.separator + "Pictures";
			
			try {
				//首先创建路径（有则不创建，没有则创建）
				File appDir = new File(path);
				if (!appDir.exists()) {
					appDir.mkdirs();
				}
				
				//这个才是文件
				fileName = fileName + ".png";
				File file = new File(appDir, fileName);
				if (!file.exists()) {
					file.createNewFile();
				}
				InputStream inputStream = responseBody.byteStream();
				BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
				bitmap = BitmapFactory.decodeStream(bufferedInputStream);
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
				
				//通过io流的方式来压缩保存图片
				boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream);
				bufferedOutputStream.flush();
				bufferedOutputStream.close();
				//保存图片后发送广播通知更新数据库
				Uri uri = Uri.fromFile(file);
				context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
				return isSuccess;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return false;
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
	 * @param context
	 * @param file
	 */
	private static void updatePhotoMedia(Context context, File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		intent.setData(Uri.fromFile(file));
		context.sendBroadcast(intent);
	}
	
	/**
	 * 对文件进行写操作
	 *
	 * @param context
	 * @param fileName
	 */
	public static boolean copyPublicDirectoryFile(Context context, String resourcesPath, String fileName) {
		Bitmap bitmap = null;
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
			String fileId = PictureManager.queryFile(context, resourcesPath);
			if (!TextUtils.isEmpty(fileId)) {
				Uri uri = PictureManager.getMediaFileUriFromID(fileId);
				
				//Android 外部目录
				String mimeType = "image/png";
				ContentValues values = new ContentValues();
				values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName);
				values.put(MediaStore.Files.FileColumns.TITLE, fileName);
				values.put(MediaStore.Files.FileColumns.MIME_TYPE, mimeType);
				values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
				
				Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				//				Uri external;
				//				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
				//									external = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
				//				} else {
				//									external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				//				}
				ContentResolver resolver = context.getContentResolver();
				Uri insertUri = resolver.insert(external, values);
				try {
					InputStream inputStream = null;
					BufferedInputStream bufferedInputStream = null;
					OutputStream outputStream = null;
					BufferedOutputStream bufferedOutputStream = null;
					
					inputStream = resolver.openInputStream(uri);
					bufferedInputStream = new BufferedInputStream(inputStream);
					bitmap = BitmapFactory.decodeStream(bufferedInputStream);
					bufferedInputStream = new BufferedInputStream(inputStream);
					if (insertUri != null) {
						outputStream = resolver.openOutputStream(insertUri);
						if (outputStream != null) {
							bufferedOutputStream = new BufferedOutputStream(outputStream);
						}
					}
					if (bufferedOutputStream != null) {
						//通过io流的方式来压缩保存图片
						boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream);
						bufferedOutputStream.flush();
						bufferedInputStream.close();
						bufferedOutputStream.close();
						return isSuccess;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			//一定要是外部目录
			String path = Environment.getExternalStorageDirectory().getAbsolutePath()
							  + File.separator + "Pictures";
			
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			try {
				//首先创建路径（有则不创建，没有则创建）
				File appDir = new File(path);
				if (!appDir.exists()) {
					appDir.mkdirs();
				}
				
				//这个才是文件
				fileName = fileName + ".png";
				File fileUpdate = new File(appDir, fileName);
				
				if (!fileUpdate.exists()) {
					fileUpdate.createNewFile();
				}
				
				File resourcesFile = new File(resourcesPath);
				fis = new FileInputStream(resourcesFile);
				bis = new BufferedInputStream(fis);
				bitmap = BitmapFactory.decodeStream(bis);
				fos = new FileOutputStream(fileUpdate);
				bos = new BufferedOutputStream(fos);
				
				//通过io流的方式来压缩保存图片
				boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
				bos.flush();
				bos.close();
				//保存图片后发送广播通知更新数据库
				PictureManager.updatePhotoMedia(context, fileUpdate);
				return isSuccess;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 查询外部存储所有媒体文件
	 */
	@RequiresApi(api = Build.VERSION_CODES.Q)
	public static String queryFile(Context context, String resourcesPath) {
		Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), null, null, null, null);
		String fileId = null;
		while (cursor.moveToNext()) {
			String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID));
			String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Downloads.DATA));
			Log.i(TAG, "queryFile id*****" + id);
			Log.i(TAG, "queryFile data*****" + data);
			
			if (resourcesPath.equals(data)) {
				fileId = id;
				break;
			}
		}
		cursor.close();
		return fileId;
	}
	
	/**
	 * 根据媒体文件的ID来获取文件的Uri
	 *
	 * @param id
	 * @return
	 */
	public static Uri getMediaFileUriFromID(String id) {
		return MediaStore.Files.getContentUri("external").buildUpon().appendPath(String.valueOf(id)).build();
	}
	
}
