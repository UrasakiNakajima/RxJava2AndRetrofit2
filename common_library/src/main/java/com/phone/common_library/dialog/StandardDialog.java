package com.phone.common_library.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.phone.common_library.R;
import com.phone.common_library.callback.OnItemViewClickListener;

public class StandardDialog {

    private AlertDialog standardDialog;

    private TextView tevTitle;
    private TextView tevContent;
    private View viewHorizontalLine;
    private TextView tevCancel;
    private View viewVerticalLine;
    private TextView tevOk;


    @SuppressLint("RestrictedApi")
    public StandardDialog(@NonNull Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_standard, null, false);
        tevTitle = (TextView) view.findViewById(R.id.tev_title);
        tevContent = (TextView) view.findViewById(R.id.tev_content);
        viewHorizontalLine = (View) view.findViewById(R.id.view_horizontal_line);
        tevCancel = (TextView) view.findViewById(R.id.tev_cancel);
        viewVerticalLine = (View) view.findViewById(R.id.view_vertical_line);
        tevOk = (TextView) view.findViewById(R.id.tev_ok);


        //设置R.style.dialog_decimal_style和setView(view, 0, 0, 0, 0)就可以去掉
        //AlertDialog的默认边框，此时AlertDialog的layout的宽高就是AlertDialog的宽高
        standardDialog = new AlertDialog.Builder(context, R.style.dialog_decimal_style)
                .setView(view, 0, 0, 0, 0)
                .create();
        tevCancel.setOnClickListener(v -> {
            onItemViewClickListener.onItemClickListener(0, v);
        });
        tevOk.setOnClickListener(v -> {
            onItemViewClickListener.onItemClickListener(1, v);
        });
        standardDialog.show();
    }

    public void setTevContent(String content) {
        tevContent.setText(content);
    }

    public void setTevCancelHide() {
        viewVerticalLine.setVisibility(View.GONE);
        tevCancel.setVisibility(View.GONE);
    }

    public void setCannotHide() {
        standardDialog.setCancelable(false);
        standardDialog.setCanceledOnTouchOutside(false);
    }

    public void hideStandardDialog() {
        if (standardDialog != null) {
            standardDialog.dismiss();
            standardDialog = null;
        }
    }

    private OnItemViewClickListener onItemViewClickListener;

    public void setOnItemViewClickListener(OnItemViewClickListener onItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener;
    }

}
