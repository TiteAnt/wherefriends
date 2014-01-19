package com.tite.system.comfc.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密
 * @author zhuwf 2013-12-19 下午5:49:36
 * @version 7.0
 */

public class EncryptionUtils {


	/**
	 * 加密
	 * @param str String 需要加密的明文
	 * @throws Exception
	 * @return String 返回加密后的密文
	 * @throws NoSuchAlgorithmException 
	 */
	public static String encryption(String str) throws NoSuchAlgorithmException{
		MessageDigest md5 = null;
		
		md5 = MessageDigest.getInstance("MD5");
		
		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}

		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}
	
}
