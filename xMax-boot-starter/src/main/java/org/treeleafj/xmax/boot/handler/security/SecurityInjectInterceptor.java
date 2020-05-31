package org.treeleafj.xmax.boot.handler.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.treeleafj.xmax.boot.basic.IgnoreInject;
import org.treeleafj.xmax.boot.utils.RequestUtils;
import org.treeleafj.xmax.json.Jsoner;
import org.treeleafj.xmax.safe.SqlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 安全访问拦截器
 * <p>
 * 拦截SQL注入和XSS注入
 * <p>
 * Created by leaf on 2017/5/18.
 */
public class SecurityInjectInterceptor implements HandlerInterceptor {

    private Logger log = LoggerFactory.getLogger(SecurityInjectInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws IOException {

        if (o instanceof HandlerMethod) {

            boolean checkSql = true;
            boolean checkXss = true;

            HandlerMethod mh = (HandlerMethod) o;
            if (mh.hasMethodAnnotation(IgnoreInject.class)) {
                IgnoreInject ii = mh.getMethodAnnotation(IgnoreInject.class);
                if (!ii.sql()) {
                    checkSql = false;
                }
                if (!ii.xss()) {
                    checkXss = false;
                }
            }

            String ip = RequestUtils.getIp(request);

            if (checkSql) {
                Enumeration<String> parameterNames = request.getParameterNames();
                while (parameterNames.hasMoreElements()) {
                    String name = parameterNames.nextElement();
                    String[] vals = request.getParameterValues(name);
                    for (String val : vals) {
                        if (SqlUtils.isSqlInject(val)) {
                            log.warn("用户端{}调用[{}]接口传入的参数[{}={}]为sql注入, 全部参数为:{}", ip, request.getRequestURI(), name, val, Jsoner.toJson(request.getParameterMap()));
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().println("{\"code\":\"9991\",\"msg\":\"risk requqest\"}");
                            return false;
                        }
                    }
                }
            }

            if (checkXss) {
                Enumeration<String> parameterNames = request.getParameterNames();
                while (parameterNames.hasMoreElements()) {
                    String name = parameterNames.nextElement();
                    String[] vals = request.getParameterValues(name);
                    for (String val : vals) {
                        if (XssValidatior.isXss(name, val)) {
                            log.warn("用户端{}调用[{}]接口传入的参数[{}={}]为xss注入, 全部参数为:{}", ip, request.getRequestURI(), name, val, Jsoner.toJson(request.getParameterMap()));
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().println("{\"code\":\"9991\",\"msg\":\"risk requqest\"}");
                            return false;
                        }
                    }
                }
            }

        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {

    }
}
