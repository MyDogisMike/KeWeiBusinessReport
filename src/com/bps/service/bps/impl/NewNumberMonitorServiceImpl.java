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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.bps.dal.dao.bps.BpsRwHistoryDao;
import com.bps.dal.dao.bps.NewNumberMonitorDao;
import com.bps.dal.object.PageResult;
import com.bps.dal.object.QueryParams;
import com.bps.dal.object.bps.BpsUserInfo;
import com.bps.dal.object.bps.NewNumberMonitor;
import com.bps.service.bps.NewNumberMonitorService;
import com.bps.util.ExcelExport;
import com.bps.util.RedisUtil;

public class NewNumberMonitorServiceImpl implements NewNumberMonitorService{
	@Resource
	private BpsRwHistoryDao bpsRwHistoryDaoRealTime;
	@Resource
	private NewNumberMonitorDao newNumberMonitorDao;
	@Autowired
	private RedisUtil redisUtil;

	@Override
	public PageResult<NewNumberMonitor> getNewNumberMonitor(QueryParams params) {
		//中心和小组Map
		Map<String, String> centerMap = (Map<String, String>) redisUtil.getJedis().hgetAll(RedisUtil.BPS_CENTER);;
		Map<String, Map<String, String>> centerGroupMap = (Map<String, Map<String, String>>) RedisUtil.deserialize(redisUtil.getJedis().get(RedisUtil.BPS_GROUP.getBytes()));
		if(centerMap == null || centerGroupMap == null){
			centerMap = bpsRwHistoryDaoRealTime.getAllCenter("getAllCenter");
			
			centerGroupMap = new HashMap<String, Map<String, String>>();
			for(String centerKey : centerMap.keySet()){
				Map<String, String> groupMap = bpsRwHistoryDaoRealTime.getGroupByCenterId("getGroupByCenterId", centerKey);
				centerGroupMap.put(centerKey, groupMap);
			}
			redisUtil.getJedis().hmset(RedisUtil.BPS_CENTER, centerMap);
			redisUtil.getJedis().set(RedisUtil.BPS_GROUP.getBytes(), RedisUtil.serialize(centerGroupMap));
		}
		
		String[] ywTypeArr = {"EPP", "账单分期", "大额EPPC", "EPPC", "备用金"};
		PageResult<NewNumberMonitor> result = new PageResult<NewNumberMonitor>();
		List<NewNumberMonitor> dataList = new ArrayList<NewNumberMonitor>();

		Map<String, Object> userParam = new HashMap<String, Object>();
		Map<String, String> dataParam = new HashMap<String, String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		userParam.put("center", params.getCenter());
		userParam.put("group", params.getGroup());
		userParam.put("userName", params.getUserName());
		userParam.put("beginTime", format.format(new Date())+" 00:00:00");
		userParam.put("endTime", format.format(new Date())+" 23:59:59");
		userParam.put("rows", params.getRows());
		userParam.put("skipRow", params.getSkipRow());
		//获取有数据派发的用户名List
		List<String> haveDataUser = bpsRwHistoryDaoRealTime.getHaveDataUser("getHaveDataUser", userParam);
		List<BpsUserInfo> userList = bpsRwHistoryDaoRealTime.getUserInfoByTime("getUserInfoByTime", userParam);
		Long count = bpsRwHistoryDaoRealTime.getCountUserInfoByTime("getCountUserInfoByTime", userParam);
		if(count == null) count = 0L;
		for (BpsUserInfo user : userList){
			String userName = user.getUserName();
			dataParam.put("userName", userName);
			
			//先从Redis中获取中心和组别信息，如果没有则从数据库中获取再保存到Redis中
			String centerId = user.getCenterId();
			String groupId = user.getGroupId();
			
			String centerText = centerMap.get(centerId);
			if(centerText == null){
				centerText = bpsRwHistoryDaoRealTime.getTextById("getTextById", centerId);
				centerMap.put(centerId, centerText);
				Map<String, String> groupMap = new HashMap<String, String>();
				centerGroupMap.put(centerId, groupMap);
			}
			String groupText = centerGroupMap.get(centerId).get(groupId);
			if(groupText == null){
				groupText = bpsRwHistoryDaoRealTime.getTextById("getTextById", groupId);
				Map<String, String> groupMap = centerGroupMap.get(centerId);
				groupMap.put(groupId, groupText);
				centerGroupMap.put(centerId, groupMap);
			}
			//判断该用户是否在haveDataUser中，如果不在则表示没有数据，则所有数据为0；
			boolean dataFlag = false;
			for(String dataUser : haveDataUser) {
				if(dataUser.equalsIgnoreCase(userName)) {
					dataFlag = true;
					break;
				}
			}
			for (String ywType : ywTypeArr){
				if(!"".equals(params.getYwType()) && !params.getYwType().equals(ywType)){
					continue;
				}
				NewNumberMonitor obj = new NewNumberMonitor(user);
				dataParam.put("ywType", ywType);
				obj.setYwType(ywType);
				
				obj.setCenter(centerText);
				obj.setGroup(groupText);
				//如果该用户不存在数据派发,不进行数据库查询直接赋默认值
				if(!dataFlag){
					obj.setConnectNum(0L);
					obj.setDistributeMoney(0.0);
					obj.setDistributeNum(0L);
					obj.setOutboundNum(0L);
					obj.setPercentageComplete("0");
					obj.setSuccessAccept(0L);
				}else{
					obj.setDistributeNum(newNumberMonitorDao.getDistributeNum("getDistributeNum", dataParam));
					if("EPP".equals(ywType) || "账单分期".equals(ywType)){
						obj.setDistributeMoney(newNumberMonitorDao.getDistributeMoney1("getDistributeMoney1", dataParam));
					}else{
						double moneyA = newNumberMonitorDao.getDistributeMoney2A("getDistributeMoney2A", dataParam);
						double moneyB = newNumberMonitorDao.getDistributeMoney2B("getDistributeMoney2B", dataParam);
						obj.setDistributeMoney(moneyA + moneyB);
						
					}
					obj.setOutboundNum(newNumberMonitorDao.getOutboundNum("getOutboundNum", dataParam));
					if(obj.getDistributeNum()==0 || obj.getOutboundNum()==0) {
						obj.setPercentageComplete("0");
					}else{
						double percent = Double.parseDouble(obj.getOutboundNum()+"") / obj.getDistributeNum() * 100;
						percent = (double)Math.round(percent*100)/100;
						obj.setPercentageComplete(percent + "%");
					}
					obj.setConnectNum(newNumberMonitorDao.getNConnectNum("getNConnectNum", dataParam));
					obj.setSuccessAccept(newNumberMonitorDao.getSuccessAccept("getSuccessAccept", dataParam));
				}
				
				dataList.add(obj);
			}
			
		}
		
		result.setRows(dataList);
		result.setTotal(count);
		return result;
	}
	
	@Override
	public void exportNewNumberMonitorExcel(QueryParams params, HttpServletResponse response) {
		PageResult<NewNumberMonitor> result = getNewNumberMonitor(params);
		
		String[] headers = { "所属中心", "数据业务类型","组别", "营销员工号", "营销员姓名", "新数派发量", "新数派发金额", "新数外呼量", "新数完成率", "新数接通量", "新数成功受理量"};
		List<String> fields = new ArrayList<String>();
		fields.add("center");
		fields.add("group");
		fields.add("userName");
		fields.add("userRealName");
		fields.add("ywType");
		fields.add("distributeNum");
		fields.add("distributeMoney");
		fields.add("outboundNum");
		fields.add("percentageComplete");
		fields.add("connectNum");
		fields.add("successAccept");
		ExcelExport<NewNumberMonitor> ex = new ExcelExport<NewNumberMonitor>();
		
		try{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			HSSFWorkbook workbook = ex.exportExcel("BPS-新数监控报表", headers, result.getRows(), fields);
			workbook.write(os);
			workbook.close();
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
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
