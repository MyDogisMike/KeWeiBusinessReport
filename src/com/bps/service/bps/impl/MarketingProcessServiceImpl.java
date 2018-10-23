package com.bps.service.bps.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.bps.dal.dao.bps.BpsRwHistoryDao;
import com.bps.dal.dao.bps.MarketingProcessDao;
import com.bps.dal.dao.hw.TNottollUniversalDao;
import com.bps.dal.object.PageResult;
import com.bps.dal.object.QueryParams;
import com.bps.dal.object.bps.BpsUserInfo;
import com.bps.dal.object.bps.MarketingProcess;
import com.bps.service.bps.MarketingProcessService;
import com.bps.util.ExcelExport;
import com.bps.util.RedisUtil;

public class MarketingProcessServiceImpl implements MarketingProcessService{
	@Resource
	private BpsRwHistoryDao bpsRwHistoryDao;
	@Resource
	private MarketingProcessDao marketingProcessDao;
	@Resource
	private TNottollUniversalDao tNottollUniversalDao;
	@Autowired
	private RedisUtil redisUtil;

	@Override
	public PageResult<MarketingProcess> getMarketingProcess(QueryParams params) {
		//中心和小组Map
		Map<String, String> centerMap = (Map<String, String>) redisUtil.getJedis().hgetAll(RedisUtil.BPS_CENTER);;
		Map<String, Map<String, String>> centerGroupMap = (Map<String, Map<String, String>>) RedisUtil.deserialize(redisUtil.getJedis().get(RedisUtil.BPS_GROUP.getBytes()));
		String[] ywTypeArr = {"EPP", "账单分期", "大额EPPC", "EPPC", "EPPC备用金"};
		PageResult<MarketingProcess> result = new PageResult<MarketingProcess>();
		List<MarketingProcess> dataList = new ArrayList<MarketingProcess>();
		Map<String, Object> userParam = new HashMap<String, Object>();
		Map<String, Object> dataParam = new HashMap<String, Object>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		userParam.put("center", params.getCenter());
		userParam.put("group", params.getGroup());
		userParam.put("userName", params.getUserName());
		userParam.put("beginTime", format.format(new Date())+" 00:00:00");
		userParam.put("endTime", userParam.get("beginTime")+" 23:59:59");
		userParam.put("rows", params.getRows());
		userParam.put("skipRow", params.getSkipRow());
		//获取有数据派发的用户名List
		List<String> haveDataUser = bpsRwHistoryDao.getHaveDataUser("getHaveDataUser", userParam);
		List<BpsUserInfo> userList = bpsRwHistoryDao.getUserInfoByTime("getUserInfoByTime", userParam);
		Long count = bpsRwHistoryDao.getCountUserInfoByTime("getCountUserInfoByTime", userParam);
		if(count == null) count = 0L;
		for (BpsUserInfo user : userList){
			dataParam.put("userName", user.getUserName());
			
			//先从Redis中获取中心和组别信息，如果没有则从数据库中获取再保存到Redis中
			String centerId = user.getCenterId();
			String groupId = user.getGroupId();
			
			String centerText = centerMap.get(centerId);
			if(centerText == null){
				centerText = bpsRwHistoryDao.getTextById("getTextById", centerId);
				centerMap.put(centerId, centerText);
				Map<String, String> groupMap = new HashMap<String, String>();
				centerGroupMap.put(centerId, groupMap);
			}
			String groupText = centerGroupMap.get(centerId).get(groupId);
			if(groupText == null){
				groupText = bpsRwHistoryDao.getTextById("getTextById", groupId);
				Map<String, String> groupMap = centerGroupMap.get(centerId);
				groupMap.put(groupId, groupText);
				centerGroupMap.put(centerId, groupMap);
			}
			
			//判断该用户是否在haveDataUser中，如果不在则表示没有数据，则所有数据为0；
			boolean dataFlag = haveDataUser.contains(user.getUserName());
			
			for (String ywType : ywTypeArr){
				if(!"".equals(params.getYwType()) && !params.getYwType().equals(ywType)){
					continue;
				}
				MarketingProcess obj = new MarketingProcess(user);
				dataParam.put("ywType", ywType);
				obj.setYwType(ywType);
				
				obj.setCenter(centerText);
				obj.setGroup(groupText);
				
				//如果该用户不存在数据派发,不进行数据库查询直接赋默认值
				if(!dataFlag){
					obj.setCommunicateTotleTime(0L);
					obj.setSuccessAcceptNum(0L);
					obj.setSuccessApproveMoney(0.0);
					obj.setWhDataNum(0L);
				}else{
					obj.setWhDataNum(marketingProcessDao.getTodayWhDateNum("getTodayWhDateNum", dataParam));
					obj.setSuccessAcceptNum(marketingProcessDao.getSuccessAcceptNum("getSuccessAcceptNum", dataParam));
					Double moneyA = marketingProcessDao.getTodaySuccessApproveMoneyA("getTodaySuccessApproveMoneyA", dataParam);
					Double moneyB = marketingProcessDao.getTodaySuccessApproveMoneyB("getTodaySuccessApproveMoneyB", dataParam);
					if(moneyA == null) moneyA = 0.00;
					if(moneyB == null) moneyB = 0.00;
					obj.setSuccessApproveMoney(moneyA + moneyB);
					List<String> recordList = marketingProcessDao.getTodayRecordId("getTodayRecordId", dataParam);
					if(recordList.size() < 1){//如果没有录音id，则时长设为0
						obj.setCommunicateTotleTime(0L);
					}else{
						dataParam.put("recordList", recordList);
						Long unConnectTotalTime = tNottollUniversalDao.getTodayTotalCallsTime("getTodayTotalCallsTime", dataParam);
						if(unConnectTotalTime == null) {//没有总时长则为0
							obj.setCommunicateTotleTime(0L);
						}else{
							//话后满意度时长
							Long satisfactionTime = marketingProcessDao.getAfterSatisfactionTime("getAfterSatisfactionTime", dataParam);
							if(satisfactionTime == null) satisfactionTime = 0L;
							obj.setCommunicateTotleTime(unConnectTotalTime - satisfactionTime);
						}
					}
				}
				
				
				
				dataList.add(obj);
			}
			
		}
		result.setRows(dataList);
		result.setTotal(count);
		return result;
	}

