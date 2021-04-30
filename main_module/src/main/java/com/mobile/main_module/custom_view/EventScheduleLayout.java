package com.mobile.main_module.custom_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobile.common_library.manager.ScreenManager;
import com.mobile.main_module.R;
import com.mobile.main_module.bean.EventScheduleListBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * author : Urasaki
 * e-mail : 1164688204@qq.com
 * date   : 2021/4/2910:36
 * desc   :
 * version: 1.0
 */
public class EventScheduleLayout extends LinearLayout {
	
	private Context                                     mContext;
	//	private List<FrameLayout>                           mFrameLayoutList       = new ArrayList<>();
	//	private List<LinearLayout.LayoutParams>             mFrameLayoutParamsList = new ArrayList<>();
	//	private List<TextView>                              mTevTimeList           = new ArrayList<>();
	//	private List<FrameLayout.LayoutParams>              mTevTimeParamsList     = new ArrayList<>();
	//	private List<TextView>                              mTevContentList        = new ArrayList<>();
	//	private List<FrameLayout.LayoutParams>              mTevContentParamsList  = new ArrayList<>();
	//	private List<TextView>                              mTevStatusList         = new ArrayList<>();
	//	private List<FrameLayout.LayoutParams>              mTevStatusParamsList   = new ArrayList<>();
	private List<EventScheduleListBean.DataDTO.RowsDTO> mRowsDTOList = new ArrayList<>();
	private EventScheduleListBean.DataDTO.RowsDTO       mRowsDTO;
	private String                                      mStartTimeStr;
	private String                                      mEndTimeStr;
	private String                                      mStartTimeNextStr;
	private String                                      mTimeStr;
	private String                                      mContentStr;
	private String                                      mStatusStr;
	private String                                      mZeroTimeStr;
	private long                                        mStartTime;
	private long                                        mZeroTime;
	private long                                        mStartTimeNext;
	private long                                        mEndTime;
	private long                                        mTimeQuantum;
	private long                                        mTimeQuantumNext;
	
	public EventScheduleLayout(Context context, @Nullable AttributeSet attrs, List<EventScheduleListBean.DataDTO.RowsDTO> rowsDTOList) {
		super(context, attrs);
		setOrientation(VERTICAL);
		
		mContext = context;
		initViews(rowsDTOList);
	}
	
