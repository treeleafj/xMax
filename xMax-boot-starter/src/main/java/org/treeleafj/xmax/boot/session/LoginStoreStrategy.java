package org.treeleafj.xmax.boot.session;

import java.io.Serializable;

/**
 * 登录缓存策略
 *
 * @author leaf
 * @date 2017-03-11 10:26
 */
public interface LoginStoreStrategy {

    /**
     * 获取登录用户ID
     *
     * @param obj
     * @return
     */
    Serializable getLoginUserId(Object obj);

    /**
     * 获取登录用户
     *
     * @param obj
     * @return
     */
    Object getLoginUser(Object obj);

    /**
     * 重新加载用户数据
     *
     * @param object
     * @return
     */
    Object reload(Object object);
}
