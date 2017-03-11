package org.treeleafj.xmax.boot.session;

import org.springframework.beans.factory.annotation.Autowired;
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

    private SessionKey sessionKey = new SessionKey("_login_user");

    @Autowired(required = false)
    private LoginStoreStrategy loginStoreStrategy;

    public void setSessionKey(SessionKey sessionKey) {
        this.sessionKey = sessionKey;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);

        LoginUser loginUserAnno = methodParameter.getParameterAnnotation(LoginUser.class);

        Class<?> parameterType = methodParameter.getParameterType();

        Object returnValue = request.getSession().getAttribute(sessionKey.getKey());

        //有存储策略,则使用存储策略,如果没有,采用默认的,且只支持返回登录用户对象,不支持返回登录用户的ID
        if (loginStoreStrategy != null && returnValue != null) {

            if (loginUserAnno.reload()) {
                returnValue = loginStoreStrategy.reload(returnValue);
            }

            if (String.class.isAssignableFrom(parameterType)) {
                returnValue = loginStoreStrategy.getLoginUserId(returnValue);
            } else {
                returnValue = loginStoreStrategy.getLoginUser(returnValue);
            }
        }

        if (returnValue != null) {
            return returnValue;
        }

        if (loginUserAnno.require()) {
            throw new ServiceException(RetCode.FAIL_UNLOGIN, unLoginErrorMessage);//未登录,抛出未登录异常
        }

        return null;
    }
}
