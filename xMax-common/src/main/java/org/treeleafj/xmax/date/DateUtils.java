package org.treeleafj.xmax.date;

import org.treeleafj.xmax.exception.ServiceException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 日期工具
 *
 * @author leaf
 * @date 2017-03-10 15:32
 */
public class DateUtils {

    /**
     * yyyy-MM-dd HH:mm:ss.SSS
     */
    public static final String DATE_TIME_MILLISECOND_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * HH:mm:ss
     */
    public static final String TIME_PATTERN = "HH:mm:ss";

    public static String format(Date date, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static String formatDateTime(Date date) {
        return format(date, DATE_TIME_PATTERN);
    }

    public static String formatDate(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static Date parse(String date, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(date);
        } catch (ParseException e) {
            throw new ServiceException("日期格式错误", e);
        }
    }

    public static Date parseDateTime(String date) {
        return parse(date, DATE_TIME_PATTERN);
    }

    public static Date parseDate(String date) {
        return parse(date, DATE_PATTERN);
    }

    /**
     * 以时间单位比较两个日期间相差的值,结果采用向下取整,即不满一天算0天, 不满一分钟算0分钟
     *
     * @return 相差的天数, d1小d2则返回负数
     */
    public static long compare(Date d1, Date d2, TimeUnit timeUnit) {
        long t = d1.getTime() - d2.getTime();
        return timeUnit.convert(t, TimeUnit.MILLISECONDS);
    }
}
