package com.phone.base64_and_file.thread;

import android.text.TextUtils;

import com.phone.base64_and_file.Base64AndFileManager;
import com.phone.base64_and_file.FileManager;
import com.phone.common_library.callback.OnCommonBothParamCallback;
import com.phone.common_library.manager.LogManager;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PictureToBase64TaskThreadPool {

    private static final String TAG = PictureToBase64TaskThreadPool.class.getSimpleName();
    private String base64Str;
    private String filePath;
    private String txtFilePath = null;
    private List<String> base64StrList;
    private ExecutorService pictureToBase64TaskExcutor;

    public PictureToBase64TaskThreadPool(String filePath) {
        this.filePath = filePath;
        pictureToBase64TaskExcutor = Executors.newSingleThreadExecutor();
    }

    public void submit() {
        pictureToBase64TaskExcutor.submit(new Runnable() {
            @Override
            public void run() {
                LogManager.i(TAG, "PictureToBase64TaskThread*******" + Thread.currentThread().getName());

                base64Str = null;
                File file = new File(filePath);
                LogManager.i(TAG, "PictureToBase64TaskThread filePath*******" + filePath);
                if (file.exists()) {
//                base64Str = Base64AndFileManager.fileToBase64(file);
//                base64Str = Base64AndFileManager.fileToBase64Test(file);
                    base64Str = Base64AndFileManager.fileToBase64Second(file);
                }

                if (!TextUtils.isEmpty(base64Str)) {
                    String fileName = "base64Str.txt";
                    txtFilePath = FileManager.writeStrToTextFile(base64Str, filePath, fileName);
                    base64StrList = Base64AndFileManager.getBase64StrList(txtFilePath);
//                base64Str = "";
//                StringBuilder stringBuilder = new StringBuilder();
//                for (int i = 0; i < base64StrList.size(); i++) {
//                    stringBuilder.append(base64StrList.get(i));
//                }
//                base64Str = stringBuilder.toString();

                    //把字符串的最後一個打印出來，然後看看和RecyclerView顯示的最後一個字符串是否一致
                    LogManager.i(TAG, "base64StrList******" + base64StrList.get(base64StrList.size() - 1));
                }

                if (base64StrList.size() > 0) {
                    onCommonBothParamCallback.onSuccess(base64StrList, base64Str);
                } else {
                    onCommonBothParamCallback.onError("圖片不存在");
                }
            }
        });
    }

    private OnCommonBothParamCallback<List<String>> onCommonBothParamCallback;

    public void setOnCommonBothParamCallback(OnCommonBothParamCallback<List<String>> onCommonBothParamCallback) {
        this.onCommonBothParamCallback = onCommonBothParamCallback;
    }
}

