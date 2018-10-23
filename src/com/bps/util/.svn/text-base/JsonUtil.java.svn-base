package com.bps.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.log4j.Logger;

/**
 * json 有关的
 * 
 * @author andalee
 * @date 2017/3/31
 * @version 2.0
 */
public class JsonUtil {
	private static final Logger logger = Logger.getLogger(JsonUtil.class);

	/** Commons Logging instance. */
	private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
			.getLog(JsonUtil.class);

	/**
	 * @param obj
	 *            任意对象
	 * @return String
	 * @throws SQLException
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static String object2json(Object obj) throws SQLException {
		StringBuilder json = new StringBuilder();
		if (obj == null) {
			json.append("\"\"");
		} else if (obj instanceof String || obj instanceof Byte) {
			json.append("\"").append(string2json(obj.toString())).append("\"");
		} else if (obj instanceof Integer || obj instanceof Float
				|| obj instanceof Short || obj instanceof Double
				|| obj instanceof BigDecimal || obj instanceof BigInteger) {
			json.append("").append(string2json(obj.toString())).append("");
		} else if (obj instanceof Boolean) {
			json.append("").append(string2json(obj.toString())).append("");
		} else if (obj instanceof Long) {
			json.append("").append(string2json(obj.toString())).append("");
		} else if (obj instanceof Object[]) {
			json.append(array2json((Object[]) obj));
		} else if (obj instanceof List) {
			json.append(list2json((List<?>) obj));
		} else if (obj instanceof Map) {
			json.append(map2json((Map<?, ?>) obj));
			// json.append(map2jsonLow((Map<?, ?>) obj));
		} else if (obj instanceof Set) {
			json.append(set2json((Set<?>) obj));
		}
		// else if (obj instanceof Date) {
		// json.append("\"").append(date2json((Date) obj)).append("\"");
		// }
		else {
			json.append(bean2json(obj));
		}
		return json.toString();
	}

	public static String date2json(Date date) {
		return CommonUtil.formatDate(date, "yyyy-MM-dd");
	}

	/**
	 * @param bean
	 *            bean对象 调用GET SET方法
	 * @return String
	 */
	public static String bean2json(Object bean) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		PropertyDescriptor[] props = null;
		try {
			props = Introspector.getBeanInfo(bean.getClass(), Object.class)
					.getPropertyDescriptors();
		} catch (IntrospectionException e) {
		}
		if (props != null) {
			for (int i = 0; i < props.length; i++) {
				try {
					String name = object2json(props[i].getName());

					String value = object2json(props[i].getReadMethod().invoke(
							bean));
					if ("\"callbacks\"".equals(name)) {
						value = "\"\"";
					}
					json.append(name);
					json.append(":");

					json.append(value);
					json.append(",");
				} catch (Exception e) {
				}
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	/**
	 * @param list
	 *            list对象
	 * @return String
	 * @throws SQLException
	 */
	public static String list2json(List<?> list) throws SQLException {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	/**
	 * @param array
	 *            对象数组
	 * @return String
	 * @throws SQLException
	 */
	public static String array2json(Object[] array) throws SQLException {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (array != null && array.length > 0) {
			for (Object obj : array) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	/**
	 * @param map
	 *            map对象
	 * @return String
	 * @throws SQLException
	 */
	public static String map2json(Map<?, ?> map) throws SQLException {
		StringBuilder json = new StringBuilder();
		json.append("{");
		if (map != null && map.size() > 0) {
			for (Object key : map.keySet()) {
				json.append(object2json(key));
				json.append(":");
				json.append(object2json(map.get(key)));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	/**
	 * @param map
	 *            map对象 大写转为小写 用于IBATIS生成MAP时使用
	 * 
	 * @return String
	 * @throws SQLException
	 */
	public static String map2jsonLow(Map<?, ?> map) throws SQLException {
		StringBuilder json = new StringBuilder();
		json.append("{");
		if (map != null && map.size() > 0) {
			for (Object key : map.keySet()) {
				json.append(object2json(key).toLowerCase());
				json.append(":");
				json.append(object2json(map.get(key)));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	/**
	 * @param set
	 *            集合对象
	 * @return String
	 * @throws SQLException
	 */
	public static String set2json(Set<?> set) throws SQLException {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (set != null && set.size() > 0) {
			for (Object obj : set) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	/**
	 * @param s
	 *            参数
	 * @return String
	 */
	public static String string2json(String s) {
		if (s == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if (ch >= '\u0000' && ch <= '\u001F') {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}
}
