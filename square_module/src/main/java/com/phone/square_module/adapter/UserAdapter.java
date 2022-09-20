package com.phone.square_module.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phone.common_library.bean.User;
import com.phone.common_library.callback.OnItemItemViewClickListener;
import com.phone.common_library.callback.OnItemViewClickListener;
import com.phone.square_module.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "DrawInventoryAdapter";
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_BODY = 1;
    private static final int TYPE_FOOTER = 2;
    private final Context context;
    private List<User> userList = new ArrayList<>();

    public UserAdapter(Context context) {
        this.context = context;
    }

    public void clearData() {
        this.userList.clear();
        notifyDataSetChanged();
    }

    public void addAllData(List<User> userList) {
        this.userList.addAll(userList);
        notifyDataSetChanged();
    }

    public void refreshData() {
        notifyDataSetChanged();
    }

    public void refreshSingleData(int position) {
        notifyItemChanged(position);
    }

    public List<User> getUserList() {
        return userList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(context).inflate(R.layout.item_user_header, null);
            return new HeaderHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            view = LayoutInflater.from(context).inflate(R.layout.item_user_footer, null);
            return new FooterHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_user_body, null);
            return new BodyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BodyHolder) {
            BodyHolder bodyHolder = (BodyHolder) holder;

            bodyHolder.tevUserName.setText(userList.get(position - 1).getUserName());
            bodyHolder.tevUserId.setText(userList.get(position - 1).getUserId());
            bodyHolder.tevDate.setText(userList.get(position - 1).getDate());
            bodyHolder.tevDelete.setOnClickListener(v -> {
                onItemViewClickListener.onItemClickListener(position, v);
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == userList.size() + 2 - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_BODY;
        }
    }

    @Override
    public int getItemCount() {
        return userList.size() + 2;
    }

    private static class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private static class BodyHolder extends RecyclerView.ViewHolder {

        private TextView tevUserName;
        private TextView tevUserId;
        private TextView tevDate;
        private TextView tevDelete;

        public BodyHolder(@NonNull View itemView) {
            super(itemView);

            tevUserName = (TextView) itemView.findViewById(R.id.tev_user_name);
            tevUserId = (TextView) itemView.findViewById(R.id.tev_user_id);
            tevDate = (TextView) itemView.findViewById(R.id.tev_date);
            tevDelete = (TextView) itemView.findViewById(R.id.tev_delete);
        }
    }

    private static class FooterHolder extends RecyclerView.ViewHolder {

        public FooterHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private OnItemViewClickListener onItemViewClickListener;

    public void setOnItemViewClickListener(OnItemViewClickListener onItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener;
    }
}
