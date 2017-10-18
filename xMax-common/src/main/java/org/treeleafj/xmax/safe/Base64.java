package org.treeleafj.xmax.safe;

/**
 * @Author leaf
 * 2015/9/12 0012 1:27.
 */
public class Base64 {

    /**
     * 将byte数据进行Base64编码
     *
     * @param data
     * @return
     */
    public static String encode(byte[] data) {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(data);
    }

    /**
     * 将字符串进行base64解码
     *
     * @param data
     * @return
     */
    public static byte[] decode(String data) {
        return org.apache.commons.codec.binary.Base64.decodeBase64(data);
    }

    /**
     * 将byte数据进行url安全的Base64编码
     *
     * @param data
     * @return
     */
    public static String encodeURLSafe(byte[] data) {
        return org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(data);
    }

    /**
     * 将URL安全的字符串进行base64解码
     *
     * @param data
     * @return
     */
    public static byte[] decodeURLSafe(String data) {
        return decode(data);
    }
}
