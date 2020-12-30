package com.mobile.rxjava2andretrofit2.kotlin.resource

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.base.BaseFragment
import com.mobile.rxjava2andretrofit2.custom_view.LazyViewPager
import com.mobile.rxjava2andretrofit2.kotlin.resource.fragment.ResourceChildFragment
import com.mobile.rxjava2andretrofit2.main.MainActivity
import com.mobile.rxjava2andretrofit2.main.adapter.TabFragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_resource.*

class ResourceFragment : BaseFragment() {

    private val TAG: String = "ResourceFragment"
    private var mainActivity: MainActivity? = null

    private var fragmentList: MutableList<Fragment> = mutableListOf()
    private var fragmentPagerAdapter: TabFragmentPagerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // TODO: inflate a fragment view
        rootView = super.onCreateView(inflater, container, savedInstanceState)
        return rootView
    }

    override fun initLayoutId(): Int {
        return R.layout.fragment_resource
    }

    override fun initData() {
        mainActivity = activity as MainActivity

        fragmentList.add(ResourceChildFragment.getInstance("all"))
        fragmentList.add(ResourceChildFragment.getInstance("福利"))
        fragmentList.add(ResourceChildFragment.getInstance("Android"))
        fragmentList.add(ResourceChildFragment.getInstance("iOS"))
        fragmentList.add(ResourceChildFragment.getInstance("休息视频"))
//        fragmentList.add(ResourceChildFragment.getInstance("App"))
    }

    override fun initViews() {
        fragmentPagerAdapter = TabFragmentPagerAdapter(childFragmentManager, fragmentList)
        mine_view_pager.setAdapter(fragmentPagerAdapter)
        mine_view_pager.setOnPageChangeListener(object : LazyViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        mine_view_pager.setCurrentItem(position)
                        tev_all.setTextColor(resources.getColor(R.color.color_FFE066FF))
                        tev_beautiful_woman.setTextColor(resources.getColor(R.color.color_FF999999))
                        tev_android.setTextColor(resources.getColor(R.color.color_FF999999))
                        tev_ios.setTextColor(resources.getColor(R.color.color_FF999999))
                        tev_video.setTextColor(resources.getColor(R.color.color_FF999999))
//                        tev_app.setTextColor(resources.getColor(R.color.color_FF999999))
                    }
                    1 -> {
                        mine_view_pager.setCurrentItem(position)
                        tev_all.setTextColor(resources.getColor(R.color.color_FF999999))
                        tev_beautiful_woman.setTextColor(resources.getColor(R.color.color_FFE066FF))
                        tev_android.setTextColor(resources.getColor(R.color.color_FF999999))
                        tev_ios.setTextColor(resources.getColor(R.color.color_FF999999))
                        tev_video.setTextColor(resources.getColor(R.color.color_FF999999))
//                        tev_app.setTextColor(resources.getColor(R.color.color_FF999999))
                    }
                    2 -> {
                        mine_view_pager.setCurrentItem(position)
                        tev_all.setTextColor(resources.getColor(R.color.color_FF999999))
                        tev_beautiful_woman.setTextColor(resources.getColor(R.color.color_FF999999))
                        tev_android.setTextColor(resources.getColor(R.color.color_FFE066FF))
                        tev_ios.setTextColor(resources.getColor(R.color.color_FF999999))
                        tev_video.setTextColor(resources.getColor(R.color.color_FF999999))
//                        tev_app.setTextColor(resources.getColor(R.color.color_FF999999))
                    }
                    3 -> {
                        mine_view_pager.setCurrentItem(position)
                        tev_all.setTextColor(resources.getColor(R.color.color_FF999999))
                        tev_beautiful_woman.setTextColor(resources.getColor(R.color.color_FF999999))
                        tev_android.setTextColor(resources.getColor(R.color.color_FF999999))
                        tev_ios.setTextColor(resources.getColor(R.color.color_FFE066FF))
                        tev_video.setTextColor(resources.getColor(R.color.color_FF999999))
//                        tev_app.setTextColor(resources.getColor(R.color.color_FF999999))
                    }
                    4 -> {
                        mine_view_pager.setCurrentItem(position)
                        tev_all.setTextColor(resources.getColor(R.color.color_FF999999))
                        tev_beautiful_woman.setTextColor(resources.getColor(R.color.color_FF999999))
                        tev_android.setTextColor(resources.getColor(R.color.color_FF999999))
                        tev_ios.setTextColor(resources.getColor(R.color.color_FF999999))
                        tev_video.setTextColor(resources.getColor(R.color.color_FFE066FF))
//                        tev_app.setTextColor(resources.getColor(R.color.color_FF999999))
                    }
//                    5 -> {
//                        mine_view_pager.setCurrentItem(position)
//                        tev_all.setTextColor(resources.getColor(R.color.color_FF999999))
//                        tev_beautiful_woman.setTextColor(resources.getColor(R.color.color_FF999999))
//                        tev_android.setTextColor(resources.getColor(R.color.color_FF999999))
//                        tev_ios.setTextColor(resources.getColor(R.color.color_FF999999))
//                        tev_video.setTextColor(resources.getColor(R.color.color_FF999999))
//                        tev_app.setTextColor(resources.getColor(R.color.color_FFE066FF))
//                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        tev_all.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mine_view_pager.setCurrentItem(0)
                tev_all.setTextColor(resources.getColor(R.color.color_FFE066FF))
                tev_beautiful_woman.setTextColor(resources.getColor(R.color.color_FF999999))
                tev_android.setTextColor(resources.getColor(R.color.color_FF999999))
                tev_ios.setTextColor(resources.getColor(R.color.color_FF999999))
                tev_video.setTextColor(resources.getColor(R.color.color_FF999999))
//                tev_app.setTextColor(resources.getColor(R.color.color_FF999999))
            }
        })
        tev_beautiful_woman.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mine_view_pager.setCurrentItem(1)
                tev_all.setTextColor(resources.getColor(R.color.color_FF999999))
                tev_beautiful_woman.setTextColor(resources.getColor(R.color.color_FFE066FF))
                tev_android.setTextColor(resources.getColor(R.color.color_FF999999))
                tev_ios.setTextColor(resources.getColor(R.color.color_FF999999))
                tev_video.setTextColor(resources.getColor(R.color.color_FF999999))
