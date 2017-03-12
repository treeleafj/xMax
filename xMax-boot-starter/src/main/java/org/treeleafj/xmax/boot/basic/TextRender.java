package org.treeleafj.xmax.boot.basic;

import lombok.Data;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 文本类型的返回
 *
 * @author leaf
 * @date 2017-03-11 17:44
 */
@Data
public class TextRender extends Render {

    /**
     * 字符集,默认UTF-8
     */
    private String charset = "utf-8";

    /**
     * HTTP协议内容类型
     */
    private String contentType;

    /**
     * 内容
     */
    private String content;

    /**
     * 返回纯文本格式的content内容
     *
     * @param content
     */
    public TextRender(String content) {
        this(CONTENT_TYPE_TEXT, content);
    }

    /**
     * 返回指定的contentType协议的content内容
     *
     * @param contentType
     * @param content
     */
    public TextRender(String contentType, String content) {
        this.contentType = contentType;
        this.content = content;
    }

    /**
     * 返回指定的contentType协议的content内容
     *
     * @param contentType contentType协议头
     * @param content     内容
     * @param charset     字符串集
     */
    public TextRender(String contentType, String content, String charset) {
        this.contentType = contentType;
        this.content = content;
        this.charset = charset;
    }

    @Override
    public void render(HttpServletResponse response) {
        response.setContentType(this.getContentType() + ";charset=" + this.getCharset());
        response.setCharacterEncoding(this.getCharset());
        String content = this.getContent();
        try {
            IOUtils.write(content, response.getOutputStream(), this.getCharset());
        } catch (IOException e) {
            throw new RuntimeException("数据写入失败", e);
        }
    }
}
