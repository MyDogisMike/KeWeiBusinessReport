package com.bps.dal.dao.bps.impl;

import com.bps.dal.dao.bps.MarketingPerformanceDao;
import com.bps.dal.dao.ibatis.IbatisBaseDao;

public class MarketingPerformanceDaoImpl extends IbatisBaseDao implements MarketingPerformanceDao {

	@Override
	public Double getApproveMoneyA(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getApproveMoneyB(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getMainAcceptNumA(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getMainAcceptNumB(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getMainApproveMoneyA(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getMainApproveMoneyB(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getApproveMoneyWithPeriodA(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getApproveMoneyWithPeriodB(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getCrossApproveMoneyA(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getCrossApproveMoneyB(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getAutoBindNum(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

}
