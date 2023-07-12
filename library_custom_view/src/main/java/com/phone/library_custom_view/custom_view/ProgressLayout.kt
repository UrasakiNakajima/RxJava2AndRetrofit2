package com.phone.library_custom_view.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_base.manager.ScreenManager
import com.phone.library_custom_view.R
import com.qmuiteam.qmui.widget.QMUIProgressBar

class ProgressLayout(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    val mProgressBar = QMUIProgressBar(context, attrs)
    val mLayoutParams = LayoutParams(
        ScreenManager.dpToPx(ScreenManager.getDimenDp(R.dimen.base_dp_200).toFloat()),
        ScreenManager.dpToPx(ScreenManager.getDimenDp(R.dimen.base_dp_200).toFloat())
    )

    init {
        mLayoutParams.gravity = Gravity.CENTER
        mProgressBar.setTextColor(ResourcesManager.getColor(R.color.base_color_FFFFFFFF))
//        mProgressBar.setTextSize(
//            ScreenManager.spToPx(ScreenManager.getDimenSp(R.dimen.base_sp_48).toFloat())
//        )
        mProgressBar.setTextSize(50)
        mProgressBar.setType(QMUIProgressBar.TYPE_CIRCLE)
        mProgressBar.setBarColor(
            ResourcesManager.getColor(R.color.library_color_FF999999),
            ResourcesManager.getColor(R.color.library_color_FF198CFF)
        )
//        //设置文字样式 分数样式 如 10/100
//        mProgressBar.setQMUIProgressBarTextGenerator { progressBar, value, maxValue -> "$value/$maxValue" }
        //设置文字样式  百分之样式 如 10%
        mProgressBar.setQMUIProgressBarTextGenerator({ progressBar, value, maxValue -> (100 * value / maxValue).toString() + "%" })

        addView(mProgressBar, mLayoutParams)
    }

}