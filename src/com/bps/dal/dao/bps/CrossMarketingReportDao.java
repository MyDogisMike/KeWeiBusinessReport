package com.bps.dal.dao.bps;


public interface CrossMarketingReportDao {
	
	//查询EPP和账单分期的派发金额
	public Double getWhDistributeMoney1(String sqlId, Object params);
	//下面两个方法的和为EPPC、备用金和大额EPPC的派发金额
	public Double getWhDistributeMoney2A(String sqlId, Object params);
	public Double getWhDistributeMoney2B(String sqlId, Object params);
	
	public Long getSuccessApproveDataNumA(String sqlId, Object params);
	public Long getSuccessApproveDataNumB(String sqlId, Object params);
	
	public Double getSuccessApproveDataMoneyA(String sqlId, Object params);
	public Double getSuccessApproveDataMoneyB(String sqlId, Object params);
	
	public Long getCrossEPPBinding(String sqlId, Object params);
	
	public Long getCrossMGLWill(String sqlId, Object params);
	
	public Long getCrossInsuranceWill(String sqlId, Object params);
	
	public Long getCrossBillBinding(String sqlId, Object params);
	
}
