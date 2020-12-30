package com.mobile.rxjava2andretrofit2.kotlin.square

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.base.BaseFragment

class SquareFragment() : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initLayoutId(): Int {
        return R.layout.fragment_resource
    }

    override fun initData() {

    }

    override fun initViews() {

    }

    override fun initLoadData() {

    }
}