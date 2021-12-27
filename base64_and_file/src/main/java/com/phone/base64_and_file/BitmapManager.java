package com.phone.base64_and_file;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;

import id.zelory.compressor.Compressor;

public class BitmapManager {

    /**
     * 图像的旋转方向是0
     */
    public static final int ROTATE0 = 0;
    /**
     * 图像的旋转方向是90
     */
    public static final int ROTATE90 = 90;
    /**
     * 图像的旋转方向是180
     */
    public static final int ROTATE180 = 180;
    /**
     * 图像的旋转方向是270
     */
    public static final int ROTATE270 = 270;
    /**
     * 图像的旋转方向是360
     */
    public static final int ROTATE360 = 360;
    /**
     * 图片太大内存溢出后压缩的比例
     */
    public static final int PIC_COMPRESS_SIZE = 4;
    /**
     * 图像压缩边界
     */
    public static final int IMAGEBOUND = 128;
    /**
     * 图片显示最大边的像素
     */
    public static final int MAXLENTH = 1024;
    /**
     * Log TAG
     */
    private static final String TAG = "ImageUtils";
    /**
     * Log switch
     */
    private static final boolean DEBUG = false;
    /**
     * 保存图片的质量：100
     */
    private static final int QUALITY = 100;
    /**
     * 默认的最大尺寸
     */
    private static final int DEFAULT_MAX_SIZE_CELL_NETWORK = 600;
    /**
     * 题编辑wifi环境下压缩的最大尺寸
     */
    private static final int QUESTION_MAX_SIZE_CELL_NETWORK = 1024;
    /**
     * 图片压缩的质量
     */
    private static final int QUESTION_IMAGE_JPG_QUALITY = 75;
    /**
     * 默认的图片压缩的质量
     */
    private static final int DEFAULT_IMAGE_JPG_QUALITY = 50;
    /**
     * 网络请求超时时间
     */
    private static final int CONNECTTIMEOUT = 3000;

    /**
     * Private constructor to prohibit nonsense instance creation.
     */
    private BitmapManager() {
    }

