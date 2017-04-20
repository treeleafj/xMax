package org.treeleafj.xmax.export.format;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 输出工具,子类实现负责输出各种对应格式的文件
 * <p>
 * Created by leaf on 2017/4/10.
 */
public interface Output {

    /**
     * 将数据输出
     *
     * @param out             输出的地方
     * @param sheetInfos      对原有模本的一个读取结果
     * @param resolverResults 对模板的一个解析情况
     * @param data
     */
    void write(OutputStream out, List<SheetInfo> sheetInfos, List<FormatResolverResult> resolverResults, Map<String, Object> data);

}
