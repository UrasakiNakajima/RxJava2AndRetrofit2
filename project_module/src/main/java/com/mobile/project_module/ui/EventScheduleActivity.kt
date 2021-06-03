package com.mobile.project_module.ui

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.mobile.common_library.base.BaseAppActivity
import com.mobile.common_library.callback.OnDialogCallback
import com.mobile.common_library.fragment.EventScheduleDialogFragment
import com.mobile.project_module.R

class EventScheduleActivity : BaseAppActivity() {

    companion object {
        private const val TAG = "EventScheduleActivity"
    }

    private var toolbar: Toolbar? = null
    private var layoutBack: FrameLayout? = null
    private var imvBack: ImageView? = null
    private var tevTitle: TextView? = null
    private var tevEventSchedule: TextView? = null

    override fun initLayoutId(): Int {
        return R.layout.activity_event_schedule
    }

    override fun initData() {

    }

    override fun initViews() {
        toolbar = findViewById(R.id.toolbar);
        layoutBack = findViewById(R.id.layout_back);
        imvBack = findViewById(R.id.imv_back);
        tevTitle = findViewById(R.id.tev_title);
        tevEventSchedule = findViewById(R.id.tev_event_schedule);

        imvBack!!.setColorFilter(ContextCompat.getColor(this, R.color.white))
        layoutBack!!.setOnClickListener {
            finish()
        }
        tevEventSchedule!!.setOnClickListener {
            val eventScheduleDialogFragment: EventScheduleDialogFragment = EventScheduleDialogFragment.newInstance()
            //			ReserveSpaceDialogFragment reserveSpaceDialogFragment=ReserveSpaceDialogFragment.newInstance();
            //			ReserveSpaceDialogFragment reserveSpaceDialogFragment=ReserveSpaceDialogFragment.newInstance();
            eventScheduleDialogFragment.setOnDialogCallback(object : OnDialogCallback<Int?> {

                override fun onDialogClick(view: View?, success: Int?) {
                    if (success == 0) {

                    } else if (success == 1) {

                    } else {

                    }
                }

                override fun onDialogClick(view: View?, success: Int?, params: Map<String?, String?>?) {

                }
            })
            eventScheduleDialogFragment.show(supportFragmentManager, "FOF")
        }


        setToolbar(false, R.color.color_FFE066FF)
    }

    override fun initLoadData() {

    }

}