package com.bps.dal.dao.bps;

public interface SpecialMarketingReportDao {
	public Long getConnectNum(String sqlId, Object params);
	
	public Long getSuccessAcceptAmount(String sqlId, Object params);
	
	public Double getSuccessAcceptMoney(String sqlId, Object params);
	
	public Long getSuccessApproveAmountA(String sqlId, Object params);
	public Long getSuccessApproveAmountB(String sqlId, Object params);
	
	public Double getSuccessApproveMoneyA(String sqlId, Object params);
	public Double getSuccessApproveMoneyB(String sqlId, Object params);
}
