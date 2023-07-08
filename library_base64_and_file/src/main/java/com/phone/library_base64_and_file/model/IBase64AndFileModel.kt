package com.phone.library_base64_and_file.model

import android.content.Context
import com.phone.library_base64_and_file.bean.Base64AndFileBean
import com.phone.library_base.callback.OnCommonSingleParamCallback

interface IBase64AndFileModel {

    fun showCompressedPicture(
        context: Context,
        base64AndFileBean: Base64AndFileBean,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<Base64AndFileBean>
    )

    fun showPictureToBase64(
        base64AndFileBean: Base64AndFileBean,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<Base64AndFileBean>
    )

    fun showBase64ToPicture(
        base64AndFileBean: Base64AndFileBean,
        onCommonSingleParamCallback: OnCommonSingleParamCallback<Base64AndFileBean>
    )

    fun detachViewModel()
}