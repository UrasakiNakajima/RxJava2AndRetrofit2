package com.phone.common_library.callback

import android.view.View

interface OnDialogCallback<T> {

    fun onDialogClick(view: View?, success: T?)

    fun onDialogClick(view: View?, success: T?, params: Map<String?, String?>?)
}