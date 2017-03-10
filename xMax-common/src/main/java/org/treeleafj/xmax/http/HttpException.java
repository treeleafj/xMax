package org.treeleafj.xmax.http;

import org.treeleafj.xmax.exception.BaseException;
import org.treeleafj.xmax.exception.RetCode;

/**
 * Created by leaf on 2015/6/29.
 */
public class HttpException extends BaseException {

    public HttpException(String message) {
        super(RetCode.FAIL_REMOTE, message);
    }

    public HttpException(String code, String message) {
        super(code, message);
    }

    public HttpException(String message, Throwable t) {
        super(RetCode.FAIL_REMOTE, message, t);
    }

    public HttpException(String code, String message, Throwable t) {
        super(code, message, t);
    }
}
