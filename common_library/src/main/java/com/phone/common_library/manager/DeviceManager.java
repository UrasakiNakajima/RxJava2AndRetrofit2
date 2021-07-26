package com.phone.common_library.manager;

import com.blankj.utilcode.util.DeviceUtils;

import java.util.UUID;

/**
 * author : Urasaki
 * e-mail : 1164688204@qq.com
 * date   : 2021/5/2616:00
 * desc   :
 * version: 1.0
 */
public class DeviceManager {
	
	private static final String TAG = "DeviceManager";
	
	/**
	 * 获取唯一码
	 *
	 * @return
	 */
	public static String getDeviceUUid() {
		String androidId = DeviceUtils.getAndroidID();
		UUID deviceUuid = new UUID(androidId.hashCode(), ((long) androidId.hashCode() << 32));
		return deviceUuid.toString();
	}
	
}
