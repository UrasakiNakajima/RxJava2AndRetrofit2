package com.phone.common_library.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.phone.common_library.R;
import com.phone.common_library.bean.AddressBean;
import com.phone.common_library.bean.UserBean;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.callback.OnCommonSuccessCallback;
import com.phone.common_library.callback.OnItemViewClick2Listener;
import com.phone.common_library.manager.ResourcesManager;

import java.util.ArrayList;
import java.util.List;

public class StandardCreateUserDialog {

    private AlertDialog alertDialog;
    private TextView tevTitle;
    private EditText edtUserId;
    private EditText edtPassword;
    private EditText edtBirthday;
    private EditText edtAge;
    private EditText edtSalary;
    private View viewHorizontalLine;
    private TextView tevCancel;
    private View viewVerticalLine;
    private TextView tevOk;

    private String userId;
    private String password;
    private String birthday;
    private String age;
    private String salary;
//    private String userName;


    @SuppressLint("RestrictedApi")
    public StandardCreateUserDialog(@NonNull Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_standard_create_user, null, false);
        tevTitle = (TextView) view.findViewById(R.id.tev_title);
        edtUserId = (EditText) view.findViewById(R.id.edt_user_id);
        edtPassword = (EditText) view.findViewById(R.id.edt_password);
        edtBirthday = (EditText) view.findViewById(R.id.edt_birthday);
        edtAge = (EditText) view.findViewById(R.id.edt_age);
        edtSalary = (EditText) view.findViewById(R.id.edt_salary);
        viewHorizontalLine = (View) view.findViewById(R.id.view_horizontal_line);
        tevCancel = (TextView) view.findViewById(R.id.tev_cancel);
        viewVerticalLine = (View) view.findViewById(R.id.view_vertical_line);
        tevOk = (TextView) view.findViewById(R.id.tev_ok);


        //设置R.style.standard_dialog_style2就可以去掉
        //AlertDialog的默认边框，此时AlertDialog的layout的宽高就是AlertDialog的宽高
        alertDialog = new AlertDialog.Builder(context, R.style.standard_dialog_style)
                .setView(view)
                .create();
        tevCancel.setOnClickListener(v -> {
            onItemViewClick2Listener.onItemClickListener(0, v, null);
        });
        tevOk.setOnClickListener(v -> {
            userId = edtUserId.getText().toString();
            password = edtPassword.getText().toString();
            birthday = edtBirthday.getText().toString();
            age = edtAge.getText().toString();
            salary = edtSalary.getText().toString();
            if (!TextUtils.isEmpty(userId)
                    && !TextUtils.isEmpty(password)
                    && !TextUtils.isEmpty(birthday)
                    && !TextUtils.isEmpty(age)
                    && !TextUtils.isEmpty(salary)) {
                UserBean userBean = new UserBean();
                userBean.setUserId(userId);
                userBean.setPassword(password);
                userBean.setAge(Integer.parseInt(age));
                userBean.setBirthday(birthday);
                userBean.setSalary(Double.parseDouble(salary));

//                UserBean userBean = new UserBean();
//                userBean.setUserId("13513311001");
//                userBean.setPassword("12345678");
//                userBean.setAge(Integer.parseInt("23"));
//                userBean.setBirthday("2000-10-10");
//                userBean.setSalary(Double.parseDouble("7000"));

                List<AddressBean> addressBeanList = new ArrayList<>();
                addressBeanList.add(new AddressBean("北莱茵-威斯特法伦州", "波恩"));
                addressBeanList.add(new AddressBean("汉堡州", "汉堡"));
                userBean.setAddressBeanList(addressBeanList);
                onItemViewClick2Listener.onItemClickListener(1, v, userBean);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.please_fill_in_the_information_completely), Toast.LENGTH_SHORT).show();
            }
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

    public void setTevTitle(String content) {
        tevTitle.setText(content);
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

    private OnItemViewClick2Listener<UserBean> onItemViewClick2Listener;

    public void setOnItemViewClick2Listener(OnItemViewClick2Listener<UserBean> onItemViewClick2Listener) {
        this.onItemViewClick2Listener = onItemViewClick2Listener;
    }

    private OnCommonSuccessCallback onCommonSuccessCallback;

    public void setOnCommonSuccessCallback(OnCommonSuccessCallback onCommonSuccessCallback) {
        this.onCommonSuccessCallback = onCommonSuccessCallback;
    }
}
