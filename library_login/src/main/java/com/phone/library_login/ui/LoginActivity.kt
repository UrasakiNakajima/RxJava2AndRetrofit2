package com.phone.library_login.ui

import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.phone.library_common.BuildConfig
import com.phone.library_mvp.BaseMvpRxAppActivity
import com.phone.library_base.common.ConstantData
import com.phone.library_login.bean.DataGetVerification
import com.phone.library_login.bean.DataLogin
import com.phone.library_base.manager.SystemManager.getSystemId
import com.phone.library_base.base.IBaseView
import com.phone.library_base.manager.ActivityPageManager
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_base.manager.SharedPreferencesManager
import com.phone.library_glide.manager.ImageLoaderManager
import com.phone.library_login.R
import com.phone.library_login.presenter.LoginPresenterImpl
import com.phone.library_login.view.ILoginView
import com.phone.library_network.manager.RetrofitManager

@Route(path = ConstantData.Route.ROUTE_LOGIN)
class LoginActivity : BaseMvpRxAppActivity<IBaseView, LoginPresenterImpl>(), ILoginView {

    private val TAG = LoginActivity::class.java.simpleName
    private var toolbar: Toolbar? = null
    private var layoutBack: FrameLayout? = null
    private var imvBack: ImageView? = null
    private var imvHeadPortrait: ImageView? = null
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
        return R.layout.login_activity_login
    }

    override fun initData() {
        ActivityPageManager.instance().finishAllActivityExcept(LoginActivity::class.java)
    }

    override fun initViews() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        layoutBack = findViewById<View>(R.id.layout_back) as FrameLayout
        imvBack = findViewById<View>(R.id.imv_back) as ImageView
        imvHeadPortrait = findViewById<View>(R.id.imv_head_portrait) as ImageView
        edtUserId = findViewById<View>(R.id.edt_user_id) as EditText
        edtPassword = findViewById<View>(R.id.edt_password) as EditText
        layoutVerificationCode = findViewById<View>(R.id.layout_verification_code) as FrameLayout
        edtVerificationCode = findViewById<View>(R.id.edt_verification_code) as EditText
        tevGetAuthCode = findViewById<View>(R.id.tev_get_auth_code) as TextView
        tevLogin = findViewById<View>(R.id.tev_login) as TextView
        tevJumpToRegister = findViewById<View>(R.id.tev_jump_to_register) as TextView
        setToolbar(true, R.color.library_color_FFFFFFFF)
        imvBack?.setColorFilter(ResourcesManager.getColor(R.color.library_color_000000))
        layoutBack?.setOnClickListener { finish() }
        imvHeadPortrait?.let {
            ImageLoaderManager.displayRound(mRxAppCompatActivity, it, R.mipmap.library_picture35)
        }
        tevGetAuthCode?.setOnClickListener { view: View? -> getAuthCode() }
        tevLogin?.setOnClickListener { //                initLoginWithAuthCode()
            initLogin()
        }
        tevJumpToRegister?.setOnClickListener {
            ARouter.getInstance().build(ConstantData.Route.ROUTE_REGISTER)
                .navigation()
        }
    }

    override fun initLoadData() {
        edtUserId?.setText("13510001000")
        edtPassword?.setText("12345678")
    }

    override fun attachPresenter(): LoginPresenterImpl {
        return LoginPresenterImpl(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        LogManager.i(TAG, "onNewIntent")
    }

    override fun getAuthCodeSuccess(success: DataGetVerification) {
        if (!mRxAppCompatActivity.isFinishing) {
            showToast(success.content, true)
            hideLoading()
        }
    }

    override fun getAuthCodeError(error: String) {
        if (!mRxAppCompatActivity.isFinishing) {
            showToast(error, true)
            hideLoading()
        }
    }

    override fun loginWithAuthCodeSuccess(success: DataLogin) {
        if (!mRxAppCompatActivity.isFinishing) {
            hideLoading()
            customStartActivity()
        }
    }

    override fun loginWithAuthCodeError(error: String) {
        if (!mRxAppCompatActivity.isFinishing) {
            showToast(error, true)
            hideLoading()
        }
    }

    override fun loginSuccess(success: String) {
        if (!mRxAppCompatActivity.isFinishing) {
            hideLoading()
            customStartActivity()
        }
    }

    override fun loginError(error: String) {
        if (!mRxAppCompatActivity.isFinishing) {
            showToast(error, true)
            hideLoading()
        }
    }

    private fun customStartActivity() {
        SharedPreferencesManager.put("isLogin", true)
        if (!BuildConfig.IS_MODULE) {
            ARouter.getInstance().build(ConstantData.Route.ROUTE_MAIN)
                .navigation()
        } else {
            val whichPage = mBaseApplication.mWhichPage
            when (whichPage) {
                "Main" -> {
                    ARouter.getInstance().build(ConstantData.Route.ROUTE_MAIN)
                        .navigation()
                }

                "Home" -> {
                    ARouter.getInstance().build(ConstantData.Route.ROUTE_HOME)
                        .navigation()
                }

                "Project" -> {
                    ARouter.getInstance().build(ConstantData.Route.ROUTE_PROJECT)
                        .navigation()
                }

                "Square" -> {
                    ARouter.getInstance().build(ConstantData.Route.ROUTE_SQUARE)
                        .navigation()
                }

                "Resource" -> {
                    ARouter.getInstance().build(ConstantData.Route.ROUTE_RESOURCE)
                        .navigation()
                }

                "Mine" -> {
                    ARouter.getInstance().build(ConstantData.Route.ROUTE_MINE)
                        .navigation()
                }
            }
        }
        finish()
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
            showToast(ResourcesManager.getString(R.string.library_please_enter_one_user_name), true)
            return
        }
        if (TextUtils.isEmpty(verificationCode)) {
            showToast(ResourcesManager.getString(R.string.library_please_input_a_password), true)
            return
        }
        if (TextUtils.isEmpty(phoneDevice)) {
            showToast(ResourcesManager.getString(R.string.library_please_input_a_password), true)
            return
        }
        showLoading()
        if (RetrofitManager.isNetworkAvailable()) {
            mBodyParams.clear()
            mBodyParams["account"] = userId!!
            mBodyParams["captcha"] = verificationCode!!
            mBodyParams["type"] = "1" //1 APP
            mBodyParams["phoneDevice"] = phoneDevice!!
            presenter.loginWithAuthCode(this, mBodyParams)
        } else {
            showToast(
                ResourcesManager.getString(R.string.library_please_check_the_network_connection),
                true
            )
            hideLoading()
        }
    }

    private fun initLogin() {
        showLoading()
        if (RetrofitManager.isNetworkAvailable()) {
            userId = edtUserId?.text.toString()
            password = edtPassword?.text.toString()
            if (TextUtils.isEmpty(userId)) {
                showToast(
                    ResourcesManager.getString(R.string.library_please_enter_one_user_name),
                    true
                )
                return
            }
            if (TextUtils.isEmpty(password)) {
                showToast(
                    ResourcesManager.getString(R.string.library_please_input_a_password),
                    true
                )
                return
            }
            mBodyParams.clear()
            mBodyParams["userId"] = userId!!
            mBodyParams["password"] = password!!
            presenter.login(this, mBodyParams)
        } else {
            showToast(
                ResourcesManager.getString(R.string.library_please_check_the_network_connection),
                true
            )
            hideLoading()
        }
    }


}