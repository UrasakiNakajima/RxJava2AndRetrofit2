package com.phone.module_square.function_menu.view

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.Gravity
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.phone.library_base.manager.ScreenManager
import com.phone.library_glide.manager.ImageLoaderManager
import com.phone.module_square.R
import com.phone.module_square.function_menu.bean.BarrageBean
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/**
 * 弹幕动画
 */
class BarrageCardView : FrameLayout {

    companion object {
        private const val TAG = "BarrageCardView"
    }

    private var mContext: Context
    private val mScreenWidth = ScreenManager.getScreenWidth()
    private val mMarginEnd = ScreenManager.dpToPx(25F)
    private val mMarginTop = ScreenManager.dpToPx(8F)
    private val mMarginCenter = ScreenManager.dpToPx(51F)
    private val mMarginBottom = ScreenManager.dpToPx(94F)
    private val mMargin6 = ScreenManager.dpToPx(6F)
    private val mMarginStart26 = ScreenManager.dpToPx(26F)
    private val mMarginStart18 = ScreenManager.dpToPx(18F)
    private val mLeftPadding23 = ScreenManager.dpToPx(23F)
    private val mLeftPadding26 = ScreenManager.dpToPx(26F)
    private val mRightPadding11 = ScreenManager.dpToPx(11F)
    private val mRightPadding15 = ScreenManager.dpToPx(15F)
    private var mTevLeftPadding = mLeftPadding23
    private val mTevTopPadding = ScreenManager.dpToPx(5F)
    private var mTevRightPadding = mRightPadding11
    private val mTevBottomPadding = ScreenManager.dpToPx(5F)
    private val mImvAvatarSizeMine = ScreenManager.dpToPx(40F)
    private val mImvAvatarSizeOthers = ScreenManager.dpToPx(32F)
    private val mImvAvatarBgSizeMine = ScreenManager.dpToPx(44F)
    private val mImvAvatarBgSizeOthers = ScreenManager.dpToPx(36F)
    private val mImvAvatarBgSizeMineInt = mImvAvatarBgSizeMine
    private val mImvAvatarBgSizeOthersInt = mImvAvatarBgSizeOthers
    private val mCornerRadiusImvMine = (mImvAvatarBgSizeMine / 2).toFloat()
    private val mCornerRadiusImvOthers = (mImvAvatarBgSizeOthers / 2).toFloat()

    private var mBarrageList = mutableListOf<BarrageBean>()
    private var mMineBarrageList = mutableListOf<BarrageBean>()
    private val mSpeed = 200F
    private val mHandler = Handler(Looper.getMainLooper())

