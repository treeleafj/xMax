package org.treeleafj.xmax.boot.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * 新的excel,csv等视图解析器
 * Created by leaf on 2017/4/11.
 */
public class ExportViewResolver extends AbstractCachingViewResolver implements Ordered {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    // json支持的后缀
    protected Set<String> suffixes = new LinkedHashSet<String>(Arrays.asList("xls", "xlsx", "csv"));

    protected int order = 1;

    protected String prefix = "";

    protected String suffix = "";

    // excel模板的本地路径
    protected String templatePath = null;

    protected String templateParameter = "template";

    //查找不到模板是否抛异常
    protected boolean noTemplateThrowExp = true;

    /**
     * 获取View的模板文件路径,如果templateParameter的参数有设定值，则取参数中设定的名称作为
     * excel模板文件名，否则使用uri作为模板文件名
     *
     * @param viewName
     * @return
     */
    protected String getResponseViewName(String viewName, String exportType) {
        HttpServletRequest request = this.getRequest();
        String tempalteView = null;
        //尝试从templateParameter中指定的参数获取模板视图名
        if (org.apache.commons.lang3.StringUtils.isNoneBlank(templateParameter)) {
            tempalteView = request.getParameter(templateParameter);
        }

        //如果templateParameter为空，则使用uri作为模板视图名
        if (!StringUtils.hasText(tempalteView)) {
            tempalteView = viewName;
        } else {

            /*
             * 如果用templateParameter作为模板视图，则需要将uri中最后部分替换为参数指定的名称
             * 例如/demotest/list_test.xls?template=trade，则需要替换uri生成/demotest/trade.xls
             * 作为视图
             */
//            FilePathInfo pathInfo = new FilePathInfo(viewName);
//            StringBuilder sb = new StringBuilder(64);
//            if (pathInfo.getPath() != null) {
//                sb.append(pathInfo.getPath());
//                if (!pathInfo.getPath().endsWith("/"))
//                    sb.append("/");
//            }
//
//            if (pathInfo.getFileName() != null)
//                sb.append(tempalteView);
//
//            if (pathInfo.getFileExtension() != null) {
//                sb.append(".");
//                if ("csv".equalsIgnoreCase(exportType)) {//针对csv做处理
//                    sb.append("csv");
//                } else {
//                    sb.append(pathInfo.getFileExtension());
//                }
//            }
//            tempalteView = sb.toString();
        }

        return tempalteView;
    }

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        HttpServletRequest request = this.getRequest();
        viewName = request.getRequestURI();
        String suffix = StringUtils.getFilenameExtension(viewName);
        // 判断viewName后缀是否为支持的后缀
        if (suffix == null || !suffixes.contains(suffix)) {
            return null;
        }

        String exportType = request.getParameter("exportType");
        if (!StringUtils.hasText(exportType)) {
            return null;
        }
        viewName = getResponseViewName(viewName, exportType);
        return super.resolveViewName(viewName, locale);
    }

    @Override
    protected View loadView(String viewName, Locale locale) throws Exception {
        String suffix = StringUtils.getFilenameExtension(viewName);
        // 判断viewName后缀是否为支持的后缀
        if (suffix != null && suffixes.contains(suffix)) {
            return buildView(viewName, suffix);
        }
        return null;
    }

    /**
     * 创建View
     *
     * @param viewName
     * @return
     * @throws Exception
     */
    protected ExportView buildView(String viewName, String suffix) throws Exception {
        String templateFile = getPrefix() + viewName + getSuffix();
        ExportView view = new ExportView(suffix);
        view.setTemplatePath(this.templatePath);
        view.setTempalteFile(templateFile);
        view.setApplicationContext(getApplicationContext());
        return view;
    }


    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getTemplateParameter() {
        return templateParameter;
    }

    public void setTemplateParameter(String templateParameter) {
        this.templateParameter = templateParameter;
    }

    public HttpServletRequest getRequest() {
        RequestAttributes reqAttr = RequestContextHolder.getRequestAttributes();
        if (reqAttr != null && reqAttr instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) reqAttr).getRequest();
        }
        return null;
    }
}
