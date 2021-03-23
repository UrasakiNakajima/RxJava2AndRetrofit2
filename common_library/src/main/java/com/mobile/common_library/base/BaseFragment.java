package com.mobile.common_library.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobile.common_library.MineApplication;

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

    protected void showCustomToast(int left, int right,
                                   int textSize, int textColor,
                                   int bgColor, int height,
                                   int roundRadius, String message) {
        FrameLayout frameLayout = new FrameLayout(activity);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        frameLayout.setLayoutParams(layoutParams);
        TextView textView = new TextView(activity);
        FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, height);
        textView.setLayoutParams(layoutParams1);
        textView.setPadding(left, 0, right, 0);
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);
        textView.setGravity(Gravity.CENTER);
        textView.setIncludeFontPadding(false);
        GradientDrawable gradientDrawable = new GradientDrawable();//创建drawable
        gradientDrawable.setColor(bgColor);
        gradientDrawable.setCornerRadius(roundRadius);
        textView.setBackground(gradientDrawable);
        textView.setText(message);
        frameLayout.addView(textView);

        Toast toast = new Toast(activity);
        toast.setView(frameLayout);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
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

//        RefWatcher refWatcher = MineApplication.getRefWatcher();
//        refWatcher.watch(this);
    }
}
