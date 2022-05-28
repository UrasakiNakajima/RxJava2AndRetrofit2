package com.phone.common_library.manager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.UUID;

public class SystemIdManager {

    private static final String TAG = SystemIdManager.class.getSimpleName();
    //保存文件的路径
//    private static final String CACHE_IMAGE_DIR = "aray/cache/devices";
//    private static final String CACHE_IMAGE_DIR = "Android/data/systemId";
    private static final String CACHE_IMAGE_DIR = "Android/systemId";
    //保存的文件 采用隐藏文件的形式进行保存
    private static final String DEVICES_FILE_NAME = ".DEVICES";

    /**
     * 获取设备唯一标识符
     *
     * @param context
     * @return
     */
    public static String getSystemId(Context context) {
        //读取保存的在sd卡中的唯一标识符
        String systemId = readSystemId(context);
        //用于生成最终的唯一标识符
        StringBuffer s = new StringBuffer();
        //判断是否已经生成过,
        if (systemId != null && !"".equals(systemId)) {
            LogManager.i(TAG, "已经生成过");
            return systemId;
        }
        try {
            //获取IMES(也就是常说的SystemId)
            systemId = getIMIEStatus(context);
            s.append(systemId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //获取设备的MACAddress地址 去掉中间相隔的冒号
            systemId = getLocalMac(context).replace(":", "");
            s.append(systemId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //如果以上搜没有获取相应的则自己生成相应的UUID作为相应设备唯一标识符
        if (s == null || s.length() <= 0) {
            UUID uuid = UUID.randomUUID();
            systemId = uuid.toString().replace("-", "");
            s.append(systemId);
        }
        //为了统一格式对设备的唯一标识进行md5加密 最终生成32位字符串
        String md5 = getMD5(s.toString(), false);
        if (s.length() > 0) {
            //持久化操作, 进行保存到SD卡中
            saveSystemId(md5, context);
        } else {

        }
        return md5;
    }

    /**
     * 读取固定的文件中的内容,这里就是读取sd卡中保存的设备唯一标识符
     *
     * @param context
     * @return
     */
    public static String readSystemId(Context context) {
        File file = getSystemIdDir(context);
        if (file.exists()) {
            LogManager.i(TAG, "file.exists()");
            if (file.length() > 1) {
                LogManager.i(TAG, "file.length() > 1");
                StringBuffer buffer = new StringBuffer();
                try {
                    FileInputStream fis = new FileInputStream(file);
                    InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
                    Reader in = new BufferedReader(isr);
                    int i;
                    while ((i = in.read()) > -1) {
                        buffer.append((char) i);
                    }
                    in.close();
                    return buffer.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        } else {
            LogManager.i(TAG, "xfile.exists()");
            return null;
        }
    }

//    /**
//     * 获取设备的DeviceId(IMES) 这里需要相应的权限<br/>
//     * 需要 READ_PHONE_STATE 权限
//     *
//     * @param context
//     * @return
//     */
//    private static String getIMIEStatus(Context context) {
//        TelephonyManager tm = (TelephonyManager) context
//                .getSystemService(Context.TELEPHONY_SERVICE);
//        String systemId = tm.getDeviceId();
//        return systemId;
//    }

    /**
     * 获取设备的DeviceId(IMES) 这里需要相应的权限<br/>
     * 需要 READ_PHONE_STATE 权限
     *
     * @param context
     * @return
     */
    public static String getIMIEStatus(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String iMIEStatus = null;
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            iMIEStatus = telephonyManager.getDeviceId();
            LogManager.i(TAG, "iMIEStatus*****");
        }
        return iMIEStatus;
    }


    /**
     * 获取设备MAC 地址 由于 6.0 以后 WifiManager 得到的 MacAddress得到都是 相同的没有意义的内容
     * 所以采用以下方法获取Mac地址
     *
     * @param context
     * @return
     */
    private static String getLocalMac(Context context) {
//        WifiManager wifi = (WifiManager) context
//                .getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = wifi.getConnectionInfo();
//        return info.getMacAddress();


        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "";
            }
            byte[] addr = networkInterface.getHardwareAddress();


            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "";
        }
        return macAddress;
    }

    /**
     * 保存 内容到 SD卡中,  这里保存的就是 设备唯一标识符
     *
     * @param str
     * @param context
     */
    public static void saveSystemId(String str, Context context) {
        File file = getSystemIdDir(context);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            Writer out = new OutputStreamWriter(fos, "UTF-8");
            out.write(str);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对挺特定的 内容进行 md5 加密
     *
     * @param message   加密明文
     * @param upperCase 加密以后的字符串是是大写还是小写  true 大写  false 小写
     * @return
     */
    public static String getMD5(String message, boolean upperCase) {
        String md5str = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] input = message.getBytes();

            byte[] buff = md.digest(input);

            md5str = bytesToHex(buff, upperCase);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5str;
    }


    public static String bytesToHex(byte[] bytes, boolean upperCase) {
        StringBuffer md5str = new StringBuffer();
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        if (upperCase) {
            return md5str.toString().toUpperCase();
        }
        return md5str.toString().toLowerCase();
    }

    /**
     * 统一处理设备唯一标识 保存的文件的地址
     *
     * @param context
     * @return
     */
    private static File getSystemIdDir(Context context) {
        File file = null;
        File dirs = new File(Environment.getExternalStorageDirectory(), CACHE_IMAGE_DIR);
        if (!dirs.exists()) {
            LogManager.i(TAG, "!dir.exists()");
//            dir.mkdirs();

            //适配android 9
            boolean isCreateDirsSuccess = DocumentManager.mkdirs(context, dirs);
            LogManager.i(TAG, "getSystemIdDir isCreateDirsSuccess*****" + isCreateDirsSuccess);
        }

        file = new File(dirs, DEVICES_FILE_NAME); // 用DEVICES_FILE_NAME给文件命名
        if (file.exists()) {
            LogManager.i(TAG, "getSystemIdDir file.exists()");
        } else {
            LogManager.i(TAG, "getSystemIdDir !file.exists()");
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            //适配android 9
            boolean isCreateFileSuccess = DocumentManager.canWrite(file);
            LogManager.i(TAG, "getSystemIdDir isCreateFileSuccess*****" + isCreateFileSuccess);
        }
        return file;
    }

}
