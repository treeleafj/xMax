package org.treeleafj.xmax.template;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Map;

/**
 * 基于velocity实现的模版解析
 *
 * @Author leaf
 * 2015/9/3 0003 2:06.
 */
public class VelocityTemplater extends Templater {

    public static final String ENCODING = "UTF-8";

    private static VelocityEngine ve = new VelocityEngine();

    static {
        //设置模板加载路径，这里设置的是class下
        ve.setProperty(Velocity.RESOURCE_LOADER, "class");
        ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        ve.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogChute");
        ve.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_CACHE, false);
//        ve.setProperty("file.resource.loader.modificationCheckInterval", 10);
        ve.init();
    }

    private String path;

    public VelocityTemplater(String path) {
        this.path = path;
    }

    @Override
    public String parse(Map<String, Object> param) {

//        Template template = ve.getTemplate("com/jleaf/test/netty/template/" + name + ".vm", ENCODING);
        Template template = ve.getTemplate(path, ENCODING);

        VelocityContext velocityContext = new VelocityContext();

        for (Map.Entry<String, Object> entry : param.entrySet()) {
            velocityContext.put(entry.getKey(), entry.getValue());
        }

        StringWriter sw = new StringWriter();
        template.merge(velocityContext, sw);

        return sw.toString();
    }

}
