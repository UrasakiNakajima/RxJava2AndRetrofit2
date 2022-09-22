package com.phone.common_library.manager;

import com.phone.common_library.bean.UserBean;
import com.phone.common_library.dao.DaoManager;
import com.phone.common_library.greendao.UserBeanDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class UserBeanDaoManager {

    private static final String TAG = UserBeanDaoManager.class.getSimpleName();
    private DaoManager daoManager;
    private volatile static UserBeanDaoManager userBeanDaoManager;

    private UserBeanDaoManager() {
        daoManager = DaoManager.getInstance();
    }

    public static UserBeanDaoManager getInstance() {
        if (userBeanDaoManager == null) {
            synchronized (UserBeanDaoManager.class) {
                if (userBeanDaoManager == null) {
                    userBeanDaoManager = new UserBeanDaoManager();
                }
            }
        }
        return userBeanDaoManager;
    }

    /**
     * 完成User记录的插入，如果表未创建，先创建User表
     *
     * @param userBean
     * @return
     */
    public boolean insert(UserBean userBean) {
        boolean flag = false;
        flag = daoManager.getDaoSession().getUserBeanDao().insert(userBean) == -1 ? false : true;
        LogManager.i(TAG, "insert User :" + flag + "-->" + userBean.toString());
        return flag;
    }

    /**
     * 修改一条数据
     *
     * @param userBean
     * @return
     */
    public boolean update(UserBean userBean) {
        boolean flag = false;
        try {
            daoManager.getDaoSession().getUserBeanDao().update(userBean);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     *
     * @param userBean
     * @return
     */
    public boolean delete(UserBean userBean) {
        boolean flag = false;
        try {
            //按照id删除
            daoManager.getDaoSession().getUserBeanDao().delete(userBean);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除多条记录
     *
     * @param drawInventoryBeanList
     * @return
     */
    public boolean deleteInTx(List<UserBean> drawInventoryBeanList) {
        boolean flag = false;
        try {
            //按照id删除
            daoManager.getDaoSession().getUserBeanDao().deleteInTx(drawInventoryBeanList);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除所有记录
     *
     * @return
     */
    public boolean deleteAll() {
        boolean flag = false;
        try {
            //按照id删除
            daoManager.getDaoSession().deleteAll(UserBean.class);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    public List<UserBean> queryAll() {
        return daoManager.getDaoSession().getUserBeanDao().loadAll();
    }

    /**
     * 根据主键id查询记录
     *
     * @param key
     * @return
     */
    public UserBean queryById(long key) {
        return daoManager.getDaoSession().getUserBeanDao().load(key);
    }

    /**
     * 使用native sql进行查询操作
     */
    public List<UserBean> queryByNativeSql(String sql, String[] conditions) {
        return daoManager.getDaoSession().getUserBeanDao().queryRaw(sql, conditions);
    }

    /**
     * 使用queryBuilder进行查询
     *
     * @return
     */
    public List<UserBean> queryByQueryBuilder(String userId) {
        QueryBuilder<UserBean> queryBuilder = daoManager.getDaoSession().getUserBeanDao().queryBuilder();
        return queryBuilder.where(UserBeanDao.Properties.UserId.eq(userId)).list();
    }

}
