package org.treeleafj.xmax.boot.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treeleafj.xmax.json.Jsoner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author leaf
 * @date 2017-03-10 16:14
 */
public class RequestUtils {

    private static Logger log = LoggerFactory.getLogger(RequestUtils.class);


    /**
     * \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),
     * 字符串在编译时会被转码一次,所以是 "\\b"
     * \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)
     */
    private static final String PHONE_REG = "\\b(ip(hone|od)|android|opera m(ob|in)i"
            + "|windows (phone|ce)|blackberry"
            + "|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"
            + "|laystation portable)|nokia|fennec|htc[-_]"
            + "|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";

    private static final String TABLE_REG = "\\b(ipad|tablet|(Nexus 7)|up.browser"
            + "|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";

    /**
     * 移动设备正则匹配：手机端
     */
    private static final Pattern PHONE_PAT = Pattern.compile(PHONE_REG, Pattern.CASE_INSENSITIVE);

    /**
     * 移动设备正则匹配：平板
     */
    private static final Pattern TABLE_PAT = Pattern.compile(TABLE_REG, Pattern.CASE_INSENSITIVE);

    /**
     * 判断客户端是否移动端
     *
     * @param request
     * @return
     */
    public static boolean isMobile(HttpServletRequest request) {
        if ("0".equals(request.getParameter("_m"))) {
            return false;
        }

        if (StringUtils.isNotBlank(request.getHeader("x-wap-profile"))) {
            return true;
        }

        String userAgent = StringUtils.defaultString(request.getHeader("USER-AGENT")).toLowerCase();

        // 匹配
        return PHONE_PAT.matcher(userAgent).find() || TABLE_PAT.matcher(userAgent).find();
    }


    /**
     * 判断客户端是否ajax请求
     */
    public static boolean isAjax(HttpServletRequest request) {
        String ajax = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(ajax)) {
            return true;
        }
        String accept = request.getHeader("Accept");
        return accept != null && accept.contains("json");
    }

    /**
     * 判断客户端是否采用json方式的数据格式请求
     */
    public static boolean isJson(HttpServletRequest request) {
        String contentType = request.getHeader("CONTENT-TYPE");
        return StringUtils.contains(contentType, "application/json");
    }

    /**
     * 写入json返回
     *
     * @param model
     * @param response
     */
    public static void writeJson(Object model, HttpServletResponse response) {
        try {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(Jsoner.toJson(model));
        } catch (IOException e) {
            log.error("JSON输出出错", e);
        }
    }

    /**
     * 获取客户端IP
     *
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr();
        }

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

}
