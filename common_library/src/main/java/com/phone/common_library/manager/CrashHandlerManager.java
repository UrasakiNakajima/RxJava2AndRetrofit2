package com.phone.common_library.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.phone.common_library.BaseApplication;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce : 造成App崩潰的異常管理類（在Application裏初始化，只要一有崩潰的異常就會被捕獲）
 */
public class CrashHandlerManager implements Thread.UncaughtExceptionHandler {

    public static final String TAG = CrashHandlerManager.class.getSimpleName();

    //系统默认的UncaughtException处理类 
    private final Thread.UncaughtExceptionHandler mDefaultHandler;
    private static CrashHandlerManager instance;

    /*使用Properties 来保存设备的信息和错误堆栈信息*/
    private final Properties mDeviceCrashInfo = new Properties();

    //存储设备信息
    private final Map<String, String> mDevInfoMap = new ArrayMap<>();
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private CrashHandlerManager() {
        //获得默认的handle
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //重新设置handle  设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 保证只有一个实例
     *
     * @return
     */
    public static CrashHandlerManager getInstance() {
        if (instance == null) {
            synchronized (CrashHandlerManager.class) {
                if (instance == null) {
                    instance = new CrashHandlerManager();
                }
            }
        }

        return instance;
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        //如果開發人員没有处理则让系统默认的异常处理器来处理
        if (!handleException(e) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(t, e);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
                LogManager.i(TAG, "error");
            }

            //结束程序
            ActivityPageManager.getInstance().exitApp2();
        }
    }

    /**
     * Throwable 包含了其线程创建时线程执行堆栈的快照
     * 收集設備信息和保存異常日誌到文件
     *
     * @param throwable
     * @return
     */
    public boolean handleException(Throwable throwable) {
        if (throwable == null) {
            return false;
        }

//        final String msg = throwable.getLocalizedMessage();
//        new Thread() {
//            public void run() {
//                Looper.prepare();
//                Toast.makeText(mContext, "msg" + msg, Toast.LENGTH_LONG).show();
//                Looper.loop();
//            }
//        }.start();

        //2.打印异常堆栈（推薦使用，讓系統把異常日誌打印出來）
        throwable.printStackTrace();
        //收集設備信息
        collectDeviceInfo();
        //保存異常日誌到文件
        saveCrashInfoToFile(throwable);
        //使用HTTP Post發送錯誤報告
        sendCrashReportsToServer();
        return true;
    }

    public void sendPreviousReportsToServer() {
        //使用HTTP Post發送錯誤報告
        sendCrashReportsToServer();
    }

    /**
     * 使用HTTP Post發送錯誤報告
     */
    private void sendCrashReportsToServer() {
        String[] crFiles = getCrashReportFiles();
        if (crFiles != null && crFiles.length > 0) {
            TreeSet<String> sortedFiles = new TreeSet<String>(Arrays.asList(crFiles));
            for (String fileName : sortedFiles) {
                File cr = new File(BaseApplication.getInstance().getFilesDir(), fileName);
                postReport(cr);
                //cr.delete(); 
            }
        }
    }

    private void postReport(File cr) {
        // TODO 使用HTTP Post 发送错误报告到服务器

    }

    private static final String CRASH_REPORTER_EXTENSION = ".cr";

    private String[] getCrashReportFiles() {
        File filesDir = BaseApplication.getInstance().getExternalCacheDir();
        FilenameFilter filter = new FilenameFilter() {

            public boolean accept(File dir, String filename) {
                return filename.endsWith(CRASH_REPORTER_EXTENSION);
            }
        };
        return filesDir.list(filter);
    }

    /**
     * 保存異常日誌到文件
     *
     * @param throwable
     * @return
     */
    private String saveCrashInfoToFile(Throwable throwable) {
        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<String, String> entry : mDevInfoMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            buffer.append(key).append("=").append(value).append('\n');
        }
        //可以用其回收在字符串缓冲区中的输出来构造字符串 
        Writer writer = new StringWriter();
        //向文本输出流打印对象的格式化表示形式 
        PrintWriter printWriter = new PrintWriter(writer);
        //将此 throwable 及其追踪输出至标准错误流 
        throwable.printStackTrace(printWriter);
        Throwable cause = throwable.getCause();
        while (cause != null) {
            //异常链 
            cause.printStackTrace();
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        buffer.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatdate.format(new Date(timestamp));

            String fileName = "crash-" + time + "-" + timestamp + ".txt";
            String path = BaseApplication.getInstance().getExternalCacheDir() + "/crash_xy/";//     /sdcard/crash/crash-time-timestamp.log
            File dirs = new File(path);
            if (!dirs.exists()) {
                dirs.mkdirs();
            }
            File file = new File(path, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

//                FileOutputStream trace = new FileOutputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            mDeviceCrashInfo.store(bufferedOutputStream, "");
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            //output 针对内存来说的 output到file中
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(buffer.toString().getBytes());
            bos.flush();
            bos.close();
            return fileName;
        } catch (Exception e) {
            LogManager.i(TAG, "an error occured while writing file...", e);
        }
        return null;
    }

