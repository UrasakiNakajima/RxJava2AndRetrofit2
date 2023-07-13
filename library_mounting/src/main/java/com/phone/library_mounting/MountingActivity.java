package com.phone.library_mounting;

import android.graphics.Color;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.phone.library_base.callback.OnCommonSuccessCallback;
import com.phone.library_base.manager.LogManager;
import com.phone.library_base.manager.ResourcesManager;
import com.phone.library_base.manager.ThreadPoolManager;
import com.phone.library_common.adapter.TabFragmentStatePagerAdapter;
import com.phone.library_base.common.ConstantData;
import com.phone.library_base.manager.ScreenManager;
import com.phone.library_common.adapter.ViewPager2Adapter;
import com.phone.library_mounting.databinding.MountingActivityMountingBinding;
import com.phone.library_mounting.adapter.MineCommonNavigatorAdapter;
import com.phone.library_mounting.fragment.CommodityFragment;
import com.phone.library_binding.BaseBindingRxAppActivity;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

@Route(path = ConstantData.Route.ROUTE_MOUNTING)
public class MountingActivity extends BaseBindingRxAppActivity<MountingActivityMountingBinding> {

    private static final String TAG = MountingActivity.class.getSimpleName();
    private int imvBannerHeight;
    private int slideMaxHeight;

    @Override
    protected int initLayoutId() {
        return R.layout.mounting_activity_mounting;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        setToolbar2(false, R.color.library_color_transparent, false);
        setMounting();

        mDatabind.imvBack.setColorFilter(ResourcesManager.getColor(R.color.library_color_FFFFFFFF));
        mDatabind.layoutBack.setOnClickListener(v -> {
            finish();
        });
        mDatabind.refreshLayout.setOnRefreshListener(refreshLayout -> {
            initLoadData();
            mDatabind.refreshLayout.finishRefresh(1000);
        });
//        mDatabind.magicIndicator.setBackgroundColor(ResourcesManager.getColor(R.color.library_color_transparent));
        mDatabind.viewPager2.setBackgroundColor(ResourcesManager.getColor(R.color.library_color_transparent));
    }

    private void setMounting() {
        mDatabind.imvBanner.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (mDatabind.imvBanner.getHeight() > 0) {
                imvBannerHeight = mDatabind.imvBanner.getHeight();
                slideMaxHeight = imvBannerHeight
                        - ScreenManager.getDimenPx(R.dimen.base_dp_73)
                        - ScreenManager.getDimenPx(R.dimen.base_dp_1);
            }
        });
        mDatabind.appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            if (verticalOffset < 0) {
                setToolbar2(true, R.color.library_color_transparent, false);
                double slideHeight = Math.abs(verticalOffset);
                if (slideHeight < slideMaxHeight) {
                    int proportion = (int) ((slideHeight / slideMaxHeight) * 255);
                    LogManager.i(TAG, "proportion******" + proportion);
                    int color = Color.argb(proportion, 255, 255, 255);
                    mDatabind.toolbar.setBackgroundColor(color);
                } else {
                    mDatabind.toolbar.setBackgroundColor(ResourcesManager.getColor(R.color.library_color_FFFFFFFF));
                }
                mDatabind.tevTitle.setTextColor(ResourcesManager.getColor(R.color.library_color_99000000));
                mDatabind.imvBack.setColorFilter(ResourcesManager.getColor(R.color.library_color_99000000));
            } else {
                setToolbar2(false, R.color.library_color_transparent, false);
                mDatabind.toolbar.setBackgroundColor(ResourcesManager.getColor(R.color.library_color_transparent));
                mDatabind.tevTitle.setTextColor(ResourcesManager.getColor(R.color.library_color_FFFFFFFF));
                mDatabind.imvBack.setColorFilter(ResourcesManager.getColor(R.color.library_color_FFFFFFFF));
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
            mDatabind.viewPager2.setCurrentItem(position);
        });
        mDatabind.magicIndicator.setNavigator(commonNavigator);
        List<Fragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < titleList.size(); i++) {
            fragmentList.add(CommodityFragment.get(titleList.get(i)));
        }

        // ORIENTATION_HORIZONTAL：水平滑动（默认），ORIENTATION_VERTICAL：竖直滑动
        mDatabind.viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        // 适配
        mDatabind.viewPager2.setAdapter(new ViewPager2Adapter(fragmentList, getSupportFragmentManager(), getLifecycle()));
        mDatabind.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                mDatabind.magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mDatabind.magicIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                mDatabind.magicIndicator.onPageScrollStateChanged(state);
            }
        });
//        ViewPagerHelper.bind(mDatabind.magicIndicator, mDatabind.viewPager2);
    }

    @Override
    protected void initLoadData() {
        showLoading();
        ThreadPoolManager.instance().createScheduledThreadPoolToUIThread2(1000, () -> {
            setData();
            hideLoading();
        });
    }


}