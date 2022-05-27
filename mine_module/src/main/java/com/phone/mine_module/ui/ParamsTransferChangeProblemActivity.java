package com.phone.mine_module.ui;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.phone.common_library.base.BaseAppActivity;
import com.phone.common_library.manager.LogManager;
import com.phone.mine_module.R;
import com.phone.mine_module.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 参数传递值改变问题
 */
public class ParamsTransferChangeProblemActivity extends BaseAppActivity {

    private static final String TAG = ParamsTransferChangeProblemActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView tevTitle;
    private TextView tevUserBeforeChange;
    private TextView tevNumberBeforeChange;
    private TextView tevStrBeforeChange;
    private TextView tevStringListBeforeChange;
    private LinearLayout layoutAfterChange;
    private TextView tevUserAfterChange;
    private TextView tevNumberAfterChange;
    private TextView tevStrAfterChange;
    private TextView tevStringListAfterChange;
    private TextView tevUserAfterChange2;
    private TextView tevStringListAfterChange2;


    private User user;
    private int number;
    private String str;
    private ArrayList<String> stringList = new ArrayList<>();

    @Override
    protected int initLayoutId() {
        return R.layout.activity_params_transfer_change_problem;
    }

    @Override
    protected void initData() {
        user = new User("Erwin", "man", 25);
        number = 10;
        str = "Rommel";
        stringList.add("Manstein");
        stringList.add("Manstein2");
        stringList.add("Manstein3");
        stringList.add("Manstein4");
        stringList.add("Manstein5");
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tevTitle = (TextView) findViewById(R.id.tev_title);
        tevUserBeforeChange = (TextView) findViewById(R.id.tev_user_before_change);
        tevNumberBeforeChange = (TextView) findViewById(R.id.tev_number_before_change);
        tevStrBeforeChange = (TextView) findViewById(R.id.tev_str_before_change);
        tevStringListBeforeChange = (TextView) findViewById(R.id.tev_stringList_before_change);
        layoutAfterChange = (LinearLayout) findViewById(R.id.layout_after_change);
        tevUserAfterChange = (TextView) findViewById(R.id.tev_user_after_change);
        tevNumberAfterChange = (TextView) findViewById(R.id.tev_number_after_change);
        tevStrAfterChange = (TextView) findViewById(R.id.tev_str_after_change);
        tevStringListAfterChange = (TextView) findViewById(R.id.tev_stringList_after_change);
        tevUserAfterChange2 = (TextView) findViewById(R.id.tev_user_after_change2);
        tevStringListAfterChange2 = (TextView) findViewById(R.id.tev_stringList_after_change2);

        setToolbar(false, R.color.color_FFE066FF);

    }

    @Override
    protected void initLoadData() {
        LogManager.i(TAG, "change before user*****" + user);
        LogManager.i(TAG, "change before number*****" + number);
        LogManager.i(TAG, "change before str*****" + str);
        LogManager.i(TAG, "change before stringList*****" + stringList);
        tevUserBeforeChange.setText("before_change:" + user.toString());
        tevNumberBeforeChange.setText("before_change:" + String.valueOf(number));
        tevStrBeforeChange.setText("before_change:" + str);
        tevStringListBeforeChange.setText("before_change:" + stringList.toString());
//        change(user, number, str, stringList);
//        LogManager.i(TAG, "change after user*****" + user);
//        LogManager.i(TAG, "change after number*****" + number);
//        LogManager.i(TAG, "change after str*****" + str);
//        LogManager.i(TAG, "change after stringList*****" + stringList.toString());
//        tevUserAfterChange.setText("after_change:" + user.toString());
//        tevNumberAfterChange.setText("after_change:" + String.valueOf(number));
//        tevStrAfterChange.setText("after_change:" + str);
//        tevStringListAfterChange.setText("after_change:" + stringList.toString());
        layoutAfterChange.setVisibility(View.GONE);


        try {
            change2((User) user.clone(), (ArrayList<String>) stringList.clone());
            LogManager.i(TAG, "change2 after user*****" + user);
            LogManager.i(TAG, "change2 after stringList*****" + stringList.toString());
            tevUserAfterChange2.setText("before_change2:" + user.toString());
            tevStringListAfterChange2.setText("before_change2:" + stringList.toString());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }

    private void change(User user, int number, String str, final List<String> stringList) {
        user.setAge(26);
        user.setName("Erwin2");
        number = 11;
        str = "Rommel2";
        stringList.add("Manstein6");
    }

    private void change2(final User user, final ArrayList<String> stringList) {
        user.setAge(27);
        user.setName("Erwin3");
        stringList.add("Manstein7");
    }


}