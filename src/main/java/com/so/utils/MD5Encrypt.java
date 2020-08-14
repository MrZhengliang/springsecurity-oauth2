package com.so.utils;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 加密
 * 
 * @author
 * @since 2009-4-2 下午02:32:38
 * @Contact <a href="mailto:hao.yh@qq.com">hao.yh@qq.com</a>
 * @copyright Woniu.com
 */
public class MD5Encrypt {

	/**
	 * 使用MD5加密
	 * 
	 * @param signature
	 *            待加密字符串
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String signature) throws RuntimeException {
		return encrypt(signature, "", true);
	}

	/**
	 * 使用MD5加密 - 带编码，用于中文加密
	 * 
	 * @param signature
	 * @param encode
	 * @return
	 * @throws RuntimeException
	 */
	public static String encrypt(String signature, String encode) throws RuntimeException {
		return encrypt(signature, encode, true);
	}

	/**
	 * 使用MD5加密
	 * 
	 * @param signature
	 *            待加密字符串
	 * @param encode
	 *            字符编码(用于中文加密)
	 * @param isUpperCase
	 *            返回加密结果是否转为大写
	 * @return
	 * @throws RuntimeException
	 */
	public static String encrypt(String signature, String encode, boolean isUpperCase) throws RuntimeException {
		if (signature == null) {
			return null;
		}
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("加密算法错误");
		}
		String ret = null;
		byte[] plainText = null;
		try {
			if (StringUtils.isBlank(encode)) {
				plainText = signature.getBytes();
			} else {
				plainText = signature.getBytes(encode);
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("MD5加密异常", e);
		}

		md5.update(plainText);
		ret = bytes2HexString(md5.digest(), isUpperCase);
		return ret;
	}

	/**
	 * 二行制转字符串
	 * 
	 * @param bytes
	 *            字节数组
	 * @param isUpperCase
	 *            返回加密结果是否转为大写
	 * @return 二进制字符串
	 */
	public static String bytes2HexString(byte[] bytes, boolean isUpperCase) {
		String hs = "";
		if (bytes != null) {
			for (byte b : bytes) {
				String tmp = (Integer.toHexString(b & 0XFF));
				if (tmp.length() == 1) {
					hs += "0" + tmp;
				} else {
					hs += tmp;
				}
			}
		}
		if (isUpperCase) {
			return hs.toUpperCase();
		} else {
			return hs;
		}
	}

/*	public static void main(String[] args) {
		System.out.println(MD5Encrypt.encrypt("*()FJ!#$I!@OI#UKLFJD:SFLU!OP@#U", "utf-8"));
	}
	*/
	public static String getSHA1(String input) throws NoSuchAlgorithmException {
		return encryptMethod(input, "SHA-1");
	}
	
	public static String encryptMethod(String strSrc, String encName)
			throws NoSuchAlgorithmException {
		MessageDigest md = null;
		String strDes = null;
		byte[] bt = strSrc.getBytes();
		if (encName == null || "".equals(encName)) {
			encName = "MD5";
		}
		md = MessageDigest.getInstance(encName);
		md.update(bt);
		strDes = bytes2Hex(md.digest()); // to HexString

		return strDes;
	}
	
	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
}
