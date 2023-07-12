package com.phone.module_home.presenter

import com.phone.library_base.base.IBaseView
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_mvp.BasePresenter
import com.phone.module_home.R
import com.phone.module_home.model.HomeModelImpl
import com.phone.module_home.model.IHomeModel
import com.phone.module_home.view.IHomePageView
import com.trello.rxlifecycle3.components.support.RxFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 17:05
 * introduce : Presenter登录模块的（一个小模块对应一个Presenter）
 */

class HomePresenterImpl(baseView: IBaseView) : BasePresenter<IBaseView>(), IHomePresenter {

    companion object {
        private val TAG = HomePresenterImpl::class.java.simpleName
    }

    //    private IFirstPageView homePageView;//P需要与V 交互，所以需要持有V的引用
    private var mModel: IHomeModel

    //创建一个CoroutineScope 对象，创建的时候可以指定运行线程（默认运行在子线程）
    //即使Activity/Fragment已经被销毁，协程仍然在执行，所以需要绑定生命周期（就是在Activity/Fragment 销毁的时候取消这个协程），避免内存泄漏。
    //开启mCoroutineScope?.launch{} 或mCoroutineScope?.async{} 方法的时候可以指定运行线程（根据指定的线程来，不指定是创建的时候的线程）。
    var mCoroutineScope = CoroutineScope(Dispatchers.Main)

    init {
        attachView(baseView)
        mModel = HomeModelImpl()
    }

    override fun homePage(rxFragment: RxFragment, bodyParams: Map<String, String>) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IHomePageView) {
                val homePageView = baseView

                mCoroutineScope.launch {

//                    //协程内部只开启多个launch是并行的
//                    launch {
//                        delay(2000)
//                        LogManager.i(TAG, "launch delay(2000)")
//                    }
//                    launch {
//                        delay(1000)
//                        LogManager.i(TAG, "launch delay(1000)")
//                    }

                    val apiResponse = executeRequest { mModel.homePage(bodyParams) }
                    LogManager.i(
                        TAG, "homePage"
                    )
                    if (apiResponse.result != null && apiResponse.error_code == 0) {
                        val list = apiResponse.result?.data ?: mutableListOf()
                        if (list.size > 0) {
                            homePageView.homePageDataSuccess(
                                list
                            )
                        } else {
                            homePageView.homePageDataError(
                                ResourcesManager.getString(
                                    R.string.library_no_data_available
                                )
                            )
                        }
                    } else {
                        homePageView.homePageDataError(
                            apiResponse.reason ?: ResourcesManager.getString(
                                R.string.library_loading_failed
                            )
                        )
                    }
                    LogManager.i(TAG, "homePage2 thread name*****${Thread.currentThread().name}")
                }

            }
        }
    }

    override fun detachView() {
        mCoroutineScope.cancel()
        super.detachView()
    }

}