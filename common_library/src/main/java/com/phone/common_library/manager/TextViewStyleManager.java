package com.phone.common_library.manager;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.phone.common_library.spannable.VerticalAlignTextSpan;

public class TextViewStyleManager {

    private static final String TAG = TextViewStyleManager.class.getSimpleName();

    public static void setTextViewStyle(TextView textView,
                                        String data,
                                        int start) {
        Spannable spannable = new SpannableString(data);
        // 粗体
        spannable.setSpan(new StyleSpan(Typeface.BOLD), start, data.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        // 显示
        textView.setText(spannable);
    }

    public static void setTextViewStyle(Context context,
                                        TextView textView,
                                        String data,
                                        int start,
                                        int end,
                                        float size) {
        Spannable spannable = new SpannableString(data);
        // 粗体
        spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new AbsoluteSizeSpan(ScreenManager.spToPx(context, size)), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        // 显示
        textView.setText(spannable);
    }

    public static void setTextViewStyleVerticalCenter(Context context,
                                        TextView textView,
                                        String data,
                                        int start,
                                        int end,
                                        float size) {
        Spannable spannable = new SpannableString(data);
        // 粗体
        spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new AbsoluteSizeSpan(ScreenManager.spToPx(context, size)), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //SpannableString字体大小不一致垂直居中显示
        spannable.setSpan(new VerticalAlignTextSpan(context, size), start, end, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        // 显示
        textView.setText(spannable);
    }

}
