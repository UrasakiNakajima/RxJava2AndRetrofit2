package com.phone.module_main.mounting;

import android.graphics.Color;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.phone.library_common.adapter.TabFragmentStatePagerAdapter;
import com.phone.library_common.base.BaseBindingRxAppActivity;
import com.phone.library_common.common.ConstantData;
import com.phone.library_common.manager.LogManager;
import com.phone.library_common.manager.ResourcesManager;
import com.phone.library_common.manager.ScreenManager;
import com.phone.module_main.R;
import com.phone.module_main.databinding.MainActivityMountingBinding;
import com.phone.module_main.mounting.adapter.MineCommonNavigatorAdapter;
import com.phone.module_main.mounting.fragment.CommodityFragment;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

@Route(path = ConstantData.Route.ROUTE_MOUNTING)
public class MountingActivity extends BaseBindingRxAppActivity<MainActivityMountingBinding> {

    private static final String TAG = MountingActivity.class.getSimpleName();
    private int imvBannerHeight;
    private int slideMaxHeight;

    @Override
    protected int initLayoutId() {
        return R.layout.main_activity_mounting;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        setToolbar2(false, R.color.color_transparent, false);
        setMounting();

        mDatabind.imvBack.setColorFilter(ResourcesManager.getColor(R.color.color_FFFFFFFF));
        mDatabind.layoutBack.setOnClickListener(v -> {
            finish();
        });
        mDatabind.refreshLayout.setOnRefreshListener(refreshLayout -> {
            setData();
            mDatabind.refreshLayout.finishRefresh(1000);
        });
        setData();
    }

    private void setMounting() {
        mDatabind.imvBanner.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (mDatabind.imvBanner.getHeight() > 0) {
                imvBannerHeight = mDatabind.imvBanner.getHeight();
                slideMaxHeight = imvBannerHeight
                        - ScreenManager.getDimenPx(R.dimen.dp_73)
                        - ScreenManager.getDimenPx(R.dimen.dp_1);
            }
        });
        mDatabind.appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            if (verticalOffset < 0) {
                setToolbar2(true, R.color.color_transparent, false);
                double slideHeight = Math.abs(verticalOffset);
                if (slideHeight < slideMaxHeight) {
                    int proportion = (int) ((slideHeight / slideMaxHeight) * 255);
                    LogManager.i(TAG, "proportion******" + proportion);
                    int color = Color.argb(proportion, 255, 255, 255);
                    mDatabind.toolbar.setBackgroundColor(color);
                } else {
                    mDatabind.toolbar.setBackgroundColor(ResourcesManager.getColor(R.color.color_FFFFFFFF));
                }
                mDatabind.tevTitle.setTextColor(ResourcesManager.getColor(R.color.color_99000000));
                mDatabind.imvBack.setColorFilter(ResourcesManager.getColor(R.color.color_99000000));
            } else {
                setToolbar2(false, R.color.color_transparent, false);
                mDatabind.toolbar.setBackgroundColor(ResourcesManager.getColor(R.color.color_transparent));
                mDatabind.tevTitle.setTextColor(ResourcesManager.getColor(R.color.color_FFFFFFFF));
                mDatabind.imvBack.setColorFilter(ResourcesManager.getColor(R.color.color_FFFFFFFF));
            }
        });
    }

    private void setData() {
        List<String> titleList = new ArrayList<>();
        titleList.add("苹果手机");
        titleList.add("口红");
        titleList.add("包包");
        //创建indicator适配器
        MineCommonNavigatorAdapter mineCommonNavigatorAdapter = new MineCommonNavigatorAdapter(titleList);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);//自我调节位置，实现自我平分
        commonNavigator.setAdapter(mineCommonNavigatorAdapter);
        mineCommonNavigatorAdapter.setOnIndicatorTapClickListener(position -> {
            mDatabind.viewPager.setCurrentItem(position);
        });
        mDatabind.magicIndicator.setNavigator(commonNavigator);
        List<Fragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < titleList.size(); i++) {
            fragmentList.add(CommodityFragment.get(titleList.get(i)));
        }
        TabFragmentStatePagerAdapter tabFragmentStatePagerAdapter =
                new TabFragmentStatePagerAdapter(getSupportFragmentManager(), fragmentList);
        mDatabind.viewPager.setAdapter(tabFragmentStatePagerAdapter);
        ViewPagerHelper.bind(mDatabind.magicIndicator, mDatabind.viewPager);
    }

    @Override
    protected void initLoadData() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


}