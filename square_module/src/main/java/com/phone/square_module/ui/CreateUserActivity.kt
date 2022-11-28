package com.phone.square_module.ui

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSONObject
import com.phone.common_library.base.BaseRxAppActivity
import com.phone.common_library.bean.UserBean
import com.phone.common_library.dialog.StandardDialog
import com.phone.common_library.dialog.StandardUserDialog
import com.phone.common_library.manager.*
import com.phone.square_module.R
import com.phone.square_module.adapter.UserBeanAdapter
import java.util.*

class CreateUserActivity : BaseRxAppActivity() {

    private val TAG = CreateUserActivity::class.java.simpleName
    private var layoutRoot: LinearLayout? = null
    private var toolbar: Toolbar? = null
    private var layoutBack: FrameLayout? = null
    private var imvBack: ImageView? = null
    private var tevTitle: TextView? = null
    private var rcvUser: RecyclerView? = null
    private var tevCreateUser: TextView? = null
    private var tevDeleteAllUser: TextView? = null

    private var userBeanDaoManager: UserBeanDaoManager? = null
    private var userBeanAdapter: UserBeanAdapter? = null

    private var createUserDialog //创建用户Dialog
            : StandardUserDialog? = null
    private var updateUserDialog //修改用户Dialog
            : StandardUserDialog? = null
    private var deleteUserDialog //删除用户Dialog
            : StandardDialog? = null
    private var deleteAllUserDialog //删除全部用户Dialog
            : StandardDialog? = null
    private var queryUserList: List<UserBean> = mutableListOf()

    override fun initLayoutId() = R.layout.activity_create_user

    override fun initData() {
        userBeanDaoManager = UserBeanDaoManager()
    }

    override fun initViews() {
        layoutRoot = findViewById<View>(R.id.layout_root) as LinearLayout
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        layoutBack = findViewById<View>(R.id.layout_back) as FrameLayout
        imvBack = findViewById<View>(R.id.imv_back) as ImageView
        tevTitle = findViewById<View>(R.id.tev_title) as TextView
        rcvUser = findViewById<View>(R.id.rcv_user) as RecyclerView
        tevCreateUser = findViewById<View>(R.id.tev_create_user) as TextView
        tevDeleteAllUser = findViewById<View>(R.id.tev_delete_all_user) as TextView
        setToolbar(false, R.color.color_FF198CFF)
        imvBack?.setColorFilter(ResourcesManager.getColor(R.color.white))
        layoutBack?.setOnClickListener { v: View? -> finish() }
        tevCreateUser?.setOnClickListener { v: View? -> showCreateUserDialog() }
        tevDeleteAllUser?.setOnClickListener { v: View? -> showDeleteAllUserDialog() }
        initAdapter()

//        int[] arr = {10, 7, 8, 5, 17, 11, 18, 29, 21, 16, 26}
//        for (int i = 0 i < arr.length - 1 i++) {
//            for (int j = 0 j < arr.length - 1 - i j++) {
//                if (arr[j] > arr[j + 1]) {
//                    int temp = arr[j]
//                    arr[j] = arr[j + 1]
//                    arr[j + 1] = temp
//                }
//            }
//        }
//        for (int i = 0 i < arr.length i++) {
//            LogManager.i(TAG, "number" + i + "*****" + arr[i])
//        }
    }

