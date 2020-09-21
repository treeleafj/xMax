package org.treeleafj.xmax.boot;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.treeleafj.xmax.boot.exception.GlobalExceptionHandler;
import org.treeleafj.xmax.boot.handler.ClientInfoHandlerMethodArgumentResolver;
import org.treeleafj.xmax.boot.handler.ParamHandlerMethodArgumentResolver;
import org.treeleafj.xmax.boot.handler.PrintLogHandlerInerceptor;
import org.treeleafj.xmax.boot.handler.RenderHandlerMethodReturnValueHandler;
import org.treeleafj.xmax.boot.handler.security.SecurityInjectInterceptor;
import org.treeleafj.xmax.boot.session.LoginHandlerInterceptor;
import org.treeleafj.xmax.boot.session.LoginUserSessionHandlerMethodArgumentResolver;
import org.treeleafj.xmax.date.DateUtils;
import org.treeleafj.xmax.exception.RetCode;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author leaf
 * @date 2017-03-10 16:00
 */
@Data
@ConfigurationProperties("xmax")
public class XMaxConfig implements WebMvcConfigurer {

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private PrintLogHandlerInerceptor printLogHandlerInerceptor;

    @Autowired
    private SecurityInjectInterceptor securityInjectInterceptor;

    @Autowired
    private LoginHandlerInterceptor loginHandlerInterceptor;

    @Autowired
    private ParamHandlerMethodArgumentResolver paramHandlerMethodArgumentResolver;

    @Autowired
    private LoginUserSessionHandlerMethodArgumentResolver loginUserSessionHandlerMethodArgumentResolver;

    @Autowired
    private ClientInfoHandlerMethodArgumentResolver clientInfoHandlerMethodArgumentResolver;

    @Autowired
    private RenderHandlerMethodReturnValueHandler renderHandlerMethodReturnValueHandler;

    /**
     * 页面错误跳转到的视图页面地址
     */
    private String errorView = "error";

    /**
     * 未知错误的提示代码
     */
    private String unknownErrorCode = RetCode.FAIL_UNKNOWN;

    /**
     * 未知错误的提示信息
     */
    private String unknownErrorMsg = "网络繁忙,请稍后尝试!";

    /**
     * 是否打印接口访问日志
     */
    private boolean printLog = true;

    /**
     * 是否添加参数检测拦截器检查参数中的sql注入等问题
     */
    private boolean checkParam = false;

    /**
     * 是否启用登录拦截器,判断必须要登录,默认false不启用
     */
    private boolean checkLogin = false;

    /**
     * 默认错误格式
     */
    private String defaultErrorFormat = "html";

    /**
     * 是否启用@LoginUser的解析
     */
    private boolean enableLoginUser = true;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(Date.class, new DateFormatter() {

            @Override
            public Date parse(String text, Locale locale) {
                if (text.length() <= DateUtils.DATE_PATTERN.length()) {
                    return DateUtils.parseDate(text);
                } else {
                    return DateUtils.parseDateTime(text);
                }
            }
        });
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(renderHandlerMethodReturnValueHandler);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(paramHandlerMethodArgumentResolver);
        argumentResolvers.add(clientInfoHandlerMethodArgumentResolver);
        if (enableLoginUser) {
            argumentResolvers.add(loginUserSessionHandlerMethodArgumentResolver);
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (checkParam) {
            registry.addInterceptor(securityInjectInterceptor);
        }
        if (printLog) {
            //添加接口调用打印
            registry.addInterceptor(printLogHandlerInerceptor);
        }
        if (checkLogin) {
            registry.addInterceptor(loginHandlerInterceptor);
        }
    }
}
