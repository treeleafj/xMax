package org.treeleafj.xmax.boot.basic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略日志打印
 *
 * Created by leaf on 2017/12/4.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreLogPrint {

    /**
     * 是否忽略请求入参的打印, 默认忽略
     */
    boolean ignoreIn() default true;

    /**
     * 是否忽略请求出去时耗时打印, 默认忽略
     */
    boolean ignoreOut() default true;

}
