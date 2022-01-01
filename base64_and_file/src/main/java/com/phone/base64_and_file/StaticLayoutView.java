package com.phone.base64_and_file;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class StaticLayoutView extends View {

    private Layout layout = null;

    private int width;
    private int height;

    public StaticLayoutView(Context context) {
        super(context);
    }

    public StaticLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StaticLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        if (layout != null) {
            //直接使用layout来绘制文本
            layout.draw(canvas, null, null, 0);
        }
        canvas.restore();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (layout != null) {
            setMeasuredDimension(layout.getWidth(), layout.getHeight());
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        if (this.layout.getWidth() != width || this.layout.getHeight() != height) {
            width = this.layout.getWidth();
            height = this.layout.getHeight();
            requestLayout();
        }
    }

}

