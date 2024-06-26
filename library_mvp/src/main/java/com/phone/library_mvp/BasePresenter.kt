package com.phone.library_mvp

import com.phone.library_network.bean.ApiResponse2
import com.phone.library_network.bean.ApiResponse3
import com.phone.library_network.bean.ApiException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.json.JSONException
import retrofit2.HttpException
import java.lang.ref.WeakReference
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/7 10:55
 * introduce :
 */

open class BasePresenter<T> {

    private val TAG = "BasePresenter"
    private var modelView: WeakReference<T?>? = null

    protected fun attachView(view: T) {
        modelView = WeakReference(view)
    }

    protected fun obtainView(): T? {
        return if (isAttach()) modelView?.get() else null
    }

    private fun isAttach(): Boolean {
        return modelView != null && modelView?.get() != null
    }

    open fun detachView() {
        if (isAttach()) {
            modelView?.clear()
            modelView = null
        }
    }

    /**
     * 在协程或者挂起函数里调用，挂起函数里必须要切换到线程（这里切换到IO线程）
     */
    protected suspend fun <T> executeRequest(block: suspend () -> ApiResponse2<T>): ApiResponse2<T> =
        withContext(Dispatchers.IO) {
            var response = ApiResponse2<T>()
            runCatching {
                block()
            }.onSuccess {
                response = it
            }.onFailure {
                it.printStackTrace()
                val apiException = getApiException(it)
                response.error_code = apiException.errorCode
                response.reason = apiException.errorMessage
                response.error = apiException
            }.getOrDefault(response)
        }

    /**
     * 在协程或者挂起函数里调用，挂起函数里必须要切换到线程（这里切换到IO线程）
     */
    protected suspend fun <T> executeFlowRequest(
        reponseBlock: suspend () -> ApiResponse2<T>,
        errorBlock: ((Int, String) -> Unit)? = null
    ): ApiResponse2<T>? {
        var response: ApiResponse2<T>? = null
        flow {
            emit(reponseBlock())
        }.catch {
            it.printStackTrace()
            val apiException = getApiException(it)
//            response?.error_code = apiException.errorCode
//            response?.reason = apiException.errorMessage
//            response?.error = apiException

            //这里catch之后还是运行在IO线程，需要切成UI线程
            withContext(Dispatchers.Main) {
                errorBlock?.let {
                    it(apiException.errorCode, apiException.errorMessage)
                }
            }
        }.flowOn(Dispatchers.IO).collect {
            response = it
        }
        return response
    }

    /**
     * 在协程或者挂起函数里调用，挂起函数里必须要切换到线程（这里切换到IO线程）
     */
    protected suspend fun <T> executeRequest2(block: suspend () -> ApiResponse3<T>): ApiResponse3<T> =
        withContext(Dispatchers.IO) {
            var response = ApiResponse3<T>()
            runCatching {
                block()
            }.onSuccess {
                response = it
            }.onFailure {
                it.printStackTrace()
                val apiException = getApiException(it)
                response.error_code = apiException.errorCode
                response.reason = apiException.errorMessage
                response.error = apiException
            }.getOrDefault(response)
        }

    /**
     * 在协程或者挂起函数里调用，挂起函数里必须要切换到线程（这里切换到IO线程）
     */
    protected suspend fun <T> executeFlowRequest2(
        reponseBlock: suspend () -> ApiResponse3<T>,
        errorBlock: ((Int, String) -> Unit)? = null
    ): ApiResponse3<T>? {
        var response: ApiResponse3<T>? = null
        flow {
            emit(reponseBlock())
        }.catch {
            val apiException = getApiException(it)
//            response.error_code = apiException.errorCode
//            response.reason = apiException.errorMessage
//            response.error = apiException
            errorBlock?.let {
                it(apiException.errorCode, apiException.errorMessage)
            }
        }.flowOn(Dispatchers.IO).collect {
            response = it
        }
        return response
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

}