package com.phone.module_square.ui

import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_common.base.BaseBindingRxAppActivity
import com.phone.library_common.bean.UserBean
import com.phone.library_common.common.ConstantData
import com.phone.library_common.dialog.StandardDialog
import com.phone.library_common.dialog.StandardUserDialog
import com.phone.library_common.manager.*
import com.phone.module_square.R
import com.phone.module_square.adapter.UserBeanAdapter
import com.phone.module_square.databinding.SquareActivityCreateUserBinding
import java.util.*

@Route(path = ConstantData.Route.ROUTE_CREATE_USER)
class CreateUserActivity : BaseBindingRxAppActivity<SquareActivityCreateUserBinding>() {

    private val TAG = CreateUserActivity::class.java.simpleName
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

    override fun initLayoutId() = R.layout.square_activity_create_user

    override fun initData() {
        userBeanDaoManager = UserBeanDaoManager()
    }

    override fun initViews() {
        setToolbar(false, R.color.color_FF198CFF)
        mDatabind.apply {
            imvBack.setColorFilter(ResourcesManager.getColor(R.color.white))
            layoutBack.setOnClickListener { v: View? -> finish() }
            tevCreateUser.setOnClickListener { v: View? -> showCreateUserDialog() }
            tevDeleteAllUser.setOnClickListener { v: View? -> showDeleteAllUserDialog() }
        }
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
        mDatabind.rcvUser.let {
            it.layoutManager = linearLayoutManager
            it.itemAnimator = DefaultItemAnimator()
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
            it.adapter = userBeanAdapter
        }
    }

    override fun initLoadData() {
        queryUserList()
    }

    override fun showLoading() {
        if (!mLoadView.isShown()) {
            mLoadView.setVisibility(View.VISIBLE)
            mLoadView.start()
        }
    }

    override fun hideLoading() {
        if (mLoadView.isShown()) {
            mLoadView.stop()
            mLoadView.setVisibility(View.GONE)
        }
    }

