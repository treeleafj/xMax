package org.treeleafj.xmax.validate;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 身份证校验
 * <p>
 * Created by leaf on 2017/10/30.
 */
public class IDCardValidator implements Validator<String> {

    private static Map<String, String> cityMap = new HashMap<>();

    private static final String REGEX = "^(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)?$";

    private static Pattern pattern = Pattern.compile(REGEX);

    static {
        cityMap.put("11", "北京");
        cityMap.put("12", "天津");
        cityMap.put("13", "河北");
        cityMap.put("14", "山西");
        cityMap.put("15", "内蒙古");

        cityMap.put("21", "辽宁");
        cityMap.put("22", "吉林");
        cityMap.put("23", "黑龙江");

        cityMap.put("31", "上海");
        cityMap.put("32", "江苏");
        cityMap.put("33", "浙江");
        cityMap.put("34", "安徽");
        cityMap.put("35", "福建");
        cityMap.put("36", "江西");
        cityMap.put("37", "山东");

        cityMap.put("41", "河南");
        cityMap.put("42", "湖北");
        cityMap.put("43", "湖南");
        cityMap.put("44", "广东");
        cityMap.put("45", "广西");
        cityMap.put("46", "海南");

        cityMap.put("50", "重庆");
        cityMap.put("51", "四川");
        cityMap.put("52", "贵州");
        cityMap.put("53", "云南");
        cityMap.put("54", "西藏");

        cityMap.put("61", "陕西");
        cityMap.put("62", "甘肃");
        cityMap.put("63", "青海");
        cityMap.put("64", "宁夏");
        cityMap.put("65", "新疆");
    }

    /**
     * 是否采用严格校验
     */
    private boolean strict = true;

    public IDCardValidator() {
    }

    /**
     * @param strict 是否采用严格模式校验身份证,默认是true
     */
    public IDCardValidator(boolean strict) {
        this.strict = strict;
    }

    @Override
    public boolean validate(String obj) {
        if (strict) {
            return this.is18ByteIdCardComplex(obj);
        }
        return this.is18ByteIdCard(obj);
    }

    /**
     * 18位身份证校验,粗略的校验
     *
     * @param idCard 身份证
     */
    public boolean is18ByteIdCard(String idCard) {
        Matcher matcher = pattern.matcher(idCard);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 18位身份证校验,严格的校验
     *
     * @param idCard 身份证
     */
    public boolean is18ByteIdCardComplex(String idCard) {

        Matcher matcher = pattern.matcher(idCard);
        int[] prefix = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        int[] suffix = new int[]{1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2};
        if (matcher.matches()) {
            if (cityMap.get(idCard.substring(0, 2)) == null) {
                return false;
            }
            int idCardWiSum = 0; //用来保存前17位各自乖以加权因子后的总和
            for (int i = 0; i < 17; i++) {
                idCardWiSum += Integer.valueOf(idCard.substring(i, i + 1)) * prefix[i];
            }

            //计算出校验码所在数组的位置
            int idCardMod = idCardWiSum % 11;
            //得到最后一位身份证号码
            String idCardLast = idCard.substring(17);

            //如果等于2，则说明校验码是10，身份证号码最后一位应该是X
            if (idCardMod == 2) {
                if ("x".equalsIgnoreCase(idCardLast)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                //用计算出的验证码与最后一位身份证号码匹配，如果一致，说明通过，否则是无效的身份证号码
                if (idCardLast.equals(suffix[idCardMod] + "")) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

}
