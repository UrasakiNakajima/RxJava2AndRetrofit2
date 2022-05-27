package com.phone.base64_and_file;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.YuvImage;
import android.media.ExifInterface;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.phone.common_library.manager.LogManager;

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
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;

import id.zelory.compressor.Compressor;

public class BitmapManager {

    private static final String TAG = "BitmapManager";
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
     * 图片合成
     *
     * @param bitmap 位图1
     * @param mark   位图2
     * @return Bitmap
     */
    public static Bitmap createBitmap(Bitmap bitmap, Bitmap mark) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int mW = mark.getWidth();
        int mH = mark.getHeight();
        Bitmap newbitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个长宽一样的位图

        Canvas cv = new Canvas(newbitmap);
        cv.drawBitmap(bitmap, 0, 0, null);// 在 0，0坐标开始画入bitmap
        cv.drawBitmap(mark, w - mW, h - mH, null);// 在右下角画入水印mark
        cv.save();// 保存
        cv.restore();// 存储
        return newbitmap;
    }

    /**
     * 放大缩小图片
     *
     * @param bitmap 位图
     * @param w      新的宽度
     * @param h      新的高度
     * @return Bitmap
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidht, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    /**
     * 旋转图片
     *
     * @param bitmap 要旋转的图片
     * @param angle  旋转角度
     * @return bitmap
     */
    public static Bitmap rotate(Bitmap bitmap, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    /**
     * 圆形图片
     *
     * @param source      位图
     * @param strokeWidth 裁剪范围 0表示最大
     * @param bl          是否需要描边
     * @param bl          画笔粗细
     * @param bl          颜色代码
     * @return bitmap
     */
    public static Bitmap createCircleBitmap(Bitmap source, int strokeWidth, boolean bl, int edge, int color) {

        int diameter = source.getWidth() < source.getHeight() ? source.getWidth() : source.getHeight();
        Bitmap target = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);//创建画布

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2, paint);//绘制圆形
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//取相交裁剪
        canvas.drawBitmap(source, strokeWidth, strokeWidth, paint);
        if (bl) {
            if (color == 0) color = 0xFFFEA248;//默认橘黄色
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);//描边
            paint.setStrokeWidth(edge);
            canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2, paint);
        }
        return target;
    }

    /**
     * 圆角图片
     *
     * @param bitmap 位图
     * @param rx     x方向上的圆角半径
     * @param ry     y方向上的圆角半径
     * @param bl     是否需要描边
     * @param bl     画笔粗细
     * @param bl     颜色代码
     * @return bitmap
     */
    public static Bitmap createCornerBitmap(Bitmap bitmap, int rx, int ry, boolean bl, int edge, int color) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);//创建画布

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        canvas.drawRoundRect(rectF, rx, ry, paint);//绘制圆角矩形
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//取相交裁剪
        canvas.drawBitmap(bitmap, rect, rect, paint);
        if (bl) {
            if (color == 0) color = 0xFFFEA248;//默认橘黄色
            paint.setColor(color);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);//描边
            paint.setStrokeWidth(edge);
            canvas.drawRoundRect(rectF, rx, ry, paint);
        }
        return output;
    }

    /**
     * 按比例裁剪图片
     *
     * @param bitmap 位图
     * @param wScale 裁剪宽 0~100%
     * @param hScale 裁剪高 0~100%
     * @return bitmap
     */
    public static Bitmap cropBitmap(Bitmap bitmap, float wScale, float hScale) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int wh = (int) (w * wScale);
        int hw = (int) (h * hScale);

        int retX = (int) (w * (1 - wScale) / 2);
        int retY = (int) (h * (1 - hScale) / 2);

        return Bitmap.createBitmap(bitmap, retX, retY, wh, hw, null, false);
    }

    /**
     * 获得带倒影的图片方法
     *
     * @param bitmap 位图
     * @param region 倒影区域 0.1~1
     * @return bitmap
     */
    public static Bitmap createReflectionBitmap(Bitmap bitmap, float region) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);//镜像缩放
        Bitmap reflectionBitmap = Bitmap.createBitmap(
                bitmap, 0
                , (int) (height * (1 - region))//从哪个点开始绘制
                , width
                , (int) (height * region)//绘制多高
                , matrix, false);

        Bitmap reflectionWithBitmap = Bitmap.createBitmap(width, height + (int) (height * region),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(reflectionWithBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawBitmap(reflectionBitmap, 0, height, null);

        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                reflectionWithBitmap.getHeight()
                , 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);

        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));//取两层绘制交集。显示下层。
        canvas.drawRect(0, height, width, reflectionWithBitmap.getHeight(), paint);
        return reflectionWithBitmap;
    }

    /**
     * 图片质量压缩
     *
     * @param bitmap
     * @param many   百分比
     * @return
     */
    public static Bitmap compressBitmap(Bitmap bitmap, float many) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, (int) many * 100, baos);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        return BitmapFactory.decodeStream(isBm, null, null);
    }

    /**
     * 高级图片质量压缩
     *
     * @param bitmap  位图
     * @param maxSize 压缩后的大小，单位kb
     */
    public static Bitmap imageZoom(Bitmap bitmap, double maxSize) {
        // 将bitmap放至数组中，意在获得bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 格式、质量、输出流
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] b = baos.toByteArray();
        // 将字节换成KB
        double mid = b.length / 1024;
        // 获取bitmap大小 是允许最大大小的多少倍
        double i = mid / maxSize;
        // 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
        doRecycledIfNot(bitmap);
        if (i > 1) {
            // 缩放图片 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
            // （保持宽高不变，缩放后也达到了最大占用空间的大小）
            return scaleWithWH(bitmap, bitmap.getWidth() / Math.sqrt(i),
                    bitmap.getHeight() / Math.sqrt(i));
        }
        return null;
    }

    /***
     * 图片缩放
     *@param bitmap 位图
     * @param w 新的宽度
     * @param h 新的高度
     * @return Bitmap
     */
    public static Bitmap scaleWithWH(Bitmap bitmap, double w, double h) {
        if (w == 0 || h == 0 || bitmap == null) {
            return bitmap;
        } else {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            Matrix matrix = new Matrix();
            float scaleWidth = (float) (w / width);
            float scaleHeight = (float) (h / height);

            matrix.postScale(scaleWidth, scaleHeight);
            return Bitmap.createBitmap(bitmap, 0, 0, width, height,
                    matrix, true);
        }
    }

    /**
     * YUV视频流格式转bitmap
     *
     * @param data YUV视频流格式
     * @return width 设置高度
     */
    public static Bitmap getBitmap(byte[] data, int width, int height) {
        Bitmap bitmap;
        YuvImage yuvimage = new YuvImage(data, ImageFormat.NV21, width, height, null);
        //data是onPreviewFrame参数提供
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, yuvimage.getWidth(), yuvimage.getHeight()), 100, baos);//
        // 80--JPG图片的质量[0-100],100最高
        byte[] rawImage = baos.toByteArray();
        BitmapFactory.Options options = new BitmapFactory.Options();
        SoftReference<Bitmap> softRef = new SoftReference<Bitmap>(BitmapFactory.decodeByteArray(rawImage, 0, rawImage
                .length, options));
        bitmap = softRef.get();
        return bitmap;
    }

    /**
     * 图片资源文件转bitmap
     *
     * @param context
     * @param resId
     * @return bitmap
     */
    public static Bitmap getBitmapResources(Context context, int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    /**
     * 将图片路径转Bitmap
     *
     * @return Bitmap
     * @Param path 图片路径
     */
    public static Bitmap getBitmapPath(String path) {
        return BitmapFactory.decodeFile(path);
    }

    /**
     * bitmap保存到指定路径
     *
     * @param path 图片的绝对路径
     * @param bmp  位图
     * @return bitmap
     */
    public static boolean saveFile(String path, Bitmap bmp) {
        if (TextUtils.isEmpty(path) || bmp == null) return false;

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        } else {
            File p = file.getParentFile();
            if (!p.exists()) {
                p.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 回收一个未被回收的Bitmap
     *
     * @param bitmap
     */
    public static void doRecycledIfNot(Bitmap bitmap) {
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    /**
     * 等比压缩图片
     *
     * @param resBitmap 原图
     * @param desWidth  压缩后图片的宽度
     * @param desHeight 压缩后图片的高度
     * @return 压缩后的图片
     */
    public static Bitmap scaleImage(Bitmap resBitmap, int desWidth, int desHeight) {
        int resWidth = resBitmap.getWidth();
        int resHeight = resBitmap.getHeight();
        if (resHeight > desHeight || resWidth > desWidth) {
            // 计算出实际宽高和目标宽高的比率
            final float heightRatio = (float) desHeight / (float) resHeight;
            final float widthRatio = (float) desWidth / (float) resWidth;
            float scale = heightRatio < widthRatio ? heightRatio : widthRatio;
            LogManager.i(TAG, "scaleImage scale*****" + scale);
            //開平方會有一點損失精度
            float prescription = (float) Math.sqrt(scale);
//            float prescription2 = (float) Math.sqrt(prescription);
            LogManager.i(TAG, "scaleImage prescription*****" + prescription);
//            LogManager.i(TAG, "scaleImage prescription2*****" + prescription2);
            Bitmap bitmap = null;
            //循環壓縮bitmap，防止一次壓縮bitmap卡頓問題（子線程把主線成卡頓了）
            for (int i = 0; i < 2; i++) {
                if (bitmap != null) {
                    bitmap = scale(bitmap, prescription);
//                    bitmap = scale(bitmap, prescription2);
                } else {
                    bitmap = scale(resBitmap, prescription);
//                    bitmap = scale(resBitmap, prescription2);
                }
                LogManager.i(TAG, i + "scaleImage prescription*****" + prescription);
//                LogManager.i(TAG, i + "scaleImage prescription2*****" + prescription2);
            }
            return bitmap;
        }
        return resBitmap;
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
     * byte转Uri
     *
     * @param context
     * @param data
     * @return
     */
    public static Uri saveTakePictureImage(Context context, byte[] data) {
        File file = context.getExternalFilesDir("file1");
        file = new File(file.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
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
            if (fos != null) {
                try {
                    fos.close();
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
//            options.inSampleSize = 8;//图片的长宽都是原来的1/8
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
            //刷新此输出流并强制写出所有缓冲的输出字节
            baos.flush();
        } catch (IOException e) {
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
            //刷新此输出流并强制写出所有缓冲的输出字节
            bos.flush();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
        File dirs2 = new File(dirsPath);
        if (!dirs2.exists()) {
            dirs2.mkdirs();
        }

        File file = null;
        try {
            file = new Compressor(context)
//                    .setMaxWidth(1280)
//                    .setMaxHeight(960)
                    .setQuality(50)
                    .setCompressFormat(Bitmap.CompressFormat.PNG)
                    .setDestinationDirectoryPath(dirsPath)
                    .compressToFile(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File getAssetFile(Context context, String assetFileName, String dirsPath) {
        AssetManager assetManager = context.getAssets();
        File dirs = new File(dirsPath);
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
        File file = new File(dirs, assetFileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        InputStream is = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            is = assetManager.open(assetFileName);
            bis = new BufferedInputStream(is);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            byte[] buffer = new byte[1024];
            int len = 0;
            while (-1 != (len = bis.read(buffer))) {
                bos.write(buffer, 0, len);
            }
            //刷新此输出流并强制写出所有缓冲的输出字节
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return file;
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
