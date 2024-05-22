package com.phone.module_square.function_menu.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.phone.library_base.BaseApplication
import com.phone.library_base.manager.LogManager
import com.phone.library_mvvm.BaseViewModel
import com.phone.library_network.OnDownloadCallBack
import com.phone.library_network.SingleLiveData
import com.phone.library_network.bean.DownloadState
import com.phone.library_network.bean.State
import com.phone.library_network.manager.RetrofitManager
import com.phone.library_room.Book
import com.phone.module_square.model.FunctionMenuModeImpl
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class FunctionMenuViewModelImpl : BaseViewModel(), IFunctionMenuViewMode {

    companion object {
        private val TAG: String = FunctionMenuViewModelImpl::class.java.simpleName
    }

    private val mFunctionMenuModel by lazy { FunctionMenuModeImpl() }

    //2.首先定义一个SingleLiveData的实例
    val downloadData = SingleLiveData<DownloadState<Int>>()

    //3.首先定义一个SingleLiveData的实例
    val insertData = MutableLiveData<State<String>>()

    //4.首先定义一个SingleLiveData的实例
    val queryData = MutableLiveData<State<List<Book>>>()

    override fun downloadFile(rxAppCompatActivity: RxAppCompatActivity) {
//        RetrofitManager.instance.downloadFile(rxFragment,
//            mFunctionMenuModel.downloadFile(),
//            BaseApplication.instance().externalCacheDir!!.absolutePath,
//            "artist_kirara_asuka.mov",
//            object : OnDownloadCallBack {
//                override fun onProgress(progress: Int, total: Long, speed: Long) {
//                    LogManager.i(TAG, "progress:$progress, speed:$speed")
//                    downloadData.postValue(DownloadState.ProgressState(progress, total, speed))
//                }
//
//                override fun onCompleted(file: File) {
//                    LogManager.i(TAG, "下载文件成功")
//                    downloadData.postValue(DownloadState.CompletedState(file))
//                }
//
//                override fun onError(e: Throwable?) {
//                    LogManager.i(TAG, "下载文件异常", e)
//                    downloadData.postValue(DownloadState.ErrorState("下载文件异常*****${e.toString()}"))
//                }
//            })


        rxAppCompatActivity.lifecycleScope.launch(Dispatchers.IO) {
            RetrofitManager.instance.downloadFile3(mFunctionMenuModel.downloadFile2(),
                BaseApplication.instance().externalCacheDir!!.absolutePath,
                "artist_kirara_asuka.mov",
                object : OnDownloadCallBack {
                    override fun onProgress(progress: Int, total: Long, speed: Long) {
                        LogManager.i(TAG, "progress:$progress, speed:$speed")
                        downloadData.postValue(DownloadState.ProgressState(progress, total, speed))
                    }

                    override fun onCompleted(file: File) {
                        LogManager.i(TAG, "下载文件成功")
                        downloadData.postValue(DownloadState.CompletedState(file))
                    }

                    override fun onError(e: Throwable?) {
                        LogManager.i(TAG, "下载文件异常", e)
                        downloadData.postValue(DownloadState.ErrorState("下载文件异常*****${e.toString()}"))
                    }
                })
        }
    }

    override fun insertBook(rxAppCompatActivity: RxAppCompatActivity, success: String) {
        rxAppCompatActivity.lifecycleScope.launch {
            val apiResponse = executeRequest {
                mFunctionMenuModel.insertBook(success)
            }

            if (apiResponse.data != null && apiResponse.errorCode == 0) {
//                val book = apiResponse.data!!
                insertData.value = State.SuccessState("插入數據庫成功")
            } else {
                insertData.value =
                    State.ErrorState("插入數據庫失敗")
//                insertData.value = State.ErrorState(apiResponse.errorMsg)
            }
        }
    }

    override fun queryBook() {
        viewModelScope.launch {
            val apiResponse = executeRequest {
                mFunctionMenuModel.queryBook()
            }

            if (apiResponse.data != null && apiResponse.errorCode == 0) {
                queryData.value = State.SuccessState(apiResponse.data!!)
            } else {
                queryData.value =
                    State.ErrorState("查詢數據庫失敗")
            }
        }
    }


}