package com.bps.dal.dao.bps.impl;

import java.util.List;

import com.bps.bean.MoneyObj;
import com.bps.dal.dao.bps.SpecialMarketingReportDao;
import com.bps.dal.dao.ibatis.IbatisBaseDao;
@SuppressWarnings("unchecked")
public class SpecialMarketingReportDaoImpl extends IbatisBaseDao implements SpecialMarketingReportDao {
	
	@Override
	public List<MoneyObj> getWhDistributeMoney1(String sqlId, Object params) {
		return (List<MoneyObj>)getSqlMapClientTemplate().queryForList(sqlId, params);
	}

	@Override
	public List<MoneyObj> getWhDistributeMoney2A(String sqlId, Object params) {
		return (List<MoneyObj>)getSqlMapClientTemplate().queryForList(sqlId, params);
	}

	@Override
	public List<MoneyObj> getWhDistributeMoney2B(String sqlId, Object params) {
		return (List<MoneyObj>)getSqlMapClientTemplate().queryForList(sqlId, params);
	}
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
