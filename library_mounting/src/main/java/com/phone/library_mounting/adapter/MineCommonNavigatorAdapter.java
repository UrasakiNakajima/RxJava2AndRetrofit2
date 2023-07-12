package com.phone.library_mounting.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.phone.library_base.manager.ResourcesManager;
import com.phone.library_mounting.R;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

public class MineCommonNavigatorAdapter extends CommonNavigatorAdapter {

    private static final String TAG = MineCommonNavigatorAdapter.class.getSimpleName();
    private List<String> titleList = new ArrayList<>();//存放indicator的标题
    private OnIndicatorTapClickListener mListener = null;

    public MineCommonNavigatorAdapter(List<String> titleList) {
        this.titleList.clear();
        this.titleList.addAll(titleList);
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, int index) {
        // ColorTransitionPagerTitleView 设置两种颜色过渡的指示器
        ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
        colorTransitionPagerTitleView.setNormalColor(ResourcesManager.getColor(R.color.library_color_333333));//普通状态
        colorTransitionPagerTitleView.setSelectedColor(ResourcesManager.getColor(R.color.library_color_FFFFFF));//选中时状态
        colorTransitionPagerTitleView.setTextSize(18);
        colorTransitionPagerTitleView.setText(titleList.get(index));
        colorTransitionPagerTitleView.setTextColor(ResourcesManager.getColor(R.color.library_color_FF333333));
        colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mViewPager.setCurrentItem(index);
                if (mListener != null) {
                    mListener.onTabClick(index);
                }
            }
        });
        return colorTransitionPagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
        linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        linePagerIndicator.setColors(Color.WHITE);
        return linePagerIndicator;
    }

    //为了与ViewPage进行连动，需要设置一个接口，通知使用者实现被点击事件的处理
    public void setOnIndicatorTapClickListener(OnIndicatorTapClickListener listener) {
        this.mListener = listener;
    }

    public interface OnIndicatorTapClickListener {
        void onTabClick(int position);
    }


}
