package cn.dmego.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StringUtil {
	/**
	 * String 转 float
	 * @param str
	 * @return
	 */
	public static float StrToFlo(String str) {
		float result = 0;
		try {
			result = Integer.parseInt(str);
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		return result;
	}
	
	/**
	 * String 转 int 
	 * @param str
	 * @return
	 */
	public static int StrToInt(String str) {
		int result = 0;
		try {
			result = Integer.parseInt(str);
		}catch(NumberFormatException e) {
			result = 0;
		}
		return result;
	}
	
	
	/**
	 * 获取盐值（随机字符串）
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static List<String> splitProperties(String properties) {
		List<String> options = new ArrayList<String>();
		String option = null;
		String[] strings = properties.split(",");
		for (String string : strings) {
			if (!"0".equals(string)) {
				option = string.charAt(3) + "";
				options.add(option);
			}
		}
		return options;
	}
}
