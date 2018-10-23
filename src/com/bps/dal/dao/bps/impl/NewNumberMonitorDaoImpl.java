package com.bps.dal.dao.bps.impl;

import com.bps.dal.dao.bps.NewNumberMonitorDao;
import com.bps.dal.dao.ibatis.IbatisBaseDao;

public class NewNumberMonitorDaoImpl extends IbatisBaseDao implements NewNumberMonitorDao {

	@Override
	public Long getDistributeNum(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getOutboundNum(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getNConnectNum(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getSuccessAccept(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getDistributeMoney1(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getDistributeMoney2A(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getDistributeMoney2B(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

}
