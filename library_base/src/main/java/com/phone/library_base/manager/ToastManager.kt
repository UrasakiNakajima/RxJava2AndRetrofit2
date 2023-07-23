package com.phone.library_base.manager

import android.content.Context
import android.widget.Toast

object ToastManager {

    @JvmStatic
    fun toast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}