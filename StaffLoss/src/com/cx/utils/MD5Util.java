package com.cx.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

	public  final static String MD5(String s){
		 char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};       
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
	            e.printStackTrace();
	            return null;
	}	
	}
	
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		/*System.out.println(MD5Util.MD5("20121221"));
       System.out.println(MD5Util.MD5("加密"));
       System.out.println(MD5Util.MD5("123"));
       */
	}
	public static String getMd532Value(String sSecret) {
		try {
		MessageDigest bmd5 = MessageDigest.getInstance("MD5");
		bmd5.update(sSecret.getBytes());
		int i;
		StringBuffer buf = new StringBuffer();
		byte[] b = bmd5.digest();
		for (int offset = 0; offset < b.length; offset++) {
		i = b[offset];
		if (i < 0)
		i += 256;
		if (i < 16)
		buf.append("0");
		buf.append(Integer.toHexString(i));
		}
		return buf.toString();
		} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
		}
		return "";
		}
}
