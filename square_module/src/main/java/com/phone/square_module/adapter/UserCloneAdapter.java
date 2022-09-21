package com.phone.square_module.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phone.common_library.bean.UserCloneBean;
import com.phone.common_library.callback.OnItemViewClickListener;
import com.phone.square_module.R;

import java.util.ArrayList;
import java.util.List;

public class UserCloneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = UserCloneAdapter.class.getSimpleName();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_BODY = 1;
    private static final int TYPE_FOOTER = 2;
    private final Context context;
    private List<UserCloneBean> userCloneBeanList = new ArrayList<>();

    public UserCloneAdapter(Context context) {
        this.context = context;
    }

    public void clearData() {
        this.userCloneBeanList.clear();
        notifyDataSetChanged();
    }

    public void addAllData(List<UserCloneBean> userCloneBeanList) {
        this.userCloneBeanList.addAll(userCloneBeanList);
        notifyDataSetChanged();
    }

    public void refreshData() {
        notifyDataSetChanged();
    }

    public void refreshSingleData(int position) {
        notifyItemChanged(position);
    }

    public List<UserCloneBean> getUserCloneList() {
        return userCloneBeanList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(context).inflate(R.layout.item_user_header, parent, false);
            return new HeaderHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            view = LayoutInflater.from(context).inflate(R.layout.item_user_footer, parent, false);
            return new FooterHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_user_body, parent, false);
            return new BodyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BodyHolder) {
            BodyHolder bodyHolder = (BodyHolder) holder;

            bodyHolder.tevUserName.setText(userCloneBeanList.get(position - 1).getUserName());
            bodyHolder.tevUserId.setText(userCloneBeanList.get(position - 1).getUserId());
            bodyHolder.tevDate.setText(userCloneBeanList.get(position - 1).getDate());
            bodyHolder.tevSalary.setText(userCloneBeanList.get(position - 1).getSalaryBigDecimal().toPlainString());
            bodyHolder.tevAddress.setText(userCloneBeanList.get(position - 1).getAddressBeanList().get(0).getCounty()
                    + context.getResources().getString(R.string.chinese_colon)
                    + userCloneBeanList.get(position - 1).getAddressBeanList().get(0).getCity());
            bodyHolder.tevDelete.setOnClickListener(v -> {
                onItemViewClickListener.onItemClickListener(position, v);
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == userCloneBeanList.size() + 2 - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_BODY;
        }
    }

    @Override
    public int getItemCount() {
        return userCloneBeanList.size() + 2;
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
        private TextView tevSalary;
        private TextView tevAddress;
        private TextView tevDelete;

        public BodyHolder(@NonNull View itemView) {
            super(itemView);

            tevUserName = (TextView) itemView.findViewById(R.id.tev_user_name);
            tevUserId = (TextView) itemView.findViewById(R.id.tev_user_id);
            tevDate = (TextView) itemView.findViewById(R.id.tev_date);
            tevSalary = (TextView) itemView.findViewById(R.id.tev_salary);
            tevAddress = (TextView) itemView.findViewById(R.id.tev_address);
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
