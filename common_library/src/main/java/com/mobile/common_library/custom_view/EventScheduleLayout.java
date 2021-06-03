package com.mobile.common_library.custom_view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobile.common_library.R;
import com.mobile.common_library.bean.EventScheduleListBean;
import com.mobile.common_library.manager.ScreenManager;

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
 * date   : 2021/4/30 13:36
 * desc   :
 * version: 1.0
 */
public class EventScheduleLayout extends LinearLayout {
	
	private Context                                     mContext;
	private List<EventScheduleListBean.DataDTO.RowsDTO> mRowsDTOList = new ArrayList<>();
	private EventScheduleListBean.DataDTO.RowsDTO       mRowsDTO;
	private String                                      mStartTimeStr;
	private String                                      mEndTimeStr;
	private String                                      mStartTimeNextStr;
	private float                                       mStartTime;
	private long                                        mStartTimeNext;
	private double                                      mEndTime;
	private double                                      mTimeQuantum;
	private double                                      mTimeQuantumNext;
	private int                                         mTotalTimeNumber;
	private int                                         startHourInt;
	private int                                         startHourRealInt;
	private int                                         startMinuteRealInt;
	private int                                         endHourInt;
	private int                                         endHourRealInt;
	private int                                         endMinuteRealInt;
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
			ScreenManager.dipToPx(mContext, 75), LayoutParams.MATCH_PARENT);
		//		LayoutParams layoutParamsStart = new LayoutParams(
		//			ScreenManager.dipToPx(mContext, getResources().getDimension((int) (R.dimen.dp_75))), LayoutParams.MATCH_PARENT);
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
				
				if (i <= mRowsDTOList.size() - 1) {
					if (i == 0) {//第一次，画出左边布局
						mStartTimeStr = mRowsDTO.getActiveStartTime();
						mEndTimeStr = mRowsDTOList.get(mRowsDTOList.size() - 1).getActiveEndTime();
						try {
							mStartTime = dateStrConvertedToMillisecond(mStartTimeStr);
							String[] arr = mStartTimeStr.split(" ");
							mStartTimeStr = arr[1];
							String[] arrHour = mStartTimeStr.split(":");
							startHourRealInt = Integer.valueOf(arrHour[0]);
							startMinuteRealInt = Integer.valueOf(arrHour[1]);
							if (startHourRealInt >= 8) {
								startHourInt = 8;
							} else {
								startHourInt = startHourRealInt;
							}
							
							String[] arrEnd = mEndTimeStr.split(" ");
							mEndTimeStr = arrEnd[1];
							String[] arrHourEnd = mEndTimeStr.split(":");
							endHourRealInt = Integer.parseInt(arrHourEnd[0]);
							endMinuteRealInt = Integer.valueOf(arrHourEnd[1]);
							if (endHourRealInt < 17 && endMinuteRealInt == 0) {
								endHourInt = 18;
							} else if (endHourRealInt == 17 && endMinuteRealInt > 0) {
								endHourInt = 19;
							} else {
								if (endMinuteRealInt == 0) {
									endHourInt = endHourRealInt + 1;
								} else {
									endHourInt = endHourRealInt + 1 + 1;
								}
							}
							mTotalTimeNumber = (endHourInt - startHourInt);
							
							LayoutParams imvHeaderParams = new LayoutParams(
								ScreenManager.dipToPx(mContext, 10),
								ScreenManager.dipToPx(mContext, 9));
							imvHeaderParams.setMarginStart(ScreenManager.dipToPx(mContext, 53));
							ImageView imvHeader = new ImageView(mContext);
							imvHeader.setImageResource(R.mipmap.icon_time_line_triangle);
							layoutStart.addView(imvHeader, imvHeaderParams);
							LayoutParams lineViewParams = new LayoutParams(
								ScreenManager.dipToPx(mContext, 4),
								ScreenManager.dipToPx(mContext, 12));
							lineViewParams.setMarginStart(ScreenManager.dipToPx(mContext, 56f));
							DottedLineView dottedLineView = new DottedLineView(mContext);
							layoutStart.addView(dottedLineView, lineViewParams);
							
							//画左边布局
							for (int j = 0; j < mTotalTimeNumber; j++) {
								if (j == mTotalTimeNumber - 1) {
									//最后一个
									FrameLayout outerLayout = new FrameLayout(mContext);
									LayoutParams outerLayoutParams = new LayoutParams(
										ScreenManager.dipToPx(mContext, 75),
										ScreenManager.dipToPx(mContext, 16));
									
									FrameLayout.LayoutParams tevTimeParams = new FrameLayout.LayoutParams(
										FrameLayout.LayoutParams.WRAP_CONTENT,
										FrameLayout.LayoutParams.WRAP_CONTENT);
									tevTimeParams.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
									tevTimeParams.setMarginEnd(ScreenManager.dipToPx(mContext, 26));
									//时间
									TextView tevTime = new TextView(mContext);
									tevTime.setText(startHourInt + j + ":00");
									tevTime.setTextColor(ContextCompat.getColor(mContext, R.color.color_FF333333));
									tevTime.setTextSize(12);
									outerLayout.addView(tevTime, tevTimeParams);
									
									FrameLayout.LayoutParams lineViewParams2 = new FrameLayout.LayoutParams(
										ScreenManager.dipToPx(mContext, 4),
										ScreenManager.dipToPx(mContext, 3));
									lineViewParams2.setMarginStart(
										ScreenManager.dipToPx(mContext, 56f));
									dottedLineView = new DottedLineView(mContext);
									outerLayout.addView(dottedLineView, lineViewParams2);
									FrameLayout.LayoutParams imvMajorTimeNodeParams = new FrameLayout.LayoutParams(
										ScreenManager.dipToPx(mContext, 10),
										ScreenManager.dipToPx(mContext, 10));
									
									imvMajorTimeNodeParams.gravity = Gravity.CENTER_VERTICAL;
									imvMajorTimeNodeParams.setMarginStart(ScreenManager.dipToPx(mContext, 53));
									//									imvMajorTimeNodeParams.setMargins(
									//										ScreenManager.dipToPx(mContext, 53),
									//										ScreenManager.dipToPx(mContext, 3), 0, 0);
									ImageView imvMajorTimeNode = new ImageView(mContext);
									imvMajorTimeNode.setImageResource(R.mipmap.icon_major_time_node);
									outerLayout.addView(imvMajorTimeNode, imvMajorTimeNodeParams);
									lineViewParams2 = new FrameLayout.LayoutParams(
										ScreenManager.dipToPx(mContext, 4),
										ScreenManager.dipToPx(mContext, 3));
									lineViewParams2.setMargins(
										ScreenManager.dipToPx(mContext, 56f),
										ScreenManager.dipToPx(mContext, 13f), 0, 0);
									dottedLineView = new DottedLineView(mContext);
									outerLayout.addView(dottedLineView, lineViewParams2);
									layoutStart.addView(outerLayout, outerLayoutParams);
									
									lineViewParams = new LayoutParams(
										ScreenManager.dipToPx(mContext, 4),
										ScreenManager.dipToPx(mContext, 21));
									lineViewParams.setMarginStart(ScreenManager.dipToPx(mContext, 56f));
									dottedLineView = new DottedLineView(mContext);
									layoutStart.addView(dottedLineView, lineViewParams);
									imvHeaderParams = new LayoutParams(
										ScreenManager.dipToPx(mContext, 10),
										ScreenManager.dipToPx(mContext, 9));
									imvHeaderParams.setMarginStart(ScreenManager.dipToPx(mContext, 53));
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
										ScreenManager.dipToPx(mContext, 75),
										ScreenManager.dipToPx(mContext, 16));
									
									FrameLayout.LayoutParams tevTimeParams = new FrameLayout.LayoutParams(
										FrameLayout.LayoutParams.WRAP_CONTENT,
										FrameLayout.LayoutParams.WRAP_CONTENT);
									tevTimeParams.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
									tevTimeParams.setMarginEnd(ScreenManager.dipToPx(mContext, 26));
									//时间
									TextView tevTime = new TextView(mContext);
									tevTime.setText(startHourInt + j + ":00");
									tevTime.setTextColor(ContextCompat.getColor(mContext, R.color.color_FF333333));
									tevTime.setTextSize(12);
									tevTime.setIncludeFontPadding(false);
									outerLayout.addView(tevTime, tevTimeParams);
									
									FrameLayout.LayoutParams lineViewParams2 = new FrameLayout.LayoutParams(
										ScreenManager.dipToPx(mContext, 4),
										ScreenManager.dipToPx(mContext, 3));
									lineViewParams2.setMarginStart(
										ScreenManager.dipToPx(mContext, 56f));
									dottedLineView = new DottedLineView(mContext);
									outerLayout.addView(dottedLineView, lineViewParams2);
									FrameLayout.LayoutParams imvMajorTimeNodeParams = new FrameLayout.LayoutParams(
										ScreenManager.dipToPx(mContext, 10),
										ScreenManager.dipToPx(mContext, 10));
									imvMajorTimeNodeParams.gravity = Gravity.CENTER_VERTICAL;
									imvMajorTimeNodeParams.setMarginStart(ScreenManager.dipToPx(mContext, 53));
									//									imvMajorTimeNodeParams.setMargins(
									//										ScreenManager.dipToPx(mContext, 53),
									//										ScreenManager.dipToPx(mContext, 3), 0, 0);
									ImageView imvMajorTimeNode = new ImageView(mContext);
									imvMajorTimeNode.setImageResource(R.mipmap.icon_major_time_node);
									outerLayout.addView(imvMajorTimeNode, imvMajorTimeNodeParams);
									
									lineViewParams2 = new FrameLayout.LayoutParams(
										ScreenManager.dipToPx(mContext, 4),
										ScreenManager.dipToPx(mContext, 3));
									lineViewParams2.setMargins(
										ScreenManager.dipToPx(mContext, 56f),
										ScreenManager.dipToPx(mContext, 13f), 0, 0);
									dottedLineView = new DottedLineView(mContext);
									outerLayout.addView(dottedLineView, lineViewParams2);
									layoutStart.addView(outerLayout, outerLayoutParams);
									
									lineViewParams = new LayoutParams(
										ScreenManager.dipToPx(mContext, 4),
										ScreenManager.dipToPx(mContext, 22));
									lineViewParams.setMarginStart(ScreenManager.dipToPx(mContext, 56));
									dottedLineView = new DottedLineView(mContext);
									layoutStart.addView(dottedLineView, lineViewParams);
									
									LayoutParams imvSmallTimeNodeParams = new LayoutParams(ScreenManager.dipToPx(mContext, 5),
																						   ScreenManager.dipToPx(mContext, 5));
									imvSmallTimeNodeParams.setMarginStart(ScreenManager.dipToPx(mContext, 55.5f));
									ImageView imvSmallTimeNode = new ImageView(mContext);
									imvSmallTimeNode.setImageResource(R.mipmap.icon_small_time_node);
									layoutStart.addView(imvSmallTimeNode, imvSmallTimeNodeParams);
									
									LayoutParams lineViewParams3 = new LayoutParams(ScreenManager.dipToPx(mContext, 4),
																					ScreenManager.dipToPx(mContext, 22));
									lineViewParams3.setMarginStart(ScreenManager.dipToPx(mContext, 56));
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
					if (i < mRowsDTOList.size() - 1) {
						mStartTimeNextStr = mRowsDTOList.get(i + 1).getActiveStartTime();
					} else {
						mStartTimeNextStr = null;
					}
					mEndTimeStr = mRowsDTO.getActiveEndTime();
					//					mTimeStr = mRowsDTO.getActiveTime();
					//					mFrameLayoutList.add(new FrameLayout(mContext));
					try {
						mStartTime = dateStrConvertedToMillisecond(mStartTimeStr);
						mEndTime = dateStrConvertedToMillisecond(mEndTimeStr);
						if (!TextUtils.isEmpty(mStartTimeNextStr)) {
							mStartTimeNext = dateStrConvertedToMillisecond(mStartTimeNextStr);
						} else {
							mStartTimeNext = 0;
						}
						
						mTimeQuantum = mEndTime - mStartTime;
						if (mStartTimeNext > 0) {
							mTimeQuantumNext = mStartTimeNext - mEndTime;
						} else {
							mTimeQuantumNext = 0;
						}
						//有多少个分钟（以1分钟为基本时间单位）
						timeNumber = (mTimeQuantum / 1000 / 60);
						//						float mTimeQuantumFloat = mTimeQuantum;
						//						float hourNumber = mTimeQuantumFloat / 1000 / 60 / 60;
						if (mTimeQuantumNext > 0) {
							//空布局需要添加的高度数
							timeNumberNext = (mTimeQuantumNext / 1000 / 60);
						} else {
							timeNumberNext = 0;
						}
						
						if (startHourRealInt >= 8 && i == 0) {
							int num = (startHourRealInt - startHourInt) * 60 + startMinuteRealInt;
							
							FrameLayout mEmptyLayout = new FrameLayout(mContext);
							LayoutParams mEmptyLayoutParams =
								new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
												 ScreenManager.dipToPx(mContext, (float) (65d * num / 60d)));
							layoutEnd.addView(mEmptyLayout, mEmptyLayoutParams);
						} else if (startHourRealInt < 8 && i == 0) {
							int num = startMinuteRealInt;
							FrameLayout mEmptyLayout = new FrameLayout(mContext);
							LayoutParams mEmptyLayoutParams =
								new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
												 ScreenManager.dipToPx(mContext, (float) (65d * num / 60d)));
							layoutEnd.addView(mEmptyLayout, mEmptyLayoutParams);
						}
						FrameLayout mFrameLayout = new FrameLayout(mContext);
						
						TextView tevTime = new TextView(mContext);
						tevTime.setText(mRowsDTO.getActiveTime());
						tevTime.setTextColor(ContextCompat.getColor(mContext, R.color.color_FF999999));
						tevTime.setTextSize(12);
						tevTime.setIncludeFontPadding(false);
						FrameLayout.LayoutParams tevTimeParams =
							new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
														 FrameLayout.LayoutParams.WRAP_CONTENT);
						tevTimeParams.setMargins(ScreenManager.dipToPx(mContext, 12),
												 ScreenManager.dipToPx(mContext, 12), 0, 0);
						tevTimeParams.gravity = Gravity.START | Gravity.TOP;
						tevTime.setLayoutParams(tevTimeParams);
						//						mFrameLayout.addView(tevTime, tevTimeParams);
						mFrameLayout.addView(tevTime);
						
						TextView tevContent = new TextView(mContext);
						tevContent.setText(mRowsDTO.getActiveTitle());
						tevContent.setTextColor(ContextCompat.getColor(mContext, R.color.color_FF333333));
						tevContent.setTextSize(13);
						tevContent.setIncludeFontPadding(false);
						tevContent.setLines(2);
						tevContent.setEllipsize(TextUtils.TruncateAt.END);
						
						FrameLayout.LayoutParams tevContentParams =
							new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
														 FrameLayout.LayoutParams.WRAP_CONTENT);
						tevContentParams.setMargins(ScreenManager.dipToPx(mContext, 13),
													ScreenManager.dipToPx(mContext, 31),
													ScreenManager.dipToPx(mContext, 13), 0);
						tevContentParams.gravity = Gravity.START | Gravity.TOP;
						tevContent.setLayoutParams(tevContentParams);
						//						mFrameLayout.addView(tevContent, tevContentParams);
						mFrameLayout.addView(tevContent);
						TextView tevStatus = new TextView(mContext);
						//						tevStatus.setText(mRowsDTO.getAuditStatusName());
						tevStatus.setTextColor(ContextCompat.getColor(mContext, R.color.color_FF3258F7));
						tevStatus.setTextSize(12);
						tevStatus.setIncludeFontPadding(false);
						
						if (mRowsDTO.getAuditStatus() == 0) {
							mFrameLayout.setBackground(getResources().getDrawable(R.drawable.corners_7_color_ffd0d6ec));
							tevStatus.setText(getResources().getString(R.string.check_pending));
						} else if (mRowsDTO.getAuditStatus() == 2) {
							mFrameLayout.setBackground(getResources().getDrawable(R.drawable.corners_7_color_ffcfe9ff));
							tevStatus.setText(getResources().getString(R.string.checked));
						}
						FrameLayout.LayoutParams tevStatusParams =
							new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
														 FrameLayout.LayoutParams.WRAP_CONTENT);
						tevStatusParams.setMargins(0, ScreenManager.dipToPx(mContext, 10),
												   ScreenManager.dipToPx(mContext, 13), 0);
						tevStatusParams.gravity = Gravity.END | Gravity.TOP;
						tevStatus.setLayoutParams(tevStatusParams);
						//						mFrameLayout.addView(tevStatus, tevStatusParams);
						mFrameLayout.addView(tevStatus);
						
						LayoutParams mFrameLayoutParams;
						if (i == 0) {
							mFrameLayoutParams =
								new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
												 ScreenManager.dipToPx(mContext, (float) (65d * timeNumber / 60d)
																	   //																					 + ((hourNumber - 1) * 10)
												 ));
							mFrameLayoutParams.setMargins(0, ScreenManager.dipToPx(mContext, 24), ScreenManager.dipToPx(mContext, 26), 0);
						} else {
							mFrameLayoutParams =
								new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
												 ScreenManager.dipToPx(mContext, (float) (65d * timeNumber / 60d)
																	   //																					 + ((hourNumber - 1) * 10)
												 ));
							mFrameLayoutParams.setMarginEnd(ScreenManager.dipToPx(mContext, 26));
						}
						
						layoutEnd.addView(mFrameLayout, mFrameLayoutParams);
						if (timeNumberNext > 0) {
							FrameLayout mEmptyLayout = new FrameLayout(mContext);
							//						mEmptyLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_FF9FACFF));
							LayoutParams mEmptyLayoutParams =
								new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
												 ScreenManager.dipToPx(mContext, (float) (65d * timeNumberNext / 60d)));
							layoutEnd.addView(mEmptyLayout, mEmptyLayoutParams);
						}
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
						//						mFrameLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_FFFF0000));
						mFrameLayout.setBackground(getResources().getDrawable(R.drawable.corners_7_color_ffd0d6ec));
						
						LayoutParams mFrameLayoutParams =
							new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
											 ScreenManager.dipToPx(mContext, (float) (65d * timeNumber / 60d)
																   //																				 + (+((hourNumber - 1) * 10))
											 ));
						mFrameLayoutParams.setMarginEnd(ScreenManager.dipToPx(mContext, 26));
						FrameLayout.LayoutParams tevTimeParams =
							new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
														 FrameLayout.LayoutParams.WRAP_CONTENT);
						TextView tevTime = new TextView(mContext);
						tevTime.setText(mRowsDTO.getActiveTime());
						tevTime.setTextColor(ContextCompat.getColor(mContext, R.color.color_FF999999));
						tevTime.setTextSize(12);
						tevTime.setIncludeFontPadding(false);
						tevTimeParams.setMargins(ScreenManager.dipToPx(mContext, 12),
												 ScreenManager.dipToPx(mContext, 12), 0, 0);
						tevTimeParams.gravity = Gravity.START | Gravity.TOP;
						mFrameLayout.addView(tevTime, tevTimeParams);
						FrameLayout.LayoutParams tevContentParams =
							new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
														 FrameLayout.LayoutParams.WRAP_CONTENT);
						TextView tevContent = new TextView(mContext);
						tevContent.setText(mRowsDTO.getActiveTitle());
						tevContent.setTextColor(ContextCompat.getColor(mContext, R.color.color_FF333333));
						tevContent.setTextSize(13);
						tevContent.setIncludeFontPadding(false);
						tevContent.setLines(2);
						tevContent.setEllipsize(TextUtils.TruncateAt.END);
						tevContentParams.setMargins(ScreenManager.dipToPx(mContext, 13),
													ScreenManager.dipToPx(mContext, 31),
													ScreenManager.dipToPx(mContext, 13), 0);
						tevContentParams.gravity = Gravity.START | Gravity.TOP;
						mFrameLayout.addView(tevContent, tevContentParams);
						FrameLayout.LayoutParams tevStatusParams =
							new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
														 FrameLayout.LayoutParams.WRAP_CONTENT);
						TextView tevStatus = new TextView(mContext);
						tevStatus.setText(mRowsDTO.getAuditStatusName());
						tevStatus.setTextColor(ContextCompat.getColor(mContext, R.color.color_FF3258F7));
						tevStatus.setTextSize(12);
						tevStatus.setIncludeFontPadding(false);
						if (mRowsDTO.getAuditStatus() == 0) {
							mFrameLayout.setBackground(getResources().getDrawable(R.drawable.corners_7_color_ffd0d6ec));
							tevStatus.setText(getResources().getString(R.string.check_pending));
						} else if (mRowsDTO.getAuditStatus() == 2) {
							mFrameLayout.setBackground(getResources().getDrawable(R.drawable.corners_7_color_ffcfe9ff));
							tevStatus.setText(getResources().getString(R.string.checked));
						}
						tevStatusParams.setMargins(0, ScreenManager.dipToPx(mContext, 10),
												   ScreenManager.dipToPx(mContext, 13), 0);
						tevStatusParams.gravity = Gravity.END | Gravity.TOP;
						mFrameLayout.addView(tevStatus, tevStatusParams);
						layoutEnd.addView(mFrameLayout, mFrameLayoutParams);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			//第一次，画出左边布局
			startHourInt = 8;
			endHourInt = 18;
			mTotalTimeNumber = (endHourInt - startHourInt);
			
			LayoutParams imvHeaderParams = new LayoutParams(
				ScreenManager.dipToPx(mContext, 10),
				ScreenManager.dipToPx(mContext, 9));
			imvHeaderParams.setMarginStart(ScreenManager.dipToPx(mContext, 53));
			ImageView imvHeader = new ImageView(mContext);
			imvHeader.setImageResource(R.mipmap.icon_time_line_triangle);
			layoutStart.addView(imvHeader, imvHeaderParams);
			LayoutParams lineViewParams = new LayoutParams(
				ScreenManager.dipToPx(mContext, 4),
				ScreenManager.dipToPx(mContext, 12));
			lineViewParams.setMarginStart(ScreenManager.dipToPx(mContext, 56f));
			DottedLineView dottedLineView = new DottedLineView(mContext);
			layoutStart.addView(dottedLineView, lineViewParams);
			
			//画左边布局
			for (int j = 0; j < mTotalTimeNumber; j++) {
				if (j == mTotalTimeNumber - 1) {
					//最后一个
					FrameLayout outerLayout = new FrameLayout(mContext);
					LayoutParams outerLayoutParams = new LayoutParams(
						ScreenManager.dipToPx(mContext, 75),
						ScreenManager.dipToPx(mContext, 16));
					
					FrameLayout.LayoutParams tevTimeParams = new FrameLayout.LayoutParams(
						FrameLayout.LayoutParams.WRAP_CONTENT,
						FrameLayout.LayoutParams.WRAP_CONTENT);
					tevTimeParams.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
					tevTimeParams.setMarginEnd(ScreenManager.dipToPx(mContext, 26));
					//时间
					TextView tevTime = new TextView(mContext);
					tevTime.setText(startHourInt + j + ":00");
					tevTime.setTextColor(ContextCompat.getColor(mContext, R.color.color_FF333333));
					tevTime.setTextSize(12);
					outerLayout.addView(tevTime, tevTimeParams);
					
					FrameLayout.LayoutParams lineViewParams2 = new FrameLayout.LayoutParams(
						ScreenManager.dipToPx(mContext, 4),
						ScreenManager.dipToPx(mContext, 3));
					lineViewParams2.setMarginStart(
						ScreenManager.dipToPx(mContext, 56f));
					dottedLineView = new DottedLineView(mContext);
					outerLayout.addView(dottedLineView, lineViewParams2);
					FrameLayout.LayoutParams imvMajorTimeNodeParams = new FrameLayout.LayoutParams(
						ScreenManager.dipToPx(mContext, 10),
						ScreenManager.dipToPx(mContext, 10));
					
					imvMajorTimeNodeParams.gravity = Gravity.CENTER_VERTICAL;
					imvMajorTimeNodeParams.setMarginStart(ScreenManager.dipToPx(mContext, 53));
					//									imvMajorTimeNodeParams.setMargins(
					//										ScreenManager.dipToPx(mContext, 53),
					//										ScreenManager.dipToPx(mContext, 3), 0, 0);
					ImageView imvMajorTimeNode = new ImageView(mContext);
					imvMajorTimeNode.setImageResource(R.mipmap.icon_major_time_node);
					outerLayout.addView(imvMajorTimeNode, imvMajorTimeNodeParams);
					lineViewParams2 = new FrameLayout.LayoutParams(
						ScreenManager.dipToPx(mContext, 4),
						ScreenManager.dipToPx(mContext, 3));
					lineViewParams2.setMargins(
						ScreenManager.dipToPx(mContext, 56f),
						ScreenManager.dipToPx(mContext, 13f), 0, 0);
					dottedLineView = new DottedLineView(mContext);
					outerLayout.addView(dottedLineView, lineViewParams2);
					layoutStart.addView(outerLayout, outerLayoutParams);
					
					lineViewParams = new LayoutParams(
						ScreenManager.dipToPx(mContext, 4),
						ScreenManager.dipToPx(mContext, 21));
					lineViewParams.setMarginStart(ScreenManager.dipToPx(mContext, 56f));
					dottedLineView = new DottedLineView(mContext);
					layoutStart.addView(dottedLineView, lineViewParams);
					imvHeaderParams = new LayoutParams(
						ScreenManager.dipToPx(mContext, 10),
						ScreenManager.dipToPx(mContext, 9));
					imvHeaderParams.setMarginStart(ScreenManager.dipToPx(mContext, 53));
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
						ScreenManager.dipToPx(mContext, 75),
						ScreenManager.dipToPx(mContext, 16));
					
					FrameLayout.LayoutParams tevTimeParams = new FrameLayout.LayoutParams(
						FrameLayout.LayoutParams.WRAP_CONTENT,
						FrameLayout.LayoutParams.WRAP_CONTENT);
					tevTimeParams.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
					tevTimeParams.setMarginEnd(ScreenManager.dipToPx(mContext, 26));
					//时间
					TextView tevTime = new TextView(mContext);
					tevTime.setText(startHourInt + j + ":00");
					tevTime.setTextColor(ContextCompat.getColor(mContext, R.color.color_FF333333));
					tevTime.setTextSize(12);
					tevTime.setIncludeFontPadding(false);
					outerLayout.addView(tevTime, tevTimeParams);
					
					FrameLayout.LayoutParams lineViewParams2 = new FrameLayout.LayoutParams(
						ScreenManager.dipToPx(mContext, 4),
						ScreenManager.dipToPx(mContext, 3));
					lineViewParams2.setMarginStart(
						ScreenManager.dipToPx(mContext, 56f));
					dottedLineView = new DottedLineView(mContext);
					outerLayout.addView(dottedLineView, lineViewParams2);
					FrameLayout.LayoutParams imvMajorTimeNodeParams = new FrameLayout.LayoutParams(
						ScreenManager.dipToPx(mContext, 10),
						ScreenManager.dipToPx(mContext, 10));
					imvMajorTimeNodeParams.gravity = Gravity.CENTER_VERTICAL;
					imvMajorTimeNodeParams.setMarginStart(ScreenManager.dipToPx(mContext, 53));
					//									imvMajorTimeNodeParams.setMargins(
					//										ScreenManager.dipToPx(mContext, 53),
					//										ScreenManager.dipToPx(mContext, 3), 0, 0);
					ImageView imvMajorTimeNode = new ImageView(mContext);
					imvMajorTimeNode.setImageResource(R.mipmap.icon_major_time_node);
					outerLayout.addView(imvMajorTimeNode, imvMajorTimeNodeParams);
					
					lineViewParams2 = new FrameLayout.LayoutParams(
						ScreenManager.dipToPx(mContext, 4),
						ScreenManager.dipToPx(mContext, 3));
					lineViewParams2.setMargins(
						ScreenManager.dipToPx(mContext, 56f),
						ScreenManager.dipToPx(mContext, 13f), 0, 0);
					dottedLineView = new DottedLineView(mContext);
					outerLayout.addView(dottedLineView, lineViewParams2);
					layoutStart.addView(outerLayout, outerLayoutParams);
					
					lineViewParams = new LayoutParams(
						ScreenManager.dipToPx(mContext, 4),
						ScreenManager.dipToPx(mContext, 22));
					lineViewParams.setMarginStart(ScreenManager.dipToPx(mContext, 56));
					dottedLineView = new DottedLineView(mContext);
					layoutStart.addView(dottedLineView, lineViewParams);
					
					LayoutParams imvSmallTimeNodeParams = new LayoutParams(ScreenManager.dipToPx(mContext, 5),
																		   ScreenManager.dipToPx(mContext, 5));
					imvSmallTimeNodeParams.setMarginStart(ScreenManager.dipToPx(mContext, 55.5f));
					ImageView imvSmallTimeNode = new ImageView(mContext);
					imvSmallTimeNode.setImageResource(R.mipmap.icon_small_time_node);
					layoutStart.addView(imvSmallTimeNode, imvSmallTimeNodeParams);
					
					LayoutParams lineViewParams3 = new LayoutParams(ScreenManager.dipToPx(mContext, 4),
																	ScreenManager.dipToPx(mContext, 22));
					lineViewParams3.setMarginStart(ScreenManager.dipToPx(mContext, 56));
					DottedLineView dottedLineView2 = new DottedLineView(mContext);
					layoutStart.addView(dottedLineView2, lineViewParams3);
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
