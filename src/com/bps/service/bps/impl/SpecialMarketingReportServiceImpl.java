package com.bps.service.bps.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bps.bean.MoneyObj;
import com.bps.bean.ReportSaveObj;
import com.bps.dal.dao.bps.BpsRwHistoryDao;
import com.bps.dal.dao.bps.CrossMarketingReportDao;
import com.bps.dal.dao.bps.SpecialMarketingReportDao;
import com.bps.dal.object.bps.BpsUserInfo;
import com.bps.dal.object.bps.DefinedReportInfo;
import com.bps.dal.object.bps.SpecialMarketingReport;
import com.bps.service.bps.SpecialMarketingReportService;
import com.bps.util.DateUtil;
import com.bps.util.FileUtil;
import com.bps.util.RedisUtil;

public class SpecialMarketingReportServiceImpl implements SpecialMarketingReportService{
	private static final Logger logger = Logger.getLogger("logs");
	@Resource
	private BpsRwHistoryDao bpsRwHistoryDao;
	@Resource
	private SpecialMarketingReportDao specialMarketingReportDao;

	@Autowired
	private RedisUtil redisUtil;
	
	private Map<String, String> centerMap = null;
	private Map<String, Map<String, String>> centerGroupMap = null;
	private Map<String, String[]> rateMap = null;
	private final String headersStr = "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,外呼数据派发金额,接通量,成功受理量,成功受理金额,成功批核量,成功批核金额,3期批核量,3期批核金额,6期批核量,6期批核金额,12期批核量,12期批核金额,18期批核量,18期批核金额,24期批核量,24期批核金额,36期批核量,36期批核金额,批核收入";
	