    private fun initAdapter() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        rcvUser?.layoutManager = linearLayoutManager
        rcvUser?.itemAnimator = DefaultItemAnimator()
        userBeanAdapter = UserBeanAdapter(this)
        userBeanAdapter?.setOnItemViewClickListener { position: Int, view: View ->
//            switch (view.getId()){
//                case R.id.tev_delete:
//                    showDeleteUserDialog(position)
//                    break
//            }
            if (view.id == R.id.tev_update) {
                showUpdateUserDialog(position)
            } else if (view.id == R.id.tev_delete) {
                showDeleteUserDialog(position)
            }
        }
        rcvUser?.adapter = userBeanAdapter
    }

    override fun initLoadData() {
        queryUserList()
    }

    private fun queryUserList() {
        showLoading()
        ThreadPoolManager.get().createSyncThreadPool {
            queryUserList = userBeanDaoManager?.queryAll() ?: mutableListOf()
            MainThreadManager {
                if (queryUserList.size > 0) {
//            /**
//             * 这里只是为了试一下拷贝对象属性（如果有这个属性才会拷贝，没有则无法拷贝）
//             */
//            UserCloneBean userCloneBean = new UserCloneBean()
//            try {
//                CopyPropertiesManager.copyProperties(queryUserList.get(0), userCloneBean)
//                String userCloneBeanJsonStr = JSONObject.toJSONString(userCloneBean)
//                LogManager.i(TAG, "userCloneBeanJsonStr******" + userCloneBeanJsonStr)
//            } catch (Exception e) {
//                e.printStackTrace()
//            }


//            UserListBean userListBean = new UserListBean()
//            userListBean.setCode(200)
//            userListBean.setMessage("success")
//            userListBean.setUserBeanList(queryUserList)
//            String userListJsonStr = JSONObject.toJSONString(userListBean)
//            LogManager.i(TAG, "userListJsonStr******" + userListJsonStr)

//            /**
//             * 这里只是为了试一下UserListBean类中的UserBean类生成的json字符串的Double类型的字段salary，解析后变成UserResponse的String类型的字段salary
//             * 会不会报错（实际上不会报错，java的8种基本类型的字段都可以解析成String类型，只是这样做不规范）
//             */
//            UserResponseListBean userResponseListBean = JSONObject.parseObject(userListJsonStr, UserResponseListBean.class)
//            LogManager.i(TAG, "userResponseListBean******" + userResponseListBean.toString())
//            String userResponseListJsonStr = JSONObject.toJSONString(userResponseListBean)
//            LogManager.i(TAG, "userResponseListJsonStr******" + userResponseListJsonStr)
                    Collections.reverse(queryUserList)
                    userBeanAdapter?.clearData()
                    userBeanAdapter?.addData(queryUserList)
                    tevTitle?.let {
                        TextViewStyleManager.setTextViewStyleVerticalCenter(
                            it,
                            ResourcesManager.getString(R.string.created_b)
                                    + queryUserList.size
                                    + ResourcesManager.getString(R.string.users_b),
                            ResourcesManager.getString(R.string.created_b).length,
                            ResourcesManager.getString(R.string.created_b).length + queryUserList.size.toString().length + 1,
                            28f
                        )
                    }

//            Timer timer = new Timer()
//            TimerTask timerTask = new TimerTask() {
//                @Override
//                public void run() {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Bitmap bitmap = PictureManager.getCacheBitmapFromView(CreateUserActivity.this, layoutRoot)
//                            PictureManager.saveImageToPath(CreateUserActivity.this, bitmap, "create_user")
//                        }
//                    })
//                }
//            }
//            timer.schedule(timerTask, 2000)
                } else {
                    tevTitle?.let {
                        TextViewStyleManager.setTextViewStyleVerticalCenter(
                            it,
                            ResourcesManager.getString(R.string.created_b)
                                    + 0
                                    + ResourcesManager.getString(R.string.users_b),
                            ResourcesManager.getString(R.string.created_b).length,
                            ResourcesManager.getString(R.string.created_b).length + 2,
                            28f
                        )
                    }
                }
                hideLoading()
            }.subThreadToUIThread()
        }
    }

    private fun showCreateUserDialog() {
        if (createUserDialog == null) {
            createUserDialog = StandardUserDialog(this)
            createUserDialog?.setTevTitle(ResourcesManager.getString(R.string.create_user))
            //            createUserDialog?.setCannotHide()
            createUserDialog?.setOnCommonSuccessCallback {
                createUserDialog?.hideStandardDialog()
                createUserDialog = null
            }
            createUserDialog?.setOnItemViewClick2Listener { position: Int, view: View?, success: UserBean? ->
                createUserDialog?.hideStandardDialog()
                createUserDialog = null
                showLoading()
                if (position == 0) {
                    hideLoading()
                    return@setOnItemViewClick2Listener
                }
                success?.let {
                    val userBeanList =
                        userBeanDaoManager?.queryByQueryBuilder(success.userId)
                    if (userBeanList != null && userBeanList.size > 0 && userBeanList[0]
                            .userId == success.userId
                    ) {
                        showToast(
                            ResourcesManager.getString(R.string.this_user_has_been_added),
                            true
                        )
                    } else {
                        ThreadPoolManager.get().createSyncThreadPool {
//                            List<UserBean> userBeanAddList = new ArrayList<>()
//                            for (int i = 0 i < 20000 i++) {
                            val jsonStr =
                                JSONObject.toJSONString(success)
                            val userBean =
                                JSONObject.parseObject(
                                    jsonStr,
                                    UserBean::class.java
                                )
                            //                                userBeanAddList.add(userBean)
//                            }
//                            userBeanDaoManager?.insertInTx(userBeanAddList)
                            userBeanDaoManager?.insert(userBean)
                        }
                    }
                    ThreadPoolManager.get().createSyncThreadPool {
                        queryUserList = userBeanDaoManager?.queryAll() ?: mutableListOf()
                        MainThreadManager {
                            if (queryUserList.size > 0) {
                                Collections.reverse(queryUserList)
                                userBeanAdapter?.clearData()
                                userBeanAdapter?.addData(queryUserList)
                                tevTitle?.let {
                                    TextViewStyleManager.setTextViewStyleVerticalCenter(
                                        it,
                                        ResourcesManager.getString(R.string.created_b)
                                                + queryUserList.size
                                                + ResourcesManager.getString(R.string.users_b),
                                        ResourcesManager.getString(R.string.created_b).length,
                                        ResourcesManager.getString(R.string.created_b).length + queryUserList.size.toString().length + 1,
                                        28f
                                    )
                                }
                            } else {
                                userBeanAdapter?.clearData()
                                tevTitle?.let {
                                    TextViewStyleManager.setTextViewStyleVerticalCenter(
                                        it,
                                        ResourcesManager.getString(R.string.created_b)
                                                + 0
                                                + ResourcesManager.getString(R.string.users_b),
                                        ResourcesManager.getString(R.string.created_b).length,
                                        ResourcesManager.getString(R.string.created_b).length + 2,
                                        28f
                                    )
                                }
                            }
                            hideLoading()
                        }.subThreadToUIThread()
                    }
                }
            }
        }
    }

    private fun showUpdateUserDialog(position: Int) {
        if (updateUserDialog == null) {
            updateUserDialog = StandardUserDialog(this)
            updateUserDialog?.setTevTitle(ResourcesManager.getString(R.string.create_user))
            updateUserDialog?.setUserData(queryUserList.get(position))
            //            updateUserDialog?.setCannotHide()
            updateUserDialog?.setOnCommonSuccessCallback {
                updateUserDialog?.hideStandardDialog()
                updateUserDialog = null
            }
            updateUserDialog?.setOnItemViewClick2Listener { position2: Int, view: View?, success: UserBean? ->
                updateUserDialog?.hideStandardDialog()
                updateUserDialog = null
                showLoading()
                if (position2 == 0) {
                    hideLoading()
                    return@setOnItemViewClick2Listener
                }
                success?.let {
                    val userBeanList =
                        userBeanDaoManager?.queryByQueryBuilder(success.userId)
                    if (userBeanList != null && userBeanList.size > 0 && userBeanList[0]
                            .userId == success.userId
                    ) {
                        success.id = userBeanList[0].id
                        ThreadPoolManager.get().createSyncThreadPool {
                            userBeanDaoManager?.update(success)
                            MainThreadManager({
                                showToast(
                                    ResourcesManager.getString(R.string.this_user_has_modified),
                                    true
                                )
                            })
                                .subThreadToUIThread()
                        }
                    } else {
                        showToast(
                            ResourcesManager.getString(R.string.this_user_cannot_be_found),
                            true
                        )
                    }
                    ThreadPoolManager.get().createSyncThreadPool {
                        queryUserList = userBeanDaoManager?.queryAll() ?: mutableListOf()
                        MainThreadManager {
                            if (queryUserList.size > 0) {
                                Collections.reverse(queryUserList)
                                userBeanAdapter?.clearData()
                                userBeanAdapter?.addData(queryUserList)
                                tevTitle?.let {
                                    TextViewStyleManager.setTextViewStyleVerticalCenter(
                                        it,
                                        ResourcesManager.getString(R.string.created_b)
                                                + queryUserList.size
                                                + ResourcesManager.getString(R.string.users_b),
                                        ResourcesManager.getString(R.string.created_b).length,
                                        ResourcesManager.getString(R.string.created_b).length + queryUserList.size.toString().length + 1,
                                        28f
                                    )
                                }
                            } else {
                                userBeanAdapter?.clearData()
                                tevTitle?.let {
                                    TextViewStyleManager.setTextViewStyleVerticalCenter(
                                        it,
                                        ResourcesManager.getString(R.string.created_b)
                                                + 0
                                                + ResourcesManager.getString(R.string.users_b),
                                        ResourcesManager.getString(R.string.created_b).length,
                                        ResourcesManager.getString(R.string.created_b).length + 2,
                                        28f
                                    )
                                }
                            }
                            hideLoading()
                        }.subThreadToUIThread()
                    }
                }
            }
        }
    }

    private fun showDeleteUserDialog(position: Int) {
        if (deleteUserDialog == null) {
            deleteUserDialog = StandardDialog(this)
            deleteUserDialog?.setTevContent(ResourcesManager.getString(R.string.delete_user))
            //            deleteUserDialog?.setCannotHide()
            deleteUserDialog?.setOnCommonSuccessCallback {
                deleteUserDialog?.hideStandardDialog()
                deleteUserDialog = null
            }
            deleteUserDialog?.setOnItemViewClickListener { position2: Int, view: View? ->
                if (position2 == 0) {
                    deleteUserDialog?.hideStandardDialog()
                    deleteUserDialog = null
                } else {
                    deleteUserDialog?.hideStandardDialog()
                    deleteUserDialog = null
                    showLoading()
                    ThreadPoolManager.get().createSyncThreadPool {
                        userBeanDaoManager?.delete(
                            userBeanAdapter?.userBeanList?.get(position) ?: UserBean()
                        )
                        queryUserList = userBeanDaoManager?.queryAll() ?: mutableListOf()
                        MainThreadManager {
                            if (queryUserList.size > 0) {
                                Collections.reverse(queryUserList)
                                userBeanAdapter?.clearData()
                                userBeanAdapter?.addData(queryUserList)
                                tevTitle?.let {
                                    TextViewStyleManager.setTextViewStyleVerticalCenter(
                                        it,
                                        ResourcesManager.getString(R.string.created_b)
                                                + queryUserList.size
                                                + ResourcesManager.getString(R.string.users_b),
                                        ResourcesManager.getString(R.string.created_b).length,
                                        ResourcesManager.getString(R.string.created_b).length + queryUserList.size.toString().length + 1,
                                        28f
                                    )
                                }
                            } else {
                                userBeanAdapter?.clearData()
                                tevTitle?.let {
                                    TextViewStyleManager.setTextViewStyleVerticalCenter(
                                        it,
                                        ResourcesManager.getString(R.string.created_b)
                                                + 0
                                                + ResourcesManager.getString(R.string.users_b),
                                        ResourcesManager.getString(R.string.created_b).length,
                                        ResourcesManager.getString(R.string.created_b).length + 2,
                                        28f
                                    )
                                }
                            }
                            hideLoading()
                        }
                    }
                }
            }
        }
    }

    private fun showDeleteAllUserDialog() {
        if (deleteAllUserDialog == null) {
            deleteAllUserDialog = StandardDialog(this)
            deleteAllUserDialog?.setTevContent(ResourcesManager.getString(R.string.delete_all_user))
            //            deleteAllUserDialog?.setCannotHide()
            deleteAllUserDialog?.setOnCommonSuccessCallback {
                deleteAllUserDialog?.hideStandardDialog()
                deleteAllUserDialog = null
            }
            deleteAllUserDialog?.setOnItemViewClickListener { position: Int, view: View? ->
                if (position == 0) {
                    deleteAllUserDialog?.hideStandardDialog()
                    deleteAllUserDialog = null
                } else {
                    deleteAllUserDialog?.hideStandardDialog()
                    deleteAllUserDialog = null
                    showLoading()
                    ThreadPoolManager.get().createSyncThreadPool {
                        queryUserList = userBeanDaoManager?.queryAll() ?: mutableListOf()
                        userBeanDaoManager?.deleteInTx(queryUserList)
                        queryUserList = userBeanDaoManager?.queryAll() ?: mutableListOf()
                        MainThreadManager {
                            if (queryUserList.size > 0) {
                                Collections.reverse(queryUserList)
                                userBeanAdapter?.clearData()
                                userBeanAdapter?.addData(queryUserList)
                                tevTitle?.let {
                                    TextViewStyleManager.setTextViewStyleVerticalCenter(
                                        it,
                                        ResourcesManager.getString(R.string.created_b)
                                                + queryUserList.size
                                                + ResourcesManager.getString(R.string.users_b),
                                        ResourcesManager.getString(R.string.created_b).length,
                                        ResourcesManager.getString(R.string.created_b).length + queryUserList.size.toString().length + 1,
                                        28f
                                    )
                                }
                            } else {
                                userBeanAdapter?.clearData()
                                tevTitle?.let {
                                    TextViewStyleManager.setTextViewStyleVerticalCenter(
                                        it,
                                        ResourcesManager.getString(R.string.created_b)
                                                + 0
                                                + ResourcesManager.getString(R.string.users_b),
                                        ResourcesManager.getString(R.string.created_b).length,
                                        ResourcesManager.getString(R.string.created_b).length + 2,
                                        28f
                                    )
                                }
                            }
                            hideLoading()
                        }.subThreadToUIThread()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        userBeanDaoManager?.closeConnection()
        ThreadPoolManager.get().shutdownNowSyncThreadPool()
        super.onDestroy()
    }

}