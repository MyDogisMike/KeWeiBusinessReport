package com.bps.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.bps.exception.BaseException;
import com.bps.web.constants.Constants;

/**
 * 公共 有关的
 * 
 * @author andalee
 * @date 2017/3/31
 * @version 2.0
 */

public class CommonUtil {
	private static final Logger logger = Logger.getLogger(CommonUtil.class);

	/**
	 * 根据Cookie信息获取当前登录用户信息，如果不合法则返回null
	 * 
	 * @param cookies
	 * @return
	 */

	public static List<Map<String, Object>> listMapToLowerCase(
			List<Map<String, Object>> list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				list.set(i, mapToLowerCase(map));
			}
		}
		return list;
	}

	public static Map<String, Object> mapToLowerCase(Map<String, Object> inMap) {
		Map<String, Object> resultMap = null;
		if (inMap != null) {
			if (LinkedHashMap.class.isInstance(inMap)) {
				resultMap = new LinkedHashMap<String, Object>();
			} else if (HashMap.class.isInstance(inMap)) {
				resultMap = new HashMap<String, Object>();
			} else {
				return inMap;
			}
			for (Iterator<Entry<String, Object>> it = inMap.entrySet()
					.iterator(); it.hasNext();) {
				Entry<String, Object> e = it.next();
				String new_key = e.getKey().toLowerCase();
				resultMap.put(new_key, e.getValue());
			}
		}
		return resultMap;
	}


	public static String changeEncode(String str, String fromEncode,
			String toEncode) throws UnsupportedEncodingException {
		String returnStr;
		returnStr = new String(str.getBytes(fromEncode), toEncode);
		return returnStr;
	}

	public static String changeEncodeToUtf8(String str)
			throws UnsupportedEncodingException {
		return changeEncode(str, "ISO-8859-1", "UTF-8");
	}

	public static String formatDate(Date date, String format) {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.format(date);
	}

	public static String formatDate(Date date) {
		return formatDate(date, "yyyy-MM-dd");
	}

	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date toDate(String date_string, String format) {
		try {
			SimpleDateFormat sf = new SimpleDateFormat(format);
			return sf.parse(date_string);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date toDate(String date_string) {
		String format = "yyyy-MM-dd";
		if (date_string.indexOf(":") >= 0) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		return toDate(date_string, format);
	}

	public static List<String> changeToList(String str) {
		if (isEmpty(str)) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		int fromIndex = 0;
		int toIndex = 0;
		while ((toIndex = str.indexOf("},{", fromIndex)) > -1) {
			String s = str.substring(fromIndex, toIndex + 1);
			fromIndex = toIndex + 2;
			list.add(s);
		}
		String s = str.substring(fromIndex);
		if (!isEmpty(s)) {
			list.add(s);
		}
		return list;
	}

	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmpty(Object str) {
		if (str == null) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmpty(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmpty(Long str) {
		if (str == null) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmpty(List<Object> list) {
		if (list == null || list.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmpty(String[] arr) {
		if (arr == null || arr.length == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 把jsonobject对象转化为HashMap对象
	 * 
	 * @param jsonObject
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> jsonToMap(JSONObject jsonObject) {
		HashMap<String, Object> result_map = null;
		if (jsonObject != null) {
			result_map = new HashMap<String, Object>();
			for (Iterator<Entry<String, Object>> it = jsonObject.entrySet()
					.iterator(); it.hasNext();) {
				Entry<String, Object> o = it.next();
				String key = o.getKey();
				Object value_obj = o.getValue();
				if (JSONArray.class.isInstance(value_obj)) {
					JSONArray arr = (JSONArray) value_obj;
					result_map.put(key,
							JSONArray.toList(arr, "", new JsonConfig()));
				} else {
					result_map.put(key, value_obj);
				}
			}
		}
		return result_map;
	}

	// public static HashMap extGridOut(List root, int totalProperty) {
	// HashMap resultMap = new HashMap();
	// resultMap.put("root", root);
	// resultMap.put("totalProperty", totalProperty);
	// return resultMap;
	// }

	/**
	 * 从map中获取String
	 * 
	 * @date 2017/3/31
	 * @time 下午05:18:31
	 * @param map
	 * @param key
	 * @return
	 */
	public static String getMapString(Map<String, Object> map, String key) {
		if (map == null) {
			return null;
		}
		Object value_obj = map.get(key);
		if (value_obj == null) {
			return null;
		} else {
			return value_obj.toString().trim();
		}
	}

	/**
	 * 从map中获取long
	 * 
	 * @date 2017/3/31
	 * @time 下午05:18:20
	 * @param map
	 * @param key
	 * @return
	 */
	public static Long getMapLong(HashMap<String, Object> map, String key) {
		String value = getMapString(map, key);
		if (isEmpty(value)) {
			return null;
		} else {
			return Long.parseLong(value);
		}
	}

	public static Double getMapDouble(HashMap<String, Object> map, String key) {
		String value = getMapString(map, key);
		if (isEmpty(value)) {
			return null;
		} else {
			return Double.parseDouble(value);
		}
	}

	/**
	 * 从map中获取Int
	 * 
	 * @date 2017/3/31
	 * @time 下午05:18:20
	 * @param map
	 * @param key
	 * @return
	 */
	public static Integer getMapInt(HashMap<String, Object> map, String key) {
		String value = getMapString(map, key);
		if (isEmpty(value)) {
			return null;
		} else {
			return Integer.parseInt(value);
		}
	}


	public static HashMap<String, Object> stringToMap(String str) {
		HashMap<String, Object> map = null;
		try {
			JSONObject json_obj = new JSONObject();
			json_obj = JSONObject.fromObject(str);
			map = jsonToMap(json_obj);
		} catch (Exception e) {// 出错不影响全局
		}
		return map;
	}


	/**
	 * 在位数不到total的情况下前面补0,默认长度过长则裁剪
	 * 
	 * @date 2011-7-21
	 * @time 上午11:36:16
	 * @param number
	 * @param total
	 * @param cut_if_long
	 * @return
	 */
	public static String addZeroToNumber(Long number, int total) {
		if (number == null) {
			return null;
		}
		String number_string = number.toString();
		return addZeroToNumber(number_string, total, true);
	}

	/**
	 * 在位数不到total的情况下前面补0
	 * 
	 * @date 2017/3/31
	 * @time 上午11:36:16
	 * @param number
	 * @param total
	 * @param cut_if_long
	 * @return
	 */
	public static String addZeroToNumber(String number, int total,
			boolean cut_if_long) {
		if (number == null) {
			return null;
		}
		int length = number.length();
		if (length < total) {
			for (int i = length; i < total; i++) {
				number = "0" + number;
			}
		} else if (cut_if_long && length > total) {
			number = number.substring(length - total, length);
		}
		return number;
	}

	public static String format(String value, String type) {
		if ("".equals(value)) {
			value = "''";
		} else {
			if ("date".equals(type)) {
				value = "to_date('" + value + "','YYYY/MM/DD HH24:MI:SS')";
			} else if ("int".equals(type) || "string".equals(type)) {
				value = "'" + value + "'";
			}
		}

		return value;
	}

	public static String getNowTimeString() {
		return getNowTimeString("yyyyMMddHHmmss");
	}

	public static String getNowDateString() {
		return getNowTimeString("yyyyMMdd");
	}

	public static String getNowTimeString(String format) {
		return formatDate(new Date(), format);
	}

	public static String arrayToString(String[] arr) {
		if (arr == null) {
			return null;
		} else {
			String re = "";
			for (int i = 0; i < arr.length; i++) {
				re += arr[i] + ",";
			}
			if (!"".equals(re)) {
				re = re.substring(0, re.length() - 1);
			}
			return re;
		}
	}

	public static String listToString(List<String> list) {
		return listToString(list, ",");
	}

	public static String listToString(List<String> list, String split) {
		if (list == null) {
			return null;
		} else {
			String re = "";
			for (String s : list) {
				re += s + split;
			}
			if (!"".equals(re)) {
				re = re.substring(0, re.length() - 1);
			}
			return re;
		}
	}

	/**
	 * 取得两个数组中的公共部分
	 * 
	 * @author andalee
	 * @date 2017/3/31
	 * @time 上午12:19:06
	 * @updater
	 * @update-time
	 * @update-info
	 * @param new_phone_numbers
	 * @param old_phone_numbers
	 * @return
	 */
	public static List<String> getExistNumbers(List<String> new_phone_numbers,
			List<String> old_phone_numbers) {
		List<String> result_list = new ArrayList<String>(
				Arrays.asList(new String[old_phone_numbers.size()]));
		Collections.copy(result_list, old_phone_numbers);
		result_list.retainAll(new_phone_numbers);
		return result_list;
	}

	/**
	 * 取得第一个数组相对第二个数组新增的
	 * 
	 * @author andalee
	 * @date 2017/3/31
	 * @time 上午12:19:25
	 * @updater
	 * @update-time
	 * @update-info
	 * @param new_phone_numbers
	 * @param old_phone_numbers
	 * @return
	 */
	public static List<String> getNewNumbers(List<String> new_phone_numbers,
			List<String> old_phone_numbers) {
		List<String> result_list = new ArrayList<String>(
				Arrays.asList(new String[new_phone_numbers.size()]));
		Collections.copy(result_list, new_phone_numbers);
		result_list.removeAll(old_phone_numbers);
		return result_list;
	}

	/**
	 * 取得第一个数组相对第二个数组缺少的
	 * 
	 * @author ye
	 * @date 2017/3/31
	 * @time 上午12:19:44
	 * @updater
	 * @update-time
	 * @update-info
	 * @param new_phone_numbers
	 * @param old_phone_numbers
	 * @return
	 */
	public static List<String> getRemoveNumbers(List<String> new_phone_numbers,
			List<String> old_phone_numbers) {
		List<String> result_list = new ArrayList<String>(
				Arrays.asList(new String[old_phone_numbers.size()]));
		Collections.copy(result_list, old_phone_numbers);
		result_list.removeAll(new_phone_numbers);
		return result_list;
	}

	// public static String urlEncode(String word) {
	// return java.net.URLEncoder.encode(word);
	// }
	//
	// public static String urlEncode(String word, String code)
	// throws UnsupportedEncodingException {
	// return java.net.URLEncoder.encode(word, code);
	// }
	//
	// public static String urlDecode(String word) {
	// return java.net.URLDecoder.decode(word);
	// }
	//
	// public static String urlDecode(String word, String code)
	// throws UnsupportedEncodingException {
	// return java.net.URLDecoder.decode(word, code);
	// }

	public static Date addMonth(Date from_date, int month) {
		// Date d = (Date) from_date.clone();
		Calendar c = Calendar.getInstance();
		c.setTime(from_date);
		int new_month = c.get(Calendar.MONTH);
		new_month += month;
		int add_year = new_month / 12;
		new_month %= 12;
		if (add_year > 0) {
			int year = c.get(Calendar.YEAR);
			c.set(Calendar.YEAR, year + add_year);
			// d.setYear(d.getYear() + add_year);
		}
		c.set(Calendar.MONTH, new_month);
		return c.getTime();
	}

	// public static void main(String[] args) {
	// Date d1 = new Date();
	// Date d2 = CommonUtil.addMonth(d1, 13);
	// System.out.println(CommonUtil.formatDate(d1));
	// System.out.println(CommonUtil.formatDate(d2));
	// }

	public static boolean isBeforeNow(Date from_date) {
		Date now_date = new Date();
		if (from_date.before(now_date)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isBeforeOrEqualNow(Date from_date) {
		Date now_date = new Date();
		if (now_date.before(from_date)) {
			return false;
		} else {
			return true;
		}
	}

	public static int getDaysBetweenDate(Date from_date, Date to_date) {
		int days;
		days = (int) ((to_date.getTime() - from_date.getTime()) / (24 * 60 * 60 * 1000));
		return days;
	}

	public static double getDoubleLeft(double f, int place) {
		BigDecimal b = new BigDecimal(f);
		double f1 = b.setScale(place, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;
	}

	public static boolean isPhoneNo(String phoneNo) {
		boolean isPhone = false;
		if (phoneNo != null && phoneNo.length() == 11) {// 手机号码为11位
			String firstChar = phoneNo.substring(0, 1);
			if ("1".equals(firstChar)) {// 且第一位为1
				isPhone = true;
			}
		}
		return isPhone;
	}

	public static String getPhone(String phoneNo) {
		String returnPhone = null;
		if (phoneNo != null) {
			returnPhone = phoneNo.replace("+86", "");
			returnPhone = returnPhone.trim();
		}
		return returnPhone;
	}

	 
	// public static void main(String[] args) {
	// double d2 = CommonUtil.getDoubleLeft(35d, 3);
	// System.out.println(d2);
	// }

	// public static void main4(String[] args) {
	// Date d1 = new Date("2011/12/8");
	// boolean d2 = CommonUtil.isBeforeNow(d1);
	// System.out.println(d2);
	// }
	//
	// public static void main2(String[] args) {
	// Date d1 = new Date("2000/02/01");
	// d1 = CommonUtil.addMonth(d1, 36);
	// System.out.println(CommonUtil.formatDate(d1));
	// }
	//
	// public static void main3(String[] args) {
	// Date d1 = new Date("2000/02/01");
	// Date d2 = new Date("2000/03/01");
	// int b1 = CommonUtil.getDaysBetweenDate(d1, d2);
	// Date d3 = new Date("2011/02/01");
	// Date d4 = new Date("2011/03/01");
	// int b2 = CommonUtil.getDaysBetweenDate(d3, d4);
	// System.out.println(b1);
	// System.out.println(b2);
	// }
	// public static void main(String[] args) {
	// HashMap m = new HashMap();
	// m.put("ASDF", "asdf");
	// HashMap t = (HashMap) mapToLowerCase(m);
	// System.out.print(HashMap.class.isInstance(m));
	// }

	// public static Object mapToLowerCase(LinkedHashMap<String, Object> inMap)
	// {
	// if (inMap == null) {
	// return null;
	// }
	// LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String,
	// Object>();
	// for (Iterator<Entry<String, Object>> it = inMap.entrySet().iterator(); it
	// .hasNext();) {
	// Entry<String, Object> e = it.next();
	// String key = e.getKey().toString().toLowerCase();
	// Object valueObj = e.getValue();
	// resultMap.put(key, valueObj);
	// }
	// return resultMap;
	// }
}
