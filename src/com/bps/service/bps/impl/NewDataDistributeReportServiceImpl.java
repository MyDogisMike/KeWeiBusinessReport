package com.bps.service.bps.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bps.bean.ReportSaveObj;
import com.bps.dal.dao.bps.BpsRwHistoryDao;
import com.bps.dal.dao.bps.NewDataDistributeReportDao;
import com.bps.dal.object.bps.BpsUserInfo;
import com.bps.dal.object.bps.DefinedReportInfo;
import com.bps.dal.object.bps.NewDataDistributeReport;
import com.bps.exception.BaseException;
import com.bps.service.bps.NewDataDistributeReportService;
import com.bps.util.DateUtil;
import com.bps.util.FileUtil;
import com.bps.util.RedisUtil;

public class NewDataDistributeReportServiceImpl implements NewDataDistributeReportService{
	private static final Logger logger = Logger.getLogger("logs");
	@Resource
	private BpsRwHistoryDao bpsRwHistoryDao;
	@Resource
	private NewDataDistributeReportDao newDataDistributeReportDao;
	@Autowired
	private RedisUtil redisUtil;
	
	private Map<String, String> centerMap = null;
	private Map<String, Map<String, String>> centerGroupMap = null;
	private final String headersStr = "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,数据类别,新数派发量,新数派发金额,新数外呼量,新数接通量,新数成功受理量,新数成功受理金额,新数成功批核量,新数成功批核金额,错误数据量（M标）";
	
