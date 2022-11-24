package com.phone.main_module.custom_view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.phone.common_library.manager.ResourcesManager;
import com.phone.common_library.manager.ScreenManager;
import com.phone.main_module.R;
import com.phone.main_module.bean.EventScheduleListBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import androidx.annotation.Nullable;

/**
 * author : Urasaki
 * e-mail : 1164688204@qq.com
 * date   : 2021/4/30 13:36
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
	//	private String                                      mTimeStr;
	private String                                      mContentStr;
	private String                                      mStatusStr;
	private String                                      mZeroTimeStr;
	private float                                       mStartTime;
	//	private long                                        mZeroTime;
	private long                                        mStartTimeNext;
	private double                                      mEndTime;
	private double                                      mTimeQuantum;
	private double                                      mTimeQuantumNext;
	private int                                         mTotalTimeNumber;
	private int                                         startHourInt;
	private double                                      timeNumber;
	private double                                      timeNumberNext;
	
	public EventScheduleLayout(Context context, @Nullable AttributeSet attrs, List<EventScheduleListBean.DataDTO.RowsDTO> rowsDTOList) {
		super(context, attrs);
		setOrientation(HORIZONTAL);
		
		mContext = context;
		initViews(rowsDTOList);
	}
	
	private void initViews(List<EventScheduleListBean.DataDTO.RowsDTO> rowsDTOList) {
		LayoutParams layoutParamsStart = new LayoutParams(
			ScreenManager.dpToPx(75), LayoutParams.MATCH_PARENT);
		LayoutParams layoutParamsEnd = new LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		LinearLayout layoutStart = new LinearLayout(mContext);
		layoutStart.setOrientation(VERTICAL);
		LinearLayout layoutEnd = new LinearLayout(mContext);
		layoutEnd.setOrientation(VERTICAL);
		this.addView(layoutStart, layoutParamsStart);
		this.addView(layoutEnd, layoutParamsEnd);
		
		if (rowsDTOList != null && rowsDTOList.size() > 0) {
			mRowsDTOList.addAll(rowsDTOList);
			for (int i = 0; i < mRowsDTOList.size(); i++) {
				mRowsDTO = mRowsDTOList.get(i);
				
				if (i < mRowsDTOList.size() - 1) {
					if (i == 0) {//第一次，画出左边布局
						mStartTimeStr = mRowsDTO.getActiveStartTime();
						try {
							mStartTime = dateStrConvertedToMillisecond(mStartTimeStr);
							String[] arr = mStartTimeStr.split(" ");
							mStartTimeStr = arr[1];
							String[] arrHour = mStartTimeStr.split(":");
							startHourInt = Integer.valueOf(arrHour[0]);
							
							mEndTimeStr = arr[0] + " 25:00:00";
							mEndTime = dateStrConvertedToMillisecond(mEndTimeStr);
							mTotalTimeNumber = (int) ((mEndTime - mStartTime) / 1000 / 60 / 60 + 1);
							
							LayoutParams imvHeaderParams = new LayoutParams(
								ScreenManager.dpToPx(10),
								ScreenManager.dpToPx(9));
							imvHeaderParams.setMarginStart(ScreenManager.dpToPx(53));
							ImageView imvHeader = new ImageView(mContext);
							imvHeader.setImageResource(R.mipmap.icon_time_line_triangle);
							layoutStart.addView(imvHeader, imvHeaderParams);
							LayoutParams lineViewParams = new LayoutParams(
								ScreenManager.dpToPx(4),
								ScreenManager.dpToPx(12));
							lineViewParams.setMarginStart(ScreenManager.dpToPx(56f));
							DottedLineView dottedLineView = new DottedLineView(mContext);
							layoutStart.addView(dottedLineView, lineViewParams);
							
							//画左边布局
							for (int j = 0; j < mTotalTimeNumber; j++) {
								if (j == mTotalTimeNumber - 1) {
									//最后一个
									FrameLayout outerLayout = new FrameLayout(mContext);
									LayoutParams outerLayoutParams = new LayoutParams(
										ScreenManager.dpToPx(75),
										ScreenManager.dpToPx(16));
									
									FrameLayout.LayoutParams tevTimeParams = new FrameLayout.LayoutParams(
										FrameLayout.LayoutParams.WRAP_CONTENT,
										FrameLayout.LayoutParams.WRAP_CONTENT);
									tevTimeParams.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
									tevTimeParams.setMarginEnd(ScreenManager.dpToPx(26));
									//时间
									TextView tevTime = new TextView(mContext);
									tevTime.setText(startHourInt + j + ":00");
									tevTime.setTextColor(ResourcesManager.getColor(R.color.color_FF333333));
									tevTime.setTextSize(12);
									outerLayout.addView(tevTime, tevTimeParams);
									
									FrameLayout.LayoutParams lineViewParams2 = new FrameLayout.LayoutParams(
										ScreenManager.dpToPx(4),
										ScreenManager.dpToPx(3));
									lineViewParams2.setMarginStart(
										ScreenManager.dpToPx(56f));
									dottedLineView = new DottedLineView(mContext);
									outerLayout.addView(dottedLineView, lineViewParams2);
									FrameLayout.LayoutParams imvMajorTimeNodeParams = new FrameLayout.LayoutParams(
										ScreenManager.dpToPx(10),
										ScreenManager.dpToPx(10));
									
									imvMajorTimeNodeParams.gravity = Gravity.CENTER_VERTICAL;
									imvMajorTimeNodeParams.setMarginStart(ScreenManager.dpToPx(53));
									//									imvMajorTimeNodeParams.setMargins(
									//										ScreenManager.dpToPx(53),
									//										ScreenManager.dpToPx(3), 0, 0);
									ImageView imvMajorTimeNode = new ImageView(mContext);
									imvMajorTimeNode.setImageResource(R.mipmap.icon_major_time_node);
									outerLayout.addView(imvMajorTimeNode, imvMajorTimeNodeParams);
									lineViewParams2 = new FrameLayout.LayoutParams(
										ScreenManager.dpToPx(4),
										ScreenManager.dpToPx(3));
									lineViewParams2.setMargins(
										ScreenManager.dpToPx(56f),
										ScreenManager.dpToPx(13f), 0, 0);
									dottedLineView = new DottedLineView(mContext);
									outerLayout.addView(dottedLineView, lineViewParams2);
									layoutStart.addView(outerLayout, outerLayoutParams);
									
									lineViewParams = new LayoutParams(
										ScreenManager.dpToPx(4),
										ScreenManager.dpToPx(21));
									lineViewParams.setMarginStart(ScreenManager.dpToPx(56f));
									dottedLineView = new DottedLineView(mContext);
									layoutStart.addView(dottedLineView, lineViewParams);
									imvHeaderParams = new LayoutParams(
										ScreenManager.dpToPx(10),
										ScreenManager.dpToPx(9));
									imvHeaderParams.setMarginStart(ScreenManager.dpToPx(53));
									imvHeader = new ImageView(mContext);
									imvHeader.setImageResource(R.mipmap.icon_time_line_triangle);
									layoutStart.addView(imvHeader, imvHeaderParams);
									
									//构造ObjectAnimator对象的方法
									ObjectAnimator animator = ObjectAnimator.ofFloat(imvHeader, "rotation", 0.0F, 180.0F);//设置先顺时针360度旋转然后逆时针360度旋转动画
									animator.setDuration(50);//设置旋转时间
									animator.start();//开始执行动画（顺时针旋转动画）
								} else {
									FrameLayout outerLayout = new FrameLayout(mContext);
									LayoutParams outerLayoutParams = new LayoutParams(
										ScreenManager.dpToPx(75),
										ScreenManager.dpToPx(16));
									
									FrameLayout.LayoutParams tevTimeParams = new FrameLayout.LayoutParams(
										FrameLayout.LayoutParams.WRAP_CONTENT,
										FrameLayout.LayoutParams.WRAP_CONTENT);
									tevTimeParams.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
									tevTimeParams.setMarginEnd(ScreenManager.dpToPx(26));
									//时间
									TextView tevTime = new TextView(mContext);
									tevTime.setText(startHourInt + j + ":00");
									tevTime.setTextColor(ResourcesManager.getColor(R.color.color_FF333333));
									tevTime.setTextSize(12);
									tevTime.setIncludeFontPadding(false);
									outerLayout.addView(tevTime, tevTimeParams);
									
									FrameLayout.LayoutParams lineViewParams2 = new FrameLayout.LayoutParams(
										ScreenManager.dpToPx(4),
										ScreenManager.dpToPx(3));
									lineViewParams2.setMarginStart(
										ScreenManager.dpToPx(56f));
									dottedLineView = new DottedLineView(mContext);
									outerLayout.addView(dottedLineView, lineViewParams2);
									FrameLayout.LayoutParams imvMajorTimeNodeParams = new FrameLayout.LayoutParams(
										ScreenManager.dpToPx(10),
										ScreenManager.dpToPx(10));
									imvMajorTimeNodeParams.gravity = Gravity.CENTER_VERTICAL;
									imvMajorTimeNodeParams.setMarginStart(ScreenManager.dpToPx(53));
									//									imvMajorTimeNodeParams.setMargins(
									//										ScreenManager.dpToPx(53),
									//										ScreenManager.dpToPx(3), 0, 0);
									ImageView imvMajorTimeNode = new ImageView(mContext);
									imvMajorTimeNode.setImageResource(R.mipmap.icon_major_time_node);
									outerLayout.addView(imvMajorTimeNode, imvMajorTimeNodeParams);
									
									lineViewParams2 = new FrameLayout.LayoutParams(
										ScreenManager.dpToPx(4),
										ScreenManager.dpToPx(3));
									lineViewParams2.setMargins(
										ScreenManager.dpToPx(56f),
										ScreenManager.dpToPx(13f), 0, 0);
									dottedLineView = new DottedLineView(mContext);
									outerLayout.addView(dottedLineView, lineViewParams2);
									layoutStart.addView(outerLayout, outerLayoutParams);
									
									lineViewParams = new LayoutParams(
										ScreenManager.dpToPx(4),
										ScreenManager.dpToPx(22));
									lineViewParams.setMarginStart(ScreenManager.dpToPx(56));
									dottedLineView = new DottedLineView(mContext);
									layoutStart.addView(dottedLineView, lineViewParams);
									
									LayoutParams imvSmallTimeNodeParams = new LayoutParams(ScreenManager.dpToPx(5),
																						   ScreenManager.dpToPx(5));
									imvSmallTimeNodeParams.setMarginStart(ScreenManager.dpToPx(55.5f));
									ImageView imvSmallTimeNode = new ImageView(mContext);
									imvSmallTimeNode.setImageResource(R.mipmap.icon_small_time_node);
									layoutStart.addView(imvSmallTimeNode, imvSmallTimeNodeParams);
									
									LayoutParams lineViewParams3 = new LayoutParams(ScreenManager.dpToPx(4),
																					ScreenManager.dpToPx(22));
									lineViewParams3.setMarginStart(ScreenManager.dpToPx(56));
									DottedLineView dottedLineView2 = new DottedLineView(mContext);
									layoutStart.addView(dottedLineView2, lineViewParams3);
								}
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					
					//画出右边布局
					mStartTimeStr = mRowsDTO.getActiveStartTime();
					mStartTimeNextStr = mRowsDTOList.get(i + 1).getActiveStartTime();
					mEndTimeStr = mRowsDTO.getActiveEndTime();
					//					mTimeStr = mRowsDTO.getActiveTime();
					//					mFrameLayoutList.add(new FrameLayout(mContext));
					try {
						mStartTime = dateStrConvertedToMillisecond(mStartTimeStr);
						mEndTime = dateStrConvertedToMillisecond(mEndTimeStr);
						mStartTimeNext = dateStrConvertedToMillisecond(mStartTimeNextStr);
						mTimeQuantum = mEndTime - mStartTime;
						mTimeQuantumNext = mStartTimeNext - mEndTime;
						//有多少个分钟（以1分钟为基本时间单位）
						timeNumber = (mTimeQuantum / 1000 / 60);
						//						float mTimeQuantumFloat = mTimeQuantum;
						//						float hourNumber = mTimeQuantumFloat / 1000 / 60 / 60;
						//空布局需要添加的高度数
						timeNumberNext = (mTimeQuantumNext / 1000 / 60);
						FrameLayout mFrameLayout = new FrameLayout(mContext);
						//						mFrameLayout.setBackgroundColor(ResourcesManager.getColor(R.color.color_FFFF0000));
						mFrameLayout.setBackground(getResources().getDrawable(R.drawable.corners_7_color_ffd0d6ec));
						
						TextView tevTime = new TextView(mContext);
						tevTime.setText(mRowsDTO.getActiveTime());
						tevTime.setTextColor(ResourcesManager.getColor(R.color.color_FF999999));
						tevTime.setTextSize(12);
						//						tevTime.setIncludeFontPadding(false);
						FrameLayout.LayoutParams tevTimeParams =
							new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
														 FrameLayout.LayoutParams.WRAP_CONTENT);
						tevTimeParams.setMargins(ScreenManager.dpToPx(12),
												 ScreenManager.dpToPx(12), 0, 0);
						tevTimeParams.gravity = Gravity.START | Gravity.TOP;
						tevTime.setLayoutParams(tevTimeParams);
						//						mFrameLayout.addView(tevTime, tevTimeParams);
						mFrameLayout.addView(tevTime);
						
						TextView tevContent = new TextView(mContext);
						tevContent.setText(mRowsDTO.getActiveTitle());
						tevContent.setTextColor(ResourcesManager.getColor(R.color.color_FF333333));
						tevContent.setTextSize(13);
						//						tevContent.setIncludeFontPadding(false);
						FrameLayout.LayoutParams tevContentParams =
							new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
														 FrameLayout.LayoutParams.WRAP_CONTENT);
						tevContentParams.setMargins(ScreenManager.dpToPx(13),
													ScreenManager.dpToPx(31), 0, 0);
						tevContentParams.gravity = Gravity.START | Gravity.TOP;
						tevContent.setLayoutParams(tevContentParams);
						//						mFrameLayout.addView(tevContent, tevContentParams);
						mFrameLayout.addView(tevContent);
						TextView tevStatus = new TextView(mContext);
						tevStatus.setText(mRowsDTO.getAuditStatusName());
						tevStatus.setTextColor(ResourcesManager.getColor(R.color.color_FF3258F7));
						tevStatus.setTextSize(12);
						//						tevStatus.setIncludeFontPadding(false);
						FrameLayout.LayoutParams tevStatusParams =
							new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
														 FrameLayout.LayoutParams.WRAP_CONTENT);
						tevStatusParams.setMargins(0, ScreenManager.dpToPx(10),
												   ScreenManager.dpToPx(13), 0);
						tevStatusParams.gravity = Gravity.END | Gravity.TOP;
						tevStatus.setLayoutParams(tevStatusParams);
						//						mFrameLayout.addView(tevStatus, tevStatusParams);
						mFrameLayout.addView(tevStatus);
						
						LayoutParams mFrameLayoutParams;
						if (i == 0) {
							mFrameLayoutParams =
								new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
												 ScreenManager.dpToPx((float) (65d * timeNumber / 60d + 1)
																	   //																					 + ((hourNumber - 1) * 10)
												 ));
							mFrameLayoutParams.setMargins(0, ScreenManager.dpToPx(24 + 10), ScreenManager.dpToPx(26), 0);
						} else {
							mFrameLayoutParams =
								new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
												 ScreenManager.dpToPx((float) (65d * timeNumber / 60d + 1)
																	   //																					 + ((hourNumber - 1) * 10)
												 ));
							mFrameLayoutParams.setMarginEnd(ScreenManager.dpToPx(26));
						}
						
						layoutEnd.addView(mFrameLayout, mFrameLayoutParams);
						FrameLayout mEmptyLayout = new FrameLayout(mContext);
						//						mEmptyLayout.setBackgroundColor(ResourcesManager.getColor(R.color.color_FF9FACFF));
						LayoutParams mEmptyLayoutParams =
							new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
											 ScreenManager.dpToPx((float) (65d * timeNumberNext / 60d + 1)));
						layoutEnd.addView(mEmptyLayout, mEmptyLayoutParams);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					mStartTimeStr = mRowsDTO.getActiveStartTime();
					mEndTimeStr = mRowsDTO.getActiveEndTime();
					//					mTimeStr = mRowsDTO.getActiveTime();
					//					mFrameLayoutList.add(new FrameLayout(mContext));
					try {
						mStartTime = dateStrConvertedToMillisecond(mStartTimeStr);
						mEndTime = dateStrConvertedToMillisecond(mEndTimeStr);
						mTimeQuantum = mEndTime - mStartTime;
						//有多少个分钟（以1分钟为基本时间单位）
						timeNumber = (mTimeQuantum / 1000 / 60);
						//						float mTimeQuantumFloat = mTimeQuantum;
						//						float hourNumber = mTimeQuantumFloat / 1000 / 60 / 60;
						FrameLayout mFrameLayout = new FrameLayout(mContext);
						//						mFrameLayout.setBackgroundColor(ResourcesManager.getColor(R.color.color_FFFF0000));
						mFrameLayout.setBackground(getResources().getDrawable(R.drawable.corners_7_color_ffd0d6ec));
						
						LayoutParams mFrameLayoutParams =
							new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
											 ScreenManager.dpToPx((float) (65d * timeNumber / 60d + 1)
																   //																				 + (+((hourNumber - 1) * 10))
											 ));
						mFrameLayoutParams.setMarginEnd(ScreenManager.dpToPx(26));
						FrameLayout.LayoutParams tevTimeParams =
							new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
														 FrameLayout.LayoutParams.WRAP_CONTENT);
						TextView tevTime = new TextView(mContext);
						tevTime.setText(mRowsDTO.getActiveTime());
						tevTime.setTextColor(ResourcesManager.getColor(R.color.color_FF999999));
						tevTime.setTextSize(12);
						//						tevTime.setIncludeFontPadding(false);
						tevTimeParams.setMargins(ScreenManager.dpToPx(12),
												 ScreenManager.dpToPx(12), 0, 0);
						tevTimeParams.gravity = Gravity.START | Gravity.TOP;
						mFrameLayout.addView(tevTime, tevTimeParams);
						FrameLayout.LayoutParams tevContentParams =
							new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
														 FrameLayout.LayoutParams.WRAP_CONTENT);
						TextView tevContent = new TextView(mContext);
						tevContent.setText(mRowsDTO.getActiveTitle());
						tevContent.setTextColor(ResourcesManager.getColor(R.color.color_FF333333));
						tevContent.setTextSize(13);
						//						tevContent.setIncludeFontPadding(false);
						tevContentParams.setMargins(ScreenManager.dpToPx(13),
													ScreenManager.dpToPx(31), 0, 0);
						tevContentParams.gravity = Gravity.START | Gravity.TOP;
						mFrameLayout.addView(tevContent, tevContentParams);
						FrameLayout.LayoutParams tevStatusParams =
							new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
														 FrameLayout.LayoutParams.WRAP_CONTENT);
						TextView tevStatus = new TextView(mContext);
						tevStatus.setText(mRowsDTO.getAuditStatusName());
						tevStatus.setTextColor(ResourcesManager.getColor(R.color.color_FF3258F7));
						tevStatus.setTextSize(12);
						//						tevStatus.setIncludeFontPadding(false);
						tevStatusParams.setMargins(0, ScreenManager.dpToPx(10),
												   ScreenManager.dpToPx(13), 0);
						tevStatusParams.gravity = Gravity.END | Gravity.TOP;
						mFrameLayout.addView(tevStatus, tevStatusParams);
						layoutEnd.addView(mFrameLayout, mFrameLayoutParams);
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
