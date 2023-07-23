package com.phone.module_main.ui

import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.phone.library_base.base.BaseRxAppActivity
import com.phone.library_custom_view.bean.EventScheduleListBean.DataDTO.RowsDTO
import com.phone.library_custom_view.custom_view.EventScheduleLayout
import com.phone.module_main.R

class CustomViewActivity : BaseRxAppActivity() {

    companion object {
        private val TAG = CustomViewActivity::class.java.simpleName
    }

    val rowsDTOList = mutableListOf<RowsDTO>()
    private lateinit var layoutEventSchedule: FrameLayout

    override fun initLayoutId(): Int {
        return R.layout.main_activity_custom_view
    }

    override fun initData() {
        val rowsDTO = RowsDTO()
        rowsDTO.activeStartTime = "2021-04-29 07:15:00"
        rowsDTO.activeEndTime = "2021-04-29 09:20:00"
        rowsDTO.activeTime = "2021-04-29 07:15-09:20"
        rowsDTO.activeTitle = "新活动"
        rowsDTO.auditStatusName = "待审核"
        val rowsDTO2 = RowsDTO()
        rowsDTO2.activeStartTime = "2021-04-29 10:25:00"
        rowsDTO2.activeEndTime = "2021-04-29 12:30:00"
        rowsDTO2.activeTime = "2021-04-29 10:25-12:30"
        rowsDTO2.activeTitle = "新活动2"
        rowsDTO2.auditStatusName = "待审核"
        val rowsDTO3 = RowsDTO()
        rowsDTO3.activeStartTime = "2021-04-29 14:30:00"
        rowsDTO3.activeEndTime = "2021-04-29 17:10:00"
        rowsDTO3.activeTime = "2021-04-29 14:30-17:10"
        rowsDTO3.activeTitle = "新活动3"
        rowsDTO3.auditStatusName = "待审核"
        val rowsDTO5 = RowsDTO()
        rowsDTO5.activeStartTime = "2021-04-29 18:10:00"
        rowsDTO5.activeEndTime = "2021-04-29 20:10:00"
        rowsDTO5.activeTime = "2021-04-29 18:10-20:10"
        rowsDTO5.activeTitle = "新活动5"
        rowsDTO5.auditStatusName = "待审核"
        val rowsDTO6 = RowsDTO()
        rowsDTO6.activeStartTime = "2021-04-29 21:10:00"
        rowsDTO6.activeEndTime = "2021-04-29 23:00:00"
        rowsDTO6.activeTime = "2021-04-29 21:10-23:00"
        rowsDTO6.activeTitle = "新活动6"
        rowsDTO6.auditStatusName = "待审核"
        rowsDTOList.add(rowsDTO)
        rowsDTOList.add(rowsDTO2)
        rowsDTOList.add(rowsDTO3)
        rowsDTOList.add(rowsDTO5)
        rowsDTOList.add(rowsDTO6)
    }

    override fun initViews() {
        layoutEventSchedule = findViewById<View>(R.id.layout_event_schedule) as FrameLayout

        val eventScheduleLayout = EventScheduleLayout(this, rowsDTOList)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        layoutEventSchedule.addView(eventScheduleLayout, layoutParams)
    }

    override fun initLoadData() {

    }

}