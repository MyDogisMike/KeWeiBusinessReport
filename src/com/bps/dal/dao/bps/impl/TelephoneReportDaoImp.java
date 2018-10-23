package com.bps.dal.dao.bps.impl;

import java.util.List;

import com.bps.dal.dao.bps.TelephoneReportDao;
import com.bps.dal.dao.ibatis.IbatisBaseDao;

@SuppressWarnings("unchecked")
public class TelephoneReportDaoImp extends IbatisBaseDao  implements TelephoneReportDao {


	@Override
	public Long getAcceptDateNum(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getTotalCalls(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}
	
	@Override
	public List<String> getRecordId(String sqlId, Object params) {
		return (List<String>)getSqlMapClientTemplate().queryForList(sqlId, params);
	}

	@Override
	public Double getSatisfactionTime(String sqlId, Object params) {
		return (Double)getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public List<String> getDataList(String sqlId, Object params) {
		return (List<String>)getSqlMapClientTemplate().queryForList(sqlId, params);
	}

	@Override
	public List<String> getRecordIdByWorkOrder(String sqlId, Object params) {
		return (List<String>)getSqlMapClientTemplate().queryForList(sqlId, params);
	}

	@Override
	public Double getSatisfactionTimeByWorkOrder(String sqlId, Object params) {
		return (Double)getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

}
