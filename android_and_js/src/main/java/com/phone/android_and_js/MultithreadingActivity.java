package com.phone.android_and_js;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.RequiresApi;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2021/7/9 14:26
 * introduce : 多线程Activity
 */

public class MultithreadingActivity extends BaseAppActivity {
	
	private static final String TAG = "MultithreadingActivity";
	
	@Override
	protected int initLayoutId() {
		return R.layout.activity_multithreading;
	}
	
	@Override
	protected void initData() {
		
	}
	
	@Override
	protected void initViews() {
		
	}
	
	@Override
	protected void initLoadData() {
		multithreadingSynchronization();
	}
	
	/**
	 * 多个线程之间的同步（测试）
	 */
	private void multithreadingSynchronization() {
		//		try {
		//			Thread thread1 = new Thread(new Thread1());
		//			thread1.start();
		//			thread1.join();
		//			Thread thread2 = new Thread(new Thread2());
		//			thread2.start();
		//			thread2.join();
		//			Thread thread3 = new Thread(new Thread3());
		//			thread3.start();
		//		} catch (InterruptedException e) {
		//			e.printStackTrace();
		//		}
		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(new Thread1());
		executor.submit(new Thread2());
		executor.submit(new Thread3());
		executor.shutdown();
	}
	
	public static class Thread1 implements Runnable {
		
		@Override
		public void run() {
			System.out.println("Thread1");
			LogManager.i(TAG, "Thread1");
		}
	}
	
	public static class Thread2 implements Runnable {
		
		@Override
		public void run() {
			System.out.println("Thread2");
			LogManager.i(TAG, "Thread2");
		}
	}
	
	public static class Thread3 implements Runnable {
		
		@Override
		public void run() {
			System.out.println("Thread3");
			LogManager.i(TAG, "Thread3");
		}
	}
	
	@RequiresApi(api = Build.VERSION_CODES.Q)
	public static void copyPrivateImgToCommen(Context context, String orgFilePath, String displayName) {
		ContentValues values = new ContentValues();
		values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, displayName);
		values.put(MediaStore.Files.FileColumns.TITLE, displayName);
		values.put(MediaStore.Files.FileColumns.MIME_TYPE, "image/*");
		values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "Mine");
		Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		ContentResolver resolver = context.getContentResolver();
		Uri insertUri = resolver.insert(external, values);
		InputStream is = null;
		
		BufferedInputStream bis = null;
		OutputStream os = null;
		BufferedOutputStream bos = null;
		try {
			is = new FileInputStream(new File(orgFilePath));
			bis = new BufferedInputStream(is);
			if (insertUri != null) {
				os = resolver.openOutputStream(insertUri);
				if (os != null) {
					bos = new BufferedOutputStream(os);
				}
			}
			if (os != null) {
				byte[] buffer = new byte[1024 * 2];//一次缓冲2k
				int len = 0;
				while ((len = bis.read(buffer)) != -1) {
					bos.write(buffer, 0, len);
				}
				bos.flush();
				bis.close();
				bos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}