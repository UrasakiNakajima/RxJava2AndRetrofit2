package com.phone.base64_and_file;

import com.phone.base64_and_file.bean.Base64AndFileBean;
import com.phone.common_library.base.IBaseView;

public interface IBase64AndFileView extends IBaseView {

    void showCompressedPictureSuccess(Base64AndFileBean success);

    void showCompressedPictureError(String error);

    void showPictureToBase64Success(Base64AndFileBean success);

    void showPictureToBase64Error(String error);

    void showBase64ToPictureSuccess(Base64AndFileBean success);

    void showBase64ToPictureError(String error);

}
