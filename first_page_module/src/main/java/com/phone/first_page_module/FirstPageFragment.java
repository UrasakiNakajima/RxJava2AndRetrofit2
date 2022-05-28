package com.phone.first_page_module;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.phone.common_library.base.BaseMvpRxFragment;
import com.phone.common_library.base.IBaseView;
import com.phone.common_library.callback.OnCommonRxPermissionsCallback;
import com.phone.common_library.callback.RcvOnItemViewClickListener;
import com.phone.common_library.manager.LogManager;
import com.phone.common_library.manager.RetrofitManager;
import com.phone.common_library.manager.RxPermissionsManager;
import com.phone.common_library.manager.ScreenManager;
import com.phone.common_library.manager.SystemIdManager;
import com.phone.common_library.ui.NewsDetailActivity;
import com.phone.first_page_module.adapter.FirstPageAdapter;
import com.phone.first_page_module.bean.FirstPageResponse;
import com.phone.first_page_module.presenter.FirstPagePresenterImpl;
import com.phone.first_page_module.view.IFirstPageView;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = "/first_page_module/first_page")
public class FirstPageFragment extends BaseMvpRxFragment<IBaseView, FirstPagePresenterImpl>
        implements IFirstPageView {

    private static final String TAG = FirstPageFragment.class.getSimpleName();
    private FrameLayout layoutOutLayer;
    private Toolbar toolbar;
    private TextView tevTitle;
    private TextView tevRequestPermissions;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView rcvData;
    private QMUILoadingView loadView;

    private List<FirstPageResponse.ResultData.JuheNewsBean> mJuheNewsBeanList = new ArrayList<>();
    private FirstPageAdapter firstPageAdapter;
    //	private FirstPageAdapter2                              firstPageAdapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean isRefresh;
    private Map<String, String> paramMap = new HashMap<>();

    private AlertDialog mPermissionsDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_first_page;
    }

    @Override
    protected void initData() {
        isRefresh = true;
    }

    @Override
    protected void initViews() {
        layoutOutLayer = (FrameLayout) rootView.findViewById(R.id.layout_out_layer);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        tevTitle = (TextView) rootView.findViewById(R.id.tev_title);
        tevRequestPermissions = (TextView) rootView.findViewById(R.id.tev_request_permissions);
        refreshLayout = (SmartRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        rcvData = (RecyclerView) rootView.findViewById(R.id.rcv_data);
        loadView = (QMUILoadingView) rootView.findViewById(R.id.loadView);

        tevTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogManager.i(TAG, "tev_title");
                initFirstPage();
            }
        });
        tevRequestPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogManager.i(TAG, "tevRequestPermissions");
                initRxPermissionsRxFragment();
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
        //		firstPageAdapter = new FirstPageAdapter2(rxAppCompatActivity, R.layout.item_first_page);
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

        //		startAsyncTask();

//        try {
//            //製造一個不會造成App崩潰的異常（类强制转换异常java.lang.ClassCastException）
//            User user = new User2();
//            User3 user3 = (User3) user;
//            LogManager.i(TAG, user3.toString());
//        } catch (Exception e) {
//            ExceptionManager.getInstance().throwException(rxAppCompatActivity, e);
//        }
    }

    @Override
    protected FirstPagePresenterImpl attachPresenter() {
        return new FirstPagePresenterImpl(this);
    }

//	private void startAsyncTask() {
//
//		// This async task is an anonymous class and therefore has a hidden reference to the outer
//		// class MainActivity. If the activity gets destroyed before the task finishes (e.g. rotation),
//		// the activity instance will leak.
//		new AsyncTask<Void, Void, Void>() {
//			@Override
//			protected Void doInBackground(Void... params) {
//				// Do some slow work in background
//				SystemClock.sleep(10000);
//				return null;
//			}
//		}.execute();
//
//		Toast.makeText(rxAppCompatActivity, "请关闭这个A完成泄露", Toast.LENGTH_SHORT).show();
//	}

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
        if (!rxAppCompatActivity.isFinishing()) {
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
        }
    }

    @Override
    public void firstPageDataError(String error) {
        if (!rxAppCompatActivity.isFinishing()) {
            //            showToast(error, true);
            showCustomToast(ScreenManager.dpToPx(rxAppCompatActivity, 20f), ScreenManager.dpToPx(rxAppCompatActivity, 20f),
                    18, getResources().getColor(R.color.white),
                    getResources().getColor(R.color.color_FFE066FF), ScreenManager.dpToPx(rxAppCompatActivity, 40f),
                    ScreenManager.dpToPx(rxAppCompatActivity, 20f), error,
                    true);
            if (isRefresh) {
                refreshLayout.finishRefresh(false);
            } else {
                refreshLayout.finishLoadMore(false);
            }
        }
    }

    /**
     * RxFragment里需要的时候直接调用就行了
     */
    private void initRxPermissionsRxFragment() {
        RxPermissionsManager rxPermissionsManager = RxPermissionsManager.getInstance();
        rxPermissionsManager.initRxPermissionsRxFragment(this, new OnCommonRxPermissionsCallback() {
            @Override
            public void onRxPermissionsAllPass() {
//                //製造一個不會造成App崩潰的異常（类强制转换异常java.lang.ClassCastException）
//                User user = new User2();
//                User3 user3 = (User3) user;
//                LogManager.i(TAG, user3.toString());

                if (TextUtils.isEmpty(baseApplication.getSystemId())) {
                    String systemId = SystemIdManager.getSystemId(rxAppCompatActivity);
                    baseApplication.setSystemId(systemId);
                    LogManager.i(TAG, "isEmpty systemId*****" + baseApplication.getSystemId());
                } else {
                    LogManager.i(TAG, "systemId*****" + baseApplication.getSystemId());
                }
            }

            @Override
            public void onNotCheckNoMorePromptError() {
                showSystemSetupDialog();
            }

            @Override
            public void onCheckNoMorePromptError() {
                showSystemSetupDialog();
            }
        });
    }

    private void showSystemSetupDialog() {
        cancelPermissionsDialog();
        if (mPermissionsDialog == null) {
            mPermissionsDialog = new AlertDialog.Builder(rxAppCompatActivity)
                    .setTitle("权限设置")
                    .setMessage("获取相关权限失败，将导致部分功能无法正常使用，请到设置页面手动授权")
                    .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionsDialog();
                            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", rxAppCompatActivity.getApplicationContext().getPackageName(), null);
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
            presenter.firstPage(this, bodyParams);
        } else {
            firstPageDataError(getResources().getString(R.string.please_check_the_network_connection));
        }
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        layoutOutLayer.removeAllViews();
        super.onDestroy();
    }
}
