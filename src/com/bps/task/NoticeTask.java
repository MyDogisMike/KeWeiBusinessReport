package com.bps.task;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bps.dal.dao.bps.BpsRwHistoryDao;
import com.bps.dal.object.bps.DefinedReportInfo;
import com.bps.service.bps.CrossMarketingReportService;
import com.bps.service.bps.NewDataDistributeReportService;
import com.bps.service.bps.SpecialMarketingReportService;
import com.bps.service.bps.SuperscriptReportService;
import com.bps.service.bps.TelephoneReportService;
import com.bps.util.DateUtil;
import com.bps.util.RedisUtil;

public class NoticeTask {
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
		System.out.println("日报进来了");
		FileInputStream in = null;
		FileInputStream rateIn = null;
		Properties rateProperties = new Properties();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		beginTime = date + " 00:00:00";
		endTime = date + " 23:59:59";
		try {
			Map<String, String[]> rateMap = new HashMap<String, String[]>();
			String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			//System.out.println("path======>"+path);
			in = new FileInputStream(path+"ReportSaveUrl.properties");
			properties.load(new InputStreamReader(in, "utf-8"));
			rateIn = new FileInputStream(path+"rate.properties");
			rateProperties.load(new InputStreamReader(rateIn, "utf-8"));
			for(Object key : rateProperties.keySet()){
				String value = (String) rateProperties.get(key);
				String[] rate = value.split(",");
				rateMap.put((String)key, rate);
			}
			redisUtil.getJedis().set(RedisUtil.BPS_RATE.getBytes(), RedisUtil.serialize(rateMap));
		} catch (Exception e) {
			System.out.println("读取properties文件出错");
			e.printStackTrace();
			logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  读取properties文件出错===>  "+e.toString());
		}finally{
			if(in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		Map<String, String> centerMap = bpsRwHistoryDao.getAllCenter("getAllCenter");
		
		Map<String, Map<String, String>> centerGroupMap = new HashMap<String, Map<String, String>>();
		for(String centerKey : centerMap.keySet()){
			Map<String, String> groupMap = bpsRwHistoryDao.getGroupByCenterId("getGroupByCenterId", centerKey);
			centerGroupMap.put(centerKey, groupMap);
		}
		
		redisUtil.getJedis().del(RedisUtil.BPS_CENTER);
		redisUtil.getJedis().del(RedisUtil.BPS_GROUP.getBytes());
		
		redisUtil.getJedis().hmset(RedisUtil.BPS_CENTER, centerMap);
		redisUtil.getJedis().set(RedisUtil.BPS_GROUP.getBytes(), RedisUtil.serialize(centerGroupMap));

		centerMap = (Map<String, String>) redisUtil.getJedis().hgetAll(RedisUtil.BPS_CENTER);
		centerGroupMap = (Map<String, Map<String, String>>) RedisUtil.deserialize(redisUtil.getJedis().get(RedisUtil.BPS_GROUP.getBytes()));
		
		runReckon();
		System.out.println("日报结束了");
	}
	
	public void runReckon(){
		ExecutorService executor = Executors.newCachedThreadPool();
		Worker telephoneReport = new Worker("telephoneReport");
		Worker superscriptReport = new Worker("superscriptReport");
		Worker crossMarketingReport = new Worker("crossMarketingReport");
		Worker newDataDistributeReport = new Worker("newDataDistributeReport");
		Worker specialMarketingReport = new Worker("specialMarketingReport");
		Worker definedReport = new Worker("definedReport");
		executor.execute(telephoneReport); 
		executor.execute(superscriptReport);
		executor.execute(crossMarketingReport);
		executor.execute(newDataDistributeReport);
		executor.execute(specialMarketingReport);
		executor.execute(definedReport);
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
			}else if(this.name.equals("definedReport")){
				this.createDefinedReport();
			}
		}
		
		private void createTelephoneReport(){
			try {
				
				telephoneReportService.createReport(beginTime, endTime, properties.getProperty("telephoneDayUrl"), "话务日报");
			} catch (Exception e) {
				System.out.println("生成"+beginTime+"~"+endTime+"的话务报表错误");
				e.printStackTrace();
				logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  生成"+beginTime+"~"+endTime+"的话务报表错误===>  "+e.toString());
			}
		}
		
		private void createSuperscriptReport(){
			try {
				superscriptService.createReport(beginTime, endTime, properties.getProperty("superscriptDayUrl"), "每日上标及跟进库存日报");
			} catch (Exception e) {
				System.out.println("生成"+beginTime+"~"+endTime+"的每日上标及跟进库存报表错误");
				e.printStackTrace();
				logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  生成"+beginTime+"~"+endTime+"的每日上标及跟进库存报表错误===>  "+e.toString());
			}
		}
		
		private void createCrossMarketingReport(){
			try {
				crossMarketingReportService.createReport(beginTime, endTime, properties.getProperty("crossMarketingDayUrl"), "交叉营销成效日报");
			} catch (Exception e) {
				System.out.println("生成"+beginTime+"~"+endTime+"的交叉营销成效报表错误");
				e.printStackTrace();
				logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  生成"+beginTime+"~"+endTime+"的交叉营销成效报表报表错误===>  "+e.toString());
			}
		}
		
		private void createNewDataDistributeReport(){
			try {
				newDataDistributeReportService.createReport(beginTime, endTime, properties.getProperty("newDataDistributeDayUrl"), "新数据派发及成效日报");
			} catch (Exception e) {
				System.out.println("生成"+beginTime+"~"+endTime+"的新数据派发及成效报表错误");
				e.printStackTrace();
				logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  生成"+beginTime+"~"+endTime+"的新数据派发及成效报表错误===>  "+e.toString());
			}
		}
		
		private void createSpecialMarketingReport(){
			try {
				specialMarketingReportService.createReport(beginTime, endTime, properties.getProperty("specialMarketingDayUrl"), "专项营销成效日报");
			} catch (Exception e) {
				System.out.println("生成"+beginTime+"~"+endTime+"的专项营销成效报表错误");
				e.printStackTrace();
				logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  生成"+beginTime+"~"+endTime+"的专项营销成效报表错误===>  "+e.toString());
			}
		}
		
		private void createDefinedReport(){
			try {
				List<DefinedReportInfo> reportList = bpsRwHistoryDao.getDefinedReportInfo("getDefinedReportInfo", null);
				if(reportList != null){
					for (DefinedReportInfo reportInfo : reportList){
						if("BPS-话务报表".equals(reportInfo.getBaobiaoName())){
							telephoneReportService.createDefinedReport(reportInfo, properties.getProperty("definedReportURL"));
						}else if("BPS-每日上标及跟进库存".equals(reportInfo.getBaobiaoName())){
							superscriptService.createDefinedReport(reportInfo, properties.getProperty("definedReportURL"));
						}else if("BPS-交叉营销成效".equals(reportInfo.getBaobiaoName())){
							crossMarketingReportService.createDefinedReport(reportInfo, properties.getProperty("definedReportURL"));
						}else if("BPS-专项营销成效".equals(reportInfo.getBaobiaoName())){
							specialMarketingReportService.createDefinedReport(reportInfo, properties.getProperty("definedReportURL"));
						}else if("BPS-新数据派发及成效".equals(reportInfo.getBaobiaoName())){
							newDataDistributeReportService.createDefinedReport(reportInfo, properties.getProperty("definedReportURL"));
						}
					}
				}
			} catch (Exception e) {
				System.out.println("生成自定义区间报表错误");
				e.printStackTrace();
				logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  生成自定义区间报表错误===>  "+e.toString());
			}
		}
	}

}
