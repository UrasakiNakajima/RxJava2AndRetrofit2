package com.phone.module_main.main;

import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.gyf.immersionbar.ImmersionBar;
import com.phone.library_common.adapter.TabFragmentStatePagerAdapter;
import com.phone.library_common.base.BaseBindingRxAppActivity;
import com.phone.library_common.manager.ResourcesManager;
import com.phone.library_common.manager.ScreenManager;
import com.phone.library_common.manager.ToolbarManager;
import com.phone.module_main.R;
import com.phone.module_main.databinding.ActivityMountingBinding;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/module_main/mounting")
public class MountingActivity extends BaseBindingRxAppActivity<ActivityMountingBinding> {

    private static final String TAG = MountingActivity.class.getSimpleName();
    private int imvBannerHeight;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_mounting;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        setToolbar(true, R.color.color_transparent, false);
        setMounting();

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
            }
        });
        mDatabind.appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            if (verticalOffset != 0) {
                double slideHeight = Math.abs(verticalOffset);
                if (slideHeight < (imvBannerHeight
                        - ScreenManager.getDimenPx(R.dimen.dp_73)
                        - ScreenManager.getDimenPx(R.dimen.dp_1))) {
                    mDatabind.toolbar.setBackgroundColor(ResourcesManager.getColor(R.color.color_transparent));
                    ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                            .statusBarDarkFont(true)
                            .statusBarColor(R.color.color_transparent) //状态栏颜色，不写默认透明色
                            //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                            .keyboardEnable(true)
                            .init();
                } else {
                    mDatabind.toolbar.setBackgroundColor(ResourcesManager.getColor(R.color.color_FFFFFFFF));
                    ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                            .statusBarDarkFont(true)
                            .statusBarColor(R.color.color_FFFFFFFF) //状态栏颜色，不写默认透明色
                            //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                            .keyboardEnable(true)
                            .init();
                }
            } else {
                mDatabind.toolbar.setBackgroundColor(ResourcesManager.getColor(R.color.color_transparent));
                ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                        .statusBarDarkFont(true)
                        .statusBarColor(R.color.color_transparent) //状态栏颜色，不写默认透明色
                        //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                        .keyboardEnable(true)
                        .init();
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
        fragmentList.add(new CommodityFragment());
        fragmentList.add(new CommodityFragment());
        fragmentList.add(new CommodityFragment());
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

    protected void setToolbar(boolean isDarkFont, int statusBarColor, boolean isResizeChildOfContent) {
        if (isDarkFont) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//        window.setNavigationBarColor(Color.TRANSPARENT);

            ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                    .statusBarDarkFont(isDarkFont)
                    .statusBarColor(statusBarColor) //状态栏颜色，不写默认透明色
                    //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                    .keyboardEnable(true)
                    .init();
        } else {
            ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                    .statusBarDarkFont(isDarkFont)
                    .statusBarColor(statusBarColor) //状态栏颜色，不写默认透明色
                    //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                    .keyboardEnable(true)
                    .init();

            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//        window.setNavigationBarColor(Color.TRANSPARENT);
        }
        if (isResizeChildOfContent) {
            ToolbarManager.assistActivity(findViewById(android.R.id.content));
        }
    }


}