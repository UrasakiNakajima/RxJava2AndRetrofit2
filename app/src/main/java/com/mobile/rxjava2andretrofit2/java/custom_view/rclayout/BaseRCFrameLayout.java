package com.mobile.rxjava2andretrofit2.java.custom_view.rclayout;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/11/12 15:30
 * introduce : 圆角子FrameLayout
 */
public abstract class BaseRCFrameLayout extends RCFrameLayout {

    private static final String TAG = "BaseRCFrameLayout";
    private Activity activity;
    protected Map<String, String> bodyParams;
    private Intent intent;
    private Bundle bundle;

    public BaseRCFrameLayout(@NonNull Context context) {
        super(context);

        this.activity = (Activity) context;
        bodyParams = new HashMap<>();
    }

    public BaseRCFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.activity = (Activity) context;
        bodyParams = new HashMap<>();
    }

    protected abstract View initLayout();

    protected abstract void initData();

    protected abstract void initViews();

    protected void initViews(View view) {

    }

    protected void initLoadData() {

    }

    public void showLoading() {
    }

    public void hideLoading() {
    }

    protected void showToast(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
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
        activity.startActivity(intent);
    }

    private boolean mStartAnimator = false;

    /**
     * 设置推荐位到达边界时抖动
     *
     * @param focusView
     * @param direction 抖动方向，取值使用{@link View#FOCUS_RIGHT},{@link View#FOCUS_LEFT},
     *                  {@link View#FOCUS_UP},{@link View#FOCUS_DOWN}
     */
    protected void setShakeAnimator(View focusView, int direction) {
        ObjectAnimator translationAnimator;

        switch (direction) {
            case FOCUS_LEFT:
                translationAnimator = ObjectAnimator.ofFloat(focusView, "translationX", 0f, -15f, 0f, 15f, 0f, -15f, 0f, 15f, 0f);
                break;
            case FOCUS_DOWN:
                translationAnimator = ObjectAnimator.ofFloat(focusView, "translationY", 0f, 15f, 0f, -15f, 0f, 15f, 0f, -15f, 0f);
                break;
            case FOCUS_UP:
                translationAnimator = ObjectAnimator.ofFloat(focusView, "translationY", 0f, -15f, 0f, 15f, 0f, -15f, 0f, 15f, 0f);
                break;
            case FOCUS_RIGHT:
            default:
                translationAnimator = ObjectAnimator.ofFloat(focusView, "translationX", 0f, 15f, 0f, -15f, 0f, 15f, 0f, -15f, 0f);
                break;
        }
        translationAnimator.setInterpolator(new LinearInterpolator());
        translationAnimator.setDuration(300);
        translationAnimator.addListener(animatorListener);
        translationAnimator.start();
    }

    private Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            mStartAnimator = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mStartAnimator = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            mStartAnimator = false;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

    protected boolean isShakeAnimationRun() {
        return mStartAnimator;
    }

}
