package com.phone.library_common.custom_view.rclayout

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import com.phone.library_common.R

class RCHelper(val context: Context, val attrs: AttributeSet?) {

    var radii = FloatArray(8) // top-left, top-right, bottom-right, bottom-left
    lateinit var mClipPath // 剪裁区域路径
            : Path
    lateinit var mPaint // 画笔
            : Paint
    var mRoundAsCircle = false // 圆形

    var mDefaultStrokeColor // 默认描边颜色
            = 0
    var mStrokeColor // 描边颜色
            = 0
    var mStrokeColorStateList // 描边颜色的状态
            : ColorStateList? = null
    var mStrokeWidth // 描边半径
            = 0
    var mClipBackground // 是否剪裁背景
            = false
    lateinit var mAreaRegion // 内容区域
            : Region
    lateinit var mLayer // 画布图层大小
            : RectF

    //--- Selector 支持 ----------------------------------------------------------------------------
    var mChecked // 是否是 check 状态
            = false
    var mOnCheckedChangeListener: OnCheckedChangeListener? = null

    init {
        initAttrs()
    }

    fun initAttrs() {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RCAttrs)
        mRoundAsCircle = ta.getBoolean(R.styleable.RCAttrs_round_as_circle, false)
        mStrokeColorStateList = ta.getColorStateList(R.styleable.RCAttrs_stroke_color)
        if (mStrokeColorStateList != null) {
            mStrokeColorStateList?.let {
                mStrokeColor = it.defaultColor
                mDefaultStrokeColor = it.defaultColor
            }
        } else {
            mStrokeColor = Color.WHITE
            mDefaultStrokeColor = Color.WHITE
        }
        mStrokeWidth = ta.getDimensionPixelSize(R.styleable.RCAttrs_stroke_width, 0)
        mClipBackground = ta.getBoolean(R.styleable.RCAttrs_clip_background, false)
        val roundCorner = ta.getDimensionPixelSize(R.styleable.RCAttrs_round_corner, 0)
        val roundCornerTopLeft = ta.getDimensionPixelSize(
            R.styleable.RCAttrs_round_corner_top_left, roundCorner
        )
        val roundCornerTopRight = ta.getDimensionPixelSize(
            R.styleable.RCAttrs_round_corner_top_right, roundCorner
        )
        val roundCornerBottomLeft = ta.getDimensionPixelSize(
            R.styleable.RCAttrs_round_corner_bottom_left, roundCorner
        )
        val roundCornerBottomRight = ta.getDimensionPixelSize(
            R.styleable.RCAttrs_round_corner_bottom_right, roundCorner
        )
        ta.recycle()
        radii[0] = roundCornerTopLeft.toFloat()
        radii[1] = roundCornerTopLeft.toFloat()
        radii[2] = roundCornerTopRight.toFloat()
        radii[3] = roundCornerTopRight.toFloat()
        radii[4] = roundCornerBottomRight.toFloat()
        radii[5] = roundCornerBottomRight.toFloat()
        radii[6] = roundCornerBottomLeft.toFloat()
        radii[7] = roundCornerBottomLeft.toFloat()
        mLayer = RectF()
        mClipPath = Path()
        mAreaRegion = Region()
        mPaint = Paint()
        mPaint.color = Color.WHITE
        mPaint.isAntiAlias = true
    }

    fun onSizeChanged(view: View, w: Int, h: Int) {
        mLayer[0f, 0f, w.toFloat()] = h.toFloat()
        refreshRegion(view)
    }

    fun refreshRegion(view: View) {
        val w = mLayer.width().toInt()
        val h = mLayer.height().toInt()
        val areas = RectF()
        areas.left = view.paddingLeft.toFloat()
        areas.top = view.paddingTop.toFloat()
        areas.right = (w - view.paddingRight).toFloat()
        areas.bottom = (h - view.paddingBottom).toFloat()
        mClipPath.reset()
        if (mRoundAsCircle) {
            val d = if (areas.width() >= areas.height()) areas.height() else areas.width()
            val r = d / 2
            val center = PointF((w / 2).toFloat(), (h / 2).toFloat())
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
                mClipPath.addCircle(center.x, center.y, r, Path.Direction.CW)
                mClipPath.moveTo(0f, 0f) // 通过空操作让Path区域占满画布
                mClipPath.moveTo(w.toFloat(), h.toFloat())
            } else {
                val y = h / 2 - r
                mClipPath.moveTo(areas.left, y)
                mClipPath.addCircle(center.x, y + r, r, Path.Direction.CW)
            }
        } else {
            mClipPath.addRoundRect(areas, radii, Path.Direction.CW)
        }
        val clip = Region(
            areas.left.toInt(), areas.top.toInt(),
            areas.right.toInt(), areas.bottom.toInt()
        )
        mAreaRegion.setPath(mClipPath, clip)
    }

    fun onClipDraw(canvas: Canvas) {
        if (mStrokeWidth > 0) {
            // 支持半透明描边，将与描边区域重叠的内容裁剪掉
            mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
            mPaint.color = Color.WHITE
            mPaint.strokeWidth = (mStrokeWidth * 2).toFloat()
            mPaint.style = Paint.Style.STROKE
            canvas.drawPath(mClipPath, mPaint)
            // 绘制描边
            mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
            mPaint.color = mStrokeColor
            mPaint.style = Paint.Style.STROKE
            canvas.drawPath(mClipPath, mPaint)
        }
        mPaint.color = Color.WHITE
        mPaint.style = Paint.Style.FILL
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
            canvas.drawPath(mClipPath, mPaint)
        } else {
            mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
            val path = Path()
            path.addRect(
                0F,
                0F,
                mLayer.width(),
                mLayer.height(),
                Path.Direction.CW
            )
            path.op(mClipPath, Path.Op.DIFFERENCE)
            canvas.drawPath(path, mPaint)
        }
    }


    fun drawableStateChanged(view: View) {
        if (view is RCAttrs) {
            val stateListArray = ArrayList<Int>()
            if (view is Checkable) {
                stateListArray.add(android.R.attr.state_checkable)
                if ((view as Checkable).isChecked) stateListArray.add(android.R.attr.state_checked)
            }
            if (view.isEnabled) stateListArray.add(android.R.attr.state_enabled)
            if (view.isFocused) stateListArray.add(android.R.attr.state_focused)
            if (view.isPressed) stateListArray.add(android.R.attr.state_pressed)
            if (view.isHovered) stateListArray.add(android.R.attr.state_hovered)
            if (view.isSelected) stateListArray.add(android.R.attr.state_selected)
            if (view.isActivated) stateListArray.add(android.R.attr.state_activated)
            if (view.hasWindowFocus()) stateListArray.add(android.R.attr.state_window_focused)
            if (mStrokeColorStateList != null) {
                mStrokeColorStateList?.let {
                    if (it.isStateful) {
                        val stateList = IntArray(stateListArray.size)
                        for (i in stateListArray.indices) {
                            stateList[i] = stateListArray[i]
                        }
                        val stateColor =
                            it.getColorForState(stateList, mDefaultStrokeColor)
                        (view as RCAttrs).setStrokeColor(stateColor)
                    }
                }
            }
        }
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(view: View?, isChecked: Boolean)
    }

}