package com.mobile.aidl_server.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.mobile.aidl_client.Book;
import com.mobile.aidl_client.BookManager;

import java.util.ArrayList;
import java.util.List;

public class AidlService extends Service {
	
	private static final String TAG = "AidlService";
	
	@Override
	public void onCreate() {
		super.onCreate();
		Book book = new Book();
		book.setName("Android AIDL 测试通过");
		book.setContent("Android AIDL内容");
		mBooks.add(book);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, String.format("on bind,intent = %s", intent.toString()));
		return mBookManager;
	}
	
	//包含Book对象的list
	private List<Book> mBooks = new ArrayList<>();
	
	//由AIDL文件生成的BookManager
	private final BookManager.Stub mBookManager = new BookManager.Stub() {
		@Override
		public List<Book> getBooks() throws RemoteException {
			synchronized (this) {
				Log.i(TAG, "invoking getBooks() method , now the list is : " + mBooks.toString());
				if (mBooks != null) {
					return mBooks;
				}
				return new ArrayList<>();
			}
		}
		
		@Override
		public void addBook(Book book) throws RemoteException {
			synchronized (this) {
				if (mBooks == null) {
					mBooks = new ArrayList<>();
				}
				if (book == null) {
					Log.i(TAG, "Book is null in In");
					book = new Book();
				}
				//尝试修改book的参数，主要是为了观察其到客户端的反馈
				book.setContent("Android AIDL内容");
				if (!mBooks.contains(book)) {
					mBooks.add(book);
				}
				//打印mBooks列表，观察客户端传过来的值
				Log.i(TAG, "invoking addBooks() method , now the list is : " + mBooks.toString());
			}
		}
	};
	
}
