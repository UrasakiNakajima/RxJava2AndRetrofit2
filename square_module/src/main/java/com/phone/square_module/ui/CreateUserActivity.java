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

import com.phone.common_library.base.BaseRxAppActivity;
import com.phone.common_library.bean.User;
import com.phone.common_library.dialog.StandardCreateUserDialog;
import com.phone.common_library.dialog.StandardDialog;
import com.phone.common_library.manager.UserDaoManager;
import com.phone.square_module.R;
import com.phone.square_module.adapter.UserAdapter;

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
    private UserAdapter userAdapter;

    private StandardCreateUserDialog createUserDialog;//创建用户Dialog
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

        userAdapter = new UserAdapter(this);
        userAdapter.setOnItemViewClickListener((position, view) -> {
            if (view.getId() == R.id.tev_delete) {
                showDeleteUserDialog(position);
            }
        });
        rcvUser.setAdapter(userAdapter);
    }

    @Override
    protected void initLoadData() {
        queryUserList();
    }

    private void queryUserList() {
        List<User> queryList = userDaoManager.queryAllUser();
        if (queryList != null && queryList.size() > 0) {
            Collections.reverse(queryList);
            userAdapter.clearData();
            userAdapter.addAllData(queryList);
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
                        Collections.reverse(queryList);
                        userAdapter.clearData();
                        userAdapter.addAllData(queryList);
                    }
                }
            });
        }
    }

    private void showDeleteUserDialog(int position) {
        if (createUserDialog == null) {
            createUserDialog = new StandardCreateUserDialog(this);
            createUserDialog.setTevTitle(getResources().getString(R.string.create_user));
            createUserDialog.setCannotHide();
            createUserDialog.setOnItemViewClick2Listener((position2, view, success) -> {
                if (position2 == 0) {
                    createUserDialog.hideStandardDialog();
                    createUserDialog = null;
                } else {
                    createUserDialog.hideStandardDialog();
                    createUserDialog = null;

                    userDaoManager.deleteUser(userAdapter.getUserList().get(position));
                    List<User> queryList = userDaoManager.queryAllUser();
                    if (queryList != null && queryList.size() > 0) {
                        Collections.reverse(queryList);
                        userAdapter.clearData();
                        userAdapter.addAllData(queryList);
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
                    if (queryList != null && queryList.size() > 0) {
                        Collections.reverse(queryList);
                        userAdapter.clearData();
                        userAdapter.addAllData(queryList);
                    } else {
                        userAdapter.clearData();
                    }
                }
            });
        }
    }

}