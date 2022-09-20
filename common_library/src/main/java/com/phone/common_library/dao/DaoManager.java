package com.phone.common_library.dao;

import com.phone.common_library.BaseApplication;
import com.phone.common_library.greendao.DaoMaster;
import com.phone.common_library.greendao.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

public class DaoManager {

    private static final String TAG = DaoManager.class.getSimpleName();
    private static final String DB_NAME = "user.db";
//    private Context context;

    private DaoMaster.DevOpenHelper devOpenHelper;
    private DaoMaster daoMaster;
    private static DaoSession daoSession;
    //多线程中要被共享的使用volatile关键字修饰
    private volatile static DaoManager daoManager;

    public DaoManager() {
        devOpenHelper = new DaoMaster.DevOpenHelper(BaseApplication.getInstance(), DB_NAME, null);
        daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
    }

    /**
     * 单例模式获得操作数据库对象
     *
     * @return
     */
    public static DaoManager getInstance() {
        if (daoManager == null) {
            synchronized (DaoManager.class) {
                if (daoManager == null) {
                    daoManager = new DaoManager();
                }
            }
        }
        return daoManager;
    }

    /**
     * 完成对数据库的添加、删除、修改、查询操作，仅仅是一个接口
     *
     * @return
     */
    public DaoSession getDaoSession() {
        if (daoSession == null) {
            daoSession = daoMaster.newSession();
        }
        return daoSession;
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
