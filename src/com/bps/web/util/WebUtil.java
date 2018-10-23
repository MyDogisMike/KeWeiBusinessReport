package com.bps.web.util;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.bps.util.CommonUtil;
import com.bps.util.JsonUtil;

/**
 * freemarker 静态方法库 可以直接在freemark中调用 date 2012-4-21 Company taobao
 * 
 * @author Figo
 * @version 1.0
 * 
 */
public class WebUtil implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6072642862504899262L;

	/**
	 * 获取当前时间
	 */
	public static String getNowDate() {
		return CommonUtil.getNowDateString();
	}

	public static String ajaxOut(Object obj) {
		String str = "";
		try {
			str = JsonUtil.object2json(obj);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	//当天0点
	public static String getDateBegin0Points(Date time){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar begindate = Calendar.getInstance(); 
		begindate.setTime(time);
		begindate.set(Calendar.HOUR_OF_DAY, 0); 
		begindate.set(Calendar.SECOND, 0); 
		begindate.set(Calendar.MINUTE, 0); 
		begindate.set(Calendar.MILLISECOND, 0); 
		
		return dateFormat.format(begindate.getTime());
	}
	//当天中午12点
		public static String getDateafternoon12Points(Date time){
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar enddate = Calendar.getInstance(); 
			enddate.setTime(time);
			enddate.set(Calendar.HOUR_OF_DAY, 12); 
			enddate.set(Calendar.SECOND, 0); 
			enddate.set(Calendar.MINUTE, 0); 
			enddate.set(Calendar.MILLISECOND, 0); 
			
			return dateFormat.format(enddate.getTime());
		}
	//当天24点
	public static String getDateEnd24Points(Date time){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar enddate = Calendar.getInstance(); 
		enddate.setTime(time);
		enddate.set(Calendar.HOUR_OF_DAY, 24); 
		enddate.set(Calendar.SECOND, 0); 
		enddate.set(Calendar.MINUTE, 0); 
		enddate.set(Calendar.MILLISECOND, 0); 
		
		return dateFormat.format(enddate.getTime());
	}
	
	//当月第一天0点
		public static String getMonthBegin0Points(Date time){
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(time);
			cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0,0);
			cal.set(Calendar.DAY_OF_MONTH,cal.getActualMinimum(Calendar.DAY_OF_MONTH)); 
			return dateFormat.format(cal.getTime());
		}
		//当月最后一天24点
		public static String getMonthEnd24Points(Date time){
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(time);
			cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0,0);
			 cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH)); 
			cal.set(Calendar.HOUR_OF_DAY, 24); 
			return dateFormat.format(cal.getTime());
		}
		//days天前0点
		public static String getDaysAgoBegin0Points(Date time,Integer days){
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(time);
			cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0,0);
			cal.set(Calendar.DATE,cal.get(Calendar.DATE)-days);  
			return dateFormat.format(cal.getTime());
		}

}
