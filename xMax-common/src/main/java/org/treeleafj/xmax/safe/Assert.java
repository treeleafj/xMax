package org.treeleafj.xmax.safe;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.util.Collection;
import java.util.Map;

/**
 * 断言工具
 */
public class Assert {

    /**
     * 断言传入的值为true
     *
     * @param v
     * @param msg
     */
    public static void isTrue(boolean v, String msg, Object... t2) {
        if (!v) {
            throw createException(msg, t2);
        }
    }

    /**
     * 断言传入的值不为null
     *
     * @param v
     * @param msg
     */
    public static void notNull(Object v, String msg, Object... t2) {
        if (v == null) {
            throw createException(msg, t2);
        }
    }

    /**
     * 断言传入的值不为空白
     *
     * @param v
     * @param msg
     */
    public static void hasText(String v, String msg, Object... t2) {
        if (StringUtils.isBlank(v)) {
            throw createException(msg, t2);
        }
    }

    /**
     * 断言传入的字符串不包含某值
     *
     * @param str
     * @param s
     * @param msg
     */
    public static void doesNotContain(String str, String s, String msg, Object... t2) {
        if (StringUtils.isNotEmpty(str) && StringUtils.isNotEmpty(s) && str.contains(s)) {
            throw createException(msg, t2);
        }
    }

    /**
     * 断言传入的字符串/集合/数组不为空
     *
     * @param v
     * @param msg
     */
    public static void notEmpty(Object v, String msg, Object... t2) {

        if (v == null) {
            throw createException(msg, 52);
        }

        if (v instanceof String) {
            if (((String) v).isEmpty()) {
                throw createException(msg, t2);
            }
        } else if (v instanceof Collection) {
            Collection c = (Collection) v;
            if (c.size() <= 0) {
                throw createException(msg, t2);
            }
        } else if (v instanceof Map) {
            if (((Map) v).size() <= 0) {
                throw createException(msg, t2);
            }
        } else if (v.getClass().isArray()) {
            if (ArrayUtils.isEmpty((Object[]) v)) {
                throw createException(msg, t2);
            }
        }
    }

    /**
     * 字符串大于或等于指定的长度
     *
     * @param s       字符串
     * @param max     长度
     * @param message 错误信息
     */
    public static void isGreaterThanOrEqual(String s, int max, String message, Object... t2) {
        if (s == null || s.length() <= max) {
            throw createException(message, t2);
        }
    }

    /**
     * 字符串小于或等于指定的长度
     *
     * @param s       字符串
     * @param min     长度
     * @param message 错误信息
     */
    public static void isLessThanOrEqual(String s, int min, String message, Object... t2) {
        if (s == null || s.length() > min) {
            throw createException(message, t2);
        }
    }

    /**
     * 字符串长度为于min与max之间,允许等于max或min
     *
     * @param s       字符窜
     * @param max     最大长度
     * @param min     最小长度
     * @param message 错误信息
     */
    public static void isBetween(String s, int max, int min, String message, Object... t2) {
        if (s == null || (s.length() > max || s.length() < min)) {
            throw createException(message, t2);
        }
    }

    /**
     * 字符串为数字
     *
     * @param s
     * @param message
     */
    public static void isNumber(String s, String message, Object... t2) {
        if (!NumberUtils.isNumber(s)) {
            throw createException(message, t2);
        }
    }

    /**
     * 给定的数字小于或等于指定的数字
     *
     * @param number1 给定的数字
     * @param number2 指定的数字
     * @param message
     */
    public static void numberLessOrEqual(int number1, int number2, String message, Object... t2) {
        if (number1 > number2) {
            throw createException(message, t2);
        }
    }

    /**
     * 抛出异常
     *
     * @param t1 如果有传递t2,则为错误码, 不然则为错误信息
     * @param t2 错误信息,可不传,不传时以t1为准, 传时为一个时,作为错误信息, 多个时, 第一个为sl4j的日志格式的字符串,后面的为参数
     */
    private static AssertException createException(String t1, Object... t2) {
        if (t2.length == 0) {
            return createException(t1);
        } else if (t2.length == 1) {
            return createException(t1, t2[0].toString());
        } else {
            Object[] params = new Object[t2.length - 1];
            System.arraycopy(t2, 1, params, 0, params.length);
            FormattingTuple ft = MessageFormatter.arrayFormat(t2[0].toString(), params);
            return createException(t1, ft.getMessage());
        }
    }

}
