package com.mobile.rxjava2andretrofit2.first_page.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.rxjava2andretrofit2.R;
import com.mobile.rxjava2andretrofit2.callback.RcvOnItemViewClickListener;
import com.mobile.rxjava2andretrofit2.first_page.bean.FirstPageResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirstPageAdapter extends RecyclerView.Adapter {

    private static final String TAG = "FirstPageAdapter";
    private Context context;
    private List<FirstPageResponse.AnsListBean> ansListBeanList = new ArrayList<>();

    public FirstPageAdapter(Context context) {
        this.context = context;
    }

    public FirstPageAdapter(Context context, List<FirstPageResponse.AnsListBean> ansListBeanList) {
        this.context = context;
        this.ansListBeanList = ansListBeanList;
    }

    public void clearData() {
        this.ansListBeanList.clear();
        notifyDataSetChanged();
    }

    public void addAllData(List<FirstPageResponse.AnsListBean> ansListBeanList) {
        this.ansListBeanList.addAll(ansListBeanList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_first_page, parent, false);
        return new ContentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentHolder) {
            ContentHolder contentHolder = (ContentHolder) holder;

            contentHolder.tevAnsid.setText(ansListBeanList.get(position).getAnsid());
            contentHolder.tevData.setText(ansListBeanList.get(position).getContent_abstract().getText());


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
        return ansListBeanList.size();
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
