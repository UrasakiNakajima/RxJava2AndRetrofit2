package com.phone.module_mine.presenter

import android.text.TextUtils
import com.phone.library_base.BaseApplication
import com.phone.library_mvp.BasePresenter
import com.phone.library_base.base.IBaseView
import com.phone.library_base.callback.OnCommonSingleParamCallback
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_network.manager.RetrofitManager
import com.phone.module_mine.R
import com.phone.module_mine.model.MineModelImpl
import com.phone.module_mine.view.IMineView
import com.phone.module_mine.view.IUserDataView
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MinePresenterImpl(baseView: IBaseView) : BasePresenter<IBaseView>(), IMinePresenter {

    companion object {
        private val TAG = MinePresenterImpl::class.java.simpleName
    }

    //    private IResourceChildView feedbackView;//P需要与V 交互，所以需要持有V的引用
    private var model = MineModelImpl()

    //创建一个MainScope 对象，默认运行在UI线程
    //即使Activity/Fragment已经被销毁，协程仍然在执行，所以需要绑定生命周期（就是在Activity/Fragment 销毁的时候取消这个协程），避免内存泄漏。
    //开启mMainScope?.launch{} 或 mMainScope?.async{} 方法的时候可以指定运行线程（根据指定的线程来，不指定是创建的时候的线程）。
    private val mainScope = MainScope()

    init {
        attachView(baseView)
    }

    override fun mineData(rxFragment: RxFragment, bodyParams: Map<String, String>) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IMineView) {
                mainScope.launch {
//                    val apiResponse = executeRequest2 { model.mineData(bodyParams) }
//                    if (apiResponse.result != null && apiResponse.error_code == 0) {
//                        val list = apiResponse.result?.data ?: mutableListOf()
//                        if (list.size > 0) {
//                            baseView.mineDataSuccess(list)
//                        } else {
//                            baseView.mineDataError(
//                                ResourcesManager.getString(
//                                    R.string.library_no_data_available
//                                )
//                            )
//                        }
//                    } else {
//                        baseView.mineDataError(
//                            apiResponse.reason ?: BaseApplication.instance().resources.getString(
//                                R.string.library_loading_failed
//                            )
//                        )
//                    }


                    val apiResponse = executeFlowRequest2(
                        reponseBlock = { model.mineData(bodyParams) },
                        errorBlock = { _, _2 ->
                            baseView.mineDataError(
                                _2
                            )
                        })
                    if (apiResponse?.error_code == 0) {
                        val list = apiResponse.result?.data ?: mutableListOf()
                        if (list.size > 0) {
                            baseView.mineDataSuccess(list)
                        } else {
                            baseView.mineDataError(
                                ResourcesManager.getString(
                                    R.string.library_no_data_available
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    override fun userData(
        rxAppCompatActivity: RxAppCompatActivity, bodyParams: Map<String, String>
    ) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IUserDataView) {
                RetrofitManager.instance.responseString5(rxAppCompatActivity,
                    model.userData(bodyParams),
                    object :
                        OnCommonSingleParamCallback<String> {
                        override fun onSuccess(success: String) {
                            LogManager.i(TAG, "userData success*****$success")
                            if (!TextUtils.isEmpty(success)) {
//                                    val response = JSONObject.parseObject(success, MineResponse::class.java)
//                                    val gson = Gson()
//                                    val response = gson.fromJson(success, MineResponse::class.java)
//                                    val response = new GsonManager().convert(success, MineResponse::class.java)

//                                    if (response.ans_list != null && response.ans_list.size > 0) {
//                                        baseView.mineDataSuccess(response.ans_list)
//                                    } else {
//                                        baseView.mineDataError(BaseApplication.instance().resources.getString(R.string.library_no_data_available))
//                                    }
                            } else {
                                baseView.userDataError(
                                    BaseApplication.instance().resources.getString(
                                        R.string.library_loading_failed
                                    )
                                )
                            }
                        }

                        override fun onError(error: String) {
                            LogManager.i(TAG, "error*****$error")
                            baseView.userDataError(error)
                        }
                    })
            }
        }
    }

    override fun userData(
        rxAppCompatActivity: RxAppCompatActivity,
        accessToken: String,
        bodyParams: Map<String, String>
    ) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IUserDataView) {
                RetrofitManager.instance.responseString5(rxAppCompatActivity,
                    model.userData(accessToken, bodyParams),
                    object :
                        OnCommonSingleParamCallback<String> {
                        override fun onSuccess(success: String) {
                            LogManager.i(TAG, "userData success*****$success")
                            if (!TextUtils.isEmpty(success)) {
//                                    val response = JSONObject.parseObject(success, MineResponse::class.java)
//                                    val gson = Gson()
//                                    val response = gson.fromJson(success, MineResponse::class.java)
//                                    val response = new GsonManager().convert(success, MineResponse::class.java)

//                                    if (response.ans_list != null && response.ans_list.size > 0) {
//                                        baseView.mineDataSuccess(response.ans_list)
//                                    } else {
//                                        baseView.mineDataError(BaseApplication.instance().resources.getString(R.string.library_no_data_available))
//                                    }
                            } else {
                                baseView.userDataError(
                                    BaseApplication.instance().resources.getString(
                                        R.string.library_loading_failed
                                    )
                                )
                            }
                        }

                        override fun onError(error: String) {
                            LogManager.i(TAG, "error*****$error")
                            baseView.userDataError(error)
                        }
                    })
            }
        }
    }

    override fun detachView() {
        mainScope.cancel()
        super.detachView()
    }

}