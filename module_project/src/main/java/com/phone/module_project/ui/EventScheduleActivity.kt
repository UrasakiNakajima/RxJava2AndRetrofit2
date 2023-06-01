package com.phone.module_project.ui

import android.view.View
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_common.base.BaseBindingRxAppActivity
import com.phone.library_common.callback.OnDialogCallback
import com.phone.library_common.fragment.EventScheduleDialogFragment
import com.phone.module_project.R
import com.phone.module_project.databinding.ProjectActivityEventScheduleBinding

@Route(path = "/module_project/ui/event_schedule")
class EventScheduleActivity : BaseBindingRxAppActivity<ProjectActivityEventScheduleBinding>() {

    companion object {
        private const val TAG = "EventScheduleActivity"
    }

    override fun initLayoutId() = R.layout.project_activity_event_schedule

    override fun initData() {
    }

    override fun initViews() {
        setToolbar(false, R.color.color_FF198CFF)
        mDatabind.imvBack.setColorFilter(ContextCompat.getColor(this, R.color.white))
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
        if (!loadView.isShown()) {
            loadView.setVisibility(View.VISIBLE)
            loadView.start()
        }
    }

    override fun hideLoading() {
        if (loadView.isShown()) {
            loadView.stop()
            loadView.setVisibility(View.GONE)
        }
    }

}