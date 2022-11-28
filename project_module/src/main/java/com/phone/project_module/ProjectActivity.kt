package com.phone.project_module

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.phone.common_library.BaseApplication
import com.phone.common_library.adapter.TabFragmentStatePagerAdapter
import com.phone.common_library.adapter.TabNavigatorAdapter
import com.phone.common_library.base.BaseMvvmAppRxActivity
import com.phone.common_library.bean.TabBean
import com.phone.common_library.manager.*
import com.phone.project_module.databinding.ActivityProjectBinding
import com.phone.project_module.fragment.SubProjectFragment
import com.phone.project_module.view.IProjectView
import com.phone.project_module.view_model.ProjectViewModelImpl
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter

class ProjectActivity :
    BaseMvvmAppRxActivity<ProjectViewModelImpl, ActivityProjectBinding>(),
    IProjectView {

    private val TAG = ProjectFragment::class.java.simpleName
    private var fragmentStatePagerAdapter: TabFragmentStatePagerAdapter? = null

    override fun initLayoutId() = R.layout.activity_project

    override fun initViewModel() =
        ViewModelProvider(rxAppCompatActivity).get(ProjectViewModelImpl::class.java)

    override fun initData() {
    }

    override fun initObservers() {
        viewModel.tabRxActivitySuccess.observe(this, {
            if (it != null && it.size > 0) {
                LogManager.i(TAG, "onChanged*****tabRxFragmentSuccess")
                projectTabDataSuccess(it)
            } else {
                projectTabDataError(BaseApplication.get().resources.getString(R.string.no_data_available))
            }
        })
        viewModel.tabRxActivityError.observe(this, {
            it?.let {
                LogManager.i(TAG, "onChanged*****tabRxFragmentError")
                projectTabDataError(it)
            }
        })
    }

    override fun initViews() {
    }

    override fun initLoadData() {
        LogManager.i(TAG, "ProjectFragment initLoadData")
        initProjectTabData()
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

    override fun projectTabDataSuccess(success: MutableList<TabBean>) {
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

    override fun projectTabDataError(error: String) {
        if (!rxAppCompatActivity.isFinishing()) {
            showCustomToast(
                ScreenManager.dpToPx(20f),
                ScreenManager.dpToPx(20f),
                18,
                ResourcesManager.getColor(R.color.white),
                ResourcesManager.getColor(R.color.color_FFE066FF),
                ScreenManager.dpToPx(40f),
                ScreenManager.dpToPx(20f),
                error,
                true
            )
        }
        hideLoading()
    }

    private fun initProjectTabData() {
        if (RetrofitManager.isNetworkAvailable()) {
            showLoading()
            viewModel.projectTabData2()
        } else {
            projectTabDataError(resources!!.getString(R.string.please_check_the_network_connection))
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