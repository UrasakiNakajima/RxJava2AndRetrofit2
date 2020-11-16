package com.mobile.rxjava2andretrofit2.base;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.gyf.immersionbar.ImmersionBar;
import com.mobile.rxjava2andretrofit2.MineApplication;
import com.mobile.rxjava2andretrofit2.R;

import java.util.Map;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/7 10:19
 * introduce :
 */

public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";
    protected MineApplication mineApplication;
    protected Activity activity;
    private Intent intent;
    private Bundle bundle;

    protected View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (rootView == null) {
//            rootView = inflater.inflate(initLayoutId(), container, false);
//        } else {
//            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
//            if (viewGroup != null) {
//                viewGroup.removeView(rootView);
//            }
//        }

        rootView = inflater.inflate(initLayoutId(), container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = getActivity();
        if (activity != null) {
            mineApplication = (MineApplication) activity.getApplication();
        }

        initData();
        initViews();
        initLoadData();
    }

    protected abstract int initLayoutId();

    protected abstract void initData();

    protected abstract void initViews();

    protected abstract void initLoadData();

    protected void showToast(String message, boolean isLongToast) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        if (!activity.isFinishing()) {
            Toast toast;
            int duration;
            if (isLongToast) {
                duration = Toast.LENGTH_LONG;
            } else {
                duration = Toast.LENGTH_SHORT;
            }
            toast = Toast.makeText(activity, message, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public boolean isOnMainThread() {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }

    protected void startActivity(Class<?> cls) {
        intent = new Intent(activity, cls);
        startActivity(intent);
    }

    protected void startActivityCarryParams(Class<?> cls, Map<String, String> params) {
        intent = new Intent(activity, cls);
        bundle = new Bundle();

        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                if (params.get(key) != null) {//如果参数不是null，才把参数传给后台
                    bundle.putString(key, params.get(key));
                }
            }
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode) {
        intent = new Intent(activity, cls);
        startActivityForResult(intent, requestCode);
    }

    protected void startActivityForResultCarryParams(Class<?> cls, Map<String, String> params, int requestCode) {
        intent = new Intent(activity, cls);
        bundle = new Bundle();

        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                if (params.get(key) != null) {//如果参数不是null，才把参数传给后台
                    bundle.putString(key, params.get(key));
                }
            }
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    protected boolean isEmpty(String dataStr) {
        if (dataStr != null && !"".equals(dataStr)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        RefWatcher refWatcher = MineApplication.getRefWatcher(activity);
//        refWatcher.watch(this);
    }
}
