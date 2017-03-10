package org.treeleafj.xmax.exception;

/**
 * 逻辑异常,做为各种自定义异常的父类
 * Created by leaf on 2015/7/13.
 */
public abstract class BaseException extends RuntimeException implements RetCodeSupport {

    private String code = RetCode.FAIL_UNKNOWN;

    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
