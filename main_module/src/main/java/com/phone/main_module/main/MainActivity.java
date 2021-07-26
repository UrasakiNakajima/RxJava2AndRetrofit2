package com.phone.main_module.main;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.immersionbar.ImmersionBar;
import com.phone.common_library.adapter.TabFragmentPagerAdapter;
import com.phone.common_library.base.BaseMvpAppActivity;
import com.phone.common_library.base.IBaseView;
import com.phone.common_library.custom_view.LazyViewPager;
import com.phone.common_library.custom_view.MineLazyViewPager;
import com.phone.main_module.R;
import com.phone.main_module.R2;
import com.phone.main_module.main.presenter.MainPresenterImpl;
import com.phone.main_module.main.view.IMainView;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import butterknife.BindView;

public class MainActivity extends BaseMvpAppActivity<IBaseView, MainPresenterImpl>
	implements IMainView {
	
	private static final String TAG = "MainActivity";
	@BindView(R2.id.mine_view_pager)
	MineLazyViewPager mineViewPager;
	@BindView(R2.id.layout_bottom)
	LinearLayout      layoutBottom;
	@BindView(R2.id.tev_first_page)
	TextView          tevFirstPage;
	@BindView(R2.id.tev_project)
	TextView          tevProject;
	@BindView(R2.id.tev_square)
	TextView          tevSquare;
	@BindView(R2.id.tev_resource_center)
	TextView          tevResourceCenter;
	@BindView(R2.id.tev_mine)
	TextView          tevMine;
	
	private List<Fragment>          fragmentList = new ArrayList<>();
	private TabFragmentPagerAdapter fragmentPagerAdapter;
	
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
	
	@Override
	protected void initViews() {
		addContentView(loadView, layoutParams);
		
		fragmentPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
		mineViewPager.setAdapter(fragmentPagerAdapter);
		mineViewPager.setOnPageChangeListener(new LazyViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			
			}
			
			@Override
			public void onPageSelected(int position) {
				switch (position) {
					case 0:
						mineViewPager.setCurrentItem(0);
						tevFirstPage.setTextColor(getResources().getColor(R.color.color_FFE066FF));
						tevProject.setTextColor(getResources().getColor(R.color.color_FF999999));
						tevSquare.setTextColor(getResources().getColor(R.color.color_FF999999));
						tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FF999999));
						tevMine.setTextColor(getResources().getColor(R.color.color_FF999999));
						
						ImmersionBar.with(MainActivity.this)
							.keyboardEnable(false)
							.statusBarDarkFont(false)
							.statusBarColor(R.color.color_FFE066FF)
							.navigationBarColor(R.color.color_FFE066FF).init();
						layoutBottom.setBackgroundColor(getResources().getColor(R.color.black));
						break;
					case 1:
						mineViewPager.setCurrentItem(1);
						tevFirstPage.setTextColor(getResources().getColor(R.color.color_FF999999));
						tevProject.setTextColor(getResources().getColor(R.color.color_FFE066FF));
						tevSquare.setTextColor(getResources().getColor(R.color.color_FF999999));
						tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FF999999));
						tevMine.setTextColor(getResources().getColor(R.color.color_FF999999));
						
						ImmersionBar.with(MainActivity.this)
							.keyboardEnable(false)
							.statusBarDarkFont(false)
							.statusBarColor(R.color.color_FF198CFF)
							.navigationBarColor(R.color.color_FF198CFF).init();
						layoutBottom.setBackgroundColor(getResources().getColor(R.color.white));
						break;
					case 2:
						mineViewPager.setCurrentItem(2);
						tevFirstPage.setTextColor(getResources().getColor(R.color.color_FF999999));
						tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FFE066FF));
						tevMine.setTextColor(getResources().getColor(R.color.color_FF999999));
						
						ImmersionBar.with(MainActivity.this)
							.keyboardEnable(false)
							.statusBarDarkFont(false)
							.statusBarColor(R.color.color_FF198CFF)
							.navigationBarColor(R.color.color_FF198CFF).init();
						layoutBottom.setBackgroundColor(getResources().getColor(R.color.white));
						break;
					case 3:
						mineViewPager.setCurrentItem(3);
						tevFirstPage.setTextColor(getResources().getColor(R.color.color_FF999999));
						tevProject.setTextColor(getResources().getColor(R.color.color_FF999999));
						tevSquare.setTextColor(getResources().getColor(R.color.color_FF999999));
						tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FFE066FF));
						tevMine.setTextColor(getResources().getColor(R.color.color_FF999999));
						
						ImmersionBar.with(MainActivity.this)
							.keyboardEnable(false)
							.statusBarDarkFont(false)
							.statusBarColor(R.color.color_FF198CFF)
							.navigationBarColor(R.color.color_FF198CFF).init();
						layoutBottom.setBackgroundColor(getResources().getColor(R.color.white));
						break;
					case 4:
						mineViewPager.setCurrentItem(4);
						tevFirstPage.setTextColor(getResources().getColor(R.color.color_FF999999));
						tevProject.setTextColor(getResources().getColor(R.color.color_FF999999));
						tevSquare.setTextColor(getResources().getColor(R.color.color_FF999999));
						tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FF999999));
						tevMine.setTextColor(getResources().getColor(R.color.color_FFE066FF));
						
						ImmersionBar.with(MainActivity.this)
							.keyboardEnable(false)
							.statusBarDarkFont(false)
							.statusBarColor(R.color.color_FFE066FF)
							.navigationBarColor(R.color.color_FFE066FF).init();
						layoutBottom.setBackgroundColor(getResources().getColor(R.color.black));
						break;
					default:
						break;
				}
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
			
			}
		});
		
		tevFirstPage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mineViewPager.setCurrentItem(0);
				tevFirstPage.setTextColor(getResources().getColor(R.color.color_FFE066FF));
				tevProject.setTextColor(getResources().getColor(R.color.color_FF999999));
				tevSquare.setTextColor(getResources().getColor(R.color.color_FF999999));
				tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FF999999));
				tevMine.setTextColor(getResources().getColor(R.color.color_FF999999));
			}
		});
		tevProject.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mineViewPager.setCurrentItem(1);
				tevFirstPage.setTextColor(getResources().getColor(R.color.color_FF999999));
				tevProject.setTextColor(getResources().getColor(R.color.color_FFE066FF));
				tevSquare.setTextColor(getResources().getColor(R.color.color_FF999999));
				tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FF999999));
				tevMine.setTextColor(getResources().getColor(R.color.color_FF999999));
			}
		});
		tevSquare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mineViewPager.setCurrentItem(2);
				tevFirstPage.setTextColor(getResources().getColor(R.color.color_FF999999));
				tevProject.setTextColor(getResources().getColor(R.color.color_FF999999));
				tevSquare.setTextColor(getResources().getColor(R.color.color_FFE066FF));
				tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FF999999));
				tevMine.setTextColor(getResources().getColor(R.color.color_FF999999));
			}
		});
		tevResourceCenter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mineViewPager.setCurrentItem(3);
				tevFirstPage.setTextColor(getResources().getColor(R.color.color_FF999999));
				tevProject.setTextColor(getResources().getColor(R.color.color_FF999999));
				tevSquare.setTextColor(getResources().getColor(R.color.color_FF999999));
				tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FFE066FF));
				tevMine.setTextColor(getResources().getColor(R.color.color_FF999999));
			}
		});
		tevMine.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mineViewPager.setCurrentItem(4);
				tevFirstPage.setTextColor(getResources().getColor(R.color.color_FF999999));
				tevProject.setTextColor(getResources().getColor(R.color.color_FF999999));
				tevSquare.setTextColor(getResources().getColor(R.color.color_FF999999));
				tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FF999999));
				tevMine.setTextColor(getResources().getColor(R.color.color_FFE066FF));
			}
		});
	}
	
	@Override
	protected void initLoadData() {
		mineViewPager.setCurrentItem(0);
		tevFirstPage.setTextColor(getResources().getColor(R.color.color_FFE066FF));
		tevProject.setTextColor(getResources().getColor(R.color.color_FF999999));
		tevSquare.setTextColor(getResources().getColor(R.color.color_FF999999));
		tevResourceCenter.setTextColor(getResources().getColor(R.color.color_FF999999));
		tevMine.setTextColor(getResources().getColor(R.color.color_FF999999));
		
		ImmersionBar.with(MainActivity.this)
			.keyboardEnable(false)
			.statusBarDarkFont(false)
			.statusBarColor(R.color.color_FFE066FF)
			.navigationBarColor(R.color.color_FFE066FF).init();
	}
	
	@Override
	protected MainPresenterImpl attachPresenter() {
		return new MainPresenterImpl(this);
	}
	
	@Override
	public void showLoading() {
		if (loadView != null && !loadView.isShown()) {
			loadView.setVisibility(View.VISIBLE);
			loadView.start();
		}
	}
	
	@Override
	public void hideLoading() {
		if (loadView != null && loadView.isShown()) {
			loadView.stop();
			loadView.setVisibility(View.GONE);
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
