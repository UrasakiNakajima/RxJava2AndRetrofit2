package com.phone.library_glide

import android.content.Context
import android.graphics.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import java.security.MessageDigest

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2021/3/31 10:50
 * introduce :
 */

class GlideRoundTransform(
    context: Context,
    leftTop_radius: Float,
    leftBottom_radius: Float,
    rightTop_radius: Float,
    rightBottom_radius: Float
) : Transformation<Bitmap> {

    private var mBitmapPool: BitmapPool
    private var leftTop_radius = 0f
    private var leftBottom_radius = 0f
    private var rightTop_radius = 0f
    private var rightBottom_radius = 0f
    private var isLeftTop = false
    private var isRightTop = false
    private var isLeftBottom = false
    private var isRightBottom = false

    init {
        mBitmapPool = Glide.get(context).bitmapPool
        this.leftTop_radius = leftTop_radius
        if (leftTop_radius != 0f) {
            isLeftTop = true
        }
        this.leftBottom_radius = leftBottom_radius
        if (leftBottom_radius != 0f) {
            isLeftBottom = true
        }
        this.rightTop_radius = rightTop_radius
        if (rightTop_radius != 0f) {
            isRightTop = true
        }
        this.rightBottom_radius = rightBottom_radius
        if (rightBottom_radius != 0f) {
            isRightBottom = true
        }
    }

    override fun transform(
        context: Context,
        resource: Resource<Bitmap?>,
        outWidth: Int,
        outHeight: Int
    ): Resource<Bitmap?> {
        val source = resource.get()
        var finalWidth: Int
        var finalHeight: Int
        //输出目标的宽高或高宽比例
        var scale: Float
        if (outWidth > outHeight) {
            //如果 输出宽度 > 输出高度 求高宽比
            scale = outHeight.toFloat() / outWidth.toFloat()
            finalWidth = source.width
            //固定原图宽度,求最终高度
            finalHeight = (source.width.toFloat() * scale).toInt()
            if (finalHeight > source.height) {
                //如果 求出的最终高度 > 原图高度 求宽高比
                scale = outWidth.toFloat() / outHeight.toFloat()
                finalHeight = source.height
                //固定原图高度,求最终宽度
                finalWidth = (source.height.toFloat() * scale).toInt()
            }
        } else if (outWidth < outHeight) {
            //如果 输出宽度 < 输出高度 求宽高比
            scale = outWidth.toFloat() / outHeight.toFloat()
            finalHeight = source.height
            //固定原图高度,求最终宽度
            finalWidth = (source.height.toFloat() * scale).toInt()
            if (finalWidth > source.width) {
                //如果 求出的最终宽度 > 原图宽度 求高宽比
                scale = outHeight.toFloat() / outWidth.toFloat()
                finalWidth = source.width
                finalHeight = (source.width.toFloat() * scale).toInt()
            }
        } else {
            //如果 输出宽度=输出高度
            finalHeight = source.height
            finalWidth = finalHeight
        }

        //修正圆角
        leftTop_radius *= finalHeight.toFloat() / outHeight.toFloat()
        leftBottom_radius *= finalHeight.toFloat() / outHeight.toFloat()
        rightTop_radius *= finalHeight.toFloat() / outHeight.toFloat()
        rightBottom_radius *= finalHeight.toFloat() / outHeight.toFloat()
        var outBitmap: Bitmap? = mBitmapPool[finalWidth, finalHeight, Bitmap.Config.ARGB_8888]
        if (outBitmap == null) {
            outBitmap = Bitmap.createBitmap(finalWidth, finalHeight, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(outBitmap!!)
        val paint = Paint()
        //关联画笔绘制的原图bitmap
        val shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        //计算中心位置,进行偏移
        val width = (source.width - finalWidth) / 2
        val height = (source.height - finalHeight) / 2
        if (width != 0 || height != 0) {
            val matrix = Matrix()
            matrix.setTranslate((-width).toFloat(), (-height).toFloat())
            shader.setLocalMatrix(matrix)
        }
        paint.shader = shader
        paint.isAntiAlias = true
        val rectF = RectF(
            0.0f, 0.0f, canvas.width.toFloat(),
            canvas.height.toFloat()
        )
        val outerR = floatArrayOf(
            leftTop_radius,
            leftTop_radius,
            rightTop_radius,
            rightTop_radius,
            rightBottom_radius,
            rightBottom_radius,
            leftBottom_radius,
            leftBottom_radius
        ) //左上，右上，右下，左下
        val path = Path()
        path.addRoundRect(rectF, outerR, Path.Direction.CW)
        canvas.drawPath(path, paint)
        return BitmapResource.obtain(outBitmap, mBitmapPool)!!
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
    }

}