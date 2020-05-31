package org.treeleafj.xmax.boot.handler.security;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treeleafj.xmax.json.Jsoner;

import java.util.List;
import java.util.Map;

/**
 * XSS安全校验器
 */
public class XssValidatior {

    private static Logger logger = LoggerFactory.getLogger(XssValidatior.class);

    private static String[] VALIDCHARS = new String[]{"&", "\"", "'", "<", ">"};

    public static boolean isXss(String name, String value) {
        if (StringUtils.containsAny(name, VALIDCHARS)) {
            return true;
        }

        if (StringUtils.isBlank(value)) {
            return false;
        }

        if (value.contains("\"")) {
            if (isJsonXss(value)) {
                return true;
            }
            return false;
        } else {
            return StringUtils.containsAny(value, VALIDCHARS);
        }
    }

    private static boolean isJsonXss(String value) {

        try {
            if (value.startsWith("[") && value.endsWith("]")) {
                List list = Jsoner.toArray(value, Map.class);
                return objIsXss(list);
            } else if (value.startsWith("{") && value.endsWith("}")) {
                Map map = Jsoner.toObj(value, Map.class);
                return objIsXss(map);
            } else {
                return true;
            }
        } catch (Exception var3) {
            logger.warn("转译{}为json对象失败", value);
            return true;
        }
    }

    private static boolean objIsXss(Object obj) {

        if (obj == null) {
            return false;
        }

        if (obj instanceof String) {
            String value = (String) obj;
            return isXss(null, value);
        } else {
            if (obj instanceof List) {
                List list = (List) obj;
                for (Object o : list) {
                    if (objIsXss(o)) {
                        return true;
                    }
                }
            } else if (obj instanceof Map) {
                Map<String, Object> map = (Map) obj;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (objIsXss(entry.getValue())) {
                        return true;
                    }
                    if (objIsXss(entry.getKey())) {
                        return true;
                    }
                }
            } else {
                return isXss(null, obj.toString());
            }
            return false;
        }
    }
}
