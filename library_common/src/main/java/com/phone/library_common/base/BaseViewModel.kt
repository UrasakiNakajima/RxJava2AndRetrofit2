package com.phone.library_common.base

import androidx.lifecycle.ViewModel
import com.phone.library_common.bean.ApiResponse
import com.phone.library_common.common.ApiException
import com.phone.library_common.manager.LogManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
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
     * 在协程或者挂起函数里调用，挂起函数里必须要切换到线程（这里切换到IO线程）
     */
    protected suspend fun <T> executeRequest(block: suspend () -> ApiResponse<T>): ApiResponse<T> =
        withContext(Dispatchers.IO) {

//            //协程内部只开启多个launch是并行的
//            launch {
//                delay(2000)
//                LogManager.i(TAG, "launch delay(2000)")
//            }
//            launch {
//                delay(1000)
//                LogManager.i(TAG, "launch delay(1000)")
//            }

            var response: ApiResponse<T>
            runCatching {
                block()
            }.onSuccess {
                response = it
            }.onFailure {
                it.printStackTrace()
                response = ApiResponse<T>()
                val apiException = getApiException(it)
                response.errorCode = apiException.errorCode
                response.errorMsg = apiException.errorMessage
                response.error = apiException
            }.getOrDefault(ApiResponse<T>())
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
                ApiException("取消请求异常", -10)
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