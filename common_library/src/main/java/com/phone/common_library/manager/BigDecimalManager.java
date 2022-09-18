package com.phone.common_library.manager;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * double精确运算工具类（5位后边的四舍五入，小数位可自定义，至多可以保留5位小数，如果计算的数值总和很大，超过50 0000，请使用带有DoubleToStr的方法，
 * 不然可能会有保留小数位出现问题，不然也可能double数字会转成科学计数法显示）
 *
 * 拓展知识：直接舍去小数点5位后边的数不要，就稍微有一点点繁琐了，文中带DoubleCompatible字段的方法都是直接舍去小数点5位后边的数不要，
 * 至多可以保留5位小数。
 * 注意：如果计算的数值总和很大，超过50 0000，请使用带有DoubleCompatibleToStr的方法，不然可能会有保留小数位出现问题，不然也可能double数字
 * 会转成科学计数法显示，但是带有DoubleCompatibleToStr的方法计算之后的总和不要超过900 0000 0000，不然BigDecimal也无法提供舍去小数点5位
 * 后边的数不要的精确计算了（只能用上边的四舍五入进行计算了）
 *
 */
public class BigDecimalManager {

    /**
     * double类型的加法运算（需要舍入，保留5位小数，小数位可自定义）
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
     * double类型的超大数值加法运算（需要舍入，保留5位小数，小数位可自定义）
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
     * double类型的减法运算（需要舍入，保留5位小数，小数位可自定义）
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
     * double类型的超大数值减法运算（需要舍入，保留5位小数，小数位可自定义）
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
     * double类型的乘法运算（需要舍入，保留5位小数，小数位可自定义）
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
     * double类型的超大数值的乘法运算（需要舍入，保留5位小数，小数位可自定义）
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
     * double类型的超大数值的乘法运算（需要舍入，保留5位小数，小数位可自定义）
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
     * double类型的除法运算（需要舍入，保留5位小数，小数位可自定义）
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
     * double类型的超大数值的除法运算（需要舍入，保留5位小数，小数位可自定义）
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

    /**
     * double类型的保留小数（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static double getDoubleKeepDecimals(double m1, int scale) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        double value = p1.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        return value;
    }

    /**
     * double类型的保留小数（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static String getDoubleKeepDecimalsToStr(double m1, int scale) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        String value = p1.setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString();
        return value;
    }








    /**
     * double类型的加法运算兼容（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static double additionDoubleCompatible(double m1, double m2, int scale) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        double value = p1.add(p2).setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();

        return keepDecimalsCompatibleToDou(value, scale);
    }

    /**
     * double类型的加法运算兼容（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static double additionDoubleCompatible(String m1, String m2, int scale) {
        BigDecimal p1 = new BigDecimal(m1);
        BigDecimal p2 = new BigDecimal(m2);
        double value = p1.add(p2).setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();

        return keepDecimalsCompatibleToDou(value, scale);
    }

    /**
     * double类型的加法运算兼容，可以把double类型的科学计数法转化成小数字符串，
     * 主要用来显示小数（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static String additionDoubleCompatibleToStr(String m1, String m2, int scale) {
        BigDecimal p1 = new BigDecimal(m1);
        BigDecimal p2 = new BigDecimal(m2);
        double value = p1.add(p2).setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();

        return keepDecimalsCompatibleToStr(value, scale);
    }

    /**
     * double类型的减法运算兼容（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static double subtractionDoubleCompatible(double m1, double m2, int scale) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        double value = p1.subtract(p2).setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();

        return keepDecimalsCompatibleToDou(value, scale);
    }

    /**
     * double类型的减法运算兼容（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static double subtractionDoubleCompatible(String m1, String m2, int scale) {
        BigDecimal p1 = new BigDecimal(m1);
        BigDecimal p2 = new BigDecimal(m2);
        double value = p1.subtract(p2).setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();

        return keepDecimalsCompatibleToDou(value, scale);
    }

    /**
     * double类型的加法运算兼容，可以把double类型的科学计数法转化成小数字符串，
     * 主要用来显示小数（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static String subtractionDoubleCompatibleToStr(String m1, String m2, int scale) {
        BigDecimal p1 = new BigDecimal(m1);
        BigDecimal p2 = new BigDecimal(m2);
        double value = p1.subtract(p2).setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();

        return keepDecimalsCompatibleToStr(value, scale);
    }

    /**
     * double类型的除法运算兼容（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static double multiplicationDoubleCompatible(double m1, double m2, int scale) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        double value = p1.multiply(p2).setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();

        return keepDecimalsCompatibleToDou(value, scale);
    }

    /**
     * double类型的加法运算兼容，可以把double类型的科学计数法转化成小数字符串，
     * 主要用来显示小数（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static String multiplicationDoubleCompatibleToStr(double m1, double m2, int scale) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        double value = p1.multiply(p2).setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();

        return keepDecimalsCompatibleToStr(value, scale);
    }

    /**
     * double类型的加法运算兼容，可以把double类型的科学计数法转化成小数字符串，
     * 主要用来显示小数（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static String multiplicationDoubleCompatibleToStr(String m1, String m2, int scale) {
        BigDecimal p1 = new BigDecimal(m1);
        BigDecimal p2 = new BigDecimal(m2);
        double value = p1.multiply(p2).setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();

        return keepDecimalsCompatibleToStr(value, scale);
    }

    /**
     * double类型的除法运算兼容（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static double divisionDoubleCompatible(double m1, double m2, int scale) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        double value = p1.divide(p2, scale, BigDecimal.ROUND_DOWN).doubleValue();

        return keepDecimalsCompatibleToDou(value, scale);
    }

    /**
     * double类型的加法运算兼容，可以把double类型的科学计数法转化成小数字符串，
     * 主要用来显示小数（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static String divisionDoubleCompatibleToStr(double m1, double m2, int scale) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        double value = p1.divide(p2, scale, BigDecimal.ROUND_DOWN).doubleValue();

        return keepDecimalsCompatibleToStr(value, scale);
    }

    /**
     * double类型的除法运算兼容（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @param m2
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static double divisionDoubleCompatible(String m1, String m2, int scale) {
        BigDecimal p1 = new BigDecimal(m1);
        BigDecimal p2 = new BigDecimal(m2);
        double value = p1.divide(p2, scale, BigDecimal.ROUND_DOWN).doubleValue();

        return keepDecimalsCompatibleToDou(value, scale);
    }

    /**
     * double类型的保留小数（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static double getDoubleKeepDecimalsCompatible(double m1, int scale) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        double value = p1.setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();

        return keepDecimalsCompatibleToDou(value, scale);
    }

    /**
     * double类型的加法运算兼容，可以把double类型的科学计数法转化成小数字符串，
     * 主要用来显示小数（自定义保留5位小数，至多保留5位小数，舍去后边的值不要，小数位不足5位不用补零）
     * @param m1
     * @return  不加doubleValue()则, 返回BigDecimal对象
     */
    public static String getDoubleKeepDecimalsCompatibleToStr(double m1, int scale) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        double value = p1.setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();

