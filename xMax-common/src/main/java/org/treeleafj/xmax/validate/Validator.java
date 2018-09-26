package org.treeleafj.xmax.validate;

/**
 * 验证器统一接口
 *
 * @param <T>
 * @author leaf
 * @date 2014-11-5 下午10:11:00
 */
public interface Validator<T> {

    /**
     * 验证
     * @param obj 要验证的对象
     * @return 是否验证通过
     */
    boolean validate(T obj);
}
