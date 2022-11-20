package com.phone.square_module.ui;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.phone.common_library.base.BaseRxAppActivity;
import com.phone.common_library.base.IBaseView;
import com.phone.common_library.bean.UserBean;
import com.phone.common_library.dialog.StandardDialog;
import com.phone.common_library.dialog.StandardUserDialog;
import com.phone.common_library.manager.MainThreadManager;
import com.phone.common_library.manager.ResourcesManager;
import com.phone.common_library.manager.TextViewStyleManager;
import com.phone.common_library.manager.ThreadPoolManager;
import com.phone.common_library.manager.UserBeanDaoManager;
import com.phone.square_module.R;
import com.phone.square_module.adapter.UserBeanAdapter;

import java.util.Collections;
import java.util.List;

public class CreateUserActivity extends BaseRxAppActivity implements IBaseView {

    private static final String TAG = CreateUserActivity.class.getSimpleName();
    private LinearLayout layoutRoot;
    private Toolbar toolbar;
    private FrameLayout layoutBack;
    private ImageView imvBack;
    private TextView tevTitle;
    private RecyclerView rcvUser;
    private TextView tevCreateUser;
    private TextView tevDeleteAllUser;


    private UserBeanDaoManager userBeanDaoManager;
    private UserBeanAdapter userBeanAdapter;

