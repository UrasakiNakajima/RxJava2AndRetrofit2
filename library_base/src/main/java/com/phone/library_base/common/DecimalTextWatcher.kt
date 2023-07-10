package com.phone.library_base.common

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class DecimalTextWatcher(val editText: EditText, val afterDecimalNum: Int) : TextWatcher {

    private val TAG = DecimalTextWatcher::class.java.simpleName
    private var beforeData: String? = null
    private var beforeDataArr: Array<String>? = null
    private var index = 0

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        beforeData = s.toString()
        beforeDataArr = null
        beforeData?.let {
            //请注意，kotlin 分割字符串无需转义符
            if (it.contains(".")) {
                //请注意，kotlin 分割字符串无需转义符
                beforeDataArr = it.split(".").toTypedArray()
            }
        }
        index = editText.selectionStart
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable) {
        val afterData = s.toString()
        editText.removeTextChangedListener(this)
        //请注意，kotlin 分割字符串无需转义符
        if (afterData.contains(".") && afterData.length > 1) {
            beforeDataArr?.let {
                if (it.size > 1) {
                    for (i in it.indices) {
                        if (afterData.contains(it[i])) {
                            //请注意，kotlin 分割字符串无需转义符
                            val afterDataArr = afterData.split(".").toTypedArray()
                            if (afterDataArr.size > 1 && afterDataArr[1].length > afterDecimalNum) {
                                if (index >= afterDataArr[0].length) {
                                    s.delete(index, index + 1)
                                }
                                break
                            }
                        }
                    }
                }
            }
        }
        editText.addTextChangedListener(this)
    }
}