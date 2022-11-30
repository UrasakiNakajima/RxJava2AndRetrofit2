package com.phone.library_common.manager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import com.phone.library_common.BaseApplication
import com.phone.library_common.manager.LogManager.i
import java.io.*
import java.net.NetworkInterface
import java.net.SocketException
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*

object SystemIdManager {

    private val TAG = SystemIdManager::class.java.simpleName

    //保存文件的路径
    //    private static final String CACHE_IMAGE_DIR = "aray/cache/devices";
    //    private static final String CACHE_IMAGE_DIR = "Android/data/systemId";
    private val CACHE_IMAGE_DIR = "Android/systemId"

    //保存的文件 采用隐藏文件的形式进行保存
    private val DEVICES_FILE_NAME = ".DEVICES"

    /**
     * 获取设备唯一标识符
     *
     * @return
     */
    fun getSystemId(): String {
        //读取保存的在sd卡中的唯一标识符
        var systemId = readSystemId()
        //用于生成最终的唯一标识符
        val stringBuilder = StringBuilder()
        //判断是否已经生成过,
        if (!TextUtils.isEmpty(systemId)) {
            i(TAG, "已经生成过")
            return systemId
        }
        try {
            //获取IMES(也就是常说的SystemId)
            systemId = getIMIEStatus()
            stringBuilder.append(systemId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            //获取设备的MACAddress地址 去掉中间相隔的冒号
            systemId = getLocalMac().replace(":", "")
            stringBuilder.append(systemId)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //如果以上搜没有获取相应的则自己生成相应的UUID作为相应设备唯一标识符
        if (stringBuilder.length <= 0) {
            val uuid = UUID.randomUUID()
            systemId = uuid.toString().replace("-", "")
            stringBuilder.append(systemId)
        }
        //为了统一格式对设备的唯一标识进行md5加密 最终生成32位字符串
        val md5 = getMD5(stringBuilder.toString(), false)
        if (stringBuilder.length > 0) {
            //持久化操作, 进行保存到SD卡中
            saveSystemId(md5)
        }
        return md5
    }

    /**
     * 读取固定的文件中的内容,这里就是读取sd卡中保存的设备唯一标识符
     *
     * @return
     */
    fun readSystemId(): String {
        val file = getSystemIdDir()
        return if (file.exists()) {
            i(TAG, "file.exists()")
            if (file.length() > 1) {
                i(TAG, "file.length() > 1")
                val buffer = StringBuilder()
                try {
                    val fis = FileInputStream(file)
                    val isr = InputStreamReader(fis, "UTF-8")
                    val `in`: Reader = BufferedReader(isr)
                    var i: Int
                    while (`in`.read().also { i = it } > -1) {
                        buffer.append(i.toChar())
                    }
                    `in`.close()
                    buffer.toString()
                } catch (e: IOException) {
                    e.printStackTrace()
                    ""
                }
            } else {
                ""
            }
        } else {
            i(TAG, "xfile.exists()")
            ""
        }
    }

    /**
     * 获取手机IMEI（需要“android.permission.READ_PHONE_STATE”权限）
     * 需要 READ_PHONE_STATE 权限
     *
     * @return
     */
    fun getIMIEStatus(): String {
        val telephonyManager = BaseApplication.get()
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var iMIEStatus: String? = null
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(
                    BaseApplication.get(),
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return ""
            }
            iMIEStatus = telephonyManager.deviceId
            i(TAG, "iMIEStatus*****$iMIEStatus")
        }
        return iMIEStatus ?: ""
    }


    /**
     * 获取设备MAC 地址 由于 6.0 以后 WifiManager 得到的 MacAddress得到都是 相同的没有意义的内容
     * 所以采用以下方法获取Mac地址
     *
     * @return
     */
    private fun getLocalMac(): String {
//        WifiManager wifi = (WifiManager) context
//                .getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = wifi.getConnectionInfo();
//        return info.getMacAddress();
        var macAddress: String? = null
        val buffer = StringBuffer()
        var networkInterface: NetworkInterface? = null
        try {
            networkInterface = NetworkInterface.getByName("eth1")
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0")
            }
            if (networkInterface == null) {
                return ""
            }
            val addr = networkInterface.hardwareAddress
            for (b in addr) {
                buffer.append(String.format("%02X:", b))
            }
            if (buffer.length > 0) {
                buffer.deleteCharAt(buffer.length - 1)
            }
            macAddress = buffer.toString()
        } catch (e: SocketException) {
            e.printStackTrace()
            return ""
        }
        return macAddress
    }

    /**
     * 保存 内容到 SD卡中,  这里保存的就是 设备唯一标识符
     *
     * @param str
     */
    fun saveSystemId(str: String) {
        val file = getSystemIdDir()
        try {
            val fos = FileOutputStream(file)
            val out: Writer = OutputStreamWriter(fos, StandardCharsets.UTF_8)
            out.write(str)
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 对挺特定的 内容进行 md5 加密
     *
     * @param message   加密明文
     * @param upperCase 加密以后的字符串是是大写还是小写  true 大写  false 小写
     * @return
     */
    fun getMD5(message: String, upperCase: Boolean): String {
        var md5str = ""
        try {
            val md = MessageDigest.getInstance("MD5")
            val input = message.toByteArray()
            val buff = md.digest(input)
            md5str = bytesToHex(buff, upperCase)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return md5str
    }

    fun bytesToHex(bytes: ByteArray, upperCase: Boolean): String {
        val md5str = StringBuilder()
        var digital: Int
        for (aByte in bytes) {
            digital = aByte.toInt()
            if (digital < 0) {
                digital += 256
            }
            if (digital < 16) {
                md5str.append("0")
            }
            md5str.append(Integer.toHexString(digital))
        }
        return if (upperCase) {
            md5str.toString().toUpperCase()
        } else md5str.toString().toLowerCase()
    }

    /**
     * 统一处理设备唯一标识 保存的文件的地址
     *
     * @return
     */
    private fun getSystemIdDir(): File {
        val file: File
        val dirs = File(BaseApplication.get().externalCacheDir, CACHE_IMAGE_DIR)
        if (!dirs.exists()) {
            i(TAG, "!dir.exists()")
            //            dir.mkdirs();

            //适配android 9
            val isCreateDirsSuccess: Boolean = DocumentManager.mkdirs(dirs)
            i(
                TAG,
                "getSystemIdDir isCreateDirsSuccess*****$isCreateDirsSuccess"
            )
        }
        file = File(dirs, DEVICES_FILE_NAME) // 用DEVICES_FILE_NAME给文件命名
        if (file.exists()) {
            i(TAG, "getSystemIdDir file.exists()")
        } else {
            i(TAG, "getSystemIdDir !file.exists()")
            //            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            //适配android 9
            val isCreateFileSuccess: Boolean = DocumentManager.canWrite(file)
            i(
                TAG,
                "getSystemIdDir isCreateFileSuccess*****$isCreateFileSuccess"
            )
        }
        return file
    }

}