package org.treeleafj.xmax.export.format;

/**
 * 行解析情况,标示一段要处理的标签, #foreach到#end会认为是一行, 其它就真的是一行就是一行
 * Created by leaf on 2017/4/11.
 */
public class FormatRowResolverResult {

    /**
     * 模板中的第几行
     */
    private Integer rowIndex;

    /**
     * 是否是循环,是的话,使用 formRowIndex和endRowIndex, 不是则使用rowIndex
     */
    private boolean foreach;

    /**
     * 循环语句开始于模板中的第几行
     */
    private Integer fromRowIndex;

    /**
     * 循环语句结束于模板中的第几行
     */
    private Integer endRowIndex;

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public boolean isForeach() {
        return foreach;
    }

    public void setForeach(boolean foreach) {
        this.foreach = foreach;
    }

    public Integer getFromRowIndex() {
        return fromRowIndex;
    }

    public void setFromRowIndex(Integer fromRowIndex) {
        this.fromRowIndex = fromRowIndex;
    }

    public Integer getEndRowIndex() {
        return endRowIndex;
    }

    public void setEndRowIndex(Integer endRowIndex) {
        this.endRowIndex = endRowIndex;
    }
}
