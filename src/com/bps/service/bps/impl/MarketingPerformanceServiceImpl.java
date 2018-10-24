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
import com.bps.dal.dao.bps.MarketingPerformanceDao;
import com.bps.dal.dao.bps.MarketingProcessDao;
import com.bps.dal.dao.hw.TNottollUniversalDao;
import com.bps.dal.object.PageResult;
import com.bps.dal.object.QueryParams;
import com.bps.dal.object.bps.BpsUserInfo;
import com.bps.dal.object.bps.MarketingPerformance;
import com.bps.service.bps.MarketingPerformanceService;
import com.bps.util.ExcelExport;
import com.bps.util.RedisUtil;

public class MarketingPerformanceServiceImpl implements MarketingPerformanceService{
	@Resource
	private BpsRwHistoryDao bpsRwHistoryDao;
	@Resource
	private MarketingPerformanceDao marketingPerformanceDao;
	@Resource
	private MarketingProcessDao marketingProcessDao;
	@Resource
	private TNottollUniversalDao tNottollUniversalDao;
	@Autowired
	private RedisUtil redisUtil;

	@Override
	public PageResult<MarketingPerformance> getMarketingPerformance(QueryParams params) {
		//中心和小组Map
		Map<String, String> centerMap = (Map<String, String>) redisUtil.getJedis().hgetAll(RedisUtil.BPS_CENTER);;
		Map<String, Map<String, String>> centerGroupMap = (Map<String, Map<String, String>>) RedisUtil.deserialize(redisUtil.getJedis().get(RedisUtil.BPS_GROUP.getBytes()));
		String[] ywTypeArr = {"EPP", "账单分期", "大额EPPC", "EPPC", "EPPC备用金"};
		PageResult<MarketingPerformance> result = new PageResult<MarketingPerformance>();
		List<MarketingPerformance> dataList = new ArrayList<MarketingPerformance>();
		Map<String, Object> userParam = new HashMap<String, Object>();
		Map<String, Object> dataParam = new HashMap<String, Object>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		userParam.put("center", params.getCenter());
		userParam.put("group", params.getGroup());
		userParam.put("userName", params.getUserName());
		userParam.put("beginTime", format.format(new Date())+" 00:00:00");
		userParam.put("endTime", format.format(new Date())+" 23:59:59");
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
				MarketingPerformance obj = new MarketingPerformance(user);
				dataParam.put("ywType", ywType);
				obj.setYwType(ywType);
				
				obj.setCenter(centerText);
				obj.setGroup(groupText);
				
				//如果该用户不存在数据派发,不进行数据库查询直接赋默认值
				if(!dataFlag){
					obj.setApproveMoney(0.0);
					obj.setApproveMoney18(0.0);
					obj.setApproveMoney24(0.0);
					obj.setApproveMoney36(0.0);
					obj.setAutoBindBillNum(0L);
					obj.setAutoBindEPPNum(0L);
					obj.setCommunicateTotleTime(0L);
					obj.setCrossBigEPPCApproveMoney(0.0);
					obj.setCrossBillApproveMoney(0.0);
					obj.setCrossEPPApproveMoney(0.0);
					obj.setCrossEPPCApproveMoney(0.0);
					obj.setMainAcceptNum(0L);
					obj.setMainApproveMoney(0.0);
				}else{
					Double moneyA = 0.00;
					Double moneyB = 0.00;
					if("EPPC".equals(ywType) || "EPPC备用金".equals(ywType)){
						obj.setCrossEPPCApproveMoney(0.00);
					}else{
						dataParam.put("dataType", "EPPC");
						moneyA = marketingPerformanceDao.getCrossApproveMoneyA("getCrossApproveMoneyA", dataParam);
						moneyB = marketingPerformanceDao.getCrossApproveMoneyB("getCrossApproveMoneyB", dataParam);
						obj.setCrossEPPCApproveMoney(moneyA + moneyB);
					}
					if("EPP".equals(ywType)){
						obj.setCrossEPPApproveMoney(0.00);
					}else{
						dataParam.put("dataType", "EPP");
						moneyA = marketingPerformanceDao.getCrossApproveMoneyA("getCrossApproveMoneyA", dataParam);
						moneyB = marketingPerformanceDao.getCrossApproveMoneyB("getCrossApproveMoneyB", dataParam);
						obj.setCrossEPPApproveMoney(moneyA + moneyB);
					}
					if("大额EPPC".equals(ywType)){
						obj.setCrossBigEPPCApproveMoney(0.00);
					}else{
						dataParam.put("dataType", "大额EPPC");
						moneyA = marketingPerformanceDao.getCrossApproveMoneyA("getCrossApproveMoneyA", dataParam);
						moneyB = marketingPerformanceDao.getCrossApproveMoneyB("getCrossApproveMoneyB", dataParam);
						obj.setCrossBigEPPCApproveMoney(moneyA + moneyB);
					}
					if("账单分期".equals(ywType)){
						obj.setCrossBillApproveMoney(0.00);
					}else{
						dataParam.put("dataType", "账单分期");
						moneyA = marketingPerformanceDao.getCrossApproveMoneyA("getCrossApproveMoneyA", dataParam);
						moneyB = marketingPerformanceDao.getCrossApproveMoneyB("getCrossApproveMoneyB", dataParam);
						obj.setCrossBillApproveMoney(moneyA + moneyB);
					}
					
					
					moneyA = marketingPerformanceDao.getApproveMoneyA("getApproveMoneyA", dataParam);
					moneyB = marketingPerformanceDao.getApproveMoneyB("getApproveMoneyB", dataParam);
					obj.setApproveMoney(moneyA + moneyB);
					Long numA = marketingPerformanceDao.getMainAcceptNumA("getMainAcceptNumA", dataParam);
					Long numB = marketingPerformanceDao.getMainAcceptNumB("getMainAcceptNumB", dataParam);
					obj.setMainAcceptNum(numA + numB);
					moneyA = marketingPerformanceDao.getMainApproveMoneyA("getMainApproveMoneyA", dataParam);
					moneyB = marketingPerformanceDao.getMainApproveMoneyB("getMainApproveMoneyB", dataParam);
					obj.setMainApproveMoney(moneyA + moneyB);
					dataParam.put("period", "18期");
					moneyA = marketingPerformanceDao.getApproveMoneyWithPeriodA("getApproveMoneyWithPeriodA", dataParam);
					moneyB = marketingPerformanceDao.getApproveMoneyWithPeriodB("getApproveMoneyWithPeriodB", dataParam);
					obj.setApproveMoney18(moneyA + moneyB);
					dataParam.put("period", "24期");
					moneyA = marketingPerformanceDao.getApproveMoneyWithPeriodA("getApproveMoneyWithPeriodA", dataParam);
					moneyB = marketingPerformanceDao.getApproveMoneyWithPeriodB("getApproveMoneyWithPeriodB", dataParam);
					obj.setApproveMoney24(moneyA + moneyB);
					dataParam.put("period", "36期");
					moneyA = marketingPerformanceDao.getApproveMoneyWithPeriodA("getApproveMoneyWithPeriodA", dataParam);
					moneyB = marketingPerformanceDao.getApproveMoneyWithPeriodB("getApproveMoneyWithPeriodB", dataParam);
					obj.setApproveMoney36(moneyA + moneyB);
					dataParam.put("dataType", "EPP");
					obj.setAutoBindEPPNum(marketingPerformanceDao.getAutoBindNum("getAutoBindNum", dataParam));
					dataParam.put("dataType", "账单分期");
					obj.setAutoBindBillNum(marketingPerformanceDao.getAutoBindNum("getAutoBindNum", dataParam));
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
	public void exportMarketingPerformanceExcel(QueryParams params,
			HttpServletResponse response) {
		PageResult<MarketingPerformance> result = getMarketingPerformance(params);
		
		String[] headers = { "所属中心", "数据业务类型","组别", "营销员工号", "营销员姓名", "批核收入", "主营成功受理量", "主营成功批核金额", "18期批核金额", "24期批核金额", "36期批核金额", "交叉EPP批核金额", "交叉账单批核金额", "交叉EPPC批核金额", "交叉大额EPPC批核金额", "自动绑定EPP量", "自动绑定账单分期量", "接通通话总时长"};
		List<String> fields = new ArrayList<String>();
		fields.add("center");
		fields.add("group");
		fields.add("userName");
		fields.add("userRealName");
		fields.add("ywType");
		fields.add("approveMoney");
		fields.add("mainAcceptNum");
		fields.add("mainApproveMoney");
		fields.add("approveMoney18");
		fields.add("approveMoney24");
		fields.add("approveMoney36");
		fields.add("crossEPPApproveMoney");
		fields.add("crossBillApproveMoney");
		fields.add("crossEPPCApproveMoney");
		fields.add("crossBigEPPCApproveMoney");
		fields.add("autoBindEPPNum");
		fields.add("autoBindBillNum");
		fields.add("communicateTotleTime");
		ExcelExport<MarketingPerformance> ex = new ExcelExport<MarketingPerformance>();
		
		try{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ex.exportExcel("BPS-营销业绩监控", headers, result.getRows(), "yyyy-MM-dd", fields).write(os);
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
