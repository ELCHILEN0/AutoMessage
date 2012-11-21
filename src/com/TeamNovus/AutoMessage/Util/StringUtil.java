package com.TeamNovus.AutoMessage.Util;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

public class StringUtil {

	public static boolean isInteger(String s) {
		try {
			Integer.valueOf(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static String concat(String[] s, int start, int end) {
		String[] args = Arrays.copyOfRange(s, start, end);
		return StringUtils.join(args, " ");
	}
}
