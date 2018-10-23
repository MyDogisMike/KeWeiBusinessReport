package com.bps.dal.dao.bps;

import java.util.List;

public interface NewDataDistributeReportDao {
	public List<String> getDataType(String sqlId, Object params);
	
	public Long getDistributeAmount(String sqlId, Object params);
	
	//查询epp和账单分期的派发金额
	public Double getNewDistributeMoney1(String sqlId, Object params);
	
	//查询EPPC、备用金和大额EPPC的派发金额
	public Double getNewDistributeMoney2A(String sqlId, Object params);
	public Double getNewDistributeMoney2B(String sqlId, Object params);
	
	public Long getWhNum(String sqlId, Object params);
	
	public Long getNewConnectNum(String sqlId, Object params);
	
	public Long getNewSuccessAcceptAmount(String sqlId, Object params);
	
	public Double getNewSuccessAcceptMoney(String sqlId, Object params);
	
	public List<String> getSuccessApproveDataA(String sqlId, Object params);
	public List<String> getSuccessApproveDataB(String sqlId, Object params);
	
	public Double getNewSuccessApproveMoneyA(String sqlId, Object params);
	public Double getNewSuccessApproveMoneyB(String sqlId, Object params);
	
	public Long getWrongDataNum(String sqlId, Object params);
}
