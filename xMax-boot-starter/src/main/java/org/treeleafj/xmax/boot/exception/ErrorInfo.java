package org.treeleafj.xmax.boot.exception;

import lombok.Data;

/**
 * @author leaf
 * @date 2017-03-10 16:12
 */
@Data
public class ErrorInfo {

    private String uri;

    private Throwable exception;

    private String ext;

    private boolean ajax;
}
