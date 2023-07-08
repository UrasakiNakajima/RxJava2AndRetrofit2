package com.phone.library_base64_and_file.view

import com.phone.library_base64_and_file.bean.Base64AndFileBean
import com.phone.library_base.base.IBaseView

interface IBase64AndFileView : IBaseView {

    fun showCompressedPictureSuccess(success: Base64AndFileBean)

    fun showCompressedPictureError(error: String)

    fun showPictureToBase64Success(success: Base64AndFileBean)

    fun showPictureToBase64Error(error: String)

    fun showBase64ToPictureSuccess(success: Base64AndFileBean)

    fun showBase64ToPictureError(error: String)

}