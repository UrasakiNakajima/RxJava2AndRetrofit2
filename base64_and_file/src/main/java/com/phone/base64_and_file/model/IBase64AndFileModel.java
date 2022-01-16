package com.phone.base64_and_file.model;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;

import com.phone.base64_and_file.IBase64AndFileView;
import com.phone.common_library.callback.OnCommonBothParamCallback;
import com.phone.common_library.callback.OnCommonSingleParamCallback;

import java.util.List;

public interface IBase64AndFileModel {

    void showCompressedPicture(Context context,
                               String dirsPath,
                               String dirsPath2,
                               OnCommonBothParamCallback<Bitmap> onCommonBothParamCallback);

    void showPictureToBase64(String filePath,
                             OnCommonBothParamCallback<List<String>> onCommonBothParamCallback);

    void showBase64ToPicture(String filePath,
                             String base64Str,
                             OnCommonSingleParamCallback<Bitmap> onCommonSingleParamCallback);

}
