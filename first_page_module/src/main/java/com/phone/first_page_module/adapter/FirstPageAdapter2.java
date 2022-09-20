package com.phone.first_page_module.adapter;

import android.content.Context;
import android.view.View;

import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.phone.common_library.callback.OnItemViewClickListener;
import com.phone.first_page_module.R;
import com.phone.common_library.bean.FirstPageResponse;

import java.util.List;

public class FirstPageAdapter2
	extends CommonRecycleViewAdapter<FirstPageResponse.ResultData.JuheNewsBean> {
	
	private static final String  TAG = "FirstPageAdapter";
	private              Context mContext;
	
	public FirstPageAdapter2(Context context, int layoutId) {
		super(context, layoutId);
		this.mContext = context;
	}
	
	@Override
	public void convert(ViewHolderHelper holder, FirstPageResponse.ResultData.JuheNewsBean juheNewsBean, int position) {
		String title = juheNewsBean.getTitle();
		String author = juheNewsBean.getAuthor_name();
		String time = juheNewsBean.getDate();
		String imgSrc = juheNewsBean.getThumbnail_pic_s();
		String imgMid = juheNewsBean.getThumbnail_pic_s02();
		String imgRight = juheNewsBean.getThumbnail_pic_s03();
		
		holder.setText(R.id.news_summary_author, author);
		holder.setText(R.id.news_summary_title_tv, title);
		holder.setText(R.id.news_summary_time, time);
		holder.setImageUrl(R.id.news_summary_photo_iv_left, imgSrc);
		holder.setImageUrl(R.id.news_summary_photo_iv_middle, imgMid);
		holder.setImageUrl(R.id.news_summary_photo_iv_right, imgRight);
		holder.setOnClickListener(R.id.ll_root, new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onItemViewClickListener.onItemClickListener(position, holder.itemView);
			}
		});
	}
	
	public void clearData() {
		this.mDatas.clear();
		notifyDataSetChanged();
	}
	
	public void addAllData(List<FirstPageResponse.ResultData.JuheNewsBean> dataBeanList) {
		this.mDatas.addAll(dataBeanList);
		notifyDataSetChanged();
	}
	
	private OnItemViewClickListener onItemViewClickListener;
	
	public void setRcvOnItemViewClickListener(OnItemViewClickListener onItemViewClickListener) {
		this.onItemViewClickListener = onItemViewClickListener;
	}
}