	@Override
	public void exportMarketingProcessExcel(QueryParams params,
			HttpServletResponse response) {
		PageResult<MarketingProcess> result = getMarketingProcess(params);
		
		String[] headers = { "所属中心", "数据业务类型","组别", "营销员工号", "营销员姓名", "外呼数据量", "成功受理量", "成功批核金额", "接通通话总时长"};
		List<String> fields = new ArrayList<String>();
		fields.add("center");
		fields.add("group");
		fields.add("userName");
		fields.add("userRealName");
		fields.add("ywType");
		fields.add("whDataNum");
		fields.add("successAcceptNum");
		fields.add("successApproveMoney");
		fields.add("communicateTotleTime");
		ExcelExport<MarketingProcess> ex = new ExcelExport<MarketingProcess>();
		
		try{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ex.exportExcel("BPS-营销过程监控报表", headers, result.getRows(), "yyyy-MM-dd", fields).write(os);
			byte[] content = os.toByteArray();
			InputStream is = new BufferedInputStream(new ByteArrayInputStream(content));
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
		    is.close();
		    response.reset();
		    response.setContentType("application/octet-stream");
		    Date date = new Date();
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		    String dateStr = sdf.format(date);
		    response.addHeader("Content-Disposition",
		            "attachment; filename=" + new String(dateStr.getBytes()) + ".xls");
		    //response.addHeader("Content-Length", "" + file.length());
		    OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		    toClient.write(buffer);
		    toClient.flush();
		    toClient.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
