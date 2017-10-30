package org.treeleafj.xmax.boot;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.treeleafj.xmax.boot.exception.GlobalExceptionHandler;
import org.treeleafj.xmax.boot.handler.*;
import org.treeleafj.xmax.boot.session.LoginUserSessionHandlerMethodArgumentResolver;
import org.treeleafj.xmax.date.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author leaf
 * @date 2017-03-10 16:00
 */
@Data
@ConfigurationProperties("xMax")
public class XMaxConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private PrintLogHandlerInerceptor printLogHandlerInerceptor;

    @Autowired
    private SqlInjectInterceptor sqlInjectInterceptor;

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
     * 是否打印接口访问日志
     */
    private boolean printLog = true;

    /**
     * 是否添加参数检测拦截器检查参数中的sql注入等问题
     */
    private boolean checkParam = false;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(Date.class, new DateFormatter() {

            @Override
            public Date parse(String text, Locale locale) throws ParseException {
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
        super.addReturnValueHandlers(returnValueHandlers);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(paramHandlerMethodArgumentResolver);
        argumentResolvers.add(clientInfoHandlerMethodArgumentResolver);
        argumentResolvers.add(loginUserSessionHandlerMethodArgumentResolver);
        super.addArgumentResolvers(argumentResolvers);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (checkParam) {
            registry.addInterceptor(sqlInjectInterceptor);
        }
        if (printLog) {
            registry.addInterceptor(printLogHandlerInerceptor);//添加接口调用打印
        }
        super.addInterceptors(registry);
    }
}
