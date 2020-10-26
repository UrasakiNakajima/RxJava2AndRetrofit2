package com.mobile.rxjava2andretrofit2.main.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2020/3/27 11:16
 * introduce :
 */

public class TabFragmentsPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = TabFragmentsPagerAdapter.class.getSimpleName();
    private List<Fragment> fragmentsList;

    public TabFragmentsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public TabFragmentsPagerAdapter(FragmentManager fm, List<Fragment> fragmentsList) {
        super(fm);
        this.fragmentsList = fragmentsList;
    }

    public void addFragment(Fragment fragment){
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
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
