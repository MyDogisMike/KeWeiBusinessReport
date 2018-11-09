package com.bps.task;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bps.dal.dao.bps.BpsRwHistoryDao;
import com.bps.service.bps.CrossMarketingReportService;
import com.bps.service.bps.NewDataDistributeReportService;
import com.bps.service.bps.SpecialMarketingReportService;
import com.bps.service.bps.SuperscriptReportService;
import com.bps.service.bps.TelephoneReportService;
import com.bps.task.NoticeTask.Worker;
import com.bps.util.DateUtil;
import com.bps.util.RedisUtil;

public class WeekNoticeTask {
	@Resource
	private BpsRwHistoryDao bpsRwHistoryDao;
	@Autowired
	private RedisUtil redisUtil;
	@Resource
	private TelephoneReportService telephoneReportService;
	@Resource
	private SuperscriptReportService superscriptService;
	@Resource
	private CrossMarketingReportService crossMarketingReportService;
	@Resource
	private NewDataDistributeReportService newDataDistributeReportService;
	@Resource
	private SpecialMarketingReportService specialMarketingReportService;
	private static final Logger logger = Logger.getLogger("logs");
	
	private Properties properties = new Properties();
	private String beginTime = null;
	private String endTime = null;
	
	public void run(){
		System.out.println("周报进来了");
		FileInputStream in = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;  
		int offset = 1 - dayOfWeek;  
		cal.add(Calendar.DATE, offset - 7);  
		beginTime = format.format(cal.getTime())+" 00:00:00";
		
		cal.add(Calendar.DATE, 6);
		endTime = format.format(cal.getTime())+" 23:59:59";
		try {
			String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			//System.out.println("path======>"+path);
			in = new FileInputStream(path+"ReportSaveUrl.properties");
			properties.load(new InputStreamReader(in, "utf-8"));
		} catch (Exception e) {
			System.out.println("读取ReportSaveUrl.properties文件出错");
			e.printStackTrace();
			logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  读取ReportSaveUrl.properties文件出错===>  "+e.toString());
		}finally{
			if(in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
//		Map<String, String> centerMap = bpsRwHistoryDao.getAllCenter("getAllCenter");
//		
//		Map<String, Map<String, String>> centerGroupMap = new HashMap<String, Map<String, String>>();
//		for(String centerKey : centerMap.keySet()){
//			Map<String, String> groupMap = bpsRwHistoryDao.getGroupByCenterId("getGroupByCenterId", centerKey);
//			centerGroupMap.put(centerKey, groupMap);
//		}
//		redisUtil.getJedis().hmset(RedisUtil.BPS_CENTER, centerMap);
//		redisUtil.getJedis().set(RedisUtil.BPS_GROUP.getBytes(), RedisUtil.serialize(centerGroupMap));

		runReckon();
		System.out.println("周报结束了");
	}
	
	public void runReckon(){
		ExecutorService executor = Executors.newCachedThreadPool();
		Worker telephoneReport = new Worker("telephoneReport");
		Worker superscriptReport = new Worker("superscriptReport");
		Worker crossMarketingReport = new Worker("crossMarketingReport");
		Worker newDataDistributeReport = new Worker("newDataDistributeReport");
		Worker specialMarketingReport = new Worker("specialMarketingReport");
		executor.execute(telephoneReport);
		executor.execute(superscriptReport);
		executor.execute(crossMarketingReport);
		executor.execute(newDataDistributeReport);
		executor.execute(specialMarketingReport);
		executor.shutdown();
	}
	
	class Worker implements Runnable {
		private String name;
		public Worker(String name){
			this.name = name;
		}
		@Override
		public void run()
		{
			if(this.name.equals("telephoneReport")){
				this.createTelephoneReport();
			}else if(this.name.equals("superscriptReport")){
				this.createSuperscriptReport();
			}else if(this.name.equals("crossMarketingReport")){
				this.createCrossMarketingReport();
			}else if(this.name.equals("newDataDistributeReport")){
				this.createNewDataDistributeReport();
			}else if(this.name.equals("specialMarketingReport")){
				this.createSpecialMarketingReport();
			}
		}
		
		private void createTelephoneReport(){
			try {
				telephoneReportService.createReport(beginTime, endTime, properties.getProperty("telephoneWeekUrl"), "话务周报");
			} catch (Exception e) {
				System.out.println("生成"+beginTime+"~"+endTime+"的话务报表错误");
				e.printStackTrace();
				logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  生成"+beginTime+"~"+endTime+"的话务报表错误===>  "+e.toString());
			}
		}
		
		private void createSuperscriptReport(){
			try {
				superscriptService.createReport(beginTime, endTime, properties.getProperty("superscriptWeekUrl"), "每日上标及跟进库存周报");
			} catch (Exception e) {
				System.out.println("生成"+beginTime+"~"+endTime+"的上标及跟进库存报表错误");
				e.printStackTrace();
				logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  生成"+beginTime+"~"+endTime+"的每日上标及跟进库存报表错误===>  "+e.toString());
			}
		}
		
		private void createCrossMarketingReport(){
			try {
				crossMarketingReportService.createReport(beginTime, endTime, properties.getProperty("crossMarketingWeekUrl"), "交叉营销成效周报");
			} catch (Exception e) {
				System.out.println("生成"+beginTime+"~"+endTime+"的交叉营销成效报表错误");
				e.printStackTrace();
				logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  生成"+beginTime+"~"+endTime+"的交叉营销成效报表报表错误===>  "+e.toString());
			}
		}
		private void createNewDataDistributeReport(){
			try {
				newDataDistributeReportService.createReport(beginTime, endTime, properties.getProperty("newDataDistributeWeekUrl"), "新数据派发及成效周报");
			} catch (Exception e) {
				System.out.println("生成"+beginTime+"~"+endTime+"的新数据派发及成效报表错误");
				e.printStackTrace();
				logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  生成"+beginTime+"~"+endTime+"的新数据派发及成效报表错误===>  "+e.toString());
			}
		}
		
		private void createSpecialMarketingReport(){
			try {
				specialMarketingReportService.createReport(beginTime, endTime, properties.getProperty("specialMarketingWeekUrl"), "专项营销成效周报");
			} catch (Exception e) {
				System.out.println("生成"+beginTime+"~"+endTime+"的专项营销成效报表错误");
				e.printStackTrace();
				logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  生成"+beginTime+"~"+endTime+"的专项营销成效报表错误===>  "+e.toString());
			}
		}
	}

}
