package org.treeleafj.xmax.boot.handler;

import org.springframework.web.servlet.view.AbstractView;
import org.treeleafj.xmax.boot.basic.Render;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author leaf
 * @date 2017-03-11 18:04
 */
public class RenderView extends AbstractView {

    private Render render;

    public RenderView(Render render) {
        this.render = render;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        render.render(httpServletResponse);
    }
}
