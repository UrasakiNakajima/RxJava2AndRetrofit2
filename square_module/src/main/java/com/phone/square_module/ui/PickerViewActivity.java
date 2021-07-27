package com.phone.square_module.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.phone.common_library.base.BaseAppActivity;
import com.phone.common_library.manager.GetJsonDataManager;
import com.phone.common_library.manager.LogManager;
import com.phone.square_module.R;
import com.phone.square_module.bean.ProvincesBean;

import org.json.JSONArray;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PickerViewActivity extends BaseAppActivity {

    private static final String TAG = "PickerViewActivity";
    private TextView tevShow;

    protected Map<String, String> bodyParams;

    private OptionsPickerView pvOptions;
    private List<ProvincesBean> options1Items;
    private ArrayList<ArrayList<String>> options2Items;
    private ArrayList<ArrayList<ArrayList<String>>> options3Items;
    private boolean isAnalyticalDataComplete;
    private AnalyticalDataAsyncTask analyticalDataAsyncTask;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_picker_view;
    }

    @Override
    protected void initData() {
        bodyParams = new HashMap<>();

        options1Items = new ArrayList<>();
        options2Items = new ArrayList<>();
        options3Items = new ArrayList<>();

        analyticalDataAsyncTask = new AnalyticalDataAsyncTask((PickerViewActivity) appCompatActivity);
        analyticalDataAsyncTask.execute();
    }

    @Override
    protected void initViews() {
        tevShow = findViewById(R.id.tev_show);
        tevShow.setOnClickListener(v -> {

            pvOptions.show();
        });
    }

    @Override
    protected void initLoadData() {

    }


    private void initPickerView() {// 弹出选择器
        pvOptions = new OptionsPickerBuilder(appCompatActivity, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View view) {
                //返回的分别是三个级别的选中位置
                String province = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                String city = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";

                String county = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";

//                bodyParams.clear();
//                bodyParams.put("province", province);
//                bodyParams.put("city", city);
//                bodyParams.put("county", county);
//                startActivityForResultCarryParams(PickerViewActivity.class, bodyParams, 203);
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setSubmitColor(getResources().getColor(R.color.color_FF198CFF))
                .setCancelColor(getResources().getColor(R.color.colorBlack666))
                .setContentTextSize(20)
                .isDialog(true)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器

        Dialog mDialog = pvOptions.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvOptions.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
                dialogWindow.getDecorView().setBackgroundColor(getResources().getColor(R.color.colorTransparentA));
                dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
                dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
    }

    private boolean initAnalyticalData(PickerViewActivity pickerViewActivity) {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataManager().getJson(appCompatActivity, "province.json");//获取assets目录下的json文件数据

        ArrayList<ProvincesBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }
        return true;
    }

    public ArrayList<ProvincesBean> parseData(String result) {//Gson 解析
        ArrayList<ProvincesBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                ProvincesBean entity = gson.fromJson(data.optJSONObject(i).toString(), ProvincesBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            isAnalyticalDataComplete = false;
        }
        return detail;
    }


    protected class AnalyticalDataAsyncTask extends AsyncTask<String, Integer, Boolean> {

        private WeakReference<PickerViewActivity> activityWeakReference;

        private AnalyticalDataAsyncTask(PickerViewActivity pickerViewActivity) {
            activityWeakReference = new WeakReference<PickerViewActivity>(pickerViewActivity);
        }

        /**
         * 这里是开始线程之前执行的,是在UI线程
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isAnalyticalDataComplete = false;
//            mProgressBar.setMax(100);
        }

        /**
         * 这是在后台子线程中执行的
         *
         * @param params
         * @return
         */
        @Override
        protected Boolean doInBackground(String... params) {
            if (isCancelled()) {
                return false;
            }

            PickerViewActivity pickerViewActivity = activityWeakReference.get();
            if (pickerViewActivity != null) {
                isAnalyticalDataComplete = initAnalyticalData(pickerViewActivity);
            }
            return isAnalyticalDataComplete;
        }

        /**
         * 当任务被取消时回调
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();

        }

        /**
         * 更新进度
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            if (isCancelled()) {
                return;
            }
        }

        /**
         * 当任务执行完成是调用,在UI线程
         *
         * @param isAnalyticalDataComplete
         */
        @Override
        protected void onPostExecute(Boolean isAnalyticalDataComplete) {
            super.onPostExecute(isAnalyticalDataComplete);

            LogManager.i(TAG, "onPostExecute*****isAnalyticalDataComplete=" + isAnalyticalDataComplete);
            if (isAnalyticalDataComplete) {
                initPickerView();
                if (analyticalDataAsyncTask != null && analyticalDataAsyncTask.getStatus() == Status.RUNNING) {
                    analyticalDataAsyncTask.cancel(true);
                    analyticalDataAsyncTask = null;
                }
            }
        }
    }

}