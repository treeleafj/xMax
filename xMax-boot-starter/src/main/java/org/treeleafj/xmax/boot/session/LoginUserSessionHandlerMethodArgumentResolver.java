package org.treeleafj.xmax.boot.session;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.treeleafj.xmax.boot.basic.LoginUser;
import org.treeleafj.xmax.exception.RetCode;
import org.treeleafj.xmax.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;

/**
 * 专门处理接口方法上获取登录对象,解耦HttpServletRequest
 *
 * @author leaf
 * @date 2016-10-25 12:18
 */
public class LoginUserSessionHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private String unLoginErrorMessage = "请先登录!";

    private SessionKey sessionKey;

    public void setSessionKey(SessionKey sessionKey) {
        this.sessionKey = sessionKey;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);

        Object logUser = request.getSession().getAttribute(sessionKey.getKey());
        if (logUser != null) {
            return logUser;
        }

        LoginUser loginUserAnno = methodParameter.getParameterAnnotation(LoginUser.class);
        if (loginUserAnno.require()) {
            throw new ServiceException(RetCode.FAIL_UNLOGIN, unLoginErrorMessage);//未登录,抛出未登录异常
        }

        return null;
    }
}
