package com.phone.common_library.base;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.phone.common_library.BaseApplication;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/7 10:35
 * introduce :
 */

public abstract class BaseMvpFragment<V, T extends BasePresenter<V>> extends Fragment {

    private static final String TAG = "BaseMvpFragment";
    protected T presenter;

    protected String url;
    protected Map<String, String> bodyParams;
    protected BaseApplication baseApplication;
    protected AppCompatActivity appCompatActivity;
    protected Intent intent;
    protected Bundle bundle;

    protected View rootView;
    private Unbinder unbinder;
    protected Fragment fragment;
//    private boolean isFirstLoad = true;

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

        fragment = this;
        rootView = inflater.inflate(initLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bodyParams = new HashMap<>();
        appCompatActivity = (AppCompatActivity) getActivity();
        if (appCompatActivity != null) {
            baseApplication = (BaseApplication) appCompatActivity.getApplication();
        }

        initData();
        presenter = attachPresenter();
        initViews();
        initLoadData();
    }

    protected abstract int initLayoutId();

    protected abstract void initData();

    protected abstract void initViews();

    protected abstract void initLoadData();

    /**
     * 适配为不同的view 装载不同的presenter
     *
     * @return
     */
    protected abstract T attachPresenter();

    protected void showToast(String message, boolean isLongToast) {
        //        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        if (!appCompatActivity.isFinishing()) {
            Toast toast;
            int duration;
            if (isLongToast) {
                duration = Toast.LENGTH_LONG;
            } else {
                duration = Toast.LENGTH_SHORT;
            }
            toast = Toast.makeText(appCompatActivity, message, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    protected void showCustomToast(int left, int right,
                                   int textSize, int textColor,
                                   int bgColor, int height,
                                   int roundRadius, String message,
                                   boolean isLongToast) {
        FrameLayout frameLayout = new FrameLayout(appCompatActivity);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        frameLayout.setLayoutParams(layoutParams);
        TextView textView = new TextView(appCompatActivity);
        FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, height);
        textView.setLayoutParams(layoutParams1);
        textView.setPadding(left, 0, right, 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        textView.setTextColor(textColor);
        textView.setGravity(Gravity.CENTER);
        textView.setIncludeFontPadding(false);
        GradientDrawable gradientDrawable = new GradientDrawable();//创建drawable
        gradientDrawable.setColor(bgColor);
        gradientDrawable.setCornerRadius(roundRadius);
        textView.setBackground(gradientDrawable);
        textView.setText(message);
        frameLayout.addView(textView);

        Toast toast = new Toast(appCompatActivity);
        toast.setView(frameLayout);
        if (isLongToast) {
            toast.setDuration(Toast.LENGTH_LONG);
        } else {
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public boolean isOnMainThread() {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }

    protected void startActivity(Class<?> cls) {
        intent = new Intent(appCompatActivity, cls);
        startActivity(intent);
    }

    protected void startActivityCarryParams(Class<?> cls, Map<String, String> params) {
        intent = new Intent(appCompatActivity, cls);
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
        intent = new Intent(appCompatActivity, cls);
        startActivityForResult(intent, requestCode);
    }

    protected void startActivityForResultCarryParams(Class<?> cls, Map<String, String> params, int requestCode) {
        intent = new Intent(appCompatActivity, cls);
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

    protected void detachPresenter() {
        if (presenter != null) {
            presenter.detachView();
            presenter = null;
        }
    }

    @Override
    public void onDestroyView() {
        detachPresenter();

        if (bodyParams != null) {
            bodyParams.clear();
            bodyParams = null;
        }
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
