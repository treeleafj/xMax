package org.treeleafj.xmax.export.tag;

import org.treeleafj.xmax.export.format.TagUtils;

/**
 * Created by leaf on 2017/4/21.
 */
public class TagResolver {

    public Tag resolve(String s) {
        if (TagUtils.isVar(s)) {
            return new VarTag();
        } else if (TagUtils.isCall(s)) {
            return new CallTag();
        } else if (TagUtils.isForeachBegin(s)) {
            return new ForeachTag();
        }
        return null;
    }

}
