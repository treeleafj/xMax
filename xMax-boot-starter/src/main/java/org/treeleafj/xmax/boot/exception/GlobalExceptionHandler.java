package org.treeleafj.xmax.boot.exception;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.treeleafj.xmax.boot.XMaxConfig;
import org.treeleafj.xmax.boot.utils.RequestUtils;
import org.treeleafj.xmax.exception.BaseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理
 *
 * @author leaf
 * @date 2017-03-10 16:03
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public static String PRINT_LOG_FLAG = "_prePrintLogHandlerFlag";

    @Autowired
    private XMaxConfig xMaxConfig;

    public GlobalExceptionHandler() {
        log.info("开启xMax全局异常拦截处理");
    }

    @ExceptionHandler
    public Object handle(HttpServletRequest request, HttpServletResponse response, Throwable t) {
        if (request.getAttribute(PRINT_LOG_FLAG) == null) {
            //说明异常情况导致没有进入PrintLogHandlerInerceptor,那么这里就要直接打印出来
            log.error("调用[{}]接口出现错误", request.getServletPath(), t);
        }

        request.setAttribute("_exception", t);

        String path = request.getServletPath();
        String ext = FilenameUtils.getExtension(path);
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setException(t);
        errorInfo.setUri(request.getRequestURI());
        errorInfo.setExt(ext);
        errorInfo.setAjax("json".equals(xMaxConfig.getDefaultErrorFormat()) || RequestUtils.isAjax(request) || RequestUtils.isJson(request));
        return this.exceptionHandle(request, response, errorInfo);
    }

    protected Object exceptionHandle(HttpServletRequest request, HttpServletResponse response, ErrorInfo errorInfo) {

        boolean printJson = errorInfo.isAjax() || "json".equals(errorInfo.getExt());
        if (printJson) {
            Map model = new HashMap(2);
            if (errorInfo.getException() instanceof BaseException) {
                BaseException exception = (BaseException) errorInfo.getException();
                model.put("code", exception.getCode());
                model.put("msg", exception.getMessage());
            } else {
                model.put("code", xMaxConfig.getUnknownErrorCode());
                model.put("msg", xMaxConfig.getUnknownErrorMsg());
            }
            RequestUtils.writeJson(model, response);
            return null;
        } else {
            ModelAndView mav = new ModelAndView(xMaxConfig.getErrorView());
            if (errorInfo.getException() instanceof BaseException) {
                BaseException exception = (BaseException) errorInfo.getException();
                mav.addObject("code", exception.getCode());
                mav.addObject("msg", exception.getMessage());
            } else {
                mav.addObject("code", xMaxConfig.getUnknownErrorCode());
                mav.addObject("msg", xMaxConfig.getUnknownErrorMsg());
            }
            return mav;
        }
    }
}
