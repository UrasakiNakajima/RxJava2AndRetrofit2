package com.phone.library_mvp

import android.os.Bundle
import android.util.ArrayMap
import android.view.View
import com.phone.library_base.base.BaseRxFragment

abstract class BaseMvpRxFragment<V, T : BasePresenter<V>> : BaseRxFragment() {

    private val TAG = BaseMvpRxFragment::class.java.simpleName
    protected var presenter: T? = null

    protected var url: String? = null
    protected var mBodyParams = ArrayMap<String, String>()
    // 是否第一次加载
    protected var isFirstLoad = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter = attachPresenter()
        super.onViewCreated(view, savedInstanceState)

//        RxPermissionsManager rxPermissionsManager = RxPermissionsManager.getInstance(this);
//        rxPermissionsManager.initRxPermissionsRxFragment(new OnCommonRxPermissionsCallback() {
//            @Override
//            public void onRxPermissionsAllPass() {
//                CrashHandlerManager crashHandlerManager = CrashHandlerManager.getInstance(mRxAppCompatActivity);
//                crashHandlerManager.sendPreviousReportsToServer();
//                crashHandlerManager.init();
//            }
//
//            @Override
//            public void onNotCheckNoMorePromptError() {
//
//            }
//
//            @Override
//            public void onCheckNoMorePromptError() {
//
//            }
//        });
    }

    /**
     * 适配为不同的view 装载不同的presenter
     *
     * @return
     */
    protected abstract fun attachPresenter(): T

    protected fun detachPresenter() {
        presenter?.detachView()
    }

    override fun onDestroyView() {
        detachPresenter()
        mBodyParams.clear()
        if (mIsLoadView) {
            if (!mRxAppCompatActivity.isFinishing) {
                mDialogManager.dismissProgressBarDialog()
            }
        } else {
            if (!mRxAppCompatActivity.isFinishing) {
                mDialogManager.dismissLoadingDialog()
            }
        }
        mRootView?.let {
            mRootView = null
        }
        super.onDestroyView()
    }

}
