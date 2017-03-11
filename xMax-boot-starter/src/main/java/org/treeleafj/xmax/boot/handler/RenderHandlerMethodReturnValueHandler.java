package org.treeleafj.xmax.boot.handler;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.treeleafj.xmax.boot.basic.Render;

/**
 * 处理Controller返回Render的结果
 * <p>
 * Created by leaf on 2015/4/29.
 *
 * @see org.treeleafj.xmax.boot.basic.RedirectRender
 * @see org.treeleafj.xmax.boot.basic.JsonRender
 * @see org.treeleafj.xmax.boot.basic.TextRender
 */
public class RenderHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return Render.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue,
                                  MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {

        Render render = (Render) returnValue;
        mavContainer.setView(new RenderView(render));
    }

}
