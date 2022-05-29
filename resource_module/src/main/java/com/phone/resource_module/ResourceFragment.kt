package com.phone.resource_module

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.common_library.adapter.TabFragmentStatePagerAdapter
import com.phone.common_library.base.BaseRxFragment
import com.phone.common_library.custom_view.LazyViewPager
import com.phone.resource_module.fragment.AndroidAndJsFragment
import com.phone.resource_module.fragment.ResourceChildFragment
import kotlinx.android.synthetic.main.fragment_resource.*

@Route(path = "/resource_module/resource")
class ResourceFragment : BaseRxFragment() {

    private val TAG = ResourceFragment::class.java.simpleName

    private var fragmentList: MutableList<Fragment> = mutableListOf()
    private var fragmentStatePagerAdapter: TabFragmentStatePagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO: inflate a fragment view
        rootView = super.onCreateView(inflater, container, savedInstanceState)
        return rootView
    }

    override fun initLayoutId(): Int {
        return R.layout.fragment_resource
    }

    override fun initData() {
        fragmentList.add(ResourceChildFragment.getInstance("all"))
        fragmentList.add(ResourceChildFragment.getInstance("福利"))
        fragmentList.add(ResourceChildFragment.getInstance("Android"))
        fragmentList.add(ResourceChildFragment.getInstance("iOS"))
        fragmentList.add(AndroidAndJsFragment.getInstance("H5"))
//        fragmentList.add(ResourceChildFragment.getInstance("App"))
    }

    override fun initViews() {
        fragmentStatePagerAdapter =
            TabFragmentStatePagerAdapter(
                childFragmentManager,
                fragmentList
            )
        mine_view_pager.setAdapter(fragmentStatePagerAdapter)
        mine_view_pager.setOnPageChangeListener(object : LazyViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        mine_view_pager.setCurrentItem(position)
                        tev_all.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FFE066FF
                            )
                        )
                        tev_beautiful_woman.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FF999999
                            )
                        )
                        tev_android.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FF999999
                            )
                        )
                        tev_ios.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FF999999
                            )
                        )
                        tev_h5.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FF999999
                            )
                        )
//                        tev_app.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
                    }
                    1 -> {
                        mine_view_pager.setCurrentItem(position)
                        tev_all.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FF999999
                            )
                        )
                        tev_beautiful_woman.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FFE066FF
                            )
                        )
                        tev_android.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FF999999
                            )
                        )
                        tev_ios.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FF999999
                            )
                        )
                        tev_h5.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FF999999
                            )
                        )
//                        tev_app.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
                    }
                    2 -> {
                        mine_view_pager.setCurrentItem(position)
                        tev_all.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FF999999
                            )
                        )
                        tev_beautiful_woman.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FF999999
                            )
                        )
                        tev_android.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FFE066FF
                            )
                        )
                        tev_ios.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FF999999
                            )
                        )
                        tev_h5.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FF999999
                            )
                        )
//                        tev_app.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
                    }
                    3 -> {
                        mine_view_pager.setCurrentItem(position)
                        tev_all.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FF999999
                            )
                        )
                        tev_beautiful_woman.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FF999999
                            )
                        )
                        tev_android.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FF999999
                            )
                        )
                        tev_ios.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FFE066FF
                            )
                        )
                        tev_h5.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FF999999
                            )
                        )
//                        tev_app.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
                    }
                    4 -> {
                        mine_view_pager.setCurrentItem(position)
                        tev_all.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FF999999
                            )
                        )
                        tev_beautiful_woman.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FF999999
                            )
                        )
                        tev_android.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FF999999
                            )
                        )
                        tev_ios.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FF999999
                            )
                        )
                        tev_h5.setTextColor(
                            ContextCompat.getColor(
                                rxAppCompatActivity!!,
                                R.color.color_FFE066FF
                            )
                        )
//                        tev_app.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
                    }
//                    5 -> {
//                        mine_view_pager.setCurrentItem(position)
//                        tev_all.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
//                        tev_beautiful_woman.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
//                        tev_android.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
//                        tev_ios.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
//                        tev_h5.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
//                        tev_app.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FFE066FF))
//                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        tev_all.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mine_view_pager.setCurrentItem(0)
                tev_all.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FFE066FF
                    )
                )
                tev_beautiful_woman.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FF999999
                    )
                )
                tev_android.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FF999999
                    )
                )
                tev_ios.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FF999999
                    )
                )
                tev_h5.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FF999999
                    )
                )
//                tev_app.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
            }
        })
        tev_beautiful_woman.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mine_view_pager.setCurrentItem(1)
                tev_all.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FF999999
                    )
                )
                tev_beautiful_woman.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FFE066FF
                    )
                )
                tev_android.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FF999999
                    )
                )
                tev_ios.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FF999999
                    )
                )
                tev_h5.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FF999999
                    )
                )
//                tev_app.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
            }
        })
        tev_android.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mine_view_pager.setCurrentItem(2)
                tev_all.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FF999999
                    )
                )
                tev_beautiful_woman.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FF999999
                    )
                )
                tev_android.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FFE066FF
                    )
                )
                tev_ios.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FF999999
                    )
                )
                tev_h5.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FF999999
                    )
                )
//                tev_app.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
            }
        })
        tev_ios.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mine_view_pager.setCurrentItem(3)
                tev_all.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FF999999
                    )
                )
                tev_beautiful_woman.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FF999999
                    )
                )
                tev_android.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FF999999
                    )
                )
                tev_ios.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FFE066FF
                    )
                )
                tev_h5.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FF999999
                    )
                )
//                tev_app.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
            }
        })
        tev_h5.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mine_view_pager.setCurrentItem(4)
                tev_all.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FF999999
                    )
                )
                tev_beautiful_woman.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FF999999
                    )
                )
                tev_android.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FF999999
                    )
                )
                tev_ios.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FF999999
                    )
                )
                tev_h5.setTextColor(
                    ContextCompat.getColor(
                        rxAppCompatActivity!!,
                        R.color.color_FFE066FF
                    )
                )
//                tev_app.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
            }
        })
//        tev_app.setOnClickListener(object :View.OnClickListener{
//            override fun onClick(v: View?) {
//                mine_view_pager.setCurrentItem(5)
//                tev_all.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
//                tev_beautiful_woman.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
//                tev_android.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
//                tev_ios.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
//                tev_h5.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
//                tev_app.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FFE066FF))
//            }
//        })
    }

    override fun initLoadData() {
        mine_view_pager.setCurrentItem(0)
        tev_all.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!, R.color.color_FFE066FF))
        tev_beautiful_woman.setTextColor(
            ContextCompat.getColor(
                rxAppCompatActivity!!,
                R.color.color_FF999999
            )
        )
        tev_android.setTextColor(
            ContextCompat.getColor(
                rxAppCompatActivity!!,
                R.color.color_FF999999
            )
        )
        tev_ios.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!, R.color.color_FF999999))
        tev_h5.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!, R.color.color_FF999999))
//        tev_app.setTextColor(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FF999999))
    }

    override fun onDestroyView() {

        super.onDestroyView()
    }

    override fun onDestroy() {
        fragmentList.clear()
        super.onDestroy()
    }
}