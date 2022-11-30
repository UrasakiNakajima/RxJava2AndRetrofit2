package com.phone.library_common.manager

import com.phone.library_common.BaseApplication
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * 不會造成App崩潰的異常（被try(){}catch拋出來的異常）管理類
 */
class GetJsonDataManager {

    fun getJson(fileName: String?): String {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = BaseApplication.get().assets
            val bf = BufferedReader(
                InputStreamReader(
                    assetManager.open(fileName!!)
                )
            )
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }
}