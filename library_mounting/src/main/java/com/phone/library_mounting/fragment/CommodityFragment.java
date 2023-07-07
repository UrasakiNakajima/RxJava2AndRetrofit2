package com.phone.library_mounting.fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phone.library_common.base.BaseBindingRxFragment;
import com.phone.library_mounting.R;
import com.phone.library_mounting.databinding.MountingFragmentCommodityBinding;
import com.phone.library_mounting.adapter.CommodityAdapter;
import com.phone.library_mounting.bean.CommodityBean;

import java.util.ArrayList;
import java.util.List;

public class CommodityFragment extends BaseBindingRxFragment<MountingFragmentCommodityBinding> {

    private static final String TAG = CommodityFragment.class.getSimpleName();
    private String commodity;

    private LinearLayoutManager linearLayoutManager;
    private CommodityAdapter commodityAdapter;

    private List<CommodityBean> commodityBeanList = new ArrayList<>();

    public static CommodityFragment get(String commodity) {
        CommodityFragment commodityFragment = new CommodityFragment();
        Bundle bundle = new Bundle();
        bundle.putString("commodity", commodity);
        commodityFragment.setArguments(bundle);
        return commodityFragment;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.mounting_fragment_commodity;
    }

    @Override
    protected void initData() {
        commodity = getArguments().getString("commodity");
        commodityBeanList.add(new CommodityBean(0, commodity + "11", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 5000, 1000));
        commodityBeanList.add(new CommodityBean(0, commodity + "11", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 5000, 1000));
        commodityBeanList.add(new CommodityBean(0, commodity + "12", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 5500, 1000));
        commodityBeanList.add(new CommodityBean(0, commodity + "12", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 5500, 1000));
        commodityBeanList.add(new CommodityBean(0, commodity + "15", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 6300, 1000));
        commodityBeanList.add(new CommodityBean(0, commodity + "15", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 6300, 1000));
        commodityBeanList.add(new CommodityBean(0, commodity + "15", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 6300, 1000));
        commodityBeanList.add(new CommodityBean(0, commodity + "15", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 6300, 1000));
        commodityBeanList.add(new CommodityBean(0, commodity + "15", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 6300, 1000));
        commodityBeanList.add(new CommodityBean(0, commodity + "14", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 5900, 1000));
        commodityBeanList.add(new CommodityBean(0, commodity + "11", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 5900, 1000));
        commodityBeanList.add(new CommodityBean(0, commodity + "11", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 5900, 1000));
        commodityBeanList.add(new CommodityBean(0, commodity + "12", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 5900, 1000));
        commodityBeanList.add(new CommodityBean(0, commodity + "12", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 5900, 1000));
    }

    @Override
    protected void initViews() {
        initAdapter();
    }

    private void initAdapter() {
        linearLayoutManager = new LinearLayoutManager(mRxAppCompatActivity);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mDatabind.rcvCommodity.setLayoutManager(linearLayoutManager);
        mDatabind.rcvCommodity.setItemAnimator(new DefaultItemAnimator());

        commodityAdapter = new CommodityAdapter(mRxAppCompatActivity);
        mDatabind.rcvCommodity.setAdapter(commodityAdapter);
        commodityAdapter.addData(commodityBeanList);
    }

    @Override
    protected void initLoadData() {

    }


}
