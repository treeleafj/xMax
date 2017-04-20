package org.treeleafj.xmax.export.format;

import java.util.ArrayList;
import java.util.List;

/**
 * 行信息
 *
 * Created by leaf on 2017/4/11.
 */
public class LineInfo {

    private List<CloumnInfo> cloumnInfos = new ArrayList<CloumnInfo>(0);

    public List<CloumnInfo> getCloumnInfos() {
        return cloumnInfos;
    }

    public void setCloumnInfos(List<CloumnInfo> cloumnInfos) {
        this.cloumnInfos = cloumnInfos;
    }
}
