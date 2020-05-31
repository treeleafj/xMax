package org.treeleafj.xmax.boot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.treeleafj.xmax.boot.exception.GlobalExceptionHandler;
import org.treeleafj.xmax.boot.handler.ClientInfoHandlerMethodArgumentResolver;
import org.treeleafj.xmax.boot.handler.ParamHandlerMethodArgumentResolver;
import org.treeleafj.xmax.boot.handler.PrintLogHandlerInerceptor;
import org.treeleafj.xmax.boot.handler.RenderHandlerMethodReturnValueHandler;
import org.treeleafj.xmax.boot.handler.security.SecurityInjectInterceptor;
import org.treeleafj.xmax.boot.session.LoginHandlerInterceptor;
import org.treeleafj.xmax.boot.session.LoginUserSessionHandlerMethodArgumentResolver;

/**
 * Created by leaf on 2017/5/6 0006.
 */
public class XMaxBeans {

    /**
     * 全局异常拦截转译和打印
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    /**
     * 全局请求日志打印
     */
    @Bean
    public PrintLogHandlerInerceptor printLogHandlerInerceptor() {
        return new PrintLogHandlerInerceptor();
    }

    /**
     * {@link org.treeleafj.xmax.boot.basic.Param} controller方法入参为Param类型时的转译器
     */
    @Bean
    public ParamHandlerMethodArgumentResolver paramHandlerMethodArgumentResolver() {
        return new ParamHandlerMethodArgumentResolver();
    }

    /**
     * {@link org.treeleafj.xmax.boot.basic.LoginUser} 该登录用户注解处理
     */
    @Bean
    public LoginUserSessionHandlerMethodArgumentResolver loginUserSessionHandlerMethodArgumentResolver() {
        return new LoginUserSessionHandlerMethodArgumentResolver();
    }

    /**
     * {@link org.treeleafj.xmax.bean.ClientInfo} controller方法入参为ClientInfo类型时的转译器
     */
    @Bean
    public ClientInfoHandlerMethodArgumentResolver clientInfoHandlerMethodArgumentResolver() {
        return new ClientInfoHandlerMethodArgumentResolver();
    }

    /**
     * {@link org.treeleafj.xmax.boot.basic.Render} controller方法返回值为Render类型时的转译器
     */
    @Bean
    public RenderHandlerMethodReturnValueHandler renderHandlerMethodReturnValueHandler() {
        return new RenderHandlerMethodReturnValueHandler();
    }

    @Bean
    public SecurityInjectInterceptor sqlInjectInterceptor() {
        return new SecurityInjectInterceptor();
    }

    @Bean
    public LoginHandlerInterceptor loginHandlerInterceptor() {
        return new LoginHandlerInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }
}
