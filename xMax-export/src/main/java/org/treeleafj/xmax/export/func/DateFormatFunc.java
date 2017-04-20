package org.treeleafj.xmax.export.func;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leaf on 2017/4/20.
 */
public class DateFormatFunc implements Func {

    public String formatDate(Date date, String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

}
