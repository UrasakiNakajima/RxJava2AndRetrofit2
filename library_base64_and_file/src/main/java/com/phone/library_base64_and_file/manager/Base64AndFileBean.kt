package com.phone.library_base64_and_file.manager

import android.graphics.Bitmap
import java.io.File

class Base64AndFileBean {

    private var dirsPath: String? = null
    private var dirsPathCompressed: String? = null
    private var dirsPathCompressedRecover: String? = null
    private var file: File? = null
    private var fileCompressed: File? = null
    private var fileCompressedRecover: File? = null
    private var bitmap: Bitmap? = null
    private var bitmapCompressed: Bitmap? = null
    private var bitmapCompressedRecover: Bitmap? = null
    private var base64Str: String? = null
    private var txtFilePath: String? = null
    private val base64StrList: MutableList<String> = ArrayList()

    fun getDirsPath(): String? {
        return dirsPath
    }

    fun setDirsPath(dirsPath: String?) {
        this.dirsPath = dirsPath
    }

    fun getDirsPathCompressed(): String? {
        return dirsPathCompressed
    }

    fun setDirsPathCompressed(dirsPathCompressed: String?) {
        this.dirsPathCompressed = dirsPathCompressed
    }

    fun getDirsPathCompressedRecover(): String? {
        return dirsPathCompressedRecover
    }

    fun setDirsPathCompressedRecover(dirsPathCompressedRecover: String?) {
        this.dirsPathCompressedRecover = dirsPathCompressedRecover
    }

    fun getFile(): File? {
        return file
    }

    fun setFile(file: File?) {
        this.file = file
    }

    fun getFileCompressed(): File? {
        return fileCompressed
    }

    fun setFileCompressed(fileCompressed: File?) {
        this.fileCompressed = fileCompressed
    }

    fun getFileCompressedRecover(): File? {
        return fileCompressedRecover
    }

    fun setFileCompressedRecover(fileCompressedRecover: File?) {
        this.fileCompressedRecover = fileCompressedRecover
    }

    fun getBitmap(): Bitmap? {
        return bitmap
    }

    fun setBitmap(bitmap: Bitmap?) {
        this.bitmap = bitmap
    }

    fun getBitmapCompressed(): Bitmap? {
        return bitmapCompressed
    }

    fun setBitmapCompressed(bitmapCompressed: Bitmap?) {
        this.bitmapCompressed = bitmapCompressed
    }

    fun getBitmapCompressedRecover(): Bitmap? {
        return bitmapCompressedRecover
    }

    fun setBitmapCompressedRecover(bitmapCompressedRecover: Bitmap?) {
        this.bitmapCompressedRecover = bitmapCompressedRecover
    }

    fun getBase64Str(): String? {
        return base64Str
    }

    fun setBase64Str(base64Str: String?) {
        this.base64Str = base64Str
    }

    fun getTxtFilePath(): String? {
        return txtFilePath
    }

    fun setTxtFilePath(txtFilePath: String?) {
        this.txtFilePath = txtFilePath
    }

    fun getBase64StrList(): List<String> {
        return base64StrList
    }

    fun setBase64StrList(base64StrList: List<String>?) {
        this.base64StrList.clear()
        this.base64StrList.addAll(base64StrList!!)
    }
}