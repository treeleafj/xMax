package org.treeleafj.xmax.export.format.csv;

import org.treeleafj.xmax.export.ExportException;
import org.treeleafj.xmax.export.format.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by leaf on 2017/4/10.
 */
public class CsvOutput implements Output {

    private Logger log = LoggerFactory.getLogger(CsvOutput.class);

    /**
     * 系统换行符
     */
    private String lineSeparator = System.getProperty("line.separator");

    private CsvFormat format;

    public CsvOutput(CsvFormat format) {
        this.format = format;
    }

    @Override
    public void write(OutputStream out, List<SheetInfo> sheetInfos, List<FormatResolverResult> resolverResults, Map<String, Object> data) {
        if (sheetInfos.size() <= 0) {
            log.warn("工作表数量为0,不做导出");
            throw new ExportException("", "工作表数量为0,不做导出, 请检查csv模板");
        }

        SheetInfo sheetInfo = sheetInfos.get(0);
        if (sheetInfo.getLineInfos().size() <= 0) {
            log.warn("模板为空模板,不做导出");
            return;
        }

        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(out, this.format.getWriteCharset()));

            FormatResolverResult resolverResult = null;
            if (resolverResults.size() > 0) {
                resolverResult = resolverResults.get(0);
            }

            int lastWriteIndex = 0;
            for (int i = 0; i < resolverResult.getRowResolverResults().size(); i++) {
                FormatRowResolverResult rowResolverResult = resolverResult.getRowResolverResults().get(i);

                //写入普通行
                if (rowResolverResult.isForeach() && rowResolverResult.getFromRowIndex() > lastWriteIndex) {
                    for (int start = lastWriteIndex; start < rowResolverResult.getFromRowIndex(); start++) {
                        this.writeNoVarRow(sheetInfo.getLineInfos().get(start), bufferedWriter);
                    }
                } else if (!rowResolverResult.isForeach() && rowResolverResult.getRowIndex() > lastWriteIndex) {
                    for (int start = lastWriteIndex; start < rowResolverResult.getRowIndex(); start++) {
                        this.writeNoVarRow(sheetInfo.getLineInfos().get(start), bufferedWriter);
                    }
                }

                //写入需要处理的行
                if (rowResolverResult.isForeach()) {
                    lastWriteIndex = rowResolverResult.getEndRowIndex() + 1;

                    LineInfo beginLineInfo = sheetInfo.getLineInfos().get(rowResolverResult.getFromRowIndex());
                    String[] tags = TagUtils.parseForeachTag(beginLineInfo.getCloumnInfos().get(0).getContent());
                    Iterable<Object> iterable = (Iterable<Object>) TagUtils.parseValue(tags[1], data);
                    Iterator<Object> iterator = iterable.iterator();

                    LineInfo templateLineInfo = sheetInfo.getLineInfos().get(rowResolverResult.getFromRowIndex() + 1);

                    while (iterator.hasNext()) {
                        Object item = iterator.next();
                        data.put(tags[0], item);
                        this.writeVarRow(templateLineInfo, bufferedWriter, data);
                        data.remove(tags[0]);
                    }

                } else {
                    lastWriteIndex = rowResolverResult.getRowIndex() + 1;
                    LineInfo templateLineInfo = sheetInfo.getLineInfos().get(rowResolverResult.getRowIndex());
                    this.writeVarRow(templateLineInfo, bufferedWriter, data);
                }
            }
            IOUtils.closeQuietly(bufferedWriter);
        } catch (UnsupportedEncodingException e) {
            log.error("包装输出流失败", e);
            throw new ExportException("", "包装输出流失败");
        } catch (IOException e) {
            log.error("导出写入失败", e);
            throw new ExportException("", "导出写入失败");
        }
    }

    /**
     * 写入含有变量需要处理的一条普通行
     *
     * @param lineInfo
     * @param bufferedWriter
     * @param data
     */
    private void writeVarRow(LineInfo lineInfo, BufferedWriter bufferedWriter, Map<String, Object> data) throws IOException {
        LineInfo temp = new LineInfo();
        for (short cellIndex = 0; cellIndex < lineInfo.getCloumnInfos().size(); cellIndex++) {
            String content = lineInfo.getCloumnInfos().get(cellIndex).getContent();
            Object o;
            if (TagUtils.isVar(content) || TagUtils.isCall(content)) {
                o = TagUtils.parseValue(content, data);
            } else {
                o = content;
            }

            CloumnInfo tempCloumn = new CloumnInfo();
            if (o != null) {
                tempCloumn.setContent(o.toString());
            } else {
                tempCloumn.setContent("");
            }
            temp.getCloumnInfos().add(tempCloumn);
        }
        this.writeNoVarRow(temp, bufferedWriter);
    }

    /**
     * 写入没有变量不需要处理的一条普通行
     *
     * @param lineInfo
     * @param bufferedWriter
     */
    private void writeNoVarRow(LineInfo lineInfo, BufferedWriter bufferedWriter) throws IOException {

        //没有需要解析的,直接写入(就是说,此csv模板根本就不需要解析什么数据)
        StringBuilder sb = new StringBuilder();
        for (int cellIndex = 0; cellIndex < lineInfo.getCloumnInfos().size(); cellIndex++) {
            sb.append('\t');
            sb.append(lineInfo.getCloumnInfos().get(cellIndex).getContent());
            sb.append(',');
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        bufferedWriter.write(sb.toString());
        bufferedWriter.write(lineSeparator);
    }
}
