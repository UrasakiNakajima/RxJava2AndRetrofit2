package com.phone.module_project.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_base.BaseApplication
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_base.manager.ScreenManager
import com.phone.library_common.adapter.TabNavigatorAdapter
import com.phone.library_mvvm.BaseMvvmRxFragment
import com.phone.library_network.bean.State
import com.phone.library_common.bean.TabBean
import com.phone.library_base.common.ConstantData
import com.phone.library_base.manager.LogManager
import com.phone.library_common.manager.*
import com.phone.library_network.manager.RetrofitManager
import com.phone.library_base.manager.ThreadPoolManager
import com.phone.library_common.adapter.ViewPager2Adapter
import com.phone.module_project.R
import com.phone.module_project.databinding.ProjectFragmentProjectBinding
import com.phone.module_project.view.IProjectView
import com.phone.module_project.view_model.ProjectViewModelImpl
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter

@Route(path = ConstantData.Route.ROUTE_PROJECT_FRAGMENT)
class ProjectFragment : BaseMvvmRxFragment<ProjectViewModelImpl, ProjectFragmentProjectBinding>(R.layout.project_fragment_project),
    IProjectView {

    private val TAG = ProjectFragment::class.java.simpleName
    private var onPageChangeCallback: ViewPager2.OnPageChangeCallback? = null

//    override fun initLayoutId() = R.layout.project_fragment_project

    /**
     * 这里ViewModelProvider的参数要使用this，不要使用rxAppCompatActivity
     */
    override fun initViewModel() = ViewModelProvider(this).get(ProjectViewModelImpl::class.java)

    override fun initData() {
    }

    override fun initObservers() {
        mViewModel.tabData.observe(this, {
            LogManager.i(TAG, "ProjectFragment onChanged*****dataxRxFragment")
            when (it) {
                is State.SuccessState -> {
                    if (it.success.size > 0) {
                        projectTabDataSuccess(it.success)
                    } else {
                        projectTabDataError(BaseApplication.instance().resources.getString(R.string.library_no_data_available))
                    }
                }

                is State.ErrorState -> {
                    projectTabDataError(
                        it.errorMsg
                    )
                }

                else -> {}
            }
        })
    }

    override fun initViews() {
    }

    override fun initLoadData() {
        if (isFirstLoad) {
            initProjectTabData()
            LogManager.i(TAG, "initLoadData*****$TAG")
            isFirstLoad = false
        }
    }

    override fun projectTabDataSuccess(success: MutableList<TabBean>) {
        if (!mRxAppCompatActivity.isFinishing) {
            val fragmentList = mutableListOf<Fragment>()
            success.forEach {
                fragmentList.add(SubProjectFragment().apply {
                    //想各个fragment传递信息
                    val bundle = Bundle()
                    bundle.putInt("type", 20)
                    bundle.putInt("tabId", it.id)
                    bundle.putString("name", it.name)
                    arguments = bundle
                })
            }

            // ORIENTATION_HORIZONTAL：水平滑动（默认），ORIENTATION_VERTICAL：竖直滑动
            mDatabind?.mineViewPager2?.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            // 预加载所有的Fragment，但是只执行第一个Fragment onResmue 方法
            mDatabind?.mineViewPager2?.offscreenPageLimit = fragmentList.size
            // 适配
            mDatabind?.mineViewPager2?.adapter = ViewPager2Adapter(
                fragmentList, mRxAppCompatActivity.getSupportFragmentManager(), getLifecycle()
            )
            //下划线绑定
            val commonNavigator = CommonNavigator(mRxAppCompatActivity)
            commonNavigator.adapter = getCommonNavigatorAdapter(success)
            mDatabind?.tabLayout?.navigator = commonNavigator
            onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    mDatabind?.tabLayout?.onPageScrollStateChanged(state)
                }

                override fun onPageScrolled(
                    position: Int, positionOffset: Float, positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    mDatabind?.tabLayout?.onPageScrolled(
                        position, positionOffset, positionOffsetPixels
                    )
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    mDatabind?.tabLayout?.onPageSelected(position)
                }
            }
            MagicIndicatorManager.bindForViewPager2(
                mDatabind?.mineViewPager2,
                onPageChangeCallback!!
            )
            hideLoading()
        }
    }

    override fun projectTabDataError(error: String) {
        if (!mRxAppCompatActivity.isFinishing()) {
            showCustomToast(
                ScreenManager.dpToPx(20f),
                ScreenManager.dpToPx(20f),
                18,
                ResourcesManager.getColor(R.color.library_white),
                ResourcesManager.getColor(R.color.library_color_FF198CFF),
                ScreenManager.dpToPx(40f),
                ScreenManager.dpToPx(20f),
                error
            )
            hideLoading()
        }
    }

    private fun initProjectTabData() {
        showLoading()
        ThreadPoolManager.instance().createScheduledThreadPoolToUIThread2(1000, {
            if (RetrofitManager.isNetworkAvailable()) {
                mViewModel.projectTabData()
            } else {
                projectTabDataError(resources.getString(R.string.library_please_check_the_network_connection))
            }
        })
    }

    /**
     * 获取下划线根跟字适配器
     */
    private fun getCommonNavigatorAdapter(tabList: MutableList<TabBean>): CommonNavigatorAdapter {
        return TabNavigatorAdapter(mutableListOf<String>().apply {
            //将tab转换为String
            tabList.forEach {
                it.name?.let { it1 -> add(it1) }
            }
        }) {
            mDatabind?.mineViewPager2?.currentItem = it
        }
    }

    override fun onDestroy() {
        onPageChangeCallback?.let {
            MagicIndicatorManager.unBindForViewPager2(
                mDatabind?.mineViewPager2, it
            )
        }
        ThreadPoolManager.instance().shutdownNowScheduledThreadPool()
        super.onDestroy()
    }
}