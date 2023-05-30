package com.phone.module_mine.ui

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.phone.library_common.base.BaseMvpRxAppActivity
import com.phone.library_common.base.IBaseView
import com.phone.library_common.manager.ResourcesManager
import com.phone.module_mine.R
import com.phone.module_mine.presenter.MinePresenterImpl
import com.phone.module_mine.view.IUserDataView

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

    }

    override fun userDataError(error: String) {

    }

    private fun initUserData() {
        mBodyParams.clear()
        mBodyParams.put("loginType", "3")

        presenter.userData(this, mBodyParams)
//        presenter.userData(this, baseApplication.accessToken, mBodyParams)
    }

}