package com.phone.module_resource

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_common.BaseApplication
import com.phone.library_common.adapter.TabFragmentStatePagerAdapter
import com.phone.library_common.adapter.TabNavigatorAdapter
import com.phone.library_common.base.BaseMvvmRxFragment
import com.phone.library_common.base.State
import com.phone.library_common.bean.TabBean
import com.phone.library_common.common.ConstantData
import com.phone.library_common.manager.*
import com.phone.module_resource.databinding.ResourceFragmentResourceBinding
import com.phone.module_resource.fragment.AndroidAndJsFragment
import com.phone.module_resource.fragment.SubResourceFragment
import com.phone.module_resource.view.IResourceView
import com.phone.module_resource.view_model.ResourceViewModelImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter

@Route(path = ConstantData.Route.ROUTE_RESOURCE)
class ResourceFragment :
    BaseMvvmRxFragment<ResourceViewModelImpl, ResourceFragmentResourceBinding>(), IResourceView {

    private val TAG = ResourceFragment::class.java.simpleName
    private var fragmentStatePagerAdapter: TabFragmentStatePagerAdapter? = null

    override fun initLayoutId() = R.layout.resource_fragment_resource

    /**
     * 这里ViewModelProvider的参数要使用this，不要使用rxAppCompatActivity
     */
    override fun initViewModel() = ViewModelProvider(this).get(ResourceViewModelImpl::class.java)

    override fun initData() {
    }

    override fun initObservers() {
        viewModel.tabRxFragment.observe(this, {
//            LogManager.i(TAG, "onChanged*****tabRxFragment")
            when (it) {
                is State.SuccessState -> {
                    if (it.success != null && it.success.size > 0) {
                        resourceTabDataSuccess(it.success)
                    } else {
                        resourceTabDataError(BaseApplication.instance().resources.getString(R.string.no_data_available))
                    }
                }

                is State.ErrorState -> {
                    resourceTabDataError(
                        it.errorMsg
                    )
                }
            }
        })
    }

    override fun initViews() {
    }

    override fun initLoadData() {
//        lifecycleScope.launch {
//            LogManager.i(TAG, "viewModelScope.launch thread name*****" + Thread.currentThread().name)
//            //协程内部开启多个withContext、launch{}和async{}的时候，有一个规律，执行第一个withContext的时候就会串行执行（遇到withContext函数就会串行执行），
//            //然后执行完了第一个withContext，再并行执行第一个launch、第一个async、第二个launch、第二个async、第二个withContext，执行第二个withContext
//            //的时候又会串行执行（遇到withContext函数就会串行执行），然后执行完了第二个withContext，再并行执行第三个async、第四个async、第三个launch、第四个launch、第三个withContext，
//            //执行第三个withContext的时候又会串行执行（遇到withContext函数就会串行执行），然后执行完了第三个withContext，再执行第五个launch和第五个async。
//            withContext(Dispatchers.IO) {//第一个withContext
//                delay(2000)
//                LogManager.i(TAG, "first withContext delay(2000)")
//            }
//            launch {//第一个launch
//                delay(1000)
//                LogManager.i(TAG, "first launch delay(1000)")
//            }
//            async {//第一个async
//                delay(1000)
//                LogManager.i(TAG, "first async delay(1000)")
//            }
//            launch {//第二个launch
//                delay(1000)
//                LogManager.i(TAG, "second launch delay(1000)")
//            }
//            async {//第二个async
//                delay(1000)
//                LogManager.i(TAG, "second async delay(1000)")
//            }
//            withContext(Dispatchers.IO) {//第二个withContext
//                delay(2000)
//                LogManager.i(TAG, "second withContext delay(2000)")
//            }
//            async {//第三个async
//                delay(2000)
//                LogManager.i(TAG, "third async delay(2000)")
//            }
//            async {//第四个async
//                delay(2000)
//                LogManager.i(TAG, "fourth async delay(2000)")
//            }
//            launch {//第三个launch
//                delay(2000)
//                LogManager.i(TAG, "third launch delay(2000)")
//            }
//            launch {//第四个launch
//                delay(2000)
//                LogManager.i(TAG, "fourth launch delay(2000)")
//            }
//            withContext(Dispatchers.IO) {//第三个withContext
//                delay(1000)
//                LogManager.i(TAG, "third withContext delay(1000)")
//            }
//            launch {//第五个launch
//                delay(1000)
//                LogManager.i(TAG, "fifth launch delay(1000)")
//            }
//            async {//第五个async
//                delay(1000)
//                LogManager.i(TAG, "fifth async delay(1000)")
//            }
//        }

//        LogManager.i(TAG, "ResourceFragment initLoadData")
        initResourceTabData()
    }

    override fun showLoading() {
        if (!mRxAppCompatActivity.isFinishing() && !mDatabind.loadView.isShown()) {
            mDatabind.loadView.visibility = View.VISIBLE
            mDatabind.loadView.start()
        }
    }

    override fun hideLoading() {
        if (!mRxAppCompatActivity.isFinishing() && mDatabind.loadView.isShown()) {
            mDatabind.loadView.stop()
            mDatabind.loadView.visibility = View.GONE
        }
    }

    override fun resourceTabDataSuccess(success: MutableList<TabBean>) {
        if (!mRxAppCompatActivity.isFinishing) {
            val fragmentList = mutableListOf<Fragment>()
            val dataList = mutableListOf<TabBean>()
            val tabBean = TabBean()
            tabBean.name = ResourcesManager.getString(R.string.android_and_js_interactive)
            dataList.add(tabBean)
            dataList.addAll(success)

            for (i in dataList) {
                if (ResourcesManager.getString(R.string.android_and_js_interactive)
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

            fragmentStatePagerAdapter = TabFragmentStatePagerAdapter(
                childFragmentManager, fragmentList
            )
            mDatabind.mineViewPager2.setAdapter(fragmentStatePagerAdapter)
            //下划线绑定
            val commonNavigator = CommonNavigator(mRxAppCompatActivity)
            commonNavigator.adapter = getCommonNavigatorAdapter(dataList)
            mDatabind.tabLayout.navigator = commonNavigator
            MagicIndicatorManager.bindForViewPager(mDatabind.mineViewPager2, mDatabind.tabLayout)
            hideLoading()
        }
    }

    override fun resourceTabDataError(error: String) {
        if (!mRxAppCompatActivity.isFinishing()) {
            showCustomToast(
                ScreenManager.dpToPx(20f),
                ScreenManager.dpToPx(20f),
                18,
                ResourcesManager.getColor(R.color.white),
                ResourcesManager.getColor(R.color.color_FF198CFF),
                ScreenManager.dpToPx(40f),
                ScreenManager.dpToPx(20f),
                error,
                true
            )
            hideLoading()
        }
    }

    private fun initResourceTabData() {
        showLoading()
        if (RetrofitManager.isNetworkAvailable()) {
            viewModel.resourceTabData()
        } else {
            resourceTabDataError(resources.getString(R.string.please_check_the_network_connection))
        }
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
            mDatabind.mineViewPager2.currentItem = it
        }
    }

}