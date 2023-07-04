package com.phone.module_mine.presenter

import android.text.TextUtils
import com.google.gson.reflect.TypeToken
import com.phone.library_common.BaseApplication
import com.phone.library_common.base.BasePresenter
import com.phone.library_common.base.IBaseView
import com.phone.library_common.bean.ApiResponse3
import com.phone.library_common.bean.MineResult
import com.phone.library_common.callback.OnCommonSingleParamCallback
import com.phone.library_common.manager.GsonManager
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.ResourcesManager
import com.phone.library_common.manager.RetrofitManager
import com.phone.module_mine.R
import com.phone.module_mine.model.MineModelImpl
import com.phone.module_mine.view.IMineView
import com.phone.module_mine.view.IUserDataView
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MinePresenterImpl(baseView: IBaseView) : BasePresenter<IBaseView>(), IMinePresenter {

    companion object {
        private val TAG = MinePresenterImpl::class.java.simpleName
    }

    //    private IResourceChildView feedbackView;//P需要与V 交互，所以需要持有V的引用
    private var model = MineModelImpl()

    //创建一个MainScope 对象，默认运行在UI线程
    //Activity 销毁的时候记得要取消掉，避免内存泄漏
    //开启GlobalScope.launch{} 或GlobalScope.async{} 方法的时候可以指定运行线程（根据指定的线程来，不指定是创建的时候的线程）。
    private val mainScope = MainScope()

    init {
        attachView(baseView)
    }

    override fun mineData(rxFragment: RxFragment, bodyParams: Map<String, String>) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IMineView) {
                mainScope.launch {

//                    LogManager.i(TAG, "mainScope.launch")
//                    //协程内部开启多个withContext、launch{}和async{}的时候，有一个规律，执行第一个withContext的时候，就会串行执行，
//                    //然后执行完了第一个withContext，再并行执行第一个launch和第二个withContext，执行第二个withContext
//                    //的时候，又会串行执行，然后执行完了第二个withContext，再并行执行第一个async和第三个withContext，
//                    //执行第三个withContext的时候，又会串行执行，然后执行完了第三个withContext，再执行最后一个launch
//                    withContext(Dispatchers.IO) {//第一个withContext
//                        delay(3000)
//                        LogManager.i(TAG, "withContext delay(3000)")
//                    }
//                    launch {//第一个launch
//                        delay(1000)
//                        LogManager.i(TAG, "launch delay(1000)")
//                    }
//                    withContext(Dispatchers.IO) {//第二个withContext
//                        delay(1000)
//                        LogManager.i(TAG, "withContext delay(1000)")
//                    }
//                    async {//第一个async
//                        delay(2000)
//                        LogManager.i(TAG, "async delay(2000)")
//                    }
//                    withContext(Dispatchers.IO) {//第三个withContext
//                        delay(2000)
//                        LogManager.i(TAG, "withContext delay(2000)")
//                    }
//                    launch {//第二个launch
//                        delay(1000)
//                        LogManager.i(TAG, "launch delay(1000)")
//                    }

                    val apiResponse = executeRequest2 { model.mineData(bodyParams) }
                    if (apiResponse.result != null && apiResponse.error_code == 0) {
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
                    } else {
                        baseView.mineDataError(
                            apiResponse.reason ?: BaseApplication.instance().resources.getString(
                                R.string.library_loading_failed
                            )
                        )
                    }
                }
            }
        }
    }

    override fun mineData2(
        rxAppCompatActivity: RxAppCompatActivity, bodyParams: Map<String, String>
    ) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IMineView) {
                RetrofitManager.instance().responseString5(rxAppCompatActivity,
                    model.mineData2(bodyParams),
                    object : OnCommonSingleParamCallback<String> {
                        override fun onSuccess(success: String) {
                            LogManager.i(TAG, "mineData success*****$success")
                            if (!TextUtils.isEmpty(success)) {
//                                    val response = JSONObject.parseObject(success, MineResponse::class.java)
//                                    val gson = Gson()
//                                    val response = gson.fromJson(success, MineResponse::class.java)
                                val type2 =
                                    object : TypeToken<ApiResponse3<MineResult>>() {}.type
                                val response: ApiResponse3<MineResult> =
                                    GsonManager().fromJson(success ?: "", type2)
                                if (response.result != null && response.error_code == 0) {
                                    val list = response.result?.data ?: mutableListOf()
                                    baseView.mineDataSuccess(list)
                                } else {
                                    baseView.mineDataError(
                                        response.reason
                                            ?: BaseApplication.instance().resources.getString(
                                                R.string.library_loading_failed
                                            )
                                    )
                                }
                            } else {
                                baseView.mineDataError(
                                    BaseApplication.instance().resources.getString(
                                        R.string.library_loading_failed
                                    )
                                )
                            }
                        }

                        override fun onError(error: String) {
                            LogManager.i(TAG, "error*****$error")
                            baseView.mineDataError(error)
                        }
                    })
            }
        }
    }

    override fun userData(
        rxAppCompatActivity: RxAppCompatActivity, bodyParams: Map<String, String>
    ) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IUserDataView) {
                RetrofitManager.instance().responseString5(rxAppCompatActivity,
                    model.userData(bodyParams),
                    object : OnCommonSingleParamCallback<String> {
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
                RetrofitManager.instance().responseString5(rxAppCompatActivity,
                    model.userData(accessToken, bodyParams),
                    object : OnCommonSingleParamCallback<String> {
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