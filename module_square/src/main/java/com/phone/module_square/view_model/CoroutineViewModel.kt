package com.phone.module_square.view_model

import androidx.lifecycle.viewModelScope
import com.phone.library_common.base.BaseViewModel
import com.phone.library_common.base.State
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.SingleLiveData
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 这个CoroutineViewModel只是用来在ViewModel中启动一个协程，没有其他用处
 */
class CoroutineViewModel : BaseViewModel() {

    companion object {
        private val TAG = CoroutineViewModel::class.java.simpleName
    }

    val executeSuccess = SingleLiveData<State<String>>()

    fun startViewModelScope() {
        //方法五：在Android MVVM架构的viewModel中启动一个新协程（推荐使用），该协程默认运行在UI线程，
        //协程和该组件生命周期绑定，组件销毁时，协程一并销毁，从而实现安全可靠地协程调用。
        //调用viewModelScope.launch{} 或 viewModelScope.async{} 方法的时候可以指定运行线程（根据指定的线程来，不指定默认是UI线程）。
        viewModelScope.launch {
            LogManager.i(TAG, "viewModelScope.launch thread name*****" + Thread.currentThread().name)
            delay(2000)

            executeSuccess.value = State.SuccessState("success")
        }

//        viewModelScope.async {
//            LogManager.i(TAG, "viewModelScope.async thread name*****" + Thread.currentThread().name)
//            delay(2000)
//
//            executeSuccess.value = State.SuccessState("success")
//        }
    }

}