//                tev_app.setTextColor(resources.getColor(R.color.color_FF999999))
            }
        })
        tev_android.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mine_view_pager.setCurrentItem(2)
                tev_all.setTextColor(resources.getColor(R.color.color_FF999999))
                tev_beautiful_woman.setTextColor(resources.getColor(R.color.color_FF999999))
                tev_android.setTextColor(resources.getColor(R.color.color_FFE066FF))
                tev_ios.setTextColor(resources.getColor(R.color.color_FF999999))
                tev_video.setTextColor(resources.getColor(R.color.color_FF999999))
//                tev_app.setTextColor(resources.getColor(R.color.color_FF999999))
            }
        })
        tev_ios.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mine_view_pager.setCurrentItem(3)
                tev_all.setTextColor(resources.getColor(R.color.color_FF999999))
                tev_beautiful_woman.setTextColor(resources.getColor(R.color.color_FF999999))
                tev_android.setTextColor(resources.getColor(R.color.color_FF999999))
                tev_ios.setTextColor(resources.getColor(R.color.color_FFE066FF))
                tev_video.setTextColor(resources.getColor(R.color.color_FF999999))
//                tev_app.setTextColor(resources.getColor(R.color.color_FF999999))
            }
        })
        tev_video.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mine_view_pager.setCurrentItem(4)
                tev_all.setTextColor(resources.getColor(R.color.color_FF999999))
                tev_beautiful_woman.setTextColor(resources.getColor(R.color.color_FF999999))
                tev_android.setTextColor(resources.getColor(R.color.color_FF999999))
                tev_ios.setTextColor(resources.getColor(R.color.color_FF999999))
                tev_video.setTextColor(resources.getColor(R.color.color_FFE066FF))
//                tev_app.setTextColor(resources.getColor(R.color.color_FF999999))
            }
        })
//        tev_app.setOnClickListener(object :View.OnClickListener{
//            override fun onClick(v: View?) {
//                mine_view_pager.setCurrentItem(5)
//                tev_all.setTextColor(resources.getColor(R.color.color_FF999999))
//                tev_beautiful_woman.setTextColor(resources.getColor(R.color.color_FF999999))
//                tev_android.setTextColor(resources.getColor(R.color.color_FF999999))
//                tev_ios.setTextColor(resources.getColor(R.color.color_FF999999))
//                tev_video.setTextColor(resources.getColor(R.color.color_FF999999))
//                tev_app.setTextColor(resources.getColor(R.color.color_FFE066FF))
//            }
//        })
    }

    override fun initLoadData() {
        mine_view_pager.setCurrentItem(0)
        tev_all.setTextColor(resources.getColor(R.color.color_FFE066FF))
        tev_beautiful_woman.setTextColor(resources.getColor(R.color.color_FF999999))
        tev_android.setTextColor(resources.getColor(R.color.color_FF999999))
        tev_ios.setTextColor(resources.getColor(R.color.color_FF999999))
        tev_video.setTextColor(resources.getColor(R.color.color_FF999999))
//        tev_app.setTextColor(resources.getColor(R.color.color_FF999999))
    }

    override fun onDestroyView() {
        fragmentList.clear()
        super.onDestroyView()
    }
}