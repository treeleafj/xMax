package org.treeleafj.xmax.boot.basic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 不做参数注入检查
 * <p>
 * Created by leaf on 2017/5/18.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreInject {

    boolean sql() default false;

    boolean html() default false;
}
