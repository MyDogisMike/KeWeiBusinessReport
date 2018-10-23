package com.bps.dal.dao.bps;

public interface NewNumberMonitorDao {
	public Long getDistributeNum(String sqlId, Object params);
	
	public Double getDistributeMoney1(String sqlId, Object params);
	
	public Double getDistributeMoney2A(String sqlId, Object params);
	
	public Double getDistributeMoney2B(String sqlId, Object params);
	
	public Long getOutboundNum(String sqlId, Object params);
	
	public Long getNConnectNum(String sqlId, Object params);
	
	public Long getSuccessAccept(String sqlId, Object params);
	
}
