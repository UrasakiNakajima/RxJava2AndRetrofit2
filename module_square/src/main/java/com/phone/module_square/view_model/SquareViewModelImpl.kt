package com.phone.module_square.view_model

import android.text.TextUtils
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

    private var mModel = SquareModelImpl()
    private var mJob: Job? = null

    //1.首先定义两个SingleLiveData的实例
    val dataxRxFragmentSuccess = SingleLiveData<List<SubDataSquare>>()
    val dataxRxFragmentError = SingleLiveData<String>()

    //1.首先定义两个SingleLiveData的实例
    val dataxRxActivitySuccess = SingleLiveData<List<SubDataSquare>>()
    val dataxRxActivityError = SingleLiveData<String>()

    override fun squareData(rxFragment: RxFragment, currentPage: String) {
        LogManager.i(TAG, "squareData thread name*****${Thread.currentThread().name}")

        mJob?.cancel()
        mJob = GlobalScope.launch(Dispatchers.Main) {
            //开启GlobalScope.launch这种协程之后就是在MAIN线程执行了（根据指定的线程来）
            val apiResponse = execute { mModel.squareData(currentPage) }

            if (apiResponse.data != null && apiResponse.errorCode == 0) {
                val responseData = apiResponse.data?.datas ?: mutableListOf()
                if (responseData.size > 0) {
                    dataxRxFragmentSuccess.value = responseData
                } else {
                    dataxRxFragmentError.value =
                        ResourcesManager.getString(R.string.no_data_available)
                }
            } else {
                dataxRxFragmentError.value = apiResponse.errorMsg
            }
        }
    }

    override fun squareData2(
        rxAppCompatActivity: RxAppCompatActivity, currentPage: String
    ) {
        RetrofitManager.instance().responseString5(rxAppCompatActivity,
            mModel.squareData2(currentPage),
            object : OnCommonSingleParamCallback<String> {
                override fun onSuccess(success: String) {
                    LogManager.i(TAG, "success*****$success")
                    if (!TextUtils.isEmpty(success)) {
                        val response = GsonManager().convert(success, SquareBean::class.java)
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
        mJob?.cancel()
    }

}