package com.phone.aidl.app_aidl_server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException
import com.phone.aidl.aidl.Book
import com.phone.aidl.aidl.BookController
import com.phone.library_base.manager.LogManager
import java.util.ArrayList

class AidlService : Service() {

    private var mBookList: MutableList<Book> = ArrayList()

    override fun onCreate() {
        super.onCreate()
        initData()
    }

    private fun initData() {
        val book1 = Book("封神榜")
        val book2 = Book("公主小妹")
        val book3 = Book("仙剑奇侠传")
        val book4 = Book("飘")
        val book5 = Book("茶花女")
        val book6 = Book("解忧杂货铺")
        val book7 = Book("活着")
        val book8 = Book("三生三世十里桃花")
        mBookList.add(book1)
        mBookList.add(book2)
        mBookList.add(book3)
        mBookList.add(book4)
        mBookList.add(book5)
        mBookList.add(book6)
        mBookList.add(book7)
        mBookList.add(book8)
    }

    private val mStub: BookController.Stub = object : BookController.Stub() {
        @Throws(RemoteException::class)
        override fun getInt(): Int {
            return mBookList.size
        }

        @Throws(RemoteException::class)
        override fun getString(): String {
            return mBookList[0].name ?: ""
        }

        @Throws(RemoteException::class)
        override fun getBookList(): List<Book> {
            return mBookList
        }

        @Throws(RemoteException::class)
        override fun addBook(book: Book?) {
            if (book != null) {
                mBookList.add(book)
            } else {
                LogManager.i("ruxing", "接收到了一个空对象 Inout")
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return mStub
    }


}