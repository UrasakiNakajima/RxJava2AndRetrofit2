package com.phone.base64_and_file.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.phone.base64_and_file.IBase64AndFileView;

import java.util.List;
import java.util.Map;

public interface IBase64AndFilePresenter {

    void showCompressedPicture(Context context,
                               String dirsPath,
                               String dirsPath2);

    void showPictureToBase64(String filePath);

    void showBase64ToPicture(String filePath,
                              String base64Str);

}
