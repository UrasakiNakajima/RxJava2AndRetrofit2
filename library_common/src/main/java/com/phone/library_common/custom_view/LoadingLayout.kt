package com.phone.library_common.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import com.phone.library_common.R
import com.phone.library_common.manager.ResourcesManager
import com.phone.library_common.manager.ScreenManager
import com.qmuiteam.qmui.widget.QMUILoadingView

class LoadingLayout(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    var loadingView = QMUILoadingView(context, attrs)
    var layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

    init {
        layoutParams.gravity = Gravity.CENTER
        loadingView.setSize(ScreenManager.dpToPx(ScreenManager.getDimenDp(R.dimen.library_dp_33).toFloat()))
        loadingView.setColor(ResourcesManager.getColor(R.color.library_color_FF999999))
        addView(loadingView, layoutParams)
    }


}