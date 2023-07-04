package com.phone.module_project

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.phone.library_common.BaseApplication
import com.phone.library_common.adapter.TabFragmentStatePagerAdapter
import com.phone.library_common.adapter.TabNavigatorAdapter
import com.phone.library_common.base.BaseMvvmAppRxActivity
import com.phone.library_common.base.State
import com.phone.library_common.bean.TabBean
import com.phone.library_common.manager.*
import com.phone.module_project.databinding.ProjectActivityProjectBinding
import com.phone.module_project.fragment.SubProjectFragment
import com.phone.module_project.view.IProjectView
import com.phone.module_project.view_model.ProjectViewModelImpl
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter

class ProjectActivity :
    BaseMvvmAppRxActivity<ProjectViewModelImpl, ProjectActivityProjectBinding>(),
    IProjectView {

    private val TAG = ProjectFragment::class.java.simpleName
    private var fragmentStatePagerAdapter: TabFragmentStatePagerAdapter? = null

    override fun initLayoutId() = R.layout.project_activity_project

    override fun initViewModel() =
        ViewModelProvider(mRxAppCompatActivity).get(ProjectViewModelImpl::class.java)

    override fun initData() {
    }

    override fun initObservers() {
        viewModel.dataxRxActivity.observe(this, {
            LogManager.i(TAG, "onChanged*****dataxRxActivity")
            when (it) {
                is State.SuccessState -> {
                    if (it.success != null && it.success.size > 0) {
                        projectTabDataSuccess(it.success)
                    } else {
                        projectTabDataError(BaseApplication.instance().resources.getString(R.string.no_data_available))
                    }
                }

                is State.ErrorState -> {
                    projectTabDataError(
                        it.errorMsg
                    )
                }
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
            fragmentStatePagerAdapter =
                TabFragmentStatePagerAdapter(
                    supportFragmentManager,
                    fragmentList
                )
            mDatabind.mineViewPager2.setAdapter(fragmentStatePagerAdapter)
            //下划线绑定
            val commonNavigator = CommonNavigator(mRxAppCompatActivity)
            commonNavigator.adapter = getCommonNavigatorAdapter(success)
            mDatabind.tabLayout.navigator = commonNavigator
            MagicIndicatorManager.bindForViewPager(mDatabind.mineViewPager2, mDatabind.tabLayout)
            hideLoading()
        }
    }

    override fun projectTabDataError(error: String) {
        if (!mRxAppCompatActivity.isFinishing()) {
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
            hideLoading()
        }
    }

    private fun initProjectTabData() {
        showLoading()
        if (RetrofitManager.isNetworkAvailable()) {
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