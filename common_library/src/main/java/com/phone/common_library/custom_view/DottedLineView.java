package com.phone.common_library.custom_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.phone.common_library.R;

import androidx.core.content.ContextCompat;

/**
 * author : Urasaki
 * e-mail : 1164688204@qq.com
 * date   : 2021/4/29 11:19
 * desc   : 虚线View
 * version: 1.0
 */
public class DottedLineView extends View {
	
	private static final String TAG        = "DottedLineView";
	private              Paint  mDashPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private              Rect   mRect      = new Rect();
	
	public DottedLineView(Context context) {
		super(context, null);
	}
	
	public DottedLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	@SuppressLint({"InlinedApi", "NewApi"})
	private void init() {
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		final DisplayMetrics metrics = getResources().getDisplayMetrics();
		float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, metrics);
		float dashGap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, metrics);
		float dashWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, metrics);
		
		mDashPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_FF3258F7));
		mDashPaint.setStyle(Paint.Style.STROKE);
		mDashPaint.setStrokeWidth(width);
		mDashPaint.setAntiAlias(true);
		//DashPathEffect是Android提供的虚线样式API，具体的使用可以参考下面的介绍
		mDashPaint.setPathEffect(new DashPathEffect(new float[]{dashWidth, dashGap}, 0));
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		//取出线条的位置（位置的定义放在XML的layout中，具体如下xml文件所示）
		mRect.left = left;
		mRect.top = top;
		mRect.right = right;
		mRect.bottom = bottom;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		final DisplayMetrics metrics = getResources().getDisplayMetrics();
		float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, metrics);
		float dashGap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, metrics);
		float dashWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics);
		
		mDashPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_FF3258F7));
		mDashPaint.setStyle(Paint.Style.STROKE);
		mDashPaint.setStrokeWidth(width);
		mDashPaint.setAntiAlias(true);
		//DashPathEffect是Android提供的虚线样式API，具体的使用可以参考下面的介绍
		mDashPaint.setPathEffect(new DashPathEffect(new float[]{dashWidth, dashGap}, 0));
		
		float x0 = (mRect.right - mRect.left) / 2f;
		float y0 = 0;
		float x1 = x0;
		float y1 = y0 + mRect.bottom - mRect.top;
		canvas.drawLine(x0, y0, x1, y1, mDashPaint);
	}
}
