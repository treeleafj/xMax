package org.treeleafj.xmax.boot.basic;

import javax.servlet.http.HttpServletResponse;

/**
 * 返回指定状态码
 *
 * Created by leaf on 2017/10/13.
 */
public class StatusRender extends Render {

    private int status;

    public StatusRender(int status) {
        this.status = status;
    }

    @Override
    public void render(HttpServletResponse response) {
        response.setStatus(status);
    }
}
