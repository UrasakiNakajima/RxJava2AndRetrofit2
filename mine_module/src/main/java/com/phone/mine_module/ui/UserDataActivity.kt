package com.phone.mine_module.ui

import android.view.View
import androidx.core.content.ContextCompat
import com.phone.common_library.base.BaseMvpRxAppActivity
import com.phone.common_library.base.IBaseView
import com.phone.mine_module.R
import com.phone.mine_module.presenter.MinePresenterImpl
import com.phone.mine_module.view.IUserDataView
import kotlinx.android.synthetic.main.activity_user_data.*

class UserDataActivity : BaseMvpRxAppActivity<IBaseView, MinePresenterImpl>(), IUserDataView {

    companion object {
        private val TAG = UserDataActivity::class.java.simpleName
    }

    override fun initLayoutId() = R.layout.activity_user_data

    override fun initData() {
    }

    override fun initViews() {
        setToolbar(false, R.color.color_FFE066FF)
        imv_back.setColorFilter(ContextCompat.getColor(this, R.color.color_FF198CFF))

        layout_back.setOnClickListener {
            finish()
        }
    }

    override fun initLoadData() {
        initUserData();
    }

    override fun attachPresenter() = MinePresenterImpl(this)

    override fun showLoading() {
        if (!loadView.isShown) {
            loadView.visibility = View.VISIBLE
            loadView.start()
        }
    }

    override fun hideLoading() {
        if (loadView.isShown) {
            loadView.stop()
            loadView.visibility = View.GONE
        }
    }

    override fun userDataSuccess(success: String) {

    }

    override fun userDataError(error: String) {

    }

    private fun initUserData() {
        bodyParams.clear()
        bodyParams.put("loginType", "3")

        presenter?.userData(this, bodyParams)
//        presenter.userData(this, baseApplication.accessToken, bodyParams)
    }

}