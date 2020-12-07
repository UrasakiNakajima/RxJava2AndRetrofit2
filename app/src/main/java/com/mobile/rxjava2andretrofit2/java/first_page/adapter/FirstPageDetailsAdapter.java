package com.mobile.rxjava2andretrofit2.java.first_page.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.rxjava2andretrofit2.R;
import com.mobile.rxjava2andretrofit2.java.callback.RcvOnItemViewClickListener;
import com.mobile.rxjava2andretrofit2.java.first_page.bean.FirstPageDetailsResponse;
import com.mobile.rxjava2andretrofit2.java.manager.LogManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirstPageDetailsAdapter extends RecyclerView.Adapter {

    private static final String TAG = "FirstPageDetailsAdapter";
    private Context context;
    private List<FirstPageDetailsResponse.DataBean> dataBeanList = new ArrayList<>();

    public FirstPageDetailsAdapter(Context context) {
        this.context = context;
    }

    public FirstPageDetailsAdapter(Context context, List<FirstPageDetailsResponse.DataBean> dataBeanList) {
        this.context = context;
        this.dataBeanList = dataBeanList;
    }

    public void clearData() {
        this.dataBeanList.clear();
        notifyDataSetChanged();
    }

    public void addAllData(List<FirstPageDetailsResponse.DataBean> dataBeanList) {
        this.dataBeanList.addAll(dataBeanList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_first_page_details, parent, false);
        return new ContentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentHolder) {
            ContentHolder contentHolder = (ContentHolder) holder;
//            contentHolder.tevCode.setText(dataBeanList.get(position).getCode());
            String data = dataBeanList.get(position).getContent();
            LogManager.i(TAG, "onBindViewHolder data*****" + data);
            contentHolder.tevData.setText(data);
            contentHolder.tevData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rcvOnItemViewClickListener.onItemClickListener(position, v);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataBeanList.size();
    }

    protected static class ContentHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tev_code)
        TextView tevCode;
        @BindView(R.id.tev_data)
        TextView tevData;

        public ContentHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private RcvOnItemViewClickListener rcvOnItemViewClickListener;

    public void setRcvOnItemViewClickListener(RcvOnItemViewClickListener rcvOnItemViewClickListener) {
        this.rcvOnItemViewClickListener = rcvOnItemViewClickListener;
    }
}
