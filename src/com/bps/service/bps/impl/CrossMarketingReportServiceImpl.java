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

import com.bps.bean.ReportSaveObj;
import com.bps.dal.dao.bps.BpsRwHistoryDao;
import com.bps.dal.dao.bps.CrossMarketingReportDao;
import com.bps.dal.object.bps.BpsUserInfo;
import com.bps.dal.object.bps.CrossMarketingReport;
import com.bps.dal.object.bps.DefinedReportInfo;
import com.bps.service.bps.CrossMarketingReportService;
import com.bps.util.DateUtil;
import com.bps.util.FileUtil;
import com.bps.util.RedisUtil;

public class CrossMarketingReportServiceImpl implements CrossMarketingReportService{
	private static final Logger logger = Logger.getLogger("logs");
	@Resource
	private BpsRwHistoryDao bpsRwHistoryDao;
	@Resource
	private CrossMarketingReportDao crossMarketingReportDao;
	@Autowired
	private RedisUtil redisUtil;
	
	private Map<String, String> centerMap = null;
	private Map<String, Map<String, String>> centerGroupMap = null;
	private final String headersStr = "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,外呼数据派发金额,交叉EPP批核量,交叉账单批核量,交叉EPPC批核量,交叉大额EPPC批核量,交叉EPP批核金额,交叉账单批核金额,交叉EPPC批核金额,交叉大额EPPC批核金额,交叉EPP自动绑定量,交叉MGL意愿量,交叉保险意愿量,交叉自动绑定账单分期量,交叉EPP批核金额（3期）,交叉EPP批核金额（6期）,交叉EPP批核金额（12期）,交叉EPP批核金额（18期）,交叉EPP批核金额（24期）,交叉EPP批核金额（36期）,交叉账单批核金额（3期）,交叉账单批核金额（6期）,交叉账单批核金额（12期）,交叉账单批核金额（18期）,交叉账单批核金额（24期）,交叉账单批核金额（36期）,交叉EPPC批核金额（3期）,交叉EPPC批核金额（6期）,交叉EPPC批核金额（12期）,交叉EPPC批核金额（18期）,交叉EPPC批核金额（24期）,交叉EPPC批核金额（36期）,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额";
	
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
		System.out.println(dateStr+"交叉营销成效报表生成开始"+DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
		try{
			if(centerMap == null){
				centerMap = (Map<String, String>) redisUtil.getJedis().hgetAll(RedisUtil.BPS_CENTER);
			}
			if(centerGroupMap == null) {
				centerGroupMap = (Map<String, Map<String, String>>) RedisUtil.deserialize(redisUtil.getJedis().get(RedisUtil.BPS_GROUP.getBytes()));
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Map<String, List<CrossMarketingReport>> dataMap = new HashMap<String, List<CrossMarketingReport>>();
			paramMap.put("beginTime", beginTime);
			paramMap.put("endTime", endTime);
			paramMap.put("skipRow", -1);	//不分页查询所有数据
			//获取有数据派发的用户名List
			List<String> haveDataUser = bpsRwHistoryDao.getHaveDataUser("getHaveDataUser", paramMap);
			List<BpsUserInfo> userList = bpsRwHistoryDao.getUserInfoByTime("getUserInfoByTime", paramMap);
			for(BpsUserInfo user : userList){
				String centerId = user.getCenterId();
				String groupId = user.getGroupId();
				//判断是否为BPO成员，如果是则将其所在小组ID添加进bpoCenterGroup以便区分
				String roleIds = ","+user.getRoleIds()+",";
				if(roleIds.contains(",172,")){	//如果包含172，那就是营销员（BPO）
					Set<String> bpoList = bpoCenterGroup.get(centerId);
					if(bpoList == null){
						bpoList = new HashSet<String>();
					}
					bpoList.add(groupId);
					bpoCenterGroup.put(centerId, bpoList);
				}
				
				List<CrossMarketingReport> dataList = null;
				if(centerGroupMap.get(centerId).get(groupId) != null){	//表示有组别信息，否则只有中心信息
					dataList = dataMap.get(groupId);	//获取该小组的list
				}else{
					dataList = dataMap.get(centerId);	//没有小组信息的就获取该中心的list
				}
				if(dataList == null) dataList = new ArrayList<CrossMarketingReport>();
				
				//判断该用户是否在haveDataUser中，如果不在则表示没有数据，则所有数据为0；
				boolean dataFlag = haveDataUser.contains(user.getUserName());
				
				List<CrossMarketingReport> tempDataList = getUserCrossMarketingReport(user, paramMap, dataFlag);
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
			ReportSaveObj reportSave = new ReportSaveObj("BPS-交叉营销成效");	//文件保存对象
//			StringBuffer allStrBuf = new StringBuffer();	//所有的数据
			List<CrossMarketingReport> allDataList = new ArrayList<CrossMarketingReport>();
			FileUtil<CrossMarketingReport> fileUtil = new FileUtil<CrossMarketingReport>();
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
			reportFields.add("crossEPPAmount");
			reportFields.add("crossBillAmount");
			reportFields.add("crossEPPCAmount");
			reportFields.add("crossBigEPPCAmount");
			reportFields.add("crossEPPMoney");
			reportFields.add("crossBillMoney");
			reportFields.add("crossEPPCMoney");
			reportFields.add("crossBigEPPCMoney");
			reportFields.add("crossEPPBinding");
			reportFields.add("crossMGLWill");
			reportFields.add("crossInsuranceWill");
			reportFields.add("crossBillBinding");
			reportFields.add("crossEPPMoney3");
			reportFields.add("crossEPPMoney6");
			reportFields.add("crossEPPMoney12");
			reportFields.add("crossEPPMoney18");
			reportFields.add("crossEPPMoney24");
			reportFields.add("crossEPPMoney36");
			reportFields.add("crossBillMoney3");
			reportFields.add("crossBillMoney6");
			reportFields.add("crossBillMoney12");
			reportFields.add("crossBillMoney18");
			reportFields.add("crossBillMoney24");
			reportFields.add("crossBillMoney36");
			reportFields.add("crossEPPCMoney3");
			reportFields.add("crossEPPCMoney6");
			reportFields.add("crossEPPCMoney12");
			reportFields.add("crossEPPCMoney18");
			reportFields.add("crossEPPCMoney24");
			reportFields.add("crossEPPCMoney36");
			reportFields.add("crossBigEPPCMoney3");
			reportFields.add("crossBigEPPCMoney6");
			reportFields.add("crossBigEPPCMoney12");
			reportFields.add("crossBigEPPCMoney18");
			reportFields.add("crossBigEPPCMoney24");
			reportFields.add("crossBigEPPCMoney36");
			
			
			long allFileNum = 0L;
			
			for(String centerId : centerMap.keySet()){
//				StringBuffer centerStrBuf = new StringBuffer();	//中心的数据
				List<CrossMarketingReport> centerDataList = new ArrayList<CrossMarketingReport>();
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
					List<CrossMarketingReport> groupList = dataMap.get(groupId);
					if(groupList != null){
						groupFileNum += groupList.size();
						centerDataList.addAll(groupList);
						centerFileNum += groupList.size();
//						for(CrossMarketingReport report : groupList){
//							groupStrBuf.append(report.toString()+"\r\n");
//							groupFileNum++;
//						}
//						centerStrBuf.append(groupStrBuf.toString());
//						centerFileNum += groupFileNum;
					}else{
						groupList = new ArrayList<CrossMarketingReport>();
					}
					
//					groupStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,外呼数据派发金额,交叉EPP批核量,交叉账单批核量,交叉EPPC批核量,交叉大额EPPC批核量,交叉EPP批核金额,交叉账单批核金额,交叉EPPC批核金额,交叉大额EPPC批核金额,交叉EPP自动绑定量,交叉MGL意愿量,交叉保险意愿量,交叉自动绑定账单分期量,交叉EPP批核金额（3期）,交叉EPP批核金额（6期）,交叉EPP批核金额（12期）,交叉EPP批核金额（18期）,交叉EPP批核金额（24期）,交叉EPP批核金额（36期）,交叉账单批核金额（3期）,交叉账单批核金额（6期）,交叉账单批核金额（12期）,交叉账单批核金额（18期）,交叉账单批核金额（24期）,交叉账单批核金额（36期）,交叉EPPC批核金额（3期）,交叉EPPC批核金额（6期）,交叉EPPC批核金额（12期）,交叉EPPC批核金额（18期）,交叉EPPC批核金额（24期）,交叉EPPC批核金额（36期）,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额\r\n");
					String groupPath = saveUrl+dateStr+File.separator+centerId.trim()+File.separator+groupId.trim()+File.separator;
					String totalPath = groupPath + reportName;
					//System.out.println(groupPath);
					File groupFile = new File(totalPath);
					File groupPathFile = groupFile.getParentFile();
					if(!groupPathFile.exists()){
						groupPathFile.mkdirs();
					}
//					FileUtil.createFile(totalPath, groupStrBuf.toString());
					
					fileUtil.createFile(totalPath, "BPS-交叉营销成效", headers, groupList, reportFields);

					reportSave.setFileName(reportName+"&"+centerId+"_"+groupId);
					reportSave.setFileNum(groupFileNum);
					reportSave.setFilePath(groupPath);
					reportSave.setTime(format.format(new Date()));
					bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);
				}
				List<CrossMarketingReport> centerList = dataMap.get(centerId);
				if(centerList != null){
					centerDataList.addAll(centerList);
					centerFileNum += centerList.size();
//					for(CrossMarketingReport report : centerList){
//						centerStrBuf.append(report.toString()+"\r\n");
//						centerFileNum++;
//					}
				}
				allDataList.addAll(centerDataList);
//				allStrBuf.append(centerStrBuf.toString());
				allFileNum += centerFileNum;
//				centerStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,外呼数据派发金额,交叉EPP批核量,交叉账单批核量,交叉EPPC批核量,交叉大额EPPC批核量,交叉EPP批核金额,交叉账单批核金额,交叉EPPC批核金额,交叉大额EPPC批核金额,交叉EPP自动绑定量,交叉MGL意愿量,交叉保险意愿量,交叉自动绑定账单分期量,交叉EPP批核金额（3期）,交叉EPP批核金额（6期）,交叉EPP批核金额（12期）,交叉EPP批核金额（18期）,交叉EPP批核金额（24期）,交叉EPP批核金额（36期）,交叉账单批核金额（3期）,交叉账单批核金额（6期）,交叉账单批核金额（12期）,交叉账单批核金额（18期）,交叉账单批核金额（24期）,交叉账单批核金额（36期）,交叉EPPC批核金额（3期）,交叉EPPC批核金额（6期）,交叉EPPC批核金额（12期）,交叉EPPC批核金额（18期）,交叉EPPC批核金额（24期）,交叉EPPC批核金额（36期）,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额\r\n");
				String centerPath = saveUrl+dateStr+File.separator+centerId.trim()+File.separator;
				String totalPath = centerPath + reportName;
				File centerFile = new File(totalPath);
				File centerPathFile = centerFile.getParentFile();
				if(!centerPathFile.exists()){
					centerPathFile.mkdirs();
				}
//				FileUtil.createFile(totalPath, centerStrBuf.toString());
				fileUtil.createFile(totalPath, "BPS-交叉营销成效", headers, centerDataList, reportFields);
				
				reportSave.setFileName(reportName+"&"+centerId);
				reportSave.setFileNum(centerFileNum);
				reportSave.setFilePath(centerPath);
				reportSave.setTime(format.format(new Date()));
				bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);
			}
//			allStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,外呼数据派发金额,交叉EPP批核量,交叉账单批核量,交叉EPPC批核量,交叉大额EPPC批核量,交叉EPP批核金额,交叉账单批核金额,交叉EPPC批核金额,交叉大额EPPC批核金额,交叉EPP自动绑定量,交叉MGL意愿量,交叉保险意愿量,交叉自动绑定账单分期量,交叉EPP批核金额（3期）,交叉EPP批核金额（6期）,交叉EPP批核金额（12期）,交叉EPP批核金额（18期）,交叉EPP批核金额（24期）,交叉EPP批核金额（36期）,交叉账单批核金额（3期）,交叉账单批核金额（6期）,交叉账单批核金额（12期）,交叉账单批核金额（18期）,交叉账单批核金额（24期）,交叉账单批核金额（36期）,交叉EPPC批核金额（3期）,交叉EPPC批核金额（6期）,交叉EPPC批核金额（12期）,交叉EPPC批核金额（18期）,交叉EPPC批核金额（24期）,交叉EPPC批核金额（36期）,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额\r\n");
			String allPath = saveUrl+dateStr+File.separator;
			String totalPath = allPath + reportName;
			File allFile = new File(totalPath);
			File allPathFile = allFile.getParentFile();
			if(!allPathFile.exists()){
				allPathFile.mkdirs();
			}
//			FileUtil.createFile(totalPath, allStrBuf.toString());
			fileUtil.createFile(totalPath, "BPS-交叉营销成效", headers, allDataList, reportFields);
			
			reportSave.setFileName(reportName+"&");
			reportSave.setFileNum(allFileNum);
			reportSave.setFilePath(allPath);
			reportSave.setTime(format.format(new Date()));
			bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);
			
			
			//bpo文件生成
			reportName = "BPO-BPS-"+reportName;
			reportSave.setType("BPO-BPS-交叉营销成效");	//文件保存对象
//			allStrBuf.setLength(0);	//所有的数据
			allDataList.clear();
			allFileNum = 0L;
			for(String centerId : bpoCenterGroup.keySet()){
//				StringBuffer centerStrBuf = new StringBuffer();	//中心的数据
				List<CrossMarketingReport> centerDataList = new ArrayList<CrossMarketingReport>();
				long centerFileNum = 0L;
				for(String groupId : bpoCenterGroup.get(centerId)){
//					StringBuffer groupStrBuf = new StringBuffer();	//小组的数据
					long groupFileNum = 0L;
					List<CrossMarketingReport> groupList = dataMap.get(groupId);
					if(groupList != null){
						groupFileNum += groupList.size();
						centerDataList.addAll(groupList);
						centerFileNum += groupList.size();
//						for(CrossMarketingReport report : groupList){
//							groupStrBuf.append(report.toString()+"\r\n");
//							groupFileNum++;
//						}
//						centerStrBuf.append(groupStrBuf.toString());
//						centerFileNum += groupFileNum;
					}else{
						groupList = new ArrayList<CrossMarketingReport>();
					}
//					groupStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,外呼数据派发金额,交叉EPP批核量,交叉账单批核量,交叉EPPC批核量,交叉大额EPPC批核量,交叉EPP批核金额,交叉账单批核金额,交叉EPPC批核金额,交叉大额EPPC批核金额,交叉EPP自动绑定量,交叉MGL意愿量,交叉保险意愿量,交叉自动绑定账单分期量,交叉EPP批核金额（3期）,交叉EPP批核金额（6期）,交叉EPP批核金额（12期）,交叉EPP批核金额（18期）,交叉EPP批核金额（24期）,交叉EPP批核金额（36期）,交叉账单批核金额（3期）,交叉账单批核金额（6期）,交叉账单批核金额（12期）,交叉账单批核金额（18期）,交叉账单批核金额（24期）,交叉账单批核金额（36期）,交叉EPPC批核金额（3期）,交叉EPPC批核金额（6期）,交叉EPPC批核金额（12期）,交叉EPPC批核金额（18期）,交叉EPPC批核金额（24期）,交叉EPPC批核金额（36期）,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额\r\n");
					String groupPath = saveUrl+dateStr+File.separator+centerId.trim()+File.separator+groupId.trim()+File.separator;
					String bpoTotalPath = groupPath + reportName;
					//System.out.println(groupPath);
					File groupFile = new File(bpoTotalPath);
					File groupPathFile = groupFile.getParentFile();
					if(!groupPathFile.exists()){
						groupPathFile.mkdirs();
					}
//					FileUtil.createFile(bpoTotalPath, groupStrBuf.toString());
					fileUtil.createFile(totalPath, "BPO-BPS-交叉营销成效", headers, groupList, reportFields);

					reportSave.setFileName(reportName+"&"+centerId+"_"+groupId);
					reportSave.setFileNum(groupFileNum);
					reportSave.setFilePath(groupPath);
					reportSave.setTime(format.format(new Date()));
					bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);
				}
				List<CrossMarketingReport> centerList = dataMap.get(centerId);
				if(centerList != null){
					centerDataList.addAll(centerList);
					centerFileNum += centerList.size();
//					for(CrossMarketingReport report : centerList){
//						centerStrBuf.append(report.toString()+"\r\n");
//						centerFileNum++;
//					}
				}
				allDataList.addAll(centerDataList);
//				allStrBuf.append(centerStrBuf.toString());
				allFileNum += centerFileNum;
//				centerStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,外呼数据派发金额,交叉EPP批核量,交叉账单批核量,交叉EPPC批核量,交叉大额EPPC批核量,交叉EPP批核金额,交叉账单批核金额,交叉EPPC批核金额,交叉大额EPPC批核金额,交叉EPP自动绑定量,交叉MGL意愿量,交叉保险意愿量,交叉自动绑定账单分期量,交叉EPP批核金额（3期）,交叉EPP批核金额（6期）,交叉EPP批核金额（12期）,交叉EPP批核金额（18期）,交叉EPP批核金额（24期）,交叉EPP批核金额（36期）,交叉账单批核金额（3期）,交叉账单批核金额（6期）,交叉账单批核金额（12期）,交叉账单批核金额（18期）,交叉账单批核金额（24期）,交叉账单批核金额（36期）,交叉EPPC批核金额（3期）,交叉EPPC批核金额（6期）,交叉EPPC批核金额（12期）,交叉EPPC批核金额（18期）,交叉EPPC批核金额（24期）,交叉EPPC批核金额（36期）,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额\r\n");
				String centerPath = saveUrl+dateStr+File.separator+centerId.trim()+File.separator;
				String bpoTotalPath = centerPath + reportName;
				File centerFile = new File(bpoTotalPath);
				File centerPathFile = centerFile.getParentFile();
				if(!centerPathFile.exists()){
					centerPathFile.mkdirs();
				}
//				FileUtil.createFile(bpoTotalPath, centerStrBuf.toString());
				fileUtil.createFile(totalPath, "BPO-BPS-交叉营销成效", headers, centerDataList, reportFields);
				
