package org.treeleafj.xmax.exception;

/**
 * 通用错误码
 *
 * @Author leaf
 * 2015/8/28 0028 1:08.
 */
public abstract class RetCode {

    /**
     * 成功
     */
    public final static String OK = "0000";

    /**
     * 未知异常
     */
    public final static String FAIL_UNKNOWN = "99999";

    /**
     * 逻辑错误
     */
    public final static String FAIL_LOGIC = "9998";

    /**
     * 参数异常
     */
    public final static String FAIL_PARAM = "9997";

    /**
     * 缓存操作异常
     */
    public final static String FAIL_CACHE = "9996";

    /**
     * 数据库操作异常
     */
    public final static String FAIL_DB = "9995";

    /**
     * 远程访问异常
     */
    public final static String FAIL_REMOTE = "9994";

    /**
     * 未登陆异常
     */
    public final static String FAIL_UNLOGIN = "9993";

    /**
     * 禁止访问异常
     */
    public final static String FAIL_FORBIDDEN = "9992";
}
