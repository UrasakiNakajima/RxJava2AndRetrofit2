package com.phone.module_square.function_menu.ui

import android.view.View
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_base.base.BaseRxAppActivity
import com.phone.library_base.common.ConstantData
import com.phone.library_base.manager.StatusBarManager
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.ScreenManager
import com.phone.module_square.R
import com.phone.module_square.function_menu.bean.BarrageBean
import com.phone.module_square.function_menu.view.ActivityStartView
import com.phone.module_square.function_menu.view.BarrageCardView


@Route(path = ConstantData.Route.ROUTE_SQUARE_ACTIVITY_START)
class ActivityStartActivity : BaseRxAppActivity() {

    companion object {
        @JvmStatic
        private val TAG = ActivityStartActivity::class.java.simpleName
    }

    private val mViewStatusBar: View by lazy { findViewById<View>(R.id.view_status_bar) }
    private val mToolbar: Toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val mLayoutBack: FrameLayout by lazy { findViewById<FrameLayout>(R.id.layout_back) }
    private val mImvBack: ImageView by lazy { findViewById<ImageView>(R.id.imv_back) }
    private val mTevTitle: TextView by lazy { findViewById<TextView>(R.id.tev_title) }
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
        setToolbar(false, R.color.library_black)

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

        mLayoutBack.setOnClickListener {
            finish()
        }

        val statusBarHeight = StatusBarManager.getStatusBarHeight()
        LogManager.i(TAG, "statusBarHeight*****${StatusBarManager.getStatusBarHeight()}")
        mViewStatusBar.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, statusBarHeight)
        LogManager.i(TAG, "statusBarHeight dp*****${ScreenManager.pxToDp(statusBarHeight.toFloat())}")

        val navigationBarHeight = StatusBarManager.getNavigationBarHeight(this@ActivityStartActivity)
        LogManager.i(TAG, "navigationBarHeight*****${navigationBarHeight}")
        LogManager.i(TAG, "navigationBarHeight dp*****${ScreenManager.pxToDp(navigationBarHeight.toFloat())}")
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