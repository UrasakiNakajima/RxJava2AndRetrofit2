package com.phone.common_library.spannable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.phone.common_library.manager.LogManager;
import com.phone.common_library.manager.ScreenManager;

public class VerticalAlignTextSpan extends ReplacementSpan {

    private static final String TAG = VerticalAlignTextSpan.class.getSimpleName();
    private Context context;
    private float fontSizeSp = -1;//单位:sp

    public VerticalAlignTextSpan() {
    }

    public VerticalAlignTextSpan(Context context, float fontSizeSp) {
        this.context = context;
        this.fontSizeSp = fontSizeSp;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        Paint newPaint = getCustomTextPaint(paint);
        return (int) newPaint.measureText(text, start, end);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        Paint newPaint = getCustomTextPaint(paint);
        Paint.FontMetricsInt fontMetricsInt = newPaint.getFontMetricsInt();
        int offsetY = (y + fontMetricsInt.ascent + y + fontMetricsInt.descent) / 2 - (top + bottom) / 2;
        LogManager.i(TAG, "offsetY-> " + offsetY);
        canvas.drawText(text, start, end, x, y - offsetY, newPaint);
    }

    private TextPaint getCustomTextPaint(Paint srcPaint) {
        TextPaint textPaint = new TextPaint(srcPaint);
        if (fontSizeSp != -1) {//-1没有重设fontSize
            textPaint.setTextSize(ScreenManager.spToPx(context, fontSizeSp));//sp转px
        }
        return textPaint;
    }

}