	private void initViews(List<EventScheduleListBean.DataDTO.RowsDTO> rowsDTOList) {
		if (rowsDTOList != null && rowsDTOList.size() > 0) {
			mRowsDTOList.addAll(rowsDTOList);
			for (int i = 0; i < mRowsDTOList.size(); i++) {
				mRowsDTO = mRowsDTOList.get(i);
				
				if (i < mRowsDTOList.size() - 1) {
					if (i == 0) {
						mStartTimeStr = mRowsDTO.getActiveStartTime();
						String[] arr = mStartTimeStr.split(" ");
						if (arr != null && arr.length > 0) {
							arr[1] = "00:00:00";
							mZeroTimeStr = arr[0] + " " + arr[1];
							try {
								mZeroTime = dateStrConvertedToMillisecond(mZeroTimeStr);
								mStartTime = dateStrConvertedToMillisecond(mStartTimeStr);
								if (mStartTime > mZeroTime) {
									long timeNumber = (mStartTime - mZeroTime) / 1000 / 60;
									FrameLayout mEmptyLayout0 = new FrameLayout(mContext);
									//						mEmptyLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_FF9FACFF));
									LayoutParams mEmptyLayoutParams0 =
										new LayoutParams(ScreenManager.dipTopx(mContext, 259),
														 ScreenManager.dipTopx(mContext, 65f * timeNumber / 60f));
									this.addView(mEmptyLayout0, mEmptyLayoutParams0);
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					}
					
					mStartTimeStr = mRowsDTO.getActiveStartTime();
					mStartTimeNextStr = mRowsDTOList.get(i + 1).getActiveStartTime();
					mEndTimeStr = mRowsDTO.getActiveEndTime();
					mTimeStr = mRowsDTO.getActiveTime();
					//					mFrameLayoutList.add(new FrameLayout(mContext));
					try {
						mStartTime = dateStrConvertedToMillisecond(mStartTimeStr);
						mEndTime = dateStrConvertedToMillisecond(mEndTimeStr);
						mStartTimeNext = dateStrConvertedToMillisecond(mStartTimeNextStr);
						mTimeQuantum = mEndTime - mStartTime;
						mTimeQuantumNext = mStartTimeNext - mEndTime;
						//有多少个分钟（以1分钟为基本时间单位）
						int timeNumber = (int) (mTimeQuantum / 1000 / 60);
						int timeNumberNext = (int) (mTimeQuantumNext / 1000 / 60);
						FrameLayout mFrameLayout = new FrameLayout(mContext);
						//						mFrameLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_FFFF0000));
						mFrameLayout.setBackground(getResources().getDrawable(R.drawable.corners_7_color_ffd0d6ec));
						
						TextView tevTime = new TextView(mContext);
						tevTime.setText(mRowsDTO.getActiveTime());
						tevTime.setTextColor(ContextCompat.getColor(mContext, R.color.color_FF999999));
						tevTime.setTextSize(12);
						//						tevTime.setIncludeFontPadding(false);
						FrameLayout.LayoutParams tevTimeParams =
							new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
														 FrameLayout.LayoutParams.WRAP_CONTENT);
						tevTimeParams.setMargins(ScreenManager.dipTopx(mContext, 12),
												 ScreenManager.dipTopx(mContext, 12), 0, 0);
						tevTimeParams.gravity = Gravity.START | Gravity.TOP;
						tevTime.setLayoutParams(tevTimeParams);
						//						mFrameLayout.addView(tevTime, tevTimeParams);
						mFrameLayout.addView(tevTime);
						
						TextView tevContent = new TextView(mContext);
						tevContent.setText(mRowsDTO.getActiveTitle());
						tevContent.setTextColor(ContextCompat.getColor(mContext, R.color.color_FF333333));
						tevContent.setTextSize(13);
						//						tevContent.setIncludeFontPadding(false);
						FrameLayout.LayoutParams tevContentParams =
							new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
														 FrameLayout.LayoutParams.WRAP_CONTENT);
						tevContentParams.setMargins(ScreenManager.dipTopx(mContext, 13),
													ScreenManager.dipTopx(mContext, 31), 0, 0);
						tevContentParams.gravity = Gravity.START | Gravity.TOP;
						tevContent.setLayoutParams(tevContentParams);
						//						mFrameLayout.addView(tevContent, tevContentParams);
						mFrameLayout.addView(tevContent);
						TextView tevStatus = new TextView(mContext);
						tevStatus.setText(mRowsDTO.getAuditStatusName());
						tevStatus.setTextColor(ContextCompat.getColor(mContext, R.color.color_FF3258F7));
						tevStatus.setTextSize(12);
						//						tevStatus.setIncludeFontPadding(false);
						FrameLayout.LayoutParams tevStatusParams =
							new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
														 FrameLayout.LayoutParams.WRAP_CONTENT);
						tevStatusParams.setMargins(0, ScreenManager.dipTopx(mContext, 10),
												   ScreenManager.dipTopx(mContext, 13), 0);
						tevStatusParams.gravity = Gravity.END | Gravity.TOP;
						tevStatus.setLayoutParams(tevStatusParams);
						//						mFrameLayout.addView(tevStatus, tevStatusParams);
						mFrameLayout.addView(tevStatus);
						LayoutParams mFrameLayoutParams =
							new LayoutParams(ScreenManager.dipTopx(mContext, 259),
											 ScreenManager.dipTopx(mContext, 55f / 60f * timeNumber));
						this.addView(mFrameLayout, mFrameLayoutParams);
						FrameLayout mEmptyLayout = new FrameLayout(mContext);
						//						mEmptyLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_FF9FACFF));
						LayoutParams mEmptyLayoutParams =
							new LayoutParams(ScreenManager.dipTopx(mContext, 259),
											 ScreenManager.dipTopx(mContext, 55f / 60f * timeNumberNext));
						this.addView(mEmptyLayout, mEmptyLayoutParams);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					mStartTimeStr = mRowsDTO.getActiveStartTime();
					mEndTimeStr = mRowsDTO.getActiveEndTime();
					mTimeStr = mRowsDTO.getActiveTime();
					//					mFrameLayoutList.add(new FrameLayout(mContext));
					try {
						mStartTime = dateStrConvertedToMillisecond(mStartTimeStr);
						mEndTime = dateStrConvertedToMillisecond(mEndTimeStr);
						mTimeQuantum = mEndTime - mStartTime;
						//有多少个分钟（以1分钟为基本时间单位）
						int timeNumber = (int) (mTimeQuantum / 1000 / 60);
						FrameLayout mFrameLayout = new FrameLayout(mContext);
						//						mFrameLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_FFFF0000));
						mFrameLayout.setBackground(getResources().getDrawable(R.drawable.corners_7_color_ffd0d6ec));
						
						LayoutParams mFrameLayoutParams =
							new LayoutParams(ScreenManager.dipTopx(mContext, 259),
											 ScreenManager.dipTopx(mContext, 55f / 60f * timeNumber));
						FrameLayout.LayoutParams tevTimeParams =
							new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
														 FrameLayout.LayoutParams.WRAP_CONTENT);
						TextView tevTime = new TextView(mContext);
						tevTime.setText(mRowsDTO.getActiveTime());
						tevTime.setTextColor(ContextCompat.getColor(mContext, R.color.color_FF999999));
						tevTime.setTextSize(12);
						//						tevTime.setIncludeFontPadding(false);
						tevTimeParams.setMargins(ScreenManager.dipTopx(mContext, 12),
												 ScreenManager.dipTopx(mContext, 12), 0, 0);
						tevTimeParams.gravity = Gravity.START + Gravity.TOP;
						mFrameLayout.addView(tevTime, tevTimeParams);
						FrameLayout.LayoutParams tevContentParams =
							new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
														 FrameLayout.LayoutParams.WRAP_CONTENT);
						TextView tevContent = new TextView(mContext);
						tevContent.setText(mRowsDTO.getActiveTitle());
						tevContent.setTextColor(ContextCompat.getColor(mContext, R.color.color_FF333333));
						tevContent.setTextSize(13);
						//						tevContent.setIncludeFontPadding(false);
						tevContentParams.setMargins(ScreenManager.dipTopx(mContext, 13),
													ScreenManager.dipTopx(mContext, 31), 0, 0);
						tevContentParams.gravity = Gravity.START + Gravity.TOP;
						mFrameLayout.addView(tevContent, tevContentParams);
						FrameLayout.LayoutParams tevStatusParams =
							new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
														 FrameLayout.LayoutParams.WRAP_CONTENT);
						TextView tevStatus = new TextView(mContext);
						tevStatus.setText(mRowsDTO.getAuditStatusName());
						tevStatus.setTextColor(ContextCompat.getColor(mContext, R.color.color_FF3258F7));
						tevStatus.setTextSize(12);
						//						tevStatus.setIncludeFontPadding(false);
						tevStatusParams.setMargins(0, ScreenManager.dipTopx(mContext, 10),
												   ScreenManager.dipTopx(mContext, 13), 0);
						tevStatusParams.gravity = Gravity.END + Gravity.TOP;
						mFrameLayout.addView(tevStatus, tevStatusParams);
						this.addView(mFrameLayout, mFrameLayoutParams);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		
	}
	
	/**
	 * 将时间字符串转化成毫秒数
	 *
	 * @param dateStr
	 * @return
	 */
	@SuppressWarnings("JavaDoc")
	public long dateStrConvertedToMillisecond(String dateStr) throws ParseException {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"));
		//24小时制
		@SuppressLint("SimpleDateFormat")
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long time = simpleDateFormat.parse(dateStr).getTime();
		return time;
	}
	
}
