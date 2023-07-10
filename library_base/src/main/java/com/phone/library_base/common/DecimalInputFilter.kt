package com.phone.library_base.common

import android.text.InputFilter
import android.text.Spanned

class DecimalInputFilter(val beforeDecimalNum: Int, val afterDecimalNum: Int) : InputFilter {

    private val TAG = DecimalInputFilter::class.java.simpleName

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val beforeData = dest.toString()
        val inputData = source.toString()
        if (source.length == 0 && dend > dstart) {
            //删除操作

            //使用系统的
            return null
        } else {
            //输入操作
            if (beforeData.contains(".")) {
                //请注意，kotlin 分割字符串无需转义符
                val beforeDataArr = beforeData.split(".").toTypedArray()
                if (beforeDataArr.size > 1 && beforeDataArr[0].length >= beforeDecimalNum && beforeDataArr[1].length >= afterDecimalNum) {
                    //返回自定义的
                    return ""
                } else if (beforeDataArr.size >= 1 && beforeDataArr[0].length >= beforeDecimalNum && dstart <= beforeDecimalNum) {
                    //返回自定义的
                    return ""
                } else if (beforeDataArr.size > 1 && beforeDataArr[1].length >= afterDecimalNum && dend >= beforeDecimalNum) {
                    //返回自定义的
                    return ""
                }
            } else {
                //请注意，kotlin 分割字符串无需转义符
                if (beforeData.length >= beforeDecimalNum && !".".equals(inputData)) {
                    //返回自定义的
                    return ""
                }
            }
        }

        //使用系统的
        return null
    }

}