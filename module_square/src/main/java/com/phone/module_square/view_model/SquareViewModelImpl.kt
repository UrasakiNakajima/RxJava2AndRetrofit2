package com.phone.module_square.view_model

import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.phone.library_common.base.BaseViewModel
import com.phone.library_common.bean.SubDataSquare
import com.phone.library_common.bean.SquareBean
import com.phone.library_common.callback.OnCommonSingleParamCallback
import com.phone.library_common.manager.*
import com.phone.module_square.model.SquareModelImpl
import com.phone.module_square.R
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SquareViewModelImpl() : BaseViewModel(), ISquareViewModel {

    companion object {
        private val TAG: String = SquareViewModelImpl::class.java.simpleName
    }

    private var model = SquareModelImpl()
    private val jobList = mutableListOf<Job>()

    //1.首先定义两个SingleLiveData的实例
    val dataxRxFragmentSuccess = SingleLiveData<MutableList<SubDataSquare>>()
    val dataxRxFragmentError = SingleLiveData<String>()

    //1.首先定义两个SingleLiveData的实例
    val dataxRxActivitySuccess = SingleLiveData<MutableList<SubDataSquare>>()
    val dataxRxActivityError = SingleLiveData<String>()

    override fun squareData(rxFragment: RxFragment, currentPage: String) {
        val job = GlobalScope.launch {
            //开启GlobalScope.launch这种协程之后就是在线程执行了
            val subDataSquareList = squareDataSuspend(currentPage)

            withContext(Dispatchers.Main) {
                //然后切换到主线程
                dataxRxFragmentSuccess.value = subDataSquareList
            }
        }
        jobList.add(job)

//            RetrofitManager.getInstance()
//                .responseStringRxFragmentBindToLifecycle(rxFragment,
//                    model.squareData(currentPage),
//                    object : OnCommonSingleParamCallback<String> {
//                        override fun onSuccess(success: String) {
//                            LogManager.i(TAG, "success*****$success")
//                            if (!TextUtils.isEmpty(success)) {
//                                val response: SquareBean =
//                                    new GsonManager().convert(success, SquareBean::class.java)
//                                if (response.data?.datas != null && response.data!!.datas!!.size > 0) {
////                                LogManager.i(TAG, "response*****${response.toString()}")
//
//
//                                    dataxRxFragmentSuccess.value = response.data!!.datas
//                                } else {
//                                    dataxRxFragmentError.value =
//                                        ResourcesManager.getString(R.string.no_data_available)
//                                }
//                            } else {
//                                dataxRxFragmentError.value =
//                                    ResourcesManager.getString(R.string.loading_failed)
//                            }
//                        }
//
//                        override fun onError(error: String) {
//                            LogManager.i(TAG, "error*****$error")
//                            dataxRxFragmentError.value = error
//                        }
//                    })
//        }
    }

    /**
     * 在协程或者挂起函数里调用，挂起函数里必须要切换到线程（这里切换到IO线程）
     */
    suspend fun squareDataSuspend(currentPage: String): MutableList<SubDataSquare> {
        val subDataSquareList = mutableListOf<SubDataSquare>()
        LogManager.i(TAG, "squareDataSuspend thread name*****${Thread.currentThread().name}")

        withContext(Dispatchers.IO) {
            val success = model.squareData2(currentPage).execute().body()?.string()
            LogManager.i(TAG, "success*****$success")
            if (!TextUtils.isEmpty(success)) {
                val response =
                    GsonManager().convert(success ?: "", SquareBean::class.java)
                LogManager.i(TAG, "withContext thread name*****${Thread.currentThread().name}")

                val responseData = response.data?.datas ?: mutableListOf()
                if (responseData.size > 0) {
                    subDataSquareList.clear()
                    subDataSquareList.addAll(responseData)
                }
            }
        }

        return subDataSquareList
    }

    override fun squareData2(
        rxAppCompatActivity: RxAppCompatActivity,
        currentPage: String
    ) {
        RetrofitManager.instance()
            .responseString5(rxAppCompatActivity,
                model.squareData(currentPage),
                object : OnCommonSingleParamCallback<String> {
                    override fun onSuccess(success: String) {
                        LogManager.i(TAG, "success*****$success")
                        if (!TextUtils.isEmpty(success)) {
                            val response =
                                GsonManager()
                                    .convert(success, SquareBean::class.java)
                            val responseData = response.data?.datas ?: mutableListOf()
                            if (responseData.size > 0) {
                                dataxRxActivitySuccess.value = responseData
                            } else {
                                dataxRxActivityError.value =
                                    ResourcesManager.getString(R.string.no_data_available)
                            }
                        } else {
                            dataxRxActivityError.value =
                                ResourcesManager.getString(R.string.loading_failed)
                        }
                    }

                    override fun onError(error: String) {
                        LogManager.i(TAG, "error*****$error")
                        dataxRxActivityError.value = error
                    }
                })
    }

    override fun onCleared() {
        super.onCleared()
        for (i in 0..jobList.size - 1) {
            if (jobList.get(i).isActive) {
                jobList.get(i).cancel()
            }
        }
    }

}