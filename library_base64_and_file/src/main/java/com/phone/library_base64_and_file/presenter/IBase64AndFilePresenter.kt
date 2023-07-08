package com.phone.library_base64_and_file.presenter

import android.content.Context
import com.phone.library_base64_and_file.bean.Base64AndFileBean

interface IBase64AndFilePresenter {

    fun showCompressedPicture(
        context: Context,
        base64AndFileBean: Base64AndFileBean
    )

    fun showPictureToBase64(base64AndFileBean: Base64AndFileBean)

    fun showBase64ToPicture(base64AndFileBean: Base64AndFileBean)

}