	@Override
	public boolean createReport(String beginTime, String endTime, String saveUrl, String reportName) {
		Map<String, Set<String>> bpoCenterGroup = new HashMap<String, Set<String>>();
		String beginDate = beginTime.split(" ")[0];
		String endDate = endTime.split(" ")[0];
		String dateStr = beginDate;
		if(!beginDate.equals(endDate)){
			dateStr = beginDate + "~" + endDate;
		}
		reportName += "_"+dateStr.trim()+".xls";
		System.out.println(dateStr+"专项营销成效报表生成开始"+DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
		try{
			if(centerMap == null){
				centerMap = (Map<String, String>) redisUtil.getJedis().hgetAll(RedisUtil.BPS_CENTER);
			}
			if(centerGroupMap == null) {
				centerGroupMap = (Map<String, Map<String, String>>) RedisUtil.deserialize(redisUtil.getJedis().get(RedisUtil.BPS_GROUP.getBytes()));
			}
			if(rateMap == null) {
				rateMap = (Map<String, String[]>) RedisUtil.deserialize(redisUtil.getJedis().get(RedisUtil.BPS_RATE.getBytes()));
			}
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Map<String, List<SpecialMarketingReport>> dataMap = new HashMap<String, List<SpecialMarketingReport>>();
			paramMap.put("beginTime", beginTime);
			paramMap.put("endTime", endTime);
			paramMap.put("skipRow", -1);	//不分页查询所有数据
			//获取有数据派发的用户名List
			List<String> haveDataUser = bpsRwHistoryDao.getHaveDataUser("getHaveDataUser", paramMap);
			List<BpsUserInfo> userList = bpsRwHistoryDao.getUserInfoByTime("getUserInfoByTime", paramMap);
			List<BpsUserInfo> bpoUserList = bpsRwHistoryDao.getBPOUserInfo("getBPOUserInfo", paramMap);
			for(BpsUserInfo user : userList){
				String centerId = user.getCenterId();
				String groupId = user.getGroupId();
				
				List<SpecialMarketingReport> dataList = null;
				if(centerGroupMap.get(centerId).get(groupId) != null){	//表示有组别信息，否则只有中心信息
					dataList = dataMap.get(groupId);	//获取该小组的list
				}else{
					dataList = dataMap.get(centerId);	//没有小组信息的就获取该中心的list
				}
				if(dataList == null) dataList = new ArrayList<SpecialMarketingReport>();
				
				//判断该用户是否在haveDataUser中，如果不在则表示没有数据，则所有数据为0；
				String userName = user.getUserName();
				boolean dataFlag = false;
				for(String dataUser : haveDataUser) {
					if(dataUser.equalsIgnoreCase(userName)) {
						dataFlag = true;
						break;
					}
				}
				List<SpecialMarketingReport> tempDataList = getUserSpecialMarketingReport(user, paramMap, dataFlag);
				if(tempDataList.size() > 0){
					dataList.addAll(tempDataList);
					if(centerGroupMap.get(centerId).get(groupId) != null){	//表示有组别信息，否则只有中心信息
						dataMap.put(groupId, dataList);	//因为中心和小组信息共用一张表，所以两者的id（主键）不可能有相同的情况
					}else{	//将没有小组信息的数据保存进中心数据的List
						dataMap.put(centerId, dataList);
					}
				}
			}
			
			//bpo用户
			for(BpsUserInfo user : bpoUserList) {
				String centerId = user.getCenterId();
				String groupId = user.getGroupId();
				
				Set<String> bpoList = bpoCenterGroup.get(centerId);
				if(bpoList == null){
					bpoList = new HashSet<String>();
				}
				bpoList.add(groupId);
				bpoCenterGroup.put(centerId, bpoList);
				
				List<SpecialMarketingReport> dataList = null;
				if(centerGroupMap.get(centerId).get(groupId) != null){	//表示有组别信息，否则只有中心信息
					dataList = dataMap.get(groupId);	//获取该小组的list
				}else{
					dataList = dataMap.get(centerId);	//没有小组信息的就获取该中心的list
				}
				if(dataList == null) dataList = new ArrayList<SpecialMarketingReport>();
				
				//判断该用户是否在haveDataUser中，如果不在则表示没有数据，则所有数据为0；
				String userName = user.getUserName();
				boolean dataFlag = false;
				for(String dataUser : haveDataUser) {
					if(dataUser.equalsIgnoreCase(userName)) {
						dataFlag = true;
						break;
					}
				}
				
				List<SpecialMarketingReport> tempDataList = getUserSpecialMarketingReport(user, paramMap, dataFlag);
				if(tempDataList.size() > 0){
					dataList.addAll(tempDataList);
					if(centerGroupMap.get(centerId).get(groupId) != null){	//表示有组别信息，否则只有中心信息
						dataMap.put(groupId, dataList);	//因为中心和小组信息共用一张表，所以两者的id（主键）不可能有相同的情况
					}else{	//将没有小组信息的数据保存进中心数据的List
						dataMap.put(centerId, dataList);
					}
				}
			}
			
			SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ReportSaveObj reportSave = new ReportSaveObj("BPS-专项营销成效");	//文件保存对象
//			StringBuffer allStrBuf = new StringBuffer();	//所有的数据
			List<SpecialMarketingReport> allDataList = new ArrayList<SpecialMarketingReport>();
			FileUtil<SpecialMarketingReport> fileUtil = new FileUtil<SpecialMarketingReport>();
			String[] headers = headersStr.split(",");
			List<String> reportFields = new ArrayList<String>();
			reportFields.add("beginTime");
			reportFields.add("endTime");
			reportFields.add("center");
			reportFields.add("group");
			reportFields.add("userName");
			reportFields.add("userRealName");
			reportFields.add("ywType");
			reportFields.add("whDateNum");
			reportFields.add("whDistributeMoney");
			reportFields.add("connectNum");
			reportFields.add("successAcceptAmount");
			reportFields.add("successAcceptMoney");
			reportFields.add("successApproveAmount");
			reportFields.add("successApproveMoney");
			reportFields.add("approveAmount3");
			reportFields.add("approveMoney3");
			reportFields.add("approveAmount6");
			reportFields.add("approveMoney6");
			reportFields.add("approveAmount12");
			reportFields.add("approveMoney12");
			reportFields.add("approveAmount18");
			reportFields.add("approveMoney18");
			reportFields.add("approveAmount24");
			reportFields.add("approveMoney24");
			reportFields.add("approveAmount36");
			reportFields.add("approveMoney36");
			reportFields.add("approveIncome");
			
			long allFileNum = 0L;
			for(String centerId : centerMap.keySet()){
//				StringBuffer centerStrBuf = new StringBuffer();	//中心的数据
				List<SpecialMarketingReport> centerDataList = new ArrayList<SpecialMarketingReport>();
				long centerFileNum = 0L;
				//获取该中心下的bpo小组IDList
				Set<String> bpoList = bpoCenterGroup.get(centerId);
				for(String groupId : centerGroupMap.get(centerId).keySet()){
					//如果是bpo小组则这里不生成文件，放到下面bpo文件生成代码块统一生成
					if(bpoList != null && bpoList.contains(groupId)){
						continue;
					}
					
//					StringBuffer groupStrBuf = new StringBuffer();	//小组的数据
					long groupFileNum = 0L;
					List<SpecialMarketingReport> groupList = dataMap.get(groupId);
					if(groupList != null){
						groupFileNum += groupList.size();
						centerDataList.addAll(groupList);
						centerFileNum += groupList.size();
//						for(SpecialMarketingReport report : groupList){
//							groupStrBuf.append(report.toString()+"\r\n");
//							groupFileNum++;
//						}
//						centerStrBuf.append(groupStrBuf.toString());
//						centerFileNum += groupFileNum;
					}else{
						groupList = new ArrayList<SpecialMarketingReport>();
					}
					
//					groupStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,呼数据派发金额,接通量,成功受理量,成功受理金额,成功批核量,成功批核金额,3期批核量,3期批核金额,6期批核量,6期批核金额,12期批核量,12期批核金额,18期批核量,18期批核金额,24期批核量,24期批核金额,36期批核量,36期批核金额,批核收入\r\n");
					String groupPath = saveUrl+dateStr+File.separator+centerId.trim()+File.separator+groupId.trim()+File.separator;
					String totalPath = groupPath + reportName;
					//System.out.println(groupPath);
					File groupFile = new File(totalPath);
					File groupPathFile = groupFile.getParentFile();
					if(!groupPathFile.exists()){
						groupPathFile.mkdirs();
					}
//					FileUtil.createFile(totalPath, groupStrBuf.toString());
					fileUtil.createFile(totalPath, "BPS-专项营销成效", headers, groupList, reportFields);

					reportSave.setFileName(reportName+"&"+centerId+"_"+groupId);
					reportSave.setFileNum(groupFileNum);
					reportSave.setFilePath(groupPath);
					reportSave.setTime(format.format(new Date()));
					bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);
				}
				List<SpecialMarketingReport> centerList = dataMap.get(centerId);
				if(centerList != null){
					centerDataList.addAll(centerList);
					centerFileNum += centerList.size();
//					for(SpecialMarketingReport report : centerList){
//						centerStrBuf.append(report.toString()+"\r\n");
//						centerFileNum++;
//					}
				}
				allDataList.addAll(centerDataList);
//				allStrBuf.append(centerStrBuf.toString());
				allFileNum += centerFileNum;
//				centerStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,呼数据派发金额,接通量,成功受理量,成功受理金额,成功批核量,成功批核金额,3期批核量,3期批核金额,6期批核量,6期批核金额,12期批核量,12期批核金额,18期批核量,18期批核金额,24期批核量,24期批核金额,36期批核量,36期批核金额,批核收入\r\n");
				String centerPath = saveUrl+dateStr+File.separator+centerId.trim()+File.separator;
				String totalPath = centerPath + reportName;
				File centerFile = new File(totalPath);
				File centerPathFile = centerFile.getParentFile();
				if(!centerPathFile.exists()){
					centerPathFile.mkdirs();
				}
//				FileUtil.createFile(totalPath, centerStrBuf.toString());
				fileUtil.createFile(totalPath, "BPS-专项营销成效", headers, centerDataList, reportFields);
				
				reportSave.setFileName(reportName+"&"+centerId);
				reportSave.setFileNum(centerFileNum);
				reportSave.setFilePath(centerPath);
				reportSave.setTime(format.format(new Date()));
				bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);
			}
//			allStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,呼数据派发金额,接通量,成功受理量,成功受理金额,成功批核量,成功批核金额,3期批核量,3期批核金额,6期批核量,6期批核金额,12期批核量,12期批核金额,18期批核量,18期批核金额,24期批核量,24期批核金额,36期批核量,36期批核金额,批核收入\r\n");
			String allPath = saveUrl+dateStr+File.separator;
			String totalPath = allPath + reportName;
			File allFile = new File(totalPath);
			File allPathFile = allFile.getParentFile();
			if(!allPathFile.exists()){
				allPathFile.mkdirs();
			}
//			FileUtil.createFile(totalPath, allStrBuf.toString());
			fileUtil.createFile(totalPath, "BPS-专项营销成效", headers, allDataList, reportFields);
			
			reportSave.setFileName(reportName+"&");
			reportSave.setFileNum(allFileNum);
			reportSave.setFilePath(allPath);
			reportSave.setTime(format.format(new Date()));
			bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);
			
