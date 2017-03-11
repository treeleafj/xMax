package org.treeleafj.xmax.boot.basic;

import lombok.Data;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.treeleafj.xmax.exception.RetCode;
import org.treeleafj.xmax.json.Jsoner;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leaf
 * @date 2017-03-11 17:35
 */
@Data
public class JsonRender extends Render {

    private String code = RetCode.OK;

    private String msg;

    private Object data;

    public JsonRender(String msg) {
        this.msg = msg;
    }

    public JsonRender(Object data) {
        this.data = data;
    }

    public JsonRender(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public void render(HttpServletResponse response) {
        Map<String, Object> map;
        if (data != null) {
            try {
                map = PropertyUtils.describe(data);
            } catch (Exception e) {
                throw new RuntimeException("转义数据失败", e);
            }
        } else {
            map = new HashMap<>();
        }

        map.put("code", code);
        map.put("msg", msg);

        response.setContentType("application/json;charset=utf-8");
        try {
            IOUtils.write(Jsoner.toJson(map), response.getOutputStream(), "utf-8");
        } catch (IOException e) {
            throw new RuntimeException("数据写入失败", e);
        }
    }
}