    private StandardUserDialog createUserDialog;//创建用户Dialog
    private StandardUserDialog updateUserDialog;//修改用户Dialog
    private StandardDialog deleteUserDialog;//删除用户Dialog
    private StandardDialog deleteAllUserDialog;//删除全部用户Dialog
    private List<UserBean> queryUserList;

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
        userBeanDaoManager = new UserBeanDaoManager();
    }

    @Override
    protected void initViews() {
        layoutRoot = (LinearLayout) findViewById(R.id.layout_root);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        layoutBack = (FrameLayout) findViewById(R.id.layout_back);
        imvBack = (ImageView) findViewById(R.id.imv_back);
        tevTitle = (TextView) findViewById(R.id.tev_title);
        rcvUser = (RecyclerView) findViewById(R.id.rcv_user);
        tevCreateUser = (TextView) findViewById(R.id.tev_create_user);
        tevDeleteAllUser = (TextView) findViewById(R.id.tev_delete_all_user);

        setToolbar(false, R.color.color_FF198CFF);
        imvBack.setColorFilter(ResourcesManager.getColor(R.color.white));

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

//        int[] arr = {10, 7, 8, 5, 17, 11, 18, 29, 21, 16, 26};
//        for (int i = 0; i < arr.length - 1; i++) {
//            for (int j = 0; j < arr.length - 1 - i; j++) {
//                if (arr[j] > arr[j + 1]) {
//                    int temp = arr[j];
//                    arr[j] = arr[j + 1];
//                    arr[j + 1] = temp;
//                }
//            }
//        }
//        for (int i = 0; i < arr.length; i++) {
//            LogManager.i(TAG, "number" + i + "*****" + arr[i]);
//        }
    }

    private void initAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
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
            if (view.getId() == R.id.tev_update) {
                showUpdateUserDialog(position);
            } else if (view.getId() == R.id.tev_delete) {
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
        showLoading();
        ThreadPoolManager.getInstance().multiplexSyncThreadPool(60, () -> {
            queryUserList = userBeanDaoManager.queryAll();

            new MainThreadManager(() -> {
                if (queryUserList != null && queryUserList.size() > 0) {
//            /**
//             * 这里只是为了试一下拷贝对象属性（如果有这个属性才会拷贝，没有则无法拷贝）
//             */
//            UserCloneBean userCloneBean = new UserCloneBean();
//            try {
//                CopyPropertiesManager.copyProperties(queryUserList.get(0), userCloneBean);
//                String userCloneBeanJsonStr = JSONObject.toJSONString(userCloneBean);
//                LogManager.i(TAG, "userCloneBeanJsonStr******" + userCloneBeanJsonStr);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


//            UserListBean userListBean = new UserListBean();
//            userListBean.setCode(200);
//            userListBean.setMessage("success");
//            userListBean.setUserBeanList(queryUserList);
//            String userListJsonStr = JSONObject.toJSONString(userListBean);
//            LogManager.i(TAG, "userListJsonStr******" + userListJsonStr);

//            /**
//             * 这里只是为了试一下UserListBean类中的UserBean类生成的json字符串的Double类型的字段salary，解析后变成UserResponse的String类型的字段salary
//             * 会不会报错（实际上不会报错，java的8种基本类型的字段都可以解析成String类型，只是这样做不规范）
//             */
//            UserResponseListBean userResponseListBean = JSONObject.parseObject(userListJsonStr, UserResponseListBean.class);
//            LogManager.i(TAG, "userResponseListBean******" + userResponseListBean.toString());
//            String userResponseListJsonStr = JSONObject.toJSONString(userResponseListBean);
//            LogManager.i(TAG, "userResponseListJsonStr******" + userResponseListJsonStr);

                    Collections.reverse(queryUserList);
                    userBeanAdapter.clearData();
                    userBeanAdapter.addData(queryUserList);
                    TextViewStyleManager.setTextViewStyleVerticalCenter(this,
                            tevTitle, getResources().getString(R.string.created_b)
                                    + queryUserList.size()
                                    + getResources().getString(R.string.users_b),
                            getResources().getString(R.string.created_b).length(),
                            getResources().getString(R.string.created_b).length() + String.valueOf(queryUserList.size()).length() + 1,
                            28);

//            Timer timer = new Timer();
//            TimerTask timerTask = new TimerTask() {
//                @Override
//                public void run() {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Bitmap bitmap = PictureManager.getCacheBitmapFromView(CreateUserActivity.this, layoutRoot);
//                            PictureManager.saveImageToPath(CreateUserActivity.this, bitmap, "create_user");
//                        }
//                    });
//                }
//            };
//            timer.schedule(timerTask, 2000);
                } else {
                    TextViewStyleManager.setTextViewStyleVerticalCenter(this,
                            tevTitle, getResources().getString(R.string.created_b)
                                    + 0
                                    + getResources().getString(R.string.users_b),
                            getResources().getString(R.string.created_b).length(),
                            getResources().getString(R.string.created_b).length() + 2,
                            28);
                }
                hideLoading();
            }).subThreadToUIThread();
        });
    }

    private void showCreateUserDialog() {
        if (createUserDialog == null) {
            createUserDialog = new StandardUserDialog(this);
            createUserDialog.setTevTitle(getResources().getString(R.string.create_user));
//            createUserDialog.setCannotHide();
            createUserDialog.setOnCommonSuccessCallback(() -> {
                createUserDialog.hideStandardDialog();
                createUserDialog = null;
            });
            createUserDialog.setOnItemViewClick2Listener((position, view, success) -> {
                createUserDialog.hideStandardDialog();
                createUserDialog = null;


                showLoading();
                if (position != 0) {
                    List<UserBean> userBeanList = userBeanDaoManager.queryByQueryBuilder(success.getUserId());
                    if (userBeanList != null && userBeanList.size() > 0 && userBeanList.get(0).getUserId().equals(success.getUserId())) {
                        showToast(ResourcesManager.getString(R.string.this_user_has_been_added), true);
                    } else {
                        ThreadPoolManager.getInstance().multiplexSyncThreadPool(60, () -> {
//                            List<UserBean> userBeanAddList = new ArrayList<>();
//                            for (int i = 0; i < 20000; i++) {
                            String jsonStr = JSONObject.toJSONString(success);
                            UserBean userBean = JSONObject.parseObject(jsonStr, UserBean.class);
//                                userBeanAddList.add(userBean);
//                            }
//                            userBeanDaoManager.insertInTx(userBeanAddList);
                            userBeanDaoManager.insert(userBean);
                        });
                    }

                    ThreadPoolManager.getInstance().multiplexSyncThreadPool(60, () -> {
                        queryUserList = userBeanDaoManager.queryAll();
                        new MainThreadManager(() -> {
                            if (queryUserList != null && queryUserList.size() > 0) {
                                Collections.reverse(queryUserList);
                                userBeanAdapter.clearData();
                                userBeanAdapter.addData(queryUserList);
                                TextViewStyleManager.setTextViewStyleVerticalCenter(this,
                                        tevTitle, getResources().getString(R.string.created_b)
                                                + queryUserList.size()
                                                + getResources().getString(R.string.users_b),
                                        getResources().getString(R.string.created_b).length(),
                                        getResources().getString(R.string.created_b).length() + String.valueOf(queryUserList.size()).length() + 1,
                                        28);
                            } else {
                                userBeanAdapter.clearData();
                                TextViewStyleManager.setTextViewStyleVerticalCenter(this,
                                        tevTitle, getResources().getString(R.string.created_b)
                                                + 0
                                                + getResources().getString(R.string.users_b),
                                        getResources().getString(R.string.created_b).length(),
                                        getResources().getString(R.string.created_b).length() + 2,
                                        28);
                            }
                            hideLoading();
                        }).subThreadToUIThread();
                    });
                }
            });
        }
    }

    private void showUpdateUserDialog(int position) {
        if (updateUserDialog == null) {
            updateUserDialog = new StandardUserDialog(this);
            updateUserDialog.setTevTitle(getResources().getString(R.string.create_user));
            updateUserDialog.setUserData(queryUserList.get(position));
//            updateUserDialog.setCannotHide();
            updateUserDialog.setOnCommonSuccessCallback(() -> {
                updateUserDialog.hideStandardDialog();
                updateUserDialog = null;
            });
            updateUserDialog.setOnItemViewClick2Listener((position2, view, success) -> {
                updateUserDialog.hideStandardDialog();
                updateUserDialog = null;


                showLoading();
                if (position2 != 0) {
                    List<UserBean> userBeanList = userBeanDaoManager.queryByQueryBuilder(success.getUserId());
                    if (userBeanList != null && userBeanList.size() > 0 && userBeanList.get(0).getUserId().equals(success.getUserId())) {
                        success.setId(userBeanList.get(0).getId());
                        ThreadPoolManager.getInstance().multiplexSyncThreadPool(60, () -> {
                            userBeanDaoManager.update(success);
                            new MainThreadManager(() ->
                                    showToast(ResourcesManager.getString(R.string.this_user_has_modified), true))
                                    .subThreadToUIThread();
                        });
                    } else {
                        showToast(ResourcesManager.getString(R.string.this_user_cannot_be_found), true);
                    }

                    ThreadPoolManager.getInstance().multiplexSyncThreadPool(60, () -> {
                        queryUserList = userBeanDaoManager.queryAll();
                        new MainThreadManager(() -> {
                            if (queryUserList != null && queryUserList.size() > 0) {
                                Collections.reverse(queryUserList);
                                userBeanAdapter.clearData();
                                userBeanAdapter.addData(queryUserList);
                                TextViewStyleManager.setTextViewStyleVerticalCenter(this,
                                        tevTitle, getResources().getString(R.string.created_b)
                                                + queryUserList.size()
                                                + getResources().getString(R.string.users_b),
                                        getResources().getString(R.string.created_b).length(),
                                        getResources().getString(R.string.created_b).length() + String.valueOf(queryUserList.size()).length() + 1,
                                        28);
                            } else {
                                userBeanAdapter.clearData();
                                TextViewStyleManager.setTextViewStyleVerticalCenter(this,
                                        tevTitle, getResources().getString(R.string.created_b)
                                                + 0
                                                + getResources().getString(R.string.users_b),
                                        getResources().getString(R.string.created_b).length(),
                                        getResources().getString(R.string.created_b).length() + 2,
                                        28);
                            }
                            hideLoading();
                        }).subThreadToUIThread();
                    });
                }
            });
        }
    }

    private void showDeleteUserDialog(int position) {
        if (deleteUserDialog == null) {
            deleteUserDialog = new StandardDialog(this);
            deleteUserDialog.setTevContent(getResources().getString(R.string.delete_user));
//            deleteUserDialog.setCannotHide();
            deleteUserDialog.setOnCommonSuccessCallback(() -> {
                deleteUserDialog.hideStandardDialog();
                deleteUserDialog = null;
            });
            deleteUserDialog.setOnItemViewClickListener((position2, view) -> {
                if (position2 == 0) {
                    deleteUserDialog.hideStandardDialog();
                    deleteUserDialog = null;
                } else {
                    deleteUserDialog.hideStandardDialog();
                    deleteUserDialog = null;

                    showLoading();
                    ThreadPoolManager.getInstance().multiplexSyncThreadPool(60, () -> {
                        userBeanDaoManager.delete(userBeanAdapter.getUserBeanList().get(position));
                        queryUserList = userBeanDaoManager.queryAll();

                        new MainThreadManager(() -> {
                            if (queryUserList != null && queryUserList.size() > 0) {
                                Collections.reverse(queryUserList);
                                userBeanAdapter.clearData();
                                userBeanAdapter.addData(queryUserList);
                                TextViewStyleManager.setTextViewStyleVerticalCenter(this,
                                        tevTitle, getResources().getString(R.string.created_b)
                                                + queryUserList.size()
                                                + getResources().getString(R.string.users_b),
                                        getResources().getString(R.string.created_b).length(),
                                        getResources().getString(R.string.created_b).length() + String.valueOf(queryUserList.size()).length() + 1,
                                        28);
                            } else {
                                userBeanAdapter.clearData();
                                TextViewStyleManager.setTextViewStyleVerticalCenter(this,
                                        tevTitle, getResources().getString(R.string.created_b)
                                                + 0
                                                + getResources().getString(R.string.users_b),
                                        getResources().getString(R.string.created_b).length(),
                                        getResources().getString(R.string.created_b).length() + 2,
                                        28);
                            }
                            hideLoading();
                        });
                    });
                }
            });
        }
    }

    private void showDeleteAllUserDialog() {
        if (deleteAllUserDialog == null) {
            deleteAllUserDialog = new StandardDialog(this);
            deleteAllUserDialog.setTevContent(getResources().getString(R.string.delete_all_user));
//            deleteAllUserDialog.setCannotHide();
            deleteAllUserDialog.setOnCommonSuccessCallback(() -> {
                deleteAllUserDialog.hideStandardDialog();
                deleteAllUserDialog = null;
            });
            deleteAllUserDialog.setOnItemViewClickListener((position, view) -> {
                if (position == 0) {
                    deleteAllUserDialog.hideStandardDialog();
                    deleteAllUserDialog = null;
                } else {
                    deleteAllUserDialog.hideStandardDialog();
                    deleteAllUserDialog = null;

                    showLoading();
                    ThreadPoolManager.getInstance().multiplexSyncThreadPool(60, () -> {
                        queryUserList = userBeanDaoManager.queryAll();
                        userBeanDaoManager.deleteInTx(queryUserList);
                        queryUserList = userBeanDaoManager.queryAll();


                        new MainThreadManager(() -> {
                            if (queryUserList != null && queryUserList.size() > 0) {
                                Collections.reverse(queryUserList);
                                userBeanAdapter.clearData();
                                userBeanAdapter.addData(queryUserList);
                                TextViewStyleManager.setTextViewStyleVerticalCenter(this,
                                        tevTitle, getResources().getString(R.string.created_b)
                                                + queryUserList.size()
                                                + getResources().getString(R.string.users_b),
                                        getResources().getString(R.string.created_b).length(),
                                        getResources().getString(R.string.created_b).length() + String.valueOf(queryUserList.size()).length() + 1,
                                        28);
                            } else {
                                userBeanAdapter.clearData();
                                TextViewStyleManager.setTextViewStyleVerticalCenter(this,
                                        tevTitle, getResources().getString(R.string.created_b)
                                                + 0
                                                + getResources().getString(R.string.users_b),
                                        getResources().getString(R.string.created_b).length(),
                                        getResources().getString(R.string.created_b).length() + 2,
                                        28);
                            }
                            hideLoading();
                        }).subThreadToUIThread();
                    });
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        userBeanDaoManager.closeConnection();
        ThreadPoolManager.getInstance().shutdownNow();
        super.onDestroy();
    }

}