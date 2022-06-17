package com.phone.first_page_module.manager;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.phone.common_library.BaseApplication;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.manager.ExceptionManager;
import com.phone.common_library.manager.LogManager;

public class AMAPLocationManager {

    private static final String TAG = AMAPLocationManager.class.getSimpleName();
    private static AMAPLocationManager amapLocationManager;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private AMapLocationListener mLocationListener;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;


    private AMAPLocationManager(Context context) {
        initLocation(context);
    }

    public static AMAPLocationManager getInstance(BaseApplication baseApplication) {
        if (amapLocationManager == null) {
            synchronized (AMAPLocationManager.class) {
                if (amapLocationManager == null) {
                    amapLocationManager = new AMAPLocationManager(baseApplication);
                }
            }
        }
        return amapLocationManager;
    }

    private void initLocation(Context context) {
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        LogManager.i(TAG, "address*****" + aMapLocation.getAddress());
                        //可在其中解析amapLocation获取相应内容。
                        onCommonSingleParamCallback.onSuccess(aMapLocation);
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        String errorMessage = "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo();
                        LogManager.i(TAG, "errorMessage" + errorMessage);
                        onCommonSingleParamCallback.onError(errorMessage);
                    }
                }
            }
        };

        AMapLocationClient.updatePrivacyShow(context, true, true);
        AMapLocationClient.updatePrivacyAgree(context, true);
        //初始化定位
        try {
            mLocationClient = new AMapLocationClient(context);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionManager.getInstance().throwException(context, e);
        }
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
    }

    public void startLocation() {
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
    }

    public void stopLocation() {
        if (null != mLocationClient) {
            mLocationClient.stopLocation();
        }
    }

    public void destoryLocation() {
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
        }
    }

    private OnCommonSingleParamCallback<AMapLocation> onCommonSingleParamCallback;

    public void setOnCommonSingleParamCallback(OnCommonSingleParamCallback<AMapLocation> onCommonSingleParamCallback) {
        this.onCommonSingleParamCallback = onCommonSingleParamCallback;
    }
}
