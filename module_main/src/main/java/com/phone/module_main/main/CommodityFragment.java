package com.phone.module_main.main;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phone.library_common.base.BaseBindingRxFragment;
import com.phone.module_main.R;
import com.phone.module_main.databinding.FragmentCommodityBinding;

import java.util.ArrayList;
import java.util.List;

public class CommodityFragment extends BaseBindingRxFragment<FragmentCommodityBinding> {

    private static final String TAG = CommodityFragment.class.getSimpleName();
    private LinearLayoutManager linearLayoutManager;
    private CommodityAdapter commodityAdapter;

    private List<CommodityBean> commodityBeanList = new ArrayList<>();

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_commodity;
    }

    @Override
    protected void initData() {
        commodityBeanList.add(new CommodityBean(0, "苹果11", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 5000, 1000));
        commodityBeanList.add(new CommodityBean(0, "苹果11", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 5000, 1000));
        commodityBeanList.add(new CommodityBean(0, "苹果12", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 5500, 1000));
        commodityBeanList.add(new CommodityBean(0, "苹果12", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 5500, 1000));
        commodityBeanList.add(new CommodityBean(0, "苹果15", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 6300, 1000));
        commodityBeanList.add(new CommodityBean(0, "苹果15", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 6300, 1000));
        commodityBeanList.add(new CommodityBean(0, "苹果15", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 6300, 1000));
        commodityBeanList.add(new CommodityBean(0, "苹果15", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 6300, 1000));
        commodityBeanList.add(new CommodityBean(0, "苹果15", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 6300, 1000));
        commodityBeanList.add(new CommodityBean(0, "苹果14", "精心设计，"
                + "处处带来改变。循环利用，" +
                "打开新思路。力保个人信息安全，" +
                "这很 iPhone", 5900, 1000));
        commodityBeanList.add(new CommodityBean(0, "苹果14", "精心设计，"
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
