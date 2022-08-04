package com.phone.common_library.manager;

import java.math.BigDecimal;

/**
 * 如果计算的数值总和很大很大，超过100 0000 0000，请使用带有ToStr的方法，不然double数字会转成科学计数法显示
 */
public class BigDecimalManager {

    /**
     * double类型的加法运算（不需要舍入）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static double additionDouble(double m1, double m2) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.add(p2).doubleValue();
    }

    /**
     * double类型的加法运算（需要舍入，保留三位小数）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static double additionDouble(double m1, double m2, int scale) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.add(p2).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * double类型的超大数值加法运算（需要舍入，保留三位小数）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static String additionDoubleToStr(double m1, double m2, int scale) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.add(p2).setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    /**
     * double类型的减法运算
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static double subtractionDouble(double m1, double m2) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.subtract(p2).doubleValue();
    }

    /**
     * double类型的减法运算（需要舍入，保留三位小数）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static double subtractionDouble(double m1, double m2, int scale) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.subtract(p2).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * double类型的超大数值减法运算（需要舍入，保留三位小数）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static String subtractionDoubleToStr(double m1, double m2, int scale) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.subtract(p2).setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    /**
     * double类型的乘法运算
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static double multiplicationDouble(double m1, double m2) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.multiply(p2).doubleValue();
    }

    /**
     * double类型的乘法运算
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static double multiplicationDouble(double m1, double m2, int scale) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.multiply(p2).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * double类型的超大数值的乘法运算
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static String multiplicationDoubleToStr(double m1, double m2, int scale) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.multiply(p2).setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    /**
     * double类型的超大数值的乘法运算
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static String multiplicationDoubleToStr(String m1, String m2, int scale) {
        BigDecimal p1 = new BigDecimal(m1);
        BigDecimal p2 = new BigDecimal(m2);
        return p1.multiply(p2).setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    /**
     * double类型的除法运算
     * @param   m1
     * @param   m2
     * @param   scale
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static double divisionDouble(double m1, double m2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("Parameter error");
        }
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.divide(p2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * double类型的超大数值的除法运算
     * @param   m1
     * @param   m2
     * @param   scale
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static String divisionDoubleToStr(double m1, double m2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("Parameter error");
        }
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.divide(p2, scale, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

}
