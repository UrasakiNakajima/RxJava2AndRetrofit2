package com.phone.library_greendao

import com.phone.library_base.BaseApplication
import com.phone.library_base.manager.LogManager
import com.phone.library_greendao.bean.UserBean
import com.phone.library_greendao.greendao.DaoMaster
import com.phone.library_greendao.greendao.DaoMaster.DevOpenHelper
import com.phone.library_greendao.greendao.DaoSession
import com.phone.library_greendao.greendao.UserBeanDao
import org.greenrobot.greendao.query.QueryBuilder

class UserBeanDaoManager {

    private val TAG = UserBeanDaoManager::class.java.simpleName
    private val DB_NAME = "user_bean.db"
    private var mDevOpenHelper: DevOpenHelper? = null
    private var daoSession: DaoSession? = null

    init {
        mDevOpenHelper = DevOpenHelper(BaseApplication.instance(), DB_NAME, null)
        val daoMaster =
            DaoMaster(mDevOpenHelper?.writableDatabase)
        daoSession = daoMaster.newSession()
    }

    /**
     * 完成User记录的插入，如果表未创建，先创建User表
     *
     * @param userBean
     * @return
     */
    fun insert(userBean: UserBean): Boolean {
        var flag = false
        flag = daoSession?.userBeanDao?.insert(userBean) != -1L
        LogManager.i(TAG, "insert User :$flag-->$userBean")
        return flag
    }

    /**
     * 完成User记录的插入，如果表未创建，先创建User表
     *
     * @param userBeanList
     * @return
     */
    fun insertInTx(userBeanList: List<UserBean>) {
        daoSession?.userBeanDao?.insertInTx(userBeanList)
    }

    /**
     * 修改一条数据
     *
     * @param userBean
     * @return
     */
    fun update(userBean: UserBean) {
        daoSession?.userBeanDao?.update(userBean)
    }

    /**
     * 修改多条数据
     *
     * @param userBeanList
     * @return
     */
    fun update(userBeanList: List<UserBean>) {
        daoSession?.userBeanDao?.updateInTx(userBeanList)
    }

    /**
     * 删除单条记录
     *
     * @param userBean
     * @return
     */
    fun delete(userBean: UserBean) {
        //按照id删除
        daoSession?.userBeanDao?.delete(userBean)
    }

    /**
     * 删除多条记录
     *
     * @param userBeanList
     */
    fun deleteInTx(userBeanList: List<UserBean>) {
        //按照id删除
        daoSession?.userBeanDao?.deleteInTx(userBeanList)
    }

    /**
     * 删除所有记录
     *
     * @return
     */
    fun deleteAll() {
        //按照id删除
        daoSession?.deleteAll(UserBean::class.java)
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    fun queryAll(): List<UserBean> {
        return daoSession?.userBeanDao?.loadAll() ?: mutableListOf()
    }

    /**
     * 根据主键id查询记录
     *
     * @param key
     * @return
     */
    fun queryById(key: Long): UserBean {
        return daoSession?.userBeanDao?.load(key) ?: UserBean()
    }

    /**
     * 使用native sql进行查询操作
     */
    fun queryByNativeSql(sql: String, conditions: Array<String>): List<UserBean> {
        return daoSession?.userBeanDao?.queryRaw(sql, *conditions) ?: mutableListOf()
    }

    /**
     * 使用queryBuilder进行查询
     *
     * @return
     */
    fun queryByQueryBuilder(userId: String): List<UserBean> {
        val queryBuilder = daoSession?.userBeanDao?.queryBuilder()
        return queryBuilder?.where(UserBeanDao.Properties.UserId.eq(userId))?.list()
            ?: mutableListOf()
    }

    /**
     * 打开输出日志，默认关闭
     */
    fun setDebug() {
        QueryBuilder.LOG_SQL = true
        QueryBuilder.LOG_VALUES = true
    }

    /**
     * 关闭所有的操作，数据库开启后，使用完毕要关闭
     */
    fun closeConnection() {
        closeHelper()
        closeDaoSession()
    }

    fun closeHelper() {
        mDevOpenHelper?.close()
        mDevOpenHelper = null
    }

    fun closeDaoSession() {
        daoSession?.clear()
        daoSession = null
    }

}