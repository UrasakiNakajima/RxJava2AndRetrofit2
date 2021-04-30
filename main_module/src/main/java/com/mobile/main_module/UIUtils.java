package com.mobile.main_module;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

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
	 * 得到上下文
	 *
	 * @return
	 */
	public static Context getContext() {
		return MineApplication.getInstance().getApplicationContext();
	}
	
	/**
	 * 得到Resource对象
	 */
	public static Resources getResources() {
		return getContext().getResources();
	}
	
	/**
	 * dp值转换成px值
	 */
	public static int dp2px(float dpValue) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
	}

	/**
	 * dp-->px
	 *
	 * @param dip
	 * @return
	 */
	public static int dp2Px(int dip) {
		//px/dp = density ①
		//px/(ppi/160) = dp ②
		
		//px和dp倍数关系
		float density = UIUtils.getResources().getDisplayMetrics().density;
		
		//ppi
		int densityDpi = UIUtils.getResources().getDisplayMetrics().densityDpi;
		
		/**
		 320x480    1  1px=1dp            160
		 480x800   1.5 1.5px = 1dp        240
		 1280x720  2    2px=1dp           320
		 */
		
		int px = (int) (dip * density + .5f);
		
		return px;
	}
	
}
