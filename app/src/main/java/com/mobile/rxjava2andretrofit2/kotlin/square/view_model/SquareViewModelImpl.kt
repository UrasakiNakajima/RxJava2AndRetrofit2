package com.mobile.rxjava2andretrofit2.kotlin.square.view_model

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.mobile.rxjava2andretrofit2.MineApplication
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.base.BaseViewModel
import com.mobile.rxjava2andretrofit2.callback.OnCommonSingleParamCallback
import com.mobile.rxjava2andretrofit2.kotlin.square.bean.DataX
import com.mobile.rxjava2andretrofit2.kotlin.square.bean.SquareBean
import com.mobile.rxjava2andretrofit2.kotlin.square.model.SquareModelImpl
import com.mobile.rxjava2andretrofit2.manager.GsonManager
import com.mobile.rxjava2andretrofit2.manager.LogManager
import com.mobile.rxjava2andretrofit2.manager.RetrofitManager

class SquareViewModelImpl() : BaseViewModel() {

    companion object {
        private val TAG: String = "ProjectViewModelImpl"
    }

    private var model: SquareModelImpl = SquareModelImpl()
    //1.首先定义两个MutableLiveData的实例
    private val dataxSuccess: MutableLiveData<List<DataX>> = MutableLiveData()
    private val dataxError: MutableLiveData<String> = MutableLiveData()

    fun squareData(currentPage: String) {
        disposable = RetrofitManager
                .getInstance()
                .responseString(model.squareData(currentPage), object : OnCommonSingleParamCallback<String> {
                    override fun onSuccess(success: String) {
                        LogManager.i(TAG, "success*****$success")
                        if (!TextUtils.isEmpty(success)) {
                            val response: SquareBean = GsonManager.getInstance().convert(success, SquareBean::class.java)
                            if (response.data?.datas != null && response.data!!.datas!!.size > 0) {
//                                LogManager.i(TAG, "response*****${response.toString()}")


                                dataxSuccess.value = response.data!!.datas
                            } else {
                                dataxError.value = MineApplication.getInstance().resources.getString(R.string.no_data_available)
                            }
                        } else {
                            dataxError.value = MineApplication.getInstance().resources.getString(R.string.loading_failed)
                        }
                    }

                    override fun onError(error: String) {
                        LogManager.i(TAG, "error*****$error")
                        dataxError.value = error
                    }
                })

        disposableList.add(disposable!!)
    }

    fun getDataxSuccess(): MutableLiveData<List<DataX>> {
        return dataxSuccess
    }

    fun getDataxError(): MutableLiveData<String> {
        return dataxError
    }

}