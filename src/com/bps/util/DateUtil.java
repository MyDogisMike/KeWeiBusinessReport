package com.bps.util;

import java.rmi.activation.ActivationException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.DataFormatException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * andalee  2017/3/31
 */
public final class DateUtil {
	private static Log log = LogFactory.getLog(DateUtil.class);

	private static final SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

	private DateUtil() {
	}
	
	
	/**
	 * andalee 20170416  计算两个时间相差多少秒
	 * 获取今天开始时间
	 * @return
	 */
	public static Long dateDiffSecond(String endtime,String begintime){
		Date beginTimeDate=DateUtil.str2Date(begintime, "yyyy-MM-dd HH:mm:ss");
		Date endTimeDate=DateUtil.str2Date(endtime, "yyyy-MM-dd HH:mm:ss");
		Calendar beginTimecalendar = Calendar.getInstance();
		beginTimecalendar.setTime(beginTimeDate);
		Calendar endTimecalendar = Calendar.getInstance();
		endTimecalendar.setTime(endTimeDate);
		Long seconds=(endTimecalendar.getTimeInMillis()-beginTimecalendar.getTimeInMillis())/1000;
		return seconds;
	}
	
	/**
	 * 获取今天开始时间
	 * @return
	 */
	public static Date getTodayStartTime(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		calendar.set(Calendar.MINUTE, 0);  
		calendar.set(Calendar.SECOND, 0);  
		Date start = calendar.getTime();
		return start;
	}
	
	
	/**
	 * 获取本周第一天时间
	 * @return
	 */
	public static Date getFirstDayCurrWeek(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		calendar.set(Calendar.MINUTE, 0);  
		calendar.set(Calendar.SECOND, 0);  
		Date start = calendar.getTime();
		return start;
	}
	
	/**
	 * 获取指定时间当月的第一天
	 * @return
	 */
	public static Date getFirstDayCurrMonth(String nowdate){
		Date tempdate=null;
		try {
			tempdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(nowdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(tempdate);
		//本月第一天
		calendar.set(Calendar.DATE, 1); 
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		calendar.set(Calendar.MINUTE, 0);  
		calendar.set(Calendar.SECOND, 0);  
		Date start = calendar.getTime();
		return start;
	}
	
	/**
	 * 获取指定时间当月的最后一天
	 * @return
	 */
	public static Date getLastDayCurrMonth(String nowdate){
		Date tempdate=null;
		try {
			tempdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(nowdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(tempdate);
		 //本月最后一天
        calendar.add(Calendar.MONTH, 1);    //加一个月
        calendar.set(Calendar.DATE, 1);        //设置为该月第一天
        calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
        Date start = calendar.getTime();
        /*
        String day_last = df.format(calendar.getTime());
        StringBuffer endStr = new StringBuffer().append(day_last).append(" 23:59:59");
        day_last = endStr.toString();
        */
		return start;
	}
	
	/**
	 * 获取今天剩余秒速
	 * @return
	 */
	public static Long getTodayRemainSeconds(){
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+1); 
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		calendar.set(Calendar.MINUTE, 0);  
		calendar.set(Calendar.SECOND, 0);
		
		//Date nextday = calendar.getTime();
		Long seconds=calendar.getTimeInMillis()-System.currentTimeMillis();
		return seconds/1000;
	}
	
	

	/**
	 * Date->String
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateToStr(Date date, String pattern) {
		SimpleDateFormat formater2 = new SimpleDateFormat(pattern);
		if(date==null){
			return "";
		}
		return formater2.format(date);
	}
	
	/**
	 * 获取当前时间并转换成String
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getNowDate(String pattern) {
		Date date = new Date();
		SimpleDateFormat formater2 = new SimpleDateFormat(pattern);
		if(date==null){
			return "";
		}
		return formater2.format(date);
	}

	/**
	 * 获取指定日期的前后日期 i为正数 向后推迟i天，负数时向前提前i天
	 * @Project IBReport
	 * @param specifiedDay  指定日期
	 * @param Formatparameters 返回的时间格式
	 * @param i
	 * @return
	 * @author hejin
	 * @date 2017-11-2 下午03:58:52 
	 * @version V 1.0
	 */
	public static String getBeForeNDate(String specifiedDay,String Formatparameters,int i) 
	 {
		Calendar c = Calendar.getInstance(); 
		Date date=null; 
		try { 
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay); 
		} catch (ParseException e) { 
		e.printStackTrace(); 
		} 
		c.setTime(date); 
		int day=c.get(Calendar.DATE); 
		c.set(Calendar.DATE,day+i); 

		String dayBefore=new SimpleDateFormat(Formatparameters).format(c.getTime()); 
		return dayBefore;
	 }
	
	/**
	 * 获取指定日期到前后日期 的所有日期；i为正数 向后推迟i天，负数时向前提前i天
	 * @Project IBReport
	 * @param specifiedDay  指定日期
	 * @param Formatparameters 返回的时间格式
	 * @param i
	 * @return String[]
	 * @author hejin
	 * @date 2017-11-2 下午03:58:52 
	 * @version V 1.0
	 */
	public static String[] getBeForeNDateStr(String specifiedDay,String Formatparameters,int i) 
	 {
		int sum=Math.abs(i);
		String[] dateStr=new String[sum]; 
		Calendar c = Calendar.getInstance(); 
		Date date=null; 
		try { 
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay); 
		} catch (ParseException e) { 
		e.printStackTrace(); 
		} 
		c.setTime(date); 
		int day=c.get(Calendar.DATE);
		for (int j = 0; j < sum; j++) {
			c.set(Calendar.DATE,day+j); 
			String dayBefore=new SimpleDateFormat(Formatparameters).format(c.getTime()); 
			dateStr[j]=dayBefore;
		}
		
		return dateStr;
	 }
	
	/**
	 * 获取当前时间的前一天并转换成String
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getNextDay(String pattern) { 
		Date date = new Date();
		SimpleDateFormat formater2 = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -1);  
        date = calendar.getTime();  
        return formater2.format(date);  
    }
	
	
	/**
	 * convert string to date ,with custom pattern,throws
	 * IllegalArgumentException
	 * 
	 * @param str
	 * @param format
	 * @return
	 */
	public static Date str2Date(String str, String format) {
		SimpleDateFormat formater = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = formater.parse(str);
		} catch (ParseException e) {
			//
		}
		return date;
	}

