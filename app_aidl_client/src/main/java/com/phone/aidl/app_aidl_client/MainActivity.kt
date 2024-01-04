package com.phone.aidl.app_aidl_client

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.widget.Button
import com.phone.aidl.aidl.Book
import com.phone.aidl.aidl.BookController
import com.phone.library_base.base.BaseRxAppActivity
import com.phone.library_base.manager.LogManager

class MainActivity : BaseRxAppActivity() {

    private val TAG = "MainActivity"
    private var bookController: BookController? = null
    private var connected = false
    private var bookList: List<Book>? = null

    private var mBtnBindService: Button? = null
    private var mBtnGetBookList: Button? = null
    private var mBtnAddBook: Button? = null
    private var mBtnGetBookSize: Button? = null
    private var mBtnGetFirstBookName: Button? = null

    override fun initLayoutId(): Int = R.layout.activity_main

    override fun initData() {

    }

    override fun initViews() {
        mBtnBindService = findViewById(R.id.btn_bind_service)
        mBtnGetBookList = findViewById(R.id.btn_get_book_list)
        mBtnAddBook = findViewById(R.id.btn_add)
        mBtnGetBookSize = findViewById(R.id.btn_get_book_size)
        mBtnGetFirstBookName = findViewById(R.id.btn_first_book_name)
        mBtnBindService?.setOnClickListener {
            val intent = Intent("com.phone.aidl.app_aidl_server.AidlService")
            intent.setClassName("com.phone.aidl.app_aidl_server","com.phone.aidl.app_aidl_server.AidlService")
            bindService(intent, serviceConnection, BIND_AUTO_CREATE)
        }
        mBtnGetBookList?.setOnClickListener {
            if (connected) {
                try {
                    bookList = bookController?.bookList
                    bookList?.let {
                        for (i in it.indices) {
                            LogManager.i(TAG, "name=${it.get(i).name}")
                        }
                    }
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
        }
        mBtnAddBook?.setOnClickListener {
            if (connected) {
                val book = Book("新书")
                try {
                    bookController?.addBook(book)
                    LogManager.i(TAG, "向服务器添加了一本新书===${book.name}")
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
        }
        mBtnGetBookSize?.setOnClickListener {
            if (connected) {
                try {
                    var size = 0
                    bookController?.int?.let {
                        size = it
                        LogManager.i(TAG, "共有${size}本书")
                    }

                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
        }
        mBtnGetFirstBookName?.setOnClickListener {
            if (connected) {
                try {
                    val name = bookController?.string
                    LogManager.i(TAG, "第一本书的书名是：$name")
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun initLoadData() {

    }

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            bookController = BookController.Stub.asInterface(service)
            connected = true
            LogManager.i(
                TAG,
                "onServiceConnected connected*****$connected"
            )
        }

        override fun onServiceDisconnected(name: ComponentName) {
            bookController = null
            connected = false
            LogManager.i(
                TAG,
                "onServiceDisconnected connected*****$connected"
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (connected) {
            unbindService(serviceConnection)
        }
    }
}