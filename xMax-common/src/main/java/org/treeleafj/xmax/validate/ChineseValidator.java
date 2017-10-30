package org.treeleafj.xmax.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验中文
 *
 * Created by leaf on 2017/10/30.
 */
public class ChineseValidator implements Validator<String> {

    private String regex = "^[\u4e00-\u9fa5],{0,}$";
    private Pattern pattern = Pattern.compile(regex);

    @Override
    public boolean validate(String obj) {
        Matcher matcher = pattern.matcher(obj);
        return matcher.matches();
    }
}
