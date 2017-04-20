package org.treeleafj.xmax.export;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.treeleafj.xmax.export.format.FormatResolver;
import org.treeleafj.xmax.export.format.excel.ExcelFormat;
import org.treeleafj.xmax.export.func.DateFormatFunc;
import org.treeleafj.xmax.export.func.NumberFomatFunc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by leaf on 2017/4/20.
 */
public class ExporterTest {

    @Test
    public void export() throws Exception {
        String rootDir = System.getProperty("user.dir");
        String templateFile = rootDir + "/src/test/resources/template.xls";
        FileInputStream in = new FileInputStream(new File(templateFile));
        FileOutputStream out = new FileOutputStream("/result.xls");

        HashMap<String, Object> data = this.getData();

        Exporter exporter = new Exporter();
        exporter.setFormat(new ExcelFormat(in));
        exporter.setFormatResolver(new FormatResolver());
        exporter.setData(data);
        exporter.export(out);
        IOUtils.closeQuietly(out);
    }

    public HashMap<String,Object> getData() {
        HashMap<String, Object> data = new HashMap<>();
        int num = 100;
        List<Object> list = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            Merchant merchant = new Merchant();
            merchant.setMerchantNo("SH" + (i + 1));
            merchant.setName("商户" + (i + 1));
            merchant.setBalance(Double.valueOf(i));
            merchant.setCreateTime(new Date());
            list.add(merchant);
        }
        data.put("rows", list);
        data.put("numberFormat", new NumberFomatFunc());
        data.put("dateFormt", new DateFormatFunc());
        return data;
    }
}