package org.treeleafj.xmax.boot.basic;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Callable;

/**
 * 异步处理渲染
 *
 * Created by leaf on 2017/10/13.
 */
public class AsyncRender extends Render implements Callable {

    private Callable callable;

    public AsyncRender(Callable callable) {
        this.callable = callable;
    }

    @Override
    public Object call() throws Exception {
        return callable.call();
    }

    @Override
    public void render(HttpServletResponse response) {

    }
}