	/**
	 * convert string to date, with default pattern,throws ParseException
	 * 
	 * @param str
	 * @return
	 */
	public static Date str2Date(String str) {
		Date date = null;
		try {
			date = formater.parse(str);
		} catch (ParseException e) {
			//
		}
		return date;
	}

	/**
	 * convert date to string,with custom pattern,throws
	 * IllegalArgumentException
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String date2Str(Date date, String format) {
		SimpleDateFormat formater = new SimpleDateFormat(format);
		if(date!=null){
			return formater.format(date);
		}
		else{
			return "";
		}
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String date2Str(Date date) {
		if(date!=null){
			return formater.format(date);
		}
		else{
			return "";
		}
			
	}

	/**
	 * 由日期,小时,分,钟组合成一个 yyyy-MM-dd hh:mm:ss 格式的 Date object
	 * 
	 * @param date
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @return
	 */
	public static Date str2Date(String date, String hours, String minutes, String seconds) {
		String secondsNew = seconds;
		if (null == date || "".equals(date) || null == hours || "".equals(hours) || null == minutes
				|| "".equals(minutes)) {
			return null;
		}
		if (null == secondsNew) {
			secondsNew = "00";
		}
		Date retDate = null;
		StringBuffer dateBuffer = new StringBuffer(date);
		dateBuffer.append(" ").append(hours).append(":").append(minutes).append(":").append(secondsNew);
		retDate = str2Date(dateBuffer.toString(), "yyyy-MM-dd HH:mm:ss");
		return retDate;
	}


	/**
	 * String->Date
	 * 
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static Date strToDate(String str, String pattern) {
		Date dateTemp = null;
		SimpleDateFormat formater2 = new SimpleDateFormat(pattern);
		try {
			dateTemp = formater2.parse(str);
		} catch (Exception e) {
			log.error("exception in convert string to date!");
		}
		return dateTemp;
	}

	/**
	 * 
	 * @author andalee 2017/3/31
	 * @param dateStr
	 * @return
	 */
	public static Date getStartMonth(String dateStr) {
		return strToDate(dateStr, "yyyy-MM");
	}

	/**
	 * 
	 * @author andalee 2017/3/31
	 * @param dateStr
	 * @return
	 */
	public static Date getEndMonth(String dateStr) {
		Date d = strToDate(dateStr, "yyyy-MM");
		Calendar date = Calendar.getInstance();
		date.setTime(d);
		date.add(Calendar.MONTH, 1);
		return new Date(date.getTimeInMillis());
	}

	/**
	 * 
	 * @author andalee 2017/3/31
	 * @param dateStr
	 * @return
	 */
	public static String getBeforeMonth(String dateStr) {
		Date d = strToDate(dateStr, "yyyy-MM");
		Calendar date = Calendar.getInstance();
		date.setTime(d);
		date.add(Calendar.MONTH, -1);
		return dateToStr(new Date(date.getTimeInMillis()), "yyyy-MM");
	}

	/**
	 * 
	 * @author andalee 2017/3/31
	 * @param intYear
	 * @param intMonth
	 * @return
	 */
	public static String getYearMonth(int intYear, int intMonth) {
		String str = "" + intYear + "-" + intMonth;
		return str;
	}

