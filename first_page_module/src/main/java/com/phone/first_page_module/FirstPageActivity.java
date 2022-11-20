package com.phone.first_page_module;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.phone.common_library.BaseApplication;
import com.phone.common_library.base.BaseMvpRxAppActivity;
import com.phone.common_library.base.IBaseView;
import com.phone.common_library.bean.FirstPageResponse;
import com.phone.common_library.callback.OnCommonRxPermissionsCallback;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.callback.OnItemViewClickListener;
import com.phone.common_library.manager.LogManager;
import com.phone.common_library.manager.RetrofitManager;
import com.phone.common_library.manager.RxPermissionsManager;
import com.phone.common_library.manager.ScreenManager;
import com.phone.common_library.manager.SystemManager;
import com.phone.common_library.ui.NewsDetailActivity;
import com.phone.first_page_module.adapter.FirstPageAdapter;
import com.phone.first_page_module.manager.AMAPLocationManager;
import com.phone.first_page_module.presenter.FirstPagePresenterImpl;
import com.phone.first_page_module.view.IFirstPageView;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

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

    private FirstPageAdapter firstPageAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean isRefresh;

    private AMAPLocationManager amapLocationManager;

    private AlertDialog mPermissionsDialog;
    private String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

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
                initRxPermissions();
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
        firstPageAdapter.setRcvOnItemViewClickListener(new OnItemViewClickListener() {
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
                    intent.putExtra("detailUrl", firstPageAdapter.mJuheNewsBeanList.get(position).getUrl());
                    startActivity(intent);
                }
            }
        });
        rcvData.setAdapter(firstPageAdapter);

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
                firstPageAdapter.mJuheNewsBeanList.clear();
                firstPageAdapter.mJuheNewsBeanList.addAll(success);
                firstPageAdapter.clearData();
                firstPageAdapter.addData(firstPageAdapter.mJuheNewsBeanList);
                refreshLayout.finishRefresh();
            } else {
                firstPageAdapter.addData(firstPageAdapter.mJuheNewsBeanList);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 207) {
//            initRxPermissions();
        }
    }

    /**
     * RxAppCompatActivity里需要的时候直接调用就行了
     */
    private void initRxPermissions() {
        RxPermissionsManager rxPermissionsManager = RxPermissionsManager.getInstance();
        rxPermissionsManager.initRxPermissions(this, permissions, new OnCommonRxPermissionsCallback() {
            @Override
            public void onRxPermissionsAllPass() {
                //所有的权限都授予
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
                //至少一个权限未授予且未勾选不再提示
                showSystemSetupDialog();
            }

            @Override
            public void onCheckNoMorePromptError() {
                //至少一个权限未授予且勾选了不再提示
                showSystemSetupDialog();
            }
        });
    }

    private void showSystemSetupDialog() {
        cancelPermissionsDialog();
        if (mPermissionsDialog == null) {
            mPermissionsDialog = new AlertDialog.Builder(this)
                    .setTitle("权限设置")
                    .setMessage("获取相关权限失败，将导致部分功能无法正常使用，请到设置页面手动授权")
                    .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionsDialog();
                            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, 207);
                        }
                    })
                    .create();
        }

        mPermissionsDialog.setCancelable(false);
        mPermissionsDialog.setCanceledOnTouchOutside(false);
        mPermissionsDialog.show();
    }

    /**
     * 关闭对话框
     */
    private void cancelPermissionsDialog() {
        if (mPermissionsDialog != null) {
            mPermissionsDialog.cancel();
            mPermissionsDialog = null;
        }
    }

    private void initFirstPage() {
        showLoading();
        if (RetrofitManager.isNetworkAvailable(rxAppCompatActivity)) {
            bodyParams.clear();

            bodyParams.put("type", "yule");
            bodyParams.put("key", "d5cc661633a28f3cf4b1eccff3ee7bae");
            presenter.firstPage2(rxAppCompatActivity, bodyParams);
        } else {
            firstPageDataError(getResources().getString(R.string.please_check_the_network_connection));
            hideLoading();
        }
    }

}
