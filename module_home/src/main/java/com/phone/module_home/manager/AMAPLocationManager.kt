package com.phone.module_home.manager

import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.phone.library_base.callback.OnCommonSingleParamCallback
import com.phone.library_common.manager.ExceptionManager
import com.phone.library_base.manager.LogManager

class AMAPLocationManager {

    //声明AMapLocationClient类对象
    private var mLocationClient: AMapLocationClient? = null

    //声明AMapLocationClientOption对象
    private var mLocationOption: AMapLocationClientOption? = null

    /**
     * 保证只有一个实例
     *
     * @return
     */
    companion object {
        private val TAG: String = AMAPLocationManager::class.java.getSimpleName()

        @Volatile
        private var instance: AMAPLocationManager? = null
            get() {
                if (field == null) {
                    field = AMAPLocationManager()
                }
                return field
            }

        //       Synchronized添加后就是线程安全的的懒汉模式
        @Synchronized
        @JvmStatic
        fun instance(): AMAPLocationManager {
            return instance!!
        }
    }

    init {
        initLocation()
    }

    private fun initLocation() {
        //可在其中解析amapLocation获取相应内容。
        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
        //声明定位回调监听器
        val mLocationListener: AMapLocationListener = object : AMapLocationListener {
            override fun onLocationChanged(aMapLocation: AMapLocation) {
                if (aMapLocation.getErrorCode() == 0) {
                    LogManager.i(TAG, "address*****" + aMapLocation.getAddress())
                    //可在其中解析amapLocation获取相应内容。
                    onCommonSingleParamCallback?.onSuccess(aMapLocation)
                    stopLocation()
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    val errorMessage = ("location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo())
                    LogManager.i(TAG, "errorMessage$errorMessage")
                    onCommonSingleParamCallback?.onError(errorMessage)
                }
            }
        }
        AMapLocationClient.updatePrivacyShow(com.phone.library_base.BaseApplication.instance(), true, true)
        AMapLocationClient.updatePrivacyAgree(com.phone.library_base.BaseApplication.instance(), true)
        //初始化定位
        try {
            mLocationClient = AMapLocationClient(com.phone.library_base.BaseApplication.instance())
        } catch (e: Exception) {
            e.printStackTrace()
            ExceptionManager.instance().throwException(e)
        }
        //设置定位回调监听
        mLocationClient?.setLocationListener(mLocationListener)

        //初始化AMapLocationClientOption对象
        mLocationOption = AMapLocationClientOption()
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        mLocationOption?.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn)
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption?.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
        //        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
//        mLocationOption?.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving)
//        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
//        mLocationOption?.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors)
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption?.setInterval(1000)
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption?.setNeedAddress(true)
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption?.setHttpTimeOut(20000)
    }

    fun startLocation() {
        mLocationClient?.setLocationOption(mLocationOption)
        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
        mLocationClient?.stopLocation()
        mLocationClient?.startLocation()
    }

    fun stopLocation() {
        mLocationClient?.stopLocation()
    }

    fun destoryLocation() {
        stopLocation()
        mLocationClient?.onDestroy()
    }

    private var onCommonSingleParamCallback: OnCommonSingleParamCallback<AMapLocation>? = null

    fun setOnCommonSingleParamCallback(onCommonSingleParamCallback: OnCommonSingleParamCallback<AMapLocation>) {
        this.onCommonSingleParamCallback = onCommonSingleParamCallback
    }
}