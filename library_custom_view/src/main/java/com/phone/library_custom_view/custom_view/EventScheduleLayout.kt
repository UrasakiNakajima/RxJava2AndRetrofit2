package com.phone.library_custom_view.custom_view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.phone.library_common.R
import com.phone.library_custom_view.bean.EventScheduleListBean
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_common.manager.ScreenManager
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * author : Urasaki
 * e-mail : 1164688204@qq.com
 * date   : 2021/4/30 13:36
 * desc   :
 * version: 1.0
 */
class EventScheduleLayout(
    val mContext: Context,
    rowsDTOList: List<EventScheduleListBean.DataDTO.RowsDTO>
) : LinearLayout(
    mContext
) {

    private val mRowsDTOList: MutableList<EventScheduleListBean.DataDTO.RowsDTO> = ArrayList()
    private var mRowsDTO: EventScheduleListBean.DataDTO.RowsDTO? = null
    private var mStartTimeStr: String? = null
    private var mEndTimeStr: String? = null
    private var mStartTimeNextStr: String? = null
    private var mStartTime = 0f
    private var mStartTimeNext: Long = 0
    private var mEndTime = 0.0
    private var mTimeQuantum = 0.0
    private var mTimeQuantumNext = 0.0
    private var mTotalTimeNumber = 0
    private var startHourInt = 0
    private var startHourRealInt = 0
    private var startMinuteRealInt = 0
    private var endHourInt = 0
    private var endHourRealInt = 0
    private var endMinuteRealInt = 0
    private var timeNumber = 0.0
    private var timeNumberNext = 0.0

    init {
        setOrientation(LinearLayout.HORIZONTAL)
        initViews(rowsDTOList)
    }

    private fun initViews(rowsDTOList: List<EventScheduleListBean.DataDTO.RowsDTO>?) {
        val layoutParamsStart = LinearLayout.LayoutParams(
            ScreenManager.dpToPx(75f), LinearLayout.LayoutParams.MATCH_PARENT
        )
        //		LayoutParams layoutParamsStart = new LayoutParams(
        //			ScreenManager.ScreenManager.dpToPx(getResources().getDimension((int) (R.dimen.library_dp_75))), LayoutParams.MATCH_PARENT);
        val layoutParamsEnd = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
        )
        val layoutStart = LinearLayout(mContext)
        layoutStart.orientation = LinearLayout.VERTICAL
        val layoutEnd = LinearLayout(mContext)
        layoutEnd.orientation = LinearLayout.VERTICAL
        this.addView(layoutStart, layoutParamsStart)
        this.addView(layoutEnd, layoutParamsEnd)
        if (rowsDTOList != null && rowsDTOList.size > 0) {
            mRowsDTOList.clear()
            mRowsDTOList.addAll(rowsDTOList)
            for (i in mRowsDTOList.indices) {
                mRowsDTO = mRowsDTOList[i]
                if (i <= mRowsDTOList.size - 1) {
                    if (i == 0) { //第一次，画出左边布局
                        mStartTimeStr = mRowsDTO?.activeStartTime
                        mEndTimeStr = mRowsDTOList[mRowsDTOList.size - 1].activeEndTime
                        try {
                            mStartTime = dateStrConvertedToMillisecond(mStartTimeStr).toFloat()
                            val arr = mStartTimeStr?.split(" ")?.toTypedArray()
                            arr?.let {
                                mStartTimeStr = arr[1]
                            }
                            val arrHour = mStartTimeStr?.split(":")?.toTypedArray()
                            arrHour?.let {
                                startHourRealInt = Integer.valueOf(arrHour[0])
                                startMinuteRealInt = Integer.valueOf(arrHour[1])
                            }
                            startHourInt = if (startHourRealInt >= 8) {
                                8
                            } else {
                                startHourRealInt
                            }
                            val arrEnd = mEndTimeStr?.split(" ")?.toTypedArray()
                            arrEnd?.let {
                                mEndTimeStr = arrEnd[1]
                            }
                            val arrHourEnd = mEndTimeStr?.split(":")?.toTypedArray()
                            arrHourEnd?.get(0)?.let {
                                endHourRealInt = arrHourEnd[0].toInt()
                                endMinuteRealInt = Integer.valueOf(arrHourEnd[1])
                            }
                            endHourInt = if (endHourRealInt < 17 && endMinuteRealInt == 0) {
                                18
                            } else if (endHourRealInt == 17 && endMinuteRealInt > 0) {
                                19
                            } else {
                                if (endMinuteRealInt == 0) {
                                    endHourRealInt + 1
                                } else {
                                    endHourRealInt + 1 + 1
                                }
                            }
                            mTotalTimeNumber = endHourInt - startHourInt
                            var imvHeaderParams = LinearLayout.LayoutParams(
                                ScreenManager.dpToPx(10f),
                                ScreenManager.dpToPx(9f)
                            )
                            imvHeaderParams.marginStart = ScreenManager.dpToPx(53f)
                            var imvHeader = ImageView(mContext)
                            imvHeader.setImageResource(R.mipmap.icon_time_line_triangle)
                            layoutStart.addView(imvHeader, imvHeaderParams)
                            var lineViewParams = LinearLayout.LayoutParams(
                                ScreenManager.dpToPx(4f),
                                ScreenManager.dpToPx(12f)
                            )
                            lineViewParams.marginStart = ScreenManager.dpToPx(56f)
                            var dottedLineView = DottedLineView(mContext, null)
                            layoutStart.addView(dottedLineView, lineViewParams)

                            //画左边布局
                            for (j in 0 until mTotalTimeNumber) {
                                if (j == mTotalTimeNumber - 1) {
                                    //最后一个
                                    val outerLayout = FrameLayout(mContext)
                                    val outerLayoutParams = LinearLayout.LayoutParams(
                                        ScreenManager.dpToPx(75f),
                                        ScreenManager.dpToPx(16f)
                                    )
                                    val tevTimeParams = FrameLayout.LayoutParams(
                                        FrameLayout.LayoutParams.WRAP_CONTENT,
                                        FrameLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    tevTimeParams.gravity = Gravity.END or Gravity.CENTER_VERTICAL
                                    tevTimeParams.marginEnd = ScreenManager.dpToPx(26f)
                                    //时间
                                    val tevTime = TextView(mContext)
                                    tevTime.setText("$startHourInt + $j + :00")
                                    tevTime.setTextColor(ResourcesManager.getColor(R.color.library_color_FF333333))
                                    tevTime.textSize = 12f
                                    outerLayout.addView(tevTime, tevTimeParams)
                                    var lineViewParams2 = FrameLayout.LayoutParams(
                                        ScreenManager.dpToPx(4f),
                                        ScreenManager.dpToPx(3f)
                                    )
                                    lineViewParams2.marginStart = ScreenManager.dpToPx(56f)
                                    dottedLineView = DottedLineView(mContext, null)
                                    outerLayout.addView(dottedLineView, lineViewParams2)
                                    val imvMajorTimeNodeParams = FrameLayout.LayoutParams(
                                        ScreenManager.dpToPx(10f),
                                        ScreenManager.dpToPx(10f)
                                    )
                                    imvMajorTimeNodeParams.gravity = Gravity.CENTER_VERTICAL
                                    imvMajorTimeNodeParams.marginStart = ScreenManager.dpToPx(53f)
                                    //									imvMajorTimeNodeParams.setMargins(
                                    //										ScreenManager.ScreenManager.dpToPx(53),
                                    //										ScreenManager.ScreenManager.dpToPx(3), 0, 0);
                                    val imvMajorTimeNode = ImageView(mContext)
                                    imvMajorTimeNode.setImageResource(R.mipmap.icon_major_time_node)
                                    outerLayout.addView(imvMajorTimeNode, imvMajorTimeNodeParams)
                                    lineViewParams2 = FrameLayout.LayoutParams(
                                        ScreenManager.dpToPx(4f),
                                        ScreenManager.dpToPx(3f)
                                    )
                                    lineViewParams2.setMargins(
                                        ScreenManager.dpToPx(56f),
                                        ScreenManager.dpToPx(13f), 0, 0
                                    )
                                    dottedLineView = DottedLineView(mContext, null)
                                    outerLayout.addView(dottedLineView, lineViewParams2)
                                    layoutStart.addView(outerLayout, outerLayoutParams)
                                    lineViewParams = LinearLayout.LayoutParams(
                                        ScreenManager.dpToPx(4f),
                                        ScreenManager.dpToPx(21f)
                                    )
                                    lineViewParams.marginStart = ScreenManager.dpToPx(56f)
                                    dottedLineView = DottedLineView(mContext, null)
                                    layoutStart.addView(dottedLineView, lineViewParams)
                                    imvHeaderParams = LinearLayout.LayoutParams(
                                        ScreenManager.dpToPx(10f),
                                        ScreenManager.dpToPx(9f)
                                    )
                                    imvHeaderParams.marginStart = ScreenManager.dpToPx(53f)
                                    imvHeader = ImageView(mContext)
                                    imvHeader.setImageResource(R.mipmap.icon_time_line_triangle)
                                    layoutStart.addView(imvHeader, imvHeaderParams)

                                    //构造ObjectAnimator对象的方法
                                    val animator = ObjectAnimator.ofFloat(
                                        imvHeader,
                                        "rotation",
                                        0.0f,
                                        180.0f
                                    ) //设置先顺时针360度旋转然后逆时针360度旋转动画
                                    animator.duration = 50 //设置旋转时间
                                    animator.start() //开始执行动画（顺时针旋转动画）
                                } else {
                                    val outerLayout = FrameLayout(mContext)
                                    val outerLayoutParams = LinearLayout.LayoutParams(
                                        ScreenManager.dpToPx(75f),
                                        ScreenManager.dpToPx(16f)
                                    )
                                    val tevTimeParams = FrameLayout.LayoutParams(
                                        FrameLayout.LayoutParams.WRAP_CONTENT,
                                        FrameLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    tevTimeParams.gravity = Gravity.END or Gravity.CENTER_VERTICAL
                                    tevTimeParams.marginEnd = ScreenManager.dpToPx(26f)
                                    //时间
                                    val tevTime = TextView(mContext)
                                    tevTime.setText("$startHourInt + $j + 00")
                                    tevTime.setTextColor(ResourcesManager.getColor(R.color.library_color_FF333333))
                                    tevTime.textSize = 12f
                                    tevTime.includeFontPadding = false
                                    outerLayout.addView(tevTime, tevTimeParams)
                                    var lineViewParams2 = FrameLayout.LayoutParams(
                                        ScreenManager.dpToPx(4f),
                                        ScreenManager.dpToPx(3f)
                                    )
                                    lineViewParams2.marginStart = ScreenManager.dpToPx(56f)
                                    dottedLineView = DottedLineView(mContext, null)
                                    outerLayout.addView(dottedLineView, lineViewParams2)
                                    val imvMajorTimeNodeParams = FrameLayout.LayoutParams(
                                        ScreenManager.dpToPx(10f),
                                        ScreenManager.dpToPx(10f)
                                    )
                                    imvMajorTimeNodeParams.gravity = Gravity.CENTER_VERTICAL
                                    imvMajorTimeNodeParams.marginStart = ScreenManager.dpToPx(53f)
                                    //									imvMajorTimeNodeParams.setMargins(
                                    //										ScreenManager.ScreenManager.dpToPx(53),
                                    //										ScreenManager.ScreenManager.dpToPx(3), 0, 0);
                                    val imvMajorTimeNode = ImageView(mContext)
                                    imvMajorTimeNode.setImageResource(R.mipmap.icon_major_time_node)
                                    outerLayout.addView(imvMajorTimeNode, imvMajorTimeNodeParams)
                                    lineViewParams2 = FrameLayout.LayoutParams(
                                        ScreenManager.dpToPx(4f),
                                        ScreenManager.dpToPx(3f)
                                    )
                                    lineViewParams2.setMargins(
                                        ScreenManager.dpToPx(56f),
                                        ScreenManager.dpToPx(13f), 0, 0
                                    )
                                    dottedLineView = DottedLineView(mContext, null)
                                    outerLayout.addView(dottedLineView, lineViewParams2)
                                    layoutStart.addView(outerLayout, outerLayoutParams)
                                    lineViewParams = LinearLayout.LayoutParams(
                                        ScreenManager.dpToPx(4f),
                                        ScreenManager.dpToPx(22f)
                                    )
                                    lineViewParams.marginStart = ScreenManager.dpToPx(56f)
                                    dottedLineView = DottedLineView(mContext, null)
                                    layoutStart.addView(dottedLineView, lineViewParams)
                                    val imvSmallTimeNodeParams = LinearLayout.LayoutParams(
                                        ScreenManager.dpToPx(5f),
                                        ScreenManager.dpToPx(5f)
                                    )
                                    imvSmallTimeNodeParams.marginStart = ScreenManager.dpToPx(55.5f)
                                    val imvSmallTimeNode = ImageView(mContext)
                                    imvSmallTimeNode.setImageResource(R.mipmap.icon_small_time_node)
                                    layoutStart.addView(imvSmallTimeNode, imvSmallTimeNodeParams)
                                    val lineViewParams3 = LinearLayout.LayoutParams(
                                        ScreenManager.dpToPx(4f),
                                        ScreenManager.dpToPx(22f)
                                    )
                                    lineViewParams3.marginStart = ScreenManager.dpToPx(56f)
                                    val dottedLineView2 = DottedLineView(mContext, null)
                                    layoutStart.addView(dottedLineView2, lineViewParams3)
                                }
                            }
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                    }

                    //画出右边布局
                    mStartTimeStr = mRowsDTO?.activeStartTime
                    mStartTimeNextStr = if (i < mRowsDTOList.size - 1) {
                        mRowsDTOList[i + 1].activeStartTime
                    } else {
                        null
                    }
                    mEndTimeStr = mRowsDTO?.activeEndTime
                    //					mTimeStr = mRowsDTO.getActiveTime();
                    //					mFrameLayoutList.add(new FrameLayout(mContext));
                    try {
                        mStartTime = dateStrConvertedToMillisecond(mStartTimeStr).toFloat()
                        mEndTime = dateStrConvertedToMillisecond(mEndTimeStr).toDouble()
                        mStartTimeNext = if (!TextUtils.isEmpty(mStartTimeNextStr)) {
                            dateStrConvertedToMillisecond(mStartTimeNextStr)
                        } else {
                            0
                        }
                        mTimeQuantum = mEndTime - mStartTime
                        mTimeQuantumNext = if (mStartTimeNext > 0) {
                            mStartTimeNext - mEndTime
                        } else {
                            0.0
                        }
                        //有多少个分钟（以1分钟为基本时间单位）
                        timeNumber = mTimeQuantum / 1000 / 60
                        //						float mTimeQuantumFloat = mTimeQuantum;
                        //						float hourNumber = mTimeQuantumFloat / 1000 / 60 / 60;
                        timeNumberNext = if (mTimeQuantumNext > 0) {
                            //空布局需要添加的高度数
                            mTimeQuantumNext / 1000 / 60
                        } else {
                            0.0
                        }
                        if (startHourRealInt >= 8 && i == 0) {
                            val num = (startHourRealInt - startHourInt) * 60 + startMinuteRealInt
                            val mEmptyLayout = FrameLayout(mContext)
                            val mEmptyLayoutParams = LinearLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                ScreenManager.dpToPx((65.0 * num / 60.0).toFloat())
                            )
                            layoutEnd.addView(mEmptyLayout, mEmptyLayoutParams)
                        } else if (startHourRealInt < 8 && i == 0) {
                            val num = startMinuteRealInt
                            val mEmptyLayout = FrameLayout(mContext)
                            val mEmptyLayoutParams = LinearLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                ScreenManager.dpToPx((65.0 * num / 60.0).toFloat())
                            )
                            layoutEnd.addView(mEmptyLayout, mEmptyLayoutParams)
                        }
                        val mFrameLayout = FrameLayout(mContext)
                        mFrameLayout.background =
                            resources.getDrawable(R.drawable.library_corners_7_color_ffd0d6ec)
                        val tevTime = TextView(mContext)
                        tevTime.text = mRowsDTO?.activeTime
                        tevTime.setTextColor(ResourcesManager.getColor(R.color.library_color_FF999999))
                        tevTime.textSize = 12f
                        tevTime.includeFontPadding = false
                        val tevTimeParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                        )
                        tevTimeParams.setMargins(
                            ScreenManager.dpToPx(12f),
                            ScreenManager.dpToPx(12f), 0, 0
                        )
                        tevTimeParams.gravity = Gravity.START or Gravity.TOP
                        tevTime.layoutParams = tevTimeParams
                        //						mFrameLayout.addView(tevTime, tevTimeParams);
                        mFrameLayout.addView(tevTime)
                        val tevContent = TextView(mContext)
                        tevContent.text = mRowsDTO?.activeTitle
                        tevContent.setTextColor(ResourcesManager.getColor(R.color.library_color_FF333333))
                        tevContent.textSize = 13f
                        tevContent.includeFontPadding = false
                        tevContent.setLines(2)
                        tevContent.ellipsize = TextUtils.TruncateAt.END
                        val tevContentParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                        )
                        tevContentParams.setMargins(
                            ScreenManager.dpToPx(13f),
                            ScreenManager.dpToPx(31f),
                            ScreenManager.dpToPx(13f), 0
                        )
                        tevContentParams.gravity = Gravity.START or Gravity.TOP
                        tevContent.layoutParams = tevContentParams
                        //						mFrameLayout.addView(tevContent, tevContentParams);
                        mFrameLayout.addView(tevContent)
                        val tevStatus = TextView(mContext)
                        //						tevStatus.setText(mRowsDTO.getAuditStatusName());
                        tevStatus.setTextColor(ResourcesManager.getColor(R.color.library_color_FF3258F7))
                        tevStatus.textSize = 12f
                        tevStatus.includeFontPadding = false
                        if (mRowsDTO?.auditStatus == 0) {
                            mFrameLayout.background =
                                resources.getDrawable(R.drawable.library_corners_7_color_ffd0d6ec)
                            tevStatus.setText(ResourcesManager.getString(R.string.library_check_pending))
                        } else if (mRowsDTO?.auditStatus == 2) {
                            mFrameLayout.background =
                                resources.getDrawable(R.drawable.library_corners_7_color_ffcfe9ff)
                            tevStatus.setText(ResourcesManager.getString(R.string.library_checked))
                        }
                        val tevStatusParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                        )
                        tevStatusParams.setMargins(
                            0, ScreenManager.dpToPx(10f),
                            ScreenManager.dpToPx(13f), 0
                        )
                        tevStatusParams.gravity = Gravity.END or Gravity.TOP
                        tevStatus.layoutParams = tevStatusParams
                        //						mFrameLayout.addView(tevStatus, tevStatusParams);
                        mFrameLayout.addView(tevStatus)
                        var mFrameLayoutParams: LinearLayout.LayoutParams
                        if (i == 0) {
                            mFrameLayoutParams = LinearLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                ScreenManager.dpToPx(
                                    (65.0 * timeNumber / 60.0).toFloat() //																					 + ((hourNumber - 1) * 10)
                                )
                            )
                            mFrameLayoutParams.setMargins(
                                0,
                                ScreenManager.dpToPx(24f),
                                ScreenManager.dpToPx(26f),
                                0
                            )
                        } else {
                            mFrameLayoutParams = LinearLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                ScreenManager.dpToPx(
                                    (65.0 * timeNumber / 60.0).toFloat() //																					 + ((hourNumber - 1) * 10)
                                )
                            )
                            mFrameLayoutParams.marginEnd = ScreenManager.dpToPx(26f)
                        }
                        layoutEnd.addView(mFrameLayout, mFrameLayoutParams)
                        if (timeNumberNext > 0) {
                            val mEmptyLayout = FrameLayout(mContext)
                            //						mEmptyLayout.setBackgroundColor(ResourcesManager.getColor(R.color.library_color_FF9FACFF));
                            val mEmptyLayoutParams = LinearLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                ScreenManager.dpToPx((65.0 * timeNumberNext / 60.0).toFloat())
                            )
                            layoutEnd.addView(mEmptyLayout, mEmptyLayoutParams)
                        }
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                } else {
                    mStartTimeStr = mRowsDTO?.activeStartTime
                    mEndTimeStr = mRowsDTO?.activeEndTime
                    //					mTimeStr = mRowsDTO.getActiveTime();
                    //					mFrameLayoutList.add(new FrameLayout(mContext));
                    try {
                        mStartTime = dateStrConvertedToMillisecond(mStartTimeStr).toFloat()
                        mEndTime = dateStrConvertedToMillisecond(mEndTimeStr).toDouble()
                        mTimeQuantum = mEndTime - mStartTime
                        //有多少个分钟（以1分钟为基本时间单位）
                        timeNumber = mTimeQuantum / 1000 / 60
                        //						float mTimeQuantumFloat = mTimeQuantum;
                        //						float hourNumber = mTimeQuantumFloat / 1000 / 60 / 60;
                        val mFrameLayout = FrameLayout(mContext)
                        //						mFrameLayout.setBackgroundColor(ResourcesManager.getColor(R.color.library_color_FFFF0000));
                        mFrameLayout.background =
                            resources.getDrawable(R.drawable.library_corners_7_color_ffd0d6ec)
                        val mFrameLayoutParams = LinearLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            ScreenManager.dpToPx(
                                (65.0 * timeNumber / 60.0).toFloat() //																				 + (+((hourNumber - 1) * 10))
                            )
                        )
                        mFrameLayoutParams.marginEnd = ScreenManager.dpToPx(26f)
                        val tevTimeParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                        )
                        val tevTime = TextView(mContext)
                        tevTime.text = mRowsDTO?.activeTime
                        tevTime.setTextColor(ResourcesManager.getColor(R.color.library_color_FF999999))
                        tevTime.textSize = 12f
                        tevTime.includeFontPadding = false
                        tevTimeParams.setMargins(
                            ScreenManager.dpToPx(12f),
                            ScreenManager.dpToPx(12f), 0, 0
                        )
                        tevTimeParams.gravity = Gravity.START or Gravity.TOP
                        mFrameLayout.addView(tevTime, tevTimeParams)
                        val tevContentParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                        )
                        val tevContent = TextView(mContext)
                        tevContent.text = mRowsDTO?.activeTitle
                        tevContent.setTextColor(ResourcesManager.getColor(R.color.library_color_FF333333))
                        tevContent.textSize = 13f
                        tevContent.includeFontPadding = false
                        tevContent.setLines(2)
                        tevContent.ellipsize = TextUtils.TruncateAt.END
                        tevContentParams.setMargins(
                            ScreenManager.dpToPx(13f),
                            ScreenManager.dpToPx(31f),
                            ScreenManager.dpToPx(13f), 0
                        )
                        tevContentParams.gravity = Gravity.START or Gravity.TOP
                        mFrameLayout.addView(tevContent, tevContentParams)
                        val tevStatusParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                        )
                        val tevStatus = TextView(mContext)
                        tevStatus.text = mRowsDTO?.auditStatusName
                        tevStatus.setTextColor(ResourcesManager.getColor(R.color.library_color_FF3258F7))
                        tevStatus.textSize = 12f
                        tevStatus.includeFontPadding = false
                        if (mRowsDTO?.auditStatus == 0) {
                            mFrameLayout.background =
                                resources.getDrawable(R.drawable.library_corners_7_color_ffd0d6ec)
                            tevStatus.setText(ResourcesManager.getString(R.string.library_check_pending))
                        } else if (mRowsDTO?.auditStatus == 2) {
                            mFrameLayout.background =
                                resources.getDrawable(R.drawable.library_corners_7_color_ffcfe9ff)
                            tevStatus.setText(ResourcesManager.getString(R.string.library_checked))
                        }
                        tevStatusParams.setMargins(
                            0, ScreenManager.dpToPx(10f),
                            ScreenManager.dpToPx(13f), 0
                        )
                        tevStatusParams.gravity = Gravity.END or Gravity.TOP
                        mFrameLayout.addView(tevStatus, tevStatusParams)
                        layoutEnd.addView(mFrameLayout, mFrameLayoutParams)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                }
            }
        } else {
            //第一次，画出左边布局
            startHourInt = 8
            endHourInt = 18
            mTotalTimeNumber = endHourInt - startHourInt
            var imvHeaderParams = LinearLayout.LayoutParams(
                ScreenManager.dpToPx(10f),
                ScreenManager.dpToPx(9f)
            )
            imvHeaderParams.marginStart = ScreenManager.dpToPx(53f)
            var imvHeader = ImageView(mContext)
            imvHeader.setImageResource(R.mipmap.icon_time_line_triangle)
            layoutStart.addView(imvHeader, imvHeaderParams)
            var lineViewParams = LinearLayout.LayoutParams(
                ScreenManager.dpToPx(4f),
                ScreenManager.dpToPx(12f)
            )
            lineViewParams.marginStart = ScreenManager.dpToPx(56f)
            var dottedLineView = DottedLineView(mContext, null)
            layoutStart.addView(dottedLineView, lineViewParams)

            //画左边布局
            for (j in 0 until mTotalTimeNumber) {
                if (j == mTotalTimeNumber - 1) {
                    //最后一个
                    val outerLayout = FrameLayout(mContext)
                    val outerLayoutParams = LinearLayout.LayoutParams(
                        ScreenManager.dpToPx(75f),
                        ScreenManager.dpToPx(16f)
                    )
                    val tevTimeParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )
                    tevTimeParams.gravity = Gravity.END or Gravity.CENTER_VERTICAL
                    tevTimeParams.marginEnd = ScreenManager.dpToPx(26f)
                    //时间
                    val tevTime = TextView(mContext)
                    tevTime.setText("$startHourInt + $j + 00")
                    tevTime.setTextColor(ResourcesManager.getColor(R.color.library_color_FF333333))
                    tevTime.textSize = 12f
                    outerLayout.addView(tevTime, tevTimeParams)
                    var lineViewParams2 = FrameLayout.LayoutParams(
                        ScreenManager.dpToPx(4f),
                        ScreenManager.dpToPx(3f)
                    )
                    lineViewParams2.marginStart = ScreenManager.dpToPx(56f)
                    dottedLineView = DottedLineView(mContext, null)
                    outerLayout.addView(dottedLineView, lineViewParams2)
                    val imvMajorTimeNodeParams = FrameLayout.LayoutParams(
                        ScreenManager.dpToPx(10f),
                        ScreenManager.dpToPx(10f)
                    )
                    imvMajorTimeNodeParams.gravity = Gravity.CENTER_VERTICAL
                    imvMajorTimeNodeParams.marginStart = ScreenManager.dpToPx(53f)
                    //									imvMajorTimeNodeParams.setMargins(
                    //										ScreenManager.ScreenManager.dpToPx(53),
                    //										ScreenManager.ScreenManager.dpToPx(3), 0, 0);
                    val imvMajorTimeNode = ImageView(mContext)
                    imvMajorTimeNode.setImageResource(R.mipmap.icon_major_time_node)
                    outerLayout.addView(imvMajorTimeNode, imvMajorTimeNodeParams)
                    lineViewParams2 = FrameLayout.LayoutParams(
                        ScreenManager.dpToPx(4f),
                        ScreenManager.dpToPx(3f)
                    )
                    lineViewParams2.setMargins(
                        ScreenManager.dpToPx(56f),
                        ScreenManager.dpToPx(13f), 0, 0
                    )
                    dottedLineView = DottedLineView(mContext, null)
                    outerLayout.addView(dottedLineView, lineViewParams2)
                    layoutStart.addView(outerLayout, outerLayoutParams)
                    lineViewParams = LinearLayout.LayoutParams(
                        ScreenManager.dpToPx(4f),
                        ScreenManager.dpToPx(21f)
                    )
                    lineViewParams.marginStart = ScreenManager.dpToPx(56f)
                    dottedLineView = DottedLineView(mContext, null)
                    layoutStart.addView(dottedLineView, lineViewParams)
                    imvHeaderParams = LinearLayout.LayoutParams(
                        ScreenManager.dpToPx(10f),
                        ScreenManager.dpToPx(9f)
                    )
                    imvHeaderParams.marginStart = ScreenManager.dpToPx(53f)
                    imvHeader = ImageView(mContext)
                    imvHeader.setImageResource(R.mipmap.icon_time_line_triangle)
                    layoutStart.addView(imvHeader, imvHeaderParams)

                    //构造ObjectAnimator对象的方法
                    val animator = ObjectAnimator.ofFloat(
                        imvHeader,
                        "rotation",
                        0.0f,
                        180.0f
                    ) //设置先顺时针360度旋转然后逆时针360度旋转动画
                    animator.duration = 50 //设置旋转时间
                    animator.start() //开始执行动画（顺时针旋转动画）
                } else {
                    val outerLayout = FrameLayout(mContext)
                    val outerLayoutParams = LinearLayout.LayoutParams(
                        ScreenManager.dpToPx(75f),
                        ScreenManager.dpToPx(16f)
                    )
                    val tevTimeParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )
                    tevTimeParams.gravity = Gravity.END or Gravity.CENTER_VERTICAL
                    tevTimeParams.marginEnd = ScreenManager.dpToPx(26f)
                    //时间
                    val tevTime = TextView(mContext)
                    tevTime.setText("$startHourInt + $j + 00")
                    tevTime.setTextColor(ResourcesManager.getColor(R.color.library_color_FF333333))
                    tevTime.textSize = 12f
                    tevTime.includeFontPadding = false
                    outerLayout.addView(tevTime, tevTimeParams)
                    var lineViewParams2 = FrameLayout.LayoutParams(
                        ScreenManager.dpToPx(4f),
                        ScreenManager.dpToPx(3f)
                    )
                    lineViewParams2.marginStart = ScreenManager.dpToPx(56f)
                    dottedLineView = DottedLineView(mContext, null)
                    outerLayout.addView(dottedLineView, lineViewParams2)
                    val imvMajorTimeNodeParams = FrameLayout.LayoutParams(
                        ScreenManager.dpToPx(10f),
                        ScreenManager.dpToPx(10f)
                    )
                    imvMajorTimeNodeParams.gravity = Gravity.CENTER_VERTICAL
                    imvMajorTimeNodeParams.marginStart = ScreenManager.dpToPx(53f)
                    //									imvMajorTimeNodeParams.setMargins(
                    //										ScreenManager.ScreenManager.dpToPx(53),
                    //										ScreenManager.ScreenManager.dpToPx(3), 0, 0);
                    val imvMajorTimeNode = ImageView(mContext)
                    imvMajorTimeNode.setImageResource(R.mipmap.icon_major_time_node)
                    outerLayout.addView(imvMajorTimeNode, imvMajorTimeNodeParams)
                    lineViewParams2 = FrameLayout.LayoutParams(
                        ScreenManager.dpToPx(4f),
                        ScreenManager.dpToPx(3f)
                    )
                    lineViewParams2.setMargins(
                        ScreenManager.dpToPx(56f),
                        ScreenManager.dpToPx(13f), 0, 0
                    )
                    dottedLineView = DottedLineView(mContext, null)
                    outerLayout.addView(dottedLineView, lineViewParams2)
                    layoutStart.addView(outerLayout, outerLayoutParams)
                    lineViewParams = LinearLayout.LayoutParams(
                        ScreenManager.dpToPx(4f),
                        ScreenManager.dpToPx(22f)
                    )
                    lineViewParams.marginStart = ScreenManager.dpToPx(56f)
                    dottedLineView = DottedLineView(mContext, null)
                    layoutStart.addView(dottedLineView, lineViewParams)
                    val imvSmallTimeNodeParams = LinearLayout.LayoutParams(
                        ScreenManager.dpToPx(5f),
                        ScreenManager.dpToPx(5f)
                    )
                    imvSmallTimeNodeParams.marginStart = ScreenManager.dpToPx(55.5f)
                    val imvSmallTimeNode = ImageView(mContext)
                    imvSmallTimeNode.setImageResource(R.mipmap.icon_small_time_node)
                    layoutStart.addView(imvSmallTimeNode, imvSmallTimeNodeParams)
                    val lineViewParams3 = LinearLayout.LayoutParams(
                        ScreenManager.dpToPx(4f),
                        ScreenManager.dpToPx(22f)
                    )
                    lineViewParams3.marginStart = ScreenManager.dpToPx(56f)
                    val dottedLineView2 = DottedLineView(mContext, null)
                    layoutStart.addView(dottedLineView2, lineViewParams3)
                }
            }
        }
    }

    /**
     * 将时间字符串转化成毫秒数
     *
     * @param dateStr
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun dateStrConvertedToMillisecond(dateStr: String?): Long {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"))
        //24小时制
        val simpleDateFormat =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateStr?.let { simpleDateFormat.parse(it)?.time } ?: 0
    }

}