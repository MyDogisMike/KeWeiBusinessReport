package com.bps.dal.dao.bps.impl;

import com.bps.dal.dao.bps.CrossMarketingReportDao;
import com.bps.dal.dao.ibatis.IbatisBaseDao;

public class CrossMarketingReportDaoImpl extends IbatisBaseDao implements CrossMarketingReportDao{
	
	@Override
	public Double getCrossWhDistributeMoney1(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getCrossWhDistributeMoney2A(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getCrossWhDistributeMoney2B(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
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