			//bpo文件生成
			reportName = "BPO-BPS-"+reportName;
			reportSave.setType("BPO-BPS-专项营销成效");	//文件保存对象
//			allStrBuf.setLength(0);	//所有的数据
			allDataList.clear();
			allFileNum = 0L;
			for(String centerId : bpoCenterGroup.keySet()){
//				StringBuffer centerStrBuf = new StringBuffer();	//中心的数据
				List<SpecialMarketingReport> centerDataList = new ArrayList<SpecialMarketingReport>();
				long centerFileNum = 0L;
				for(String groupId : bpoCenterGroup.get(centerId)){
//					StringBuffer groupStrBuf = new StringBuffer();	//小组的数据
					long groupFileNum = 0L;
					List<SpecialMarketingReport> groupList = dataMap.get(groupId);
					if(groupList != null){
						groupFileNum += groupList.size();
						centerDataList.addAll(groupList);
						centerFileNum += groupList.size();
//						for(SpecialMarketingReport report : groupList){
//							groupStrBuf.append(report.toString()+"\r\n");
//							groupFileNum++;
//						}
//						centerStrBuf.append(groupStrBuf.toString());
//						centerFileNum += groupFileNum;
					}else{
						groupList = new ArrayList<SpecialMarketingReport>();
					}
//					groupStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,呼数据派发金额,接通量,成功受理量,成功受理金额,成功批核量,成功批核金额,3期批核量,3期批核金额,6期批核量,6期批核金额,12期批核量,12期批核金额,18期批核量,18期批核金额,24期批核量,24期批核金额,36期批核量,36期批核金额,批核收入\r\n");
					String groupPath = saveUrl+dateStr+File.separator+centerId.trim()+File.separator+groupId.trim()+File.separator;
					String bpoTotalPath = groupPath + reportName;
					//System.out.println(groupPath);
					File groupFile = new File(bpoTotalPath);
					File groupPathFile = groupFile.getParentFile();
					if(!groupPathFile.exists()){
						groupPathFile.mkdirs();
					}
//					FileUtil.createFile(bpoTotalPath, groupStrBuf.toString());
					fileUtil.createFile(bpoTotalPath, "BPO-BPS-专项营销成效", headers, groupList, reportFields);

					reportSave.setFileName(reportName+"&"+centerId+"_"+groupId);
					reportSave.setFileNum(groupFileNum);
					reportSave.setFilePath(groupPath);
					reportSave.setTime(format.format(new Date()));
					bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);
				}
				List<SpecialMarketingReport> centerList = dataMap.get(centerId);
				if(centerList != null){
					centerDataList.addAll(centerList);
					centerFileNum += centerList.size();
//					for(SpecialMarketingReport report : centerList){
//						centerStrBuf.append(report.toString()+"\r\n");
//						centerFileNum++;
//					}
				}
				allDataList.addAll(centerDataList);
//				allStrBuf.append(centerStrBuf.toString());
				allFileNum += centerFileNum;
//				centerStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,呼数据派发金额,接通量,成功受理量,成功受理金额,成功批核量,成功批核金额,3期批核量,3期批核金额,6期批核量,6期批核金额,12期批核量,12期批核金额,18期批核量,18期批核金额,24期批核量,24期批核金额,36期批核量,36期批核金额,批核收入\r\n");
				String centerPath = saveUrl+dateStr+File.separator+centerId.trim()+File.separator;
				String bpoTotalPath = centerPath + reportName;
				File centerFile = new File(bpoTotalPath);
				File centerPathFile = centerFile.getParentFile();
				if(!centerPathFile.exists()){
					centerPathFile.mkdirs();
				}
//				FileUtil.createFile(bpoTotalPath, centerStrBuf.toString());
				fileUtil.createFile(bpoTotalPath, "BPO-BPS-专项营销成效", headers, centerDataList, reportFields);
				