    /**
     * 得到要显示的图片数据
     */
    public static Bitmap createBitmap(byte[] data, float orientation) {
        Bitmap bitmap = null;
        Bitmap transformed = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        try {

            int width = 2000;
            int hight = 2000;
            int min = Math.min(width, hight);
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, opts);
            opts.inSampleSize =
                    BitmapManager.computeSampleSize(opts, min, BitmapManager.MAXLENTH * BitmapManager.MAXLENTH);
            opts.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
            transformed = BitmapManager.rotateBitmap(orientation, bitmap);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
            if (transformed != null && !transformed.isRecycled()) {
                transformed.recycle();
                transformed = null;
            }
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, opts);
            opts.inSampleSize =
                    BitmapManager.computeSampleSize(opts, -1, opts.outWidth * opts.outHeight
                            / BitmapManager.PIC_COMPRESS_SIZE);
            opts.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
            transformed = BitmapManager.rotateBitmap(orientation, bitmap);
        }
        if (transformed != bitmap && bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        return transformed;

    }

    /**
     * 根据从数据中读到的方向旋转图片
     *
     * @param orientation 图片方向
     * @param bitmap      要旋转的bitmap
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmap(float orientation, Bitmap bitmap) {
        Bitmap transformed;
        Matrix m = new Matrix();
        if (orientation == 0) {
            transformed = bitmap;
        } else {
            m.setRotate(orientation);
            transformed = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        }
        return transformed;
    }

    /**
     * 获取无损压缩图片合适的压缩比例
     *
     * @param options        图片的一些设置项
     * @param minSideLength  最小边长
     * @param maxNumOfPixels 最大的像素数目
     * @return 返回合适的压缩值
     */
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = BitmapManager.computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) { // SUPPRESS CHECKSTYLE
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8; // SUPPRESS CHECKSTYLE
        }
        return roundedSize;
    }

    /**
     * 获取无损压缩图片的压缩比
     *
     * @param options        图片的一些设置项
     * @param minSideLength  最小边长
     * @param maxNumOfPixels 最大的像素数目
     * @return 返回合适的压缩值
     */
    public static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength,
                                               int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound =
                (minSideLength == -1) ? BitmapManager.IMAGEBOUND : (int) Math.min(
                        Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 解析图片的旋转方向
     *
     * @param path 图片的路径
     * @return 旋转角度
     */
    public static int decodeImageDegree(String path) {
        int degree = ROTATE0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation =
                    exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = ROTATE90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = ROTATE180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = ROTATE270;
                    break;
                default:
                    degree = ROTATE0;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            degree = ROTATE0;
        }
        return degree;
    }

    /**
     * 等比压缩图片
     *
     * @param bitmap 原图
     * @param scale  压缩因子
     * @return 压缩后的图片
     */
    private static Bitmap scale(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 尺寸缩放
     *
     * @param bitmap bitmap
     * @param w      width
     * @param h      height
     * @return scaleBitmap
     */
    public static Bitmap scale(Bitmap bitmap, int w, int h) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

    }

    /**
     * 等比压缩图片
     *
     * @param resBitmap 原图
     * @param desWidth  压缩后图片的宽度
     * @param desHeight 压缩后图片的高度
     * @return 压缩后的图片
     */
    public static Bitmap calculateInSampleSize(Bitmap resBitmap, int desWidth, int desHeight) {
        int resWidth = resBitmap.getWidth();
        int resHeight = resBitmap.getHeight();
        if (resHeight > desHeight || resWidth > desWidth) {
            // 计算出实际宽高和目标宽高的比率
            final float heightRatio = (float) desHeight / (float) resHeight;
            final float widthRatio = (float) desWidth / (float) resWidth;
            float scale = heightRatio < widthRatio ? heightRatio : widthRatio;
            return scale(resBitmap, scale);
        }
        return resBitmap;
    }

    /**
     * byte转Uri
     *
     * @param context
     * @param data
     * @return
     */
    public static Uri saveTakePictureImage(Context context, byte[] data) {
        File file = context.getExternalFilesDir("file1");
        file = new File(file.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg");
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            fout.write(data);
            fout.flush();
        } catch (Exception e) {
            e.printStackTrace();

            // 异常时删除保存失败的文件
            try {
                if (file != null && file.exists() && file.isFile()) {
                    file.delete();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e(TAG, file.getAbsolutePath());
        return Uri.fromFile(file);
    }

    /**
     * byte转成bitmap
     *
     * @param data
     * @param width
     * @param height
     * @return
     */
    public static Bitmap yuvToBitmap(byte[] data, int width, int height) {
        int frameSize = width * height;
        int[] rgba = new int[frameSize];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int y = (0xff & ((int) data[i * width + j]));
                int u = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 0]));
                int v = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 1]));
                y = y < 16 ? 16 : y;

                int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));
                int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
                int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));

                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);

                rgba[i * width + j] = 0xff000000 + (b << 16) + (g << 8) + r;
            }
        }

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmp.setPixels(rgba, 0, width, 0, 0, width, height);
        return bmp;
    }

    /**
     * byte转成bitmap
     *
     * @param depthBytes
     * @param width
     * @param height
     * @return
     */
    public static Bitmap depthToBitmap(byte[] depthBytes, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int[] argbData = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            argbData[i] = (((int) depthBytes[i * 2] + depthBytes[i * 2 + 1] * 256) / 10 & 0x000000ff)
                    | ((((int) depthBytes[i * 2] + depthBytes[i * 2 + 1] * 256) / 10) & 0x000000ff) << 8
                    | ((((int) depthBytes[i * 2] + depthBytes[i * 2 + 1] * 256) / 10) & 0x000000ff) << 16
                    | 0xff000000;

        }
        bitmap.setPixels(argbData, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * byte转bitmap
     *
     * @param bytes
     * @param width
     * @param height
     * @return
     */
    public static Bitmap rGBToBitmap(byte[] bytes, int width, int height) {
        // use Bitmap.Config.ARGB_8888 instead of type is OK
        Bitmap stitchBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        byte[] rgba = new byte[width * height * 4];
        for (int i = 0; i < width * height; i++) {
            byte b1 = bytes[i * 3 + 0];
            byte b2 = bytes[i * 3 + 1];
            byte b3 = bytes[i * 3 + 2];
            // set value
            rgba[i * 4 + 0] = b1;
            rgba[i * 4 + 1] = b2;
            rgba[i * 4 + 2] = b3;
            rgba[i * 4 + 3] = (byte) 255;
        }
        stitchBmp.copyPixelsFromBuffer(ByteBuffer.wrap(rgba));
        return stitchBmp;
    }

    /**
     * byte转bitmap
     *
     * @param bytes
     * @param width
     * @param height
     * @return
     */
    public static Bitmap bGRToBitmap(byte[] bytes, int width, int height) {
        // use Bitmap.Config.ARGB_8888 instead of type is OK
        Bitmap stitchBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        byte[] rgba = new byte[width * height * 4];
        for (int i = 0; i < width * height; i++) {
            byte b1 = bytes[i * 3 + 0];
            byte b2 = bytes[i * 3 + 1];
            byte b3 = bytes[i * 3 + 2];
            // set value
            rgba[i * 4 + 0] = b3;
            rgba[i * 4 + 1] = b2;
            rgba[i * 4 + 2] = b1;
            rgba[i * 4 + 3] = (byte) 255;
        }
        stitchBmp.copyPixelsFromBuffer(ByteBuffer.wrap(rgba));
        return stitchBmp;
    }

    public static byte[] aRGBToR(byte[] bytes, int width, int height) {
        byte[] IR = new byte[width * height];
        for (int i = 0; i < width * height; i++) {
            IR[i] = bytes[i * 4];
        }
        return IR;
    }

//    /**
//     * 百度人脸图片转换
//     *
//     * @param newInstance
//     * @return
//     */
//    public static Bitmap getInstaceBmp(BDFaceImageInstance newInstance) {
//        Bitmap transBmp = null;
//        if (newInstance.imageType == BDFaceSDKCommon.BDFaceImageType.BDFACE_IMAGE_TYPE_RGBA) {
//            transBmp = Bitmap.createBitmap(newInstance.width, newInstance.height, Bitmap.Config.ARGB_8888);
//            transBmp.copyPixelsFromBuffer(ByteBuffer.wrap(newInstance.data));
//        } else if (newInstance.imageType == BDFaceSDKCommon.BDFaceImageType.BDFACE_IMAGE_TYPE_BGR) {
//            transBmp = BitmapManager.BGR2Bitmap(newInstance.data, newInstance.width, newInstance.height);
//        } else if (newInstance.imageType == BDFaceSDKCommon.BDFaceImageType.BDFACE_IMAGE_TYPE_YUV_NV21) {
//            transBmp = BitmapManager.yuv2Bitmap(newInstance.data, newInstance.width, newInstance.height);
//        } else if (newInstance.imageType == BDFaceSDKCommon.BDFaceImageType.BDFACE_IMAGE_TYPE_GRAY) {
//            transBmp = Depth2Bitmap(newInstance.data, newInstance.width, newInstance.height);
//        }
//        return transBmp;
//    }

    /**
     * 获取图片数据
     *
     * @param path
     * @return
     */
    public static Bitmap getBitmap(String path) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        Bitmap bm = null;
        try {
            fis = new FileInputStream(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;//图片的长宽都是原来的1/8
            bis = new BufferedInputStream(fis);
            bm = BitmapFactory.decodeStream(bis, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }

    /**
     * @param bitmap
     * @return
     */
    public static InputStream bitmapToInputStream(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        ByteArrayInputStream byteArrInputStream = new ByteArrayInputStream(
                baos.toByteArray());

        return byteArrInputStream;
    }

    /**
     * 这个是把文件变成二进制流
     *
     * @param imagepath
     * @return
     * @throws Exception
     */
    public static byte[] readStream(String imagepath) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        try {
            fis = new FileInputStream(imagepath);
            bis = new BufferedInputStream(fis);

            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while (-1 != (len = bis.read(buffer))) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }

    /**
     * 将Bitmap转换成文件
     * 保存文件
     *
     * @param bitmap
     * @param fileName
     * @throws IOException
     */
    public static File saveFile(Bitmap bitmap, String path, String fileName) {
        File dirs = new File(path);
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
        File captureFile = new File(dirs, fileName);
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(captureFile));
            bos.flush();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return captureFile;
    }

    /**
     * 读取文件的大小
     */

    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                size = bis.available();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bis != null) {
                        bis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return size;
        }
        return 0;
    }

    /**
     * 返回byte的数据大小对应的文本
     *
     * @param size
     * @return
     */
    public static String getDataSize(long size) {
        DecimalFormat formater = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "bytes";
        } else if (size < 1024 * 1024) {
            float kbsize = size / 1024f;
            return formater.format(kbsize) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mbsize = size / 1024f / 1024f;
            return formater.format(mbsize) + "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            float gbsize = size / 1024f / 1024f / 1024f;
            return formater.format(gbsize) + "GB";
        } else {
            return "size: error";
        }
    }

    /**
     * 使用Compressor IO模式自定义压缩
     *
     * @param path .setMaxWidth(640).setMaxHeight(480)这两个数值越高，压缩力度越小，图片也不清晰
     *             .setQuality(75)这个方法只是设置图片质量，并不影响压缩图片的大小KB
     *             .setCompressFormat(Bitmap.CompressFormat.WEBP) WEBP图片格式是Google推出的 压缩强，质量 高，但是IOS不识别，需要把图片转为字节流然后转PNG格式
     *             .setCompressFormat(Bitmap.CompressFormat.PNG)PNG格式的压缩，会导致图片变大，并耗过大的内 存，手机反应缓慢
     *             .setCompressFormat(Bitmap.CompressFormat.JPEG)JPEG压缩；压缩速度比PNG快，质量一般，基本上属于1/10的压缩比例
     */
    public static File initCompressorIO(Context context, String path, String dirsPath) {
        try {
            File file = new Compressor(context)
//                    .setMaxWidth(2016)
//                    .setMaxHeight(1512)
                    .setQuality(10)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(dirsPath)
                    .compressToFile(new File(path));

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * base64转bitmap
     *
     * @param base64String
     * @return
     */
    public static Bitmap base64ToBitmap(String base64String) {
        base64String = Uri.decode(base64String);
        byte[] decode = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        return bitmap;
    }

}
