package org.treeleafj.xmax.export.format;

import org.treeleafj.xmax.export.ExportException;

import java.util.ArrayList;
import java.util.List;

/**
 * 模板解析器,用于解析模板上面哪些行是需要处理的
 * <p>
 * Created by leaf on 2017/4/10.
 */
public class FormatResolver {

    /**
     * 解析行,用于定位哪些行是要处理的
     *
     * @param sheetInfos
     * @return
     */
    public List<FormatResolverResult> resolveRows(List<SheetInfo> sheetInfos) {
        List<FormatResolverResult> resolverResults = new ArrayList<FormatResolverResult>();
        for (int sheetIndex = 0; sheetIndex < sheetInfos.size(); sheetIndex++) {
            SheetInfo sheetInfo = sheetInfos.get(sheetIndex);
            List<LineInfo> lineInfos = sheetInfo.getLineInfos();

            FormatResolverResult frr = new FormatResolverResult();
            resolverResults.add(frr);
            for (int lineIndex = 0; lineIndex < lineInfos.size(); lineIndex++) {
                LineInfo lineInfo = lineInfos.get(lineIndex);

                for (int cloumnIndex = 0; cloumnIndex < lineInfo.getCloumnInfos().size(); cloumnIndex++) {
                    //先判断是否循环标签
                    CloumnInfo cloumnInfo = lineInfo.getCloumnInfos().get(cloumnIndex);
                    if (TagUtils.isForeachBegin(lineInfo.getCloumnInfos().get(0).getContent())) {//循环标签只支持写在第一列
                        //(循环标签从开始到结束)
                        FormatRowResolverResult rowResolverResult = new FormatRowResolverResult();
                        rowResolverResult.setForeach(true);
                        rowResolverResult.setFromRowIndex(lineIndex);
                        rowResolverResult.setEndRowIndex(null);//结束行号暂时设置为null
                        frr.getRowResolverResults().add(rowResolverResult);//将开始加进去
                        break;

                    } else if (TagUtils.isForeachEnd(cloumnInfo.getContent())) {
                        FormatRowResolverResult last = this.getLastRowResolverResult(frr);
                        if (last == null) {
                            throw new ExportException("导出失败,模板未标明结束#end");
                        }
                        last.setEndRowIndex(lineIndex);//补上行号
                        break;

                    } else if (TagUtils.isCall(cloumnInfo.getContent()) || TagUtils.isVar(cloumnInfo.getContent())) { //再判断是否非循环的普通的变量标签或函数标签
                        FormatRowResolverResult last = this.getLastRowResolverResult(frr);
                        if (last != null && last.isForeach() && last.getEndRowIndex() == null) {//说明还未到循环结束的标签
                            break;
                        } else {//说明是非循环里面的含有普通变量标签行
                            FormatRowResolverResult rowResolverResult = new FormatRowResolverResult();
                            rowResolverResult.setForeach(false);
                            rowResolverResult.setRowIndex(lineIndex);
                            frr.getRowResolverResults().add(rowResolverResult);
                            break;
                        }
                    }
                    //否则都认为是纯文本,不是标签,不做处理
                }
            }
        }
        return resolverResults;
    }

    /**
     * 获取最后一个行解析结果
     */
    private FormatRowResolverResult getLastRowResolverResult(FormatResolverResult frr) {
        if (frr.getRowResolverResults().size() > 0) {
            return frr.getRowResolverResults().get(frr.getRowResolverResults().size() - 1);
        }
        return null;
    }
}
