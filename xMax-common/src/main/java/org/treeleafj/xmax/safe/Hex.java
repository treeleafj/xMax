package org.treeleafj.xmax.safe;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Random;

/**
 * MD算法加密工具
 *
 * @author leaf
 * @date 2014-11-9 下午10:23:48
 */
public abstract class Hex {

	private static Logger log = LoggerFactory.getLogger(Hex.class);

	/**
	 * 自动根据要加密的内容生成固定的随机盐,并进行加密
	 */
	public static String autoSaltMd5(String s) {
		Random r = new Random();
		StringBuilder sb = new StringBuilder(16);
		sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
		int len = sb.length();
		if (len < 16) {
			for (int i = 0; i < 16 - len; i++) {
				sb.append("0");
			}
		}

		String salt = sb.toString();// 16位随机盐
		s = md5(s + salt);
		char[] cs = new char[48];
		for (int i = 0; i < 48; i += 3) {
			cs[i] = s.charAt(i / 3 * 2);
			char c = salt.charAt(i / 3);
			cs[i + 1] = c;
			cs[i + 2] = s.charAt(i / 3 * 2 + 1);
		}
		return new String(cs);
	}

	/**
	 * 自动根据明文校验与autoSaltMd5方法加密后得到的是否一致
	 */
	public static boolean verify(String s, String md5) {
		char[] cs1 = new char[32];
		char[] cs2 = new char[16];
		for (int i = 0; i < 48; i += 3) {
			cs1[i / 3 * 2] = md5.charAt(i);
			cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
			cs2[i / 3] = md5.charAt(i + 1);
		}
		String salt = new String(cs2);
		return md5(s + salt).equals(new String(cs1));
	}

	/**
	 * MD5加密(32位)
	 *
	 * @param s
	 * @return
	 */
	public static String md5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			log.error("MD5加密失败:" + s, e);
			throw new RuntimeException("密码加密失败!", e);
		}
	}

    /**
     * MD5(加盐)加密(32位)
     *
     * @param s    要加密的字符串
     * @param salt 盐值
     * @return
     */
    public static String md5(String s, String salt) {
        return md5(s + salt);
    }

}
