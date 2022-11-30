package com.phone.module_main.main;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.phone.common_library.bean.EventScheduleListBean;
import com.phone.common_library.custom_view.EventScheduleLayout;
import com.phone.module_main.R;

import java.util.ArrayList;
import java.util.List;

public class CustomViewActivity extends AppCompatActivity {

    private static final String TAG = "CustomViewActivity";
    private FrameLayout layoutEventSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        initViews();

        EventScheduleListBean.DataDTO.RowsDTO rowsDTO = new EventScheduleListBean.DataDTO.RowsDTO();
        rowsDTO.setActiveStartTime("2021-04-29 07:15:00");
        rowsDTO.setActiveEndTime("2021-04-29 09:20:00");
        rowsDTO.setActiveTime("2021-04-29 07:15-09:20");
        rowsDTO.setActiveTitle("新活动");
        rowsDTO.setAuditStatusName("待审核");

        EventScheduleListBean.DataDTO.RowsDTO rowsDTO2 = new EventScheduleListBean.DataDTO.RowsDTO();
        rowsDTO2.setActiveStartTime("2021-04-29 10:25:00");
        rowsDTO2.setActiveEndTime("2021-04-29 12:30:00");
        rowsDTO2.setActiveTime("2021-04-29 10:25-12:30");
        rowsDTO2.setActiveTitle("新活动2");
        rowsDTO2.setAuditStatusName("待审核");

        EventScheduleListBean.DataDTO.RowsDTO rowsDTO3 = new EventScheduleListBean.DataDTO.RowsDTO();
        rowsDTO3.setActiveStartTime("2021-04-29 14:30:00");
        rowsDTO3.setActiveEndTime("2021-04-29 17:10:00");
        rowsDTO3.setActiveTime("2021-04-29 14:30-17:10");
        rowsDTO3.setActiveTitle("新活动3");
        rowsDTO3.setAuditStatusName("待审核");

        EventScheduleListBean.DataDTO.RowsDTO rowsDTO5 = new EventScheduleListBean.DataDTO.RowsDTO();
        rowsDTO5.setActiveStartTime("2021-04-29 18:10:00");
        rowsDTO5.setActiveEndTime("2021-04-29 20:10:00");
        rowsDTO5.setActiveTime("2021-04-29 18:10-20:10");
        rowsDTO5.setActiveTitle("新活动5");
        rowsDTO5.setAuditStatusName("待审核");

        EventScheduleListBean.DataDTO.RowsDTO rowsDTO6 = new EventScheduleListBean.DataDTO.RowsDTO();
        rowsDTO6.setActiveStartTime("2021-04-29 21:10:00");
        rowsDTO6.setActiveEndTime("2021-04-29 23:00:00");
        rowsDTO6.setActiveTime("2021-04-29 21:10-23:00");
        rowsDTO6.setActiveTitle("新活动6");
        rowsDTO6.setAuditStatusName("待审核");

        List<EventScheduleListBean.DataDTO.RowsDTO> rowsDTOList = new ArrayList<>();
        rowsDTOList.add(rowsDTO);
        rowsDTOList.add(rowsDTO2);
        rowsDTOList.add(rowsDTO3);
        rowsDTOList.add(rowsDTO5);
        rowsDTOList.add(rowsDTO6);

        EventScheduleLayout eventScheduleLayout = new EventScheduleLayout(this, rowsDTOList);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        layoutEventSchedule.addView(eventScheduleLayout, layoutParams);

        //		DottedLineView dottedLineView = new DottedLineView(this);
        //		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
        //			ScreenManager.dipTopx(this, 4),
        //			LinearLayout.LayoutParams.MATCH_PARENT);
        //		layoutParams.setMarginStart(ScreenManager.dipTopx(this, 30));
        //		layoutEventSchedule.addView(dottedLineView, layoutParams);
    }

    private void initViews() {
        layoutEventSchedule = (FrameLayout) findViewById(R.id.layout_event_schedule);
    }

}