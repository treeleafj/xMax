package org.treeleafj.xmax.boot.basic;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Map;

/**
 * @author leaf
 * @date 2017-03-11 17:35
 */
public abstract class Render implements Serializable {

    /**
     * 返回html协议的网页
     */
    public final static String CONTENT_TYPE_HTML = "text/html";

    /**
     * 返回纯文本
     */
    public final static String CONTENT_TYPE_TEXT = "text/plain";

    /**
     * 返回xml格式
     */
    public final static String CONTENT_TYPE_XML = "text/xml";

    /**
     * 返回json格式
     */
    public final static String CONTENT_TYPE_JSON = "application/json";

    /**
     * 返回javacript格式
     */
    public final static String CONTENT_TYPE_JAVASCRIPT = "application/javascript";

    public static Render json() {
        return new JsonRender();
    }

    public static Render json(Object obj) {
        return new JsonRender(obj);
    }

    public static Render json(String msg) {
        return new JsonRender(msg);
    }

    public static Render json(String code, String msg) {
        return new JsonRender(code, msg);
    }

    public static Render redirect(String path) {
        return new RedirectRender(path);
    }

    public static Render redirect(String path, Map<String, String> param) {
        return new RedirectRender(path, param);
    }

    public abstract void render(HttpServletResponse response);
}
