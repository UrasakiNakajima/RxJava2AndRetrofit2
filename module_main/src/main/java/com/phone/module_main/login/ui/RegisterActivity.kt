package com.phone.module_main.login.ui

import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.phone.common_library.base.BaseMvpRxAppActivity
import com.phone.common_library.base.IBaseView
import com.phone.common_library.manager.ResourcesManager
import com.phone.common_library.manager.RetrofitManager.Companion.isNetworkAvailable
import com.phone.module_main.R
import com.phone.module_main.login.presenter.LoginPresenterImpl
import com.phone.module_main.login.view.IRegisterView

class RegisterActivity : BaseMvpRxAppActivity<IBaseView, LoginPresenterImpl>(), IRegisterView {

    private val TAG = RegisterActivity::class.java.simpleName
    private var toolbar: Toolbar? = null
    private var layoutBack: FrameLayout? = null
    private var imvBack: ImageView? = null
    private var edtUserId: EditText? = null
    private var edtPassword: EditText? = null
    private var edtConfirmPassword: EditText? = null
    private var tevRegister: TextView? = null

    override fun initLayoutId(): Int {
        return R.layout.activity_register
    }

    override fun initData() {
    }

    override fun initViews() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        layoutBack = findViewById<View>(R.id.layout_back) as FrameLayout
        imvBack = findViewById<View>(R.id.imv_back) as ImageView
        edtUserId = findViewById<View>(R.id.edt_user_id) as EditText
        edtPassword = findViewById<View>(R.id.edt_password) as EditText
        edtConfirmPassword = findViewById<View>(R.id.edt_confirm_password) as EditText
        tevRegister = findViewById<View>(R.id.tev_register) as TextView
        setToolbar(true, R.color.color_FFFFFFFF)
        imvBack?.setColorFilter(ResourcesManager.getColor(R.color.color_000000))
        layoutBack?.setOnClickListener { finish() }
        tevRegister?.setOnClickListener { initRegister() }
    }

    override fun initLoadData() {
        edtUserId?.setText("13510001000")
        edtPassword?.setText("12345678")
        edtConfirmPassword?.setText("12345678")
    }

    override fun attachPresenter(): LoginPresenterImpl {
        return LoginPresenterImpl(this)
    }

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

    override fun registerSuccess(success: String) {
        startActivity(LoginActivity::class.java)
    }

    override fun registerError(error: String) {
        showToast(error, true)
    }

    private fun initRegister() {
        if (isNetworkAvailable()) {
            val password = edtPassword?.text.toString()
            val confirmPassword = edtConfirmPassword?.text.toString()
            if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword) && password == confirmPassword) {
                mBodyParams.clear()
                mBodyParams["userId"] = edtUserId?.text.toString()
                mBodyParams["password"] = edtPassword?.text.toString()
                mBodyParams["confirmPassword"] = edtConfirmPassword?.text.toString()
                //        String data = MapManager.mapToJsonStr(getBodyParams())
//        String requestData = JSONObject.toJSONString(getBodyParams())
//        LogManager.i(TAG, "requestData*****" + requestData)
                presenter.register(this, mBodyParams)
            } else {
                showToast(
                    ResourcesManager.getString(R.string.the_passwords_entered_twice_are_inconsistent),
                    true
                )
            }
        } else {
            showToast(
                ResourcesManager.getString(R.string.please_check_the_network_connection),
                true
            )
        }
    }

}