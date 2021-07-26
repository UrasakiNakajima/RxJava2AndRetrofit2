package com.phone.aidl_server.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.phone.aidl_client.Book;
import com.phone.aidl_client.BookManager;
import com.phone.aidl_server.bean.ConnectBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class AidlServerService extends Service {
	
	private static final String     TAG       = "AidlService";
	//包含Book对象的list
	private              List<Book> mBookList = new ArrayList<>();
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "onBind");
		EventBus.getDefault().post(new ConnectBean("已连接"));
		return mBookManager;
	}
	
	//由AIDL文件生成的BookManager
	private BookManager.Stub mBookManager = new BookManager.Stub() {
		@Override
		public List<Book> getBookList() throws RemoteException {
			synchronized (this) {
				Log.i(TAG, "getBooks*****" + mBookList.toString());
				return mBookList;
			}
		}
		
		@Override
		public void addBook(Book book) throws RemoteException {
			synchronized (this) {
				//尝试修改book的参数，主要是为了观察其到客户端的反馈
				book.setContent(book.getContent() + "$");
				mBookList.add(book);
				//打印mBooks列表，观察客户端传过来的值
				Log.i(TAG, "addBook*****" + mBookList.toString());
			}
		}
	};
	
}
