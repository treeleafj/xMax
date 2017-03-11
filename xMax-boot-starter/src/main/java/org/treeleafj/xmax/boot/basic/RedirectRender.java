package org.treeleafj.xmax.boot.basic;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.treeleafj.xmax.http.basic.Http;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leaf
 * @date 2017-03-11 17:37
 */
@Data
public class RedirectRender extends Render {

    /**
     * 跳转地址
     */
    private String path;

    /**
     * 参数,对于web会自动组成?a=1&b=2&c=3这样的方式
     */
    private Map<String, String> param = new HashMap<String, String>();

    /**
     * @param path 要跳转的路径
     */
    public RedirectRender(String path) {
        this.path = path;
    }

    /**
     * @param path  要跳转的路径
     * @param param 地址上的参数,对于web会自动组成?a=1&b=2&c=3这样的方式
     */
    public RedirectRender(String path, Map<String, String> param) {
        this.path = path;
        this.param = param;
    }

    @Override
    public void render(HttpServletResponse response) {
        String param = Http.param2UrlParam(this.getParam());
        StringBuilder stringBuilder = new StringBuilder(this.getPath());
        if (StringUtils.isNotBlank(param)) {
            stringBuilder.append('?');
            stringBuilder.append(param);
        }
        try {
            response.sendRedirect(stringBuilder.toString());
        } catch (IOException e) {
            throw new RuntimeException("页面重定向失败", e);
        }
    }
}