				reportSave.setFileName(reportName+"&"+centerId);
				reportSave.setFileNum(centerFileNum);
				reportSave.setFilePath(centerPath);
				reportSave.setTime(format.format(new Date()));
				bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);
			}
//			allStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,呼数据派发金额,接通量,成功受理量,成功受理金额,成功批核量,成功批核金额,3期批核量,3期批核金额,6期批核量,6期批核金额,12期批核量,12期批核金额,18期批核量,18期批核金额,24期批核量,24期批核金额,36期批核量,36期批核金额,批核收入\r\n");
			allPath = saveUrl+dateStr+File.separator;
			totalPath = allPath + reportName;
			allFile = new File(totalPath);
			allPathFile = allFile.getParentFile();
			if(!allPathFile.exists()){
				allPathFile.mkdirs();
			}
//			FileUtil.createFile(totalPath, allStrBuf.toString());
			fileUtil.createFile(totalPath, "BPO-BPS-专项营销成效", headers, allDataList, reportFields);
			
			reportSave.setFileName(reportName+"&");
			reportSave.setFileNum(allFileNum);
			reportSave.setFilePath(allPath);
			reportSave.setTime(format.format(new Date()));
			bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);
			
			System.out.println(dateStr+"专项营销成效报表生成结束"+DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
		}catch (Exception e) {
			e.printStackTrace();
			logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  生成"+dateStr+"专项营销成效报表错误===>  "+e.toString());
			return false;
		}
		return true;
	}


	@Override
	public List<SpecialMarketingReport> getUserSpecialMarketingReport(BpsUserInfo user, Map<String, Object> paramMap, boolean dataFlag) {
		String[] ywTypeArr = {"EPP", "账单分期", "大额EPPC", "EPPC", "EPPC备用金"};
		List<SpecialMarketingReport> tempDataList =new ArrayList<SpecialMarketingReport>();
		String[] rate = {};
		
		SpecialMarketingReport parentRepor = new SpecialMarketingReport(user);
		
		String centerId = user.getCenterId();
		String groupId = user.getGroupId();
		parentRepor.setCenter(centerMap.get(centerId));
		parentRepor.setGroup(centerGroupMap.get(centerId).get(groupId));
		
		parentRepor.setBeginTime((String)paramMap.get("beginTime"));
		parentRepor.setEndTime((String)paramMap.get("endTime"));
		
		for (String ywType : ywTypeArr){
			SpecialMarketingReport report = (SpecialMarketingReport) parentRepor.clone();
			report.setYwType(ywType);
			//如果该用户不存在数据派发,不进行数据库查询直接赋默认值
			if(!dataFlag){
				report.setApproveAmount3(0L);
				report.setApproveAmount6(0L);
				report.setApproveAmount12(0L);
				report.setApproveAmount18(0L);
				report.setApproveAmount24(0L);
				report.setApproveAmount36(0L);
				report.setApproveIncome(0.0);
				report.setApproveMoney3(0.0);
				report.setApproveMoney6(0.0);
				report.setApproveMoney12(0.0);
				report.setApproveMoney18(0.0);
				report.setApproveMoney24(0.0);
				report.setApproveMoney36(0.0);
				report.setConnectNum(0L);
				report.setSuccessAcceptAmount(0L);
				report.setSuccessAcceptMoney(0.0);
				report.setSuccessApproveAmount(0L);
				report.setSuccessApproveMoney(0.0);
				report.setWhDateNum(0L);
				report.setWhDistributeMoney(0.0);
			}else{
				String userName = user.getUserName();
				paramMap.put("userName", userName);
				paramMap.put("ywType", ywType);
				
				//派发金额
				if("EPP".equals(ywType) || "账单分期".equals(ywType)){
					double money = 0.0;
					List<MoneyObj> moneyObjList = specialMarketingReportDao.getWhDistributeMoney1("getWhDistributeMoney1", paramMap);
					for (MoneyObj obj : moneyObjList) {
						money += obj.getMoney();
					}
					report.setWhDistributeMoney(money);
				}else{
					double moneyA = 0.0;
					double moneyB = 0.0;
					List<MoneyObj> moneyObjListA = specialMarketingReportDao.getWhDistributeMoney2A("getWhDistributeMoney2A", paramMap);
					for (MoneyObj obj : moneyObjListA) {
						moneyA += obj.getMoney();
					}
					List<MoneyObj> moneyObjListB = specialMarketingReportDao.getWhDistributeMoney2B("getWhDistributeMoney2B", paramMap);
					for (MoneyObj obj : moneyObjListB) {
						moneyB += obj.getMoney();
					}
					report.setWhDistributeMoney(moneyA + moneyB);
				}
				
				if("EPP".equals(ywType)){
					rate = rateMap.get("EPP");
				}else if("账单分期".equals(ywType)){
					rate = rateMap.get("BILL");
				}else if("大额EPPC".equals(ywType)){
					rate = rateMap.get("BIGEPPC");
				}else if("EPPC".equals(ywType)){
					rate = rateMap.get("EPPC");
				}else if("EPPC备用金".equals(ywType)){
					rate = rateMap.get("EPPCCash");
				}
				
				report.setWhDateNum(bpsRwHistoryDao.getWhDateNum("getWhDateNum", paramMap));
				report.setConnectNum(specialMarketingReportDao.getConnectNum("getConnectNum", paramMap));
				//SuccessAcceptAmount和SuccessAcceptMoney和下面的分期指标的计算公用一个SQL但不需要期数所以给period赋个none
				paramMap.put("period", "none");
				report.setSuccessAcceptAmount(specialMarketingReportDao.getSuccessAcceptAmount("getSuccessAcceptAmount", paramMap));
				report.setSuccessAcceptMoney(specialMarketingReportDao.getSuccessAcceptMoney("getSuccessAcceptMoney", paramMap));
				long numA = specialMarketingReportDao.getSuccessApproveAmountA("getSuccessApproveAmountA", paramMap);
				long numB = specialMarketingReportDao.getSuccessApproveAmountB("getSuccessApproveAmountB", paramMap);
				report.setSuccessApproveAmount(numA + numB);
				double moneyA = specialMarketingReportDao.getSuccessApproveMoneyA("getSuccessApproveMoneyA", paramMap);
				double moneyB = specialMarketingReportDao.getSuccessApproveMoneyB("getSuccessApproveMoneyB", paramMap);
				report.setSuccessApproveMoney(moneyA + moneyB);
				paramMap.put("period", "3期");
				numA = specialMarketingReportDao.getSuccessApproveAmountA("getSuccessApproveAmountA", paramMap);
				numB = specialMarketingReportDao.getSuccessApproveAmountB("getSuccessApproveAmountB", paramMap);
				report.setApproveAmount3(numA + numB);
				moneyA = specialMarketingReportDao.getSuccessApproveMoneyA("getSuccessApproveMoneyA", paramMap);
				moneyB = specialMarketingReportDao.getSuccessApproveMoneyB("getSuccessApproveMoneyB", paramMap);
				report.setApproveMoney3(moneyA + moneyB);
				paramMap.put("period", "6期");
				numA = specialMarketingReportDao.getSuccessApproveAmountA("getSuccessApproveAmountA", paramMap);
				numB = specialMarketingReportDao.getSuccessApproveAmountB("getSuccessApproveAmountB", paramMap);
				report.setApproveAmount6(numA + numB);
				moneyA = specialMarketingReportDao.getSuccessApproveMoneyA("getSuccessApproveMoneyA", paramMap);
				moneyB = specialMarketingReportDao.getSuccessApproveMoneyB("getSuccessApproveMoneyB", paramMap);
				report.setApproveMoney6(moneyA + moneyB);
				paramMap.put("period", "12期");
				numA = specialMarketingReportDao.getSuccessApproveAmountA("getSuccessApproveAmountA", paramMap);
				numB = specialMarketingReportDao.getSuccessApproveAmountB("getSuccessApproveAmountB", paramMap);
				report.setApproveAmount12(numA + numB);
				moneyA = specialMarketingReportDao.getSuccessApproveMoneyA("getSuccessApproveMoneyA", paramMap);
				moneyB = specialMarketingReportDao.getSuccessApproveMoneyB("getSuccessApproveMoneyB", paramMap);
				report.setApproveMoney12(moneyA + moneyB);
				paramMap.put("period", "18期");
				numA = specialMarketingReportDao.getSuccessApproveAmountA("getSuccessApproveAmountA", paramMap);
				numB = specialMarketingReportDao.getSuccessApproveAmountB("getSuccessApproveAmountB", paramMap);
				report.setApproveAmount18(numA + numB);
				moneyA = specialMarketingReportDao.getSuccessApproveMoneyA("getSuccessApproveMoneyA", paramMap);
				moneyB = specialMarketingReportDao.getSuccessApproveMoneyB("getSuccessApproveMoneyB", paramMap);
				report.setApproveMoney18(moneyA + moneyB);
				paramMap.put("period", "24期");
				numA = specialMarketingReportDao.getSuccessApproveAmountA("getSuccessApproveAmountA", paramMap);
				numB = specialMarketingReportDao.getSuccessApproveAmountB("getSuccessApproveAmountB", paramMap);
				report.setApproveAmount24(numA + numB);
				moneyA = specialMarketingReportDao.getSuccessApproveMoneyA("getSuccessApproveMoneyA", paramMap);
				moneyB = specialMarketingReportDao.getSuccessApproveMoneyB("getSuccessApproveMoneyB", paramMap);
				report.setApproveMoney24(moneyA + moneyB);
				paramMap.put("period", "36期");
				numA = specialMarketingReportDao.getSuccessApproveAmountA("getSuccessApproveAmountA", paramMap);
				numB = specialMarketingReportDao.getSuccessApproveAmountB("getSuccessApproveAmountB", paramMap);
				report.setApproveAmount36(numA + numB);
				moneyA = specialMarketingReportDao.getSuccessApproveMoneyA("getSuccessApproveMoneyA", paramMap);
				moneyB = specialMarketingReportDao.getSuccessApproveMoneyB("getSuccessApproveMoneyB", paramMap);
				report.setApproveMoney36(moneyA + moneyB);
				
				double approveIncome = 0.00;
				if(rate.length > 5){
					approveIncome += report.getApproveMoney3()*3*Double.parseDouble(rate[0])/100;
					approveIncome += report.getApproveMoney6()*6*Double.parseDouble(rate[1])/100;
					approveIncome += report.getApproveMoney12()*12*Double.parseDouble(rate[2])/100;
					approveIncome += report.getApproveMoney18()*18*Double.parseDouble(rate[3])/100;
					approveIncome += report.getApproveMoney24()*24*Double.parseDouble(rate[4])/100;
					approveIncome += report.getApproveMoney36()*36*Double.parseDouble(rate[5])/100;
				}
				report.setApproveIncome(approveIncome);
			}
			
			tempDataList.add(report);
		}
		
		return tempDataList;
	}


	@Override
	public boolean createDefinedReport(DefinedReportInfo reportInfo, String saveUrl) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(centerMap == null){
			centerMap = (Map<String, String>) redisUtil.getJedis().hgetAll(RedisUtil.BPS_CENTER);
		}
		if(centerGroupMap == null) {
			centerGroupMap = (Map<String, Map<String, String>>) RedisUtil.deserialize(redisUtil.getJedis().get(RedisUtil.BPS_GROUP.getBytes()));
		}
		paramMap.put("beginTime", reportInfo.getStartTime());
		paramMap.put("endTime", reportInfo.getEndTime());
		paramMap.put("skipRow", -1);	//不分页查询所有数据
		String centerText = reportInfo.getZhongxin();
		//设置生成的报表名称 例如：BPS-新数据派发及成效_20180605 09:00~20180608 12:00_广四中心_一组.xls
		String beginTime = reportInfo.getStartTime().replaceAll("-", "").substring(0, 14);
		String endTime = reportInfo.getEndTime().replaceAll("-", "").substring(0, 14);
		beginTime = beginTime.replaceAll(" ", "_");
		endTime = endTime.replaceAll(" ", "_");
		beginTime = beginTime.replaceAll(":", "");
		endTime = endTime.replaceAll(":", "");
		String prefix = "BPS-专项营销成效_"+beginTime+"~"+endTime;
		String realPreFix = "BPS-SpecialMarketingReport_"+beginTime+"~"+endTime;
		StringBuffer reportName = new StringBuffer(prefix);
		StringBuffer realReportName = new StringBuffer(realPreFix);
		if(centerText != null && !"".equals(centerText)){
			reportName.append("_"+centerText);
			for(Map.Entry<String, String> entry : centerMap.entrySet()){
				if(centerText.equals(entry.getValue())){
					realReportName.append("_"+entry.getKey());
					paramMap.put("center", entry.getKey());
					break;
				}
			}
			String groupText = reportInfo.getXiaozu();
			if(groupText != null && !"".equals(groupText)){
				reportName.append("_"+groupText);
				for(Map.Entry<String, String> entry : centerGroupMap.get(paramMap.get("center")).entrySet()){
					if(groupText.equals(entry.getValue())){
						realReportName.append("_"+entry.getKey());
						paramMap.put("group", entry.getKey());
						break;
					}
				}
			}
		}
		reportName.append(".xls");
		reportInfo.setFilezuName(reportName.toString());
		realReportName.append(".xls");
		reportInfo.setFilezuRealname(realReportName.toString());
		try{
			//获取有数据派发的用户名List
			List<String> haveDataUser = bpsRwHistoryDao.getHaveDataUser("getHaveDataUser", paramMap);
			List<BpsUserInfo> userList = bpsRwHistoryDao.getUserInfoByTime("getUserInfoByTime", paramMap);
			List<SpecialMarketingReport> dataList = new ArrayList<SpecialMarketingReport>();
			List<SpecialMarketingReport> bpoDataList = new ArrayList<SpecialMarketingReport>();
			List<BpsUserInfo> bpoUserList = bpsRwHistoryDao.getBPOUserInfo("getBPOUserInfo", paramMap);
			for(BpsUserInfo user : userList){
				//判断该用户是否在haveDataUser中，如果不在则表示没有数据，则所有数据为0；
				String userName = user.getUserName();
				boolean dataFlag = false;
				for(String dataUser : haveDataUser) {
					if(dataUser.equalsIgnoreCase(userName)) {
						dataFlag = true;
						break;
					}
				}
				List<SpecialMarketingReport> tempDataList = getUserSpecialMarketingReport(user, paramMap, dataFlag);
				
				dataList.addAll(tempDataList);
			}
			
			//bpo营销员
			for(BpsUserInfo user : userList){
				//判断该用户是否在haveDataUser中，如果不在则表示没有数据，则所有数据为0；
				String userName = user.getUserName();
				boolean dataFlag = false;
				for(String dataUser : haveDataUser) {
					if(dataUser.equalsIgnoreCase(userName)) {
						dataFlag = true;
						break;
					}
				}
				List<SpecialMarketingReport> tempDataList = getUserSpecialMarketingReport(user, paramMap, dataFlag);
				
				bpoDataList.addAll(tempDataList);
			}
			
			SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			StringBuffer dataStrBuf = new StringBuffer();
//			long fileNum = 0;
//			dataStrBuf.append("开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,呼数据派发金额,接通量,成功受理量,成功受理金额,成功批核量,成功批核金额,3期批核量,3期批核金额,6期批核量,6期批核金额,12期批核量,12期批核金额,18期批核量,18期批核金额,24期批核量,24期批核金额,36期批核量,36期批核金额,批核收入\r\n");
//			for (SpecialMarketingReport report : dataList){
//				dataStrBuf.append(report.toString()+"\r\n");
//				fileNum++;
//			}
			FileUtil<SpecialMarketingReport> fileUtil = new FileUtil<SpecialMarketingReport>();
			String[] headers = headersStr.split(",");
			List<String> reportFields = new ArrayList<String>();
			reportFields.add("beginTime");
			reportFields.add("endTime");
			reportFields.add("center");
			reportFields.add("group");
			reportFields.add("userName");
			reportFields.add("userRealName");
			reportFields.add("ywType");
			reportFields.add("whDateNum");
			reportFields.add("whDistributeMoney");
			reportFields.add("connectNum");
			reportFields.add("successAcceptAmount");
			reportFields.add("successAcceptMoney");
			reportFields.add("successApproveAmount");
			reportFields.add("successApproveMoney");
			reportFields.add("approveAmount3");
			reportFields.add("approveMoney3");
			reportFields.add("approveAmount6");
			reportFields.add("approveMoney6");
			reportFields.add("approveAmount12");
			reportFields.add("approveMoney12");
			reportFields.add("approveAmount18");
			reportFields.add("approveMoney18");
			reportFields.add("approveAmount24");
			reportFields.add("approveMoney24");
			reportFields.add("approveAmount36");
			reportFields.add("approveMoney36");
			reportFields.add("approveIncome");
			
			
			String path = saveUrl+realReportName;
			File file = new File(path);
			File pathFile = file.getParentFile();
			if(!pathFile.exists()){
				pathFile.mkdirs();
			}
//			FileUtil.createFile(path, dataStrBuf.toString());
			fileUtil.createFile(path, "BPS-专项营销成效", headers, dataList, reportFields);
			
			String bpoPath = saveUrl+"BPO-"+realReportName;
			File bpFfile = new File(bpoPath);
			File bpoPathFile = bpFfile.getParentFile();
			if(!bpoPathFile.exists()){
				bpoPathFile.mkdirs();
			}
			fileUtil.createFile(bpoPath, "BPO-BPS-专项营销成效", headers, bpoDataList, reportFields);
			
			//是否生成营销员报表
			if("是".equals(reportInfo.getYxyData())){
				paramMap.put("userName", reportInfo.getAssignName());
				String specificReportName = prefix + "_" +reportInfo.getAssignName() + ".xls";
				reportInfo.setFileyxyName(specificReportName);
				String realSpecificReportName = realPreFix + "_" +reportInfo.getAssignName() + ".xls";
				reportInfo.setFileyxyRealname(realSpecificReportName);
				List<BpsUserInfo> specificUserList = bpsRwHistoryDao.getUserInfoByTime("getUserInfoByTime", paramMap);
				
				List<SpecialMarketingReport> specificDataList = new ArrayList<SpecialMarketingReport>();
				for(BpsUserInfo user : specificUserList){
					//判断该用户是否在haveDataUser中，如果不在则表示没有数据，则所有数据为0；
					String userName = user.getUserName();
					boolean dataFlag = false;
					for(String dataUser : haveDataUser) {
						if(dataUser.equalsIgnoreCase(userName)) {
							dataFlag = true;
							break;
						}
					}
					List<SpecialMarketingReport> tempDataList = getUserSpecialMarketingReport(user, paramMap, dataFlag);
					specificDataList.addAll(tempDataList);
				}
				
//				StringBuffer specificDataStrBuf = new StringBuffer();
//				long specificFileNum = 0;
//				specificDataStrBuf.append("开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,呼数据派发金额,接通量,成功受理量,成功受理金额,成功批核量,成功批核金额,3期批核量,3期批核金额,6期批核量,6期批核金额,12期批核量,12期批核金额,18期批核量,18期批核金额,24期批核量,24期批核金额,36期批核量,36期批核金额,批核收入\r\n");
//				for (SpecialMarketingReport report : specificDataList){
//					specificDataStrBuf.append(report.toString()+"\r\n");
//					specificFileNum++;
//				}
				
				
				String specificPath = saveUrl+realSpecificReportName;
				File specificFile = new File(specificPath);
				File specificPathFile = specificFile.getParentFile();
				if(!specificPathFile.exists()){
					specificPathFile.mkdirs();
				}
//				FileUtil.createFile(specificPath, specificDataStrBuf.toString());
				fileUtil.createFile(specificPath, "BPS-专项营销成效", headers, specificDataList, reportFields);
			}
			
			reportInfo.setZhixingTime(format.format(new Date()));
			reportInfo.setState("已执行");
			bpsRwHistoryDao.updateDefinedReportInfo("updateDefinedReportInfo", reportInfo);
		}catch (Exception e) {
			e.printStackTrace();
			logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  生成"+reportInfo.getStartTime()+"~"+reportInfo.getEndTime()+"自定义专项营销成效报表错误===>  "+e.toString());
			return false;
		}
		return true;
	}

}
