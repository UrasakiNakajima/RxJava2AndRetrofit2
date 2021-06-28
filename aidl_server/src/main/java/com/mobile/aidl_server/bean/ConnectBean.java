package com.mobile.aidl_server.bean;

/**
 * author : Urasaki
 * e-mail : 1164688204@qq.com
 * date   : 2021/6/2814:54
 * desc   :
 * version: 1.0
 */
public class ConnectBean {
	
	private String connectInfo;
	
	public ConnectBean(String connectInfo) {
		this.connectInfo = connectInfo;
	}
	
	public String getConnectInfo() {
		return connectInfo;
	}
	
	public void setConnectInfo(String connectInfo) {
		this.connectInfo = connectInfo;
	}
	
	@Override
	public String toString() {
		return "ConnectBean{" +
				   "connectInfo='" + connectInfo + '\'' +
				   '}';
	}
}
