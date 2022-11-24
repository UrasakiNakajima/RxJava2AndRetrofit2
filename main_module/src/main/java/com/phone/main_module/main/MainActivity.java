package com.phone.main_module.main;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.phone.common_library.adapter.TabFragmentStatePagerAdapter;
import com.phone.common_library.base.BaseMvpRxAppActivity;
import com.phone.common_library.base.IBaseView;
import com.phone.common_library.custom_view.LazyViewPager;
import com.phone.common_library.custom_view.MineLazyViewPager;
import com.phone.main_module.BuildConfig;
import com.phone.main_module.R;
import com.phone.main_module.main.presenter.MainPresenterImpl;
import com.phone.main_module.main.view.IMainView;

import java.util.ArrayList;
import java.util.List;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce :
 */
public class MainActivity extends BaseMvpRxAppActivity<IBaseView, MainPresenterImpl>
        implements IMainView {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView tevPleaseAddComponents;
    private LinearLayout layoutMain;
    private MineLazyViewPager mineViewPager;
    private LinearLayout layoutBottom;
    private TextView tevFirstPage;
    private TextView tevProject;
    private TextView tevSquare;
    private TextView tevResourceCenter;
    private TextView tevMine;

    private final List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        if (!BuildConfig.IS_MODULE) {
            //Jump with parameters
            Fragment firstPageFragment = (Fragment) ARouter.getInstance().build("/first_page_module/first_page")
                    //                .withString("max_behot_time", (System.currentTimeMillis() / 1000) + "")
                    .navigation();
            fragmentList.add(firstPageFragment);
            //Jump with parameters
            Fragment projectFragment = (Fragment) ARouter.getInstance().build("/project_module/project")
                    //                .withString("max_behot_time", (System.currentTimeMillis() / 1000) + "")
                    .navigation();
            fragmentList.add(projectFragment);
            //Jump with parameters
            Fragment squareFragment = (Fragment) ARouter.getInstance().build("/square_module/square")
                    //                .withString("max_behot_time", (System.currentTimeMillis() / 1000) + "")
                    .navigation();
            fragmentList.add(squareFragment);
            //Jump with parameters
            Fragment resourceFragment = (Fragment) ARouter.getInstance().build("/resource_module/resource")
                    //                .withString("max_behot_time", (System.currentTimeMillis() / 1000) + "")
                    .navigation();
            fragmentList.add(resourceFragment);
            //Jump with parameters
            Fragment mineFragment = (Fragment) ARouter.getInstance().build("/mine_module/mine")
                    //                .withString("max_behot_time", (System.currentTimeMillis() / 1000) + "")
                    .navigation();
            fragmentList.add(mineFragment);
        }
    }

    @Override
    protected void initViews() {
        tevPleaseAddComponents = (TextView) findViewById(R.id.tev_please_add_components);
        layoutMain = (LinearLayout) findViewById(R.id.layout_main);
        mineViewPager = (MineLazyViewPager) findViewById(R.id.mine_view_pager);
        layoutBottom = (LinearLayout) findViewById(R.id.layout_bottom);
        tevFirstPage = (TextView) findViewById(R.id.tev_first_page);
        tevProject = (TextView) findViewById(R.id.tev_project);
        tevSquare = (TextView) findViewById(R.id.tev_square);
        tevResourceCenter = (TextView) findViewById(R.id.tev_resource_center);
        tevMine = (TextView) findViewById(R.id.tev_mine);


        TabFragmentStatePagerAdapter fragmentStatePagerAdapter = new TabFragmentStatePagerAdapter(getSupportFragmentManager(), fragmentList);
        mineViewPager.setAdapter(fragmentStatePagerAdapter);
        mineViewPager.setOnPageChangeListener(new LazyViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetTabData(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tevFirstPage.setOnClickListener(v -> {
            resetTabData(0);
        });
        tevProject.setOnClickListener(v -> {
            resetTabData(1);
        });
        tevSquare.setOnClickListener(v -> {
            resetTabData(2);
        });
        tevResourceCenter.setOnClickListener(v -> {
            resetTabData(3);
        });
        tevMine.setOnClickListener(v -> {
            resetTabData(4);
        });
    }

    private void resetTabData(int position) {
        mineViewPager.setCurrentItem(position);

        tevFirstPage.setTextColor(getResources().getColor(R.color.color_FF999999));
        tevProject.setTextColor(getResources().getColor(R.color.color_FF999999));
        tevSquare.setTextColor(getResources().getColor(R.color.color_FF999999));
        tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FF999999));
        tevMine.setTextColor(getResources().getColor(R.color.color_FF999999));

        setToolbar(false, R.color.color_FF198CFF);
        switch (position) {
            case 0:
                tevFirstPage.setTextColor(getResources().getColor(R.color.color_FFE066FF));
                layoutBottom.setBackgroundColor(getResources().getColor(R.color.black));
                break;
            case 1:
                tevProject.setTextColor(getResources().getColor(R.color.color_FFE066FF));
                layoutBottom.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case 2:
                tevSquare.setTextColor(getResources().getColor(R.color.color_FFE066FF));
                layoutBottom.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case 3:
                tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FFE066FF));
                layoutBottom.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case 4:
                tevMine.setTextColor(getResources().getColor(R.color.color_FFE066FF));
                layoutBottom.setBackgroundColor(getResources().getColor(R.color.black));
                break;
            default:
                tevFirstPage.setTextColor(getResources().getColor(R.color.color_FFE066FF));
                layoutBottom.setBackgroundColor(getResources().getColor(R.color.black));
                break;
        }
    }

    @Override
    protected void initLoadData() {
        if (!BuildConfig.IS_MODULE) {
            tevPleaseAddComponents.setVisibility(View.GONE);
            layoutMain.setVisibility(View.VISIBLE);
            mineViewPager.setCurrentItem(0);
            tevFirstPage.setTextColor(getResources().getColor(R.color.color_FFE066FF));
            tevProject.setTextColor(getResources().getColor(R.color.color_FF999999));
            tevSquare.setTextColor(getResources().getColor(R.color.color_FF999999));
            tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FF999999));
            tevMine.setTextColor(getResources().getColor(R.color.color_FF999999));

            setToolbar(false, R.color.color_FF198CFF);
        } else {
            tevPleaseAddComponents.setVisibility(View.VISIBLE);
            layoutMain.setVisibility(View.GONE);
        }

    }

    @NonNull
    @Override
    protected MainPresenterImpl attachPresenter() {
        return new MainPresenterImpl(this);
    }

    @Override
    public void showLoading() {
        if (getLoadView() != null && !getLoadView().isShown()) {
            getLoadView().setVisibility(View.VISIBLE);
            getLoadView().start();
        }
    }

    @Override
    public void hideLoading() {
        if (getLoadView() != null && getLoadView().isShown()) {
            getLoadView().stop();
            getLoadView().setVisibility(View.GONE);
        }
    }

    @Override
    public void mainDataSuccess(String success) {
        showToast(success, true);
    }

    @Override
    public void mainDataError(String error) {
        showToast(error, true);
    }

}