				reportSave.setFileName(reportName+"&"+centerId);
				reportSave.setFileNum(centerFileNum);
				reportSave.setFilePath(centerPath);
				reportSave.setTime(format.format(new Date()));
				bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);
			}
//			allStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,外呼数据派发金额,交叉EPP批核量,交叉账单批核量,交叉EPPC批核量,交叉大额EPPC批核量,交叉EPP批核金额,交叉账单批核金额,交叉EPPC批核金额,交叉大额EPPC批核金额,交叉EPP自动绑定量,交叉MGL意愿量,交叉保险意愿量,交叉自动绑定账单分期量,交叉EPP批核金额（3期）,交叉EPP批核金额（6期）,交叉EPP批核金额（12期）,交叉EPP批核金额（18期）,交叉EPP批核金额（24期）,交叉EPP批核金额（36期）,交叉账单批核金额（3期）,交叉账单批核金额（6期）,交叉账单批核金额（12期）,交叉账单批核金额（18期）,交叉账单批核金额（24期）,交叉账单批核金额（36期）,交叉EPPC批核金额（3期）,交叉EPPC批核金额（6期）,交叉EPPC批核金额（12期）,交叉EPPC批核金额（18期）,交叉EPPC批核金额（24期）,交叉EPPC批核金额（36期）,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额\r\n");
			allPath = saveUrl+dateStr+File.separator;
			totalPath = allPath + reportName;
			allFile = new File(totalPath);
			allPathFile = allFile.getParentFile();
			if(!allPathFile.exists()){
				allPathFile.mkdirs();
			}
