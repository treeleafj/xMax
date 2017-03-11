package org.treeleafj.xmax.boot.handler;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.treeleafj.xmax.bean.ClientInfo;
import org.treeleafj.xmax.boot.utils.RequestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 客户端信息处理
 * <p>
 * 专门处理方法入参上的ClientInfo类型的构建
 *
 * @Author leaf
 * 2015/9/4 0004 18:52.
 */
public class ClientInfoHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return ClientInfo.class.equals(methodParameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);

        ClientInfo clientInfo = new ClientInfo();
        if (request != null) {
            clientInfo.setIp(RequestUtils.getIp(request));
            clientInfo.setMobile(RequestUtils.isMobile(request));
        }

        return clientInfo;
    }
}
