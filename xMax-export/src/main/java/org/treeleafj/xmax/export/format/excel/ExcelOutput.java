package org.treeleafj.xmax.export.format.excel;

import org.treeleafj.xmax.export.ExportException;
import org.treeleafj.xmax.export.format.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by leaf on 2017/4/10.
 */
public class ExcelOutput implements Output {

    private Logger log = LoggerFactory.getLogger(ExcelOutput.class);

    private ExcelFormat format;

    public ExcelOutput(ExcelFormat format) {
        this.format = format;
    }

    @Override
    public void write(OutputStream out, List<SheetInfo> sheetInfos, List<FormatResolverResult> resolverResults, Map<String, Object> data) {
        HSSFWorkbook workbook = this.format.getWorkbook();
        for (int sheetIndex = 0; sheetIndex < resolverResults.size(); sheetIndex++) {
            HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            FormatResolverResult formatResolverResult = resolverResults.get(sheetIndex);
            this.write(data, sheet, formatResolverResult);
        }
        try {
            workbook.write(out);
        } catch (IOException e) {
            log.error("导出文件失败", e);
            throw new ExportException("", "导出失败");
        }
    }

    /**
     * 写入数据到excel中
     *
     * @param data                 数据
     * @param sheet                excel工作表
     * @param formatResolverResult 整个工作表的解析情况
     */
    protected void write(Map<String, Object> data, HSSFSheet sheet, FormatResolverResult formatResolverResult) {
        int offset = 0;//#foreach循环导致的步长
        for (int i = 0; i < formatResolverResult.getRowResolverResults().size(); i++) {
            FormatRowResolverResult rowResolverResult = formatResolverResult.getRowResolverResults().get(i);

            if (rowResolverResult.isForeach()) {//处理循环标签
                offset = this.writeForeach(data, sheet, offset, rowResolverResult);
            } else {//处理普通标签
                this.writeVar(data, sheet, offset, rowResolverResult);
            }
        }
    }

    /**
     * 写入非循环数据,即普通变量数据和函数调勇
     *
     * @param data              数据
     * @param sheet             excel工作表
     * @param offset            已经被循环写入的数据量(因为涉及到excel row的偏移处理,在出现多个#foreach时,此值会变化)
     * @param rowResolverResult 行解析情况
     */
    private void writeVar(Map<String, Object> data, HSSFSheet sheet, int offset, FormatRowResolverResult rowResolverResult) {
        HSSFRow row = sheet.getRow(rowResolverResult.getRowIndex() + offset);
        for (short cellIndex = 0; cellIndex <= row.getLastCellNum(); cellIndex++) {
            HSSFCell cell = row.getCell(cellIndex);
            if (cell == null || StringUtils.isBlank(cell.getStringCellValue()) || cell.getCellType() != HSSFCell.CELL_TYPE_STRING) {
                continue;
            }
            String tag = cell.getStringCellValue();
            Object v = TagUtils.parseValue(tag, data);
            cell.setCellValue(v != null ? v.toString() : "");
        }
    }

    /**
     * 写入循环数据
     *
     * @param data              数据
     * @param sheet             excel工作表
     * @param offset            已经被循环写入的数据量(因为涉及到excel row的偏移处理,在出现多个#foreach时,此值会变化)
     * @param rowResolverResult 行解析结果
     * @return
     */
    private int writeForeach(Map<String, Object> data, HSSFSheet sheet, int offset, FormatRowResolverResult rowResolverResult) {
        HSSFRow row = sheet.getRow(rowResolverResult.getFromRowIndex() + offset);
        String[] tags = TagUtils.parseForeachTag(row.getCell((short) 0).getStringCellValue());

        Iterable<Object> iterable = (Iterable<Object>) TagUtils.parseValue(tags[1], data);
        Iterator<Object> iterator = iterable.iterator();

        //只能用这种笨方法求总记录数
        int total = 0;
        while (iterator.hasNext()) {
            iterator.next();
            total++;
        }
        iterator = iterable.iterator();

        //先将从此行开始的row往下移动N行,N见数据大小,不可以在循环中每次下移一条,那样会造成性能非常慢
        sheet.shiftRows(rowResolverResult.getFromRowIndex() + offset, sheet.getLastRowNum(), total);
        HSSFRow templateRow = sheet.getRow(rowResolverResult.getFromRowIndex() + offset + 1 + total);

        while (iterator.hasNext()) {
            Object item = iterator.next();
            data.put(tags[0], item);

            HSSFRow newRow = sheet.createRow(rowResolverResult.getFromRowIndex() + offset);
            //循环插入excel
            for (short cellIndex = 0; cellIndex <= templateRow.getLastCellNum(); cellIndex++) {
                HSSFCell cell = newRow.getCell(cellIndex);
                HSSFCell templateCell = templateRow.getCell(cellIndex);//作为模板的单元格

                if (templateCell == null || StringUtils.isBlank(templateCell.getStringCellValue())
                        || templateCell.getCellType() != HSSFCell.CELL_TYPE_STRING) {
                    continue;
                }

                if (cell == null) {
                    cell = newRow.createCell(cellIndex);
                    cell.setCellStyle(templateCell.getCellStyle());
                    cell.setCellType(templateCell.getCellType());
                }

                String tag = templateCell.getStringCellValue();
                Object v = TagUtils.parseValue(tag, data);
                cell.setCellValue(v != null ? v.toString() : "");
            }
            data.remove(tags[0]);

            offset++;//步长加1
        }

        //往最后创建三行空白行,用于替换掉#foreach的三行时凑足行数上移
        int num = rowResolverResult.getEndRowIndex() - rowResolverResult.getFromRowIndex();
        for (int n = 0; n < num; n++) {
            sheet.createRow(sheet.getLastRowNum() + n + 1);
        }
        //再将后面的行再往上移3行,覆盖掉#foreach的三行
        sheet.shiftRows(rowResolverResult.getEndRowIndex() + offset + 1, sheet.getLastRowNum(),
                rowResolverResult.getFromRowIndex() - rowResolverResult.getEndRowIndex() - 1);
        return offset;
    }
}
