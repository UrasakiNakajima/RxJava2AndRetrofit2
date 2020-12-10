package com.mobile.rxjava2andretrofit2.first_page.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobile.rxjava2andretrofit2.R;
import com.mobile.rxjava2andretrofit2.callback.RcvOnItemViewClickListener;
import com.mobile.rxjava2andretrofit2.first_page.bean.VideoListBean;
import com.mobile.rxjava2andretrofit2.manager.LogManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoListAdapter extends RecyclerView.Adapter {

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

        @BindView(R.id.tev_id)
        TextView tevId;
        @BindView(R.id.imv_data)
        ImageView imvData;

        protected ContentHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private RcvOnItemViewClickListener rcvOnItemViewClickListener;

    public void setRcvOnItemViewClickListener(RcvOnItemViewClickListener rcvOnItemViewClickListener) {
        this.rcvOnItemViewClickListener = rcvOnItemViewClickListener;
    }
}
