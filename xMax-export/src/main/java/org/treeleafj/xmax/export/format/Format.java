package org.treeleafj.xmax.export.format;

import java.util.List;

/**
 * 针对各种导出格式的一个高层抽象
 * <p>
 * Created by leaf on 2017/4/10.
 */
public interface Format {

    /**
     * 解析出模板的原始数据
     *
     * @return
     */
    List<SheetInfo> resolveOriginalInfo();

    Output getOutput();
}
