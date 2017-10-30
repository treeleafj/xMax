package org.treeleafj.xmax.boot.view;

import org.treeleafj.xmax.export.Exporter;
import org.treeleafj.xmax.export.format.Format;
import org.treeleafj.xmax.export.format.csv.CsvFormat;
import org.treeleafj.xmax.export.format.excel.ExcelFormat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by leaf on 2017/4/11.
 */
public class ExportView extends AbstractView {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected static final String CONTENT_TYPE = "application/vnd.ms-excel";

    protected Map<String, Object> dataFormatters = null;

    // excel模板的本地路径
    protected String templatePath = null;
    // 模板文件
    protected String tempalteFile;

    protected static final String FILE_NAME_PARAM = "downfilename";

    private String suffix;

    public ExportView(String suffix) {
        setContentType(CONTENT_TYPE);
        this.suffix = suffix;
    }

    /**
     * 获取下载的文件名
     *
     * @param request
     * @return
     */
    protected String getDownFileName(HttpServletRequest request) {
        String fileName = request.getParameter(FILE_NAME_PARAM);
        if (fileName != null && fileName.length() > 0) {
            if (fileName.length() > 60) {
                fileName = fileName.substring(0, 60);
            }
            if (!fileName.endsWith("." + suffix)) {
                fileName = fileName + "." + suffix;
            }

            fileName = fileName.replaceAll("\r", "");
            fileName = fileName.replaceAll("\n", "");

            return fileName;
        }
        return null;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        // 检查模板文件是否存在
        boolean res = checkTemplate(request);
        if (!res) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "not found excel template");
            response.flushBuffer();
            return;
        }

        // 获取下载时输出的文件名
        String downFileName = getDownFileName(request);
        if (StringUtils.isNoneBlank(downFileName)) {
            String userAgent = request.getHeader("USER-AGENT");
            if (StringUtils.contains(userAgent, "MSIE")) { // IE浏览器
                downFileName = URLEncoder.encode(downFileName, "UTF8");
            } else if (StringUtils.contains(userAgent, "Mozilla")) { // google,
                // 火狐浏览器
                downFileName = new String(downFileName.getBytes(), "ISO8859-1");
            } else {
                downFileName = URLEncoder.encode(downFileName, "UTF8"); // 其他浏览器
            }
            response.setHeader("Content-Disposition", "attachment; filename=" + downFileName);
        }

        //收集数据格式化的bean，然后放在model中，以支持excel输出时调用格式化操作
        Map<String, Object> formmaters = detectExcelFormatter();
        Map<String, Object> data;
        if (formmaters != null && formmaters.size() > 0) {
            data = new LinkedHashMap<String, Object>(model);
            data.putAll(formmaters);
        } else {
            data = model;
        }

        Exporter exporter = new Exporter();
        exporter.setData(data);
        Format format = getFormat(request);
        exporter.setFormat(format);

        exporter.export(response.getOutputStream());
    }

    private Format getFormat(HttpServletRequest request) throws FileNotFoundException {
        InputStream in;
        if (templatePath == null) {
            in = request.getSession().getServletContext().getResourceAsStream(tempalteFile);
        } else {
            in = new FileInputStream(templatePath + tempalteFile);
        }

        Format format;
        if ("csv".equalsIgnoreCase(this.suffix)) {
            format = new CsvFormat(in, "UTF-8", "GBK");
        } else {
            format = new ExcelFormat(in);
        }
        return format;
    }

    /**
     * 检查模板是否存在
     *
     * @return
     */
    protected boolean checkTemplate(HttpServletRequest request) {
        try {
            // 如果配置了本地模板路径则查看文件是否存在
            if (StringUtils.isNoneBlank(getTemplatePath())) {
                File f = new File(getTemplatePath() + tempalteFile);
                if (!f.exists()) {
                    logger.error("not found excel template:{}", getTemplatePath() + tempalteFile);
                    return false;
                } else {
                    return true;
                }
            } else {
                // 如果配置为资源目录，则检查模板资源文件是否存在
                try {
                    ServletContext ctx = request.getSession().getServletContext();
                    URL url = ctx.getResource(tempalteFile);
                    if (url != null) {
                        return true;
                    } else {
                        logger.error("not found excel tempalte:{}", tempalteFile);
                        return false;
                    }
                } catch (Throwable e) {
                    logger.error("not found excel tempalte:{}", tempalteFile);
                    return false;
                }
            }
        } catch (Throwable t) {
            logger.error("look excel tempalte fail:" + tempalteFile, t);
            return false;
        }
    }

    protected Map<String, Object> detectExcelFormatter() {
        if (dataFormatters != null) {
            return dataFormatters;
        }

        try {
            Class classz = Class.forName("cn.swiftpass.slite.web.support.ExcelViewTemplateFormatter");
            return BeanFactoryUtils.beansOfTypeIncludingAncestors(getApplicationContext(), classz);
        } catch (ClassNotFoundException e) {
            logger.warn("获取ExcelViewTemplateFormatter接口的信息失败,请检查是否有次接口", e);
            return new HashMap<String, Object>();
        }
    }


    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getTempalteFile() {
        return tempalteFile;
    }

    public void setTempalteFile(String tempalteFile) {
        this.tempalteFile = tempalteFile;
    }

}
