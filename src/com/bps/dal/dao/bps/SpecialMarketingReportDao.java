package com.bps.dal.dao.bps;

import java.util.List;

import com.bps.bean.MoneyObj;

public interface SpecialMarketingReportDao {
	
	//查询EPP和账单分期的派发金额
	public List<MoneyObj> getWhDistributeMoney1(String sqlId, Object params);
	//下面两个方法的和为EPPC、备用金和大额EPPC的派发金额
	public List<MoneyObj> getWhDistributeMoney2A(String sqlId, Object params);
	public List<MoneyObj> getWhDistributeMoney2B(String sqlId, Object params);
	
	public Long getConnectNum(String sqlId, Object params);
	
	public Long getSuccessAcceptAmount(String sqlId, Object params);
	
	public Double getSuccessAcceptMoney(String sqlId, Object params);
	
	public Long getSuccessApproveAmountA(String sqlId, Object params);
	public Long getSuccessApproveAmountB(String sqlId, Object params);
	
	public Double getSuccessApproveMoneyA(String sqlId, Object params);
	public Double getSuccessApproveMoneyB(String sqlId, Object params);
}
