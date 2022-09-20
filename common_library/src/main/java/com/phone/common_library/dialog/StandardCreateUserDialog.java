package com.phone.common_library.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.phone.common_library.R;
import com.phone.common_library.bean.Address;
import com.phone.common_library.bean.User;
import com.phone.common_library.callback.OnItemViewClick2Listener;
import com.phone.common_library.manager.DateManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StandardCreateUserDialog {

    private AlertDialog standardDialog;
    private TextView tevTitle;
    private EditText edtUserName;
    private EditText edtUserId;
    private EditText edtDate;
    private View viewHorizontalLine;
    private TextView tevCancel;
    private View viewVerticalLine;
    private TextView tevOk;


    @SuppressLint("RestrictedApi")
    public StandardCreateUserDialog(@NonNull Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_standard_create_user, null, false);
        tevTitle = (TextView) view.findViewById(R.id.tev_title);
        edtUserName = (EditText) view.findViewById(R.id.edt_user_name);
        edtUserId = (EditText) view.findViewById(R.id.edt_user_id);
        edtDate = (EditText) view.findViewById(R.id.edt_date);
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
            User user = new User();
            user.setUserId(edtUserId.getText().toString());
            user.setAge(19);
            user.setUserName(edtUserName.getText().toString());
            user.setDate(edtDate.getText().toString());
//                    Address address = new Address();
//                    address.setCounty("北莱茵-威斯特法伦州");
//                    address.setCity("波恩");
            List<Address> addressList = new ArrayList<>();
            addressList.add(new Address("北莱茵-威斯特法伦州", "波恩"));
            addressList.add(new Address("汉堡州", "汉堡"));
            user.setAddressList(addressList);
            user.setDate(DateManager.dateToDataStr(new Date(System.currentTimeMillis())));
            onItemViewClick2Listener.onItemClickListener(1, v, user);
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

    private OnItemViewClick2Listener<User> onItemViewClick2Listener;

    public void setOnItemViewClick2Listener(OnItemViewClick2Listener<User> onItemViewClick2Listener) {
        this.onItemViewClick2Listener = onItemViewClick2Listener;
    }
}
