package org.treeleafj.xmax.export;

import org.treeleafj.xmax.exception.BaseException;

/**
 * 导出异常
 * <p>
 * Created by leaf on 2017/4/11.
 */
public class ExportException extends BaseException {

    public ExportException(String message) {
        super(message);
    }

    public ExportException(String code, String message) {
        super(code, message);
    }
}
