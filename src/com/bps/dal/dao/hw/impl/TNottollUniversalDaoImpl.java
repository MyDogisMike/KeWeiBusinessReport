package com.bps.dal.dao.hw.impl;

import com.bps.dal.dao.hw.TNottollUniversalDao;
import com.bps.dal.dao.ibatis.IbatisBaseDao;


public class TNottollUniversalDaoImpl extends IbatisBaseDao implements TNottollUniversalDao{

	@Override
	public Double getTotalCallsTime(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getTodayTotalCallsTime(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

}
