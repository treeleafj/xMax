package org.treeleafj.xmax.exception;

/**
 * 服务逻辑异常
 * <p/>
 * Created by leaf on 2015/7/13.
 */
public class ServiceException extends BaseException {

    public ServiceException(String message) {
        super(RetCode.FAIL_LOGIC, message);
    }

    public ServiceException(String code, String message) {
        super(code, message);
    }

    public ServiceException(String message, Throwable throwable) {
        super(RetCode.FAIL_LOGIC, message, throwable);
    }

    public ServiceException(String code, String message, Throwable throwable) {
        super(code, message, throwable);
    }
}
