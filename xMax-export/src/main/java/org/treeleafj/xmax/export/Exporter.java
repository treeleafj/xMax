package org.treeleafj.xmax.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treeleafj.xmax.export.format.Format;
import org.treeleafj.xmax.export.format.FormatResolver;
import org.treeleafj.xmax.export.format.FormatResolverResult;
import org.treeleafj.xmax.export.format.SheetInfo;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 针对excel/csv等导出工具的一个高层抽象,提供数据导出成excel/csv等功能
 * Created by leaf on 2017/4/10.
 */
public class Exporter {

    private Logger log = LoggerFactory.getLogger(Exporter.class);

    private FormatResolver formatResolver = new FormatResolver();

    /**
     * 模板格式
     */
    private Format format;

    /**
     * 要导出的数据,如果是pageInfo.getRows(),记得要找个map包装起来
     */
    private Map<String, Object> data;

    /**
     * 执行导出
     *
     * @param out 输出位置
     */
    public void export(OutputStream out) {
        List<SheetInfo> sheetInfos = this.format.resolveOriginalInfo();
        List<FormatResolverResult> resolverResults = formatResolver.resolveRows(sheetInfos);

        long t = System.currentTimeMillis();
        this.format.getOutput().write(out, sheetInfos, resolverResults, this.data);
        log.info("导出完成,用时{}毫秒", System.currentTimeMillis() - t);
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public FormatResolver getFormatResolver() {
        return formatResolver;
    }

    public void setFormatResolver(FormatResolver formatResolver) {
        this.formatResolver = formatResolver;
    }

}
