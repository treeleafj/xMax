package org.treeleafj.xmax.boot.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.treeleafj.xmax.boot.utils.RequestUtils;
import org.treeleafj.xmax.boot.utils.UriUtils;
import org.treeleafj.xmax.exception.BaseException;
import org.treeleafj.xmax.exception.RetCode;

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

    @ExceptionHandler
    public Object handle(HttpServletRequest request, HttpServletResponse response, Throwable t, ModelMap modelMap) {
        request.setAttribute("_exception", t);
        String path = request.getServletPath();
        String ext = UriUtils.getExt(path);
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setException(t);
        errorInfo.setUri(request.getRequestURI());
        errorInfo.setExt(ext);
        errorInfo.setAjax(RequestUtils.isAjax(request));
        return this.exceptionHandle(request, response, errorInfo);
    }

    protected Object exceptionHandle(HttpServletRequest request, HttpServletResponse response, ErrorInfo errorInfo) {

        boolean printJson = errorInfo.isAjax() || ".json".equals(errorInfo.getExt());
        if (printJson) {
            Map model = new HashMap();
            if (errorInfo.getException() instanceof BaseException) {
                BaseException exception = (BaseException) errorInfo.getException();
                model.put("code", exception.getCode());
                model.put("msg", exception.getMessage());
            } else {
                model.put("code", RetCode.FAIL_UNKNOWN);
                model.put("msg", "网络繁忙,请稍后尝试!");
            }
            RequestUtils.writeJson(model, response);
            return null;
        } else {
            ModelAndView mav = new ModelAndView("error");
            if (errorInfo.getException() instanceof BaseException) {
                BaseException exception = (BaseException) errorInfo.getException();
                mav.addObject("code", exception.getCode());
                mav.addObject("msg", exception.getMessage());
            } else {
                mav.addObject("code", RetCode.FAIL_UNKNOWN);
                mav.addObject("msg", "网络繁忙,请稍后尝试!");
            }
            return mav;
        }
    }
}
