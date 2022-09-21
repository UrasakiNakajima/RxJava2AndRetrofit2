package com.phone.square_module.ui;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.phone.common_library.base.BaseRxAppActivity;
import com.phone.common_library.bean.User;
import com.phone.common_library.bean.UserCloneBean;
import com.phone.common_library.bean.UserCloneListBean;
import com.phone.common_library.dialog.StandardCreateUserDialog;
import com.phone.common_library.dialog.StandardDialog;
import com.phone.common_library.manager.CopyPropertiesManager;
import com.phone.common_library.manager.LogManager;
import com.phone.common_library.manager.UserDaoManager;
import com.phone.square_module.R;
import com.phone.square_module.adapter.UserCloneAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateUserActivity extends BaseRxAppActivity {

    private static final String TAG = CreateUserActivity.class.getSimpleName();
    private Toolbar toolbar;
    private FrameLayout layoutBack;
    private ImageView imvBack;
    private TextView tevTitle;
    private TextView tevCreateUser;
    private TextView tevDeleteAllUser;
    private RecyclerView rcvUser;

    private UserDaoManager userDaoManager = UserDaoManager.getInstance();
    private LinearLayoutManager linearLayoutManager;
    private UserCloneAdapter userCloneAdapter;

    private StandardCreateUserDialog createUserDialog;//创建用户Dialog
    private StandardDialog deletUserDialog;//删除用户Dialog
    private StandardDialog deleteAllUserDialog;//删除全部用户Dialog


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_create_user;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        layoutBack = (FrameLayout) findViewById(R.id.layout_back);
        imvBack = (ImageView) findViewById(R.id.imv_back);
        tevTitle = (TextView) findViewById(R.id.tev_title);
        tevCreateUser = (TextView) findViewById(R.id.tev_create_user);
        tevDeleteAllUser = (TextView) findViewById(R.id.tev_delete_all_user);
        rcvUser = (RecyclerView) findViewById(R.id.rcv_user);

        setToolbar(false, R.color.color_FF198CFF);
        imvBack.setColorFilter(ContextCompat.getColor(rxAppCompatActivity, R.color.white));

        layoutBack.setOnClickListener(v -> {
            finish();
        });
        tevCreateUser.setOnClickListener(v -> {
            showCreateUserDialog();
        });
        tevDeleteAllUser.setOnClickListener(v -> {
            showDeleteAllUserDialog();
        });

        initAdapter();
    }

    private void initAdapter() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvUser.setLayoutManager(linearLayoutManager);
        rcvUser.setItemAnimator(new DefaultItemAnimator());

        userCloneAdapter = new UserCloneAdapter(this);
        userCloneAdapter.setOnItemViewClickListener((position, view) -> {
//            switch (view.getId()){
//                case R.id.tev_delete:
//                    showDeleteUserDialog(position);
//                    break;
//            }
            if (view.getId() == R.id.tev_delete) {
                showDeleteUserDialog(position);
            }
        });
        rcvUser.setAdapter(userCloneAdapter);
    }

    @Override
    protected void initLoadData() {
        queryUserList();
    }

    private void queryUserList() {
        List<User> queryList = userDaoManager.queryAllUser();
        if (queryList != null && queryList.size() > 0) {
            List<UserCloneBean> userCloneBeanList = new ArrayList<>();
            for (int i = 0; i < queryList.size(); i++) {
                UserCloneBean userCloneBean = new UserCloneBean();
                try {
                    CopyPropertiesManager.copyProperties(queryList.get(i), userCloneBean);
//                    userCloneBean.setSalaryBigDecimal(userCloneBean.getSalary());
                    userCloneBean.setSalaryBigDecimal(BigDecimal.valueOf(userCloneBean.getSalary()));
                    userCloneBeanList.add(userCloneBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Collections.reverse(userCloneBeanList);
            userCloneAdapter.clearData();
            userCloneAdapter.addAllData(userCloneBeanList);

            UserCloneListBean userCloneListBean = new UserCloneListBean();
            userCloneListBean.setCode(200);
            userCloneListBean.setMessage("success");
            userCloneListBean.setUserCloneBeanList(userCloneBeanList);
            String jsonStr = JSONObject.toJSONString(userCloneListBean);

            LogManager.i(TAG, "jsonStr******" + jsonStr);
        }
    }

    private void showCreateUserDialog() {
        if (createUserDialog == null) {
            createUserDialog = new StandardCreateUserDialog(this);
            createUserDialog.setTevTitle(getResources().getString(R.string.create_user));
            createUserDialog.setCannotHide();
            createUserDialog.setOnItemViewClick2Listener((position, view, success) -> {
                if (position == 0) {
                    createUserDialog.hideStandardDialog();
                    createUserDialog = null;
                } else {
                    createUserDialog.hideStandardDialog();
                    createUserDialog = null;
                    userDaoManager.insertUser(success);

                    List<User> queryList = userDaoManager.queryAllUser();
                    if (queryList != null && queryList.size() > 0) {
                        List<UserCloneBean> userCloneBeanList = new ArrayList<>();
                        for (int i = 0; i < queryList.size(); i++) {
                            UserCloneBean userCloneBean = new UserCloneBean();
                            try {
                                CopyPropertiesManager.copyProperties(queryList.get(i), userCloneBean);
//                                userCloneBean.setSalaryBigDecimal(userCloneBean.getSalary());
                                userCloneBean.setSalaryBigDecimal(BigDecimal.valueOf(userCloneBean.getSalary()));
                                userCloneBeanList.add(userCloneBean);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        Collections.reverse(userCloneBeanList);
                        userCloneAdapter.clearData();
                        userCloneAdapter.addAllData(userCloneBeanList);
                    } else {
                        userCloneAdapter.clearData();
                    }
                }
            });
        }
    }

    private void showDeleteUserDialog(int position) {
        if (deletUserDialog == null) {
            deletUserDialog = new StandardDialog(this);
            deletUserDialog.setTevContent(getResources().getString(R.string.delete_user));
            deletUserDialog.setCannotHide();
            deletUserDialog.setOnItemViewClickListener((position2, view) -> {
                if (position2 == 0) {
                    deletUserDialog.hideStandardDialog();
                    deletUserDialog = null;
                } else {
                    deletUserDialog.hideStandardDialog();
                    deletUserDialog = null;

                    User user = new User();
                    try {
                        CopyPropertiesManager.copyProperties(userCloneAdapter.getUserCloneList().get(position), user);
                        userDaoManager.deleteUser(user);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    List<User> queryList = userDaoManager.queryAllUser();
                    if (queryList != null && queryList.size() > 0) {
                        List<UserCloneBean> userCloneBeanList = new ArrayList<>();
                        for (int i = 0; i < queryList.size(); i++) {
                            UserCloneBean userCloneBean = new UserCloneBean();
                            try {
                                CopyPropertiesManager.copyProperties(queryList.get(i), userCloneBean);
//                                userCloneBean.setSalaryBigDecimal(userCloneBean.getSalary());
                                userCloneBean.setSalaryBigDecimal(BigDecimal.valueOf(userCloneBean.getSalary()));
                                userCloneBeanList.add(userCloneBean);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        Collections.reverse(userCloneBeanList);
                        userCloneAdapter.clearData();
                        userCloneAdapter.addAllData(userCloneBeanList);
                    } else {
                        userCloneAdapter.clearData();
                    }
                }
            });
        }
    }

    private void showDeleteAllUserDialog() {
        if (deleteAllUserDialog == null) {
            deleteAllUserDialog = new StandardDialog(this);
            deleteAllUserDialog.setTevContent(getResources().getString(R.string.delete_all_user));
            deleteAllUserDialog.setCannotHide();
            deleteAllUserDialog.setOnItemViewClickListener((position, view) -> {
                if (position == 0) {
                    deleteAllUserDialog.hideStandardDialog();
                    deleteAllUserDialog = null;
                } else {
                    deleteAllUserDialog.hideStandardDialog();
                    deleteAllUserDialog = null;

                    List<User> queryList = userDaoManager.queryAllUser();
                    userDaoManager.deleteUserInTx(queryList);
                    List<User> queryNewList = userDaoManager.queryAllUser();
                    if (queryNewList != null && queryNewList.size() > 0) {
                        List<UserCloneBean> userCloneBeanList = new ArrayList<>();
                        for (int i = 0; i < queryNewList.size(); i++) {
                            UserCloneBean userCloneBean = new UserCloneBean();
                            try {
                                CopyPropertiesManager.copyProperties(queryNewList.get(i), userCloneBean);
//                                userCloneBean.setSalaryBigDecimal(userCloneBean.getSalary());
                                userCloneBean.setSalaryBigDecimal(BigDecimal.valueOf(userCloneBean.getSalary()));
                                userCloneBeanList.add(userCloneBean);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        Collections.reverse(userCloneBeanList);
                        userCloneAdapter.clearData();
                        userCloneAdapter.addAllData(userCloneBeanList);
                    } else {
                        userCloneAdapter.clearData();
                    }
                }
            });
        }
    }

}