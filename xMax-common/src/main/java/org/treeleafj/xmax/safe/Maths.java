package org.treeleafj.xmax.safe;

import java.math.BigDecimal;

/**
 * 数学计算工具
 *
 * @Author leaf
 * 2016/2/24 0024 1:52.
 */
public class Maths {

    /**
     * 多个数字相加
     *
     * @param v
     * @return
     */
    public static Double add(Double... v) {
        BigDecimal r = new BigDecimal(v[0].toString());
        for (int i = 1; i < v.length; i++) {
            r = r.add(new BigDecimal(v[i].toString()));
        }
        return r.doubleValue();
    }

    /**
     * 多个数字相减
     *
     * @param v
     * @return
     */
    public static Double subtract(Double... v) {
        BigDecimal r = new BigDecimal(v[0].toString());
        for (int i = 1; i < v.length; i++) {
            r = r.subtract(new BigDecimal(v[i].toString()));
        }
        return r.doubleValue();
    }

    /**
     * 多个数字相乘
     *
     * @param v
     * @return
     */
    public static Double multiply(Double... v) {
        BigDecimal r = new BigDecimal(v[0].toString());
        for (int i = 1; i < v.length; i++) {
            r = r.multiply(new BigDecimal(v[i].toString()));
        }
        return r.doubleValue();
    }

    /**
     * 多个数字相除
     *
     * @param v
     * @return
     */
    public static Double divide(Double... v) {
        BigDecimal r = new BigDecimal(v[0].toString());
        for (int i = 1; i < v.length; i++) {
            r = r.divide(new BigDecimal(v[i].toString()));
        }
        return r.doubleValue();
    }

    /**
     * 多个数字相除,保留指定位数
     *
     * @param scale        要保留的位数
     * @param roundingMode 小数省略的模式,参见:BigDecimal.ROUND_HALF_UP等
     * @param v
     * @return
     */
    public static Double divide(int scale, int roundingMode, Double... v) {
        BigDecimal r = new BigDecimal(v[0].toString());
        for (int i = 1; i < v.length; i++) {
            r = r.divide(new BigDecimal(v[i].toString()), scale, roundingMode);
        }
        return r.doubleValue();
    }

    public static void main(String[] args) {
        System.out.println(add(0.2, 0.1, 0.1));
        System.out.println(subtract(0.2, 0.1, 0.1, 0.1));
        System.out.println(multiply(0.2, 0.1, 0.1));
        System.out.println(divide(0.2, 0.1, 0.1));
        System.out.println(divide(2, BigDecimal.ROUND_HALF_UP, 0.211, 0.4));
    }
}
