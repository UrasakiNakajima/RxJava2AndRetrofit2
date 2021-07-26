package com.phone.common_library.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * author    : Urasaki
 * e-mail    : Urasaki@qq.com
 * date      : 2020/3/10 14:12
 * introduce : 禁止左右滑动
 */

public class MineLazyViewPager extends LazyViewPager {

    private boolean isCanScroll = false;

    public MineLazyViewPager(Context context) {
        super(context);
    }

    public MineLazyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

}
