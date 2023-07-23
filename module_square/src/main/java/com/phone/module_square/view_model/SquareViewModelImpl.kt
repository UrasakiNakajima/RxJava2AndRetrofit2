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
import com.phone.library_room.AppRoomDataBase
import com.phone.library_room.Book
import com.phone.module_square.model.SquareModelImpl
import com.phone.module_square.R
import com.trello.rxlifecycle3.components.support.RxFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class SquareViewModelImpl : BaseViewModel(), ISquareViewModel {

    companion object {
        private val TAG: String = SquareViewModelImpl::class.java.simpleName
    }

    private var mSquareModel = SquareModelImpl()
//    private var mJob: Job? = null

    //1.首先定义一个SingleLiveData的实例
    val liveData = SingleLiveData<State<List<SubDataSquare>>>()

    //2.首先定义一个SingleLiveData的实例
    val downloadData = SingleLiveData<DownloadState<Int>>()

    //3.首先定义一个SingleLiveData的实例
    val insertData = MutableLiveData<State<Book>>()

    //4.首先定义一个SingleLiveData的实例
    val queryData = MutableLiveData<State<List<Book>>>()

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

        viewModelScope.launch {
            val apiResponse = executeRequest { mSquareModel.squareData(currentPage) }

            if (apiResponse.data != null && apiResponse.errorCode == 0) {
                val responseData = apiResponse.data?.datas ?: mutableListOf()
                if (responseData.size > 0) {
                    liveData.value = State.SuccessState(responseData)
                } else {
                    liveData.value =
                        State.ErrorState(ResourcesManager.getString(R.string.library_no_data_available))
                }
            } else {
                liveData.value = State.ErrorState(apiResponse.errorMsg)
            }
        }
    }

    override fun downloadFile(rxFragment: RxFragment) {
//        RetrofitManager.instance().downloadFile(rxFragment,
//            mSquareModel.downloadFile(),
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


        rxFragment.lifecycleScope.launch(Dispatchers.IO) {
            RetrofitManager.instance().downloadFile3(mSquareModel.downloadFile2(),
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

    override fun insertBook(rxFragment: RxFragment, success: String) {
        rxFragment.lifecycleScope.launch {
            val book = executeInsertBook {
                val appRoomDataBase = AppRoomDataBase.instance()
                val book = Book()
                val strArr = success.split(".")
                book.bookName = "書名：${strArr[0]}"
                book.anchor = "作者：${strArr[1]}"
                book.briefIntroduction = "簡介：${strArr[2]}"
                appRoomDataBase.bookDao().insert(book)

//                val book2 = Book()
//                book2.bookName = "EnglishXC2"
//                book2.anchor = "rommelXC2"
//                appRoomDataBase.bookDao().insert(book2)
//                val bookList = appRoomDataBase.bookDao().queryAll()
//                for (i in 0..bookList.size - 1) {
//                    LogManager.i(TAG, "book*****" + bookList.get(i).bookName)
//                }
//                AppRoomDataBase.decrypt(
//                    AppRoomDataBase.DATABASE_DECRYPT_NAME,
//                    AppRoomDataBase.DATABASE_ENCRYPT_NAME,
//                    AppRoomDataBase.DATABASE_DECRYPT_KEY
//                )
                book
            }

            if (book.isSuccess) {
                insertData.value = State.SuccessState(book)
            } else {
                insertData.value = State.ErrorState(book.message)
            }
        }
    }

    private suspend fun executeInsertBook(block: suspend () -> Book): Book =
        withContext(Dispatchers.IO) {
            var book = Book()
            runCatching {
                block.invoke()
            }.onSuccess {
                book = it
                book.isSuccess = true
                book.message = "插入數據庫成功"
            }.onFailure {
                it.printStackTrace()
                book.isSuccess = false
                book.message = "插入數據庫失敗"
            }.getOrDefault(book)
        }

    override fun queryBook() {
        viewModelScope.launch {
            val bookList = executeQueryBook {
                val appRoomDataBase = AppRoomDataBase.instance()
                appRoomDataBase.bookDao().queryAll()
            }
            queryData.value = State.SuccessState(bookList)
        }
    }

    private suspend fun executeQueryBook(block: () -> List<Book>): List<Book> =
        withContext(Dispatchers.IO) {
            var bookList = mutableListOf<Book>()
            runCatching {
                block.invoke()
            }.onSuccess {
                bookList = it as MutableList<Book>
            }.onFailure {
                it.printStackTrace()
            }.getOrDefault(bookList)
        }

    override fun onCleared() {
//        mJob?.cancel()
        super.onCleared()
    }

}