package org.treeleafj.xmax.export.format;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treeleafj.xmax.export.ExportException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by leaf on 2017/4/11.
 */
public class TagUtils {

    private static Logger log = LoggerFactory.getLogger(TagUtils.class);

    /**
     * 是否#foreach循环的开始标签
     *
     * @param v 模板单元格上的内容
     */
    public static boolean isForeachBegin(String v) {
        return StringUtils.startsWith(v, "#foreach ");
    }

    /**
     * 是否foreach循环的结束标签
     *
     * @param v 模板单元格上的内容
     */
    public static boolean isForeachEnd(String v) {
        return StringUtils.equalsIgnoreCase(StringUtils.trim(v), "#end");
    }

    /**
     * 是否#call标签
     *
     * @param v 模板单元格上的内容
     */
    public static boolean isCall(String v) {
        return StringUtils.startsWith(v, "#call ");
    }

    /**
     * 是否#end标签
     *
     * @param v 模板单元格上的内容
     */
    public static boolean isVar(String v) {
        return StringUtils.startsWith(v, "${");
    }

    /**
     * 解析foreach标签,解析出临时item名称和集合变量
     *
     * @param tag
     * @return
     */
    public static String[] parseForeachTag(String tag) {
        //#foreach item in ${pageInfo.rows}
        String[] split = tag.substring(9).split(" in ");
        if (split.length != 2) {
            throw new ExportException("", "解析" + tag + "失败");
        }
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].trim();
        }
        return split;
    }

    /**
     * 解析标签的值
     *
     * @param tag
     * @param data
     * @return
     */
    public static Object parseValue(String tag, Map<String, Object> data) {
        tag = tag.trim();
        if (isCall(tag)) {//方法调用标签
            //#call xlsDataFormatter.formatDate(${item.accDate},"yyyy-MM-dd")
            String name = tag.substring(5).trim();
            int start = name.indexOf('(');
            int end = name.lastIndexOf(')');
            if (start < 0 || end < 0) {
                throw new ExportException("", "模板解析失败:" + tag);
            }

            String method = name.substring(0, start);
            String arg = name.substring(start + 1, end);

            StringTokenizer st = new StringTokenizer(method, ".");

            String objName = st.nextToken();
            String methodName = st.nextToken();

            String[] args = arg.split(",");
            List<Object> argObjs = new ArrayList<Object>(args.length);

            for (int i = 0; i < args.length; i++) {
                String s = args[i].trim();
                Object paramValue = parseValue(s, data);
                argObjs.add(paramValue);
            }

            Object o = data.get(objName);
            if (o == null) {
                return null;
            }
            return invokeMethod(o, methodName, argObjs);
        } else if (isVar(tag)) {//变量标签
            //${item.xx}
            String name = tag.replace("${", "").replace("}", "");
            String[] split = name.split("\\.");
            Object v = data.get(split[0]);
            for (int i = 1; i < split.length; i++) {
                if (v == null) {
                    return null;
                }
                if (v instanceof Map) {
                    Map tmp = (Map) v;
                    v = tmp.get(split[i]);
                } else {
                    v = getFieldValue(v, split[i]);
                }
            }
            return v;
        } else {
            if ("true".equals(tag) || "false".equals(tag)) {//是boolean值
                return Boolean.parseBoolean(tag);
            } else if (NumberUtils.isCreatable(tag)) {//是数字
                if (tag.contains(".")) {
                    return Double.parseDouble(tag);
                } else {
                    return Integer.parseInt(tag);
                }
            } else if (tag.startsWith("\"") && tag.endsWith("\"")) {//是字符串
                return tag.substring(1, tag.length() - 1);
            }
        }
        return null;
    }

    private static Object invokeMethod(Object o, String methodName, List<Object> argObjs) {
        Class[] paramTypes = new Class[argObjs.size()];//参数类型
        for (int i = 0; i < argObjs.size(); i++) {
            if (argObjs.get(i) == null) {
                return StringUtils.EMPTY;
            }
            paramTypes[i] = argObjs.get(i).getClass();
        }
        Method method = null;
        Class clazz = o.getClass();
        while (clazz != null) {
            try {
                method = clazz.getDeclaredMethod(methodName, paramTypes);
            } catch (Exception e) {
                //no such method exception
            }

            if (method != null) {
                break;
            }

            clazz = clazz.getSuperclass();
        }

        if (method == null) {
            log.error("查询不到方法:{}.{}", o.getClass().getName(), methodName);
            return StringUtils.EMPTY;
        }

        try {
            return method.invoke(o, argObjs.toArray(new Object[argObjs.size()]));
        } catch (Exception e) {
            log.error("导出工具调用方法{}.{}失败", o.getClass().getName(), methodName, e);
        }
        return null;
    }

    private static Object getFieldValue(Object o, String name) {
        try {
            Field field = o.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(o);
        } catch (Exception e) {
            throw new ExportException("", name + "属性不存在,无法访问");
        }
    }
}
