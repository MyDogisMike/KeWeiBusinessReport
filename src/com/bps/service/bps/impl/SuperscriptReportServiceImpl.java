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
import com.bps.dal.dao.bps.SuperscriptReportDao;
import com.bps.dal.object.bps.BpsUserInfo;
import com.bps.dal.object.bps.DefinedReportInfo;
import com.bps.dal.object.bps.SuperscriptReport;
import com.bps.exception.BaseException;
import com.bps.service.bps.SuperscriptReportService;
import com.bps.util.DateUtil;
import com.bps.util.FileUtil;
import com.bps.util.RedisUtil;

public class SuperscriptReportServiceImpl implements SuperscriptReportService{
	private static final Logger logger = Logger.getLogger("logs");
	@Resource
	private BpsRwHistoryDao bpsRwHistoryDao;
	@Resource
	private SuperscriptReportDao superscriptReportDao;
	@Autowired
	private RedisUtil redisUtil;
	private Map<String, String> centerMap = null;
	private Map<String, Map<String, String>> centerGroupMap = null;
	private final String headersStr = "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,A标（主营）,A标（交叉EPP）,A标（交叉账单分期）,A标（交叉EPPC）,A标（交叉大额EPPC）,B标（主营）,B标（交叉EPP）,B标（交叉账单分期）,B标（交叉EPPC）,B标（交叉大额EPPC）,F标,F00（主营）,F00（交叉EPP）,F00（交叉账单分期）,F00（交叉EPPC）,F00（交叉大额EPPC）,F01（主营）,F01（交叉EPP）,F01（交叉账单分期）,F01（交叉EPPC）,F01（交叉大额EPPC）,F02,F02（交叉EPP）,F02（交叉账单分期）,F02（交叉EPPC）,F02（交叉大额EPPC）,G01（主营）,G01（交叉EPP）,G01（交叉账单分期）,G01（交叉EPPC）,G01（交叉大额EPPC）,G02（主营）,G02（交叉EPP）,G02（交叉账单分期）,G02（交叉EPPC）,G02（交叉大额EPPC）,M标（主营）,M标（交叉EPP）,M标（交叉账单分期）,M标（交叉EPPC）,M标（交叉大额EPPC）,待跟进数据量,未正常结案过期量,未外呼数据量";
	
