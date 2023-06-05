package com.phone.library_common.manager

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.ArrayMap
import com.phone.library_common.BaseApplication
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce : 造成App崩潰的異常管理類（在Application裏初始化，只要一有崩潰的異常就會被捕獲）
 */
class CrashHandlerManager private constructor() : Thread.UncaughtExceptionHandler {

    val TAG = CrashHandlerManager::class.java.simpleName

    //系统默认的UncaughtException处理类
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null
    private var instance: CrashHandlerManager? = null

    /*使用Properties 来保存设备的信息和错误堆栈信息*/
    private val mDeviceCrashInfo = Properties()

    //存储设备信息
    private val mDevInfoMap: MutableMap<String, String> = ArrayMap()

    @SuppressLint("SimpleDateFormat")
    private val formatdate = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")

    init {
        //获得默认的handle
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        //重新设置handle  设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 保证只有一个实例
     *
     * @return
     */
    companion object {
        @Volatile
        private var instance: CrashHandlerManager? = null
            get() {
                if (field == null) {
                    field = CrashHandlerManager()
                }
                return field
            }

        //Synchronized添加后就是线程安全的的懒汉模式
        @Synchronized
        @JvmStatic
        fun instance(): CrashHandlerManager? {
            return instance
        }
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    override fun uncaughtException(t: Thread, e: Throwable) {
        //如果開發人員没有处理则让系统默认的异常处理器来处理
        if (!handleException(e) && mDefaultHandler != null) {
            mDefaultHandler?.uncaughtException(t, e)
        } else {
            try {
                Thread.sleep(2000)
            } catch (interruptedException: InterruptedException) {
                interruptedException.printStackTrace()
                LogManager.i(TAG, "error")
            }

            //结束程序
            ActivityPageManager.instance().exitApp()
        }
    }

    /**
     * Throwable 包含了其线程创建时线程执行堆栈的快照
     * 收集設備信息和保存異常日誌到文件
     *
     * @param throwable
     * @return
     */
    fun handleException(throwable: Throwable?): Boolean {
        if (throwable == null) {
            return false
        }

//        final String msg = throwable.getLocalizedMessage()
//        new Thread() {
//            public void run() {
//                Looper.prepare()
//                Toast.makeText(mContext, "msg" + msg, Toast.LENGTH_LONG).show()
//                Looper.loop()
//            }
//        }.start()

        //2.打印异常堆栈（推薦使用，讓系統把異常日誌打印出來）
        throwable.printStackTrace()
        //收集設備信息
        collectDeviceInfo()
        //保存異常日誌到文件
        saveCrashInfoToFile(throwable)
        //使用HTTP Post發送錯誤報告
        sendCrashReportsToServer()
        return true
    }

    fun sendPreviousReportsToServer() {
        //使用HTTP Post發送錯誤報告
        sendCrashReportsToServer()
    }

    /**
     * 使用HTTP Post發送錯誤報告
     */
    private fun sendCrashReportsToServer() {
        val crFiles = getCrashReportFiles()
        if (crFiles != null && crFiles.size > 0) {
            val sortedFiles = TreeSet(Arrays.asList(*crFiles))
            for (fileName in sortedFiles) {
                val cr = File(BaseApplication.instance().filesDir, fileName)
                postReport(cr)
                //cr.delete()
            }
        }
    }

    private fun postReport(cr: File) {
        // TODO 使用HTTP Post 发送错误报告到服务器
    }

    private val CRASH_REPORTER_EXTENSION = ".cr"

    private fun getCrashReportFiles(): Array<String>? {
        val filesDir = BaseApplication.instance().externalCacheDir
        filesDir?.let {
            val filter =
                FilenameFilter { dir, filename -> filename.endsWith(CRASH_REPORTER_EXTENSION) }
            return it.list(filter)
        }
        return null
    }

    /**
     * 保存異常日誌到文件
     *
     * @param throwable
     * @return
     */
    private fun saveCrashInfoToFile(throwable: Throwable): String? {
        val builder = StringBuilder()
        for ((key, value) in mDevInfoMap) {
            builder.append(key).append("=").append(value).append('\n')
        }
        //可以用其回收在字符串缓冲区中的输出来构造字符串
        val writer: Writer = StringWriter()
        //向文本输出流打印对象的格式化表示形式
        val printWriter = PrintWriter(writer)
        //将此 throwable 及其追踪输出至标准错误流
        throwable.printStackTrace(printWriter)
        var cause = throwable.cause
        while (cause != null) {
            //异常链
            cause.printStackTrace()
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        builder.append(result)
        try {
            val timestamp = System.currentTimeMillis()
            val time = formatdate.format(Date(timestamp))
            val fileName = "crash-$time-$timestamp.txt"
            val path =
                BaseApplication.instance().externalCacheDir?.absolutePath + File.separator + "crash_xy" //     /sdcard/crash/crash-time-timestamp.log
            val dirs = File(path)
            if (!dirs.exists()) {
                dirs.mkdirs()
            }
            val file = File(path, fileName)
            if (!file.exists()) {
                file.createNewFile()
            }

//                FileOutputStream trace = new FileOutputStream(file)
            val fileOutputStream = FileOutputStream(file)
            val bufferedOutputStream = BufferedOutputStream(fileOutputStream)
            mDeviceCrashInfo.store(bufferedOutputStream, "")
            bufferedOutputStream.flush()
            bufferedOutputStream.close()
            //output 针对内存来说的 output到file中
            val fos = FileOutputStream(file)
            val bos = BufferedOutputStream(fos)
            bos.write(builder.toString().toByteArray())
            bos.flush()
            bos.close()
            return fileName
        } catch (e: Exception) {
            LogManager.i(TAG, "an error occured while writing file...", e)
        }
        return null
    }

    /**
     * 保存內存不足日誌到文件（在即將殺死App之前保存）
     *
     * @param info
     * @return
     */
    fun saveTrimMemoryInfoToFile(info: String?): String? {
        collectDeviceInfo()
        LogManager.i(TAG, "saveTrimMemoryInfoToFile")
        val buffer = StringBuilder()
        for ((key, value) in mDevInfoMap) {
            buffer.append(key).append("=").append(value).append('\n')
        }
        buffer.append(info)
        try {
            val timestamp = System.currentTimeMillis()
            val time = formatdate.format(Date(timestamp))
            val fileName = "aCrash-$time-$timestamp.txt"
            val path =
                BaseApplication.instance().externalCacheDir?.absolutePath + File.separator + "aCrash_xy" //     /sdcard/crash/crash-time-timestamp.log
            val dirs = File(path)
            if (!dirs.exists()) {
                dirs.mkdirs()
            }
            val file = File(path, fileName)
            if (!file.exists()) {
                file.createNewFile()
            }

            //output 针对内存来说的 output到file中
            val fos = FileOutputStream(file)
            val bos = BufferedOutputStream(fos)
            bos.write(buffer.toString().toByteArray())
            bos.flush()
            bos.close()
            return fileName
        } catch (e: Exception) {
            LogManager.i(TAG, "an error occured while writing file...", e)
        }
        return null
    }

    /**
     * 收集設備信息
     */
    private fun collectDeviceInfo() {
        try {
            val packageManager = BaseApplication.instance().packageManager
            val packageInfo = packageManager.getPackageInfo(
                BaseApplication.instance().packageName,
                PackageManager.GET_ACTIVITIES
            )
            if (packageInfo != null) {
                val versionName =
                    if (packageInfo.versionName == null) "null" else packageInfo.versionName
                val versionCode = packageInfo.versionCode.toString() + ""
                mDevInfoMap["versionName"] = versionName
                mDevInfoMap["versionCode"] = versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            LogManager.i(TAG, "NameNotFoundException")
        }

//        //使用反射 获得Build类的所有类里的变量
//        //  Class   代表类在运行时的一个映射
//        //在Build类中包含各种设备信息,
//        // 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
//        // 具体信息请参考后面的截图
//        Field[] fields = Build.class.getDeclaredFields()
//        for (Field field : fields) {
//            try {
//                field.setAccessible(true)
//                //get方法返回指定对象上此 Field 表示的字段的值
//                mDevInfoMap.put(field.getName(), field.get(null).toString())
//                LogManager.i(TAG, field.getName() + ":" + field.get(null).toString())
//            } catch (Exception e) {
//                LogManager.i(TAG, "an error occured when collect crash info", e)
//            }
//        }

        SystemManager.getDeviceBrand()?.let {
            mDevInfoMap["手机厂商"] = it
        }
        SystemManager.getSystemModel()?.let {
            mDevInfoMap["手机型号"] = it
        }
        SystemManager.getSystemLanguage()?.let {
            mDevInfoMap["手机当前系统语言"] = it
        }
        SystemManager.getSystemVersion()?.let {
            mDevInfoMap["手机系统版本号"] = it
        }
        SystemManager.getSystemVersion()?.let {
            mDevInfoMap["手机CPU架构"] = it
        }

//        //手机厂商
//        SystemManager.getDeviceBrand()
//        //手机型号
//        SystemManager.getSystemModel()
//        //手机当前系统语言
//        SystemManager.getSystemLanguage()
//        //手机系统版本号
//        SystemManager.getSystemVersion()
//        //手机CPU架构
//        SystemManager.getDeviceCpuAbi()
//        //手機唯一識別碼
//        SystemManager.getSystemId(mContext)
    }

//    /**
//     * 使用HTTP服务之前，需要确定网络畅通，我们可以使用下面的方式判断网络是否可用
//     *
//     * @param context
//     * @return
//     */
//    public static boolean isNetworkAvailable(Context context) {
//        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)
//        NetworkInfo[] info = mgr.getAllNetworkInfo()
//        if (info != null) {
//            for (int i = 0 i < info.length i++) {
//                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                    return true
//                }
//            }
//        }
//        return false
//    }

}