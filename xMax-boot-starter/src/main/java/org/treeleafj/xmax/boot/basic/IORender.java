package org.treeleafj.xmax.boot.basic;

import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by leaf on 2017/3/12 0012.
 */
public class IORender extends Render {

    /**
     * HTTP协议内容类型
     */
    private String contentType;

    private InputStream in;

    public IORender(InputStream in) {
        this.in = in;
    }

    public IORender(String contentType, InputStream in) {
        this.contentType = contentType;
        this.in = in;
    }

    @Override
    public void render(HttpServletResponse response) {
        if (contentType != null) {
            response.setContentType(contentType);
        }

        try {
            IOUtils.copy(in, response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("写入数据错误", e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}
