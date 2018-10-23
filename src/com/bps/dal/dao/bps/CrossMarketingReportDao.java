package com.bps.dal.dao.bps;

import java.util.Map;

public interface CrossMarketingReportDao {
	public Map<String, String> getEPPDistributeMoney(String sqlId, Object params);
	
	public Map<String, String> getBillDistributeMoney(String sqlId, Object params);
	
	public Map<String, String> getEPPCGeneralDistributeMoney(String sqlId, Object params);
	
	public Map<String, String> getEPPCCreditDistributeMoney(String sqlId, Object params);
	
	public Map<String, String> getImprestGeneralDistributeMoney(String sqlId, Object params);
	
	public Map<String, String> getImprestCreditDistributeMoney(String sqlId, Object params);
	
	public Map<String, String> getBigEPPCGeneralDistributeMoney(String sqlId, Object params);
	
	public Map<String, String> getBigEPPCCreditDistributeMoney(String sqlId, Object params);
	
	public Long getSuccessApproveDataNumA(String sqlId, Object params);
	public Long getSuccessApproveDataNumB(String sqlId, Object params);
	
	public Double getSuccessApproveDataMoneyA(String sqlId, Object params);
	public Double getSuccessApproveDataMoneyB(String sqlId, Object params);
	
	public Long getCrossEPPBinding(String sqlId, Object params);
	
	public Long getCrossMGLWill(String sqlId, Object params);
	
	public Long getCrossInsuranceWill(String sqlId, Object params);
	
	public Long getCrossBillBinding(String sqlId, Object params);
	
}
