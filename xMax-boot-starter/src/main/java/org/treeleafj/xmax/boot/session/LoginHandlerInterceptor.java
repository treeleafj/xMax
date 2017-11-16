package org.treeleafj.xmax.boot.session;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.treeleafj.xmax.boot.basic.IgnoreLogin;
import org.treeleafj.xmax.exception.RetCode;
import org.treeleafj.xmax.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器,拦截所有请求,要求登录后才能访问,除非接口上标注@IgnoreLogin
 *
 * @author leaf
 * @date 2017/11/16
 */
public class LoginHandlerInterceptor implements HandlerInterceptor, InitializingBean {

    private String unLoginErrorMessage = "请先登录!";

    @Autowired(required = false)
    private SessionKey sessionKey;

    public void setSessionKey(SessionKey sessionKey) {
        this.sessionKey = sessionKey;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if (o instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) o;

            if (hm.getBeanType().getSimpleName().equals("BasicErrorController")) {
                return true;
            }

            IgnoreLogin ignoreLogin = hm.getMethodAnnotation(IgnoreLogin.class);
            if (ignoreLogin != null) {
                return true;
            }

            if (request.getSession(false) == null || request.getSession().getAttribute(sessionKey.getKey()) == null) {
                throw new ServiceException(RetCode.FAIL_UNLOGIN, unLoginErrorMessage);//未登录,抛出未登录异常
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

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.sessionKey == null) {
            this.sessionKey = SessionKeyFactory.buildDefaultSessionKey();
        }
    }
}