//			FileUtil.createFile(totalPath, allStrBuf.toString());
			fileUtil.createFile(totalPath, "BPO-BPS-交叉营销成效", headers, allDataList, reportFields);
			
			reportSave.setFileName(reportName+"&");
			reportSave.setFileNum(allFileNum);
			reportSave.setFilePath(allPath);
			reportSave.setTime(format.format(new Date()));
			bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);
			
			System.out.println(dateStr+"交叉营销成效报表生成结束"+DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
		}catch (Exception e) {
			e.printStackTrace();
			logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  生成"+dateStr+"交叉营销成效报表错误===>  "+e.toString());
			return false;
		}
		return true;
	}


	@Override
	public List<CrossMarketingReport> getUserCrossMarketingReport(BpsUserInfo user, Map<String, Object> paramMap, boolean dataFlag) {
		String[] ywTypeArr = {"EPP", "账单分期", "大额EPPC", "EPPC", "EPPC备用金"};
		List<CrossMarketingReport> tempDataList = new ArrayList<CrossMarketingReport>();
		
		CrossMarketingReport parentRepor = new CrossMarketingReport(user);
		
		String centerId = user.getCenterId();
		String groupId = user.getGroupId();
		parentRepor.setCenter(centerMap.get(centerId));
		parentRepor.setGroup(centerGroupMap.get(centerId).get(groupId));
		
		parentRepor.setBeginTime((String)paramMap.get("beginTime"));
		parentRepor.setEndTime((String)paramMap.get("endTime"));

		for (String ywType : ywTypeArr){
			CrossMarketingReport report = (CrossMarketingReport) parentRepor.clone();
			report.setYwType(ywType);
			//如果该用户不存在数据派发,不进行数据库查询直接赋默认值
			if(!dataFlag){
				report.setCrossBigEPPCAmount(0L);
				report.setCrossBigEPPCMoney(0.0);
				report.setCrossBigEPPCMoney3(0.0);
				report.setCrossBigEPPCMoney6(0.0);
				report.setCrossBigEPPCMoney12(0.0);
				report.setCrossBigEPPCMoney18(0.0);
				report.setCrossBigEPPCMoney24(0.0);
				report.setCrossBigEPPCMoney36(0.0);
				report.setCrossBillAmount(0L);
				report.setCrossBillBinding(0L);
				report.setCrossBillMoney(0.0);
				report.setCrossBillMoney3(0.0);
				report.setCrossBillMoney6(0.0);
				report.setCrossBillMoney12(0.0);
				report.setCrossBillMoney18(0.0);
				report.setCrossBillMoney24(0.0);
				report.setCrossBillMoney36(0.0);
				report.setCrossEPPAmount(0L);
				report.setCrossEPPBinding(0L);
				report.setCrossEPPMoney(0.0);
				report.setCrossEPPMoney3(0.0);
				report.setCrossEPPMoney6(0.0);
				report.setCrossEPPMoney12(0.0);
				report.setCrossEPPMoney18(0.0);
				report.setCrossEPPMoney24(0.0);
				report.setCrossEPPMoney36(0.0);
				report.setCrossEPPCAmount(0L);
				report.setCrossEPPCMoney(0.0);
				report.setCrossEPPCMoney3(0.0);
				report.setCrossEPPCMoney6(0.0);
				report.setCrossEPPCMoney12(0.0);
				report.setCrossEPPCMoney18(0.0);
				report.setCrossEPPCMoney24(0.0);
				report.setCrossEPPCMoney36(0.0);
				report.setCrossInsuranceWill(0L);
				report.setCrossMGLWill(0L);
				report.setWhDateNum(0L);
				report.setWhDistributeMoney(0.0);
			}else{
				String userName = user.getUserName();
				paramMap.put("userName", userName);
				paramMap.put("ywType", ywType);
				
				long numA = 0L;
				long numB = 0L;
				double moneyA = 0.0;
				double moneyB = 0.0;
				//派发金额
				if("EPP".equals(ywType) || "账单分期".equals(ywType)){
					report.setWhDistributeMoney(crossMarketingReportDao.getWhDistributeMoney1("getWhDistributeMoney1", paramMap));
				}else{
					moneyA = crossMarketingReportDao.getWhDistributeMoney2A("getWhDistributeMoney2A", paramMap);
					moneyB = crossMarketingReportDao.getWhDistributeMoney2B("getWhDistributeMoney2B", paramMap);
					report.setWhDistributeMoney(moneyA + moneyB);
				}
				
				report.setWhDateNum(bpsRwHistoryDao.getWhDateNum("getWhDateNum", paramMap));
				report.setCrossEPPBinding(crossMarketingReportDao.getCrossEPPBinding("getCrossEPPBinding", paramMap));
				report.setCrossMGLWill(crossMarketingReportDao.getCrossMGLWill("getCrossMGLWill", paramMap));
				report.setCrossInsuranceWill(crossMarketingReportDao.getCrossInsuranceWill("getCrossInsuranceWill", paramMap));
				report.setCrossBillBinding(crossMarketingReportDao.getCrossBillBinding("getCrossBillBinding", paramMap));
				
				
				//交叉账单分期批核量和批核金额
				if("账单分期".equals(ywType)){
					report.setCrossBillAmount(0L);
					report.setCrossBillMoney(0.00);
					report.setCrossBillMoney3(0.00);
					report.setCrossBillMoney6(0.00);
					report.setCrossBillMoney12(0.00);
					report.setCrossBillMoney18(0.00);
					report.setCrossBillMoney24(0.00);
					report.setCrossBillMoney36(0.00);
				}else{
					paramMap.put("dataType", "账单分期");
					numA = crossMarketingReportDao.getSuccessApproveDataNumA("getSuccessApproveDataNumA", paramMap);
					numB = crossMarketingReportDao.getSuccessApproveDataNumB("getSuccessApproveDataNumB", paramMap);
					report.setCrossBillAmount(numA + numB);
					paramMap.put("period", "0期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossBillMoney(moneyA + moneyB);
					paramMap.put("period", "3期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossBillMoney3(moneyA + moneyB);
					paramMap.put("period", "6期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossBillMoney6(moneyA + moneyB);
					paramMap.put("period", "12期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossBillMoney12(moneyA + moneyB);
					paramMap.put("period", "18期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossBillMoney18(moneyA + moneyB);
					paramMap.put("period", "24期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossBillMoney24(moneyA + moneyB);
					paramMap.put("period", "36期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossBillMoney36(moneyA + moneyB);
				}
				
				//大额EPPC批核量和批核金额
				if("大额EPPC".equals(ywType)){
					report.setCrossBigEPPCAmount(0L);
					report.setCrossBigEPPCMoney(0.00);
					report.setCrossBigEPPCMoney3(0.00);
					report.setCrossBigEPPCMoney6(0.00);
					report.setCrossBigEPPCMoney12(0.00);
					report.setCrossBigEPPCMoney18(0.00);
					report.setCrossBigEPPCMoney24(0.00);
					report.setCrossBigEPPCMoney36(0.00);
				}else{
					paramMap.put("dataType", "大额EPPC");
					numA = crossMarketingReportDao.getSuccessApproveDataNumA("getSuccessApproveDataNumA", paramMap);
					numB = crossMarketingReportDao.getSuccessApproveDataNumB("getSuccessApproveDataNumB", paramMap);
					report.setCrossBigEPPCAmount(numA + numB);
					paramMap.put("period", "0期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossBigEPPCMoney(moneyA + moneyB);
					paramMap.put("period", "3期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossBigEPPCMoney3(moneyA + moneyB);
					paramMap.put("period", "6期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossBigEPPCMoney6(moneyA + moneyB);
					paramMap.put("period", "12期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossBigEPPCMoney12(moneyA + moneyB);
					paramMap.put("period", "18期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossBigEPPCMoney18(moneyA + moneyB);
					paramMap.put("period", "24期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossBigEPPCMoney24(moneyA + moneyB);
					paramMap.put("period", "36期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossBigEPPCMoney36(moneyA + moneyB);
				}
				
				
				//交叉EPPC批核量和批核金额
				if("EPPC".equals(ywType) || "EPPC备用金".equals(ywType)){
					report.setCrossEPPCAmount(0L);
					report.setCrossEPPCMoney(0.00);
					report.setCrossEPPCMoney3(0.00);
					report.setCrossEPPCMoney6(0.00);
					report.setCrossEPPCMoney12(0.00);
					report.setCrossEPPCMoney18(0.00);
					report.setCrossEPPCMoney24(0.00);
					report.setCrossEPPCMoney36(0.00);
				}else{
					paramMap.put("dataType", "EPPC");
					numA = crossMarketingReportDao.getSuccessApproveDataNumA("getSuccessApproveDataNumA", paramMap);
					numB = crossMarketingReportDao.getSuccessApproveDataNumB("getSuccessApproveDataNumB", paramMap);
					report.setCrossEPPCAmount(numA + numB);
					paramMap.put("period", "0期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossEPPCMoney(moneyA + moneyB);
					paramMap.put("period", "3期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossEPPCMoney3(moneyA + moneyB);
					paramMap.put("period", "6期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossEPPCMoney6(moneyA + moneyB);
					paramMap.put("period", "12期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossEPPCMoney12(moneyA + moneyB);
					paramMap.put("period", "18期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossEPPCMoney18(moneyA + moneyB);
					paramMap.put("period", "24期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossEPPCMoney24(moneyA + moneyB);
					paramMap.put("period", "36期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossEPPCMoney36(moneyA + moneyB);
				}
				
				//交叉EPP批核量和批核金额
				if("EPP".equals(ywType)){
					report.setCrossEPPAmount(0L);
					report.setCrossEPPMoney(0.00);
					report.setCrossEPPMoney3(0.00);
					report.setCrossEPPMoney6(0.00);
					report.setCrossEPPMoney12(0.00);
					report.setCrossEPPMoney18(0.00);
					report.setCrossEPPMoney24(0.00);
					report.setCrossEPPMoney36(0.00);
				}else{
					//由于EPPC及备用金业务交叉EPP业务时没有带备用金字段值，修改有关交叉EPP的统计SQL,其他业务不需关联即可查询，不做修改
					if("EPPC".equals(ywType) || "EPPC备用金".equals(ywType)){
						paramMap.put("ywType", "EPP-"+ywType);
					}
					paramMap.put("dataType", "EPP");
					numA = crossMarketingReportDao.getSuccessApproveDataNumA("getSuccessApproveDataNumA", paramMap);
					numB = crossMarketingReportDao.getSuccessApproveDataNumB("getSuccessApproveDataNumB", paramMap);
					report.setCrossEPPAmount(numA + numB);
					paramMap.put("period", "0期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossEPPMoney(moneyA + moneyB);
					paramMap.put("period", "3期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossEPPMoney3(moneyA + moneyB);
					paramMap.put("period", "6期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossEPPMoney6(moneyA + moneyB);
					paramMap.put("period", "12期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossEPPMoney12(moneyA + moneyB);
					paramMap.put("period", "18期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossEPPMoney18(moneyA + moneyB);
					paramMap.put("period", "24期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossEPPMoney24(moneyA + moneyB);
					paramMap.put("period", "36期");
					moneyA = crossMarketingReportDao.getSuccessApproveDataMoneyA("getSuccessApproveDataMoneyA", paramMap);
					moneyB = crossMarketingReportDao.getSuccessApproveDataMoneyB("getSuccessApproveDataMoneyB", paramMap);
					report.setCrossEPPMoney36(moneyA + moneyB);
				}
				
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
		String centerText = reportInfo.getZhongxin();
		//设置生成的报表名称 例如：BPS-新数据派发及成效_20180605 09:00~20180608 12:00_广四中心_一组.xls
		String beginTime = reportInfo.getStartTime().replaceAll("-", "").substring(0, 14);
		String endTime = reportInfo.getEndTime().replaceAll("-", "").substring(0, 14);
		beginTime = beginTime.replaceAll(" ", "_");
		endTime = endTime.replaceAll(" ", "_");
		beginTime = beginTime.replaceAll(":", "");
		endTime = endTime.replaceAll(":", "");
		String prefix = "BPS-交叉营销成效_"+beginTime+"~"+endTime;
		String realPreFix = "BPS-CrossMarketingReport_"+beginTime+"~"+endTime;
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
			List<CrossMarketingReport> dataList = new ArrayList<CrossMarketingReport>();
			for(BpsUserInfo user : userList){
				//判断该用户是否在haveDataUser中，如果不在则表示没有数据，则所有数据为0；
				boolean dataFlag = haveDataUser.contains(user.getUserName());
				List<CrossMarketingReport> tempDataList = getUserCrossMarketingReport(user, paramMap, dataFlag);
				dataList.addAll(tempDataList);
			}
			
			SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			StringBuffer dataStrBuf = new StringBuffer();
//			long fileNum = 0;
//			dataStrBuf.append("开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,外呼数据派发金额,交叉EPP批核量,交叉账单批核量,交叉EPPC批核量,交叉大额EPPC批核量,交叉EPP批核金额,交叉账单批核金额,交叉EPPC批核金额,交叉大额EPPC批核金额,交叉EPP自动绑定量,交叉MGL意愿量,交叉保险意愿量,交叉自动绑定账单分期量,交叉EPP批核金额（3期）,交叉EPP批核金额（6期）,交叉EPP批核金额（12期）,交叉EPP批核金额（18期）,交叉EPP批核金额（24期）,交叉EPP批核金额（36期）,交叉账单批核金额（3期）,交叉账单批核金额（6期）,交叉账单批核金额（12期）,交叉账单批核金额（18期）,交叉账单批核金额（24期）,交叉账单批核金额（36期）,交叉EPPC批核金额（3期）,交叉EPPC批核金额（6期）,交叉EPPC批核金额（12期）,交叉EPPC批核金额（18期）,交叉EPPC批核金额（24期）,交叉EPPC批核金额（36期）,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额\r\n");
//			for (CrossMarketingReport report : dataList){
//				dataStrBuf.append(report.toString()+"\r\n");
//				fileNum++;
//			}
			FileUtil<CrossMarketingReport> fileUtil = new FileUtil<CrossMarketingReport>();
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
			reportFields.add("crossEPPAmount");
			reportFields.add("crossBillAmount");
			reportFields.add("crossEPPCAmount");
			reportFields.add("crossBigEPPCAmount");
			reportFields.add("crossEPPMoney");
			reportFields.add("crossBillMoney");
			reportFields.add("crossEPPCMoney");
			reportFields.add("crossBigEPPCMoney");
			reportFields.add("crossEPPBinding");
			reportFields.add("crossMGLWill");
			reportFields.add("crossInsuranceWill");
			reportFields.add("crossBillBinding");
			reportFields.add("crossEPPMoney3");
			reportFields.add("crossEPPMoney6");
			reportFields.add("crossEPPMoney12");
			reportFields.add("crossEPPMoney18");
			reportFields.add("crossEPPMoney24");
			reportFields.add("crossEPPMoney36");
			reportFields.add("crossBillMoney3");
			reportFields.add("crossBillMoney6");
			reportFields.add("crossBillMoney12");
			reportFields.add("crossBillMoney18");
			reportFields.add("crossBillMoney24");
			reportFields.add("crossBillMoney36");
			reportFields.add("crossEPPCMoney3");
			reportFields.add("crossEPPCMoney6");
			reportFields.add("crossEPPCMoney12");
			reportFields.add("crossEPPCMoney18");
			reportFields.add("crossEPPCMoney24");
			reportFields.add("crossEPPCMoney36");
			reportFields.add("crossBigEPPCMoney3");
			reportFields.add("crossBigEPPCMoney6");
			reportFields.add("crossBigEPPCMoney12");
			reportFields.add("crossBigEPPCMoney18");
			reportFields.add("crossBigEPPCMoney24");
			reportFields.add("crossBigEPPCMoney36");
			
			
			String path = saveUrl+realReportName;
			File file = new File(path);
			File pathFile = file.getParentFile();
			if(!pathFile.exists()){
				pathFile.mkdirs();
			}
//			FileUtil.createFile(path, dataStrBuf.toString());
			fileUtil.createFile(path, "BPS-交叉营销成效", headers, dataList, reportFields);
			
			//是否生成营销员报表
			if("是".equals(reportInfo.getYxyData())){
				paramMap.put("userName", reportInfo.getAssignName());
				String specificReportName = prefix + "_" +reportInfo.getAssignName() + ".xls";
				reportInfo.setFileyxyName(specificReportName);
				String realSpecificReportName = realPreFix + "_" +reportInfo.getAssignName() + ".xls";
				reportInfo.setFileyxyRealname(realSpecificReportName);
				List<BpsUserInfo> specificUserList = bpsRwHistoryDao.getUserInfoByTime("getUserInfoByTime", paramMap);
				
				List<CrossMarketingReport> specificDataList = new ArrayList<CrossMarketingReport>();
				for(BpsUserInfo user : specificUserList){
					//判断该用户是否在haveDataUser中，如果不在则表示没有数据，则所有数据为0；
					boolean dataFlag = haveDataUser.contains(user.getUserName());
					List<CrossMarketingReport> tempDataList = getUserCrossMarketingReport(user, paramMap, dataFlag);
					specificDataList.addAll(tempDataList);
				}
				
//				StringBuffer specificDataStrBuf = new StringBuffer();
//				long specificFileNum = 0;
//				specificDataStrBuf.append("开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,外呼数据派发金额,交叉EPP批核量,交叉账单批核量,交叉EPPC批核量,交叉大额EPPC批核量,交叉EPP批核金额,交叉账单批核金额,交叉EPPC批核金额,交叉大额EPPC批核金额,交叉EPP自动绑定量,交叉MGL意愿量,交叉保险意愿量,交叉自动绑定账单分期量,交叉EPP批核金额（3期）,交叉EPP批核金额（6期）,交叉EPP批核金额（12期）,交叉EPP批核金额（18期）,交叉EPP批核金额（24期）,交叉EPP批核金额（36期）,交叉账单批核金额（3期）,交叉账单批核金额（6期）,交叉账单批核金额（12期）,交叉账单批核金额（18期）,交叉账单批核金额（24期）,交叉账单批核金额（36期）,交叉EPPC批核金额（3期）,交叉EPPC批核金额（6期）,交叉EPPC批核金额（12期）,交叉EPPC批核金额（18期）,交叉EPPC批核金额（24期）,交叉EPPC批核金额（36期）,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额,交叉大额EPPC批核金额\r\n");
//				for (CrossMarketingReport report : specificDataList){
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
				fileUtil.createFile(specificPath, "BPS-交叉营销成效", headers, specificDataList, reportFields);
			}
			
			reportInfo.setZhixingTime(format.format(new Date()));
			reportInfo.setState("已执行");
			bpsRwHistoryDao.updateDefinedReportInfo("updateDefinedReportInfo", reportInfo);
		}catch (Exception e) {
			e.printStackTrace();
			logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  生成"+reportInfo.getStartTime()+"~"+reportInfo.getEndTime()+"自定义交叉营销成效报表错误===>  "+e.toString());
			return false;
		}
		return true;
	}

}
