package com.phone.common_library.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phone.common_library.common.ApiException
import com.phone.common_library.manager.LogManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/10/17 15:10
 * introduce :
 */

open class BaseViewModel : ViewModel() {

    companion object {
        private val TAG: String = BaseViewModel::class.java.simpleName
    }

    /**
     * 错误信息liveData
     */
    val errorLiveData = MutableLiveData<ApiException>()

    protected fun <T> launch(block: suspend () -> T) {
        viewModelScope.launch {
            runCatching {
                block()
            }.onFailure {
                it.printStackTrace()
                getApiException(it).apply {
                    withContext(Dispatchers.Main){
//                        toast(errorMessage)
                        //统一响应错误信息
                        errorLiveData.value = this@apply
                    }
                }
            }
        }
    }

    /**
     * 捕获异常信息
     */
    private fun getApiException(e: Throwable): ApiException {
        return when (e) {
            is UnknownHostException -> {
                ApiException("网络异常", -100)
            }
            is JSONException -> {//|| e is JsonParseException
                ApiException("数据异常", -100)
            }
            is SocketTimeoutException -> {
                ApiException("连接超时", -100)
            }
            is ConnectException -> {
                ApiException("连接错误", -100)
            }
            is HttpException -> {
                ApiException("http code ${e.code()}", -100)
            }
            is ApiException -> {
                e
            }
            /**
             * 如果协程还在运行，个别机型退出当前界面时，viewModel会通过抛出CancellationException，
             * 强行结束协程，与java中InterruptException类似，所以不必理会,只需将toast隐藏即可
             */
            is CancellationException -> {
                ApiException("", -10)
            }
            else -> {
                ApiException("未知错误", -100)
            }
        }
    }

    override fun onCleared() {
        LogManager.i(TAG, "onCleared")
        super.onCleared()
    }
}