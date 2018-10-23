package com.bps.dal.dao.bps.impl;

import java.util.List;

import com.bps.dal.dao.bps.MarketingProcessDao;
import com.bps.dal.dao.ibatis.IbatisBaseDao;

@SuppressWarnings("unchecked")
public class MarketingProcessDaoImpl extends IbatisBaseDao implements MarketingProcessDao {

	@Override
	public Long getTodayWhDateNum(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getSuccessAcceptNum(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getTodaySuccessApproveMoneyA(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getTodaySuccessApproveMoneyB(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getAfterSatisfactionTime(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public List<String> getTodayRecordId(String sqlId, Object params) {
		return (List<String>)getSqlMapClientTemplate().queryForList(sqlId, params);
	}


}
