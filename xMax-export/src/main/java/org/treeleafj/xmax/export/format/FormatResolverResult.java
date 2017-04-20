package org.treeleafj.xmax.export.format;

import java.util.ArrayList;
import java.util.List;

/**
 * 对模板里含有特殊标签的一个解析情况,用于标识哪行是要特殊处理的,是否是循环标签,对于未含有特殊标签的行不进行解析
 * <p>
 * Created by leaf on 2017/4/10.
 */
public class FormatResolverResult {

    private List<FormatRowResolverResult> rowResolverResults = new ArrayList<FormatRowResolverResult>();

    public List<FormatRowResolverResult> getRowResolverResults() {
        return rowResolverResults;
    }

    public void setRowResolverResults(List<FormatRowResolverResult> rowResolverResults) {
        this.rowResolverResults = rowResolverResults;
    }
}
