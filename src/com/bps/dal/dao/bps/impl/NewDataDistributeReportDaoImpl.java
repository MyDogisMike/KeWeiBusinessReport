package com.bps.dal.dao.bps.impl;

import java.util.List;

import com.bps.dal.dao.bps.NewDataDistributeReportDao;
import com.bps.dal.dao.ibatis.IbatisBaseDao;

@SuppressWarnings("unchecked")
public class NewDataDistributeReportDaoImpl extends IbatisBaseDao implements NewDataDistributeReportDao {

	@Override
	public Long getDistributeAmount(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public List<String> getDataType(String sqlId, Object params) {
		return (List<String>) getSqlMapClientTemplate().queryForList(sqlId, params);
	}

	@Override
	public Double getNewDistributeMoney1(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getNewDistributeMoney2A(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getNewDistributeMoney2B(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getWhNum(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getNewConnectNum(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getNewSuccessAcceptAmount(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getNewSuccessAcceptMoney(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public List<String> getSuccessApproveDataA(String sqlId, Object params) {
		return (List<String>) getSqlMapClientTemplate().queryForList(sqlId, params);
	}

	@Override
	public List<String> getSuccessApproveDataB(String sqlId, Object params) {
		return (List<String>) getSqlMapClientTemplate().queryForList(sqlId, params);
	}

	@Override
	public Double getNewSuccessApproveMoneyA(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Double getNewSuccessApproveMoneyB(String sqlId, Object params) {
		return (Double) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

	@Override
	public Long getWrongDataNum(String sqlId, Object params) {
		return (Long) getSqlMapClientTemplate().queryForObject(sqlId, params);
	}

}
