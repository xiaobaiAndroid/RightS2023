package com.android.launcher.util;

import java.math.BigDecimal;

/**
 * @description:
 * @createDate: 2023/6/5
 */
public class BigDecimalUtils {
    /**
     * 相加
     * @param floatA
     * @param floatB
     * @return
     */
    public static synchronized float add(String floatA, String floatB) {
        BigDecimal a2 = new BigDecimal(floatA);
        BigDecimal b2 = new BigDecimal(floatB);
        return a2.add(b2).floatValue();
    }

    /**
     * 相减
     * @param floatA
     * @param floatB
     * @return
     */
    public static synchronized float sub(String floatA, String floatB) {
        BigDecimal a2 = new BigDecimal(floatA);
        BigDecimal b2 = new BigDecimal(floatB);
        return a2.subtract(b2).floatValue();
    }

    /**
     * 相乘
     * @param doubleValA
     * @param doubleValB
     * @return
     */
    public static synchronized float mul(String doubleValA, String doubleValB) {
        BigDecimal a2 = new BigDecimal(doubleValA);
        BigDecimal b2 = new BigDecimal(doubleValB);
        return a2.multiply(b2).floatValue();
    }

    /**
     * 相除
     * @param doubleValA
     * @param doubleValB
     * @param scale 除不尽时指定精度
     * @return
     */
    public static synchronized float div(String doubleValA, String doubleValB, int scale) {
        BigDecimal a2 = new BigDecimal(doubleValA);
        BigDecimal b2 = new BigDecimal(doubleValB);
        return a2.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
