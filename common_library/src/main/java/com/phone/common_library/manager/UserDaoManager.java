package com.phone.common_library.manager;

import com.phone.common_library.bean.User;
import com.phone.common_library.dao.DaoManager;
import com.phone.common_library.greendao.UserDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class UserDaoManager {

    private static final String TAG = UserDaoManager.class.getSimpleName();
    private DaoManager daoManager;
    private volatile static UserDaoManager userDaoManager;

    private UserDaoManager() {
        daoManager = DaoManager.getInstance();
    }

    public static UserDaoManager getInstance() {
        if (userDaoManager == null) {
            synchronized (UserDaoManager.class) {
                if (userDaoManager == null) {
                    userDaoManager = new UserDaoManager();
                }
            }
        }
        return userDaoManager;
    }

    /**
     * 完成User记录的插入，如果表未创建，先创建User表
     *
     * @param user
     * @return
     */
    public boolean insertUser(User user) {
        boolean flag = false;
        flag = daoManager.getDaoSession().getUserDao().insert(user) == -1 ? false : true;
        LogManager.i(TAG, "insert User :" + flag + "-->" + user.toString());
        return flag;
    }

    /**
     * 修改一条数据
     *
     * @param user
     * @return
     */
    public boolean updateUser(User user) {
        boolean flag = false;
        try {
            daoManager.getDaoSession().getUserDao().update(user);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     *
     * @param user
     * @return
     */
    public boolean deleteUser(User user) {
        boolean flag = false;
        try {
            //按照id删除
            daoManager.getDaoSession().getUserDao().delete(user);
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
    public boolean deleteUserInTx(List<User> drawInventoryBeanList) {
        boolean flag = false;
        try {
            //按照id删除
            daoManager.getDaoSession().getUserDao().deleteInTx(drawInventoryBeanList);
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
            daoManager.getDaoSession().deleteAll(User.class);
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
    public List<User> queryAllUser() {
        return daoManager.getDaoSession().getUserDao().loadAll();
    }

    /**
     * 根据主键id查询记录
     *
     * @param key
     * @return
     */
    public User queryUserById(long key) {
        return daoManager.getDaoSession().getUserDao().load(key);
    }

    /**
     * 使用native sql进行查询操作
     */
    public List<User> queryUserByNativeSql(String sql, String[] conditions) {
        return daoManager.getDaoSession().getUserDao().queryRaw(sql, conditions);
    }

    /**
     * 使用queryBuilder进行查询
     *
     * @return
     */
    public List<User> queryUserByQueryBuilder(String userId) {
        QueryBuilder<User> queryBuilder = daoManager.getDaoSession().getUserDao().queryBuilder();
        return queryBuilder.where(UserDao.Properties.UserId.eq(userId)).list();
    }

}
