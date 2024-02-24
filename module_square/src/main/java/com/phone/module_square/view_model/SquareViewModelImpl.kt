package com.phone.module_square.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.phone.library_base.BaseApplication
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_mvvm.BaseViewModel
import com.phone.library_network.bean.State
import com.phone.library_common.bean.SubDataSquare
import com.phone.library_network.OnDownloadCallBack
import com.phone.library_network.SingleLiveData
import com.phone.library_network.bean.DownloadState
import com.phone.library_network.manager.RetrofitManager
import com.phone.library_room.Book
import com.phone.module_square.model.SquareModelImpl
import com.phone.module_square.R
import com.trello.rxlifecycle3.components.support.RxFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File

class SquareViewModelImpl : BaseViewModel(), ISquareViewModel {

    companion object {
        private val TAG: String = SquareViewModelImpl::class.java.simpleName
    }

    private val mSquareModel by lazy { SquareModelImpl() }
//    private var mJob: Job? = null

    //1.首先定义一个SingleLiveData的实例
    val liveData = SingleLiveData<State<List<SubDataSquare>>>()

    override fun squareData(rxFragment: RxFragment, currentPage: String) {
        LogManager.i(TAG, "squareData thread name*****${Thread.currentThread().name}")

//        mJob?.cancel()
//        //使用GlobalScope 单例对象直接调用launch/async开启协程
//        //在应用范围内启动一个新协程，协程的生命周期与应用程序一致。
//        //由于这样启动的协程存在启动协程的组件已被销毁但协程还存在的情况，极限情况下可能导致资源耗尽，
//        //所以Activity 销毁的时候记得要取消掉，避免内存泄漏
//        //不建议使用，尤其是在客户端这种需要频繁创建销毁组件的场景。
//        //开启GlobalScope.launch{} 或GlobalScope.async{} 方法的时候可以指定运行线程（根据指定的线程来，不指定默认是子线程）。
//        mJob =
//            GlobalScope.launch(Dispatchers.Main) {
//                val apiResponse = executeRequest { mSquareModel.squareData(currentPage) }
//
//                if (apiResponse.data != null && apiResponse.errorCode == 0) {
//                    val responseData = apiResponse.data?.datas ?: mutableListOf()
//                    if (responseData.size > 0) {
//                        liveData.value = State.SuccessState(responseData)
//                    } else {
//                        liveData.value =
//                            State.ErrorState(ResourcesManager.getString(R.string.library_no_data_available))
//                    }
//                } else {
//                    liveData.value = State.ErrorState(apiResponse.errorMsg)
//                }
//            }

        viewModelScope.launch(Dispatchers.IO) {
            //开启多个协程的并发和并行（和线程的并发和并行是一样的），因为外层协程viewModelScope.launch(Dispatchers.IO)运行在IO类型的线程池中，
            // 子协程（launch{}和async{}）内部执行的时候也没切换线程，所以是并发和并行可能同时执行的（这也取决于当前IO线程池和手机的cpu核心数，如果是单核cpu肯定是并发的，
            // 如果是多核cpu，如果cpu核心数小于子协程数，那并发和并行是同时存在的，如果cpu核心数大于子协程数，那么就是并行执行的）
            launch {
                Thread.sleep(200)
                LogManager.i(TAG, "launch io thread name*****${Thread.currentThread().name}")
            }
            launch {
                LogManager.i(TAG, "launch2 io thread name*****${Thread.currentThread().name}")
            }
            async {
                LogManager.i(TAG, "async io thread name*****${Thread.currentThread().name}")
            }
            launch {
                Thread.sleep(500)
                LogManager.i(TAG, "launch3 io thread name*****${Thread.currentThread().name}")
            }
            async {
                Thread.sleep(500)
                LogManager.i(TAG, "async2 io thread name*****${Thread.currentThread().name}")
            }
            launch {
                Thread.sleep(100)
                LogManager.i(TAG, "launch4 io thread name*****${Thread.currentThread().name}")
            }
            launch {
                Thread.sleep(100)
                LogManager.i(TAG, "launch5 io thread name*****${Thread.currentThread().name}")
            }
        }

        viewModelScope.launch {
//            val apiResponse = executeRequest { mSquareModel.squareData(currentPage) }
//
//            if (apiResponse.data != null && apiResponse.errorCode == 0) {
//                val responseData = apiResponse.data?.datas ?: mutableListOf()
//                if (responseData.isNotEmpty()) {
//                    liveData.value = State.SuccessState(responseData)
//                } else {
//                    liveData.value =
//                        State.ErrorState(ResourcesManager.getString(R.string.library_no_data_available))
//                }
//            } else {
//                liveData.value = State.ErrorState(apiResponse.errorMsg)
//            }

            val apiResponse =
                executeFlowRequest(reponseBlock = { mSquareModel.squareData(currentPage) },
                    errorBlock = { _, _2 ->
                        liveData.value = State.ErrorState(_2)
                    })
            if (apiResponse?.errorCode == 0) {
                val responseData = apiResponse.data?.datas
                if (!responseData.isNullOrEmpty()) {
                    liveData.value = State.SuccessState(responseData)
                } else {
                    liveData.value =
                        State.ErrorState(ResourcesManager.getString(R.string.library_no_data_available))
                }
            } else {
                apiResponse?.errorMsg?.let {
                    liveData.value =
                        State.ErrorState(it)
                }
            }
        }
    }

    override fun onCleared() {
//        mJob?.cancel()
        super.onCleared()
    }

}