package com.phone.common_library.common;

import android.text.InputFilter;
import android.text.Spanned;

public class DecimalInputFilter implements InputFilter {

    private static final String TAG = DecimalInputFilter.class.getSimpleName();
    //小数点前边几位
    private int beforeDecimalNum;
    //小数点后边几位
    private int afterDecimalNum;

    public DecimalInputFilter(int beforeDecimalNum, int afterDecimalNum) {
        this.beforeDecimalNum = beforeDecimalNum;
        this.afterDecimalNum = afterDecimalNum;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String beforeData = dest.toString();
        String inputData = source.toString();
        if (source.length() == 0 && dend > dstart) {
            //删除操作

            //使用系统的
            return null;
        } else {
            //输入操作
            if (beforeData.contains(".")) {
                String[] beforeDataArr = beforeData.split("\\.");
                if (beforeDataArr.length > 1 && beforeDataArr[0].length() >= beforeDecimalNum && beforeDataArr[1].length() >= afterDecimalNum) {
                    //返回自定义的
                    return "";
                } else if (beforeDataArr.length >= 1 && beforeDataArr[0].length() >= beforeDecimalNum && dstart <= beforeDecimalNum) {
                    //返回自定义的
                    return "";
                } else if (beforeDataArr.length > 1 && beforeDataArr[1].length() >= afterDecimalNum && dend >= beforeDecimalNum) {
                    //返回自定义的
                    return "";
                }
            } else {
                if (beforeData.length() >= beforeDecimalNum && !inputData.equals(".")) {
                    //返回自定义的
                    return "";
                }
            }
        }


        //使用系统的
        return null;
    }

}