        return keepDecimalsCompatibleToStr(value, scale);
    }

    private static double keepDecimalsCompatibleToDou(double value, int scale) {
        //将科学计数法转化成小数
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        // 设置数的小数部分所允许的最小位数
        numberFormat.setMinimumFractionDigits(0);
        // 设置数的小数部分所允许的最大位数
        numberFormat.setMaximumFractionDigits(scale);
        String valueStr = numberFormat.format(value);

        //返回的double数据如果很大还是科学计数法的，如果需要显示，建议返回String类型的
        if (valueStr.contains(".")){
            String[] valueStrArr = valueStr.split("\\.");
            if (valueStrArr.length > 1 && valueStrArr[1].length() > 0){
                if (scale <= 0){
                    return Double.parseDouble(valueStrArr[0]);
                } else {
                    if (valueStrArr[1].length() < scale){
                        return Double.parseDouble(valueStrArr[0] + "." + valueStrArr[1]);
                    } else {
                        return Double.parseDouble(valueStrArr[0] + "." + valueStrArr[1].substring(0, scale));
                    }
                }
            } else {
                return Double.parseDouble(valueStr);
            }
        } else {
            return Double.parseDouble(valueStr);
        }
    }

    private static String keepDecimalsCompatibleToStr(double value, int scale) {
        //将科学计数法转化成小数
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        // 设置数的小数部分所允许的最小位数
        numberFormat.setMinimumFractionDigits(0);
        // 设置数的小数部分所允许的最大位数
        numberFormat.setMaximumFractionDigits(scale);
        String valueStr = numberFormat.format(value);

        if (valueStr.contains(".")){
            String[] valueStrArr = valueStr.split("\\.");
            if (valueStrArr.length > 1 && valueStrArr[1].length() > 0){
                if (scale <= 0){
                    return valueStrArr[0];
                } else {
                    if (valueStrArr[1].length() < scale){
                        return valueStrArr[0] + "." + valueStrArr[1];
                    } else {
                        return valueStrArr[0] + "." + valueStrArr[1].substring(0, scale);
                    }
                }
            } else {
                return valueStr;
            }
        } else {
            return valueStr;
        }
    }

}
