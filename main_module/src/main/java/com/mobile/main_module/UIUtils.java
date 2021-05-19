package com.mobile.main_module;

import com.blankj.utilcode.util.DeviceUtils;

import java.util.UUID;

/**
 * @创建人 Wade
 * @项目名称 Console
 * @包名 net.p5w.console.utils
 * @创建时间 2021/3/29 14:12
 * @描述 ——————————————
 * @版本 $$Rev$$
 * @更新者 $$Author$$
 * @更新时间 $$Date$$
 * @更新描述
 */
public class UIUtils {
	
	private static final String TAG = "UIUtils";
	
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
