package com.mobile.aidl_client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.aidl_client.adapter.BookAdapter;
import com.mobile.common_library.base.BaseAppActivity;
import com.mobile.common_library.manager.LogManager;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AidlClientActivity extends BaseAppActivity {
	
	private static final String       TAG = "AidlActivity";
	private              TextView     tevBookName;
	private              TextView     tevBookContent;
	private              RecyclerView rcvBook;
	private              TextView     tevAddBook;
	
	//由AIDL文件生成的Java类
	private BookManager mBookManager = null;
	
	//标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
	private boolean mBound = false;
	
	//包含Book对象的list
	private List<Book> mBookList = new ArrayList<>();
	private int        num;
	
	private LinearLayoutManager mLayoutManager;
	private BookAdapter         mBookAdapter;
	
	@Override
	protected int initLayoutId() {
		return R.layout.activity_aidl_client;
	}
	
	@Override
	protected void initData() {
		num = 0;
	}
	
	@Override
	protected void initViews() {
		tevBookName = (TextView) findViewById(R.id.tev_book_name);
		tevBookContent = (TextView) findViewById(R.id.tev_book_content);
		rcvBook = (RecyclerView) findViewById(R.id.rcv_book);
		tevAddBook = (TextView) findViewById(R.id.tev_add_book);
		
		tevAddBook.setOnClickListener(view -> {
			
			initAddBook();
		});
		
		initAdapter();
	}
	
	private void initAdapter() {
		mLayoutManager = new LinearLayoutManager(appCompatActivity);
		mLayoutManager.setOrientation(RecyclerView.VERTICAL);
		rcvBook.setLayoutManager(mLayoutManager);
		rcvBook.setItemAnimator(new DefaultItemAnimator());
		
		mBookAdapter = new BookAdapter(appCompatActivity);
		rcvBook.setAdapter(mBookAdapter);
	}
	
	private void initAddBook() {
		//如果与服务端的连接处于未连接状态，则尝试连接
		if (!mBound) {
			attemptToBindService();
			Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
			return;
		}
		if (mBookManager == null) {
			return;
		}
		
		num++;
		Book book = new Book();
		book.setName("新书" + num);
		book.setContent("新书内容" + num);
		try {
			mBookManager.addBook(book);
			mBookList.clear();
			mBookList.addAll(mBookManager.getBooks());
			mBookAdapter.clearData();
			mBookAdapter.addAllData(mBookList);
			LogManager.i(TAG, "initAddBook*****" + book.toString());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void initLoadData() {
	
	}
	
	/**
	 * 尝试与服务端建立连接
	 */
	private void attemptToBindService() {
		Intent intent = new Intent();
		intent.setAction("com.mobile.aidl_client.aidl");
		intent.setPackage("com.mobile.aidl_server");
		bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
	}
	
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			LogManager.i(TAG, "service connected");
			mBookManager = BookManager.Stub.asInterface(service);
			mBound = true;
			
			if (mBookManager != null) {
				if (mBookList.size() > 0) {
					LogManager.i(TAG, "onServiceConnected*****" + mBookList.toString());
				}
			}
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			LogManager.i(TAG, "service disconnected");
			mBound = false;
		}
	};
	
	@Override
	protected void onStart() {
		super.onStart();
		if (!mBound) {
			attemptToBindService();
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if (mBound) {
			unbindService(mServiceConnection);
			mBound = false;
		}
	}
	
}
