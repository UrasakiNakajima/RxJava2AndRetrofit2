package com.phone.common_library.fragment

import android.view.View
import android.widget.LinearLayout
import com.phone.common_library.R
import com.phone.common_library.base.BaseBindingDialogFragment
import com.phone.common_library.bean.EventScheduleListBean
import com.phone.common_library.callback.OnDialogCallback
import com.phone.common_library.custom_view.EventScheduleLayout
import com.phone.common_library.databinding.DialogFragmentEventScheduleBinding

/**
 * author : Urasaki
 * e-mail : 1164688204@qq.com
 * date   : 2021/4/28 15:55
 * desc   : 活动日程弹框
 * version: 1.0
 */
class EventScheduleDialogFragment :
    BaseBindingDialogFragment<DialogFragmentEventScheduleBinding>() {

    private val TAG = EventScheduleDialogFragment::class.java.simpleName

    private val rowsDTOList: MutableList<EventScheduleListBean.DataDTO.RowsDTO> = ArrayList()

    private fun setSuccessView() {
        mDatabind.nestedScrollView.visibility = View.VISIBLE
        val rowsDTO = EventScheduleListBean.DataDTO.RowsDTO()
        rowsDTO.activeStartTime = "2021-04-29 07:15:00"
        rowsDTO.activeEndTime = "2021-04-29 09:20:00"
        rowsDTO.activeTime = "2021-04-29 07:15-09:20"
        rowsDTO.activeTitle = "新活动"
        rowsDTO.auditStatusName = "待审核"
        rowsDTO.auditStatus = 0
        val rowsDTO2 = EventScheduleListBean.DataDTO.RowsDTO()
        rowsDTO2.activeStartTime = "2021-04-29 10:25:00"
        rowsDTO2.activeEndTime = "2021-04-29 12:30:00"
        rowsDTO2.activeTime = "2021-04-29 10:25-12:30"
        rowsDTO2.activeTitle = "新活动2"
        rowsDTO2.auditStatusName = "已审核"
        rowsDTO2.auditStatus = 2
        val rowsDTO3 = EventScheduleListBean.DataDTO.RowsDTO()
        rowsDTO3.activeStartTime = "2021-04-29 14:30:00"
        rowsDTO3.activeEndTime = "2021-04-29 17:10:00"
        rowsDTO3.activeTime = "2021-04-29 14:30-17:10"
        rowsDTO3.activeTitle = "新活动3"
        rowsDTO3.auditStatusName = "已审核"
        rowsDTO3.auditStatus = 2
        val rowsDTO5 = EventScheduleListBean.DataDTO.RowsDTO()
        rowsDTO5.activeStartTime = "2021-04-29 18:10:00"
        rowsDTO5.activeEndTime = "2021-04-29 20:10:00"
        rowsDTO5.activeTime = "2021-04-29 18:10-20:10"
        rowsDTO5.activeTitle = "新活动5"
        rowsDTO5.auditStatusName = "待审核"
        rowsDTO5.auditStatus = 0
        val rowsDTO6 = EventScheduleListBean.DataDTO.RowsDTO()
        rowsDTO6.activeStartTime = "2021-04-29 21:10:00"
        rowsDTO6.activeEndTime = "2021-04-29 23:00:00"
        rowsDTO6.activeTime = "2021-04-29 21:10-23:00"
        rowsDTO6.activeTitle = "新活动6"
        rowsDTO6.auditStatusName = "已审核"
        rowsDTO6.auditStatus = 2
        rowsDTOList.clear()
        rowsDTOList.add(rowsDTO)
        rowsDTOList.add(rowsDTO2)
        rowsDTOList.add(rowsDTO3)
        rowsDTOList.add(rowsDTO5)
        rowsDTOList.add(rowsDTO6)

        mRxAppCompatActivity?.let {
            val eventScheduleLayout = EventScheduleLayout(it, rowsDTOList)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            mDatabind.layoutEventSchedule.addView(eventScheduleLayout, layoutParams)
        }
    }

    override fun initLayoutId(): Int = R.layout.dialog_fragment_event_schedule

    override fun initData() {
    }

    override fun initViews() {
        mDatabind.layoutCloseDialog.setOnClickListener { dismiss() }
        setSuccessView()
    }

    override fun initLoadData() {
    }

    private var onDialogCallback: OnDialogCallback<Int>? = null

    override fun setOnDialogCallback(onDialogCallback: OnDialogCallback<Int>?) {
        this.onDialogCallback = onDialogCallback
    }

    override fun getOnDialogCallback() = onDialogCallback

}