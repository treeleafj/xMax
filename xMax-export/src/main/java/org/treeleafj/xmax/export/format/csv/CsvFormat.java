package org.treeleafj.xmax.export.format.csv;

import lombok.Data;
import org.treeleafj.xmax.export.ExportException;
import org.treeleafj.xmax.export.format.*;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by leaf on 2017/4/10.
 */
@Data
public class CsvFormat implements Format {

    private List<String> source = new ArrayList<String>(0);

    private InputStream in;

    private String readCharset = "utf-8";
    private String writeCharset = "utf-8";

    public CsvFormat(InputStream in, String readCharset,  String writeCharset) {
        this.in = in;
        this.readCharset = readCharset;
        this.writeCharset = writeCharset;

        try {
            source = IOUtils.readLines(in, this.readCharset);
        } catch (IOException e) {
            throw new ExportException("", "读取csv模板失败");
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    @Override
    public List<SheetInfo> resolveOriginalInfo() {

        List<SheetInfo> sheetInfos = new ArrayList<SheetInfo>(1);//csv只会有一个工作表,不像excel可能存在多工作表
        SheetInfo sheetInfo = new SheetInfo();
        for (int i = 0; i < this.source.size(); i++) {
            LineInfo lineInfo = new LineInfo();

            String item = this.source.get(i);
            StringTokenizer st = new StringTokenizer(item, ",");
            while (st.hasMoreTokens()) {
                String cell = st.nextToken();
                CloumnInfo cloumnInfo = new CloumnInfo();
                cloumnInfo.setContent(cell.replaceAll("\\|", ","));
                lineInfo.getCloumnInfos().add(cloumnInfo);
            }

            sheetInfo.getLineInfos().add(lineInfo);
        }
        sheetInfos.add(sheetInfo);
        return sheetInfos;
    }

    @Override
    public Output getOutput() {
        return new CsvOutput(this);
    }

}
