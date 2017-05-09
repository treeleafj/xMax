package org.treeleafj.xmax.boot.basic;

import lombok.Data;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.IOUtils;
import org.treeleafj.xmax.exception.RetCode;
import org.treeleafj.xmax.json.Jsoner;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * json格式的返回
 *
 * @author leaf
 * @date 2017-03-11 17:35
 */
@Data
public class JsonRender extends Render {

    protected Map<String, Object> map;

    public JsonRender() {
        this(RetCode.OK, null);
    }

    public JsonRender(String msg) {
        this(RetCode.OK, msg);
    }

    public JsonRender(Object data) {
        if (data instanceof Map) {
            this.map = (Map) data;
        } else if (data instanceof Collection || data.getClass().isArray()) {
            this.map = new HashMap<>();
            this.map.put("list", data);
        } else {
            try {
                this.map = PropertyUtils.describe(data);
                this.map.remove("class");
            } catch (Exception e) {
                throw new RuntimeException("转义数据失败", e);
            }
        }
        this.map.put("code", RetCode.OK);
    }

    public JsonRender(String code, String msg) {
        this.map = new HashedMap();
        this.map.put("code", code);
        this.map.put("msg", msg);
    }

    @Override
    public void render(HttpServletResponse response) {
        response.setContentType("application/json;charset=utf-8");
        try {
            IOUtils.write(Jsoner.toJson(this.map), response.getOutputStream(), "utf-8");
        } catch (IOException e) {
            throw new RuntimeException("数据写入失败", e);
        }
    }
}
