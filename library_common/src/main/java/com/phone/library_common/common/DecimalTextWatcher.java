package com.phone.library_common.common;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

public class DecimalTextWatcher implements TextWatcher {

    private static final String TAG = DecimalTextWatcher.class.getSimpleName();
    private String beforeData;
    private String[] beforeDataArr;
    private EditText editText;
    //小数点后边几位
    private int afterDecimalNum;
    //    private OnCommonSingleParamCallback<String> onCommonSingleParamCallback;
    private int index;

    public DecimalTextWatcher(EditText editText, int afterDecimalNum) {
        this.editText = editText;
        this.afterDecimalNum = afterDecimalNum;
    }

//    public DecimalTextWatcher(EditText editText, int afterDecimalNum, OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
//        this.editText = editText;
//        this.afterDecimalNum = afterDecimalNum;
//        this.onCommonSingleParamCallback = onCommonSingleParamCallback;
//    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        beforeData = s.toString();
        beforeDataArr = null;
        if (!TextUtils.isEmpty(beforeData) && beforeData.contains(".")) {
            beforeDataArr = beforeData.split("\\.");
        }
        index = editText.getSelectionStart();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String afterData = s.toString();

        editText.removeTextChangedListener(this);
        if (afterData.contains(".") && afterData.length() > 1) {
            if (beforeDataArr != null && beforeDataArr.length > 1) {
                for (int i = 0; i < beforeDataArr.length; i++) {
                    if (afterData.contains(beforeDataArr[i])) {
                        String[] afterDataArr = afterData.split("\\.");
                        if (afterDataArr != null && afterDataArr.length > 1 && afterDataArr[1].length() > afterDecimalNum) {
                            if (index >= afterDataArr[0].length()) {
                                s.delete(index, index + 1);
                            }
                            break;
                        }
                    }
                }
            }
        }
        editText.addTextChangedListener(this);

//        afterData = s.toString();
//        if (!TextUtils.isEmpty(afterData)) {
//            onCommonSingleParamCallback.onSuccess(afterData);
//        } else {
//            onCommonSingleParamCallback.onError("");
//        }
    }

}
