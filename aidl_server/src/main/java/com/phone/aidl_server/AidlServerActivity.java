package com.phone.aidl_server;

import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.phone.aidl_server.bean.ConnectBean;
import com.phone.library_common.base.BaseRxAppActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AidlServerActivity extends BaseRxAppActivity {

    private static final String TAG = AidlServerActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView tevTitle;
    private TextView tevConnectInfo;

    @Override
    protected int initLayoutId() {
        return R.layout.server_activity_aidl_server;
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tevTitle = (TextView) findViewById(R.id.tev_title);
        tevConnectInfo = (TextView) findViewById(R.id.tev_connect_info);

        setToolbar(false, R.color.library_color_FF198CFF);
    }

    @Override
    protected void initLoadData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiver(ConnectBean connectBean) {

        tevConnectInfo.setText(connectBean.getConnectInfo());
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}