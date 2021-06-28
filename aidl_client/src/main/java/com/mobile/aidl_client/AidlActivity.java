package com.mobile.aidl_client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.mobile.aidl_client.base.BaseAppActivity;

import java.util.List;

public class AidlActivity extends BaseAppActivity {
	
	private static final String      TAG          = "AidlActivity";
	//由AIDL文件生成的Java类
	private              BookManager mBookManager = null;
	
	//标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
	private boolean mBound = false;
	
	//包含Book对象的list
	private List<Book> mBooks;
	
	@Override
	protected int initLayoutId() {
		return R.layout.activity_aidl;
	}
	
	@Override
	protected void initData() {
	
	}
	
	@Override
	protected void initViews() {
	
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
			Log.i(TAG, "service connected");
			mBookManager = BookManager.Stub.asInterface(service);
			mBound = true;
			
			if (mBookManager != null) {
				try {
					mBooks = mBookManager.getBooks();
					Log.i(TAG, mBooks.toString());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i(getLocalClassName(), "service disconnected");
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
