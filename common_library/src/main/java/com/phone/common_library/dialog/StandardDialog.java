package com.phone.common_library.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.phone.common_library.R;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.callback.OnCommonSuccessCallback;
import com.phone.common_library.callback.OnItemViewClickListener;
import com.phone.common_library.manager.ResourcesManager;

public class StandardDialog {

    private AlertDialog alertDialog;

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


        //设置R.style.standard_dialog_style就可以去掉
        //AlertDialog的默认边框，此时AlertDialog的layout的宽高就是AlertDialog的宽高
        alertDialog = new AlertDialog.Builder(context, R.style.standard_dialog_style)
                .setView(view)
                .create();
        tevCancel.setOnClickListener(v -> {
            onItemViewClickListener.onItemClickListener(0, v);
        });
        tevOk.setOnClickListener(v -> {
            onItemViewClickListener.onItemClickListener(1, v);
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onCommonSuccessCallback.onSuccess();
            }
        });
        alertDialog.show();

        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(null);
            window.setGravity(Gravity.CENTER);
//            window.setWindowAnimations(R.style.PictureThemeDialogWindowStyle);
            WindowManager.LayoutParams params = window.getAttributes();
            window.setAttributes(params);
            //把 DecorView 的默认 padding 取消，同时 DecorView 的默认大小也会取消
            window.getDecorView().setPadding(0, 0, 0, 0);
        }
    }

    public void setTevContent(String content) {
        tevContent.setText(content);
    }

    public void setTevCancelHide() {
        viewVerticalLine.setVisibility(View.GONE);
        tevCancel.setVisibility(View.GONE);
    }

    public void setCannotHide() {
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
    }

    public void hideStandardDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    private OnItemViewClickListener onItemViewClickListener;

    public void setOnItemViewClickListener(OnItemViewClickListener onItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener;
    }

    private OnCommonSuccessCallback onCommonSuccessCallback;

    public void setOnCommonSuccessCallback(OnCommonSuccessCallback onCommonSuccessCallback) {
        this.onCommonSuccessCallback = onCommonSuccessCallback;
    }
}
