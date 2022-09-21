package com.phone.common_library.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.phone.common_library.R;
import com.phone.common_library.bean.AddressBean;
import com.phone.common_library.bean.UserBean;
import com.phone.common_library.callback.OnItemViewClick2Listener;

import java.util.ArrayList;
import java.util.List;

public class StandardCreateUserDialog {

    private AlertDialog standardDialog;
    private TextView tevTitle;
    private EditText edtUserName;
    private EditText edtUserId;
    private EditText edtDate;
    private EditText edtAge;
    private EditText edtSalary;
    private View viewHorizontalLine;
    private TextView tevCancel;
    private View viewVerticalLine;
    private TextView tevOk;

    private String userName;
    private String userId;
    private String date;
    private String age;
    private String salary;
//    private String userName;
//    private String userName;


    @SuppressLint("RestrictedApi")
    public StandardCreateUserDialog(@NonNull Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_standard_create_user, null, false);
        tevTitle = (TextView) view.findViewById(R.id.tev_title);
        edtUserName = (EditText) view.findViewById(R.id.edt_user_name);
        edtUserId = (EditText) view.findViewById(R.id.edt_user_id);
        edtDate = (EditText) view.findViewById(R.id.edt_date);
        edtAge = (EditText) view.findViewById(R.id.edt_age);
        edtSalary = (EditText) view.findViewById(R.id.edt_salary);
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
            onItemViewClick2Listener.onItemClickListener(0, v, null);
        });
        tevOk.setOnClickListener(v -> {
            userName = edtUserName.getText().toString();
            userId = edtUserId.getText().toString();
            date = edtDate.getText().toString();
            age = edtAge.getText().toString();
            salary = edtSalary.getText().toString();
            if (!TextUtils.isEmpty(userName)
                    && !TextUtils.isEmpty(userId)
                    && !TextUtils.isEmpty(date)
                    && !TextUtils.isEmpty(age)
                    && !TextUtils.isEmpty(salary)) {
                UserBean userBean = new UserBean();
                userBean.setUserId(userId);
                userBean.setAge(Integer.parseInt(age));
                userBean.setUserName(userName);
                userBean.setDate(date);
                userBean.setSalary(Double.parseDouble(salary));
//                    Address address = new Address();
//                    address.setCounty("北莱茵-威斯特法伦州");
//                    address.setCity("波恩");
                List<AddressBean> addressBeanList = new ArrayList<>();
                addressBeanList.add(new AddressBean("北莱茵-威斯特法伦州", "波恩"));
                addressBeanList.add(new AddressBean("汉堡州", "汉堡"));
                userBean.setAddressBeanList(addressBeanList);
                onItemViewClick2Listener.onItemClickListener(1, v, userBean);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.please_fill_in_the_information_completely), Toast.LENGTH_SHORT).show();
            }
        });
        standardDialog.show();
    }

    public void setTevTitle(String content) {
        tevTitle.setText(content);
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

    private OnItemViewClick2Listener<UserBean> onItemViewClick2Listener;

    public void setOnItemViewClick2Listener(OnItemViewClick2Listener<UserBean> onItemViewClick2Listener) {
        this.onItemViewClick2Listener = onItemViewClick2Listener;
    }
}
