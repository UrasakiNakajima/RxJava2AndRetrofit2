package com.phone.module_resource.fragment

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
import com.phone.module_resource.R
import com.phone.module_resource.databinding.ResourceFragmentResourceBinding
import com.phone.module_resource.view.IResourceView
import com.phone.module_resource.view_model.ResourceViewModelImpl
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter

@Route(path = ConstantData.Route.ROUTE_RESOURCE_FRAGMENT)
class ResourceFragment :
    BaseMvvmRxFragment<ResourceViewModelImpl, ResourceFragmentResourceBinding>(R.layout.resource_fragment_resource), IResourceView {

    private val TAG = ResourceFragment::class.java.simpleName
    private var onPageChangeCallback: ViewPager2.OnPageChangeCallback? = null

//    override fun initLayoutId() = R.layout.resource_fragment_resource

    /**
     * 这里ViewModelProvider的参数要使用this，不要使用rxAppCompatActivity
     */
    override fun initViewModel() = ViewModelProvider(this).get(ResourceViewModelImpl::class.java)

    override fun initData() {
    }

    override fun initObservers() {
        mViewModel.tabData.observe(this, {
//            LogManager.i(TAG, "onChanged*****tabRxFragment")
            when (it) {
                is State.SuccessState -> {
                    if (it.success.size > 0) {
                        resourceTabDataSuccess(it.success)
                    } else {
                        resourceTabDataError(BaseApplication.instance().resources.getString(R.string.library_no_data_available))
                    }
                }

                is State.ErrorState -> {
                    resourceTabDataError(
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
            initResourceTabData()
            LogManager.i(TAG, "initLoadData*****$TAG")
            isFirstLoad = false
        }
    }

    override fun resourceTabDataSuccess(success: MutableList<TabBean>) {
        if (!mRxAppCompatActivity.isFinishing) {
            val fragmentList = mutableListOf<Fragment>()
            val dataList = mutableListOf<TabBean>()
            val tabBean = TabBean()
            tabBean.name = ResourcesManager.getString(R.string.library_android_and_js_interactive)
            dataList.add(tabBean)
            dataList.addAll(success)

            for (i in dataList) {
                if (ResourcesManager.getString(R.string.library_android_and_js_interactive)
                        .equals(i.name)
                ) {
                    fragmentList.add(AndroidAndJsFragment())
                } else {
                    fragmentList.add(SubResourceFragment().apply {
                        //想各个fragment传递信息
                        val bundle = Bundle()
                        bundle.putInt("type", 20)
                        bundle.putInt("tabId", i.id)
                        bundle.putString("name", i.name)
                        arguments = bundle
                    })
                }
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
            commonNavigator.adapter = getCommonNavigatorAdapter(dataList)
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

    override fun resourceTabDataError(error: String) {
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

    private fun initResourceTabData() {
        showLoading()
        ThreadPoolManager.instance().createScheduledThreadPoolToUIThread2(1000, {
            if (RetrofitManager.isNetworkAvailable()) {
                mViewModel.resourceTabData()
            } else {
                resourceTabDataError(resources.getString(R.string.library_please_check_the_network_connection))
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
            MagicIndicatorManager.unBindForViewPager2(mDatabind?.mineViewPager2, it)
        }
        ThreadPoolManager.instance().shutdownNowScheduledThreadPool()
        super.onDestroy()
    }
}