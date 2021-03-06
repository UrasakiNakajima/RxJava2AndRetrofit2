package com.phone.common_library.manager;

import android.os.Environment;

import com.phone.common_library.BaseApplication;
import com.phone.common_library.callback.OnCommonSingleParamCallback;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/17 10:35
 * introduce :
 */

public class ReadAndWriteManager {

    private static final String TAG = ReadAndWriteManager.class.getSimpleName();
    private static ReadAndWriteManager manager;
//    private List<Disposable> disposableList;
    private Disposable disposable;

    /**
     * 私有构造器 无法外部创建
     */
    private ReadAndWriteManager() {
    }

    public static ReadAndWriteManager getInstance() {
        if (manager == null) {
            synchronized (ReadAndWriteManager.class) {
                if (manager == null) {
                    manager = new ReadAndWriteManager();
                }
            }
        }

        return manager;
    }

    /**
     * 将内容写入sd卡中
     *
     * @param fileName 要写入的文件名
     * @param content  待写入的内容
     * @throws IOException
     */
    public void writeExternal(String fileName,
                              String content,
                              OnCommonSingleParamCallback<Boolean> onCommonSingleParamCallback) {
//        //获取外部存储卡的可用状态
//        String storageState = Environment.getExternalStorageState();
//        //判断是否存在可用的的SD Card
//        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
//            //路径： /storage/emulated/0/Android/data/com.yoryky.demo/cache/yoryky.txt
////            filename = context.getExternalCacheDir().getAbsolutePath() + File.separator + filename;
//            filename = Environment.getExternalStorageDirectory().getAbsolutePath()
//                    + File.separator
//                    + "Android"
//                    + File.separator
//                    + fileName;
//
//            LogManager.i(TAG,"writeExternal");
//            FileOutputStream outputStream = new FileOutputStream(filename, false);
//            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
//            bufferedOutputStream.write(content.getBytes());
//            bufferedOutputStream.close();
//        }

        LogManager.i(TAG, "writeExternal");
//        disposableList = new ArrayList<>();
        disposable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                LogManager.i(TAG, "Observable thread is*****" + Thread.currentThread().getName());
                String FILEPATH = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + File.separator
                        + "Mine";
                File dirs = new File(FILEPATH);
                if (!dirs.exists()) {
                    LogManager.i(TAG, "writeExternal*****!dirs.exists()");
                    dirs.mkdirs();
                }
                File file = new File(dirs, fileName);
                if (!file.exists()) {
                    LogManager.i(TAG, "writeExternal*****!file.exists()");
                    file.createNewFile();
                } else {
                    LogManager.i(TAG, "writeExternal file.getAbsolutePath()*****" + file.getAbsolutePath());
                }
                FileOutputStream outputStream = new FileOutputStream(file, false);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                bufferedOutputStream.write(content.getBytes());
                bufferedOutputStream.close();
                e.onNext(true);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isSuccess) throws Exception {
                        LogManager.i(TAG, "isSuccess*****" + isSuccess);
                        onCommonSingleParamCallback.onSuccess(isSuccess);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogManager.i(TAG, "throwable*****" + throwable.getMessage());
                        // 异常处理
                        onCommonSingleParamCallback.onError("写入失败");
                    }
                });
//        disposableList.add(disposable);
    }

    /**
     * 从sd card文件中读取数据
     *
     * @param filename 待读取的sd card
     * @return
     * @throws IOException
     */
    public static String readExternal(String filename) throws IOException {
        StringBuilder stringBuilder = new StringBuilder("");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filename = BaseApplication.getInstance().getExternalCacheDir().getAbsolutePath() + File.separator + filename;
            //打开文件输入流
            FileInputStream inputStream = new FileInputStream(filename);

            byte[] buffer = new byte[1024];
            int len = inputStream.read(buffer);
            //读取文件内容
            while (len > 0) {
                stringBuilder.append(new String(buffer, 0, len));

                //继续将数据放到buffer中
                len = inputStream.read(buffer);
            }
            //关闭输入流
            inputStream.close();
        }
        return stringBuilder.toString();
    }

//    private boolean isSubscribe() {
//        if (disposableList != null && disposableList.size() > 0) {
//            return true;
//        }
//        return false;
//    }

    public void unSubscribe() {
//        if (isSubscribe()) {
//            for (int i = 0; i < disposableList.size(); i++) {
//                disposable = disposableList.get(i);
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                    disposable = null;
                }
//            }
//            disposableList.clear();
//            disposableList = null;
//        }
    }

}
