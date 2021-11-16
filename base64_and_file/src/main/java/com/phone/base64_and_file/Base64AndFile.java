package com.phone.base64_and_file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class Base64AndFile {

    /**
     * @param base64Content
     * @param filePath
     * @param fileName
     * @return
     */
    public static File base64ToFile(String base64Content, String filePath, String fileName) {
        BASE64Decoder decoder = new BASE64Decoder();
        ByteArrayInputStream bais = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        File file = null;

        try {
            File dirs = new File(filePath);
            if (!dirs.exists()) {
                dirs.mkdirs();
            }
            file = new File(dirs, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            byte[] bytes = decoder.decodeBuffer(base64Content);//base64编码内容转换为字节数组
            bais = new ByteArrayInputStream(bytes);
            bis = new BufferedInputStream(bais);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            byte[] buffer = new byte[1024 * 2];
            int len = 0;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }


    /**
     * @param is
     * @return
     */
    public static String fileToBase64(InputStream is) {
        BASE64Encoder encoder = new BASE64Encoder();
        byte[] bytes = null;
//        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
//        BufferedOutputStream bos = null;

        try {
//            fis = new FileInputStream(file);
            bis = new BufferedInputStream(is);
            baos = new ByteArrayOutputStream();
//            bos = new BufferedOutputStream(baos);
            byte[] buffer = new byte[1024];
            int len = bis.read(buffer);
            while (len != -1) {
                baos.write(buffer, 0, len);
                len = bis.read(buffer);
            }
            //刷新此输出流并强制写出所有缓冲的输出字节
            baos.flush();
            bytes = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return encoder.encodeBuffer(bytes).trim();
    }

    /**
     * @param file
     * @return
     */
    public static String fileToBase64(File file) {
        BASE64Encoder encoder = new BASE64Encoder();
        byte[] bytes = null;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
//        BufferedOutputStream bos = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            baos = new ByteArrayOutputStream();
//            bos = new BufferedOutputStream(baos);
            byte[] buffer = new byte[1024];
            int len = bis.read(buffer);
            while (len != -1) {
                baos.write(buffer, 0, len);
                len = bis.read(buffer);
            }
            //刷新此输出流并强制写出所有缓冲的输出字节
            baos.flush();
            bytes = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return encoder.encodeBuffer(bytes).trim();
    }

}
