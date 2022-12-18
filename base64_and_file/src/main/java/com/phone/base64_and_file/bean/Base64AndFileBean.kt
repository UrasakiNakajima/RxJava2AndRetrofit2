package com.phone.base64_and_file.bean

import android.graphics.Bitmap
import java.io.File

class Base64AndFileBean {
    var dirsPath: String? = null
    var dirsPathCompressed: String? = null
    var dirsPathCompressedRecover: String? = null
    var file: File? = null
    var fileCompressed: File? = null
    var fileCompressedRecover: File? = null
    var bitmap: Bitmap? = null
    var bitmapCompressed: Bitmap? = null
    var bitmapCompressedRecover: Bitmap? = null
    var base64Str: String? = null
    var txtFilePath: String? = null
    val base64StrList: MutableList<String> = ArrayList()
}