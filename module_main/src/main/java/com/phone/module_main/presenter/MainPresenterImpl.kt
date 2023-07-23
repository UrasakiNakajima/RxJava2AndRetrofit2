package com.phone.module_main.presenter

import com.phone.library_base.base.IBaseView
import com.phone.library_mvp.BasePresenter
import com.phone.module_main.model.IMainModel
import com.phone.module_main.model.MainModelImpl
import com.phone.module_main.view.IMainView
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/10 17:18
 * introduce : Presenter登录模块的（一个小模块对应一个Presenter）
 */

class MainPresenterImpl(baseView: IBaseView) : BasePresenter<IBaseView>(), IMainPresenter {

    companion object {
        private val TAG = MainPresenterImpl::class.java.simpleName
    }

    //    private IBaseView baseView;//P需要与V 交互，所以需要持有V的引用
    private val model: IMainModel = MainModelImpl()

    init {
        attachView(baseView)
    }

    override fun mainData(
        rxAppCompatActivity: RxAppCompatActivity,
        bodyParams: Map<String, String>
    ) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IMainView) {
                val mainView = baseView

//                RetrofitManager.instance()
//                        .responseString3(rxAppCompatActivity, model.mainData(bodyParams), new OnCommonSingleParamCallback<String>() {
//                            @Override
//                            public void onSuccess(String success) {
//
//                            }
//
//                            @Override
//                            public void onError(String error) {
//
//                            }
//                        });
            }
        }
    }

}
