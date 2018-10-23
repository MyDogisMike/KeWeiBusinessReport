package com.bps.dal.dao.bps;

public interface MarketingPerformanceDao {
	public Double getApproveMoneyA(String sqlId, Object params);
	public Double getApproveMoneyB(String sqlId, Object params);
	
	public Long getMainAcceptNumA(String sqlId, Object params);
	public Long getMainAcceptNumB(String sqlId, Object params);
	
	public Double getMainApproveMoneyA(String sqlId, Object params);
	public Double getMainApproveMoneyB(String sqlId, Object params);
	
	public Double getApproveMoneyWithPeriodA(String sqlId, Object params);
	public Double getApproveMoneyWithPeriodB(String sqlId, Object params);
	
	public Double getCrossApproveMoneyA(String sqlId, Object params);
	public Double getCrossApproveMoneyB(String sqlId, Object params);
	
	public Long getAutoBindNum(String sqlId, Object params);
}
