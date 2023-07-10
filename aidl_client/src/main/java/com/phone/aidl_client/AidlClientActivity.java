package com.phone.aidl_client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phone.aidl_client.adapter.BookAdapter;
import com.phone.library_base.base.BaseRxAppActivity;
import com.phone.library_base.manager.LogManager;

import java.util.ArrayList;
import java.util.List;

public class AidlClientActivity extends BaseRxAppActivity {

    private static final String TAG = AidlClientActivity.class.getSimpleName();
    private Toolbar toolbar;
    private TextView tevTitle;
    private TextView tevConnectServerService;
    private TextView tevAddBook;
    private RecyclerView rcvBook;


    //由AIDL文件生成的Java类
    private BookManager mBookManager = null;

    //标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
    private boolean isConnectServer = false;

    //包含Book对象的list
    private List<Book> mBookList = new ArrayList<>();
    private int num;

    private LinearLayoutManager mLayoutManager;
    private BookAdapter mBookAdapter;

    @Override
    protected int initLayoutId() {
        return R.layout.client_activity_aidl_client;
    }

    @Override
    protected void initData() {
        num = 0;
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tevTitle = (TextView) findViewById(R.id.tev_title);
        tevConnectServerService = (TextView) findViewById(R.id.tev_connect_server_service);
        tevAddBook = (TextView) findViewById(R.id.tev_add_book);
        rcvBook = (RecyclerView) findViewById(R.id.rcv_book);
        setToolbar(false, R.color.library_color_FF198CFF);

        tevConnectServerService.setOnClickListener(v -> {
            connectServerService();
        });
        tevAddBook.setOnClickListener(v -> {
            initAddBook();
        });

        initAdapter();
    }

    private void initAdapter() {
        mLayoutManager = new LinearLayoutManager(mRxAppCompatActivity);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvBook.setLayoutManager(mLayoutManager);
        rcvBook.setItemAnimator(new DefaultItemAnimator());

        mBookAdapter = new BookAdapter(mRxAppCompatActivity);
        rcvBook.setAdapter(mBookAdapter);
    }

    private void connectServerService() {
        //如果与服务端的连接处于未连接状态，则尝试连接
        if (!isConnectServer) {
            attemptToBindService();
        }
    }

    private void initAddBook() {
        if (mBookManager == null) {
            Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }

        num++;
        Book book = new Book();
        book.setName("新书" + num);
        book.setContent("新书内容" + num);
        try {
            mBookManager.addBook(book);
            mBookList.clear();
            mBookList.addAll(mBookManager.getBookList());
            mBookAdapter.clearData();
            mBookAdapter.addData(mBookList);
            LogManager.i(TAG, "initAddBook*****" + book.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initLoadData() {

    }

    /**
     * 尝试与服务端建立连接
     */
    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setAction("com.phone.aidl_client.aidl");
        intent.setPackage("com.phone.aidl_server");
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogManager.i(TAG, "service connected");
            mBookManager = BookManager.Stub.asInterface(service);
            isConnectServer = true;
            Toast.makeText(AidlClientActivity.this, "已连接服务端", Toast.LENGTH_SHORT).show();

            if (mBookManager != null) {
                if (mBookList.size() > 0) {
                    LogManager.i(TAG, "onServiceConnected*****" + mBookList.toString());
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogManager.i(TAG, "service disconnected");
            isConnectServer = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
//		if (!isConnectServer) {
//			attemptToBindService();
//		}
    }

    @Override
    protected void onStop() {
        super.onStop();
//		if (isConnectServer) {
//			unbindService(mServiceConnection);
//			isConnectServer = false;
//		}
    }

}
