package com.phone.resource_module

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gyf.immersionbar.ImmersionBar
import com.phone.common_library.BaseApplication
import com.phone.common_library.adapter.TabFragmentStatePagerAdapter
import com.phone.common_library.base.BaseMvvmAppRxActivity
import com.phone.common_library.base.BaseRxAppActivity
import com.phone.common_library.bean.DataX
import com.phone.common_library.custom_view.LazyViewPager
import com.phone.common_library.custom_view.LazyViewPager2
import com.phone.common_library.manager.LogManager
import com.phone.common_library.manager.MagicIndicatorManager
import com.phone.common_library.manager.RetrofitManager
import com.phone.common_library.manager.ScreenManager
import com.phone.resource_module.adapter.TabNavigatorAdapter
import com.phone.resource_module.bean.TabBean
import com.phone.resource_module.databinding.ActivityResourceBinding
import com.phone.resource_module.databinding.ActivityResourceBindingImpl
import com.phone.resource_module.databinding.FragmentResourceBinding
import com.phone.resource_module.fragment.AndroidAndJsFragment
import com.phone.resource_module.fragment.ResourceChildFragment
import com.phone.resource_module.view.IResourceView
import com.phone.resource_module.view_model.ResourceViewModelImpl
import kotlinx.android.synthetic.main.activity_resource.*
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import java.util.ArrayList

class ResourceActivity :
    BaseMvvmAppRxActivity<ResourceViewModelImpl, ActivityResourceBinding>(),
    IResourceView {

    private val TAG = ResourceActivity::class.java.simpleName

    private lateinit var tabSuccessObserver: Observer<MutableList<TabBean>>
    private lateinit var tabErrorObserver: Observer<String>
    private var fragmentStatePagerAdapter: TabFragmentStatePagerAdapter? = null

    override fun initLayoutId() = R.layout.activity_resource

    override fun initViewModel() =
        ViewModelProvider(rxAppCompatActivity).get(ResourceViewModelImpl::class.java)

    override fun initData() {
    }

    override fun initObservers() {
        tabSuccessObserver = object : Observer<MutableList<TabBean>> {
            override fun onChanged(t: MutableList<TabBean>?) {
                if (t != null && t.size > 0) {
                    LogManager.i(TAG, "onChanged*****tabSuccessObserver")
                    resourceTabDataSuccess(t)
                } else {
                    resourceTabDataError(BaseApplication.getInstance().resources.getString(R.string.no_data_available))
                }
            }
        }

        tabErrorObserver = object : Observer<String> {
            override fun onChanged(t: String?) {
                if (!TextUtils.isEmpty(t)) {
                    LogManager.i(TAG, "onChanged*****dataxErrorObserver")
                    resourceTabDataError(t!!)
                }
            }
        }

        viewModel.tabRxActivitySuccess.observe(this, tabSuccessObserver)
        viewModel.tabRxActivityError.observe(this, tabErrorObserver)
    }

    override fun initViews() {
        setToolbar(false, R.color.color_FF198CFF)
    }

    override fun initLoadData() {
        LogManager.i(TAG, "ResourceActivity initLoadData")
        initResourceTabData()
    }

    override fun showLoading() {
        if (!mDatabind.loadView.isShown()) {
            mDatabind.loadView.setVisibility(View.VISIBLE)
            mDatabind.loadView.start()
        }
    }

    override fun hideLoading() {
        if (mDatabind.loadView.isShown()) {
            mDatabind.loadView.stop()
            mDatabind.loadView.setVisibility(View.GONE)
        }
    }

    override fun resourceTabDataSuccess(success: MutableList<TabBean>) {
        val fragmentList = mutableListOf<Fragment>()
        success.forEach {
            fragmentList.add(ResourceChildFragment().apply {
                //想各个fragment传递信息
                val bundle = Bundle()
                bundle.putInt("type", 20)
                bundle.putInt("tabId", it.id)
                bundle.putString("name", it.name)
                arguments = bundle
            })
        }
        fragmentStatePagerAdapter =
            TabFragmentStatePagerAdapter(
                supportFragmentManager,
                fragmentList
            )
        mDatabind.mineViewPager2.setAdapter(fragmentStatePagerAdapter)

        //下划线绑定
        val commonNavigator = CommonNavigator(rxAppCompatActivity)
        commonNavigator.adapter = getCommonNavigatorAdapter(success)
        mDatabind.tabLayout.navigator = commonNavigator
        MagicIndicatorManager.bindForViewPager(mDatabind.mineViewPager2, mDatabind.tabLayout)

        hideLoading()
    }

    override fun resourceTabDataError(error: String) {
        if (!rxAppCompatActivity.isFinishing()) {
            showCustomToast(
                ScreenManager.dpToPx(rxAppCompatActivity, 20f),
                ScreenManager.dpToPx(rxAppCompatActivity, 20f),
                18,
                ContextCompat.getColor(rxAppCompatActivity, R.color.white),
                ContextCompat.getColor(rxAppCompatActivity, R.color.color_FFE066FF),
                ScreenManager.dpToPx(rxAppCompatActivity, 40f),
                ScreenManager.dpToPx(rxAppCompatActivity, 20f),
                error,
                true
            )
        }
        hideLoading()
    }

    private fun initResourceTabData() {
        if (RetrofitManager.isNetworkAvailable(rxAppCompatActivity)) {
            showLoading()
            viewModel.resourceTabData2()
        } else {
            resourceTabDataError(resources!!.getString(R.string.please_check_the_network_connection))
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