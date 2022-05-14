package com.phone.square_module.view_model

import android.text.TextUtils
import com.phone.common_library.BaseApplication
import com.phone.common_library.base.BaseViewModel
import com.phone.common_library.callback.OnCommonSingleParamCallback
import com.phone.common_library.manager.GsonManager
import com.phone.common_library.manager.LogManager
import com.phone.common_library.manager.RetrofitManager
import com.phone.common_library.manager.SingleLiveData
import com.phone.square_module.bean.DataX
import com.phone.square_module.bean.SquareBean
import com.phone.square_module.model.SquareModelImpl
import com.phone.square_module.R
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment

class SquareViewModelImpl() : BaseViewModel(), ISquareViewModel {

    companion object {
        private val TAG: String = SquareViewModelImpl::class.java.simpleName
    }

    private var model: SquareModelImpl = SquareModelImpl()

    //1.首先定义两个SingleLiveData的实例
    private val dataxSuccess: SingleLiveData<List<DataX>> = SingleLiveData()
    private val dataxError: SingleLiveData<String> = SingleLiveData()

    //1.首先定义两个SingleLiveData的实例
    private val dataxDetailsSuccess: SingleLiveData<List<DataX>> = SingleLiveData()
    private val dataxDetailsError: SingleLiveData<String> = SingleLiveData()

    override fun squareData(rxFragment: RxFragment, currentPage: String) {
        RetrofitManager.getInstance()
            .responseStringRxFragment(rxFragment,
                model.squareData(currentPage),
                object : OnCommonSingleParamCallback<String> {
                    override fun onSuccess(success: String) {
                        LogManager.i(TAG, "success*****$success")
                        if (!TextUtils.isEmpty(success)) {
                            val response: SquareBean =
                                GsonManager.getInstance().convert(success, SquareBean::class.java)
                            if (response.data?.datas != null && response.data!!.datas!!.size > 0) {
//                                LogManager.i(TAG, "response*****${response.toString()}")


                                dataxSuccess.value = response.data!!.datas
                            } else {
                                dataxError.value =
                                    BaseApplication.getInstance().resources.getString(R.string.no_data_available)
                            }
                        } else {
                            dataxError.value =
                                BaseApplication.getInstance().resources.getString(R.string.loading_failed)
                        }
                    }

                    override fun onError(error: String) {
                        LogManager.i(TAG, "error*****$error")
                        dataxError.value = error
                    }
                })
    }

    override fun getDataxSuccess(): SingleLiveData<List<DataX>> {
        return dataxSuccess
    }

    override fun getDataxError(): SingleLiveData<String> {
        return dataxError
    }

    override fun squareDetails(rxAppCompatActivity: RxAppCompatActivity, currentPage: String) {
        RetrofitManager.getInstance()
            .responseStringRxAppActivity(rxAppCompatActivity,
                model.squareDetails(currentPage),
                object : OnCommonSingleParamCallback<String> {
                    override fun onSuccess(success: String) {
                        LogManager.i(TAG, "success*****$success")
                        if (!TextUtils.isEmpty(success)) {
                            val response: SquareBean =
                                GsonManager.getInstance().convert(success, SquareBean::class.java)
                            if (response.data?.datas != null && response.data!!.datas!!.size > 0) {
//                                LogManager.i(TAG, "response*****${response.toString()}")


                                dataxDetailsSuccess.value = response.data!!.datas
                            } else {
                                dataxDetailsError.value =
                                    BaseApplication.getInstance().resources.getString(R.string.no_data_available)
                            }
                        } else {
                            dataxDetailsError.value =
                                BaseApplication.getInstance().resources.getString(R.string.loading_failed)
                        }
                    }

                    override fun onError(error: String) {
                        LogManager.i(TAG, "error*****$error")
                        dataxDetailsError.value = error
                    }
                })
    }

    override fun getDataxDetailsSuccess(): SingleLiveData<List<DataX>> {
        return dataxDetailsSuccess
    }

    override fun getDataxDetailsError(): SingleLiveData<String> {
        return dataxDetailsError
    }

}