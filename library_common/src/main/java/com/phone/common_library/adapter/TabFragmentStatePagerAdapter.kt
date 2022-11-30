package com.phone.common_library.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class TabFragmentStatePagerAdapter(fm: FragmentManager, var fragmentList: List<Fragment>) :
    FragmentStatePagerAdapter(fm) {

    private val TAG = TabFragmentStatePagerAdapter::class.java.simpleName

    fun addFragment(fragment: Fragment?) {}

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return super.instantiateItem(container, position)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }
}