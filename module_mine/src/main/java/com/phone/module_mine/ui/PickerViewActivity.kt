package com.phone.module_mine.ui

import android.Manifest
import android.graphics.Color
import android.os.AsyncTask
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.google.gson.Gson
import com.phone.library_binding.BaseBindingRxAppActivity
import com.phone.library_base.common.ConstantData
import com.phone.library_base.manager.StatusBarManager
import com.phone.library_common.manager.GetJsonDataManager
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_base.manager.ScreenManager
import com.phone.module_mine.R
import com.phone.module_mine.bean.ProvincesBean
import com.phone.module_mine.databinding.MineActivityPickerViewBinding
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import org.json.JSONArray

@Route(path = ConstantData.Route.ROUTE_PICKER_VIEW)
class PickerViewActivity : BaseBindingRxAppActivity<MineActivityPickerViewBinding>() {

    companion object {
        @JvmStatic
        private val TAG = PickerViewActivity::class.java.simpleName
    }

    protected var bodyParams: Map<String, String>? = null
    private var pvOptions: OptionsPickerView<Any>? = null
    private var options1Items = mutableListOf<ProvincesBean>()
    private var options2Items = mutableListOf<MutableList<String>>()
    private var options3Items = mutableListOf<MutableList<MutableList<String>>>()
    var isAnalyticalDataComplete = false
    internal var analyticalDataAsyncTask: AnalyticalDataAsyncTask? = null

    // where this is an Activity or Fragment instance
    private var rxPermissions: RxPermissions? = null

    override fun initLayoutId() = R.layout.mine_activity_picker_view

    override fun initData() {
        bodyParams = HashMap()
        analyticalDataAsyncTask =
            AnalyticalDataAsyncTask()
        analyticalDataAsyncTask?.execute()
        initRxPermissions()
    }

