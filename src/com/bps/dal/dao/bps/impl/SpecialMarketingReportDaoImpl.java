package com.bps.dal.dao.bps.impl;

import com.bps.dal.dao.bps.SpecialMarketingReportDao;
import com.bps.dal.dao.ibatis.IbatisBaseDao;

public class SpecialMarketingReportDaoImpl extends IbatisBaseDao implements SpecialMarketingReportDao {

	@Override
	public Long getConnectNum(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getSuccessAcceptAmount(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getSuccessAcceptMoney(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getSuccessApproveAmountA(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getSuccessApproveAmountB(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getSuccessApproveMoneyA(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getSuccessApproveMoneyB(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

}
