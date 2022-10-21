package com.phone.square_module.ui;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.phone.common_library.base.BaseRxAppActivity;
import com.phone.common_library.bean.UserCloneBean;
import com.phone.common_library.bean.UserResponseListBean;
import com.phone.common_library.bean.UserBean;
import com.phone.common_library.bean.UserListBean;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.dialog.StandardCreateUserDialog;
import com.phone.common_library.dialog.StandardDialog;
import com.phone.common_library.manager.CopyPropertiesManager;
import com.phone.common_library.manager.LogManager;
import com.phone.common_library.manager.PictureManager;
import com.phone.common_library.manager.TextViewStyleManager;
import com.phone.common_library.manager.UserBeanDaoManager;
import com.phone.square_module.R;
import com.phone.square_module.adapter.UserBeanAdapter;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CreateUserActivity extends BaseRxAppActivity {

    private static final String TAG = CreateUserActivity.class.getSimpleName();
    private LinearLayout layoutRoot;
    private Toolbar toolbar;
    private FrameLayout layoutBack;
    private ImageView imvBack;
    private TextView tevTitle;
    private TextView tevCreateUser;
    private TextView tevDeleteAllUser;
    private RecyclerView rcvUser;

    private UserBeanDaoManager userBeanDaoManager = UserBeanDaoManager.getInstance();
    private LinearLayoutManager linearLayoutManager;
    private UserBeanAdapter userBeanAdapter;

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
        layoutRoot = (LinearLayout) findViewById(R.id.layout_root);
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

        userBeanAdapter = new UserBeanAdapter(this);
        userBeanAdapter.setOnItemViewClickListener((position, view) -> {
//            switch (view.getId()){
//                case R.id.tev_delete:
//                    showDeleteUserDialog(position);
//                    break;
//            }
            if (view.getId() == R.id.tev_delete) {
                showDeleteUserDialog(position);
            }
        });
        rcvUser.setAdapter(userBeanAdapter);
    }

    @Override
    protected void initLoadData() {
        queryUserList();
    }

    private void queryUserList() {
        List<UserBean> queryList = userBeanDaoManager.queryAll();
        if (queryList != null && queryList.size() > 0) {
            /**
             * 这里只是为了试一下拷贝对象属性（如果有这个属性才会拷贝，没有则无法拷贝）
             */
            UserCloneBean userCloneBean = new UserCloneBean();
            try {
                CopyPropertiesManager.copyProperties(queryList.get(0), userCloneBean);
                String userCloneBeanJsonStr = JSONObject.toJSONString(userCloneBean);
                LogManager.i(TAG, "userCloneBeanJsonStr******" + userCloneBeanJsonStr);
            } catch (Exception e) {
                e.printStackTrace();
            }


            UserListBean userListBean = new UserListBean();
            userListBean.setCode(200);
            userListBean.setMessage("success");
            userListBean.setUserBeanList(queryList);
            String userListJsonStr = JSONObject.toJSONString(userListBean);
            LogManager.i(TAG, "userListJsonStr******" + userListJsonStr);

            /**
             * 这里只是为了试一下UserListBean类中的UserBean类生成的json字符串的Double类型的字段salary，解析后变成UserResponse的String类型的字段salary
             * 会不会报错（实际上不会报错，java的8种基本类型的字段都可以解析成String类型，只是这样做不规范）
             */
            UserResponseListBean userResponseListBean = JSONObject.parseObject(userListJsonStr, UserResponseListBean.class);
            LogManager.i(TAG, "userResponseListBean******" + userResponseListBean.toString());
            String userResponseListJsonStr = JSONObject.toJSONString(userResponseListBean);
            LogManager.i(TAG, "userResponseListJsonStr******" + userResponseListJsonStr);


            Collections.reverse(queryList);
            userBeanAdapter.clearData();
            userBeanAdapter.addAllData(queryList);
            TextViewStyleManager.setTextViewStyleVerticalCenter(this,
                    tevTitle, getResources().getString(R.string.created_b)
                            + queryList.size()
                            + getResources().getString(R.string.users_b),
                    getResources().getString(R.string.created_b).length(),
                    getResources().getString(R.string.created_b).length() + String.valueOf(queryList.size()).length() + 1,
                    28);

            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = PictureManager.getCacheBitmapFromView(CreateUserActivity.this, layoutRoot);
                            PictureManager.saveImageToPath(CreateUserActivity.this, bitmap, "create_user");
                        }
                    });
                }
            };
            timer.schedule(timerTask, 2000);
        } else {
            TextViewStyleManager.setTextViewStyleVerticalCenter(this,
                    tevTitle, getResources().getString(R.string.created_b)
                            + queryList.size()
                            + getResources().getString(R.string.users_b),
                    getResources().getString(R.string.created_b).length(),
                    getResources().getString(R.string.created_b).length() + String.valueOf(queryList.size()).length() + 1,
                    28);
        }
    }

    private void showCreateUserDialog() {
        if (createUserDialog == null) {
            createUserDialog = new StandardCreateUserDialog(this);
            createUserDialog.setTevTitle(getResources().getString(R.string.create_user));
//            createUserDialog.setCannotHide();
            createUserDialog.setOnCommonSingleParamCallback(new OnCommonSingleParamCallback<String>() {
                @Override
                public void onSuccess(String success) {
                    createUserDialog.hideStandardDialog();
                    createUserDialog = null;
                }

                @Override
                public void onError(String error) {

                }
            });
            createUserDialog.setOnItemViewClick2Listener((position, view, success) -> {
                if (position == 0) {
                    createUserDialog.hideStandardDialog();
                    createUserDialog = null;
                } else {
                    createUserDialog.hideStandardDialog();
                    createUserDialog = null;
                    userBeanDaoManager.insert(success);
                    List<UserBean> queryList = userBeanDaoManager.queryAll();
                    if (queryList != null && queryList.size() > 0) {
                        Collections.reverse(queryList);
                        userBeanAdapter.clearData();
                        userBeanAdapter.addAllData(queryList);
                        TextViewStyleManager.setTextViewStyleVerticalCenter(this,
                                tevTitle, getResources().getString(R.string.created_b)
                                        + queryList.size()
                                        + getResources().getString(R.string.users_b),
                                getResources().getString(R.string.created_b).length(),
                                getResources().getString(R.string.created_b).length() + String.valueOf(queryList.size()).length() + 1,
                                28);
                    } else {
                        userBeanAdapter.clearData();
                        TextViewStyleManager.setTextViewStyleVerticalCenter(this,
                                tevTitle, getResources().getString(R.string.created_b)
                                        + queryList.size()
                                        + getResources().getString(R.string.users_b),
                                getResources().getString(R.string.created_b).length(),
                                getResources().getString(R.string.created_b).length() + String.valueOf(queryList.size()).length() + 1,
                                28);
                    }
                }
            });
        }
    }

    private void showDeleteUserDialog(int position) {
        if (deletUserDialog == null) {
            deletUserDialog = new StandardDialog(this);
            deletUserDialog.setTevContent(getResources().getString(R.string.delete_user));
//            deletUserDialog.setCannotHide();
            createUserDialog.setOnCommonSingleParamCallback(new OnCommonSingleParamCallback<String>() {
                @Override
                public void onSuccess(String success) {
                    createUserDialog.hideStandardDialog();
                    createUserDialog = null;
                }

                @Override
                public void onError(String error) {

                }
            });
            deletUserDialog.setOnItemViewClickListener((position2, view) -> {
                if (position2 == 0) {
                    deletUserDialog.hideStandardDialog();
                    deletUserDialog = null;
                } else {
                    deletUserDialog.hideStandardDialog();
                    deletUserDialog = null;

                    userBeanDaoManager.delete(userBeanAdapter.getUserBeanList().get(position));
                    List<UserBean> queryList = userBeanDaoManager.queryAll();
                    if (queryList != null && queryList.size() > 0) {
                        Collections.reverse(queryList);
                        userBeanAdapter.clearData();
                        userBeanAdapter.addAllData(queryList);
                        TextViewStyleManager.setTextViewStyleVerticalCenter(this,
                                tevTitle, getResources().getString(R.string.created_b)
                                        + queryList.size()
                                        + getResources().getString(R.string.users_b),
                                getResources().getString(R.string.created_b).length(),
                                getResources().getString(R.string.created_b).length() + String.valueOf(queryList.size()).length() + 1,
                                28);
                    } else {
                        userBeanAdapter.clearData();
                        TextViewStyleManager.setTextViewStyleVerticalCenter(this,
                                tevTitle, getResources().getString(R.string.created_b)
                                        + queryList.size()
                                        + getResources().getString(R.string.users_b),
                                getResources().getString(R.string.created_b).length(),
                                getResources().getString(R.string.created_b).length() + String.valueOf(queryList.size()).length() + 1,
                                28);
                    }
                }
            });
        }
    }

    private void showDeleteAllUserDialog() {
        if (deleteAllUserDialog == null) {
            deleteAllUserDialog = new StandardDialog(this);
            deleteAllUserDialog.setTevContent(getResources().getString(R.string.delete_all_user));
//            deleteAllUserDialog.setCannotHide();
            deleteAllUserDialog.setOnItemViewClickListener((position, view) -> {
                if (position == 0) {
                    deleteAllUserDialog.hideStandardDialog();
                    deleteAllUserDialog = null;
                } else {
                    deleteAllUserDialog.hideStandardDialog();
                    deleteAllUserDialog = null;

                    List<UserBean> queryList = userBeanDaoManager.queryAll();
                    userBeanDaoManager.deleteInTx(queryList);
                    List<UserBean> queryNewList = userBeanDaoManager.queryAll();
                    if (queryNewList != null && queryNewList.size() > 0) {
                        Collections.reverse(queryList);
                        userBeanAdapter.clearData();
                        userBeanAdapter.addAllData(queryList);
                        TextViewStyleManager.setTextViewStyleVerticalCenter(this,
                                tevTitle, getResources().getString(R.string.created_b)
                                        + queryList.size()
                                        + getResources().getString(R.string.users_b),
                                getResources().getString(R.string.created_b).length(),
                                getResources().getString(R.string.created_b).length() + String.valueOf(queryList.size()).length() + 1,
                                28);
                    } else {
                        userBeanAdapter.clearData();
                        TextViewStyleManager.setTextViewStyleVerticalCenter(this,
                                tevTitle, getResources().getString(R.string.created_b)
                                        + queryList.size()
                                        + getResources().getString(R.string.users_b),
                                getResources().getString(R.string.created_b).length(),
                                getResources().getString(R.string.created_b).length() + String.valueOf(queryList.size()).length() + 1,
                                28);
                    }
                }
            });
        }
    }

}