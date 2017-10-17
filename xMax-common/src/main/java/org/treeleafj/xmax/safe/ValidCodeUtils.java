package org.treeleafj.xmax.safe;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 验证码工具
 * <p>
 * Created by leaf on 2017/2/18 0018.
 */
public class ValidCodeUtils {

    private static char[] CODES = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    private static char[] NUM_CODES = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    /**
     * 构建验证码并返回
     *
     * @param isNumber 是否存数字
     * @param count    验证码数量
     * @return 验证码
     */
    public static String buildValidCode(boolean isNumber, int count) {
        char[] codes;
        if (isNumber) {
            codes = NUM_CODES;
        } else {
            codes = CODES;
        }
        StringBuilder sb = new StringBuilder();
        ThreadLocalRandom current = ThreadLocalRandom.current();
        for (int i = 0; i < count; i++) {
            sb.append(codes[current.nextInt(codes.length)]);
        }
        return sb.toString();
    }

    /**
     * 构建纯数字且7位长度的验证码并返回
     *
     * @return 验证码
     */
    public static String buildValidCode() {
        return buildValidCode(true, 4);
    }
}
