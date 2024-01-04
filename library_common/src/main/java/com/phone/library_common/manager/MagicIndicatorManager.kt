package com.phone.library_common.manager

import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import net.lucode.hackware.magicindicator.MagicIndicator

object MagicIndicatorManager {

    @JvmStatic
    fun bindForViewPager2(vp: ViewPager2?, onPageChangeCallback: OnPageChangeCallback) {
        vp?.registerOnPageChangeCallback(onPageChangeCallback)
    }

    @JvmStatic
    fun unBindForViewPager2(vp: ViewPager2?, onPageChangeCallback: OnPageChangeCallback) {
        vp?.unregisterOnPageChangeCallback(onPageChangeCallback)
    }

    @JvmStatic
    fun bindForViewPager(vp: ViewPager, miTabs: MagicIndicator) {
        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                miTabs.onPageScrollStateChanged(state)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                miTabs.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                miTabs.onPageSelected(position)
            }
        })
    }
}