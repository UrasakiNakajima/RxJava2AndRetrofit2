package com.phone.main_module.login.ui

import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.common_library.base.BaseMvpRxAppActivity
import com.phone.common_library.base.IBaseView
import com.phone.common_library.manager.ActivityPageManager
import com.phone.common_library.manager.LogManager
import com.phone.common_library.manager.ResourcesManager
import com.phone.common_library.manager.RetrofitManager
import com.phone.common_library.manager.SystemManager.getSystemId
import com.phone.main_module.R
import com.phone.main_module.login.bean.GetVerificationCode
import com.phone.main_module.login.bean.LoginResponse
import com.phone.main_module.login.presenter.LoginPresenterImpl
import com.phone.main_module.login.view.ILoginView
import com.phone.main_module.main.MainActivity

@Route(path = "/main_module/login")
class LoginActivity : BaseMvpRxAppActivity<IBaseView, LoginPresenterImpl>(), ILoginView {

    private val TAG = LoginActivity::class.java.simpleName
    private var toolbar: Toolbar? = null
    private var layoutBack: FrameLayout? = null
    private var imvBack: ImageView? = null
    private var edtUserId: EditText? = null
    private var edtPassword: EditText? = null
    private var layoutVerificationCode: FrameLayout? = null
    private var edtVerificationCode: EditText? = null
    private var tevGetAuthCode: TextView? = null
    private var tevLogin: TextView? = null
    private var tevJumpToRegister: TextView? = null

    private var userId: String? = null
    private var password: String? = null
    private var verificationCode: String? = null
    private var phoneDevice: String? = null

    override fun initLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initData() {
        ActivityPageManager.get()?.finishAllActivityExcept(LoginActivity::class.java)
    }

    override fun initViews() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        layoutBack = findViewById<View>(R.id.layout_back) as FrameLayout
        imvBack = findViewById<View>(R.id.imv_back) as ImageView
        edtUserId = findViewById<View>(R.id.edt_user_id) as EditText
        edtPassword = findViewById<View>(R.id.edt_password) as EditText
        layoutVerificationCode = findViewById<View>(R.id.layout_verification_code) as FrameLayout
        edtVerificationCode = findViewById<View>(R.id.edt_verification_code) as EditText
        tevGetAuthCode = findViewById<View>(R.id.tev_get_auth_code) as TextView
        tevLogin = findViewById<View>(R.id.tev_login) as TextView
        tevJumpToRegister = findViewById<View>(R.id.tev_jump_to_register) as TextView
        setToolbar(true, R.color.color_FFFFFFFF)
        imvBack?.setColorFilter(ResourcesManager.getColor(R.color.color_000000))
        layoutBack?.setOnClickListener { finish() }
        tevGetAuthCode?.setOnClickListener { view: View? -> getAuthCode() }
        tevLogin?.setOnClickListener { //                initLoginWithAuthCode()
            initLogin()
        }
        tevJumpToRegister?.setOnClickListener { startActivity(RegisterActivity::class.java) }
    }

    override fun initLoadData() {
        edtUserId?.setText("13510001000")
        edtPassword?.setText("12345678")
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        LogManager.i(TAG, "onNewIntent")
    }

    override fun getAuthCodeSuccess(success: GetVerificationCode.DataDTO) {
        showToast(success?.content, true)
    }

    override fun getAuthCodeError(error: String) {
        showToast(error, true)
    }

    override fun loginWithAuthCodeSuccess(success: LoginResponse.DataDTO) {
        //		showToast(success.getUserName(), true)
        startActivity(MainActivity::class.java)
        finish()
    }

    override fun loginWithAuthCodeError(error: String) {
        showToast(error, true)
    }

    override fun loginSuccess(success: String) {
        mBaseApplication?.setLogin(true)
        startActivity(MainActivity::class.java)
        finish()
    }

    override fun loginError(error: String) {
        showToast(error, true)
    }

    private fun getAuthCode() {
        userId = edtUserId?.text.toString()
        //		verificationCode = edtVerificationCode.getText().toString()
        //		phoneDevice = UIUtils.getDeviceUUid().toString()
        userId?.let {
            mBodyParams.clear()
            mBodyParams["account"] = it
            presenter.getAuthCode(this, mBodyParams)
        }
    }

    private fun initLoginWithAuthCode() {
        userId = edtUserId?.text.toString()
        verificationCode = edtVerificationCode?.text.toString()
        phoneDevice = getSystemId()
        if (TextUtils.isEmpty(userId)) {
            showToast(ResourcesManager.getString(R.string.please_enter_one_user_name), true)
            return
        }
        if (TextUtils.isEmpty(verificationCode)) {
            showToast(ResourcesManager.getString(R.string.please_input_a_password), true)
            return
        }
        if (TextUtils.isEmpty(phoneDevice)) {
            showToast(ResourcesManager.getString(R.string.please_input_a_password), true)
            return
        }
        mBodyParams.clear()
        mBodyParams["account"] = userId!!
        mBodyParams["captcha"] = verificationCode!!
        mBodyParams["type"] = "1" //1 APP
        mBodyParams["phoneDevice"] = phoneDevice!!
        presenter.loginWithAuthCode(this, mBodyParams)
    }

    private fun initLogin() {
        if (RetrofitManager.isNetworkAvailable()) {
            userId = edtUserId?.text.toString()
            password = edtPassword?.text.toString()
            if (TextUtils.isEmpty(userId)) {
                showToast(ResourcesManager.getString(R.string.please_enter_one_user_name), true)
                return
            }
            if (TextUtils.isEmpty(password)) {
                showToast(ResourcesManager.getString(R.string.please_input_a_password), true)
                return
            }
            mBodyParams.clear()
            mBodyParams["userId"] = userId!!
            mBodyParams["password"] = password!!
            presenter.login(this, mBodyParams)
        } else {
            showToast(
                ResourcesManager.getString(R.string.please_check_the_network_connection),
                true
            )
        }
    }


}