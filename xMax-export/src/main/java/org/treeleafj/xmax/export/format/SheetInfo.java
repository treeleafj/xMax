package org.treeleafj.xmax.export.format;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作表信息(主要用于兼容excel模板里存在多个工作表的情况)
 * <p>
 * Created by leaf on 2017/4/11.
 */
public class SheetInfo {

    private String name;

    private List<LineInfo> lineInfos = new ArrayList<LineInfo>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LineInfo> getLineInfos() {
        return lineInfos;
    }

    public void setLineInfos(List<LineInfo> lineInfos) {
        this.lineInfos = lineInfos;
    }
}
