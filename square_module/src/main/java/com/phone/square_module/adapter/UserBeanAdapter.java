package com.phone.square_module.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phone.common_library.bean.UserBean;
import com.phone.common_library.callback.OnItemViewClickListener;
import com.phone.square_module.R;

import java.util.ArrayList;
import java.util.List;

public class UserBeanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = UserBeanAdapter.class.getSimpleName();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_BODY = 1;
    private static final int TYPE_FOOTER = 2;
    private final Context context;
    private List<UserBean> userBeanList = new ArrayList<>();

    public UserBeanAdapter(Context context) {
        this.context = context;
    }

    public void clearData() {
        this.userBeanList.clear();
        notifyDataSetChanged();
    }

    public void addAllData(List<UserBean> userBeanList) {
        this.userBeanList.addAll(userBeanList);
        notifyDataSetChanged();
    }

    public void refreshData() {
        notifyDataSetChanged();
    }

    public void refreshSingleData(int position) {
        notifyItemChanged(position);
    }

    public List<UserBean> getUserBeanList() {
        return userBeanList;
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
        if (holder instanceof HeaderHolder) {
            HeaderHolder headerHolder = (HeaderHolder) holder;

            headerHolder.tevUserName.setText(userBeanList.get(position).getUserName());
            headerHolder.tevUserId.setText(userBeanList.get(position).getUserId());
            headerHolder.tevDate.setText(userBeanList.get(position).getDate());
            headerHolder.tevSalary.setText(String.valueOf(userBeanList.get(position).getSalary()));
            headerHolder.tevAddress.setText(userBeanList.get(position).getAddressBeanList().get(0).getCounty()
                    + context.getResources().getString(R.string.chinese_colon)
                    + userBeanList.get(position).getAddressBeanList().get(0).getCity());
            headerHolder.tevDelete.setOnClickListener(v -> {
                onItemViewClickListener.onItemClickListener(position, v);
            });
        } else if (holder instanceof FooterHolder) {
            FooterHolder footerHolder = (FooterHolder) holder;

            footerHolder.tevUserName.setText(userBeanList.get(position).getUserName());
            footerHolder.tevUserId.setText(userBeanList.get(position).getUserId());
            footerHolder.tevDate.setText(userBeanList.get(position).getDate());
            footerHolder.tevSalary.setText(String.valueOf(userBeanList.get(position).getSalary()));
            footerHolder.tevAddress.setText(userBeanList.get(position).getAddressBeanList().get(0).getCounty()
                    + context.getResources().getString(R.string.chinese_colon)
                    + userBeanList.get(position).getAddressBeanList().get(0).getCity());
            footerHolder.tevDelete.setOnClickListener(v -> {
                onItemViewClickListener.onItemClickListener(position, v);
            });
        } else {
            BodyHolder bodyHolder = (BodyHolder) holder;

            bodyHolder.tevUserName.setText(userBeanList.get(position).getUserName());
            bodyHolder.tevUserId.setText(userBeanList.get(position).getUserId());
            bodyHolder.tevDate.setText(userBeanList.get(position).getDate());
            bodyHolder.tevSalary.setText(String.valueOf(userBeanList.get(position).getSalary()));
            bodyHolder.tevAddress.setText(userBeanList.get(position).getAddressBeanList().get(0).getCounty()
                    + context.getResources().getString(R.string.chinese_colon)
                    + userBeanList.get(position).getAddressBeanList().get(0).getCity());
            bodyHolder.tevDelete.setOnClickListener(v -> {
                onItemViewClickListener.onItemClickListener(position, v);
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == userBeanList.size() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_BODY;
        }
    }

    @Override
    public int getItemCount() {
        return userBeanList.size();
    }

    private static class HeaderHolder extends RecyclerView.ViewHolder {

        private TextView tevUserName;
        private TextView tevUserId;
        private TextView tevDate;
        private TextView tevSalary;
        private TextView tevAddress;
        private TextView tevDelete;

        public HeaderHolder(@NonNull View itemView) {
            super(itemView);

            tevUserName = (TextView) itemView.findViewById(R.id.tev_user_name);
            tevUserId = (TextView) itemView.findViewById(R.id.tev_user_id);
            tevDate = (TextView) itemView.findViewById(R.id.tev_date);
            tevSalary = (TextView) itemView.findViewById(R.id.tev_salary);
            tevAddress = (TextView) itemView.findViewById(R.id.tev_address);
            tevDelete = (TextView) itemView.findViewById(R.id.tev_delete);
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

        private TextView tevUserName;
        private TextView tevUserId;
        private TextView tevDate;
        private TextView tevSalary;
        private TextView tevAddress;
        private TextView tevDelete;

        public FooterHolder(@NonNull View itemView) {
            super(itemView);

            tevUserName = (TextView) itemView.findViewById(R.id.tev_user_name);
            tevUserId = (TextView) itemView.findViewById(R.id.tev_user_id);
            tevDate = (TextView) itemView.findViewById(R.id.tev_date);
            tevSalary = (TextView) itemView.findViewById(R.id.tev_salary);
            tevAddress = (TextView) itemView.findViewById(R.id.tev_address);
            tevDelete = (TextView) itemView.findViewById(R.id.tev_delete);
        }
    }

    private OnItemViewClickListener onItemViewClickListener;

    public void setOnItemViewClickListener(OnItemViewClickListener onItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener;
    }
}
