package org.treeleafj.xmax.boot;

import org.springframework.context.annotation.Bean;
import org.treeleafj.xmax.boot.exception.GlobalExceptionHandler;
import org.treeleafj.xmax.boot.handler.ClientInfoHandlerMethodArgumentResolver;
import org.treeleafj.xmax.boot.handler.ParamHandlerMethodArgumentResolver;
import org.treeleafj.xmax.boot.handler.PrintLogHandlerInerceptor;
import org.treeleafj.xmax.boot.handler.RenderHandlerMethodReturnValueHandler;
import org.treeleafj.xmax.boot.session.LoginUserSessionHandlerMethodArgumentResolver;

/**
 * Created by leaf on 2017/5/6 0006.
 */
public class XMaxBeans {

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public PrintLogHandlerInerceptor printLogHandlerInerceptor() {
        return new PrintLogHandlerInerceptor();
    }

    @Bean
    public ParamHandlerMethodArgumentResolver paramHandlerMethodArgumentResolver() {
        return new ParamHandlerMethodArgumentResolver();
    }

    @Bean
    public LoginUserSessionHandlerMethodArgumentResolver loginUserSessionHandlerMethodArgumentResolver() {
        return new LoginUserSessionHandlerMethodArgumentResolver();
    }

    @Bean
    public ClientInfoHandlerMethodArgumentResolver clientInfoHandlerMethodArgumentResolver() {
        return new ClientInfoHandlerMethodArgumentResolver();
    }

    @Bean
    public RenderHandlerMethodReturnValueHandler renderHandlerMethodReturnValueHandler() {
        return new RenderHandlerMethodReturnValueHandler();
    }
}