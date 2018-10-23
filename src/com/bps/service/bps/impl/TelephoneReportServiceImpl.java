package com.bps.service.bps.impl;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bps.bean.ReportSaveObj;
import com.bps.dal.dao.bps.BpsRwHistoryDao;
import com.bps.dal.dao.bps.TelephoneReportDao;
import com.bps.dal.dao.hw.TNottollUniversalDao;
import com.bps.dal.object.bps.BpsUserInfo;
import com.bps.dal.object.bps.DefinedReportInfo;
import com.bps.dal.object.bps.TelephoneReport;
import com.bps.exception.BaseException;
import com.bps.service.bps.TelephoneReportService;
import com.bps.util.DateUtil;
import com.bps.util.FileUtil;
import com.bps.util.RedisUtil;

public class TelephoneReportServiceImpl implements TelephoneReportService{
	private static final Logger logger = Logger.getLogger(TelephoneReportServiceImpl.class);
	@Resource
	private BpsRwHistoryDao bpsRwHistoryDao;
	@Resource
	private TelephoneReportDao telephoneReportDao;
	@Resource
	private TNottollUniversalDao tNottollUniversalDao;
	@Autowired
	private RedisUtil redisUtil;
	
	private Map<String, String> centerMap = null;
	private Map<String, Map<String, String>> centerGroupMap = null;

