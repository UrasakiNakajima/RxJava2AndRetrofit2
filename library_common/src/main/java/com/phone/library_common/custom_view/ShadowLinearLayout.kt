package com.phone.library_common.custom_view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.*
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.phone.library_common.R
import com.phone.library_common.manager.GlideRoundManager

class ShadowLinearLayout(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var clickAbleFalseDrawable: Drawable? = null
    private var clickAbleFalseColor = -101

    private var layoutBackground: Drawable? = null
    private var layoutBackground_true: Drawable? = null
    private lateinit var firstView: View

    private var mBackGroundColor = 0
    private var mBackGroundColor_true = -101
    private var mShadowColor = 0
    private var mShadowLimit = 0f
    private var mCornerRadius = 0f
    private var mDx = 0f
    private var mDy = 0f
    private var leftShow = false
    private var rightShow = false
    private var topShow = false
    private var bottomShow = false
    private lateinit var shadowPaint: Paint
    private lateinit var paint: Paint

    private var leftPadding = 0
    private var topPadding = 0
    private var rightPadding = 0
    private var bottomPadding = 0

    //阴影布局子空间区域
    private val rectf = RectF()

    //ShadowLinearLayout的样式，是只需要pressed还是selected。默认是pressed.
    private var selectorType = 1
    private var isShowShadow = true
    private var isSym = false

    //增加各个圆角的属性
    private var mCornerRadius_leftTop = 0f
    private var mCornerRadius_rightTop = 0f
    private var mCornerRadius_leftBottom = 0f
    private var mCornerRadius_rightBottom = 0f

    //边框画笔
    private lateinit var paint_stroke: Paint
    private var stroke_with = 0f
    private var stroke_color = 0
    private var stroke_color_true = 0

    private var isMClickable = false


    //关于控件填充渐变色
    private var startColor = 0
    private var centerColor = 0
    private var endColor = 0
    private var angle = 0


    //在普遍情况，在点击或选中按钮时，很可能伴随着textView的字体颜色变化
    private var mTextViewResId = -1
    private lateinit var mTextView: TextView
    private var textColor = 0
    private var textColor_true = 0
    private var text: String? = null
    private var text_true: String? = null

    init {
        initView(context, null)
    }

    override fun setClickable(clickable: Boolean) {
        super.setClickable(clickable)
        isMClickable = clickable
        changeSwitchClickable()
        if (isMClickable) {
            super.setOnClickListener(onClickListener)
        }
        if (startColor != -101 && endColor != -101) {
            gradient(paint)
        }
    }


    //解决xml设置clickable = false时。代码设置true时，点击事件无效的bug
    private var onClickListener: OnClickListener? = null

    override fun setOnClickListener(l: OnClickListener?) {
        onClickListener = l
        if (isMClickable) {
            super.setOnClickListener(l)
        }
    }

    fun changeSwitchClickable() {
        //不可点击的状态只在press mode的模式下生效
        if (selectorType == 1 && firstView != null) {

            //press mode
            if (!isMClickable) {
                //不可点击的状态。
                if (clickAbleFalseColor != -101) {
                    //说明设置了颜色
                    if (layoutBackground != null) {
                        //说明此时是设置了图片的模式
                        firstView.background.alpha = 0
                    }
                    paint.color = clickAbleFalseColor
                    postInvalidate()
                } else if (clickAbleFalseDrawable != null) {
                    //说明设置了背景图
                    setmBackGround(clickAbleFalseDrawable)
                    paint.color = Color.parseColor("#00000000")
                    postInvalidate()
                }
            } else {
                //可点击的状态
                if (layoutBackground != null) {
                    setmBackGround(layoutBackground)
                } else {
                    if (firstView.background != null) {
                        firstView.background.alpha = 0
                    }
                }
                paint.color = mBackGroundColor
                postInvalidate()
            }
        }
    }

    //增加selector样式
    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        if (width != 0) {
            if (selectorType == 2) {
                if (selected) {
                    if (mBackGroundColor_true != -101) {
                        paint.color = mBackGroundColor_true
                    }
                    paint.shader = null
                    if (stroke_color_true != -101) {
                        paint_stroke.color = stroke_color_true
                    }
                    if (layoutBackground_true != null) {
                        setmBackGround(layoutBackground_true)
                    }
                    if (mTextView != null) {
                        mTextView.setTextColor(textColor_true)
                        if (!TextUtils.isEmpty(text_true)) {
                            mTextView.text = text_true
                        }
                    }
                } else {
                    paint.color = mBackGroundColor
                    if (startColor != -101) {
                        gradient(paint)
                    }
                    if (stroke_color != -101) {
                        paint_stroke.color = stroke_color
                    }
                    if (layoutBackground != null) {
                        setmBackGround(layoutBackground)
                    }
                    if (mTextView != null) {
                        mTextView.setTextColor(textColor)
                        if (!TextUtils.isEmpty(text)) {
                            mTextView.text = text
                        }
                    }
                }
                postInvalidate()
            }
        } else {
            addOnLayoutChangeListener(object : OnLayoutChangeListener {
                override fun onLayoutChange(
                    v: View,
                    left: Int,
                    top: Int,
                    right: Int,
                    bottom: Int,
                    oldLeft: Int,
                    oldTop: Int,
                    oldRight: Int,
                    oldBottom: Int
                ) {
                    removeOnLayoutChangeListener(this)
                    isSelected = isSelected
                }
            })
        }
    }


    //是否隐藏阴影
    fun setShadowHidden(isShowShadow: Boolean) {
        this.isShowShadow = !isShowShadow
        if (width != 0 && height != 0) {
            setBackgroundCompat(width, height)
        }
    }

    //设置x轴偏移量
    fun setShadowOffsetX(mDx: Float) {
        if (isShowShadow) {
            if (Math.abs(mDx) > mShadowLimit) {
                if (mDx > 0) {
                    this.mDx = mShadowLimit
                } else {
                    this.mDx = -mShadowLimit
                }
            } else {
                this.mDx = mDx
            }
            setPadding()
        }
    }

    //动态设置y轴偏移量
    fun setShadowOffsetY(mDy: Float) {
        if (isShowShadow) {
            if (Math.abs(mDy) > mShadowLimit) {
                if (mDy > 0) {
                    this.mDy = mShadowLimit
                } else {
                    this.mDy = -mShadowLimit
                }
            } else {
                this.mDy = mDy
            }
            setPadding()
        }
    }


    fun getCornerRadius(): Float {
        return mCornerRadius
    }

    //动态设置 圆角属性
    fun setCornerRadius(mCornerRadius: Int) {
        this.mCornerRadius = mCornerRadius.toFloat()
        if (width != 0 && height != 0) {
            setBackgroundCompat(width, height)
        }
    }

    fun getShadowLimit(): Float {
        return mShadowLimit
    }

    //动态设置阴影扩散区域
    fun setShadowLimit(mShadowLimit: Int) {
        if (isShowShadow) {
            val dip5 = context.resources.getDimension(R.dimen.dp_5).toInt()
            if (mShadowLimit >= dip5) {
                this.mShadowLimit = mShadowLimit.toFloat()
            } else {
                this.mShadowLimit = dip5.toFloat()
            }
            setPadding()
        }
    }

    //动态设置阴影颜色值
    fun setShadowColor(mShadowColor: Int) {
        this.mShadowColor = mShadowColor
        if (width != 0 && height != 0) {
            setBackgroundCompat(width, height)
        }
    }


    fun setSpecialCorner(leftTop: Int, rightTop: Int, leftBottom: Int, rightBottom: Int) {
        mCornerRadius_leftTop = leftTop.toFloat()
        mCornerRadius_rightTop = rightTop.toFloat()
        mCornerRadius_leftBottom = leftBottom.toFloat()
        mCornerRadius_rightBottom = rightBottom.toFloat()
        if (width != 0 && height != 0) {
            setBackgroundCompat(width, height)
        }
    }


    //是否隐藏阴影的上边部分
    fun setShadowHiddenTop(topShow: Boolean) {
        this.topShow = !topShow
        setPadding()
    }

    fun setShadowHiddenBottom(bottomShow: Boolean) {
        this.bottomShow = !bottomShow
        setPadding()
    }


    fun setShadowHiddenRight(rightShow: Boolean) {
        this.rightShow = !rightShow
        setPadding()
    }


    fun setShadowHiddenLeft(leftShow: Boolean) {
        this.leftShow = !leftShow
        setPadding()
    }


    fun setLayoutBackground(color: Int) {
        if (layoutBackground_true != null) {
            throw UnsupportedOperationException("使用了ShadowLinearLayout_hl_layoutBackground_true属性，要与ShadowLinearLayout_hl_layoutBackground属性统一为颜色")
        }
        mBackGroundColor = color
        if (selectorType == 2) {
            //select模式
            if (!this.isSelected) {
                paint.color = mBackGroundColor
            }
        } else {
            paint.color = mBackGroundColor
        }
        postInvalidate()
    }


    fun setLayoutBackgroundTrue(color: Int) {
        if (layoutBackground != null) {
            throw UnsupportedOperationException("使用了ShadowLinearLayout_hl_layoutBackground属性，要与ShadowLinearLayout_hl_layoutBackground_true属性统一为颜色")
        }
        mBackGroundColor_true = color
        if (selectorType == 2) {
            //select模式
            if (this.isSelected) {
                paint.color = mBackGroundColor_true
            }
        }
        postInvalidate()
    }


    fun setStrokeColor(color: Int) {
        stroke_color = color
        if (selectorType == 2) {
            //select模式
            if (!this.isSelected) {
                paint_stroke.color = stroke_color
            }
        } else {
            paint_stroke.color = stroke_color
        }
        postInvalidate()
    }


    fun setStrokeColorTrue(color: Int) {
        stroke_color_true = color
        if (selectorType == 2) {
            //select模式
            if (this.isSelected) {
                paint_stroke.color = stroke_color_true
            }
        }
        postInvalidate()
    }

    fun setStrokeWidth(stokeWidth: Int) {
        stroke_with = stokeWidth.toFloat()
        if (stroke_with > dip2px(7f)) {
            stroke_with = dip2px(5f).toFloat()
        }
        paint_stroke.strokeWidth = stroke_with
        postInvalidate()
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun onFinishInflate() {
        super.onFinishInflate()
        if (mTextViewResId != -1) {
            mTextView = findViewById(mTextViewResId)
            if (mTextView == null) {
                throw NullPointerException("ShadowLinearLayout找不到hl_bindTextView，请确保绑定的资源id在ShadowLinearLayout内")
            } else {
                if (textColor == -101) {
                    textColor = mTextView.currentTextColor
                }
                if (textColor_true == -101) {
                    textColor_true = mTextView.currentTextColor
                }
                mTextView.setTextColor(textColor)
                if (!TextUtils.isEmpty(text)) {
                    mTextView.text = text
                }
            }
        }
        firstView = getChildAt(0)
        if (firstView == null) {
            firstView = this@ShadowLinearLayout
            //当子View都没有的时候。默认不使用阴影
            isShowShadow = false
        }
        if (firstView != null) {

            //selector样式不受clickable的影响
            if (selectorType == 2) {
                //if (this.isSelected()) {
                //这个方法内已经判断了是否为空
                //  setmBackGround(layoutBackground_true);
                //} else {
                //  setmBackGround(layoutBackground);
                //  }
            } else {
                if (isMClickable) {
                    setmBackGround(layoutBackground)
                } else {
                    setmBackGround(clickAbleFalseDrawable)
                    if (clickAbleFalseColor != -101) {
                        paint.color = clickAbleFalseColor
                    }
                }
            }
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            setBackgroundCompat(w, h)
            if (startColor != -101) {
                gradient(paint)
            }
        }
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        initAttributes(attrs)
        shadowPaint = Paint()
        shadowPaint.isAntiAlias = true
        shadowPaint.style = Paint.Style.FILL
        paint_stroke = Paint()
        paint_stroke.isAntiAlias = true
        paint_stroke.style = Paint.Style.STROKE
        paint_stroke.strokeWidth = stroke_with
        if (stroke_color != -101) {
            paint_stroke.color = stroke_color
        }


        //矩形画笔
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.FILL
        //打个标记
        paint.color = mBackGroundColor
        setPadding()
    }

    //将画笔附上 渐变色
    fun gradient(paint: Paint) {
        if (!isMClickable) {
            paint.shader = null
            return
        }

        //左上 x,y   leftPadding, topPadding,
        //右下 x,y   getWidth() - rightPadding, getHeight() - bottomPadding
        val colors: IntArray
        colors = if (centerColor == -101) {
            intArrayOf(startColor, endColor)
        } else {
            intArrayOf(startColor, centerColor, endColor)
        }
        if (angle < 0) {
            val trueAngle = angle % 360
            angle = trueAngle + 360
        }

        //当设置的角度大于0的时候
        //这个要算出每隔45度
        val trueAngle = angle % 360
        val angleFlag = trueAngle / 45
        val linearGradient: LinearGradient
        when (angleFlag) {
            0 -> {
                linearGradient = LinearGradient(
                    leftPadding.toFloat(),
                    topPadding.toFloat(),
                    (width - rightPadding).toFloat(),
                    topPadding.toFloat(),
                    colors,
                    null,
                    Shader.TileMode.CLAMP
                )
                paint.shader = linearGradient
            }
            1 -> {
                linearGradient = LinearGradient(
                    leftPadding.toFloat(),
                    (height - bottomPadding).toFloat(),
                    (width - rightPadding).toFloat(),
                    topPadding.toFloat(),
                    colors,
                    null,
                    Shader.TileMode.CLAMP
                )
                paint.shader = linearGradient
            }
            2 -> {
                val x_ = (width - rightPadding - leftPadding) / 2 + leftPadding
                linearGradient = LinearGradient(
                    x_.toFloat(),
                    (height - bottomPadding).toFloat(),
                    x_.toFloat(),
                    topPadding.toFloat(),
                    colors,
                    null,
                    Shader.TileMode.CLAMP
                )
                paint.shader = linearGradient
            }
            3 -> {
                linearGradient = LinearGradient(
                    (width - rightPadding).toFloat(),
                    (height - bottomPadding).toFloat(),
                    leftPadding.toFloat(),
                    topPadding.toFloat(),
                    colors,
                    null,
                    Shader.TileMode.CLAMP
                )
                paint.shader = linearGradient
            }
            4 -> {
                linearGradient = LinearGradient(
                    (width - rightPadding).toFloat(),
                    topPadding.toFloat(),
                    leftPadding.toFloat(),
                    topPadding.toFloat(),
                    colors,
                    null,
                    Shader.TileMode.CLAMP
                )
                paint.shader = linearGradient
            }
            5 -> {
                linearGradient = LinearGradient(
                    (width - rightPadding).toFloat(),
                    topPadding.toFloat(),
                    leftPadding.toFloat(),
                    (height - bottomPadding).toFloat(),
                    colors,
                    null,
                    Shader.TileMode.CLAMP
                )
                paint.shader = linearGradient
            }
            6 -> {
                val x = (width - rightPadding - leftPadding) / 2 + leftPadding
                linearGradient = LinearGradient(
                    x.toFloat(),
                    topPadding.toFloat(),
                    x.toFloat(),
                    (height - bottomPadding).toFloat(),
                    colors,
                    null,
                    Shader.TileMode.CLAMP
                )
                paint.shader = linearGradient
            }
            7 -> {
                linearGradient = LinearGradient(
                    leftPadding.toFloat(),
                    topPadding.toFloat(),
                    (width - rightPadding).toFloat(),
                    (height - bottomPadding).toFloat(),
                    colors,
                    null,
                    Shader.TileMode.CLAMP
                )
                paint.shader = linearGradient
            }
        }
    }


    fun dip2px(dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    fun setPadding() {
        if (isShowShadow && mShadowLimit > 0) {
            //控件区域是否对称，默认是对称。不对称的话，那么控件区域随着阴影区域走
            if (isSym) {
                val xPadding = (mShadowLimit + Math.abs(mDx)).toInt()
                val yPadding = (mShadowLimit + Math.abs(mDy)).toInt()
                leftPadding = if (leftShow) {
                    xPadding
                } else {
                    0
                }
                topPadding = if (topShow) {
                    yPadding
                } else {
                    0
                }
                rightPadding = if (rightShow) {
                    xPadding
                } else {
                    0
                }
                bottomPadding = if (bottomShow) {
                    yPadding
                } else {
                    0
                }
            } else {
                if (Math.abs(mDy) > mShadowLimit) {
                    mDy = if (mDy > 0) {
                        mShadowLimit
                    } else {
                        0 - mShadowLimit
                    }
                }
                if (Math.abs(mDx) > mShadowLimit) {
                    mDx = if (mDx > 0) {
                        mShadowLimit
                    } else {
                        0 - mShadowLimit
                    }
                }
                topPadding = if (topShow) {
                    (mShadowLimit - mDy).toInt()
                } else {
                    0
                }
                bottomPadding = if (bottomShow) {
                    (mShadowLimit + mDy).toInt()
                } else {
                    0
                }
                rightPadding = if (rightShow) {
                    (mShadowLimit - mDx).toInt()
                } else {
                    0
                }
                leftPadding = if (leftShow) {
                    (mShadowLimit + mDx).toInt()
                } else {
                    0
                }
            }
            setPadding(leftPadding, topPadding, rightPadding, bottomPadding)

//            setPadding(dip2px(leftPadding), dip2px(topPadding),
//                    dip2px(rightPadding), dip2px(bottomPadding));
        }
    }

    private fun setBackgroundCompat(w: Int, h: Int) {
        if (isShowShadow) {
            //判断传入的颜色值是否有透明度
            isAddAlpha(mShadowColor)
            val bitmap = createShadowBitmap(
                w,
                h,
                mCornerRadius,
                mShadowLimit,
                mDx,
                mDy,
                mShadowColor,
                Color.TRANSPARENT
            )
            val drawable = BitmapDrawable(bitmap)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                setBackgroundDrawable(drawable)
            } else {
                background = drawable
            }
        } else {
            if (getChildAt(0) == null) {
                if (layoutBackground != null) {
                    firstView = this@ShadowLinearLayout
                    if (isMClickable) {
                        setmBackGround(layoutBackground)
                    } else {
                        changeSwitchClickable()
                    }
                } else {
                    //解决不执行onDraw方法的bug就是给其设置一个透明色
                    setBackgroundColor(Color.parseColor("#00000000"))
                }
            } else {
                setBackgroundColor(Color.parseColor("#00000000"))
            }
        }
    }


    private fun initAttributes(attrs: AttributeSet?) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.library_ShadowLayout)
            ?: return
        try {
            //默认是显示
            isShowShadow = !attr.getBoolean(R.styleable.library_ShadowLayout_library_hl_shadowHidden, false)
            leftShow = !attr.getBoolean(R.styleable.library_ShadowLayout_library_hl_shadowHiddenLeft, false)
            rightShow = !attr.getBoolean(R.styleable.library_ShadowLayout_library_hl_shadowHiddenRight, false)
            bottomShow = !attr.getBoolean(R.styleable.library_ShadowLayout_library_hl_shadowHiddenBottom, false)
            topShow = !attr.getBoolean(R.styleable.library_ShadowLayout_library_hl_shadowHiddenTop, false)
            mCornerRadius = attr.getDimension(
                R.styleable.library_ShadowLayout_library_hl_cornerRadius,
                resources.getDimension(R.dimen.dp_0)
            )
            mCornerRadius_leftTop =
                attr.getDimension(R.styleable.library_ShadowLayout_library_hl_cornerRadius_leftTop, -1f)
            mCornerRadius_leftBottom =
                attr.getDimension(R.styleable.library_ShadowLayout_library_hl_cornerRadius_leftBottom, -1f)
            mCornerRadius_rightTop =
                attr.getDimension(R.styleable.library_ShadowLayout_library_hl_cornerRadius_rightTop, -1f)
            mCornerRadius_rightBottom =
                attr.getDimension(R.styleable.library_ShadowLayout_library_hl_cornerRadius_rightBottom, -1f)

            //默认扩散区域宽度
            mShadowLimit = attr.getDimension(R.styleable.library_ShadowLayout_library_hl_shadowLimit, 0f)
            if (mShadowLimit == 0f) {
                //如果阴影没有设置阴影扩散区域，那么默认隐藏阴影
                isShowShadow = false
            } else {
                val dip5 = context.resources.getDimension(R.dimen.dp_5).toInt()
                if (mShadowLimit < dip5) {
                    mShadowLimit = dip5.toFloat()
                }
            }

            //x轴偏移量
            mDx = attr.getDimension(R.styleable.library_ShadowLayout_library_hl_shadowOffsetX, 0f)
            //y轴偏移量
            mDy = attr.getDimension(R.styleable.library_ShadowLayout_library_hl_shadowOffsetY, 0f)
            mShadowColor = attr.getColor(
                R.styleable.library_ShadowLayout_library_hl_shadowColor,
                resources.getColor(R.color.library_default_shadow_color)
            )
            selectorType = attr.getInt(R.styleable.library_ShadowLayout_library_hl_shapeMode, 1)
            isSym = attr.getBoolean(R.styleable.library_ShadowLayout_library_hl_shadowSymmetry, true)

            //背景颜色的点击(默认颜色为白色)
            mBackGroundColor = resources.getColor(R.color.library_default_shadowback_color)
            val background = attr.getDrawable(R.styleable.library_ShadowLayout_library_hl_layoutBackground)
            if (background != null) {
                if (background is ColorDrawable) {
                    mBackGroundColor = background.color
                } else {
                    layoutBackground = background
                }
            }
            val trueBackground = attr.getDrawable(R.styleable.library_ShadowLayout_library_hl_layoutBackground_true)
            if (trueBackground != null) {
                if (trueBackground is ColorDrawable) {
                    mBackGroundColor_true = trueBackground.color
                } else {
                    layoutBackground_true = trueBackground
                }
            }
            if (mBackGroundColor_true != -101 && layoutBackground != null) {
                throw UnsupportedOperationException("使用了library_ShadowLayout_library_hl_layoutBackground_true属性，必须先设置library_ShadowLayout_library_hl_layoutBackground属性。且设置颜色时，必须保持都为颜色")
            }
            if (layoutBackground == null && layoutBackground_true != null) {
                throw UnsupportedOperationException("使用了library_ShadowLayout_library_hl_layoutBackground_true属性，必须先设置library_ShadowLayout_library_hl_layoutBackground属性。且设置图片时，必须保持都为图片")
            }

            //边框颜色的点击
            stroke_color = attr.getColor(R.styleable.library_ShadowLayout_library_hl_strokeColor, -101)
            stroke_color_true = attr.getColor(R.styleable.library_ShadowLayout_library_hl_strokeColor_true, -101)
            if (stroke_color == -101 && stroke_color_true != -101) {
                throw UnsupportedOperationException("使用了library_ShadowLayout_library_hl_strokeColor_true属性，必须先设置library_ShadowLayout_library_hl_strokeColor属性")
            }
            stroke_with =
                attr.getDimension(R.styleable.library_ShadowLayout_library_hl_strokeWith, dip2px(1f).toFloat())
            //规定边框长度最大不错过7dp
            if (stroke_with > dip2px(7f)) {
                stroke_with = dip2px(5f).toFloat()
            }
            val clickAbleFalseBackground =
                attr.getDrawable(R.styleable.library_ShadowLayout_library_hl_layoutBackground_clickFalse)
            if (clickAbleFalseBackground != null) {
                if (clickAbleFalseBackground is ColorDrawable) {
                    clickAbleFalseColor = clickAbleFalseBackground.color
                } else {
                    clickAbleFalseDrawable = clickAbleFalseBackground
                }
            }
            startColor = attr.getColor(R.styleable.library_ShadowLayout_library_hl_startColor, -101)
            centerColor = attr.getColor(R.styleable.library_ShadowLayout_library_hl_centerColor, -101)
            endColor = attr.getColor(R.styleable.library_ShadowLayout_library_hl_endColor, -101)
            if (startColor != -101) {
                //说明设置了渐变色的起始色
                if (endColor == -101) {
                    throw UnsupportedOperationException("使用了library_ShadowLayout_library_hl_startColor渐变起始色，必须搭配终止色library_ShadowLayout_library_hl_endColor")
                }
            }
            angle = attr.getInt(R.styleable.library_ShadowLayout_library_hl_angle, 0)
            require(angle % 45 == 0) { "Linear gradient requires 'angle' attribute to be a multiple of 45" }
            if (selectorType == 3) {
                //如果是ripple的话
                if (mBackGroundColor == -101 || mBackGroundColor_true == -101) {
                    throw NullPointerException("使用了library_ShadowLayout_library的水波纹，必须设置使用了library_ShadowLayout_library_hl_layoutBackground和使用了library_ShadowLayout_library_hl_layoutBackground_true属性，且为颜色值")
                }

                //如果是设置了图片的话，那么也不支持水波纹
                if (layoutBackground != null) {
                    selectorType = 1
                }
            }
            mTextViewResId = attr.getResourceId(R.styleable.library_ShadowLayout_library_hl_bindTextView, -1)
            textColor = attr.getColor(R.styleable.library_ShadowLayout_library_hl_textColor, -101)
            textColor_true = attr.getColor(R.styleable.library_ShadowLayout_library_hl_textColor_true, -101)
            text = attr.getString(R.styleable.library_ShadowLayout_library_hl_text)
            text_true = attr.getString(R.styleable.library_ShadowLayout_library_hl_text_true)
            isMClickable = attr.getBoolean(R.styleable.library_ShadowLayout_library_clickable, true)
            setClickable(isMClickable)
        } finally {
            attr.recycle()
        }
    }


    private fun createShadowBitmap(
        shadowWidth: Int, shadowHeight: Int, cornerRadius: Float, shadowRadius: Float,
        dx: Float, dy: Float, shadowColor: Int, fillColor: Int
    ): Bitmap {
        //优化阴影bitmap大小,将尺寸缩小至原来的1/4。
        var shadowWidth = shadowWidth
        var shadowHeight = shadowHeight
        var cornerRadius = cornerRadius
        var shadowRadius = shadowRadius
        var dx = dx
        var dy = dy
        dx = dx / 4
        dy = dy / 4
        shadowWidth = if (shadowWidth / 4 == 0) 1 else shadowWidth / 4
        shadowHeight = if (shadowHeight / 4 == 0) 1 else shadowHeight / 4
        cornerRadius = cornerRadius / 4
        shadowRadius = shadowRadius / 4
        val output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(output)

        //这里缩小limit的是因为，setShadowLayer后会将bitmap扩散到shadowWidth，shadowHeight
        //同时也要根据某边的隐藏情况去改变
        var rect_left = 0f
        var rect_right = 0f
        var rect_top = 0f
        var rect_bottom = 0f
        rect_left = if (leftShow) {
            shadowRadius
        } else {
            //            rect_left = 0;
            val maxLeftTop = Math.max(cornerRadius, mCornerRadius_leftTop)
            val maxLeftBottom = Math.max(cornerRadius, mCornerRadius_leftBottom)
            val maxLeft = Math.max(maxLeftTop, maxLeftBottom)
            maxLeft
        }
        rect_top = if (topShow) {
            shadowRadius
        } else {
            //            rect_top = 0;
            val maxLeftTop = Math.max(cornerRadius, mCornerRadius_leftTop)
            val maxRightTop = Math.max(cornerRadius, mCornerRadius_rightTop)
            val maxTop = Math.max(maxLeftTop, maxRightTop)
            maxTop
        }
        rect_right = if (rightShow) {
            shadowWidth - shadowRadius
        } else {
            //            rect_right = shadowWidth;
            val maxRightTop = Math.max(cornerRadius, mCornerRadius_rightTop)
            val maxRightBottom = Math.max(cornerRadius, mCornerRadius_rightBottom)
            val maxRight = Math.max(maxRightTop, maxRightBottom)
            shadowWidth - maxRight
        }
        rect_bottom = if (bottomShow) {
            shadowHeight - shadowRadius
        } else {
            //            rect_bottom = shadowHeight;
            val maxLeftBottom = Math.max(cornerRadius, mCornerRadius_leftBottom)
            val maxRightBottom = Math.max(cornerRadius, mCornerRadius_rightBottom)
            val maxBottom = Math.max(maxLeftBottom, maxRightBottom)
            shadowHeight - maxBottom
        }
        val shadowRect = RectF(
            rect_left,
            rect_top,
            rect_right,
            rect_bottom
        )
        if (isSym) {
            if (dy > 0) {
                shadowRect.top += dy
                shadowRect.bottom -= dy
            } else if (dy < 0) {
                shadowRect.top += Math.abs(dy)
                shadowRect.bottom -= Math.abs(dy)
            }
            if (dx > 0) {
                shadowRect.left += dx
                shadowRect.right -= dx
            } else if (dx < 0) {
                shadowRect.left += Math.abs(dx)
                shadowRect.right -= Math.abs(dx)
            }
        } else {
            shadowRect.top -= dy
            shadowRect.bottom -= dy
            shadowRect.right -= dx
            shadowRect.left -= dx
        }
        shadowPaint.color = fillColor
        if (!isInEditMode) { //dx  dy
            shadowPaint.setShadowLayer(shadowRadius / 2, dx, dy, shadowColor)
        }
        if (mCornerRadius_leftBottom == -1f && mCornerRadius_leftTop == -1f && mCornerRadius_rightTop == -1f && mCornerRadius_rightBottom == -1f) {
            //如果没有设置整个属性，那么按原始去画
            canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint)
        } else {
            //目前最佳的解决方案
            rectf.left = leftPadding.toFloat()
            rectf.top = topPadding.toFloat()
            rectf.right = (width - rightPadding).toFloat()
            rectf.bottom = (height - bottomPadding).toFloat()
            shadowPaint.isAntiAlias = true
            val leftTop: Int
            leftTop = if (mCornerRadius_leftTop == -1f) {
                mCornerRadius.toInt() / 4
            } else {
                mCornerRadius_leftTop.toInt() / 4
            }
            val leftBottom: Int
            leftBottom = if (mCornerRadius_leftBottom == -1f) {
                mCornerRadius.toInt() / 4
            } else {
                mCornerRadius_leftBottom.toInt() / 4
            }
            val rightTop: Int
            rightTop = if (mCornerRadius_rightTop == -1f) {
                mCornerRadius.toInt() / 4
            } else {
                mCornerRadius_rightTop.toInt() / 4
            }
            val rightBottom: Int
            rightBottom = if (mCornerRadius_rightBottom == -1f) {
                mCornerRadius.toInt() / 4
            } else {
                mCornerRadius_rightBottom.toInt() / 4
            }
            val outerR = floatArrayOf(
                leftTop.toFloat(),
                leftTop.toFloat(),
                rightTop.toFloat(),
                rightTop.toFloat(),
                rightBottom.toFloat(),
                rightBottom.toFloat(),
                leftBottom.toFloat(),
                leftBottom.toFloat()
            ) //左上，右上，右下，左下
            val path = Path()
            path.addRoundRect(shadowRect, outerR, Path.Direction.CW)
            canvas.drawPath(path, shadowPaint)
        }
        return output
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun dispatchDraw(canvas: Canvas) {
        val trueHeight = (rectf.bottom - rectf.top).toInt()
        if (getChildAt(0) != null) {
            if (mCornerRadius_leftTop == -1f && mCornerRadius_leftBottom == -1f && mCornerRadius_rightTop == -1f && mCornerRadius_rightBottom == -1f) {
                //说明没有设置过任何特殊角、且是半圆。
                if (mCornerRadius > trueHeight / 2) {
                    val path = Path()
                    path.addRoundRect(
                        rectf,
                        (trueHeight / 2).toFloat(),
                        (trueHeight / 2).toFloat(),
                        Path.Direction.CW
                    )
                    canvas.clipPath(path)
                } else {
                    val path = Path()
                    path.addRoundRect(rectf, mCornerRadius, mCornerRadius, Path.Direction.CW)
                    canvas.clipPath(path)
                }
            } else {
                val outerR = getCornerValue(trueHeight)
                val path = Path()
                path.addRoundRect(
                    leftPadding.toFloat(),
                    topPadding.toFloat(),
                    (width - rightPadding).toFloat(),
                    (height - bottomPadding).toFloat(),
                    outerR,
                    Path.Direction.CW
                )
                canvas.clipPath(path)
            }
        }
        super.dispatchDraw(canvas)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rectf.left = leftPadding.toFloat()
        rectf.top = topPadding.toFloat()
        rectf.right = (width - rightPadding).toFloat()
        rectf.bottom = (height - bottomPadding).toFloat()
        val trueHeight = (rectf.bottom - rectf.top).toInt()
        //如果都为0说明，没有设置特定角，那么按正常绘制
        if (getChildAt(0) != null) {
            if (mCornerRadius_leftTop == -1f && mCornerRadius_leftBottom == -1f && mCornerRadius_rightTop == -1f && mCornerRadius_rightBottom == -1f) {
                if (mCornerRadius > trueHeight / 2) {
                    if (selectorType != 3) {
                        if (layoutBackground == null && layoutBackground_true == null) {
                            //画圆角矩形
                            canvas.drawRoundRect(
                                rectf, (trueHeight / 2).toFloat(), (trueHeight / 2).toFloat(),
                                paint
                            )
                            //解决边框线太洗时，四角的width偏大和其他边不同
                            if (stroke_color != -101) {
                                val the_height = (rectf.bottom - rectf.top).toInt()
                                val the_height_stoke =
                                    (rectf.bottom - stroke_with / 2 - rectf.top - stroke_with / 2).toInt()
                                val trueCorner = (trueHeight * the_height_stoke / 2 / the_height)
                                val rectFStroke = RectF(
                                    rectf.left + stroke_with / 2,
                                    rectf.top + stroke_with / 2,
                                    rectf.right - stroke_with / 2,
                                    rectf.bottom - stroke_with / 2
                                )
                                canvas.drawRoundRect(
                                    rectFStroke, trueCorner.toFloat(), trueCorner.toFloat(),
                                    paint_stroke
                                )
                            }
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            val outerR = getCornerValue(trueHeight)
                            ripple(outerR)
                        }
                    }
                } else {
                    if (selectorType != 3) {
                        if (layoutBackground == null && layoutBackground_true == null) {
                            canvas.drawRoundRect(rectf, mCornerRadius, mCornerRadius, paint)
                            if (stroke_color != -101) {
                                val the_height = (rectf.bottom - rectf.top).toInt()
                                val the_height_stoke =
                                    (rectf.bottom - stroke_with / 2 - rectf.top - stroke_with / 2).toInt()
                                val trueCorner =
                                    (mCornerRadius * the_height_stoke / the_height).toInt()
                                val rectFStroke = RectF(
                                    rectf.left + stroke_with / 2,
                                    rectf.top + stroke_with / 2,
                                    rectf.right - stroke_with / 2,
                                    rectf.bottom - stroke_with / 2
                                )
                                canvas.drawRoundRect(
                                    rectFStroke, trueCorner.toFloat(), trueCorner.toFloat(),
                                    paint_stroke
                                )
                            }
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            val outerR = getCornerValue(trueHeight)
                            ripple(outerR)
                        }
                    }
                }
            } else {
                if (layoutBackground == null && layoutBackground_true == null) {
                    setSpaceCorner(canvas, trueHeight)
                }
            }
        }
    }


    fun getCornerValue(trueHeight: Int): FloatArray {
        var leftTop: Int
        var rightTop: Int
        var rightBottom: Int
        var leftBottom: Int
        leftTop = if (mCornerRadius_leftTop == -1f) {
            mCornerRadius.toInt()
        } else {
            mCornerRadius_leftTop.toInt()
        }
        if (leftTop > trueHeight / 2) {
            leftTop = trueHeight / 2
        }
        rightTop = if (mCornerRadius_rightTop == -1f) {
            mCornerRadius.toInt()
        } else {
            mCornerRadius_rightTop.toInt()
        }
        if (rightTop > trueHeight / 2) {
            rightTop = trueHeight / 2
        }
        rightBottom = if (mCornerRadius_rightBottom == -1f) {
            mCornerRadius.toInt()
        } else {
            mCornerRadius_rightBottom.toInt()
        }
        if (rightBottom > trueHeight / 2) {
            rightBottom = trueHeight / 2
        }
        leftBottom = if (mCornerRadius_leftBottom == -1f) {
            mCornerRadius.toInt()
        } else {
            mCornerRadius_leftBottom.toInt()
        }
        if (leftBottom > trueHeight / 2) {
            leftBottom = trueHeight / 2
        }
        return floatArrayOf(
            leftTop.toFloat(),
            leftTop.toFloat(),
            rightTop.toFloat(),
            rightTop.toFloat(),
            rightBottom.toFloat(),
            rightBottom.toFloat(),
            leftBottom.toFloat(),
            leftBottom.toFloat()
        )
    }


    //这里是为了解决stokeWith很大时，圆角盖不住底部四个角的bug(issues #86)
    fun getCornerValueOther(trueHeight: Int, stokeWith: Int): FloatArray {
        var trueHeight = trueHeight
        trueHeight = trueHeight - stokeWith * 2
        var leftTop: Int
        var rightTop: Int
        var rightBottom: Int
        var leftBottom: Int
        leftTop = if (mCornerRadius_leftTop == -1f) {
            mCornerRadius.toInt()
        } else {
            mCornerRadius_leftTop.toInt()
        }
        if (leftTop > trueHeight / 2) {
            leftTop = trueHeight / 2
        }
        rightTop = if (mCornerRadius_rightTop == -1f) {
            mCornerRadius.toInt()
        } else {
            mCornerRadius_rightTop.toInt()
        }
        if (rightTop > trueHeight / 2) {
            rightTop = trueHeight / 2
        }
        rightBottom = if (mCornerRadius_rightBottom == -1f) {
            mCornerRadius.toInt()
        } else {
            mCornerRadius_rightBottom.toInt()
        }
        if (rightBottom > trueHeight / 2) {
            rightBottom = trueHeight / 2
        }
        leftBottom = if (mCornerRadius_leftBottom == -1f) {
            mCornerRadius.toInt()
        } else {
            mCornerRadius_leftBottom.toInt()
        }
        if (leftBottom > trueHeight / 2) {
            leftBottom = trueHeight / 2
        }
        leftTop = leftTop - stokeWith
        rightTop = rightTop - stokeWith
        leftBottom = leftBottom - stokeWith
        rightBottom = rightBottom - stokeWith
        return floatArrayOf(
            leftTop.toFloat(),
            leftTop.toFloat(),
            rightTop.toFloat(),
            rightTop.toFloat(),
            rightBottom.toFloat(),
            rightBottom.toFloat(),
            leftBottom.toFloat(),
            leftBottom.toFloat()
        )
    }


    //这是自定义四个角的方法。
    private fun setSpaceCorner(canvas: Canvas, trueHeight: Int) {
        val outerR = getCornerValue(trueHeight)
        if (stroke_color != -101) {
            if (selectorType != 3) {
                val mDrawables = ShapeDrawable(RoundRectShape(outerR, null, null))
                if (paint.shader != null) {
                    mDrawables.paint.shader = paint.shader
                } else {
                    mDrawables.paint.color = paint.color
                }
                mDrawables.setBounds(
                    leftPadding,
                    topPadding,
                    width - rightPadding,
                    height - bottomPadding
                )
                mDrawables.draw(canvas)
                val outerR_stroke = getCornerValueOther(
                    trueHeight,
                    stroke_with.toInt()
                )
                val mDrawablesStroke = ShapeDrawable(RoundRectShape(outerR_stroke, null, null))
                mDrawablesStroke.paint.color = paint_stroke.color
                mDrawablesStroke.paint.style = Paint.Style.STROKE
                mDrawablesStroke.paint.strokeWidth = stroke_with
                mDrawablesStroke.setBounds(
                    (leftPadding + stroke_with / 2).toInt(),
                    (topPadding + stroke_with / 2).toInt(),
                    (width - rightPadding - stroke_with / 2).toInt(),
                    (height - bottomPadding - stroke_with / 2).toInt()
                )
                mDrawablesStroke.draw(canvas)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ripple(outerR)
                }
            }
        } else {
            if (selectorType != 3) {
                val mDrawables = ShapeDrawable(RoundRectShape(outerR, null, null))
                if (paint.shader != null) {
                    mDrawables.paint.shader = paint.shader
                } else {
                    mDrawables.paint.color = paint.color
                }
                mDrawables.setBounds(
                    leftPadding,
                    topPadding,
                    width - rightPadding,
                    height - bottomPadding
                )
                mDrawables.draw(canvas)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ripple(outerR)
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun ripple(outRadius: FloatArray) {
        val stateList = arrayOf(
            intArrayOf(android.R.attr.state_pressed),
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(
                android.R.attr.state_activated
            ),
            intArrayOf()
        )

        //深蓝
        val normalColor = mBackGroundColor
        //玫瑰红
        val pressedColor = mBackGroundColor_true
        val stateColorList = intArrayOf(
            pressedColor,
            pressedColor,
            pressedColor,
            normalColor
        )
        val colorStateList = ColorStateList(stateList, stateColorList)
        val roundRectShape = RoundRectShape(outRadius, null, null)
        val maskDrawable = ShapeDrawable()
        maskDrawable.shape = roundRectShape
        maskDrawable.paint.style = Paint.Style.FILL
        if (startColor != -101) {
            //如果设置了渐变色的话
            gradient(maskDrawable.paint)
        } else {
            maskDrawable.paint.color = normalColor
        }
        val contentDrawable = ShapeDrawable()
        contentDrawable.shape = roundRectShape
        contentDrawable.paint.style = Paint.Style.FILL
        if (startColor != -101) {
            //如果设置了渐变色的话
            gradient(contentDrawable.paint)
        } else {
            contentDrawable.paint.color = normalColor
        }

        //contentDrawable实际是默认初始化时展示的；maskDrawable 控制了rippleDrawable的范围
        val rippleDrawable = RippleDrawable(colorStateList, contentDrawable, maskDrawable)
        firstView.background = rippleDrawable
    }


    fun isAddAlpha(color: Int) {
        //获取单签颜色值的透明度，如果没有设置透明度，默认加上#2a
        if (Color.alpha(color) == 255) {
            var red = Integer.toHexString(Color.red(color))
            var green = Integer.toHexString(Color.green(color))
            var blue = Integer.toHexString(Color.blue(color))
            if (red.length == 1) {
                red = "0$red"
            }
            if (green.length == 1) {
                green = "0$green"
            }
            if (blue.length == 1) {
                blue = "0$blue"
            }
            val endColor = "#2a$red$green$blue"
            mShadowColor = convertToColorInt(endColor)
        }
    }


    @Throws(IllegalArgumentException::class)
    fun convertToColorInt(argb: String): Int {
        var argb = argb
        if (!argb.startsWith("#")) {
            argb = "#$argb"
        }
        return Color.parseColor(argb)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (selectorType == 3) {
            //如果是水波纹模式，那么不需要进行下面的渲染，采用系统ripper即可
            if (isMClickable) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> if (selectorType == 3) {
                        if (mTextView != null) {
                            mTextView.setTextColor(textColor_true)
                            if (!TextUtils.isEmpty(text_true)) {
                                mTextView.text = text_true
                            }
                        }
                    }
                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> if (selectorType == 3) {
                        if (mTextView != null) {
                            mTextView.setTextColor(textColor)
                            if (!TextUtils.isEmpty(text)) {
                                mTextView.text = text
                            }
                        }
                    }
                }
            }
            return super.onTouchEvent(event)
        }
        if (mBackGroundColor_true != -101 || stroke_color_true != -101 || layoutBackground_true != null) {
            if (isMClickable) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> if (selectorType == 1) {
                        if (mBackGroundColor_true != -101) {
                            paint.color = mBackGroundColor_true
                            //打个标记
                            paint.shader = null
                        }
                        if (stroke_color_true != -101) {
                            paint_stroke.color = stroke_color_true
                        }
                        if (layoutBackground_true != null) {
                            setmBackGround(layoutBackground_true)
                        }
                        postInvalidate()
                        if (mTextView != null) {
                            mTextView.setTextColor(textColor_true)
                            if (!TextUtils.isEmpty(text_true)) {
                                mTextView.text = text_true
                            }
                        }
                    }
                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> if (selectorType == 1) {
                        //打个标记
                        paint.color = mBackGroundColor
                        if (startColor != -101) {
                            gradient(paint)
                        }
                        if (stroke_color != -101) {
                            paint_stroke.color = stroke_color
                        }
                        if (layoutBackground != null) {
                            setmBackGround(layoutBackground)
                        }
                        postInvalidate()
                        if (mTextView != null) {
                            mTextView.setTextColor(textColor)
                            if (!TextUtils.isEmpty(text)) {
                                mTextView.text = text
                            }
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }


    fun setmBackGround(drawable: Drawable?) {
        if (firstView != null && drawable != null) {
            if (mCornerRadius_leftTop == -1f && mCornerRadius_leftBottom == -1f && mCornerRadius_rightTop == -1f && mCornerRadius_rightBottom == -1f) {
                GlideRoundManager.setRoundCorner(firstView, drawable, mCornerRadius)
            } else {
                val leftTop: Int
                leftTop = if (mCornerRadius_leftTop == -1f) {
                    mCornerRadius.toInt()
                } else {
                    mCornerRadius_leftTop.toInt()
                }
                val leftBottom: Int
                leftBottom = if (mCornerRadius_leftBottom == -1f) {
                    mCornerRadius.toInt()
                } else {
                    mCornerRadius_leftBottom.toInt()
                }
                val rightTop: Int
                rightTop = if (mCornerRadius_rightTop == -1f) {
                    mCornerRadius.toInt()
                } else {
                    mCornerRadius_rightTop.toInt()
                }
                val rightBottom: Int
                rightBottom = if (mCornerRadius_rightBottom == -1f) {
                    mCornerRadius.toInt()
                } else {
                    mCornerRadius_rightBottom.toInt()
                }
                GlideRoundManager.setCorners(
                    firstView,
                    drawable,
                    leftTop.toFloat(),
                    leftBottom.toFloat(),
                    rightTop.toFloat(),
                    rightBottom.toFloat()
                )
            }
        }
    }
}