    /**
     * 保存內存不足日誌到文件（在即將殺死App之前保存）
     *
     * @param info
     * @return
     */
    public String saveTrimMemoryInfoToFile(String info) {
        collectDeviceInfo();
        LogManager.i(TAG, "saveTrimMemoryInfoToFile");
        StringBuilder buffer = new StringBuilder();
        for (Map.Entry<String, String> entry : mDevInfoMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            buffer.append(key).append("=").append(value).append('\n');
        }

        buffer.append(info);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatdate.format(new Date(timestamp));

            String fileName = "aCrash-" + time + "-" + timestamp + ".txt";
            String path = BaseApplication.getInstance().getExternalCacheDir() + "/aCrash_xy/";//     /sdcard/crash/crash-time-timestamp.log
            File dirs = new File(path);
            if (!dirs.exists()) {
                dirs.mkdirs();
            }
            File file = new File(path, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            //output 针对内存来说的 output到file中
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(buffer.toString().getBytes());
            bos.flush();
            bos.close();
            return fileName;
        } catch (Exception e) {
            LogManager.i(TAG, "an error occured while writing file...", e);
        }
        return null;
    }

    /**
     * 收集設備信息
     */
    private void collectDeviceInfo() {
        try {
            PackageManager packageManager = BaseApplication.getInstance().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(BaseApplication.getInstance().getPackageName(), PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                String versionName = packageInfo.versionName == null ? "null" : packageInfo.versionName;
                String versionCode = packageInfo.versionCode + "";
                mDevInfoMap.put("versionName", versionName);
                mDevInfoMap.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogManager.i(TAG, "NameNotFoundException");
        }

//        //使用反射 获得Build类的所有类里的变量
//        //  Class   代表类在运行时的一个映射
//        //在Build类中包含各种设备信息,
//        // 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
//        // 具体信息请参考后面的截图
//        Field[] fields = Build.class.getDeclaredFields();
//        for (Field field : fields) {
//            try {
//                field.setAccessible(true);
//                //get方法返回指定对象上此 Field 表示的字段的值
//                mDevInfoMap.put(field.getName(), field.get(null).toString());
//                LogManager.i(TAG, field.getName() + ":" + field.get(null).toString());
//            } catch (Exception e) {
//                LogManager.i(TAG, "an error occured when collect crash info", e);
//            }
//        }

        mDevInfoMap.put("手机厂商", SystemManager.getDeviceBrand());
        mDevInfoMap.put("手机型号", SystemManager.getSystemModel());
        mDevInfoMap.put("手机当前系统语言", SystemManager.getSystemLanguage());
        mDevInfoMap.put("手机系统版本号", SystemManager.getSystemVersion());
        mDevInfoMap.put("手机CPU架构", SystemManager.getDeviceCpuAbi());

//        //手机厂商
//        SystemManager.getDeviceBrand();
//        //手机型号
//        SystemManager.getSystemModel();
//        //手机当前系统语言
//        SystemManager.getSystemLanguage();
//        //手机系统版本号
//        SystemManager.getSystemVersion();
//        //手机CPU架构
//        SystemManager.getDeviceCpuAbi();
//        //手機唯一識別碼
//        SystemManager.getSystemId(mContext);
    }

//    /**
//     * 使用HTTP服务之前，需要确定网络畅通，我们可以使用下面的方式判断网络是否可用
//     *
//     * @param context
//     * @return
//     */
//    public static boolean isNetworkAvailable(Context context) {
//        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo[] info = mgr.getAllNetworkInfo();
//        if (info != null) {
//            for (int i = 0; i < info.length; i++) {
//                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }


}
