package com.phone.module_square.function_menu.ui

import android.view.View
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_binding.BaseBindingRxAppActivity
import com.phone.library_base.callback.OnDialogCallback
import com.phone.library_base.common.ConstantData
import com.phone.library_custom_view.fragment.EventScheduleDialogFragment
import com.phone.module_square.R
import com.phone.module_square.databinding.SquareActivityEventScheduleBinding

@Route(path = ConstantData.Route.ROUTE_EVENT_SCHEDULE)
class EventScheduleActivity : BaseBindingRxAppActivity<SquareActivityEventScheduleBinding>() {

    companion object {
        private val TAG = EventScheduleActivity::class.java.simpleName
    }

    override fun initLayoutId() = R.layout.square_activity_event_schedule

    override fun initData() {
    }

    override fun initViews() {
        setToolbar(false, R.color.library_black)

        mDatabind.imvBack.setColorFilter(ContextCompat.getColor(this, R.color.library_white))
        mDatabind.layoutBack.setOnClickListener {
            finish()
        }
        mDatabind.tevEventSchedule.setOnClickListener {
            val eventScheduleDialogFragment = EventScheduleDialogFragment()
            eventScheduleDialogFragment.onDialogCallback = (object : OnDialogCallback<Int> {
                override fun onDialogClick(view: View, success: Int) {
                    if (success == 0) {

                    } else if (success == 1) {

                    } else {

                    }
                }

                override fun onDialogClick(
                    view: View,
                    success: Int,
                    params: Map<String, String>
                ) {

                }

            })
            eventScheduleDialogFragment.show(supportFragmentManager, "FOF")
        }
    }

    override fun initLoadData() {
    }

}