package com.bps.web.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.ui.ModelMap;

import com.bps.util.DateUtil;


public class ValidatorUtil {
	public static String ERR_EMPTY = "can not be empty";
	public static String ERR_NUMBER = "not a number";
	public static String STATUS_SUCCESS  = "SUCCESS";
	public static String STATUS_FAIL  = "FAILED";
	
	public static String KEY_STATUS = "status";
	public static String KEY_ERROR_MSG = "error_msg";
	public static String KEY_RESULT = "data";
	public static String ERROR_SYSTEM  = "SYSTEM EXCEPTION"; 
	
	
	public static String ERR_DATE_YYYY_MM_DD = "the date formate must be \"YYYY-MM-DD\"";
	public static boolean isEmptyOrWhitespace(String str) {
		if (null == str) {
			return true;
		}
		str = str.trim();
		if (StringUtils.isEmpty(str)) {
			return true;
		}
		return false;
	}
	
	public static boolean isNotNumber(String str) {
		if (null == str) {
			return true;
		}
		str = str.trim();
		if (!StringUtils.isNumeric(str)) {
			return true;
		}
		return false;
	}
	
	public static void SUCCESS(final ModelMap modelMap) {
		modelMap.put(ValidatorUtil.KEY_STATUS, ValidatorUtil.STATUS_SUCCESS);
	}
	
	public static boolean checkSimpDateFormate(String dateStr){
		try {
			DateUtil.str2Date(dateStr);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}
}
