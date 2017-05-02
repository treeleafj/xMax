package org.treeleafj.xmax.boot.basic;

import org.apache.commons.io.IOUtils;
import org.treeleafj.xmax.json.Jsoner;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 专门处理jsonp方式的请求(跨域专用)
 * <p>
 * Created by leaf on 2017/5/2.
 */
public class JsonpRender extends JsonRender {

    private String callback;

    public JsonpRender(String callback) {
        super();
        this.callback = callback;
    }

    public JsonpRender(String callback, String msg) {
        super(msg);
        this.callback = callback;
    }

    public JsonpRender(String callback, Object data) {
        super(data);
        this.callback = callback;
    }

    public JsonpRender(String callback, String code, String msg) {
        super(code, msg);
        this.callback = callback;
    }

    @Override
    public void render(HttpServletResponse response) {
        response.setContentType("application/json;charset=utf-8");

        String method = callback;
        String content = Jsoner.toJson(this.map);
        StringBuilder sb = new StringBuilder();
        sb.append(method);
        sb.append('(');
        sb.append(content);
        sb.append(");");

        try {
            IOUtils.write(sb.toString(), response.getOutputStream(), "utf-8");
        } catch (IOException e) {
            throw new RuntimeException("数据写入失败", e);
        }
    }
}
