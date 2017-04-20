package org.treeleafj.xmax.export.format.excel;

import org.treeleafj.xmax.export.ExportException;
import org.treeleafj.xmax.export.format.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel模板格式
 * <p>
 * Created by leaf on 2017/4/10.
 */
public class ExcelFormat implements Format {

    private Logger log = LoggerFactory.getLogger(ExcelFormat.class);

    /**
     * 指定的模板文件
     */
    private HSSFWorkbook workbook;

    /**
     * 构建excel模板格式
     *
     * @param in 指定的模板文件
     */
    public ExcelFormat(InputStream in) {
        try {
            this.workbook = new HSSFWorkbook(in);
        } catch (IOException e) {
            log.error("读取模板失败", e);
            throw new ExportException("", "读取模板失败");
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    @Override
    public List<SheetInfo> resolveOriginalInfo() {
        List<SheetInfo> sheetInfos = new ArrayList<SheetInfo>(workbook.getNumberOfSheets());
        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            SheetInfo sheetInfo = new SheetInfo();
            sheetInfo.setName(workbook.getSheetName(sheetIndex));
            sheetInfos.add(sheetInfo);

            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                HSSFRow row = sheet.getRow(rowIndex);
                if (row == null) {
                    row = sheet.createRow(rowIndex);
                }
                LineInfo lineInfo = new LineInfo();
                for (int cellIndex = 0; cellIndex <= row.getLastCellNum(); cellIndex++) {
                    HSSFCell cell = row.getCell((short) cellIndex);

                    CloumnInfo cloumnInfo = new CloumnInfo();

                    if (!(cell == null || cell.getCellType() != HSSFCell.CELL_TYPE_STRING)) {
                        cloumnInfo.setContent(StringUtils.defaultString(cell.getStringCellValue()));
                    }
                    lineInfo.getCloumnInfos().add(cloumnInfo);
                }

                sheetInfo.getLineInfos().add(lineInfo);
            }
        }
        return sheetInfos;
    }

    @Override
    public Output getOutput() {
        return new ExcelOutput(this);
    }

    public HSSFWorkbook getWorkbook() {
        return workbook;
    }
}
