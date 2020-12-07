package com.mobile.rxjava2andretrofit2.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.rxjava2andretrofit2.R;
import com.mobile.rxjava2andretrofit2.callback.RcvOnItemViewClickListener;
import com.mobile.rxjava2andretrofit2.first_page.bean.FirstPageResponse;
import com.mobile.rxjava2andretrofit2.mine.bean.MineResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MineAdapter extends RecyclerView.Adapter {

    private static final String TAG = "MineAdapter";
    private Context context;
    private List<MineResponse.AnsListBean> list = new ArrayList<>();

    public MineAdapter(Context context) {
        this.context = context;
    }

    public MineAdapter(Context context, List<MineResponse.AnsListBean> list) {
        this.context = context;
        this.list = list;
    }

    public void clearData() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public void addAllData(List<MineResponse.AnsListBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mine, parent, false);
        return new ContentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentHolder) {
            ContentHolder contentHolder = (ContentHolder) holder;

            contentHolder.tevAnsid.setText(list.get(position).getAnsid());
            contentHolder.tevData.setText(list.get(position).getContent_abstract().getText());

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
        return list.size();
    }

    static class ContentHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tev_ansid)
        TextView tevAnsid;
        @BindView(R.id.tev_data)
        TextView tevData;

        private ContentHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private RcvOnItemViewClickListener rcvOnItemViewClickListener;

    public void setRcvOnItemViewClickListener(RcvOnItemViewClickListener rcvOnItemViewClickListener) {
        this.rcvOnItemViewClickListener = rcvOnItemViewClickListener;
    }
}
