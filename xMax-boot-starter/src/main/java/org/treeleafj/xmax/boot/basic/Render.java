package org.treeleafj.xmax.boot.basic;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;
import org.treeleafj.xmax.http.HttpHeader;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author leaf
 * @date 2017-03-11 17:35
 */
public abstract class Render implements Serializable {

    private static Logger log = LoggerFactory.getLogger(Render.class);

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

    /**
     * 返回一个code为000的json数据
     */
    public static Render json() {
        return new JsonRender();
    }

    /**
     * 返回一个json数据
     *
     * @param obj 要返回的数据
     */
    public static Render json(Object obj) {
        return new JsonRender(obj);
    }

    /**
     * 返回一个code为0000的json数据
     *
     * @param msg 返回描述信息
     */
    public static Render json(String msg) {
        return new JsonRender(msg);
    }

    /**
     * 返回一个json数据
     *
     * @param code 返回码
     * @param msg  返回描述信息
     */
    public static Render json(String code, String msg) {
        return new JsonRender(code, msg);
    }

    /**
     * 控制页面Redirect跳转
     *
     * @param path 跳转的路径
     */
    public static Render redirect(String path) {
        return new RedirectRender(path);
    }

    /**
     * 控制页面Redirect跳转
     *
     * @param path  跳转的路径
     * @param param 要带在url上的参数
     */
    public static Render redirect(String path, Map<String, String> param) {
        return new RedirectRender(path, param);
    }

    /**
     * 返回一个特定文本
     *
     * @param text 文本内容
     */
    public static Render text(String text) {
        return new TextRender(text);
    }

    /**
     * 返回一个特定文本
     *
     * @param contentType http的Content-Type
     * @param text        文本内容
     */
    public static Render text(String contentType, String text) {
        return new TextRender(contentType, text);
    }

    /**
     * 返回一个特定文本
     *
     * @param contentType http的Content-Type
     * @param text        文本内容
     * @param charset     字符集
     */
    public static Render text(String contentType, String text, String charset) {
        return new TextRender(contentType, text, charset);
    }

    /**
     * 同步返回一个数据流
     *
     * @param in 数据流
     */
    public static Render io(InputStream in) {
        return new IORender(in);
    }

    /**
     * 同步返回一个数据流
     *
     * @param contentType http的Content-Type
     * @param in          数据流
     */
    public static Render io(String contentType, InputStream in) {
        return new IORender(contentType, in);
    }

    /**
     * 异步处理数据
     */
    public static Render async(Callable callable) {
        return new AsyncRender(callable);
    }

    /**
     * 异步输出数据
     *
     * @param in 输入流
     */
    public static Render async(InputStream in, HttpHeader... headers) {
        HttpServletResponse response = ((ServletWebRequest) RequestContextHolder.getRequestAttributes()).getResponse();
        return new AsyncRender(() -> {

            if (headers.length > 0) {
                headers[0].forEach(name -> {
                    response.setHeader(name, headers[0].getHeader(name));
                });
            }

            try (
                    ServletOutputStream out = response.getOutputStream();
            ) {
                IOUtils.copy(in, out);
            } catch (IOException e) {
                log.info("输出文件错误", e);
            } finally {
                IOUtils.closeQuietly(in);
            }
            return "";
        });
    }

    /**
     * 返回一个特定状态码
     *
     * @param status 状态码
     * @return
     */
    public static Render status(int status) {
        return new StatusRender(status);
    }

    /**
     * 由子类实现的渲染方法
     * @param response http servlet response
     */
    public abstract void render(HttpServletResponse response);
}
