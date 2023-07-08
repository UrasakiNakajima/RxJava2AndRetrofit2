package com.phone.module_project.ui

import android.view.View
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_mvvm.BaseBindingRxAppActivity
import com.phone.library_common.callback.OnDialogCallback
import com.phone.library_common.common.ConstantData
import com.phone.library_custom_view.fragment.EventScheduleDialogFragment
import com.phone.module_project.R
import com.phone.module_project.databinding.ProjectActivityEventScheduleBinding

@Route(path = ConstantData.Route.ROUTE_EVENT_SCHEDULE)
class EventScheduleActivity : BaseBindingRxAppActivity<ProjectActivityEventScheduleBinding>() {

    companion object {
        private const val TAG = "EventScheduleActivity"
    }

    override fun initLayoutId() = R.layout.project_activity_event_schedule

    override fun initData() {
    }

    override fun initViews() {
        setToolbar(false, R.color.library_color_FF198CFF)
        mDatabind.imvBack.setColorFilter(ContextCompat.getColor(this, R.color.library_white))
        mDatabind.layoutBack.setOnClickListener {
            finish()
        }
        mDatabind.tevEventSchedule.setOnClickListener {
            val eventScheduleDialogFragment = EventScheduleDialogFragment()
            eventScheduleDialogFragment.setOnDialogCallback(object : OnDialogCallback<Int> {
                override fun onDialogClick(view: View?, success: Int?) {
                    if (success == 0) {

                    } else if (success == 1) {

                    } else {

                    }
                }

                override fun onDialogClick(
                    view: View?,
                    success: Int?,
                    params: Map<String?, String?>?
                ) {

                }

            })
            eventScheduleDialogFragment.show(supportFragmentManager, "FOF")
        }
    }

    override fun initLoadData() {
    }

    override fun showLoading() {
        if (!mLoadView.isShown()) {
            mLoadView.setVisibility(View.VISIBLE)
            mLoadView.start()
        }
    }

    override fun hideLoading() {
        if (mLoadView.isShown()) {
            mLoadView.stop()
            mLoadView.setVisibility(View.GONE)
        }
    }

}