    override fun initViews() {
        setToolbar(false, R.color.library_color_FF198CFF)
        mDatabind?.apply {
            imvBack.setColorFilter(ResourcesManager.getColor(R.color.library_white))
            layoutBack.setOnClickListener { view: View? -> finish() }
            tevShow.setOnClickListener { v: View? ->
                pvOptions?.show()
//            val eventScheduleDialogFragment = EventScheduleDialogFragment()
//            eventScheduleDialogFragment.show(supportFragmentManager, "FOF")
            }

            val statusBarHeight = StatusBarManager.getStatusBarHeight()
            LogManager.i(TAG, "statusBarHeight*****${StatusBarManager.getStatusBarHeight()}")
            viewStatusBar.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, statusBarHeight)
            LogManager.i(TAG, "statusBarHeight dp*****${ScreenManager.pxToDp(statusBarHeight.toFloat())}")
        }


    }

    override fun initLoadData() {
    }

    /**
     * 這個只是一個請求權限的框架，無論成功與失敗都不做任何處理
     */
    private fun initRxPermissions() {
        if (rxPermissions == null) {
            rxPermissions = RxPermissions(this)
        }
        rxPermissions?.let {
            it.requestEachCombined(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE //                        , Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) //解决rxjava导致的内存泄漏问题
                .compose(bindToLifecycle())
                .subscribe { permission: Permission ->  // will emit 2 Permission objects
                    if (permission.granted) {
                        // `permission.name` is granted !

                        // 所有的权限都授予
                        LogManager.i(TAG, "所有的权限都授予")
                        LogManager.i(
                            TAG,
                            "用户已经同意该权限 permission.name*****" + permission.name
                        )

//                        Intent bindIntent = new Intent(this, Base64AndFileService.class)
//                        // 绑定服务和活动，之后活动就可以去调服务的方法了
//                        bindService(bindIntent, connection, BIND_AUTO_CREATE)
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // Denied permission without ask never again

                        // 至少一个权限未授予且未勾选不再提示
                        LogManager.i(TAG, "至少一个权限未授予且未勾选不再提示")
                    } else {
                        // Denied permission with ask never again
                        // Need to go to the settings

                        // 至少一个权限未授予且勾选了不再提示
                        LogManager.i(TAG, "至少一个权限未授予且勾选了不再提示")
                    }
                }
        }
    }

    private fun initPickerView() { // 弹出选择器
        pvOptions = OptionsPickerBuilder(
            mRxAppCompatActivity
        ) { options1, options2, options3, view ->
            //返回的分别是三个级别的选中位置
            val province =
                if (options1Items.size > 0) options1Items[options1].pickerViewText else ""
            val city = if (options2Items.size > 0
                && options2Items[options1].size > 0
            ) options2Items[options1][options2] else ""
            val county =
                if (options2Items.size > 0 && options3Items[options1].size > 0 && options3Items[options1][options2].size > 0
                ) options3Items[options1][options2][options3] else ""

//                bodyParams.clear()
//                bodyParams.put("province", province)
//                bodyParams.put("city", city)
//                bodyParams.put("county", county)
//                startActivityForResultCarryParams(PickerViewActivity.class, bodyParams, 203)
        }
            .setTitleText("城市选择")
            .setDividerColor(Color.BLACK)
            .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
            .setSubmitColor(ResourcesManager.getColor(R.color.library_color_FF198CFF))
            .setCancelColor(ResourcesManager.getColor(R.color.library_colorBlack666))
            .setContentTextSize(20)
            .isDialog(true)
            .build<Any>()

        /*pvOptions.setPicker(options1Items)//一级选择器
        pvOptions.setPicker(options1Items, options2Items)//二级选择器*/
        pvOptions?.setPicker(
            options1Items as List<Any>,
            options2Items as List<MutableList<String>>,
            options3Items as List<MutableList<MutableList<String>>>
        ) //三级选择器
        val mDialog = pvOptions?.getDialog()
        if (mDialog != null) {
            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
            )
            params.leftMargin = 0
            params.rightMargin = 0
            pvOptions?.getDialogContainerLayout()?.layoutParams = params
            val dialogWindow = mDialog.window
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim) //修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM) //改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f)
                dialogWindow.decorView.setBackgroundColor(ResourcesManager.getColor(R.color.library_color_transparent))
                dialogWindow.decorView.setPadding(0, 0, 0, 0)
                dialogWindow.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }
    }

    private fun initAnalyticalData(): Boolean { //解析数据
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         */
        val JsonData = GetJsonDataManager.getJson("library_province.json") //获取assets目录下的json文件数据
        val jsonBean = parseData(JsonData) //用Gson 转成实体
        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean
        for (i in jsonBean.indices) { //遍历省份
            val cityList = mutableListOf<String>() //该省的城市列表（第二级）
            val province_AreaList = mutableListOf<MutableList<String>>() //该省的所有地区列表（第三极）
            for (c in jsonBean[i].cityList.indices) { //遍历该省份的所有城市
                val cityName = jsonBean[i].cityList[c].name
                cityList.add(cityName) //添加城市
                val city_AreaList = ArrayList<String>() //该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("")
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea())
                }*/city_AreaList.addAll(jsonBean[i].cityList[c].area)
                province_AreaList.add(city_AreaList) //添加该省所有地区数据
            }
            /**
             * 添加城市数据
             */
            options2Items.add(cityList)
            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList)
        }
        return true
    }

    fun parseData(result: String?): ArrayList<ProvincesBean> { //Gson 解析
        val detail = ArrayList<ProvincesBean>()
        try {
            val data = JSONArray(result)
            val gson = Gson()
            for (i in 0 until data.length()) {
                val entity = gson.fromJson(
                    data.optJSONObject(i).toString(),
                    ProvincesBean::class.java
                )
                detail.add(entity)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            isAnalyticalDataComplete = false
        }
        return detail
    }

    override fun onDestroy() {
        analyticalDataAsyncTask?.let {
            if (it.status == AsyncTask.Status.RUNNING) {
                it.cancel(true)
            }
        }
        super.onDestroy()
    }

    inner class AnalyticalDataAsyncTask : AsyncTask<String?, Int?, Boolean>() {

        /**
         * 这里是开始线程之前执行的,是在UI线程
         */
        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute()
            this@PickerViewActivity.isAnalyticalDataComplete = false
            //            mProgressBar.setMax(100)
        }

        /**
         * 这是在后台子线程中执行的
         *
         * @param params
         * @return
         */
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: String?): Boolean {
            if (isCancelled) {
                return false
            }
            isAnalyticalDataComplete = initAnalyticalData()
            return isAnalyticalDataComplete
        }

        /**
         * 当任务被取消时回调
         */
        @Deprecated("Deprecated in Java")
        override fun onCancelled() {
            super.onCancelled()
        }

        /**
         * 更新进度
         *
         * @param values
         */
        @Deprecated("Deprecated in Java")
        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            if (isCancelled) {
                return
            }
        }

        /**
         * 当任务执行完成是调用,在UI线程
         *
         * @param isAnalyticalDataComplete
         */
        @Deprecated("Deprecated in Java")
        override fun onPostExecute(isAnalyticalDataComplete: Boolean) {
            super.onPostExecute(isAnalyticalDataComplete)
            LogManager.i(
                TAG,
                "onPostExecute*****isAnalyticalDataComplete=$isAnalyticalDataComplete"
            )
            if (isAnalyticalDataComplete) {
                initPickerView()
                if (analyticalDataAsyncTask?.getStatus() == Status.RUNNING) {
                    analyticalDataAsyncTask?.cancel(true)
                    analyticalDataAsyncTask = null
                }
            }
        }
    }


}