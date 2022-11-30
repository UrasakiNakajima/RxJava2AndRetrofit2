package com.phone.module_square.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phone.library_common.bean.UserBean;
import com.phone.library_common.callback.OnItemViewClickListener;
import com.phone.library_common.manager.ResourcesManager;
import com.phone.module_square.R;

import java.util.ArrayList;
import java.util.List;

public class UserBeanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = UserBeanAdapter.class.getSimpleName();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_BODY = 1;
    private static final int TYPE_FOOTER = 2;
    private final Context context;
    private final List<UserBean> userBeanList = new ArrayList<>();

    public UserBeanAdapter(Context context) {
        this.context = context;
    }

    public void clearData() {
        notifyItemRangeRemoved(0, this.userBeanList.size());
        notifyItemRangeChanged(0, this.userBeanList.size());
        this.userBeanList.clear();
    }

    public void addData(List<UserBean> userBeanList) {
        notifyItemRangeInserted(this.userBeanList.size(), userBeanList.size());
        this.userBeanList.addAll(userBeanList);
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

            if (!TextUtils.isEmpty(userBeanList.get(position).getUserId())) {
                headerHolder.tevUserId.setText(ResourcesManager.INSTANCE.getString(R.string.user_id_b) + userBeanList.get(position).getUserId());
            } else {
                headerHolder.tevUserId.setText(ResourcesManager.INSTANCE.getString(R.string.user_id_b));
            }
            if (!TextUtils.isEmpty(userBeanList.get(position).getBirthday())) {
                headerHolder.tevBirthday.setText(ResourcesManager.INSTANCE.getString(R.string.birthday_b) + userBeanList.get(position).getBirthday());
            } else {
                headerHolder.tevBirthday.setText(ResourcesManager.INSTANCE.getString(R.string.birthday_b));
            }
            if (userBeanList.get(position).getSalary() != null) {
                headerHolder.tevSalary.setText(ResourcesManager.INSTANCE.getString(R.string.salary_b) + userBeanList.get(position).getSalary());
            } else {
                headerHolder.tevSalary.setText(ResourcesManager.INSTANCE.getString(R.string.salary_b));
            }
            if (userBeanList.get(position).getAddressBeanList() != null && userBeanList.get(position).getAddressBeanList().size() > 0) {
                headerHolder.tevAddress.setText(ResourcesManager.INSTANCE.getString(R.string.address_b)
                        + userBeanList.get(position).getAddressBeanList().get(0).getCity());
            } else {
                headerHolder.tevAddress.setText(ResourcesManager.INSTANCE.getString(R.string.address_b));
            }
            headerHolder.tevUpdate.setOnClickListener(v -> {
                onItemViewClickListener.onItemClickListener(position, v);
            });
            headerHolder.tevDelete.setOnClickListener(v -> {
                onItemViewClickListener.onItemClickListener(position, v);
            });
        } else if (holder instanceof FooterHolder) {
            FooterHolder footerHolder = (FooterHolder) holder;

            if (!TextUtils.isEmpty(userBeanList.get(position).getUserId())) {
                footerHolder.tevUserId.setText(ResourcesManager.INSTANCE.getString(R.string.user_id_b) + userBeanList.get(position).getUserId());
            } else {
                footerHolder.tevUserId.setText(ResourcesManager.INSTANCE.getString(R.string.user_id_b));
            }
            if (!TextUtils.isEmpty(userBeanList.get(position).getBirthday())) {
                footerHolder.tevBirthday.setText(ResourcesManager.INSTANCE.getString(R.string.birthday_b) + userBeanList.get(position).getBirthday());
            } else {
                footerHolder.tevBirthday.setText(ResourcesManager.INSTANCE.getString(R.string.birthday_b));
            }
            if (userBeanList.get(position).getSalary() != null) {
                footerHolder.tevSalary.setText(ResourcesManager.INSTANCE.getString(R.string.salary_b) + userBeanList.get(position).getSalary());
            } else {
                footerHolder.tevSalary.setText(ResourcesManager.INSTANCE.getString(R.string.salary_b));
            }
            if (userBeanList.get(position).getAddressBeanList() != null && userBeanList.get(position).getAddressBeanList().size() > 0) {
                footerHolder.tevAddress.setText(ResourcesManager.INSTANCE.getString(R.string.address_b)
                        + userBeanList.get(position).getAddressBeanList().get(0).getCity());
            } else {
                footerHolder.tevAddress.setText(ResourcesManager.INSTANCE.getString(R.string.address_b));
            }
            footerHolder.tevUpdate.setOnClickListener(v -> {
                onItemViewClickListener.onItemClickListener(position, v);
            });
            footerHolder.tevDelete.setOnClickListener(v -> {
                onItemViewClickListener.onItemClickListener(position, v);
            });
        } else {
            BodyHolder bodyHolder = (BodyHolder) holder;

            if (!TextUtils.isEmpty(userBeanList.get(position).getUserId())) {
                bodyHolder.tevUserId.setText(ResourcesManager.INSTANCE.getString(R.string.user_id_b) + userBeanList.get(position).getUserId());
            } else {
                bodyHolder.tevUserId.setText(ResourcesManager.INSTANCE.getString(R.string.user_id_b));
            }
            if (!TextUtils.isEmpty(userBeanList.get(position).getBirthday())) {
                bodyHolder.tevBirthday.setText(ResourcesManager.INSTANCE.getString(R.string.birthday_b) + userBeanList.get(position).getBirthday());
            } else {
                bodyHolder.tevBirthday.setText(ResourcesManager.INSTANCE.getString(R.string.birthday_b));
            }
            if (userBeanList.get(position).getSalary() != null) {
                bodyHolder.tevSalary.setText(ResourcesManager.INSTANCE.getString(R.string.salary_b) + userBeanList.get(position).getSalary());
            } else {
                bodyHolder.tevSalary.setText(ResourcesManager.INSTANCE.getString(R.string.salary_b));
            }
            if (userBeanList.get(position).getAddressBeanList() != null && userBeanList.get(position).getAddressBeanList().size() > 0) {
                bodyHolder.tevAddress.setText(ResourcesManager.INSTANCE.getString(R.string.address_b)
                        + userBeanList.get(position).getAddressBeanList().get(0).getCity());
            } else {
                bodyHolder.tevAddress.setText(ResourcesManager.INSTANCE.getString(R.string.address_b));
            }
            bodyHolder.tevUpdate.setOnClickListener(v -> {
                onItemViewClickListener.onItemClickListener(position, v);
            });
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

        private TextView tevUserId;
        private TextView tevBirthday;
        private TextView tevUpdate;
        private TextView tevSalary;
        private TextView tevAddress;
        private TextView tevDelete;

        public HeaderHolder(@NonNull View itemView) {
            super(itemView);

            tevUserId = (TextView) itemView.findViewById(R.id.tev_user_id);
            tevBirthday = (TextView) itemView.findViewById(R.id.tev_birthday);
            tevUpdate = (TextView) itemView.findViewById(R.id.tev_update);
            tevSalary = (TextView) itemView.findViewById(R.id.tev_salary);
            tevAddress = (TextView) itemView.findViewById(R.id.tev_address);
            tevDelete = (TextView) itemView.findViewById(R.id.tev_delete);
        }
    }

    private static class BodyHolder extends RecyclerView.ViewHolder {

        private TextView tevUserId;
        private TextView tevBirthday;
        private TextView tevUpdate;
        private TextView tevSalary;
        private TextView tevAddress;
        private TextView tevDelete;

        public BodyHolder(@NonNull View itemView) {
            super(itemView);

            tevUserId = (TextView) itemView.findViewById(R.id.tev_user_id);
            tevBirthday = (TextView) itemView.findViewById(R.id.tev_birthday);
            tevUpdate = (TextView) itemView.findViewById(R.id.tev_update);
            tevSalary = (TextView) itemView.findViewById(R.id.tev_salary);
            tevAddress = (TextView) itemView.findViewById(R.id.tev_address);
            tevDelete = (TextView) itemView.findViewById(R.id.tev_delete);
        }
    }

    private static class FooterHolder extends RecyclerView.ViewHolder {

        private TextView tevUserId;
        private TextView tevBirthday;
        private TextView tevUpdate;
        private TextView tevSalary;
        private TextView tevAddress;
        private TextView tevDelete;

        public FooterHolder(@NonNull View itemView) {
            super(itemView);

            tevUserId = (TextView) itemView.findViewById(R.id.tev_user_id);
            tevBirthday = (TextView) itemView.findViewById(R.id.tev_birthday);
            tevUpdate = (TextView) itemView.findViewById(R.id.tev_update);
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
