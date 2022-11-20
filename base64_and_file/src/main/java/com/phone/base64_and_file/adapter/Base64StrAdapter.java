package com.phone.base64_and_file.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phone.base64_and_file.R;

import java.util.ArrayList;
import java.util.List;

public class Base64StrAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = Base64StrAdapter.class.getSimpleName();
    public List<String> base64StrList = new ArrayList<>();
    private Context context;

    public Base64StrAdapter(Context context) {
        this.context = context;
    }

    public Base64StrAdapter(List<String> base64StrList, Context context) {
        this.base64StrList = base64StrList;
        this.context = context;
    }

    public void clearData() {
        notifyItemRangeRemoved(0, this.base64StrList.size());
        this.base64StrList.clear();
    }

    public void addData(List<String> base64StrList) {
        notifyItemRangeInserted(this.base64StrList.size(), base64StrList.size());
        this.base64StrList.addAll(base64StrList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_base64_str, parent, false);
        return new BodyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BodyHolder) {
            BodyHolder bodyHolder = (BodyHolder) holder;
            bodyHolder.tevBase64Str.setText(base64StrList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return base64StrList.size();
    }

    private class BodyHolder extends RecyclerView.ViewHolder {

        private TextView tevBase64Str;

        public BodyHolder(@NonNull View itemView) {
            super(itemView);

            tevBase64Str = (TextView) itemView.findViewById(R.id.tev_base64_str);
        }
    }
}
