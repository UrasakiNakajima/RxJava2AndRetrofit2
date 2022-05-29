package com.phone.first_page_module.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phone.common_library.callback.RcvOnItemViewClickListener;
import com.phone.common_library.manager.LogManager;
import com.phone.first_page_module.R;
import com.phone.first_page_module.bean.VideoListBean;

import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "VideoListAdapter";
    private Context context;
    private List<VideoListBean.LargeImageListBean> videoListBeanList = new ArrayList<>();

    public VideoListAdapter(Context context) {
        this.context = context;
    }

    public VideoListAdapter(Context context, List<VideoListBean.LargeImageListBean> videoListBeanList) {
        this.context = context;
        this.videoListBeanList = videoListBeanList;
    }

    public void clearData() {
        this.videoListBeanList.clear();
        notifyDataSetChanged();
    }

    public void addAllData(List<VideoListBean.LargeImageListBean> videoListBeanList) {
        this.videoListBeanList.addAll(videoListBeanList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video_list, parent, false);
        return new ContentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentHolder) {
            ContentHolder contentHolder = (ContentHolder) holder;
//            contentHolder.tevCode.setText(videoListBeanList.get(position).getCode());
            String data = videoListBeanList.get(position).getUrl();
            LogManager.i(TAG, "onBindViewHolder data*****" + data);

//            ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams();
            Glide.with(context).load(data).into(contentHolder.imvData);
        }
    }

    @Override
    public int getItemCount() {
        return videoListBeanList.size();
    }

    protected static class ContentHolder extends RecyclerView.ViewHolder {

        private TextView tevId;
        private ImageView imvData;

        protected ContentHolder(@NonNull View itemView) {
            super(itemView);
            tevId = itemView.findViewById(R.id.tev_id);
            imvData = itemView.findViewById(R.id.imv_data);
        }
    }

    private RcvOnItemViewClickListener rcvOnItemViewClickListener;

    public void setRcvOnItemViewClickListener(RcvOnItemViewClickListener rcvOnItemViewClickListener) {
        this.rcvOnItemViewClickListener = rcvOnItemViewClickListener;
    }
}
