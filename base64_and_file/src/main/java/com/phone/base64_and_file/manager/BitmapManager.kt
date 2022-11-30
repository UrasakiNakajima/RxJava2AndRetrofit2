package com.phone.base64_and_file.manager

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import com.phone.library_common.manager.LogManager.i
import id.zelory.compressor.Compressor
import java.io.*
import java.lang.ref.SoftReference
import java.nio.ByteBuffer
import java.text.DecimalFormat

object BitmapManager {

    private val TAG = BitmapManager::class.java.simpleName

    /**
     * 图像的旋转方向是0
     */
    val ROTATE0 = 0

    /**
     * 图像的旋转方向是90
     */
    val ROTATE90 = 90

    /**
     * 图像的旋转方向是180
     */
    val ROTATE180 = 180

    /**
     * 图像的旋转方向是270
     */
    val ROTATE270 = 270

    /**
     * 图像的旋转方向是360
     */
    val ROTATE360 = 360

    /**
     * 图片太大内存溢出后压缩的比例
     */
    val PIC_COMPRESS_SIZE = 4

    /**
     * 图像压缩边界
     */
    val IMAGEBOUND = 128

    /**
     * 图片显示最大边的像素
     */
    val MAXLENTH = 1024

    /**
     * Log switch
     */
    private val DEBUG = false

    /**
     * 保存图片的质量：100
     */
    private val QUALITY = 100

    /**
     * 默认的最大尺寸
     */
    private val DEFAULT_MAX_SIZE_CELL_NETWORK = 600

    /**
     * 题编辑wifi环境下压缩的最大尺寸
     */
    private val QUESTION_MAX_SIZE_CELL_NETWORK = 1024

    /**
     * 图片压缩的质量
     */
    private val QUESTION_IMAGE_JPG_QUALITY = 75

    /**
     * 默认的图片压缩的质量
     */
    private val DEFAULT_IMAGE_JPG_QUALITY = 50

    /**
     * 网络请求超时时间
     */
    private val CONNECTTIMEOUT = 3000

    /**
     * Private constructor to prohibit nonsense instance creation.
     */
    private fun BitmapManager() {}

