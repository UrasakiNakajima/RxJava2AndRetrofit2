package com.mobile.common_library.custom_view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

/**
 * author    : Urasaki
 * e-mail    : Urasaki@qq.com
 * date      : 2020/3/26 11:11
 * introduce : 沉浸式状态栏Toolbar辅助类，避免Toolbar设置fitSystemWindows="true"当软键盘弹出时Toolbar被拉伸
 */


public class ImmerseGroup extends FrameLayout {

    public ImmerseGroup(Context context) {
        super(context);
    }

    public ImmerseGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImmerseGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), 0);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
