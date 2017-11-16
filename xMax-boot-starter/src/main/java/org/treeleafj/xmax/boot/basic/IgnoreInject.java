package org.treeleafj.xmax.boot.basic;

/**
 * 不做参数注入检查
 * <p>
 * Created by leaf on 2017/5/18.
 */
public @interface IgnoreInject {

    boolean sql() default false;

    boolean html() default false;
}
