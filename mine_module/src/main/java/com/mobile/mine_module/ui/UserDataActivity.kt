package com.mobile.mine_module.ui

import android.view.View
import androidx.core.content.ContextCompat
import com.mobile.common_library.base.BaseMvpAppActivity
import com.mobile.common_library.base.IBaseView
import com.mobile.mine_module.R
import com.mobile.mine_module.presenter.MinePresenterImpl
import com.mobile.mine_module.view.IUserDataView
import kotlinx.android.synthetic.main.activity_user_data.*

class UserDataActivity : BaseMvpAppActivity<IBaseView, MinePresenterImpl>(), IUserDataView {

    companion object {
        private const val TAG = "UserDataActivity"
    }

    override fun initLayoutId(): Int {
        return R.layout.activity_user_data
    }

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

    override fun attachPresenter(): MinePresenterImpl {
        return MinePresenterImpl(this)
    }

    override fun showLoading() {
        if (loadView != null && !loadView.isShown) {
            loadView.visibility = View.VISIBLE
            loadView.start()
        }
    }

    override fun hideLoading() {
        if (loadView != null && loadView.isShown) {
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

        presenter.userData(this, bodyParams)
//        presenter.userData(this, baseApplication.accessToken, bodyParams)
    }

}