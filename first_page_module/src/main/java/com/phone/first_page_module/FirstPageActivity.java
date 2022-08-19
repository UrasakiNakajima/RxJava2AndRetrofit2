package com.phone.first_page_module;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.phone.common_library.BaseApplication;
import com.phone.common_library.base.BaseMvpRxAppActivity;
import com.phone.common_library.base.IBaseView;
import com.phone.common_library.callback.OnCommonRxPermissionsCallback;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.callback.RcvOnItemViewClickListener;
import com.phone.common_library.manager.LogManager;
import com.phone.common_library.manager.RetrofitManager;
import com.phone.common_library.manager.RxPermissionsManager;
import com.phone.common_library.manager.ScreenManager;
import com.phone.common_library.manager.SystemManager;
import com.phone.common_library.ui.NewsDetailActivity;
import com.phone.first_page_module.adapter.FirstPageAdapter;
import com.phone.common_library.bean.FirstPageResponse;
import com.phone.first_page_module.manager.AMAPLocationManager;
import com.phone.first_page_module.presenter.FirstPagePresenterImpl;
import com.phone.first_page_module.view.IFirstPageView;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce :
 */
public class FirstPageActivity extends BaseMvpRxAppActivity<IBaseView, FirstPagePresenterImpl>
        implements IFirstPageView {

    private static final String TAG = FirstPageActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView tevTitle;
    private TextView tevRequestPermissionAndStartLocating;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView rcvData;
    private QMUILoadingView loadView;

    private List<FirstPageResponse.ResultData.JuheNewsBean> mJuheNewsBeanList = new ArrayList<>();
    private FirstPageAdapter firstPageAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean isRefresh;

    private AMAPLocationManager amapLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_first_page;
    }

    @Override
    protected void initData() {
        isRefresh = true;

        amapLocationManager = AMAPLocationManager.getInstance(BaseApplication.getInstance());
        amapLocationManager.setOnCommonSingleParamCallback(new OnCommonSingleParamCallback<AMapLocation>() {
            @Override
            public void onSuccess(AMapLocation success) {
                LogManager.i(TAG, "address*****" + success.getAddress());
            }

            @Override
            public void onError(String error) {
                LogManager.i(TAG, "error*****" + error);
            }
        });
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tevTitle = (TextView) findViewById(R.id.tev_title);
        tevRequestPermissionAndStartLocating = (TextView) findViewById(R.id.tev_request_permission_and_start_locating);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        rcvData = (RecyclerView) findViewById(R.id.rcv_data);
        loadView = (QMUILoadingView) findViewById(R.id.loadView);


        setToolbar(false, R.color.color_FFE066FF);

        tevRequestPermissionAndStartLocating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogManager.i(TAG, "tevRequestPermissions");
                initRxPermissionsRxAppCompatActivity();
            }
        });
        initAdapter();
    }

    private void initAdapter() {
        linearLayoutManager = new LinearLayoutManager(rxAppCompatActivity);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvData.setLayoutManager(linearLayoutManager);
        rcvData.setItemAnimator(new DefaultItemAnimator());

        firstPageAdapter = new FirstPageAdapter(rxAppCompatActivity);
        //		firstPageAdapter = new FirstPageAdapter2(activity, R.layout.item_first_page);
        firstPageAdapter.setRcvOnItemViewClickListener(new RcvOnItemViewClickListener() {
            @Override
            public void onItemClickListener(int position, View view) {
                //				if (view.getId() == R.id.tev_data) {
                //					//					url = "http://rbv01.ku6.com/omtSn0z_PTREtneb3GRtGg.mp4";
                //					//					url = "http://rbv01.ku6.com/7lut5JlEO-v6a8K3X9xBNg.mp4";
                //					url = "https://t-cmcccos.cxzx10086.cn/statics/shopping/detective_conan_japanese.mp4";
                //					//					fileFullname = mFileDTO.getFName();
                //					String[] arr = url.split("\\.");
                //					if (arr != null && arr.length > 0) {
                //						String fileName = "";
                //						StringBuilder stringBuilder = new StringBuilder();
                //						for (int i = 0; i < arr.length - 1; i++) {
                //							stringBuilder.append(arr[i]);
                //						}
                //						fileName = stringBuilder.toString();
                //						String suffix = arr[arr.length - 1];
                //
                //						paramMap.clear();
                //						paramMap.put("url", url);
                //						paramMap.put("suffix", suffix);
                //						startActivityCarryParams(ShowVideoActivity.class, paramMap);
                //					}
                //
                //				}

                if (view.getId() == R.id.ll_root) {
                    Intent intent = new Intent(rxAppCompatActivity, NewsDetailActivity.class);
                    intent.putExtra("detailUrl", mJuheNewsBeanList.get(position).getUrl());
                    startActivity(intent);
                }
            }
        });
        rcvData.setAdapter(firstPageAdapter);
        firstPageAdapter.clearData();
        firstPageAdapter.addAllData(mJuheNewsBeanList);

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogManager.i(TAG, "onLoadMore");
                isRefresh = false;
                initFirstPage();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                initFirstPage();
            }
        });
    }

    @Override
    protected void initLoadData() {
        refreshLayout.autoRefresh();
    }

    @Override
    protected FirstPagePresenterImpl attachPresenter() {
        return new FirstPagePresenterImpl(this);
    }

    @Override
    public void showLoading() {
        if (loadView != null && !loadView.isShown()) {
            loadView.setVisibility(View.VISIBLE);
            loadView.start();
        }
    }

    @Override
    public void hideLoading() {
        if (loadView != null && loadView.isShown()) {
            loadView.stop();
            loadView.setVisibility(View.GONE);
        }
    }

    @Override
    public void firstPageDataSuccess(List<FirstPageResponse.ResultData.JuheNewsBean> success) {
        if (!this.isFinishing()) {
            if (isRefresh) {
                mJuheNewsBeanList.clear();
                mJuheNewsBeanList.addAll(success);
                firstPageAdapter.clearData();
                firstPageAdapter.addAllData(mJuheNewsBeanList);
                refreshLayout.finishRefresh();
            } else {
                mJuheNewsBeanList.addAll(success);
                firstPageAdapter.clearData();
                firstPageAdapter.addAllData(mJuheNewsBeanList);
                refreshLayout.finishLoadMore();
            }
            hideLoading();
        }
    }

    @Override
    public void firstPageDataError(String error) {
        if (!this.isFinishing()) {
            //            showToast(error, true);
            showCustomToast(ScreenManager.dpToPx(this, 20f), ScreenManager.dpToPx(this, 20f),
                    16, getResources().getColor(R.color.white),
                    getResources().getColor(R.color.color_FFE066FF), ScreenManager.dpToPx(this, 40f),
                    ScreenManager.dpToPx(this, 20f), error,
                    true);
            if (isRefresh) {
                refreshLayout.finishRefresh(false);
            } else {
                refreshLayout.finishLoadMore(false);
            }
            hideLoading();
        }
    }

    /**
     * RxAppCompatActivity里需要的时候直接调用就行了
     */
    private void initRxPermissionsRxAppCompatActivity() {
        RxPermissionsManager rxPermissionsManager = new RxPermissionsManager();
        rxPermissionsManager.initRxPermissionsRxAppCompatActivity2(this, new OnCommonRxPermissionsCallback() {
                    @Override
                    public void onRxPermissionsAllPass() {
//                //製造一個不會造成App崩潰的異常（类强制转换异常java.lang.ClassCastException）
//                User user = new User2();
//                User3 user3 = (User3) user;
//                LogManager.i(TAG, user3.toString());

                        if (TextUtils.isEmpty(baseApplication.getSystemId())) {
                            String systemId = SystemManager.getSystemId(baseApplication);
                            baseApplication.setSystemId(systemId);
                            LogManager.i(TAG, "isEmpty systemId*****" + baseApplication.getSystemId());
                        } else {
                            LogManager.i(TAG, "systemId*****" + baseApplication.getSystemId());
                        }

                        amapLocationManager.startLocation();
                    }

                    @Override
                    public void onNotCheckNoMorePromptError() {

                    }

                    @Override
                    public void onCheckNoMorePromptError() {

                    }
                }, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void initFirstPage() {
        showLoading();
        if (RetrofitManager.isNetworkAvailable(rxAppCompatActivity)) {
            bodyParams.clear();

            bodyParams.put("type", "yule");
            bodyParams.put("key", "d5cc661633a28f3cf4b1eccff3ee7bae");
            presenter.firstPageRxAppCompatActivity(rxAppCompatActivity, bodyParams);
        } else {
            firstPageDataError(getResources().getString(R.string.please_check_the_network_connection));
            hideLoading();
        }
    }

}
