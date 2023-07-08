package com.phone.module_mine.ui

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_mvp.BaseMvpRxAppActivity
import com.phone.library_base.base.IBaseView
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_common.common.ConstantData
import com.phone.library_network.manager.RetrofitManager
import com.phone.module_mine.R
import com.phone.module_mine.presenter.MinePresenterImpl
import com.phone.module_mine.view.IUserDataView

@Route(path = ConstantData.Route.ROUTE_USER_DATA)
class UserDataActivity : BaseMvpRxAppActivity<IBaseView, MinePresenterImpl>(), IUserDataView {

    companion object {
        private val TAG = UserDataActivity::class.java.simpleName
    }

    private var toolbar: Toolbar? = null
    private var layoutBack: FrameLayout? = null
    private var imvBack: ImageView? = null
    private var tevTitle: TextView? = null

    override fun initLayoutId() = R.layout.mine_activity_user_data

    override fun initData() {
    }

    override fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        layoutBack = findViewById(R.id.layout_back)
        imvBack = findViewById(R.id.imv_back)
        tevTitle = findViewById(R.id.tev_title)

        setToolbar(false, R.color.library_color_FF198CFF)
        imvBack?.setColorFilter(ResourcesManager.getColor(R.color.library_color_FF198CFF))
        layoutBack?.setOnClickListener {
            finish()
        }
    }

    override fun initLoadData() {
        initUserData()
    }

    override fun attachPresenter() = MinePresenterImpl(this)

    override fun showLoading() {
        if (!mLoadView.isShown) {
            mLoadView.visibility = View.VISIBLE
            mLoadView.start()
        }
    }

    override fun hideLoading() {
        if (mLoadView.isShown) {
            mLoadView.stop()
            mLoadView.visibility = View.GONE
        }
    }

    override fun userDataSuccess(success: String) {

        hideLoading()
    }

    override fun userDataError(error: String) {

        hideLoading()
    }

    private fun initUserData() {
        showLoading()
        if (RetrofitManager.isNetworkAvailable()) {
            mBodyParams.clear()
            mBodyParams.put("loginType", "3")
            presenter.userData(this, mBodyParams)
//        presenter.userData(this, baseApplication.accessToken, mBodyParams)
        } else {
            userDataError(ResourcesManager.getString(R.string.library_please_check_the_network_connection))
        }
    }

}