	@Override
	public boolean createReport(String beginTime, String endTime, String saveUrl, String reportName)
			throws BaseException {
		String beginDate = beginTime.split(" ")[0];
		String endDate = endTime.split(" ")[0];
		String dateStr = beginDate;
		if(!beginDate.equals(endDate)){
			dateStr = beginDate + "~" + endDate;
		}
		reportName += "_"+dateStr.trim()+".xls";
		String realReportName = "SuperscriptReport_"+dateStr.trim()+".xls";
		System.out.println(dateStr+"每日上标及跟进库存报表生成开始"+DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
		
		try{
			if(centerMap == null){
				centerMap = (Map<String, String>) redisUtil.getJedis().hgetAll(RedisUtil.BPS_CENTER);
			}
			if(centerGroupMap == null) {
				centerGroupMap = (Map<String, Map<String, String>>) RedisUtil.deserialize(redisUtil.getJedis().get(RedisUtil.BPS_GROUP.getBytes()));
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Map<String, List<SuperscriptReport>> dataMap = new HashMap<String, List<SuperscriptReport>>();
			paramMap.put("beginTime", beginTime);
			paramMap.put("endTime", endTime);
			paramMap.put("skipRow", -1);	//不分页查询所有数据
			//获取有数据派发的用户名List
			List<String> haveDataUser = bpsRwHistoryDao.getHaveDataUser("getHaveDataUser", paramMap);
			List<BpsUserInfo> userList = bpsRwHistoryDao.getUserInfoByTime("getUserInfoByTime", paramMap);
			for(BpsUserInfo user : userList){
				String centerId = user.getCenterId();
				String groupId = user.getGroupId();
				
				List<SuperscriptReport> dataList = null;
				if(centerGroupMap.get(centerId).get(groupId) != null){	//表示有组别信息，否则只有中心信息
					dataList = dataMap.get(groupId);	//获取该小组的list
				}else{
					dataList = dataMap.get(centerId);	//没有小组信息的就获取该中心的list
				}
				if(dataList == null) dataList = new ArrayList<SuperscriptReport>();
				
				//判断该用户是否在haveDataUser中，如果不在则表示没有数据，则所有数据为0；
				String userName = user.getUserName();
				boolean dataFlag = false;
				for(String dataUser : haveDataUser) {
					if(dataUser.equalsIgnoreCase(userName)) {
						dataFlag = true;
						break;
					}
				}
				List<SuperscriptReport> tempDataList = getUserSuperscriptReport(user, paramMap, dataFlag);
				if(tempDataList.size() > 0){
					dataList.addAll(tempDataList);
					if(centerGroupMap.get(centerId).get(groupId) != null){	//表示有组别信息，否则只有中心信息
						dataMap.put(groupId, dataList);	//因为中心和小组信息共用一张表，所以两者的id（主键）不可能有相同的情况
					}else{	//将没有小组信息的数据保存进中心数据的List
						dataMap.put(centerId, dataList);
					}
				}
			}//遍历用户for循环
			
			SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ReportSaveObj reportSave = new ReportSaveObj("BPS-每日上标及跟进库存");	//文件保存对象
			reportSave.setRealFileName(realReportName);
//			StringBuffer allStrBuf = new StringBuffer();	//所有的数据
			List<SuperscriptReport> allDataList = new ArrayList<SuperscriptReport>();
			FileUtil<SuperscriptReport> fileUtil = new FileUtil<SuperscriptReport>();
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
			reportFields.add("bidAMainBusi");
			reportFields.add("bidACrossEPP");
			reportFields.add("bidACrossBill");
			reportFields.add("bidACrossEPPC");
			reportFields.add("bidACrossBigEPPC");
			reportFields.add("bidBMainBusi");
			reportFields.add("bidBCrossEPP");
			reportFields.add("bidBCrossBill");
			reportFields.add("bidBCrossEPPC");
			reportFields.add("bidBCrossBigEPPC");
			reportFields.add("bidF");
			reportFields.add("bidF00MainBusi");
			reportFields.add("bidF00CrossEPP");
			reportFields.add("bidF00CrossBill");
			reportFields.add("bidF00CrossEPPC");
			reportFields.add("bidF00CrossBigEPPC");
			reportFields.add("bidF01MainBusi");
			reportFields.add("bidF01CrossEPP");
			reportFields.add("bidF01CrossBill");
			reportFields.add("bidF01CrossEPPC");
			reportFields.add("bidF01CrossBigEPPC");
			reportFields.add("bidF02MainBusi");
			reportFields.add("bidF02CrossEPP");
			reportFields.add("bidF02CrossBill");
			reportFields.add("bidF02CrossEPPC");
			reportFields.add("bidF02CrossBigEPPC");
			reportFields.add("bidG01MainBusi");
			reportFields.add("bidG01CrossEPP");
			reportFields.add("bidG01CrossBill");
			reportFields.add("bidG01CrossEPPC");
			reportFields.add("bidG01CrossBigEPPC");
			reportFields.add("bidG02MainBusi");
			reportFields.add("bidG02CrossEPP");
			reportFields.add("bidG02CrossBill");
			reportFields.add("bidG02CrossEPPC");
			reportFields.add("bidG02CrossBigEPPC");
			reportFields.add("bidMMainBusi");
			reportFields.add("bidMCrossEPP");
			reportFields.add("bidMCrossBill");
			reportFields.add("bidMCrossEPPC");
			reportFields.add("bidMCrossBigEPPC");
			reportFields.add("dgjDataNum");
			reportFields.add("gqDataNum");
			reportFields.add("wwhDataNum");
			
			long allFileNum = 0L;
			for(String centerId : centerMap.keySet()){
//				StringBuffer centerStrBuf = new StringBuffer();	//中心的数据
				List<SuperscriptReport> centerDataList = new ArrayList<SuperscriptReport>();
				long centerFileNum = 0L;
				for(String groupId : centerGroupMap.get(centerId).keySet()){
//					StringBuffer groupStrBuf = new StringBuffer();	//小组的数据
					long groupFileNum = 0L;
					List<SuperscriptReport> groupList = dataMap.get(groupId);
					if(groupList != null){
						groupFileNum += groupList.size();
						centerDataList.addAll(groupList);
						centerFileNum += groupList.size();
//						for(SuperscriptReport report : groupList){
//							groupStrBuf.append(report.toString()+"\r\n");
//							groupFileNum++;
//						}
//						centerStrBuf.append(groupStrBuf.toString());
//						centerFileNum += groupFileNum;
					}else{
						groupList = new ArrayList<SuperscriptReport>();
					}
//					groupStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,A标（主营）,A标（交叉EPP）,A标（交叉账单分期）,A标（交叉EPPC）,A标（交叉大额EPPC）,B标（主营）,B标（交叉EPP）,B标（交叉账单分期）,B标（交叉EPPC）,B标（交叉大额EPPC）,F标,F00（主营）,F00（交叉EPP）,F00（交叉账单分期）,F00（交叉EPPC）,F00（交叉大额EPPC）,F01（主营）,F01（交叉EPP）,F01（交叉账单分期）,F01（交叉EPPC）,F01（交叉大额EPPC）,F02,F02（交叉EPP）,F02（交叉账单分期）,F02（交叉EPPC）,F02（交叉大额EPPC）,G01（主营）,G01（交叉EPP）,G01（交叉账单分期）,G01（交叉EPPC）,G01（交叉大额EPPC）,G02（主营）,G02（交叉EPP）,G02（交叉账单分期）,G02（交叉EPPC）,G02（交叉大额EPPC）,M标（主营）,M标（交叉EPP）,M标（交叉账单分期）,M标（交叉EPPC）,M标（交叉大额EPPC）,待跟进数据量,未正常结案过期量,未外呼数据量\r\n");
					String groupPath = saveUrl+dateStr+File.separator+centerId.trim()+File.separator+groupId.trim()+File.separator;
					String totalPath = groupPath + realReportName;
					//System.out.println(groupPath);
					File groupFile = new File(totalPath);
					File groupPathFile = groupFile.getParentFile();
					if(!groupPathFile.exists()){
						groupPathFile.mkdirs();
					}
//					FileUtil.createFile(totalPath, groupStrBuf.toString());
					fileUtil.createFile(totalPath, "BPS-每日上标及跟进库存", headers, groupList, reportFields);

					reportSave.setFileName(reportName+"&"+centerId+"_"+groupId);
					reportSave.setFileNum(groupFileNum);
					reportSave.setFilePath(groupPath);
					reportSave.setTime(format.format(new Date()));
					bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);
				}
				List<SuperscriptReport> centerList = dataMap.get(centerId);
				if(centerList != null){
					centerDataList.addAll(centerList);
					centerFileNum += centerList.size();
//					for(SuperscriptReport report : centerList){
//						centerStrBuf.append(report.toString()+"\r\n");
//						centerFileNum++;
//					}
				}
				allDataList.addAll(centerDataList);
//				allStrBuf.append(centerStrBuf.toString());
				allFileNum += centerFileNum;
//				centerStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,A标（主营）,A标（交叉EPP）,A标（交叉账单分期）,A标（交叉EPPC）,A标（交叉大额EPPC）,B标（主营）,B标（交叉EPP）,B标（交叉账单分期）,B标（交叉EPPC）,B标（交叉大额EPPC）,F标,F00（主营）,F00（交叉EPP）,F00（交叉账单分期）,F00（交叉EPPC）,F00（交叉大额EPPC）,F01（主营）,F01（交叉EPP）,F01（交叉账单分期）,F01（交叉EPPC）,F01（交叉大额EPPC）,F02,F02（交叉EPP）,F02（交叉账单分期）,F02（交叉EPPC）,F02（交叉大额EPPC）,G01（主营）,G01（交叉EPP）,G01（交叉账单分期）,G01（交叉EPPC）,G01（交叉大额EPPC）,G02（主营）,G02（交叉EPP）,G02（交叉账单分期）,G02（交叉EPPC）,G02（交叉大额EPPC）,M标（主营）,M标（交叉EPP）,M标（交叉账单分期）,M标（交叉EPPC）,M标（交叉大额EPPC）,待跟进数据量,未正常结案过期量,未外呼数据量\r\n");
				String centerPath = saveUrl+dateStr+File.separator+centerId.trim()+File.separator;
				String totalPath = centerPath + realReportName;
				File centerFile = new File(totalPath);
				File centerPathFile = centerFile.getParentFile();
				if(!centerPathFile.exists()){
					centerPathFile.mkdirs();
				}
//				FileUtil.createFile(totalPath, centerStrBuf.toString());
				fileUtil.createFile(totalPath, "BPS-每日上标及跟进库存", headers, centerDataList, reportFields);
				
				reportSave.setFileName(reportName+"&"+centerId);
				reportSave.setFileNum(centerFileNum);
				reportSave.setFilePath(centerPath);
				reportSave.setTime(format.format(new Date()));
				bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);
			}
//			allStrBuf.insert(0, "开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,A标（主营）,A标（交叉EPP）,A标（交叉账单分期）,A标（交叉EPPC）,A标（交叉大额EPPC）,B标（主营）,B标（交叉EPP）,B标（交叉账单分期）,B标（交叉EPPC）,B标（交叉大额EPPC）,F标,F00（主营）,F00（交叉EPP）,F00（交叉账单分期）,F00（交叉EPPC）,F00（交叉大额EPPC）,F01（主营）,F01（交叉EPP）,F01（交叉账单分期）,F01（交叉EPPC）,F01（交叉大额EPPC）,F02,F02（交叉EPP）,F02（交叉账单分期）,F02（交叉EPPC）,F02（交叉大额EPPC）,G01（主营）,G01（交叉EPP）,G01（交叉账单分期）,G01（交叉EPPC）,G01（交叉大额EPPC）,G02（主营）,G02（交叉EPP）,G02（交叉账单分期）,G02（交叉EPPC）,G02（交叉大额EPPC）,M标（主营）,M标（交叉EPP）,M标（交叉账单分期）,M标（交叉EPPC）,M标（交叉大额EPPC）,待跟进数据量,未正常结案过期量,未外呼数据量\r\n");
			String allPath = saveUrl+dateStr+File.separator;
			String totalPath = allPath + realReportName;
			File allFile = new File(totalPath);
			File allPathFile = allFile.getParentFile();
			if(!allPathFile.exists()){
				allPathFile.mkdirs();
			}
//			FileUtil.createFile(totalPath, allStrBuf.toString());
			fileUtil.createFile(totalPath, "BPS-每日上标及跟进库存", headers, allDataList, reportFields);
			
			reportSave.setFileName(reportName+"&");
			reportSave.setFileNum(allFileNum);
			reportSave.setFilePath(allPath);
			reportSave.setTime(format.format(new Date()));
			bpsRwHistoryDao.insertReportSave("insertReportSave", reportSave);
			System.out.println(dateStr+"每日上标及跟进库存报表生成结束"+DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss"));
		}catch (Exception e) {
			e.printStackTrace();
			logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  生成"+dateStr+"每日上标及跟进库存报表错误===>  "+e.toString());
			return false;
		}
		return true;
	}

	@Override
	public List<SuperscriptReport> getUserSuperscriptReport(BpsUserInfo user, Map<String, Object> paramMap, boolean dataFlag) {
		String[] ywTypeArr = {"EPP", "账单分期", "大额EPPC", "EPPC", "备用金"};
		List<SuperscriptReport> tempDataList = new ArrayList<SuperscriptReport>();
		
		SuperscriptReport parentRepor = new SuperscriptReport(user);
		
		String centerId = user.getCenterId();
		String groupId = user.getGroupId();
		parentRepor.setCenter(centerMap.get(centerId));
		parentRepor.setGroup(centerGroupMap.get(centerId).get(groupId));
		
		parentRepor.setBeginTime((String)paramMap.get("beginTime"));
		parentRepor.setEndTime((String)paramMap.get("endTime"));
		
		for (String ywType : ywTypeArr){
			SuperscriptReport report = (SuperscriptReport) parentRepor.clone();
			
			report.setYwType(ywType);
			//如果该用户不存在数据派发,不进行数据库查询直接赋默认值
			if(!dataFlag){
				report.setBidACrossBigEPPC(0L);
				report.setBidACrossBill(0L);
				report.setBidACrossEPP(0L);
				report.setBidACrossEPPC(0L);
				report.setBidAMainBusi(0L);
				report.setBidBCrossBigEPPC(0L);
				report.setBidBCrossBill(0L);
				report.setBidBCrossEPP(0L);
				report.setBidBCrossEPPC(0L);
				report.setBidBMainBusi(0L);
				report.setBidF(0L);
				report.setBidF00CrossBigEPPC(0L);
				report.setBidF00CrossBill(0L);
				report.setBidF00CrossEPP(0L);
				report.setBidF00CrossEPPC(0L);
				report.setBidF00MainBusi(0L);
				report.setBidF01CrossBigEPPC(0L);
				report.setBidF01CrossBill(0L);
				report.setBidF01CrossEPP(0L);
				report.setBidF01CrossEPPC(0L);
				report.setBidF01MainBusi(0L);
				report.setBidF02CrossBigEPPC(0L);
				report.setBidF02CrossBill(0L);
				report.setBidF02CrossEPP(0L);
				report.setBidG02CrossEPPC(0L);
				report.setBidG02MainBusi(0L);
				report.setBidG01CrossBigEPPC(0L);
				report.setBidG01CrossBill(0L);
				report.setBidG01CrossEPP(0L);
				report.setBidG01CrossEPPC(0L);
				report.setBidG01MainBusi(0L);
				report.setBidG02CrossBigEPPC(0L);
				report.setBidG02CrossBill(0L);
				report.setBidG02CrossEPP(0L);
				report.setBidG02CrossEPPC(0L);
				report.setBidG02MainBusi(0L);
				report.setBidMCrossBigEPPC(0L);
				report.setBidMCrossBill(0L);
				report.setBidMCrossEPP(0L);
				report.setBidMCrossEPPC(0L);
				report.setBidMMainBusi(0L);
				report.setDgjDataNum(0L);
				report.setGqDataNum(0L);
				report.setWhDateNum(0L);
				report.setWwhDataNum(0L);
			}else{
				String userName = user.getUserName();
				paramMap.put("userName", userName);
				paramMap.put("ywType", ywType);
				report.setWhDateNum(bpsRwHistoryDao.getWhDateNum("getWhDateNum", paramMap));
				report.setDgjDataNum(superscriptReportDao.getDgjDataNum("getDgjDataNum", paramMap));
				report.setGqDataNum(superscriptReportDao.getGqDataNum("getGqDataNum", paramMap));
				report.setWwhDataNum(superscriptReportDao.getWwhDataNum("getWwhDataNum", paramMap));
				
				//查询A标的交叉数据
				paramMap.put("bidType", "A");
				report.setBidAMainBusi(superscriptReportDao.getBidMainBusi("getBidMainBusi", paramMap));
				paramMap.put("dataType", "EPP");
				report.setBidACrossEPP(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "账单分期");
				report.setBidACrossBill(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "大额EPPC");
				report.setBidACrossBigEPPC(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "EPPC");
				report.setBidACrossEPPC(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				//查询B标的交叉数据
				paramMap.put("bidType", "B");
				report.setBidBMainBusi(superscriptReportDao.getBidMainBusi("getBidMainBusi", paramMap));
				paramMap.put("dataType", "EPP");
				report.setBidBCrossEPP(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "账单分期");
				report.setBidBCrossBill(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "大额EPPC");
				report.setBidBCrossBigEPPC(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "EPPC");
				report.setBidBCrossEPPC(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				//查询F标的数据
				report.setBidF(superscriptReportDao.getBidFData("getBidFData", paramMap));
				
				//查询F00标的交叉数据
				paramMap.put("bidType", "F00");
				report.setBidF00MainBusi(superscriptReportDao.getBidMainBusi("getBidMainBusi", paramMap));
				paramMap.put("dataType", "EPP");
				report.setBidF00CrossEPP(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "账单分期");
				report.setBidF00CrossBill(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "大额EPPC");
				report.setBidF00CrossBigEPPC(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "EPPC");
				report.setBidF00CrossEPPC(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				//查询F01标的交叉数据
				paramMap.put("bidType", "F01");
				report.setBidF01MainBusi(superscriptReportDao.getBidMainBusi("getBidMainBusi", paramMap));
				paramMap.put("dataType", "EPP");
				report.setBidF01CrossEPP(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "账单分期");
				report.setBidF01CrossBill(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "大额EPPC");
				report.setBidF01CrossBigEPPC(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "EPPC");
				report.setBidF01CrossEPPC(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				//查询F02标的交叉数据
				paramMap.put("bidType", "F02");
				report.setBidF02MainBusi(superscriptReportDao.getBidMainBusi("getBidMainBusi", paramMap));
				paramMap.put("dataType", "EPP");
				report.setBidF02CrossEPP(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "账单分期");
				report.setBidF02CrossBill(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "大额EPPC");
				report.setBidF02CrossBigEPPC(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "EPPC");
				report.setBidF02CrossEPPC(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				//查询G01标的交叉数据
				paramMap.put("bidType", "G01");
				report.setBidG01MainBusi(superscriptReportDao.getBidMainBusi("getBidMainBusi", paramMap));
				paramMap.put("dataType", "EPP");
				report.setBidG01CrossEPP(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "账单分期");
				report.setBidG01CrossBill(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "大额EPPC");
				report.setBidG01CrossBigEPPC(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "EPPC");
				report.setBidG01CrossEPPC(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				//查询G02标的交叉数据
				paramMap.put("bidType", "G02");
				report.setBidG02MainBusi(superscriptReportDao.getBidMainBusi("getBidMainBusi", paramMap));
				paramMap.put("dataType", "EPP");
				report.setBidG02CrossEPP(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "账单分期");
				report.setBidG02CrossBill(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "大额EPPC");
				report.setBidG02CrossBigEPPC(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "EPPC");
				report.setBidG02CrossEPPC(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				//查询M标的交叉数据
				paramMap.put("bidType", "M");
				report.setBidMMainBusi(superscriptReportDao.getBidMainBusi("getBidMainBusi", paramMap));
				paramMap.put("dataType", "EPP");
				report.setBidMCrossEPP(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "账单分期");
				report.setBidMCrossBill(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "大额EPPC");
				report.setBidMCrossBigEPPC(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				paramMap.put("dataType", "EPPC");
				report.setBidMCrossEPPC(superscriptReportDao.getBidCrossData("getBidCrossData", paramMap));
				
				if("EPP".equals(ywType)){
					report.setBidACrossEPP(0L);
					report.setBidBCrossEPP(0L);
					report.setBidF00CrossEPP(0L);
					report.setBidF01CrossEPP(0L);
					report.setBidF02CrossEPP(0L);
					report.setBidG01CrossEPP(0L);
					report.setBidG02CrossEPP(0L);
					report.setBidMCrossEPP(0L);
				}else if("账单分期".equals(ywType)){
					report.setBidACrossBill(0L);
					report.setBidBCrossBill(0L);
					report.setBidF00CrossBill(0L);
					report.setBidF01CrossBill(0L);
					report.setBidF02CrossBill(0L);
					report.setBidG01CrossBill(0L);
					report.setBidG02CrossBill(0L);
					report.setBidMCrossBill(0L);
				}else if("EPPC".equals(ywType) || "备用金".equals(ywType)){
					report.setBidACrossEPPC(0L);
					report.setBidBCrossEPPC(0L);
					report.setBidF00CrossEPPC(0L);
					report.setBidF01CrossEPPC(0L);
					report.setBidF02CrossEPPC(0L);
					report.setBidG01CrossEPPC(0L);
					report.setBidG02CrossEPPC(0L);
					report.setBidMCrossEPPC(0L);
				}else if("大额EPPC".equals(ywType)){
					report.setBidACrossBigEPPC(0L);
					report.setBidBCrossBigEPPC(0L);
					report.setBidF00CrossBigEPPC(0L);
					report.setBidF01CrossBigEPPC(0L);
					report.setBidF02CrossBigEPPC(0L);
					report.setBidG01CrossBigEPPC(0L);
					report.setBidG02CrossBigEPPC(0L);
					report.setBidMCrossBigEPPC(0L);
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
		paramMap.put("skipRow", -1);	//不分页查询所有数据
		String centerText = reportInfo.getZhongxin();
		//设置生成的报表名称 例如：BPS-新数据派发及成效_20180605 09:00~20180608 12:00_广四中心_一组.xls
		String beginTime = reportInfo.getStartTime().replaceAll("-", "").substring(0, 14);
		String endTime = reportInfo.getEndTime().replaceAll("-", "").substring(0, 14);
		beginTime = beginTime.replaceAll(" ", "_");
		endTime = endTime.replaceAll(" ", "_");
		beginTime = beginTime.replaceAll(":", "");
		endTime = endTime.replaceAll(":", "");
		String prefix = "BPS-每日上标及跟进库存_"+beginTime+"~"+endTime;
		String realPreFix = "BPS-SuperscriptReport_"+beginTime+"~"+endTime;
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
			List<SuperscriptReport> dataList = new ArrayList<SuperscriptReport>();
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
				List<SuperscriptReport> tempDataList = getUserSuperscriptReport(user, paramMap, dataFlag);
				dataList.addAll(tempDataList);
			}
			
			SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			StringBuffer dataStrBuf = new StringBuffer();
//			long fileNum = 0;
//			dataStrBuf.append("开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,A标（主营）,A标（交叉EPP）,A标（交叉账单分期）,A标（交叉EPPC）,A标（交叉大额EPPC）,B标（主营）,B标（交叉EPP）,B标（交叉账单分期）,B标（交叉EPPC）,B标（交叉大额EPPC）,F标,F00（主营）,F00（交叉EPP）,F00（交叉账单分期）,F00（交叉EPPC）,F00（交叉大额EPPC）,F01（主营）,F01（交叉EPP）,F01（交叉账单分期）,F01（交叉EPPC）,F01（交叉大额EPPC）,F02,F02（交叉EPP）,F02（交叉账单分期）,F02（交叉EPPC）,F02（交叉大额EPPC）,G01（主营）,G01（交叉EPP）,G01（交叉账单分期）,G01（交叉EPPC）,G01（交叉大额EPPC）,G02（主营）,G02（交叉EPP）,G02（交叉账单分期）,G02（交叉EPPC）,G02（交叉大额EPPC）,M标（主营）,M标（交叉EPP）,M标（交叉账单分期）,M标（交叉EPPC）,M标（交叉大额EPPC）,待跟进数据量,未正常结案过期量,未外呼数据量\r\n");
//			for (SuperscriptReport report : dataList){
//				dataStrBuf.append(report.toString()+"\r\n");
//				fileNum++;
//			}
			FileUtil<SuperscriptReport> fileUtil = new FileUtil<SuperscriptReport>();
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
			reportFields.add("bidAMainBusi");
			reportFields.add("bidACrossEPP");
			reportFields.add("bidACrossBill");
			reportFields.add("bidACrossEPPC");
			reportFields.add("bidACrossBigEPPC");
			reportFields.add("bidBMainBusi");
			reportFields.add("bidBCrossEPP");
			reportFields.add("bidBCrossBill");
			reportFields.add("bidBCrossEPPC");
			reportFields.add("bidBCrossBigEPPC");
			reportFields.add("bidF");
			reportFields.add("bidF00MainBusi");
			reportFields.add("bidF00CrossEPP");
			reportFields.add("bidF00CrossBill");
			reportFields.add("bidF00CrossEPPC");
			reportFields.add("bidF00CrossBigEPPC");
			reportFields.add("bidF01MainBusi");
			reportFields.add("bidF01CrossEPP");
			reportFields.add("bidF01CrossBill");
			reportFields.add("bidF01CrossEPPC");
			reportFields.add("bidF01CrossBigEPPC");
			reportFields.add("bidF02MainBusi");
			reportFields.add("bidF02CrossEPP");
			reportFields.add("bidF02CrossBill");
			reportFields.add("bidF02CrossEPPC");
			reportFields.add("bidF02CrossBigEPPC");
			reportFields.add("bidG01MainBusi");
			reportFields.add("bidG01CrossEPP");
			reportFields.add("bidG01CrossBill");
			reportFields.add("bidG01CrossEPPC");
			reportFields.add("bidG01CrossBigEPPC");
			reportFields.add("bidG02MainBusi");
			reportFields.add("bidG02CrossEPP");
			reportFields.add("bidG02CrossBill");
			reportFields.add("bidG02CrossEPPC");
			reportFields.add("bidG02CrossBigEPPC");
			reportFields.add("bidMMainBusi");
			reportFields.add("bidMCrossEPP");
			reportFields.add("bidMCrossBill");
			reportFields.add("bidMCrossEPPC");
			reportFields.add("bidMCrossBigEPPC");
			reportFields.add("dgjDataNum");
			reportFields.add("gqDataNum");
			reportFields.add("wwhDataNum");
			
			
			String path = saveUrl+realReportName;
			File file = new File(path);
			File pathFile = file.getParentFile();
			if(!pathFile.exists()){
				pathFile.mkdirs();
			}
//			FileUtil.createFile(path, dataStrBuf.toString());
			fileUtil.createFile(path, "BPS-每日上标及跟进库存", headers, dataList, reportFields);
			
			//是否生成营销员报表
			if("是".equals(reportInfo.getYxyData())){
				paramMap.put("userName", reportInfo.getAssignName());
				String specificReportName = prefix + "_" +reportInfo.getAssignName() + ".xls";
				reportInfo.setFileyxyName(specificReportName);
				String realSpecificReportName = realPreFix + "_" +reportInfo.getAssignName() + ".xls";
				reportInfo.setFileyxyRealname(realSpecificReportName);
				List<BpsUserInfo> specificUserList = bpsRwHistoryDao.getUserInfoByTime("getUserInfoByTime", paramMap);
				
				List<SuperscriptReport> specificDataList = new ArrayList<SuperscriptReport>();
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
					List<SuperscriptReport> tempDataList = getUserSuperscriptReport(user, paramMap, dataFlag);
					specificDataList.addAll(tempDataList);
				}
				
//				StringBuffer specificDataStrBuf = new StringBuffer();
//				long specificFileNum = 0;
//				specificDataStrBuf.append("开始时间,结束时间,所属中心,所属组别,座席工号,座席姓名,数据业务类型,外呼数据量,A标（主营）,A标（交叉EPP）,A标（交叉账单分期）,A标（交叉EPPC）,A标（交叉大额EPPC）,B标（主营）,B标（交叉EPP）,B标（交叉账单分期）,B标（交叉EPPC）,B标（交叉大额EPPC）,F标,F00（主营）,F00（交叉EPP）,F00（交叉账单分期）,F00（交叉EPPC）,F00（交叉大额EPPC）,F01（主营）,F01（交叉EPP）,F01（交叉账单分期）,F01（交叉EPPC）,F01（交叉大额EPPC）,F02,F02（交叉EPP）,F02（交叉账单分期）,F02（交叉EPPC）,F02（交叉大额EPPC）,G01（主营）,G01（交叉EPP）,G01（交叉账单分期）,G01（交叉EPPC）,G01（交叉大额EPPC）,G02（主营）,G02（交叉EPP）,G02（交叉账单分期）,G02（交叉EPPC）,G02（交叉大额EPPC）,M标（主营）,M标（交叉EPP）,M标（交叉账单分期）,M标（交叉EPPC）,M标（交叉大额EPPC）,待跟进数据量,未正常结案过期量,未外呼数据量\r\n");
//				for (SuperscriptReport report : specificDataList){
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
				fileUtil.createFile(specificPath, "BPS-每日上标及跟进库存", headers, specificDataList, reportFields);
			}
			
			reportInfo.setZhixingTime(format.format(new Date()));
			reportInfo.setState("已执行");
			bpsRwHistoryDao.updateDefinedReportInfo("updateDefinedReportInfo", reportInfo);
		}catch (Exception e) {
			e.printStackTrace();
			logger.info(DateUtil.getNowDate("yyyy-MM-dd HH:mm:ss")+"  生成"+reportInfo.getStartTime()+"~"+reportInfo.getEndTime()+"自定义每日上标及跟进库存报表错误===>  "+e.toString());
			return false;
		}
		return true;
	}

}