	@Override
	public boolean createReport(String beginTime, String endTime, String saveUrl, String reportName) {
		String beginDate = beginTime.split(" ")[0];
		String endDate = endTime.split(" ")[0];
		String dateStr = beginDate;
		if(!beginDate.equals(endDate)){
			dateStr = beginDate + "~" + endDate;
		}
		reportName += "_"+dateStr.trim()+".xls";
		System.out.println(dateStr+"新数据派发及成效报表生成开始"+DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
		try{
			if(centerMap == null){
				centerMap = (Map<String, String>) redisUtil.getJedis().hgetAll(RedisUtil.BPS_CENTER);
			}
			if(centerGroupMap == null) {
				centerGroupMap = (Map<String, Map<String, String>>) RedisUtil.deserialize(redisUtil.getJedis().get(RedisUtil.BPS_GROUP.getBytes()));
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Map<String, List<NewDataDistributeReport>> dataMap = new HashMap<String, List<NewDataDistributeReport>>();
			paramMap.put("beginTime", beginTime);
			paramMap.put("endTime", endTime);
			paramMap.put("skipRow", -1);	//不分页查询所有数据
			//获取有数据派发的用户名List
			List<String> haveDataUser = bpsRwHistoryDao.getHaveDataUser("getHaveDataUser", paramMap);
			List<BpsUserInfo> userList = bpsRwHistoryDao.getUserInfoByTime("getUserInfoByTime", paramMap);
			for(BpsUserInfo user : userList){
				String centerId = user.getCenterId();
				String groupId = user.getGroupId();
				
				List<NewDataDistributeReport> dataList = null;
				if(centerGroupMap.get(centerId).get(groupId) != null){	//表示有组别信息，否则只有中心信息
					dataList = dataMap.get(groupId);	//获取该小组的list
				}else{
					dataList = dataMap.get(centerId);	//没有小组信息的就获取该中心的list
				}
				if(dataList == null) dataList = new ArrayList<NewDataDistributeReport>();
				
				//判断该用户是否在haveDataUser中，如果不在则表示没有数据，则所有数据为0；
				String userName = user.getUserName();
				boolean dataFlag = false;
				for(String dataUser : haveDataUser) {
					if(dataUser.equalsIgnoreCase(userName)) {
						dataFlag = true;
						break;
					}
				}
				List<NewDataDistributeReport> tempDataList = getUserNewDataDistributeReport(user, paramMap, dataFlag);
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
			ReportSaveObj reportSave = new ReportSaveObj("BPS-新数据派发及成效");	//文件保存对象
//			StringBuffer allStrBuf = new StringBuffer();	//所有的数据
			List<NewDataDistributeReport> allDataList = new ArrayList<NewDataDistributeReport>();
			FileUtil<NewDataDistributeReport> fileUtil = new FileUtil<NewDataDistributeReport>();
			String[] headers = headersStr.split(",");
			List<String> reportFields = new ArrayList<String>();
			reportFields.add("beginTime");
			reportFields.add("endTime");
			reportFields.add("center");
			reportFields.add("group");
			reportFields.add("userName");
			reportFields.add("userRealName");
			reportFields.add("ywType");
			reportFields.add("dataType");
			reportFields.add("distributeAmount");
			reportFields.add("distributeMoney");
			reportFields.add("whNum");
			reportFields.add("connectNum");
			reportFields.add("successAcceptAmount");
			reportFields.add("successAcceptMoney");
			reportFields.add("successApproveAmount");
			reportFields.add("successApproveMoney");
			reportFields.add("wrongDataNum");
			
			long allFileNum = 0L;
			for(String centerId : centerMap.keySet()){
//				StringBuffer centerStrBuf = new StringBuffer();	//中心的数据
				List<NewDataDistributeReport> centerDataList = new ArrayList<NewDataDistributeReport>();
				long centerFileNum = 0L;
				for(String groupId : centerGroupMap.get(centerId).keySet()){
//					StringBuffer groupStrBuf = new StringBuffer();	//小组的数据
					long groupFileNum = 0L;
					List<NewDataDistributeReport> groupList = dataMap.get(groupId);
					if(groupList != null){
						groupFileNum += groupList.size();
						centerDataList.addAll(groupList);
						centerFileNum += groupList.size();
//						for(NewDataDistributeReport report : groupList){
//							groupStrBuf.append(report.toString()+"\r\n");
//							groupFileNum++;
//						}
//						centerStrBuf.append(groupStrBuf.toString());
//						centerFileNum += groupFileNum;
					}else{
						groupList = new ArrayList<NewDataDistributeReport>();
					}
//					groupStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,数据类别,新数派发量,新数派发金额,新数外呼量,新数接通量,新数成功受理量,新数成功受理金额,新数成功批核量,新数成功批核金额,错误数据量（M标）\r\n");
					String groupPath = saveUrl+dateStr+File.separator+centerId.trim()+File.separator+groupId.trim()+File.separator;
					String totalPath = groupPath + reportName;
					//System.out.println(groupPath);
					File groupFile = new File(totalPath);
					File groupPathFile = groupFile.getParentFile();
					if(!groupPathFile.exists()){
						groupPathFile.mkdirs();
					}
//					FileUtil.createFile(totalPath, groupStrBuf.toString());
					fileUtil.createFile(totalPath, "BPS-新数据派发及成效", headers, groupList, reportFields);

					reportSave.setFileName(reportName+"&"+centerId+"_"+groupId);
					reportSave.setFileNum(groupFileNum);
					reportSave.setFilePath(groupPath);
					reportSave.setTime(format.format(new Date()));
					bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);
				}
				List<NewDataDistributeReport> centerList = dataMap.get(centerId);
				if(centerList != null){
					centerDataList.addAll(centerList);
					centerFileNum += centerList.size();
//					for(NewDataDistributeReport report : centerList){
//						centerStrBuf.append(report.toString()+"\r\n");
//						centerFileNum++;
//					}
				}
				allDataList.addAll(centerDataList);
//				allStrBuf.append(centerStrBuf.toString());
				allFileNum += centerFileNum;
//				centerStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,数据类别,新数派发量,新数派发金额,新数外呼量,新数接通量,新数成功受理量,新数成功受理金额,新数成功批核量,新数成功批核金额,错误数据量（M标）\r\n");
				String centerPath = saveUrl+dateStr+File.separator+centerId.trim()+File.separator;
				String totalPath = centerPath + reportName;
				File centerFile = new File(totalPath);
				File centerPathFile = centerFile.getParentFile();
				if(!centerPathFile.exists()){
					centerPathFile.mkdirs();
				}
//				FileUtil.createFile(totalPath, centerStrBuf.toString());
				fileUtil.createFile(totalPath, "BPS-新数据派发及成效", headers, centerDataList, reportFields);
				
				reportSave.setFileName(reportName+"&"+centerId);
				reportSave.setFileNum(centerFileNum);
				reportSave.setFilePath(centerPath);
				reportSave.setTime(format.format(new Date()));
				bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);
			}
//			allStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,数据类别,新数派发量,新数派发金额,新数外呼量,新数接通量,新数成功受理量,新数成功受理金额,新数成功批核量,新数成功批核金额,错误数据量（M标）\r\n");
			String allPath = saveUrl+dateStr+File.separator;
			String totalPath = allPath + reportName;
			File allFile = new File(totalPath);
			File allPathFile = allFile.getParentFile();
			if(!allPathFile.exists()){
				allPathFile.mkdirs();
			}
//			FileUtil.createFile(totalPath, allStrBuf.toString());
			fileUtil.createFile(totalPath, "BPS-新数据派发及成效", headers, allDataList, reportFields);
			
			reportSave.setFileName(reportName+"&");
			reportSave.setFileNum(allFileNum);
			reportSave.setFilePath(allPath);
			reportSave.setTime(format.format(new Date()));
			bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);
			System.out.println(dateStr+"新数据派发及成效报表生成结束"+DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
		}catch (Exception e) {
			e.printStackTrace();
			logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  生成"+dateStr+"新数据派发及成效报表错误===>  "+e.toString());
			return false;
		}
		return true;
	}


	@Override
	public List<NewDataDistributeReport> getUserNewDataDistributeReport(BpsUserInfo user, Map<String, Object> paramMap, boolean dataFlag) {
		String[] ywTypeArr = {"EPP", "账单分期", "大额EPPC", "EPPC", "EPPC备用金"};
		List<NewDataDistributeReport> tempDataList = new ArrayList<NewDataDistributeReport>();
		String userName = user.getUserName();
		paramMap.put("userName", userName);
		NewDataDistributeReport parentRepor = new NewDataDistributeReport(user);
		String centerId = user.getCenterId();
		String groupId = user.getGroupId();
		parentRepor.setCenter(centerMap.get(centerId));
		parentRepor.setGroup(centerGroupMap.get(centerId).get(groupId));
		

		parentRepor.setBeginTime((String)paramMap.get("beginTime"));
		parentRepor.setEndTime((String)paramMap.get("endTime"));
		
		for (String ywType : ywTypeArr){
			NewDataDistributeReport report = (NewDataDistributeReport) parentRepor.clone();
			paramMap.put("ywType", ywType);
			report.setYwType(ywType);
			
			List<String> dataTypeList = newDataDistributeReportDao.getDataType("getDataType", paramMap);
			for (String dataType : dataTypeList){
				NewDataDistributeReport newReport = (NewDataDistributeReport)report.clone();
				newReport.setDataType(dataType);
				if(!dataFlag){
					newReport.setConnectNum(0L);
					newReport.setDistributeAmount(0L);
					newReport.setDistributeMoney(0.0);
					newReport.setSuccessAcceptAmount(0L);
					newReport.setSuccessAcceptMoney(0.0);
					newReport.setSuccessApproveAmount(0);
					newReport.setSuccessApproveMoney(0.0);
					newReport.setWhNum(0L);
					newReport.setWrongDataNum(0L);
				}else{
					paramMap.put("dataType", dataType);
					newReport.setDistributeAmount(newDataDistributeReportDao.getDistributeAmount("getDistributeAmount", paramMap));
					if("EPP".equals(ywType) || "账单分期".equals(ywType)){
						newReport.setDistributeMoney(newDataDistributeReportDao.getNewDistributeMoney1("getNewDistributeMoney1", paramMap));
					}else{
						double moneyA = newDataDistributeReportDao.getNewDistributeMoney2A("getNewDistributeMoney2A", paramMap);
						double moneyB = newDataDistributeReportDao.getNewDistributeMoney2B("getNewDistributeMoney2B", paramMap);
						newReport.setDistributeMoney(moneyA+moneyB);
					}
					newReport.setWhNum(newDataDistributeReportDao.getWhNum("getWhNum", paramMap));
					newReport.setConnectNum(newDataDistributeReportDao.getNewConnectNum("getNewConnectNum", paramMap));
					newReport.setSuccessAcceptAmount(newDataDistributeReportDao.getNewSuccessAcceptAmount("getNewSuccessAcceptAmount", paramMap));
					newReport.setSuccessAcceptMoney(newDataDistributeReportDao.getNewSuccessAcceptMoney("getNewSuccessAcceptMoney", paramMap));
					
					List<String> dataA = newDataDistributeReportDao.getSuccessApproveDataA("getSuccessApproveDataA", paramMap);
					List<String> dataB = newDataDistributeReportDao.getSuccessApproveDataB("getSuccessApproveDataB", paramMap);
					dataB.removeAll(dataA);
					dataA.addAll(dataB);
					newReport.setSuccessApproveAmount(dataA.size());
					double moneyA = newDataDistributeReportDao.getNewSuccessApproveMoneyA("getNewSuccessApproveMoneyA", paramMap);
					double moneyB = newDataDistributeReportDao.getNewSuccessApproveMoneyB("getNewSuccessApproveMoneyB", paramMap);
					newReport.setSuccessApproveMoney(moneyA+moneyB);
					newReport.setWrongDataNum(newDataDistributeReportDao.getWrongDataNum("getWrongDataNum", paramMap));
				}
				
				tempDataList.add(newReport);
			}
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
		String prefix = "BPS-新数据派发及成效_"+beginTime+"~"+endTime;
		String realPreFix = "BPS-NewDataDistributeReport_"+beginTime+"~"+endTime;
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
			List<NewDataDistributeReport> dataList = new ArrayList<NewDataDistributeReport>();
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
				List<NewDataDistributeReport> tempDataList = getUserNewDataDistributeReport(user, paramMap, dataFlag);
				dataList.addAll(tempDataList);
			}
			
			SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			StringBuffer dataStrBuf = new StringBuffer();
//			long fileNum = 0;
//			dataStrBuf.append("开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,数据类别,新数派发量,新数派发金额,新数外呼量,新数接通量,新数成功受理量,新数成功受理金额,新数成功批核量,新数成功批核金额,错误数据量（M标）\r\n");
//			for (NewDataDistributeReport report : dataList){
//				dataStrBuf.append(report.toString()+"\r\n");
//				fileNum++;
//			}
			FileUtil<NewDataDistributeReport> fileUtil = new FileUtil<NewDataDistributeReport>();
			String[] headers = headersStr.split(",");
			List<String> reportFields = new ArrayList<String>();
			reportFields.add("beginTime");
			reportFields.add("endTime");
			reportFields.add("center");
			reportFields.add("group");
			reportFields.add("userName");
			reportFields.add("userRealName");
			reportFields.add("ywType");
			reportFields.add("dataType");
			reportFields.add("distributeAmount");
			reportFields.add("distributeMoney");
			reportFields.add("whNum");
			reportFields.add("connectNum");
			reportFields.add("successAcceptAmount");
			reportFields.add("successAcceptMoney");
			reportFields.add("successApproveAmount");
			reportFields.add("successApproveMoney");
			reportFields.add("wrongDataNum");
			
			
			String path = saveUrl+realReportName;
			File file = new File(path);
			File pathFile = file.getParentFile();
			if(!pathFile.exists()){
				pathFile.mkdirs();
			}
//			FileUtil.createFile(path, dataStrBuf.toString());
			fileUtil.createFile(path, "BPS-新数据派发及成效", headers, dataList, reportFields);
			
			//是否生成营销员报表
			if("是".equals(reportInfo.getYxyData())){
				paramMap.put("userName", reportInfo.getAssignName());
				String specificReportName = prefix + "_" +reportInfo.getAssignName() + ".xls";
				reportInfo.setFileyxyName(specificReportName);
				String realSpecificReportName = realPreFix + "_" +reportInfo.getAssignName() + ".xls";
				reportInfo.setFileyxyRealname(realSpecificReportName);
				List<BpsUserInfo> specificUserList = bpsRwHistoryDao.getUserInfoByTime("getUserInfoByTime", paramMap);
				
				List<NewDataDistributeReport> specificDataList = new ArrayList<NewDataDistributeReport>();
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
					List<NewDataDistributeReport> tempDataList = getUserNewDataDistributeReport(user, paramMap, dataFlag);
					specificDataList.addAll(tempDataList);
				}
				
//				StringBuffer specificDataStrBuf = new StringBuffer();
//				long specificFileNum = 0;
//				specificDataStrBuf.append("开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,数据类别,新数派发量,新数派发金额,新数外呼量,新数接通量,新数成功受理量,新数成功受理金额,新数成功批核量,新数成功批核金额,错误数据量（M标）\r\n");
//				for (NewDataDistributeReport report : specificDataList){
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
				fileUtil.createFile(specificPath, "BPS-新数据派发及成效", headers, specificDataList, reportFields);
			}
			
			reportInfo.setZhixingTime(format.format(new Date()));
			reportInfo.setState("已执行");
			bpsRwHistoryDao.updateDefinedReportInfo("updateDefinedReportInfo", reportInfo);
		}catch (Exception e) {
			e.printStackTrace();
			logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  生成"+reportInfo.getStartTime()+"~"+reportInfo.getEndTime()+"自定义新数据派发及成效报表错误===>  "+e.toString());
			return false;
		}
		return true;
	}

}
