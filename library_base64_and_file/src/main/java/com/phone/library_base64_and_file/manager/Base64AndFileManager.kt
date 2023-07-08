package com.phone.library_base64_and_file.manager

import Decoder.BASE64Decoder
import Decoder.BASE64Encoder
import android.util.Base64
import com.phone.library_base.manager.LogManager
import java.io.*
import java.util.*

object Base64AndFileManager {

    private val TAG = Base64AndFileManager::class.java.simpleName

    /**
     * @param base64Content
     * @param dirsPath
     * @param fileName
     * @return
     */
    fun base64ToFile(base64Content: String, dirsPath: String, fileName: String): File {
        val decoder = BASE64Decoder()
        val bais: ByteArrayInputStream
        var bis: BufferedInputStream? = null
        val fos: FileOutputStream
        var bos: BufferedOutputStream? = null
        var file: File? = null
        try {
            val dirs = File(dirsPath)
            if (!dirs.exists()) {
                dirs.mkdirs()
            }
            file = File(dirs, fileName)
            if (!file.exists()) {
                file.createNewFile()
            }

            //base64编码内容转换为字节数组
            val bytes = decoder.decodeBuffer(base64Content)
            bais = ByteArrayInputStream(bytes)
            bis = BufferedInputStream(bais)
            fos = FileOutputStream(file)
            bos = BufferedOutputStream(fos)
            val buffer = ByteArray(1024 * 8)
            var len = 0
            while (bis.read(buffer).also { len = it } != -1) {
                bos.write(buffer, 0, len)
            }
            bos.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                bis?.close()
                bos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return file!!
    }


    /**
     * @param is
     * @return
     */
    fun fileToBase64(`is`: InputStream): String {
        val encoder = BASE64Encoder()
        var bytes: ByteArray? = null
        //        FileInputStream fis = null;
        var bis: BufferedInputStream? = null
        var baos: ByteArrayOutputStream? = null
        //        BufferedOutputStream bos = null;
        try {
//            fis = new FileInputStream(file);
            bis = BufferedInputStream(`is`)
            baos = ByteArrayOutputStream()
            //            bos = new BufferedOutputStream(baos);
            val buffer = ByteArray(1024 * 8)
            var len = 0
            while (bis.read(buffer).also { len = it } != -1) {
                baos.write(buffer, 0, len)
            }
            //刷新此输出流并强制写出所有缓冲的输出字节
            baos.flush()
            bytes = baos.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                bis?.close()
                baos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return encoder.encodeBuffer(bytes).trim { it <= ' ' }
    }

    /**
     * @param file
     * @return
     */
    fun fileToBase64(file: File?): String {
        val encoder = BASE64Encoder()
        var bytes: ByteArray? = null
        var fis: FileInputStream? = null
        var bis: BufferedInputStream? = null
        var baos: ByteArrayOutputStream? = null
        //        BufferedOutputStream bos = null;
        try {
            fis = FileInputStream(file)
            bis = BufferedInputStream(fis)
            baos = ByteArrayOutputStream()
            //            bos = new BufferedOutputStream(baos);
            val buffer = ByteArray(1024 * 8)
            var len = 0
            while (bis.read(buffer).also { len = it } != -1) {
                baos.write(buffer, 0, len)
            }
            //刷新此输出流并强制写出所有缓冲的输出字节
            baos.flush()
            bytes = baos.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                bis?.close()
                baos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return encoder.encodeBuffer(bytes).trim { it <= ' ' }
    }

    /**
     * @param file
     * @return
     */
    fun fileToBase64Test(file: File?): String {
        val encoder = BASE64Encoder()
        var bytes: ByteArray? = null
        var fis: FileInputStream? = null
        var bis: BufferedInputStream? = null
        var baos: ByteArrayOutputStream? = null
        //        BufferedOutputStream bos = null;
        try {
            fis = FileInputStream(file)
            bis = BufferedInputStream(fis)
            baos = ByteArrayOutputStream()
            //            bos = new BufferedOutputStream(baos);
            val buffer = ByteArray(1024 * 8)
            var len = 0
            while (bis.read(buffer).also { len = it } != -1) {
                baos.write(buffer, 0, len)
            }
            //刷新此输出流并强制写出所有缓冲的输出字节
            baos.flush()
            bytes = baos.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                bis?.close()
                baos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        LogManager.i(TAG, "bytes.length******" + bytes!!.size)
        val byteList: MutableList<ByteArray?> = ArrayList()
        if (bytes.size > 10) {
            val byte1 = bytes.size / 10
            for (i in 0..9) {
                var sub1: ByteArray? = null
                sub1 = if (i == 0) {
                    Arrays.copyOf(bytes, byte1 * (i + 1))
                } else {
                    Arrays.copyOfRange(bytes, byte1 * i, byte1 * (i + 1))
                }
                byteList.add(sub1)
            }
            val byte2 = bytes.size % 10
            if (byte2 > 0) {
                val sub2 = Arrays.copyOf(bytes, byte1 * (9 + 1))
                byteList.add(sub2)
            }
        }
        //        byte[] sub1 = Arrays.copyOf(bytes, 3);
//        byte[] sub2 = Arrays.copyOfRange(bytes, 3, bytes.length);

//        String[] str = {"112", "2321", "3231", "4443", "5321"};
//        String[] sub1 = Arrays.copyOf(str, 3);
//        String[] sub2 = Arrays.copyOfRange(str, 3, str.length);
//        LogManager.i(TAG, "Arrays.deepToString(sub1)******" + Arrays.deepToString(sub1));
//        LogManager.i(TAG, "Arrays.deepToString(sub2)******" + Arrays.deepToString(sub2));
        var base64Str: String? = null
        if (byteList.size > 0) {
            base64Str = ""
            for (i in byteList.indices) {
                base64Str += encoder.encodeBuffer(byteList[i]).trim { it <= ' ' }
            }
        } else {
            base64Str = encoder.encodeBuffer(bytes).trim { it <= ' ' }
        }
        return base64Str
    }

    /**
     * @param base64Content
     * @param dirsPath
     * @return
     */
    fun base64ToFileSecond(base64Content: String, dirsPath: String, fileName: String): File? {
        var bais: ByteArrayInputStream? = null
        var bis: BufferedInputStream? = null
        var fos: FileOutputStream? = null
        var bos: BufferedOutputStream? = null
        var file: File? = null
        try {
            val dirs = File(dirsPath)
            if (!dirs.exists()) {
                dirs.mkdirs()
            }
            file = File(dirs, fileName)
            if (!file.exists()) {
                file.createNewFile()
            }

            //base64编码内容转换为字节数组
            val bytes = Base64.decode(base64Content, Base64.DEFAULT)
            //base64编码内容转换为字节数组
//            byte[] bytes = Base64.decode(base64Content, Base64.NO_WRAP);
            bais = ByteArrayInputStream(bytes)
            bis = BufferedInputStream(bais)
            fos = FileOutputStream(file)
            bos = BufferedOutputStream(fos)
            val buffer = ByteArray(1024 * 8)
            var len = 0
            while (bis.read(buffer).also { len = it } != -1) {
                bos.write(buffer, 0, len)
            }
            bos.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                bis?.close()
                bos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return file
    }


    /**
     * @param is
     * @return
     */
    fun fileToBase64Second(`is`: InputStream?): String {
        var bytes: ByteArray? = null
        //        FileInputStream fis = null;
        var bis: BufferedInputStream? = null
        var baos: ByteArrayOutputStream? = null
        //        BufferedOutputStream bos = null;
        try {
//            fis = new FileInputStream(file);
            bis = BufferedInputStream(`is`)
            baos = ByteArrayOutputStream()
            //            bos = new BufferedOutputStream(baos);
            val buffer = ByteArray(1024 * 8)
            var len = 0
            while (bis.read(buffer).also { len = it } != -1) {
                baos.write(buffer, 0, len)
            }
            //刷新此输出流并强制写出所有缓冲的输出字节
            baos.flush()
            bytes = baos.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                bis?.close()
                baos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return Base64.encodeToString(bytes, Base64.DEFAULT).trim { it <= ' ' }
//        return Base64.encodeToString(bytes, Base64.NO_WRAP).trim();
    }

    /**
     * @param file
     * @return
     */
    fun fileToBase64Second(file: File?): String {
        var bytes: ByteArray? = null
        var fis: FileInputStream? = null
        var bis: BufferedInputStream? = null
        var baos: ByteArrayOutputStream? = null
        //        BufferedOutputStream bos = null;
        try {
            fis = FileInputStream(file)
            bis = BufferedInputStream(fis)
            baos = ByteArrayOutputStream()
            //            bos = new BufferedOutputStream(baos);
            val buffer = ByteArray(1024 * 8)
            var len = 0
            while (bis.read(buffer).also { len = it } != -1) {
                baos.write(buffer, 0, len)
            }
            //刷新此输出流并强制写出所有缓冲的输出字节
            baos.flush()
            bytes = baos.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                bis?.close()
                baos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return Base64.encodeToString(bytes, Base64.DEFAULT).trim { it <= ' ' }
//        return Base64.encodeToString(bytes, Base64.NO_WRAP).trim();
    }


//    /**
//     * 還在驗證中，請不要使用
//     *
//     * @param base64Content
//     * @param dirsPath
//     * @param fileName
//     * @return
//     */
//    public static File base64ToFileThird(String base64Content, String dirsPath, String fileName) {
//        ByteArrayInputStream bais = null;
//        BufferedInputStream bis = null;
//        FileOutputStream fos = null;
//        BufferedOutputStream bos = null;
//        File file = null;
//
//        try {
//            File dirs = new File(dirsPath);
//            if (!dirs.exists()) {
//                dirs.mkdirs();
//            }
//            file = new File(dirs, fileName);
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//
//            //base64编码内容转换为字节数组
//            byte[] bytes = org.apache.commons.codec.binary.Base64.decodeBase64(base64Content);
//            bais = new ByteArrayInputStream(bytes);
//            bis = new BufferedInputStream(bais);
//            fos = new FileOutputStream(file);
//            bos = new BufferedOutputStream(fos);
//            byte[] buffer = new byte[1024 * 8];
//            int len = 0;
//            while ((len = bis.read(buffer)) != -1) {
//                bos.write(buffer, 0, len);
//            }
//            bos.flush();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (bis != null) {
//                    bis.close();
//                }
//                if (bos != null) {
//                    bos.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return file;
//    }


//    /**
//     * 還在驗證中，請不要使用
//     *
//     * @param is
//     * @return
//     */
//    public static String fileToBase64Third(InputStream is) {
//        byte[] bytes = null;
////        FileInputStream fis = null;
//        BufferedInputStream bis = null;
//        ByteArrayOutputStream baos = null;
////        BufferedOutputStream bos = null;
//
//        try {
////            fis = new FileInputStream(file);
//            bis = new BufferedInputStream(is);
//            baos = new ByteArrayOutputStream();
////            bos = new BufferedOutputStream(baos);
//            byte[] buffer = new byte[1024 * 8];
//            int len = 0;
//            while ((len = bis.read(buffer)) != -1) {
//                baos.write(buffer, 0, len);
//            }
//            //刷新此输出流并强制写出所有缓冲的输出字节
//            baos.flush();
//            bytes = baos.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (bis != null) {
//                    bis.close();
//                }
//                if (baos != null) {
//                    baos.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
//    }

//    /**
//     * 還在驗證中，請不要使用
//     *
//     * @param file
//     * @return
//     */
//    public static String fileToBase64Third(File file) {
//        byte[] bytes = null;
//        FileInputStream fis = null;
//        BufferedInputStream bis = null;
//        ByteArrayOutputStream baos = null;
////        BufferedOutputStream bos = null;
//        try {
//            fis = new FileInputStream(file);
//            bis = new BufferedInputStream(fis);
//            baos = new ByteArrayOutputStream();
////            bos = new BufferedOutputStream(baos);
//            byte[] buffer = new byte[1024 * 8];
//            int len = 0;
//            while ((len = bis.read(buffer)) != -1) {
//                baos.write(buffer, 0, len);
//            }
//            //刷新此输出流并强制写出所有缓冲的输出字节
//            baos.flush();
//            bytes = baos.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (bis != null) {
//                    bis.close();
//                }
//                if (baos != null) {
//                    baos.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
//    }

    //    /**
    //     * 還在驗證中，請不要使用
    //     *
    //     * @param base64Content
    //     * @param dirsPath
    //     * @param fileName
    //     * @return
    //     */
    //    public static File base64ToFileThird(String base64Content, String dirsPath, String fileName) {
    //        ByteArrayInputStream bais = null;
    //        BufferedInputStream bis = null;
    //        FileOutputStream fos = null;
    //        BufferedOutputStream bos = null;
    //        File file = null;
    //
    //        try {
    //            File dirs = new File(dirsPath);
    //            if (!dirs.exists()) {
    //                dirs.mkdirs();
    //            }
    //            file = new File(dirs, fileName);
    //            if (!file.exists()) {
    //                file.createNewFile();
    //            }
    //
    //            //base64编码内容转换为字节数组
    //            byte[] bytes = org.apache.commons.codec.binary.Base64.decodeBase64(base64Content);
    //            bais = new ByteArrayInputStream(bytes);
    //            bis = new BufferedInputStream(bais);
    //            fos = new FileOutputStream(file);
    //            bos = new BufferedOutputStream(fos);
    //            byte[] buffer = new byte[1024 * 8];
    //            int len = 0;
    //            while ((len = bis.read(buffer)) != -1) {
    //                bos.write(buffer, 0, len);
    //            }
    //            bos.flush();
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        } finally {
    //            try {
    //                if (bis != null) {
    //                    bis.close();
    //                }
    //                if (bos != null) {
    //                    bos.close();
    //                }
    //            } catch (IOException e) {
    //                e.printStackTrace();
    //            }
    //        }
    //
    //        return file;
    //    }
    //    /**
    //     * 還在驗證中，請不要使用
    //     *
    //     * @param is
    //     * @return
    //     */
    //    public static String fileToBase64Third(InputStream is) {
    //        byte[] bytes = null;
    ////        FileInputStream fis = null;
    //        BufferedInputStream bis = null;
    //        ByteArrayOutputStream baos = null;
    ////        BufferedOutputStream bos = null;
    //
    //        try {
    ////            fis = new FileInputStream(file);
    //            bis = new BufferedInputStream(is);
    //            baos = new ByteArrayOutputStream();
    ////            bos = new BufferedOutputStream(baos);
    //            byte[] buffer = new byte[1024 * 8];
    //            int len = 0;
    //            while ((len = bis.read(buffer)) != -1) {
    //                baos.write(buffer, 0, len);
    //            }
    //            //刷新此输出流并强制写出所有缓冲的输出字节
    //            baos.flush();
    //            bytes = baos.toByteArray();
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        } finally {
    //            try {
    //                if (bis != null) {
    //                    bis.close();
    //                }
    //                if (baos != null) {
    //                    baos.close();
    //                }
    //            } catch (IOException e) {
    //                e.printStackTrace();
    //            }
    //        }
    //
    //        return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
    //    }
    //    /**
    //     * 還在驗證中，請不要使用
    //     *
    //     * @param file
    //     * @return
    //     */
    //    public static String fileToBase64Third(File file) {
    //        byte[] bytes = null;
    //        FileInputStream fis = null;
    //        BufferedInputStream bis = null;
    //        ByteArrayOutputStream baos = null;
    ////        BufferedOutputStream bos = null;
    //        try {
    //            fis = new FileInputStream(file);
    //            bis = new BufferedInputStream(fis);
    //            baos = new ByteArrayOutputStream();
    ////            bos = new BufferedOutputStream(baos);
    //            byte[] buffer = new byte[1024 * 8];
    //            int len = 0;
    //            while ((len = bis.read(buffer)) != -1) {
    //                baos.write(buffer, 0, len);
    //            }
    //            //刷新此输出流并强制写出所有缓冲的输出字节
    //            baos.flush();
    //            bytes = baos.toByteArray();
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        } finally {
    //            try {
    //                if (bis != null) {
    //                    bis.close();
    //                }
    //                if (baos != null) {
    //                    baos.close();
    //                }
    //            } catch (IOException e) {
    //                e.printStackTrace();
    //            }
    //        }
    //
    //        return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
    //    }

    fun getBase64StrList(filePath: String): List<String> {
        val base64StrList = mutableListOf<String>()
        LogManager.i(TAG, "base64StrList size*****" + base64StrList.size)
        try {
            val fis = FileInputStream(filePath)
            val bis = BufferedInputStream(fis)
            val buffer = ByteArray(1024 * 8)
            var len = 0
            var base64Content: String
            while (bis.read(buffer).also { len = it } != -1) {
                base64Content = String(buffer, 0, len)
                base64StrList.add(base64Content)
            }
            LogManager.i(TAG, "base64StrList size*****" + base64StrList.size)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return base64StrList
    }

}