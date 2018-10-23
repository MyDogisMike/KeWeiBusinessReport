package com.bps.dal.dao.bps;

import java.util.List;

public interface MarketingProcessDao {
	public Long getTodayWhDateNum(String sqlId, Object params);
	
	public Long getSuccessAcceptNum(String sqlId, Object params);
	
	public Double getTodaySuccessApproveMoneyA(String sqlId, Object params);
	public Double getTodaySuccessApproveMoneyB(String sqlId, Object params);
	
	public List<String> getTodayRecordId(String sqlId, Object params);
	
	public Long getAfterSatisfactionTime(String sqlId, Object params);
}