    private fun queryUserList() {
        showLoading()
        ThreadPoolManager.instance().createSyncThreadPool {
            queryUserList = userBeanDaoManager?.queryAll() ?: mutableListOf()
            LogManager.i(TAG, "queryUserList*****${queryUserList.size}")
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
                    userBeanAdapter?.let {
                        it.clearData()
                        it.addData(queryUserList)
                    }
                    mDatabind.tevTitle.let {
                        TextViewStyleManager.setTextViewStyleVerticalCenter(
                            it,
                            ResourcesManager.getString(R.string.created_b)
                                    + queryUserList.size
                                    + ResourcesManager.getString(R.string.users_b),
                            ResourcesManager.getString(R.string.created_b).length,
                            ResourcesManager.getString(R.string.created_b).length + queryUserList.size.toString().length,
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
                    mDatabind.tevTitle.let {
                        TextViewStyleManager.setTextViewStyleVerticalCenter(
                            it,
                            ResourcesManager.getString(R.string.created_b)
                                    + 0
                                    + ResourcesManager.getString(R.string.users_b),
                            ResourcesManager.getString(R.string.created_b).length,
                            ResourcesManager.getString(R.string.created_b).length + 1,
                            28f
                        )
                    }
                }
                hideLoading()
            }
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
                        userBeanDaoManager?.queryByQueryBuilder(success.userId!!)
                    if (userBeanList != null && userBeanList.size > 0 && userBeanList[0]
                            .userId == success.userId
                    ) {
                        showToast(
                            ResourcesManager.getString(R.string.this_user_has_been_added),
                            true
                        )
                    } else {
                        ThreadPoolManager.instance().createSyncThreadPool {
//                            List<UserBean> userBeanAddList = new ArrayList<>()
//                            for (int i = 0 i < 20000 i++) {
//                            val jsonStr =
//                                GsonManager().toJson(success)
//                            val userBean = GsonManager().convert(
//                                jsonStr,
//                                UserBean::class.java
//                            )
                            //                                userBeanAddList.add(userBean)
//                            }
//                            userBeanDaoManager?.insertInTx(userBeanAddList)
                            userBeanDaoManager?.insert(success)
                        }
                    }
                    ThreadPoolManager.instance().createSyncThreadPool {
                        queryUserList = userBeanDaoManager?.queryAll() ?: mutableListOf()
                        MainThreadManager {
                            if (queryUserList.size > 0) {
                                Collections.reverse(queryUserList)
                                userBeanAdapter?.clearData()
                                userBeanAdapter?.addData(queryUserList)
                                mDatabind.tevTitle.let {
                                    TextViewStyleManager.setTextViewStyleVerticalCenter(
                                        it,
                                        ResourcesManager.getString(R.string.created_b)
                                                + queryUserList.size
                                                + ResourcesManager.getString(R.string.users_b),
                                        ResourcesManager.getString(R.string.created_b).length,
                                        ResourcesManager.getString(R.string.created_b).length + queryUserList.size.toString().length,
                                        28f
                                    )
                                }
                            } else {
                                userBeanAdapter?.clearData()
                                mDatabind.tevTitle.let {
                                    TextViewStyleManager.setTextViewStyleVerticalCenter(
                                        it,
                                        ResourcesManager.getString(R.string.created_b)
                                                + 0
                                                + ResourcesManager.getString(R.string.users_b),
                                        ResourcesManager.getString(R.string.created_b).length,
                                        ResourcesManager.getString(R.string.created_b).length + 1,
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
                        userBeanDaoManager?.queryByQueryBuilder(success.userId!!)
                    if (userBeanList != null && userBeanList.size > 0 && userBeanList[0]
                            .userId == success.userId
                    ) {
                        success.id = userBeanList[0].id
                        ThreadPoolManager.instance().createSyncThreadPool {
                            userBeanDaoManager?.update(success)
                            MainThreadManager {
                                showToast(
                                    ResourcesManager.getString(R.string.this_user_has_modified),
                                    true
                                )
                            }

                        }
                    } else {
                        showToast(
                            ResourcesManager.getString(R.string.this_user_cannot_be_found),
                            true
                        )
                    }
                    ThreadPoolManager.instance().createSyncThreadPool {
                        queryUserList = userBeanDaoManager?.queryAll() ?: mutableListOf()
                        MainThreadManager {
                            if (queryUserList.size > 0) {
                                Collections.reverse(queryUserList)
                                userBeanAdapter?.clearData()
                                userBeanAdapter?.addData(queryUserList)
                                mDatabind.tevTitle.let {
                                    TextViewStyleManager.setTextViewStyleVerticalCenter(
                                        it,
                                        ResourcesManager.getString(R.string.created_b)
                                                + queryUserList.size
                                                + ResourcesManager.getString(R.string.users_b),
                                        ResourcesManager.getString(R.string.created_b).length,
                                        ResourcesManager.getString(R.string.created_b).length + queryUserList.size.toString().length,
                                        28f
                                    )
                                }
                            } else {
                                userBeanAdapter?.clearData()
                                mDatabind.tevTitle.let {
                                    TextViewStyleManager.setTextViewStyleVerticalCenter(
                                        it,
                                        ResourcesManager.getString(R.string.created_b)
                                                + 0
                                                + ResourcesManager.getString(R.string.users_b),
                                        ResourcesManager.getString(R.string.created_b).length,
                                        ResourcesManager.getString(R.string.created_b).length + 1,
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
                    ThreadPoolManager.instance().createSyncThreadPool {
                        userBeanDaoManager?.delete(
                            userBeanAdapter?.userBeanList?.get(position) ?: UserBean()
                        )
                        queryUserList = userBeanDaoManager?.queryAll() ?: mutableListOf()
                        MainThreadManager {
                            if (queryUserList.size > 0) {
                                Collections.reverse(queryUserList)
                                userBeanAdapter?.clearData()
                                userBeanAdapter?.addData(queryUserList)
                                mDatabind.tevTitle.let {
                                    TextViewStyleManager.setTextViewStyleVerticalCenter(
                                        it,
                                        ResourcesManager.getString(R.string.created_b)
                                                + queryUserList.size
                                                + ResourcesManager.getString(R.string.users_b),
                                        ResourcesManager.getString(R.string.created_b).length,
                                        ResourcesManager.getString(R.string.created_b).length + queryUserList.size.toString().length,
                                        28f
                                    )
                                }
                            } else {
                                userBeanAdapter?.clearData()
                                mDatabind.tevTitle.let {
                                    TextViewStyleManager.setTextViewStyleVerticalCenter(
                                        it,
                                        ResourcesManager.getString(R.string.created_b)
                                                + 0
                                                + ResourcesManager.getString(R.string.users_b),
                                        ResourcesManager.getString(R.string.created_b).length,
                                        ResourcesManager.getString(R.string.created_b).length + 1,
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
                    ThreadPoolManager.instance().createSyncThreadPool {
                        queryUserList = userBeanDaoManager?.queryAll() ?: mutableListOf()
                        userBeanDaoManager?.deleteInTx(queryUserList)
                        queryUserList = userBeanDaoManager?.queryAll() ?: mutableListOf()
                        MainThreadManager {
                            if (queryUserList.size > 0) {
                                Collections.reverse(queryUserList)
                                userBeanAdapter?.clearData()
                                userBeanAdapter?.addData(queryUserList)
                                mDatabind.tevTitle.let {
                                    TextViewStyleManager.setTextViewStyleVerticalCenter(
                                        it,
                                        ResourcesManager.getString(R.string.created_b)
                                                + queryUserList.size
                                                + ResourcesManager.getString(R.string.users_b),
                                        ResourcesManager.getString(R.string.created_b).length,
                                        ResourcesManager.getString(R.string.created_b).length + queryUserList.size.toString().length,
                                        28f
                                    )
                                }
                            } else {
                                userBeanAdapter?.clearData()
                                mDatabind.tevTitle.let {
                                    TextViewStyleManager.setTextViewStyleVerticalCenter(
                                        it,
                                        ResourcesManager.getString(R.string.created_b)
                                                + 0
                                                + ResourcesManager.getString(R.string.users_b),
                                        ResourcesManager.getString(R.string.created_b).length,
                                        ResourcesManager.getString(R.string.created_b).length + 1,
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

    override fun onDestroy() {
        userBeanDaoManager?.closeConnection()
        ThreadPoolManager.instance().shutdownNowSyncThreadPool()
        super.onDestroy()
    }

}