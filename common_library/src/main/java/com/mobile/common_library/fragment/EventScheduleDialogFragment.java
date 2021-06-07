package com.mobile.common_library.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobile.common_library.R;
import com.mobile.common_library.bean.EventScheduleListBean;
import com.mobile.common_library.callback.OnDialogCallback;
import com.mobile.common_library.custom_view.EventScheduleLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;

import static android.view.View.VISIBLE;

/**
 * author : Urasaki
 * e-mail : 1164688204@qq.com
 * date   : 2021/4/28 15:55
 * desc   : 活动日程弹框
 * version: 1.0
 */
public class EventScheduleDialogFragment extends DialogFragment {
	
	private static final String                                      TAG         = "EventScheduleDialogFragment";
	private              TextView                                    tevEventSchedule;
	private              FrameLayout                                 layoutCloseDialog;
	private              NestedScrollView                            nestedScrollView;
	private              FrameLayout                                 layoutEventSchedule;
	private              List<EventScheduleListBean.DataDTO.RowsDTO> rowsDTOList = new ArrayList<>();
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_fragment_event_schedule, container, false);
		
		initViews(view);
		return view;
	}
	
	private void initViews(View rootView) {
		tevEventSchedule = (TextView) rootView.findViewById(R.id.tev_event_schedule);
		layoutCloseDialog = (FrameLayout) rootView.findViewById(R.id.layout_close_dialog);
		nestedScrollView = (NestedScrollView) rootView.findViewById(R.id.nested_scroll_view);
		layoutEventSchedule = (FrameLayout) rootView.findViewById(R.id.layout_event_schedule);
		
		layoutCloseDialog.setOnClickListener(view1 -> {
			dismiss();
		});
		
		setSuccessView();
	}
	
	public static EventScheduleDialogFragment newInstance() {
		EventScheduleDialogFragment fragment = new EventScheduleDialogFragment();
		return fragment;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		// 下面这些设置必须在此方法(onStart())中才有效
		Window window = getDialog().getWindow();
		// 如果不设置这句代码, 那么弹框就会与四边都有一定的距离
		//        window.setBackgroundDrawableResource(android.R.color.transparent);
		window.setBackgroundDrawableResource(R.drawable.corners_14_color_white);
		// 设置动画
		//		window.setWindowAnimations(R.style.BottomDialogAnimation);
		
		WindowManager.LayoutParams params = window.getAttributes();
		params.gravity = Gravity.BOTTOM;
		// 如果不设置宽度,那么即使你在布局中设置宽度为 match_parent 也不会起作用
		params.width = getResources().getDisplayMetrics().widthPixels;
		window.setAttributes(params);
	}
	
	private OnDialogCallback<Integer> onDialogCallback;
	
	public void setOnDialogCallback(OnDialogCallback<Integer> onDialogCallback) {
		this.onDialogCallback = onDialogCallback;
	}
	
	private void setSuccessView() {
		nestedScrollView.setVisibility(VISIBLE);
		EventScheduleListBean.DataDTO.RowsDTO rowsDTO = new EventScheduleListBean.DataDTO.RowsDTO();
		rowsDTO.setActiveStartTime("2021-04-29 07:15:00");
		rowsDTO.setActiveEndTime("2021-04-29 09:20:00");
		rowsDTO.setActiveTime("2021-04-29 07:15-09:20");
		rowsDTO.setActiveTitle("新活动");
		rowsDTO.setAuditStatusName("待审核");
		rowsDTO.setAuditStatus(0);
		
		EventScheduleListBean.DataDTO.RowsDTO rowsDTO2 = new EventScheduleListBean.DataDTO.RowsDTO();
		rowsDTO2.setActiveStartTime("2021-04-29 10:25:00");
		rowsDTO2.setActiveEndTime("2021-04-29 12:30:00");
		rowsDTO2.setActiveTime("2021-04-29 10:25-12:30");
		rowsDTO2.setActiveTitle("新活动2");
		rowsDTO2.setAuditStatusName("已审核");
		rowsDTO2.setAuditStatus(2);
		
		EventScheduleListBean.DataDTO.RowsDTO rowsDTO3 = new EventScheduleListBean.DataDTO.RowsDTO();
		rowsDTO3.setActiveStartTime("2021-04-29 14:30:00");
		rowsDTO3.setActiveEndTime("2021-04-29 17:10:00");
		rowsDTO3.setActiveTime("2021-04-29 14:30-17:10");
		rowsDTO3.setActiveTitle("新活动3");
		rowsDTO3.setAuditStatusName("已审核");
		rowsDTO3.setAuditStatus(2);
		
		EventScheduleListBean.DataDTO.RowsDTO rowsDTO5 = new EventScheduleListBean.DataDTO.RowsDTO();
		rowsDTO5.setActiveStartTime("2021-04-29 18:10:00");
		rowsDTO5.setActiveEndTime("2021-04-29 20:10:00");
		rowsDTO5.setActiveTime("2021-04-29 18:10-20:10");
		rowsDTO5.setActiveTitle("新活动5");
		rowsDTO5.setAuditStatusName("待审核");
		rowsDTO5.setAuditStatus(0);
		
		EventScheduleListBean.DataDTO.RowsDTO rowsDTO6 = new EventScheduleListBean.DataDTO.RowsDTO();
		rowsDTO6.setActiveStartTime("2021-04-29 21:10:00");
		rowsDTO6.setActiveEndTime("2021-04-29 23:00:00");
		rowsDTO6.setActiveTime("2021-04-29 21:10-23:00");
		rowsDTO6.setActiveTitle("新活动6");
		rowsDTO6.setAuditStatusName("已审核");
		rowsDTO6.setAuditStatus(2);
		
		rowsDTOList.clear();
		rowsDTOList.add(rowsDTO);
		rowsDTOList.add(rowsDTO2);
		rowsDTOList.add(rowsDTO3);
		rowsDTOList.add(rowsDTO5);
		rowsDTOList.add(rowsDTO6);
		
		EventScheduleLayout eventScheduleLayout = new EventScheduleLayout(getActivity(), null, rowsDTOList);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,
			LinearLayout.LayoutParams.MATCH_PARENT);
		layoutEventSchedule.addView(eventScheduleLayout, layoutParams);
	}
	
}
