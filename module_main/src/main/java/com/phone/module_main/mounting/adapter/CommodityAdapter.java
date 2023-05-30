package com.phone.module_main.mounting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phone.module_main.R;
import com.phone.module_main.mounting.bean.CommodityBean;

import java.util.ArrayList;
import java.util.List;

public class CommodityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = CommodityAdapter.class.getSimpleName();
    private Context context;
    private List<CommodityBean> commodityBeanList = new ArrayList<>();

    public CommodityAdapter(Context context) {
        this.context = context;
    }

    public void clearData() {
        notifyItemRangeRemoved(0, commodityBeanList.size());
        notifyItemRangeChanged(0, commodityBeanList.size());
        commodityBeanList.clear();
    }

    public void addData(List<CommodityBean> commodityBeanList) {
        notifyItemRangeInserted(this.commodityBeanList.size(), commodityBeanList.size());
        this.commodityBeanList.addAll(commodityBeanList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_item_commodity, parent, false);
        return new BodyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BodyHolder) {
            BodyHolder bodyHolder = (BodyHolder) holder;
            bodyHolder.tevName.setText(commodityBeanList.get(position).getName());
            bodyHolder.tevDescribe.setText(commodityBeanList.get(position).getDescribe());
            bodyHolder.tevPrice.setText("价格：" + commodityBeanList.get(position).getPrice());
            bodyHolder.tevSalesVolume.setText("销量：" + commodityBeanList.get(position).getSalesVolume());
        }
    }

    @Override
    public int getItemCount() {
        return commodityBeanList.size();
    }

    private static class BodyHolder extends RecyclerView.ViewHolder {
        private TextView tevName;
        private TextView tevPrice;
        private TextView tevSalesVolume;
        private TextView tevDescribe;

        public BodyHolder(@NonNull View itemView) {
            super(itemView);

            tevName = (TextView) itemView.findViewById(R.id.tev_name);
            tevPrice = (TextView) itemView.findViewById(R.id.tev_price);
            tevSalesVolume = (TextView) itemView.findViewById(R.id.tev_salesVolume);
            tevDescribe = (TextView) itemView.findViewById(R.id.tev_describe);
        }
    }


}
