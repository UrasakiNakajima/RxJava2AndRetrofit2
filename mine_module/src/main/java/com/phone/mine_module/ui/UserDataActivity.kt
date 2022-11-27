package com.phone.mine_module.ui

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.phone.common_library.base.BaseMvpRxAppActivity
import com.phone.common_library.base.IBaseView
import com.phone.common_library.manager.ResourcesManager
import com.phone.mine_module.R
import com.phone.mine_module.presenter.MinePresenterImpl
import com.phone.mine_module.view.IUserDataView

class UserDataActivity : BaseMvpRxAppActivity<IBaseView, MinePresenterImpl>(), IUserDataView {

    companion object {
        private val TAG = UserDataActivity::class.java.simpleName
    }

    private var toolbar: Toolbar? = null
    private var layoutBack: FrameLayout? = null
    private var imvBack: ImageView? = null
    private var tevTitle: TextView? = null

    override fun initLayoutId() = R.layout.activity_user_data

    override fun initData() {
    }

    override fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        layoutBack = findViewById(R.id.layout_back)
        imvBack = findViewById(R.id.imv_back)
        tevTitle = findViewById(R.id.tev_title)

        setToolbar(false, R.color.color_FF198CFF)
        imvBack?.setColorFilter(ResourcesManager.getColor(R.color.color_FF198CFF))
        layoutBack?.setOnClickListener {
            finish()
        }
    }

    override fun initLoadData() {
        initUserData()
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