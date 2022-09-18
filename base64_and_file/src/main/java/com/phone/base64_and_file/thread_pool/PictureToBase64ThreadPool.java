package com.phone.base64_and_file.thread_pool;

import android.text.TextUtils;

import com.phone.base64_and_file.manager.Base64AndFileManager;
import com.phone.base64_and_file.manager.FileManager;
import com.phone.base64_and_file.bean.Base64AndFileBean;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.manager.LogManager;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PictureToBase64ThreadPool {

    private static final String TAG = PictureToBase64ThreadPool.class.getSimpleName();
    private Base64AndFileBean base64AndFileBean;
    private ExecutorService pictureToBase64TaskExcutor;

    public PictureToBase64ThreadPool(Base64AndFileBean base64AndFileBean) {
        this.base64AndFileBean = base64AndFileBean;
        pictureToBase64TaskExcutor = Executors.newSingleThreadExecutor();
    }

    public void submit() {
        pictureToBase64TaskExcutor.submit(new Runnable() {
            @Override
            public void run() {
                LogManager.i(TAG, "PictureToBase64TaskThread*******" + Thread.currentThread().getName());

                String base64Str = null;
                File fileCompressed = new File(base64AndFileBean.getFileCompressed().getAbsolutePath());
                LogManager.i(TAG, "PictureToBase64TaskThread fileCompressedPath*******" + fileCompressed.getAbsolutePath());
                if (fileCompressed.exists()) {
                    base64Str = Base64AndFileManager.fileToBase64(fileCompressed);
//                base64Str = Base64AndFileManager.fileToBase64Test(fileCompressed);
//                    base64Str = Base64AndFileManager.fileToBase64Second(fileCompressed);
                    base64AndFileBean.setBase64Str(base64Str);
                }

                if (!TextUtils.isEmpty(base64Str)) {
                    String fileName = "base64Str.txt";
                    String txtFilePath = FileManager.writeStrToTextFile(base64Str, base64AndFileBean.getDirsPathCompressed(), fileName);
                    List<String> base64StrList = Base64AndFileManager.getBase64StrList(txtFilePath);
                    base64AndFileBean.setTxtFilePath(txtFilePath);
                    base64AndFileBean.setBase64StrList(base64StrList);
//                base64Str = "";
//                StringBuilder stringBuilder = new StringBuilder();
//                for (int i = 0; i < base64StrList.size(); i++) {
//                    stringBuilder.append(base64StrList.get(i));
//                }
//                base64Str = stringBuilder.toString();

                    //把字符串的最後一個打印出來，然後看看和RecyclerView顯示的最後一個字符串是否一致
                    LogManager.i(TAG, "base64StrList******" + base64StrList.get(base64StrList.size() - 1));
                }

                if (base64AndFileBean.getBase64StrList() != null && base64AndFileBean.getBase64StrList().size() > 0) {
                    onCommonSingleParamCallback.onSuccess(base64AndFileBean);
                } else {
                    onCommonSingleParamCallback.onError("圖片不存在");
                }
            }
        });
    }

    private OnCommonSingleParamCallback<Base64AndFileBean> onCommonSingleParamCallback;

    public void setOnCommonSingleParamCallback(OnCommonSingleParamCallback<Base64AndFileBean> onCommonSingleParamCallback) {
        this.onCommonSingleParamCallback = onCommonSingleParamCallback;
    }
}