	@Override
	public boolean createReport(String beginTime, String endTime, String saveUrl, String reportName) throws BaseException{
		String beginDate = beginTime.split(" ")[0];
		String endDate = endTime.split(" ")[0];
		String dateStr = beginDate;
		if(!beginDate.equals(endDate)){
			dateStr = beginDate + "~" + endDate;
		}
		reportName += "_"+dateStr.trim()+".txt";
		System.out.println(dateStr+"话务报表生成开始"+DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
		try{
			if(centerMap == null){
				centerMap = (Map<String, String>) redisUtil.getJedis().hgetAll(RedisUtil.BPS_CENTER);
				centerGroupMap = (Map<String, Map<String, String>>) RedisUtil.deserialize(redisUtil.getJedis().get(RedisUtil.BPS_GROUP.getBytes()));
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Map<String, List<TelephoneReport>> dataMap = new HashMap<String, List<TelephoneReport>>();
			paramMap.put("beginTime", beginTime);
			paramMap.put("endTime", endTime);
			paramMap.put("skipRow", -1);	//不分页查询所有数据
			//获取有数据派发的用户名List
			List<String> haveDataUser = bpsRwHistoryDao.getHaveDataUser("getHaveDataUser", paramMap);
			List<BpsUserInfo> userList = bpsRwHistoryDao.getUserInfoByTime("getUserInfoByTime", paramMap);
			for(BpsUserInfo user : userList){
				
				String centerId = user.getCenterId();
				String groupId = user.getGroupId();
				
				List<TelephoneReport> dataList = null;
				if(centerGroupMap.get(centerId).get(groupId) != null){	//表示有组别信息，否则只有中心信息
					dataList = dataMap.get(groupId);	//获取该小组的list
				}else{
					dataList = dataMap.get(centerId);	//没有小组信息的就获取该中心的list
				}
				if(dataList == null) dataList = new ArrayList<TelephoneReport>();
				
				//判断该用户是否在haveDataUser中，如果不在则表示没有数据，则所有数据为0；
				boolean dataFlag = haveDataUser.contains(user.getUserName());
				List<TelephoneReport> tempDataList = getUserTelephoneReport(user, paramMap, dataFlag);
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
			ReportSaveObj reportSave = new ReportSaveObj("BPS-话务报表");	//文件保存对象
			StringBuffer allStrBuf = new StringBuffer();	//所有的数据
			long allFileNum = 0L;
			for(String centerId : centerMap.keySet()){
				StringBuffer centerStrBuf = new StringBuffer();	//中心的数据
				long centerFileNum = 0L;
				for(String groupId : centerGroupMap.get(centerId).keySet()){
					StringBuffer groupStrBuf = new StringBuffer();	//小组的数据
					long groupFileNum = 0L;
					List<TelephoneReport> groupList = dataMap.get(groupId);
					if(groupList != null){
						for(TelephoneReport report : groupList){
							//System.out.println(report.toString());
							groupStrBuf.append(report.toString()+"\r\n");
							groupFileNum++;
						}
						centerStrBuf.append(groupStrBuf.toString());
						centerFileNum += groupFileNum;
					}
					groupStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,成功受理客户量,总外呼次数,总接通次数,未接通呼叫总时长,接通通话总时长,成功数据通话总时长\r\n");
					String groupPath = saveUrl+dateStr+File.separator+centerId.trim()+File.separator+groupId.trim()+File.separator;
					String totalPath = groupPath + reportName;
					//System.out.println(groupPath);
					File groupFile = new File(totalPath);
					File groupPathFile = groupFile.getParentFile();
					if(!groupPathFile.exists()){
						groupPathFile.mkdirs();
					}
					FileUtil.createFile(totalPath, groupStrBuf.toString());
					
					reportSave.setFileName(reportName+"&"+centerId+"_"+groupId);
					reportSave.setFileNum(groupFileNum);
					reportSave.setFilePath(groupPath);
					reportSave.setTime(format.format(new Date()));
					bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);

				}
				List<TelephoneReport> centerList = dataMap.get(centerId);
				if(centerList != null){
					for(TelephoneReport report : centerList){
						//System.out.println(report.toString());
						centerStrBuf.append(report.toString()+"\r\n");
						centerFileNum++;
					}
				}
				allStrBuf.append(centerStrBuf.toString());
				allFileNum += centerFileNum;
				centerStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,成功受理客户量,总外呼次数,总接通次数,未接通呼叫总时长,接通通话总时长,成功数据通话总时长\r\n");
				String centerPath = saveUrl+dateStr+File.separator+centerId.trim()+File.separator;
				String totalPath = centerPath + reportName;
				//System.out.println(centerPath);
				File centerFile = new File(totalPath);
				File centerPathFile = centerFile.getParentFile();
				if(!centerPathFile.exists()){
					centerPathFile.mkdirs();
				}
				FileUtil.createFile(totalPath, centerStrBuf.toString());
				
				reportSave.setFileName(reportName+"&"+centerId);
				reportSave.setFileNum(centerFileNum);
				reportSave.setFilePath(centerPath);
				reportSave.setTime(format.format(new Date()));
				bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);
			}
			allStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,成功受理客户量,总外呼次数,总接通次数,未接通呼叫总时长,接通通话总时长,成功数据通话总时长\r\n");
			String allPath = saveUrl+dateStr+File.separator;
			String totalPath = allPath + reportName;
			//System.out.println(allPath);
			File allFile = new File(totalPath);
			File allPathFile = allFile.getParentFile();
			if(!allPathFile.exists()){
				allPathFile.mkdirs();
			}
			FileUtil.createFile(totalPath, allStrBuf.toString());
			
			reportSave.setFileName(reportName+"&");
			reportSave.setFileNum(allFileNum);
			reportSave.setFilePath(allPath);
			reportSave.setTime(format.format(new Date()));
			bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);
			
			System.out.println(dateStr+"话务报表生成结束"+DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
		}catch (BaseException e) {
			e.printStackTrace();
			logger.warn(e.toString());
			return false;
		}
		return true;
	}

	@Override
	public List<TelephoneReport> getUserTelephoneReport(BpsUserInfo user, Map<String, Object> paramMap, boolean dataFlag) {
		String[] ywTypeArr = {"EPP", "账单分期", "大额EPPC", "EPPC", "EPPC备用金"};
		List<TelephoneReport> tempDataList = new ArrayList<TelephoneReport>();
		
		TelephoneReport parentRepor = new TelephoneReport(user);
		
		String centerId = user.getCenterId();
		String groupId = user.getGroupId();
		parentRepor.setCenter(centerMap.get(centerId));
		parentRepor.setGroup(centerGroupMap.get(centerId).get(groupId));
		
		parentRepor.setBeginTime((String)paramMap.get("beginTime"));
		parentRepor.setEndTime((String)paramMap.get("endTime"));
		
		
		for (String ywType : ywTypeArr){
			
			TelephoneReport report = (TelephoneReport) parentRepor.clone();
			report.setYwType(ywType);
			
			//如果该用户不存在数据派发,不进行数据库查询直接赋默认值
			if(!dataFlag){
				report.setAcceptDateNum(0L);
				report.setTotalCalls(0L);
				report.setTotalCallsTime(0.0);
				report.setTotalMissCallsTime(0.0);
				report.setTotalSuccessCallsTime(0.0);
				report.setWhDateNum(0L);
			}else{
				String userName = user.getUserName();
				paramMap.put("userName", userName);
				paramMap.put("ywType", ywType);
				
				if("EPP".equals(ywType)){
					paramMap.put("ywTypeId", "1");
				}else if("账单分期".equals(ywType)){
					paramMap.put("ywTypeId", "10");
				}else if("大额EPPC".equals(ywType)){
					paramMap.put("ywTypeId", "11");
				}else if("EPPC".equals(ywType)){
					paramMap.put("ywTypeId", "2");
				}else if("EPPC备用金".equals(ywType)){
					paramMap.put("ywTypeId", "2");
				}
				
				report.setWhDateNum(bpsRwHistoryDao.getWhDateNum("getWhDateNum", paramMap));
				report.setAcceptDateNum(telephoneReportDao.getAcceptDateNum("getAcceptDateNum", paramMap));
				report.setTotalCalls(telephoneReportDao.getTotalCalls("getTotalCalls", paramMap));
				List<String> userRecordList = telephoneReportDao.getRecordId("getRecordId", paramMap);
				if(userRecordList.size() < 1){	//如果没有录音id，则时长设为0
					report.setTotalMissCallsTime(0.00);
					report.setTotalCallsTime(0.00);
				}else{
					paramMap.put("recordList", userRecordList);
					Double totalMissCallsTime = tNottollUniversalDao.getTotalCallsTime("getTotalCallsTime", paramMap);
					if(totalMissCallsTime == null){
						report.setTotalMissCallsTime(0.00);
						report.setTotalCallsTime(0.00);
					}else{
						report.setTotalMissCallsTime(totalMissCallsTime);
						//话后满意度时长
						Double satisfactionTime = telephoneReportDao.getSatisfactionTime("getSatisfactionTime", paramMap);
						if(satisfactionTime == null) satisfactionTime = 0.00;
						report.setTotalCallsTime(totalMissCallsTime - satisfactionTime);
					}
				}
				List<String> dataList = telephoneReportDao.getDataList("getDataList", paramMap);
				if(dataList.size() < 1){	//如果没有工单id，则成功数据通话总时长设为0
					report.setTotalSuccessCallsTime(0.00);
				}else{
					paramMap.put("dataList", dataList);
					List<String> workOrderRecord = telephoneReportDao.getRecordIdByWorkOrder("getRecordIdByWorkOrder", paramMap);
					if(workOrderRecord.size() < 1){
						report.setTotalSuccessCallsTime(0.00);
					}else{
						paramMap.put("recordList", workOrderRecord);
						Double totalCallsTime = tNottollUniversalDao.getTotalCallsTime("getTotalCallsTime", paramMap);
						if(totalCallsTime == null){
							report.setTotalSuccessCallsTime(0.00);
						}else{
							Double workOrderSatisfactionTime = telephoneReportDao.getSatisfactionTimeByWorkOrder("getSatisfactionTimeByWorkOrder", paramMap);
							report.setTotalSuccessCallsTime(totalCallsTime - workOrderSatisfactionTime);
						}
					}
				}
			}
			
			
			tempDataList.add(report);
			
		}//业务类型for循环结束
		
		return tempDataList;
	}

	@Override
	public boolean createDefinedReport(DefinedReportInfo reportInfo, String saveUrl) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(centerMap == null){
			centerMap = (Map<String, String>) redisUtil.getJedis().hgetAll(RedisUtil.BPS_CENTER);
			centerGroupMap = (Map<String, Map<String, String>>) RedisUtil.deserialize(redisUtil.getJedis().get(RedisUtil.BPS_GROUP.getBytes()));
		}
		paramMap.put("beginTime", reportInfo.getStartTime());
		paramMap.put("endTime", reportInfo.getEndTime());
		String centerText = reportInfo.getZhongxin();
		//设置生成的报表名称 例如：BPS-新数据派发及成效_20180605 09:00~20180608 12:00_广四中心_一组.txt
		String beginTime = reportInfo.getStartTime().replaceAll("-", "").substring(0, 14);
		String endTime = reportInfo.getEndTime().replaceAll("-", "").substring(0, 14);
		beginTime = beginTime.replaceAll(" ", "_");
		endTime = endTime.replaceAll(" ", "_");
		beginTime = beginTime.replaceAll(":", "");
		endTime = endTime.replaceAll(":", "");
		String prefix = "BPS-话务报表_"+beginTime+"~"+endTime;
		String realPreFix = "BPS-TelephoneReport_"+beginTime+"~"+endTime;
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
		reportName.append(".txt");
		reportInfo.setFilezuName(reportName.toString());
		realReportName.append(".txt");
		reportInfo.setFilezuRealname(realReportName.toString());
		try{
			//获取有数据派发的用户名List
			List<String> haveDataUser = bpsRwHistoryDao.getHaveDataUser("getHaveDataUser", paramMap);
			List<BpsUserInfo> userList = bpsRwHistoryDao.getUserInfoByTime("getUserInfoByTime", paramMap);
			List<TelephoneReport> dataList = new ArrayList<TelephoneReport>();
			for(BpsUserInfo user : userList){
				//判断该用户是否在haveDataUser中，如果不在则表示没有数据，则所有数据为0；
				boolean dataFlag = haveDataUser.contains(user.getUserName());
				List<TelephoneReport> tempDataList = getUserTelephoneReport(user, paramMap, dataFlag);
				dataList.addAll(tempDataList);
			}
			
			SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			StringBuffer dataStrBuf = new StringBuffer();
			long fileNum = 0;
			dataStrBuf.append("开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,成功受理客户量,总外呼次数,总接通次数,未接通呼叫总时长,接通通话总时长,成功数据通话总时长\r\n");
			for (TelephoneReport report : dataList){
				dataStrBuf.append(report.toString()+"\r\n");
				fileNum++;
			}
			
			
			String path = saveUrl+realReportName;
			File file = new File(path);
			File pathFile = file.getParentFile();
			if(!pathFile.exists()){
				pathFile.mkdirs();
			}
			FileUtil.createFile(path, dataStrBuf.toString());
			
			//是否生成营销员报表
			if("是".equals(reportInfo.getYxyData())){
				paramMap.put("userName", reportInfo.getAssignName());
				String specificReportName = prefix + "_" +reportInfo.getAssignName() + ".txt";
				reportInfo.setFileyxyName(specificReportName);
				String realSpecificReportName = realPreFix + "_" +reportInfo.getAssignName() + ".txt";
				reportInfo.setFileyxyRealname(realSpecificReportName);
				List<BpsUserInfo> specificUserList = bpsRwHistoryDao.getUserInfoByTime("getUserInfoByTime", paramMap);
				
				List<TelephoneReport> specificDataList = new ArrayList<TelephoneReport>();
				for(BpsUserInfo user : specificUserList){
					//判断该用户是否在haveDataUser中，如果不在则表示没有数据，则所有数据为0；
					boolean dataFlag = haveDataUser.contains(user.getUserName());
					List<TelephoneReport> tempDataList = getUserTelephoneReport(user, paramMap, dataFlag);
					specificDataList.addAll(tempDataList);
				}
				
				StringBuffer specificDataStrBuf = new StringBuffer();
				long specificFileNum = 0;
				specificDataStrBuf.append("开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,成功受理客户量,总外呼次数,总接通次数,未接通呼叫总时长,接通通话总时长,成功数据通话总时长\r\n");
				for (TelephoneReport report : specificDataList){
					specificDataStrBuf.append(report.toString()+"\r\n");
					specificFileNum++;
				}
				
				
				String specificPath = saveUrl+realSpecificReportName;
				File specificFile = new File(specificPath);
				File specificPathFile = specificFile.getParentFile();
				if(!specificPathFile.exists()){
					specificPathFile.mkdirs();
				}
				FileUtil.createFile(specificPath, specificDataStrBuf.toString());
				
			}
			
			reportInfo.setZhixingTime(format.format(new Date()));
			reportInfo.setState("已执行");
			System.out.println(reportInfo.toString());
			bpsRwHistoryDao.updateDefinedReportInfo("updateDefinedReportInfo", reportInfo);
		}catch (BaseException e) {
			e.printStackTrace();
			logger.warn(e.toString());
			return false;
		}
		return true;
	}
	
}
