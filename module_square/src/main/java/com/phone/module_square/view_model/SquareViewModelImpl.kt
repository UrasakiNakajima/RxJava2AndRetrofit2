package com.phone.module_square.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.phone.library_base.BaseApplication
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_mvvm.BaseViewModel
import com.phone.library_network.bean.State
import com.phone.library_common.bean.SubDataSquare
import com.phone.library_common.callback.OnDownloadListener
import com.phone.library_network.DownloadProgressHandler
import com.phone.library_network.OnDownloadCallBack
import com.phone.library_network.SingleLiveData
import com.phone.library_network.bean.DownloadState
import com.phone.library_network.manager.RetrofitManager
import com.phone.module_square.model.SquareModelImpl
import com.phone.module_square.R
import com.trello.rxlifecycle3.components.support.RxFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

class SquareViewModelImpl : BaseViewModel(), ISquareViewModel {

    companion object {
        private val TAG: String = SquareViewModelImpl::class.java.simpleName
    }

    private var mModel = SquareModelImpl()
    private var mJob: Job? = null

    //1.首先定义两个SingleLiveData的实例
    val dataxRxFragment = MutableLiveData<State<List<SubDataSquare>>>()

    //1.首先定义两个SingleLiveData的实例
    val downloadData = SingleLiveData<DownloadState<Int>>()

    override fun squareData(rxFragment: RxFragment, currentPage: String) {
        LogManager.i(TAG, "squareData thread name*****${Thread.currentThread().name}")

        mJob?.cancel()

        //使用GlobalScope 单例对象直接调用launch/async开启协程
        //在应用范围内启动一个新协程，协程的生命周期与应用程序一致。
        //由于这样启动的协程存在启动协程的组件已被销毁但协程还存在的情况，极限情况下可能导致资源耗尽，
        //所以Activity 销毁的时候记得要取消掉，避免内存泄漏
        //不建议使用，尤其是在客户端这种需要频繁创建销毁组件的场景。
        //开启GlobalScope.launch{} 或GlobalScope.async{} 方法的时候可以指定运行线程（根据指定的线程来，不指定默认是子线程）。
        mJob =
            GlobalScope.launch(Dispatchers.Main) {

//                //协程内部只开启多个async是并行的
//                async {
//                    delay(2000)
//                    LogManager.i(TAG, "async delay(2000)")
//                    LogManager.i(TAG, "async thread name*****" + Thread.currentThread().name)
//                }
//                async {
//                    delay(1000)
//                    LogManager.i(TAG, "async delay(1000)")
//                    LogManager.i(TAG, "async2 thread name*****" + Thread.currentThread().name)
//                }


                val apiResponse = executeRequest { mModel.squareData(currentPage) }

                if (apiResponse.data != null && apiResponse.errorCode == 0) {
                    val responseData = apiResponse.data?.datas ?: mutableListOf()
                    if (responseData.size > 0) {
                        dataxRxFragment.value = State.SuccessState(responseData)
                    } else {
                        dataxRxFragment.value =
                            State.ErrorState(ResourcesManager.getString(R.string.library_no_data_available))
                    }
                } else {
                    dataxRxFragment.value = State.ErrorState(apiResponse.errorMsg)
                }
            }
    }

    override fun downloadFile(rxFragment: RxFragment) {
        RetrofitManager.instance().downloadFile(rxFragment,
            mModel.downloadFile(),
            BaseApplication.instance().externalCacheDir!!.absolutePath,
            "artist_kirara_asuka.mov",
            object : DownloadProgressHandler() {
                override fun onProgress(progress: Int, total: Long, speed: Long) {
                    LogManager.i(TAG, "progress:$progress, speed:$speed")
                    downloadData.value = DownloadState.ProgressState(progress, total, speed)
                }

                override fun onCompleted(file: File?) {
                    LogManager.i(TAG, "下载文件成功")
                    downloadData.value = DownloadState.CompletedState(file!!)
                }

                override fun onError(e: Throwable?) {
                    LogManager.i(TAG, "下载文件异常", e)
                    downloadData.value =
                        DownloadState.ErrorState("下载文件异常*****${e.toString()}")
                }
            }
        )


//        RetrofitManager.instance().downloadFile2(rxFragment,
//            mModel.downloadFile(),
//            BaseApplication.instance().externalCacheDir!!.absolutePath,
//            "artist_kirara_asuka.mov",
//            object : OnDownloadCallBack {
//                override fun onProgress(progress: Int, total: Long, speed: Long) {
//                    LogManager.i(TAG, "progress:$progress, speed:$speed")
//                    downloadData.value = DownloadState.ProgressState(progress, total, speed)
//                }
//
//                override fun onCompleted(file: File?) {
//                    LogManager.i(TAG, "下载文件成功")
//                    downloadData.value = DownloadState.CompletedState(file!!)
//                }
//
//                override fun onError(e: Throwable?) {
//                    LogManager.i(TAG, "下载文件异常", e)
//                    downloadData.value =
//                        DownloadState.ErrorState("下载文件异常*****${e.toString()}")
//                }
//            })
    }

    override fun onCleared() {
        mJob?.cancel()
        super.onCleared()
    }

}