    /**
     * 图片合成
     *
     * @param bitmap 位图1
     * @param mark   位图2
     * @return Bitmap
     */
    fun createBitmap(bitmap: Bitmap, mark: Bitmap): Bitmap? {
        val w = bitmap.width
        val h = bitmap.height
        val mW = mark.width
        val mH = mark.height
        val newbitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888) // 创建一个长宽一样的位图
        val cv = Canvas(newbitmap)
        cv.drawBitmap(bitmap, 0f, 0f, null) // 在 0，0坐标开始画入bitmap
        cv.drawBitmap(mark, (w - mW).toFloat(), (h - mH).toFloat(), null) // 在右下角画入水印mark
        cv.save() // 保存
        cv.restore() // 存储
        return newbitmap
    }

    /**
     * 放大缩小图片
     *
     * @param bitmap 位图
     * @param w      新的宽度
     * @param h      新的高度
     * @return Bitmap
     */
    fun zoomBitmap(bitmap: Bitmap, w: Int, h: Int): Bitmap? {
        val width = bitmap.width
        val height = bitmap.height
        val matrix = Matrix()
        val scaleWidht = w.toFloat() / width
        val scaleHeight = h.toFloat() / height
        matrix.postScale(scaleWidht, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

    /**
     * 旋转图片
     *
     * @param bitmap 要旋转的图片
     * @param angle  旋转角度
     * @return bitmap
     */
    fun rotate(bitmap: Bitmap, angle: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        return Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width,
            bitmap.height, matrix, true
        )
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
    fun createCircleBitmap(
        source: Bitmap,
        strokeWidth: Int,
        bl: Boolean,
        edge: Int,
        color: Int
    ): Bitmap? {
        var color = color
        val diameter = if (source.width < source.height) source.width else source.height
        val target = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(target) //创建画布
        val paint = Paint()
        paint.isAntiAlias = true
        canvas.drawCircle(
            (diameter / 2).toFloat(),
            (diameter / 2).toFloat(),
            (diameter / 2).toFloat(),
            paint
        ) //绘制圆形
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN) //取相交裁剪
        canvas.drawBitmap(source, strokeWidth.toFloat(), strokeWidth.toFloat(), paint)
        if (bl) {
            if (color == 0) color = -0x15db8 //默认橘黄色
            paint.color = color
            paint.style = Paint.Style.STROKE //描边
            paint.strokeWidth = edge.toFloat()
            canvas.drawCircle(
                (diameter / 2).toFloat(),
                (diameter / 2).toFloat(),
                (diameter / 2).toFloat(),
                paint
            )
        }
        return target
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
    fun createCornerBitmap(
        bitmap: Bitmap,
        rx: Int,
        ry: Int,
        bl: Boolean,
        edge: Int,
        color: Int
    ): Bitmap? {
        var color = color
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output) //创建画布
        val paint = Paint()
        paint.isAntiAlias = true
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        canvas.drawRoundRect(rectF, rx.toFloat(), ry.toFloat(), paint) //绘制圆角矩形
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN) //取相交裁剪
        canvas.drawBitmap(bitmap, rect, rect, paint)
        if (bl) {
            if (color == 0) color = -0x15db8 //默认橘黄色
            paint.color = color
            paint.color = color
            paint.style = Paint.Style.STROKE //描边
            paint.strokeWidth = edge.toFloat()
            canvas.drawRoundRect(rectF, rx.toFloat(), ry.toFloat(), paint)
        }
        return output
    }

    /**
     * 按比例裁剪图片
     *
     * @param bitmap 位图
     * @param wScale 裁剪宽 0~100%
     * @param hScale 裁剪高 0~100%
     * @return bitmap
     */
    fun cropBitmap(bitmap: Bitmap, wScale: Float, hScale: Float): Bitmap? {
        val w = bitmap.width
        val h = bitmap.height
        val wh = (w * wScale).toInt()
        val hw = (h * hScale).toInt()
        val retX = (w * (1 - wScale) / 2).toInt()
        val retY = (h * (1 - hScale) / 2).toInt()
        return Bitmap.createBitmap(bitmap, retX, retY, wh, hw, null, false)
    }

    /**
     * 获得带倒影的图片方法
     *
     * @param bitmap 位图
     * @param region 倒影区域 0.1~1
     * @return bitmap
     */
    fun createReflectionBitmap(bitmap: Bitmap, region: Float): Bitmap? {
        val width = bitmap.width
        val height = bitmap.height
        val matrix = Matrix()
        matrix.preScale(1f, -1f) //镜像缩放
        val reflectionBitmap = Bitmap.createBitmap(
            bitmap, 0, (height * (1 - region)).toInt() //从哪个点开始绘制
            , width, (height * region).toInt() //绘制多高
            , matrix, false
        )
        val reflectionWithBitmap = Bitmap.createBitmap(
            width, height + (height * region).toInt(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(reflectionWithBitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        canvas.drawBitmap(reflectionBitmap, 0f, height.toFloat(), null)
        val shader = LinearGradient(
            0F, bitmap.height.toFloat(), 0F,
            reflectionWithBitmap.height.toFloat(), 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP
        )
        val paint = Paint()
        paint.shader = shader
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN) //取两层绘制交集。显示下层。
        canvas.drawRect(
            0f,
            height.toFloat(),
            width.toFloat(),
            reflectionWithBitmap.height.toFloat(),
            paint
        )
        return reflectionWithBitmap
    }

    /**
     * 图片质量压缩
     *
     * @param bitmap
     * @param many   百分比
     * @return
     */
    fun compressBitmap(bitmap: Bitmap, many: Float): Bitmap? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, many.toInt() * 100, baos)
        val isBm = ByteArrayInputStream(baos.toByteArray())
        return BitmapFactory.decodeStream(isBm, null, null)
    }

    /**
     * 高级图片质量压缩
     *
     * @param bitmap  位图
     * @param maxSize 压缩后的大小，单位kb
     */
    fun imageZoom(bitmap: Bitmap, maxSize: Double): Bitmap? {
        // 将bitmap放至数组中，意在获得bitmap的大小（与实际读取的原文件要大）
        val baos = ByteArrayOutputStream()
        // 格式、质量、输出流
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos)
        val b = baos.toByteArray()
        // 将字节换成KB
        val mid = (b.size / 1024).toDouble()
        // 获取bitmap大小 是允许最大大小的多少倍
        val i = mid / maxSize
        // 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
        doRecycledIfNot(bitmap)
        return if (i > 1) {
            // 缩放图片 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
            // （保持宽高不变，缩放后也达到了最大占用空间的大小）
            scaleWithWH(
                bitmap, bitmap.width / Math.sqrt(i),
                bitmap.height / Math.sqrt(i)
            )
        } else null
    }

    /***
     * 图片缩放
     * @param bitmap 位图
     * @param w 新的宽度
     * @param h 新的高度
     * @return Bitmap
     */
    fun scaleWithWH(bitmap: Bitmap?, w: Double, h: Double): Bitmap? {
        return if (w == 0.0 || h == 0.0 || bitmap == null) {
            bitmap
        } else {
            val width = bitmap.width
            val height = bitmap.height
            val matrix = Matrix()
            val scaleWidth = (w / width).toFloat()
            val scaleHeight = (h / height).toFloat()
            matrix.postScale(scaleWidth, scaleHeight)
            Bitmap.createBitmap(
                bitmap, 0, 0, width, height,
                matrix, true
            )
        }
    }

    /**
     * YUV视频流格式转bitmap
     *
     * @param data YUV视频流格式
     * @return width 设置高度
     */
    fun getBitmap(data: ByteArray?, width: Int, height: Int): Bitmap? {
        val yuvimage = YuvImage(data, ImageFormat.NV21, width, height, null)
        //data是onPreviewFrame参数提供
        val baos = ByteArrayOutputStream()
        yuvimage.compressToJpeg(Rect(0, 0, yuvimage.width, yuvimage.height), 100, baos) //
        // 80--JPG图片的质量[0-100],100最高
        val rawImage = baos.toByteArray()
        val options = BitmapFactory.Options()
        val softRef =
            SoftReference(BitmapFactory.decodeByteArray(rawImage, 0, rawImage.size, options))
        return softRef.get()
    }

    /**
     * 图片资源文件转bitmap
     *
     * @param context
     * @param resId
     * @return bitmap
     */
    fun getBitmapResources(context: Context, resId: Int): Bitmap? {
        return BitmapFactory.decodeResource(context.resources, resId)
    }

    /**
     * 将图片路径转Bitmap
     *
     * @return Bitmap
     * @Param path 图片路径
     */
    fun getBitmapPath(path: String?): Bitmap? {
        return BitmapFactory.decodeFile(path)
    }

    /**
     * bitmap保存到指定路径
     *
     * @param path 图片的绝对路径
     * @param bmp  位图
     * @return bitmap
     */
    fun saveFile(path: String?, bmp: Bitmap?): Boolean {
        if (TextUtils.isEmpty(path) || bmp == null) return false
        val file = File(path)
        if (file.exists()) {
            file.delete()
        } else {
            val p = file.parentFile
            if (!p.exists()) {
                p.mkdirs()
            }
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        var fos: FileOutputStream? = null
        var bos: BufferedOutputStream? = null
        try {
            fos = FileOutputStream(file)
            bos = BufferedOutputStream(fos)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            bos.flush()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } finally {
            if (bos != null) {
                try {
                    bos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return true
    }

    /**
     * 回收一个未被回收的Bitmap
     *
     * @param bitmap
     */
    fun doRecycledIfNot(bitmap: Bitmap) {
        if (!bitmap.isRecycled) {
            bitmap.recycle()
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
    fun scaleImage(resBitmap: Bitmap, desWidth: Int, desHeight: Int): Bitmap? {
        val resWidth = resBitmap.width
        val resHeight = resBitmap.height
        if (resHeight > desHeight || resWidth > desWidth) {
            // 计算出实际宽高和目标宽高的比率
            val heightRatio = desHeight.toFloat() / resHeight.toFloat()
            val widthRatio = desWidth.toFloat() / resWidth.toFloat()
            val scale = if (heightRatio < widthRatio) heightRatio else widthRatio
            i(TAG, "scaleImage scale*****$scale")
            //開平方會有一點損失精度
            val prescription = Math.sqrt(scale.toDouble()).toFloat()
            //再次开平方
            val prescription2 = Math.sqrt(prescription.toDouble()).toFloat()
            //            LogManager.INSTANCE.i(TAG, "scaleImage prescription*****" + prescription);
            i(
                TAG,
                "scaleImage prescription2*****$prescription2"
            )
            var bitmap: Bitmap? = null
            //循環壓縮bitmap，防止一次壓縮bitmap卡頓問題（子線程把主線成卡頓了）
            for (i in 0..3) {
                bitmap = bitmap?.let {
                    //                    bitmap = scale(bitmap, prescription);
                    scale(it, prescription2)
                }
                    ?: //                    bitmap = scale(resBitmap, prescription);
                            scale(resBitmap, prescription2)
                //                LogManager.INSTANCE.i(TAG, i + "scaleImage prescription*****" + prescription);
                i(TAG, i.toString() + "scaleImage prescription2*****" + prescription2)
            }
            return bitmap
        }
        return resBitmap
    }

    /**
     * 等比压缩图片
     *
     * @param bitmap 原图
     * @param scale  压缩因子
     * @return 压缩后的图片
     */
    private fun scale(bitmap: Bitmap, scale: Float): Bitmap {
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * 尺寸缩放
     *
     * @param bitmap bitmap
     * @param w      width
     * @param h      height
     * @return scaleBitmap
     */
    fun scale(bitmap: Bitmap?, w: Int, h: Int): Bitmap? {
        if (bitmap == null) {
            return null
        }
        val width = bitmap.width
        val height = bitmap.height
        val matrix = Matrix()
        val scaleWidth = w.toFloat() / width
        val scaleHeight = h.toFloat() / height
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

    /**
     * byte转Uri
     *
     * @param context
     * @param data
     * @return
     */
    fun saveTakePictureImage(context: Context, data: ByteArray?): Uri? {
        var file = context.getExternalFilesDir("file1")
        file = File(file?.absolutePath + File.separator + System.currentTimeMillis() + ".jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            fos.write(data)
            fos.flush()
        } catch (e: Exception) {
            e.printStackTrace()

            // 异常时删除保存失败的文件
            try {
                if (file != null && file.exists() && file.isFile) {
                    file.delete()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return null
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        Log.e(TAG, file.absolutePath)
        return Uri.fromFile(file)
    }

    /**
     * byte转成bitmap
     *
     * @param data
     * @param width
     * @param height
     * @return
     */
    fun yuvToBitmap(data: ByteArray, width: Int, height: Int): Bitmap? {
        val frameSize = width * height
        val rgba = IntArray(frameSize)
        for (i in 0 until height) {
            for (j in 0 until width) {
                var y = 0xff and data[i * width + j].toInt()
                val u = 0xff and data[frameSize + (i shr 1) * width + (j and 1.inv()) + 0]
                    .toInt()
                val v = 0xff and data[frameSize + (i shr 1) * width + (j and 1.inv()) + 1]
                    .toInt()
                y = if (y < 16) 16 else y
                var r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128))
                var g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128))
                var b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128))
                r = if (r < 0) 0 else if (r > 255) 255 else r
                g = if (g < 0) 0 else if (g > 255) 255 else g
                b = if (b < 0) 0 else if (b > 255) 255 else b
                rgba[i * width + j] = -0x1000000 + (b shl 16) + (g shl 8) + r
            }
        }
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bmp.setPixels(rgba, 0, width, 0, 0, width, height)
        return bmp
    }

    /**
     * byte转成bitmap
     *
     * @param depthBytes
     * @param width
     * @param height
     * @return
     */
    fun depthToBitmap(depthBytes: ByteArray, width: Int, height: Int): Bitmap? {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val argbData = IntArray(width * height)
        for (i in 0 until width * height) {
            argbData[i] =
                ((depthBytes[i * 2].toInt() + depthBytes[i * 2 + 1] * 256) / 10 and 0x000000ff
                        or ((depthBytes[i * 2].toInt() + depthBytes[i * 2 + 1] * 256) / 10 and 0x000000ff shl 8
                        ) or ((depthBytes[i * 2].toInt() + depthBytes[i * 2 + 1] * 256) / 10 and 0x000000ff shl 16
                        ) or -0x1000000)
        }
        bitmap.setPixels(argbData, 0, width, 0, 0, width, height)
        return bitmap
    }

    /**
     * byte转bitmap
     *
     * @param bytes
     * @param width
     * @param height
     * @return
     */
    fun rGBToBitmap(bytes: ByteArray, width: Int, height: Int): Bitmap? {
        // use Bitmap.Config.ARGB_8888 instead of type is OK
        val stitchBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val rgba = ByteArray(width * height * 4)
        for (i in 0 until width * height) {
            val b1 = bytes[i * 3 + 0]
            val b2 = bytes[i * 3 + 1]
            val b3 = bytes[i * 3 + 2]
            // set value
            rgba[i * 4 + 0] = b1
            rgba[i * 4 + 1] = b2
            rgba[i * 4 + 2] = b3
            rgba[i * 4 + 3] = 255.toByte()
        }
        stitchBmp.copyPixelsFromBuffer(ByteBuffer.wrap(rgba))
        return stitchBmp
    }

    /**
     * byte转bitmap
     *
     * @param bytes
     * @param width
     * @param height
     * @return
     */
    fun bGRToBitmap(bytes: ByteArray, width: Int, height: Int): Bitmap? {
        // use Bitmap.Config.ARGB_8888 instead of type is OK
        val stitchBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val rgba = ByteArray(width * height * 4)
        for (i in 0 until width * height) {
            val b1 = bytes[i * 3 + 0]
            val b2 = bytes[i * 3 + 1]
            val b3 = bytes[i * 3 + 2]
            // set value
            rgba[i * 4 + 0] = b3
            rgba[i * 4 + 1] = b2
            rgba[i * 4 + 2] = b1
            rgba[i * 4 + 3] = 255.toByte()
        }
        stitchBmp.copyPixelsFromBuffer(ByteBuffer.wrap(rgba))
        return stitchBmp
    }

    fun aRGBToR(bytes: ByteArray, width: Int, height: Int): ByteArray? {
        val IR = ByteArray(width * height)
        for (i in 0 until width * height) {
            IR[i] = bytes[i * 4]
        }
        return IR
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
    fun getBitmap(path: String?): Bitmap? {
        var fis: FileInputStream? = null
        var bis: BufferedInputStream? = null
        var bm: Bitmap? = null
        try {
            fis = FileInputStream(path)
            val options = BitmapFactory.Options()
            //            options.inSampleSize = 8;//图片的长宽都是原来的1/8
            bis = BufferedInputStream(fis)
            bm = BitmapFactory.decodeStream(bis, null, options)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            try {
                bis?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return bm
    }

    /**
     * @param bitmap
     * @return
     */
    fun bitmapToInputStream(bitmap: Bitmap): InputStream {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return ByteArrayInputStream(
            baos.toByteArray()
        )
    }

    /**
     * 这个是把文件变成二进制流
     *
     * @param imagepath
     * @return
     * @throws Exception
     */
    fun readStream(imagepath: String?): ByteArray? {
        var fis: FileInputStream? = null
        var bis: BufferedInputStream? = null
        var baos: ByteArrayOutputStream? = null
        try {
            fis = FileInputStream(imagepath)
            bis = BufferedInputStream(fis)
            baos = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len = 0
            while (-1 != bis.read(buffer).also { len = it }) {
                baos.write(buffer, 0, len)
            }
            //刷新此输出流并强制写出所有缓冲的输出字节
            baos.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bis?.close()
                baos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return baos?.toByteArray()
    }

    /**
     * 将Bitmap转换成文件
     * 保存文件
     *
     * @param bitmap
     * @param fileName
     * @throws IOException
     */
    fun saveFile(bitmap: Bitmap, dirsPath: String, fileName: String): File {
        val dirs = File(dirsPath)
        if (!dirs.exists()) {
            dirs.mkdirs()
        }
        val captureFile = File(dirs, fileName)
        var bos: BufferedOutputStream? = null
        try {
            bos = BufferedOutputStream(FileOutputStream(captureFile))
            //刷新此输出流并强制写出所有缓冲的输出字节
            bos.flush()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (bos != null) {
                try {
                    bos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return captureFile
    }

    /**
     * 读取文件的大小
     */
    fun getFileSize(file: File): Long {
        var size: Long = 0
        if (file.exists()) {
            var fis: FileInputStream? = null
            var bis: BufferedInputStream? = null
            try {
                fis = FileInputStream(file)
                bis = BufferedInputStream(fis)
                size = bis.available().toLong()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (bis != null) {
                    try {
                        bis.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            return size
        }
        return 0
    }

    /**
     * 返回byte的数据大小对应的文本
     *
     * @param size
     * @return
     */
    fun getDataSize(size: Long): String? {
        val formater = DecimalFormat("####.00")
        return if (size < 1024) {
            size.toString() + "bytes"
        } else if (size < 1024 * 1024) {
            val kbsize = size / 1024f
            formater.format(kbsize.toDouble()) + "KB"
        } else if (size < 1024 * 1024 * 1024) {
            val mbsize = size / 1024f / 1024f
            formater.format(mbsize.toDouble()) + "MB"
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            val gbsize = size / 1024f / 1024f / 1024f
            formater.format(gbsize.toDouble()) + "GB"
        } else {
            "size: error"
        }
    }

    /**
     * 使用Compressor IO模式自定义压缩
     *
     * @param path .setMaxWidth(640).setMaxHeight(480)这两个数值越高，压缩力度越小，图片也不清晰
     * .setQuality(75)这个方法只是设置图片质量，并不影响压缩图片的大小KB
     * .setCompressFormat(Bitmap.CompressFormat.WEBP) WEBP图片格式是Google推出的 压缩强，质量 高，但是IOS不识别，需要把图片转为字节流然后转PNG格式
     * .setCompressFormat(Bitmap.CompressFormat.PNG)PNG格式的压缩，会导致图片变大，并耗过大的内 存，手机反应缓慢
     * .setCompressFormat(Bitmap.CompressFormat.JPEG)JPEG压缩；压缩速度比PNG快，质量一般，基本上属于1/10的压缩比例
     */
    fun initCompressorIO(context: Context?, path: String?, dirsPath: String?): File? {
        val dirs2 = File(dirsPath)
        if (!dirs2.exists()) {
            dirs2.mkdirs()
        }
        var file: File? = null
        try {
            file = Compressor(context) //                    .setMaxWidth(1280)
                //                    .setMaxHeight(960)
                .setQuality(50)
                .setCompressFormat(Bitmap.CompressFormat.PNG)
                .setDestinationDirectoryPath(dirsPath)
                .compressToFile(File(path))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    fun getAssetFile(context: Context, dirsPath: String, assetFileName: String): File {
        val assetManager = context.assets
        val dirs = File(dirsPath)
        if (!dirs.exists()) {
            dirs.mkdirs()
        }
        val file = File(dirs, assetFileName)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        var `is`: InputStream? = null
        var bis: BufferedInputStream? = null
        var fos: FileOutputStream? = null
        var bos: BufferedOutputStream? = null
        try {
            `is` = assetManager.open(assetFileName)
            bis = BufferedInputStream(`is`)
            fos = FileOutputStream(file)
            bos = BufferedOutputStream(fos)
            val buffer = ByteArray(1024)
            var len = 0
            while (-1 != bis.read(buffer).also { len = it }) {
                bos.write(buffer, 0, len)
            }
            //刷新此输出流并强制写出所有缓冲的输出字节
            bos.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (bis != null) {
                try {
                    bis.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (bos != null) {
                try {
                    bos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return file
    }

    /**
     * base64转bitmap
     *
     * @param base64String
     * @return
     */
    fun base64ToBitmap(base64String: String?): Bitmap? {
        var base64String = base64String
        base64String = Uri.decode(base64String)
        val decode =
            Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decode, 0, decode.size)
    }

}