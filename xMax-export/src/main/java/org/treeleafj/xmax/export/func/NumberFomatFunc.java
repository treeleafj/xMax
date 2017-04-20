package org.treeleafj.xmax.export.func;

import java.text.DecimalFormat;

/**
 * Created by leaf on 2017/4/20.
 */
public class NumberFomatFunc implements Func {

    public String formatYuanFen(Double n) {
        if (n == null) {
            return "";
        }
        DecimalFormat format = new DecimalFormat("##,###.00");
        return format.format(n);
    }

}
