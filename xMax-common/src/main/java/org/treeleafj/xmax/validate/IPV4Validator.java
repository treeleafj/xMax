package org.treeleafj.xmax.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IP V4校验
 * <p>
 * Created by leaf on 2017/10/30.
 */
public class IPV4Validator implements Validator<String> {
    private final String num = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

    private String regex = "^" + num + "\\." + num + "\\." + num + "\\." + num + "$";

    private Pattern pattern = Pattern.compile(regex);

    @Override
    public boolean validate(String obj) {
        Matcher matcher = pattern.matcher(obj);
        return matcher.matches();
    }
}
