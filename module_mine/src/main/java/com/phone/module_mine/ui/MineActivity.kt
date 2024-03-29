package com.phone.module_mine.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_binding.BaseBindingRxAppActivity
import com.phone.library_base.common.ConstantData
import com.phone.module_mine.R
import com.phone.module_mine.databinding.MineActivityMineBinding
import com.phone.module_mine.fragment.MineFragment

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce :
 */

@Route(path = ConstantData.Route.ROUTE_MINE)
class MineActivity : BaseBindingRxAppActivity<MineActivityMineBinding>() {

    companion object {
        private val TAG: String = MineActivity::class.java.simpleName
    }

    override fun initLayoutId() = R.layout.mine_activity_mine

    override fun initData() {

    }

    override fun initViews() {
        setToolbar(false, R.color.library_color_FF198CFF)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, MineFragment()) // 此处的R.id.fragment_container是要盛放fragment的父容器
            .commit()
    }

    override fun initLoadData() {

    }

}
