package com.phone.base64_and_file;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Toast;

import com.phone.common_library.base.IBaseView;

import java.util.List;

public interface IBase64AndFileView extends IBaseView {

    void showCompressedPictureSuccess(Bitmap bitmap, String compressedPicturePath);

    void showCompressedPictureError(String error);

    void showPictureToBase64Success(List<String> base64StrList, String base64Str);

    void showPictureToBase64Error(String error);

    void showBase64ToPictureSuccess(Bitmap bitmap);

    void showBase64ToPictureError(String error);

}
