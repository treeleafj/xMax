package org.treeleafj.xmax.safe;

import java.util.regex.Pattern;

/**
 * @author leaf
 * @date 2017-03-10 15:44
 */
public class XSSUtils {

    private static final Pattern SCRIPT_PATTERN = Pattern.compile("<script[^>]*?>.*?</script>");

    private static final Pattern HTML_PATTERN = Pattern.compile("<[^>]+>");

    /**
     * 是否网站的跨站脚本注入(HTML或Script)
     *
     * @param s
     * @return
     */
    public boolean isWebXSS(String s) {
        return HTML_PATTERN.matcher(s).find() || SCRIPT_PATTERN.matcher(s).find();
    }

}
