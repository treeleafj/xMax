package org.treeleafj.xmax.safe;

import org.apache.commons.collections.CollectionUtils;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * @author leaf
 * @date 2017-03-11 11:08
 */
public class SqlUtils {

    private static final Pattern SQL_PATTERN_ANNOTATION = Pattern.compile("^.*/\\*.*?\\*/$");

    private static String[] badStrs = null;

    static {
        // 过滤掉的sql关键字，可以手动添加
        String badStr = "and|exec|execute|insert|select|delete|update|count|drop|chr|mid|master|truncate|"
                + "char|declare|sitename|net user|xp_cmdshell|or|create|"
                + "table|from|grant|group_concat|column_name|"
                + "information_schema.columns|table_schema|union|where|"
                + "like|//|/|%|#";
        badStrs = badStr.split("\\|");
    }

    /**
     * 是否SQL注入
     *
     * @param s
     * @return
     */
    public static boolean isSqlInject(String s) {
        return SQL_PATTERN_ANNOTATION.matcher(s).find() || sqlValidate(s);
    }

    private static boolean sqlValidate(String str) {
        String[] array = str.toLowerCase().split("\\s");
        return CollectionUtils.containsAny(Arrays.asList(array), Arrays.asList(badStrs));
    }

}
