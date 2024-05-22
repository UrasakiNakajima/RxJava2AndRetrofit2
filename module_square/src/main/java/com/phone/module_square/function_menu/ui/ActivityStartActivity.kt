package com.phone.module_square.function_menu.ui

import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_base.base.BaseRxAppActivity
import com.phone.library_base.common.ConstantData
import com.phone.library_base.manager.ScreenManager
import com.phone.module_square.R
import com.phone.module_square.function_menu.bean.BarrageBean
import com.phone.module_square.function_menu.view.ActivityStartView
import com.phone.module_square.function_menu.view.BarrageCardView


@Route(path = ConstantData.Route.ROUTE_SQUARE_ACTIVITY_START)
class ActivityStartActivity : BaseRxAppActivity() {

    private val mLayoutBarrage: FrameLayout by lazy { findViewById<FrameLayout>(R.id.layout_barrage) }
    private val mLayoutActivityStart: FrameLayout by lazy { findViewById<FrameLayout>(R.id.layout_activity_start) }

    private var mBarrageList = mutableListOf<BarrageBean>()
    private var mBarrageCardView: BarrageCardView? = null
    private var mActivityStartView: ActivityStartView? = null

    private var mIsFirstAdd = false

    override fun initLayoutId(): Int {
        return R.layout.square_activity_activity_start
    }

    override fun initData() {
        for (index in 0 until 10) {
            val barrageBean = BarrageBean("", "Trump$index")
            mBarrageList.add(barrageBean)
        }
    }

    override fun initViews() {
        mBarrageCardView = BarrageCardView(this@ActivityStartActivity, null)
        val barrageLayoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, ScreenManager.dpToPx(142F))
        mLayoutBarrage.addView(mBarrageCardView, barrageLayoutParams)

        mActivityStartView = ActivityStartView(this@ActivityStartActivity, null)
        val activityStartViewLayoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        mActivityStartView?.onSummonedCallBack = object : ActivityStartView.OnSummonedCallBack {
            override fun onSummoned() {
                if (!mIsFirstAdd) {
                    val barrageBean = BarrageBean("", "Rommel")
                    barrageBean.oneself = true
                    mBarrageCardView?.addBarrage(barrageBean)
                    mIsFirstAdd = true
                }
            }
        }
        mLayoutActivityStart.addView(
            mActivityStartView,
            activityStartViewLayoutParams
        )
    }

    override fun initLoadData() {
    }

    override fun onResume() {
        super.onResume()
        mBarrageCardView?.onResume(mBarrageList)
    }

    override fun onStop() {
        super.onStop()
        mBarrageCardView?.onStop()
    }


}