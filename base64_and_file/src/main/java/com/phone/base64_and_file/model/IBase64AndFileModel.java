package com.phone.base64_and_file.model;

import android.content.Context;

import com.phone.base64_and_file.bean.Base64AndFileBean;
import com.phone.common_library.callback.OnCommonSingleParamCallback;

public interface IBase64AndFileModel {

    void showCompressedPicture(Context context,
                               Base64AndFileBean base64AndFileBean,
                               OnCommonSingleParamCallback<Base64AndFileBean> onCommonSingleParamCallback);

    void showPictureToBase64(Base64AndFileBean base64AndFileBean,
                             OnCommonSingleParamCallback<Base64AndFileBean> onCommonSingleParamCallback);

    void showBase64ToPicture(Base64AndFileBean base64AndFileBean,
                             OnCommonSingleParamCallback<Base64AndFileBean> onCommonSingleParamCallback);

}
