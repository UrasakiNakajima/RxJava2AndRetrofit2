package com.phone.library_common.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.phone.library_common.BaseApplication
import com.phone.library_common.BuildConfig
import com.phone.library_common.JavaGetData
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.SharedPreferencesManager
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteDatabaseHook
import net.sqlcipher.database.SupportFactory

@Database(entities = [Book::class], version = 2)
abstract class AppRoomDataBase : RoomDatabase() {
    //创建DAO的抽象类
    abstract fun bookDao(): BookDao

    companion object {
        private val TAG = AppRoomDataBase::class.java.simpleName

        val DATABASE_ENCRYPT_KEY =
            JavaGetData.nativeDatabaseEncryptKey(BaseApplication.get(), BuildConfig.IS_RELEASE)
        val passphrase = SQLiteDatabase.getBytes(DATABASE_ENCRYPT_KEY.toCharArray())
        val factory = SupportFactory(passphrase, object : SQLiteDatabaseHook {
            override fun preKey(database: SQLiteDatabase?) {
            }

            override fun postKey(database: SQLiteDatabase?) {
//                database?.execSQL("PRAGMA cipher_page_size = 1024")
//                database?.execSQL("PRAGMA kdf_iter = 64000")
//                database?.execSQL("PRAGMA cipher_hmac_algorithm = HMAC_SHA1")
//                database?.execSQL("PRAGMA cipher_kdf_algorithm = PBKDF2_HMAC_SHA1")
            }
        }, true)

        const val DATABASE_NAME = "simple_app.db"
        const val DATABASE_ENCRYPT_NAME = "simple_encrypt_app.db"
        const val DATABASE_DECRYPT_NAME = "simple_decrypt_app.db"


        @Volatile
        private var databaseInstance: AppRoomDataBase? = null

        @Synchronized
        @JvmStatic
        fun get(): AppRoomDataBase {
            if (databaseInstance == null) {
//                databaseInstance = Room.databaseBuilder(
//                    BaseApplication.get(),
//                    AppRoomDataBase::class.java,
//                    DATABASE_NAME
//                )
//                    .allowMainThreadQueries()//允许在主线程操作数据库，一般不推荐；设置这个后主线程调用增删改查不会报错，否则会报错
////                    .openHelperFactory(factory)
//                    .build()


                databaseInstance = Room.databaseBuilder(
                    BaseApplication.get(),
                    AppRoomDataBase::class.java,
                    DATABASE_ENCRYPT_NAME
                )
                    .allowMainThreadQueries()//允许在主线程操作数据库，一般不推荐；设置这个后主线程调用增删改查不会报错，否则会报错
                    .openHelperFactory(factory)
                    .build()

                val dataEncryptTimes = SharedPreferencesManager.get("dataEncryptTimes", "0")
                if ("0".equals(dataEncryptTimes)) {
                    encrypt(
                        DATABASE_ENCRYPT_NAME,
                        DATABASE_NAME,
                        DATABASE_ENCRYPT_KEY
                    )
                    SharedPreferencesManager.put("dataEncryptTimes", "1")
                }
            }
            return databaseInstance!!
        }


        /**
         * 加密数据库
         * @param encryptedName 加密后的数据库名称
         * @param name 要加密的数据库名称
         * @param key 密码
         */
        @JvmStatic
        fun encrypt(encryptedName: String, name: String, key: String) {
            try {
                val databaseFile = BaseApplication.get().getDatabasePath(name)
                LogManager.i(TAG, "databaseFile*****${databaseFile.absolutePath}")
                val database: SQLiteDatabase =
                    SQLiteDatabase.openOrCreateDatabase(databaseFile, "", null) //打开要加密的数据库

                /*String passwordString = "123456"; //只能对已加密的数据库修改密码，且无法直接修改为“”或null的密码
                database.changePassword(passwordString.toCharArray());*/
                val encrypteddatabaseFile =
                    BaseApplication.get().getDatabasePath(encryptedName) //新建加密后的数据库文件
                LogManager.i(TAG, "encrypteddatabaseFile*****${encrypteddatabaseFile.absolutePath}")
                //deleteDatabase(SDcardPath + encryptedName);

                //连接到加密后的数据库，并设置密码
                database.rawExecSQL(
                    String.format(
                        "ATTACH DATABASE '%s' as " + encryptedName.split(".")[0] + " KEY '" + key + "';",
                        encrypteddatabaseFile.getAbsolutePath()
                    )
                )
                //输出要加密的数据库表和数据到加密后的数据库文件中
                database.rawExecSQL("SELECT sqlcipher_export('" + encryptedName.split(".")[0] + "');")
                //断开同加密后的数据库的连接
                database.rawExecSQL("DETACH DATABASE " + encryptedName.split(".")[0] + ";")

                //打开加密后的数据库，测试数据库是否加密成功
                val encrypteddatabase: SQLiteDatabase =
                    SQLiteDatabase.openOrCreateDatabase(encrypteddatabaseFile, key, null)
                //encrypteddatabase.setVersion(database.getVersion());
                encrypteddatabase.close() //关闭数据库
                database.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * 解密数据库
         * @param decryptedName 解密后的数据库名称
         * @param name 要解密的数据库名称
         * @param key 密码
         */
        @JvmStatic
        fun decrypt(decryptedName: String, name: String, key: String) {
            try {
                val databaseFile = BaseApplication.get().getDatabasePath(name)
                val database: SQLiteDatabase =
                    SQLiteDatabase.openOrCreateDatabase(databaseFile, key, null)
                val decryptedDatabaseFile = BaseApplication.get().getDatabasePath(decryptedName)
                //deleteDatabase(SDcardPath + decryptedName);

                //连接到解密后的数据库，并设置密码为空
                database.rawExecSQL(
                    String.format(
                        "ATTACH DATABASE '%s' as " + decryptedName.split(".")[0] + " KEY '';",
                        decryptedDatabaseFile.getAbsolutePath()
                    )
                )
                database.rawExecSQL("SELECT sqlcipher_export('" + decryptedName.split(".")[0] + "');")
                database.rawExecSQL("DETACH DATABASE " + decryptedName.split(".")[0] + ";")

                val decrypteddatabase: SQLiteDatabase =
                    SQLiteDatabase.openOrCreateDatabase(decryptedDatabaseFile, "", null)
                //decrypteddatabase.setVersion(database.getVersion());
                decrypteddatabase.close()
                database.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


}
