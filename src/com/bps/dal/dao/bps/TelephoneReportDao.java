package com.bps.dal.dao.bps;

import java.util.List;

public interface TelephoneReportDao {
	
	/**
	 * 查询指定时间内某员工的成功受理数据量
	 */
	public Long getAcceptDateNum(String sqlId, Object params);
	
	/**
	 * 查询指定时间内某员工的外呼总次数
	 */
	public Long getTotalCalls(String sqlId, Object params);
	
	/**
	 * 查询指定时间内某员工的外呼录音Id
	 */
	public List<String> getRecordId(String sqlId, Object params);
	
	/**
	 * 查询指定时间内某员工的话后满意度时长
	 */
	public Double getSatisfactionTime(String sqlId, Object params);
	
	/**
	 * 查询指定时间内某员工的数据名单
	 */
	public List<String> getDataList(String sqlId, Object params);
	
	/**
	 * 根据工单查询外呼录音Id
	 */
	public List<String> getRecordIdByWorkOrder(String sqlId, Object params);
	
	/**
	 * 根据工单查询满意度时长
	 */
	public Double getSatisfactionTimeByWorkOrder(String sqlId, Object params);
	
}
