package com.bps.dal.dao.hw;


public interface TNottollUniversalDao {
	/**
	 * 根据录音id列表和员工号查询通话总时长
	 */
	public Double getTotalCallsTime(String sqlId, Object params);
	
	public Long getTodayTotalCallsTime(String sqlId, Object params);
}
