package com.mobile.common_library.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * author    : Urasaki
 * e-mail    : Urasaki@qq.com
 * date      : 2020/3/27 11:16
 * introduce :
 */

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = TabFragmentPagerAdapter.class.getSimpleName();
    private List<Fragment> fragmentList;

    public TabFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public TabFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    public void addFragment(Fragment fragment){
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}
