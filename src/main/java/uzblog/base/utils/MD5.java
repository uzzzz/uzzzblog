/*
+--------------------------------------------------------------------------
|   
|   ========================================
|    
|   
|
+---------------------------------------------------------------------------
*/
package uzblog.base.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;

/**
 * MD5摘要
 */
public class MD5 {

	/**
	 * 对字符串进行Md5加密
	 * 
	 * @param input 原文
	 * @return md5后的密文
	 */
	private static String md5(String input) {
		byte[] code = null;
		try {
			code = MessageDigest.getInstance("md5").digest(input.getBytes());
		} catch (NoSuchAlgorithmException e) {
			code = input.getBytes();
		}
		BigInteger bi = new BigInteger(code);
		return bi.abs().toString(32).toUpperCase();
	}

	/**
	 * 对字符串进行Md5加密
	 * 
	 * @param password 原文
	 * @param salt     随机数
	 * @return string
	 */
	public static String encryptPasswordMD5(String password, String salt) {
		if (StringUtils.isEmpty(salt)) {
			return md5(password);
		}
		return md5(salt + md5(password));
	}
}
