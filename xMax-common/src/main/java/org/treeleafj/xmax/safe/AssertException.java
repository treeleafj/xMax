package org.treeleafj.xmax.safe;

import org.treeleafj.xmax.exception.BaseException;
import org.treeleafj.xmax.exception.RetCode;

/**
 * 断言异常,一般用于参数判断
 *
 * @Author leaf
 * 2015/6/26 0026 0:36.
 */
public class AssertException extends BaseException {

    public AssertException(String message) {
        super(RetCode.FAIL_PARAM, message);
    }

    public AssertException(String retCode, String message) {
        super(retCode, message);
    }
}
