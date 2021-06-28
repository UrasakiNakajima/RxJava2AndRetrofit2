package com.mobile.aidl_server;

import android.widget.TextView;

import com.mobile.aidl_server.bean.ConnectBean;
import com.mobile.common_library.base.BaseAppActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AidlServerActivity extends BaseAppActivity {
	
	private static final String   TAG = "AidlServerActivity";
	private              TextView tevConnectInfo;
	
	@Override
	protected int initLayoutId() {
		return R.layout.activity_aidl_server;
	}
	
	@Override
	protected void initData() {
		EventBus.getDefault().register(this);
	}
	
	@Override
	protected void initViews() {
		tevConnectInfo = (TextView) findViewById(R.id.tev_connect_info);
		
	}
	
	@Override
	protected void initLoadData() {
	
	}
	
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onReceiver(ConnectBean connectBean) {
		
		tevConnectInfo.setText(connectBean.getConnectInfo());
	}
	
	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
}