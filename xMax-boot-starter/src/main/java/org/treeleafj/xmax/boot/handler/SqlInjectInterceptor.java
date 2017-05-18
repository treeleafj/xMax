package org.treeleafj.xmax.boot.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.treeleafj.xmax.boot.basic.DontCheckParam;
import org.treeleafj.xmax.boot.utils.RequestUtils;
import org.treeleafj.xmax.json.Jsoner;
import org.treeleafj.xmax.safe.SqlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * Created by leaf on 2017/5/18.
 */
public class SqlInjectInterceptor implements HandlerInterceptor {

    private Logger log = LoggerFactory.getLogger(SqlInjectInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

        if (o instanceof HandlerMethod) {
            HandlerMethod mh = (HandlerMethod) o;
            if (mh.hasMethodAnnotation(DontCheckParam.class)) {
                return true;
            }
            while (parameterNames.hasMoreElements()) {
                String name = parameterNames.nextElement();
                String[] vals = httpServletRequest.getParameterValues(name);
                for (String val : vals) {
                    if (SqlUtils.isSqlInject(val)) {
                        String ip = RequestUtils.getIp(httpServletRequest);
                        log.info("用户端{}传入的参数{}为sql注入, 全部参数为:{}", ip, val, Jsoner.toJson(httpServletRequest.getParameterMap()));
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
