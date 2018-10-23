package com.bps.dal.dao.bps.impl;

import java.util.Map;

import com.bps.dal.dao.bps.CrossMarketingReportDao;
import com.bps.dal.dao.ibatis.IbatisBaseDao;

@SuppressWarnings("unchecked")
public class CrossMarketingReportDaoImpl extends IbatisBaseDao implements CrossMarketingReportDao{

	@Override
	public Map<String, String> getEPPDistributeMoney(String sqlId, Object params) {
		return getSqlMapClientTemplate().queryForMap(sqlId, params, "drKey", "money");
	}

	@Override
	public Map<String, String> getBillDistributeMoney(String sqlId,Object params) {
		return getSqlMapClientTemplate().queryForMap(sqlId, params, "drKey", "money");
	}

	@Override
	public Map<String, String> getBigEPPCCreditDistributeMoney(String sqlId,
			Object params) {
		return getSqlMapClientTemplate().queryForMap(sqlId, params, "drKey", "money");
	}

	@Override
	public Map<String, String> getBigEPPCGeneralDistributeMoney(String sqlId,
			Object params) {
		return getSqlMapClientTemplate().queryForMap(sqlId, params, "drKey", "money");
	}

	@Override
	public Map<String, String> getEPPCCreditDistributeMoney(String sqlId,
			Object params) {
		return getSqlMapClientTemplate().queryForMap(sqlId, params, "drKey", "money");
	}

	@Override
	public Map<String, String> getEPPCGeneralDistributeMoney(String sqlId,
			Object params) {
		return getSqlMapClientTemplate().queryForMap(sqlId, params, "drKey", "money");
	}

	@Override
	public Map<String, String> getImprestCreditDistributeMoney(String sqlId,
			Object params) {
		return getSqlMapClientTemplate().queryForMap(sqlId, params, "drKey", "money");
	}

	@Override
	public Map<String, String> getImprestGeneralDistributeMoney(String sqlId,
			Object params) {
		return getSqlMapClientTemplate().queryForMap(sqlId, params, "drKey", "money");
	}

	@Override
	public Long getSuccessApproveDataNumA(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}
	
	@Override
	public Long getSuccessApproveDataNumB(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getSuccessApproveDataMoneyA(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getSuccessApproveDataMoneyB(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getCrossEPPBinding(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getCrossMGLWill(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getCrossInsuranceWill(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getCrossBillBinding(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

}