	/**
	 * 
	 * @author andalee 2017/3/31
	 * @param intYear
	 * @param intMonth
	 * @param intDay
	 * @return
	 */
	public static String getStringDate(int intYear, int intMonth, int intDay) {
		String str = "" + intYear + "-" + intMonth + "-" + intDay;
		return str;
	}

	public static String getStringDate(String formart) {
		SimpleDateFormat df = new SimpleDateFormat(formart);// 日期格式
		String dateFormart = df.format(new Date());
		return dateFormart;
	}

	
	/**
	 * 
	 * @author andalee  2017/3/31
	 * @param intYear
	 * @param intMonth
	 * @param intDay
	 * @return
	 */
	public static String getStandardStringDate(int intYear, int intMonth, int intDay) {
		String str = intToStandardString(intYear) + "-" + intToStandardString(intMonth) + "-"
				+ intToStandardString(intDay);
		return str;
	}

	/**
	 * input 2 output "02"
	 * 
	 * @author andalee 2017/3/31
	 * @param intNum
	 * @return
	 */
	public static String intToStandardString(int intNum) {
		if (intNum >= 0 && intNum < 10) {
			return "0" + intNum;
		} else {
			return "" + intNum;
		}
	}


	/**
	 * 将星期的数字转换为汉语
	 * 
	 * @author andalee
	 * @param intWeek
	 * @return
	 */
	public static String convWeek(int intWeek) {
		String strWeek = null;
		switch (intWeek) {
		case 0:
			strWeek = "日";
			break;
		case 1:
			strWeek = "一";
			break;
		case 2:
			strWeek = "二";
			break;
		case 3:
			strWeek = "三";
			break;
		case 4:
			strWeek = "四";
			break;
		case 5:
			strWeek = "五";
			break;
		case 6:
			strWeek = "六";
			break;
		default:
			break;
		}

		return strWeek;
	}

	/**
	 * @author andalee
	 * @return Return a timestamp object
	 */
	@SuppressWarnings("deprecation")
	public static Timestamp getATimeStamp() {
		Calendar date = Calendar.getInstance();
		date.add(Calendar.YEAR, 10);
		return new Timestamp(date.getTimeInMillis());
	}

	/**
	 * @author andalee
	 * @param startTime
	 * @param duringTime
	 * @return Return a Date object
	 */
	public static Date getEndTime(Date startTime, Integer duringTime) {
		// duringTime :以分钟为单位
		Calendar date = Calendar.getInstance();
		date.setTime(startTime);
		date.add(Calendar.MINUTE, duringTime);
		return date.getTime();
	}

	/**
	 * 
	 * @param date
	 * @param formaterString
	 * @return date
	 */
	public static Date formatDate(Date date, String formaterString) {

		SimpleDateFormat sFormater = new SimpleDateFormat(formaterString);
		String tempDate = sFormater.format(date);
		Date result = date;// 转换失败 不做改变
		try {
			result = sFormater.parse(tempDate);
		} catch (ParseException e) {
			throw new RuntimeException("字符串转换成日期出错", e);
		}

		return result;

	}
	
	public static Timestamp date2DateTime(Date date){
		Timestamp time = new Timestamp(date.getTime());
		return time;
	}
	
