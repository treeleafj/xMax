package org.treeleafj.xmax.safe;

import org.apache.commons.collections.CollectionUtils;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * @author leaf
 * @date 2017-03-10 15:44
 */
public class XSSUtils {

    private static final Pattern SCRIPT_PATTERN = Pattern.compile("<script[^>]*?>.*?</script>");

    private static final Pattern HTML_PATTERN = Pattern.compile("<[^>]+>");

    private static final Pattern SQL_PATTERN_ANNOTATION = Pattern.compile("^.*/\\*.*?\\*/$");

    private String[] badStrs = null;

    {
        // 过滤掉的sql关键字，可以手动添加
        String badStr = "and|exec|execute|insert|select|delete|update|count|drop|chr|mid|master|truncate|"
                + "char|declare|sitename|net user|xp_cmdshell|or|create|"
                + "table|from|grant|group_concat|column_name|"
                + "information_schema.columns|table_schema|union|where|"
                + "like|//|/|%|#";
        badStrs = badStr.split("\\|");
    }

    /**
     * 是否网站的跨站脚本注入(HTML或Script)
     *
     * @param s
     * @return
     */
    public boolean isWebXSS(String s) {
        return HTML_PATTERN.matcher(s).find() || SCRIPT_PATTERN.matcher(s).find();
    }

    /**
     * 是否SQL注入
     *
     * @param s
     * @return
     */
    public boolean isSqlInject(String s) {
        return SQL_PATTERN_ANNOTATION.matcher(s).find() || sqlValidate(s);
    }

    private boolean sqlValidate(String str) {
        String[] array = str.toLowerCase().split("\\s");
        return CollectionUtils.containsAny(Arrays.asList(array), Arrays.asList(badStrs));
    }
}
