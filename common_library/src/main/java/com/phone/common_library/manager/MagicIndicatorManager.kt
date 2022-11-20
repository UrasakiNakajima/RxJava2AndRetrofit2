package com.phone.common_library.manager

import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import net.lucode.hackware.magicindicator.MagicIndicator

object MagicIndicatorManager {

    fun bindForViewPager2(vp: ViewPager2, miTabs: MagicIndicator) {
        vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                miTabs.onPageScrollStateChanged(state)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                miTabs.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                miTabs.onPageSelected(position)
            }
        })
    }

    fun bindForViewPager(vp: ViewPager, miTabs: MagicIndicator) {
        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                miTabs.onPageScrollStateChanged(state)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                miTabs.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                miTabs.onPageSelected(position)
            }
        })
    }
}