    private var mThreadPool: ExecutorService? = null
    private val mObjectAnimatorList = mutableListOf<ObjectAnimator>()
    private var mNickNameSuffix = ""

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        mNickNameSuffix =
            " ${mContext.resources.getString(R.string.library_video_play)}"
    }

    fun addAllData(barrageList: MutableList<BarrageBean>) {
        mBarrageList.clear()
        mBarrageList.addAll(barrageList)
        mMineBarrageList.clear()
        mMineBarrageList.addAll(mBarrageList)
    }

    fun addBarrage(barrageBean: BarrageBean) {
        mBarrageList.add(0, barrageBean)
        mMineBarrageList.add(0, barrageBean)
    }

    private fun startShowBarrageAnimation() {
        showBarrageAnimation(0, 0)
        showBarrageAnimation(1, 1000)
        showBarrageAnimation(2, 2000)
    }

    private fun stopShowBarrageAnimation() {
        if (mThreadPool != null && !mThreadPool!!.isShutdown) {
            mThreadPool?.shutdownNow()
        }
        mThreadPool = null
        if (mObjectAnimatorList.size > 0) {
            for (index in mObjectAnimatorList.size - 1 downTo 0) { //downTo 倒序遍历
                if (index <= mObjectAnimatorList.size - 1) {
                    mObjectAnimatorList.get(index).cancel()
                }
            }
        }
        mObjectAnimatorList.clear()
        mHandler.removeCallbacksAndMessages(null)
    }

    private fun showBarrageAnimation(flag: Int, delay: Long) {
        if (delay > 0) {
            if (mThreadPool == null) {
                mThreadPool = Executors.newCachedThreadPool()
            }
            mThreadPool?.submit {
                Thread.sleep(delay)
                mHandler.post {
                    createAndMoveView(flag)
                }
            }
        } else {
            mHandler.post {
                createAndMoveView(flag)
            }
        }
    }

    private fun createAndMoveView(flag: Int) {
        if (mMineBarrageList.size == 0) {
            mMineBarrageList.clear()
            mMineBarrageList.addAll(mBarrageList)
        }
        if (mMineBarrageList.size == 0) {
            return
        }
        var marginTemporary: Int
        if (flag == 0) {
            marginTemporary = mMarginTop
        } else if (flag == 1) {
            marginTemporary = mMarginCenter
        } else {
            marginTemporary = mMarginBottom
        }
        val barrageBean = mMineBarrageList[0]
        if (mMineBarrageList.size > 0) {
            mMineBarrageList.removeAt(0)
        }
        val barrageLayout = FrameLayout(mContext)
        val tevNickName = TextView(mContext)
        if (barrageBean.oneself) {
            mTevLeftPadding = mLeftPadding26
            mTevRightPadding = mRightPadding15
        } else {
            mTevLeftPadding = mLeftPadding23
            mTevRightPadding = mRightPadding11
        }
        tevNickName.setPadding(
            mTevLeftPadding, mTevTopPadding, mTevRightPadding, mTevBottomPadding
        )
        tevNickName.includeFontPadding = false
        tevNickName.setTextSize(13F)
        tevNickName.gravity = Gravity.CENTER
        val nickName = barrageBean.nickName ?: ""
        val stringBuilder = StringBuilder()
        stringBuilder.append(nickName)
        stringBuilder.append(mNickNameSuffix)
        val tevGradientDrawable = GradientDrawable()
        tevGradientDrawable.cornerRadius = ScreenManager.dpToPx(12F).toFloat()
        if (barrageBean.oneself) {
            tevGradientDrawable.setColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.library_color_FFFFC73B
                )
            )
            val spannableString = SpannableString(stringBuilder.toString())
            spannableString.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        mContext,
                        R.color.library_color_FFE066FF
                    )
                ), 0, nickName.length + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            tevNickName.setText(spannableString)
            tevNickName.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.library_color_000000
                )
            )
        } else {
            tevGradientDrawable.setColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.library_color_CFFFFFFF
                )
            )
            tevNickName.setTextColor(ContextCompat.getColor(mContext, R.color.library_color_000000))
            tevNickName.text = stringBuilder.toString()
        }
        tevGradientDrawable.setStroke(
            ScreenManager.dpToPx(2F), ContextCompat.getColor(mContext, R.color.library_color_000000)
        )
        tevNickName.background = tevGradientDrawable
        val tevNickNameParams = LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        )
        tevNickNameParams.gravity = Gravity.CENTER_VERTICAL
        if (barrageBean.oneself) {
            tevNickNameParams.marginStart = mMarginStart26
        } else {
            tevNickNameParams.marginStart = mMarginStart18
        }
        barrageLayout.addView(tevNickName, tevNickNameParams)


        val imvAvatar = AppCompatImageView(mContext)
        ImageLoaderManager.displayRound(
            mContext,
            imvAvatar, R.mipmap.library_app
        )
        if (!barrageBean.url.isNullOrEmpty()) {
            ImageLoaderManager.displayRound(
                mContext,
                imvAvatar, barrageBean.url
            )
        }
        val imvAvatarSize: Int
        if (barrageBean.oneself) {
            imvAvatarSize = mImvAvatarSizeMine
        } else {
            imvAvatarSize = mImvAvatarSizeOthers
        }
        val imvAvatarLayoutParams = LayoutParams(
            imvAvatarSize, imvAvatarSize
        )
        imvAvatarLayoutParams.gravity = Gravity.CENTER


        val imvAvatarOfLayout = FrameLayout(mContext)
        imvAvatarOfLayout.addView(imvAvatar, imvAvatarLayoutParams)
        val imvAvatarOfLayoutGradientDrawable = GradientDrawable()
        if (barrageBean.oneself) {
            marginTemporary -= mMargin6
            imvAvatarOfLayoutGradientDrawable.cornerRadius = mCornerRadiusImvMine
        } else {
            imvAvatarOfLayoutGradientDrawable.cornerRadius = mCornerRadiusImvOthers
        }
        imvAvatarOfLayoutGradientDrawable.setStroke(
            ScreenManager.dpToPx(3F), ContextCompat.getColor(mContext, R.color.library_color_000000)
        )
        imvAvatarOfLayout.background = imvAvatarOfLayoutGradientDrawable
        val imvAvatarOfLayoutParams: LayoutParams = if (barrageBean.oneself) {
            LayoutParams(mImvAvatarBgSizeMineInt, mImvAvatarBgSizeMineInt)
        } else {
            LayoutParams(mImvAvatarBgSizeOthersInt, mImvAvatarBgSizeOthersInt)
        }
        barrageLayout.addView(imvAvatarOfLayout, imvAvatarOfLayoutParams)
        val barrageLayoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        )
        barrageLayoutParams.marginStart = mScreenWidth
        barrageLayoutParams.topMargin = marginTemporary

        if (barrageBean.oneself) {
            val imvStarDecoration = AppCompatImageView(mContext)
            ImageLoaderManager.displayCorners(
                imvStarDecoration, ContextCompat.getDrawable(
                    mContext, R.mipmap.library_app2
                )
            )
            val imvStarDecorationLayoutParams =
                LayoutParams(ScreenManager.dpToPx(20F), ScreenManager.dpToPx(27F))
            imvStarDecorationLayoutParams.marginStart = ScreenManager.dpToPx(29F)
            barrageLayout.addView(imvStarDecoration, imvStarDecorationLayoutParams)

            val imvChickenNuggets = AppCompatImageView(mContext)
            ImageLoaderManager.displayCorners(
                imvChickenNuggets, ContextCompat.getDrawable(
                    mContext, R.mipmap.library_app2
                )
            )
            val imvChickenNuggetsLayoutParams =
                LayoutParams(ScreenManager.dpToPx(14F), ScreenManager.dpToPx(14F))
            imvChickenNuggetsLayoutParams.topMargin = ScreenManager.dpToPx(7F)
            imvChickenNuggetsLayoutParams.gravity = Gravity.END
            barrageLayout.addView(imvChickenNuggets, imvChickenNuggetsLayoutParams)
        }
        addView(barrageLayout, barrageLayoutParams)
        measuredWidth(barrageLayout, barrageBean)

        val startX = (mScreenWidth + barrageBean.width).toFloat()
        val duration = (startX / mSpeed) * 1000
        val nextShowTime = ((barrageBean.width + mMarginEnd) / mSpeed) * 1000

        val objectAnimator = ObjectAnimator.ofFloat(barrageLayout, "translationX", -startX) //沿着x轴平移
        objectAnimator.duration = duration.toLong()
        objectAnimator.interpolator = LinearInterpolator()
        objectAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                removeView(barrageLayout)
                if (mObjectAnimatorList.size > 0) {
                    mObjectAnimatorList.removeAt(0)
                }
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {

            }
        })
        objectAnimator.start()
        mObjectAnimatorList.add(objectAnimator)

        showBarrageAnimation(flag, nextShowTime.toLong())
    }

    private fun measuredWidth(frameLayout: FrameLayout, barrageBean: BarrageBean) {
        val widthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        frameLayout.measure(widthSpec, heightSpec)
        val measuredWidth: Int = frameLayout.getMeasuredWidth() //测量得到的textview的宽
        barrageBean.width = measuredWidth
    }

    fun onResume(barrageBeanList: MutableList<BarrageBean>) {
        addAllData(barrageBeanList)
        startShowBarrageAnimation()
    }

    fun onStop() {
        stopShowBarrageAnimation()
        mBarrageList.clear()
        mMineBarrageList.clear()
        removeAllViews()
    }

}