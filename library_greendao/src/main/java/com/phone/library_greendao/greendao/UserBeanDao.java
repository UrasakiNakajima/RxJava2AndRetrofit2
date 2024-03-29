package com.phone.library_greendao.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.phone.library_greendao.AddressBeanListConverter;
import java.util.List;

import com.phone.library_greendao.bean.UserBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER_BEAN".
*/
public class UserBeanDao extends AbstractDao<UserBean, Long> {

    public static final String TABLENAME = "USER_BEAN";

    /**
     * Properties of entity UserBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserId = new Property(1, String.class, "userId", false, "USER_ID");
        public final static Property Password = new Property(2, String.class, "password", false, "PASSWORD");
        public final static Property Birthday = new Property(3, String.class, "birthday", false, "BIRTHDAY");
        public final static Property Age = new Property(4, Integer.class, "age", false, "AGE");
        public final static Property Salary = new Property(5, Double.class, "salary", false, "SALARY");
        public final static Property AddressBeanList = new Property(6, String.class, "addressBeanList", false, "ADDRESS_BEAN_LIST");
    }

    private final AddressBeanListConverter addressBeanListConverter = new AddressBeanListConverter();

    public UserBeanDao(DaoConfig config) {
        super(config);
    }
    
    public UserBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"USER_ID\" TEXT," + // 1: userId
                "\"PASSWORD\" TEXT," + // 2: password
                "\"BIRTHDAY\" TEXT," + // 3: birthday
                "\"AGE\" INTEGER," + // 4: age
                "\"SALARY\" REAL," + // 5: salary
                "\"ADDRESS_BEAN_LIST\" TEXT);"); // 6: addressBeanList
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UserBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(2, userId);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(3, password);
        }
 
        String birthday = entity.getBirthday();
        if (birthday != null) {
            stmt.bindString(4, birthday);
        }
 
        Integer age = entity.getAge();
        if (age != null) {
            stmt.bindLong(5, age);
        }
 
        Double salary = entity.getSalary();
        if (salary != null) {
            stmt.bindDouble(6, salary);
        }
 
        List addressBeanList = entity.getAddressBeanList();
        if (addressBeanList != null) {
            stmt.bindString(7, addressBeanListConverter.convertToDatabaseValue(addressBeanList));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UserBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(2, userId);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(3, password);
        }
 
        String birthday = entity.getBirthday();
        if (birthday != null) {
            stmt.bindString(4, birthday);
        }
 
        Integer age = entity.getAge();
        if (age != null) {
            stmt.bindLong(5, age);
        }
 
        Double salary = entity.getSalary();
        if (salary != null) {
            stmt.bindDouble(6, salary);
        }
 
        List addressBeanList = entity.getAddressBeanList();
        if (addressBeanList != null) {
            stmt.bindString(7, addressBeanListConverter.convertToDatabaseValue(addressBeanList));
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public UserBean readEntity(Cursor cursor, int offset) {
        UserBean entity = new UserBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // userId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // password
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // birthday
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // age
            cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5), // salary
            cursor.isNull(offset + 6) ? null : addressBeanListConverter.convertToEntityProperty(cursor.getString(offset + 6)) // addressBeanList
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UserBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPassword(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setBirthday(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAge(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setSalary(cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5));
        entity.setAddressBeanList(cursor.isNull(offset + 6) ? null : addressBeanListConverter.convertToEntityProperty(cursor.getString(offset + 6)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(UserBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(UserBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(UserBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
