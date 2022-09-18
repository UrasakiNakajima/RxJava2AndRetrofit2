package com.phone.base64_and_file.presenter;

import android.content.Context;

import com.phone.base64_and_file.bean.Base64AndFileBean;

public interface IBase64AndFilePresenter {

    void showCompressedPicture(Context context,
                               Base64AndFileBean base64AndFileBean);

    void showPictureToBase64(Base64AndFileBean base64AndFileBean);

    void showBase64ToPicture(Base64AndFileBean base64AndFileBean);

}
