package com.phone.common_library.manager

import java.math.BigDecimal
import java.text.NumberFormat

object BigDecimalManager {

    private val TAG = BigDecimalManager::class.java.simpleName

    /**
     * double类型的加法运算（需要舍入，保留5位小数，小数位可自定义）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun additionDouble(m1: Double, m2: Double, scale: Int): Double {
        val p1 = BigDecimal(java.lang.Double.toString(m1))
        val p2 = BigDecimal(java.lang.Double.toString(m2))
        return p1.add(p2).setScale(scale, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    /**
     * double类型的超大数值加法运算（需要舍入，保留5位小数，小数位可自定义）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun additionDoubleToStr(m1: Double, m2: Double, scale: Int): String? {
        val p1 = BigDecimal(java.lang.Double.toString(m1))
        val p2 = BigDecimal(java.lang.Double.toString(m2))
        return p1.add(p2).setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString()
    }

    /**
     * double类型的减法运算（需要舍入，保留5位小数，小数位可自定义）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun subtractionDouble(m1: Double, m2: Double, scale: Int): Double {
        val p1 = BigDecimal(java.lang.Double.toString(m1))
        val p2 = BigDecimal(java.lang.Double.toString(m2))
        return p1.subtract(p2).setScale(scale, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    /**
     * double类型的超大数值减法运算（需要舍入，保留5位小数，小数位可自定义）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun subtractionDoubleToStr(m1: Double, m2: Double, scale: Int): String? {
        val p1 = BigDecimal(java.lang.Double.toString(m1))
        val p2 = BigDecimal(java.lang.Double.toString(m2))
        return p1.subtract(p2).setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString()
    }

    /**
     * double类型的乘法运算（需要舍入，保留5位小数，小数位可自定义）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun multiplicationDouble(m1: Double, m2: Double, scale: Int): Double {
        val p1 = BigDecimal(java.lang.Double.toString(m1))
        val p2 = BigDecimal(java.lang.Double.toString(m2))
        return p1.multiply(p2).setScale(scale, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    /**
     * double类型的超大数值的乘法运算（需要舍入，保留5位小数，小数位可自定义）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun multiplicationDoubleToStr(m1: Double, m2: Double, scale: Int): String? {
        val p1 = BigDecimal(java.lang.Double.toString(m1))
        val p2 = BigDecimal(java.lang.Double.toString(m2))
        return p1.multiply(p2).setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString()
    }

    /**
     * double类型的超大数值的乘法运算（需要舍入，保留5位小数，小数位可自定义）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun multiplicationDoubleToStr(m1: String?, m2: String?, scale: Int): String? {
        val p1 = BigDecimal(m1)
        val p2 = BigDecimal(m2)
        return p1.multiply(p2).setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString()
    }

    /**
     * double类型的除法运算（需要舍入，保留5位小数，小数位可自定义）
     * @param   m1
     * @param   m2
     * @param   scale
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun divisionDouble(m1: Double, m2: Double, scale: Int): Double {
        require(scale >= 0) { "Parameter error" }
        val p1 = BigDecimal(java.lang.Double.toString(m1))
        val p2 = BigDecimal(java.lang.Double.toString(m2))
        return p1.divide(p2, scale, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    /**
     * double类型的超大数值的除法运算（需要舍入，保留5位小数，小数位可自定义）
     * @param   m1
     * @param   m2
     * @param   scale
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun divisionDoubleToStr(m1: Double, m2: Double, scale: Int): String? {
        require(scale >= 0) { "Parameter error" }
        val p1 = BigDecimal(java.lang.Double.toString(m1))
        val p2 = BigDecimal(java.lang.Double.toString(m2))
        return p1.divide(p2, scale, BigDecimal.ROUND_HALF_UP).toPlainString()
    }

    /**
     * double类型的保留小数（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun getDoubleKeepDecimals(m1: Double, scale: Int): Double {
        val p1 = BigDecimal(java.lang.Double.toString(m1))
        return p1.setScale(scale, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    /**
     * double类型的保留小数（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun getDoubleKeepDecimalsToStr(m1: Double, scale: Int): String? {
        val p1 = BigDecimal(java.lang.Double.toString(m1))
        return p1.setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString()
    }


    /**
     * double类型的加法运算兼容（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun additionDoubleCompatible(m1: Double, m2: Double, scale: Int): Double {
        val p1 = BigDecimal(java.lang.Double.toString(m1))
        val p2 = BigDecimal(java.lang.Double.toString(m2))
        val value = p1.add(p2).setScale(scale, BigDecimal.ROUND_DOWN).toDouble()
        return keepDecimalsCompatibleToDou(value, scale)
    }

    /**
     * double类型的加法运算兼容（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun additionDoubleCompatible(m1: String?, m2: String?, scale: Int): Double {
        val p1 = BigDecimal(m1)
        val p2 = BigDecimal(m2)
        val value = p1.add(p2).setScale(scale, BigDecimal.ROUND_DOWN).toDouble()
        return keepDecimalsCompatibleToDou(value, scale)
    }

    /**
     * double类型的加法运算兼容，可以把double类型的科学计数法转化成小数字符串，
     * 主要用来显示小数（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun additionDoubleCompatibleToStr(m1: String?, m2: String?, scale: Int): String? {
        val p1 = BigDecimal(m1)
        val p2 = BigDecimal(m2)
        val value = p1.add(p2).setScale(scale, BigDecimal.ROUND_DOWN).toDouble()
        return keepDecimalsCompatibleToStr(value, scale)
    }

    /**
     * double类型的减法运算兼容（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun subtractionDoubleCompatible(m1: Double, m2: Double, scale: Int): Double {
        val p1 = BigDecimal(java.lang.Double.toString(m1))
        val p2 = BigDecimal(java.lang.Double.toString(m2))
        val value = p1.subtract(p2).setScale(scale, BigDecimal.ROUND_DOWN).toDouble()
        return keepDecimalsCompatibleToDou(value, scale)
    }

    /**
     * double类型的减法运算兼容（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun subtractionDoubleCompatible(m1: String?, m2: String?, scale: Int): Double {
        val p1 = BigDecimal(m1)
        val p2 = BigDecimal(m2)
        val value = p1.subtract(p2).setScale(scale, BigDecimal.ROUND_DOWN).toDouble()
        return keepDecimalsCompatibleToDou(value, scale)
    }

    /**
     * double类型的加法运算兼容，可以把double类型的科学计数法转化成小数字符串，
     * 主要用来显示小数（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun subtractionDoubleCompatibleToStr(m1: String?, m2: String?, scale: Int): String? {
        val p1 = BigDecimal(m1)
        val p2 = BigDecimal(m2)
        val value = p1.subtract(p2).setScale(scale, BigDecimal.ROUND_DOWN).toDouble()
        return keepDecimalsCompatibleToStr(value, scale)
    }

    /**
     * double类型的除法运算兼容（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun multiplicationDoubleCompatible(m1: Double, m2: Double, scale: Int): Double {
        val p1 = BigDecimal(java.lang.Double.toString(m1))
        val p2 = BigDecimal(java.lang.Double.toString(m2))
        val value = p1.multiply(p2).setScale(scale, BigDecimal.ROUND_DOWN).toDouble()
        return keepDecimalsCompatibleToDou(value, scale)
    }

    /**
     * double类型的加法运算兼容，可以把double类型的科学计数法转化成小数字符串，
     * 主要用来显示小数（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun multiplicationDoubleCompatibleToStr(m1: Double, m2: Double, scale: Int): String? {
        val p1 = BigDecimal(java.lang.Double.toString(m1))
        val p2 = BigDecimal(java.lang.Double.toString(m2))
        val value = p1.multiply(p2).setScale(scale, BigDecimal.ROUND_DOWN).toDouble()
        return keepDecimalsCompatibleToStr(value, scale)
    }

    /**
     * double类型的加法运算兼容，可以把double类型的科学计数法转化成小数字符串，
     * 主要用来显示小数（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun multiplicationDoubleCompatibleToStr(m1: String?, m2: String?, scale: Int): String? {
        val p1 = BigDecimal(m1)
        val p2 = BigDecimal(m2)
        val value = p1.multiply(p2).setScale(scale, BigDecimal.ROUND_DOWN).toDouble()
        return keepDecimalsCompatibleToStr(value, scale)
    }

    /**
     * double类型的除法运算兼容（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun divisionDoubleCompatible(m1: Double, m2: Double, scale: Int): Double {
        val p1 = BigDecimal(java.lang.Double.toString(m1))
        val p2 = BigDecimal(java.lang.Double.toString(m2))
        val value = p1.divide(p2, scale, BigDecimal.ROUND_DOWN).toDouble()
        return keepDecimalsCompatibleToDou(value, scale)
    }

    /**
     * double类型的加法运算兼容，可以把double类型的科学计数法转化成小数字符串，
     * 主要用来显示小数（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun divisionDoubleCompatibleToStr(m1: Double, m2: Double, scale: Int): String? {
        val p1 = BigDecimal(java.lang.Double.toString(m1))
        val p2 = BigDecimal(java.lang.Double.toString(m2))
        val value = p1.divide(p2, scale, BigDecimal.ROUND_DOWN).toDouble()
        return keepDecimalsCompatibleToStr(value, scale)
    }

    /**
     * double类型的除法运算兼容（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun divisionDoubleCompatible(m1: String?, m2: String?, scale: Int): Double {
        val p1 = BigDecimal(m1)
        val p2 = BigDecimal(m2)
        val value = p1.divide(p2, scale, BigDecimal.ROUND_DOWN).toDouble()
        return keepDecimalsCompatibleToDou(value, scale)
    }

    /**
     * double类型的保留小数（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun getDoubleKeepDecimalsCompatible(m1: Double, scale: Int): Double {
        val p1 = BigDecimal(java.lang.Double.toString(m1))
        val value = p1.setScale(scale, BigDecimal.ROUND_DOWN).toDouble()
        return keepDecimalsCompatibleToDou(value, scale)
    }

    /**
     * double类型的加法运算兼容，可以把double类型的科学计数法转化成小数字符串，
     * 主要用来显示小数（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    fun getDoubleKeepDecimalsCompatibleToStr(m1: Double, scale: Int): String? {
        val p1 = BigDecimal(java.lang.Double.toString(m1))
        val value = p1.setScale(scale, BigDecimal.ROUND_DOWN).toDouble()
        return keepDecimalsCompatibleToStr(value, scale)
    }

    private fun keepDecimalsCompatibleToDou(value: Double, scale: Int): Double {
        //将科学计数法转化成小数
        val numberFormat = NumberFormat.getInstance()
        numberFormat.isGroupingUsed = false
        // 设置数的小数部分所允许的最小位数
        numberFormat.minimumFractionDigits = 0
        // 设置数的小数部分所允许的最大位数
        numberFormat.maximumFractionDigits = scale
        val valueStr = numberFormat.format(value)

        //返回的double数据如果很大还是科学计数法的，如果需要显示，建议返回String类型的
        return if (valueStr.contains(".")) {
            val valueStrArr = valueStr.split("\\.").toTypedArray()
            if (valueStrArr.size > 1 && valueStrArr[1].length > 0) {
                if (scale <= 0) {
                    valueStrArr[0].toDouble()
                } else {
                    if (valueStrArr[1].length < scale) {
                        (valueStrArr[0] + "." + valueStrArr[1]).toDouble()
                    } else {
                        (valueStrArr[0] + "." + valueStrArr[1].substring(0, scale)).toDouble()
                    }
                }
            } else {
                valueStr.toDouble()
            }
        } else {
            valueStr.toDouble()
        }
    }

    private fun keepDecimalsCompatibleToStr(value: Double, scale: Int): String? {
        //将科学计数法转化成小数
        val numberFormat = NumberFormat.getInstance()
        numberFormat.isGroupingUsed = false
        // 设置数的小数部分所允许的最小位数
        numberFormat.minimumFractionDigits = 0
        // 设置数的小数部分所允许的最大位数
        numberFormat.maximumFractionDigits = scale
        val valueStr = numberFormat.format(value)
        return if (valueStr.contains(".")) {
            val valueStrArr = valueStr.split("\\.").toTypedArray()
            if (valueStrArr.size > 1 && valueStrArr[1].length > 0) {
                if (scale <= 0) {
                    valueStrArr[0]
                } else {
                    if (valueStrArr[1].length < scale) {
                        valueStrArr[0] + "." + valueStrArr[1]
                    } else {
                        valueStrArr[0] + "." + valueStrArr[1].substring(0, scale)
                    }
                }
            } else {
                valueStr
            }
        } else {
            valueStr
        }
    }

}