package org.treeleafj.xmax.boot.handler;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.treeleafj.xmax.boot.basic.Param;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 对接口入参Map类型处理
 *
 * @Author leaf
 * 2015/9/4 0004 13:17.
 */
public class ParamHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(Param.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        Iterator<String> parameterNames = nativeWebRequest.getParameterNames();
        Map<String, String> param = new HashMap<>();
        while (parameterNames.hasNext()) {
            String name = parameterNames.next();
            param.put(name, nativeWebRequest.getParameter(name));
        }
        return new Param(param);
    }
}