	public static long DateDiff(Date nowdate,Date inputdate) throws DataFormatException, ActivationException {
		long nowTime;
		long diffTime;
		long days = 0;
		try { 
			nowTime = (nowdate.getTime() / 1000);
			diffTime = (inputdate.getTime() / 1000);
		    if (nowTime > diffTime) {
		        days = (nowTime - diffTime) / (1 * 60 * 60 * 24);
		    } else {
		        days = (diffTime - nowTime) / (1 * 60 * 60 * 24);
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return days;
		
	}
	
	
	/*
	 * 当天日期增加或减少几天
	 */
	public static Date DateAdd(Date date,int datediff){
		Calendar calendar=Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.add(Calendar.DATE, datediff);      
	    Date times=calendar.getTime();
	    return times;
	}
	
	/**
	 * 2017/3/31
	 * 注释：type：年份，月份，日期，  向前或者向后推几个单位
	 * andalee
	 */
	public static String dateAdd(String type,int diff){
		Calendar calendar=Calendar.getInstance();
		if("year".equalsIgnoreCase(type)){
			calendar.add(Calendar.YEAR, diff);      
		}else if("month".equalsIgnoreCase(type)){
			calendar.add(Calendar.MONTH, diff);      
		}else if("date".equalsIgnoreCase(type)){
			calendar.add(Calendar.DATE, diff);      
		}
		Date date = calendar.getTime();
	    return date2Str(date);
	}
	
	/**
	 * @date 2017/3/31
	 * 向前推一个月
	 * lvjq
	 */
	public static String lastMonth() {
		Date date = new Date();
		int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
		int month = Integer.parseInt(new SimpleDateFormat("MM").format(date)) - 1;
		int day = Integer.parseInt(new SimpleDateFormat("dd").format(date));

		if (month == 0) {
			year -= 1;
			month = 12;
		} else if (day > 28) {
			if (month == 2) {
				if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
					day = 29;
				} else
					day = 28;
			} else if ((month == 4 || month == 6 || month == 9 || month == 11)
					&& day == 31) {
				day = 30;
			}
		}
		String y = year + "";
		String m = "";
		String d = "";
		if (month < 10)
			m = "0" + month;
		else
			m = month + "";
		if (day < 10)
			d = "0" + day;
		else
			d = day + "";
		return y + "-" + m + "-" + d;
	}
	
	  public static String getFormatDateTime(Date dateValue, String strFormat)
	  {
	    SimpleDateFormat format = new SimpleDateFormat(strFormat);
	    //format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));//初始化当前时区解决去到的时间比当前时间少8小时
	    return format.format(dateValue);
	  }
	
	  public static String getFormatDateTime(String strDateValue, String strFormat1, String strFormat2)
	    throws ParseException
	  {
	    SimpleDateFormat sdfFormat = new SimpleDateFormat(strFormat1);
	    Date dateValue = sdfFormat.parse(strDateValue);
	    return getFormatDateTime(dateValue, strFormat2);
	  }
	  
	  /**
	   *  某一月份有多少工作日
	   * @Project IBReport
	   * @param year
	   * @param month
	   * @return
	   * @author hejin
	   * @date 2018-3-13 下午04:14:24 
	   * @version V 1.0
	   */
	  public static List<Date> getDates(int year,int month){    
	        List<Date> dates = new ArrayList<Date>();    
	        Calendar cal = Calendar.getInstance();    
	        cal.set(Calendar.YEAR, year);    
	        cal.set(Calendar.MONTH,  month - 1);    
	        cal.set(Calendar.DATE, 1);    
	            
	            
	        while(cal.get(Calendar.YEAR) == year &&     
	                cal.get(Calendar.MONTH) < month){    
	            int day = cal.get(Calendar.DAY_OF_WEEK);    
	                
	            if(!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)){    
	                dates.add((Date)cal.getTime().clone());    
	            }    
	            cal.add(Calendar.DATE, 1);    
	        }    
	        return dates;    
	    
	    }    

	  /*
	   * 指定年月有多少工作日
	   */
	  public static int getDutyDays(String strStartDate,String strEndDate) {  
		  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
		  Date startDate=null;  
		  Date endDate = null;  
		    
		  try {  
			  startDate=df.parse(strStartDate);  
			  endDate = df.parse(strEndDate);  
		  } catch (ParseException e) {  
			  System.out.println("非法的日期格式,无法进行转换");  
			  e.printStackTrace();  
		  }  
		  int result = 0;  
		  while (startDate.compareTo(endDate) <= 0) {  
		  if (startDate.getDay() != 6 && startDate.getDay() != 0)  
			  result++;  
			  startDate.setDate(startDate.getDate() + 1);  
		  }  
		    
		  return result;  
		  }  
	  
	  /** 
	   * 得到几天前的时间 
	   * @param d 
	   * @param day 
	   * @return 
	   */  
	  public static String getDateBefore(Date d,int day,String pattern){  
	   Calendar now =Calendar.getInstance();  
	   now.setTime(d);  
	   now.set(Calendar.DATE,now.get(Calendar.DATE)-day);
	   SimpleDateFormat formater2 = new SimpleDateFormat(pattern);
	   Date date = now.getTime();
	   if(date==null){
			return "";
		}
		return formater2.format(date);
	  }  
	    
	  /** 
	   * 得到几天后的时间 
	   * @param d 
	   * @param day 
	   * @return 
	   */  
	  public static String getDateAfter(Date d,int day,String pattern){  
	   Calendar now =Calendar.getInstance();  
	   now.setTime(d);  
	   now.set(Calendar.DATE,now.get(Calendar.DATE)+day); 
	   SimpleDateFormat formater2 = new SimpleDateFormat(pattern);
	   Date date = now.getTime();
	   if(date==null){
			return "";
		}
		return formater2.format(date); 
	  } 
	  
	public static void main(String[] args) throws Exception {
		String[] dateStr=new String[5];
		for (int i = 0; i < dateStr.length; i++) {
			String day=getDateBefore(str2Date("2018-02-27"),+i, "MM月dd日");
			dateStr[i]=day;
		}
		for (int i = 0; i < dateStr.length; i++) {
			System.out.println(dateStr[i]);
		}
	}
}
