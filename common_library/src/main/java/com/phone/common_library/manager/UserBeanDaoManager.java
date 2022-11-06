package com.phone.common_library.manager;

import com.phone.common_library.BaseApplication;
import com.phone.common_library.bean.UserBean;
import com.phone.common_library.greendao.DaoMaster;
import com.phone.common_library.greendao.DaoSession;
import com.phone.common_library.greendao.UserBeanDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class UserBeanDaoManager {

    private static final String TAG = UserBeanDaoManager.class.getSimpleName();
    private static final String DB_NAME = "user_bean.db";
    private DaoMaster.DevOpenHelper devOpenHelper;
    private DaoMaster daoMaster;
    private  DaoSession daoSession;

    public UserBeanDaoManager() {
        devOpenHelper = new DaoMaster.DevOpenHelper(BaseApplication.getInstance(), DB_NAME, null);
        daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }

    /**
     * 完成User记录的插入，如果表未创建，先创建User表
     *
     * @param userBean
     * @return
     */
    public boolean insert(UserBean userBean) {
        boolean flag = false;
        flag = daoSession.getUserBeanDao().insert(userBean) == -1 ? false : true;
        LogManager.i(TAG, "insert User :" + flag + "-->" + userBean.toString());
        return flag;
    }

    /**
     * 完成User记录的插入，如果表未创建，先创建User表
     *
     * @param userBeanList
     * @return
     */
    public void insertInTx(List<UserBean> userBeanList) {
        daoSession.getUserBeanDao().insertInTx(userBeanList);
    }

    /**
     * 修改一条数据
     *
     * @param userBean
     * @return
     */
    public void update(UserBean userBean) {
        daoSession.getUserBeanDao().update(userBean);
    }

    /**
     * 修改一条数据
     *
     * @param userBeanList
     * @return
     */
    public void update(List<UserBean> userBeanList) {
        daoSession.getUserBeanDao().updateInTx(userBeanList);
    }

    /**
     * 删除单条记录
     *
     * @param userBean
     * @return
     */
    public void delete(UserBean userBean) {
        //按照id删除
        daoSession.getUserBeanDao().delete(userBean);
    }

    /**
     * 删除多条记录
     *
     * @param userBeanList
     */
    public void deleteInTx(List<UserBean> userBeanList) {
        //按照id删除
        daoSession.getUserBeanDao().deleteInTx(userBeanList);
    }

    /**
     * 删除所有记录
     *
     * @return
     */
    public void deleteAll() {
        //按照id删除
        daoSession.deleteAll(UserBean.class);
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    public List<UserBean> queryAll() {
        if (daoSession.getUserBeanDao().loadAll() == null){
            return new ArrayList<>();
        }
        return daoSession.getUserBeanDao().loadAll();
    }

    /**
     * 根据主键id查询记录
     *
     * @param key
     * @return
     */
    public UserBean queryById(long key) {
        return daoSession.getUserBeanDao().load(key);
    }

    /**
     * 使用native sql进行查询操作
     */
    public List<UserBean> queryByNativeSql(String sql, String[] conditions) {
        return daoSession.getUserBeanDao().queryRaw(sql, conditions);
    }

    /**
     * 使用queryBuilder进行查询
     *
     * @return
     */
    public List<UserBean> queryByQueryBuilder(String userId) {
        QueryBuilder<UserBean> queryBuilder = daoSession.getUserBeanDao().queryBuilder();
        return queryBuilder.where(UserBeanDao.Properties.UserId.eq(userId)).list();
    }

    /**
     * 打开输出日志，默认关闭
     */
    public void setDebug() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    /**
     * 关闭所有的操作，数据库开启后，使用完毕要关闭
     */
    public void closeConnection() {
        closeHelper();
        closeDaoSession();
    }

    public void closeHelper() {
        if (devOpenHelper != null) {
            devOpenHelper.close();
            devOpenHelper = null;
        }
    }

    public void closeDaoSession() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
    }

}
