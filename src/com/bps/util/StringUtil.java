package com.bps.util;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.commons.lang.StringUtils;


public class StringUtil {


	/**
	 * unicode 转换成 中文
	 * 
	 * @author
	 * @param theString
	 * @return
	 */

	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed   \\uxxxx   encoding.");
						}

					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}
	
	public static String pStr(String str){
		if(str==null){
			return "";
		}
		str = StringUtils.trim(str);
		str = str.replaceAll("'", "＇").replace("\\", "");
		return str;
	}


	/**
	 * 把字符串根据分隔符生成数组。
	 * 
	 * @param line
	 *            字符串
	 * @param delim
	 *            分隔符
	 * @return String[] 字符串数组
	 */
	public static String[] split(String line, String delim) {
		if (line == null) {
			return new String[0];
		}
		List list = new ArrayList();
		StringTokenizer t = new StringTokenizer(line, delim);

		while (t.hasMoreTokens()) {
			list.add(t.nextToken());
		}

		return (String[]) list.toArray(new String[list.size()]);
	}

	/**
	 * 转化日志的编码
	 * 
	 * @param logInfo
	 *            日志信息
	 * @return String 转化编码后的日志信息
	 */
	public static String transLog(String logInfo) {
		return StringUtil.transChiTo8859(logInfo);
	}

	/**
	 * 解决中文的乱码问题
	 * 
	 * @param chi
	 *            为输入的要汉化的字符串
	 * @return String 经过转换后的字符串
	 */
	public static String transChiTo8859(String chi) {

		if (StringUtil.isEmpty(chi))
			return "";

		String result = null;
		byte temp[];
		try {
			temp = chi.getBytes("GBK");
			result = new String(temp, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
		}

		return result;

	}

	public static String parseEnter(String html) {
		String reg = "[\r\n]";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("\3\2\1");
		return s;
	}

	public static String parseHtml(String html) {
		if (html != null) {
			return html.replaceAll("\r", "<br>");
		}
		return "";
	}

	/**
	 * 取得某个数值是否被选择中,值=1? 支持最多10位
	 * 
	 * @author andalee
	 * @param num
	 *            数字
	 * @param idx
	 *            位置
	 * @return
	 */
	public static boolean getBinIsOne(int num, int idx) {
		String str = "0000000000" + Integer.toBinaryString(num);
		str = str.substring(str.length() - 10);
		if (str.substring(10 - idx, 11 - idx).equalsIgnoreCase("1")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断多选的是否选中
	 * 
	 * @param str
	 * @param i
	 * @return
	 */
	public static boolean checkSelected(String str, int i) {
		if (str == null) {
			return false;
		}
		try {
			String[] splits = split(str, ",");
			for (String s_num : splits) {
				int num = Integer.parseInt(s_num);
				if (num == i) {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * 城市值
	 * 
	 * @author andalee
	 * @param toAreaIds
	 * @return
	 */
	public String getCityByArea(String area) {
		if (area != null && area.length() > 1) {
			return area.substring(0, area.length());
		} else {
			return "";
		}
	}

	/**
	 * 
	 * @author andalee
	 * @param list
	 * @param index
	 * @return
	 */
	public Object getObjByList(List list, int index) {
		if (list == null || list.isEmpty()) {
			return new Object();
		} else {
			return list.get(index);
		}
	}

	public static boolean isEmpty(String str) {
		if (str != null && !str.trim().equalsIgnoreCase("")) {
			return false;
		}
		return true;
	}

	/**
	 * key1 \1 value1 \0 key2 \1 value2
	 * 
	 * @param richText
	 * @param key
	 * @return
	 */
	public String getRichTextValue(String richText, String key) {
		if (richText != null) {
			String[] arr = StringUtil.split(richText, "\0");
			for (String str : arr) {
				String[] obj = StringUtil.split(str, "\1");
				if (obj != null && obj.length == 2) {
					if (key.equalsIgnoreCase(obj[0])) {
						return obj[1];
					}
				}
			}

		}
		return "";
	}

	/**
	 * key1=value1 \1 key2=value2 \2 key1=value1 \1 key2=value2 取得酒店房型信息
	 * 
	 * @author andalee
	 * @param richText
	 * @return
	 */
	public String getHotelHouseInfo(String richText) {
		if (richText == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		String[] arr = StringUtil.split(richText, "\2");
		for (String str : arr) {
			String[] obj = StringUtil.split(str, "\1");
			for (String fieldStr : obj) {
				String[] fields = fieldStr.split("=");
				if (fields[0].equalsIgnoreCase("houseType")) {
					sb.append(fields[1]).append(":");
				} else if (fields[0].equalsIgnoreCase("housePrice")) {
					sb.append(fields[1]).append(" * ");
				} else if (fields[0].equalsIgnoreCase("houseNum")) {
					sb.append(fields[1]);
				}
			}
			sb.append("<br>");
		}
		return sb.toString();
	}

	/**
	 * 取得某个用户的增减费用 type=1 增 type=-1 减
	 * 
	 * @param feeText
	 * @param type
	 * @return
	 */
	public int getTouristFee(String feeText, int type) {
		int addRet = 0;
		int subRet = 0;
		if (feeText != null) {
			String[] arr = StringUtil.split(feeText, "\0");
			for (String str : arr) {
				String[] obj = StringUtil.split(str, "\1");
				if (obj != null && obj.length == 2) {
					if ("price10".equalsIgnoreCase(obj[0])
							|| "price11".equalsIgnoreCase(obj[0])
							|| "price12".equalsIgnoreCase(obj[0])
							|| "price13".equalsIgnoreCase(obj[0])
							|| "price14".equalsIgnoreCase(obj[0])
							|| "price15".equalsIgnoreCase(obj[0])) {
						try {
							int i = Integer.parseInt(obj[1]);
							subRet += i;
						} catch (Exception e) {
						}
					} else {
						try {
							int i = Integer.parseInt(obj[1]);
							addRet += i;
						} catch (Exception e) {
						}
					}
				}
			}
		}

		if (type == 1) {
			return addRet;
		} else {
			return subRet;
		}
	}

	public static String getReceiveStat(int receiveStat) {
		String returnStr = "";
		switch (receiveStat) {
		case 0:
			returnStr = "收客中";
			break;
		case 1:
			returnStr = "取消";
			break;
		case 2:
			returnStr = "停售";
			break;
		case 3:
			returnStr = "代理停售";
			break;
		}
		return returnStr;
	}

	public static String getNotice(String name, Timestamp createTime,
			int length, String tourIds) {
		StringBuffer ret = new StringBuffer();

		try {
			if (name.getBytes("GBK").length > length) {
				ret.append("<span title='").append(name).append("'>").append(
						dochar(name, length)).append("...").append("</span>");
			} else {
				ret.append(name);
			}
		} catch (Exception e) {

		}
		// 当线路分类不为null时,在线路之后加上线路分类
		if (tourIds != null && tourIds.trim() != "") {
			ret.append("<span class=\"tourIds\">");
			for (int i = 1; i <= 11; i++) {
				if (checkSelected(tourIds, i)) {
					if (i == 1) {
						ret.append("春节&nbsp;");
					}
					if (i == 2) {
						ret.append("国庆&nbsp;");
					}
					if (i == 3) {
						ret.append("五一&nbsp;");
					}
					if (i == 4) {
						ret.append("中秋&nbsp;");
					}
					if (i == 5) {
						ret.append("元旦&nbsp;");
					}
					if (i == 6) {
						ret.append("特价&nbsp;");
					}
					if (i == 7) {
						ret.append("控位&nbsp;");
					}
					if (i == 8) {
						ret.append("推荐&nbsp;");
					}
					if (i == 9) {
						ret.append("金假期&nbsp;");
					}
					if (i == 10) {
						ret.append("醉夕阳&nbsp;");
					}
					if (i == 11) {
						ret.append("小脚丫&nbsp;");
					}
				}
			}
			ret.append("</span>");
		}
		Date date = new Date();
		if (createTime != null
				&& (date.getTime() - createTime.getTime()) / (1000 * 60 * 60) < 24) {
			ret.append("<span class=\"new\">NEW</span>");
		} else {
			ret.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\r\t");
		}
		return ret.toString();
	}

	public static String getNotice(String name, Timestamp createTime) {
		int length = 12;
		return getNotice(name, createTime, length, null);
	}

	/**
	 * 截取字符串 包括中文
	 * 
	 * @param str
	 * @param count
	 * @return
	 */
	public static String dochar(String str, int count) {
		try {
			byte[] temp = str.getBytes("GBK");
			byte[] bArray = new byte[count * 2];

			int i;
			int ii = 0;// 用于判断最后一个是不是一半汉字
			String strc = "full";

			for (i = 0; i < count; i++) {
				bArray[i] = temp[i];
			}
			for (i = 0; i < count; i++) {
				if (bArray[i] < 0) {
					ii++;
				}
			}
			if (ii % 2 != 0) {
				strc = "hard";
			}

			// 截下去为完全的时候
			if (strc.equals("full") && bArray[i] < 0) {
				bArray[i] = ' ';
			}
			// 截下去为一半的时候
			if (strc.equals("hard") && bArray[i - 1] < 0) {
				bArray[i - 1] = ' ';
			}
			// String ret = new String(bArray,"utf8");
			String ret = new String(bArray, "gbk");
			// String ret2 = new String(bArray,"iso8859-1");
			return ret.trim();
		} catch (Exception e) {
			return str;
		}
	}

	public static String getChnmoney(double number_i) {
		String strNum = String.valueOf(number_i);
		int n, m, k, i, j, q, p, r, s = 0;
		int length, subLength, pstn;
		String change, output, subInput, input = strNum;
		output = "";
		if (strNum.equals(""))
			return null;
		else {
			length = input.length();
			pstn = input.indexOf('.'); // 小数点的位置

			if (pstn == -1) {
				subLength = length;// 获得小数点前的数字
				subInput = input;
			} else {
				subLength = pstn;
				subInput = input.substring(0, subLength);
			}

			char[] array = new char[4];
			char[] array2 = { '仟', '佰', '拾' };
			char[] array3 = { '亿', '万', '元', '角', '分' };

			n = subLength / 4;// 以千为单位
			m = subLength % 4;

			if (m != 0) {
				for (i = 0; i < (4 - m); i++) {
					subInput = '0' + subInput;// 补充首位的零以便处理
				}
				n = n + 1;
			}
			k = n;

			for (i = 0; i < n; i++) {
				p = 0;
				change = subInput.substring(4 * i, 4 * (i + 1));
				array = change.toCharArray();// 转换成数组处理

				for (j = 0; j < 4; j++) {
					output += formatC(array[j]);// 转换成中文
					if (j < 3) {
						output += array2[j];// 补进单位，当为零是不补（千百十）
					}
					p++;
				}

				if (p != 0)
					output += array3[3 - k];// 补进进制（亿万元分角）
				// 把多余的零去掉

				String[] str = { "零仟", "零佰", "零拾" };
				for (s = 0; s < 3; s++) {
					while (true) {
						q = output.indexOf(str[s]);
						if (q != -1)
							output = output.substring(0, q) + "零"
									+ output.substring(q + str[s].length());
						else
							break;
					}
				}
				while (true) {
					q = output.indexOf("零零");
					if (q != -1)
						output = output.substring(0, q) + "零"
								+ output.substring(q + 2);
					else
						break;
				}
				String[] str1 = { "零亿", "零万", "零元" };
				for (s = 0; s < 3; s++) {
					while (true) {
						q = output.indexOf(str1[s]);
						if (q != -1)
							output = output.substring(0, q)
									+ output.substring(q + 1);
						else
							break;
					}
				}
				k--;
			}

			if (pstn != -1)// 小数部分处理
			{
				for (i = 1; i < length - pstn; i++) {
					if (input.charAt(pstn + i) != '0') {
						output += formatC(input.charAt(pstn + i));
						output += array3[2 + i];
					} else if (i < 2)
						output += "零";
					else
						output += "";
				}
			}
			if (output.substring(0, 1).equals("零"))
				output = output.substring(1);
			if (output.substring(output.length() - 1, output.length()).equals(
					"零"))
				output = output.substring(0, output.length() - 1);
			return output += "整";
		}
	}

	public static String formatC(char x) {
		String a = "";
		switch (x) {
		case '0':
			a = "零";
			break;
		case '1':
			a = "壹";
			break;
		case '2':
			a = "贰";
			break;
		case '3':
			a = "叁";
			break;
		case '4':
			a = "肆";
			break;
		case '5':
			a = "伍";
			break;
		case '6':
			a = "陆";
			break;
		case '7':
			a = "柒";
			break;
		case '8':
			a = "捌";
			break;
		case '9':
			a = "玖";
			break;
		}
		return a;
	}

	public static String getNowTime() {
		return DateUtil.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	public static String getHourMinitus(String hours, String minitues) {
		String str = "";
		if (hours == null || hours.equalsIgnoreCase("")) {
			return "";
		}
		str = " " + hours;
		if (minitues != null && !minitues.trim().equalsIgnoreCase("")) {
			str += ":" + minitues;
		}
		return str;
	}

	/**
	 * 取得两个时间之间的差数（天）
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static String getTime(Date startDate, Date endDate) {
		if (startDate != null && endDate != null) {
			int date = (int) (endDate.getTime() - startDate.getTime())
					/ (1000 * 60 * 60 * 24);
			return String.valueOf(date);
		}
		return "";
	}

	/**
	 * 取得两个时间之间的差数（天）
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static String getTimeIncludeHead(Date startDate, Date endDate) {
		if (startDate != null && endDate != null) {
			int date = (int) (endDate.getTime() - startDate.getTime())
					/ (1000 * 60 * 60 * 24) + 1;
			return String.valueOf(date);
		}
		return "";
	}

	/**
	 * 对某个日期加num
	 * 
	 * @param nowTime
	 * @param dayNum
	 * @return
	 */
	public static String getTimeToPlusNum(Date nowTime, int dayNum) {
		if (nowTime == null) {
			return "";
		}
		Date time = new Date(nowTime.getYear(), nowTime.getMonth(), nowTime
				.getDate()
				+ dayNum);
		return DateUtil.date2Str(time);
	}

	public static String readByURL(String url) {

		StringBuffer sb = new StringBuffer();

		try {

			String sCurrentLine = "";
			java.io.InputStream l_urlStream;
			java.net.URL l_url = new java.net.URL(url);
			java.net.HttpURLConnection l_connection = (java.net.HttpURLConnection) l_url
					.openConnection();

			l_connection.connect();

			l_urlStream = l_connection.getInputStream();

			java.io.BufferedReader l_reader = new java.io.BufferedReader(
					new java.io.InputStreamReader(l_urlStream, "UTF8"));

			while ((sCurrentLine = l_reader.readLine()) != null) {
				sb.append(sCurrentLine).append("\r\n");
			}
			l_reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();

	}

	/**
	 * @param data
	 * @return
	 */
	public static String decodeUrlByUtf8(String data) {
		try {
			return java.net.URLDecoder.decode(data, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * encodeurl，使用Utf8编码
	 * 
	 * @param name
	 *            要编码的字符
	 * @return String 编码后的字符
	 */
	public static String encodeUrlByUtf8(String name) {
		if (name == null) {
			return "";
		}
		try {
			name = java.net.URLEncoder.encode(name, "UTF-8");
		} catch (Exception e) {
		}
		return name;
	}

	public static int parseInt(String s) {
		if (s == null) {
			return 0;
		}
		try {
			int i = Integer.parseInt(s);
			return i;
		} catch (Exception e) {
		}
		return 0;
	}



	private static String getFileName(String fileName) {
		if (fileName != null && !fileName.equalsIgnoreCase("")) {
			int index = fileName.indexOf(".");
			return fileName.substring(0, index);
		}
		return null;
	}

	/**
	 * 将helloWorld转hello_world
	 * 
	 * @param input
	 * @return
	 */
	public static String getSplitString(String input) {
		StringBuffer str = new StringBuffer();
		if (input != null) {
			for (int i = 0; i < input.length(); i++) {
				char a = input.charAt(i);
				if (i != 0 && (a >= 'A') && (a <= 'Z')) {
					a += 32;
					str.append("_");
				}
				str.append(a);
			}
		}
		return str.toString();
	}

	public static double parseDouble(String s){
		if(s == null){
			return 0.0;
		}
		try{
			double i = Double.parseDouble(s);
			return i;
		}catch(Exception e){
			
		}
		return 0.0;
	}
	
	public static void main(String[] args) {
		String url = "20100821/XXjboAUQpcUcpgXX-1553961978-130996.jpg20100821/KEuzqPw04i5riAGx1224155231-130996.jpg20100821/7L2esmdfCXW9BXKz-1710842397-130996.jpg20100821/oZz4DYsKmeYKjhUS-1583773864-130996.jpg20100821/X28IfAlLMnKvhBN0-1045702915-130996.jpg20100821/vmqmfrbHgb4MziZ61511046168-130996.jpg20100821/kRVYsbbJlMmrnslN-1612217050-130996.jpg";
		
		String[] a  =StringUtils.splitByWholeSeparator(url, ".jpg");

		System.out.println(a.toString());
		 

	}

	/**
	 * 把控字符转换成空串
	 * @param str
	 * @return
	 */
	public static String blanknull(String str){
		if(str!=null){
			return str;
		}else{
			return "";
		}
	}
	
	public static void main2(String[] args) {
		String url = "http://www.aaa.ccc.com?asd=asd&key=v2&zhong=中午你";
		String s1 = encodeUrlByUtf8("￥");
		String d1 = decodeUrlByUtf8(s1);
		String d2 = decodeUrlByUtf8(d1);
		String d3 = decodeUrlByUtf8(d2);
		String s2 = encodeUrlByUtf8(s1);
		System.out.println(s1);
		System.out.println(s2);
		System.out.println(d1);
		System.out.println(d2);
		System.out.println(d3);

	}
}
